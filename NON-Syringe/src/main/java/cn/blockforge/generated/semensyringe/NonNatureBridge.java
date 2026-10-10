package cn.blockforge.generated.semensyringe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class NonNatureBridge {
	private static final String NO_N_POTIONS = "com.nonid.potion.NonPotions";
	private static final String NO_N_PLAYER_API = "com.nonid.api.NonPlayerApi";
	private static final String NO_N_PROFILES = "com.nonid.data.NonEntityProfiles";
	private static final String NO_N_MAIN = "com.nonid.NeedsOfNature";
	private static final int DEFAULT_TINT = 15721430;
	private static final int MIXED_DEFAULT_ML = 100;

	private NonNatureBridge() {
	}

	public static boolean isLiquidBottle(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		try {
			return (boolean) invokeStatic(NO_N_POTIONS, "isLiquidBottle", new Class<?>[]{ItemStack.class}, stack);
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.debug("NoN 液体瓶识别失败", exception);
			return false;
		}
	}

	@Nullable
	public static Identifier bottleDonor(ItemStack stack) {
		try {
			return (Identifier) invokeStatic(NO_N_POTIONS, "getLiquidBottleEntityTypeId", new Class<?>[]{ItemStack.class}, stack);
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.debug("读取 NoN 液体瓶供体失败", exception);
			return null;
		}
	}

	public static int bottleMilliliters(@Nullable Identifier donor) {
		if (donor == null) {
			return MIXED_DEFAULT_ML;
		}
		try {
			Object config = invokeStatic(NO_N_MAIN, "getConfig", new Class<?>[0]);
			Object configuredGains = invokeInstance(config, "getLiquidGainByEntity");
			if (configuredGains instanceof Map<?, ?> gains) {
				Object configured = gains.get(donor.toString());
				if (configured instanceof Number amount && amount.intValue() > 0) {
					return clampMilliliters(amount.intValue());
				}
			}
			Object packGain = invokeStatic(NO_N_PROFILES, "getPackLiquidGain", new Class<?>[]{Identifier.class}, donor);
			if (packGain instanceof Number amount && amount.intValue() > 0) {
				return clampMilliliters(amount.intValue());
			}
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.debug("查询 NoN 供体液体量失败，使用兜底值", exception);
		}
		return MIXED_DEFAULT_ML;
	}

	public static boolean tankEnabled() {
		try {
			return (boolean) invokeStatic(NO_N_PLAYER_API, "isLiquidTankEnabled", new Class<?>[0]);
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.debug("查询 NoN 液体罐开关失败", exception);
			return false;
		}
	}

	public static int liquidColor(@Nullable Identifier donor) {
		try {
			String method = donor == null ? "getPackMixedLiquidColorRgb" : "getPackLiquidColorRgb";
			Class<?>[] parameters = donor == null ? new Class<?>[0] : new Class<?>[]{Identifier.class};
			Object value = donor == null
					? invokeStatic(NO_N_PROFILES, method, parameters)
					: invokeStatic(NO_N_PROFILES, method, parameters, donor);
			return value instanceof Number color ? color.intValue() : DEFAULT_TINT;
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.debug("查询 NoN 液体颜色失败，使用默认色", exception);
			return DEFAULT_TINT;
		}
	}

	public static int inject(ServerPlayer target, @Nullable Identifier donor, int milliliters) {
		try {
			Object result = invokeStatic(NO_N_PLAYER_API, "addLiquid",
					new Class<?>[]{ServerPlayer.class, Identifier.class, int.class}, target, donor, milliliters);
			return result instanceof Number amount ? amount.intValue() : 0;
		} catch (RuntimeException exception) {
			SemenSyringeMod.LOGGER.warn("NoN 注液失败", exception);
			return 0;
		}
	}

	private static int clampMilliliters(int amount) {
		return Math.max(1, Math.min(SyringeLogic.SYRINGE_CAPACITY, amount));
	}

	private static Object invokeStatic(String className, String methodName, Class<?>[] parameterTypes, Object... arguments) {
		try {
			Class<?> type = Class.forName(className);
			Method method = type.getMethod(methodName, parameterTypes);
			return method.invoke(null, arguments);
		} catch (InvocationTargetException exception) {
			Throwable cause = exception.getCause();
			if (cause instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			throw new IllegalStateException(cause);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(exception);
		}
	}

	private static Object invokeInstance(Object instance, String methodName) {
		try {
			return instance.getClass().getMethod(methodName).invoke(instance);
		} catch (InvocationTargetException exception) {
			Throwable cause = exception.getCause();
			if (cause instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			throw new IllegalStateException(cause);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(exception);
		}
	}
}
