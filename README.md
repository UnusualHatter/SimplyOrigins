# OriginsPaper

A Paper plugin for **Minecraft 26.1.2** that reimplements the *Origins* system (races with
passive/active powers) using **only the public Paper/Bukkit API** — no NMS, no mixins, no shaded
jars, no external dependencies beyond `paper-api`.

16 origins, a two-screen selection GUI with custom heads, per-player persistence, and a single
global tick driving every power.

---

## Requirements

- **Java 25**
- **Paper 26.1.2** server (api-version `26.1`)
- Gradle wrapper is included (`./gradlew`), which runs on **Gradle 9.5.1**

## Build

```bash
./gradlew build                 # compile + assemble the jar
./gradlew build --stacktrace    # with full error trace
```

Output: **`build/libs/originspaper-1.0.0.jar`**

## Deploy

1. Copy `originspaper-1.0.0.jar` into your server's `plugins/` folder.
2. Start a Paper 26.1.2 server.
3. On first join a player is prompted to choose an origin (the GUI cannot be closed until they do).

---

## Gameplay

- **Choose an origin:** the selection GUI opens automatically on first join. Click an origin to see
  its details, then **Escolher** to confirm. Choices are permanent (admin-only reset).
- **Active skill:** **sneak + swap-hands (default `F`)** triggers the origin's single active power
  (if it has one), respecting its cooldown.
- **Persistence:** each player's origin is stored at `plugins/OriginsPaper/data/<uuid>.yml`. The
  Shulk's extra bag is stored at `plugins/OriginsPaper/shulk_inv/<uuid>.yml`.

### Commands

| Command | Permission | Description |
|---|---|---|
| `/origin` | `originspaper.select` (default: true) | Opens the selection GUI (only if you have no origin, or you are admin) |
| `/origin info [player]` | `originspaper.select` | Shows the origin and its powers |
| `/origin set <player> <origin>` | `originspaper.admin` | Forces a player's origin |
| `/origin reset <player>` | `originspaper.admin` | Removes a player's origin and reprompts |

### The 16 origins

`human`, `merling`, `arachnid`, `blazeborn`, `feline`, `elytrian`, `enderian`, `shulk`, `dragon`,
`wolf`, `owl`, `gryphon`, `goat`, `bear`, `rabbit`, `fox`. (No Phantom — phasing through blocks
requires NMS and is out of scope.)

---

## Architecture

```
dev.originspaper
├── OriginsPaper            # main class: singleton, single 20-tick scheduler, wiring
├── api/                    # Origin (record), PowerType, ActivePowerType
├── registry/               # OriginRegistry (16 origins), PlayerOriginData, PlayerDataManager
├── listener/               # PowerEventListener (event fan-out), ActiveSkillListener, OriginSelectionListener
├── gui/                    # OriginSelectionGUI + OriginDetailGUI (+ InventoryHolder markers)
├── power/
│   ├── shared/             # parameterised, reusable powers (attributes, immunities, diets, wings…)
│   └── origins/<origin>/   # origin-specific power logic (climbing, webs, hoarder bag, pounce…)
├── command/                # /origin
└── util/                   # attributes, effects, skulls, persistence, cooldowns, text, food, armor
```

### Key design decisions (verified against the live 26.1.2 API)

- **Attributes are non-prefixed** in this version (`MAX_HEALTH`, `MOVEMENT_SPEED`, `JUMP_STRENGTH`,
  …). Modifiers are applied deterministically by a `NamespacedKey` derived from each power id, so
  add/remove is idempotent (`AttributeUtil`).
- **One global tick:** a single `runTaskTimer(20t)` iterates online players and calls `onTick` on
  each active power. Powers needing slower cadences (40t/100t) gate on `OriginsPaper.tick()`.
- **Damage dispatch:** `EntityDamageByEntityEvent` shares `EntityDamageEvent`'s handler list, so a
  single handler covers both. `onDamage` is dispatched victim-side; `onDamageByEntity` attacker-side.
- **`PlayerArmorChangeEvent` is not cancellable** in this API, so armor-restriction and flight-wing
  powers let the change happen and **revert it one tick later** (`ArmorRevertSupport`,
  `ElytraFlightPower`).
- **Permanent potion effects** use duration `999999`, `ambient=true, particles=false, icon=false`,
  and are refreshed from the tick loop.

### Custom heads / textures

The selection GUI uses textured player heads where a valid texture URL is available (Human). For the
non-human origins the PRD's placeholder texture URLs are not real, so each falls back to a thematic
**material icon** (e.g. `SPIDER_EYE` for Arachnid, `BLAZE_ROD` for Blazeborn, `DRAGON_HEAD` for
Dragon). Drop real texture URLs into `OriginRegistry` to switch any origin to a custom head.

---

## Out of scope

Phantom origin, player-initiated origin swapping (admin only), origin layers/groups, external
datapack support, third-party plugin integration, and custom (non-vanilla) sounds/particles.
