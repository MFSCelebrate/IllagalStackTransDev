package main.java.me.dniym.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum Msg {

    //@  违规地点
    //~name~ 玩家或实体的名称
    //~contents~ 物品展示框的内容
    //~removedblocks~ 移除的方块
    //~author~    书作者
    //~blockType  方块类型
    //~item~

    PluginPrefix("[非法堆叠] -"),
    PluginTeleportText(ChatColor.GOLD + "点击传送至此"),

    ChestRemoved("从 ~entity~ 身上移除了箱子 @"),
    ChestPrevented("阻止了 ~player~ 将箱子放在 ~entity~ 身上 @"),
    StaffChestPunishment(
            "惩罚了 ~player~，因其持续尝试将箱子放在生物身上。已移除一个 ~entity~，清空该玩家背包并将其踢出服务器。 @"),
    BookRemoved("发现一本包含不符合指定字符集字符的书，已将其移除。作者是：~author~"),
    TooManyPages("发现一本书页过多的书，已从 ~name~ 的背包中移除"),
    SignRemoved("在世界中发现一个不符合允许字符集的告示牌 @"),
    SignRemovedOnPlace(
            "检测到 ~name~ 放置的告示牌包含不允许的字符集中的字符。可能是复制机漏洞！ @"),
    SignKickPlayerMsg("不允许放置包含 Unicode 字符的告示牌。"),
    ShulkerClick("从 ~name~ 点击的潜影盒中移除了另一个潜影盒"),
    ShulkerPlace("从玩家 ~player~ 身上移除了一个包含非法堆叠或附魔物品的潜影盒 @"),

    ItemFrameRemoveOnExtend("移除了一个在活塞收回时被拉回、包含 ~contents~ 的物品展示框"),
    ItemFrameRemoveOnRetract("移除了一个在活塞伸出时被推回、包含 ~contents~ 的物品展示框"),
    PistonRetractionDupe("阻止了铁轨/地毯复制漏洞并移除了活塞 @ ~removedblocks~"),
    NetherPortalBlock("阻止了一个实体进入或离开下界：~name~ @"),
    EndPortalBlock("阻止了一个实体进入或离开末地：~name~ @"),
    MinecartGlitch1("定位并移除了一个卡在另一个方块内的矿车：~blocktype~ @"),
    MinecartGlitch2("阻止了一个矿车卡进方块 @"),
    HeadInsideSolidBlock("阻止了 ~player~ 的头在乘坐 ~vehicle~ 时卡入固体方块 @"),
    HeadInsideSolidBlock2("阻止了 ~player~ 的头卡入 ~block~ 原因：潜在的X光透视漏洞"),
    MinecartMount("阻止了 ~entity~ 骑上 ~vehicle~ @"),
    ZeroTickGlitch("阻止了零刻作物生长漏洞并移除了 (~removedblocks~) @"),
    NamedItemRemovalHopper("在漏斗中发现名为 ~item~ 的物品，已移除 @"),
    NamedItemRemovalPlayer("在 ~name~ 的背包中发现名为 ~item~ 的物品，已移除 @"),
    ItemTypeRemovedPlayer("在 ~name~ 的背包中发现被列入黑名单的物品类型：~item~，已移除"),
    ItemTypeRemovedPlayerOnPlace("~name~ 尝试放置被列入黑名单的物品类型：~item~，已移除"),
    ItemTypeRemovedPlayerOnDrop("~name~ 尝试丢弃被列入黑名单的物品类型：~item~，已移除"),
    ItemTypeRemoved("在 ~player~ 的背包中发现被列入黑名单的物品类型：~item~，已移除"),
    VexEjected("发现一个被困在船里的恼鬼，已将其释放 @"),
    SilkTouchBookBlocked("阻止了 ~name~ 使用精准采集附魔书破坏 ~block~ @"),

    PistonHeadRemoval("一个活塞头被炸毁.. 正在移除一个无基座的活塞 @"),
    IllegalStackLogin("登录时从玩家 ~name~ 身上移除了非法堆叠物品：~item~ (~amount~)"),
    IllegalStackOffhand("从 ~name~ 的副手移除了非法堆叠物品：~item~ (~amount~)"),
    IllegalStackPlayerBody("物品扫描时从 ~name~ 身上移除了非法堆叠物品：~item~ (~amount~)"),
    IllegalStackItemScan("物品扫描时从玩家 ~name~ 身上移除了非法堆叠物品：~item~ (~amount~)"),
    IllegalStackOnClick("点击背包时从玩家 ~name~ 身上移除了非法堆叠物品：~item~ (~amount~)"),
    IllegalStackShorten("修正了玩家 ~name~ 触发的非法堆叠物品：~item~ (~amount~)"),
    IllegalStackDurability("修正了玩家 ~name~ 触发的物品耐久度：~item~"),
    IllegalStackUnstack(
            "将玩家 ~name~ 触发的非法堆叠物品拆分为正常堆叠：~item~ (~amount~)。有 ~lost~ 个物品因无法容纳而丢失。"),
    InvalidPotionRemoved("从 ~name~ 身上移除了无效药水，该药水具有以下效果：~effects~"),
    InvalidThrownPotionRemoved("移除了 ~name~ 投掷的无效药水，该药水具有以下效果：~effects~"),
    
    UnbreakableItemCleared("从玩家 ~name~ 身上的物品 ~item~ 移除了无法破坏标志"),
    CustomAttribsRemoved("移除了 ~name~ 手持物品 ~item~ 上的自定义属性 (~attributes~)"),
    CustomAttribsRemoved2("移除了 ~name~ 装备的物品 ~item~ 上的自定义属性 (~attributes~)"),
    CustomAttribsRemoved3("移除了 ~name~ 背包中的物品 ~item~ 上的自定义属性 (~attributes~)"),
    GlideActivateMaxBuild("阻止了 ~name~ 在超过建筑高度上限时激活鞘翅 @"),
    GlideAboveMaxBuild("玩家 ~name~ 在超过建筑高度上限时使用鞘翅，已禁用滑翔 @"),
    CorrectedPlayerLocation("(可能是珍珠卡入方块的漏洞) 修正了 ~player~ 的末影珍珠传送位置 @"),
    StoppedPushableArmorStand("阻止了盔甲架被活塞垂直抬起 @"),
    StoppedPushableEntity("阻止了一个实体（盔甲架/末地水晶）被推入另一个实体 @"),
    RemovedRenamedItem("从 ~name~ 的背包中移除了一个重命名的物品 ~item~"),
    BlockedTripwireDupe(
            "玩家 ~name~ 尝试在活板门上放置绊线钩，已移除 (PreventTripwireDupe = true)"),
    BlockedStringDupe(
            "移除了一个会被水破坏的线.. 可能是线复制漏洞 @"),
    
    GenericItemRemoval("物品 ~item~ 被防护 ~protection~ 移除，来源 ~source~"),
    PlayerTrappedPortalMsg(
            "&c抱歉 ~name~，该传送门似乎没有有效的出口！如果您通过它将会被困住！"),
    PlayerCommandSleepMsg("&c抱歉，睡觉时所有命令都被禁用！"),
    PlayerDisabledBookMsg("&c抱歉，此服务器禁用了玩家编辑书！"),
    PlayerKickMsgFishing("&c此服务器不允许使用自动点击钓鱼！"),
    PlayerKickMsgFishMod(
            "&c注意！您似乎正在使用自动钓鱼模组... 请更换钓鱼位置以免被踢！"),
    PlayerSwimExploitMsg("&c那个村民现在正忙着游泳，无法与您交易！"),
    PlayerCMIShulkerNest("&c抱歉，您不能将潜影盒放入另一个潜影盒！"),
    PlayerDisabledHorseChestMsg("&c抱歉，马、羊驼、骡子等生物上的箱子已被禁用！"),
    PlayerDisabledRidingChestedMsg("&c抱歉，驯服能携带箱子的生物已被禁用！"),
    PlayerKickForChestMsg("&c您已被警告过关于生物身上的箱子，请停止尝试。"),

    PlayerNearbyNotification("检测到附近的漏洞 ~prot~"),
    PlayerNetherBlock("&c抱歉 ~name~，玩家不允许在下界顶部！"),
    PlayerEnchantBlocked("&c抱歉 ~name~，不允许附魔此物品。"),
    PlayerRepairBlocked("&c抱歉 ~name~，不允许修复此物品。"),
    PlayerSpawnEggBlock("&c抱歉，您不能使用刷怪蛋更改刷怪笼类型！"),
    PlayerItemCraftPrevented("&c抱歉 ~name~，您不能合成该物品！"),

    StaffMsgChangedSpawnerType("玩家 ~player~ 使用 ~type~ 更改了刷怪笼类型 @"),
    StaffMsgEndGatewayVehicleRemoved("玩家 ~name~ 试图带着 ~vehicle~ 通过末地折跃门，已移除该实体。"),
    StaffMsgBlockedPortalLogin("破坏了一个被困住的下界传送门 @"),
    StaffMsgBlockedPortal("阻止了 ~player~ 通过一个被阻挡/被困住的下界传送门 @"),
    StaffMsgDropperExploit("检测到漏斗/投掷器循环漏洞，移除了一个发射器/投掷器 @"),
    StaffMsgDispenerFlint("检测到一个朝下的发射器试图发射打火石，这可能导致服务器崩溃 @"),
    StaffMsgSpawnerReset("~name~ 破坏时，一个 ~type~ 刷怪笼被重置为猪刷怪笼 @"),
    StaffMsgSpawnerOnSpawnReset("一个 ~type~ 刷怪笼在尝试生成生物时被重置为猪刷怪笼 @"),
    StaffMsgCreativeBlock("阻止了 ~name~ 通过创造模式保存的工具栏加载非法物品。"),
    StaffMsgNetherBlock("阻止了 ~name~ 进入下界顶部 @"),
    StaffMsgNetherFix("已将 ~name~ 从下界天花板上方传送下来 @"),
    StaffMsgUnderNether("~name~ 因在下界底层下方飞行而被击杀 @"),
    StaffMsgNetherCart("阻止了 ~name~ 骑乘载具在下界天花板上方移动 @"),
    StaffMsgBookRemoved("从玩家 ~name~ 处移除了一个可写书，因为玩家创建书籍已被禁用！"),
    StaffProtectionToggleMsg("防护 ~protection~ 已被 ~name~ ~status~"),
    StaffInvalidProtectionMsg("&c您必须提供一个有效的防护名称来添加值："),
    StaffOptionUpdated("&a选项更新成功！"),
    StaffSingleWordsOnly("&c该防护不支持多词参数！"),
    StaffStringUpdated("&a值已更新为字符串：~value~"),
    StaffEnchantBypass(
            "&c请用主手持要添加到附魔物品白名单的物品左键点击，空手左键点击或使用 /istack cancel 取消。"),
    StaffEnchantBypassCancel("&c附魔物品白名单添加物品模式已禁用。"),
    StaffEnchantBypassAdded("&a已将 ~itemdata~ 添加到附魔物品白名单！"),
    StaffSpamFishingNotice("&a~player~ 正在疯狂钓鱼！连续 ~casts~ 次抛竿未间隔2秒！ @"),
    StaffAutoFishingNotice("&a~player~ 似乎在使用自动钓鱼模组.. 在彼此0.3格范围内钓到了 ~count~ 次 @"),
    StaffBadShulkerRemoved("从 ~name~ 处移除了一个包含过多物品（~size~）的被黑潜影盒 @"),
    StaffBadShulkerInWorld("从世界中移除了一个掉落的、包含过多物品（~size~）的被黑潜影盒 @"),
    StaffNoItem("您必须在主手持有物品才能强制修复其附魔！"),
    StaffNoEnchants("此物品没有需要修复的附魔！"),
    StaffEnchantFixed("已纠正 ~item~ 上的 ~amount~ 个附魔"),
    StaffNoNBTAPI(
            "您的服务器上未找到 NBT-Api，但由于 ~prot~ 已启用，需要使用它！请从以下地址下载并安装：https://www.spigotmc.org/resources/nbt-api.7939/"),
    StaffEnchantNotFixed("非法堆叠插件未检测到此物品上有任何无效附魔。"),
    StaffEndPortalProtected("阻止了使用发射器破坏末地传送门 @"),
    StaffMsgNoPerm("您没有使用该非法堆叠功能的权限，所需权限节点：~perm~"),
    StaffMsgBedExplosion("阻止了床被用作爆炸物 @"),
    DestroyedEnchantedItem("销毁了玩家 ~player~ 身上的非法附魔物品 ~item~ (~enchant~ 等级 ~lvl)"),
    IllegalEnchantLevel("&a修正了玩家 ~player~ 身上物品 ~item~ 的附魔等级 ~enchant~ (原等级 ~lvl~)"),
    IllegalEnchantType(
            "&a无法修正玩家 ~player~ 身上物品 ~item~ 的附魔 ~enchant~ (等级 ~lvl~)，该附魔对此物品类型无效！"),
    PreventedItemCraft("根据 DisableCraftingRecipes 设置，阻止了 ~player~ 合成 ~item~");

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Msg.class.getSimpleName());

    private String value;

    Msg(String val) {
        this.setValue(val);
    }

    public String getValue(String variable) {
        String val = value;
        val = val.replace("~perm~", variable);
        val = val.replace("~prot~", variable);
        val = val.replace("~author~", variable);
        val = val.replace("~name~", variable);
        val = val.replace("~contents~", variable);
        val = val.replace("~removedblocks~", variable);
        val = val.replace("@", "@" + variable);
        val = val.replace("~value~", variable);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Location location, String types) {
        String val = value;
        val = val.replace("@", "@ " + location.toString());
        val = val.replace("~removedblocks~", types);
        val = val.replace("~contents~", types);
        val = val.replace("~item~", types);
        val = val.replace("~name~", types);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(ItemStack is, Protections prot, Player plr, String source) {
        String val = value;

        val = val.replace("~item~", is.getType().name());
        val = val.replace("~protection~", prot.getDisplayName());
        val = val.replace("~source~", source);
        if (plr != null) {
            val = plr.getName() + " - " + val;
        }

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player player, String displayName) {
        String val = value;
        val = val.replace("@", "@ " + player.getLocation());
        val = val.replace("~item~", displayName);
        val = val.replace("~name~", player.getName());
        val = val.replace("~player~", player.getName());
        val = val.replace("~type~", displayName);
        val = val.replace("~block~", displayName);
        val = val.replace("~effects~", displayName);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(HumanEntity le, String value) {
        String val = value;

        val = val.replace("~author~", value);
        val = val.replace("~name~", le.getName());
        return val;
    }

    public String getValue(Player p, Location loc) {
        String val = value;

        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + loc.toString());
        return val;
    }

    public String getValue(String source, Location loc) {
        String val = value;
        val = val.replace("~type~", source);
        val = val.replace("~player~", source);
        val = val.replace("@", "@ " + loc.toString());
        return val;
    }

    public String getValue(Player p, Integer count, Location loc) {
        String val = value;

        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~count~", count.toString());
        return val;
    }

    public String getValue(Player p, Entity ent) {
        String val = value;

        val = val.replace("~name~", p.getName());
        val = val.replace("~player~", p.getName());
        val = val.replace("~entity~", ent.getName());
        val = val.replace("~vehicle~", ent.getType().name());
        val = val.replace("@", "@ " + ent.getLocation());
        return val;
    }

    public String getValue(Entity ent1, Entity ent2) {
        String val = value;

        if (ent1 instanceof Player) {
            val = val.replace("~entity~", ent1.getName());
        } else {
            val = val.replace("~entity~", ent1.getType().name());
        }

        val = val.replace("~vehicle~", ent2.getType().name());
        val = val.replace("@", "@ " + ent1.getLocation());
        return val;
    }

    public String getValue(Location loc, ItemStack is) {
        String val = value;


        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Object obj, ItemStack is, Enchantment en) {


        String val = value;

        val = val.replace("~item~", is.getType().name());
        if (en == null) {
            val = val.replace("~enchant~", "");
        } else {
            val = val.replace("~enchant~", en.getName());
            val = val.replace("~lvl~", is.getEnchantmentLevel(en) + "");
        }
        if (obj instanceof BlockState) {
            val = val.replace("~player~", "一个 " + ((BlockState) obj).getBlock().getType().name() + " 的背包");
        } else if (obj instanceof Player) {
            val = val.replace("~player~", ((Player) obj).getName());
        } else if (obj instanceof Inventory) {
            val = val.replace("~player~", ((Inventory) obj).getType().name() + " - ");
        } else if (obj instanceof Location) {
            val = val.replace("~player~", "一个潜影盒");
        } else if (obj instanceof Container) {
            val = val.replace("~player~", ((Container) obj).getType().name());
        }

        Location loc = null;

        if (obj instanceof BlockState) {
            loc = ((BlockState) obj).getLocation();
        }
        if (obj instanceof Player) {
            loc = ((Player) obj).getLocation();
        } else if (obj instanceof Block) {
            loc = ((Block) obj).getLocation();
        } else if (obj instanceof Inventory) {
            loc = ((Inventory) obj).getLocation();
        } else if (obj instanceof Location) {
            loc = ((Location) obj);
        } else if (obj instanceof Container) {
            loc = ((Container) obj).getLocation();
        }

        if (loc != null) {
            val = val.replace("@", "@ " + loc);
        } else {
            val = val.replace("@", "@ 未知位置 ");
        }
        return val;

    }

    public String getValue(Player p, ItemStack is, Enchantment en) {
        String val = value;

        val = val.replace("~item~", is.getType().name());
        val = val.replace("~enchant~", en.getName());
        val = val.replace("~lvl~", is.getEnchantmentLevel(en) + "");
        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + p.getLocation());
        return val;

    }

    public String getValue(Player p, EntityType et) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~type~", et.name());
        val = val.replace("~name~", p.getName());
        return ChatColor.translateAlternateColorCodes('&', val);
    }


    public String getValue(Object obj, ItemStack is) {
        if (obj instanceof Inventory) {
            if (((Inventory) obj).getHolder() instanceof Player) {
                Inventory inv = (Inventory) obj;
                return getValue((Player) inv.getHolder(), is);
            } else {
                return getValue(((Inventory) obj).getLocation(), is);
            }
        } else if (obj instanceof Container) {
            return getValue(((Container) obj), is);
        }
        LOGGER.error(
                "在日志记录操作期间，向 IllegalStack 传递了一个未知对象 {}，请在 Spigot 论坛或 IllegalStack Discord 上向 dNiym 报告此问题。",
                obj.toString()
        );
        return "???";
    }

    public String getValue(Container c, ItemStack is) {
        String val = value;

        val = val.replace("@", "@ " + c.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", c.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(ItemStack is, Object obj, StringBuilder list) {
        String val = value;

        if (obj instanceof Player) {
            val = val.replace("@", "@ " + ((Player) obj).getLocation());
            val = val.replace("~name~", ((Player) obj).getName());
        } else if (obj instanceof Inventory) {
            Inventory inv = null;
            inv = (Inventory) obj;
            if (inv.getHolder() instanceof Container) {
                val = val.replace(
                        "~name~",
                        ((Container) inv.getHolder())
                                .getLocation()
                                .getBlock()
                                .getType()
                                .name() + " @" + ((Container) inv.getHolder()).getLocation()
                );
            } else if (inv.getHolder() instanceof DoubleChest) {
                val = val.replace(
                        "~name~",
                        ((DoubleChest) inv.getHolder()).getLocation().getBlock().getType().name() + " @" + inv
                                .getLocation()
                                .toString()
                );
            } else if (inv.getHolder() instanceof Player) {
                val = val.replace("~name~", ((Player) inv.getHolder()).getName() + " @" + inv.getLocation().toString());
            } else {
                LOGGER.error(
                        "IllegalStack 本应发送一条描述库存的消息，但无法确定其类型！请在 IllegalStack Discord 或 Spigot 上联系 dNiym，附带此消息：{} ",
                        obj.toString()
                );
            }
        }


        val = val.replace("~item~", is.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~attributes~", list);

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is, String list) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~attributes~", list);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getConfigVal() {
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(Protections p, String name, String status) {
        String val = value;

        val = val.replace("~protection~", p.name());
        val = val.replace("~name~", name);
        val = val.replace("~status~", "" + status);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, int size) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~size~", "" + size);
        val = val.replace("~name~", p.getName());


        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Location loc, int size) {
        String val = value;

        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~size~", "" + size);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is, int lostItems) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~lost~", "" + lostItems);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}