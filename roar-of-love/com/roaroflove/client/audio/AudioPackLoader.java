//
// Decompiled by Jadx - 881ms
//
package com.roaroflove.client.audio;

import com.roaroflove.RoarOfLove;
import com.roaroflove.sound.RoLSounds;
import com.roaroflove.util.AudioPaths;
import com.roaroflove.util.CallsAvailability;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3283;
import net.minecraft.class_3300;
import net.minecraft.class_3414;

public final class AudioPackLoader {
    public static final String PACK_FOLDER_NAME = "RoarOfLove_Audio";
    private static final String PACK_ZIP_NAME = "RoarOfLove-Audio.zip";
    public static final int RESOURCE_PACK_VERSION = 75;
    private static final List<String> CALL_STANDARD_NAMES = new ArrayList();
    private static volatile String lastStatus = "";
    private static boolean folderEnsured = false;
    private static volatile boolean packDirty = false;
    private static final Set<String> BUILTIN_ASSET_PATHS = new LinkedHashSet();

    static {
        CALL_STANDARD_NAMES.addAll(RoLSounds.CALL_BEGIN_FILES);
        CALL_STANDARD_NAMES.add("end");
    }

    private AudioPackLoader() {
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    public static String lastStatus() {
        if (lastStatus == null || lastStatus.isEmpty()) {
            lastStatus = tr("roar_of_love.status.builtin_simple", new Object[0]);
        }
        return lastStatus;
    }

    public static void onClientStarted() {
        try {
            ensureRootFolder();
            lastStatus = tr("roar_of_love.status.builtin_simple", new Object[0]);
        } catch (IOException e) {
            lastStatus = tr("roar_of_love.status.init_failed", e.getMessage());
            RoarOfLove.LOGGER.warn("[roar_of_love] 音频目录初始化失败", e);
        }
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                generatePack();
                ensurePackEnabled(method_1551);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 启动时重建覆盖包失败（自定义音频本次不生效）", th);
        }
    }

    public static void onWorldJoin() {
        try {
            refreshBuiltinAssets(class_310.method_1551());
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 内置音频登记异常", th);
        }
        try {
            refreshBundledCallsFromClient();
        } catch (Throwable th2) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 可用叫声登记失败", th2);
        }
    }

    public static void onWorldLeave() {
    }

    public static String slotKind(String str) {
        int indexOf = str == null ? -1 : str.indexOf(58);
        return indexOf < 0 ? "" : str.substring(0, indexOf);
    }

    public static String slotName(String str) {
        int indexOf = str == null ? -1 : str.indexOf(58);
        return indexOf < 0 ? "" : str.substring(indexOf + 1);
    }

    public static Path customDirFor(String str) {
        String slotKind = slotKind(str);
        String slotName = slotName(str);
        if (slotName.isEmpty()) {
            return null;
        }
        Path audioFolder = AudioPaths.audioFolder();
        char c = 65535;
        switch (slotKind.hashCode()) {
            case 98262:
                if (slotKind.equals("cat")) {
                    c = 0;
                    break;
                }
                break;
            case 3045982:
                if (slotKind.equals("call")) {
                    c = 1;
                    break;
                }
                break;
            case 3214227:
                if (slotKind.equals("hunt")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return audioFolder.resolve("actionsounds").resolve(slotName);
            case 1:
                return "default".equals(slotName) ? audioFolder.resolve("calls") : audioFolder.resolve("calls").resolve(slotName);
            case 2:
                return audioFolder.resolve("vanilla").resolve("hurt");
            default:
                return null;
        }
    }

    public static String resourcePathFor(String str, String str2) {
        String slotKind = slotKind(str);
        String slotName = slotName(str);
        String str3 = str2 + ".ogg";
        char c = 65535;
        switch (slotKind.hashCode()) {
            case 98262:
                if (slotKind.equals("cat")) {
                    c = 0;
                    break;
                }
                break;
            case 3045982:
                if (slotKind.equals("call")) {
                    c = 1;
                    break;
                }
                break;
            case 3214227:
                if (slotKind.equals("hunt")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "assets/roar_of_love/sounds/actionsounds/" + slotName + "/" + str3;
            case 1:
                if ("default".equals(slotName)) {
                    return "assets/roar_of_love/sounds/calls/" + str3;
                }
                return "assets/roar_of_love/sounds/calls/" + slotName + "/" + str3;
            case 2:
                return "assets/roar_of_love/sounds/vanilla/hurt/" + str3;
            default:
                return null;
        }
    }

    public static Path customFileFor(String str, String str2) {
        Path customDirFor = customDirFor(str);
        if (customDirFor == null) {
            return null;
        }
        return customDirFor.resolve(str2 + ".ogg");
    }

    public static boolean hasCustom(String str, String str2) {
        Path customFileFor = customFileFor(str, str2);
        return customFileFor != null && Files.isRegularFile(customFileFor, new LinkOption[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002b, code lost:
    
        if (r3[3] == 83) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isOggVorbis(Path path) {
        boolean z = true;
        try {
            InputStream newInputStream = Files.newInputStream(path, new OpenOption[0]);
            try {
                byte[] bArr = new byte[4];
                if (newInputStream.read(bArr) == 4 && bArr[0] == 79 && bArr[1] == 103 && bArr[2] == 103) {
                }
                z = false;
                if (newInputStream != null) {
                    newInputStream.close();
                    return z;
                }
                return z;
            } finally {
            }
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean hasBuiltin(String str, String str2) {
        String builtinRelPath = builtinRelPath(str, str2);
        if (builtinRelPath == null) {
            return false;
        }
        if (BUILTIN_ASSET_PATHS.isEmpty()) {
            return AudioPackLoader.class.getResource("/assets/roar_of_love/sounds/" + builtinRelPath) != null;
        }
        return BUILTIN_ASSET_PATHS.contains(builtinRelPath);
    }

    private static void refreshBuiltinAssets(class_310 class_310Var) {
        if (class_310Var != null) {
            try {
                if (class_310Var.method_1478() != null) {
                    class_3300 method_1478 = class_310Var.method_1478();
                    LinkedHashSet linkedHashSet = new LinkedHashSet();
                    int i = 0;
                    for (String str : RoLSounds.CATEGORIES) {
                        Iterator it = ((List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of())).iterator();
                        while (it.hasNext()) {
                            i++;
                            String str2 = "actionsounds/" + str + "/" + ((String) it.next()) + ".ogg";
                            if (method_1478.method_14486(class_2960.method_60655("roar_of_love", "sounds/" + str2)).isPresent()) {
                                linkedHashSet.add(str2);
                            }
                        }
                    }
                    int i2 = i;
                    for (String str3 : RoLSounds.CALL_GROUPS) {
                        Iterator it2 = RoLSounds.callSlotsFor(str3).iterator();
                        int i3 = i2;
                        while (it2.hasNext()) {
                            i3++;
                            String builtinRelPathFor = RoLSounds.builtinRelPathFor("call:" + str3, (String) it2.next());
                            if (builtinRelPathFor != null && method_1478.method_14486(class_2960.method_60655("roar_of_love", "sounds/" + builtinRelPathFor)).isPresent()) {
                                linkedHashSet.add(builtinRelPathFor);
                            }
                        }
                        i2 = i3;
                    }
                    Iterator it3 = RoLSounds.hurtSlots().iterator();
                    while (it3.hasNext()) {
                        i2++;
                        String builtinRelPathFor2 = RoLSounds.builtinRelPathFor("hunt:hurt", (String) it3.next());
                        if (builtinRelPathFor2 != null && method_1478.method_14486(class_2960.method_60655("roar_of_love", "sounds/" + builtinRelPathFor2)).isPresent()) {
                            linkedHashSet.add(builtinRelPathFor2);
                        }
                    }
                    BUILTIN_ASSET_PATHS.clear();
                    BUILTIN_ASSET_PATHS.addAll(linkedHashSet);
                    RoarOfLove.LOGGER.info("[roar_of_love] 内置音频登记完成：检测 {} 项，命中 {} 项", Integer.valueOf(i2), Integer.valueOf(linkedHashSet.size()));
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 内置音频登记失败（界面会退回 classloader 探测）", th);
            }
        }
    }

    private static String builtinRelPath(String str, String str2) {
        return RoLSounds.builtinRelPathFor(str, str2);
    }

    public static String replaceFrom(String str, String str2, Path path) {
        String tr;
        if (path != null) {
            try {
                if (Files.isRegularFile(path, new LinkOption[0])) {
                    if (!path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg")) {
                        tr = tr("roar_of_love.chat.replace_not_ogg", new Object[0]);
                    } else if (!isOggVorbis(path)) {
                        tr = tr("roar_of_love.chat.replace_not_really_ogg", path.getFileName().toString());
                    } else {
                        Path customFileFor = customFileFor(str, str2);
                        if (customFileFor == null) {
                            tr = tr("roar_of_love.chat.replace_unsupported", new Object[0]);
                        } else {
                            Files.createDirectories(customFileFor.getParent(), new FileAttribute[0]);
                            Files.copy(path, customFileFor, StandardCopyOption.REPLACE_EXISTING);
                            packDirty = true;
                            RoarOfLove.LOGGER.info("[roar_of_love] 替换音频 {} -> {}", path.getFileName(), customFileFor);
                            tr = tr("roar_of_love.chat.replace_staged", str2);
                        }
                    }
                    return tr;
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 替换音频失败", th);
                return tr("roar_of_love.chat.replace_error", String.valueOf(th.getMessage()));
            }
        }
        tr = tr("roar_of_love.chat.replace_bad_src", new Object[0]);
        return tr;
    }

    public static String resetSlot(String str, String str2) {
        String tr;
        try {
            Path customFileFor = customFileFor(str, str2);
            if (customFileFor == null) {
                tr = tr("roar_of_love.chat.replace_unsupported", new Object[0]);
            } else if (!Files.exists(customFileFor, new LinkOption[0])) {
                tr = tr("roar_of_love.chat.reset_none", str2);
            } else {
                Files.delete(customFileFor);
                packDirty = true;
                RoarOfLove.LOGGER.info("[roar_of_love] 已恢复内置音频 {}", customFileFor);
                tr = tr("roar_of_love.chat.reset_staged", str2);
            }
            return tr;
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 恢复内置音频失败", th);
            return tr("roar_of_love.chat.replace_error", String.valueOf(th.getMessage()));
        }
    }

    public static boolean isPackDirty() {
        return packDirty;
    }

    public static void clearDirty() {
        packDirty = false;
    }

    public static String applyNow() {
        if (!packDirty) {
            return tr("roar_of_love.chat.apply_nothing", new Object[0]);
        }
        String rebuildAndReload = rebuildAndReload();
        if (rebuildAndReload.isEmpty()) {
            packDirty = false;
            return tr("roar_of_love.chat.apply_ok", new Object[0]);
        }
        return rebuildAndReload;
    }

    public static void applyOnExit() {
        if (packDirty) {
            String rebuildAndReload = rebuildAndReload();
            if (rebuildAndReload.isEmpty()) {
                packDirty = false;
                sendChat(tr("roar_of_love.chat.apply_ok", new Object[0]));
            } else {
                sendChat(rebuildAndReload);
            }
        }
    }

    public static String sourceLabel(String str, String str2) {
        return tr(hasCustom(str, str2) ? "roar_of_love.ui.src_custom" : "roar_of_love.ui.src_builtin", new Object[0]);
    }

    public static String rebuildAndReload() {
        String str;
        try {
            generatePack();
            try {
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 == null) {
                    str = "";
                } else if (!ensurePackEnabled(method_1551)) {
                    str = tr("roar_of_love.chat.pack_enable_failed", new Object[0]);
                } else {
                    method_1551.method_1513();
                    str = "";
                }
                return str;
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 资源重载失败（音频已写入，重启游戏后生效）", th);
                return tr("roar_of_love.chat.pack_reload_failed", new Object[0]);
            }
        } catch (Throwable th2) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 生成覆盖包失败", th2);
            return tr("roar_of_love.chat.pack_build_failed", String.valueOf(th2.getMessage()));
        }
    }

    public static void generatePack() throws IOException {
        Path resolve = AudioPaths.resourcePacksDir().resolve(PACK_ZIP_NAME);
        Files.createDirectories(resolve.getParent(), new FileAttribute[0]);
        Files.createDirectories(AudioPaths.audioFolder(), new FileAttribute[0]);
        ArrayList<Path> arrayList = new ArrayList();
        collectOgg(AudioPaths.audioFolder(), arrayList);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        String path = AudioPaths.audioFolder().toAbsolutePath().toString();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(((Path) it.next()).toAbsolutePath().toString().substring(path.length() + 1).replace('\\', '/'));
        }
        String buildSoundsJson = buildSoundsJson(linkedHashSet);
        OutputStream newOutputStream = Files.newOutputStream(resolve, new OpenOption[0]);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(newOutputStream);
            try {
                String str = "{\"pack\":{\"pack_format\":75,\"min_format\":75,\"max_format\":75,\"description\":\"" + tr("roar_of_love.pack.desc", new Object[0]).replace("\"", "'") + "\"}}";
                zipOutputStream.putNextEntry(new ZipEntry("pack.mcmeta"));
                zipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
                zipOutputStream.closeEntry();
                for (Path path2 : arrayList) {
                    zipOutputStream.putNextEntry(new ZipEntry("assets/roar_of_love/sounds/" + path2.toAbsolutePath().toString().substring(path.length() + 1).replace('\\', '/')));
                    Files.copy(path2, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.putNextEntry(new ZipEntry("assets/roar_of_love/sounds.json"));
                zipOutputStream.write(buildSoundsJson.getBytes(StandardCharsets.UTF_8));
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                if (newOutputStream != null) {
                    newOutputStream.close();
                }
                RoarOfLove.LOGGER.info("[roar_of_love] 覆盖包已生成：{} 个自定义音频 -> {}", Integer.valueOf(arrayList.size()), resolve.getFileName());
            } finally {
            }
        } catch (Throwable th) {
            if (newOutputStream != null) {
                try {
                    newOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static String buildSoundsJson(Set<String> set) {
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        boolean z2 = true;
        Iterator it = RoLSounds.CATEGORIES.iterator();
        while (true) {
            z = z2;
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            List<String> list = (List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of());
            ArrayList arrayList = new ArrayList();
            for (String str2 : list) {
                if (hasAsset(set, "actionsounds/" + str + "/" + str2 + ".ogg")) {
                    arrayList.add(str2);
                }
            }
            if (!arrayList.isEmpty()) {
                boolean appendEvent = appendEvent(sb, str, "actionsounds/" + str, arrayList, z);
                Iterator it2 = arrayList.iterator();
                while (true) {
                    z = appendEvent;
                    if (it2.hasNext()) {
                        String str3 = (String) it2.next();
                        appendEvent = appendEvent(sb, str + "." + str3, "actionsounds/" + str, List.of(str3), z);
                    }
                }
            }
            z2 = z;
        }
        for (String str4 : RoLSounds.CALL_GROUPS) {
            List<String> callSlotsFor = RoLSounds.callSlotsFor(str4);
            ArrayList<String> arrayList2 = new ArrayList();
            String str5 = "default".equals(str4) ? "calls" : "calls/" + str4;
            for (String str6 : callSlotsFor) {
                if (hasAsset(set, str5 + "/" + str6 + ".ogg")) {
                    arrayList2.add(str6);
                }
            }
            if (!arrayList2.isEmpty()) {
                for (String str7 : arrayList2) {
                    z = appendEvent(sb, RoLSounds.callEventKey(str4, str7), str5, List.of(str7), z);
                }
            }
        }
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (String str8 : RoLSounds.hurtSlots()) {
            if (set.contains("vanilla/hurt/" + str8 + ".ogg")) {
                arrayList3.add(str8);
                arrayList4.add("vanilla/hurt");
            } else if (set.contains("hurt/" + str8 + ".ogg")) {
                arrayList3.add(str8);
                arrayList4.add("hurt");
            } else if (AudioPackLoader.class.getResource("/assets/roar_of_love/sounds/vanilla/hurt/" + str8 + ".ogg") != null) {
                arrayList3.add(str8);
                arrayList4.add("vanilla/hurt");
            }
        }
        if (!arrayList3.isEmpty()) {
            boolean appendEventWithDirs = appendEventWithDirs(sb, "hunt", arrayList3, arrayList4, z);
            int i = 0;
            while (true) {
                int i2 = i;
                boolean z3 = appendEventWithDirs;
                if (i2 >= arrayList3.size()) {
                    break;
                }
                appendEventWithDirs = appendEvent(sb, "hunt." + ((String) arrayList3.get(i2)), (String) arrayList4.get(i2), List.of((String) arrayList3.get(i2)), z3);
                i = i2 + 1;
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static boolean appendEventWithDirs(StringBuilder sb, String str, List<String> list, List<String> list2, boolean z) {
        if (!z) {
            sb.append(",\n");
        }
        sb.append("  \"").append(str).append("\": {\n    \"sounds\": [\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("      {\"name\": \"roar_of_love:").append(list2.get(i)).append("/").append(list.get(i)).append("\", \"attenuation_distance\": 256}");
            if (i < list.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append("    ]\n  }");
        return false;
    }

    private static boolean hasAsset(Set<String> set, String str) {
        if (set.contains(str)) {
            return true;
        }
        if (str == null || str.isEmpty() || str.startsWith("/") || str.contains("..")) {
            return false;
        }
        return AudioPackLoader.class.getResource("/assets/roar_of_love/sounds/" + str) != null;
    }

    private static boolean appendEvent(StringBuilder sb, String str, String str2, List<String> list, boolean z) {
        if (!z) {
            sb.append(",\n");
        }
        sb.append("  \"").append(str).append("\": {\n    \"sounds\": [\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("      {\"name\": \"roar_of_love:").append(str2).append("/").append(list.get(i)).append("\", \"attenuation_distance\": 256}");
            if (i < list.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append("    ]\n  }");
        return false;
    }

    private static void collectOgg(Path path, List<Path> list) throws IOException {
        if (Files.isDirectory(path, new LinkOption[0])) {
            Stream<Path> list2 = Files.list(path);
            try {
                Objects.requireNonNull(list2);
                Iterable<Path> iterable = list2::iterator;
                for (Path path2 : iterable) {
                    try {
                        if (Files.isDirectory(path2, new LinkOption[0])) {
                            if (!path2.getFileName().toString().startsWith("_")) {
                                collectOgg(path2, list);
                            }
                        } else if (path2.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg")) {
                            list.add(path2);
                        }
                    } catch (Throwable th) {
                    }
                }
                if (list2 != null) {
                    list2.close();
                }
            } catch (Throwable th2) {
                if (list2 != null) {
                    try {
                        list2.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    public static boolean ensurePackEnabled(class_310 class_310Var) {
        try {
            if (class_310Var.field_1690 == null) {
                return false;
            }
            List list = class_310Var.field_1690.field_1887;
            list.remove(PACK_FOLDER_NAME);
            if (!list.contains(PACK_ZIP_NAME)) {
                list.add(PACK_ZIP_NAME);
                class_310Var.field_1690.method_1640();
            }
            try {
                class_3283 method_1520 = class_310Var.method_1520();
                if (method_1520 != null) {
                    method_1520.method_14445();
                    method_1520.method_49427(PACK_ZIP_NAME);
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 更新资源包管理器状态失败（不影响 options 写入）", th);
            }
            return true;
        } catch (Throwable th2) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 启用覆盖包失败", th2);
            return false;
        }
    }

    private static void ensureRootFolder() throws IOException {
        Path audioFolder = AudioPaths.audioFolder();
        Files.createDirectories(audioFolder, new FileAttribute[0]);
        if (!folderEnsured) {
            folderEnsured = true;
            Path resolve = audioFolder.resolve("README.txt");
            if (!Files.exists(resolve, new LinkOption[0])) {
                Files.writeString(resolve, tr("roar_of_love.readme.folder", new Object[0]), StandardCharsets.UTF_8, new OpenOption[0]);
            }
        }
    }

    public static void openAudioFolder() {
        openFolder(AudioPaths.audioFolder());
    }

    public static void openFolder(Path path) {
        Process start;
        try {
            Files.createDirectories(path, new FileAttribute[0]);
            String path2 = path.toAbsolutePath().toString();
            String lowerCase = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
            if (lowerCase.contains("win")) {
                start = new ProcessBuilder("explorer.exe", path2).start();
            } else if (lowerCase.contains("mac")) {
                start = new ProcessBuilder("open", path2).start();
            } else {
                start = new ProcessBuilder("xdg-open", path2).start();
            }
            sendChat(tr("roar_of_love.chat.opened", path2));
            RoarOfLove.LOGGER.info("[roar_of_love] 已请求打开文件夹 {} (pid={})", path2, Long.valueOf(start.pid()));
        } catch (Exception e) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 打开文件夹失败", e);
            try {
                Files.createDirectories(path, new FileAttribute[0]);
                Desktop.getDesktop().open(path.toFile());
                sendChat(tr("roar_of_love.chat.opened_fallback", path.toAbsolutePath()));
            } catch (Exception e2) {
                sendChat(tr("roar_of_love.chat.open_failed", e2.getMessage()));
            }
        }
    }

    private static void refreshBundledCallsFromClient() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null && method_1551.method_1478() != null) {
            class_3300 method_1478 = method_1551.method_1478();
            for (String str : RoLSounds.CALL_GROUPS) {
                String str2 = "default".equals(str) ? "sounds/calls/" : "sounds/calls/" + str + "/";
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                for (String str3 : CALL_STANDARD_NAMES) {
                    if (method_1478.method_14486(class_2960.method_60655("roar_of_love", str2 + str3 + ".ogg")).isPresent()) {
                        linkedHashSet.add(str3);
                    }
                }
                if (!linkedHashSet.isEmpty()) {
                    linkedHashMap.put(str, linkedHashSet);
                }
            }
        }
        CallsAvailability.setBundledCallFiles(linkedHashMap);
    }

    public static class_3414 pickCategoryPreview(String str) {
        class_3300 method_1478 = class_310.method_1551() == null ? null : class_310.method_1551().method_1478();
        ArrayList arrayList = new ArrayList();
        for (String str2 : (List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of())) {
            if (method_1478 == null || method_1478.method_14486(class_2960.method_60655("roar_of_love", "sounds/actionsounds/" + str + "/" + str2 + ".ogg")).isPresent()) {
                arrayList.add(str2);
            }
        }
        if (arrayList.isEmpty()) {
            return RoLSounds.get(str);
        }
        class_3414 class_3414Var = RoLSounds.get(str + "." + ((String) arrayList.get(ThreadLocalRandom.current().nextInt(arrayList.size()))));
        return class_3414Var == null ? RoLSounds.get(str) : class_3414Var;
    }

    public static class_3414 pickCallPreview(String str, boolean z) {
        class_3414 class_3414Var;
        class_3414 class_3414Var2;
        if (str == null || str.isEmpty()) {
            str = "default";
        }
        Set<String> availableCallFiles = CallsAvailability.availableCallFiles(str);
        ArrayList arrayList = new ArrayList();
        for (String str2 : availableCallFiles) {
            if (str2.startsWith("begin")) {
                arrayList.add(str2);
            }
        }
        if (!z || arrayList.isEmpty() || (class_3414Var2 = RoLSounds.get(RoLSounds.callEventKey(str, (String) arrayList.get(ThreadLocalRandom.current().nextInt(arrayList.size()))))) == null) {
            if (z || !availableCallFiles.contains("end") || (class_3414Var = RoLSounds.get(RoLSounds.callEventKey(str, "end"))) == null) {
                return null;
            }
            return class_3414Var;
        }
        return class_3414Var2;
    }

    public static void sendChat(String str) {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1724 != null) {
                method_1551.field_1724.method_7353(class_2561.method_43470("[Roar of Love] " + str), false);
            }
        } catch (Exception e) {
        }
    }

    public static List<String> missingDefaultAssets(class_310 class_310Var) {
        ArrayList arrayList = new ArrayList();
        if (class_310Var != null && class_310Var.method_1478() != null) {
            class_3300 method_1478 = class_310Var.method_1478();
            for (String[] strArr : new String[][]{new String[]{"roar_of_love", "sounds/actionsounds/wet/wet01.ogg"}, new String[]{"roar_of_love", "sounds/actionsounds/dryimpacts/impactdry01.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/hurt/hurt1.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/tnt/fuse.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/bow/bow.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/anvil/anvil_land.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/xp/orb.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/break/break.ogg"}, new String[]{"roar_of_love", "sounds/vanilla/lava/fizz.ogg"}, new String[]{"roar_of_love", "sounds/actionsounds/motions/motion01.ogg"}, new String[]{"roar_of_love", "sounds/actionsounds/retract/retract01.ogg"}, new String[]{"roar_of_love", "sounds/actionsounds/shots/shot_in01.ogg"}, new String[]{"roar_of_love", "sounds/actionsounds/wetimpacts/impactwet01.ogg"}}) {
                if (!method_1478.method_14486(class_2960.method_60655(strArr[0], strArr[1])).isPresent()) {
                    arrayList.add(strArr[1].replace("sounds/", ""));
                }
            }
        }
        return arrayList;
    }
}
