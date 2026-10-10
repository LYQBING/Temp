package cn.blockforge.generated.semensyringe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.Identifier;

public record SyringeContents(Optional<Identifier> donor, int milliliters, int tint) {
	public static final Codec<SyringeContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.optionalFieldOf("donor").forGetter(SyringeContents::donor),
			Codec.INT.fieldOf("milliliters").forGetter(SyringeContents::milliliters),
			Codec.INT.fieldOf("tint").forGetter(SyringeContents::tint)
	).apply(instance, SyringeContents::new));

	public Identifier donorId() {
		return this.donor.orElse(null);
	}
}
