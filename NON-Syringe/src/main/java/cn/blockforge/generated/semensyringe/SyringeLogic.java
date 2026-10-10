package cn.blockforge.generated.semensyringe;

import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class SyringeLogic {
	public static final int SYRINGE_CAPACITY = 1000;
	public static final int INJECTION_DOSE = 100;

	private SyringeLogic() {
	}

	public static ItemStack makeFilled(ItemStack syringe, ItemStack bottle) {
		ItemStack result = syringe.copyWithCount(1);
		SyringeContents current = result.get(SyringeDataComponents.CONTENTS);
		Identifier bottleDonor = NonNatureBridge.bottleDonor(bottle);
		int currentMl = current == null ? 0 : current.milliliters();
		int gain = NonNatureBridge.bottleMilliliters(bottleDonor);
		Identifier mergedDonor = bottleDonor;
		if (current != null && current.milliliters() > 0 && !current.donor().equals(Optional.ofNullable(bottleDonor))) {
			mergedDonor = null;
		}
		int total = Math.min(SYRINGE_CAPACITY, currentMl + gain);
		result.set(SyringeDataComponents.CONTENTS,
				new SyringeContents(Optional.ofNullable(mergedDonor), total, NonNatureBridge.liquidColor(mergedDonor)));
		return result;
	}

	public static boolean inject(Level level, Player injector, Player target, InteractionHand hand) {
		if (level.isClientSide() || !(injector instanceof ServerPlayer serverInjector)
				|| !(target instanceof ServerPlayer serverTarget)) {
			return false;
		}
		ItemStack held = injector.getItemInHand(hand);
		if (!(held.getItem() instanceof SyringeItem)) {
			return false;
		}
		SyringeContents contents = held.get(SyringeDataComponents.CONTENTS);
		if (contents == null || contents.milliliters() <= 0) {
			feedback(injector, "semen_syringe.msg.empty");
			return false;
		}
		if (!NonNatureBridge.tankEnabled()) {
			feedback(injector, "semen_syringe.msg.tank_disabled");
			return false;
		}
		int dose = Math.min(INJECTION_DOSE, contents.milliliters());
		int accepted = NonNatureBridge.inject(serverTarget, contents.donorId(), dose);
		if (accepted <= 0) {
			feedback(injector, "semen_syringe.msg.rejected");
			return false;
		}
		consume(serverInjector, hand, contents, Math.min(accepted, contents.milliliters()));
		injector.swing(hand, true);
		target.playSound(SoundEvents.GENERIC_DRINK, 0.5F, 1.7F);
		injector.sendSystemMessage(Component.translatable("semen_syringe.msg.injected", donorText(contents.donorId()), accepted));
		if (target != injector) {
			target.sendSystemMessage(Component.translatable("semen_syringe.msg.injected_by", injector.getDisplayName(), accepted));
		}
		return true;
	}

	private static void consume(ServerPlayer injector, InteractionHand hand, SyringeContents contents, int usedMl) {
		ItemStack held = injector.getItemInHand(hand);
		if (!(held.getItem() instanceof SyringeItem)) {
			return;
		}
		int remaining = contents.milliliters() - usedMl;
		if (remaining > 0) {
			held.set(SyringeDataComponents.CONTENTS, new SyringeContents(contents.donor(), remaining, contents.tint()));
		} else {
			held.remove(SyringeDataComponents.CONTENTS);
		}
		injector.getInventory().setChanged();
	}

	private static Component donorText(@Nullable Identifier donor) {
		if (donor == null) {
			return Component.translatable("semen_syringe.tooltip.mixed");
		}
		return Component.translatable("entity." + donor.getNamespace() + "." + donor.getPath());
	}

	private static void feedback(Player player, String translationKey) {
		player.sendSystemMessage(Component.translatable(translationKey));
	}
}
