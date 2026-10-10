package com.roaroflove.client.ui;

import com.roaroflove.config.RoarOfLoveConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

public final class RoarHoverTooltip {
	private static final List<Item> ITEMS = new ArrayList<>();
	private RoarHoverTooltip() {}
	public static void reset() { ITEMS.clear(); }
	public static void register(int x, int y, int width, int height, String key) { if (key != null && !key.isEmpty()) ITEMS.add(new Item(x, y, width, height, key)); }
	public static void render(class_332 graphics, int mouseX, int mouseY, int screenWidth, int screenHeight) {
		if (!RoarOfLoveConfig.isTooltips() || ITEMS.isEmpty()) return;
		class_310 client = class_310.method_1551();
		if (client == null || client.field_1772 == null) return;
		class_327 font = client.field_1772;
		for (Item item : ITEMS) {
			if (mouseX < item.x || mouseX > item.x + item.width || mouseY < item.y || mouseY > item.y + item.height) continue;
			String text = class_2561.method_43469(item.key, new Object[0]).getString();
			if (text == null || text.isEmpty() || text.equals(item.key)) return;
			List<String> lines = wrap(font, text);
			int width = 0;
			for (String line : lines) width = Math.max(width, font.method_27525(class_2561.method_43470(line)));
			int x = Math.max(4, Math.min(mouseX + 10, screenWidth - width - 12));
			int y = mouseY + 12;
			int height = lines.size() * 11 + 8;
			if (y + height > screenHeight - 4) y = mouseY - height - 6;
			y = Math.max(4, y);
			graphics.method_25294(x - 4, y - 4, x + width + 4, y + height - 2, -267382764);
			int lineY = y - 2;
			for (String line : lines) {
				graphics.method_27535(font, class_2561.method_43470(line), x, lineY, -1513240);
				lineY += 11;
			}
			return;
		}
	}
	private static List<String> wrap(class_327 font, String text) {
		List<String> lines = new ArrayList<>();
		while (!text.isEmpty()) {
			if (font.method_27525(class_2561.method_43470(text)) <= 168) { lines.add(text); break; }
			int length = text.length();
			while (length > 1 && font.method_27525(class_2561.method_43470(text.substring(0, length))) > 168) length--;
			int split = text.lastIndexOf(' ', length);
			if (split <= 0 || split < length - 12) split = length;
			lines.add(text.substring(0, split).trim());
			text = text.substring(split).trim();
		}
		return lines;
	}
	private static final class Item {
		final int x, y, width, height;
		final String key;
		Item(int x, int y, int width, int height, String key) { this.x = x; this.y = y; this.width = width; this.height = height; this.key = key; }
	}
}
