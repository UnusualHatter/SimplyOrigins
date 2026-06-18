package dev.originspaper.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Vanilla-particle helpers shared by every power.
 *
 * <p><b>Why this exists / why it is defensive:</b> on this Paper build several particles require an
 * extra <i>data</i> argument and throw {@code IllegalArgumentException: missing required data ...}
 * when spawned without it (e.g. {@code DRAGON_BREATH} needs a {@link Float}, {@code DUST} needs
 * {@link Particle.DustOptions}, {@code BLOCK}/{@code FALLING_DUST} need {@link BlockData},
 * {@code ENTITY_EFFECT}/{@code AMBIENT_ENTITY_EFFECT} need a {@link Color}). Because effects can't
 * be unit-tested on a live server cheaply, the core {@link #spawn} inspects
 * {@link Particle#getDataType()} and supplies a sensible default when the caller didn't pass one,
 * so a power can never crash the tick/skill it runs in. If a particle needs data we don't know how
 * to build, we skip it rather than throw.
 */
public final class ParticleUtil {

    private ParticleUtil() {}

    /** Builds a {@link Particle.DustOptions} for coloured {@code DUST} particles. */
    public static Particle.DustOptions dust(Color color, float size) {
        return new Particle.DustOptions(color, size);
    }

    // ---------------------------------------------------------------------
    // Core
    // ---------------------------------------------------------------------

    public static void spawn(Particle particle, Location loc, int count,
                             double ox, double oy, double oz, double extra) {
        spawn(particle, loc, count, ox, oy, oz, extra, null);
    }

    /**
     * Spawns a particle, auto-supplying required data when {@code data} is null or the wrong type.
     * Never throws for a missing/incorrect data argument — it substitutes a default or skips.
     */
    public static void spawn(Particle particle, Location loc, int count,
                             double ox, double oy, double oz, double extra, Object data) {
        if (particle == null || loc == null) {
            return;
        }
        World world = loc.getWorld();
        if (world == null) {
            return;
        }
        Class<?> required = particle.getDataType();
        if (required == Void.class) {
            // No data accepted — calling the data overload with null would be fine, but the
            // no-data overload is unambiguous and avoids any null-typing surprises.
            world.spawnParticle(particle, loc, count, ox, oy, oz, extra);
            return;
        }
        Object effective = (data != null && required.isInstance(data)) ? data : defaultData(required);
        if (effective == null) {
            return; // required data we can't synthesise — skip instead of crashing
        }
        world.spawnParticle(particle, loc, count, ox, oy, oz, extra, effective);
    }

    /** Convenience: a localized puff with no directional bias. */
    public static void spawn(Particle particle, Location loc, int count) {
        spawn(particle, loc, count, 0, 0, 0, 0, null);
    }

    private static Object defaultData(Class<?> required) {
        if (required == Float.class) {
            return 1.0f;
        }
        if (required == Integer.class) {
            return 0;
        }
        if (required == Color.class) {
            return Color.WHITE;
        }
        if (Particle.DustOptions.class.isAssignableFrom(required)) {
            return new Particle.DustOptions(Color.WHITE, 1.0f);
        }
        if (BlockData.class.isAssignableFrom(required)) {
            return Material.STONE.createBlockData();
        }
        if (ItemStack.class.isAssignableFrom(required)) {
            return new ItemStack(Material.STONE);
        }
        return null;
    }

    // ---------------------------------------------------------------------
    // Shapes (each derives its World from the supplied Location)
    // ---------------------------------------------------------------------

    /** A horizontal ring of {@code points} particles at radius around {@code center}. */
    public static void spawnRing(Particle particle, Location center, double radius,
                                 int points, double extra) {
        spawnRing(particle, center, radius, points, extra, null);
    }

    public static void spawnRing(Particle particle, Location center, double radius,
                                 int points, double extra, Object data) {
        if (center == null || points <= 0) {
            return;
        }
        for (int i = 0; i < points; i++) {
            double a = 2 * Math.PI * i / points;
            Location p = center.clone().add(Math.cos(a) * radius, 0, Math.sin(a) * radius);
            spawn(particle, p, 1, 0, 0, 0, extra, data);
        }
    }

    /** An evenly distributed hollow sphere (golden-angle spiral) around {@code center}. */
    public static void spawnSphere(Particle particle, Location center, double radius,
                                   int points, double extra) {
        spawnSphere(particle, center, radius, points, extra, null);
    }

    public static void spawnSphere(Particle particle, Location center, double radius,
                                   int points, double extra, Object data) {
        if (center == null || points <= 0) {
            return;
        }
        for (int i = 0; i < points; i++) {
            double y = 1 - (i / (double) Math.max(1, points - 1)) * 2; // 1 .. -1
            double r = Math.sqrt(Math.max(0, 1 - y * y));
            double theta = i * 2.39996323; // golden angle
            double x = Math.cos(theta) * r;
            double z = Math.sin(theta) * r;
            Location p = center.clone().add(x * radius, y * radius, z * radius);
            spawn(particle, p, 1, 0, 0, 0, extra, data);
        }
    }

    /** A widening cone from {@code origin} along {@code direction}. */
    public static void spawnCone(Particle particle, Location origin, Vector direction,
                                 double length, double maxRadius, double step,
                                 int perStep, double extra) {
        spawnCone(particle, origin, direction, length, maxRadius, step, perStep, extra, null);
    }

    public static void spawnCone(Particle particle, Location origin, Vector direction,
                                 double length, double maxRadius, double step,
                                 int perStep, double extra, Object data) {
        if (origin == null || direction == null || length <= 0) {
            return;
        }
        Vector dir = direction.clone().normalize();
        double stepDist = Math.max(0.1, step);
        for (double d = stepDist; d <= length; d += stepDist) {
            Location point = origin.clone().add(dir.clone().multiply(d));
            double spread = (maxRadius / length) * d;
            spawn(particle, point, perStep, spread, spread, spread, extra, data);
        }
    }

    /** A flat-ish outward burst at ground level around {@code center}. */
    public static void spawnGroundBurst(Particle particle, Location center, double radius,
                                        int count, double extra) {
        spawnGroundBurst(particle, center, radius, count, extra, null);
    }

    public static void spawnGroundBurst(Particle particle, Location center, double radius,
                                        int count, double extra, Object data) {
        if (center == null) {
            return;
        }
        spawn(particle, center.clone().add(0, 0.1, 0), count, radius * 0.6, 0.1, radius * 0.6, extra, data);
    }

    /** A small localized cluster — typically dropped behind a moving entity each tick. */
    public static void spawnTrail(Particle particle, Location loc, int count, double spread) {
        spawn(particle, loc, count, spread, spread, spread, 0.0, null);
    }

    public static void spawnTrail(Particle particle, Location loc, int count, double spread, Object data) {
        spawn(particle, loc, count, spread, spread, spread, 0.0, data);
    }
}
