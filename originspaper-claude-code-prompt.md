# OriginsPaper — Prompt para Claude Code

> Cole este documento inteiro no Claude Code ao iniciar uma sessão nova em um diretório vazio.
> Ele contém tudo que o assistente precisa para criar o repositório do zero, sem perguntas.

---

## Contexto

Crie um plugin Paper para Minecraft 26.1.2 chamado **OriginsPaper** que reimplementa o sistema de Origins (raças/classes com habilidades passivas) usando **exclusivamente a API pública do Paper/Bukkit** — sem NMS, sem mixins, sem shadow jars, sem dependências externas além do Paper API.

O plugin deve ser compilável com `./gradlew build` e deployável colocando o JAR em `plugins/`.

---

## Stack técnico

- **Linguagem**: Java 25
- **Build**: Gradle com Kotlin DSL (`build.gradle.kts`)
- **API alvo**: `io.papermc.paper:paper-api:26.1.2.build.+` (repositório: `https://repo.papermc.io/repository/maven-public/`)
- **Java toolchain**: `JavaLanguageVersion.of(25)`
- **Sem dependências externas** além do Paper API

### `build.gradle.kts` mínimo correto

```kotlin
plugins {
    java
}

group = "dev.originspaper"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
```

### `plugin.yml` mínimo correto

```yaml
name: OriginsPaper
version: "1.0.0"
main: dev.originspaper.OriginsPaper
api-version: "26.1"
description: Origins system for Paper 26.1.2
authors: [OriginsPaper]
commands:
  origin:
    description: Manage your origin
    usage: /origin [set|reset|info] [player] [origin]
```

---

## Estrutura de diretórios esperada

```
originspaper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── src/main/
│   ├── java/dev/originspaper/
│   │   ├── OriginsPaper.java
│   │   ├── api/
│   │   │   ├── Origin.java
│   │   │   ├── PowerType.java
│   │   │   └── ActivePowerType.java
│   │   ├── registry/
│   │   │   ├── OriginRegistry.java
│   │   │   └── PlayerOriginData.java
│   │   ├── listener/
│   │   │   ├── OriginSelectionListener.java
│   │   │   ├── ActiveSkillListener.java
│   │   │   └── PowerEventListener.java
│   │   ├── gui/
│   │   │   ├── OriginSelectionGUI.java     ← tela de lista de origins
│   │   │   └── OriginDetailGUI.java        ← tela de detalhe de uma origin
│   │   ├── power/
│   │   │   ├── shared/
│   │   │   │   ├── NoFallDamagePower.java
│   │   │   │   ├── FireImmunityPower.java
│   │   │   │   ├── WaterDamagePower.java
│   │   │   │   ├── CarnivoreDietPower.java
│   │   │   │   ├── ElytraFlightPower.java
│   │   │   │   ├── NightVisionPower.java
│   │   │   │   ├── SilentStepsPower.java
│   │   │   │   ├── LightArmorOnlyPower.java
│   │   │   │   └── AttributeModifierPower.java
│   │   │   └── origins/
│   │   │       ├── merling/
│   │   │       ├── arachnid/
│   │   │       ├── blazeborn/
│   │   │       ├── feline/
│   │   │       ├── elytrian/
│   │   │       ├── enderian/
│   │   │       ├── shulk/
│   │   │       ├── dragon/
│   │   │       ├── wolf/
│   │   │       ├── owl/
│   │   │       ├── gryphon/
│   │   │       ├── goat/
│   │   │       ├── bear/
│   │   │       ├── rabbit/
│   │   │       └── fox/
│   └── resources/
│       ├── plugin.yml
│       └── config.yml
```

---

## Arquitetura central

### PowerType (interface base)

```java
public interface PowerType {
    String getId();
    void onApply(Player player);
    void onRemove(Player player);
    default void onDamage(EntityDamageEvent e) {}
    default void onDamageByEntity(EntityDamageByEntityEvent e) {}
    default void onMove(PlayerMoveEvent e) {}
    default void onTick(Player player) {}        // chamado a cada 20 ticks
    default void onFoodChange(FoodLevelChangeEvent e) {}
    default void onItemConsume(PlayerItemConsumeEvent e) {}
    default void onBlockBreak(BlockBreakEvent e) {}
    default void onBlockDropItem(BlockDropItemEvent e) {}
    default void onArmorChange(PlayerArmorChangeEvent e) {}
    default void onInteract(PlayerInteractEvent e) {}
    default void onEntityTarget(EntityTargetEvent e) {}
    default void onPotionEffect(EntityPotionEffectEvent e) {}
    default void onBedEnter(PlayerBedEnterEvent e) {}
}
```

### ActivePowerType (skill ativável)

```java
public interface ActivePowerType extends PowerType {
    void onActivate(Player player);
    long getCooldownTicks();   // retornar 0 para sem cooldown
}
```

### Detecção de skill ativa

Input: **agachar (shift) + trocar de mão (F)** → `PlayerSwapHandItemsEvent` com `player.isSneaking() == true`. Cancelar o evento para não trocar os itens. Cada origin tem no máximo 1 `ActivePowerType`.

### PlayerOriginData

```java
public class PlayerOriginData {
    private final UUID playerId;
    private Origin origin;
    private final List<PowerType> activePowers;
    private final Map<String, Long> cooldowns; // powerId → System.currentTimeMillis() da última ativação
}
```

### Persistência

Salvar em `plugins/OriginsPaper/data/<uuid>.yml`. Um arquivo por jogador, formato simples:
```yaml
origin: "merling"
```

---

## GUI de seleção — fluxo de duas telas

### Tela 1 — Lista de origins (`OriginSelectionGUI`)

**Quando abre:** automaticamente ao `PlayerJoinEvent` se o jogador não tem origin salva (primeira vez no servidor). Também via `/origin` sem permissão admin (só se não tem origin ainda).

**Layout:** inventário de 54 slots com título `"§6Escolha sua Origin"`.

- Cada origin ocupa 1 slot com um **PLAYER_HEAD customizado** como ícone (ver seção de cabeças abaixo).
- Nome do item: nome da origin em cor dourada (`§6`).
- Lore do item: 2-3 linhas resumidas dos poderes principais em cinza (`§7`), mais `§e▶ Clique para ver detalhes` na última linha.
- Origins distribuídas em grid começando no slot 10, pulando bordas (slots 0-8, 9, 17, 18, 26, 27, 35, 36, 44, 45-53 são preenchidos com vidro preto sem nome).
- Human sempre no slot 10 (primeiro slot do grid interno).
- **Clicar em qualquer origin abre a Tela 2** (detalhe) para aquela origin específica.
- O inventário não pode ser fechado pelo jogador enquanto não tiver origin (cancelar `InventoryCloseEvent` e reabrir com delay de 1 tick via scheduler, se ainda sem origin).

**Posicionamento das 16 origins no grid** (slots internos, excluindo bordas):
```
Slots válidos: 10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43
```
Distribuir as 16 origins nos primeiros 16 desses slots, preencher o resto com vidro preto.

---

### Tela 2 — Detalhe de uma origin (`OriginDetailGUI`)

**Layout:** inventário de 54 slots com título `"§6" + nomeOrigin`.

**Slot 13 (centro-topo):** PLAYER_HEAD da origin, mesmo ícone da tela 1, nome colorido.

**Slots 19-25 (linha do meio):** até 7 itens de poder, cada um representando um power da origin:
- Item: papel (`PAPER`) ou item temático se aplicável
- Nome: nome do poder em amarelo (`§e`)
- Lore: descrição do poder em cinza (`§7`), em linhas de no máximo 40 caracteres

**Slot 31 (centro):** Botão **Escolher** — LIME_WOOL com nome `§a§lEscolher esta Origin` e lore `§7Confirma sua escolha. Não poderá trocar depois.`

**Slot 29 (esquerda do botão):** Botão **Voltar** — ARROW com nome `§c§l← Voltar` — volta para a Tela 1

**Slots restantes:** preenchidos com vidro preto sem nome.

**Ao clicar em Escolher:**
1. Fechar inventário
2. Chamar `onApply()` de todos os powers da origin
3. Salvar `<uuid>.yml`
4. Enviar mensagem de confirmação: `§6Você escolheu a origin §f<nome>§6!`

---

### Cabeças customizadas (SkullMeta via PlayerProfile)

Para criar uma cabeça com skin customizada, usar `PlayerProfile` + `PlayerTextures`:

```java
public static ItemStack createSkull(String textureUrl, String displayName, List<String> lore) {
    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
    skull.editMeta(SkullMeta.class, meta -> {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(textureUrl));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        profile.setTextures(textures);
        meta.setPlayerProfile(profile);
        meta.displayName(Component.text(displayName));
        meta.lore(lore.stream().map(Component::text).toList());
    });
    return skull;
}
```

**Texturas de cada origin** — usar estas URLs do Minecraft Heads (textures.minecraft.net):

| Origin | Texture URL |
|---|---|
| Human | `http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac` (Steve) |
| Merling | `http://textures.minecraft.net/texture/2cd5e1c5c27a7c4fc08a7f27f37c5b53a7f59ff7c9d674fcbf80dbef0c0d8f4` (cabeça de lula/guardião) |
| Arachnid | `http://textures.minecraft.net/texture/bcbcafc5f4a544c0e75058a1b4b66dc7bba29935e84a216a95f3e8f4c49e3b4a` (aranha) |
| Blazeborn | `http://textures.minecraft.net/texture/5c4b76c9c89e8e7f4f7a7a60e27c47f17d41c3f5be4c2c3a8e3c24a3c5c3a2e` (blaze) |
| Feline | `http://textures.minecraft.net/texture/7d5f2376b9fa06c93f32eb48dd5e54a2c4d2e94e85ca0e81dd17f19f07b0b27` (ocelote) |
| Elytrian | `http://textures.minecraft.net/texture/4e4b9c0b1c7e9a3f6b9d2e5c8a1f4b7e0d3c6f9a2e5b8c1d4f7a0e3b6c9d2e5` (fantoche/elytra) |
| Enderian | `http://textures.minecraft.net/texture/20e73625f5304f93e8e7a7d15a09cd87b4b5e4d3c2b1a09f8e7d6c5b4a39281` (enderman) |
| Shulk | `http://textures.minecraft.net/texture/3e4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b` (shulker) |
| Dragon | `http://textures.minecraft.net/texture/a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b` (ender dragon) |
| Wolf | `http://textures.minecraft.net/texture/f1e2d3c4b5a6978869706152434a4b4c4d4e4f50515253545556575859605a5b` (lobo) |
| Owl | `http://textures.minecraft.net/texture/c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d` (coruja — usar papagaio cinza como fallback) |
| Gryphon | `http://textures.minecraft.net/texture/d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e` (águia/papagaio dourado) |
| Goat | `http://textures.minecraft.net/texture/e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f` (cabra) |
| Bear | `http://textures.minecraft.net/texture/f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a` (urso polar) |
| Rabbit | `http://textures.minecraft.net/texture/a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b` (coelho) |
| Fox | `http://textures.minecraft.net/texture/b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c` (raposa) |

> **Importante:** As URLs de textura acima para origins não-humanas são placeholders estruturalmente válidos. O Claude Code deve, ao implementar, buscar texturas reais de https://minecraft-heads.com para cada mob correspondente (site público, sem autenticação). Se não conseguir buscar, usar o PLAYER_HEAD com `setOwningPlayer` de um jogador offline com o nome do mob como fallback, ou simplesmente usar diferentes materiais de item (`SPIDER_EYE` para Arachnid, `BLAZE_ROD` para Blazeborn, etc.) como fallback seguro.

---

## As 16 Origins

### Human
Sem poderes. Origin neutra.

---

### Merling

| Power | Implementação |
|---|---|
| Gills (respira em água, sufoca em terra) | Tick: se `!player.isInWater()`, aplicar 1 de dano a cada 20t; se em água, cancelar sufocamento padrão via `EntityDamageEvent` cause SUFFOCATION |
| Aqua Affinity (quebra blocos em água normalmente) | `BlockBreakEvent`: se player está em água, não aplicar penalidade (Paper não tem setter direto — workaround: dar Mining Fatigue 0 e remover a penalidade via `player.addPotionEffect` de Haste enquanto submerso) |
| Wet Eyes (visão em água) | Tick: se submerso (`player.isUnderWater()`), aplicar Night Vision e Dolphins Grace; se não, remover |
| Fins (nado rápido) | `AttributeModifier` em `GENERIC_MOVEMENT_SPEED` +0.4, ativo apenas quando submerso (checar no tick) |
| Like Water (não afunda) | Tick: se submerso e não sneaking e velocity Y < 0, setar velocity Y = 0 |

---

### Arachnid

| Power | Implementação |
|---|---|
| Climbing (escalar paredes) | `PlayerMoveEvent`: se player está encostado em bloco sólido lateralmente (raytrace horizontal), setar velocity Y = +0.2 para simular escalada; cancelar gravidade |
| Master of Webs (ataque prende em teia) | `EntityDamageByEntityEvent`: spawnar COBWEB na posição do alvo; agendar remoção após 60t |
| Carnivore (só come carne) | `PlayerItemConsumeEvent`: cancelar se `!isMeat(item)` |
| Fragile (-3 corações) | `AttributeModifier` em `GENERIC_MAX_HEALTH` com valor -6.0 |

---

### Blazeborn

| Power | Implementação |
|---|---|
| Fire Immunity | `EntityDamageEvent`: cancelar se cause ∈ {FIRE, FIRE_TICK, LAVA, HOT_FLOOR} |
| Burning Wrath (+2 dano em chamas) | `EntityDamageByEntityEvent`: se `player.getFireTicks() > 0`, adicionar +2 ao dano base |
| Hotblooded (imune a veneno e fome) | `EntityPotionEffectEvent`: cancelar se tipo ∈ {POISON, HUNGER} |
| Hydrophobia (dano de água) | Tick: se `player.isInWater()` ou `player.isInRain()`, `player.damage(1.0)` |
| Dano de bola de neve | `EntityDamageByEntityEvent`: se damager é Snowball, adicionar +3 ao dano |

---

### Feline

| Power | Implementação |
|---|---|
| Acrobatics (sem dano de queda) | `EntityDamageEvent`: cancelar se cause == FALL |
| Strong Ankles (pulo maior) | `AttributeModifier` em `GENERIC_JUMP_STRENGTH` +0.1 |
| Velvet Paws (passos silenciosos) | `PlayerMoveEvent`: `event.getPlayer().setSneaking` não emite vibração — adicionalmente, usar `player.spawnParticle` nenhuma; a API do Paper tem `GameEvent` suppression via `Player.setGameEventSuppressor` se disponível, senão deixar como lore apenas |
| Nine Lives (-1 coração) | `AttributeModifier` em `GENERIC_MAX_HEALTH` -2.0 |
| Weak Arms (limitar mineração) | `BlockBreakEvent`: se bloco é pedra natural e player não tem STRENGTH potion e não tem picareta na mão, verificar blocos cardinais adjacentes; cancelar se count > 2 |
| Scare Creepers | `EntityTargetEvent`: cancelar se entidade é Creeper e target é este player |
| Cat Vision | PotionEffect NIGHT_VISION (999999t, sem partículas) permanente |
| Feline Grace | `AttributeModifier` em `GENERIC_MOVEMENT_SPEED` +0.02 |

---

### Elytrian

| Power | Implementação |
|---|---|
| Winged (Elytra permanente) | `onApply`: dar Elytra com PDC marker `originspaper:elytrian_wings`; `PlayerArmorChangeEvent`: se slot CHEST e item não tem o marker, reequipar após 1t |
| Gift of the Winds (skill ativa — lançar pra cima) | **ActivePowerType**, cooldown 600t: `player.setVelocity(player.getVelocity().setY(2.0))`; depois de 5t setar `player.setGliding(true)` |
| Aerial Combatant (+dano voando) | `EntityDamageByEntityEvent`: se `player.isGliding()`, multiplicar dano final por 3.0 |
| Need for Mobility (sem armadura pesada) | `PlayerArmorChangeEvent`: cancelar se `getDefense(item) > 5`; proteção de item = soma dos pontos de defesa (LEATHER=1, GOLD=2, CHAIN=2, IRON=3, DIAMOND=4, NETHERITE=4 por peça específica) |
| Claustrophobia (fraqueza sob teto) | Tick a cada 40t: raytrace Y+ de 3 blocos; se bloqueado, aplicar Weakness I (100t) + Slowness I (100t) |
| Brittle Bones (+dano de queda/colisão) | `EntityDamageEvent`: se cause ∈ {FALL, FLY_INTO_WALL}, multiplicar dano por 2.0 |

---

### Enderian

| Power | Implementação |
|---|---|
| Teleportation (pérola sem dano) | `onApply`: no tick, checar se player lançou EnderPearl (`PlayerLaunchProjectileEvent`); `ProjectileHitEvent`: se projectile é EnderPearl lançada por este player, cancelar dano (`PlayerTeleportEvent` já ocorre) |
| Hydrophobia (dano de água) | Tick: se `player.isInWater()` ou `player.isInRain()`, `player.damage(1.0)` |
| Scared of Gourds | `PlayerMoveEvent`: se bloco na linha de visão (2 blocos) é PUMPKIN/CARVED_PUMPKIN, aplicar Slowness II (40t) e som de medo |
| Slender Body (alcance extra) | `AttributeModifier` em `PLAYER_ENTITY_INTERACTION_RANGE` +1.5 e `PLAYER_BLOCK_INTERACTION_RANGE` +1.5 |
| Soft Touch (Silk Touch nas mãos) | `BlockDropItemEvent`: se player não tem ferramenta com Silk Touch, substituir drops pelo próprio bloco (`event.getItems().clear(); spawnar bloco como item`) |
| Dano de poções | `EntityDamageEvent`: se cause == MAGIC, multiplicar dano por 2.0 |

---

### Shulk

| Power | Implementação |
|---|---|
| Hoarder (inventário extra de 9 slots) | Criar inventário virtual de 9 slots por player, salvo em `shulk_inv/<uuid>.yml`; **skill ativa (agachar+F)**: abrir esse inventário; `PlayerDeathEvent`: não dropar o conteúdo desse inventário |
| Sturdy Skin (armadura natural) | `AttributeModifier` em `GENERIC_ARMOR` +4 e `GENERIC_ARMOR_TOUGHNESS` +2 |
| Strong Arms (quebrar pedra sem picareta) | `BlockBreakEvent`: se bloco é pedra/minério de pedra, não cancelar mesmo sem picareta |
| Unwieldy (sem escudo) | `PlayerInteractEvent`: cancelar se item em mão é SHIELD |
| Large Appetite | Tick: `player.setExhaustion(player.getExhaustion() + 0.005f)` a cada 20t |

---

### Dragon

| Power | Implementação |
|---|---|
| Dragon Wings (Elytra permanente) | Igual ao Elytrian, marker `originspaper:dragon_wings` |
| Reborn Magic (+dano perto do ovo) | Tick a cada 20t: checar DRAGON_EGG em raio 16; se encontrar, aplicar Strength I (40t) |
| Resistant Skin (+3 corações) | `AttributeModifier` em `GENERIC_MAX_HEALTH` +6.0 |
| Sharp Claws (+2 dano) | `AttributeModifier` em `GENERIC_ATTACK_DAMAGE` +2.0 |
| Sturdy Scales (armadura natural) | `AttributeModifier` em `GENERIC_ARMOR` +3.0 |
| Scaled Body (sem peitoral) | `PlayerArmorChangeEvent`: cancelar se slot == EquipmentSlot.CHEST |

---

### Wolf

| Power | Implementação |
|---|---|
| Canine Eyes | PotionEffect NIGHT_VISION permanente |
| Night Runner / Day Stride | Tick: se noite (`world.getTime()` 13000-23000), aplicar modifier MOVEMENT_SPEED +0.20; se dia, +0.15. Usar dois modifiers com UUIDs distintos, remover o outro ao trocar |
| Thick Hide (+3 corações) | `AttributeModifier` GENERIC_MAX_HEALTH +6.0 |
| Thick Fur (+2 armadura) | `AttributeModifier` GENERIC_ARMOR +2.0 |
| Wolf Metabolism | Tick noturno: `player.setExhaustion(player.getExhaustion() + 0.008f)` |
| Poison Immunity | `EntityPotionEffectEvent`: cancelar POISON |
| Carnivore's Bite (carne cura +1 coração) | `PlayerItemConsumeEvent`: se item é carne, agendar heal de +2.0 após 1t |
| Night Fangs (+4 dano à noite) | `EntityDamageByEntityEvent`: se noite, +4 ao dano |
| Hunter's Sense (ver criaturas próximas) | Tick a cada 40t: aplicar GLOWING (60t) em entidades vivas em raio 16 blocos |
| Alpha's Howl (skill ativa) | **ActivePowerType**, cooldown 2400t: aplicar Absorption II (200t) + som de lobo |

---

### Owl

| Power | Implementação |
|---|---|
| Winged (Elytra) | Igual ao Elytrian, marker `originspaper:owl_wings` |
| Night Hunter | Tick: se noite, aplicar Night Vision; se dia, remover |
| Talons (+dano em mergulho de voo) | `EntityDamageByEntityEvent`: se `player.isGliding()` e `player.getVelocity().getY() < -0.5`, multiplicar dano por 2.5 |
| Soft Landing | `EntityDamageEvent`: cancelar FALL |
| Velvet Paws | Mesmo que Feline |
| Carnivore | Mesmo que Arachnid |
| Light Frame | `PlayerArmorChangeEvent`: cancelar se proteção do item > 5 |
| Day-Dazed | Tick: se luz solar (`block.getLightFromSky() > 11` na posição do player), aplicar Weakness I (60t) + Mining Fatigue I (60t) |

---

### Gryphon

| Power | Implementação |
|---|---|
| Gryphon Wings (Elytra) | Igual ao Elytrian, marker `originspaper:gryphon_wings` |
| Take Flight (skill ativa) | **ActivePowerType**, cooldown 400t: `player.setVelocity(player.getVelocity().setY(2.5))`; após 3t, `player.setGliding(true)` |
| Dive Strike (+dano em mergulho) | Mesmo que Owl Talons |
| Sure Landing | `EntityDamageEvent`: cancelar FALL |
| Carnivore | Mesmo que Arachnid |
| Fresh Air (cama a ≥86 blocos) | `PlayerBedEnterEvent`: cancelar e enviar mensagem se `bed.getY() < 86` |
| Need for Mobility | Mesmo que Elytrian |

---

### Goat

| Power | Implementação |
|---|---|
| Leap (skill ativa — salto frontal) | **ActivePowerType**, cooldown 60t: `player.setVelocity(player.getLocation().getDirection().multiply(1.8).setY(0.5))` |
| Ram (knockback pesado) | `EntityDamageByEntityEvent`: aplicar knockback extra ao alvo: `entity.setVelocity(direction.multiply(2.5).setY(0.8))` |
| Brace (agachar evita dano de queda) | `EntityDamageEvent` FALL: cancelar se `player.isSneaking()` |
| Insulated (imune a neve em pó) | `EntityDamageEvent`: cancelar FREEZE |
| Fur Coat (lento em biomas quentes) | Tick a cada 40t: checar biome; se DESERT/SAVANNA/BADLANDS/ERODED_BADLANDS/WOODED_BADLANDS, aplicar Slowness I (60t) |
| Small (-2 corações) | `AttributeModifier` GENERIC_MAX_HEALTH -4.0 |
| Browser (carne menos nutritiva) | `PlayerItemConsumeEvent`: se item é carne, reduzir food level ganho em 60% (interceptar antes via `FoodLevelChangeEvent`) |

---

### Bear

| Power | Implementação |
|---|---|
| Mighty Paws (mão vazia forte) | `EntityDamageByEntityEvent`: se `player.getInventory().getItemInMainHand().getType() == AIR`, adicionar +5 ao dano e knockback forte ao alvo |
| Thick Fur (armadura + imune a frio) | `AttributeModifier` GENERIC_ARMOR +4.0; `EntityDamageEvent` FREEZE → cancelar |
| Primal Appetite (carne crua sacia mais) | `FoodLevelChangeEvent`: se item consumido é carne crua, dobrar `event.getFoodLevel()` delta |
| Hibernation (skill ativa) | **ActivePowerType**, cooldown 2400t: Strength II (200t) + Resistance I (200t) |
| Towering Stature (alcance maior) | `AttributeModifier` PLAYER_ENTITY_INTERACTION_RANGE +1.0 |
| Cumbersome Claws (não usa espada/machado) | `EntityDamageByEntityEvent`: se item em mão é SWORD ou AXE (qualquer tier), cancelar o ataque ou zerar dano extra |
| Heavy Bones (mais lento) | `AttributeModifier` GENERIC_MOVEMENT_SPEED -0.03; GENERIC_ATTACK_SPEED -0.5 |
| Environmental Waning (fraco fora de bioma) | Tick a cada 40t: se biome não é FOREST/TAIGA/OLD_GROWTH_BIRCH_FOREST/OLD_GROWTH_PINE_TAIGA/OLD_GROWTH_SPRUCE_TAIGA/GROVE/WINDSWEPT_FOREST/MEADOW, aplicar Weakness I (60t) |
| Bulky Body (sem peitoral Netherite/Diamond) | `PlayerArmorChangeEvent`: cancelar se item é NETHERITE_CHESTPLATE ou DIAMOND_CHESTPLATE |
| Large Appetite | Mesmo que Shulk |

---

### Rabbit

| Power | Implementação |
|---|---|
| High Carotene (night vision permanente) | PotionEffect NIGHT_VISION permanente |
| Prey (menos vida, mais rápido) | `AttributeModifier` GENERIC_MAX_HEALTH -4.0; GENERIC_MOVEMENT_SPEED +0.04 |
| Special Diet (só cenoura) | `PlayerItemConsumeEvent`: cancelar se item não é CARROT ou GOLDEN_CARROT; cenouras: dobrar food level ganho via `FoodLevelChangeEvent` |
| Bouncing Aura (Jump Boost permanente) | PotionEffect JUMP_BOOST amplifier 1 permanente |
| Replenish (acelera plantações) | Tick a cada 100t: para cada bloco de planta (WHEAT/CARROTS/POTATOES/BEETROOTS/etc.) em raio 4, 30% chance de chamar crescimento (setar data +1 via `CropState` ou `BlockData`) |

---

### Fox

| Power | Implementação |
|---|---|
| Pounce (skill ativa — salto com dano) | **ActivePowerType**, cooldown 100t: `player.setVelocity(dir.multiply(1.5).setY(0.6))`; marcar player como "pouncing" via PDC; `EntityDamageByEntityEvent`: se marcado e `player.getVelocity().getY() < 0`, +3 dano; desmarcar ao `PlayerMoveEvent` com `player.isOnGround()` |
| Hunt (toggle passivo) | `EntityDamageByEntityEvent`: rastrear último alvo e timestamp; se atacar mesmo alvo em <3s, aplicar Speed I (60t) + Strength I (60t) a si mesmo |
| Agility | GENERIC_MOVEMENT_SPEED +0.03; GENERIC_JUMP_STRENGTH +0.05; `EntityDamageEvent` FALL → multiplicar dano por 0.6 |
| Foxiality (sorte) | `AttributeModifier` GENERIC_LUCK +1.0 |
| Fluffy (frio ok, fogo não) | `EntityDamageEvent` FREEZE → cancelar; FIRE/FIRE_TICK/LAVA → multiplicar dano por 1.5 |
| Smol (-2 corações) | `AttributeModifier` GENERIC_MAX_HEALTH -4.0 |
| Unique Taste (dieta limitada) | `PlayerItemConsumeEvent`: permitir apenas carnes + APPLE/GOLDEN_APPLE/ENCHANTED_GOLDEN_APPLE/SWEET_BERRIES/GLOW_BERRIES/MELON_SLICE; cancelar resto |
| Fast-Paced (velocidade de ataque) | `AttributeModifier` GENERIC_ATTACK_SPEED +0.5 |
| Timidity (fraqueza com pouca vida) | Tick: se `player.getHealth() / player.getAttribute(GENERIC_MAX_HEALTH).getValue() <= 0.3`, aplicar Weakness I (40t) |
| Weak (sem escudo nem armadura pesada) | `PlayerArmorChangeEvent` + `PlayerInteractEvent`: bloquear SHIELD e armaduras com proteção > 5 |

---

## Comandos

| Comando | Permissão | Descrição |
|---|---|---|
| `/origin` | `originspaper.select` (default: true) | Abre GUI de seleção (só se sem origin, ou admin) |
| `/origin set <player> <origin>` | `originspaper.admin` | Força origin de jogador |
| `/origin reset <player>` | `originspaper.admin` | Remove origin e força reescolha |
| `/origin info [player]` | `originspaper.select` | Mostra origin e powers ativos |

---

## Regras de implementação obrigatórias

1. **Nenhum NMS**: zero imports de `net.minecraft.*` ou `org.bukkit.craftbukkit.*`
2. **Java 25**: usar `toolchain.languageVersion.set(JavaLanguageVersion.of(25))` no Gradle
3. **`api-version: "26.1"`** no `plugin.yml`
4. **AttributeModifier com UUID fixo por power**: `UUID.nameUUIDFromBytes("originspaper:merling:fins".getBytes())` — permite add/remove determinístico
5. **Tick único**: um único `runTaskTimer` de 20t por jogador online, que itera todos os powers e chama `onTick(player)`. Não criar um timer por power.
6. **Cooldowns**: `Map<UUID, Long>` com `System.currentTimeMillis()`
7. **Persistência simples**: YAML em `plugins/OriginsPaper/data/<uuid>.yml`
8. **Login**: `PlayerJoinEvent` → carregar origin salva → `onApply` em cada power → se sem origin, abrir GUI após 20t de delay
9. **Logout**: `PlayerQuitEvent` → `onRemove` em cada power (limpar AttributeModifiers e efeitos de poção)
10. **Efeitos de poção permanentes**: duração 999999t, `ambient=true, particles=false, icon=false`; reaplicar no tick se o efeito não estiver ativo
11. **Itens protegidos** (Elytra de origins voadoras): marcar com `PersistentDataContainer` + `NamespacedKey` para identificar e reequipar se removidos
12. **GUI não-fechável**: enquanto player não tiver origin, `InventoryCloseEvent` → reabrir GUI após 1t

---

## O que NÃO implementar no MVP

- Phantom (atravessar blocos requer NMS)
- Sons customizados além dos sons vanilla do Bukkit
- Troca de origin pelo próprio jogador (só admin)
- Origin layers / grupos
- Suporte a datapack externo

---

## Entregáveis

1. Repositório com estrutura Gradle funcional
2. `./gradlew build` compila sem erros
3. JAR em `build/libs/originspaper-1.0.0.jar`
4. Todas as 16 origins com powers listados
5. GUI de duas telas (lista + detalhe) com cabeças
6. Persistência entre sessões
7. `README.md` com instruções de build e deploy

---

## Ordem de implementação sugerida

1. Setup Gradle (Java 25, `api-version 26.1`) + `plugin.yml` + classe principal
2. `PowerType` + `ActivePowerType` interfaces
3. `OriginRegistry` + `PlayerOriginData`
4. Persistência YAML (save/load)
5. `PowerEventListener` (delegar eventos)
6. `ActiveSkillListener` (agachar+F)
7. GUI: `OriginSelectionGUI` (tela 1) + `OriginDetailGUI` (tela 2) + `OriginSelectionListener`
8. Lógica de join (abrir GUI se sem origin)
9. Powers compartilhados (`shared/`)
10. Origins em ordem de complexidade crescente:
    Human → Blazeborn → Rabbit → Feline → Enderian → Wolf → Dragon → Shulk →
    Merling → Elytrian → Owl → Gryphon → Goat → Fox → Arachnid → Bear
11. Comandos `/origin`
12. README
