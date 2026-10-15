/** Identidade e dados globais do site (usados pelo layout, header e footer). */
export const site = {
  name: 'Feather Flux',
  tagline: 'Origins SMP para Java e Bedrock',
  description:
    'O FeatherFlux é um servidor de Minecraft Original (Java e Bedrock). Oferecemos 21 origens (raças) pra você escolher, cada uma com seus poderes, suas fraquezas e uma progressão básica de tais habilidades.',
  /** Endereços de conexão exibidos no site (Java + Bedrock). */
  connect: {
    java: {
      main: '100.105.145.99',
      secondary: 'a definir',
    },
    bedrock: {
      ip: '100.105.145.99',
      port: '19132',
    },
  },
  /** Metadados técnicos exibidos no índice (sidebar) — estilo manual. */
  meta: {
    version: '26.2',
    build: '1.4.1',
    status: 'Estável',
  },
  /** Links externos (placeholders — troque pelos reais). */
  discord: 'https://discord.gg/apxnyRPm8d',
  /** Itens do menu principal. */
  nav: [
    { label: 'Início', href: '/' },
    { label: 'Wiki', href: '/wiki' },
    { label: 'Tutoriais', href: '/tutoriais' },
    { label: 'Changelog', href: '/changelog' },
    { label: 'Jogar', href: '/#status' },
  ],
};
