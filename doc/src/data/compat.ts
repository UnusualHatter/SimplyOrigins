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
  { key: 'suportado', label: 'Mods com suporte do servidor', color: '#6ee787' },
  { key: 'acesso', label: 'Edições e versões', color: '#7bb0ff' },
  { key: 'geral', label: 'Outros mods e resource pack', color: '#c9a0ff' },
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
  /** Tutorial relacionado, quando houver. */
  tutorial?: string;
}

export const compatItems: CompatItem[] = [
  // Suportados de propósito (têm plugin server-side)
  {
    slug: 'cpm',
    name: 'Customizable Player Models (CPM)',
    category: 'suportado',
    status: 'Suportado',
    tone: 'ok',
    desc: 'Modelos de personagem personalizados reconhecidos pelo servidor e visíveis para outros jogadores.',
    note: 'Plugin: CustomPlayerModels (Paper).',
  },
  {
    slug: 'veinminer-client',
    name: 'VeinMiner — addon de atalho (opcional)',
    category: 'suportado',
    status: 'Opcional',
    tone: 'info',
    desc: 'O VeinMiner roda no servidor e funciona para todo mundo, sem instalar nada. O addon de atalho é só um extra do seu lado: ele mostra a prévia do veio antes de quebrar e, com ele, a mineração em veio passa a valer para qualquer bloco, não só minérios.',
    note: 'Opcional, e só para Java com Fabric, NeoForge ou Quilt. Sem ele você continua minerando veios de minérios normalmente, inclusive no Bedrock.',
    tutorial: 'coleta',
  },
  {
    slug: 'voxy',
    name: 'Voxy',
    category: 'suportado',
    status: 'Suportado',
    tone: 'ok',
    desc: 'Aumenta a distância de renderização usando LODs. O servidor envia os dados necessários para o Voxy.',
    note: 'Plugin: Voxy Server Side.',
  },
  {
    slug: 'voicechat',
    name: 'Simple Voice Chat',
    category: 'suportado',
    status: 'Recomendado',
    tone: 'rec',
    desc: 'Integra o chat de voz por proximidade e os grupos privados ao servidor.',
    note: 'No Bedrock o suporte de voz é separado.',
    tutorial: 'voz',
  },

  // De onde dá pra entrar
  {
    slug: 'bedrock',
    name: 'Bedrock (celular e console)',
    category: 'acesso',
    status: 'Sem mod',
    tone: 'info',
    desc: 'A edição Bedrock acessa o mesmo mundo por meio do Geyser, sem exigir mod.',
    note: 'Geyser + Floodgate.',
    tutorial: 'entrar',
  },
  {
    slug: 'versions',
    name: 'Várias versões do Minecraft',
    category: 'acesso',
    status: 'Funciona',
    tone: 'ok',
    desc: 'O servidor roda na 26.2, que é a versão recomendada. Versões mais antigas do Minecraft Java também entram, mas os blocos e mobs novos aparecem trocados por coisas parecidas.',
    note: 'ViaVersion + ViaBackwards.',
  },

  // Mods e pack em geral
  {
    slug: 'performance',
    name: 'Mods de performance e visual',
    category: 'geral',
    status: 'Funciona',
    tone: 'ok',
    desc: 'Sodium, Iris, Lithium, minimapas, shaders e outros mods client side funcionam normalmente.',
    note: 'Mods que mudam a lógica do servidor não entram.',
  },
  {
    slug: 'resourcepack',
    name: 'Resource pack do servidor',
    category: 'geral',
    status: 'Obrigatório',
    tone: 'info',
    desc: 'Pacote de recursos do servidor com os sons e as texturas dos discos criados pelos jogadores. Ele é enviado ao entrar e atualizado sozinho quando alguém cria um disco novo.',
    tutorial: 'entrar',
  },
];

export const compatCount = compatItems.length;

/** Código de referência técnico estável: FF-CMP-001 … */
export function compatCode(i: CompatItem): string {
  return `FF-CMP-${String(compatItems.indexOf(i) + 1).padStart(3, '0')}`;
}
