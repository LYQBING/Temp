package cn.blockforge.generated.semensyringe;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class SyringeDataComponents {
	public static final DataComponentType<SyringeContents> CONTENTS = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(SemenSyringeMod.MOD_ID, "contents"),
			DataComponentType.<SyringeContents>builder()
					.persistent(SyringeContents.CODEC)
					.networkSynchronized(SyringeContents.CODEC)
					.build());

	private SyringeDataComponents() {
	}

	public static void register() {
	}
}
