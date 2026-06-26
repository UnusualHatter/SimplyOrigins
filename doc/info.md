# 📋 Documentação Técnica Completa — Site do Feather Flux

> Documento gerado em 26/06/2026. Cobre **toda** a estrutura, arquitetura, design system,
> rotas, dados e comportamento do site de documentação do servidor Minecraft **Feather Flux**
> (nome de código do projeto: `SimplyOrigins`).

---

## 1. Visão Geral

| Item | Valor |
|---|---|
| **Framework** | [Astro](https://astro.build/) v6.4.6 |
| **Linguagem** | TypeScript (strict mode) |
| **Estilização** | Vanilla CSS (scoped por componente + `global.css`) |
| **Fontes** | Google Fonts — `Special Elite` (títulos) + `Cutive Mono` (corpo) |
| **Domínio de produção** | `https://play.featherflux.com` |
| **Nome do pacote** | `featherflux-doc` |
| **Build tool** | Astro CLI (`astro build` → output estático em `dist/`) |
| **Node scripts** | `dev`, `build`, `preview`, `astro` |
| **TypeScript config** | Herda de `astro/tsconfigs/strict` |

O site é **100% estático** (SSG). Não há server-side rendering em runtime — tudo é pré-renderizado no build.

---

## 2. Estrutura de Diretórios

```
doc/
├── .astro/                  # Cache interno do Astro (gerado)
├── dist/                    # Output do build (estático)
├── node_modules/
├── public/
│   ├── logo.svg             # Favicon e OG image (SVG 512×512, ícone de pena Lucide)
│   └── encantamentos.html   # Redirect legado → /wiki/encantamentos
├── src/
│   ├── components/          # 6 componentes Astro
│   │   ├── Footer.astro
│   │   ├── Header.astro
│   │   ├── Icon.astro
│   │   ├── OriginCard.astro
│   │   ├── Stars.astro
│   │   └── WikiTabs.astro
│   ├── data/                # 6 módulos TypeScript (fonte de verdade dos dados)
│   │   ├── changelog.ts
│   │   ├── compat.ts
│   │   ├── enchantments.ts
│   │   ├── features.ts
│   │   ├── origins.ts
│   │   └── site.ts
│   ├── layouts/
│   │   └── Base.astro       # Layout raiz (HTML shell, SEO, OG, fontes)
│   ├── pages/               # File-based routing do Astro
│   │   ├── index.astro      # Landing page (/)
│   │   ├── changelog.astro  # /changelog
│   │   ├── 404.astro        # Página de erro
│   │   ├── origins/
│   │   │   └── [slug].astro # Páginas dinâmicas de cada origem (/origins/:slug)
│   │   └── wiki/
│   │       ├── index.astro            # Hub da Wiki (/wiki)
│   │       ├── origens/index.astro    # Galeria de origens (/wiki/origens)
│   │       ├── encantamentos/index.astro  # Lista de encantamentos (/wiki/encantamentos)
│   │       ├── recursos/index.astro       # Recursos do servidor (/wiki/recursos)
│   │       └── compatibilidade/index.astro # Compat de mods (/wiki/compatibilidade)
│   └── styles/
│       └── global.css       # Design tokens, reset, utilitários globais
├── astro.config.mjs
├── tsconfig.json
├── package.json
└── package-lock.json
```

---

## 3. Configuração do Astro (`astro.config.mjs`)

```js
import { defineConfig } from 'astro/config';
export default defineConfig({
  site: 'https://play.featherflux.com',
  redirects: {
    '/origins': '/wiki/origens',  // Mantém links antigos da galeria vivos
  },
});
```

- **`site`**: Define a URL canônica usada pelo layout para gerar `<link rel="canonical">` e URLs absolutas de OG.
- **`redirects`**: A antiga rota `/origins` redireciona para `/wiki/origens`.

---

## 4. Mapa de Rotas

| Rota | Arquivo | Tipo | Descrição |
|---|---|---|---|
| `/` | `pages/index.astro` | Estática | Landing page — hero, IPs, features, preview de origens, CTA |
| `/changelog` | `pages/changelog.astro` | Estática | Histórico de versões do plugin |
| `/wiki` | `pages/wiki/index.astro` | Estática | Hub da Wiki — 4 cards linkando as seções |
| `/wiki/origens` | `pages/wiki/origens/index.astro` | Estática | Galeria com busca e filtros por grupo |
| `/wiki/encantamentos` | `pages/wiki/encantamentos/index.astro` | Estática | Lista com busca e filtros por categoria |
| `/wiki/recursos` | `pages/wiki/recursos/index.astro` | Estática | Comandos e funcionalidades do servidor |
| `/wiki/compatibilidade` | `pages/wiki/compatibilidade/index.astro` | Estática | Mods client-side suportados |
| `/origins/[slug]` | `pages/origins/[slug].astro` | Dinâmica (SSG) | Página de detalhe de cada origem (16 páginas) |
| `/404` | `pages/404.astro` | Estática | Página de erro personalizada |
| `/encantamentos` | `public/encantamentos.html` | Redirect | `<meta http-equiv="refresh">` → `/wiki/encantamentos` |
| `/origins` → `/wiki/origens` | `astro.config.mjs` | Redirect | Config do Astro |

### Geração dinâmica (`[slug].astro`)

Usa `getStaticPaths()` para gerar 16 páginas no build:

```ts
export function getStaticPaths() {
  return origins.map((o) => ({ params: { slug: o.slug }, props: { origin: o } }));
}
```

Slugs: `humano`, `lontra`, `cervo`, `morcego`, `rato`, `demonio`, `lobo`, `raposa`, `urso`, `coelho`, `cabra`, `felino`, `coruja`, `grifo`, `dragao`, `mariposa`.

---

## 5. Design System (`global.css`)

### 5.1 Tokens (CSS Custom Properties)

```css
:root {
  /* Backgrounds */
  --bg: #0b0e16;
  --bg-2: #0d111b;
  --surface: #121826;

  /* Texto */
  --text: #e7ebf3;
  --text-dim: #aab2c5;
  --text-faint: #7c869c;

  /* Cores de destaque */
  --gold: #e3b94f;
  --gold-bright: #f5d27a;
  --green: #6ee787;
  --coral: #ff6b75;

  /* Glass e bordas */
  --border: rgba(255, 255, 255, 0.08);
  --glass: rgba(255, 255, 255, 0.035);
  --glass-strong: rgba(255, 255, 255, 0.07);

  /* Raios e transição */
  --radius: 16px;
  --radius-sm: 10px;
  --t: 0.2s ease;

  /* Tipografia */
  --font-display: 'Special Elite', 'Courier New', Courier, monospace;
  --font-body: 'Cutive Mono', 'Courier New', Courier, monospace;

  /* Layout */
  --maxw: 1120px;
}
```

### 5.2 Background do Body

Triple radial gradient fixo:
- Gradiente dourado (80% 50%) no topo central
- Gradiente verde (60% 40%) no canto superior direito
- Cor sólida `--bg` como base

### 5.3 Utilitários Globais

| Classe | Função |
|---|---|
| `.container` | Max-width `1120px`, padding responsivo com `clamp()` |
| `.card` | Glass background + borda + border-radius |
| `.chip` | Pill badge com borda e fundo translúcido |
| `.btn` / `.btn-gold` / `.btn-outline` / `.btn-lg` | Sistema de botões com gradiente dourado e hover lift |
| `.reveal` / `.is-visible` | Animação de entrada (fade + translateY) via IntersectionObserver |
| `.is-hidden` | `display: none !important` — usado pelos filtros da Wiki |
| `.skip-link` | Acessibilidade — pula para `#main` |
| `.eyebrow` | Label de seção (uppercase, dourado, com ícone) |
| `.section-title` | Título de seção responsivo com `clamp()` |
| `.section-sub` | Subtítulo de seção, cor `--text-dim`, max 60ch |

### 5.4 Acessibilidade

- **Skip link** para `#main` (visível só no foco)
- **`prefers-reduced-motion`**: desliga animações e scroll suave
- **`aria-label`** em ícones, navegação e elementos interativos
- **`lang="pt-BR"`** no `<html>`

---

## 6. Layout Base (`Base.astro`)

### Props

```ts
interface Props {
  title?: string;
  description?: string;
  image?: string;
}
```

### SEO e Social

- **Title**: `{title} · Feather Flux` ou fallback `Feather Flux · Origins SMP Java e Bedrock`
- **Canonical URL**: construída a partir de `Astro.site` (produção) ou `Astro.url.origin` (dev)
- **Open Graph**: `og:type`, `og:site_name`, `og:title`, `og:description`, `og:url`, `og:image`, `og:locale` (pt_BR)
- **Twitter Card**: `summary_large_image`
- **Theme color**: `#0d111b`
- **Favicon**: `/logo.svg`

### Reveal on Scroll

Script embutido no layout usando `IntersectionObserver`:
- Threshold: `0.12`
- rootMargin: `0px 0px -8% 0px`
- Adiciona `.is-visible` e faz `unobserve`
- Fallback sem IO: adiciona a classe em todos os `.reveal`

---

## 7. Componentes

### 7.1 `Header.astro`

- **Sticky** no topo com `backdrop-filter: blur(12px)` e fundo semi-transparente
- Logo: ícone de pena (SVG inline via `Icon`) + nome "Feather Flux"
- Navegação: 4 links (`Início`, `Wiki`, `Changelog`, `Jogar`)
- Botão CTA "Entrar" → link para Discord
- **Active state**: lógica que detecta se a URL começa com `/wiki` ou `/origins`
- **Responsivo**: nav esconde em `≤560px`

### 7.2 `Footer.astro`

- Logo + tagline + navegação (4 links) + copyright dinâmico (`new Date().getFullYear()`)
- "Feito com ❤️ para a comunidade"
- Disclaimer: "Não afiliado à Mojang ou Microsoft"

### 7.3 `Icon.astro`

Sistema de ícones SVG inline (estilo Lucide, stroke-based):

```ts
interface Props { name: string; size?: number; }
```

**20 ícones disponíveis**: `arrow-left`, `arrow-right`, `check`, `x`, `gauge`, `flame`, `sparkles`, `play`, `download`, `menu`, `copy`, `server`, `shield`, `feather`, `book`, `heart`, `layers`, `search`, `wand`.

Fallback para `sparkles` se o nome não existir.

### 7.4 `OriginCard.astro`

Card de uma origem com:
- **CSS custom property `--c`**: cor de acento da origem (afeta glow, borda, chip)
- **Glow**: pseudo-elemento com `radial-gradient` baseado na cor de acento
- **Conteúdo**: emoji, chip de grupo, nome, tagline, dieta, dificuldade (Stars)
- **`data-group`**: usado pelos filtros JS da Wiki
- **`data-search`**: texto pesquisável concatenado (nome, grupo, poderes, etc.)
- **Link**: `<a href="/origins/{slug}">`

### 7.5 `Stars.astro`

Indicador de dificuldade 1–5 com estrelas SVG (preenchidas/vazias):

```ts
interface Props { value: number; max?: number; }
```

Cor: `--gold` para preenchidas, `--text-faint` com opacidade para vazias.

### 7.6 `WikiTabs.astro`

Barra de abas para navegar entre as 4 seções da Wiki:
- `origens` → `/wiki/origens`
- `encantamentos` → `/wiki/encantamentos`
- `recursos` → `/wiki/recursos`
- `compatibilidade` → `/wiki/compatibilidade`

Aba ativa: gradiente dourado com `box-shadow`.

---

## 8. Módulos de Dados (`src/data/`)

Toda a informação do site vive em arquivos TypeScript exportados. **Não há CMS, banco de dados nem markdown** — os dados são hardcoded e tipados.

### 8.1 `site.ts` — Identidade global

```ts
export const site = {
  name: 'Feather Flux',
  tagline: 'Origins SMP para Java e Bedrock',
  description: '...',
  connect: {
    java: { main: 'feathersmp.playit.plus', secondary: 'star-jade.sa.mcjoin.link' },
    bedrock: { ip: '198.22.204.31', port: 1038 },
  },
  discord: 'https://discord.gg/AWFKUEJ5QS',
  nav: [ /* 4 itens */ ],
};
```

### 8.2 `origins.ts` — 16 origens jogáveis

**Tipos exportados**: `OriginGroup`, `Diet`, `ProgressionUnlock`, `Progression`, `Origin`.

Cada `Origin` contém:

| Campo | Tipo | Descrição |
|---|---|---|
| `slug` | `string` | Identificador URL-safe |
| `name` | `string` | Nome em português |
| `emoji` | `string` | Emoji representativo |
| `accent` | `string` | Cor hex de identidade visual |
| `category` | `string` | Rótulo da categoria |
| `group` | `OriginGroup` | Grupo para filtro (7 grupos) |
| `difficulty` | `1-5` | Nível de dificuldade |
| `playstyle` | `string` | Estilo de jogo |
| `diet` | `Diet` | Carnívoro / Herbívoro / Onívoro |
| `tagline` | `string` | Frase curta para cards |
| `lore` | `string` | Texto de ambientação |
| `pros` | `string[]` | Lista de vantagens |
| `cons` | `string[]` | Lista de desvantagens |
| `progression` | `Progression` | Objetivos de XP + unlocks nos níveis 3, 6, 10 |

**7 grupos**: Versátil, Aéreo, Furtivo, Combate, Mobilidade, Aquático, Tanque.

**Funções utilitárias**:
- `getOrigin(slug)` — busca por slug
- `getAdjacentOrigins(slug)` — retorna anterior/próxima (navegação circular)

### 8.3 `enchantments.ts` — 80+ encantamentos personalizados

**Tipos**: `EnchantSource`, `EnchantSourceMeta`, `EnchantCategory`, `Enchantment`.

Cada encantamento tem: `slug`, `name` (PT), `original` (EN, nome no jogo), `category`, `maxLevel`, `items`, `sources[]`, `desc`, `serve`.

**7 categorias**: Combate corpo a corpo, Arcos/bestas/tridentes, Defesa/armadura, Mineração/ferramentas, Pesca, Utilidade/mobilidade, Maldições.

**5 fontes de obtenção**: mesa, bibliotecário (vilão), baús, tesouro, especial (sp).

Total exportado: `enchantmentCount` (contagem dinâmica).

### 8.4 `features.ts` — 15 recursos do servidor

**Tipos**: `FeatureCategory`, `Feature`.

**4 categorias**: Casa e viagem, Conforto e social, Mundo e coleta, Criação e mídia.

Cada feature tem: `slug`, `name`, `category`, `command?`, `desc`, `note?`, `plugin?`.

**Plugins referenciados**: EssentialsX, WaystoneWarps, GSit, Simple Voice Chat, TAB, AxGraves, Prism, TreeFeller, VeinMiner, Sleeper, ImageFrame, URLCustomDiscs, DynamicLight.

### 8.5 `compat.ts` — 7 itens de compatibilidade

**Tipos**: `CompatCategory`, `CompatTone`, `CompatItem`.

**3 categorias**: Mods suportados de propósito, De onde dá pra entrar, Mods e pack em geral.

**Tons de status**: `ok` (verde), `rec` (dourado), `info` (azul).

Itens: CPM, Voxy, Simple Voice Chat, Bedrock (Geyser+Floodgate), ViaVersion+ViaBackwards, Mods de performance, Resource pack do servidor.

### 8.6 `changelog.ts` — Histórico de versões

**Tipo**: `ChangelogEntry` com `version`, `date` (ISO), `added?[]`, `changed?[]`, `fixed?[]`, `summary?`.

Versões documentadas: v1.2.3, v1.2.2, v1.2.1, v1.2.0.

O campo `summary` é usado no OG description; se ausente, é gerado automaticamente das listas.

---

## 9. Páginas em Detalhe

### 9.1 Landing Page (`/`)

**Seções** (em ordem):

1. **Hero** — chip com tagline, título com gradiente dourado ("Escolha sua Origem"), lead text, 2 CTAs
2. **Status/IP** — card com grid 2 colunas (Java: 2 IPs, Bedrock: IP + porta), botões "Copiar" com feedback JS
3. **Features** — grid 3 colunas, 3 cards (16 origens, progressão, Java+Bedrock)
4. **Origins Preview** — grid 3 colunas, primeiras 6 origens (`origins.slice(0, 6)`)
5. **Download/CTA** — card centralizado com IPs inline e link pro Discord

**JavaScript client-side**:
- `copy-btn`: usa `navigator.clipboard.writeText()`, feedback visual "Copiado!" por 1.6s

**Responsivo**: `≤820px` → 2 colunas; `≤560px` → 1 coluna.

### 9.2 Changelog (`/changelog`)

- Intro centralizada com link para encantamentos
- Lista de `ChangelogEntry` renderizada como cards
- Cada entrada: versão (dourado) + data formatada (`pt-BR`, dia-mês longo-ano)
- Seções coloridas: Adicionado (verde), Modificado (dourado), Corrigido (coral)
- OG description: usa `summary` do último changelog ou gera automaticamente

### 9.3 Wiki Hub (`/wiki`)

Grid 2×2 de cards-link para as 4 seções:
- Cada card mostra: ícone, título, contagem dinâmica (ex: "16 origens jogáveis"), descrição
- Efeito de glow com gradiente radial baseado na cor de acento
- Hover: lift + borda colorida + box-shadow

### 9.4 Wiki — Origens (`/wiki/origens`)

- **Barra sticky** (abaixo do header, `top: 64px`) com:
  - Input de busca (filtra por `data-search`)
  - Chips de filtro por grupo (7 grupos + "Todas")
- **Grid 4 colunas** (3 em ≤1040px, 2 em ≤760px, 1 em ≤460px)
- **Busca client-side**: combina filtro de grupo + texto, toggle `.is-hidden`
- Mensagem "Nenhuma origem encontrada" quando tudo está escondido

### 9.5 Wiki — Encantamentos (`/wiki/encantamentos`)

- Mesma barra sticky com busca + chips por categoria (7 categorias com contagem)
- Agrupados por seção (`wiki-section`), cada uma com header colorido
- Grid `auto-fill, minmax(300px, 1fr)`
- Cada card mostra: nome PT, nome original EN, nível máximo, descrição, "serve para", itens aplicáveis, selos de fonte coloridos
- Busca filtra por nome, original, desc, serve, items
- Nota no rodapé: "Plugin ExcellentEnchants"

### 9.6 Wiki — Recursos (`/wiki/recursos`)

- Sem busca (lista menor)
- Agrupados por 4 categorias com header colorido
- Cards com borda esquerda colorida (`border-left: 3px solid`)
- Mostra: nome, comando (se houver, em `<code>` dourado), descrição, nota, plugin de origem

### 9.7 Wiki — Compatibilidade (`/wiki/compatibilidade`)

- Sem busca
- 3 categorias, grid `auto-fill, minmax(330px, 1fr)`
- Cards com selo de status colorido por tom (`ok`/`rec`/`info`)
- Nota no rodapé com link para a seção de IPs

### 9.8 Detalhe de Origem (`/origins/[slug]`)

Página mais complexa do site. Seções:

1. **Back link** → `/wiki/origens`
2. **Hero**: emoji grande (92-128px) com glow, chip de grupo, nome (clamp 2.3–3.6rem), tagline
3. **Glow de fundo**: gradiente radial 900px baseado na cor de acento
4. **Metadata row**: grid 4 colunas — Categoria, Dificuldade (Stars + label), Estilo, Dieta
5. **Vantagens/Desvantagens**: grid 2 colunas de cards (verde/coral com ícones check/x)
6. **Progressão**: bloco com intro + grid 2 colunas (objetivos de XP + recompensas por nível com chips "Nv X")
7. **Lore**: card com texto de ambientação
8. **CTAs**: "Jogar como X" + "Como baixar o jogo"
9. **Navegação prev/next**: grid 2 colunas, navegação circular entre as 16 origens

**Labels de dificuldade**: `['', 'Iniciante', 'Tranquila', 'Intermediária', 'Avançada', 'Hardcore']`

**Responsivo**: ≤720px → grids viram 2 ou 1 coluna; ≤480px → hero centralizado em coluna.

### 9.9 Página 404

- Código "404" com gradiente dourado (clamp 4–8rem)
- Título: "Esta trilha não leva a lugar nenhum"
- 2 CTAs: "Voltar ao início" + "Ver Origins"

---

## 10. Assets Públicos

### `logo.svg`

SVG 512×512 com:
- Retângulo arredondado (rx=112) fundo `#0d111b`
- Borda interna com stroke dourado `#e3b94f` (opacity 0.35)
- Ícone de pena (Lucide) centralizado, scaled 10.6×, stroke com `linearGradient` dourado

### `encantamentos.html`

Redirect triplo (meta refresh + JS + canonical) de `/encantamentos` → `/wiki/encantamentos`. Mantém links antigos que possam existir (Discord, bookmarks).

---

## 11. JavaScript Client-Side

O site é quase todo estático. O JS existente é minimal e inline:

| Funcionalidade | Localização | Mecanismo |
|---|---|---|
| **Reveal on scroll** | `Base.astro` | `IntersectionObserver` com threshold 0.12 |
| **Copy IP** | `index.astro` | `navigator.clipboard.writeText()` + feedback visual |
| **Busca + filtro de origens** | `wiki/origens/index.astro` | Filtro por `data-group` + busca textual em `data-search` |
| **Busca + filtro de encantamentos** | `wiki/encantamentos/index.astro` | Filtro por `data-section` + busca textual em `data-search` |

Nenhum framework JS (React, Vue, etc.) é usado. Todo JS é vanilla, inline nos componentes Astro.

---

## 12. Técnicas de CSS Notáveis

| Técnica | Uso |
|---|---|
| **`clamp()`** | Tipografia e padding responsivos sem media queries |
| **`color-mix(in srgb, ...)`** | Cores semi-transparentes derivadas de variáveis CSS |
| **`backdrop-filter: blur()`** | Header e barra de ferramentas sticky com glassmorphism |
| **CSS custom property `--c`** | Cor de acento por origem/categoria, propagada via `style` inline |
| **`text-wrap: balance`** | Títulos com quebras de linha equilibradas |
| **Radial gradients** | Glows decorativos em cards e backgrounds |
| **Scoped styles** | Cada `.astro` tem `<style>` isolado pelo Astro compiler |

---

## 13. Dados do Servidor (Conexão)

| Edição | Tipo | Endereço |
|---|---|---|
| **Java** | IP principal | `feathersmp.playit.plus` |
| **Java** | IP secundário | `star-jade.sa.mcjoin.link` |
| **Bedrock** | IP | `198.22.204.31` |
| **Bedrock** | Porta | `1038` |
| **Discord** | Convite | `https://discord.gg/AWFKUEJ5QS` |

---

## 14. Build e Deploy

```bash
# Desenvolvimento local
npm run dev        # astro dev (hot reload)

# Build de produção
npm run build      # astro build → dist/

# Preview do build
npm run preview    # astro preview (serve dist/ localmente)
```

O output é uma pasta `dist/` com HTML estático, CSS e JS mínimos. Pode ser hospedado em qualquer servidor estático (Vercel, Netlify, Cloudflare Pages, Nginx, etc.).

---

## 15. Relação com o Plugin (Projeto Pai)

O diretório `doc/` é um subprojeto dentro do repositório `SimplyOrigins`, que é um **plugin Paper para Minecraft 26.1.2** escrito em Kotlin (build com Gradle). O site documenta o plugin mas é independente dele:

- O plugin gera o `.jar` em `build/libs/`
- O site vive em `doc/` e é um projeto Node.js separado
- Os dados das origens no site (`origins.ts`) devem ser mantidos em sincronia manual com a implementação do plugin em `src/`

---

## 16. Resumo de Contagens

| Conteúdo | Quantidade |
|---|---|
| Origens jogáveis | 16 |
| Grupos de origem | 7 |
| Encantamentos personalizados | 80+ |
| Categorias de encantamento | 7 |
| Recursos/features do servidor | 15 |
| Itens de compatibilidade | 7 |
| Versões no changelog | 4 |
| Componentes Astro | 6 |
| Páginas estáticas | 9 (+ 16 dinâmicas) |
| Ícones SVG inline | 20 |
| Total de páginas geradas | ~25 |
