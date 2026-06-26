/**
 * Compatibilidade do servidor com mods client-side, pra quem quer montar um
 * modpack só do seu lado. Regra geral: mod que roda só no cliente funciona;
 * alguns o servidor suporta de propósito (tem plugin do lado do servidor).
 */

export interface CompatCategory {
  key: string;
  label: string;
  color: string;
}

export const compatCategories: CompatCategory[] = [
  { key: 'suportado', label: 'Mods que o servidor suporta de propósito', color: '#6ee787' },
  { key: 'acesso', label: 'De onde dá pra entrar', color: '#7bb0ff' },
  { key: 'geral', label: 'Mods e pack em geral', color: '#c9a0ff' },
];

/** Tom do selo de status. */
export type CompatTone = 'ok' | 'rec' | 'info';

export interface CompatItem {
  slug: string;
  name: string;
  category: string;
  /** Selo curto: "Suportado", "Recomendado", "Sem mod"... */
  status: string;
  tone: CompatTone;
  desc: string;
  /** Observação curta (plugin, detalhe). */
  note?: string;
}

export const compatItems: CompatItem[] = [
  // Suportados de propósito (têm plugin server-side)
  {
    slug: 'cpm',
    name: 'Customizable Player Models (CPM)',
    category: 'suportado',
    status: 'Suportado',
    tone: 'ok',
    desc: 'Crie e use modelos de personagem personalizados. O servidor reconhece o mod, então os outros jogadores também veem o seu modelo.',
    note: 'Plugin: CustomPlayerModels (Paper).',
  },
  {
    slug: 'voxy',
    name: 'Voxy',
    category: 'suportado',
    status: 'Suportado',
    tone: 'ok',
    desc: 'Aumenta MUITO a distância de renderização, com LOD (o mundo ao longe vira blocões). O servidor manda os dados pro mod funcionar de verdade aqui.',
    note: 'Plugin: Voxy Server-Side.',
  },
  {
    slug: 'voicechat',
    name: 'Simple Voice Chat',
    category: 'suportado',
    status: 'Recomendado',
    tone: 'rec',
    desc: 'É o chat de voz do servidor (voz por proximidade e grupos). Pra usar, instale o mod no seu cliente, ou inclua ele no seu modpack.',
    note: 'No Bedrock o suporte de voz é separado.',
  },

  // De onde dá pra entrar
  {
    slug: 'bedrock',
    name: 'Bedrock (celular e console)',
    category: 'acesso',
    status: 'Sem mod',
    tone: 'info',
    desc: 'Dá pra entrar pela edição Bedrock sem nenhum mod, pelo Geyser. É só usar o IP e a porta do Bedrock que estão na página inicial.',
    note: 'Geyser + Floodgate.',
  },
  {
    slug: 'versions',
    name: 'Várias versões do Java',
    category: 'acesso',
    status: 'Funciona',
    tone: 'ok',
    desc: 'Você não precisa estar exatamente na versão do servidor: dá pra entrar de várias versões do Minecraft Java, das mais novas a algumas mais antigas.',
    note: 'ViaVersion + ViaBackwards.',
  },

  // Mods e pack em geral
  {
    slug: 'performance',
    name: 'Mods de performance e visual',
    category: 'geral',
    status: 'Funciona',
    tone: 'ok',
    desc: 'Sodium, Iris, Lithium, minimapa, shaders e afins funcionam normal, são só do seu lado. A regra é simples: mod que roda só no cliente, pode.',
    note: 'Mods que mudam a lógica do servidor não entram.',
  },
  {
    slug: 'resourcepack',
    name: 'Resource pack do servidor',
    category: 'geral',
    status: 'Obrigatório',
    tone: 'info',
    desc: 'Ao entrar, o servidor manda um resource pack (pros discos, texturas e modelos personalizados). É só aceitar. Se você usa um pack próprio, o do servidor fica por cima.',
  },
];

export const compatCount = compatItems.length;

/** Código de referência técnico estável: FF-CMP-001 … */
export function compatCode(i: CompatItem): string {
  return `FF-CMP-${String(compatItems.indexOf(i) + 1).padStart(3, '0')}`;
}
