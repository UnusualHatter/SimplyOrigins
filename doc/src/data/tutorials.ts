import { site } from './site';

export interface TutorialCategory {
  key: string;
  label: string;
  color: string;
}

export interface Tutorial {
  slug: string;
  title: string;
  category: string;
  summary: string;
  steps: string[];
  commands?: string[];
  note?: string;
  video?: {
    id: string;
    start: number;
    time: string;
    title: string;
    url: string;
  };
  links?: {
    label: string;
    url: string;
  }[];
}

export const tutorialCategories: TutorialCategory[] = [
  { key: 'inicio', label: 'Começando', color: '#e3b94f' },
  { key: 'viagem', label: 'Homes e viagens', color: '#7bb0ff' },
  { key: 'protecao', label: 'Proteção e privacidade', color: '#f28c5e' },
  { key: 'social', label: 'Conforto e social', color: '#5fd3e0' },
  { key: 'mundo', label: 'Mundo e criação', color: '#6ee787' },
];

export const tutorials: Tutorial[] = [
  {
    slug: 'entrar',
    title: 'Como entrar no servidor',
    category: 'inicio',
    summary: 'Veja qual endereço usar para entrar pelo Java ou pelo Bedrock.',
    steps: [
      `Se você joga no Java, abra Multijogador e adicione o endereço ${site.connect.java.main}.`,
      `Caso o endereço principal não responda, tente ${site.connect.java.secondary}.`,
      `Se você joga no Bedrock, adicione ${site.connect.bedrock.ip} com a porta ${site.connect.bedrock.port}.`,
      'Quando o jogo perguntar, aceite o resource pack do servidor para receber as texturas e os modelos.',
    ],
    note: 'Jogadores do Java e do Bedrock compartilham o mesmo mundo. Alguns recursos podem funcionar de forma diferente em cada edição.',
  },
  {
    slug: 'origem',
    title: 'Escolher e evoluir sua origem',
    category: 'inicio',
    summary: 'Sua origem define poderes, limitações e uma progressão própria.',
    steps: [
      'Na primeira vez que entrar, o menu de origens vai abrir automaticamente.',
      'Leia os poderes e as limitações antes de escolher. Seu personagem só poderá andar depois da escolha.',
      'Jogue de acordo com os objetivos da sua origem para ganhar XP de origem.',
      'A barra na tela mostra seu progresso. A Wiki explica o que cada nível libera.',
      'Se sua origem tiver um poder ativo, agache e pressione F para ativar.',
    ],
    note: 'O XP de origem é separado do XP normal do Minecraft.',
  },
  {
    slug: 'homes',
    title: 'Criar e usar homes',
    category: 'viagem',
    summary: 'Salve sua base e outros lugares importantes para voltar quando quiser.',
    steps: [
      'Vá até o lugar que deseja salvar e use /sethome nome.',
      'Para voltar, use /home nome.',
      'Se esquecer os nomes, use /homes para ver sua lista.',
      'Quando não quiser mais uma home, remova com /delhome nome.',
    ],
    commands: ['/sethome <nome>', '/home <nome>', '/homes', '/delhome <nome>'],
  },
  {
    slug: 'teleporte',
    title: 'Usar TPA com outros jogadores',
    category: 'viagem',
    summary: 'Envie um pedido de TPA para ir até alguém ou trazer essa pessoa até você.',
    steps: [
      'Use /tpa jogador quando quiser ir até outra pessoa.',
      'Quem recebeu o pedido pode usar /tpaccept para aceitar ou /tpdeny para recusar.',
      'Para chamar alguém até você, use /tpahere jogador.',
      'Espere o pedido ser aceito antes de continuar.',
    ],
    commands: ['/tpa <jogador>', '/tpahere <jogador>', '/tpaccept', '/tpdeny'],
  },
  {
    slug: 'waystones',
    title: 'Criar e usar waystones',
    category: 'viagem',
    summary: 'Monte pontos de viagem com lodestone e teleporte-se entre os que você já descobriu.',
    steps: [
      'Para criar a sua: coloque uma lodestone em cima de um bloco de pedra lisa e clique nela com o botão direito para dar um nome.',
      'No menu da waystone dá para renomear, trocar o ícone, mudar de lugar e escolher quem pode usar: só você, amigos ou todo mundo.',
      'Encontrou uma waystone de outra pessoa? Clique nela para descobrir o destino — as partículas mudam de cor.',
      'Para viajar, segure uma bússola e use o botão direito: escolha o destino e fique parado durante os cinco segundos de espera.',
      'Depois de viajar, espere trinta segundos para usar outra waystone.',
    ],
    note: 'As viagens são gratuitas e cada jogador pode criar até três waystones. Um holograma mostra o nome de cada uma.',
  },
  {
    slug: 'claims',
    title: 'Criar e gerenciar claims',
    category: 'protecao',
    summary: 'Proteja sua construção com um claim e escolha quem pode mexer nela.',
    steps: [
      'No mundo principal, pegue uma pá de ouro.',
      'Use o botão direito em dois cantos opostos da área que deseja transformar em claim.',
      'Para conferir os limites e o dono de um claim, use um graveto.',
      'Dentro do seu claim, use /trust jogador para liberar acesso ou /untrust jogador para remover.',
      'Use /trustlist para ver as permissões. Se quiser apagar o claim, use /abandonclaim.',
    ],
    commands: ['/trust <jogador>', '/untrust <jogador>', '/trustlist', '/abandonclaim'],
    note: 'Você começa com cem blocos de claim e recebe mais duzentos por hora de jogo.',
    video: {
      id: 'dfI62Dz1S64',
      start: 14,
      time: '0:14',
      title: 'Como proteger seu terreno com GriefPrevention',
      url: 'https://youtu.be/dfI62Dz1S64?t=14',
    },
  },
  {
    slug: 'grief',
    title: 'O que fazer se sua construção sofrer grief',
    category: 'protecao',
    summary: 'O CoreProtect registra as alterações do mundo para a equipe entender o que aconteceu e recuperar sua construção.',
    steps: [
      'Anote as coordenadas do local e, se souber, o horário aproximado do grief.',
      'Evite reconstruir ou retirar blocos antes da análise, pois isso deixa o histórico mais fácil de entender.',
      'Envie as coordenadas e uma breve explicação no Discord do servidor.',
      'Aguarde o retorno da equipe. Ela vai consultar o CoreProtect, identificar as alterações e restaurar o que for necessário.',
    ],
    note: 'O CoreProtect registra blocos quebrados, containers acessados, explosões e outras mudanças feitas no mundo.',
  },
  {
    slug: 'bloqueio',
    title: 'Usar o Blocker',
    category: 'protecao',
    summary: 'Escolha o que deseja bloquear de cada jogador sem afetar o restante do servidor.',
    steps: [
      'Abra o menu com /block.',
      'Escolha qualquer jogador que já tenha entrado no servidor.',
      'Marque o que deseja bloquear, como chat, voz, TPA, visibilidade, sons e partículas.',
      'Tudo passa a valer na hora e continua salvo depois que você sair.',
      'Para limpar todas as opções de alguém, use /unblock jogador.',
    ],
    commands: ['/block', '/block <jogador>', '/unblock <jogador>'],
    note: 'Nos canais sociais, o bloqueio vale nos dois sentidos assim que qualquer um dos jogadores ativa a opção.',
  },
  {
    slug: 'voz',
    title: 'Configurar o Simple Voice Chat',
    category: 'social',
    summary: 'Converse com quem está por perto ou crie um grupo privado com seus amigos.',
    steps: [
      'Instale o Simple Voice Chat compatível com a versão do seu Minecraft.',
      'Dentro do servidor, pressione V para abrir o menu do mod.',
      'Escolha seu microfone, ajuste os volumes e configure a tecla de voz.',
      'No mesmo menu, você pode criar ou entrar em um grupo para conversar de qualquer distância.',
    ],
    note: 'No Bedrock, o suporte de voz usa uma solução separada.',
    video: {
      id: 'WB9ke5rAULs',
      start: 305,
      time: '5:05',
      title: 'Como configurar o Simple Voice Chat',
      url: 'https://youtu.be/WB9ke5rAULs?t=305',
    },
    links: [
      {
        label: 'Baixar o Simple Voice Chat no Modrinth',
        url: 'https://modrinth.com/plugin/simple-voice-chat/versions',
      },
    ],
  },
  {
    slug: 'poses',
    title: 'Usar as poses do GSit',
    category: 'social',
    summary: 'Sente, deite, gire ou rasteje para tirar fotos e interagir com outros jogadores.',
    steps: [
      'Para sentar onde está, use /sit.',
      'Para deitar, use /lay. Para girar, use /spin.',
      'O comando /crawl ativa ou desativa a pose de rastejar.',
      'Para sair de uma pose, mova o personagem ou repita o comando.',
    ],
    commands: ['/sit', '/lay', '/spin', '/crawl'],
  },
  {
    slug: 'lapides',
    title: 'Recuperar itens com o AxGraves',
    category: 'mundo',
    summary: 'Quando você morre, o AxGraves cria uma grave com seus itens e seu XP.',
    steps: [
      'Depois de morrer, confira no chat onde sua grave apareceu.',
      'Sem pressa: a grave fica no local esperando, sem limite de tempo.',
      'Abra a grave normalmente ou agache e use o botão direito para pegar tudo de uma vez.',
      'Quando estiver vazia, a grave desaparece sozinha.',
    ],
    note: 'A grave não expira e continua lá mesmo depois de o servidor reiniciar.',
  },
  {
    slug: 'coleta',
    title: 'Usar TreeFeller e VeinMiner',
    category: 'mundo',
    summary: 'Colete árvores e veios inteiros sem quebrar cada bloco separadamente.',
    steps: [
      'Para ativar o TreeFeller, agache e quebre o tronco com um machado.',
      'Para ativar o VeinMiner, agache e quebre um minério com a ferramenta correta.',
      'Antes de começar, confira se há espaço no inventário e durabilidade na ferramenta.',
      'Opcional: instale o addon de atalho no seu jogo para ver a prévia do veio e minerar veios de qualquer bloco, não só minérios.',
    ],
    note: 'Os recursos só são ativados enquanto você estiver agachado. O addon é opcional e só existe para Java com Fabric, NeoForge ou Quilt — sem ele o VeinMiner continua funcionando em minérios, inclusive no Bedrock.',
    links: [
      {
        label: 'Baixar o addon de atalho (opcional)',
        url: 'https://modrinth.com/mod/veinminer-client',
      },
    ],
  },
  {
    slug: 'imagens',
    title: 'Criar imagens com o ImageFrame',
    category: 'mundo',
    summary: 'Transforme uma imagem da internet em mapas e coloque na parede com item frames.',
    steps: [
      'Pegue o endereço direto da imagem que deseja usar.',
      'Crie os mapas com /imageframe create nome endereço.',
      'Espere o ImageFrame terminar o processamento e entregar o item.',
      'Prepare os item frames na parede e use o item criado para colocar a imagem.',
    ],
    commands: ['/imageframe create <nome> <endereço>'],
    note: 'A criação depende de permissão. Peça ajuda à equipe se o comando não estiver disponível.',
    video: {
      id: 'OHRmLzg7hWA',
      start: 24,
      time: '0:24',
      title: 'Como criar mapas com o ImageFrame',
      url: 'https://youtu.be/OHRmLzg7hWA?t=24',
    },
  },
];

export const tutorialCount = tutorials.length;

export function tutorialCode(tutorial: Tutorial): string {
  return `FF-TUT-${String(tutorials.indexOf(tutorial) + 1).padStart(3, '0')}`;
}
