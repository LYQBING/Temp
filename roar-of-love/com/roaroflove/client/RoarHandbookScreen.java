package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarHandbookScreen extends class_437 {
	private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
	private int page;
	private final class_437 parent;
	private List<String> rows;

	public RoarHandbookScreen(class_437 parent) {
		super(class_2561.method_43469("roar_of_love.ui.handbook_title", new Object[0]));
		this.page = 0;
		this.parent = parent;
		this.rows = RoarHandbook.summary();
	}

	private int pages() {
		return Math.max(1, (rows.size() + 9) / 10);
	}

	protected void method_25426() {
		method_37067();
		RoarButton.resetVariants();
		int center = this.field_22789 / 2;
		int y = this.field_22790 - 26;
		method_37063(new RoarButton(center - 130, y, 80, 20, class_2561.method_43469("roar_of_love.ui.page_prev", new Object[0]), button -> turn(-1)));
		method_37063(new RoarButton(center + 50, y, 80, 20, class_2561.method_43469("roar_of_love.ui.page_next", new Object[0]), button -> turn(1)));
		method_37063(new RoarButton(center - 40, y, 80, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), button -> method_25419()));
	}

	public boolean method_25401(double x, double y, double horizontal, double vertical) {
		if (vertical == 0.0d) {
			return false;
		}
		turn(vertical > 0.0d ? -1 : 1);
		return true;
	}

	private void turn(int delta) {
		int next = Math.max(0, Math.min(pages() - 1, page + delta));
		if (next != page) {
			page = next;
			method_25426();
		}
	}

	public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
		rows = RoarHandbook.summary();
		int maxPage = Math.max(0, pages() - 1);
		if (page > maxPage) page = maxPage;
		context.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
		context.method_25294(20, 20, this.field_22789 - 20, this.field_22790 - 34, -1879048192);
		context.method_25294(20, 20, this.field_22789 - 20, 21, -1862270977);
		super.method_25394(context, mouseX, mouseY, delta);
		if (this.field_22793 == null) {
			return;
		}
		class_5250 title = class_2561.method_43469("roar_of_love.ui.handbook_title", new Object[0]);
		context.method_27535(this.field_22793, title, (this.field_22789 / 2) - (this.field_22793.method_27525(title) / 2), 6, -1);
		if (rows.isEmpty()) {
			class_5250 empty = class_2561.method_43469("roar_of_love.ui.handbook_empty", new Object[0]);
			context.method_27535(this.field_22793, empty, (this.field_22789 / 2) - (this.field_22793.method_27525(empty) / 2), 60, -5185281);
		}
		for (int row = 0; row < 10; row++) {
			int index = (page * 10) + row;
			if (index >= rows.size()) {
				break;
			}
			context.method_27535(this.field_22793, class_2561.method_43470(rows.get(index)), 32, (row * 16) + 30, -9754);
		}
		String pageStr = (page + 1) + " / " + pages();
		class_5250 pageText = class_2561.method_43470(pageStr);
		context.method_27535(this.field_22793, pageText, (this.field_22789 / 2) - (this.field_22793.method_27525(pageText) / 2), this.field_22790 - 40, -5185281);
	}

	public void method_25419() {
		if (this.field_22787 != null) {
			this.field_22787.method_1507(parent);
		}
	}
}
