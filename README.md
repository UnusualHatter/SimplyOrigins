# SimplyOrigins

Plugin Paper para **Minecraft 26.1.2** que reimplementa o sistema de *Origins* usando **apenas a API pública do Paper/Bukkit** — sem NMS, sem mixins, sem mods no cliente.

São 16 origens, uma GUI de seleção em duas telas, persistência por jogador e um único tick global que controla todos os poderes.

---

## Como funciona

- Ao entrar sem origem, abre o menu de seleção. Você não consegue fechá-lo até escolher.
- **Habilidade ativa:** `Agachar + F` (Sneak + trocar item de mão). Só algumas origens têm uma; as demais são puramente passivas.
- Não precisa de nada no cliente — roda 100% no servidor.

---

## Origens

### 🧍 Humano
Sem vantagens nem desvantagens.

### 🦦 Lontra
- **Anfíbio:** respira embaixo d'água e na terra.
- **Afinidade Aquática:** minera em velocidade normal submerso.
- **Olhos Aquáticos:** enxerga bem e ganha graça do golfinho na água.
- **Nadadeiras:** nada muito mais rápido quando submerso.
- **Como a Água:** mais controle de movimento embaixo d'água.
- **Passo Terrestre:** Velocidade I em terra firme, mesmo longe da água.
- **Dependência de Água:** fraqueza e lentidão após 10 minutos sem tocar em água ou chuva.
- **Corpo Pequeno:** 20% menor.

### 🦌 Cervo
- **Agilidade Selvagem:** mais rápido em florestas, taigas, savanas e pântanos.
- **Aterrissagem Suave:** 50% menos dano de queda.
- **Pernas Ágeis:** sobe obstáculos baixos sem pular.
- **Salto de Alerta:** pula mais alto.
- **Frágil:** 1 coração a menos.

### 🦇 Morcego
- **Visão Noturna:** permanente.
- **Asas de Morcego:** planagem (queda lenta) ao cair.
- **Mobilidade nas Cavernas:** mais rápido e mina veloz no fundo de cavernas escuras.
- **Aversão ao Sol:** exausto e fraco sob luz solar direta.
- **Frágil:** 2 corações a menos.
- **Corpo Minúsculo:** 30% menor.

### 🐀 Rato
- **Corpo Pequeno:** 30% menor.
- **Evasão:** 30% de chance de desviar de projéteis.
- **Disparada** (`Agachar + F`): corrida explosiva (Velocidade III por 1,5s, recarga de 8s).
- **Passos Silenciosos:** cosmético.
- **Veloz:** mais rápido andando e correndo.
- **Frágil:** 2 corações a menos.
- **Alcance Curto:** alcança menos longe.

### 👿 Demônio
- **Filho do Inferno:** imune a fogo, lava e chão quente.
- **Poder Infernal:** +1 coração de dano corpo a corpo.
- **Visão Noturna:** permanente.
- **Pacto Infernal** (`Agachar + F`): Força I por 10s, depois Fraqueza e Fome por 5s.
- **Aversão ao Sol:** exausto e fraco sob luz solar direta.
- **Vulnerabilidade Sagrada:** dobro de dano de magia e poções.

### 🐺 Lobo
- **Olhos Caninos:** visão noturna permanente.
- **Corredor Noturno:** mais rápido à noite, ágil de dia.
- **Metabolismo de Lobo:** gasta mais energia à noite.
- **Imunidade a Veneno.**
- **Mordida Carnívora:** comer carne cura meio coração extra.
- **Presas Noturnas:** +1 de dano à noite.
- **Sentido de Caçador:** destaca criaturas próximas.
- **Uivo do Alfa** (`Agachar + F`): ganha absorção e uiva.

### 🦊 Raposa
- **Bote** (`Agachar + F`): salto que explode em área ao aterrissar, ou causa dano extra se acertar no ar.
- **Caçada:** atacar a mesma presa repetidamente fortalece você.
- **Agilidade:** mais rápido, pula mais e cai mais leve.
- **Raposice:** mais sorte nos drops.
- **Fofo:** imune ao frio, frágil ao fogo.
- **Pequenininho:** 2 corações a menos.
- **Paladar Único:** come só carnes e algumas frutas.
- **Timidez:** fica fraco com pouca vida.
- **Fraco:** não consegue usar escudo.
- **Corpo Pequeno:** 10% menor.

### 🐻 Urso
- **Patas Poderosas:** mãos vazias causam +3 de dano e empurrão (perto da floresta).
- **Pelo Grosso:** armadura natural e imunidade ao frio.
- **Couro Resistente:** Resistência I permanente.
- **Apetite Primal:** carne crua sacia muito mais.
- **Hibernação** (`Agachar + F`): força e resistência temporárias.
- **Estatura Imponente:** maior alcance.
- **Garras Desajeitadas:** não usa espadas nem machados.
- **Ossos Pesados:** um pouco mais lento e ataca devagar.
- **Marcação Territorial:** longe de florestas por muito tempo, perde o bônus das garras (mas não perde vida).
- **Corpo Volumoso:** não usa peitorais de diamante ou netherite.
- **Grande Apetite:** fica com fome mais rápido.
- **Corpo Grande:** 10% maior.

### 🐰 Coelho
- **Rico em Caroteno:** visão noturna permanente.
- **Presa:** menos vida, mais velocidade.
- **Dieta Especial:** só come cenouras, que alimentam o dobro.
- **Aura Saltitante:** impulso de pulo permanente.
- **Reabastecimento:** acelera o crescimento de plantações próximas.
- **Corpo Pequeno:** 20% menor.

### 🐐 Cabra
- **Cabeçada** (`Agachar + F`): arremete em linha reta; explode em área ao atingir um inimigo ou ao parar.
- **Investida:** golpes correndo causam dano extra e forte empurrão.
- **Passo Firme:** nunca sofre dano de queda.
- **Salto Montanhês:** pulo permanente, salta cerca de 2 blocos de altura.
- **Isolado:** imune ao congelamento da neve em pó.
- **Pequeno:** 2 corações a menos.
- **Estômago de Cabra:** come qualquer coisa; agachar + clique no ar com um bloco na mão o mordisca (recupera meio ponto de fome).

### 🐈 Felino
- **Acrobacia:** sem dano de queda.
- **Tornozelos Fortes:** pula um pouco mais alto.
- **Patas de Veludo:** passos silenciosos (cosmético).
- **Sete Vidas:** 1 coração a menos.
- **Assusta Creepers:** creepers têm medo de você, nunca te atacam e fogem se você se aproximar.
- **Visão Felina:** noturna permanente.
- **Graça Felina:** levemente mais rápido.
- **Instinto Predador:** velocidade extra ao perseguir criaturas hostis.
- **Corpo Pequeno:** 10% menor.

### 🦉 Coruja
- **Asas de Coruja:** elytra permanente que volta sozinha.
- **Voo Silencioso:** reduz a detecção dos mobs ao planar.
- **Caçador Noturno:** à noite ganha Velocidade I, Visão Noturna e bônus de dano.
- **Ecolocalização** (`Agachar + F`): revela entidades próximas (alcance curto) através das paredes; recarga de 30s.
- **Aterrissagem Suave:** sem dano de queda.
- **Carnívoro:** só carne.
- **Cegueira Diurna:** exausto e fraco sob luz solar direta.
- **Leve:** sem armaduras pesadas.
- **Corpo Pequeno:** 10% menor.

### 🦅 Grifo
- **Asas de Grifo:** elytra permanente que volta sozinha.
- **Decolar** (`Agachar + F`): impulso forte para o céu e planagem.
- **Carnívoro:** só carne.
- **Ar Fresco:** dorme mal perto do chão (acorda grogue); só descansa bem em grandes altitudes.
- **Mobilidade:** sem armadura pesada.
- **Corpo Grande:** 10% maior.

### 🐉 Dragão
- **Asas de Dragão:** elytra permanente que volta sozinha.
- **Sopro do Dragão** (`Agachar + F`): cone de fogo que causa dano e incendeia os alvos à frente.
- **Magia Renascida:** regeneração no The End.
- **Fragilidade do Nether:** perde 2 corações de vida máxima no Nether.
- **Garras Afiadas:** +1 coração de dano corpo a corpo.
- **Predador Supremo:** só carne.
- **Corpo Escamado:** não usa peitorais (mas usa as asas).
- **Corpo Grande:** 10% maior.

### 🦋 Mariposa
Origem de suporte focada em agricultura, exploração e mobilidade. Sem combate direto.
- **Asas Trêmulas:** velocidade de queda reduzida permanentemente.
- **Voo Gracioso:** plana suavemente durante quedas e nunca sofre dano de queda.
- **Polinizadora:** agachada, faz plantações e mudas próximas crescerem como se recebessem Farinha de Osso, e o pólen cega inimigos por perto.
- **Atraída pela Luz:** Regeneração I perto de luz artificial; fica lenta na escuridão total.
- **Amiga das Abelhas:** abelhas nunca ficam agressivas com você.
- **Frágil:** 2 corações a menos.
- **Corpo Delicado:** não usa armadura de Diamante ou Netherite.

---

## Comandos e permissões

| Comando | Permissão | O que faz |
|---|---|---|
| `/origin` | `originspaper.select` (todos) | Abre o menu de seleção (se você ainda não tem origem, ou se for admin). |
| `/origin info [jogador]` | `originspaper.select` | Mostra a origem atual e lista as habilidades. |
| `/origin set <jogador> <origem>` | `originspaper.admin` | Define a origem de um jogador. |
| `/origin reset <jogador>` | `originspaper.admin` | Remove a origem e força nova escolha. |

---

## Build

Usa o Gradle Wrapper interno (precisa só de Java 25 instalado).

```bash
./gradlew build                 # compila e gera o plugin
./gradlew build --stacktrace    # compila mostrando detalhes de erros
```

O `.jar` sai em `build/libs/originspaper-1.1.0.jar`. Copie para a pasta `plugins/` de um servidor Paper 26.1.2 e inicie o servidor.
