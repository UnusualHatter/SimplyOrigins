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
    version: '2.0',
    date: '2026-09-25',
    summary: [
      'FeatherFlux 2.0 (25/09) — servidor na 26.2:',
      '• Minecraft 26.2: cavernas de enxofre, Cubo de Enxofre, blocos de enxofre e cinábrio',
      '• Todos os plugins atualizados, mundo e progresso mantidos',
      '• Discos personalizados voltaram a ter som',
    ].join('\n'),
    added: [
      'O servidor agora roda no Minecraft 26.2. Chegam as cavernas de enxofre, o Cubo de Enxofre, os blocos de enxofre e cinábrio e o disco "Bounce".',
    ],
    changed: [
      'O mundo foi convertido pra 26.2 sem resetar nada: construções, baús, claims, origens, níveis e inventários continuam como estavam.',
      'Todos os plugins foram atualizados para versões feitas para a 26.2, incluindo Geyser (Bedrock), ViaVersion, CoreProtect, LuckPerms, AxGraves, GSit, Skript e Simple Voice Chat.',
      'Dungeons and Taverns atualizado para a versão da 26.2. Os encantamentos das dungeons e as estruturas já geradas continuam iguais.',
      'OriginsPaper atualizado para a versão 1.4.1, ajustada para a 26.2: o Demônio continua imune a bloco de magma e os poderes em área seguem respeitando o PvP desligado.',
      'A versão recomendada para entrar agora é a 26.2. Versões mais antigas do Java ainda entram, mas veem os blocos e mobs novos trocados por coisas parecidas.',
    ],
    fixed: [
      'Discos personalizados voltaram a tocar a música e mostrar a textura certa: o servidor estava mandando um pacote de recursos que não existia mais.',
      'Quem usa o mod SeeU voltou a ver os jogadores distantes: o plugin do servidor estava numa versão antiga e ignorava o mod.',
      'Quem ganhou corações extras pela progressão não perde mais vida ao sair e voltar pro servidor.',
    ],
  },
  {
    version: '1.4.0',
    date: '2026-07-05',
    summary: [
      'v1.4.0 (05/07) — 5 Origens Novas:',
      '• Panda-Vermelho, Tubarão, Guaxinim, Cavalo e Gambá',
      '• Todas com habilidade ativa (Agachar + F) e progressão própria',
      '• Habilidades em área agora respeitam claims, pets e mobs nomeados',
      '• Deslogar não zera mais a recarga das habilidades',
    ].join('\n'),
    added: [
      'Cinco origens novas, todas com habilidade ativa (Agachar + F), progressão do nível 1 ao 10 e página própria na Wiki — agora são 21 no total.',
      'Panda-Vermelho: acrobata das florestas. Escala paredes como uma aranha (até 4 blocos de altura), come bambu e ovos agachado e usa a Cauda Fofa pra regenerar a si e aos aliados próximos.',
      'Tubarão: predador aquático. Nada mais rápido que qualquer origem, morde mais forte dentro da água, sente presas feridas pelo Faro de Sangue e o Avanço Voraz deixa a presa sangrando.',
      'Guaxinim: catador noturno. Come comida estragada numa boa, de vez em quando rouba uma troca dos villagers (sai de graça), acha itens simples vasculhando o mundo e some da vista com a Esgueirada.',
      'Cavalo: corredor de campo aberto. Pega embalo galopando em linha reta, salta mais alto e manda inimigos pra longe com o Coice.',
      'Gambá: defensor fedorento. O Spray Fedorento não machuca ninguém, mas aplica Lentidão, Fraqueza e Náusea em quem chegar perto — e agachado quase nenhum monstro te nota.',
    ],
    changed: [
      'Habilidades em área e efeitos hostis (Bote, Cabeçada, Sopro do Dragão, Ecolocalização, Uivo do Alfa e os das origens novas) agora respeitam claims do GriefPrevention e nunca atingem pets ou mobs nomeados. O poder continua funcionando: ele só ignora os alvos protegidos.',
      'Jogadores em mundos com PvP desligado ficam imunes aos poderes hostis das outras origens.',
      'O plugin ficou mais leve: cada poder passivo roda no seu próprio ritmo em vez de todos serem verificados a cada segundo.',
      'Pra staff: /origin debug mostra o estado do plugin, contadores de desempenho e o status das integrações; cada origem pode ser desligada individualmente na config, e os valores de balanceamento das origens novas são configuráveis.',
    ],
    fixed: [
      'Deslogar e voltar não zera mais a recarga das habilidades ativas — o cooldown agora é salvo junto com o progresso.',
      'Trocar de origem ou recarregar o plugin no meio de uma habilidade não deixa mais efeitos presos no jogador, e as limpezas de origem pararam de remover poções legítimas (as bebidas de verdade ficam).',
      'Comer comida duvidosa como Guaxinim ou Gambá não limpa mais debuffs de combate aplicados por outros jogadores.',
      'Os arquivos de progresso dos jogadores passaram a ser salvos de forma atômica, sem risco de corromper se o servidor cair no meio da gravação.',
    ],
  },
  {
    version: '1.2.6',
    date: '2026-07-02',
    summary: [
      'v1.2.6 (02/07):',
      '• Ações bloqueadas por claims não concedem mais XP de origem',
      '• Origens sem poder ativo voltaram a trocar itens entre as mãos normalmente',
      '• Habilidades e comandos administrativos ficaram mais seguros',
    ].join('\n'),
    changed: [
      'O OriginsPaper foi recompilado com a versão mais recente do código e validado no Paper 26.1.2 com Java 25.',
    ],
    fixed: [
      'Ações canceladas por claims ou outros plugins não ativam mais poderes nem contam para a progressão. Isso impede, por exemplo, ganhar XP tentando quebrar blocos protegidos.',
      'Agachar e pressionar F agora só é reservado pelo OriginsPaper quando a origem possui um poder ativo. Origens passivas voltaram a trocar itens entre as mãos normalmente.',
      'Uma falha interna ao ativar um poder não coloca mais a habilidade em cooldown sem ter funcionado.',
      'O comando administrativo /origin addxp agora rejeita valores iguais ou menores que zero e lida com quantidades muito altas sem perder o XP acumulado.',
    ],
  },
  {
    version: '1.2.5',
    date: '2026-07-01',
    summary: [
      'v1.2.5 (01/07) — Novo Sistema de Bloqueio:',
      '• Controle granular de chat, voz, visibilidade e interações',
      '• Interface própria pelo comando /block',
      '• Bloqueios recíprocos, imediatos e persistentes',
      '• Integração com os principais recursos do servidor',
    ].join('\n'),
    added: [
      'Novo sistema completo de bloqueio e privacidade. Use /block para abrir uma interface e escolher um jogador online ou offline; /unblock <jogador> remove todos os bloqueios aplicados a ele.',
      'Cada aspecto pode ser configurado separadamente: visibilidade, chat público, mensagens privadas, entrada e saída, mensagens de morte, TPA, trocas, convites de grupo, voz, TAB, sons, partículas, poses, graves e interações.',
      'O bloqueio é individual: o jogador deixa de aparecer ou interagir apenas para quem o bloqueou, sem alterar a experiência das outras pessoas no servidor.',
    ],
    changed: [
      'Canais sociais como chat, mensagens privadas, voz, TPA, trocas e convites são bloqueados nos dois sentidos quando qualquer um dos jogadores ativa a opção correspondente.',
      'A privacidade visual também cobre modelo, armadura, nome, TAB, scoreboard, sons, partículas, graves, poses e outros efeitos atribuídos ao jogador bloqueado.',
      'Integrações adicionadas para EssentialsX, Simple Voice Chat, TAB, GSit, InteractionVisualizer, AxGraves, ProtocolLib, claims e containers.',
      'Todas as alterações são aplicadas imediatamente, sem precisar fechar a interface ou reconectar. As preferências ficam salvas e continuam valendo após sair ou reiniciar o servidor.',
    ],
    fixed: [
      'Corrigido o bloqueio do Simple Voice Chat em proximidade e grupos: apenas o par bloqueado deixa de se ouvir; os demais jogadores continuam recebendo o áudio normalmente.',
      'Reforçada a recuperação automática da privacidade ao entrar, renascer ou trocar de mundo, evitando que outros plugins revelem novamente um jogador bloqueado.',
      'Diversas correções de desempenho, segurança, persistência e estabilidade no processamento dos bloqueios.',
    ],
  },
  {
    version: '1.2.4',
    date: '2026-06-26',
    summary: [
      'v1.2.4 (26/06):',
      '• Site e Wiki redesenhados, com busca rápida (Ctrl+K)',
      '• Encantos aparecem no mundo conforme a raridade de cada um',
      '• Baús, barris e cofres voltam a reabastecer — até em áreas já exploradas',
    ].join('\n'),
    added: [
      'Site e Wiki redesenhados do zero: nova identidade visual, barra lateral com o índice da Wiki, busca global por atalho (Ctrl/⌘K) e todas as páginas refeitas (início, Wiki, origens, encantamentos, recursos, compatibilidade e changelog).',
      'Os encantos personalizados aparecem no mundo conforme a raridade de cada um: na mesa de encantamento, com bibliotecários, em baús gerados pelo mundo e como tesouro (estes só por loot, troca ou pesca).',
      'Baús, barris e cofres voltam a reabastecer depois de um tempo (de 1 a 4 dias), inclusive em áreas que já foram exploradas — dá pra reencontrar os encantos novos no loot sem ter que ir cada vez mais longe.',
    ],
    changed: [
      'Na Wiki, os encantamentos passam a ser filtrados pelo item do Minecraft (espada, arco, picareta...) em vez de por categoria, e cada encanto mostra em que itens ele cabe.',
    ],
    fixed: [
      'Corrigida a formatação dos resultados da busca rápida (Ctrl/⌘K), que apareciam como texto grudado, sem espaçamento.',
    ],
  },
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
