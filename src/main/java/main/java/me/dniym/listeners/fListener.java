package main.java.me.dniym.listeners;

import io.netty.util.internal.ThreadLocalRandom;
import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.actions.IllegalStackAction;
import main.java.me.dniym.checks.BadAttributeCheck;
import main.java.me.dniym.checks.CheckUtils;
import main.java.me.dniym.checks.IllegalEnchantCheck;
import main.java.me.dniym.checks.IrritatingLegacyChecks;
import main.java.me.dniym.checks.OverstackedItemCheck;
import main.java.me.dniym.checks.RemoveItemTypesCheck;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.fishing.FishAttempt;
import main.java.me.dniym.logging.Log;
import main.java.me.dniym.timers.fTimer;
import main.java.me.dniym.timers.sTimer;
import main.java.me.dniym.utils.NBTStuff;
import main.java.me.dniym.utils.Scheduler;
import main.java.me.dniym.utils.SlimefunCompat;
import main.java.me.dniym.utils.SpigotMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
//import org.spigotmc.event.entity.EntityMountEvent;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class fListener implements Listener {

    private static final HashSet<UUID> chestOffense = new HashSet<>();
    private static final Set<Material> airBlocks = new HashSet<>();
    private static final HashSet<Material> passThrough = new HashSet<>();
    private static final BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + fListener.class.getSimpleName());
    private static Log log = null;
    private static fListener instance;
    private static Boolean is114 = false;
    private static Material portal = null;
    private static Material endPortal = null;
    private static Boolean is18 = false;
    private static int hasPassengers = -1;
    private final HashMap<Player, Long> spamCheck = new HashMap<>();
    private final Set<Material> blacklist = new HashSet<>();
    private final Set<Material> pistonCheck = new HashSet<>();
    private final Material book;
    private final HashMap<Player, Long> swapDelay = new HashMap<>();
    private final Set<Material> glassBlocks = new HashSet<>();
    private final HashSet<Player> itemWatcher = new HashSet<>();
    private final static HashSet<Material> unbreakable = new HashSet<>();
    IllegalStack plugin;
    HashMap<Block, Long> movedTNT = new HashMap<>();
    private boolean is118 = false;
    private boolean is117 = false;
    private Boolean is116 = false;
    private Boolean is115 = false;
    private Boolean is1152 = false;
    private Boolean is1142 = false;
    private Boolean is1143 = false;
    private Boolean is1144 = false;
    private Boolean is113 = false;
    private Boolean is19 = false;
    private Boolean is112 = false;
    private Boolean is110 = false;
    private HashMap<UUID, Location> teleGlitch = new HashMap<>();
    
    public fListener(IllegalStack plugin) {
        this.plugin = plugin;
        fListener.setInstance(this);
        this.setLog(new Log(plugin));
        String ver = IllegalStack.getVersion().toLowerCase();

        if (ver.equalsIgnoreCase("v1_13_R2")) {
            LOGGER.warn(
                    "使用 1.13.2 与 IllegalStack 会导致放置的潜影盒出现问题... 放置/发射的方块将不会检查超堆叠物品或非法附魔！");
            IllegalStack.setDisablePaperShulkerCheck(true);
        }

        if (ver.equalsIgnoreCase("v1_13_R1")) {
            LOGGER.warn(
                    "您正在运行一个非常不稳定的服务器版本，如果您想运行 1.13，请使用 1.13.2，否则无法保证防护效果！");
        }

        if (ver.equalsIgnoreCase("v1_13_R2")) {
            setIs113(true);
        }

        if (ver.equalsIgnoreCase("v1_12_R2") || ver.equalsIgnoreCase("v1_12_R1")) {
            setIs112(true);
        }

        if (ver.startsWith("v1_18")) {
            setIs118(true);
        }

        if (ver.startsWith("v1_17")) {
            setIs117(true);
        }

        if (ver.startsWith("v1_16")) {
            setIs116(true);
        }

        if (ver.startsWith("v1_15")) {
            is115(true);
        }

        if (ver.startsWith("v1_15_r1")) {
            is1152 = true;
        }

        if (ver.startsWith("v1_14")) {
            setIs114(true);
        }

        if (ver.equalsIgnoreCase("v1_14_r4")) {
            setIs114(true);
            setIs1144(true);
        }
        if (ver.equalsIgnoreCase("v1_14_R2")) {
            setIs114(true);
            setIs1142(true);
        }
        if (ver.equalsIgnoreCase("v1_14_R3")) {
            setIs114(true);
            setIs1143(true);
        }

        if (ver.contains("v1_8")) {
            setIs18(true);
        }

        if (ver.contains("v1_9")) {
            setIs19(true);
        }
        if (ver.contains("v1_10")) {
            setIs110(true);
        }

        blacklist.add(Material.POWERED_RAIL);
        blacklist.add(Material.ACTIVATOR_RAIL);
        blacklist.add(Material.DETECTOR_RAIL);

        for (Material m : Material.values()) {
            if (m.name().toLowerCase().contains("sign")) {
                getPassThrough().add(m);
            }

            if (!m.isSolid()) {
                if (m.name().toLowerCase().contains("glass") || m.name().toLowerCase().contains("door") || m
                        .name()
                        .toLowerCase()
                        .contains("fence") || m.name().toLowerCase().contains("portal")) {
                } else {
                    getPassThrough().add(m);
                }
            }
        }
        getPassThrough().addAll(fListener.getAirBlocks());
        getAirBlocks().add(Material.AIR);
        for (Material m : Material.values()) {
            if (m.name().contains("GLASS")) {
                getGlassBlocks().add(m);
            }
        }
        if (Material.matchMaterial("CAVE_AIR") != null) {
            getAirBlocks().add(Material.CAVE_AIR);
            getAirBlocks().add(Material.VOID_AIR);

        }

        endPortal = Material.matchMaterial("END_PORTAL");
        if (endPortal == null) {
            endPortal = Material.matchMaterial("ENDER_PORTAL");
        }

        setPortal(Material.matchMaterial("PORTAL"));
        if (getPortal() == null) {
            setPortal(Material.matchMaterial("NETHER_PORTAL"));
        }

        getUnbreakable().add(endPortal);
        //unbreakable.add(portal);
        getUnbreakable().add(Material.BEDROCK);

        String[] mats = new String[]{"ENDER_PORTAL_FRAME", "END_PORTAL_FRAME", "COMMAND", "COMMAND_BLOCK", "COMMAND_CHAIN", "CHAIN_COMMAND_BLOCK",
                "COMMAND_REPEATING", "REPEATING_COMMAND_BLOCK", "STRUCTURE_BLOCK", "BARRIER"};
        for (final String mat : mats) {
            Material testMaterial = Material.matchMaterial(mat);
            if (testMaterial != null) {
                getUnbreakable().add(testMaterial);
            }
        }

        if (!ver.contains("v1_14") && !ver.contains("v1_15") && !ver.contains("v1_16") && !ver.contains("v1_17") && !ver.contains("v1_18") && !ver.contains("V1_19") && !ver.contains("v1_20") && !ver.contains("V1_21")) {
            if (ver.contains("v1_13")) {
                LOGGER.info("检测到 MC 版本 1.13+！");

                blacklist.addAll(Tag.CARPETS.getValues());
                blacklist.add(Material.matchMaterial("RAIL"));

                pistonCheck.add(Material.matchMaterial("PISTON"));
                pistonCheck.add(Material.matchMaterial("PISTON_HEAD"));
                pistonCheck.add(Material.matchMaterial("MOVING_PISTON"));
                book = Material.WRITABLE_BOOK;
            } else {
                LOGGER.info("检测到 MC 版本 < 1.13！");
                blacklist.add(Material.matchMaterial("POWERED_RAIL"));
                blacklist.add(Material.matchMaterial("DETECTOR_RAIL"));
                blacklist.add(Material.matchMaterial("ACTIVATOR_RAIL"));

                blacklist.add(Material.matchMaterial("RAILS"));
                blacklist.add(Material.matchMaterial("CARPET"));
                book = Material.matchMaterial("BOOK_AND_QUILL");


                pistonCheck.add(Material.matchMaterial("PISTON_MOVING_PIECE"));
                Set<Material> removeCheck = new HashSet<>();
                removeCheck.add(Material.matchMaterial("PISTON_BASE"));
                removeCheck.add(Material.matchMaterial("PISTON_STICKY_BASE"));
            }
            blacklist.add(Material.DETECTOR_RAIL);
        } else {
            LOGGER.info("检测到 MC 版本 {}！", ver);
            pistonCheck.add(Material.PISTON);
            pistonCheck.add(Material.MOVING_PISTON);
            blacklist.add(Material.RAIL);
            blacklist.addAll(Tag.CARPETS.getValues());
            blacklist.add(Material.ACTIVATOR_RAIL);
            blacklist.add(Material.DETECTOR_RAIL);

            book = Material.WRITABLE_BOOK;
        }
    }

    public static void punishPlayer(Player player, Entity rightClicked) {

        if (!Protections.PunishForChestsOnMobs.isEnabled()) {
            return;
        }
        if (chestOffense.contains(player.getUniqueId()) && IllegalStackAction.isCompleted(
                Protections.PunishForChestsOnMobs,
                rightClicked,
                player
        )) {
            fListener.getLog().append(Msg.StaffChestPunishment.getValue(player, rightClicked), Protections.PunishForChestsOnMobs);
            rightClicked.eject();
            rightClicked.remove();
            player.getInventory().clear();
            player.kickPlayer(Msg.PlayerKickForChestMsg.getValue());
        } else {
            chestOffense.add(player.getUniqueId());
        }
    }

    public static Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        fListener.log = log;
    }

    public static fListener getInstance() {
        return instance;
    }

    public static void setInstance(fListener instance) {
        fListener.instance = instance;
    }

    public static Set<Material> getAirBlocks() {
        return airBlocks;
    }

    public static BlockFace[] getFaces() {
        return faces;
    }

    public static Material getPortal() {
        return portal;
    }

    public void setPortal(Material portal) {
        fListener.portal = portal;
    }

    public static Boolean getIs114() {
        return is114;
    }

    public void setIs114(Boolean is114) {
        fListener.is114 = is114;

    }

    public static HashSet<Material> getPassThrough() {
        return passThrough;
    }

    /*
     * 检查丢弃的物品是否为黑名单类型
     */

    public static Boolean is18() {
        return is18;
    }

    @EventHandler
    public void OnItemDrop(PlayerDropItemEvent e) {
        if (Protections.RemoveItemTypes.isEnabled() && RemoveItemTypesCheck.shouldRemove(e
                .getItemDrop()
                .getItemStack()
                .getType())) {
            e.setCancelled(true);
            Scheduler.runTaskLater(this.plugin, () -> {
                fListener.getLog().append(Msg.ItemTypeRemovedPlayerOnDrop.getValue(
                        e.getPlayer(),
                        e.getItemDrop().getItemStack().getType().name()
                ), Protections.RemoveItemTypes);
                e.getPlayer().getInventory().remove(e.getItemDrop().getItemStack());
            }, 2, e.getPlayer());

        }

    }

    /*
     * 放置潜影盒时检查其中是否有非法物品
     */
    @EventHandler
    public void ShulkerPlaceCheck(BlockPlaceEvent e) {

        if (IllegalStack.isDisablePaperShulkerCheck() || Protections.IgnoreAllShulkerPlaceChecks.isEnabled()) {
            return;
        }

        boolean cancel = false;

        if (Protections.RemoveItemTypes.isEnabled() && RemoveItemTypesCheck.shouldRemove(e.getBlockPlaced().getType())) {
        	
            Scheduler.runTaskLater(this.plugin, () -> {
                fListener.getLog().append(Msg.ItemTypeRemovedPlayerOnPlace.getValue(
                        e.getPlayer(),
                        e.getBlockPlaced().getType().name()
                ), Protections.RemoveItemTypes);
                e.getBlockPlaced().setType(Material.AIR);
            }, 2, e.getPlayer());

        }

        if (IllegalStack.hasShulkers()) {

            if (!IllegalStack.isBlockMetaData()) {  //旧版本
                if (e.getItemInHand().getItemMeta() instanceof BlockStateMeta) {
                    final BlockStateMeta sbm = (BlockStateMeta) e.getItemInHand().getItemMeta();
                    if (sbm.getBlockState() instanceof ShulkerBox) {
                        final ShulkerBox shulker = (ShulkerBox) sbm.getBlockState();
                        for (ItemStack is : shulker.getInventory().getContents()) {
                            if (is == null) {
                                continue;
                            }

                            boolean overstacked = false;
                            boolean illegalEnchanted = false;
                            Protections prot = null;
                            if (Protections.RemoveOverstackedItems.isEnabled()) {
                                overstacked = IrritatingLegacyChecks.CheckContainer(is, e.getPlayer().getLocation());
                                prot = Protections.RemoveOverstackedItems;
                            }
                            if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
                                illegalEnchanted = IrritatingLegacyChecks.isIllegallyEnchanted(is);
                                prot = Protections.FixIllegalEnchantmentLevels;
                            }

                            if (overstacked || illegalEnchanted || RemoveItemTypesCheck.shouldRemove(is, null)) {

                                if (!overstacked && !illegalEnchanted) {
                                    prot = Protections.RemoveItemTypes;
                                }

                                Protections p = prot;

                                if (IllegalStackAction.isCompleted(prot, e.getPlayer(), e.getBlock(), e.getItemInHand())) {
                                    cancel = true;

                                    Scheduler.runTaskLater(this.plugin, () -> {
                                        e.getPlayer().getInventory().removeItem(e.getItemInHand());
                                        fListener.getLog().append(Msg.ShulkerPlace.getValue(
                                                e.getPlayer(),
                                                e.getBlockPlaced().getLocation()
                                        ), p);
                                    }, 2, e.getPlayer());
                                }
                                break;
                            }
                        }
                    }
                }
            } else { //现代版本

                if (e.getBlockPlaced().getState() instanceof ShulkerBox) {
                    ShulkerBox c = (ShulkerBox) e.getBlock().getState();
                    for (ItemStack is : c.getInventory()) {
                        if (is == null) {
                            continue;
                        }

                        if (Protections.RemoveOverstackedItems.isEnabled())//我认为所有检查可能都需要移到各自的类中
                        {
                            OverstackedItemCheck.CheckContainer(is, c);
                        }

                        if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
                            IllegalEnchantCheck.isIllegallyEnchanted(is, c);
                        }

                        if (!Protections.RemoveItemTypes.getTxtSet().isEmpty()) {
                            RemoveItemTypesCheck.CheckForIllegalTypes(is, c);
                        }
                    }
                }

            }


        }
        if (cancel) {
            e.setCancelled(true);
        }
    }

    /*
     * 防止利用 GUI 或依赖玩家睡觉时事件不触发的战斗漏洞。
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/") && e
                .getPlayer()
                .isSleeping() && Protections.PreventCommandsInBed.isEnabled() && IllegalStackAction.isCompleted(
                Protections.PreventCommandsInBed,
                e.getPlayer()
        )) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Msg.PlayerCommandSleepMsg.getValue());
        }
    }

    /*
     * 玩家离开时进行清理
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        swapDelay.remove(e.getPlayer());
        spamCheck.remove(e.getPlayer());
        teleGlitch.remove(e.getPlayer().getUniqueId());
    }

    /*
     * 检查不良告示牌、绊线复制、玩家在下界顶部等
     */
    @EventHandler (ignoreCancelled = true)
    public void onSignPlace(BlockPlaceEvent e) {  //仅影响 1.9 到 1.12 版本
        if (Protections.PreventTripwireDupe.isEnabled(e.getBlock().getWorld())) {
            if (e.getBlock().getType() == Material.TRIPWIRE_HOOK) {
                for (BlockFace face : BlockFace.values()) {
                	
                    if ((Tag.TRAPDOORS.getValues().contains(e.getBlock().getRelative(face).getType()) || Tag.DOORS.getValues().contains(e.getBlock().getRelative(face).getType()))
                            && IllegalStackAction.isCompleted(
                            Protections.PreventTripwireDupe,
                            e.getPlayer(),
                            e.getBlockPlaced(),
                            e.getItemInHand()
                    )) {
                        e.setCancelled(true);
                        e.getPlayer().getInventory().removeItem(e.getItemInHand());
                        getLog().append(
                                Msg.BlockedTripwireDupe.getValue(e.getPlayer(), e.getPlayer().getLocation().toString()),
                                Protections.PreventTripwireDupe
                        );
                    }
                }
            }
        }


        if ((Protections.BlockBuildingAboveNether.isEnabled() || Protections.BlockPlayersAboveNether.isEnabled()) && !e
                .getPlayer()
                .isOp()) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getPlayer().getWorld().getName())) {
                return;
            }
            Location l = e.getBlock().getLocation();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                    if (l.getBlockY() >= Protections.NetherYLevel.getIntValue() && (l.getWorld().getName().toLowerCase().contains(
                            "nether") || l.getWorld().getEnvironment() == Environment.NETHER)) //已经在下界顶部
                    {
                        e.setCancelled(IllegalStackAction.isCompleted(
                                Protections.BlockPlayersAboveNether,
                                e.getPlayer(),
                                e.getBlockPlaced(),
                                e.getItemInHand()
                        ));
                    }
                }
            }
        }

        if (Protections.DisableInWorlds.isWhitelisted(e.getBlock().getWorld().getName())) {
            return;
        }
        boolean isSign = e.getBlockPlaced().getState() instanceof Sign;

        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && isSign
                && !Protections.BookAuthorWhitelist.isWhitelisted(e.getPlayer().getName())) {

            sTimer.checkSign(e.getBlock(), e.getPlayer());
        }
    }
    /*
     * 用水桶等破坏末地传送门
     */
    @EventHandler
    public void onBucketPour(PlayerBucketEmptyEvent e) {
        if (Protections.PreventEndPortalDestruction.isEnabled()) {
            boolean above112 = Material.matchMaterial("END_PORTAL") != null;

            for (BlockFace face : BlockFace.values()) {
                Block adj = e.getBlockClicked().getRelative(face);
                if ((above112 && adj.getType() == Material.END_PORTAL) || !above112 && adj.getType() == endPortal) {
                    e.setCancelled(true);
                    getLog().append(
                            Msg.StaffEndPortalProtected.getValue(e.getBlockClicked().getLocation().toString()),
                            Protections.PreventEndPortalDestruction
                    );
                }

            }
        }

    }

	/*
	private String getSource(Inventory source) {
		String src = "未知位置 : 库存类型为 " + source.getType().name();
		if(source instanceof Hopper) {
			Hopper h = (Hopper) source;
			src = getLog().cleanMessage("在 " + source.getType().name() + " 中 @" +h.getLocation());
		} else if (source instanceof ) {

		}

		return src;
	}
	 */

    @EventHandler
    public void onDispenserDispense(BlockDispenseEvent e) {

        if (IllegalStack.isDisablePaperShulkerCheck()) {
            return;
        }

        if (Protections.PreventShulkerCrash.isEnabled()) {
            if ((e.getBlock().getLocation().getY() >= 255 || e.getBlock().getLocation().getY() <= 0) && e
                    .getItem()
                    .getType()
                    .name()
                    .endsWith("SHULKER_BOX")) {
                e.setCancelled(true);
            }
        }

        if (Protections.PreventShulkerCrash2.isEnabled() && e.getItem().getType() == Material.FLINT_AND_STEEL) {
            if (e.getBlock().getState().getBlockData() instanceof Directional) {
                Directional d = (Directional) e.getBlock().getState().getBlockData();
                if (d.getFacing() == BlockFace.DOWN) {
                    e.setCancelled(true);
                    getLog().append(
                            Msg.StaffMsgDispenerFlint.getValue(e.getItem().getType().name(), e.getBlock().getLocation()),
                            Protections.PreventShulkerCrash2
                    );
                    Scheduler.runTaskLater(this.plugin, () -> e.getBlock().breakNaturally(), 4, e.getBlock().getLocation());
                }
            }
        }
        if (!Protections.RemoveItemTypes.getTxtSet().isEmpty()) {
            if (RemoveItemTypesCheck.shouldRemove(e.getItem(), e.getBlock().getState()) && IllegalStackAction.isCompleted(
                    Protections.RemoveItemTypes,
                    e.getItem(),
                    e.getBlock()
            )) {

                if (IllegalStack.hasContainers()) {

                    Container c = (Container) e.getBlock().getState();
                    for (ItemStack itemStack : c.getInventory()) {
                        if (itemStack != null && itemStack.getType() == e.getItem().getType()) {

                            Scheduler.runTaskLater(this.plugin, () -> e.getBlock().breakNaturally(), 4, e.getBlock().getLocation());


                        }
                    }
                    e.setCancelled(true);
                    return;

                }

            }

        }
        if (Protections.RemoveOverstackedItems.isEnabled() && !is18()) {

            if (IllegalStack.hasContainers()) {

                if (CheckUtils.CheckEntireContainer((Container) e.getBlock().getState())) {

                    e.getItem().setType(Material.AIR);
                    e.setCancelled(true);
                    return;
                }

            } else {
                if (IrritatingLegacyChecks.CheckContainer(e.getBlock())) {
                    fListener.getLog().append2(Msg.ShulkerPlace.getValue(
                            e.getItem().getType().name(),
                            e.getBlock().getLocation()
                    ));
                    e.setCancelled(true);
                }

            }


        }
        if (Protections.PreventEndPortalDestruction.isEnabled()) {
            if (Material.matchMaterial("END_PORTAL") != null) {
                if (e.getBlock().getState().getBlockData() instanceof Directional) {
                    Directional d = (Directional) e.getBlock().getState().getBlockData();
                    if (e.getBlock().getRelative(d.getFacing()).getType() == Material.END_PORTAL) {
                        e.setCancelled(true);
                        getLog().append2(Msg.StaffEndPortalProtected.getValue(e.getBlock().getLocation().toString()));
                    }
                }
            } else {  //1.12及以下没有方向数据，需要用另一种方式
                for (BlockFace face : BlockFace.values()) {
                    if (e.getBlock().getRelative(face).getType() == endPortal) {
                        e.setCancelled(true);
                        getLog().append2(Msg.StaffEndPortalProtected.getValue(e.getBlock().getLocation().toString()));

                    }
                }
            }
        }
    }

    @EventHandler
    public void onHopperXfer(InventoryMoveItemEvent e) { //可能影响所有版本

        if (Protections.IgnoreAllHopperChecks.isEnabled() || e.getItem() == null || e.getItem().getType() == Material.AIR) {
            return;
        }

        if (CheckUtils.CheckEntireInventory(e.getSource())) {
            e.setCancelled(true);
            Scheduler.runTaskLater(this.plugin, () -> {
                if (e.getSource().getHolder() instanceof DoubleChest) {
                    e.getSource().getHolder().getInventory().remove(e.getItem());

                } else if (e.getSource().getHolder().getInventory() instanceof BlockState) {
                    BlockState bs = (BlockState) e.getSource().getHolder();
                    bs.getBlock().breakNaturally();
                }
            }, 2, e.getSource().getLocation());

            return;
        }

        if (!Protections.DisableInWorlds.getTxtSet().isEmpty()) {
            HopperMinecart hm = null;
            HopperMinecart hm2 = null;
            Hopper h = null;
            Hopper h2 = null;
            if (e.getSource().getHolder() instanceof HopperMinecart) {
                hm = (HopperMinecart) e.getSource().getHolder();
            } else if (e.getSource().getHolder() instanceof Hopper) {
                h = (Hopper) e.getSource().getHolder();
            }

            if (e.getDestination().getHolder() instanceof HopperMinecart) {
                hm2 = (HopperMinecart) e.getDestination().getHolder();
            } else if (e.getDestination().getHolder() instanceof Hopper) {
                h2 = (Hopper) e.getDestination().getHolder();
            }
            World wld = null;
            if (hm != null) {
                wld = hm.getWorld();
            } else if (hm2 != null) {
                wld = hm2.getWorld();
            } else if (h != null) {
                wld = h.getWorld();
            } else if (h2 != null) {
                wld = h2.getWorld();
            }

            if (wld != null && Protections.DisableInWorlds.isWhitelisted(wld.getName())) {
                return;
            }
        }

        if (Protections.BlockLoopedDroppers.isEnabled() && e.getSource().getHolder() instanceof Hopper && e
                .getItem()
                .getType()
                .getMaxStackSize() <= 1) {
            if (!NBTStuff.hasSpigotNBT() && !IllegalStack.isNbtAPI()) {
                Protections.BlockLoopedDroppers.setEnabled(false);
                LOGGER.error(
                        "保护 BlockLoopedDroppers 已启用，但服务器上未检测到 NBT API 2.0+，此保护需要 NBTApi (https://www.spigotmc.org/resources/nbt-api.7939/) 在运行 spigot < 1.13 时才能工作，因此已自动禁用。");
                return;
            }

            Hopper h = (Hopper) e.getSource().getHolder();
            Dropper drop = null;
            Dispenser disp = null;
            boolean shouldCancel = false;
            if (e.getDestination().getHolder() instanceof Dropper) {
                drop = (Dropper) e.getDestination().getHolder();
            } else if (e.getDestination().getHolder() instanceof Dispenser) {
                disp = (Dispenser) e.getDestination().getHolder();
            }

            if (drop != null || disp != null) {
                ItemStack updatedStack = NBTStuff.updateTimeStamp(e.getItem(), Protections.BlockLoopedDroppers);
                if (updatedStack != null) {
                    e.setItem(updatedStack);
                    return;
                }

                //如果堆叠为空，则已有时间戳，进行检查
                updatedStack = NBTStuff.checkTimestamp(e.getItem(), Protections.BlockLoopedDroppers);
                if (updatedStack == null) //如果仍为空，则物品有时间戳且正在循环，触发检测
                {
                    shouldCancel = true;
                } else {
                    e.setItem(updatedStack); //否则时间戳已过期，刷新并什么也不做
                }
            }

            if (shouldCancel) {
                if (disp != null) {
                    disp.getBlock().setType(Material.AIR);
                }
                if (drop != null) {
                    drop.getBlock().setType(Material.AIR);
                }
                e.setItem(new ItemStack(Material.AIR, 1));
                fListener.getLog().append2(Msg.StaffMsgDropperExploit.getValue(h.getLocation().toString()));
                e.setCancelled(true);
            }

        }

        if (Protections.PreventHoppersToUnloadedChunks.isEnabled()) {
            if (e.getSource().getType() == InventoryType.HOPPER && e.getDestination().getType() == InventoryType.HOPPER) {
                HopperMinecart hm = null;
                HopperMinecart hm2 = null;
                Hopper h = null;
                Hopper h2 = null;
                if (e.getSource().getHolder() instanceof HopperMinecart) {
                    hm = (HopperMinecart) e.getSource().getHolder();
                } else {
                    h = (Hopper) e.getSource().getHolder();
                }
                if (e.getDestination().getHolder() instanceof HopperMinecart) {
                    hm2 = (HopperMinecart) e.getDestination().getHolder();
                } else {
                    h2 = (Hopper) e.getDestination().getHolder();
                }

                Chunk c1;
                Chunk c2;
                if (hm != null) {
                    c1 = hm.getLocation().getChunk();
                } else {
                    c1 = h.getChunk();
                }

                if (hm2 != null) {
                    c2 = hm2.getLocation().getChunk();
                } else {
                    c2 = h2.getChunk();
                }
                if ((c1 != c2) && (!c1.isLoaded() || !c2.isLoaded())) {
                    e.setCancelled(true);
                }
            }
        }

        if (Protections.RemoveAllRenamedItems.isEnabled() && e.getDestination().getType() == InventoryType.PLAYER) {
            Player p = (Player) e.getDestination().getHolder();
            if (!p.hasPermission("IllegalStack.RenameBypass")) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
                    getLog().append2(Msg.RemovedRenamedItem.getValue(p, e.getItem()));
                    p.getInventory().removeItem(e.getItem());
                }
            }
        }

        if (Protections.RemoveItemsMatchingName.isEnabled()) {
            if (e.getDestination().getType() == InventoryType.PLAYER) {
                if (SpigotMethods.isNPC(((LivingEntity) e.getDestination().getHolder()))) {
                    return;
                }
                Player p = (Player) e.getDestination().getHolder();

                ItemStack is = e.getItem();
                if (Protections.BlockEnchantingInstead.isEnabled() || Protections.BlockRepairsInstead.isEnabled()) {
                    return;
                }
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {

                        if (Protections.ItemNamesToRemove.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemsMatchingName,
                                    " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                            );
                        } else {
                            getLog().append2(Msg.NamedItemRemovalPlayer.getValue(p, is.getType().name()));
                            e.setCancelled(true);
                            p.getInventory().removeItem(is);
                        }
                    }
                }
            }
        }

        ItemStack is = e.getItem();
        if (is.hasItemMeta() && is.getItemMeta() instanceof BookMeta) {
            int pageCount = 0;
            BookMeta bm = (BookMeta) is.getItemMeta();
            if (bm.getAuthor() != null && Protections.BookAuthorWhitelist.isWhitelisted(bm.getAuthor())) {
                return;
            }
            if (Protections.LimitNumberOfPages.getIntValue() > 0 && bm.getPageCount() > Protections.LimitNumberOfPages.getIntValue()) {
                e.setCancelled(true);
            }
            if (Protections.RemoveBooksNotMatchingCharset.isEnabled()) {
                for (String page : bm.getPages()) {
                    if (!Charset
                            .forName(Protections.ValidCharset.getTxtValue())
                            .newEncoder()
                            .canEncode(ChatColor.stripColor(page))) {
                        pageCount++;
                    }
                }
                if (pageCount >= Protections.PageCountThreshold.getIntValue()) {
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        if (Protections.BlockEnchantingInstead.isEnabled()) {
            ItemStack is = e.getItem();
            if (NBTStuff.hasNbtTag("IllegalStack", is, "NoEnchant", Protections.BlockEnchantingInstead)) {
                e.setCancelled(true);
                getLog().append2(Msg.PlayerEnchantBlocked.getValue(e.getEnchanter().getName()));
                return;
            }

            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {
                    e.setCancelled(true);
                    getLog().append2(Msg.PlayerEnchantBlocked.getValue(e.getEnchanter().getName()));
                }
            }
        }
    }

    @EventHandler
    public void onCraftPrep(PrepareItemCraftEvent e) {

        OverstackedItemCheck.CheckStorageInventory(e.getInventory(), (Player) e.getView().getPlayer());
        IllegalEnchantCheck.CheckStorageInventory(e.getInventory(), (Player) e.getView().getPlayer());
        BadAttributeCheck.CheckStorageInventory(e.getInventory(), (Player) e.getView().getPlayer());
    }

    @EventHandler
    public void onShulkerCheck(InventoryOpenEvent e) {

        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }

        if (e.getPlayer() instanceof Player) {
            if (SpigotMethods.isNPC((e.getPlayer()))) {
                return;
            }
            Player p = (Player) e.getPlayer();

            if (Protections.PreventCommandsInBed.isEnabled() && e.getPlayer().isSleeping()) {
                e.setCancelled(true);
                return;
            }

            if (Protections.PreventNestedShulkers.isEnabled() && IllegalStack.hasShulkers()) {
                Set<ItemStack> remove = new HashSet<>();

                try {
                    if (e.getInventory().getType() == InventoryType.SHULKER_BOX || (e.getView().getTitle() != null && e
                            .getView()
                            .getTitle()
                            .toLowerCase()
                            .contains("shulker"))) {

                        for (ItemStack is : e.getInventory().getContents()) {
                            if (is != null && is.getType().name().contains("SHULKER_BOX")) {
                                remove.add(is);
                            }
                        }
                    }
                } catch (IllegalStateException ex) {
                    return;
                }
                int removed = 0;
                if (!remove.isEmpty()) {
                    for (ItemStack is : remove) {
                        e.getInventory().remove(is);
                        removed++;
                    }
                }

                if (removed > 0) {
                    if (Protections.PreventNestedShulkers.notifyOnly()) {
                        getLog().notify(Protections.PreventNestedShulkers, " 触发者: " + e.getPlayer().getName());
                    } else {
                        getLog().append2(Msg.ShulkerClick.getValue(e.getPlayer().getName()));
                    }
                }
            }


            if (Protections.FixIllegalEnchantmentLevels.isThirdPartyInventory(e.getView())) {
                return;
            }

            for (ItemStack is : e.getInventory().getContents()) {
                if (is == null) {
                    continue;
                }

                if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !is
                        .getEnchantments()
                        .isEmpty() && !mcMMOListener.ismcMMOActive(p)) {
                    if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
                    {
                        if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p
                                .getWorld()
                                .getName())) //isn't in a checked world
                        {
                            continue;
                        }
                    }
                    if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) {
                        continue;
                    }

                    HashSet<Enchantment> replace = new HashSet<>();
                    for (Enchantment en : is.getEnchantments().keySet()) {
                        if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {

                            if (SlimefunCompat.isValid(is, en)) {
                                continue;
                            }

                            if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is
                                    .getEnchantmentLevel(en) == 4341)) {
                                continue;
                            }
                            if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) {
                                break;
                            }

                            if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en))) {
                                continue;
                            }

                            if (en.canEnchantItem(is)) {
                                getLog().append2(Msg.IllegalEnchantLevel.getValue(p, is, en));
                            } else {
                                getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                            }
                            replace.add(en);
                        } else {
                            if (!en.canEnchantItem(is)) {
                                if (SlimefunCompat.isValid(is, en)) {
                                    continue;
                                }
                                replace.add(en);
                                getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                            }
                        }
                    }

                    for (Enchantment en : replace) {
                        is.removeEnchantment(en);
                        if (en.canEnchantItem(is)) {
                            is.addEnchantment(en, en.getMaxLevel());
                        }
                    }
                }

                if (Protections.RemoveOverstackedItems.isEnabled()) {
                    if (!p.isOp()) {
                        if (Protections.RemoveItemTypes.isWhitelisted(is)) {
                            if (Protections.RemoveItemTypes.notifyOnly()) {
                                getLog().notify(
                                        Protections.RemoveItemTypes,
                                        " 触发者: " + e.getPlayer().getName() + " 物品为: " + is.getType().name()
                                );
                            } else {
                                getLog().append2(Msg.ItemTypeRemovedPlayer.getValue(p, is));
                                e.getInventory().remove(is);
                            }
                        }
                    }

                    if (is.getAmount() > is.getMaxStackSize()) {

                        if (!Protections.IllegalStackMode.isEnabled())  //黑名单模式且物品在黑名单中
                        {
                            if (!Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                                continue;
                            }
                            if (Protections.AllowStack.isThirdPartyInventory(e.getView())) {
                                continue;
                            }
                            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                                is.setAmount(is.getType().getMaxStackSize());
                                fListener.getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                            } else {
                                p.getInventory().remove(is);
                                fListener.getLog().append2(Msg.IllegalStackItemScan.getValue(p, is));
                            }
                            continue;
                        }

                        if (Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                            continue;
                        }
                        if (Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && p.hasPermission(
                                "illegalstack.overstack")) {
                            continue;
                        }

                        if (Protections.AllowStack.isThirdPartyInventory(e.getView())) {
                            continue;
                        }

                        if (Protections.RemoveOverstackedItems.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveOverstackedItems,
                                    " 触发者: " + e.getPlayer().getName() + " 物品为: " + is.getType().name()
                            );
                        } else if (Protections.FixOverstackedItemInstead.isEnabled()) {
                            is.setAmount(is.getType().getMaxStackSize());
                            getLog().append2(Msg.IllegalStackShorten.getValue((Player) e.getPlayer(), is));
                        } else {
                            e.getInventory().remove(is);
                            getLog().append2(Msg.IllegalStackItemScan.getValue((Player) e.getPlayer(), is));
                        }
                    }

                    
                    if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && is != null && is.hasItemMeta() && is.getItemMeta() instanceof BookMeta) {

                        BookMeta bm = (BookMeta) is.getItemMeta();

                        if (bm.getAuthor() != null && Protections.BookAuthorWhitelist.isWhitelisted(bm.getAuthor())) {
                            return;
                        }

                        String author;
                        if (bm.getAuthor() == null) {
                            author = "未知";
                        } else {
                            author = bm.getAuthor();
                        }

                        if (Protections.LimitNumberOfPages.getIntValue() > 0 && bm.getPageCount() > Protections.LimitNumberOfPages
                                .getIntValue()) {
                            getLog().append2(Msg.TooManyPages.getValue(e.getPlayer().getName()));
                            e.getInventory().removeItem(is);
                            e.setCancelled(true);
                        }
                        if (Protections.RemoveBooksNotMatchingCharset.isEnabled()) {
                            int InvalidPages = 0;
                            for (String page : bm.getPages()) {
                                if (!Charset
                                        .forName(Protections.ValidCharset.getTxtValue())
                                        .newEncoder()
                                        .canEncode(ChatColor.stripColor(page))) {
                                    InvalidPages++;
                                }
                            }

                            if (InvalidPages >= Protections.PageCountThreshold.getIntValue()) {
                                if (Protections.RemoveBooksNotMatchingCharset.notifyOnly()) {
                                    getLog().notify(Protections.RemoveBooksNotMatchingCharset, " 触发者: " + author);
                                } else {
                                    getLog().append2(Msg.BookRemoved.getValue(author));
                                    bm.setPages(new ArrayList<>());
                                    is.setItemMeta(bm);

                                    e.getInventory().removeItem(is);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //修复 1.12.2 - 1.13 中愚蠢的地毯/铁轨复制漏洞，以及 1.12 活塞/物品展示框复制漏洞
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonBreak(BlockPistonRetractEvent event) {

        if (Protections.DisableInWorlds.isWhitelisted(event.getBlock().getWorld().getName())) {
            return;
        }
        if (Protections.PreventEndCrystalLagMachine.isEnabled()) {
            int count = 0;
            ArrayList<Entity> remove = new ArrayList<>();
            for (Block b : event.getBlocks()) {
                Block next = b.getRelative(event.getDirection());

                for (Entity ent : b.getWorld().getNearbyEntities(next.getLocation(), 1, 1, 1)) {
                    if (ent instanceof EnderCrystal || ent instanceof ArmorStand) {
                        if (Protections.PreventEndCrystalLagMachine.isThirdPartyObject(ent)) {
                            continue;
                        }
                        count++;
                        remove.add(ent);
                    }
                }
            }

            if (count > 1 && !remove.isEmpty()) {
                event.setCancelled(true);
                getLog().append2(Msg.StoppedPushableEntity.getValue(remove.get(0).getLocation(), ""));
                for (Entity ec : remove) {
                    ec.remove();
                }
            }
        }

        if (Protections.PreventIndirectTNTPowerDupe.isEnabled()) {
            for (Block b : event.getBlocks()) {
                if (getIs112()) { //针对 1.12 TNT 的 hack，它不会触发实体生成事件
                    if (b.getType() != Material.TNT) {
                        continue;
                    }
                    b.setType(Material.AIR);
                } else {
                    if (b.getType() == Material.TNT) //找到被移动的 TNT 方块
                    {
                        movedTNT.put(b, System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(213, 1132));
                    }
                }
            }
        }

        //仅适用于版本 < 1.13 的物品展示框复制
        if (Protections.PreventItemFramePistonDupe.isEnabled()) {
            Set<Entity> removed = new HashSet<>();
            BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};

            //检查活塞后部的展示框
            BlockFace dir = event.getDirection();
            for (Entity ent : event.getBlock().getWorld().getNearbyEntities(event
                    .getBlock()
                    .getRelative(dir.getOppositeFace())
                    .getLocation(), 1.1, 1.1, 1.1)) {
                if (ent instanceof ItemFrame) {
                    removed.add(ent);
                }
            }

            for (Entity ent : removed) {
                ItemFrame frame = (ItemFrame) ent;
                String contents = "无物品";
                ItemStack is = frame.getItem();
                if (is.getType() != Material.AIR) {
                    contents = is.getType().name();
                }

                if (Protections.PreventItemFramePistonDupe.notifyOnly()) {
                    getLog().notify(Protections.PreventItemFramePistonDupe, " 触发 @" + frame.getLocation());
                } else {
                    getLog().append2(Msg.ItemFrameRemoveOnExtend + " @" + frame.getLocation());
                    frame.setItem(null);
                    ent.remove();
                }
            }

            removed.clear();
            //检查此活塞移动的所有方块
            if (event.isSticky()) {
                for (Block b : event.getBlocks()) {
                    for (BlockFace face : faces) {
                        Block moved = b.getRelative(face);
                        if (moved.getType() == Material.AIR) {

                            for (Entity ent : moved.getWorld().getNearbyEntities(moved.getLocation(), 1.0, 1.0, 1.0)) {
                                if (ent instanceof ItemFrame) {
                                    if (removed.contains(ent)) {
                                        continue;
                                    }
                                    ItemFrame frame = (ItemFrame) ent;
                                    String contents = "无物品";
                                    ItemStack is = frame.getItem();
                                    if (is != null) {
                                        contents = is.getType().name();
                                    }

                                    if (Protections.PreventItemFramePistonDupe.notifyOnly()) {
                                        getLog().notify(
                                                Protections.PreventItemFramePistonDupe,
                                                " 触发 @" + frame.getLocation()
                                        );
                                    } else {
                                        getLog().append2(Msg.ItemFrameRemoveOnRetract + " @" + frame.getLocation());
                                        frame.setItem(null);
                                        removed.add(ent);
                                    }
                                }
                            }

                            for (Entity ent : removed) {
                                ent.remove();
                            }
                        }
                    }
                }
            }
        }

        if (Protections.PreventHoppersToUnloadedChunks.isEnabled()) {
            Location l1 = event.getBlock().getLocation();
            for (Block r : event.getBlocks()) {
                Location l2 = r.getLocation();
                if (l1.getChunk() != l2.getChunk()) {
                    if (!l1.getChunk().isLoaded() || !l2.getChunk().isLoaded()) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        //<1.13 有地毯/铁轨复制，只有 1.14 有 TNT 复制
        if (Protections.PreventRailDupe.isEnabled()) {
            StringBuilder types = new StringBuilder();
            for (Block b : event.getBlocks()) {
                if (blacklist.contains(b.getType())) {

                    types.append(" ").append(b.getType().name());
                    if (!Protections.PreventRailDupe.notifyOnly()) {
                        if (!Protections.BreakExploitMachines.isEnabled()) {
                            if (b.getType() != Material.AIR) {
                                b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType(), 1));
                            }
                        }
                        b.setType(Material.AIR);
                        event.setCancelled(true);
                    }
                }
            }

            if (types.length() > 0) {

                if (Protections.PreventRailDupe.notifyOnly()) {
                    getLog().notify(
                            Protections.PreventRailDupe,
                            " 触发 @" + event.getBlock().getLocation() + " 受影响的方块: " + types
                    );
                } else {
                    getLog().append2(Msg.PistonRetractionDupe.getValue(event.getBlock().getLocation(), types.toString()));
                    if (!Protections.BreakExploitMachines.isEnabled()) {
                        if (event.getBlock().getType() != Material.AIR) {
                            event.getBlock().getWorld().dropItemNaturally(
                                    event.getBlock().getLocation(),
                                    new ItemStack(event.getBlock().getType(), 1)
                            );
                        }
                    }
                    event.getBlock().setType(Material.AIR);
                }

            }
        }
    }

    @EventHandler
    public void onBookCreate(PlayerEditBookEvent e) {
        if (Protections.DisableInWorlds.getTxtSet().contains(e.getPlayer().getWorld().getName())) {
            return;
        }

        if (Protections.DisableBookWriting.isEnabled()) {
            if (!Protections.BookAuthorWhitelist.getTxtSet().isEmpty()) {
                if (Protections.BookAuthorWhitelist.isWhitelisted(e.getPlayer().getName())) {
                    return;
                }
                if (e.getNewBookMeta() != null && e.getNewBookMeta().getAuthor() != null) {
                    if (Protections.BookAuthorWhitelist.isWhitelisted(e.getNewBookMeta().getAuthor())) {
                        return;
                    }
                }
            }

            if (!Protections.DisableBookWriting.notifyOnly()) {

                e.getPlayer().sendMessage(Msg.PlayerDisabledBookMsg.getValue(e.getPlayer(), ""));
                BookMeta bm = e.getNewBookMeta();
                List<String> pages = new ArrayList<>();
                pages.add(ChatColor.RED + "*书籍已禁用*");
                bm.setAuthor(e.getPlayer().getName());
                e.setSigning(true);
                bm.setPages(pages);

                e.setNewBookMeta(bm);

                final Player player = e.getPlayer();
                Scheduler.runTaskLater(this.plugin, () -> {
                    for (ItemStack is : player.getInventory()) {
                        if (is != null && (is.getType() == book || is.getType() == Material.WRITTEN_BOOK)) {
                            BookMeta bm1 = (BookMeta) is.getItemMeta();

                            if (bm1.getAuthor() != null && Protections.BookAuthorWhitelist.isWhitelisted(bm1.getAuthor())) {
                                continue;
                            }
                            for (String page : bm1.getPages()) {
                                if (page != null && !page.isEmpty()) {
                                    if (!is18() && player.getInventory().getItemInOffHand().equals(is)) {
                                        player.getInventory().setItemInOffHand(new ItemStack(Material.AIR, 1));
                                    } else {
                                        player.getInventory().removeItem(is);
                                    }
                                }
                            }
                        }
                        if (is != null && is.getType() == Material.WRITTEN_BOOK) {
                            BookMeta bm1 = (BookMeta) is.getItemMeta();
                            if (Protections.BookAuthorWhitelist.isWhitelisted(bm1.getAuthor())) {
                                continue;
                            }

                            for (String page : bm1.getPages()) {
                                if (page.contains(ChatColor.RED + "*书籍已禁用*")) {
                                    player.getInventory().removeItem(is);
                                }
                            }
                        }
                    }
                }, 12, player);
            } else {
                getLog().notify(Protections.DisableBookWriting, " 触发者" + e.getPlayer());
            }

        }
    }

    @EventHandler
    public void EndGatewayTeleportProtection(VehicleMoveEvent e) {

        if (hasPassengers == -1) {

            try {
                e.getVehicle().getPassengers();
                hasPassengers = 1;
            } catch (NoSuchMethodError ignored) {
            }

            hasPassengers = 0;
        }

        if (hasPassengers == 0 || Protections.DisableInWorlds.isWhitelisted(e.getVehicle().getWorld().getName())) {
            return;
        }

        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e
                .getFrom()
                .getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }


        Player driver = null;
        for (Entity ent : e.getVehicle().getPassengers()) {
            if (ent instanceof Player) {
                driver = (Player) ent;
            }
        }

        if (driver == null) {
            return;
        }

        if (Protections.PreventHeadInsideBlock.isEnabled()) {
            if (driver.getEyeLocation().getBlock().getType().isSolid()) {
                fListener.getLog().append2(Msg.HeadInsideSolidBlock.getValue(driver, e.getVehicle()));
                e.getVehicle().eject();
                e.getVehicle().remove();
            }

        }

        if (!Protections.PreventEndGatewayCrashExploit.isEnabled()) {
            return;
        }
        if ((e.getFrom().getWorld().getEnvironment() == Environment.THE_END && e
                .getTo()
                .getWorld()
                .getEnvironment() == Environment.THE_END) && e.getFrom().getBlock() != e.getTo().getBlock()) {


            for (BlockFace face : BlockFace.values()) {
                Block next = e.getVehicle().getLocation().getBlock().getRelative(face);
                if (next.getType() == Material.END_GATEWAY) {
                    e.getVehicle().eject();
                    e.getVehicle().remove();
                    getLog().append2(Msg.StaffMsgEndGatewayVehicleRemoved.getValue(driver, e.getVehicle()));
                }
            }

			/*
			for(int x = (int) e.getVehicle().getBoundingBox().getMinX();x <= e.getVehicle().getBoundingBox().getMaxX(); x = x + 1)
				for(int z = (int) e.getVehicle().getBoundingBox().getMinZ();z <= e.getVehicle().getBoundingBox().getMaxZ(); z = z + 1)
				{
					for(BlockFace face:faces) {

						Block b = e.getVehicle().getWorld().getBlockAt(x, e.getTo().getBlockY(), z);
						Block next = b.getRelative(face);

						if(b.getType() == Material.END_GATEWAY || next.getType() == Material.END_GATEWAY) {
							e.getVehicle().eject();
							e.getVehicle().remove();
							getLog().append2(Msg.StaffMsgEndGatewayVehicleRemoved.getValue(driver,e.getVehicle()));
						}
					}
				}
			 */

        }
    }

    @EventHandler
    public void onEntityTeleport(EntityPortalEvent e) {


        if (e.getTo() == null || e.getFrom() == null || e.getEntity() == null) {
            return;
        }

        if (Protections.DisableInWorlds.isWhitelisted(e.getTo().getWorld().getName())) {
            return;
        }

        String whiteLiString = "";
        for (String s : Protections.DisableInWorlds.getTxtSet()) {
            whiteLiString = whiteLiString + s;
        }


        boolean blockNether = Protections.BlockNonPlayersInNetherPortal.isEnabled();
        boolean blockEnd = Protections.BlockNonPlayersInEndPortal.isEnabled();

        if (!(e.getEntity() instanceof Player)) {
            //实体传送
            World wTo = e.getTo().getWorld();
            World wFrom = e.getFrom().getWorld();


            double randY = ThreadLocalRandom.current().nextDouble(0, 1);
            if (blockNether && (wFrom.getEnvironment() == Environment.NETHER || wTo.getEnvironment() == Environment.NETHER)) {
                boolean allowed = !Protections.NetherWhiteListMode.isEnabled() || Protections.NetherWhiteList.isWhitelisted(e
                        .getEntity()
                        .getType()
                        .name());
                if (!Protections.NetherWhiteListMode.isEnabled() && Protections.NetherWhiteList.isWhitelisted(e
                        .getEntity()
                        .getType()
                        .name())) {
                    allowed = false;
                }

                if (!allowed) {
                    if (Protections.BlockNonPlayersInNetherPortal.notifyOnly()) {
                        getLog().notify(
                                Protections.BlockNonPlayersInNetherPortal,
                                " 触发者" + e.getEntity().getType().name() + " @" + e.getEntity().getLocation()
                        );
                    } else {
                        e.setCancelled(true);
                        Vector v = e.getEntity().getVelocity().multiply(-2);
                        e.getEntity().setVelocity(v);
                        if (Protections.NotifyBlockedPortalAttempts.isEnabled()) {
                            getLog().append(
                                    Msg.NetherPortalBlock.getValue(e.getEntity().getLocation(), e.getEntity().getName()),
                                    Protections.BlockNonPlayersInNetherPortal
                            );
                            LOGGER.warn(
                                    "下界传送门阻止的物品: {} 离开世界: {} 进入世界: {} 世界 {} 是否在白名单中？白名单 = {}",
                                    e.getEntity().getType().name(),
                                    e.getFrom().getWorld().getName(),
                                    e.getTo().getWorld().getName(),
                                    Protections.DisableInWorlds.isWhitelisted(e.getTo().getWorld().getName()),
                                    whiteLiString
                            );
                        }
                    }
                }
            }

            if (blockEnd && (wFrom.getEnvironment() == Environment.THE_END || wTo.getEnvironment() == Environment.THE_END)) {

                boolean allowed = !Protections.EndWhiteListMode.isEnabled() || Protections.EndWhiteList.isWhitelisted(e
                        .getEntity()
                        .getType()
                        .name());
                if (!Protections.EndWhiteListMode.isEnabled() && Protections.EndWhiteList.isWhitelisted(e
                        .getEntity()
                        .getType()
                        .name())) {
                    allowed = false;
                }

                if (!allowed) {
                    if (Protections.BlockNonPlayersInEndPortal.notifyOnly()) {
                        getLog().notify(
                                Protections.BlockNonPlayersInEndPortal,
                                " 触发者" + e.getEntity().getType().name() + " @" + e.getEntity().getLocation()
                        );
                    } else {
                        e.setCancelled(true);
                        Vector v = e.getEntity().getVelocity().multiply(-2);
                        v.setX(ThreadLocalRandom.current().nextDouble(-1, 1));
                        v.setZ(ThreadLocalRandom.current().nextDouble(-1, 1));
                        v.setY(randY);
                        e.getEntity().setVelocity(v);
                        if (Protections.NotifyBlockedPortalAttempts.isEnabled()) {
                            getLog().append2(Msg.EndPortalBlock.getValue(e.getEntity().getLocation(), e.getEntity().getName()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (Protections.DisableInWorlds.isWhitelisted(e.getChunk().getWorld().getName())) {
            return;
        }

        if (Protections.RemoveExistingGlitchedMinecarts.isEnabled()) {
            final Chunk chunk = e.getChunk();
            Scheduler.runTaskLater(this.plugin, () -> {
                if (!chunk.isLoaded()) {
                    return;
                }

                for (Entity ent : e.getChunk().getEntities()) {
                    if (ent instanceof Minecart) {

                        Block b = ent.getLocation().getBlock();
                        if (b.getType().isSolid()) {
                            if (Protections.RemoveExistingGlitchedMinecarts.notifyOnly()) {
                                getLog().notify(
                                        Protections.RemoveExistingGlitchedMinecarts,
                                        " 触发 @" + b.getLocation()
                                );
                            } else {
                                getLog().append2(Msg.MinecartGlitch1.getValue(b.getLocation(), b.getType().name()));
                                ent.remove();
                            }
                        }
                    }
                }
            }, 250, e.getChunk().getBlock(0, 0, 0).getLocation());

        }

        if (Protections.DestroyBadSignsonChunkLoad.isEnabled()) {
            String id = e.getChunk().getX() + "" + e.getChunk().getZ();
            sTimer.checkChunk(id, e.getChunk().getTileEntities());
        }
    }


    
    @EventHandler(priority = EventPriority.LOWEST)
    public void creatureSpawnEvent(CreatureSpawnEvent e) {

        if (Protections.DisableInWorlds.isWhitelisted(e.getLocation().getWorld().getName())) {
            return;
        }
    	
        if (Protections.PreventZombieItemPickup.isEnabled()) {
            if (e.getEntity() instanceof Zombie) {
                Zombie z = (Zombie) e.getEntity();
                z.setCanPickupItems(false);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof EnderDragon && Protections.BlockNonPlayersInEndPortal.isEnabled() && event
                .getEntity()
                .getWorld()
                .getEnvironment() == Environment.THE_END) {
            fTimer.setEndScanFinish(System.currentTimeMillis() + (30 * 1000));
            fTimer.setDragon(event.getEntity().getWorld());
        }
        fListener.getInstance();
        if (fListener.is18 || !Protections.PreventLootingExploit.isEnabled()) {
            return;
        }

        if (event.getEntity().getKiller() == null || event
                .getEntity()
                .getKiller() == null || event.getEntity() instanceof Player) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        ItemStack offHand = killer.getInventory().getItemInOffHand();
        ItemStack mainHand = killer.getInventory().getItemInMainHand();
        boolean nerf = false;
        if (offHand == null || offHand.getType() == Material.AIR) {
            return;
        }

        if (offHand.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) //副手附魔发现
        {
            if (mainHand.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) //主手也有抢夺附魔
            {
                if (mainHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) < offHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)) //试图用等级作弊，拒绝
                {
                    nerf = true;
                } else  //否则他们肯定是在尝试作弊，不给予任何奖励
                {
                    nerf = true;
                }
            }
        }

        if (nerf) {
            for (ItemStack is : event.getDrops()) {
                is.setAmount(1);
            }
        }
    }

    @EventHandler
    public void onPistonBreak(BlockPistonExtendEvent event) {
        //通过将方块推入矿车来修复矿车卡入漏洞

        if (Protections.DisableInWorlds.isWhitelisted(event.getBlock().getWorld().getName())) {
            return;
        }

        if (Protections.PreventEndCrystalLagMachine.isEnabled()) {
            int count = 0;
            ArrayList<Entity> remove = new ArrayList<>();

            for (Block b : event.getBlocks()) {
                for (Entity ent : b.getWorld().getNearbyEntities(b.getLocation(), 0.5, 0.5, 0.5)) {
                    if (ent instanceof EnderCrystal || ent instanceof ArmorStand) {
                        if (Protections.PreventEndCrystalLagMachine.isThirdPartyObject(ent)) {
                            continue;
                        }
                        count++;
                        remove.add(ent);
                    }
                }
            }

            Block next = event.getBlock().getRelative(event.getDirection());
            for (Entity ent : next.getWorld().getNearbyEntities(next.getLocation(), 0.5, 0.5, 0.5)) {
                if (ent instanceof EnderCrystal || ent instanceof ArmorStand) {
                    if (Protections.PreventEndCrystalLagMachine.isThirdPartyObject(ent)) {
                        continue;
                    }
                    count++;
                    remove.add(ent);
                }
            }
            if (count > 1 && !remove.isEmpty()) {
                event.setCancelled(true);
                getLog().append2(Msg.StoppedPushableEntity.getValue(remove.get(0).getLocation(), ""));
                for (Entity ec : remove) {
                    ec.remove();
                }
            }

        }

        if (Protections.PreventArmorStandLagMachine.isEnabled() && event.getDirection() == BlockFace.UP) {

            Block above = event.getBlock().getRelative(BlockFace.UP);
            for (Entity ent : above.getWorld().getNearbyEntities(above.getLocation().clone().add(0.5, 0, 0.5), 0, 1.5, 0)) {
                if (ent instanceof ArmorStand) {
                    if (Protections.PreventEndCrystalLagMachine.isThirdPartyObject(ent)) {
                        continue;
                    }
                    if (Protections.BreakExploitMachines.isEnabled()) {
                        event.getBlock().setType(Material.AIR);
                    }
                    getLog().append2(Msg.StoppedPushableArmorStand.getValue(ent.getLocation(), ""));
                    event.setCancelled(true);
                    return;
                }
            }
            if (!event.isCancelled()) {
                for (Block b : event.getBlocks()) {
                    for (Entity ent : b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0, 0.5), 0, 1.5, 0)) {
                        if (ent instanceof ArmorStand) {
                            if (Protections.PreventEndCrystalLagMachine.isThirdPartyObject(ent)) {
                                continue;
                            }
                            if (Protections.BreakExploitMachines.isEnabled()) {
                                event.getBlock().setType(Material.AIR);
                            }
                            getLog().append2(Msg.StoppedPushableArmorStand.getValue(ent.getLocation(), ""));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (Protections.PreventMinecartGlitch.isEnabled()) {
            final List<Block> bList = event.getBlocks();
            final BlockFace dir = event.getDirection();

            Scheduler.runTaskLater(this.plugin, () -> {
                for (Block b : bList) {
                    Block next = b.getRelative(dir);
                    if (next.getType() == Material.AIR || Protections.MinecartBlockWhiteList.isWhitelisted(next
                            .getType()
                            .name()) || fListener.getInstance().blacklist.contains(next.getType())) {
                        continue;
                    }

                    for (Entity ent : next.getWorld().getNearbyEntities(
                            next.getLocation().clone().add(0.5, 0.5, 0.5),
                            0.4,
                            0.4,
                            0.4
                    )) {

                        if (ent instanceof Minecart) {
                            if (Protections.PreventMinecartGlitch.notifyOnly()) {
                                getLog().notify(
                                        Protections.PreventMinecartGlitch,
                                        " 触发 @" + ent.getLocation()
                                );
                            } else {
                                getLog().append2(Msg.MinecartGlitch2.getValue(ent.getLocation(), ""));
                                ent.remove();
                            }
                        }
                    }
                }
            }, 50, event.getBlock().getLocation());
        }
        //在 1.14 中尚未修复
        if (Protections.PreventCactusDupe.isEnabled()) {
            BlockFace dir = event.getDirection();
            Block head = event.getBlock().getRelative(dir);
            Block next = head.getRelative(dir);
            List<Material> growable = loadGrowables();
            List<Material> substrate = loadSubstrates();

            //检查活塞是否将基底方块推到植物下方
            for (Block b : event.getBlocks()) {
                if (substrate.contains(b.getType())) { //推动基底方块

                    Block above = b.getRelative(BlockFace.UP);
                    if (growable.contains(above.getType())) { //上方有可生长方块
                        if (Protections.PreventCactusDupe.notifyOnly()) {
                            getLog().notify(Protections.PreventCactusDupe, " 触发 @" + event.getBlock().getLocation());
                        } else {
                            event.setCancelled(true);
                            if (!Protections.BreakExploitMachines.isEnabled()) {
                                World w = event.getBlock().getWorld();
                                w.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(event.getBlock().getType(), 1));
                                w.dropItemNaturally(above.getLocation(), new ItemStack(above.getType(), 1));
                                w.dropItemNaturally(b.getLocation(), new ItemStack(b.getType(), 1));
                            }
                            getLog().append2(Msg.ZeroTickGlitch.getValue(
                                    event.getBlock().getLocation(),
                                    above.getType().name() + ", " + b.getType().name() + ", " + event.getBlock().getType().name()
                            ));
                            b.setType(Material.AIR);
                            above.setType(Material.AIR);
                            event.getBlock().setType(Material.AIR);
                            return;
                        }
                    }
                }
            }

            for (BlockFace face : getFaces()) {
                Block adj = head.getRelative(face);
                Block adj2 = next.getRelative(face);
                Material found = null;
                if (adj.getType() == Material.CACTUS || adj.getType() == Material.CHORUS_PLANT) {
                    if (Protections.PreventCactusDupe.notifyOnly()) {
                        getLog().notify(Protections.PreventCactusDupe, " 触发 @" + adj.getLocation());
                    } else {
                        if (!Protections.BreakExploitMachines.isEnabled()) {
                            adj.getWorld().dropItemNaturally(adj.getLocation(), new ItemStack(adj.getType(), 1));
                        }
                        found = adj.getType();
                        adj.setType(Material.AIR);
                    }
                } else if (adj2.getType() == Material.CACTUS || adj2.getType() == Material.CHORUS_PLANT) {
                    if (Protections.PreventCactusDupe.notifyOnly()) {
                        getLog().notify(Protections.PreventCactusDupe, " 触发 @" + adj2.getLocation());
                    } else {
                        if (!Protections.BreakExploitMachines.isEnabled()) {
                            adj2.getWorld().dropItemNaturally(adj2.getLocation(), new ItemStack(adj2.getType(), 1));
                        }
                        found = adj2.getType();
                        adj2.setType(Material.AIR);
                    }
                }

                //检查活塞上方
                Block above = head.getRelative(BlockFace.UP);

                if (!is1152 && substrate.contains(above.getType()) && growable.contains(above
                        .getRelative(BlockFace.UP)
                        .getType())) {

                    found = above.getType();
                    above.setType(Material.AIR);
                }

                if (found != null) {
                    event.setCancelled(true);
                    if (!Protections.BreakExploitMachines.isEnabled()) {
                        event.getBlock().getWorld().dropItemNaturally(
                                event.getBlock().getLocation(),
                                new ItemStack(event.getBlock().getType(), 1)
                        );
                    }
                    event.getBlock().setType(Material.AIR);
                    //检查2
                    getLog().append2(Msg.ZeroTickGlitch.getValue(event.getBlock().getLocation(), found.name()));
                    break;
                }
            }
        }

        if (Protections.PreventHoppersToUnloadedChunks.isEnabled()) {
            Location l1 = event.getBlock().getLocation();
            for (Block r : event.getBlocks()) {
                Location l2 = r.getLocation();
                if (l1.getChunk() != l2.getChunk()) {
                    if (!l1.getChunk().isLoaded() || !l2.getChunk().isLoaded()) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (Protections.PreventItemFramePistonDupe.isEnabled()) {
            Set<Entity> removed = new HashSet<>();
            BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
            //检查活塞后部的展示框
            BlockFace dir = event.getDirection();
            for (Entity ent : event.getBlock().getWorld().getNearbyEntities(event
                    .getBlock()
                    .getRelative(dir.getOppositeFace())
                    .getLocation(), 1, 1, 1)) {
                if (ent instanceof ItemFrame) {
                    removed.add(ent);
                }
            }

            for (Entity ent : removed) {
                ItemFrame frame = (ItemFrame) ent;
                String contents = "无物品";
                ItemStack is = frame.getItem();

                if (is.getType() != Material.AIR) {
                    contents = is.getType().name();
                }
                if (Protections.PreventItemFramePistonDupe.notifyOnly()) {
                    getLog().notify(
                            Protections.PreventItemFramePistonDupe,
                            " 触发 @" + frame.getLocation() + " 物品: " + contents
                    );
                } else {
                    getLog().append2(Msg.ItemFrameRemoveOnExtend.getValue(frame.getLocation(), contents));

                    frame.setItem(null);
                    ent.remove();
                }

            }

            removed.clear();
            //检查此活塞移动的所有方块
            for (Block b : event.getBlocks()) {
                for (BlockFace face : faces) {
                    Block moved = b.getRelative(face);
                    if (moved.getType() == Material.AIR) {

                        for (Entity ent : moved.getWorld().getNearbyEntities(moved.getLocation(), 1.0, 1.0, 1.0)) {
                            if (ent instanceof ItemFrame) {
                                if (removed.contains(ent)) {
                                    continue;
                                }
                                ItemFrame frame = (ItemFrame) ent;
                                String contents = "无物品";
                                ItemStack is = frame.getItem();
                                if (is != null) {
                                    contents = is.getType().name();
                                }

                                if (Protections.PreventItemFramePistonDupe.notifyOnly()) {
                                    getLog().notify(
                                            Protections.PreventItemFramePistonDupe,
                                            " 触发 @" + frame.getLocation() + " 物品: " + contents
                                    );
                                } else {
                                    getLog().append2(Msg.ItemFrameRemoveOnExtend.getValue(frame.getLocation(), contents));
                                    frame.setItem(null);
                                    removed.add(ent);
                                }
                            }
                        }
                        if (!Protections.PreventItemFramePistonDupe.notifyOnly()) {
                            for (Entity ent : removed) {
                                ent.remove();
                            }
                        }
                    }
                }
            }
        }

        //间接动力 TNT 复制器
        if (Protections.PreventIndirectTNTPowerDupe.isEnabled()) {
            for (Block b : event.getBlocks()) {
                if (getIs112()) { //针对 1.12 TNT 的 hack，它不会触发实体生成事件
                    if (b.getType() != Material.TNT) {
                        continue;
                    }
                    if (!Protections.PreventIndirectTNTPowerDupe.notifyOnly()) {
                        b.setType(Material.AIR);
                    }
                } else {
                    if (b.getType() == Material.TNT) //找到被移动的 TNT 方块
                    {
                        movedTNT.put(b, System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(213, 1132));
                    }
                }
            }
        }

        if (Protections.PreventRailDupe.isEnabled()) {
            StringBuilder types = new StringBuilder();
            for (Block b : event.getBlocks()) {
                if (blacklist.contains(b.getType())) {
                    types.append(" ").append(b.getType().name());
                    if (!Protections.PreventRailDupe.notifyOnly()) {
                        if (!Protections.BreakExploitMachines.isEnabled()) {
                            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType(), 1));
                        }
                        b.setType(Material.AIR);
                        event.setCancelled(true);
                    }
                }
            }

            if (types.length() > 0) {
                if (Protections.PreventRailDupe.notifyOnly()) {
                    getLog().notify(
                            Protections.PreventRailDupe,
                            " 触发 @" + event.getBlock().getLocation() + " 受影响的方块: " + types
                    );
                } else {
                    getLog().append2(Msg.PistonRetractionDupe.getValue(event.getBlock().getLocation(), types.toString()));

                    if (!Protections.BreakExploitMachines.isEnabled()) {
                        event.getBlock().getWorld().dropItemNaturally(
                                event.getBlock().getLocation(),
                                new ItemStack(event.getBlock().getType(), 1)
                        );
                    }
                    event.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    private List<Material> loadSubstrates() {
        Material[] subs = new Material[]{Material.SAND, Material.RED_SAND, Material.END_STONE};
        if (is114() || is115()) {
            subs = new Material[]{Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT, Material.RED_SAND, Material.SAND, Material.GRAVEL,
                    Material.MYCELIUM, Material.PODZOL, Material.END_STONE};
        }
        return Arrays.asList(subs);
    }

    private List<Material> loadGrowables() {
        Material[] grows = new Material[]{Material.CACTUS, Material.CHORUS_FLOWER};
        fListener.getInstance();
        if (fListener.getIs114() || fListener.getInstance().is115()) {
            grows = new Material[]{Material.BAMBOO, Material.SUGAR_CANE, Material.CACTUS, Material.CHORUS_FLOWER};
        }
        return Arrays.asList(grows);
    }

    @EventHandler()
    public void CMIShulkerFix(InventoryClickEvent e) {
        if (Protections.DisableInWorlds.isWhitelisted(e.getWhoClicked().getWorld().getName())) {
            return;
        }
        if (SpigotMethods.isNPC(e.getWhoClicked())) {
            return;
        }
        if (Protections.BlockCMIShulkerStacking.isEnabled()) {
            if (Protections.PreventNestedShulkers.isEnabled() && IllegalStack.hasShulkers() && IllegalStack.isCMI()) {
                try {
                    if (e.getView().getTitle().toLowerCase().contains("shulker")) {
                    }
                } catch (IllegalStateException ex) {
                    return;
                }
                if (e.isShiftClick()) {
                    Inventory inv = e.getWhoClicked().getOpenInventory().getTopInventory();
                    if (inv.getType() != InventoryType.SHULKER_BOX && e
                            .getView()
                            .getTitle()
                            .toLowerCase()
                            .contains("shulker"))//inv.getName().toLowerCase().contains("shulker"))
                    {
                        if (e.getCurrentItem() != null && e.getCurrentItem().getType().name().contains("SHULKER_BOX")) {
                            if (Protections.PreventNestedShulkers.notifyOnly()) {
                                getLog().notify(
                                        Protections.PreventNestedShulkers,
                                        " 触发者: " + e.getWhoClicked().getName()
                                );
                            } else {
                                e.getWhoClicked().sendMessage(Msg.PlayerCMIShulkerNest.getValue());
                                e.setCancelled(true);
                            }
                        }
                    }
                }
                if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.SHULKER_BOX && e
                        .getView()
                        .getTitle()
                        .toLowerCase()
                        .contains("shulker"))//e.getClickedInventory().getName().toLowerCase().contains("shulker"))
                {

                    if (e.getCursor() != null && e.getCursor().getType().name().contains("SHULKER_BOX")) {
                        if (Protections.PreventNestedShulkers.notifyOnly()) {
                            getLog().notify(Protections.PreventNestedShulkers, " 触发者:" + e.getWhoClicked().getName());
                        } else {
                            e.getWhoClicked().sendMessage(Msg.PlayerCMIShulkerNest.getValue());
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler()
    public void onHopperTransfer(InventoryMoveItemEvent e) {

        if (Protections.IgnoreAllHopperChecks.isEnabled()) {
            return;
        }

        if (Protections.RemoveItemsMatchingName.isEnabled() && e.getSource() instanceof Hopper) {
            if (Protections.BlockEnchantingInstead.isEnabled() || Protections.BlockRepairsInstead.isEnabled()) {
                return;
            }
            Hopper h = (Hopper) e.getSource();
            ItemStack is = e.getItem();
            if (is != null && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();

                boolean loreFound = false;
                boolean nameFound = false;
                for (String s : Protections.RemoveItemsMatchingName.getLoreNameList().keySet()) {

                    if (Protections.RemoveItemsMatchingName.getLoreNameList().get(s) && im.hasLore()) {
                        for (String line : im.getLore()) {
                            if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                                if (line.equals(s)) {
                                    loreFound = true;
                                }
                            } else if (ChatColor.stripColor(line).contains(s)) {
                                loreFound = true;
                            }
                        }
                    } else if (im.hasDisplayName()) {
                        if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                            if (im.getDisplayName().equals(s)) {
                                nameFound = true;
                            }
                        } else if (ChatColor.stripColor(im.getDisplayName()).contains(s)) {
                            nameFound = true;
                        }

                    }
                    if (loreFound || nameFound) {

                        if (Protections.ItemNamesToRemove.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemsMatchingName,
                                    " 触发者: " + is.getType().name() + " @" + h.getLocation()
                            );
                        } else {
                            getLog().append2(Msg.NamedItemRemovalHopper.getValue(h.getLocation(), is.getType().name()));
                            h.getInventory().removeItem(is);

                        }
                        return;
                    }
                }
            }
        }
        if (Protections.PreventOverStackedItemInHoppers.isEnabled()) {
            if (e.getSource() instanceof Hopper) {
                Hopper h = (Hopper) e.getSource();
                if (Protections.DisableInWorlds.isWhitelisted(h.getWorld().getName())) {
                    return;
                }
            }

			/*
			if(Protections.RemoveEnchantsLargerThan.getIntValue() > 0) {
				ItemStack is = e.getItem();
				HashSet<Enchantment> remove = new HashSet<>();
				for(Enchantment en:is.getEnchantments().keySet())
					if(is.getEnchantmentLevel(en) > en.getMaxLevel() && is.getEnchantmentLevel(en) > Protections.RemoveEnchantsLargerThan.getIntValue())
					{
						getLog().append2(Msg.);
						//getLog().append2("发现非法附魔: " + is.getType().name() + " 附魔: " + en.getName()
						+ " 等级: (" + is.getEnchantmentLevel(en) + ") " + " 服务器最大等级为: " + Protections.RemoveEnchantsLargerThan.getIntValue()
						+ " " ); //getSource(e.getSource()));
						remove.add(en);

					}

				for(Enchantment en:remove)
					is.removeEnchantment(en);
			}
			 */
            if (e.getItem() != null && e.getItem().getAmount() > e.getItem().getMaxStackSize()) {
                if (Protections.AllowStack.isWhitelisted(e.getItem().getType().name())) {
                    return;
                }
                e.setCancelled(true);
                e.getSource().removeItem(e.getItem());


            }
        }
    }

    @EventHandler()
    public void onBlockExplode(BlockExplodeEvent e) {
        if (Protections.PreventBedExplosions.isEnabled()) {
            if (e.getYield() == 0.2f && e.getBlock().getLocation().getWorld().getName().toUpperCase().contains("_NETHER") || e
                    .getBlock()
                    .getLocation()
                    .getWorld()
                    .getName()
                    .toUpperCase()
                    .contains("_THE_END")) {
                e.blockList().clear();
                e.setYield(0f);
                e.setCancelled(true);
                getLog().append(
                        Msg.StaffMsgBedExplosion.getValue(e.getBlock().getLocation().toString()),
                        Protections.PreventBedExplosions
                );
            }
        }
    }

    @EventHandler()
    public void onTntExplode(EntityExplodeEvent e) {
        if (Protections.DisableInWorlds.isWhitelisted(e.getEntity().getWorld().getName())) {
            return;
        }

        if (Protections.PreventRecordDupe.isEnabled()) {
            if (e.getEntity() instanceof TNTPrimed) {
                TNTPrimed primed = (TNTPrimed) e.getEntity();
                if (primed.getSource() instanceof Skeleton) {
                    e.setCancelled(true);
                }


            }
        }
    }

    @EventHandler()
    public void onPistonExplode(EntityExplodeEvent e) //即使在 1.14 中仍然有效的漏洞
    {

        if (Protections.DisableInWorlds.isWhitelisted(e.getEntity().getWorld().getName())) {
            return;
        }

        if (Protections.PreventBedrockDestruction.isEnabled()) {
            HashSet<Block> remove = new HashSet<>();
            for (Block b : e.blockList()) {
                if (pistonCheck.contains(b.getType())) {
                    for (BlockFace face : BlockFace.values()) {
                        if (b.getRelative(face).getType() == Material.BEDROCK) {
                            remove.add(b);
                            b.setType(Material.AIR);
                            for (BlockFace face2 : faces) {
                                Block next = b.getRelative(face2);
                                if (pistonCheck.contains(next.getType())) {
                                    next.setType(Material.AIR);
                                }
                            }
                            getLog().append2(Msg.PistonHeadRemoval.getValue(b.getLocation(), ""));
                        }
                    }
                }

            }


            if (!remove.isEmpty()) {
                e.blockList().removeAll(remove);
                for (Block b : remove) {
                    Scheduler.runTaskLater(plugin, () -> b.setType(Material.AIR), 5, b.getLocation());
                }
            }

        }
    }

    @EventHandler()
    public void onDupeCheck(PlayerPickupItemEvent e) {

        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }
        if (Protections.PreventRecipeDupe.isEnabled()) {
            Player p = e.getPlayer();
            if (p.getOpenInventory().getTopInventory() instanceof CraftingInventory) {
                boolean empty = true;
                CraftingInventory ca = (CraftingInventory) p.getOpenInventory().getTopInventory();

                for (int i = 0; i < 4; i++) {
                    ItemStack is = ca.getMatrix()[i];
                    if (is != null) {
                        empty = false;
                    }
                }

                if (!empty) {
                    e.setCancelled(true);

                    if (!spamCheck.containsKey(p)) {
                        spamCheck.put(p, 0L);
                    }

                    if (System.currentTimeMillis() >= spamCheck.get(p)) {
                        spamCheck.put(p, System.currentTimeMillis() + 15000);
                        p.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreativeSet(InventoryCreativeEvent e) {

        if (Protections.RemoveCustomAttributes.isEnabled() || Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {

                if (Protections.RemoveOverstackedItems.isEnabled())//我认为所有检查可能都需要移到各自的类中
                {
                    if (OverstackedItemCheck.CheckContainer(e.getCursor(), e.getInventory())) {
                        e.setResult(Result.DENY);
                    }
                }

                if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
                    if (IllegalEnchantCheck.isIllegallyEnchanted(e.getCursor(), e.getInventory())) {
                        e.setResult(Result.DENY);
                    }
                }

                if (!Protections.RemoveItemTypes.getTxtSet().isEmpty()) {
                    if (RemoveItemTypesCheck.CheckForIllegalTypes(e.getCursor(), e.getInventory())) {
                        e.setResult(Result.DENY);
                    }
                }


                if (Protections.RemoveCustomAttributes.isEnabled()) {
                    if (Protections.AllowBypass.isEnabled() && e.getWhoClicked() instanceof Player && e.getWhoClicked()
                            .hasPermission("illegalstack.enchantbypass")) {
                        return;
                    }

                    if (BadAttributeCheck.hasBadAttributes(e.getCursor(), e.getInventory())) {
                        e.setResult(Result.DENY);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (SpigotMethods.isNPC(e.getPlayer())) {
            return;
        }
        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }

        if (Protections.PreventPortalTraps.isEnabled()) {
            Player p = e.getPlayer();

            Block exit = p.getLocation().getBlock();
            if (exit.getType() == fListener.getPortal()) {

                for (BlockFace face : fListener.getFaces()) {
                    exit = exit.getRelative(face);
                    if (exit.getType() == fListener.getPortal()) {
                        break;
                    }

                }

                boolean valid = false;
                for (int i = 0; i < 5; i++) {
                    for (BlockFace face : fListener.getFaces()) {
                        Block next = exit.getRelative(face);
                        if (getPassThrough().contains(next.getType())) {
                            if (getPassThrough().contains(next.getRelative(BlockFace.UP).getType())) {
                                valid = true;
                                break;
                            }
                        }

                    } //在出口方块未找到有效出口点，搜索并尝试找到新的有效传送门方块进行检查
                    if (!valid) {
                        p.getLocation().getBlock().breakNaturally();
                        fListener.getLog().append2(Msg.StaffMsgBlockedPortalLogin.getValue(p, p.getLocation().toString()));

                        return;
                    }
                }
            }
        }

        if (IllegalStack.isNbtAPI() && Protections.CheckGroundForBadShulkerAtLogin.isEnabled()) {
            for (Entity ent : e.getPlayer().getLocation().getChunk().getEntities()) {
                if (ent instanceof Item) {
                    Item itm = (Item) ent;
                    if (itm.getItemStack().getType().name().contains("SHULKER_BOX")) {
                        int tagSize = NBTStuff.isBadShulker(itm.getItemStack());
                        if (tagSize > 0) {
                            getLog().append2(Msg.StaffBadShulkerRemoved.getValue(itm.getLocation(), tagSize));
                            itm.remove();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < e.getPlayer().getInventory().getSize(); i++) {
            ItemStack is = e.getPlayer().getInventory().getItem(i);
            if (is == null) {
                continue;
            }
            Player p = e.getPlayer();
            if (Protections.RemoveAllRenamedItems.isEnabled()) {
                if (!p.hasPermission("IllegalStack.RenameBypass")) {
                    if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                        getLog().append2(Msg.RemovedRenamedItem.getValue(p, is));
                        p.getInventory().removeItem(is);
                    }
                }
            }

            if (Protections.RemoveItemsMatchingName.isEnabled()) {
                if (Protections.BlockEnchantingInstead.isEnabled() || Protections.BlockRepairsInstead.isEnabled()) {
                    return;
                }
                HashMap<String, Boolean> target = Protections.RemoveItemsMatchingName.getLoreNameList();
                boolean found = false;

                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    for (String s : target.keySet()) {

                        if (target.get(s) && im.hasLore()) {//是描述
                            for (String line : im.getLore()) {
                                if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                                    if (line.equals(s)) {
                                        found = true;
                                    }
                                } else if (ChatColor.stripColor(line).contains(s)) {
                                    found = true;
                                }
                            }
                        } else if (im.hasDisplayName()) {
                            if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                                if (im.getDisplayName().equals(s)) {
                                    found = true;
                                }
                            } else if (im.getDisplayName().contains(s)) {
                                found = true;
                            }

                        }
                        if (found) {
                            if (Protections.RemoveItemsMatchingName.notifyOnly()) {
                                getLog().notify(
                                        Protections.RemoveItemsMatchingName,
                                        " 触发者: " + e.getPlayer().getName() + " 物品为: " + is.getType().name()
                                );
                            } else {
                                getLog().append2(Msg.NamedItemRemovalPlayer.getValue(e.getPlayer(), is.getType().name()));
                                e.getPlayer().getInventory().removeItem(is);

                            }
                            return;
                        }
                    }
                }
            }

            if (Protections.DestroyInvalidShulkers.isEnabled()) {
                int tagSize = NBTStuff.isBadShulker(is);
                if (tagSize > 0) {
                    getLog().append2(Msg.StaffBadShulkerRemoved.getValue(e.getPlayer(), tagSize));
                    e.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }

            if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
                if (is == null || is.getEnchantments().isEmpty()) {
                    continue;
                }
                if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
                {
                    if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isn't in a checked world
                    {
                        continue;
                    }
                }
                if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) {
                    continue;
                }

                HashSet<Enchantment> replace = new HashSet<>();
                for (Enchantment en : is.getEnchantments().keySet()) {
                    if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {
                        if (SlimefunCompat.isValid(is, en)) {
                            continue;
                        }
                        if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(
                                en) == 4341)) {
                            continue;
                        }
                        if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) {
                            break;
                        }
                        if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en))) {
                            continue;
                        }
                        if (en.canEnchantItem(is)) {
                            getLog().append2(Msg.IllegalEnchantLevel.getValue(e.getPlayer(), is, en));
                        } else {
                            getLog().append2(Msg.IllegalEnchantType.getValue(e.getPlayer(), is, en));
                        }
                        replace.add(en);
                    } else {
                        if (!en.canEnchantItem(is)) {
                            if (SlimefunCompat.isValid(is, en)) {
                                continue;
                            }

                            replace.add(en);
                            getLog().append2(Msg.IllegalEnchantType.getValue(e.getPlayer(), is, en));
                        }
                    }
                }
                for (Enchantment en : replace) {
                    is.removeEnchantment(en);
                    if (en.canEnchantItem(is)) {
                        is.addEnchantment(en, en.getMaxLevel());
                    }
                }
            }

            if (Protections.RemoveOverstackedItems.isEnabled()) {
                if (!p.isOp()) {
                    if (Protections.RemoveItemTypes.isWhitelisted(is)) {
                        if (Protections.RemoveItemTypes.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemTypes,
                                    " 触发者: " + e.getPlayer().getName() + " 物品为: " + is.getType().name()
                            );
                        } else {
                            getLog().append2(Msg.ItemTypeRemovedPlayer.getValue(p, is));
                            p.getInventory().remove(is);
                        }
                    }
                }
                if (Protections.RemoveOverstackedItems.isEnabled()) {
                    if (is != null && is.getAmount() > is.getMaxStackSize()) {
                        if (!Protections.IllegalStackMode.isEnabled())  //黑名单模式且物品在黑名单中
                        {
                            if (!Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                                continue;
                            }
                            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                                is.setAmount(is.getType().getMaxStackSize());
                                fListener.getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                            } else {
                                p.getInventory().remove(is);
                                fListener.getLog().append2(Msg.IllegalStackLogin.getValue(p, is));
                            }
                            continue;
                        }
                        if (Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                            continue;
                        }
                        if (Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && e.getPlayer().hasPermission(
                                "illegalstack.overstack")) {
                            continue;
                        }
                        if (Protections.RemoveOverstackedItems.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveOverstackedItems,
                                    " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                            );
                        } else if (Protections.FixOverstackedItemInstead.isEnabled()) {
                            getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                            is.setAmount(is.getType().getMaxStackSize());
                        } else {
                            getLog().append2(Msg.IllegalStackLogin.getValue(p, is));
                            p.getInventory().remove(is);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        ItemStack is = e.getCursor();
        if (is == null || is.getType() == Material.AIR) {
            is = e.getOldCursor();
        }
        if (is == null || is.getType() == Material.AIR) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
            if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
            {
                if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isn't in a checked world
                {
                    return;
                }
            }
            if (!is.getEnchantments().isEmpty()) {
                if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) {
                    return;
                }
                HashSet<Enchantment> replace = new HashSet<>();
                for (Enchantment en : is.getEnchantments().keySet()) {
                    if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {

                        if (SlimefunCompat.isValid(is, en)) {
                            continue;
                        }
                        if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(
                                en) == 4341)) {
                            continue;
                        }

                        if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) {
                            break;
                        }
                        if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en))) {
                            continue;
                        }
                        if (en.canEnchantItem(is)) {
                            getLog().append2(Msg.IllegalEnchantLevel.getValue(p, is, en));
                        } else {
                            getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                        }
                        replace.add(en);
                    } else {
                        if (!en.canEnchantItem(is)) {
                            if (SlimefunCompat.isValid(is, en)) {
                                continue;
                            }

                            replace.add(en);
                            getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                        }
                    }
                }
                for (Enchantment en : replace) {
                    is.removeEnchantment(en);
                    if (en.canEnchantItem(is)) {
                        is.addEnchantment(en, en.getMaxLevel());
                    }
                }
            }
        }

        if (Protections.RemoveOverstackedItems.isEnabled()) {
            if (p != null) {
                if (is != null && is.getAmount() > is.getMaxStackSize()) {
                    if (Protections.AllowStack.isThirdPartyInventory(e.getView())) {
                        return;
                    }
                    if (!Protections.IllegalStackMode.isEnabled())  //黑名单模式
                    {
                        if (!Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                            return;
                        }
                        if (Protections.FixOverstackedItemInstead.isEnabled()) {
                            is.setAmount(is.getType().getMaxStackSize());
                            fListener.getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                        } else {
                            e.setCancelled(true);
                            p.getInventory().remove(is);
                            is.setAmount(1);
                            is.setType(Material.AIR);
                            fListener.getLog().append2(Msg.IllegalStackOnClick.getValue(p, is));
                        }
                        return;
                    }
                    if (Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                        return;
                    }
                    if (Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && p.hasPermission(
                            "illegalstack.overstack")) {
                        return;
                    }
                    if (Protections.RemoveOverstackedItems.notifyOnly()) {
                        getLog().notify(
                                Protections.RemoveOverstackedItems,
                                " 触发者: " + p.getName() + " 物品为: " + is.getType()
                        );
                    } else if (Protections.FixOverstackedItemInstead.isEnabled()) {
                        getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                        is.setAmount(is.getType().getMaxStackSize());
                    } else {
                        getLog().append2(Msg.IllegalStackOnClick.getValue(p, is));
                        p.getInventory().remove(is);
                    }
                }
            }
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH)
    public void onInventoryPickup(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (SpigotMethods.isNPC(e.getWhoClicked())) {
            return;
        }


        final Player p = (Player) e.getWhoClicked();


        if (p != null && Protections.DisableInWorlds.isWhitelisted(e.getWhoClicked().getWorld().getName())) {
            return;
        }

        ItemStack is = e.getCurrentItem();
        if (is != null && is.hasItemMeta() && is.getItemMeta() instanceof BookMeta) {


            BookMeta bm = (BookMeta) is.getItemMeta();
            if (bm.getAuthor() != null) {
                if (Protections.BookAuthorWhitelist.isWhitelisted(bm.getAuthor())) {

                    return;
                }
            }

            if (Protections.LimitNumberOfPages.getIntValue() > 0 && bm.getPageCount() > Protections.LimitNumberOfPages.getIntValue()) {
                getLog().append2(Msg.TooManyPages.getValue(e.getWhoClicked().getName()));
                e.getWhoClicked().getInventory().removeItem(is);

                e.setCancelled(true);
            }
            if (Protections.RemoveBooksNotMatchingCharset.isEnabled()) {
                int InvalidPages = 0;
                for (String page : bm.getPages()) {
                    if (!Charset
                            .forName(Protections.ValidCharset.getTxtValue())
                            .newEncoder()
                            .canEncode(ChatColor.stripColor(page))) {
                        InvalidPages++;
                    }
                }

                if (InvalidPages >= Protections.PageCountThreshold.getIntValue()) {
                    String author = "未知";
                    if (bm != null && bm.getAuthor() != null && bm.getAuthor().isEmpty()) {
                        author = bm.getAuthor();
                    }
                    if (Protections.RemoveBooksNotMatchingCharset.notifyOnly()) {
                        getLog().notify(Protections.RemoveBooksNotMatchingCharset, " 触发者: " + author);
                    } else {
                        getLog().append2(Msg.BookRemoved.getValue(author));
                        bm.setPages(new ArrayList<String>());
                        is.setItemMeta(bm);

                        e.getInventory().removeItem(is);
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if (Protections.RemoveItemsMatchingName.isEnabled() && p != null) {
            if (Protections.BlockEnchantingInstead.isEnabled() || Protections.BlockRepairsInstead.isEnabled()) {
                return;
            }
            if (e.isShiftClick() && e.getCurrentItem() != null) {
                if (is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {
                        if (Protections.RemoveItemsMatchingName.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemsMatchingName,
                                    " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                            );
                        } else {
                            getLog().append2(Msg.NamedItemRemovalPlayer.getValue(p, is.getType().name()));
                            e.setCancelled(true);
                            e.setResult(Result.DENY);
                            e.setCurrentItem(new ItemStack(Material.AIR));
                            return;
                        }
                    }
                }
            }
            if (e.getCursor() != null && e.getCursor().hasItemMeta()) {
                is = e.getCursor();
                if (is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (Protections.RemoveAllRenamedItems.isEnabled()) {
                        if (!p.hasPermission("IllegalStack.RenameBypass")) {
                            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                                getLog().append2(Msg.RemovedRenamedItem.getValue(p, is));
                                e.setResult(Result.DENY);
                                e.getCursor().setType(Material.AIR);
                                e.getCursor().setAmount(0);
                                e.setCursor(new ItemStack(Material.AIR, 1));
                            }
                        }

                    }
                    if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {
                        if (Protections.RemoveItemsMatchingName.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemsMatchingName,
                                    " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                            );
                        } else {
                            getLog().append2(Msg.NamedItemRemovalPlayer.getValue(p, is.getType().name()));
                            e.setCancelled(true);
                            e.setResult(Result.DENY);
                            e.getCursor().setType(Material.AIR);
                            e.getCursor().setAmount(0);
                            e.setCursor(new ItemStack(Material.AIR, 1));
                            return;
                        }
                    }
                }
            }
            for (ItemStack is2 : p.getInventory().getContents()) {
                is = is2;
                if (is == null) {
                    continue;
                }
                if (Protections.RemoveAllRenamedItems.isEnabled()) {
                    if (!p.hasPermission("IllegalStack.RenameBypass")) {
                        if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                            getLog().append2(Msg.RemovedRenamedItem.getValue(p, is));
                            e.setResult(Result.DENY);
                            e.getCursor().setType(Material.AIR);
                            e.getCursor().setAmount(0);
                            e.setCursor(new ItemStack(Material.AIR, 1));
                            final ItemStack isRemove = is;
                            Scheduler.runTaskLater(this.plugin, () -> p.getInventory().removeItem(isRemove), 12, p);

                        }
                    }
                }
                if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
                    if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
                    {
                        if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p
                                .getWorld()
                                .getName())) //isn't in a checked world
                        {
                            continue;
                        }
                    }

                    if (!is.getEnchantments().isEmpty()) {

                        if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) {
                            continue;
                        }
                        HashSet<Enchantment> replace = new HashSet<>();
                        for (Enchantment en : is.getEnchantments().keySet()) {
                            if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {

                                if (SlimefunCompat.isValid(is, en)) {
                                    continue;
                                }
                                if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is
                                        .getEnchantmentLevel(en) == 4341)) {
                                    continue;
                                }

                                if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) {
                                    break;
                                }
                                if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en))) {
                                    continue;
                                }
                                if (en.canEnchantItem(is)) {
                                    getLog().append2(Msg.IllegalEnchantLevel.getValue(p, is, en));
                                } else {
                                    getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                                }
                                replace.add(en);
                            } else {
                                if (!en.canEnchantItem(is)) {
                                    if (SlimefunCompat.isValid(is, en)) {
                                        continue;
                                    }
                                    replace.add(en);
                                    getLog().append2(Msg.IllegalEnchantType.getValue(p, is, en));
                                }
                            }
                        }
                        for (Enchantment en : replace) {
                            is.removeEnchantment(en);
                            if (en.canEnchantItem(is)) {
                                is.addEnchantment(en, en.getMaxLevel());
                            }
                        }
                    }
                }
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    boolean found = Protections.RemoveItemsMatchingName.loreNameMatch(im);
                    if (found) {
                        if (Protections.RemoveItemsMatchingName.notifyOnly()) {
                            getLog().notify(
                                    Protections.RemoveItemsMatchingName,
                                    " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                            );
                        } else {
                            getLog().append2(Msg.NamedItemRemovalPlayer.getValue(p, is.getType().name()));

                            e.setCancelled(true);
                            final ItemStack isRemove = is;
                            Scheduler.runTaskLater(this.plugin, () -> p.getInventory().removeItem(isRemove), 12, p);
                        }
                        return;
                    }
                }
            }
        }

        // 由于没有真正的方法检查这是否为自定义库存，这是阻止为根本不发生的事件（在自定义库存中与错误物品堆叠交互，不会改变库存）调用虚假日志的唯一方法
        if (Protections.RemoveOverstackedItems.isEnabled() && !e.isCancelled()) {

            if (p != null) {
                is = e.getCurrentItem();
                if (is != null && is.getAmount() > is.getMaxStackSize()) {
                    if (Protections.AllowStack.isThirdPartyInventory(e.getView())) {
                        return;
                    }
                    if (!Protections.IllegalStackMode.isEnabled()) {  //黑名单模式
                        if (!Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                            return;
                        }
                        if (Protections.FixOverstackedItemInstead.isEnabled()) {
                            is.setAmount(is.getType().getMaxStackSize());
                            fListener.getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                        } else {
                            e.setCancelled(true);
                            p.getInventory().remove(is);
                            is.setAmount(1);
                            is.setType(Material.AIR);
                            fListener.getLog().append2(Msg.IllegalStackOnClick.getValue(p, is));
                        }
                        return;
                    }
                    if (Protections.AllowStack.isWhitelisted(is.getType().name(), p)) {
                        return;
                    }
                    if (Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && p.hasPermission(
                            "illegalstack.overstack")) {
                        return;
                    }
                    if (Protections.RemoveOverstackedItems.notifyOnly()) {
                        getLog().notify(
                                Protections.RemoveOverstackedItems,
                                " 触发者: " + p.getName() + " 物品为: " + is.getType()
                        );
                    } else if (Protections.FixOverstackedItemInstead.isEnabled()) {
                        getLog().append2(Msg.IllegalStackShorten.getValue(p, is));
                        is.setAmount(is.getType().getMaxStackSize());
                    } else {
                        getLog().append2(Msg.IllegalStackOnClick.getValue(p, is));
                        p.getInventory().remove(is);
                    }
                    for (ItemStack is2 : p.getInventory().getContents()) {
                        if (is2 == null) {
                            continue;
                        }
                        if (!p.isOp()) {
                            if (Protections.RemoveItemTypes.isWhitelisted(is)) {
                                if (Protections.RemoveItemTypes.notifyOnly()) {
                                    getLog().notify(
                                            Protections.RemoveItemTypes,
                                            " 触发者: " + p.getName() + " 物品为: " + is.getType().name()
                                    );
                                } else {
                                    getLog().append2(Msg.ItemTypeRemovedPlayer.getValue(p, is));
                                    p.getInventory().remove(is);
                                }
                            }
                        }
                        if (is2 != null && is2.getAmount() > is2.getMaxStackSize()) {
                            if (!Protections.IllegalStackMode.isEnabled())  //黑名单模式且物品在黑名单中
                            {
                                if (!Protections.AllowStack.isWhitelisted(is2.getType().name(), p)) {
                                    continue;
                                }

                                if (Protections.RemoveOverstackedItems.notifyOnly()) {
                                    getLog().notify(
                                            Protections.RemoveOverstackedItems,
                                            " 触发者: " + p.getName() + " 物品为: " + is.getType()
                                    );
                                } else if (Protections.FixOverstackedItemInstead.isEnabled()) {
                                    is2.setAmount(is2.getType().getMaxStackSize());
                                    fListener.getLog().append2(Msg.IllegalStackShorten.getValue(p, is2));
                                } else {
                                    p.getInventory().remove(is2);
                                    fListener.getLog().append2(Msg.IllegalStackItemScan.getValue(p, is2));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onBookEditAttempt(PlayerInteractEvent e) {
        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.getItemWatcher().contains(e.getPlayer())) {

                ItemStack is = e.getItem();
                if (is == null || is.getType() == Material.AIR) {
                    getItemWatcher().remove(e.getPlayer());
                    e.getPlayer().sendMessage(Msg.StaffEnchantBypassCancel.getValue());
                    return;
                }

                Protections.EnchantedItemWhitelist.validate(is.serialize().toString(), e.getPlayer());
                getItemWatcher().remove(e.getPlayer());
            }
        }
        if (Protections.DisableBookWriting.isEnabled()) {
            if (Protections.BookAuthorWhitelist.isWhitelisted(e.getPlayer().getName())) {
                return;
            }
            boolean wasOffhand = false;
            if (!is18()) {
                if (e.getPlayer().getInventory().getItemInOffHand().equals(e.getItem())) {
                    wasOffhand = true;
                }
            }
            if (e.hasItem()) {
                if (e.getItem().getType() == book) {
                    if (Protections.DisableBookWriting.notifyOnly()) {
                        getLog().notify(Protections.DisableBookWriting, " 触发者: " + e.getPlayer().getName());
                    } else {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Msg.PlayerDisabledBookMsg.getValue(e.getPlayer(), ""));
                        getLog().append2(Msg.StaffMsgBookRemoved.getValue(e.getPlayer(), ""));
                        final ItemStack remove = e.getItem();
                        final Player player = e.getPlayer();
                        final boolean offhandRemove = wasOffhand;
                        Scheduler.runTaskLater(this.plugin, () -> {
                            player.getInventory().removeItem(remove);
                            if (!is18() && offhandRemove) {
                                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR, 1));
                            }
                        }, 12, player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onStringShear(BlockFromToEvent e) {
        if (Protections.DisableInWorlds.isWhitelisted(e.getBlock().getWorld().getName()) || !Protections.PreventStringDupe.isEnabled()) {
            return;
        }
        
        if(e.getToBlock().getType() == Material.TRIPWIRE)
        {
        	e.setCancelled(true);
        	e.getToBlock().setType(Material.AIR);
        	fListener.getLog().append(Msg.BlockedStringDupe.getValue(e.getBlock().getLocation(), ""), Protections.PreventStringDupe);
        }
    }


    @EventHandler
    public void onFish2(PlayerInteractEvent e) {

        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }

        if (Protections.KickForAutoClickerFishing.isEnabled() || Protections.WatchForAutoFishMod.isEnabled()) {
            if (e.hasBlock()) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player p = e.getPlayer();
                    boolean fishing = false;
                    if (is18()) {
                        if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.FISHING_ROD) {
                            fishing = true;
                        }
                    } else {
                        if (!is18() && p.getInventory().getItemInOffHand() != null && p
                                .getInventory()
                                .getItemInOffHand()
                                .getType() == Material.FISHING_ROD) {
                            fishing = true;
                        } else if (p.getInventory().getItemInMainHand() != null && p
                                .getInventory()
                                .getItemInMainHand()
                                .getType() == Material.FISHING_ROD) {
                            fishing = true;
                        }
                    }
                    if (fishing) {
                        FishAttempt fa = FishAttempt.findPlayer(p);
                        fa.addAttempt();
                        if (Protections.WatchForAutoFishMod.isEnabled() && Protections.WarnPlayerThenKickInsteadOfNotify.isEnabled()) {
                            FishHook hook = FishAttempt.findHook(p);
                            if (hook != null && fa.isBlackListedSpot(hook.getLocation())) {
                                if (Protections.WatchForAutoFishMod.notifyOnly()) {
                                    hook.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        if (Protections.DisableInWorlds.isWhitelisted(e.getPlayer().getWorld().getName())) {
            return;
        }
        if (Protections.KickForAutoClickerFishing.isEnabled() || Protections.WatchForAutoFishMod.isEnabled()) {
            FishAttempt fa = FishAttempt.findPlayer(e.getPlayer());
            FishHook hook = null;
            if (Protections.WatchForAutoFishMod.isEnabled()) {
                if (!is114() || !is115()) {
                    hook = FishAttempt.findHook(e.getPlayer());
                } else {
                    hook = e.getHook();
                }
                if (hook == null) {
                    return;
                }
                if (fa.isBlackListedSpot(hook.getLocation())) {
                    if (Protections.KickForAutoClickerFishing.notifyOnly()) {
                        getLog().notify(Protections.KickForAutoClickerFishing, " 触发者: " + e.getPlayer().getName());
                    } else {
                        e.setCancelled(true);
                        hook.remove();
                        e.setExpToDrop(0);
                    }
                    return;
                }
                if (e.getCaught() != null) {
                    fa.fishCaught(hook.getLocation());
                    //	e.getPlayer().sendMessage("鱼在此处被钓到: " + fa.getSameSpotCount());
                }
            }
            boolean kickMode = false;
            int threshold = Protections.MaxFishToNotifyStaffThenBlock.getIntValue();
            if (Protections.WarnPlayerThenKickInsteadOfNotify.isEnabled()) {
                kickMode = true;
                threshold = Protections.MaxFishAllowedBeforeKick.getIntValue();
            }
            if (threshold == 1) {
                threshold = 2;
            }
            boolean sameSpot = fa.getSameSpotCount() >= threshold - 1;
            boolean spamming = fa.getCount() > 10;
            boolean shouldKick = false;
            String message = Msg.PlayerKickMsgFishing.getValue();
            if (sameSpot && Protections.WatchForAutoFishMod.isEnabled()) {
                if (kickMode) {
                    if (fa.getSameSpotCount() >= threshold) {
                        message = Msg.PlayerKickMsgFishMod.getValue();
                        LOGGER.info(
                                " - 因涉嫌使用钓鱼模组踢出 {}，上次尝试已警告。",
                                e.getPlayer().getName()
                        );
                        shouldKick = true;
                        fa.reset();
                    } else {
                        if (e.getCaught() != null) {
                            if (!Protections.KickForAutoClickerFishing.notifyOnly()) {
                                e.getPlayer().sendMessage(Msg.PlayerKickMsgFishMod.getValue());
                            }
                        }

                    }
                } else {
                    if (Protections.WatchForAutoFishMod.notifyOnly()) {
                        getLog().notify(Protections.WatchForAutoFishMod, " 触发者: " + e.getPlayer().getName());
                    } else {
                        getLog().append2(Msg.StaffAutoFishingNotice.getValue(
                                e.getPlayer(),
                                fa.getSameSpotCount(),
                                e.getPlayer().getLocation()
                        ));
                        //getLog().append2(e.getPlayer().getName() + " 似乎在使用自动钓鱼模组.. 在彼此0.3格内钓到 " + fa.getSameSpotCount() + " 次 @" + e.getPlayer().getLocation());
                        e.setCancelled(true);
                        e.setExpToDrop(0);
                        if (hook != null) {
                            hook.remove();
                            fa.blackList(hook.getLocation());
                        }
                    }
                    fa.reset();
                }
            }
            if (spamming && Protections.KickForAutoClickerFishing.isEnabled()) {
                if (Protections.KickForAutoClickerFishing.notifyOnly()) {
                    getLog().notify(Protections.KickForAutoClickerFishing, " 触发者: " + e.getPlayer().getName());
                } else {
                    getLog().append2(Msg.StaffSpamFishingNotice.getValue(
                            e.getPlayer(),
                            fa.getCount(),
                            e.getPlayer().getLocation()
                    ));
                    e.setCancelled(true);
                    e.setExpToDrop(0);
                    shouldKick = true;
                }
                fa.setCount(0);
            }
            final String msg = message;
            if (shouldKick) {
                Scheduler.runTaskLater(this.plugin, () -> e.getPlayer().kickPlayer(msg), 1, e.getPlayer());
            }
        }
    }

    @EventHandler// (ignoreCancelled = false, priority=EventPriority.LOWEST)
    public void onTNTPrime(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Item) {
            if (RemoveItemTypesCheck.shouldRemove(((Item) e.getEntity()).getItemStack(), null)) {
                e.setCancelled(true);
            }
        }

        if (!(e.getEntity() instanceof TNTPrimed)) {
            return;
        }
        if (Protections.DisableInWorlds.isWhitelisted(e.getEntity().getWorld().getName())) {
            return;
        }
        Block bLoc = e.getEntity().getLocation().getBlock();
        HashSet<Block> expired = new HashSet<>();
        for (Block b : movedTNT.keySet()) {
            if (b.equals(bLoc)) {
                if (b.isBlockIndirectlyPowered()) {
                    e.setCancelled(true);
                }
            }
            if (System.currentTimeMillis() >= movedTNT.get(b)) {
                expired.add(b);
            }
        }
        for (Block b : expired) {
            movedTNT.remove(b);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {

        if (Protections.DisableChestsOnMobs.isEnabled() && !IllegalStack.hasProtocolLib()) {
            if (e.getRightClicked() != null) {
                ItemStack is = null;
                if (is18()) {
                    is = e.getPlayer().getItemInHand();
                } else {
                    is = e.getPlayer().getInventory().getItemInOffHand();
                    if (is == null || is.getType() != Material.CHEST) {
                        is = e.getPlayer().getInventory().getItemInMainHand();
                    }
                }
                if (is == null || is.getType() != Material.CHEST) {
                    return;
                }

                if (!IllegalStack.hasChestedAnimals()) {
                    if (e.getRightClicked() instanceof Horse) {
                        final Horse horse = (Horse) e.getRightClicked();

                        if (horse.isCarryingChest()) {
                            horse.setCarryingChest(false);
                            e.getPlayer().sendMessage(Msg.PlayerDisabledHorseChestMsg.getValue());
                            getLog().append2(Msg.ChestRemoved.getValue(e.getPlayer(), horse));
                            LOGGER.warn(
                                    "此服务器上未找到 ProtocolLib，且 DisableChestsOnMobs 防护已开启.. 作弊客户端玩家仍有可能使用马/驴在您的服务器上进行复制。强烈建议您安装 ProtocolLib 以获得最佳防护！");
                            punishPlayer(e.getPlayer(), e.getRightClicked());
                        } else {
                            e.setCancelled(true);
                            Scheduler.runTaskLater(this.plugin, () -> {
                                e.getPlayer().sendMessage(Msg.PlayerDisabledHorseChestMsg.getValue());
                                getLog().append2(Msg.ChestPrevented.getValue(e.getPlayer(), horse));
                                horse.setCarryingChest(false);
                                punishPlayer(e.getPlayer(), e.getRightClicked());
                            }, 2, e.getPlayer());
                        }
                    }
                } else if (e.getRightClicked() instanceof ChestedHorse) {

                    ChestedHorse horse = (ChestedHorse) e.getRightClicked();
                    horse.setCarryingChest(false);
                    e.setCancelled(true);

                    Scheduler.runTaskLater(this.plugin, () -> {
                        e.getPlayer().sendMessage(Msg.PlayerDisabledHorseChestMsg.getValue());
                        getLog().append2(Msg.ChestPrevented.getValue(e.getPlayer(), horse));
                        horse.setCarryingChest(false);
                        LOGGER.warn(
                                "此服务器上未找到 ProtocolLib，且 DisableChestsOnMobs 防护已开启.. 作弊客户端玩家仍有可能使用马/驴在您的服务器上进行复制。强烈建议您安装 ProtocolLib 以获得最佳防护！");
                        punishPlayer(e.getPlayer(), e.getRightClicked());
                    }, 2, e.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void NetherCeilingExploitTPCheck(PlayerTeleportEvent e) {
        if (Protections.PreventPearlGlassPhasing.isEnabled()) {
            Scheduler.runTaskLater(this.plugin, () -> {
                if (getTeleGlitch().containsKey(e.getPlayer().getUniqueId())) {
                    Location corrected = getTeleGlitch().get(e.getPlayer().getUniqueId());
                    corrected.setDirection(e.getTo().getDirection());
                    if (Scheduler.FOLIA) {
                        e.getPlayer().teleportAsync(corrected);
                    } else {
                        e.getPlayer().teleport(corrected);
                    }
                    getTeleGlitch().remove(e.getPlayer().getUniqueId());
                    if (Protections.TeleportCorrectionNotify.isEnabled()) {
                        getLog().append2(Msg.CorrectedPlayerLocation.getValue(e.getPlayer(), corrected));
                    }
                }
            }, 5, e.getPlayer());
        }
        if (Protections.BlockPlayersAboveNether.isEnabled() && !Protections.DamagePlayersAboveNether.isEnabled()) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getTo().getWorld().getName())) {
                return;
            }
            if ((!e.getFrom().getWorld().getName().toLowerCase().contains("nether") && e
                    .getFrom()
                    .getWorld()
                    .getEnvironment() != Environment.NETHER)) {
                return;
            }
            Location l = e.getTo();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (!e.getPlayer().isOp() && (l.getWorld().getName().toLowerCase().contains("nether") || l
                        .getWorld()
                        .getEnvironment() == Environment.NETHER)) {
                    e.setCancelled(true);
                    getLog().append2(Msg.StaffMsgNetherBlock.getValue(e.getPlayer(), l.toString()));
                    e.getPlayer().sendMessage(Msg.PlayerNetherBlock.getValue(e.getPlayer().getName()));
                }
            }
        }
    }

    @EventHandler
    public void VehicleEnterEvent(VehicleEnterEvent e) {
        if (Protections.BlockPlayersAboveNether.isEnabled() && e.getEntered() instanceof Player) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getVehicle().getWorld().getName())) {
                return;
            }
            Location l = e.getVehicle().getLocation();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                    if (l.getBlockY() >= Protections.NetherYLevel.getIntValue() && (l.getWorld().getName().toLowerCase().contains(
                            "nether") || l.getWorld().getEnvironment() == Environment.NETHER)) //already on top of the nether..
                    {
                        e.setCancelled(true);
                        e.getVehicle().remove();
                        getLog().append2(Msg.StaffMsgNetherCart.getValue((Player) e.getEntered(), l.toString()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent e) {
        if (Protections.BlockPlayersAboveNether.isEnabled() && e.getExited() instanceof Player && !Protections.DamagePlayersAboveNether.isEnabled()) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getVehicle().getWorld().getName())) {
                return;
            }
            Location l = e.getVehicle().getLocation();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                    if (l.getBlockY() >= Protections.NetherYLevel.getIntValue() && (l.getWorld().getName().toLowerCase().contains(
                            "nether") || l.getWorld().getEnvironment() == Environment.NETHER)) //already on top of the nether..
                    {
                        e.setCancelled(true);
                        e.getVehicle().remove();
                        getLog().append2(Msg.StaffMsgNetherCart.getValue((Player) e.getExited(), l.toString()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void NetherCeilingMovementCheck(PlayerMoveEvent e) {
    	
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || 
        		e.getFrom().getBlockY() != e.getTo().getBlockY() || 
        		e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
        	
        
                if(e.getPlayer().isOp()) 
                	return;
               
                
               
            if (Protections.KillPlayersBelowNether.isEnabled() &&
                    (e.getPlayer().isFlying() || (IllegalStack.hasElytra() && e.getPlayer().isGliding()))) {

                if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getTo().getWorld().getName())) {
                    return;
                }
                Location l = e.getTo();
                if (l.getY() < 0) {
                    if (l.getWorld().getName().toLowerCase().contains("nether") || l
                            .getWorld()
                            .getEnvironment() == Environment.NETHER) { //already on top of the nether..
                        e.getPlayer().setFlying(false);

                        if (IllegalStack.hasElytra()) {
                            e.getPlayer().setGliding(false);
                        }

                        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
                            e.getPlayer().setGameMode(GameMode.SURVIVAL);
                        }

                        if (e.getPlayer().isInvulnerable()) {
                            e.getPlayer().setInvulnerable(false);
                        }

                        e.setCancelled(true);
                        getLog().append2(Msg.StaffMsgUnderNether.getValue(e.getPlayer(), e.getPlayer().getLocation().toString()));
                        Scheduler.runTaskLater(this.plugin, () -> e.getPlayer().damage(9999), 12, e.getPlayer());
                        return;

                    }
                }
            }
        

        if (!Protections.DamagePlayersAboveNether.isEnabled() && Protections.BlockPlayersAboveNether.isEnabled()) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getTo().getWorld().getName())) {
                return;
            }
            
            Location l = e.getTo();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (e.getFrom().getBlockY() >= Protections.NetherYLevel.getIntValue() && (l
                        .getWorld()
                        .getName()
                        .toLowerCase()
                        .contains("nether") || l
                        .getWorld()
                        .getEnvironment() == Environment.NETHER)) { //already on top of the nether..
                	
                	if(e.getPlayer().hasPermission("illegalstack.notify"))
                		return;
                	
                    e.setCancelled(true);
                    if (Protections.EnsureSafeTeleportLocationIfAboveCeiling.isEnabled()) {
                        int x = e.getFrom().getBlockX();
                        int z = e.getFrom().getBlockZ();
                        BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};
                        for (int y = Protections.NetherYLevel.getIntValue(); y > (Protections.NetherYLevel.getIntValue() - 17); y--) {
                            Block b = e.getFrom().getWorld().getBlockAt(x, y, z);
                            if (b.getType() != Material.BEDROCK) {
                                b.setType(Material.AIR);
                                for (BlockFace face : faces) {
                                    if (b.getRelative(face).getType() != Material.BEDROCK) {
                                        b.getRelative(face).setType(Material.NETHERRACK);
                                    }
                                }
                                b = b.getRelative(BlockFace.DOWN);
                                if (b.getType() == Material.BEDROCK) {
                                    continue;
                                }
                                b.setType(Material.AIR);
                                for (BlockFace face : faces) {
                                    if (b.getRelative(face).getType() != Material.BEDROCK) {
                                        b.getRelative(face).setType(Material.NETHERRACK);
                                    }
                                }
                                if (b.getRelative(BlockFace.DOWN).getType() != Material.BEDROCK) {
                                    b.getRelative(BlockFace.DOWN).setType(Material.NETHERRACK);
                                }
                                Location loc = b.getLocation();
                                getLog().append2(Msg.StaffMsgNetherFix.getValue(e.getPlayer(), loc.toString()));
                                e.setCancelled(true);

                                Scheduler.runTaskLater(this.plugin, () -> {
                                    if (Scheduler.FOLIA) {
                                        e.getPlayer().teleportAsync(loc);
                                    } else {
                                        e.getPlayer().teleport(loc);
                                    }
                                }, 12, e.getPlayer());
                                return;
                            }
                        }
                    } else {
                        if (Scheduler.FOLIA) {
                            e.getPlayer().teleportAsync(e.getPlayer().getLocation().subtract(0, 3, 0));
                        } else {
                            e.getPlayer().teleport(e.getPlayer().getLocation().subtract(0, 3, 0));
                        }
                    }
                    e.setCancelled(true);
                    getLog().append2(Msg.StaffMsgNetherBlock.getValue(e.getPlayer(), l.toString()));
                    e.getPlayer().sendMessage(Msg.PlayerNetherBlock.getValue(e.getPlayer().getName()));
                }
            }
        }
        }
    }

    @EventHandler
    public void onSpawnerMine(BlockBreakEvent e) {

        if (Protections.PreventBedrockDestruction.isEnabled()) {
            if (e.getBlock().getType() == Material.OBSIDIAN && e.getBlock().getWorld().getEnvironment() == Environment.THE_END) {
                Location bLoc = e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5); //方块中心
                for (Entity ent : e.getBlock().getWorld().getNearbyEntities(bLoc, 0.1, 3.1, 0.1)) {
                    if (ent instanceof EnderCrystal) {
                        ent.remove();
                    }
                }
            }
        }
        if ((Protections.BlockBuildingAboveNether.isEnabled() || Protections.BlockPlayersAboveNether.isEnabled()) && !e
                .getPlayer()
                .isOp()) {
            if (Protections.ExcludeNetherWorldFromHeightCheck.getTxtSet().contains(e.getPlayer().getWorld().getName())) {
                return;
            }
            Location l = e.getBlock().getLocation();
            if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                if (l.getY() >= Protections.NetherYLevel.getIntValue()) {
                    if (l.getBlockY() >= Protections.NetherYLevel.getIntValue() && (l.getWorld().getName().toLowerCase().contains(
                            "nether") || l.getWorld().getEnvironment() == Environment.NETHER)) //already on top of the nether..
                    {
                        e.setCancelled(IllegalStackAction.isCompleted(
                                Protections.BlockPlayersAboveNether,
                                e.getPlayer(),
                                e.getBlock()
                        ));
                    }
                }
            }
        }

        
        if (Protections.ResetSpawnersOfType.getTxtSet().isEmpty()) {
            return;
        }
        if (e.getBlock().getType() == Material.SPAWNER) {
            BlockState bs = e.getBlock().getState();
            CreatureSpawner spawner = (CreatureSpawner) bs;
            if (Protections.ResetSpawnersOfType.isWhitelisted(spawner.getSpawnedType().name())) {
                EntityType oldType = spawner.getSpawnedType();
                spawner.setSpawnedType(EntityType.PIG);
                bs.setBlockData(spawner.getBlockData());
                bs.update(true);
                getLog().append2(Msg.StaffMsgSpawnerReset.getValue(e.getPlayer(), oldType));
            }
        }
    }

    @EventHandler
    public void onEnchantItemPrep(PrepareItemEnchantEvent e) {
        //String debug = "修改的附魔选项: ";

        if (!Protections.PreventRNGEnchant.isEnabled()) {
            return;
        }
        EnchantmentOffer[] newOffers = new EnchantmentOffer[e.getOffers().length];
        for (int i = 0; i < e.getOffers().length; i++) {
            if (e.getOffers().length > 1) {
                if (ThreadLocalRandom.current().nextBoolean()) //随机跳过部分多个选项
                {
                    continue;
                }
            }
            EnchantmentOffer eo = e.getOffers()[i];
            if (eo == null) {
                continue;
            }
            int min = eo.getCost() - ThreadLocalRandom.current().nextInt(1, 3);
            if (min <= 0) {
                min = 1 + ThreadLocalRandom.current().nextInt(eo.getCost() + 1, eo.getCost() + 3);
            }
            int oldCost = eo.getCost();
            eo.setCost(ThreadLocalRandom.current().nextInt(min, min + 5));
            newOffers[i] = eo;
            //debug = debug + " [" + i + "] 新: " + eo.getCost() + " 旧: " + oldCost;
        }
        //fListener.getLog().append2("DEBUG: 修改了 " + modified + " 个附魔选项。 " + debug);
        for (int i = 0; i < newOffers.length; i++) {
            if (newOffers[i] != null) {
                e.getOffers()[i] = newOffers[i];
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        if (!Protections.PreventRNGEnchant.isEnabled()) {
            return;
        }
        Player p = e.getEnchanter();
        int rndXp = ThreadLocalRandom.current().nextInt(-6, 5);
        float oldXp = p.getExp();
        p.giveExp(rndXp);
        String enchants = "";
        for (Enchantment enc : e.getEnchantsToAdd().keySet()) {
            if (enc == null) {
                continue;
            }
            enchants = enc.getName() + " - " + e.getEnchantsToAdd().get(enc) + ", ";
            //fListener.getLog().append2("DEBUG: 附魔随机经验为: " + rndXp + " 旧经验: " + oldXp + " 新经验 " + p.getExp()  + " " + p.getName() + " 物品 " + e.getItem().getType().name() + " 添加了 " + enchants);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
    	if(Protections.PreventVexTrapping.isEnabled(e.getEntity().getLocation())) {
    		if(e.getEntity() instanceof Vex) {
    			
    			Vex v = ((Vex)e.getEntity());
    			if(v.isInsideVehicle()) {
    				v.getVehicle().removePassenger(e.getEntity());
    	            getLog().append(Msg.VexEjected.getValue(v.getLocation().toString()), Protections.PreventVexTrapping);
    			}
    		}
    	}

    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        if (Protections.DisableCraftingRecipes.getTxtSet().isEmpty()){
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)){
            return;
        }

        Player player = ((Player) event.getWhoClicked());
        ItemStack craftResult = event.getRecipe().getResult();
        HashSet<String> disabledMaterials = Protections.DisableCraftingRecipes.getTxtSet();
        HashSet<ItemStack> itemStacks = new HashSet<>();
        for (String s : disabledMaterials){
            Material material = Material.getMaterial(s);
            if (material != null){
                ItemStack itemStack = new ItemStack(material);
                itemStacks.add(itemStack);
            }
        }

        for (ItemStack itemStack : itemStacks){
            if (craftResult.equals(itemStack)){
                event.setCancelled(IllegalStackAction.isCompleted(Protections.DisableCraftingRecipes, player));
                getLog().append(Msg.PreventedItemCraft.getValue(player, craftResult.getType().name()), Protections.DisableCraftingRecipes);
                player.sendMessage(Msg.PlayerItemCraftPrevented.getValue(player.getName()));
            }
        }
    }

    public Boolean is113() {
        return is113;
    }

    public void setIs113(Boolean is113) {
        this.is113 = is113;
    }

    public void setIs18(Boolean is18) {
        fListener.is18 = is18;
    }

    public Boolean is114() {
        return getIs114();
    }

    public void setIs19(Boolean is19) {
        this.is19 = is19;
    }

    public void setIs110(Boolean is110) {
        this.is110 = is110;
    }

    public Boolean getIs112() {
        return is112;
    }

    public void setIs112(Boolean is112) {
        this.is112 = is112;
    }

    public void setIs1142(Boolean is1142) {
        this.is1142 = is1142;
    }

    public void setIs1143(Boolean is1143) {
        this.is1143 = is1143;
    }

    public HashSet<Player> getItemWatcher() {
        return itemWatcher;
    }

    public void setIs1144(Boolean is1144) {
        this.is1144 = is1144;
    }

    public HashMap<Player, Long> getSwapDelay() {
        return swapDelay;
    }

    public Boolean is115() {
        return is115;
    }

    public void is115(Boolean is115) {
        this.is115 = is115;
    }

    public Set<Material> getGlassBlocks() {
        return glassBlocks;
    }

    public HashMap<UUID, Location> getTeleGlitch() {
        return teleGlitch;
    }

    public void setTeleGlitch(HashMap<UUID, Location> teleGlitch) {
        this.teleGlitch = teleGlitch;
    }

    public Boolean getIs116() {
        return is116;
    }

    public void setIs116(Boolean is116) {
        this.is116 = is116;
    }

    public boolean isAtLeast113() {
        return Material.matchMaterial("CAVE_AIR") != null;
    }

    public boolean isAtLeast114() {
        return Material.matchMaterial("COMPOSTER") != null;
    }

    public boolean isIs117() {
        return is117;
    }

    public void setIs117(boolean is117) {
        this.is117 = is117;
    }
    public void setIs118(boolean is118) {
        this.is118 = is118;
    }
	public static HashSet<Material> getUnbreakable() {
		return unbreakable;
	}

}
