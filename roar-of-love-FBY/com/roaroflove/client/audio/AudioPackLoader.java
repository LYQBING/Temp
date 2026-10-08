//
// Decompiled by Jadx - 1399ms
//
package com.roaroflove.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.ZipSpec;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import com.roaroflove.util.AudioPaths;
import com.roaroflove.util.CallsAvailability;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3283;
import net.minecraft.class_3288;
import net.minecraft.class_3300;
import net.minecraft.class_3414;

public final class AudioPackLoader {
    public static final String PACK_FOLDER_NAME = "RoarOfLove-Audio";
    public static final int RESOURCE_PACK_VERSION = 75;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static boolean folderEnsured = false;
    private static boolean templateEnsured = false;
    private static long lastAppliedStamp = -1;
    private static boolean packEnabled = false;
    private static volatile boolean busy = false;
    private static volatile String lastStatus = "";
    private static volatile String activePackFileName = "RoarOfLove-Audio.zip";
    private static final List<String> CALL_STANDARD_NAMES = new ArrayList();
    private static final Set<String> packProvided = new LinkedHashSet();

    static {
        CALL_STANDARD_NAMES.addAll(RoLSounds.CALL_BEGIN_FILES);
        CALL_STANDARD_NAMES.add("end");
    }

    private static List<String> providedIn(String str) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : packProvided) {
            if (str2.startsWith(str) && str2.endsWith(".ogg")) {
                String substring = str2.substring(str.length(), str2.length() - 4);
                if (substring.indexOf(47) < 0) {
                    arrayList.add(substring);
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static List<String> filesFor(String str, List<String> list) {
        List<String> providedIn = providedIn(str);
        return providedIn.isEmpty() ? list : providedIn;
    }

    private AudioPackLoader() {
    }

    public static String lastStatus() {
        return lastStatus;
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    private static String packId() {
        String str = activePackFileName;
        String packProfileId = AudioPaths.packProfileId(str);
        class_3283 manager = manager();
        if (manager != null && !manager.method_29207(packProfileId)) {
            Iterator it = manager.method_14441().iterator();
            while (it.hasNext()) {
                String method_14463 = ((class_3288) it.next()).method_14463();
                if (method_14463.endsWith(str)) {
                    return method_14463;
                }
            }
            return packProfileId;
        }
        return packProfileId;
    }

    public static boolean isBusy() {
        return busy;
    }

    public static void onClientStarted() {
        try {
            ensureRootFolderAndTemplate();
            migrateZipAllowlist();
            cleanupStalePacks();
            updateStatusNoReload();
        } catch (IOException e) {
            lastStatus = tr("roar_of_love.status.init_failed", e.getMessage());
            RoarOfLove.LOGGER.warn("[roar_of_love] 初始化失败", e);
        }
    }

    private static void migrateZipAllowlist() {
        if (RoarOfLoveConfig.needsZipMigration()) {
            List<String> detectedZipNames = detectedZipNames();
            RoarOfLoveConfig.migrateZipAllowlist(detectedZipNames);
            RoarOfLove.LOGGER.info("[roar_of_love] 已启用压缩包列表初始化：{}；新放入的压缩包默认禁用，需在设置界面手动启用", detectedZipNames);
        }
    }

    public static void onWorldJoin() {
        if (RoarOfLoveConfig.isZipEnabled()) {
            int enabledZipCount = enabledZipCount();
            boolean exists = Files.exists(packRoot().resolve("pack.mcmeta"), new LinkOption[0]);
            long j = -2;
            try {
                j = contentStamp();
            } catch (IOException e) {
            }
            boolean z = j != lastAppliedStamp;
            if ((enabledZipCount > 0 && (z || !packEnabled)) || (enabledZipCount == 0 && exists)) {
                refreshSync(AudioPackLoader::refreshBundledCallsFromClient);
            } else {
                refreshBundledCallsFromClient();
                updateStatusNoReload();
            }
        }
    }

    public static void onWorldLeave() {
    }

    public static void refreshNow(Runnable runnable) {
        refreshSync(() -> {
            refreshBundledCallsFromClient();
            if (runnable != null) {
                runnable.run();
            }
        });
    }

    private static synchronized void refreshSync(Runnable runnable) {
        synchronized (AudioPackLoader.class) {
            if (!busy) {
                busy = true;
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 == null) {
                    doRefresh(runnable);
                } else {
                    if (enabledContainsUs()) {
                        removeUsFromEnabled();
                        try {
                            RoarOfLove.LOGGER.info("[roar_of_love] 刷新前先卸载覆盖包，以释放被游戏占用的 zip 文件");
                            method_1551.method_1513().whenComplete((r3, th) -> {
                                method_1551.execute(() -> {
                                    doRefresh(runnable);
                                });
                            });
                        } catch (Throwable th2) {
                            RoarOfLove.LOGGER.warn("[roar_of_love] 卸载覆盖包失败，改为直接刷新", th2);
                        }
                    }
                    doRefresh(runnable);
                }
            }
        }
    }

    private static void doRefresh(Runnable runnable) {
        int i;
        try {
            ensureRootFolderAndTemplate();
            migrateZipAllowlist();
            long contentStamp = contentStamp();
            boolean z = packEnabled;
            Path packRoot = packRoot();
            safeDeleteTree(packRoot);
            Path preparePackZip = preparePackZip();
            safeDeleteTree(legacyPackFolder());
            List<Path> detectedZips = detectedZips();
            packProvided.clear();
            if (!detectedZips.isEmpty()) {
                Files.createDirectories(packRoot, new FileAttribute[0]);
            }
            int i2 = 0;
            for (Path path : detectedZips) {
                if (RoarOfLoveConfig.isZipDisabled(path.getFileName().toString())) {
                    i = i2;
                } else {
                    extractZip(path, packRoot);
                    i = i2 + 1;
                }
                i2 = i;
            }
            if (i2 > 0) {
                ensureBuiltinFiles(packRoot);
                writeJson(packRoot.resolve("assets/roar_of_love/sounds.json"), buildRoarSoundsJson());
                writeJson(packRoot.resolve("assets/minecraft/sounds.json"), buildMinecraftOverrideJson(packRoot));
                writeJson(packRoot.resolve("assets/needsofnature/sounds.json"), buildNeedsofnatureOverrideJson(packRoot));
                writeMcmeta(packRoot);
                activePackFileName = preparePackZip.getFileName().toString();
                writePackZip(packRoot, preparePackZip);
                enforceTopmost();
                packEnabled = enabledContainsUs();
                persistPackToOptions(true);
            } else {
                if (z) {
                    removeUsFromEnabled();
                    persistPackToOptions(false);
                }
                packEnabled = false;
            }
            lastAppliedStamp = contentStamp;
            int size = detectedZips.size();
            Runnable runnable2 = () -> {
                refreshBundledCallsFromClient();
                updateStatus(i2, size);
                if (runnable != null) {
                    runnable.run();
                }
                busy = false;
            };
            if (i2 > 0 ? true : z) {
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 == null) {
                    runnable2.run();
                    return;
                } else {
                    method_1551.method_1513().whenComplete((r2, th) -> {
                        method_1551.execute(runnable2);
                    });
                    return;
                }
            }
            runnable2.run();
        } catch (Exception e) {
            lastStatus = tr("roar_of_love.status.refresh_failed", e.getMessage());
            RoarOfLove.LOGGER.warn("[roar_of_love] 刷新失败", e);
            packEnabled = enabledContainsUs();
            busy = false;
        }
    }

    private static Path preparePackZip() {
        Path packZip = packZip();
        if (tryDelete(packZip)) {
            activePackFileName = packZip.getFileName().toString();
            return packZip;
        }
        int i = 1;
        while (true) {
            int i2 = i;
            if (i2 <= 9) {
                Path resolve = AudioPaths.resourcePacksDir().resolve("RoarOfLove-Audio-" + i2 + ".zip");
                if (!tryDelete(resolve)) {
                    i = i2 + 1;
                } else {
                    RoarOfLove.LOGGER.warn("[roar_of_love] {} 被占用，本次改用 {}", packZip.getFileName(), resolve.getFileName());
                    activePackFileName = resolve.getFileName().toString();
                    return resolve;
                }
            } else {
                activePackFileName = packZip.getFileName().toString();
                return packZip;
            }
        }
    }

    private static boolean tryDelete(Path path) {
        try {
            Files.deleteIfExists(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void safeDeleteTree(Path path) {
        try {
            deleteTree(path);
        } catch (IOException e) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 清理目录失败（可能被占用）：{}", path, e);
        }
    }

    public static String activeZipName() {
        for (String str : detectedZipNames()) {
            if (RoarOfLoveConfig.isZipEnabled(str)) {
                return str;
            }
        }
        return null;
    }

    private static void cleanupStalePacks() {
        try {
            class_3283 manager = manager();
            Collection of = manager == null ? List.of() : manager.method_29210();
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(AudioPaths.resourcePacksDir(), "RoarOfLove-Audio*.zip");
            try {
                for (Path path : newDirectoryStream) {
                    String path2 = path.getFileName().toString();
                    if (!of.contains("file/" + path2) && tryDelete(path)) {
                        RoarOfLove.LOGGER.info("[roar_of_love] 已清理失效的覆盖包 {}", path2);
                    }
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
            } finally {
            }
        } catch (Exception e) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 清理覆盖包失败", e);
        }
    }

    private static void writeMcmeta(Path path) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pack_format", 75);
        jsonObject.addProperty("min_format", 75);
        jsonObject.addProperty("max_format", 75);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("text", tr("roar_of_love.pack.desc", new Object[0]));
        jsonObject.add("description", jsonObject2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.add("pack", jsonObject);
        writeJson(path.resolve("pack.mcmeta"), jsonObject3);
    }

    private static int enabledZipCount() {
        int i = 0;
        Iterator<Path> it = detectedZips().iterator();
        while (true) {
            int i2 = i;
            if (it.hasNext()) {
                i = !RoarOfLoveConfig.isZipDisabled(it.next().getFileName().toString()) ? i2 + 1 : i2;
            } else {
                return i2;
            }
        }
    }

    private static void updateStatusNoReload() {
        updateStatus(enabledZipCount(), detectedZips().size());
    }

    private static void updateStatus(int i, int i2) {
        if (i <= 0) {
            lastStatus = i2 == 0 ? tr("roar_of_love.status.builtin_no_pack", new Object[0]) : tr("roar_of_love.status.builtin_all_off", new Object[0]);
        } else if (packEnabled) {
            lastStatus = tr("roar_of_love.status.active", Integer.valueOf(i), Integer.valueOf(i2));
        } else {
            lastStatus = tr("roar_of_love.status.not_applied", new Object[0]);
        }
    }

    private static class_3283 manager() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 == null) {
            return null;
        }
        return method_1551.method_1520();
    }

    private static boolean isTopmost() {
        class_3283 manager = manager();
        if (manager == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = manager.method_14444().iterator();
        while (it.hasNext()) {
            arrayList.add(((class_3288) it.next()).method_14463());
        }
        return !arrayList.isEmpty() && ((String) arrayList.get(arrayList.size() + (-1))).equals(packId());
    }

    private static void enforceTopmost() {
        class_3283 manager = manager();
        if (manager != null) {
            manager.method_14445();
            ArrayList arrayList = new ArrayList();
            Iterator it = manager.method_14444().iterator();
            while (it.hasNext()) {
                arrayList.add(((class_3288) it.next()).method_14463());
            }
            arrayList.remove(packId());
            arrayList.add(packId());
            manager.method_14447(arrayList);
            RoarOfLove.LOGGER.info("[roar_of_love] 覆盖包状态：profiles={} 有本包={} enabled={} 本包已启用={} 本包路径={}", new Object[]{Integer.valueOf(manager.method_14441().size()), Boolean.valueOf(manager.method_29207(packId())), Integer.valueOf(manager.method_29210().size()), Boolean.valueOf(manager.method_29210().contains(packId())), packZip().getFileName()});
        }
    }

    private static boolean enabledContainsUs() {
        class_3283 manager = manager();
        return manager != null && manager.method_29210().contains(packId());
    }

    private static void removeUsFromEnabled() {
        class_3283 manager = manager();
        if (manager != null) {
            ArrayList arrayList = new ArrayList();
            for (class_3288 class_3288Var : manager.method_14444()) {
                if (!class_3288Var.method_14463().equals(packId())) {
                    arrayList.add(class_3288Var.method_14463());
                }
            }
            manager.method_14447(arrayList);
        }
    }

    private static void enablePack() {
        class_3283 manager = manager();
        if (manager != null) {
            enforceTopmost();
            packEnabled = manager.method_29210().contains(packId());
            if (!packEnabled) {
                lastStatus = tr("roar_of_love.status.warn_pack", new Object[0]);
            }
        }
    }

    private static long contentStamp() throws IOException {
        Path audioFolder = AudioPaths.audioFolder();
        if (!Files.isDirectory(audioFolder, new LinkOption[0])) {
            return -1L;
        }
        long j = 0;
        DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(audioFolder, "*.zip");
        try {
            for (Path path : newDirectoryStream) {
                j = j + Files.size(path) + Files.getLastModifiedTime(path, new LinkOption[0]).toMillis() + path.getFileName().toString().hashCode();
            }
            if (newDirectoryStream != null) {
                newDirectoryStream.close();
            }
            return j;
        } catch (Throwable th) {
            if (newDirectoryStream != null) {
                try {
                    newDirectoryStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void ensureRootFolderAndTemplate() throws IOException {
        Path audioFolder = AudioPaths.audioFolder();
        Files.createDirectories(audioFolder, new FileAttribute[0]);
        if (!folderEnsured) {
            folderEnsured = true;
            writeReadme(audioFolder.resolve("README.txt"), tr("roar_of_love.readme.folder", new Object[0]));
        }
        if (!templateEnsured) {
            Path resolve = audioFolder.resolve("RoarOfLove-Audio-Template.zip");
            if (!Files.exists(resolve, new LinkOption[0])) {
                writeTemplateZip(resolve);
            }
            templateEnsured = true;
        }
    }

    private static void runScanToPack() throws IOException {
        int i;
        Path packRoot = packRoot();
        deleteTree(packRoot);
        Files.createDirectories(packRoot, new FileAttribute[0]);
        List<Path> detectedZips = detectedZips();
        int i2 = 0;
        boolean z = false;
        for (Path path : detectedZips) {
            if (RoarOfLoveConfig.isZipDisabled(path.getFileName().toString())) {
                i = i2;
            } else {
                z = z || extractZip(path, packRoot);
                i = i2 + 1;
            }
            i2 = i;
        }
        writeJson(packRoot.resolve("assets/roar_of_love/sounds.json"), buildRoarSoundsJson());
        writeJson(packRoot.resolve("assets/minecraft/sounds.json"), buildMinecraftOverrideJson(packRoot));
        writeJson(packRoot.resolve("assets/needsofnature/sounds.json"), buildNeedsofnatureOverrideJson(packRoot));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pack_format", 75);
        jsonObject.addProperty("min_format", 75);
        jsonObject.addProperty("max_format", 75);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("text", tr("roar_of_love.pack.desc", new Object[0]));
        jsonObject.add("description", jsonObject2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.add("pack", jsonObject);
        writeJson(packRoot.resolve("pack.mcmeta"), jsonObject3);
        if (detectedZips.isEmpty()) {
            lastStatus = tr("roar_of_love.status.scan_none", new Object[0]);
        } else if (i2 == 0) {
            lastStatus = tr("roar_of_love.status.scan_all_off", new Object[0]);
        } else {
            lastStatus = tr("roar_of_love.status.scan_active", Integer.valueOf(i2), Integer.valueOf(detectedZips.size()));
        }
    }

    private static void writeJson(Path path, JsonObject jsonObject) throws IOException {
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        Files.writeString(path, GSON.toJson(jsonObject), StandardCharsets.UTF_8, new OpenOption[0]);
    }

    private static void ensureBuiltinFiles(Path path) {
        int i = 0;
        for (String str : RoLSounds.CATEGORIES) {
            Iterator it = ((List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of())).iterator();
            while (it.hasNext()) {
                String str2 = "assets/roar_of_love/sounds/actionsounds/" + str + "/" + ((String) it.next()) + ".ogg";
                Path resolve = path.resolve(str2);
                if (!Files.exists(resolve, new LinkOption[0])) {
                    try {
                        byte[] readOwnResource = AudioPaths.readOwnResource(str2);
                        Files.createDirectories(resolve.getParent(), new FileAttribute[0]);
                        Files.write(resolve, readOwnResource, new OpenOption[0]);
                        i++;
                    } catch (IOException e) {
                    }
                }
            }
        }
        for (ZipSpec.VanillaGroup vanillaGroup : ZipSpec.VANILLA_GROUPS) {
            Iterator it2 = vanillaGroup.fileNames().iterator();
            while (it2.hasNext()) {
                String str3 = "assets/roar_of_love/sounds/vanilla/" + vanillaGroup.key() + "/" + ((String) it2.next()) + ".ogg";
                Path resolve2 = path.resolve(str3);
                if (!Files.exists(resolve2, new LinkOption[0])) {
                    try {
                        byte[] readOwnResource2 = AudioPaths.readOwnResource(str3);
                        Files.createDirectories(resolve2.getParent(), new FileAttribute[0]);
                        Files.write(resolve2, readOwnResource2, new OpenOption[0]);
                        i++;
                    } catch (IOException e2) {
                    }
                }
            }
        }
        if (i > 0) {
            RoarOfLove.LOGGER.info("[roar_of_love] 已把 {} 个内置音频复制进动态包", Integer.valueOf(i));
        }
    }

    private static void persistPackToOptions(boolean z) {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1690 != null) {
                List list = method_1551.field_1690.field_1887;
                list.remove(PACK_FOLDER_NAME);
                if (z) {
                    if (!list.contains(packId())) {
                        list.add(packId());
                        method_1551.field_1690.method_1640();
                    }
                } else if (list.remove(packId())) {
                    method_1551.field_1690.method_1640();
                }
            }
        } catch (Exception e) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 写入 options 失败", e);
        }
    }

    private static boolean extractZip(Path path, Path path2) {
        Exception e = null;
        for (String str : new String[]{"UTF-8", "GBK", "windows-1252", "ISO-8859-1"}) {
            try {
                boolean extractZipWithCharset = extractZipWithCharset(path, path2, str);
                if (!"UTF-8".equals(str)) {
                    RoarOfLove.LOGGER.info("[roar_of_love] 压缩包 {} 使用 {} 解码文件名成功", path.getFileName(), str);
                    return extractZipWithCharset;
                }
                return extractZipWithCharset;
            } catch (Exception e2) {
                e = e2;
                RoarOfLove.LOGGER.warn("[roar_of_love] 压缩包 {} 用 {} 读取失败({})，尝试下一种编码", new Object[]{path.getFileName(), str, e.getClass().getSimpleName()});
            }
        }
        RoarOfLove.LOGGER.warn("[roar_of_love] 压缩包 {} 无法读取，已跳过", path.getFileName(), e);
        return false;
    }

    private static boolean extractZipWithCharset(Path path, Path path2, String str) throws IOException {
        ZipFile zipFile;
        boolean z;
        String normalizeTop;
        Path mapEntry;
        boolean z2 = false;
        if ("UTF-8".equals(str)) {
            zipFile = new ZipFile(path.toFile());
        } else {
            zipFile = new ZipFile(path.toFile(), Charset.forName(str));
        }
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            byte[] bArr = new byte[8192];
            while (entries.hasMoreElements()) {
                ZipEntry nextElement = entries.nextElement();
                if (!nextElement.isDirectory()) {
                    String replace = nextElement.getName().replace('\\', '/');
                    if (replace.toLowerCase(Locale.ROOT).endsWith(".ogg")) {
                        String[] split = replace.split("/");
                        if (split.length >= 2 && (normalizeTop = ZipSpec.normalizeTop(split[0])) != null && (mapEntry = mapEntry(normalizeTop, split, path2)) != null) {
                            Files.createDirectories(mapEntry.getParent(), new FileAttribute[0]);
                            InputStream inputStream = zipFile.getInputStream(nextElement);
                            try {
                                OutputStream newOutputStream = Files.newOutputStream(mapEntry, new OpenOption[0]);
                                while (true) {
                                    try {
                                        int read = inputStream.read(bArr);
                                        if (read <= 0) {
                                            break;
                                        }
                                        newOutputStream.write(bArr, 0, read);
                                    } finally {
                                    }
                                }
                                if (newOutputStream != null) {
                                    newOutputStream.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                packProvided.add(path2.relativize(mapEntry).toString().replace('\\', '/'));
                                if (normalizeTop.equals("roar of love")) {
                                    z = true;
                                    z2 = z;
                                }
                            } finally {
                            }
                        }
                    }
                }
                z = z2;
                z2 = z;
            }
            if (zipFile != null) {
                zipFile.close();
            }
            return z2;
        } catch (Throwable th) {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static Path mapEntry(String str, String[] strArr, Path path) {
        ZipSpec.VanillaGroup vanillaGroupByKey;
        char c = 65535;
        switch (str.hashCode()) {
            case 695073197:
                if (str.equals("minecraft")) {
                    c = 0;
                    break;
                }
                break;
            case 715926235:
                if (str.equals("needsofnature")) {
                    c = 2;
                    break;
                }
                break;
            case 781174057:
                if (str.equals("roar of love")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (strArr.length < 3 || (vanillaGroupByKey = ZipSpec.vanillaGroupByKey(ZipSpec.keyFromDisplay(strArr[1]))) == null) {
                    return null;
                }
                return path.resolve("assets/roar_of_love/sounds/vanilla").resolve(vanillaGroupByKey.key()).resolve(strArr[2]);
            case 1:
                if (strArr.length < 3) {
                    return null;
                }
                String keyFromDisplay = ZipSpec.keyFromDisplay(strArr[1]);
                String str2 = strArr[2];
                if (keyFromDisplay.equals("calls")) {
                    if (strArr.length >= 4) {
                        String trim = strArr[2].toLowerCase(Locale.ROOT).trim();
                        String str3 = strArr[3];
                        String substring = str3.substring(0, str3.length() - 4);
                        if (RoLSounds.CALL_GROUPS.contains(trim) && !"default".equals(trim) && CALL_STANDARD_NAMES.contains(substring)) {
                            return path.resolve("assets/roar_of_love/sounds/calls").resolve(trim).resolve(str3);
                        }
                        return null;
                    }
                    if (CALL_STANDARD_NAMES.contains(str2.substring(0, str2.length() - 4))) {
                        return path.resolve("assets/roar_of_love/sounds/calls").resolve(str2);
                    }
                    return null;
                }
                if (RoLSounds.CATEGORIES.contains(keyFromDisplay)) {
                    return path.resolve("assets/roar_of_love/sounds/actionsounds").resolve(keyFromDisplay).resolve(str2);
                }
                return null;
            case 2:
                if (strArr.length < 2) {
                    return null;
                }
                Path resolve = path.resolve("assets/needsofnature");
                for (int i = 1; i < strArr.length; i++) {
                    resolve = resolve.resolve(strArr[i]);
                }
                return resolve;
            default:
                return null;
        }
    }

    private static JsonObject buildRoarSoundsJson() {
        JsonObject jsonObject = new JsonObject();
        for (String str : RoLSounds.CALL_GROUPS) {
            if (!"default".equals(str)) {
                List<String> union = union(bundledCallNames("calls/" + str + "/"), dirNames(packRoot().resolve("assets/roar_of_love/sounds/calls").resolve(str)));
                for (String str2 : union) {
                    if (str2.startsWith("begin")) {
                        jsonObject.add("call." + str + "." + str2, event("calls/" + str, List.of(str2)));
                    }
                }
                if (union.contains("end")) {
                    jsonObject.add("call." + str + ".end", event("calls/" + str, List.of("end")));
                }
            }
        }
        for (String str3 : RoLSounds.CATEGORIES) {
            List<String> filesFor = filesFor("assets/roar_of_love/sounds/actionsounds/" + str3 + "/", union((List) RoLSounds.CATEGORY_FILES.getOrDefault(str3, List.of()), dirNames(packRoot().resolve("assets/roar_of_love/sounds/actionsounds").resolve(str3))));
            if (!filesFor.isEmpty()) {
                jsonObject.add(str3, event("actionsounds/" + str3, filesFor));
                for (String str4 : filesFor) {
                    jsonObject.add(str3 + "." + str4, event("actionsounds/" + str3, List.of(str4)));
                }
            }
        }
        List<String> union2 = union(bundledCallNames("calls/"), dirNames(packRoot().resolve("assets/roar_of_love/sounds/calls")));
        ArrayList arrayList = new ArrayList();
        for (String str5 : union2) {
            if (str5.startsWith("begin")) {
                arrayList.add(str5);
            }
        }
        if (!arrayList.isEmpty()) {
            jsonObject.add("call_begin", event("calls", arrayList));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str6 = (String) it.next();
                jsonObject.add("call." + str6, event("calls", List.of(str6)));
            }
        }
        if (union2.contains("end")) {
            jsonObject.add("call_end", event("calls", List.of("end")));
            jsonObject.add("call.end", event("calls", List.of("end")));
        }
        return jsonObject;
    }

    private static JsonObject event(String str, List<String> list) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (String str2 : list) {
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("name", "roar_of_love:" + str + "/" + str2);
            jsonObject2.addProperty("attenuation_distance", 30);
            jsonArray.add(jsonObject2);
        }
        jsonObject.add("sounds", jsonArray);
        return jsonObject;
    }

    private static JsonObject buildMinecraftOverrideJson(Path path) {
        JsonObject jsonObject = new JsonObject();
        for (ZipSpec.VanillaGroup vanillaGroup : ZipSpec.VANILLA_GROUPS) {
            List<String> filesFor = filesFor("assets/roar_of_love/sounds/vanilla/" + vanillaGroup.key() + "/", union(vanillaGroup.fileNames(), dirNames(path.resolve("assets/roar_of_love/sounds/vanilla").resolve(vanillaGroup.key()))));
            if (!filesFor.isEmpty()) {
                for (String str : vanillaGroup.events()) {
                    JsonObject jsonObject2 = new JsonObject();
                    jsonObject2.addProperty("replace", true);
                    JsonArray jsonArray = new JsonArray();
                    for (String str2 : filesFor) {
                        JsonObject jsonObject3 = new JsonObject();
                        jsonObject3.addProperty("name", "roar_of_love:vanilla/" + vanillaGroup.key() + "/" + str2);
                        jsonObject3.addProperty("attenuation_distance", 16);
                        jsonArray.add(jsonObject3);
                    }
                    jsonObject2.add("sounds", jsonArray);
                    jsonObject.add(str, jsonObject2);
                }
            }
        }
        return jsonObject;
    }

    private static JsonObject buildNeedsofnatureOverrideJson(Path path) {
        JsonObject jsonObject = new JsonObject();
        for (String str : RoLSounds.CATEGORIES) {
            for (String str2 : filesFor("assets/roar_of_love/sounds/actionsounds/" + str + "/", union((List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of()), dirNames(path.resolve("assets/roar_of_love/sounds/actionsounds").resolve(str))))) {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("replace", true);
                JsonArray jsonArray = new JsonArray();
                JsonObject jsonObject3 = new JsonObject();
                jsonObject3.addProperty("name", "roar_of_love:actionsounds/" + str + "/" + str2);
                jsonObject3.addProperty("attenuation_distance", 30);
                jsonArray.add(jsonObject3);
                jsonObject2.add("sounds", jsonArray);
                jsonObject.add(str2, jsonObject2);
            }
        }
        return jsonObject;
    }

    private static List<String> union(List<String> list, List<String> list2) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(list);
        linkedHashSet.addAll(list2);
        return new ArrayList(linkedHashSet);
    }

    private static List<String> bundledCallNames(String str) {
        ArrayList arrayList = new ArrayList();
        for (String str2 : CALL_STANDARD_NAMES) {
            if (AudioPackLoader.class.getResource("/assets/roar_of_love/sounds/" + str + str2 + ".ogg") != null) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    private static List<String> dirNames(Path path) {
        ArrayList arrayList = new ArrayList();
        if (!Files.isDirectory(path, new LinkOption[0])) {
            return arrayList;
        }
        try {
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path, "*.ogg");
            try {
                Iterator<Path> it = newDirectoryStream.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().getFileName().toString().substring(0, r0.length() - 4));
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
            } finally {
            }
        } catch (IOException e) {
        }
        return arrayList;
    }

    public static List<Path> detectedZips() {
        Path audioFolder = AudioPaths.audioFolder();
        ArrayList arrayList = new ArrayList();
        if (Files.isDirectory(audioFolder, new LinkOption[0])) {
            try {
                DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(audioFolder, "*.zip");
                try {
                    for (Path path : newDirectoryStream) {
                        if (!path.getFileName().toString().equalsIgnoreCase("RoarOfLove-Audio-Template.zip")) {
                            arrayList.add(path);
                        }
                    }
                    if (newDirectoryStream != null) {
                        newDirectoryStream.close();
                    }
                } finally {
                }
            } catch (IOException e) {
            }
        }
        arrayList.sort((path2, path3) -> {
            return path2.getFileName().toString().compareToIgnoreCase(path3.getFileName().toString());
        });
        return arrayList;
    }

    public static List<String> detectedZipNames() {
        ArrayList arrayList = new ArrayList();
        Iterator<Path> it = detectedZips().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getFileName().toString());
        }
        return arrayList;
    }

    public static void setZipEnabled(String str, boolean z) {
        migrateZipAllowlist();
        RoarOfLoveConfig.setZipEnabled(str, z);
    }

    public static boolean isZipEnabled(String str) {
        return RoarOfLoveConfig.isZipEnabled(str);
    }

    private static void refreshBundledCallsFromClient() {
        new LinkedHashSet();
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

    public static void openAudioFolder() {
        Process start;
        try {
            Path audioFolder = AudioPaths.audioFolder();
            Files.createDirectories(audioFolder, new FileAttribute[0]);
            String path = audioFolder.toAbsolutePath().toString();
            String lowerCase = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
            if (lowerCase.contains("win")) {
                start = new ProcessBuilder("explorer.exe", path).start();
            } else if (lowerCase.contains("mac")) {
                start = new ProcessBuilder("open", path).start();
            } else {
                start = new ProcessBuilder("xdg-open", path).start();
            }
            sendChat(tr("roar_of_love.chat.opened", path));
            RoarOfLove.LOGGER.info("[roar_of_love] 已请求打开文件夹 {} (pid={})", path, Long.valueOf(start.pid()));
        } catch (Exception e) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 打开文件夹失败", e);
            try {
                Path audioFolder2 = AudioPaths.audioFolder();
                Files.createDirectories(audioFolder2, new FileAttribute[0]);
                Desktop.getDesktop().open(audioFolder2.toFile());
                sendChat(tr("roar_of_love.chat.opened_fallback", audioFolder2.toAbsolutePath()));
            } catch (Exception e2) {
                sendChat(tr("roar_of_love.chat.open_failed", e2.getMessage()));
            }
        }
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

    private static void writeTemplateZip(Path path) throws IOException {
        OutputStream newOutputStream = Files.newOutputStream(path, new OpenOption[0]);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(newOutputStream);
            try {
                addZipText(zipOutputStream, "README.txt", tr("roar_of_love.readme.template", new Object[0]));
                for (ZipSpec.VanillaGroup vanillaGroup : ZipSpec.VANILLA_GROUPS) {
                    for (String str : vanillaGroup.fileNames()) {
                        addZipBytes(zipOutputStream, "minecraft/" + vanillaGroup.display() + "/" + str + ".ogg", AudioPaths.readOwnResource("assets/roar_of_love/sounds/vanilla/" + vanillaGroup.key() + "/" + str + ".ogg"));
                    }
                }
                for (String str2 : RoLSounds.CATEGORIES) {
                    for (String str3 : (List) RoLSounds.CATEGORY_FILES.getOrDefault(str2, List.of())) {
                        addZipBytes(zipOutputStream, "roar of love/" + ((String) ZipSpec.CATEGORY_DISPLAY.get(str2)) + "/" + str3 + ".ogg", AudioPaths.readOwnResource("assets/roar_of_love/sounds/actionsounds/" + str2 + "/" + str3 + ".ogg"));
                    }
                }
                addZipText(zipOutputStream, "roar of love/calls(叫声)/README.txt", tr("roar_of_love.readme.calls", new Object[0]));
                zipOutputStream.close();
                if (newOutputStream != null) {
                    newOutputStream.close();
                }
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

    private static void addZipText(ZipOutputStream zipOutputStream, String str, String str2) throws IOException {
        addZipBytes(zipOutputStream, str, str2.getBytes(StandardCharsets.UTF_8));
    }

    private static void addZipBytes(ZipOutputStream zipOutputStream, String str, byte[] bArr) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(str));
        zipOutputStream.write(bArr);
        zipOutputStream.closeEntry();
    }

    private static void writeReadme(Path path, String str) throws IOException {
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        Files.writeString(path, str, StandardCharsets.UTF_8, new OpenOption[0]);
    }

    public static Path packRoot() {
        return AudioPaths.packCacheDir();
    }

    private static Path packZip() {
        return AudioPaths.packZip();
    }

    private static Path legacyPackFolder() {
        return AudioPaths.legacyPackFolder();
    }

    private static void deleteFile(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    private static void writePackZip(Path path, Path path2) throws IOException {
        Files.createDirectories(path2.getParent(), new FileAttribute[0]);
        OutputStream newOutputStream = Files.newOutputStream(path2, new OpenOption[0]);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(newOutputStream);
            try {
                ArrayList arrayList = new ArrayList();
                collectFiles(path, arrayList);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Path path3 = (Path) it.next();
                    zipOutputStream.putNextEntry(new ZipEntry(path.relativize(path3).toString().replace('\\', '/')));
                    Files.copy(path3, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.close();
                if (newOutputStream != null) {
                    newOutputStream.close();
                }
                RoarOfLove.LOGGER.info("[roar_of_love] 已生成覆盖包 {}", path2);
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

    private static void collectFiles(Path path, List<Path> list) throws IOException {
        if (Files.isDirectory(path, new LinkOption[0])) {
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path);
            try {
                for (Path path2 : newDirectoryStream) {
                    if (Files.isDirectory(path2, new LinkOption[0])) {
                        collectFiles(path2, list);
                    } else {
                        list.add(path2);
                    }
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
            } catch (Throwable th) {
                if (newDirectoryStream != null) {
                    try {
                        newDirectoryStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    private static void deleteTree(Path path) throws IOException {
        if (Files.exists(path, new LinkOption[0])) {
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path);
            try {
                for (Path path2 : newDirectoryStream) {
                    if (Files.isDirectory(path2, new LinkOption[0])) {
                        deleteTree(path2);
                    } else {
                        Files.deleteIfExists(path2);
                    }
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
                Files.deleteIfExists(path);
            } catch (Throwable th) {
                if (newDirectoryStream != null) {
                    try {
                        newDirectoryStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }
}
