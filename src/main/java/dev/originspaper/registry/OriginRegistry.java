package dev.originspaper.registry;

import dev.originspaper.api.Origin;
import dev.originspaper.api.Origin.PowerInfo;
import dev.originspaper.api.PowerType;
import dev.originspaper.power.origins.bat.BatGlidePower;
import dev.originspaper.power.origins.bat.CaveMobilityPower;
import dev.originspaper.power.origins.bear.CumbersomeClawsPower;
import dev.originspaper.power.origins.bear.MightyPawsPower;
import dev.originspaper.power.origins.deer.NaturalRunnerPower;
import dev.originspaper.power.origins.demon.HellPactPower;
import dev.originspaper.power.origins.demon.HolyVulnerabilityPower;
import dev.originspaper.power.origins.dragon.DragonBreathPower;
import dev.originspaper.power.origins.dragon.RebornMagicPower;
import dev.originspaper.power.origins.feline.WeakArmsPower;
import dev.originspaper.power.origins.fox.HuntPower;
import dev.originspaper.power.origins.fox.PouncePower;
import dev.originspaper.power.origins.fox.TimidityPower;
import dev.originspaper.power.origins.goat.BracePower;
import dev.originspaper.power.origins.goat.LeapPower;
import dev.originspaper.power.origins.goat.RamPower;
import dev.originspaper.power.origins.gryphon.FreshAirPower;
import dev.originspaper.power.origins.otter.AmphibiousPower;
import dev.originspaper.power.origins.otter.FinsPower;
import dev.originspaper.power.origins.otter.LikeWaterPower;
import dev.originspaper.power.origins.otter.WaterDependencyPower;
import dev.originspaper.power.origins.otter.WetEyesPower;
import dev.originspaper.power.origins.rat.EvasionPower;
import dev.originspaper.power.origins.owl.DayDazedPower;
import dev.originspaper.power.origins.owl.EcholocationPower;
import dev.originspaper.power.origins.owl.NightHunterPower;
import dev.originspaper.power.origins.owl.PredatorDivePower;
import dev.originspaper.power.origins.owl.SilentFlightPower;
import dev.originspaper.power.origins.rabbit.ReplenishPower;
import dev.originspaper.power.origins.wolf.CarnivoresBitePower;
import dev.originspaper.power.origins.wolf.DayNightSpeedPower;
import dev.originspaper.power.origins.wolf.HuntersSensePower;
import dev.originspaper.power.origins.wolf.NightFangsPower;
import dev.originspaper.power.shared.ActiveBuffPower;
import dev.originspaper.power.shared.ArmorSlotRestrictPower;
import dev.originspaper.power.shared.AttributeModifierPower;
import dev.originspaper.power.shared.BiomeEffectPower;
import dev.originspaper.power.shared.CarnivoreDietPower;
import dev.originspaper.power.shared.DamageImmunityPower;
import dev.originspaper.power.shared.DamageMultiplierPower;
import dev.originspaper.power.shared.DietPower;
import dev.originspaper.power.shared.DivePower;
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

    private static final Set<Biome> HOT_BIOMES = Set.of(
            Biome.DESERT, Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA,
            Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.WOODED_BADLANDS);

    private static final Set<Biome> FOREST_BIOMES = Set.of(
            Biome.FOREST, Biome.TAIGA, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.OLD_GROWTH_PINE_TAIGA,
            Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.GROVE, Biome.WINDSWEPT_FOREST, Biome.MEADOW);

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
    }

    private void registerHuman() {
        register(new Origin("human", "Human",
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
                new WaterDependencyPower("otter:water_dependency", 600L), // 10 minutes (seconds)
                new AttributeModifierPower("otter:small_body", Attribute.SCALE, -0.2));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Amphibious", "Respira perfeitamente debaixo d'água e na terra."),
                new PowerInfo("Aqua Affinity", "Minera na velocidade normal mesmo submerso."),
                new PowerInfo("Wet Eyes", "Enxerga com clareza e ganha graça do golfinho na água."),
                new PowerInfo("Fins", "Nada muito mais rápido quando submerso."),
                new PowerInfo("Like Water", "Maior controle de movimento embaixo d'água."),
                new PowerInfo("Water Dependency", "Recebe fraqueza e lentidão se ficar 10 minutos sem tocar na água ou chuva."),
                new PowerInfo("Small Body", "Seu corpo é 20% menor."));
        register(new Origin("otter", "Otter", null, Material.KELP, powers, infos));
    }

    private void registerDeer() {
        List<PowerType> powers = List.of(
                new BiomeEffectPower("deer:forest_speed", FOREST_BIOMES, true, 2, effect(PotionEffectType.SPEED, 60, 0)),
                new DamageMultiplierPower("deer:soft_landing", 0.5, DamageCause.FALL),
                new NaturalRunnerPower("deer:natural_runner"),
                new AttributeModifierPower("deer:nimble_legs", Attribute.STEP_HEIGHT, 0.5),
                new AttributeModifierPower("deer:alert_leap", Attribute.JUMP_STRENGTH, 0.1),
                new AttributeModifierPower("deer:fragile", Attribute.MAX_HEALTH, -2.0));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Forest Agility", "Mais rápido em florestas e taigas."),
                new PowerInfo("Soft Landing", "Sofre 50% menos dano de queda."),
                new PowerInfo("Natural Runner", "Correr em terrenos naturais (terra, grama, pedra) aumenta sua velocidade."),
                new PowerInfo("Nimble Legs", "Passa por cima de obstáculos baixos sem precisar pular."),
                new PowerInfo("Alert Leap", "Pula mais alto, sempre pronto para fugir do perigo."),
                new PowerInfo("Fragile", "Possui 1 coração a menos."));
        register(new Origin("deer", "Deer", null, Material.OAK_SAPLING, powers, infos));
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
                new PowerInfo("Dark Vision", "Visão noturna permanente."),
                new PowerInfo("Bat Wings", "Cai lentamente (planagem) ao cair no ar."),
                new PowerInfo("Cave Mobility", "Mais rápido e minera velozmente no fundo de cavernas escuras."),
                new PowerInfo("Sun Averse", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Fragile", "Possui 2 corações a menos."),
                new PowerInfo("Tiny Body", "Seu corpo é 30% menor."));
        register(new Origin("bat", "Bat", null, Material.BAT_SPAWN_EGG, powers, infos));
    }

    private void registerRat() {
        List<PowerType> powers = List.of(
                new AttributeModifierPower("rat:small_body", Attribute.SCALE, -0.3),
                new EvasionPower("rat:evasion"),
                new SilentStepsPower("rat:silent_steps"),
                new AttributeModifierPower("rat:swift", Attribute.MOVEMENT_SPEED, 0.03),
                new AttributeModifierPower("rat:fragile", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("rat:short_reach_block", Attribute.BLOCK_INTERACTION_RANGE, -1.0),
                new AttributeModifierPower("rat:short_reach_entity", Attribute.ENTITY_INTERACTION_RANGE, -1.0));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Small Body", "Seu corpo é 30% menor."),
                new PowerInfo("Evasion", "Você tem 30% de chance de desviar completamente de projéteis."),
                new PowerInfo("Silent Steps", "Passos silenciosos (cosmético)."),
                new PowerInfo("Swift", "Mais rápido ao andar e correr."),
                new PowerInfo("Fragile", "Possui 2 corações a menos."),
                new PowerInfo("Short Reach", "Seus bracinhos não alcançam tão longe."));
        register(new Origin("rat", "Rat", null, Material.BEETROOT, powers, infos));
    }

    private void registerDemon() {
        List<PowerType> powers = List.of(
                new FireImmunityPower("demon:hellborn"),
                new AttributeModifierPower("demon:infernal_power", Attribute.ATTACK_DAMAGE, 2.0),
                new NightVisionPower("demon:dark_vision"),
                new HellPactPower("demon:hell_pact"),
                new DayDazedPower("demon:sun_averse"),
                new HolyVulnerabilityPower("demon:holy_vulnerability"),
                new BiomeEffectPower("demon:cold_weakness", COLD_BIOMES, true, 2, effect(PotionEffectType.SLOWNESS, 60, 0)));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Hellborn", "Imune a fogo, lava e chão quente."),
                new PowerInfo("Infernal Power", "Causa +1 coração de dano corpo a corpo."),
                new PowerInfo("Dark Vision", "Visão noturna permanente."),
                new PowerInfo("Hell Pact", "Agachar + F: Ganha Força I por 10s. Depois sofre Fraqueza e Fome por 5s."),
                new PowerInfo("Sun Averse", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Holy Vulnerability", "Sofre o dobro de dano de magia e poções."),
                new PowerInfo("Cold Weakness", "Fica lento em biomas nevados e congelados."));
        register(new Origin("demon", "Demon", null, Material.NETHER_WART, powers, infos));
    }

    private void registerFeline() {
        List<PowerType> powers = List.of(
                new NoFallDamagePower("feline:acrobatics"),
                new AttributeModifierPower("feline:strong_ankles", Attribute.JUMP_STRENGTH, 0.1),
                new SilentStepsPower("feline:velvet_paws"),
                new AttributeModifierPower("feline:nine_lives", Attribute.MAX_HEALTH, -2.0),
                new WeakArmsPower("feline:weak_arms"),
                new dev.originspaper.power.shared.ScareEntityPower("feline:scare_creepers", EntityType.CREEPER),
                new NightVisionPower("feline:cat_vision"),
                new AttributeModifierPower("feline:feline_grace", Attribute.MOVEMENT_SPEED, 0.02),
                new dev.originspaper.power.origins.feline.PredatorInstinctPower("feline:predator_instinct"),
                new AttributeModifierPower("feline:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Acrobatics", "Não sofre dano de queda."),
                new PowerInfo("Strong Ankles", "Pula um pouco mais alto."),
                new PowerInfo("Velvet Paws", "Passos silenciosos (cosmético)."),
                new PowerInfo("Nine Lives", "Possui 1 coração a menos."),
                new PowerInfo("Weak Arms", "Não arranca pedra muito incrustada sem picareta."),
                new PowerInfo("Scare Creepers", "Creepers têm medo e não te atacam."),
                new PowerInfo("Cat Vision", "Visão noturna permanente."),
                new PowerInfo("Feline Grace", "Levemente mais rápido."),
                new PowerInfo("Predator Instinct", "Ganha velocidade extra ao perseguir criaturas hostis."),
                new PowerInfo("Small Body", "Seu corpo é 10% menor."));
        register(new Origin("feline", "Feline", null, Material.OCELOT_SPAWN_EGG, powers, infos));
    }



    private void registerDragon() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("dragon:wings", "dragon_wings", "Asas de Dragão"),
                new DragonBreathPower("dragon:breath"),
                new RebornMagicPower("dragon:reborn_magic"),
                new AttributeModifierPower("dragon:resistant_skin", Attribute.MAX_HEALTH, 4.0),
                new AttributeModifierPower("dragon:sharp_claws", Attribute.ATTACK_DAMAGE, 2.0),
                new CarnivoreDietPower("dragon:apex_predator"),
                new ArmorSlotRestrictPower("dragon:scaled_body", EquipmentSlot.CHEST,
                        item -> item.getType().name().endsWith("CHESTPLATE"),
                        "§cSuas escamas não permitem peitorais."),
                new AttributeModifierPower("dragon:large_body", Attribute.SCALE, 0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Dragão", "Elytra permanente que volta sozinha."),
                new PowerInfo("Dragon's Breath", "Agachar + F: sopra um cone de fogo que causa dano e incendeia os alvos à frente."),
                new PowerInfo("Reborn Magic", "Ganha regeneração no The End."),
                new PowerInfo("Resistant Skin", "Possui 2 corações a mais."),
                new PowerInfo("Sharp Claws", "Causa +1 coração de dano corpo a corpo."),
                new PowerInfo("Apex Predator", "Só consegue comer carne."),
                new PowerInfo("Scaled Body", "Não pode usar peitorais (mas usa as asas)."),
                new PowerInfo("Large Body", "Seu corpo é 10% maior."));
        register(new Origin("dragon", "Dragon", null, Material.DRAGON_HEAD, powers, infos));
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
                new ActiveBuffPower("wolf:alpha_howl", 2400L, org.bukkit.Sound.ENTITY_WOLF_GROWL,
                        effect(PotionEffectType.ABSORPTION, 200, 1)));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Canine Eyes", "Visão noturna permanente."),
                new PowerInfo("Night Runner", "Mais rápido à noite, ainda ágil de dia."),
                new PowerInfo("Wolf Metabolism", "Gasta mais energia à noite."),
                new PowerInfo("Poison Immunity", "Imune a veneno."),
                new PowerInfo("Carnivore's Bite", "Comer carne cura meio coração extra."),
                new PowerInfo("Night Fangs", "Causa +2 de dano à noite."),
                new PowerInfo("Hunter's Sense", "Destaca criaturas próximas."),
                new PowerInfo("Alpha's Howl", "Agachar + F: ganha absorção e uiva."));
        register(new Origin("wolf", "Wolf", null, Material.BONE, powers, infos));
    }



    private void registerOwl() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("owl:wings", "owl_wings", "Asas de Coruja"),
                new SilentFlightPower("owl:silent_flight"),
                new NightHunterPower("owl:night_hunter"),
                new PredatorDivePower("owl:predator_dive", 3.0),
                new EcholocationPower("owl:echolocation"),
                new NoFallDamagePower("owl:soft_landing"),
                new CarnivoreDietPower("owl:carnivore"),
                new DayDazedPower("owl:day_dazed"),
                new LightArmorOnlyPower("owl:light_frame", 5),
                new AttributeModifierPower("owl:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Coruja", "Elytra permanente que volta sozinha."),
                new PowerInfo("Silent Flight", "Reduz o alcance de detecção dos mobs ao planar."),
                new PowerInfo("Night Hunter", "De noite ganha Velocidade I, Visão Noturna e bônus de dano."),
                new PowerInfo("Predator Dive", "Atacar mergulhando causa dano massivo e sangramento."),
                new PowerInfo("Echolocation", "Agachar + F: Revela entidades próximas através das paredes."),
                new PowerInfo("Soft Landing", "Não sofre dano de queda."),
                new PowerInfo("Carnivore", "Apenas carne."),
                new PowerInfo("Day Blindness", "Fica exausto e fraco sob a luz solar direta."),
                new PowerInfo("Lightweight", "Sem armaduras pesadas."),
                new PowerInfo("Small Body", "Seu corpo é 10% menor."));
        register(new Origin("owl", "Owl", null, Material.FEATHER, powers, infos));
    }

    private void registerGryphon() {
        List<PowerType> powers = List.of(
                new ElytraFlightPower("gryphon:wings", "gryphon_wings", "Asas de Grifo"),
                new FlightLaunchPower("gryphon:take_flight", 400L, 2.5, 3L),
                new DivePower("gryphon:dive_strike", 2.5, -0.3),
                new NoFallDamagePower("gryphon:sure_landing"),
                new CarnivoreDietPower("gryphon:carnivore"),
                new FreshAirPower("gryphon:fresh_air"),
                new LightArmorOnlyPower("gryphon:need_mobility", 5),
                new AttributeModifierPower("gryphon:large_body", Attribute.SCALE, 0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Asas de Grifo", "Elytra permanente que volta sozinha."),
                new PowerInfo("Decolar", "Agachar + F: impulso forte para o céu e planagem."),
                new PowerInfo("Mergulho", "Dano extra ao atacar mergulhando."),
                new PowerInfo("Pouso Seguro", "Não sofre dano de queda."),
                new PowerInfo("Carnívoro", "Só consegue comer carne."),
                new PowerInfo("Ar Fresco", "Só dorme a 86 blocos de altura ou mais."),
                new PowerInfo("Mobilidade", "Não pode usar armadura pesada."),
                new PowerInfo("Large Body", "Seu corpo é 10% maior."));
        register(new Origin("gryphon", "Gryphon", null, Material.GOLDEN_HORSE_ARMOR, powers, infos));
    }

    private void registerGoat() {
        List<PowerType> powers = List.of(
                new LeapPower("goat:leap"),
                new RamPower("goat:ram"),
                new BracePower("goat:brace"),
                new DamageImmunityPower("goat:insulated", DamageCause.FREEZE),
                new BiomeEffectPower("goat:fur_coat", HOT_BIOMES, true, 2, effect(PotionEffectType.SLOWNESS, 60, 0)),
                new AttributeModifierPower("goat:small", Attribute.MAX_HEALTH, -4.0),
                new NutritionPower("goat:browser", FoodUtil::isMeat, 0.4));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Leap", "Agachar + F: um salto frontal poderoso."),
                new PowerInfo("Ram", "Seus golpes empurram fortemente o alvo."),
                new PowerInfo("Brace", "Agachar evita o dano de queda."),
                new PowerInfo("Insulated", "Imune ao congelamento da neve em pó."),
                new PowerInfo("Fur Coat", "Fica lento em biomas quentes."),
                new PowerInfo("Small", "Possui 2 corações a menos."),
                new PowerInfo("Browser", "Carne alimenta menos você."));
        register(new Origin("goat", "Goat", null, Material.GOAT_HORN, powers, infos));
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
                new LightArmorOnlyPower("fox:weak_armor", 5),
                new NoShieldPower("fox:weak_shield"),
                new AttributeModifierPower("fox:small_body", Attribute.SCALE, -0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Pounce", "Agachar + F: salto que causa dano ao cair."),
                new PowerInfo("Hunt", "Atacar a mesma presa repetidamente te fortalece."),
                new PowerInfo("Agility", "Mais rápido, pula mais e cai mais leve."),
                new PowerInfo("Foxiality", "Mais sorte nos drops."),
                new PowerInfo("Fluffy", "Imune ao frio, mas frágil ao fogo."),
                new PowerInfo("Smol", "Possui 2 corações a menos."),
                new PowerInfo("Unique Taste", "Come apenas carnes e algumas frutas."),
                new PowerInfo("Timidity", "Fica fraco com pouca vida."),
                new PowerInfo("Weak", "Não usa escudo nem armadura pesada."),
                new PowerInfo("Small Body", "Seu corpo é 10% menor."));
        register(new Origin("fox", "Fox", null, Material.SWEET_BERRIES, powers, infos));
    }

    private void registerBear() {
        List<PowerType> powers = List.of(
                new MightyPawsPower("bear:mighty_paws"),
                new AttributeModifierPower("bear:thick_fur_armor", Attribute.ARMOR, 4.0),
                new DamageImmunityPower("bear:thick_fur_cold", DamageCause.FREEZE),
                new NutritionPower("bear:primal_appetite", FoodUtil::isRawMeat, 2.0),
                new ActiveBuffPower("bear:hibernation", 2400L, null,
                        effect(PotionEffectType.STRENGTH, 200, 1), effect(PotionEffectType.RESISTANCE, 200, 0)),
                new AttributeModifierPower("bear:towering", Attribute.ENTITY_INTERACTION_RANGE, 1.0),
                new CumbersomeClawsPower("bear:cumbersome_claws"),
                new AttributeModifierPower("bear:heavy_bones_speed", Attribute.MOVEMENT_SPEED, -0.03),
                new AttributeModifierPower("bear:heavy_bones_atk", Attribute.ATTACK_SPEED, -0.5),
                new BiomeEffectPower("bear:environmental_waning", FOREST_BIOMES, false, 2, effect(PotionEffectType.WEAKNESS, 60, 0)),
                new ArmorSlotRestrictPower("bear:bulky_body", EquipmentSlot.CHEST,
                        item -> item.getType() == Material.NETHERITE_CHESTPLATE || item.getType() == Material.DIAMOND_CHESTPLATE,
                        "§cSeu corpo volumoso não cabe neste peitoral."),
                new ExhaustionPower("bear:large_appetite", 0.005f, false),
                new AttributeModifierPower("bear:large_body", Attribute.SCALE, 0.1));
        List<PowerInfo> infos = List.of(
                new PowerInfo("Mighty Paws", "Mãos vazias causam +3 de dano e forte empurrão."),
                new PowerInfo("Thick Fur", "Armadura natural e imunidade ao frio."),
                new PowerInfo("Primal Appetite", "Carne crua sacia muito mais."),
                new PowerInfo("Hibernation", "Agachar + F: força e resistência temporárias."),
                new PowerInfo("Towering Stature", "Alcance maior."),
                new PowerInfo("Cumbersome Claws", "Não maneja espadas nem machados."),
                new PowerInfo("Heavy Bones", "Mais lento e ataca devagar."),
                new PowerInfo("Environmental Waning", "Fica fraco longe de florestas."),
                new PowerInfo("Bulky Body", "Não usa peitorais de diamante ou netherite."),
                new PowerInfo("Large Appetite", "Fica com fome mais rápido."),
                new PowerInfo("Large Body", "Seu corpo é 10% maior."));
        register(new Origin("bear", "Bear", null, Material.HONEYCOMB, powers, infos));
    }

    private void registerRabbit() {
        List<PowerType> powers = List.of(
                new NightVisionPower("rabbit:high_carotene"),
                new AttributeModifierPower("rabbit:prey_health", Attribute.MAX_HEALTH, -4.0),
                new AttributeModifierPower("rabbit:prey_speed", Attribute.MOVEMENT_SPEED, 0.04),
                new DietPower("rabbit:special_diet", false, Set.of(Material.CARROT, Material.GOLDEN_CARROT),
                        "§cVocê só consegue comer cenouras."),
                new NutritionPower("rabbit:carrot_boost",
                        m -> m == Material.CARROT || m == Material.GOLDEN_CARROT, 2.0),
                new PermanentEffectPower("rabbit:bouncing", PotionEffectType.JUMP_BOOST, 1),
                new ReplenishPower("rabbit:replenish"),
                new AttributeModifierPower("rabbit:small_body", Attribute.SCALE, -0.2));
        List<PowerInfo> infos = List.of(
                new PowerInfo("High Carotene", "Visão noturna permanente."),
                new PowerInfo("Prey", "Menos vida, porém mais veloz."),
                new PowerInfo("Special Diet", "Só come cenouras, que alimentam o dobro."),
                new PowerInfo("Bouncing Aura", "Impulso de pulo permanente."),
                new PowerInfo("Replenish", "Acelera o crescimento de plantações próximas."),
                new PowerInfo("Small Body", "Seu corpo é 20% menor."));
        register(new Origin("rabbit", "Rabbit", null, Material.RABBIT_FOOT, powers, infos));
    }
}
