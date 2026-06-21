package dev.originspaper.progression;

import dev.originspaper.progression.OriginProgression.Objective;
import dev.originspaper.progression.OriginProgression.Unlock;

import java.util.List;
import java.util.Map;

/**
 * Per-origin progression designs: the thematic challenges that grant XP and the milestone unlocks.
 * Level 1 is the origin's existing kit, unchanged; everything here is a bonus on top, kept
 * deliberately modest/utility-leaning so high levels reward without breaking the base kit.
 */
public final class ProgressionDefs {

    private ProgressionDefs() {}

    private static OriginProgression def(List<Objective> objectives, List<Unlock> unlocks) {
        return new OriginProgression(objectives, unlocks);
    }

    private static Objective o(String description, int xp) {
        return new Objective(description, xp);
    }

    private static Unlock u(int level, String name, String description) {
        return new Unlock(level, name, description);
    }

    private static final Map<String, OriginProgression> DEFS = Map.ofEntries(
            Map.entry("human", def(
                    List.of(o("Matar monstros", 10), o("Caçar animais", 6),
                            o("Explorar o mundo", 5)),
                    List.of(u(5, "Vitalidade", "+1 coração de vida máxima."),
                            u(10, "Resiliência", "+1 coração de vida máxima (total +2).")))),
            Map.entry("otter", def(
                    List.of(o("Nadar longas distâncias", 5), o("Minerar submerso", 8),
                            o("Caçar na água", 15)),
                    List.of(u(3, "Mergulhador", "Minera mais rápido debaixo d'água."),
                            u(6, "Fôlego Aquático", "Poder do Condutor enquanto submerso."),
                            u(10, "Corrente", "Nada bem mais rápido em qualquer água.")))),
            Map.entry("deer", def(
                    List.of(o("Correr longas distâncias", 6), o("Sobreviver a grandes quedas", 18),
                            o("Matar monstros", 8)),
                    List.of(u(3, "Passada Longa", "Salto um pouco mais alto."),
                            u(6, "Cervo Veloz", "Velocidade base aumentada."),
                            u(10, "Instinto de Fuga", "Dano de queda reduzido a quase nada.")))),
            Map.entry("bat", def(
                    List.of(o("Planar pelo ar", 5), o("Minerar no escuro", 6),
                            o("Matar monstros à noite", 10)),
                    List.of(u(3, "Membrana Resistente", "+1 coração de vida máxima."),
                            u(6, "Eco da Caverna", "Mais ágil nas profundezas escuras."),
                            u(10, "Senhor da Noite", "Velocidade extra no escuro da noite.")))),
            Map.entry("rat", def(
                    List.of(o("Matar monstros", 12), o("Minerar e coletar recursos", 3),
                            o("Desviar de projéteis", 8)),
                    List.of(u(3, "Reflexos", "Chance de evasão 30% → 38%."),
                            u(6, "Fôlego de Rato", "Recarga da Disparada reduzida em 30%."),
                            u(10, "Inalcançável", "Chance de evasão 38% → 45%.")))),
            Map.entry("demon", def(
                    List.of(o("Matar monstros", 10), o("Matar no Nether", 20),
                            o("Matar à noite", 15)),
                    List.of(u(3, "Brasa", "+0,5 coração de dano corpo a corpo."),
                            u(6, "Pele de Cinzas", "Regenera lentamente perto de fogo e lava."),
                            u(10, "Fúria Infernal", "Ao matar, ganha Força I por alguns segundos.")))),
            Map.entry("wolf", def(
                    List.of(o("Matar monstros", 15), o("Matar à noite (bônus)", 25),
                            o("Derrotar um chefe", 200)),
                    List.of(u(3, "Presas Afiadas", "+1 de dano de mordida à noite."),
                            u(6, "Líder", "Recarga do Uivo do Alfa reduzida."),
                            u(10, "Alcateia", "À noite ganha Força I; o uivo fortalece aliados próximos.")))),
            Map.entry("fox", def(
                    List.of(o("Caçar animais", 10), o("Matar monstros", 8),
                            o("Comer frutas", 6)),
                    List.of(u(3, "Saltitante", "Pulo um pouco mais alto."),
                            u(6, "Faro de Sorte", "Mais sorte em drops raros."),
                            u(10, "Bote Mortal", "Dano do Bote (no ar e na aterrissagem) aumentado.")))),
            Map.entry("bear", def(
                    List.of(o("Matar monstros", 12), o("Caçar animais", 6),
                            o("Comer carne crua", 8)),
                    List.of(u(3, "Patas Brutais", "+1 de dano com mãos vazias."),
                            u(6, "Couro Espesso", "Tenacidade de armadura reforçada."),
                            u(10, "Vigor do Urso", "+2 corações de vida máxima.")))),
            Map.entry("rabbit", def(
                    List.of(o("Colher plantações", 6), o("Comer cenouras", 5),
                            o("Saltitar pelo mundo", 2)),
                    List.of(u(3, "Pulo Alto", "Impulso de pulo aumentado."),
                            u(6, "Lebre Veloz", "Velocidade aumentada."),
                            u(10, "Fertilidade", "+1 coração de vida máxima.")))),
            Map.entry("goat", def(
                    List.of(o("Matar monstros", 10), o("Caçar animais", 6),
                            o("Acertar Cabeçadas", 8)),
                    List.of(u(3, "Crânio Duro", "Dano da Cabeçada aumentado."),
                            u(6, "Impacto", "Dano e empurrão da Investida reforçados."),
                            u(10, "Avalanche", "A explosão da Cabeçada fica maior.")))),
            Map.entry("feline", def(
                    List.of(o("Matar monstros", 12), o("Caçar animais", 6),
                            o("Perseguir presas correndo", 4)),
                    List.of(u(3, "Caçador Ágil", "Velocidade extra ao correr atrás de presas."),
                            u(6, "Reflexos Felinos", "Ataca mais rápido."),
                            u(10, "Bote Felino", "Acertos têm chance de dano crítico extra.")))),
            Map.entry("owl", def(
                    List.of(o("Planar longas distâncias", 5), o("Caçar à noite", 10),
                            o("Atacar mergulhando", 8)),
                    List.of(u(3, "Olhar Noturno", "Bônus de dano noturno aumentado."),
                            u(6, "Sonar Apurado", "Recarga da Ecolocalização reduzida."),
                            u(10, "Predador Aéreo", "Mergulho de ataque causa dano extra.")))),
            Map.entry("gryphon", def(
                    List.of(o("Voar longas distâncias", 5), o("Atacar mergulhando", 8),
                            o("Matar monstros", 10)),
                    List.of(u(3, "Decolagem Forte", "Impulso da Decolagem aumentado."),
                            u(6, "Caça-Picada", "Dano ao atacar mergulhando aumentado."),
                            u(10, "Senhor dos Céus", "Recarga da Decolagem reduzida.")))),
            Map.entry("dragon", def(
                    List.of(o("Queimar alvos com o Sopro de Fogo", 5), o("Voar longas distâncias", 4),
                            o("Matar monstros no End", 25)),
                    List.of(u(3, "Chama Longa", "Alcance do Sopro do Dragão aumentado."),
                            u(6, "Brasa Eterna", "Recarga do Sopro reduzida."),
                            u(10, "Cataclismo", "O sopro queima os alvos por mais tempo.")))),
            Map.entry("moth", def(
                    List.of(o("Voar perto de flores", 5), o("Polinizar plantações", 4),
                            o("Comer frutas e plantas", 6)),
                    List.of(u(3, "Pólen Fértil", "Raio de polinização aumentado."),
                            u(6, "Corpo Resiliente", "+1 coração de vida máxima."),
                            u(10, "Aura de Vida", "Concede Saturação a jogadores próximos ao polinizar.")))));

    public static OriginProgression get(String originId) {
        return originId == null ? null : DEFS.get(originId.toLowerCase());
    }
}
