/**
 * Catálogo das 16 Origins jogáveis do Feather Flux.
 *
 * Cada Origin alimenta tanto os cards da galeria quanto a página de detalhe
 * (`/origins/[slug]`). O campo `accent` é a cor de brilho/identidade do card;
 * `group` é usado pelos filtros da galeria.
 */

export type OriginGroup =
  | 'Versátil'
  | 'Aéreo'
  | 'Furtivo'
  | 'Combate'
  | 'Mobilidade'
  | 'Aquático'
  | 'Tanque';

export type Diet = 'Carnívoro' | 'Herbívoro' | 'Onívoro';

export interface ProgressionUnlock {
  /** Nível em que o bônus é concedido (1 a 10). */
  level: number;
  name: string;
  description: string;
}

export interface Progression {
  /** O que rende XP nessa Origem — os gatilhos são temáticos, cada Origem tem os seus. */
  objectives: string[];
  /** Bônus permanentes liberados em níveis fixos, empilhados sobre o kit base (nível 1). */
  unlocks: ProgressionUnlock[];
}

export interface Origin {
  slug: string;
  name: string;
  emoji: string;
  /** Cor de acento (brilho/glyph) — harmoniza com o fundo escuro. */
  accent: string;
  category: string;
  group: OriginGroup;
  /** 1 a 5 — quanto maior, mais punitiva/avançada. */
  difficulty: 1 | 2 | 3 | 4 | 5;
  playstyle: string;
  diet: Diet;
  /** Frase de efeito curta (usada nos cards). */
  tagline: string;
  /** Texto de ambientação (usado na página de detalhe). */
  lore: string;
  pros: string[];
  cons: string[];
  progression: Progression;
}

export const origins: Origin[] = [
  {
    slug: 'humano',
    name: 'Humano',
    emoji: '🧑',
    accent: '#9aa4b8',
    category: 'Versátil',
    group: 'Versátil',
    difficulty: 1,
    playstyle: 'Livre / Iniciante',
    diet: 'Onívoro',
    tagline: 'Sem dons sobrenaturais, sem amarras. Pura adaptação.',
    lore: 'O equilíbrio em pessoa: nada de habilidades exóticas, mas também nenhuma fraqueza para o inimigo explorar. A tela em branco perfeita para quem está chegando.',
    pros: ['Nenhuma vantagem específica', 'Flexibilidade total'],
    cons: ['Nenhum poder especial', 'Nenhuma habilidade de mobilidade'],
    progression: {
      objectives: ['Matar mobs hostis', 'Caçar animais', 'Minerar e craftar'],
      unlocks: [
        { level: 5, name: 'Vitalidade', description: '+1 coração.' },
        { level: 10, name: 'Resiliência', description: '+1 coração (total: +2).' },
      ],
    },
  },
  {
    slug: 'lontra',
    name: 'Lontra',
    emoji: '🦦',
    accent: '#38bdf8',
    category: 'Explorador Aquático',
    group: 'Aquático',
    difficulty: 2,
    playstyle: 'Exploração Aquática',
    diet: 'Onívoro',
    tagline: 'O oceano é seu quintal — contanto que você não seque.',
    lore: 'Brincalhona e veloz na água, a lontra domina rios e abismos do mar. Em terra firme, porém, precisa se manter úmida ou começa a definhar.',
    pros: [
      'Respiração anfíbia: respira tanto na água quanto na terra',
      'Escava debaixo d\'água com velocidade total',
      'Visão nítida e nado veloz na água',
      'Agilidade aquática superior',
      'Movimentação precisa submersa',
      'Bônus de velocidade passivo em terra firme',
    ],
    cons: [
      'Resseca longe d\'água: sofre lentidão e fraqueza após 10 minutos em terra seca',
      'Corpo 20% menor',
    ],
    progression: {
      objectives: ['Nadar em rios ou oceanos', 'Minerar debaixo d\'água', 'Matar peixes e criaturas aquáticas'],
      unlocks: [
        { level: 3, name: 'Mergulhadora', description: 'Velocidade de mineração subaquática aumentada.' },
        { level: 6, name: 'Fôlego Aquático', description: 'Ganha Conduit Power enquanto submersa.' },
        { level: 10, name: 'Corrente', description: 'Velocidade de nado muito maior.' },
      ],
    },
  },
  {
    slug: 'cervo',
    name: 'Cervo',
    emoji: '🦌',
    accent: '#84cc16',
    category: 'Corredor da Floresta',
    group: 'Mobilidade',
    difficulty: 2,
    playstyle: 'Mobilidade Terrestre',
    diet: 'Herbívoro',
    tagline: 'Gracioso, veloz e sempre um salto à frente do perigo.',
    lore: 'Feito para a fuga: salta riachos e dispara entre as árvores como uma flecha. Mas o instinto de presa fala alto e o pânico vem fácil.',
    pros: [
      'Agilidade da selva: mais rápido em florestas, savanas e pântanos',
      'Resistência a quedas: recebe metade do dano',
      'Passos ágeis: sobe pequenos obstáculos sem precisar pular',
      'Impulso natural para saltar mais alto',
    ],
    cons: ['Vida reduzida: 1 coração a menos'],
    progression: {
      objectives: ['Correr em floresta ou savana', 'Cair de grandes alturas e sobreviver', 'Matar mobs que te perseguem'],
      unlocks: [
        { level: 3, name: 'Passada Longa', description: 'Altura do salto aumentada.' },
        { level: 6, name: 'Cervo Veloz', description: 'Velocidade de corrida maior.' },
        { level: 10, name: 'Instinto de Fuga', description: 'Dano de queda quase zerado.' },
      ],
    },
  },
  {
    slug: 'morcego',
    name: 'Morcego',
    emoji: '🦇',
    accent: '#8b5cf6',
    category: 'Voador Noturno',
    group: 'Furtivo',
    difficulty: 4,
    playstyle: 'Furtivo Noturno',
    diet: 'Carnívoro',
    tagline: 'Senhor da noite — e refém do amanhecer.',
    lore: 'Plana entre cavernas guiado por ecolocalização, invencível na escuridão. Quando o sol nasce, vira presa em vez de predador.',
    pros: [
      'Visão noturna permanente: enxerga perfeitamente no escuro',
      'Queda suave: plana no ar para evitar danos',
      'Mestre das cavernas: bônus de velocidade e mineração nas profundezas',
    ],
    cons: [
      'Aversão ao sol: sofre fraqueza e lentidão sob a luz do dia',
      'Corpo muito frágil: 2 corações a menos',
      'Tamanho corporal 30% menor',
    ],
    progression: {
      objectives: ['Planar dentro de cavernas', 'Minerar sem fonte de luz', 'Matar mobs à noite'],
      unlocks: [
        { level: 3, name: 'Membrana Resistente', description: '+1 coração.' },
        { level: 6, name: 'Eco da Caverna', description: 'Velocidade e mineração maiores nas profundezas.' },
        { level: 10, name: 'Senhor da Noite', description: 'Velocidade extra durante a noite.' },
      ],
    },
  },
  {
    slug: 'rato',
    name: 'Rato',
    emoji: '🐀',
    accent: '#a8a29e',
    category: 'Sobrevivente Furtivo',
    group: 'Furtivo',
    difficulty: 3,
    playstyle: 'Furtivo / Sobrevivência',
    diet: 'Onívoro',
    tagline: 'Pequeno, ignorado e impossível de encurralar.',
    lore: 'Passa por frestas que ninguém mais alcança e prospera onde outros morreriam de fome. Subestime-o e ele já terá sumido com seu loot.',
    pros: [
      'Evasão natural: 30% de chance de desviar de flechas e projéteis',
      'Disparada: corrida explosiva de alta velocidade (Agachar + F)',
      'Movimentação passivamente mais veloz',
      'Passos leves e silenciosos',
    ],
    cons: [
      'Tamanho corporal 30% menor',
      'Muito frágil: 2 corações a menos',
      'Braços curtos: alcance de ataque reduzido',
    ],
    progression: {
      objectives: ['Desviar de flechas e projéteis', 'Minerar e coletar drops', 'Matar mobs'],
      unlocks: [
        { level: 3, name: 'Reflexos', description: 'Evasão: 30% → 38%.' },
        { level: 6, name: 'Fôlego de Rato', description: 'Recarga da Disparada −30%.' },
        { level: 10, name: 'Inalcançável', description: 'Evasão: 38% → 45%.' },
      ],
    },
  },
  {
    slug: 'demonio',
    name: 'Demônio',
    emoji: '😈',
    accent: '#ff5964',
    category: 'Combatente Infernal',
    group: 'Combate',
    difficulty: 4,
    playstyle: 'PvP / Nether',
    diet: 'Carnívoro',
    tagline: 'Caminha sobre lava e ri das chamas — até começar a chover.',
    lore: 'Nascido das profundezas, é uma força da natureza no Nether. Mas a água é seu veneno e a chuva, sua maldição.',
    pros: [
      'Filho do Inferno: imune a fogo, lava e blocos quentes',
      'Força infernal: dano corpo a corpo aumentado (+1 coração)',
      'Visão noturna permanente',
      'Pacto Sombrio: ganha Força temporária para combate (Agachar + F)',
    ],
    cons: [
      'Queima de energia: sofre fraqueza sob luz solar direta',
      'Sofre fome e fraqueza logo após usar o Pacto Sombrio',
      'Vulnerabilidade sagrada: recebe o dobro de dano de poções e magias',
    ],
    progression: {
      objectives: ['Matar mobs hostis', 'Matar no Nether', 'Matar mobs à noite'],
      unlocks: [
        { level: 3, name: 'Brasa', description: '+0,5 coração de dano corpo a corpo.' },
        { level: 6, name: 'Pele de Cinzas', description: 'Regenera lentamente perto de fogo ou lava.' },
        { level: 10, name: 'Fúria Infernal', description: 'Kills dão Força I por alguns segundos.' },
      ],
    },
  },
  {
    slug: 'lobo',
    name: 'Lobo',
    emoji: '🐺',
    accent: '#60a5fa',
    category: 'Caçador de Matilha',
    group: 'Combate',
    difficulty: 3,
    playstyle: 'Combate em Matilha',
    diet: 'Carnívoro',
    tagline: 'Sozinho, perigoso. Em matilha, implacável.',
    lore: 'Rastreia presas pelo faro e fica mais forte cercado dos seus. A solidão, porém, apaga parte da sua fúria.',
    pros: [
      'Olhos caninos: enxerga perfeitamente no escuro',
      'Caçador Noturno: velocidade extra à noite e agilidade de dia',
      'Totalmente imune a venenos',
      'Dieta fortalecedora: comer carne cura vida extra',
      'Ataques mais letais durante a noite (+1 de dano)',
      'Faro aguçado: destaca criaturas próximas',
      'Uivo do Alfa: uiva e ganha vida extra temporária (Agachar + F)',
    ],
    cons: ['Metabolismo acelerado: gasta fome mais rápido à noite'],
    progression: {
      objectives: ['Matar mobs hostis', 'Matar mobs à noite (XP dobrado)', 'Matar um boss'],
      unlocks: [
        { level: 3, name: 'Presas Afiadas', description: '+1 de dano à noite.' },
        { level: 6, name: 'Líder', description: 'Recarga do Uivo do Alfa reduzida.' },
        {
          level: 10,
          name: 'Alcateia',
          description: 'Força I permanente à noite. O uivo também fortalece aliados próximos.',
        },
      ],
    },
  },
  {
    slug: 'raposa',
    name: 'Raposa',
    emoji: '🦊',
    accent: '#fb923c',
    category: 'Trapaceira Ágil',
    group: 'Furtivo',
    difficulty: 3,
    playstyle: 'Furtivo / Ágil',
    diet: 'Onívoro',
    tagline: 'Esperta demais para ser pega, leve demais para fazer barulho.',
    lore: 'Mestre da furtividade e do oportunismo, encontra tesouros onde outros só veem mato — e some antes que percebam.',
    pros: [
      'Bote: salto longo que causa dano em área ao pousar (Agachar + F)',
      'Caçada implacável: atacar o mesmo alvo aumenta seu dano',
      'Acrobata: mais veloz, salta mais alto e cai com leveza',
      'Raposice: sorte extra na coleta de itens',
      'Pelagem grossa: imunidade contra dano de frio',
    ],
    cons: [
      'Muito vulnerável a chamas e dano de fogo',
      'Corpo pequeno e frágil: 2 corações a menos',
      'Paladar restrito: alimenta-se apenas de carnes e frutas doces',
      'Timidez natural: seus ataques ficam fracos quando tem pouca vida',
      'Frágil demais para segurar um escudo',
      'Tamanho corporal levemente reduzido',
    ],
    progression: {
      objectives: ['Matar animais', 'Matar mobs hostis', 'Comer frutas doces'],
      unlocks: [
        { level: 3, name: 'Saltitante', description: 'Altura do pulo aumentada.' },
        { level: 6, name: 'Faro de Sorte', description: 'Mais chance de drops raros.' },
        { level: 10, name: 'Bote Mortal', description: 'Dano do Bote (no ar e ao pousar) aumentado.' },
      ],
    },
  },
  {
    slug: 'urso',
    name: 'Urso',
    emoji: '🐻',
    accent: '#d97706',
    category: 'Muralha Viva',
    group: 'Tanque',
    difficulty: 2,
    playstyle: 'Tanque / Linha de Frente',
    diet: 'Onívoro',
    tagline: 'Lento para se mover, impossível de derrubar.',
    lore: 'Uma montanha de músculos que segura a linha de frente sem piscar e se recupera hibernando entre as batalhas.',
    pros: [
      'Patas pesadas: golpes desarmados causam repulsão e muito dano',
      'Couro espesso: armadura passiva e resistência total ao frio',
      'Físico inabalável: ganha Resistência permanente',
      'Apetite selvagem: carne crua alimenta muito mais',
      'Fúria da Hibernação: bônus intenso de força e defesa (Agachar + F)',
      'Estatura imponente: braços longos dão alcance extra',
    ],
    cons: [
      'Garras desajeitadas: incapaz de empunhar espadas ou machados',
      'Movimentação e velocidade de ataque reduzidas',
      'Dependência territorial: perde a força bruta longe de florestas',
      'Corpo largo: não consegue vestir peitorais de Diamante ou Netherite',
      'Metabolismo gigante: a fome esgota mais rápido',
      'Tamanho colossal e alvo fácil',
    ],
    progression: {
      objectives: ['Matar mobs hostis', 'Matar animais', 'Comer carne crua'],
      unlocks: [
        { level: 3, name: 'Patas Brutais', description: '+1 de dano com as mãos vazias.' },
        { level: 6, name: 'Couro Espesso', description: 'Tenacidade de armadura aumentada.' },
        { level: 10, name: 'Vigor do Urso', description: '+2 corações.' },
      ],
    },
  },
  {
    slug: 'coelho',
    name: 'Coelho',
    emoji: '🐇',
    accent: '#f472b6',
    category: 'Saltador Veloz',
    group: 'Mobilidade',
    difficulty: 3,
    playstyle: 'Mobilidade Extrema',
    diet: 'Herbívoro',
    tagline: 'Salta mais alto, corre mais rápido, foge primeiro.',
    lore: 'Vive da agilidade pura: nunca se machuca ao cair, mas o menor susto o faz disparar sem olhar para trás.',
    pros: [
      'Visão excelente em ambientes escuros',
      'Extremamente veloz ao correr',
      'Nutrição especial: cenouras recuperam o dobro da fome',
      'Pernas de mola: salta sempre mais alto',
      'Aura vital: faz plantações próximas crescerem passivamente',
    ],
    cons: [
      'Paladar seletivo: só consegue se alimentar de cenouras',
      'Presa frágil: vida máxima reduzida',
      'Corpo 20% menor',
    ],
    progression: {
      objectives: ['Colher plantações', 'Comer cenouras', 'Pular em distâncias longas'],
      unlocks: [
        { level: 3, name: 'Pulo Alto', description: 'Altura do pulo aumentada.' },
        { level: 6, name: 'Lebre Veloz', description: 'Velocidade de corrida maior.' },
        { level: 10, name: 'Fertilidade', description: '+1 coração.' },
      ],
    },
  },
  {
    slug: 'cabra',
    name: 'Cabra',
    emoji: '🐐',
    accent: '#2dd4bf',
    category: 'Aríete da Montanha',
    group: 'Combate',
    difficulty: 3,
    playstyle: 'Montanhês / Investida',
    diet: 'Onívoro',
    tagline: 'Cabeçada primeiro, perguntas depois.',
    lore: 'Escala penhascos impossíveis e arremessa inimigos abismo abaixo com uma investida brutal. Recuar não está no vocabulário dela.',
    pros: [
      'Investida Brutal: avança com tudo e causa impacto em área (Agachar + F)',
      'Chifrada em disparada: ataques correndo lançam o inimigo longe',
      'Passo firme: ignora qualquer dano de queda',
      'Salto montanhês: pula 2 blocos de altura sem esforço',
      'Resistência extrema ao frio da neve em pó',
      'Estômago de aço: pode morder blocos do mundo para matar a fome',
    ],
    cons: ['Vitalidade menor: 2 corações a menos'],
    progression: {
      objectives: ['Matar mobs hostis', 'Matar animais', 'Acertar com a Cabeçada'],
      unlocks: [
        { level: 3, name: 'Crânio Duro', description: 'Dano da Cabeçada maior.' },
        { level: 6, name: 'Impacto', description: 'Dano e knockback da Investida maiores.' },
        { level: 10, name: 'Avalanche', description: 'Área da explosão da Cabeçada maior.' },
      ],
    },
  },
  {
    slug: 'felino',
    name: 'Felino',
    emoji: '🐱',
    accent: '#fbbf24',
    category: 'Predador Silencioso',
    group: 'Furtivo',
    difficulty: 3,
    playstyle: 'Ágil / Furtivo',
    diet: 'Carnívoro',
    tagline: 'Cai sempre de pé, ataca sempre por trás.',
    lore: 'Reflexos sobrenaturais e passos mudos fazem do felino um caçador perfeito nas sombras — quando não se distrai, claro.',
    pros: [
      'Pouso perfeito: totalmente imune a dano de queda',
      'Músculos elásticos: pequenos bônus de altura no pulo',
      'Caminhar discreto e inaudível',
      'Predador temido: Creepers fogem e não atacam você',
      'Olhos de gato: enxerga perfeitamente à noite',
      'Caminhada graciosa e ligeiramente mais rápida',
      'Instinto de caça: corre mais rápido ao perseguir monstros',
    ],
    cons: [
      'Sete vidas, mas corpo frágil: 1 coração a menos',
      'Tamanho corporal levemente menor',
    ],
    progression: {
      objectives: ['Matar mobs hostis', 'Matar animais', 'Matar mobs em perseguição'],
      unlocks: [
        { level: 3, name: 'Caçador Ágil', description: 'Mais velocidade ao perseguir um alvo.' },
        { level: 6, name: 'Reflexos Felinos', description: 'Velocidade de ataque aumentada.' },
        { level: 10, name: 'Bote Felino', description: 'Acertos têm chance de crítico extra.' },
      ],
    },
  },
  {
    slug: 'coruja',
    name: 'Coruja',
    emoji: '🦉',
    accent: '#818cf8',
    category: 'Sentinela Noturna',
    group: 'Aéreo',
    difficulty: 4,
    playstyle: 'Reconhecimento Noturno',
    diet: 'Carnívoro',
    tagline: 'Vê tudo no escuro. Cega de tanto sol.',
    lore: 'Plana em silêncio absoluto e enxerga o que a noite esconde. À luz do dia, no entanto, fica ofuscada e indefesa.',
    pros: [
      'Asas de Coruja: voo livre e natural que retorna automaticamente',
      'Voo fantasma: reduz detecção dos monstros ao planar',
      'Senhorita da Noite: ganha velocidade, visão e força na escuridão',
      'Visão de Rapina: revela criaturas próximas (Agachar + F)',
      'Aterrissagem elegante e sem dano de queda',
    ],
    cons: [
      'Dieta estritamente carnívora',
      'Cegueira diurna: luz do sol causa fraqueza e exaustão',
      'Corpo leve demais para suportar armaduras pesadas',
      'Tamanho corporal levemente menor',
    ],
    progression: {
      objectives: ['Voar longas distâncias', 'Matar mobs à noite', 'Atacar em mergulho'],
      unlocks: [
        { level: 3, name: 'Olhar Noturno', description: 'Dano extra à noite aumentado.' },
        { level: 6, name: 'Sonar Apurado', description: 'Recarga da Visão de Rapina reduzida.' },
        { level: 10, name: 'Predador Aéreo', description: 'Ataques em mergulho causam mais dano.' },
      ],
    },
  },
  {
    slug: 'grifo',
    name: 'Grifo',
    emoji: '🦅',
    accent: '#e3b94f',
    category: 'Combatente Aéreo',
    group: 'Combate',
    difficulty: 4,
    playstyle: 'Combate Aéreo',
    diet: 'Carnívoro',
    tagline: 'Metade águia, metade leão, dono dos céus.',
    lore: 'Rei dos ares: domina o voo livre e despenca sobre as presas com garras afiadas. Grande, orgulhoso e impossível de ignorar.',
    pros: [
      'Majestade dos céus: possui asas inatas para planagem infinita',
      'Decolagem explosiva: lança-se ao ar a qualquer momento (Agachar + F)',
    ],
    cons: [
      'Dieta estritamente carnívora',
      'Nobreza aérea: dormir em baixa altitude te deixa grogue',
      'Limitação de peso: não suporta armaduras pesadas',
      'Tamanho imponente e mais fácil de acertar',
    ],
    progression: {
      objectives: ['Voar longas distâncias', 'Atacar em mergulho', 'Matar mobs hostis'],
      unlocks: [
        { level: 3, name: 'Decolagem Forte', description: 'Impulso da Decolagem maior.' },
        { level: 6, name: 'Caça-Picada', description: 'Dano em mergulho maior.' },
        { level: 10, name: 'Senhor dos Céus', description: 'Recarga da Decolagem reduzida.' },
      ],
    },
  },
  {
    slug: 'dragao',
    name: 'Dragão',
    emoji: '🐉',
    accent: '#34d399',
    category: 'Combatente Aéreo',
    group: 'Combate',
    difficulty: 4,
    playstyle: 'PvP / Exploração',
    diet: 'Carnívoro',
    tagline: 'O ápice predador — chamas, voo e a fúria do End.',
    lore: 'Soberano absoluto dos céus: cospe fogo, voa livremente e se regenera nas terras do End. Só o calor do Nether consegue contê-lo.',
    pros: [
      'Asas dracônicas inatas para domínio aéreo',
      'Sopro de Fogo: incendeia e destrói oponentes à frente (Agachar + F)',
      'Afinidade com o Vazio: regeneração de vida no The End',
      'Garras mortais: dano corpo a corpo aumentado (+1 coração)',
    ],
    cons: [
      'Fragilidade no inferno: perde vida máxima no Nether',
      'Dieta estritamente carnívora',
      'Escamas duras: incapaz de vestir qualquer tipo de peitoral',
      'Corpo colossal e mais fácil de ser atingido',
    ],
    progression: {
      objectives: ['Queimar mobs com o Sopro de Fogo', 'Voar longas distâncias', 'Matar mobs no The End'],
      unlocks: [
        { level: 3, name: 'Chama Longa', description: 'Alcance do Sopro maior.' },
        { level: 6, name: 'Brasa Eterna', description: 'Recarga do Sopro reduzida.' },
        { level: 10, name: 'Cataclismo', description: 'Alvos queimam por mais tempo.' },
      ],
    },
  },
  {
    slug: 'mariposa',
    name: 'Mariposa',
    emoji: '🦋',
    accent: '#c4b5fd',
    category: 'Espírito da Luz',
    group: 'Aéreo',
    difficulty: 4,
    playstyle: 'Suporte Místico',
    diet: 'Herbívoro',
    tagline: 'Frágil como pó, atraída pela luz que a cura e a condena.',
    lore: 'Etérea e luminosa, recupera-se sob qualquer fonte de luz — e simplesmente não resiste ao chamado dela. Beleza e fragilidade em asas.',
    pros: [
      'Asas trêmulas: cai de forma extremamente lenta',
      'Voo gracioso: ignora todo dano de queda ao planar pelo ar',
      'Polinizadora mágica: faz plantações e mudas crescerem (Agachar)',
      'Pó ofuscante: solta pólen que cega os inimigos próximos (Agachar)',
      'Atração curativa: regenera vida passivamente perto de fontes de luz',
      'Irmãs de enxame: abelhas são totalmente inofensivas para você',
    ],
    cons: [
      'Pânico no escuro: sofre grande lentidão na escuridão total',
      'Natureza delicada: vida máxima extremamente reduzida',
      'Asas sensíveis: incapaz de vestir armaduras pesadas (Diamante ou Netherite)',
    ],
    progression: {
      objectives: ['Voar perto de flores e plantas', 'Usar Polinizar em plantações', 'Usar Pó Ofuscante em mobs'],
      unlocks: [
        { level: 3, name: 'Pólen Fértil', description: 'Raio de polinização maior.' },
        { level: 6, name: 'Corpo Resiliente', description: '+1 coração.' },
        {
          level: 10,
          name: 'Aura de Vida',
          description: 'Polinizar dá Saturação a jogadores próximos.',
        },
      ],
    },
  },
];

/** Ordem dos filtros na galeria. */
export const originGroups: OriginGroup[] = [
  'Versátil',
  'Aéreo',
  'Furtivo',
  'Combate',
  'Mobilidade',
  'Aquático',
  'Tanque',
];

export function getOrigin(slug: string): Origin | undefined {
  return origins.find((o) => o.slug === slug);
}

/** Retorna a Origin anterior e a próxima (circular) para navegação. */
export function getAdjacentOrigins(slug: string): { prev: Origin; next: Origin } {
  const i = origins.findIndex((o) => o.slug === slug);
  const prev = origins[(i - 1 + origins.length) % origins.length];
  const next = origins[(i + 1) % origins.length];
  return { prev, next };
}
