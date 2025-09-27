package archives.tater.blackknife.item;

import archives.tater.blackknife.registry.BlackKnifeAttacks;
import archives.tater.blackknife.registry.BlackKnifeComponents;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BlackKnifeItem extends Item {
    public BlackKnifeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        var attack = user.getStackInHand(hand).get(BlackKnifeComponents.ATTACK);
        return attack == null ? super.use(world, user, hand) : attack.use(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        var attack = stack.get(BlackKnifeComponents.ATTACK);
        return attack == null ? super.getMaxUseTime(stack, user) : attack.getMaxUseTime();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        var attack = stack.get(BlackKnifeComponents.ATTACK);
        return attack == null ? super.getUseAction(stack) : attack.getUseAction();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        var attack = stack.get(BlackKnifeComponents.ATTACK);
        if (attack == null) return super.finishUsing(stack, world, user);
        attack.finishUsing(stack, world, user);
        return stack;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        var attack = stack.get(BlackKnifeComponents.ATTACK);
        if (attack == null)
            super.postHit(stack, target, attacker);
        else
            attack.postHit(stack, target, attacker);
    }

    public interface Attack {

        default ActionResult use(World world, PlayerEntity user, Hand hand) {
            return ActionResult.PASS;
        }

        default int getMaxUseTime() {
            return 0;
        }

        default UseAction getUseAction() {
            return UseAction.BLOCK;
        }

        default void finishUsing(ItemStack stack, World world, LivingEntity user) {

        }

        default void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        }

        Codec<Attack> CODEC = BlackKnifeAttacks.ATTACKS.getCodec();
        PacketCodec<RegistryByteBuf, Attack> PACKET_CODEC = PacketCodecs.registryValue(BlackKnifeAttacks.ATTACKS.getKey());
    }
}
