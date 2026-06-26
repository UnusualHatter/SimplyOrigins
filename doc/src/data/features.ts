/**
 * Recursos do servidor que não são Origens nem encantamentos: comandos e
 * pequenas funcionalidades dos plugins (casa, teleporte, discos, mapas...).
 *
 * Texto em português direto. Cada recurso aponta o plugin de onde vem só como
 * referência (não precisa aparecer em destaque pro jogador).
 */

export interface FeatureCategory {
  key: string;
  label: string;
  color: string;
}

export const featureCategories: FeatureCategory[] = [
  { key: 'casa', label: 'Casa e viagem', color: '#e3b94f' },
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
}

export const features: Feature[] = [
  // Casa e viagem
  {
    slug: 'home',
    name: 'Marca "waypoints" de teletransporte.',
    category: 'casa',
    command: '/sethome · /home',
    desc: 'Marque a sua base com /sethome e volte pra ela a qualquer hora com /home. Você pode ter várias homes e pode gerenciá-las de diferentes maneiras.',
    plugin: 'EssentialsX',
  },
  {
    slug: 'tpa',
    name: 'Teleportar-se pra outros jogadores',
    category: 'casa',
    command: '/tpa <nick> · /tpaccept',
    desc: 'Peça pra se teleportar até um amigo com /tpa; o pedidos podem ser aceitos com /tpaccept. Pra chamar alguém até você, use /tpahere.',
    plugin: 'EssentialsX',
  },
  {
    slug: 'waystones',
    name: 'Viajar rápido pelo mapa',
    category: 'casa',
    desc: 'Crie pontos de viagem rápida assim como as Homes, mas que podem ser compartilhados com outros jogadores. Você pode viajar entre elas a qualquer hora.',
    plugin: 'WaystoneWarps',
  },

  // Conforto e social
  {
    slug: 'sit',
    name: 'Sentar, deitar e relaxar',
    category: 'social',
    command: '/sit · /lay · /spin',
    desc: 'Sente, deite ou GIRE em qualquer lugar! utilize o comando /sit, /lay ou /spin e aproveite a vista. Dá pra sentar em blocos, escadas, slabs, camas e até no chão.',
    plugin: 'GSit',
  },
  {
    slug: 'voicechat',
    name: 'Conversar por voz',
    category: 'social',
    desc: 'Chat de Voz por proximidade: converse com quem estiver perto de você ou até em grupos com seus amigos! Use a tecla "v" com o mod instalado para configurar.',
    note: 'Requer o mod Simple Voice Chat (veja a aba Compatibilidade).',
    plugin: 'Simple Voice Chat',
  },

  // Mundo e coleta
  {
    slug: 'graves',
    name: 'Recuperar os itens depois de morrer',
    category: 'mundo',
    desc: 'Morreu? Seus items ficam seguros em um túmulo que aparece no local da morte. Você tem 10 minutos pra voltar e pegar tudo de volta.',
    plugin: 'AxGraves',
  },
  {
    slug: 'antigrief',
    name: 'Construir livre, sem grief',
    category: 'mundo',
    desc: 'Não há sistema de claims, então sinta-se livre! No caso de griefs, o servidor registra cada alteração no mundo o que permite que a staff reverta quaisquer alterações indesejadas!',
    plugin: 'Prism',
  },
  {
    slug: 'treefeller',
    name: 'Derrubar árvores por completo',
    category: 'mundo',
    desc: 'Derrube árvores inteiras com um só golper, basta agachar enquanto quebra o tronco usando um machado!',
    plugin: 'TreeFeller',
  },
  {
    slug: 'veinminer',
    name: 'Minerar veios por completo',
    category: 'mundo',
    desc: 'Quebre todos os minérios semelhantes ao que você está mineirando com um só golper! Basta agachar enquanto quebra o bloco usando a ferramenta correta.',
    plugin: 'VeinMiner',
  },
  {
    slug: 'sleeper',
    name: 'Pular a noite dormindo',
    category: 'mundo',
    desc: 'A transição da noite para o dia acontece apenas quando 25% dos jogadores online estão dormindo.',
    plugin: 'Sleeper',
  },

  // Criação e mídia
  {
    slug: 'imageframe',
    name: 'Colocar imagens em mapas',
    category: 'criacao',
    command: '/imageframe create <nome> <link>',
    desc: 'Coloque uma imagem da internet na sua parede! Basta utilizar o comando do plugin com o link da sua imagem (Plataforma recomendada para transformas as imagens em links: https://postimages.org) e colocar os mapas em item-frames!',
    note: 'Precisa de permissão pra criar.',
    plugin: 'ImageFrame',
  },
  {
    slug: 'customdiscs',
    name: 'Ouvir discos personalizados',
    category: 'criacao',
    desc: 'O servidor oferece discos de músicas customizadas! Só a staff pode cria-los, mas todo mundo pode ouvi-los normalmente em jukeboxes (eles vêm junto nas resource packs).',
    plugin: 'URLCustomDiscs',
  },
  {
    slug: 'dynamiclight',
    name: 'Iluminar com itens na mão',
    category: 'criacao',
    desc: 'Itens que emitem luz (tochas, lanternas, glowstone, etc.) iluminam o ambiente ao redor quando segurados na mão.',
    plugin: 'DynamicLight',
  },
];

export const featureCount = features.length;

/** Código de referência técnico estável: FF-FTR-001 … */
export function featureCode(f: Feature): string {
  return `FF-FTR-${String(features.indexOf(f) + 1).padStart(3, '0')}`;
}
