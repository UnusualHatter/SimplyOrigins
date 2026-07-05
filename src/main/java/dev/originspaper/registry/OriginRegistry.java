package dev.originspaper.registry;

import dev.originspaper.api.Origin;
import dev.originspaper.api.Origin.PowerInfo;
import dev.originspaper.api.PowerType;
import dev.originspaper.api.TickCadence;
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
import dev.originspaper.power.origins.equine.EquineProgressionPower;
import dev.originspaper.power.origins.equine.GallopPower;
import dev.originspaper.power.origins.equine.IndoorClumsyPower;
import dev.originspaper.power.origins.equine.RearKickPower;
import dev.originspaper.power.origins.equine.TrailRunnerPower;
import dev.originspaper.power.origins.fox.HuntPower;
import dev.originspaper.power.origins.fox.PouncePower;
import dev.originspaper.power.origins.goat.GoatGrazePower;
import dev.originspaper.power.origins.goat.LeapPower;
import dev.originspaper.power.origins.goat.RamPower;
import dev.originspaper.power.origins.gryphon.FreshAirPower;
import dev.originspaper.power.origins.moth.DrawnToLightPower;
import dev.originspaper.power.origins.moth.FlutterGlidePower;
import dev.originspaper.power.origins.moth.FriendOfBeesPower;
import dev.originspaper.power.origins.moth.MothVisualsPower;
import dev.originspaper.power.origins.moth.PollinatorPower;
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
import dev.originspaper.power.origins.rabbit.RabbitDashPower;
import dev.originspaper.power.origins.raccoon.NightScavengerPower;
import dev.originspaper.power.origins.raccoon.RaccoonProgressionPower;
import dev.originspaper.power.origins.raccoon.RaccoonScurryPower;
import dev.originspaper.power.origins.raccoon.ScavengersLuckPower;
import dev.originspaper.power.origins.raccoon.SneakyPawsPower;
import dev.originspaper.power.origins.redpanda.ClimberPower;
import dev.originspaper.power.origins.redpanda.FluffyTailPower;
import dev.originspaper.power.origins.redpanda.RedPandaProgressionPower;
import dev.originspaper.power.origins.redpanda.TreeAcrobatPower;
import dev.originspaper.power.origins.shark.ApexSwimmerPower;
import dev.originspaper.power.origins.shark.BloodSensePower;
import dev.originspaper.power.origins.shark.DrySkinPower;
import dev.originspaper.power.origins.shark.FeedingRushPower;
import dev.originspaper.power.origins.shark.LandClumsyPower;
import dev.originspaper.power.origins.shark.PredatorBitePower;
import dev.originspaper.power.origins.shark.SharkProgressionPower;
import dev.originspaper.power.origins.wolf.AlphaHowlPower;
import dev.originspaper.power.origins.wolf.CarnivoresBitePower;
import dev.originspaper.power.origins.wolf.DayNightSpeedPower;
import dev.originspaper.power.origins.wolf.HuntersSensePower;
import dev.originspaper.power.origins.wolf.NightFangsPower;
import dev.originspaper.power.origins.human.HumanProgressionPower;
import dev.originspaper.power.origins.otter.OtterProgressionPower;
import dev.originspaper.power.origins.deer.DeerProgressionPower;
import dev.originspaper.power.origins.bat.BatProgressionPower;
import dev.originspaper.power.origins.rat.RatProgressionPower;
import dev.originspaper.power.origins.rat.ScurryPower;
import dev.originspaper.power.origins.demon.DemonProgressionPower;
import dev.originspaper.power.origins.fox.FoxProgressionPower;
import dev.originspaper.power.origins.bear.BearProgressionPower;
import dev.originspaper.power.origins.rabbit.RabbitProgressionPower;
import dev.originspaper.power.origins.goat.GoatProgressionPower;
import dev.originspaper.power.origins.feline.FelineProgressionPower;
import dev.originspaper.power.origins.owl.OwlProgressionPower;
import dev.originspaper.power.origins.gryphon.GryphonProgressionPower;
import dev.originspaper.power.origins.dragon.DragonProgressionPower;
import dev.originspaper.power.origins.moth.MothProgressionPower;
import dev.originspaper.power.shared.AmphibiousPower;
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
import dev.originspaper.power.shared.FallDamageThresholdPower;
import dev.originspaper.power.shared.FireImmunityPower;
import dev.originspaper.power.shared.FlightLaunchPower;
import dev.originspaper.power.shared.HerbivoreDietPower;
import dev.originspaper.power.shared.LightArmorOnlyPower;
import dev.originspaper.power.shared.NightVisionPower;
import dev.originspaper.power.shared.NoFallDamagePower;
import dev.originspaper.power.shared.NoShieldPower;
import dev.originspaper.power.shared.NutritionPower;
import dev.originspaper.power.shared.PermanentEffectPower;
import dev.originspaper.power.shared.PotionImmunityPower;
import dev.originspaper.power.shared.SilentStepsPower;
import dev.originspaper.power.shared.TimidityPower;
import dev.originspaper.power.shared.TrashGourmetPower;
import dev.originspaper.power.shared.WaterSpeedPenaltyPower;
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

/** Builds and stores the 16 origins, in display order (Human first). */
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
        // Per-origin kill switch: origins.<id>.enabled: false hides the origin entirely (GUI,
        // /origin set, selection). Players with a disabled saved origin are asked to reselect.
        if (!dev.originspaper.OriginsPaper.instance().getConfig()
                .getBoolean("origins." + origin.id() + ".enabled", true)) {
            dev.originspaper.OriginsPaper.instance().log()
                    .info("Origin '" + origin.id() + "' disabled in config.yml — not registered.");
            return;
        }
        origins.put(origin.id(), origin);
    }

    private static PotionEffect effect(PotionEffectType type, int duration, int amplifier) {
        return new PotionEffect(type, duration, amplifier, true, false, false);
    }

    // Config accessors for balance values under origins.<id>.* — read at registry construction,
    // so /origin reload (which rebuilds the registry after reloadConfig) picks up changes.
    private static int cfgInt(String path, int def) {
        return dev.originspaper.OriginsPaper.instance().getConfig().getInt(path, def);
    }

    private static double cfgDouble(String path, double def) {
        return dev.originspaper.OriginsPaper.instance().getConfig().getDouble(path, def);
    }

    private static boolean cfgBool(String path, boolean def) {
        return dev.originspaper.OriginsPaper.instance().getConfig().getBoolean(path, def);
    }

    /** Converts a whole number of seconds from config into game ticks. */
    private static long cfgSecondsToTicks(String path, int defSeconds) {
        return cfgInt(path, defSeconds) * 20L;
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
        registerRedPanda();
        registerShark();
        registerRaccoon();
        registerEquine();
    }

    private void registerHuman() {
        register(new Origin("human", "Humano",
                "http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac",
                Material.PLAYER_HEAD,
                List.of(new HumanProgressionPower("human:progression")),
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
                new AttributeModifierPower("otter:small_body", Attribute.SCALE, -0.2),
                new OtterProgressionPower("otter:progression"));
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
                new BiomeEffectPower("deer:forest_speed", DEER_HABITAT, true,
                        TickCadence.EVERY_2_SECONDS, effect(PotionEffectType.SPEED, 60, 0)),
                new DamageMultiplierPower("deer:soft_landing", 0.5, DamageCause.FALL),
                new NaturalRunnerPower("deer:natural_runner"),
                new AttributeModifierPower("deer:nimble_legs", Attribute.STEP_HEIGHT, 0.5),
                new AttributeModifierPower("deer:alert_leap", Attribute.JUMP_STRENGTH, 0.1),
                new AttributeModifierPower("deer:fragile", Attribute.MAX_HEALTH, -2.0),
                new DeerProgressionPower("deer:progression"));
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
                new AttributeModifierPower("bat:tiny_body", Attribute.SCALE, -0.3),
                new BatProgressionPower("bat:progression"));
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
                new ScurryPower("rat:scurry", 160L, org.bukkit.Sound.ENTITY_RABBIT_JUMP,
                        effect(PotionEffectType.SPEED, 30, 2)),
                new SilentStepsPower("rat:silent_steps"),
                new AttributeModifierPower("rat:swift", Attribute.MOVEMENT_SPEED, 0.03),
                new AttributeModifierPower("rat:fragile", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("rat:short_reach_block", Attribute.BLOCK_INTERACTION_RANGE, -1.0),
                new AttributeModifierPower("rat:short_reach_entity", Attribute.ENTITY_INTERACTION_RANGE, -1.0),
                new RatProgressionPower("rat:progression"));
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
                new InfernalVisualsPower("demon:infernal_visual"),
                new DemonProgressionPower("demon:progression"));
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
                new AttributeModifierPower("feline:small_body", Attribute.SCALE, -0.1),
                new FelineProgressionPower("feline:progression"));
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
                new DragonAuraPower("dragon:aura_visual"),
                new DragonProgressionPower("dragon:progression"));
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
                new NightVisionPower("owl:night_vision"),
                new NightHunterPower("owl:night_hunter"),
                new EcholocationPower("owl:echolocation"),
                new NoFallDamagePower("owl:soft_landing"),
                new CarnivoreDietPower("owl:carnivore"),
                new DayDazedPower("owl:day_dazed"),
                new ArmorMaterialRestrictPower("owl:no_netherite",
                        item -> item.getType().name().startsWith("NETHERITE_"),
                        "§cSeu corpo leve não suporta armaduras de netherite."),
                new AttributeModifierPower("owl:small_body", Attribute.SCALE, -0.1),
                new OwlProgressionPower("owl:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Coruja", "Elytra permanente que volta sozinha."),
                new PowerInfo("Voo Silencioso", "Reduz o alcance de detecção dos mobs ao planar."),
                new PowerInfo("Visão Noturna", "Enxerga no escuro a qualquer hora, de dia ou de noite."),
                new PowerInfo("Caçador Noturno", "De noite ganha Velocidade I e bônus de dano."),
                new PowerInfo("Ecolocalização", "Agachar + F: Revela entidades próximas através das paredes."),
                new PowerInfo("Aterrissagem Suave", "Não sofre dano de queda."),
                new PowerInfo("Carnívoro", "Apenas carne."),
                new PowerInfo("Cegueira Diurna", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Leve", "Pode usar armaduras até diamante, mas não netherite."),
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
                new AttributeModifierPower("gryphon:large_body", Attribute.SCALE, 0.1),
                new GryphonProgressionPower("gryphon:progression"));
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
                new GoatGrazePower("goat:graze"),
                new GoatProgressionPower("goat:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Cabeçada", "Agachar + F: arremete em linha reta; explode em área ao atingir um inimigo ou ao parar."),
                new PowerInfo("Investida", "Golpes correndo causam dano extra e forte empurrão."),
                new PowerInfo("Passo Firme", "Nunca sofre dano de queda."),
                new PowerInfo("Salto Montanhês", "Pulo permanente: salta cerca de 2 blocos de altura."),
                new PowerInfo("Isolado", "Imune ao congelamento da neve em pó."),
                new PowerInfo("Pequeno", "Possui 2 corações a menos."),
                new PowerInfo("Estômago de Cabra", "Come quase qualquer coisa (menos comida, ferramentas, armas e armaduras). Agachar + clique com a direita no ar com o item na mão para mordiscá-lo e recuperar meio ponto de fome."));
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
                new NutritionPower("fox:berry_appetite",
                        m -> m == Material.SWEET_BERRIES, 2.0), // frutinhas vermelhas saciam o dobro
                new TimidityPower("fox:timidity"),
                new NoShieldPower("fox:weak_shield"),
                new AttributeModifierPower("fox:small_body", Attribute.SCALE, -0.1),
                new FoxProgressionPower("fox:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Bote", "Agachar + F: salto que explode em área ao aterrissar, ou causa dano extra se acertar no ar."),
                new PowerInfo("Caçada", "Atacar a mesma presa repetidamente te fortalece."),
                new PowerInfo("Agilidade", "Mais rápido, pula mais e cai mais leve."),
                new PowerInfo("Raposice", "Mais sorte nos drops."),
                new PowerInfo("Fofo", "Imune ao frio, mas frágil ao fogo."),
                new PowerInfo("Pequenininho", "Possui 2 corações a menos."),
                new PowerInfo("Paladar Único", "Come apenas carnes e algumas frutas."),
                new PowerInfo("Gosto por Frutas-silvestres", "Frutas-silvestres (amoras) saciam o dobro de fome."),
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
                new BiomeParticlePower("bear:cold_frost_visual", COLD_BIOMES, true,
                        TickCadence.EVERY_2_SECONDS, 6, Particle.SNOWFLAKE),
                new BiomeParticlePower("bear:away_from_forest_visual", FOREST_BIOMES, false,
                        TickCadence.EVERY_3_SECONDS, 4, Particle.ASH, Particle.SMOKE),
                new AttributeModifierPower("bear:towering", Attribute.ENTITY_INTERACTION_RANGE, 1.0),
                new CumbersomeClawsPower("bear:cumbersome_claws"),
                new AttributeModifierPower("bear:heavy_bones_speed", Attribute.MOVEMENT_SPEED, -0.015),
                new AttributeModifierPower("bear:heavy_bones_atk", Attribute.ATTACK_SPEED, -0.5),
                new ArmorSlotRestrictPower("bear:bulky_body", EquipmentSlot.CHEST,
                        item -> item.getType() == Material.NETHERITE_CHESTPLATE || item.getType() == Material.DIAMOND_CHESTPLATE,
                        "§cSeu corpo volumoso não cabe neste peitoral."),
                new ExhaustionPower("bear:large_appetite", 0.005f, false),
                new AttributeModifierPower("bear:large_body", Attribute.SCALE, 0.1),
                new BearProgressionPower("bear:progression"));
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
                new FallDamageThresholdPower("rabbit:light_landing", 5.0),
                new RabbitDashPower("rabbit:dash"),
                new PredatorScentPower("rabbit:predator_scent"),
                new AttributeModifierPower("rabbit:small_body", Attribute.SCALE, -0.2),
                new RabbitProgressionPower("rabbit:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Rico em Caroteno", "Visão noturna permanente."),
                new PowerInfo("Presa", "Menos vida, porém mais veloz."),
                new PowerInfo("Dieta Especial", "Só come cenouras, que alimentam 50% a mais."),
                new PowerInfo("Aura Saltitante", "Impulso de pulo permanente."),
                new PowerInfo("Aterrissagem Leve", "Não sofre dano em quedas de até 5 blocos."),
                new PowerInfo("Disparada", "Agachar + F: um salto rápido para frente, só para movimentação (recarga de 7s)."),
                new PowerInfo("Faro de Predador", "Sente lobos, raposas e gatos próximos e os destaca, dando tempo de fugir."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 20% menor."));
        register(new Origin("rabbit", "Coelho", null, Material.RABBIT_FOOT, powers, infos));
    }

    private void registerRedPanda() {
        List<PowerType> powers = new java.util.ArrayList<>(List.of(
                new FluffyTailPower("red_panda:fluffy_tail",
                        cfgSecondsToTicks("origins.red_panda.fluffy-tail-cooldown-seconds", 25),
                        cfgInt("origins.red_panda.fluffy-tail-speed-seconds", 4) * 20,
                        cfgInt("origins.red_panda.fluffy-tail-slow-falling-seconds", 5) * 20),
                new NutritionPower("red_panda:berry_forager",
                        m -> m == Material.SWEET_BERRIES || m == Material.GLOW_BERRIES, 2.0),
                new DamageImmunityPower("red_panda:cold_fur", DamageCause.FREEZE),
                new AttributeModifierPower("red_panda:fragile", Attribute.MAX_HEALTH,
                        -2.0 * cfgInt("origins.red_panda.max-health-penalty-hearts", 1)),
                new TimidityPower("red_panda:timid"),
                new AttributeModifierPower("red_panda:small_body", Attribute.SCALE, -0.15),
                new RedPandaProgressionPower("red_panda:progression")));
        if (cfgBool("origins.red_panda.tree-acrobat-enabled", true)) {
            powers.add(0, new TreeAcrobatPower("red_panda:tree_acrobat"));
        }
        if (cfgBool("origins.red_panda.climber-enabled", true)) {
            powers.add(1, new ClimberPower("red_panda:climber"));
        }
        List<PowerInfo> infos = List.of(
                new PowerInfo("Acrobata das Árvores", "Velocidade sobre troncos, folhas, bambu, musgo e chão de floresta."),
                new PowerInfo("Escalador", "Agachado encostado em troncos, folhas ou bambu, sobe lentamente — e desce planando de leve."),
                new PowerInfo("Cauda Fofa", "Agachar + F: Velocidade II por 4s e Queda Lenta por 5s — fuga, salto entre árvores e aterrissagem (recarga 25s)."),
                new PowerInfo("Forrageadora", "Frutinhas e frutas-brilhantes saciam o dobro de fome."),
                new PowerInfo("Pelagem Quente", "Imune ao congelamento da neve em pó."),
                new PowerInfo("Frágil", "Possui 1 coração a menos."),
                new PowerInfo("Tímido", "Fica fraco com pouca vida."),
                new PowerInfo("Corpo Leve", "Peitoral de diamante/netherite desativa a acrobacia e a escalada."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 15% menor."));
        register(new Origin("red_panda", "Panda-Vermelho", null, Material.BAMBOO, powers, infos));
    }

    private void registerShark() {
        List<PowerType> powers = List.of(
                new AmphibiousPower("shark:water_breathing"),
                new ApexSwimmerPower("shark:apex_swimmer"),
                new PredatorBitePower("shark:predator_bite",
                        cfgDouble("origins.shark.predator-bite-extra-damage", 2.0)),
                new BloodSensePower("shark:blood_sense",
                        cfgDouble("origins.shark.blood-sense-radius", 16.0),
                        cfgInt("origins.shark.blood-sense-health-percent", 60) / 100.0),
                new FeedingRushPower("shark:feeding_rush",
                        cfgSecondsToTicks("origins.shark.feeding-rush-cooldown-seconds", 30),
                        cfgDouble("origins.shark.feeding-rush-damage", 4.0)),
                new DrySkinPower("shark:dry_skin",
                        cfgInt("origins.shark.dry-skin-warning-seconds", 180),
                        cfgInt("origins.shark.dry-skin-penalty-seconds", 300)),
                new LandClumsyPower("shark:land_clumsy", -0.08),
                new CarnivoreDietPower("shark:carnivore"),
                new AttributeModifierPower("shark:large_body", Attribute.SCALE, 0.1),
                new SharkProgressionPower("shark:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Respiração Aquática", "Respira perfeitamente debaixo d'água."),
                new PowerInfo("Nadador Supremo", "Nada muito mais rápido que qualquer criatura na água."),
                new PowerInfo("Mordida Predadora", "Causa +1 coração de dano corpo a corpo enquanto estiver na água."),
                new PowerInfo("Faro de Sangue", "Molhado, sente criaturas feridas num raio de 16 blocos — elas brilham para você."),
                new PowerInfo("Avanço Voraz", "Agachar + F (na água): investida submarina que morde a primeira presa no caminho (recarga 30s)."),
                new PowerInfo("Pele Seca", "Longe da água ou chuva por 3 min fica fraco; por 5 min, também lento. Molhar-se zera o tempo."),
                new PowerInfo("Desajeitado em Terra", "Levemente mais lento fora da água."),
                new PowerInfo("Carnívoro", "Só consegue comer carne e peixe."),
                new PowerInfo("Corpo Grande", "Seu corpo é 10% maior."));
        register(new Origin("shark", "Tubarão", null, Material.PRISMARINE_SHARD, powers, infos));
    }

    private void registerRaccoon() {
        List<PowerType> powers = new java.util.ArrayList<>(List.of(
                new NightScavengerPower("raccoon:night_scavenger"),
                new TrashGourmetPower("raccoon:trash_gourmet", 2.0f),
                new SneakyPawsPower("raccoon:sneaky_paws"),
                new RaccoonScurryPower("raccoon:scurry",
                        cfgSecondsToTicks("origins.raccoon.scurry-cooldown-seconds", 20),
                        cfgInt("origins.raccoon.scurry-speed-seconds", 3) * 20,
                        cfgInt("origins.raccoon.scurry-invisibility-seconds", 1) * 20),
                new AttributeModifierPower("raccoon:fragile", Attribute.MAX_HEALTH,
                        -2.0 * cfgInt("origins.raccoon.max-health-penalty-hearts", 1)),
                new AttributeModifierPower("raccoon:small_body", Attribute.SCALE, -0.15),
                new RaccoonProgressionPower("raccoon:progression")));
        if (cfgBool("origins.raccoon.scavenger-luck-enabled", true)) {
            powers.add(2, new ScavengersLuckPower("raccoon:scavengers_luck",
                    cfgDouble("origins.raccoon.scavenger-luck-chance", 0.07)));
        }
        List<PowerInfo> infos = List.of(
                new PowerInfo("Catador Noturno", "Enxerga no escuro e durante a noite."),
                new PowerInfo("Gourmet do Lixo", "Comidas duvidosas não envenenam e dão saturação extra."),
                new PowerInfo("Sorte de Catador", "Ao caçar ou vasculhar terreno natural, pode achar pequenos itens úteis."),
                new PowerInfo("Patas Furtivas", "Agachado e sem peitoral pesado, monstros só percebem você de perto."),
                new PowerInfo("Esgueirada", "Agachar + F: Velocidade II por 3s e invisibilidade breve; atacar encerra o efeito (recarga 20s)."),
                new PowerInfo("Frágil", "Possui 1 coração a menos."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 15% menor."),
                new PowerInfo("Sem Armadura Pesada", "Peitoral de diamante/netherite desativa sua furtividade."));
        register(new Origin("raccoon", "Guaxinim", null, Material.BUNDLE, powers, infos));
    }

    private void registerEquine() {
        List<PowerType> powers = List.of(
                new GallopPower("equine:gallop",
                        cfgInt("origins.equine.gallop-stage-1-seconds", 3),
                        cfgInt("origins.equine.gallop-stage-2-seconds", 6)),
                new PermanentEffectPower("equine:strong_legs", PotionEffectType.JUMP_BOOST, 0),
                new DamageMultiplierPower("equine:sure_footed",
                        1.0 - cfgInt("origins.equine.fall-damage-reduction-percent", 50) / 100.0,
                        DamageCause.FALL),
                new TrailRunnerPower("equine:trail_runner"),
                new RearKickPower("equine:rear_kick",
                        cfgSecondsToTicks("origins.equine.rear-kick-cooldown-seconds", 25),
                        cfgDouble("origins.equine.rear-kick-radius", 3.0),
                        cfgDouble("origins.equine.rear-kick-damage", 2.0)),
                new HerbivoreDietPower("equine:herbivore",
                        "§cSeu estômago herbívoro rejeita carne e peixe."),
                new WaterSpeedPenaltyPower("equine:bad_swimmer", -0.15),
                new IndoorClumsyPower("equine:indoor_clumsy"),
                new EquineProgressionPower("equine:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Galope", "Correr sem parar cria impulso: após 3s fica veloz; após 6s ao ar livre alcança Velocidade II."),
                new PowerInfo("Pernas Fortes", "Salta um pouco mais alto."),
                new PowerInfo("Passo Seguro", "Sofre 50% menos dano de queda."),
                new PowerInfo("Corredor de Trilha", "Move-se um pouco melhor sobre grama, terra, caminhos e feno."),
                new PowerInfo("Coice", "Agachar + F: golpe fraco que arremessa alvos atrás de você (recarga 25s)."),
                new PowerInfo("Herbívoro", "Não consegue comer carne ou peixe."),
                new PowerInfo("Nadador Ruim", "Move-se 15% mais devagar na água."),
                new PowerInfo("Desajeitado em Espaços Apertados", "Fica levemente lento sob tetos muito baixos."),
                new PowerInfo("Peso da Armadura", "Peitoral de diamante/netherite impede o Galope."));
        register(new Origin("equine", "Equino", null, Material.SADDLE, powers, infos));
    }

    private void registerMoth() {
        List<PowerType> powers = List.of(
                new FlutterGlidePower("moth:flutter_wings"),
                new PollinatorPower("moth:pollinator"),
                new DrawnToLightPower("moth:drawn_to_light"),
                new FriendOfBeesPower("moth:friend_of_bees"),
                new MothVisualsPower("moth:wing_dust_visual"),
                new NutritionPower("moth:glow_diet",
                        m -> m == Material.GLOW_BERRIES, 2.0), // glow berries sate twice as much
                new AttributeModifierPower("moth:fragile", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("moth:small_body", Attribute.SCALE, -0.2),
                new ArmorMaterialRestrictPower("moth:delicate_body",
                        item -> item.getType().name().startsWith("DIAMOND_")
                                || item.getType().name().startsWith("NETHERITE_"),
                        "§cSeu corpo delicado não suporta armaduras pesadas."),
                new MothProgressionPower("moth:progression"));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas Trêmulas", "Suas asas delicadas reduzem sua velocidade de queda permanentemente."),
                new PowerInfo("Voo Gracioso", "Plana suavemente durante quedas e nunca sofre dano de queda."),
                new PowerInfo("Polinizadora", "Agachada, faz plantações e mudas próximas crescerem como se recebessem Farinha de Osso, e o pólen cega inimigos por perto."),
                new PowerInfo("Atraída pela Luz", "Recebe Regeneração I perto de luz artificial (tochas, lanternas, glowstone...) enquanto estiver ferida e alimentada, e fica lenta na escuridão total."),
                new PowerInfo("Amiga das Abelhas", "Abelhas nunca ficam agressivas com você."),
                new PowerInfo("Apetite Luminoso", "Frutas-brilhantes saciam o dobro de fome e dão saturação extra."),
                new PowerInfo("Frágil", "Possui 2 corações a menos."),
                new PowerInfo("Corpo Pequeno", "Seu corpo é 20% menor."),
                new PowerInfo("Corpo Delicado", "Não pode usar armaduras de Diamante ou Netherite."));
        register(new Origin("moth", "Mariposa", null, Material.PHANTOM_MEMBRANE, powers, infos));
    }
}
