/** Identidade e dados globais do site (usados pelo layout, header e footer). */
export const site = {
  name: 'Feather Flux',
  tagline: 'Origins SMP para Java e Bedrock',
  description:
    'O FeatherFlux é um servidor de Minecraft Original (Java e Bedrock). Oferecemos 16 origens (raças) pra você escolher, cada uma com seus poderes, suas fraquezas e uma progressão básica de tais habilidades.',
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
    version: '26.1.2',
    build: '1.2.3',
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
