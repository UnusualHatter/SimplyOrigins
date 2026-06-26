import { defineConfig } from 'astro/config';

export default defineConfig({
  site: 'https://play.featherflux.com',
  // A antiga galeria /origins virou a Wiki. Mantém links/marcadores antigos vivos.
  redirects: {
    '/origins': '/wiki/origens',
  },
});
