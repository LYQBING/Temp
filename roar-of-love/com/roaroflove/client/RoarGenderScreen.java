package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarGenderScreen extends class_437 {
	private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
	private final class_437 parent;

	public RoarGenderScreen(class_437 parent) {
		super(class_2561.method_43469("roar_of_love.ui.gender_pick_title"));
		this.parent = parent;
	}

	@Override
	protected void method_25426() {
		method_37067();
		RoarButton.resetVariants();
		int centerX = this.field_22789 / 2;
		int centerY = (this.field_22790 / 2) - 10;
		method_37063(new RoarButton(centerX - 120, centerY, 240, 20,
				class_2561.method_43469("roar_of_love.ui.gender_pick_male"), button -> pick(1)));
		method_37063(new RoarButton(centerX - 120, centerY + 24, 240, 20,
				class_2561.method_43469("roar_of_love.ui.gender_pick_female"), button -> pick(2)));
	}

	private void pick(int gender) {
		RoarOfLoveConfig.setSubtitleGender(gender);
		class_310 client = class_310.method_1551();
		if (client != null) {
			client.method_1507(parent);
		}
	}

	@Override
	public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
		graphics.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f,
				this.field_22789, this.field_22790, this.field_22789, this.field_22790);
		graphics.method_25294(0, 0, this.field_22789, this.field_22790, -1342177280);
		super.method_25394(graphics, mouseX, mouseY, delta);
		if (this.field_22793 != null) {
			class_5250 title = class_2561.method_43469("roar_of_love.ui.gender_pick_title");
			graphics.method_27535(this.field_22793, title,
					(this.field_22789 / 2) - (this.field_22793.method_27525(title) / 2),
					(this.field_22790 / 2) - 40, -1);
			class_5250 hint = class_2561.method_43469("roar_of_love.ui.gender_pick_hint");
			graphics.method_27535(this.field_22793, hint,
					(this.field_22789 / 2) - (this.field_22793.method_27525(hint) / 2),
					(this.field_22790 / 2) - 28, -5185281);
		}
	}
}
