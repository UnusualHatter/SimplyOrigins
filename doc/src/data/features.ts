/**
 * Recursos do servidor que não são Origens nem encantamentos: comandos e
 * pequenas funcionalidades dos plugins (casa, teleporte, discos, mapas...).
 *
 * Texto em português direto. Cada recurso aponta o plugin de onde vem só como
 * referência (não precisa aparecer em destaque pro jogador).
 */

import { site } from './site';

export interface FeatureCategory {
  key: string;
  label: string;
  color: string;
}

export const featureCategories: FeatureCategory[] = [
  { key: 'casa', label: 'Homes e viagens', color: '#e3b94f' },
  { key: 'protecao', label: 'Claims e proteção', color: '#f28c5e' },
  { key: 'social', label: 'Conforto e social', color: '#5fd3e0' },
  { key: 'mundo', label: 'Mundo e coleta', color: '#6ee787' },
  { key: 'criacao', label: 'Criação e mídia', color: '#c9a0ff' },
];

export interface Feature {
  slug: string;
  name: string;
  category: string;
  /** Comando(s) principal(is), se houver. */
  command?: string;
  desc: string;
  /** Observação curta (permissão, detalhe). */
  note?: string;
  /** Plugin de origem (referência). */
  plugin?: string;
  /** Tutorial relacionado, quando houver. */
  tutorial?: string;
  /** Link externo (ex.: Discord). */
  link?: { label: string; url: string };
}

export const features: Feature[] = [
  // Casa e viagem
  {
    slug: 'home',
    name: 'Salvar e usar homes',
    category: 'casa',
    command: '/sethome · /home',
    desc: 'Pontos pessoais de retorno para bases, construções e outros locais importantes.',
    plugin: 'EssentialsX',
    tutorial: 'homes',
  },
  {
    slug: 'tpa',
    name: 'Usar TPA com outros jogadores',
    category: 'casa',
    command: '/tpa <nick> · /tpaccept',
    desc: 'Pedidos de TPA com confirmação para ir até outra pessoa ou trazê-la até você.',
    plugin: 'EssentialsX',
    tutorial: 'teleporte',
  },
  {
    slug: 'waystones',
    name: 'Viajar mais rápido pelo mapa',
    category: 'casa',
    desc: 'Crie sua waystone com uma lodestone sobre pedra lisa e viaje com uma bússola entre as que você já descobriu.',
    note: 'Até 3 waystones por jogador. Viagem gratuita, com 5 segundos de preparo e 30 de recarga.',
    plugin: 'WaystoneWarps',
    tutorial: 'waystones',
  },

  // Proteção e terrenos
  {
    slug: 'claims',
    name: 'Proteger construções com claims',
    category: 'protecao',
    command: '/trust · /untrust · /trustlist · /abandonclaim',
    desc: 'Claims impedem alterações sem permissão e podem liberar acesso para pessoas de confiança.',
    note: 'Ferramenta de inspeção: graveto (stick). Ferramenta de criação: pá de ouro.',
    plugin: 'GriefPrevention',
    tutorial: 'claims',
  },
  {
    slug: 'antigrief',
    name: 'Registro e reversão de griefs',
    category: 'protecao',
    desc: 'Histórico de blocos, containers, explosões e outras alterações que permite investigar e restaurar áreas.',
    note: 'A equipe atende relatos pelo Discord.',
    link: { label: 'Discord do servidor', url: site.discord },
    plugin: 'CoreProtect',
    tutorial: 'grief',
  },

  // Conforto e social
  {
    slug: 'sit',
    name: 'Sentar, deitar e relaxar',
    category: 'social',
    command: '/sit · /lay · /spin',
    desc: 'Poses visuais para sentar, deitar, girar e rastejar em diferentes lugares.',
    plugin: 'GSit',
    tutorial: 'poses',
  },
  {
    slug: 'voicechat',
    name: 'Simple Voice Chat',
    category: 'social',
    desc: 'Áudio por proximidade e grupos privados por meio do Simple Voice Chat.',
    note: 'Requer o Simple Voice Chat no Minecraft Java.',
    plugin: 'Simple Voice Chat',
    tutorial: 'voz',
  },

  // Mundo e coleta
  {
    slug: 'graves',
    name: 'Recuperar itens com graves',
    category: 'mundo',
    desc: 'O AxGraves cria uma grave com seus itens e seu XP no local da morte. Ela não desaparece: fica lá até você buscar.',
    plugin: 'AxGraves',
    tutorial: 'lapides',
  },
  {
    slug: 'treefeller',
    name: 'Derrubar árvores por completo',
    category: 'mundo',
    desc: 'Coleta do tronco e das partes conectadas de uma árvore em uma única ação.',
    plugin: 'TreeFeller',
    tutorial: 'coleta',
  },
  {
    slug: 'veinminer',
    name: 'Minerar veios por completo',
    category: 'mundo',
    desc: 'Coleta dos minérios conectados de um veio usando a ferramenta adequada. Com o addon de atalho instalado no seu jogo, isso passa a valer para qualquer bloco: pedra, terra, areia, madeira e o que mais estiver conectado.',
    note: 'Sem o addon, funciona só em minérios. Até 100 blocos por veio, agachado e com a ferramenta certa.',
    plugin: 'VeinMiner',
    tutorial: 'coleta',
  },
  {
    slug: 'sleeper',
    name: 'Pular a noite sem precisar esperar outros jogadores',
    category: 'mundo',
    desc: 'A transição da noite para o dia acontece apenas quando 25% dos jogadores online estão dormindo.',
    plugin: 'Sleeper',
  },

  // Criação e mídia
  {
    slug: 'imageframe',
    name: 'Colocar imagens da internet em mapas',
    category: 'criacao',
    command: '/imageframe create <nome> <link>',
    desc: 'Imagens da internet transformadas em mapas para exibir com item frames.',
    note: 'A criação depende de permissão.',
    plugin: 'ImageFrame',
    tutorial: 'imagens',
  },
  {
    slug: 'customdiscs',
    name: 'Discos de música personalizados',
    category: 'criacao',
    command: '/disco <link> · /disco enviar · /disco meus · /disco pegar <nº>',
    desc: 'Qualquer jogador cria discos com músicas do YouTube, do Spotify, do SoundCloud ou com um arquivo de áudio. O disco toca em qualquer jukebox e todo mundo ouve, porque o som vem no pacote de recursos do servidor.',
    note: 'Até 5 discos por semana por jogador. Dá para pegar de novo os discos que você criou.',
    plugin: 'FeatherDiscos',
    tutorial: 'discos',
  },
  {
    slug: 'dynamiclight',
    name: 'Iluminação Dinâmica',
    category: 'criacao',
    desc: 'Itens que emitem luz (tochas, lanternas, glowstone, etc.) iluminam o ambiente ao redor quando segurados na mão.',
    plugin: 'DynamicLight',
  },

  // Privacidade (mantido no fim para preservar os códigos dos recursos existentes)
  {
    slug: 'blocker',
    name: 'Bloquear jogadores e controlar interações',
    category: 'social',
    command: '/block · /unblock <jogador>',
    desc: 'Controle individual de chat, voz, TPA, visibilidade, sons, partículas e outras interações.',
    note: 'As preferências são imediatas e permanecem salvas.',
    plugin: 'UniversalBlock',
    tutorial: 'bloqueio',
  },
];

export const featureCount = features.length;

/** Código de referência técnico estável: FF-FTR-001 … */
export function featureCode(f: Feature): string {
  return `FF-FTR-${String(features.indexOf(f) + 1).padStart(3, '0')}`;
}
