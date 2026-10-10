package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.util.AudioPaths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.class_11909;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class RoarFilePickerScreen extends class_437 {
	private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
	private final Consumer<Path> onPick;
	private final class_437 parent;
	private final String slotLabel;
	private Path dir;
	private List<Path> dirs = new ArrayList<>();
	private List<Path> files = new ArrayList<>();
	private int selected;
	private int scroll;
	private String note = "";

	public RoarFilePickerScreen(class_437 parent, Path directory, String slotLabel, Consumer<Path> onPick) {
		super(class_2561.method_43469("roar_of_love.ui.pick_title", new Object[0]));
		this.parent = parent;
		this.dir = directory;
		this.slotLabel = slotLabel == null ? "" : slotLabel;
		this.onPick = onPick;
		reload();
	}

	private void reload() {
		dirs.clear();
		files.clear();
		selected = scroll = 0;
		if (dir == null || !Files.isDirectory(dir)) {
			note = class_2561.method_43469("roar_of_love.ui.pick_bad_dir", new Object[0]).getString();
			return;
		}
		try (var stream = Files.list(dir)) {
			stream.forEach(path -> {
				if (Files.isDirectory(path)) dirs.add(path);
				else if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg")) files.add(path);
			});
			Comparator<Path> byName = Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER);
			dirs.sort(byName);
			files.sort(byName);
			note = "";
		} catch (Exception exception) {
			note = class_2561.method_43469("roar_of_love.ui.pick_read_fail", new Object[]{String.valueOf(exception.getMessage())}).getString();
		}
	}

	private int count() { return dirs.size() + files.size(); }
	private int visibleRows() { return Math.max(0, (field_22790 - 92) / 16); }
	private Path item(int index) { return index < dirs.size() ? dirs.get(index) : index - dirs.size() < files.size() ? files.get(index - dirs.size()) : null; }

	protected void method_25426() {
		method_37067();
		RoarButton.resetVariants();
		int y = field_22790 - 26;
		int margin = 8;
		int gap = 4;
		int available = Math.max(4, field_22789 - (margin * 2) - (gap * 3));
		int upWidth = Math.max(1, Math.min(70, available / 4));
		int audioWidth = Math.max(1, Math.min(110, available / 3));
		int backWidth = Math.max(1, Math.min(70, available / 4));
		int confirmWidth = Math.max(1, available - upWidth - audioWidth - backWidth);
		int x = margin;
		method_37063(new RoarButton(x, y, upWidth, 20, class_2561.method_43469("roar_of_love.ui.pick_up", new Object[0]), button -> { if (dir != null && dir.getParent() != null) { dir = dir.getParent(); reload(); method_25426(); } }));
		x += upWidth + gap;
		method_37063(new RoarButton(x, y, audioWidth, 20, class_2561.method_43469("roar_of_love.ui.pick_goto_audio", new Object[0]), button -> { dir = AudioPaths.audioFolder(); reload(); method_25426(); }));
		x += audioWidth + gap;
		method_37063(new RoarButton(x, y, confirmWidth, 20, class_2561.method_43469("roar_of_love.ui.pick_confirm", new Object[0]), button -> confirm()));
		x += confirmWidth + gap;
		method_37063(new RoarButton(x, y, backWidth, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), button -> method_25419()));
	}

	private void confirm() {
		Path path = item(selected);
		if (path == null) return;
		if (Files.isDirectory(path)) { dir = path; reload(); method_25426(); return; }
		onPick.accept(path);
		method_25419();
	}

	public boolean method_25401(double x, double y, double horizontal, double vertical) {
		if (vertical == 0 || count() == 0 || visibleRows() == 0) return false;
		selected = Math.max(0, Math.min(count() - 1, selected + (vertical > 0 ? -1 : 1)));
		int visible = visibleRows();
		if (selected < scroll) scroll = selected;
		if (selected >= scroll + visible) scroll = selected - visible + 1;
		scroll = Math.max(0, Math.min(Math.max(0, count() - visible), scroll));
		return true;
	}

	public boolean method_25402(class_11909 event, boolean doubleClick) {
		int mouseY = (int) event.comp_4799();
		int listBottom = 58 + (visibleRows() * 16);
		if (visibleRows() > 0 && event.method_74245() == 0 && mouseY >= 58 && mouseY < listBottom) {
			int index = scroll + ((mouseY - 58) / 16);
			if (index >= 0 && index < count()) {
				selected = index;
				if (doubleClick) confirm();
				return true;
			}
		}
		return super.method_25402(event, doubleClick);
	}

	public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
		context.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, field_22789, field_22790, field_22789, field_22790);
		super.method_25394(context, mouseX, mouseY, delta);
		if (field_22793 != null) {
			context.method_27535(field_22793, class_2561.method_43470(dir == null ? "" : dir.toAbsolutePath().toString()), 10, 10, -1);
			context.method_27535(field_22793, class_2561.method_43470(slotLabel), 10, 24, -5185281);
			context.method_27535(field_22793, class_2561.method_43470(note), 10, 36, -256);
			for (int row = 0; row < visibleRows() && scroll + row < count(); row++) {
				int index = scroll + row;
				Path entry = item(index);
				String label = (index == selected ? "> " : "  ") + entry.getFileName() + (Files.isDirectory(entry) ? "/" : "");
				context.method_27535(field_22793, class_2561.method_43470(label), 16, 58 + row * 16, -1);
			}
		}
	}

	public void method_25419() { if (field_22787 != null) field_22787.method_1507(parent); }
}
