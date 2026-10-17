# SimplyOrigins

Plugin Paper para **Minecraft 26.1.2** que reimplementa o sistema de *Origins* usando **apenas a API pública do Paper/Bukkit** — sem NMS, sem mixins, sem mods no cliente.

São 21 origens, uma GUI de seleção em duas telas, persistência por jogador e um único tick global que controla todos os poderes.

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
- **Soco Dilacerante** (nível 5+): um golpe agachado faz o alvo sangrar — meio coração por segundo durante 5s, atravessando armadura (recarga de 12s).

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
- **Presa:** menos vida, porém mais veloz.
- **Dieta Especial:** só come cenouras, que alimentam 50% a mais.
- **Aura Saltitante:** impulso de pulo permanente.
- **Aterrissagem Leve:** sem dano em quedas de até 5 blocos.
- **Disparada** (`Agachar + F`): salto rápido para frente, só para movimentação (recarga de 7s).
- **Faro de Predador:** sente lobos, raposas e gatos próximos e os destaca, dando tempo de fugir.
- **Corpo Pequeno:** 20% menor.

### 🐐 Cabra
- **Cabeçada** (`Agachar + F`): arremete em linha reta; explode em área ao atingir um inimigo ou ao parar e arremessa quem estiver perto (o alvo atingido de frente voa mais longe).
- **Passo Firme:** nunca sofre dano de queda.
- **Salto Montanhês:** pulo permanente, salta cerca de 2 blocos de altura.
- **Isolado:** imune ao congelamento da neve em pó.
- **Pequeno:** 2 corações a menos.
- **Estômago de Cabra:** além da comida normal, mordisca quase qualquer item — agachado, clique direito no ar olhando para a frente (recupera meio ponto de fome). Ferramentas, armas, armaduras e itens de usar não são comidos.

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

### 🐾 Panda-Vermelho
- **Escalador:** agachado encostado numa parede, escala como uma aranha até 4 blocos de altura (5 no nível 10).
- **Cauda Fofa** (`Agachar + F`): Regeneração por 5s para você e jogadores próximos (recarga de 25s).
- **Forrageador:** agachado, come bambu e ovos com clique direito no ar; frutas silvestres saciam o dobro.
- **Pelagem Quente:** imune a congelamento.
- **Frágil e Tímido:** 1 coração a menos e Fraqueza I com pouca vida.
- **Corpo Leve:** peitorais de diamante/netherite desativam a escalada.

### 🦈 Tubarão
- **Respiração Aquática:** respira embaixo d'água.
- **Nadador Supremo:** grande bônus de velocidade submerso.
- **Mordida Predadora:** +1 coração de dano corpo a corpo enquanto estiver na água.
- **Faro de Sangue:** quando molhado, revela criaturas feridas próximas sem expor jogadores protegidos.
- **Avanço Voraz** (`Agachar + F`, somente na água): investida que morde a primeira presa válida e a deixa sangrando por 4s (recarga de 30s).
- **Pele Seca:** após 3 minutos seco recebe Fraqueza; após 5, também Lentidão. Água ou chuva reinicia o tempo.
- **Desajeitado em Terra:** 8% mais lento fora d'água.
- **Carnívoro:** só pode comer carne.

### 🦝 Guaxinim
- **Catador Noturno:** visão noturna em pouca luz e à noite.
- **Gourmet do Lixo:** comidas duvidosas não envenenam e dão saturação extra.
- **Sorte de Catador:** pequena chance de achar itens simples ao caçar ou vasculhar terreno natural; sem itens valiosos, blocos colocados ou áreas protegidas.
- **Ladrãozinho:** chance de uma compra com villagers sair de graça (os ingredientes voltam pro bolso).
- **Patas Furtivas:** agachado, monstros só percebem você de perto.
- **Esgueirada** (`Agachar + F`): Velocidade II por 3s e invisibilidade breve; atacar encerra o efeito (recarga de 20s).
- **Frágil:** 1 coração a menos.
- **Sem Armadura Pesada:** peitoral de diamante/netherite desativa a furtividade.

### 🐎 Cavalo
- **Galope:** boost de velocidade ao correr — com 6s de corrida em campo aberto alcança Velocidade II.
- **Pernas Fortes:** Impulso de Salto permanente.
- **Corredor de Trilha:** pequeno bônus sobre grama, terra, caminhos, plantações e feno.
- **Coice** (`Agachar + F`): pouco dano e forte empurrão em cone atrás do jogador (recarga de 25s).
- **Herbívoro:** não come carne ou peixe.
- **Nadador Ruim:** 15% mais lento na água.
- **Desajeitado em Espaços Apertados:** leve penalidade sob tetos baixos.
- **Peso da Armadura:** peitoral de diamante/netherite impede o Galope.

### 🦨 Gambá
- **Listras de Aviso:** agachado, monstros só percebem você de perto.
- **Spray Fedorento** (`Agachar + F`): spray sem dano que causa Lentidão, Fraqueza e Náusea breve; jogadores recebem durações menores (recarga de 35s).
- **Dieta de Catador:** comidas duvidosas são seguras e dão saturação extra.
- **Andarilho Noturno:** 8% mais rápido à noite ou em pouca luz.
- **Frágil:** 1 coração a menos.
- **Nadador Ruim:** 12% mais lento na água.
- **Sem Armadura Pesada:** peitoral de diamante/netherite desativa as listras e o bônus noturno.

---

## Comandos e permissões

| Comando | Permissão | O que faz |
|---|---|---|
| `/origin` | `originspaper.select` (todos) | Abre o menu de seleção (se você ainda não tem origem, ou se for admin). |
| `/origin info [jogador]` | `originspaper.select` | Mostra a origem atual e lista as habilidades. |
| `/origin set <jogador> <origem>` | `originspaper.admin` | Define a origem de um jogador. |
| `/origin reset <jogador>` | `originspaper.admin` | Remove a origem e força nova escolha. |
| `/origin reload` | `originspaper.admin` | Recarrega config, origens e integrações. |
| `/origin addxp <jogador> <qtd>` | `originspaper.admin` | Concede XP de progressão. |
| `/origin setlevel <jogador> <nível>` | `originspaper.admin` | Define o nível da origem atual. |
| `/origin debug [perf\|integrations\|player <jogador>]` | `originspaper.admin` | Estado do plugin: tick global, contadores de desempenho, status das integrações e detalhes por jogador. |

---

## Integrações

Todas as integrações são **opcionais** (softdepend): o plugin inicia e funciona normalmente sem
nenhum outro plugin instalado. Cada hook é detectado no enable (e no `/origin reload`) e pode ser
desligado individualmente em `config.yml` → `integrations.<hook>.enabled: false`. O status de cada
um aparece em `/origin debug integrations`.

| Plugin | O que a integração faz |
|---|---|
| **AxGraves** | Remove as asas protegidas das origens (coruja, grifo, dragão) do baú-túmulo no momento em que ele aparece — evita duplicação, já que as asas voltam sozinhas no respawn. |
| **GriefPrevention** | Habilidades em área e efeitos hostis respeitam claims: Bote, Cabeçada, Sopro do Dragão, Ecolocalização, Uivo do Alfa, Polinizadora, Avanço Voraz/Faro de Sangue, Cauda Fofa, Sorte de Catador, Coice e Spray Fedorento filtram alvo a alvo — a habilidade continua funcionando, apenas ignora alvos protegidos. Mobs hostis nunca são protegidos, e a proteção de pets/mobs nomeados (`SafeTargetUtil`) vale sempre, com ou sem GriefPrevention. Em caso de erro na consulta, a proteção falha fechada (nega a ação). |
| **Simple Voice Chat** | Apenas detecção + valores de configuração por enquanto (raio do uivo, ping de ecolocalização, abafamento de spray) — nenhum poder usa voz ainda. O nome registrado do plugin é `voicechat`. |
| **PlaceholderAPI** | Apenas detecção — expansão de placeholders planejada. |
| **WorldGuard** | Apenas detecção — checagem de flags de região planejada. |
| **LuckPerms** | Apenas detecção — recursos por permissão planejados. |

---

## Build

Usa o Gradle Wrapper interno (precisa só de Java 25 instalado).

```bash
./gradlew build                 # compila e gera o plugin
./gradlew build --stacktrace    # compila mostrando detalhes de erros
```

O `.jar` sai em `build/libs/originspaper-<versão>.jar` (ex.: `originspaper-1.4.0.jar`). Copie para a pasta `plugins/` de um servidor Paper 26.1.2 e inicie o servidor.
