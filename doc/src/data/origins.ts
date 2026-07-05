/**
 * Catálogo das 21 Origins jogáveis do Feather Flux.
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
    tagline: 'Sem poder nenhum, sem fraqueza nenhuma. Classe padrão do Minecraft',
    lore: 'Não tem habilidade especial, mas também não possui pontos fracos. É a escolha mais neutra e flexível.',
    pros: ['Nenhuma vantagem específica', 'Versatilidade total'],
    cons: ['Nenhum poder especial', 'Nenhuma habilidade de mobilidade'],
    progression: {
      objectives: ['Matar monstros', 'Caçar animais', 'Explorar o mundo'],
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
    tagline: 'Ágil quando submerso. Não pode ficar muito tempo fora da água.',
    lore: 'Nada mais rápido, respira embaixo d\'água e se vira bem em rios e no fundo do mar. Necessita voltar pra água de vez em quando, ou fica fraco e lento.',
    pros: [
      'Respiração anfíbia: respira tanto na água quanto na terra',
      'Escava debaixo d\'água com velocidade total',
      'Visão nítida e nado veloz na água',
      'Agilidade aquática superior',
      'Movimentação sem cooldowns debaixo d\'água',
      'Bônus de velocidade temporário quando em terra firme',
    ],
    cons: [
      'Resseca longe d\'água: sofre lentidão e fraqueza após 10 minutos em solo seco',
      'Corpo 20% menor',
    ],
    progression: {
      objectives: ['Nadar longas distâncias', 'Minerar debaixo d\'água', 'Caçar monstros na água'],
      unlocks: [
        { level: 3, name: 'Mergulhador', description: 'Velocidade de mineração subaquática aumentada.' },
        { level: 6, name: 'Fôlego Aquático', description: 'Ganha o Poder do Conduct enquanto estiver submerso.' },
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
    tagline: 'Corre rápido e pula alto. Ágil em fugas',
    lore: 'Feito pra correr: furtivo e rápido enquanto estiver em ambientes florestais.',
    pros: [
      'Agilidade da selva: mais rápido em florestas, savanas e pântanos',
      'Resistência a quedas: recebe metade do dano de queda',
      'Passos ágeis: sobe em pequenos obstáculos sem precisar pular',
      'Impulso natural para saltar mais alto',
    ],
    cons: ['Vida reduzida: 1 coração a menos'],
    progression: {
      objectives: ['Correr longas distâncias', 'Sobreviver a grandes quedas', 'Matar monstros'],
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
    tagline: 'Forte a noite e fraco de dia.',
    lore: 'Plana em cavernas e possui visão noturna. Quando o sol nasce, fica fraco e se torna uma presa fácil.',
    pros: [
      'Visão noturna permanente: enxerga perfeitamente no escuro',
      'Queda suave: plana no ar para evitar danos',
      'Mestre das cavernas: bônus de velocidade e mineração em profundezas',
    ],
    cons: [
      'Aversão ao sol: sofre fraqueza e lentidão sob a luz do dia',
      'Corpo muito frágil: 2 corações a menos',
      'Tamanho corporal 30% menor',
    ],
    progression: {
      objectives: ['Planar pelo ar', 'Minerar no escuro', 'Matar monstros à noite'],
      unlocks: [
        { level: 3, name: 'Membrana Resistente', description: '+1 coração.' },
        { level: 6, name: 'Ecolocalização', description: 'Velocidade e mineração maiores em profundezas.' },
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
    tagline: 'Pequeno, rápido e difícil de encurralar.',
    lore: 'Pequeno e ágil, consegue escapar de ataques e se esgueirar por lugares apertados.',
    pros: [
      'Evasão natural: 30% de chance de desviar de flechas e projéteis',
      'Disparada: corrida em alta velocidade (Agachar + F)',
      'Movimentação passivamente mais veloz',
      'Passos silenciosos',
    ],
    cons: [
      'Tamanho corporal 30% menor',
      'Muito frágil: 2 corações a menos',
      'Braços curtos: alcance de ação reduzido',
    ],
    progression: {
      objectives: ['Matar monstros', 'Minerar e coletar recursos', 'Desviar de projéteis'],
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
    tagline: 'Imune a fogo e lava, mas frágil na chuva e na luz do dia.',
    lore: 'Sofre fraqueza sob luz solar direta e recebe o dobro de dano de poções e magias. Mais possui imunidade a chamas, lava e blocos quentes, além de visão noturna permanente.',
    pros: [
      'Filho do Inferno: imune a fogo, lava e blocos quentes',
      'Força brutal: dano corpo a corpo aumentado (+1 coração)',
      'Visão noturna permanente',
      'Pacto Sombrio: ganha Força temporária para combate (Agachar + F)',
    ],
    cons: [
      'Queima de energia: sofre fraqueza sob luz solar direta',
      'Sofre fome e fraqueza logo após usar o Pacto Sombrio',
      'Vulnerabilidade sagrada: recebe o dobro de dano de poções e magias',
    ],
    progression: {
      objectives: ['Matar monstros', 'Matar no Nether', 'Matar à noite'],
      unlocks: [
        { level: 3, name: 'Brasa', description: '+0,5 coração de dano corpo a corpo.' },
        { level: 6, name: 'Pele de Cinzas', description: 'Regenera lentamente perto de fogo ou lava.' },
        { level: 10, name: 'Fúria Infernal', description: 'Abates concedem Força I por alguns segundos.' },
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
    tagline: 'Caça pelo faro e fica mais forte à noite e perto dos aliados.',
    lore: 'Caça em matilha e fica mais forte à noite. Tem faro apurado e consegue sentir inimigos próximos. É um predador noturno.',
    pros: [
      'Olhos caninos: enxerga perfeitamente no escuro',
      'Caçador Noturno: velocidade extra à noite e agilidade de dia',
      'Dieta fortalecedora: comer carne cura vida extra',
      'Ataques mais letais durante a noite (+1 de dano)',
      'Faro aguçado: destaca criaturas próximas',
      'Uivo do Alfa: uiva e ganha vida extra temporária (Agachar + F)',
    ],
    cons: ['Metabolismo acelerado: gasta fome mais rápido à noite'],
    progression: {
      objectives: ['Matar monstros', 'Matar à noite (bônus de XP)', 'Derrotar um chefe'],
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
    tagline: 'Anda quieta, dá saltos grandes e tem sorte achando itens bons.',
    lore: 'Furtiva e ágil, consegue se esgueirar e atacar de surpresa. Tem sorte extra ao coletar itens e consegue saltos longos.',
    pros: [
      'Bote: salto longo que causa dano em área ao atingir o chão (Agachar + F)',
      'Caçada implacável: atacar o mesmo alvo aumenta seu dano',
      'Acrobata: mais veloz, salta mais alto e não leva dano de queda (na habilidade)',
      'Pelagem grossa: imunidade contra dano de frio',
      'Gosto por frutas-silvestres: frutas vermelhas saciam o dobro de fome',
    ],
    cons: [
      'Muito vulnerável a chamas e dano de fogo',
      'Corpo pequeno e frágil: 2 corações a menos',
      'Paladar restrito: alimenta-se apenas de carnes e algumas frutas',
      'Timidez natural: seus ataques ficam fracos quando tem pouca vida',
      'Frágil demais para segurar um escudo',
      'Tamanho corporal levemente reduzido',
    ],
    progression: {
      objectives: ['Caçar animais', 'Matar monstros', 'Comer frutas'],
      unlocks: [
        { level: 3, name: 'Saltitante', description: 'Altura do pulo aumentada.' },
        { level: 6, name: 'Faro da Sorte', description: 'Mais chance de itens raros.' },
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
    tagline: 'Lento mas difícil de derrubar.',
    lore: 'Grande, forte e resistente. É lento, mas consegue aguentar muito dano e ainda causar repulsão nos inimigos.',
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
      objectives: ['Matar monstros', 'Caçar animais', 'Comer carne crua'],
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
    tagline: 'Pula alto, corre rápido e cai sem se machucar.',
    lore: 'Pula alto, corre rápido e sempre cai sem se machucar. É ágil e consegue escapar de ataques com facilidade.',
    pros: [
      'Visão noturna permanente',
      'Presa veloz: mais rápido ao correr',
      'Disparada: salto rápido para frente (Agachar + F, recarga 7s)',
      'Pernas de mola: Impulso de Salto permanente',
      'Aterrissagem leve: sem dano em quedas de até 5 blocos de altura',
      'Nutrição especial: cenouras saciam 50% a mais',
      'Faro de Predador: sente lobos, raposas e gatos próximos',
    ],
    cons: [
      'Paladar seletivo: só consegue se alimentar de cenouras',
      'Presa frágil: vida máxima reduzida',
      'Corpo 20% menor',
    ],
    progression: {
      objectives: ['Colher plantações', 'Comer cenouras', 'Saltitar pelo mundo'],
      unlocks: [
        { level: 3, name: 'Salto Alto', description: 'Altura do pulo aumentada.' },
        { level: 6, name: 'Lebre Veloz', description: 'Velocidade de corrida maior.' },
        { level: 10, name: 'Determinação', description: '+1 coração.' },
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
    lore: 'Cabeçeia inimigos e obstáculos, pula alto e sempre cai sem se machucar. É resistente ao frio e consegue comer QUASE qualquer coisa.',
    pros: [
      'Cabeçada: avança em linha reta e dá dano em área ao atingir um inimigo (Agachar + F)',
      'Investida: golpes enquanto estiver correndo causam dano extra e forte repulsão',
      'Passo firme: ignora qualquer dano de queda',
      'Salto montanhês: pula 2 blocos de altura',
      'Resistência extrema ao frio da neve em pó',
      'Estômago de aço: come QUASE qualquer item (menos ferramentas, armas e armaduras) pra matar a fome (Agachar + clique direito)',
    ],
    cons: ['Vitalidade menor: 2 corações a menos'],
    progression: {
      objectives: ['Matar monstros', 'Caçar animais', 'Acertar com a Cabeçada'],
      unlocks: [
        { level: 3, name: 'Cabeça Dura', description: 'Dano da Cabeçada aumentado.' },
        { level: 6, name: 'Impacto', description: 'Dano e repulsão da Investida maiores.' },
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
    tagline: 'Cai sempre em pé e gosta de atacar de surpresa.',
    lore: 'É um predador furtivo e brincalhão, que se esgueira silenciosamente e persegue suas presas.',
    pros: [
      'Pouso perfeito: totalmente imune a dano de queda',
      'Músculos elásticos: pequenos bônus de altura ao pular',
      'Patas de gato: anda silenciosamente',
      'Predador temido: Creepers tem medo de você',
      'Olhos de gato: enxerga perfeitamente à noite',
      'Agilidade felina: mais rápido e ágil',
    ],
    cons: [
      'Vitalidade menor: 1 coração a menos',
      'Tamanho corporal levemente menor',
    ],
    progression: {
      objectives: ['Matar monstros', 'Caçar animais', 'Perseguir presas correndo'],
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
    tagline: 'Plana silenciosamente e enxerga tudo à noite, mas fica vulnerável de dia.',
    lore: 'Plana em silêncio e enxerga tudo a noite. De dia, a luz forte atrapalha e ela se torna vulnerável.',
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
      objectives: ['Planar longas distâncias', 'Caçar à noite', 'Atacar mergulhando'],
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
    tagline: 'Metade águia, metade leão. Vive voando.',
    lore: 'Vive nos céus e ataca em mergulhos, portanto é forte, mas não suporta armaduras pesadas.',
    pros: [
      'Majestade dos céus: possui asas natas para o voo',
      'Decolagem explosiva: lança-se ao ar a qualquer momento (Agachar + F)',
    ],
    cons: [
      'Dieta estritamente carnívora',
      'Nobreza aérea: dormir em baixa altitude te deixa grogue',
      'Limitação de peso: não suporta armaduras pesadas',
      'Tamanho imponente e mais fácil de acertar',
    ],
    progression: {
      objectives: ['Voar longas distâncias', 'Atacar mergulhando', 'Matar monstros'],
      unlocks: [
        { level: 3, name: 'Vento do Norte', description: 'Impulso da Decolagem maior.' },
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
    tagline: 'Voa, cospe fogo e se cura no The End.',
    lore: 'Voa, solta fogo nos inimigos e recupera vida no End. Seu ponto fraco é o Nether, onde fica mais frágil.',
    pros: [
      'Asas dracônicas inatas para domínio aéreo',
      'Sopro de Fogo: incendeia e destrói oponentes à frente (Agachar + F)',
      'Afinidade com o Vazio: regeneração de vida no End',
      'Garras mortais: dano corpo a corpo aumentado (+1 coração)',
    ],
    cons: [
      'Fragilidade no inferno: perde vida máxima no Nether',
      'Dieta estritamente carnívora',
      'Escamas duras: incapaz de vestir qualquer tipo de peitoral',
      'Corpo colossal e mais fácil de ser atingido',
    ],
    progression: {
      objectives: ['Queimar alvos com o Sopro de Fogo', 'Voar longas distâncias', 'Matar monstros no End'],
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
    tagline: 'Frágil porém poderosa. Se cura na luz e ajuda seus amigos.',
    lore: 'É uma criatura mística que se alimenta de flores e frutas brilhantes.',
    pros: [
      'Asas trêmulas: cai de forma extremamente lenta',
      'Voo gracioso: ignora todo dano de queda ao planar pelo ar',
      'Polinizadora: agachada, faz as plantas sob seus pés crescerem mais rápido',
      'Atração curativa: regenera vida perto de luzes artificiais, desde que esteja ferida e alimentada (consome fome)',
      'Apetite luminoso: frutas-brilhantes saciam o dobro de fome e dão saturação extra',
      'Irmãs de enxame: abelhas são totalmente inofensivas para você',
    ],
    cons: [
      'Pânico no escuro: sofre grande lentidão na escuridão total',
      'Natureza delicada: vida máxima extremamente reduzida',
      'Corpo 20% menor',
      'Asas sensíveis: incapaz de vestir armaduras pesadas (Diamante ou Netherite)',
    ],
    progression: {
      objectives: ['Voar perto de flores', 'Polinizar plantações', 'Comer frutas e plantas'],
      unlocks: [
        { level: 3, name: 'Pólen Fértil', description: 'Área de polinização aumenta para 3x3.' },
        { level: 6, name: 'Corpo Resiliente', description: '+1 coração.' },
        {
          level: 10,
          name: 'Aura de Vida',
          description: 'Polinizar dá Saturação a jogadores próximos.',
        },
      ],
    },
  },
  {
    slug: 'panda-vermelho',
    name: 'Panda-Vermelho',
    emoji: '🐼',
    accent: '#fb7185',
    category: 'Acrobata das Copas',
    group: 'Mobilidade',
    difficulty: 2,
    playstyle: 'Mobilidade Florestal',
    diet: 'Onívoro',
    tagline: 'Sobe em árvore como quem sobe escada e desce planando.',
    lore: 'Pequeno acrobata das florestas. No meio das árvores ninguém alcança ele: escala troncos agachado, corre pelas copas e escapa planando com a cauda.',
    pros: [
      'Acrobata das árvores: mais veloz sobre troncos, folhas, bambu, musgo e chão de floresta',
      'Escalador: agachado encostado em troncos, folhas ou bambu, sobe devagar — e desce planando de leve',
      'Cauda Fofa: Velocidade II e Queda Lenta pra fugir ou saltar entre árvores (Agachar + F, recarga 25s)',
      'Forrageador: frutinhas e frutas-brilhantes saciam o dobro de fome',
      'Pelagem quente: imune ao congelamento da neve em pó',
    ],
    cons: [
      'Frágil: 1 coração a menos',
      'Timidez natural: seus ataques ficam fracos quando tem pouca vida',
      'Corpo leve: peitoral de Diamante ou Netherite desativa a acrobacia e a escalada',
      'Corpo 15% menor',
    ],
    progression: {
      objectives: ['Percorrer florestas e copas', 'Escalar troncos e bambus', 'Comer frutinhas'],
      unlocks: [
        { level: 3, name: 'Vitalidade Fofa', description: '+1 coração.' },
        { level: 6, name: 'Cauda Ágil', description: 'Recarga da Cauda Fofa −30%.' },
        { level: 10, name: 'Acrobata Mestre', description: 'Velocidade II sobre blocos de floresta.' },
      ],
    },
  },
  {
    slug: 'tubarao',
    name: 'Tubarão',
    emoji: '🦈',
    accent: '#22d3ee',
    category: 'Predador dos Mares',
    group: 'Aquático',
    difficulty: 3,
    playstyle: 'Combate Aquático',
    diet: 'Carnívoro',
    tagline: 'Dono da água: rápido, faminto e sempre sabe onde tem sangue.',
    lore: 'Dentro da água é o predador do topo: nada mais rápido que qualquer um, morde mais forte e sente presas feridas de longe. Em terra firme vira um peixe fora d\'água.',
    pros: [
      'Respiração aquática: respira debaixo d\'água sem limite',
      'Nadador supremo: o nado mais veloz entre todas as origens',
      'Mordida predadora: +1 coração de dano corpo a corpo dentro da água',
      'Faro de Sangue: molhado, sente criaturas feridas num raio de 16 blocos — elas brilham pra você',
      'Avanço Voraz: investida submarina que morde a primeira presa no caminho (Agachar + F na água, recarga 30s)',
    ],
    cons: [
      'Pele seca: 3 minutos longe de água ou chuva causam fraqueza; aos 5 minutos vem a lentidão',
      'Desajeitado em terra: 8% mais lento fora da água',
      'Dieta estritamente carnívora: só carne e peixe',
      'Corpo 10% maior',
    ],
    progression: {
      objectives: ['Nadar longas distâncias', 'Atacar presas na água', 'Caçar na água'],
      unlocks: [
        { level: 3, name: 'Mandíbula Feroz', description: '+0,5 coração de dano da mordida na água.' },
        { level: 6, name: 'Frenesi', description: 'Recarga do Avanço Voraz −30%.' },
        { level: 10, name: 'Terror dos Mares', description: 'Avanço mais forte e Faro de Sangue com alcance maior.' },
      ],
    },
  },
  {
    slug: 'guaxinim',
    name: 'Guaxinim',
    emoji: '🦝',
    accent: '#94a3b8',
    category: 'Catador Noturno',
    group: 'Furtivo',
    difficulty: 2,
    playstyle: 'Utilidade / Furtivo',
    diet: 'Onívoro',
    tagline: 'Come o que ninguém quer, acha o que ninguém viu e some quando a coisa aperta.',
    lore: 'Oportunista de patas rápidas: vive da noite e do que encontra vasculhando por aí. Quando a situação fica feia, some numa nuvem de fumaça.',
    pros: [
      'Catador noturno: enxerga no escuro e durante a noite',
      'Gourmet do lixo: carne podre, olho de aranha e batata venenosa não fazem mal — e ainda saciam mais',
      'Sorte de Catador: chance de achar itens simples (barbante, osso, carvão...) caçando ou cavando terreno natural',
      'Patas furtivas: agachado, monstros só percebem você de perto',
      'Esgueirada: Velocidade II e um instante de invisibilidade pra despistar (Agachar + F, recarga 20s)',
    ],
    cons: [
      'Frágil: 1 coração a menos',
      'Corpo 15% menor',
      'Armadura pesada estraga a furtividade: peitoral de Diamante ou Netherite desativa as Patas Furtivas e a invisibilidade',
      'Atacar durante a Esgueirada encerra o disfarce na hora',
    ],
    progression: {
      objectives: ['Vasculhar o mundo (achados extras)', 'Comer comidas duvidosas', 'Matar monstros à noite'],
      unlocks: [
        { level: 3, name: 'Mãos Habilidosas', description: 'Chance de achado extra aumentada.' },
        { level: 6, name: 'Pés Leves', description: 'Recarga da Esgueirada −30%.' },
        { level: 10, name: 'Mestre da Malandragem', description: 'A invisibilidade da Esgueirada dura 3x mais.' },
      ],
    },
  },
  {
    slug: 'equino',
    name: 'Equino',
    emoji: '🐴',
    accent: '#d9a066',
    category: 'Corredor dos Campos',
    group: 'Mobilidade',
    difficulty: 2,
    playstyle: 'Viagem / Campo Aberto',
    diet: 'Herbívoro',
    tagline: 'Nasceu pra galopar: quanto mais reta a estrada, mais rápido ele fica.',
    lore: 'Feito pra estrada e pro campo aberto. Pega embalo correndo e ninguém acompanha o galope em linha reta — só não espere muito dele dentro d\'água ou em corredor apertado.',
    pros: [
      'Galope: correr sem parar cria impulso — após 3s fica veloz e, com 6s ao ar livre, alcança Velocidade II',
      'Coice: dano baixo e um empurrão fortíssimo em quem estiver atrás de você (Agachar + F, recarga 25s)',
      'Pernas fortes: Impulso de Salto permanente',
      'Passo seguro: metade do dano de queda',
      'Corredor de trilha: se move melhor sobre grama, terra, caminhos e feno',
    ],
    cons: [
      'Herbívoro: não come carne nem peixe',
      'Nadador ruim: 15% mais lento na água',
      'Desajeitado em espaços apertados: fica lento sob tetos muito baixos',
      'Peso da armadura: peitoral de Diamante ou Netherite impede o Galope',
    ],
    progression: {
      objectives: ['Galopar longas distâncias', 'Acertar Coices', 'Comer vegetais e pães'],
      unlocks: [
        { level: 3, name: 'Arrancada', description: 'O Galope engata mais rápido.' },
        { level: 6, name: 'Coice Firme', description: 'Recarga do Coice −30%.' },
        { level: 10, name: 'Puro-Sangue', description: 'Velocidade III no galope a todo vapor.' },
      ],
    },
  },
  {
    slug: 'gamba',
    name: 'Gambá',
    emoji: '🦨',
    accent: '#a3e635',
    category: 'Defensor Fedorento',
    group: 'Furtivo',
    difficulty: 2,
    playstyle: 'Controle / Fuga',
    diet: 'Onívoro',
    tagline: 'Ninguém quer chegar perto. E é exatamente essa a ideia.',
    lore: 'Não caça ninguém e não procura briga: só quer distância. Quem insiste em chegar perto leva um borrifo que embaça a vista, enfraquece e dá vontade de ir embora.',
    pros: [
      'Borrifo Fedorento: nuvem sem dano que aplica Lentidão, Fraqueza e Náusea em quem estiver perto (Agachar + F, recarga 35s)',
      'Listras de aviso: agachado, monstros só percebem você de perto',
      'Dieta de catador: comidas duvidosas não envenenam e saciam mais',
      'Andarilho noturno: 8% mais rápido à noite ou em pouca luz',
    ],
    cons: [
      'Frágil: 1 coração a menos',
      'Nadador ruim: 12% mais lento na água',
      'Corpo 10% menor',
      'Armadura pesada: peitoral de Diamante ou Netherite desativa as listras e o bônus noturno',
    ],
    progression: {
      objectives: ['Atingir alvos com o Borrifo', 'Vagar pela noite', 'Comer comidas duvidosas'],
      unlocks: [
        { level: 3, name: 'Vitalidade Listrada', description: '+1 coração.' },
        { level: 6, name: 'Glândulas Rápidas', description: 'Recarga do Borrifo −30%.' },
        { level: 10, name: 'Nuvem Pestilenta', description: 'O Borrifo alcança mais longe.' },
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

/** Código de referência técnico estável: FF-ORG-001 … FF-ORG-021. */
export function originCode(o: Origin): string {
  return `FF-ORG-${String(origins.indexOf(o) + 1).padStart(3, '0')}`;
}

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
