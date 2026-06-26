/**
 * Histórico técnico de mudanças do plugin. Entradas em ordem da mais recente para a mais antiga.
 * Mantenha objetivo e direto: o que foi adicionado, alterado e corrigido.
 */
export interface ChangelogEntry {
  version: string;
  /** Data no formato ISO (YYYY-MM-DD). */
  date: string;
  added?: string[];
  changed?: string[];
  fixed?: string[];
  /**
   * Resumo curto usado no preview de redes sociais (ex.: Discord), que corta
   * descrições longas (~350 caracteres). Se ausente, é gerado das listas acima.
   */
  summary?: string;
}

export const changelog: ChangelogEntry[] = [
  {
    version: '1.2.3',
    date: '2026-06-24',
    summary: [
      'v1.2.3 (24/06):',
      '• +80 encantamentos novos (mesa, vilões e baús)',
      '• Tablist com vida, rank, ping e TPS',
      '• Anti-grief: tudo é registrado e revertido',
      '• Grupos no chat de voz para todos',
      '• Claims removido: mundo livre',
      '• Alterna-PvP removido',
      '• VeinMiner só funciona agachado',
      '• Locator Bar desativada',
    ].join('\n'),
    added: [
      'Mais de 80 novos encantamentos: encontrados na mesa de encantamento, com bibliotecários e em baús gerados pelo mundo (incluindo baús que reabastecem).',
      'Nova tablist (tecla Tab): mostra a vida de cada jogador em corações, o rank, contagem de online/staff, ping e TPS.',
      'Anti-grief: todas as alterações no mundo passam a ser registradas. Griefs podem ser revertidos pela staff.',
      'Jogadores comuns agora podem criar e usar grupos no chat de voz.',
    ],
    changed: [
      'O sistema de claims (proteção de terreno) foi removido: o mundo agora é livre pra construir. Em troca, tudo é registrado e griefs são revertidos.',
      'O alterna-PvP foi removido; as regras de PvP do servidor voltam ao padrão.',
      'VeinMiner: agora só minera o veio inteiro quando o jogador está agachado (Shift).',
    ],
    fixed: [
      'Desativada a "Locator Bar" (a barra/bússola de direção dos jogadores que aparecia acima da hotbar).',
    ],
  },
  {
    version: '1.2.2',
    date: '2026-06-22',
    changed: [
      'Coelho e Cabra: ataques com Lança durante os dashes/investidas (Disparada do Coelho; Cabeçada e Investida da Cabra) agora causam no máximo 8 de dano (4 corações). Acaba o combo Lança + dash que derrubava um jogador num único acerto. As outras armas seguem iguais.',
    ],
  },
  {
    version: '1.2.1',
    date: '2026-06-21',
    fixed: [
      'Mariposa: o objetivo "Comer frutas e plantas" só rende XP com alimentos vegetais. Carne e bebidas (leite, poções, garrafas de água) não contam mais, fechando um farm de progressão de XP.',
    ],
  },
  {
    version: '1.2.0',
    date: '2026-06-21',
    added: [
      'Coelho: habilidade ativa Disparada (Agachar + F): impulso direcional sem dano, recarga de 7 segundos.',
      'Coelho: imunidade a dano de queda em quedas de até 5 blocos.',
      'Mariposa: redução de 20% no tamanho do corpo (atributo de escala).',
      'Mariposa: frutas-brilhantes saciam o dobro de fome e concedem saturação extra ao serem consumidas.',
      'Raposa: frutas-silvestres saciam o dobro de fome.',
      'Seleção de origem: o jogador é impedido de se mover até escolher uma origem (pode apenas olhar ao redor).',
      'Progressão: a barra de progresso (boss bar) agora é permanente na tela e aparece ao escolher a origem.',
    ],
    changed: [
      'Mariposa: a cura por luz artificial agora exige vida abaixo do máximo e fome igual ou maior que 7, e passa a consumir fome. Fim da cura passiva infinita.',
      'Mariposa: a área de polinização é centrada no bloco do jogador, iniciando em 1x1 e expandindo para 3x3 no nível 3; XP por ciclo de polinização aumentado de 2 para 4.',
      'Mariposa: o objetivo "Voar perto de flores" só concede XP quando a jogadora está no ar próxima a flores.',
      'Raposa: a imunidade total a dano de queda foi restrita à janela da habilidade Bote; a redução passiva de 40% foi mantida.',
      'Cabra: o Estômago de Cabra passa a aceitar qualquer item, exceto comida, ferramentas, armas e armaduras (antes aceitava apenas blocos).',
      'Coelho: ritmo de progressão reduzido (saltar: 2 XP a cada 40 blocos; colher plantação: 6; comer cenoura: 5).',
      'Progressão: descrições dos objetivos de todas as origens revisadas para o vocabulário do jogo.',
    ],
    fixed: [
      '/origin reset agora limpa a progressão em memória e remove a boss bar; antes o progresso antigo era regravado na seleção seguinte.',
      'Rato: objetivo "Desviar de projéteis" restaurado na lista de progressão (a esquiva concede 8 de XP).',
      'Lobo: leitura de nível nas habilidades Uivo do Alfa e Presas Noturnas tornada nula-segura, evitando exceções em estados de dados não carregados.',
    ],
  },
];
