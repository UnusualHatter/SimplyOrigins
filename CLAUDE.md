# OriginsPaper — CLAUDE.md

## Autonomia

Você tem controle total sobre este repositório. Tome decisões de implementação, estrutura e arquitetura sem pedir aprovação. Quando encontrar um problema, resolva-o. Só interrompa se algo for genuinamente ambíguo e não puder ser inferido pelo contexto do projeto.

---

## O que é este projeto

Plugin Paper para Minecraft 26.1.2 que reimplementa o sistema de Origins (raças com poderes passivos) usando exclusivamente a API pública do Paper/Bukkit. Zero NMS, zero mixins, zero dependências externas além do Paper API.

---

## Stack

- Java 25
- Gradle com Kotlin DSL
- `io.papermc.paper:paper-api:26.1.2.build.+`
- `api-version: "26.1"` no `plugin.yml`

---

## Build

```bash
./gradlew build                   # compilar
./gradlew build --stacktrace      # compilar com trace de erro
```

JAR de saída: `build/libs/originspaper-1.0.0.jar`

Para testar: copiar o JAR para `plugins/` de um servidor Paper 26.1.2 local e rodar `./start.sh` ou equivalente.

---

## Estrutura do projeto

```
src/main/java/dev/originspaper/
├── OriginsPaper.java              # main class, registra listeners e o tick global
├── api/
│   ├── Origin.java                # record: id, displayName, skullTexture, fallbackIcon, powers, infos
│   ├── PowerType.java             # interface base (todos os hooks de evento são defaults opcionais)
│   └── ActivePowerType.java       # extends PowerType, adiciona onActivate + getCooldownTicks
├── command/
│   └── OriginCommand.java         # /origin [set|reset|info] + tab-complete
├── registry/
│   ├── OriginRegistry.java        # Map<String, Origin> das 15 origins
│   ├── PlayerDataManager.java     # aplica/remove powers, persiste, clamp de vida
│   └── PlayerOriginData.java      # estado por jogador: origin, powers, cooldowns
├── listener/
│   ├── OriginSelectionListener.java  # join/quit, clicks e fechamento das GUIs
│   ├── ActiveSkillListener.java      # PlayerSwapHandItemsEvent + isSneaking
│   └── PowerEventListener.java       # delega todos os eventos para os powers ativos
├── gui/
│   ├── OriginSelectionGUI.java    # tela 1: grid de origins com cabeças
│   ├── OriginDetailGUI.java       # tela 2: detalhe + botões escolher/voltar
│   ├── SelectionHolder.java       # InventoryHolder marcador da tela 1
│   └── DetailHolder.java          # InventoryHolder da tela 2 (lembra qual origin)
├── power/
│   ├── shared/                    # powers reutilizáveis (parametrizáveis)
│   └── origins/                   # um subpacote por origin com os powers específicos
└── util/                          # SkullBuilder, PersistenceUtil, CooldownUtil, AttributeUtil,
                                   # EffectUtil, FoodUtil, ArmorUtil, GroundUtil, TextUtil, OriginsLogger
```

---

## Convenções de código

- Cada `PowerType` é uma classe concreta no pacote `power/origins/<origin>/` ou `power/shared/`
- `getId()` retorna `"<origin>:<power>"`, ex: `"otter:fins"`
- `AttributeModifier` é sempre keado por um `NamespacedKey` derivado do `getId()` — use `AttributeUtil.set/clear`, que cuidam disso de forma idempotente. Nunca `UUID.randomUUID()`.
- Efeitos de poção permanentes (`EffectUtil.PERMANENT = 999999`): `ambient=true`, `particles=false`, `icon=false`. Use `EffectUtil.ensure/clear` — `ensure` só reaplica se faltar.
- Reaplicar efeitos permanentes no tick se o efeito não estiver ativo no player
- Tick global: um único `runTaskTimer` de 20t que itera `Bukkit.getOnlinePlayers()` e chama `onTick` para cada power ativo. **`OriginsPaper.tick()` conta esses ciclos (1/segundo), não game-ticks** — cuidado com unidades em modulos e janelas de tempo. Nunca criar timer por power ou por player.
- PDC: a GUI usa `NamespacedKey(plugin, "origin_id")` nas cabeças; cada `ElytraFlightPower` usa um marcador próprio por origin (ex.: `"dragon_wings"`).

---

## Persistência

- Arquivo: `plugins/OriginsPaper/data/<uuid>.yml`
- Conteúdo: `origin: "<id>"`
- Carregar no `PlayerJoinEvent` (via `PlayerDataManager.load`), salvar imediatamente após a escolha

---

## GUI — fluxo obrigatório

1. `PlayerJoinEvent`: se sem origin salva → abrir `OriginSelectionGUI` após delay de 20t
2. Tela 1 (`OriginSelectionGUI`): grid 54 slots, cabeças, clicar → abre Tela 2
3. Tela 2 (`OriginDetailGUI`): detalhe da origin, botão Escolher (slot 31) e Voltar (slot 29)
4. Escolher → aplicar powers → salvar → fechar GUI
5. Enquanto sem origin: `InventoryCloseEvent` → reabrir GUI após 1t (não deixar o player fechar)
6. Tela 1 e Tela 2 são `InventoryHolder` customizados para identificar os GUIs nos eventos de click

---

## Skill ativa

- Input: `PlayerSwapHandItemsEvent` com `player.isSneaking() == true`
- **Agachar + F é sempre consumido para qualquer jogador com origin** (o item nunca troca de mão enquanto agachado). Pressionar F sem agachar faz a troca vanilla normalmente.
- Jogador sem origin selecionada (`!data.hasOrigin()`) → não cancelar, deixar a troca vanilla acontecer
- Checar cooldown antes de chamar `onActivate`
- Cooldown armazenado em `PlayerOriginData` como `Map<String, Long>` (powerId → timestamp em ms)
- Se a origin não tem `ActivePowerType`, o input é consumido (sem troca de item) mas nenhuma skill é ativada

---

## Origins implementadas

15 origins, registradas em ordem de exibição na GUI (Human primeiro). Veja `OriginRegistry` para os powers de cada uma e `README.md` para a descrição completa:

`human` → `otter` → `deer` → `bat` → `rat` → `demon` → `wolf` → `fox` → `bear` → `rabbit` → `goat` → `feline` → `owl` → `gryphon` → `dragon`

Com habilidade ativa (`ActivePowerType`, agachar + F): `demon` (Hell Pact), `wolf` (Alpha's Howl), `fox` (Pounce), `bear` (Hibernation), `goat` (Leap), `owl` (Echolocation), `gryphon` (Take Flight), `dragon` (Dragon's Breath). As demais são apenas passivas.

---

## Erros comuns a evitar

- **Nunca** usar `ItemStack.getItemMeta()` dentro de `editMeta()` — undefined behavior
- **Nunca** `UUID.randomUUID()` em `AttributeModifier` — use `AttributeUtil` (keado por `NamespacedKey`)
- **Nunca** registrar um `Listener` mais de uma vez — registrar tudo uma vez no `onEnable`
- **Nunca** abrir GUI diretamente no `PlayerJoinEvent` — usar `runTaskLater` com 20t de delay
- `PlayerArmorChangeEvent` só é chamado para mudanças via inventário normal; equipamento via código não dispara o evento — considerar isso ao proteger itens de origin
- `EntityDamageByEntityEvent`: o damager pode ser um `Projectile`, checar o shooter se necessário

---

## O que NÃO está no escopo

- Qualquer poder que exija NMS/mixins (ex.: atravessar blocos) — fora do escopo permanentemente
- Troca de origin pelo próprio jogador (apenas admin via `/origin set|reset`)
- Suporte a datapack externo
- Integração com outros plugins
- Sons ou partículas customizadas além das vanilla

---

## Se o build quebrar

1. Rodar `./gradlew build --stacktrace` e ler o erro completo
2. Erros de `symbol not found` geralmente indicam método da API que mudou — verificar o Javadoc em `https://jd.papermc.io/paper/26.1.2/`
3. Erros de `api-version` no servidor: confirmar que `plugin.yml` tem `api-version: "26.1"` (não `"26"`, não `"26.1.2"`)
4. Erros de Java version: confirmar `toolchain.languageVersion.set(JavaLanguageVersion.of(25))` no `build.gradle.kts`
