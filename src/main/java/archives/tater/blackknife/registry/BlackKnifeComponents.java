package archives.tater.blackknife.registry;

import archives.tater.blackknife.BlackKnife;
import archives.tater.blackknife.item.BlackKnifeItem.Attack;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlackKnifeComponents {
    private static <T> ComponentType<T> register(Identifier id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                id,
                ComponentType.<T>builder()
                        .codec(codec)
                        .packetCodec(packetCodec)
                        .build()
        );
    }

    private static <T> ComponentType<T> register(String path, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return register(BlackKnife.id(path), codec, packetCodec);
    }

    public static final ComponentType<Attack> ATTACK = register("attack", Attack.CODEC, Attack.PACKET_CODEC);

    public static void init() {

    }
}
