package cn.blockforge.generated.semensyringe;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class SyringeDataComponents {
	private static final StreamCodec<RegistryFriendlyByteBuf, SyringeContents> CONTENTS_STREAM_CODEC =
			ByteBufCodecs.fromCodecWithRegistries(SyringeContents.CODEC);
	public static final DataComponentType<SyringeContents> CONTENTS = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(SemenSyringeMod.MOD_ID, "contents"),
			DataComponentType.<SyringeContents>builder()
					.persistent(SyringeContents.CODEC)
					.networkSynchronized(CONTENTS_STREAM_CODEC)
					.build());

	private SyringeDataComponents() {
	}

	public static void register() {
	}
}
