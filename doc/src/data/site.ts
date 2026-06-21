/** Identidade e dados globais do site (usados pelo layout, header e footer). */
export const site = {
  name: 'Feather Flux',
  tagline: 'Origins SMP para Java & Bedrock',
  description:
    'Feather Flux é um servidor Origins SMP de Minecraft (Java & Bedrock) com 16 Origens jogáveis — cada uma com poderes, fraquezas e uma progressão própria de nível 1 a 10.',
  /** Endereço de conexão exibido no site. */
  ip: 'play.featherflux.com',
  /** Links externos (placeholders — troque pelos reais). */
  discord: 'https://discord.gg/featherflux',
  /** Itens do menu principal. */
  nav: [
    { label: 'Início', href: '/' },
    { label: 'Origins', href: '/origins' },
    { label: 'Changelog', href: '/changelog' },
    { label: 'Jogar', href: '/#status' },
  ],
};
