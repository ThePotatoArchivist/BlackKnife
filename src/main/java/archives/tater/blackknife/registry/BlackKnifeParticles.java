package archives.tater.blackknife.registry;

import archives.tater.blackknife.BlackKnife;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlackKnifeParticles {
    public static final SimpleParticleType BLACK_KNIFE_SWEEP = Registry.register(
            Registries.PARTICLE_TYPE,
            BlackKnife.id("sweep"),
            FabricParticleTypes.simple(true)
    );

    public static void init() {

    }
}
