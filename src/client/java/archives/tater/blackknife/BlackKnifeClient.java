package archives.tater.blackknife;

import archives.tater.blackknife.registry.BlackKnifeItems;
import archives.tater.blackknife.registry.BlackKnifeParticles;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.*;

public class BlackKnifeClient implements ClientModInitializer {
    public static final int AFTERIMAGE_COUNT = 6;
    public static final int AFTERIMAGE_RATE = 2;
    public static final int MAX_AFTERIMAGE_OPACITY = 95;
    public static final int AFTERIMAGE_FADE_LENGTH = 6;
    public static final double AFTERIMAGE_SPEED = 0.125;
    public static final float HOVER_RATE_COEFFICIENT = 0.1875f;
    public static final double HOVER_AMOUNT = 0.125;

    public static final RenderStateDataKey<EntityRenderState[]> AFTERIMAGES = RenderStateDataKey.create(() -> "afterimages");

    @Internal
    public static int alpha = -1;

    public static final Map<Entity, Deque<EntityRenderState>> AFTERIMAGE_CACHE = new WeakHashMap<>();

    public static Deque<EntityRenderState> getAfterimages(Entity entity) {
        return AFTERIMAGE_CACHE.computeIfAbsent(entity, _entity -> new LinkedList<>());
    }

    public static void addAfterimage(Deque<EntityRenderState> afterimages, EntityRenderState state) {
        afterimages.offerFirst(state);
        while (afterimages.size() > AFTERIMAGE_COUNT)
            afterimages.removeLast();
    }

    public static boolean hasAfterimages(Entity entity) {
        return entity instanceof LivingEntity livingEntity && Arrays.stream(Hand.values()).anyMatch(hand -> livingEntity.getStackInHand(hand).isOf(BlackKnifeItems.BLACK_KNIFE));
    }


    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ParticleFactoryRegistry.getInstance().register(BlackKnifeParticles.BLACK_KNIFE_SWEEP, SweepAttackParticle.Factory::new);
	}
}