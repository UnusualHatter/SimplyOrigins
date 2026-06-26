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
    tagline: 'Sem poder nenhum, sem fraqueza nenhuma. Faz de tudo um pouco.',
    lore: 'Não tem habilidade especial, mas também não tem ponto fraco pra ninguém explorar. É a escolha mais tranquila pra quem tá começando e quer jogar no padrão.',
    pros: ['Nenhuma vantagem específica', 'Flexibilidade total'],
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
    tagline: 'Na água você manda. Só não pode ficar muito tempo no seco.',
    lore: 'Nada rápido, respira embaixo d\'água e se vira bem em rios e no fundo do mar. Em terra seca, precisa se molhar de vez em quando ou começa a passar mal.',
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
      objectives: ['Nadar longas distâncias', 'Minerar submerso', 'Caçar monstros na água'],
      unlocks: [
        { level: 3, name: 'Mergulhadora', description: 'Velocidade de mineração subaquática aumentada.' },
        { level: 6, name: 'Fôlego Aquático', description: 'Ganha Poder do Condutor enquanto submersa.' },
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
    tagline: 'Corre muito e pula alto. Quando complica, foge na frente.',
    lore: 'Feito pra correr: dispara entre as árvores e salta longe. Mas é bicho de fugir, não de encarar de frente.',
    pros: [
      'Agilidade da selva: mais rápido em florestas, savanas e pântanos',
      'Resistência a quedas: recebe metade do dano',
      'Passos ágeis: sobe pequenos obstáculos sem precisar pular',
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
    tagline: 'Dono da noite. De dia, melhor se esconder.',
    lore: 'Plana pelas cavernas e enxerga tudo no escuro. Quando o sol nasce, fica fraco e vira presa fácil.',
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
      objectives: ['Planar pelo ar', 'Minerar no escuro', 'Matar monstros à noite'],
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
    tagline: 'Pequeno, rápido e difícil de encurralar.',
    lore: 'Se vira em qualquer canto e foge fácil. Quando você percebe, ele já sumiu (às vezes com os seus itens).',
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
    tagline: 'Anda na lava numa boa. Só não curte água nem chuva.',
    lore: 'É em casa no Nether: fogo e lava não fazem nada. O problema dele é água e chuva, que machucam de verdade.',
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
    tagline: 'Sozinho já é perigoso. Em grupo, pior ainda pro inimigo.',
    lore: 'Caça pelo faro e fica mais forte à noite e perto dos aliados. De dia e sozinho, perde um pouco da força.',
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
    tagline: 'Esperta, leve e silenciosa. Pega o que quer e some.',
    lore: 'Anda quieta, dá saltos grandes e tem sorte achando item bom. Some antes de você reagir.',
    pros: [
      'Bote: salto longo que causa dano em área ao pousar (Agachar + F)',
      'Caçada implacável: atacar o mesmo alvo aumenta seu dano',
      'Acrobata: mais veloz, salta mais alto e cai com leveza',
      'Raposice: sorte extra na coleta de itens',
      'Pelagem grossa: imunidade contra dano de frio',
      'Gosto por frutas-silvestres: amoras saciam o dobro de fome',
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
        { level: 6, name: 'Faro de Sorte', description: 'Mais chance de itens raros.' },
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
    tagline: 'Devagar pra andar, difícil de derrubar.',
    lore: 'Aguenta porrada na linha de frente e bate forte só com as mãos. Em troca, é lento e nem usa espada ou machado.',
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
    lore: 'Vive de mobilidade: pula alto, corre muito e não toma dano de queda. É movimento o tempo todo.',
    pros: [
      'Visão noturna permanente',
      'Presa veloz: mais rápido ao correr',
      'Disparada: salto rápido para frente, só mobilidade (Agachar + F, recarga 7s)',
      'Pernas de mola: Impulso de Salto permanente',
      'Aterrissagem leve: sem dano em quedas de até 5 blocos',
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
    tagline: 'Cabeçada primeiro, pergunta depois.',
    lore: 'Sobe montanha que ninguém sobe e usa a cabeçada pra empurrar o inimigo abismo abaixo. E não toma dano de queda.',
    pros: [
      'Cabeçada: avança em linha reta e explode em área ao atingir um inimigo ou ao parar (Agachar + F)',
      'Investida: golpes correndo causam dano extra e forte empurrão',
      'Passo firme: ignora qualquer dano de queda',
      'Salto montanhês: pula 2 blocos de altura sem esforço',
      'Resistência extrema ao frio da neve em pó',
      'Estômago de aço: come quase qualquer item (menos comida, ferramentas, armas e armaduras) pra matar a fome (Agachar + clique direito)',
    ],
    cons: ['Vitalidade menor: 2 corações a menos'],
    progression: {
      objectives: ['Matar monstros', 'Caçar animais', 'Acertar com a Cabeçada'],
      unlocks: [
        { level: 3, name: 'Crânio Duro', description: 'Dano da Cabeçada maior.' },
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
    tagline: 'Cai sempre de pé e gosta de atacar de surpresa.',
    lore: 'Anda sem fazer barulho, enxerga no escuro e não toma dano de queda. Ótimo pra pegar o inimigo desprevenido.',
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
    tagline: 'Enxerga tudo no escuro. De dia, fica perdida.',
    lore: 'Plana em silêncio e enxerga tudo de noite. De dia, a luz forte atrapalha e ela fica vulnerável.',
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
    lore: 'Voa livre e mergulha em cima da presa com as garras. Como é grandão, também é alvo fácil.',
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
      objectives: ['Voar longas distâncias', 'Atacar mergulhando', 'Matar monstros'],
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
    tagline: 'Voa, cospe fogo e se cura no End.',
    lore: 'Voa, solta fogo nos inimigos e recupera vida no End. O ponto fraco é o Nether, onde fica mais frágil.',
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
    tagline: 'Bem frágil, mas se cura na luz e ajuda o time.',
    lore: 'Cai devagar, planeja sem tomar dano e se cura perto de luz. É de vidro, mas faz planta crescer e dá uma força pra quem está por perto.',
    pros: [
      'Asas trêmulas: cai de forma extremamente lenta',
      'Voo gracioso: ignora todo dano de queda ao planar pelo ar',
      'Polinizadora: agachada, faz a planta sob seus pés crescer (área cresce até 3x3 com a progressão)',
      'Pó ofuscante: solta pólen que cega os monstros próximos (Agachar)',
      'Atração curativa: regenera vida perto de luz artificial, desde que esteja ferida e alimentada (consome fome)',
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

/** Código de referência técnico estável: FF-ORG-001 … FF-ORG-016. */
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
