/** Identidade e dados globais do site (usados pelo layout, header e footer). */
export const site = {
  name: 'Feather Flux',
  tagline: 'Origins SMP para Java e Bedrock',
  description:
    'O Feather Flux é um servidor de Minecraft Origins SMP (Java e Bedrock). São 16 origens pra escolher, cada uma com seus poderes, suas fraquezas e uma progressão do nível 1 ao 10.',
  /** Endereços de conexão exibidos no site (Java + Bedrock). */
  connect: {
    java: {
      main: 'feathersmp.playit.plus',
      secondary: 'star-jade.sa.mcjoin.link',
    },
    bedrock: {
      ip: '198.22.204.31',
      port: 1038,
    },
  },
  /** Metadados técnicos exibidos no índice (sidebar) — estilo manual. */
  meta: {
    version: '1.2.3',
    build: '26.1.2',
    status: 'Estável',
  },
  /** Links externos (placeholders — troque pelos reais). */
  discord: 'https://discord.gg/AWFKUEJ5QS',
  /** Itens do menu principal. */
  nav: [
    { label: 'Início', href: '/' },
    { label: 'Wiki', href: '/wiki' },
    { label: 'Changelog', href: '/changelog' },
    { label: 'Jogar', href: '/#status' },
  ],
};
