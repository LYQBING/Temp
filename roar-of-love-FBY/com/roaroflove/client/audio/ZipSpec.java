//
// Decompiled by Jadx - 727ms
//
package com.roaroflove.client.audio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ZipSpec {
    public static final class VanillaGroup {
        private final String key;
        private final String display;
        private final List<String> events;
        private final List<String> fileNames;

        public VanillaGroup(String key, String display, List<String> events, List<String> fileNames) {
            this.key = key;
            this.display = display;
            this.events = events;
            this.fileNames = fileNames;
        }

        public String key() { return this.key; }
        public String display() { return this.display; }
        public List<String> events() { return this.events; }
        public List<String> fileNames() { return this.fileNames; }
    }
    public static final String CALLS_DISPLAY = "calls(叫声)";
    public static final String TOP_MINECRAFT = "minecraft";
    public static final String TOP_ROAR = "roar of love";
    public static final List<ZipSpec.VanillaGroup> VANILLA_GROUPS = List.of(new ZipSpec.VanillaGroup("hurt", "hurt(受伤声)", List.of("entity.player.hurt", "entity.player.hurt_drown", "entity.player.hurt_on_fire"), List.of("hurt1", "hurt2", "hurt3", "hurt4", "hurt5", "hurt6", "hurt7", "hurt8", "hurt9")), new ZipSpec.VanillaGroup("tnt", "tnt(TNT点燃)", List.of("entity.tnt.primed"), List.of("fuse")), new ZipSpec.VanillaGroup("bow", "bow(弓箭射箭)", List.of("entity.arrow.shoot"), List.of("bow")), new ZipSpec.VanillaGroup("anvil", "anvil(铁砧放置与使用)", List.of("block.anvil.land", "block.anvil.place", "block.anvil.use"), List.of("anvil_land", "anvil_use")), new ZipSpec.VanillaGroup("xp", "xp(升级与拾取经验)", List.of("entity.experience_orb.pickup", "entity.player.levelup"), List.of("orb", "levelup")), new ZipSpec.VanillaGroup("break", "break(装备损坏)", List.of("entity.item.break", "item.shield.break"), List.of("break")), new ZipSpec.VanillaGroup("lava", "lava(岩浆遇水气泡)", List.of("block.lava.extinguish"), List.of("fizz")));
    public static final Map<String, String> CATEGORY_DISPLAY = new LinkedHashMap();

    static {
        CATEGORY_DISPLAY.put("dryimpacts", "dryimpacts(普通性爱撞击声)");
        CATEGORY_DISPLAY.put("motions", "motions(抽插声)");
        CATEGORY_DISPLAY.put("retract", "retract(精液滴落)");
        CATEGORY_DISPLAY.put("shots", "shots(射精)");
        CATEGORY_DISPLAY.put("wet", "wet(口交)");
        CATEGORY_DISPLAY.put("wetimpacts", "wetimpacts(被灌满时性爱撞击声)");
    }

    public static String sanitizeKey(String str) {
        char lowerCase;
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length() && (((lowerCase = Character.toLowerCase(str.charAt(i))) >= 'a' && lowerCase <= 'z') || ((lowerCase >= '0' && lowerCase <= '9') || lowerCase == '_')); i++) {
            sb.append(lowerCase);
        }
        return sb.toString();
    }

    public static String keyFromDisplay(String str) {
        String sanitizeKey = sanitizeKey(str);
        if (sanitizeKey.isEmpty()) {
            if (str == null) {
                return "";
            }
            String lowerCase = str.toLowerCase(Locale.ROOT);
            for (String[] strArr : new String[][]{new String[]{"hurt", "受伤", "受击", "呻吟", "娇喘"}, new String[]{"wetimpacts", "灌满", "满"}, new String[]{"dryimpacts", "撞击", "干"}, new String[]{"motions", "抽插", "运动"}, new String[]{"retract", "滴落", "精液", "拔出"}, new String[]{"shots", "射精", "射"}, new String[]{"wet", "口交", "湿"}, new String[]{"calls", "叫声", "叫"}, new String[]{"tnt", "点燃", "爆炸", "tnt"}, new String[]{"bow", "弓", "箭"}, new String[]{"anvil", "铁砧", "砧"}, new String[]{"xp", "经验", "升级"}, new String[]{"break", "损坏", "破坏"}, new String[]{"lava", "岩浆", "气泡", "fizz"}}) {
                for (int i = 1; i < strArr.length; i++) {
                    if (lowerCase.contains(strArr[i])) {
                        return strArr[0];
                    }
                }
            }
            return "";
        }
        return sanitizeKey;
    }

    public static ZipSpec.VanillaGroup vanillaGroupByKey(String str) {
        for (ZipSpec.VanillaGroup vanillaGroup : VANILLA_GROUPS) {
            if (vanillaGroup.key().equals(str)) {
                return vanillaGroup;
            }
        }
        return null;
    }

    public static String normalizeTop(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.toLowerCase(Locale.ROOT).trim();
        if (trim.startsWith(TOP_MINECRAFT) || nearMiss(trim, TOP_MINECRAFT)) {
            return TOP_MINECRAFT;
        }
        if (trim.startsWith(TOP_ROAR) || trim.startsWith("roaroflove") || trim.startsWith("roar_of_love") || nearMiss(trim, TOP_ROAR) || nearMiss(trim, "roaroflove") || nearMiss(trim, "roar_of_love")) {
            return TOP_ROAR;
        }
        if (trim.startsWith("needsofnature") || nearMiss(trim, "needsofnature")) {
            return "needsofnature";
        }
        return null;
    }

    private static boolean nearMiss(String str, String str2) {
        return str.length() >= 5 && Math.abs(str.length() - str2.length()) <= 2 && editDistance(str, str2) <= 2;
    }

    private static int editDistance(String str, String str2) {
        int[] iArr = new int[str2.length() + 1];
        int[] iArr2 = new int[str2.length() + 1];
        for (int i = 0; i <= str2.length(); i++) {
            iArr[i] = i;
        }
        int i2 = 1;
        while (i2 <= str.length()) {
            iArr2[0] = i2;
            for (int i3 = 1; i3 <= str2.length(); i3++) {
                iArr2[i3] = Math.min(Math.min(iArr2[i3 - 1] + 1, iArr[i3] + 1), (str.charAt(i2 + (-1)) == str2.charAt(i3 + (-1)) ? 0 : 1) + iArr[i3 - 1]);
            }
            i2++;
            int[] iArr3 = iArr;
            iArr = iArr2;
            iArr2 = iArr3;
        }
        return iArr[str2.length()];
    }

    private ZipSpec() {
    }
}
