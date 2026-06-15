package dev.originspaper.registry;

import dev.originspaper.api.Origin;
import dev.originspaper.api.Origin.PowerInfo;
import dev.originspaper.api.PowerType;
import dev.originspaper.power.origins.bat.BatGlidePower;
import dev.originspaper.power.origins.bat.CaveMobilityPower;
import dev.originspaper.power.origins.bear.CumbersomeClawsPower;
import dev.originspaper.power.origins.bear.HibernationPower;
import dev.originspaper.power.origins.bear.MightyPawsPower;
import dev.originspaper.power.origins.deer.NaturalRunnerPower;
import dev.originspaper.power.origins.demon.HellPactPower;
import dev.originspaper.power.origins.demon.HolyVulnerabilityPower;
import dev.originspaper.power.origins.demon.InfernalVisualsPower;
import dev.originspaper.power.origins.dragon.DragonAuraPower;
import dev.originspaper.power.origins.dragon.DragonBreathPower;
import dev.originspaper.power.origins.dragon.RebornMagicPower;
import dev.originspaper.power.origins.fox.HuntPower;
import dev.originspaper.power.origins.fox.PouncePower;
import dev.originspaper.power.origins.fox.TimidityPower;
import dev.originspaper.power.origins.goat.GoatGrazePower;
import dev.originspaper.power.origins.goat.LeapPower;
import dev.originspaper.power.origins.goat.RamPower;
import dev.originspaper.power.origins.gryphon.FreshAirPower;
import dev.originspaper.power.origins.moth.DrawnToLightPower;
import dev.originspaper.power.origins.moth.FlutterGlidePower;
import dev.originspaper.power.origins.moth.FriendOfBeesPower;
import dev.originspaper.power.origins.moth.MothVisualsPower;
import dev.originspaper.power.origins.moth.PollinatorPower;
import dev.originspaper.power.origins.otter.AmphibiousPower;
import dev.originspaper.power.origins.otter.FinsPower;
import dev.originspaper.power.origins.otter.LandStridePower;
import dev.originspaper.power.origins.otter.LikeWaterPower;
import dev.originspaper.power.origins.otter.WaterDependencyPower;
import dev.originspaper.power.origins.otter.WetEyesPower;
import dev.originspaper.power.origins.rat.EvasionPower;
import dev.originspaper.power.origins.owl.DayDazedPower;
import dev.originspaper.power.origins.owl.EcholocationPower;
import dev.originspaper.power.origins.owl.NightHunterPower;
import dev.originspaper.power.origins.owl.SilentFlightPower;
import dev.originspaper.power.origins.rabbit.PredatorScentPower;
import dev.originspaper.power.origins.wolf.AlphaHowlPower;
import dev.originspaper.power.origins.wolf.CarnivoresBitePower;
import dev.originspaper.power.origins.wolf.DayNightSpeedPower;
import dev.originspaper.power.origins.wolf.HuntersSensePower;
import dev.originspaper.power.origins.wolf.NightFangsPower;
import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.power.shared.ArmorMaterialRestrictPower;
import dev.originspaper.power.shared.ArmorSlotRestrictPower;
import dev.originspaper.power.shared.AttributeModifierPower;
import dev.originspaper.power.shared.BiomeEffectPower;
import dev.originspaper.power.shared.BiomeParticlePower;
import dev.originspaper.power.shared.CarnivoreDietPower;
import dev.originspaper.power.shared.DamageImmunityPower;
import dev.originspaper.power.shared.DamageMultiplierPower;
import dev.originspaper.power.shared.DietPower;
import dev.originspaper.power.shared.DimensionAttributePower;
import dev.originspaper.power.shared.ElytraFlightPower;
import dev.originspaper.power.shared.ExhaustionPower;
import dev.originspaper.power.shared.FireImmunityPower;
import dev.originspaper.power.shared.FlightLaunchPower;
import dev.originspaper.power.shared.LightArmorOnlyPower;
import dev.originspaper.power.shared.NightVisionPower;
import dev.originspaper.power.shared.NoFallDamagePower;
import dev.originspaper.power.shared.NoShieldPower;
import dev.originspaper.power.shared.NutritionPower;
import dev.originspaper.power.shared.PermanentEffectPower;
import dev.originspaper.power.shared.PotionImmunityPower;
import dev.originspaper.power.shared.SilentStepsPower;
import dev.originspaper.util.FoodUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Builds and stores the 15 origins, in display order (Human first). */
public class OriginRegistry {

    private static final Set<Biome> FOREST_BIOMES = Set.of(
            Biome.FOREST, Biome.TAIGA, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.OLD_GROWTH_PINE_TAIGA,
            Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.GROVE, Biome.WINDSWEPT_FOREST, Biome.MEADOW);

    // Deer roam wider than the dense forests the bear claims: also savannas and swamps.
    private static final Set<Biome> DEER_HABITAT = Set.of(
            Biome.FOREST, Biome.FLOWER_FOREST, Biome.BIRCH_FOREST, Biome.DARK_FOREST,
            Biome.TAIGA, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.OLD_GROWTH_PINE_TAIGA,
            Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.GROVE, Biome.WINDSWEPT_FOREST, Biome.MEADOW,
            Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA,
            Biome.SWAMP, Biome.MANGROVE_SWAMP);

    private static final Set<Biome> COLD_BIOMES = Set.of(
            Biome.SNOWY_PLAINS, Biome.ICE_SPIKES, Biome.SNOWY_TAIGA, Biome.SNOWY_BEACH,
            Biome.FROZEN_RIVER, Biome.FROZEN_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.GROVE, Biome.JAGGED_PEAKS, Biome.FROZEN_PEAKS);

    private final Map<String, Origin> origins = new LinkedHashMap<>();

    public OriginRegistry() {
        registerAll();
    }

    public Origin get(String id) {
        return id == null ? null : origins.get(id.toLowerCase());
    }

    public Collection<Origin> all() {
        return origins.values();
    }

    private void register(Origin origin) {
        origins.put(origin.id(), origin);
    }

    private static PotionEffect effect(PotionEffectType type, int duration, int amplifier) {
        return new PotionEffect(type, duration, amplifier, true, false, false);
    }

    private void registerAll() {
        registerHuman();
        registerOtter();
        registerDeer();
        registerBat();
        registerRat();
        registerDemon();
        registerWolf();
        registerFox();
        registerBear();
        registerRabbit();
        registerGoat();
        registerFeline();
        registerOwl();
        registerGryphon();
        registerDragon();
        registerMoth();
    }

    private void registerHuman() {
        register(new Origin("human", "Humano",
                "http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac",
                Material.PLAYER_HEAD,
                List.of(),
                List.of()));
    }

    private void registerOtter() {
        List<PowerType> powers = List.of(
                new AmphibiousPower("otter:amphibious"),
                new AttributeModifierPower("otter:aqua_affinity", Attribute.SUBMERGED_MINING_SPEED, 0.8),
                new WetEyesPower("otter:wet_eyes"),
                new FinsPower("otter:fins"),
                new LikeWaterPower("otter:like_water"),
                new LandStridePower("otter:land_stride", 0),
                new WaterDependencyPower("otter:water_dependency", 600L), // 10 minutes (seconds)
                new AttributeModifierPower("otter:small_body", Attribute.SCALE, -0.2));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Anfíbio", "Respira perfeitamente debaixo d'água e na terra."),
                new PowerInfo("Afinidade Aquática", "Minera na velocidade normal mesmo submerso."),
                new PowerInfo("Olhos Aquáticos", "Enxerga com clareza e ganha graça do golfinho na água."),
                new PowerInfo("Nadadeiras", "Nada muito mais rápido quando submerso."),
                new PowerInfo("Como a Água", "Maior controle de movimento embaixo d'água."),
                new PowerInfo("Passo Terrestre", "Velocidade I em terra firme, mesmo longe da água."),
                new PowerInfo("Dependência de Água", "Recebe fraqueza e lentidão se ficar 10 minutos sem tocar na água ou chuva."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 20% menor."));
        register(new Origin("otter", "Lontra", null, Material.KELP, powers, infos));
    }

    private void registerDeer() {
        List<PowerType> powers = List.of(
                new BiomeEffectPower("deer:forest_speed", DEER_HABITAT, true, 2, effect(PotionEffectType.SPEED, 60, 0)),
                new DamageMultiplierPower("deer:soft_landing", 0.5, DamageCause.FALL),
                new NaturalRunnerPower("deer:natural_runner"),
                new AttributeModifierPower("deer:nimble_legs", Attribute.STEP_HEIGHT, 0.5),
                new AttributeModifierPower("deer:alert_leap", Attribute.JUMP_STRENGTH, 0.1),
                new AttributeModifierPower("deer:fragile", Attribute.MAX_HEALTH, -2.0));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Agilidade Selvagem", "Mais rápido em florestas, taigas, savanas e pântanos."),
                new PowerInfo("Aterrissagem Suave", "Sofre 50% menos dano de queda."),
                new PowerInfo("Corredor Natural", "Correr em terrenos naturais (terra, grama, pedra) aumenta sua velocidade."),
                new PowerInfo("Pernas Ágeis", "Passa por cima de obstáculos baixos sem precisar pular."),
                new PowerInfo("Salto de Alerta", "Pula mais alto, sempre pronto para fugir do perigo."),
                new PowerInfo("Frágil", "Possui 1 coração a menos."));
        register(new Origin("deer", "Cervo", null, Material.OAK_SAPLING, powers, infos));
    }

    private void registerBat() {
        List<PowerType> powers = List.of(
                new NightVisionPower("bat:dark_vision"),
                new BatGlidePower("bat:glide"),
                new CaveMobilityPower("bat:cave_mobility"),
                new DayDazedPower("bat:sun_averse"),
                new AttributeModifierPower("bat:fragile", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("bat:tiny_body", Attribute.SCALE, -0.3));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Visão Noturna", "Visão noturna permanente."),
                new PowerInfo("Asas de Morcego", "Cai lentamente (planagem) ao cair no ar."),
                new PowerInfo("Mobilidade nas Cavernas", "Mais rápido e minera velozmente no fundo de cavernas escuras."),
                new PowerInfo("Aversão ao Sol", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Frágil", "Possui 2 corações a menos."),
                new PowerInfo("Corpo Minúsculo", "Seu corpo é 30% menor."));
        register(new Origin("bat", "Morcego", null, Material.BAT_SPAWN_EGG, powers, infos));
    }

    private void registerRat() {
        List<PowerType> powers = List.of(
                new AttributeModifierPower("rat:small_body", Attribute.SCALE, -0.3),
                new EvasionPower("rat:evasion"),
                new ActiveBuffPower("rat:scurry", 160L, org.bukkit.Sound.ENTITY_RABBIT_JUMP,
                        effect(PotionEffectType.SPEED, 30, 2)),
                new SilentStepsPower("rat:silent_steps"),
                new AttributeModifierPower("rat:swift", Attribute.MOVEMENT_SPEED, 0.03),
                new AttributeModifierPower("rat:fragile", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("rat:short_reach_block", Attribute.BLOCK_INTERACTION_RANGE, -1.0),
                new AttributeModifierPower("rat:short_reach_entity", Attribute.ENTITY_INTERACTION_RANGE, -1.0));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Corpo Pequeno", "Seu corpo é 30% menor."),
                new PowerInfo("Evasão", "Você tem 30% de chance de desviar completamente de projéteis."),
                new PowerInfo("Disparada", "Agachar + F: corrida explosiva (Velocidade III por 1,5s)."),
                new PowerInfo("Passos Silenciosos", "Passos silenciosos (cosmético)."),
                new PowerInfo("Veloz", "Mais rápido ao andar e correr."),
                new PowerInfo("Frágil", "Possui 2 corações a menos."),
                new PowerInfo("Alcance Curto", "Seus bracinhos não alcançam tão longe."));
        register(new Origin("rat", "Rato", null, Material.BEETROOT, powers, infos));
    }

    private void registerDemon() {
        List<PowerType> powers = List.of(
                new FireImmunityPower("demon:hellborn"),
                new AttributeModifierPower("demon:infernal_power", Attribute.ATTACK_DAMAGE, 2.0),
                new NightVisionPower("demon:dark_vision"),
                new HellPactPower("demon:hell_pact"),
                new DayDazedPower("demon:sun_averse"),
                new HolyVulnerabilityPower("demon:holy_vulnerability"),
                new InfernalVisualsPower("demon:infernal_visual"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Filho do Inferno", "Imune a fogo, lava e chão quente."),
                new PowerInfo("Poder Infernal", "Causa +1 coração de dano corpo a corpo."),
                new PowerInfo("Visão Noturna", "Visão noturna permanente."),
                new PowerInfo("Pacto Infernal", "Agachar + F: Ganha Força I por 10s. Depois sofre Fraqueza e Fome por 5s."),
                new PowerInfo("Aversão ao Sol", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Vulnerabilidade Sagrada", "Sofre o dobro de dano de magia e poções."));
        register(new Origin("demon", "Demônio", null, Material.NETHER_WART, powers, infos));
    }

    private void registerFeline() {
        List<PowerType> powers = List.of(
                new NoFallDamagePower("feline:acrobatics"),
                new AttributeModifierPower("feline:strong_ankles", Attribute.JUMP_STRENGTH, 0.1),
                new SilentStepsPower("feline:velvet_paws"),
                new AttributeModifierPower("feline:nine_lives", Attribute.MAX_HEALTH, -2.0),
                new dev.originspaper.power.shared.ScareEntityPower("feline:scare_creepers", EntityType.CREEPER),
                new NightVisionPower("feline:cat_vision"),
                new AttributeModifierPower("feline:feline_grace", Attribute.MOVEMENT_SPEED, 0.02),
                new dev.originspaper.power.origins.feline.PredatorInstinctPower("feline:predator_instinct"),
                new AttributeModifierPower("feline:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Acrobacia", "Não sofre dano de queda."),
                new PowerInfo("Tornozelos Fortes", "Pula um pouco mais alto."),
                new PowerInfo("Patas de Veludo", "Passos silenciosos (cosmético)."),
                new PowerInfo("Sete Vidas", "Possui 1 coração a menos."),
                new PowerInfo("Assusta Creepers", "Creepers têm medo de você: nunca te atacam e fogem se você se aproximar."),
                new PowerInfo("Visão Felina", "Visão noturna permanente."),
                new PowerInfo("Graça Felina", "Levemente mais rápido."),
                new PowerInfo("Instinto Predador", "Ganha velocidade extra ao perseguir criaturas hostis."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 10% menor."));
        register(new Origin("feline", "Felino", null, Material.OCELOT_SPAWN_EGG, powers, infos));
    }



    private void registerDragon() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("dragon:wings", "dragon_wings", "Asas de Dragão"),
                new DragonBreathPower("dragon:breath"),
                new RebornMagicPower("dragon:reborn_magic"),
                new DimensionAttributePower("dragon:nether_frailty", Environment.NETHER, Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("dragon:sharp_claws", Attribute.ATTACK_DAMAGE, 2.0),
                new CarnivoreDietPower("dragon:apex_predator"),
                new ArmorSlotRestrictPower("dragon:scaled_body", EquipmentSlot.CHEST,
                        item -> item.getType().name().endsWith("CHESTPLATE"),
                        "§cSuas escamas não permitem peitorais."),
                new AttributeModifierPower("dragon:large_body", Attribute.SCALE, 0.1),
                new DragonAuraPower("dragon:aura_visual"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Dragão", "Elytra permanente que volta sozinha."),
                new PowerInfo("Sopro do Dragão", "Agachar + F: sopra um cone de fogo que causa dano e incendeia os alvos à frente."),
                new PowerInfo("Magia Renascida", "Ganha regeneração no The End."),
                new PowerInfo("Fragilidade do Nether", "Perde 2 corações de vida máxima enquanto estiver no Nether."),
                new PowerInfo("Garras Afiadas", "Causa +1 coração de dano corpo a corpo."),
                new PowerInfo("Predador Supremo", "Só consegue comer carne."),
                new PowerInfo("Corpo Escamado", "Não pode usar peitorais (mas usa as asas)."),
                new PowerInfo("Corpo Grande", "Seu corpo é 10% maior."));
        register(new Origin("dragon", "Dragão", null, Material.DRAGON_HEAD, powers, infos));
    }

    private void registerWolf() {
        List<PowerType> powers = List.of(
                new NightVisionPower("wolf:canine_eyes"),
                new DayNightSpeedPower("wolf:runner"),
                new ExhaustionPower("wolf:metabolism", 0.008f, true),
                new PotionImmunityPower("wolf:poison_immunity", PotionEffectType.POISON),
                new CarnivoresBitePower("wolf:carnivore_bite"),
                new NightFangsPower("wolf:night_fangs"),
                new HuntersSensePower("wolf:hunters_sense"),
                new AlphaHowlPower("wolf:alpha_howl", 2400L, org.bukkit.Sound.ENTITY_WOLF_GROWL,
                        effect(PotionEffectType.ABSORPTION, 200, 1)));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Olhos Caninos", "Visão noturna permanente."),
                new PowerInfo("Corredor Noturno", "Mais rápido à noite, ainda ágil de dia."),
                new PowerInfo("Metabolismo de Lobo", "Gasta mais energia à noite."),
                new PowerInfo("Imunidade a Veneno", "Imune a veneno."),
                new PowerInfo("Mordida Carnívora", "Comer carne cura meio coração extra."),
                new PowerInfo("Presas Noturnas", "Causa +2 de dano à noite."),
                new PowerInfo("Sentido de Caçador", "Destaca criaturas próximas."),
                new PowerInfo("Uivo do Alfa", "Agachar + F: ganha absorção e uiva."));
        register(new Origin("wolf", "Lobo", null, Material.BONE, powers, infos));
    }



    private void registerOwl() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("owl:wings", "owl_wings", "Asas de Coruja"),
                new SilentFlightPower("owl:silent_flight"),
                new NightHunterPower("owl:night_hunter"),
                new EcholocationPower("owl:echolocation"),
                new NoFallDamagePower("owl:soft_landing"),
                new CarnivoreDietPower("owl:carnivore"),
                new DayDazedPower("owl:day_dazed"),
                new LightArmorOnlyPower("owl:light_frame", 5),
                new AttributeModifierPower("owl:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Coruja", "Elytra permanente que volta sozinha."),
                new PowerInfo("Voo Silencioso", "Reduz o alcance de detecção dos mobs ao planar."),
                new PowerInfo("Caçador Noturno", "De noite ganha Velocidade I, Visão Noturna e bônus de dano."),
                new PowerInfo("Ecolocalização", "Agachar + F: Revela entidades próximas através das paredes."),
                new PowerInfo("Aterrissagem Suave", "Não sofre dano de queda."),
                new PowerInfo("Carnívoro", "Apenas carne."),
                new PowerInfo("Cegueira Diurna", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Leve", "Sem armaduras pesadas."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 10% menor."));
        register(new Origin("owl", "Coruja", null, Material.FEATHER, powers, infos));
    }

    private void registerGryphon() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("gryphon:wings", "gryphon_wings", "Asas de Grifo"),
                new FlightLaunchPower("gryphon:take_flight", 400L, 2.5, 3L),
                new CarnivoreDietPower("gryphon:carnivore"),
                new FreshAirPower("gryphon:fresh_air"),
                new LightArmorOnlyPower("gryphon:need_mobility", 5),
                new AttributeModifierPower("gryphon:large_body", Attribute.SCALE, 0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Grifo", "Elytra permanente que volta sozinha."),
                new PowerInfo("Decolar", "Agachar + F: impulso forte para o céu e planagem."),
                new PowerInfo("Carnívoro", "Só consegue comer carne."),
                new PowerInfo("Ar Fresco", "Dorme mal perto do chão (acorda grogue); só descansa bem em grandes altitudes."),
                new PowerInfo("Mobilidade", "Não pode usar armadura pesada."),
                new PowerInfo("Corpo Grande", "Seu corpo é 10% maior."));
        register(new Origin("gryphon", "Grifo", null, Material.GOLDEN_HORSE_ARMOR, powers, infos));
    }

    private void registerGoat() {
        List<PowerType> powers = List.of(
                new LeapPower("goat:leap"),
                new RamPower("goat:ram"),
                new NoFallDamagePower("goat:sure_footed"),
                new PermanentEffectPower("goat:mountain_leap", PotionEffectType.JUMP_BOOST, 1),
                new DamageImmunityPower("goat:insulated", DamageCause.FREEZE),
                new AttributeModifierPower("goat:small", Attribute.MAX_HEALTH, -4.0),
                new GoatGrazePower("goat:graze"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Cabeçada", "Agachar + F: arremete em linha reta; explode em área ao atingir um inimigo ou ao parar."),
                new PowerInfo("Investida", "Golpes correndo causam dano extra e forte empurrão."),
                new PowerInfo("Passo Firme", "Nunca sofre dano de queda."),
                new PowerInfo("Salto Montanhês", "Pulo permanente: salta cerca de 2 blocos de altura."),
                new PowerInfo("Isolado", "Imune ao congelamento da neve em pó."),
                new PowerInfo("Pequeno", "Possui 2 corações a menos."),
                new PowerInfo("Estômago de Cabra", "Come qualquer coisa. Agachar + clique no ar com um bloco na mão para mordiscá-lo e recuperar meio ponto de fome."));
        register(new Origin("goat", "Cabra", null, Material.GOAT_HORN, powers, infos));
    }

    private void registerFox() {
        List<PowerType> powers = List.of(
                new PouncePower("fox:pounce"),
                new HuntPower("fox:hunt"),
                new AttributeModifierPower("fox:agility_speed", Attribute.MOVEMENT_SPEED, 0.03),
                new AttributeModifierPower("fox:agility_jump", Attribute.JUMP_STRENGTH, 0.05),
                new DamageMultiplierPower("fox:agility_fall", 0.6, DamageCause.FALL),
                new AttributeModifierPower("fox:foxiality", Attribute.LUCK, 1.0),
                new DamageImmunityPower("fox:fluffy_cold", DamageCause.FREEZE),
                new DamageMultiplierPower("fox:fluffy_fire", 1.5, DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA),
                new AttributeModifierPower("fox:smol", Attribute.MAX_HEALTH, -4.0),
                new DietPower("fox:unique_taste", true,
                        Set.of(Material.APPLE, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE,
                                Material.SWEET_BERRIES, Material.GLOW_BERRIES, Material.MELON_SLICE),
                        "§cVocê não tem gosto por este alimento."),
                new TimidityPower("fox:timidity"),
                new NoShieldPower("fox:weak_shield"),
                new AttributeModifierPower("fox:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Bote", "Agachar + F: salto que explode em área ao aterrissar, ou causa dano extra se acertar no ar."),
                new PowerInfo("Caçada", "Atacar a mesma presa repetidamente te fortalece."),
                new PowerInfo("Agilidade", "Mais rápido, pula mais e cai mais leve."),
                new PowerInfo("Raposice", "Mais sorte nos drops."),
                new PowerInfo("Fofo", "Imune ao frio, mas frágil ao fogo."),
                new PowerInfo("Pequenininho", "Possui 2 corações a menos."),
                new PowerInfo("Paladar Único", "Come apenas carnes e algumas frutas."),
                new PowerInfo("Timidez", "Fica fraco com pouca vida."),
                new PowerInfo("Fraco", "Não consegue usar escudo."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 10% menor."));
        register(new Origin("fox", "Raposa", null, Material.SWEET_BERRIES, powers, infos));
    }

    private void registerBear() {
        List<PowerType> powers = List.of(
                new MightyPawsPower("bear:mighty_paws", FOREST_BIOMES, 300L),
                new AttributeModifierPower("bear:thick_fur_armor", Attribute.ARMOR, 4.0),
                new DamageImmunityPower("bear:thick_fur_cold", DamageCause.FREEZE),
                new PermanentEffectPower("bear:resilient_hide", PotionEffectType.RESISTANCE, 0),
                new NutritionPower("bear:primal_appetite", FoodUtil::isRawMeat, 2.0),
                new HibernationPower("bear:hibernation", 2400L, org.bukkit.Sound.ENTITY_POLAR_BEAR_WARNING,
                        effect(PotionEffectType.STRENGTH, 200, 1), effect(PotionEffectType.RESISTANCE, 200, 0)),
                new BiomeParticlePower("bear:cold_frost_visual", COLD_BIOMES, true, 2, 6, Particle.SNOWFLAKE),
                new BiomeParticlePower("bear:away_from_forest_visual", FOREST_BIOMES, false, 3, 4,
                        Particle.ASH, Particle.SMOKE),
                new AttributeModifierPower("bear:towering", Attribute.ENTITY_INTERACTION_RANGE, 1.0),
                new CumbersomeClawsPower("bear:cumbersome_claws"),
                new AttributeModifierPower("bear:heavy_bones_speed", Attribute.MOVEMENT_SPEED, -0.015),
                new AttributeModifierPower("bear:heavy_bones_atk", Attribute.ATTACK_SPEED, -0.5),
                new ArmorSlotRestrictPower("bear:bulky_body", EquipmentSlot.CHEST,
                        item -> item.getType() == Material.NETHERITE_CHESTPLATE || item.getType() == Material.DIAMOND_CHESTPLATE,
                        "§cSeu corpo volumoso não cabe neste peitoral."),
                new ExhaustionPower("bear:large_appetite", 0.005f, false),
                new AttributeModifierPower("bear:large_body", Attribute.SCALE, 0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Patas Poderosas", "Mãos vazias causam +3 de dano e forte empurrão (perto da floresta)."),
                new PowerInfo("Pelo Grosso", "Armadura natural e imunidade ao frio."),
                new PowerInfo("Couro Resistente", "Resistência I permanente."),
                new PowerInfo("Apetite Primal", "Carne crua sacia muito mais."),
                new PowerInfo("Hibernação", "Agachar + F: força e resistência temporárias."),
                new PowerInfo("Estatura Imponente", "Alcance maior."),
                new PowerInfo("Garras Desajeitadas", "Não maneja espadas nem machados."),
                new PowerInfo("Ossos Pesados", "Mais lento e ataca devagar."),
                new PowerInfo("Marcação Territorial", "Longe de florestas por muito tempo, perde o bônus das garras (mas não perde vida)."),
                new PowerInfo("Corpo Volumoso", "Não usa peitorais de diamante ou netherite."),
                new PowerInfo("Grande Apetite", "Fica com fome mais rápido."),
                new PowerInfo("Corpo Grande", "Seu corpo é 10% maior."));
        register(new Origin("bear", "Urso", null, Material.HONEYCOMB, powers, infos));
    }

    private void registerRabbit() {
        List<PowerType> powers = List.of(
                new NightVisionPower("rabbit:high_carotene"),
                new AttributeModifierPower("rabbit:prey_health", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("rabbit:prey_speed", Attribute.MOVEMENT_SPEED, 0.04),
                new DietPower("rabbit:special_diet", false, Set.of(Material.CARROT, Material.GOLDEN_CARROT),
                        "§cVocê só consegue comer cenouras."),
                new NutritionPower("rabbit:carrot_boost",
                        m -> m == Material.CARROT || m == Material.GOLDEN_CARROT, 1.5),
                new PermanentEffectPower("rabbit:bouncing", PotionEffectType.JUMP_BOOST, 1),
                new PredatorScentPower("rabbit:predator_scent"),
                new AttributeModifierPower("rabbit:small_body", Attribute.SCALE, -0.2));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Rico em Caroteno", "Visão noturna permanente."),
                new PowerInfo("Presa", "Menos vida, porém mais veloz."),
                new PowerInfo("Dieta Especial", "Só come cenouras, que alimentam 50% a mais."),
                new PowerInfo("Aura Saltitante", "Impulso de pulo permanente."),
                new PowerInfo("Faro de Predador", "Sente lobos, raposas e gatos próximos e os destaca, dando tempo de fugir."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 20% menor."));
        register(new Origin("rabbit", "Coelho", null, Material.RABBIT_FOOT, powers, infos));
    }

    private void registerMoth() {
        List<PowerType> powers = List.of(
                new FlutterGlidePower("moth:flutter_wings"),
                new PollinatorPower("moth:pollinator"),
                new DrawnToLightPower("moth:drawn_to_light"),
                new FriendOfBeesPower("moth:friend_of_bees"),
                new MothVisualsPower("moth:wing_dust_visual"),
                new AttributeModifierPower("moth:fragile", Attribute.MAX_HEALTH, -4.0),
                new ArmorMaterialRestrictPower("moth:delicate_body",
                        item -> item.getType().name().startsWith("DIAMOND_")
                                || item.getType().name().startsWith("NETHERITE_"),
                        "§cSeu corpo delicado não suporta armaduras pesadas."));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas Trêmulas", "Suas asas delicadas reduzem sua velocidade de queda permanentemente."),
                new PowerInfo("Voo Gracioso", "Plana suavemente durante quedas e nunca sofre dano de queda."),
                new PowerInfo("Polinizadora", "Agachada, faz plantações e mudas próximas crescerem como se recebessem Farinha de Osso, e o pólen cega inimigos por perto."),
                new PowerInfo("Atraída pela Luz", "Recebe Regeneração I perto de luz artificial (tochas, lanternas, glowstone...) e fica lenta na escuridão total."),
                new PowerInfo("Amiga das Abelhas", "Abelhas nunca ficam agressivas com você."),
                new PowerInfo("Frágil", "Possui 2 corações a menos."),
                new PowerInfo("Corpo Delicado", "Não pode usar armaduras de Diamante ou Netherite."));
        register(new Origin("moth", "Mariposa", null, Material.PHANTOM_MEMBRANE, powers, infos));
    }
}
