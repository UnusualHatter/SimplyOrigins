# TODO

## ✅ CONCLUÍDO — OriginsPaper 1.4.1 no Paper 26.2 (25/09/2026)

Instalado no servidor junto com a "FeatherFlux 2.0". Build limpo contra
`paper-api:26.2.build.129-stable` com `-Xlint:all` (só resta um `this-escape`
inofensivo em `ElytraFlightPower`).

Correções:
- **Magma (26.2):** o dano de magma agora chega como `CONTACT`, então
  `FireImmunityPower` testa `DamageType.HOT_FLOOR` pelo `DamageSource`. Antes da
  correção o Demônio morria em magma na 26.2 ("discovered the floor was lava").
- **PvP:** `World.getPVP()` trocado por `getGameRuleValue(GameRules.PVP)`.
- **Vida ao relogar:** os corações extras da progressão só voltam no `onTick`,
  e o logout removia o bônus antes do save — um Humano nível 10 saía com 24/24
  e voltava com 20. Agora a vida de logout é salva (`logout-health`) e devolvida
  quando o máximo volta, sem passar do valor de saída (não vira cura grátis).
  Vale também para `/origin reload` e para o desligamento do servidor.
- **Arquivo corrompido:** um `data/<uuid>.yml` ilegível era lido como vazio e o
  próximo save apagava a origem e a progressão. Agora ele é movido para
  `<uuid>.yml.corrupt-<timestamp>` com erro no log, para restaurar à mão.
- **Seleção forçada:** o congelamento prendia o jogador no ar (reset planando
  ou pulando) e o Paper expulsava por voo. Cair agora é permitido.
- **GUIs:** arrastar itens para as telas do plugin é cancelado (o item ia para
  um inventário descartado).
- **Farm de XP com pistão:** o marcador de "bloco colocado" acompanha o bloco
  empurrado/puxado por pistão.
- `api-version: "26.2"`, paper-api fixo, GriefPrevention 16.18.7. AxGraves
  segue compilando contra 1.26.0 (a 1.32 não está no repositório Maven); a API
  usada (`GraveSpawnEvent`, `Grave.getGui/updateHologram`) foi conferida no jar
  1.32.0 do servidor.

Revisado sem mudança necessária: os 23 mapas por jogador dos poderes são
limpos no `onRemove`; os 12 `runTaskLater` resolvem o jogador por UUID e checam
se o poder ainda está ativo; os 32 valores de config têm limite; os comandos
validam permissão, jogador, origem e números.

Testado com bots (mineflayer via ViaVersion) numa cópia do servidor: magma
(Demônio 20/20, Humano 20→5), relog 24→24 e 10→10, queda congelado sem kick,
Spray do Gambá com pvp false/true, arquivo corrompido, as 21 origens no nível
10 ativando habilidade sem nenhum erro no log, e nenhum modificador ou asa
sobrando depois de passar pelas 21.

Ideias para uma próxima versão (conteúdo novo da 26.2): atributos
`BOUNCINESS`, `FRICTION_MODIFIER`, `AIR_DRAG_MODIFIER`; bioma `SULFUR_CAVES`,
mob `SULFUR_CUBE`, blocos de enxofre/cinábrio. Decisão em aberto: fogueira
(`DamageType.CAMPFIRE`) nunca esteve na imunidade do Demônio.

---

## ✅ CONCLUÍDO — proteger pets/entidades nomeadas do dano em área

Feito em v1.1.7:
- Criado `util/SafeTargetUtil.isProtected(LivingEntity)` — protege `Tameable`
  domesticado e mobs com nome customizado (`customName() != null`).
- Aplicado nas explosões da Cabra (`LeapPower.blastAt` **e** na detecção de
  alvo `findTargetAlong`, para a investida atravessar o pet sem detonar) e da
  Raposa (`PouncePower.landingBlast`).
- `MightyPawsPower` (Urso) e `RamPower` (Cabra) **não** receberam o filtro: são
  golpes diretos de um único alvo (`onDamageByEntity`), ou seja, o jogador
  escolheu bater — bater no próprio pet é intencional, como no vanilla.
- Convenção registrada no `CLAUDE.md` (seção "Erros comuns a evitar").

---

## ✅ CONCLUÍDO — Sistema de progressão (Fase 2, conteúdo funcional)

Fase 1 (motor: `OriginProgress`, `ProgressionManager`, persistência, boss bar,
comandos) + UI "Minha Origem" já estavam prontas. A Fase 2 ligou o conteúdo:

- Padrão estabelecido em `power/shared/ProgressionPower` (base com `level()`,
  `award()`, acumulador de distância e `attr()` idempotente). Cada origem tem
  seu `<Origin>ProgressionPower` (sem `PowerInfo`, não aparece na GUI) que
  **ganha XP via hooks** e **lê os marcos dinamicamente** do nível atual — sem
  reaplicar/revogar manualmente, sempre em sincronia com level-up/`setlevel`.
- Marcos intrínsecos de skill ficam na própria power ativa lendo o nível:
  `EvasionPower`, `LeapPower`, `RamPower`, `PouncePower`, `EcholocationPower`,
  `FlightLaunchPower`, `DragonBreathPower`, `PollinatorPower`, `ScurryPower`.
- Lobo continua como piloto (lógica em `NightFangsPower`/`AlphaHowlPower`).
- `ProgressionDefs` reescrito para o texto bater com o efeito real (a GUI exibe
  exatamente o que o código concede/faz).
- `onDisable`/`reload` agora gravam o progresso antes de descarregar.

Anti-exploit (ver memory/progression-pattern.md):
- `PlacedBlockTracker` impede farm de XP por colocar+quebrar bloco
  (otter/bat/rat ignoram blocos colocados por jogador).
- `accrueDistance` ignora movimento dentro de veículo (anti-loop de carrinho).
- XP de mergulho (coruja/grifo) e de esquiva (rato) com cooldown anti-spam.
- Tab-complete só sugere subcomandos admin para quem tem `originspaper.admin`.

Validar em jogo: `/origin setlevel <jogador> <n>` para forçar cada marco e
confirmar liga/desliga sem stacking.

---

## Scan geral — caçar bugs no plugin

Revisão completa do código em busca de bugs reais (não apenas estilo),
cobrindo as áreas onde esse tipo de plugin costuma escondê-los:

1. **Powers individuais (`power/origins/**`, `power/shared/**`)**
   - `onApply`/`onRemove` simétricos: tudo que é setado num é limpo no outro
     (`AttributeModifier` via `AttributeUtil`, efeitos via `EffectUtil`,
     PDC markers, itens equipados).
   - Nenhum `UUID.randomUUID()` em `AttributeModifier` (sempre via
     `AttributeUtil`, idempotente).
   - `onTick` usando `plugin().tick() % N` corretamente (unidade = segundos,
     não game-ticks) — checar se algum power confundiu as duas escalas.
   - Powers com `Map<UUID, ...>` de estado (ex. `PouncePower`, `LeapPower`,
     `HellPactPower`, `FlightLaunchPower`) limpando a entrada no `onRemove`
     e no quit — risco de leak por jogador que desconecta no meio do efeito.
2. **Dispatch de eventos (`PowerEventListener`, `ActiveSkillListener`)**
   - Conferir que nenhum hook novo (ex. os de progressão) ficou sem
     despachar para os powers ativos do jogador certo (vítima vs. atacante).
   - `EntityDamageByEntityEvent` com `Projectile` — confirmar resolução do
     `shooter` em todos os pontos que precisam.
3. **Concorrência / scheduler**
   - Nenhum `runTaskTimer` por power ou por jogador (deve haver só o tick
     global). Verificar se algo introduzido recentemente violou isso.
   - `runTaskLater` que captura `Player` diretamente em vez de UUID + lookup
     — checar risco de referência obsoleta se o jogador desconectar antes do
     callback rodar.
4. **GUI (`gui/**`, `listener/OriginSelectionListener.java`)**
   - Cliques fora dos slots esperados / inventário com tamanho insuficiente
     para todas as origens com mais poderes (Urso, Raposa, Coruja, Felino).
   - Fechar a GUI de seleção sem origem ainda reabre corretamente (sem loop
     nem flicker) — válido também para a nova `OriginProgressGUI`.
5. **Persistência (`PersistenceUtil`, `PlayerDataManager`)**
   - Arquivo `<uuid>.yml` com `progress` corrompido/ausente não deve travar o
     load do jogador (fallback seguro para nível 1 / XP 0).
   - `/origin reset` e troca de origem não deixam cooldowns ou progress de
     uma origin antiga "sangrando" para a nova.
6. **Comandos (`OriginCommand`)**
   - `/origin addxp` e `/origin setlevel` com valores fora do range (negativo,
     maior que `OriginProgress.MAX_LEVEL`, não-numérico) — confirmar que não
     quebram a boss bar nem corrompem o save.
   - Tab-complete não sugere subcomandos administrativos para quem não tem
     `originspaper.admin`.
7. **`ElytraFlightPower` / `ElytraGuardListener`**
   - Reconfirmar que nenhuma combinação de morte + plugin de cova + reload
     consegue duplicar ou prender a elytra (já corrigido antes, mas é a área
     de maior histórico de bugs do plugin — vale re-testar após qualquer
     mudança no entorno).
8. **Build**
   - `./gradlew build --stacktrace` limpo, sem warnings novos de deprecação
     ou de import não usado introduzidos pelas mudanças acima.
