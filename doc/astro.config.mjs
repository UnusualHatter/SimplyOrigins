import { defineConfig } from 'astro/config';

export default defineConfig({
  site: 'https://play.featherflux.com',
  // A antiga galeria /origins virou a Wiki. Mantém links/marcadores antigos vivos.
  redirects: {
    '/origins': '/wiki/origens',
    // O Equino virou Cavalo na 1.4.0.
    '/origins/equino': '/origins/cavalo',
  },
});
