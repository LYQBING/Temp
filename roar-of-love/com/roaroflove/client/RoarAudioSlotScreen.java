package com.roaroflove.client;

import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.client.ui.RoarHoverTooltip;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import com.roaroflove.util.AudioPaths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_437;

public class RoarAudioSlotScreen extends class_437 {
	private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
	private final class_437 parent;
	private final List<String> files = new ArrayList<>();
	private String slotId;
	private int selected;
	private int scroll;
	private String notice = "";
	private long noticeUntil;

	public RoarAudioSlotScreen(class_437 parent, String slotId) {
		super(class_2561.method_43469("roar_of_love.ui.audio_title", new Object[]{class_2561.method_43469(slotId == null || slotId.isEmpty() ? "roar_of_love.ui.audio_editor" : RoLSounds.labelKeyFor(slotId), new Object[0])}));
		this.parent = parent;
		this.slotId = slotId == null ? "" : slotId;
		loadFiles();
	}

	private void loadFiles() {
		files.clear();
		String kind = AudioPackLoader.slotKind(slotId);
		String name = AudioPackLoader.slotName(slotId);
		if ("cat".equals(kind)) files.addAll(RoLSounds.CATEGORY_FILES.getOrDefault(name, List.of()));
		else if ("call".equals(kind)) files.addAll(RoLSounds.callSlotsFor(name));
		else if ("hunt".equals(kind)) files.addAll(RoLSounds.hurtSlots());
		selected = files.isEmpty() ? 0 : Math.max(0, Math.min(selected, files.size() - 1));
	}

	private int rowsPerColumn() {
		return "cat:shots".equals(slotId) ? Math.max(1, (files.size() + 1) / 2) : files.size();
	}

	private int visibleRows() {
		return Math.max(0, (field_22790 - 154) / 17);
	}

	private int selectedRow() {
		return "cat:shots".equals(slotId) ? selected % rowsPerColumn() : selected;
	}

	private void clampScroll() {
		if (visibleRows() == 0) {
			scroll = selectedRow();
			return;
		}
		int maxScroll = Math.max(0, rowsPerColumn() - visibleRows());
		if (selectedRow() < scroll) scroll = selectedRow();
		if (selectedRow() >= scroll + visibleRows()) scroll = selectedRow() - visibleRows() + 1;
		scroll = Math.max(0, Math.min(maxScroll, scroll));
	}

	protected void method_25426() {
		method_37067();
		RoarButton.resetVariants();
		RoarHoverTooltip.reset();
		if (slotId.isEmpty()) {
			int y = 34;
			int columnHeight = Math.max(23, field_22790 - 102);
			int rowsPerColumn = Math.max(1, columnHeight / 23);
			int buttonWidth = Math.max(1, (field_22789 - 36) / 2);
			int secondColumnX = 12 + buttonWidth + 12;
			int index = 0;
			for (String category : RoLSounds.CATEGORIES) {
				String id = "cat:" + category;
				int x = 12 + (index / rowsPerColumn) * (buttonWidth + 12);
				int rowY = y + (index % rowsPerColumn) * 23;
				method_37063(new RoarButton(x, rowY, buttonWidth, 20, class_2561.method_43469("roar_of_love.cat." + category, new Object[0]), button -> openSlot(id)));
				index++;
			}
			index = 0;
			for (String group : RoLSounds.CALL_GROUPS) {
				String id = "call:" + group;
				int x = secondColumnX + ((index / rowsPerColumn) * (buttonWidth + 12));
				int rowY = y + (index % rowsPerColumn) * 23;
				method_37063(new RoarButton(x, rowY, buttonWidth, 20, class_2561.method_43469("roar_of_love.call.group." + group, new Object[0]), button -> openSlot(id)));
				index++;
			}
			method_37063(new RoarButton(12, field_22790 - 52, Math.max(1, field_22789 - 24), 20, class_2561.method_43469("roar_of_love.ui.hurt_preview", new Object[0]), button -> openSlot("hunt:hurt")));
		} else {
			int visible = visibleRows();
			boolean splitShots = "cat:shots".equals(slotId);
			int rowsPerColumn = rowsPerColumn();
			clampScroll();
			for (int row = 0; row < visible; row++) {
				int index = scroll + row;
				int leftIndex = splitShots ? index : index;
				int rightIndex = splitShots ? rowsPerColumn + index : -1;
				if (leftIndex < files.size()) addFileRow(leftIndex, row, splitShots ? Math.max(1, (field_22789 / 2) - 12) : Math.max(1, field_22789 - 16));
				if (rightIndex >= 0 && rightIndex < files.size()) addFileRow(rightIndex, row, Math.max(1, field_22789 - (field_22789 / 2) - 8));
			}
			if (splitShots && field_22793 != null) {
				int leftWidth = Math.max(1, (field_22789 / 2) - 12);
				int rightX = field_22789 / 2;
				method_37063(new RoarButton(8, 28, leftWidth, 16, class_2561.method_43469("roar_of_love.ui.col_shot_in", new Object[0]), button -> {}));
				method_37063(new RoarButton(rightX, 28, Math.max(1, field_22789 - rightX - 8), 16, class_2561.method_43469("roar_of_love.ui.col_shot_out", new Object[0]), button -> {}));
			}
			int firstActionY = field_22790 - 76;
			int secondActionY = field_22790 - 52;
			int firstGap = 4;
			int firstWidth = Math.max(1, (field_22789 - 24 - (firstGap * 3)) / 4);
			int previewX = 8;
			int toggleX = previewX + firstWidth + firstGap;
			int downX = toggleX + firstWidth + firstGap;
			int upX = downX + firstWidth + firstGap;
			int secondGap = 4;
			int secondWidth = Math.max(1, (field_22789 - 24 - (secondGap * 2)) / 3);
			int replaceX = 8;
			int resetX = replaceX + secondWidth + secondGap;
			int folderX = resetX + secondWidth + secondGap;
			int folderWidth = secondWidth;
			method_37063(new RoarButton(previewX, firstActionY, firstWidth, 20, class_2561.method_43469("roar_of_love.ui.preview_one", new Object[0]), button -> preview()));
			String toggleKey = RoarOfLoveConfig.isSoundEnabled(eventKey()) ? "roar_of_love.ui.snd_off" : "roar_of_love.ui.snd_on";
			method_37063(new RoarButton(toggleX, firstActionY, firstWidth, 20, class_2561.method_43469(toggleKey, new Object[0]), button -> toggle()));
			method_37063(new RoarButton(downX, firstActionY, firstWidth, 20, class_2561.method_43470("-5%"), button -> volume(-1)));
			method_37063(new RoarButton(upX, firstActionY, firstWidth, 20, class_2561.method_43470("+5%"), button -> volume(1)));
			method_37063(new RoarButton(replaceX, secondActionY, secondWidth, 20, class_2561.method_43469("roar_of_love.ui.replace_one", new Object[0]), button -> pickFile()));
			method_37063(new RoarButton(resetX, secondActionY, secondWidth, 20, class_2561.method_43469("roar_of_love.ui.reset_one", new Object[0]), button -> reset()));
			method_37063(new RoarButton(folderX, secondActionY, folderWidth, 20, class_2561.method_43469("roar_of_love.ui.open_slot_folder", new Object[0]), button -> openSlotFolder()));
			int footerWidth = Math.max(1, Math.min(120, field_22789 - 16));
			method_37063(new RoarButton(8, field_22790 - 26, footerWidth, 20, class_2561.method_43469(AudioPackLoader.isPackDirty() ? "roar_of_love.ui.apply_now_dirty" : "roar_of_love.ui.apply_now", new Object[0]), button -> setNotice(AudioPackLoader.applyNow())));
			RoarHoverTooltip.register(downX, firstActionY, firstWidth, 20, "roar_of_love.tip.volume");
			RoarHoverTooltip.register(upX, firstActionY, firstWidth, 20, "roar_of_love.tip.volume");
			RoarHoverTooltip.register(replaceX, secondActionY, secondWidth, 20, "roar_of_love.tip.replace");
			RoarHoverTooltip.register(resetX, secondActionY, secondWidth, 20, "roar_of_love.tip.reset");
			RoarHoverTooltip.register(folderX, secondActionY, folderWidth, 20, "roar_of_love.tip.slot_folder");
			RoarHoverTooltip.register(8, field_22790 - 26, footerWidth, 20, "roar_of_love.tip.apply");
		}
		if (slotId.isEmpty()) {
			method_37063(new RoarButton(field_22789 - 88, field_22790 - 26, 80, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), button -> method_25419()));
		}
	}

	private void openSlot(String id) {
		class_310 client = class_310.method_1551();
		if (client != null) client.method_1507(new RoarAudioSlotScreen(this, id));
	}

	private String selectedFile() { return files.isEmpty() ? null : files.get(selected); }

	private void addFileRow(int index, int row, int width) {
		String file = files.get(index);
		String key = RoLSounds.eventKeyFor(slotId, file);
		boolean enabled = RoarOfLoveConfig.isSoundEnabled(key);
		boolean custom = AudioPackLoader.hasCustom(slotId, file);
		boolean builtin = AudioPackLoader.hasBuiltin(slotId, file);
		String stateKey = !enabled ? "roar_of_love.ui.tag_off" : custom ? "roar_of_love.ui.tag_custom" : builtin ? "roar_of_love.ui.tag_builtin" : "roar_of_love.ui.tag_empty";
		String state = class_2561.method_43469(stateKey, new Object[0]).getString();
		String label = (index == selected ? "> " : "  ") + file + "  " + state + "  " + (RoarOfLoveConfig.soundVolumeSteps(key) * 5) + "%";
		int x = ("cat:shots".equals(slotId) && index >= rowsPerColumn()) ? (field_22789 / 2) : 8;
		method_37063(new RoarButton(x, 46 + row * 17, width, 16, class_2561.method_43470(label), button -> { selected = index; clampScroll(); method_25426(); }));
	}

	private void openSlotFolder() {
		Path folder = AudioPackLoader.customDirFor(slotId);
		if (folder == null) {
			AudioPackLoader.sendChat(class_2561.method_43469("roar_of_love.chat.replace_unsupported", new Object[0]).getString());
			return;
		}
		AudioPackLoader.openFolder(folder);
		setNotice(class_2561.method_43469("roar_of_love.chat.opened_slot", new Object[]{folder.toAbsolutePath().toString()}).getString());
	}

	private void setNotice(String message) {
		notice = message == null ? "" : message;
		noticeUntil = System.currentTimeMillis() + 3000L;
		if (!notice.isEmpty()) AudioPackLoader.sendChat(notice);
	}
	private String eventKey() { String file = selectedFile(); return file == null ? null : RoLSounds.eventKeyFor(slotId, file); }

	private void preview() {
		String key = eventKey();
		class_3414 sound = key == null ? null : RoLSounds.get(key);
		class_310 client = class_310.method_1551();
		if (sound != null && client != null && client.method_1483() != null) client.method_1483().method_4873(class_1109.method_4757(sound, 1.0f, RoarOfLoveConfig.volume() * (RoarOfLoveConfig.soundVolumeSteps(key) / 10.0f)));
	}

	private void toggle() {
		String key = eventKey();
		if (key != null) { RoarOfLoveConfig.setSoundEnabled(key, !RoarOfLoveConfig.isSoundEnabled(key)); method_25426(); }
	}

	private void volume(int delta) {
		String key = eventKey();
		if (key != null) { RoarOfLoveConfig.setSoundVolumeSteps(key, RoarOfLoveConfig.soundVolumeSteps(key) + delta); method_25426(); }
	}

	private void pickFile() {
		String file = selectedFile();
		class_310 client = class_310.method_1551();
		if (file == null || client == null) return;
		Path directory = AudioPackLoader.customDirFor(slotId);
		if (directory == null || !Files.isDirectory(directory)) directory = AudioPaths.audioFolder();
		client.method_1507(new RoarFilePickerScreen(this, directory, file, path -> {
			String result = AudioPackLoader.replaceFrom(slotId, file, path);
			setNotice(result);
			if (result != null && result.equals(class_2561.method_43469("roar_of_love.chat.replace_staged", new Object[]{file}).getString())) {
				setNotice(AudioPackLoader.applyNow());
			}
			method_25426();
		}));
	}

	private void reset() {
		String file = selectedFile();
		if (file != null) {
			setNotice(AudioPackLoader.resetSlot(slotId, file));
			setNotice(AudioPackLoader.applyNow());
			method_25426();
		}
	}

	public boolean method_25401(double x, double y, double horizontal, double vertical) {
		if (slotId.isEmpty() || files.isEmpty() || vertical == 0.0d) return false;
		int row = Math.max(0, Math.min(rowsPerColumn() - 1, selectedRow() + (vertical > 0 ? -1 : 1)));
		int column = "cat:shots".equals(slotId) && selected >= rowsPerColumn() ? rowsPerColumn() : 0;
		selected = Math.min(files.size() - 1, column + row);
		clampScroll();
		method_25426();
		return true;
	}

	public boolean method_25402(net.minecraft.class_11909 event, boolean doubleClick) {
		int mouseY = (int) event.comp_4799();
		if (visibleRows() > 0 && event.method_74245() == 0 && mouseY >= 46 && mouseY < 46 + visibleRows() * 17) {
			int row = scroll + ((mouseY - 46) / 17);
			int leftWidth = "cat:shots".equals(slotId) ? (field_22789 / 2) - 8 : field_22789 - 16;
			int index = row;
			if ("cat:shots".equals(slotId) && event.comp_4798() >= field_22789 / 2) index = rowsPerColumn() + row;
			if (index >= 0 && index < files.size() && ("cat:shots".equals(slotId) ? event.comp_4798() >= field_22789 / 2 && event.comp_4798() < field_22789 - 8 || event.comp_4798() >= 8 && event.comp_4798() <= leftWidth + 8 : event.comp_4798() >= 8 && event.comp_4798() < field_22789 - 8)) {
				selected = index;
				clampScroll();
				method_25426();
				return true;
			}
		}
		return super.method_25402(event, doubleClick);
	}

	public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
		context.method_25290(class_10799.field_56883, BACKGROUND, 0, 0.0f, 0.0f, 0.0f, field_22789, field_22790, field_22789, field_22790);
		super.method_25394(context, mouseX, mouseY, delta);
		RoarHoverTooltip.render(context, mouseX, mouseY, field_22789, field_22790);
		if (System.currentTimeMillis() < noticeUntil && field_22793 != null && !notice.isEmpty()) {
			context.method_27535(field_22793, class_2561.method_43470(notice), 8, 12, -1);
		} else if (!slotId.isEmpty() && AudioPackLoader.isPackDirty() && field_22793 != null) {
			context.method_27535(field_22793, class_2561.method_43469("roar_of_love.ui.dirty_hint", new Object[0]), 8, 12, -15797);
		}
		if (slotId.equals("cat:shots") && field_22793 != null && rowsPerColumn() > visibleRows()) {
			context.method_27535(field_22793, class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]), field_22789 - 120, 30, -6381828);
		}
	}

	public void method_25419() {
		if (field_22787 != null && !slotId.isEmpty()) {
			if (AudioPackLoader.isPackDirty()) setNotice(AudioPackLoader.applyNow());
			field_22787.method_1507(parent);
		} else if (field_22787 != null) {
			field_22787.method_1507(parent);
		}
	}
}
