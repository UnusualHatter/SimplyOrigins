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
├── OriginsPaper.java              # main class, registra listeners e scheduler
├── api/
│   ├── Origin.java                # record com id, displayName, powers, skullTexture
│   ├── PowerType.java             # interface base
│   └── ActivePowerType.java       # extends PowerType, adiciona onActivate + getCooldownTicks
├── registry/
│   ├── OriginRegistry.java        # Map<String, Origin> das 16 origins
│   └── PlayerOriginData.java      # estado por jogador: origin, powers, cooldowns
├── listener/
│   ├── OriginSelectionListener.java  # PlayerJoinEvent, InventoryClickEvent da GUI
│   ├── ActiveSkillListener.java      # PlayerSwapHandItemsEvent + isSneaking
│   └── PowerEventListener.java       # delega todos os eventos para os powers ativos
├── gui/
│   ├── OriginSelectionGUI.java    # tela 1: grid de origins com cabeças
│   └── OriginDetailGUI.java       # tela 2: detalhe + botões escolher/voltar
├── power/
│   ├── shared/                    # powers reutilizáveis
│   └── origins/                   # um subpacote por origin
└── util/
    ├── SkullBuilder.java          # helper para criar PLAYER_HEAD com textura
    ├── PersistenceUtil.java       # save/load YAML por UUID
    └── CooldownUtil.java          # Map<UUID, Long> + helpers de cooldown
```

---

## Convenções de código

- Cada `PowerType` é uma classe concreta no pacote `power/origins/<origin>/` ou `power/shared/`
- `getId()` retorna `"<origin>:<power>"`, ex: `"merling:fins"`
- `AttributeModifier` sempre usa `UUID.nameUUIDFromBytes(getId().getBytes())` — nunca UUID aleatório
- Efeitos de poção permanentes: duração `999999`, `ambient=true`, `particles=false`, `icon=false`
- Reaplicar efeitos permanentes no tick se o efeito não estiver ativo no player
- Tick global: um único `runTaskTimer` de 20t que itera `Bukkit.getOnlinePlayers()` e chama `onTick` para cada power ativo. Nunca criar timer por power ou por player.
- PDC key padrão para itens protegidos: `new NamespacedKey(plugin, "protected_item")`

---

## Persistência

- Arquivo: `plugins/OriginsPaper/data/<uuid>.yml`
- Conteúdo: `origin: "<id>"`
- Inventário extra do Shulk: `plugins/OriginsPaper/shulk_inv/<uuid>.yml`
- Carregar no `PlayerJoinEvent`, salvar imediatamente após escolha

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
- Cancelar o evento sempre que for detectado como input de skill (não trocar os itens)
- Checar cooldown antes de chamar `onActivate`
- Cooldown armazenado em `PlayerOriginData` como `Map<String, Long>` (powerId → timestamp em ms)
- Se a origin não tem `ActivePowerType`, o input não faz nada (não cancelar o evento nesse caso)

---

## Origins implementadas

16 origins, sem Phantom. Em ordem de complexidade crescente para implementar:

`human` → `blazeborn` → `rabbit` → `feline` → `enderian` → `wolf` → `dragon` → `shulk` → `merling` → `elytrian` → `owl` → `gryphon` → `goat` → `fox` → `arachnid` → `bear`

---

## Erros comuns a evitar

- **Nunca** usar `ItemStack.getItemMeta()` dentro de `editMeta()` — undefined behavior
- **Nunca** `UUID.randomUUID()` em `AttributeModifier` — use `nameUUIDFromBytes`
- **Nunca** registrar um `Listener` mais de uma vez — registrar tudo uma vez no `onEnable`
- **Nunca** abrir GUI diretamente no `PlayerJoinEvent` — usar `runTaskLater` com 20t de delay
- `PlayerArmorChangeEvent` só é chamado para mudanças via inventário normal; equipamento via código não dispara o evento — considerar isso ao proteger itens de origin
- `EntityDamageByEntityEvent`: o damager pode ser um `Projectile`, checar o shooter se necessário

---

## O que NÃO está no escopo

- Phantom (atravessar blocos requer NMS — excluído permanentemente)
- Troca de origin pelo próprio jogador (só via admin command)
- Suporte a datapack externo
- Integração com outros plugins
- Sons ou partículas customizadas além das vanilla

---

## Se o build quebrar

1. Rodar `./gradlew build --stacktrace` e ler o erro completo
2. Erros de `symbol not found` geralmente indicam método da API que mudou — verificar o Javadoc em `https://jd.papermc.io/paper/26.1.2/`
3. Erros de `api-version` no servidor: confirmar que `plugin.yml` tem `api-version: "26.1"` (não `"26"`, não `"26.1.2"`)
4. Erros de Java version: confirmar `toolchain.languageVersion.set(JavaLanguageVersion.of(25))` no `build.gradle.kts`
