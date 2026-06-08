# SimplyOrigins

A Paper plugin for **Minecraft 26.1.2** that reimagines the *Origins* system using **only the public Paper/Bukkit API** — no NMS, no mixins, no shaded jars, and no external mods on the client-side.

15 custom origins carefully balanced, a two-screen selection GUI with custom materials, per-player persistence, and a single global tick driving all powers perfectly synchronized with the server logic.

---

## 📦 Características Principais
- **15 Origens únicas** totalmente configuradas e balanceadas.
- **Sistema Híbrido e Limpo:** Sem necessidade de mods no cliente (funciona 100% vanilla server-side).
- **Controles Intuitivos:** Habilidades ativas são acionadas usando a combinação `Agachar + F` (Sneak + Swap Hand).
- **Sem Conflitos de Física:** Totalmente reconstruído para respeitar o Knockback nativo, pulos e a física de dano do Bukkit.

---

## 🌟 As Origens

### 🧍 Humano (Human)
A base normal do Minecraft. Sem vantagens nem desvantagens.
- **Sem habilidades passivas.**

### 🦦 Lontra (Otter)
- **Amphibious:** Respira perfeitamente debaixo d'água e na terra.
- **Aqua Affinity:** Minera na velocidade normal mesmo submerso.
- **Wet Eyes:** Enxerga com clareza e ganha graça do golfinho na água.
- **Fins:** Nada muito mais rápido quando submerso.
- **Like Water:** Maior controle de movimento embaixo d'água.
- **Water Dependency:** Recebe fraqueza e lentidão se ficar 10 minutos sem tocar na água ou chuva.
- **Small Body:** Seu corpo é 20% menor.

### 🦌 Cervo (Deer)
- **Forest Agility:** Mais rápido em florestas e taigas.
- **Soft Landing:** Sofre 50% menos dano de queda.
- **Natural Runner:** Correr em terrenos naturais (terra, grama, pedra) aumenta sua velocidade.
- **Nimble Legs:** Passa por cima de obstáculos baixos sem precisar pular.
- **Alert Leap:** Pula mais alto, sempre pronto para fugir do perigo.
- **Fragile:** Possui 1 coração a menos.

### 🦇 Morcego (Bat)
- **Dark Vision:** Visão noturna permanente.
- **Bat Wings:** Cai lentamente (planagem) ao cair no ar.
- **Cave Mobility:** Mais rápido e minera velozmente no fundo de cavernas escuras.
- **Sun Averse:** Fica exausto e fraco sob a luz solar direta.
- **Fragile:** Possui 2 corações a menos.
- **Tiny Body:** Seu corpo é 30% menor.

### 🐀 Rato (Rat)
- **Small Body:** Seu corpo é 30% menor.
- **Evasion:** Você tem 30% de chance de desviar completamente de projéteis.
- **Silent Steps:** Passos silenciosos (cosmético).
- **Swift:** Mais rápido ao andar e correr.
- **Fragile:** Possui 2 corações a menos.
- **Short Reach:** Seus bracinhos não alcançam tão longe.

### 👿 Demônio (Demon)
- **Hellborn:** Imune a fogo, lava e chão quente.
- **Infernal Power:** Causa +1 coração de dano corpo a corpo.
- **Dark Vision:** Visão noturna permanente.
- **Hell Pact:** `Agachar + F`: Ganha Força I por 10s. Depois sofre Fraqueza e Fome por 5s.
- **Sun Averse:** Fica exausto e fraco sob a luz solar direta.
- **Holy Vulnerability:** Sofre o dobro de dano de magia e poções.
- **Cold Weakness:** Fica lento em biomas nevados e congelados.

### 🐺 Lobo (Wolf)
- **Canine Eyes:** Visão noturna permanente.
- **Night Runner:** Mais rápido à noite, ainda ágil de dia.
- **Wolf Metabolism:** Gasta mais energia à noite.
- **Poison Immunity:** Imune a veneno.
- **Carnivore's Bite:** Comer carne cura meio coração extra.
- **Night Fangs:** Causa +2 de dano à noite.
- **Hunter's Sense:** Destaca criaturas próximas.
- **Alpha's Howl:** `Agachar + F`: ganha absorção e uiva.

### 🦊 Raposa (Fox)
- **Pounce:** `Agachar + F`: salto que causa dano ao cair.
- **Hunt:** Atacar a mesma presa repetidamente te fortalece.
- **Agility:** Mais rápido, pula mais e cai mais leve.
- **Foxiality:** Mais sorte nos drops.
- **Fluffy:** Imune ao frio, mas frágil ao fogo.
- **Smol:** Possui 2 corações a menos.
- **Unique Taste:** Come apenas carnes e algumas frutas.
- **Timidity:** Fica fraco com pouca vida.
- **Weak:** Não usa escudo nem armadura pesada.
- **Small Body:** Seu corpo é 10% menor.

### 🐻 Urso (Bear)
- **Mighty Paws:** Mãos vazias causam +3 de dano e forte empurrão.
- **Thick Fur:** Armadura natural e imunidade ao frio.
- **Primal Appetite:** Carne crua sacia muito mais.
- **Hibernation:** `Agachar + F`: força e resistência temporárias.
- **Towering Stature:** Alcance maior.
- **Cumbersome Claws:** Não maneja espadas nem machados.
- **Heavy Bones:** Mais lento e ataca devagar.
- **Environmental Waning:** Fica fraco longe de florestas.
- **Bulky Body:** Não usa peitorais de diamante ou netherite.
- **Large Appetite:** Fica com fome mais rápido.
- **Large Body:** Seu corpo é 10% maior.

### 🐰 Coelho (Rabbit)
- **High Carotene:** Visão noturna permanente.
- **Prey:** Menos vida, porém mais veloz.
- **Special Diet:** Só come cenouras, que alimentam o dobro.
- **Bouncing Aura:** Impulso de pulo permanente.
- **Replenish:** Acelera o crescimento de plantações próximas.
- **Small Body:** Seu corpo é 20% menor.

### 🐐 Cabra (Goat)
- **Leap:** `Agachar + F`: um salto frontal poderoso.
- **Ram:** Seus golpes com impulso (correndo) empurram fortemente o alvo e causam +2 de dano.
- **Brace:** Agachar evita o dano de queda.
- **Insulated:** Imune ao congelamento da neve em pó.
- **Fur Coat:** Fica lento em biomas quentes.
- **Small:** Possui 2 corações a menos.
- **Browser:** Carne alimenta menos você.

### 🐈 Felino (Feline)
- **Acrobatics:** Não sofre dano de queda.
- **Strong Ankles:** Pula um pouco mais alto.
- **Velvet Paws:** Passos silenciosos (cosmético).
- **Nine Lives:** Possui 1 coração a menos.
- **Weak Arms:** Não arranca pedra muito incrustada sem picareta.
- **Scare Creepers:** Creepers têm medo e não te atacam.
- **Cat Vision:** Visão noturna permanente.
- **Feline Grace:** Levemente mais rápido.
- **Predator Instinct:** Ganha velocidade extra ao perseguir criaturas hostis.
- **Small Body:** Seu corpo é 10% menor.

### 🦉 Coruja (Owl)
- **Asas de Coruja:** Elytra permanente que volta sozinha.
- **Silent Flight:** Reduz o alcance de detecção dos mobs ao planar.
- **Night Hunter:** De noite ganha Velocidade I, Visão Noturna e bônus de dano.
- **Predator Dive:** Atacar mergulhando causa dano massivo e sangramento.
- **Echolocation:** `Agachar + F`: Revela entidades próximas através das paredes.
- **Soft Landing:** Não sofre dano de queda.
- **Carnivore:** Apenas carne.
- **Day Blindness:** Fica exausto e fraco sob a luz solar direta.
- **Lightweight:** Sem armaduras pesadas.
- **Small Body:** Seu corpo é 10% menor.

### 🦅 Grifo (Gryphon)
- **Asas de Grifo:** Elytra permanente que volta sozinha.
- **Decolar:** `Agachar + F`: impulso forte para o céu e planagem.
- **Mergulho:** Dano extra ao atacar mergulhando.
- **Pouso Seguro:** Não sofre dano de queda.
- **Carnívoro:** Só consegue comer carne.
- **Ar Fresco:** Só dorme a 86 blocos de altura ou mais.
- **Mobilidade:** Não pode usar armadura pesada.
- **Large Body:** Seu corpo é 10% maior.

### 🐉 Dragão (Dragon)
- **Asas de Dragão:** Elytra permanente que volta sozinha.
- **Dragon's Breath:** `Agachar + F`: sopra um cone de fogo que causa dano e incendeia os alvos à frente.
- **Reborn Magic:** Ganha regeneração no The End.
- **Resistant Skin:** Possui 2 corações a mais.
- **Sharp Claws:** Causa +1 coração de dano corpo a corpo.
- **Apex Predator:** Só consegue comer carne.
- **Scaled Body:** Não pode usar peitorais (mas usa as asas).
- **Large Body:** Seu corpo é 10% maior.

---

## 🛠 Comandos e Permissões

| Comando | Permissão | Descrição |
|---|---|---|
| `/origin` | `originspaper.select` (Padrão: Todos) | Abre a interface de seleção de Origens (apenas se você não tiver uma, ou se for admin). |
| `/origin info [player]` | `originspaper.select` | Mostra qual é a origem atual e lista todas as suas habilidades na tela. |
| `/origin set <player> <origin>` | `originspaper.admin` | Força um jogador a se tornar uma Origem específica instantaneamente. |
| `/origin reset <player>` | `originspaper.admin` | Limpa a origem do jogador e força-o a escolher uma nova ao se conectar. |

---

## ⚙ Build e Uso

O plugin é empacotado usando o **Gradle Wrapper** interno. Não requer dependências extras instaladas na sua máquina (além do Java 25).

```bash
./gradlew build                 # compila e gera o plugin
./gradlew build --stacktrace    # compila mostrando detalhes de erros
```

O arquivo compilado `.jar` será gerado em: **`build/libs/originspaper-1.0.0.jar`**.

1. Mova este arquivo para a pasta `plugins/` do seu servidor Paper.
2. Inicie o servidor.
3. Ao entrar no servidor, você ou seus jogadores receberão o menu interativo automaticamente!
