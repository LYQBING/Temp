package cn.blockforge.generated.semensyringe;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.Level;

public final class SyringeItem extends Item {
	public SyringeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player user, InteractionHand hand) {
		SyringeLogic.inject(level, user, user, hand);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Player target && user.isShiftKeyDown()) {
			SyringeLogic.inject(user.level(), user, target, hand);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, textConsumer, flag);
		SyringeContents contents = stack.get(SyringeDataComponents.CONTENTS);
		if (contents == null) {
			textConsumer.accept(Component.translatable("semen_syringe.tooltip.empty"));
			textConsumer.accept(Component.translatable("semen_syringe.tooltip.usage"));
			return;
		}
		Component donor = contents.donorId() == null
				? Component.translatable("semen_syringe.tooltip.mixed")
				: Component.translatable("entity." + contents.donorId().getNamespace() + "." + contents.donorId().getPath());
		textConsumer.accept(Component.translatable("semen_syringe.tooltip.donor", donor));
		textConsumer.accept(Component.translatable("semen_syringe.tooltip.amount", contents.milliliters()));
		textConsumer.accept(Component.translatable("semen_syringe.tooltip.dose", Math.min(100, contents.milliliters())));
		textConsumer.accept(Component.translatable("semen_syringe.tooltip.usage"));
	}
}
