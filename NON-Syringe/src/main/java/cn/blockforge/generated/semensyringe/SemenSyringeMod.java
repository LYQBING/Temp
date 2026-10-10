package cn.blockforge.generated.semensyringe;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SemenSyringeMod implements ModInitializer {
	public static final String MOD_ID = "semen_syringe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier SYRINGE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "syringe");
	public static final ResourceKey<Item> SYRINGE_KEY = ResourceKey.create(Registries.ITEM, SYRINGE_ID);
	public static Item SYRINGE;

	@Override
	public void onInitialize() {
		SyringeDataComponents.register();
		SYRINGE = Registry.register(BuiltInRegistries.ITEM, SYRINGE_ID,
				new SyringeItem(new Item.Properties().setId(SYRINGE_KEY).stacksTo(1)));
		CreativeModeTab.Builder creativeTab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
				.title(net.minecraft.network.chat.Component.translatable("itemGroup.semen_syringe.items"))
				.icon(() -> new net.minecraft.world.item.ItemStack(SYRINGE))
				.displayItems((parameters, output) -> output.accept(SYRINGE));
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
				Identifier.fromNamespaceAndPath(MOD_ID, "items"),
				creativeTab.build());
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
				Identifier.fromNamespaceAndPath(MOD_ID, "crafting_fill_syringe"), FillSyringeRecipe.SERIALIZER);
	}
}
