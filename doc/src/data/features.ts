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
    name: 'Marcar casas e voltar quando quiser',
    category: 'casa',
    command: '/sethome · /home',
    desc: 'Marque a sua base com /sethome e volte pra ela a qualquer hora com /home. Dá pra ter mais de uma casa, é só dar um nome pra cada.',
    plugin: 'EssentialsX',
  },
  {
    slug: 'tpa',
    name: 'Teleportar até outros jogadores',
    category: 'casa',
    command: '/tpa <nick> · /tpaccept',
    desc: 'Peça pra se teleportar até um amigo com /tpa; ele aceita com /tpaccept. Pra chamar alguém até você, use /tpahere.',
    plugin: 'EssentialsX',
  },
  {
    slug: 'waystones',
    name: 'Viajar rápido pelo mapa',
    category: 'casa',
    desc: 'Olhe pra uma Lodestone e crie um ponto de viagem. Depois é só abrir o menu de pontos e viajar pelo mapa num instante.',
    plugin: 'WaystoneWarps',
  },

  // Conforto e social
  {
    slug: 'sit',
    name: 'Sentar, deitar e relaxar',
    category: 'social',
    command: '/sit · /lay · /spin',
    desc: 'Sente no chão ou em blocos, deite e até gire no lugar. Bom pra descansar e pras fotos.',
    plugin: 'GSit',
  },
  {
    slug: 'voicechat',
    name: 'Conversar por voz',
    category: 'social',
    desc: 'Voz por proximidade: quanto mais perto, mais alto. Dá pra criar grupos de voz pra falar só com quem você quiser.',
    note: 'Precisa do mod Simple Voice Chat (veja a aba Compatibilidade).',
    plugin: 'Simple Voice Chat',
  },
  {
    slug: 'tablist',
    name: 'Ver quem está online (Tab)',
    category: 'social',
    desc: 'Segure a tecla Tab pra ver todo mundo que está online, com a vida em corações, o rank, o ping e o TPS do servidor.',
    plugin: 'TAB',
  },

  // Mundo e coleta
  {
    slug: 'graves',
    name: 'Recuperar os itens depois de morrer',
    category: 'mundo',
    desc: 'Morreu? Seus itens ficam guardados numa lápide no lugar da morte. Volte lá pra pegar tudo de volta antes que ela suma.',
    plugin: 'AxGraves',
  },
  {
    slug: 'antigrief',
    name: 'Construir livre, sem grief',
    category: 'mundo',
    desc: 'Não tem claim: você constrói onde quiser. Em troca, tudo que acontece no mundo é registrado e a staff reverte griefs. Pode construir tranquilo.',
    plugin: 'Prism',
  },
  {
    slug: 'treefeller',
    name: 'Derrubar árvores por completo',
    category: 'mundo',
    desc: 'Corte o tronco com um machado e a árvore cai por completo, de uma vez. Junta madeira rápido, sem ficar caçando bloco.',
    plugin: 'TreeFeller',
  },
  {
    slug: 'veinminer',
    name: 'Minerar veios por completo',
    category: 'mundo',
    desc: 'Quebrou um minério agachado (segurando Shift)? O veio todo vem junto. Solte o Shift pra minerar normal.',
    plugin: 'VeinMiner',
  },
  {
    slug: 'sleeper',
    name: 'Pular a noite dormindo',
    category: 'mundo',
    desc: 'A noite passa mesmo sem todo mundo ir dormir. Basta uma parte dos jogadores estar na cama.',
    plugin: 'Sleeper',
  },

  // Criação e mídia
  {
    slug: 'imageframe',
    name: 'Colocar imagens em mapas',
    category: 'criacao',
    command: '/imageframe create <nome> <link>',
    desc: 'Coloque uma imagem da internet num mapa: mire num quadro (item frame) e use o comando com o link da imagem.',
    note: 'Precisa de permissão pra criar.',
    plugin: 'ImageFrame',
  },
  {
    slug: 'customdiscs',
    name: 'Ouvir discos personalizados',
    category: 'criacao',
    desc: 'O servidor tem discos com músicas customizadas. Só a staff cria, mas todo mundo ouve normal na jukebox (eles vêm junto no resource pack).',
    plugin: 'URLCustomDiscs',
  },
  {
    slug: 'dynamiclight',
    name: 'Iluminar com itens na mão',
    category: 'criacao',
    desc: 'Segurar tocha, lanterna ou qualquer item que brilha ilumina ao seu redor enquanto você anda, sem precisar largar no chão.',
    plugin: 'DynamicLight',
  },
];

export const featureCount = features.length;

/** Código de referência técnico estável: FF-FTR-001 … */
export function featureCode(f: Feature): string {
  return `FF-FTR-${String(features.indexOf(f) + 1).padStart(3, '0')}`;
}
