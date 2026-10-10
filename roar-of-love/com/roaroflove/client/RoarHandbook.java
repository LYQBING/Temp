package com.roaroflove.client;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class RoarHandbook {
	private static final int CLOSE = 8;
	private static final Map<String, Integer> AFFECTION = new LinkedHashMap<>();
	private static int creatureCount;
	private static boolean loaded;

	private RoarHandbook() {
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("roar_of_love_handbook.txt");
	}

	public static synchronized void load() {
		if (loaded) {
			return;
		}
		loaded = true;
		try {
			Path file = file();
			if (Files.exists(file)) {
				for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
					String[] parts = line.split("\\t");
					if (parts.length >= 3 && "P".equals(parts[0])) {
						AFFECTION.put(parts[1], Integer.parseInt(parts[2]));
					} else if (parts.length >= 2 && "C".equals(parts[0])) {
						creatureCount = Integer.parseInt(parts[1]);
					}
				}
			}
		} catch (Exception ignored) {
			AFFECTION.clear();
			creatureCount = 0;
		}
	}

	public static synchronized void save() {
		try {
			Path file = file();
			Files.createDirectories(file.getParent());
			List<String> lines = new ArrayList<>();
			for (Map.Entry<String, Integer> entry : AFFECTION.entrySet()) {
				lines.add("P\t" + entry.getKey() + "\t" + entry.getValue());
			}
			lines.add("C\t" + creatureCount);
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (Exception ignored) {
		}
	}

	public static synchronized void noteAnimation(List<UUID> actors) {
		if (actors == null || actors.isEmpty()) {
			return;
		}
		load();
		class_310 client = class_310.method_1551();
		if (client == null || client.field_1687 == null || client.field_1724 == null) {
			return;
		}
		int found = 0;
		try {
			for (class_746 player : client.field_1687.method_18456()) {
				if (actors.contains(player.method_5667())) {
					found++;
					if (player != client.field_1724) {
						String name = player.method_5477().getString();
						AFFECTION.put(name, Math.min(50, AFFECTION.getOrDefault(name, 0) + 1));
					}
				}
			}
		} catch (Throwable ignored) {
		}
		if (actors.size() > Math.max(1, found)) {
			creatureCount = Math.min(9999, creatureCount + actors.size() - Math.max(1, found));
		}
		save();
	}

	public static synchronized int affection(String name) {
		load();
		return AFFECTION.getOrDefault(name, 0);
	}

	public static synchronized boolean isClose(String name) {
		return affection(name) >= CLOSE;
	}

	public static synchronized int creatureCount() {
		load();
		return creatureCount;
	}

	public static synchronized List<String> summary() {
		load();
		List<String> rows = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : AFFECTION.entrySet()) {
			rows.add(entry.getKey() + "   " + entry.getValue());
		}
		rows.add("creatures   " + creatureCount);
		return rows;
	}
}
