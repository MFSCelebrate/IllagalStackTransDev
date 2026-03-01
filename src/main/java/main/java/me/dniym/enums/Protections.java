package main.java.me.dniym.enums;


import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.utils.MagicHook;
import main.java.me.dniym.utils.NBTStuff;
import main.java.me.dniym.utils.SpigotMethods;
//import me.jet315.minions.MinionAPI;
//import me.jet315.minions.minions.Minion;
import net.brcdev.shopgui.gui.gui.OpenGui;

public enum Protections {

    DisableInWorlds(
            1,
            new String[]{},
            "禁用世界",
            "ALL",
            "Misc.DisableInWorlds",
            "在指定的世界中禁用IllegalStack。警告...这会完全禁用该世界中的所有防护，这意味着如果存在复制漏洞，您需要确保玩家无法将物品从非受保护世界转移到受保护世界！使用风险自负！",
            "",
            0,
            false
    ),
    //MISC SETTINGS
    InGameNotifications(
            3,
            true,
            "游戏内通知",
            "ALL",
            "Misc.InGameOffenseNotifications",
            "每当检测到漏洞时，向聊天中的工作人员发送通知。此权限默认为OP，但可以通过给予玩家 illegalstack.notify 权限节点来授予。",
            "",
            0
    ),
    LogOffensesInSeparateFile(
            3,
            true,
            "将违规记录到单独文件",
            3,
            "Misc.LogOffensesInSeparateFile",
            "将IllegalStack捕获的所有违规和位置记录到位于 plugins/IllegalStack/OffenseLog.txt 的单独文件中。",
            "",
            0
    ),
    PlayerOffenseNotifications(
            3,
            false,
            "通知附近的玩家违规",
            3,
            "Misc.NotifyNearbyPlayers",
            "通知10格范围内的玩家有关违规事件或任何由他们直接引起的事件。",
            "",
            0
    ),
    BreakExploitMachines(
            22,
            true,
            "破坏漏洞机器",
            "ALL",
            "Misc.BreakMachinesInsteadOfDroppingItems",
            "如果此设置为 FALSE，IllegalStack 将不会移除检测到的漏洞中的活塞等，而是破坏方块并掉落物品。",
            "",
            0
    ),
    ItemScanTimer(
            45,
            10,
            "物品扫描计时器延迟",
            "ALL",
            "Misc.ItemScanTimer",
            "允许您设置扫描不良物品之间的时间（以刻为单位），默认为10刻（每秒两次）。将此值增加太多可能会导致错过物品，请谨慎调整。注意：如果您调整此值，必须重启服务器才能生效。",
            "",
            0
    ),
    //ALL VERSION EXPLOITS
    FixNegativeDurability(
            64,
            true,
            "修复负耐久度",
            "ALL",
            "Exploits.NegativeDurability",
            "修复为零或更少的物品耐久度。",
            "",
            0
    ),
    PreventMinecartsInBoats(
            52,
            true,
            "防止船中放矿车",
            "ALL",
            "Exploits.Other.PreventMinecartsInBoats",
            "防止玩家将矿车放入船中，从而可能产生复制漏洞。",
            "",
            0
    ),
    PreventBedrockDestruction(
            7,
            true,
            "防止基岩破坏",
            "ALL",
            "Exploits.Other.PreventBedrockDestruction",
            "防止玩家通过TNT炸毁活塞头来破坏基岩。",
            "",
            0
    ),
    PreventEndPortalDestruction(
            51,
            true,
            "防止末地传送门破坏",
            "ALL",
            "Exploits.Other.PreventEndPortalDestruction",
            "防止玩家使用发射器破坏末地传送门方块。",
            "",
            0
    ),
    PreventPortalTraps(
            48,
            true,
            "防止下界传送门陷阱",
            "ALL",
            "Exploits.NetherPortal.PreventPortalTrap",
            "防止玩家进入没有有效出口的传送门，意味着如果玩家无法走出传送门，传送将被阻止并发送消息。",
            "",
            0
    ),
    BlockNonPlayersInNetherPortal(
            16,
            true,
            "防止下界传送门复制",
            "ALL",
            "Exploits.PortalDupe.BlockNonPlayersInNetherPortal",
            "此设置阻止非玩家实体（通常是马和驴）通过下界传送门。此漏洞用于在实体进入传送门的瞬间杀死它们以复制其背包内容，此设置还能阻止沙子/掉落物复制器。",
            "",
            0
    ),
    NetherWhiteList(
            16,
            new String[]{},
            "下界传送门白名单",
            16,
            "Exploits.PortalDupe.NetherWhiteList",
            "添加任何您希望允许前往下界的实体。警告：如果该生物能够拾取物品或拥有背包，玩家将能够在您的服务器上进行复制。",
            "",
            0,
            false
    ),
    NetherWhiteListMode(
            16,
            true,
            "白名单模式",
            16,
            "Exploits.PortalDupe.NetherWhiteListMode",
            "如果为 TRUE，此列表为白名单；如果为 FALSE，此列表为黑名单。",
            "",
            0
    ),
    BlockNonPlayersInEndPortal(
            17,
            true,
            "防止末地传送门复制",
            "ALL",
            "Exploits.PortalDupe.BlockNonPlayersInEndPortal",
            "此设置阻止非玩家实体（通常是马和驴）通过末地传送门。此漏洞用于在实体进入传送门的瞬间杀死它们以复制其背包内容，此设置还能阻止沙子/掉落物复制器。",
            "",
            0
    ),
    EndWhiteList(
            17,
            new String[]{},
            "末地传送门白名单",
            17,
            "Exploits.PortalDupe.EndWhiteList",
            "添加任何您希望允许前往末地的实体。警告：如果该生物能够拾取物品或拥有背包，玩家将能够在您的服务器上进行复制。",
            "",
            0,
            false
    ),
    EndWhiteListMode(
            17,
            true,
            "白名单模式",
            17,
            "Exploits.PortalDupe.EndWhiteListMode",
            "如果为 TRUE，此列表为白名单；如果为 FALSE，此列表为黑名单。",
            "",
            0
    ),
    NotifyBlockedPortalAttempts(
            17,
            false,
            "阻止传送门尝试时通知工作人员",
            17,
            "Exploits.PortalDupe.NotifyBlockedPortalAttempts",
            "每当传送门尝试被阻止时通知工作人员，此选项默认关闭，因为沙子复制器设置可能会非常刷屏。",
            "",
            0
    ),
    PreventEndGatewayCrashExploit(
            49,
            true,
            "防止末地折跃门崩溃漏洞",
            "> 1.9",
            "Exploits.EndGateway.PreventEndGatewayCrashExploit",
            "防止玩家骑乘实体通过末地折跃门，这可用于崩溃服务器。",
            "",
            0
    ),
    PreventHoppersToUnloadedChunks(
            11,
            true,
            "防止漏斗卸载区块漏洞",
            "ALL",
            "Exploits.Other.PreventHoppersToUnloadedChunks",
            "防止在区块边界使用两个漏斗时的复制漏洞。",
            "",
            0
    ),
    PreventMinecartGlitch(
            15,
            true,
            "防止矿车卡入漏洞",
            "ALL",
            "Exploits.MineCart.PreventMinecartGlitch",
            "防止玩家使用活塞推动方块或将矿车推入方块，此漏洞通常用于收集系统。",
            "",
            0
    ),
    MinecartBlockWhiteList(
            15,
            new String[]{},
            "允许矿车卡入的方块",
            15,
            "Exploits.MineCart.MinecartBlockWhiteList",
            "添加在此处的方块类型（材质）将在矿车卡入时被忽略。",
            "",
            0,
            false
    ),
    RemoveExistingGlitchedMinecarts(
            15,
            false,
            "移除已存在的卡入矿车",
            15,
            "Exploits.MineCart.RemoveExistingGlitchedMinecarts",
            "此设置将检测先前已卡入方块的矿车。建议仅在您知道玩家在世界中有卡入矿车时才开启此设置，一旦确认已移除，出于性能考虑请关闭此功能。",
            "",
            0
    ),
    KickForAutoClickerFishing(
            31,
            true,
            "防止自动点击钓鱼",
            "ALL",
            "Exploits.Fishing.KickForAutoClickerFishing",
            "检测使用自动点击器滥发钓鱼的玩家，他们使用一个方块（通常自动）滥发右键，鱼一上钩就被拉回。",
            "",
            0
    ),
    WatchForAutoFishMod(
            20,
            true,
            "防止自动钓鱼模组",
            "ALL",
            "Exploits.FishMod.WatchForAutoFishMod",
            "检测模拟普通玩家钓鱼的自动钓鱼模组，这不是即时检测，不会在第一次尝试时检测到自动钓鱼。",
            "",
            0
    ),
    MaxFishAllowedBeforeKick(
            20,
            5,
            "踢出前允许的最大鱼数",
            20,
            "Exploits.FishMod.MaxFishAllowedBeforeKick",
            "这是检测到自动钓鱼后，玩家在被踢出前允许钓到的鱼的数量。",
            "",
            0
    ),
    WarnPlayerThenKickInsteadOfNotify(
            20,
            false,
            "通知替代踢出",
            20,
            "Exploits.FishMod.WarnPlayerThenKickInsteadOfNotify",
            "此设置默认为 false，当怀疑玩家使用自动钓鱼模组时通知工作人员。如果设置为 true，在插件检测到玩家使用自动钓鱼模组后，会在即将踢出前警告玩家。",
            "",
            0
    ),
    MaxFishToNotifyStaffThenBlock(
            20,
            5,
            "通知前允许的最大鱼数",
            20,
            "Exploits.FishMod.MaxFishToNotifyStaffThenBlock",
            "如果通知替代踢出设置为 true，则在插件检测到玩家使用自动钓鱼模组并钓到这么多鱼后，将通知工作人员。",
            "",
            0
    ),

    PreventIndirectTNTPowerDupe(
            21,
            true,
            "防止TNT复制器",
            "ALL",
            "Exploits.TNTDupe.PreventIndirectTNTPowerDupe",
            "防止利用间接动力错误使TNT点燃并掉落/发射，但留下一块未点燃的TNT的TNT复制方法。",
            "",
            0
    ),
    PreventProjectileExploit2(
            41,
            false,
            "防止抛射物卡顿漏洞",
            "> 1.13",
            "Exploits.1_13_Exploits.Entities.PreventProjectileExploit2",
            "防止抛射物（如箭）被困在气泡柱中，导致大量这些物品不断漂浮和下落时造成卡顿。",
            "",
            0
    ),
    ProjectileDespawnDelay(
            41,
            22,
            "抛射物消失延迟",
            41,
            "Exploits.1_13_Exploits.Entities.ProjectileDespawnDelay",
            "等待抛射物消失的秒数（将此值设置得太低可能会导致箭/雪球在击中目标前消失）。",
            "",
            0
    ),
    BlockPlayersAboveNether(
            29,
            true,
            "阻止玩家在下界上方",
            "ALL",
            "Exploits.Nether.BlockPlayersAboveNether",
            "防止玩家传送/行走在下界顶部。",
            "",
            0
    ),
    EnsureSafeTeleportLocationIfAboveCeiling(
            29,
            true,
            "确保从天花板上传送下来的安全位置",
            29,
            "Exploits.Nether.EnsureSafeTeleportLocationIfAboveCeiling",
            "确保玩家从下界天花板传送下来时不会传送到固体下界岩或半空中。",
            "",
            0
    ),
    BlockBuildingAboveNether(
            29,
            true,
            "阻止在下界上方建造",
            29,
            "Exploits.Nether.BlockBuildingAboveNether",
            "防止玩家在 NetherYLevel 设置以上放置/破坏方块。",
            "",
            0
    ),
    KillPlayersBelowNether(
            29,
            false,
            "击杀下界下方的玩家",
            29,
            "Exploits.Nether.KillPlayersBelowNether",
            "击杀在下界底层下方飞行的玩家。",
            "",
            0
    ),
    NetherYLevel(
            29,
            128,
            "下界Y坐标覆盖",
            29,
            "Exploits.Nether.NetherYLevel",
            "调整下界的Y坐标高度，用于自定义世界生成器。",
            "",
            0
    ),
    ExcludeNetherWorldFromHeightCheck(
            29,
            new String[]{},
            "从高度检查中排除的世界",
            29,
            "Exploits.Nether.ExcludeNetherWorldFromHeightCheck",
            "在此处添加下界世界将排除其高度检查... 仅当您有一个下界世界具有非原版下界天花板高度时才应使用，例如可能 bSkyblockNether 等。这仅影响下界世界，如果世界不是下界则无效！",
            "",
            0,
            false
    ),

    RemoveBooksNotMatchingCharset(
            4,
            true,
            "无效书保护",
            "< 1.15",
            "Exploits.BookExploit.RemoveBooksNotMatchingCharset",
            "自动删除任何不符合配置中指定的字符集（且作者不在白名单上）的成书。此漏洞用于创建充满垃圾字符的书，这些书会增加区块大小超过服务器保存能力。它阻止区块正确保存，并允许玩家复制。",
            "",
            0
    ),
    ValidCharset(
            4,
            "US-ASCII",
            "有效字符集",
            4,
            "Exploits.BookExploit.ValidCharset",
            "这是当前设置的字符集，IllegalStack 将比较告示牌和书籍文本。如果您启用了书籍/告示牌保护，任何不属于此字符集的字符都被视为非法，有助于发现被利用的书籍和告示牌。如果您需要更改此值，可在此处找到有效字符集列表：https://docs.oracle.com/javase/7/docs/technotes/guides/intl/encoding.doc.html",
            "",
            0
    ),
    BookAuthorWhitelist(
            4,
            new String[]{},
            "书籍作者白名单",
            4,
            "Exploits.BookExploit.BookAuthorWhitelist",
            "添加到此列表的任何玩家名称将绕过所有书籍创建限制。",
            "",
            0,
            false
    ),

    PageCountThreshold(
            4,
            5,
            "页数阈值",
            4,
            "Exploits.BookExploit.PageCountThreshold",
            "每本书在被标记为非法之前允许包含非法字符的页数。",
            "",
            0
    ),
    LimitNumberOfPages(
            4,
            0,
            "页数限制",
            4,
            "Exploits.BookExploit.LimitNumberOfPages",
            "一本书可以包含的最大页数，如果设置为大于零的任何数字，则书页数超过此值的书将被移除。例如设置为 5 将移除任何有 6 页或更多页的书，无论内容如何。",
            "",
            0
    ),
    DestroyBadSignsonChunkLoad(
            5,
            false,
            "在区块加载时销毁不良告示牌",
            "ALL",
            "Exploits.SignExploit.DestroyBadSignsOnChunkLoad",
            "在区块首次加载时检查告示牌，这些告示牌具有非标准字符（通常来自作弊客户端），用于利用保存状态漏洞（如书籍复制），并防止玩家在处于该区块时登录（告示牌封禁）... 您只应在知道区块中有不良告示牌时启用此保护，因为它会消耗资源检查每个方块的告示牌。",
            "",
            0
    ),

    RemoveOverstackedItems(
            6,
            true,
            "移除超堆叠物品",
            "ALL",
            "Exploits.OverStack.RemoveOverstackedItems",
            "检测并移除数量大于原版堆叠大小的物品。",
            "",
            0
    ),
    IllegalStackMode(
            6,
            true,
            "白名单模式",
            6,
            "Exploits.OverStack.StackWhiteListMode",
            "如果为 TRUE，此列表为白名单；如果为 FALSE，此列表为黑名单。",
            "",
            0
    ),
    AllowStack(
            6,
            new String[]{"POTION"},
            "可超堆叠物品",
            6,
            "Exploits.OverStack.AllowStack",
            "添加到此列表的物品可以大于原版堆叠大小，例如药水/末影珍珠。",
            "",
            0,
            false
    ),
    PreventOverStackedItemInHoppers(
            6,
            true,
            "移除漏斗中的超堆叠物品",
            6,
            "Exploits.OverStack.PreventOverStackedItemInHoppers",
            "如果发现超堆叠物品在漏斗中，则将其移除。如果您的玩家没有大量存储的超堆叠物品，可以安全关闭此选项... 如果关闭，玩家可以使用漏斗从大堆叠中逐个提取物品。",
            "",
            0
    ),
    FixOverstackedItemInstead(
            6,
            false,
            "修复非法堆叠",
            6,
            "Exploits.OverStack.FixOverstackedItemInstead",
            "不是移除整个堆叠，而是将堆叠设置为该物品类型的最大堆叠大小。",
            "",
            0
    ),
    AllowStackForGroup(
            6,
            new String[]{},
            "按组可超堆叠物品",
            6,
            "Exploits.OverStack.GroupStack",
            "添加到此列表的物品将允许拥有 IllegalStack.Overstack 权限的玩家超堆叠。（您可以在此处添加 * 以允许拥有此权限的玩家超堆叠任何物品。）",
            "",
            0,
            false
    ),
    RemoveItemTypes(
            6,
            new String[]{},
            "按类型移除物品",
            6,
            "Exploits.OverStack.RemoveItemsOfType",
            "添加到此列表的物品类型（例如 STRUCTURE_BLOCK）将在玩家背包中发现时被移除，如果您的服务器上的玩家获得了您不希望他们拥有的方块（如基岩），这很有用。",
            "",
            0,
            false
    ),

    FixIllegalEnchantmentLevels(
            25,
            false,
            "修复非法附魔等级",
            "ALL",
            "Exploits.Enchants.FixIllegalEnchants",
            "将纠正任何大于原版 Minecraft 允许的附魔等级。",
            "",
            0
    ),
    CustomEnchantOverride(
            25,
            new String[]{},
            "附魔覆盖",
            25,
            "Exploits.Enchants.CustomEnchantOverride",
            "允许调整给定附魔的最大等级。意思是如果您将 Sharpness.10 添加到此列表，那么只有具有锋利 11 及以上的物品才会被移除。*注意* 这不会影响其他附魔。",
            "",
            0,
            false
    ),
    EnchantedItemWhitelist(
            25,
            new String[]{},
            "物品白名单",
            25,
            "Exploits.Enchants.EnchantedItemWhitelist",
            "将跳过修复任何与类型、名称和描述完全匹配的物品。",
            "",
            2,
            false
    ),
    OnlyFunctionInWorlds(
            25,
            new String[]{},
            "要检查的世界",
            25,
            "Exploits.Enchants.OnlyFunctionInWorlds",
            "将仅检查列出的世界中的非法附魔，如果此列表为空，则默认检查所有世界。",
            "",
            2,
            false
    ),
    AllowBypass(
            25,
            false,
            "允许权限绕过",
            25,
            "Exploits.Enchants.AllowBypass",
            "将允许任何拥有 illegalstack.enchantbypass 权限的玩家绕过附魔等级检查（默认为 OP）。注意，如果玩家被给予物品但没有权限，物品上的附魔仍将被移除。",
            "",
            2
    ),
    RemoveUnbreakableFlag(
            25,
            false,
            "从物品中移除无法破坏标志",
            25,
            "Exploits.Enchants.RemoveUnbreakableFlag",
            "将从玩家持有的物品中移除无法破坏标志（除非他们有绕过权限）。",
            "",
            0
    ),
    RemoveCustomAttributes(
            25,
            false,
            "移除自定义属性修饰符",
            25,
            "Exploits.Enchants.RemoveCustomAttributes",
            "将从玩家持有的物品中移除所有自定义属性（除非他们有绕过权限）。这对于清除先前作弊进入的带有 +1000 伤害的棍子或即死头盔很有用。*注意* 如果您运行的是 < 1.13，此防护需要 NbtAPI 2.1.0+。",
            "",
            2
    ),
    DestroyIllegallyEnchantedItemsInstead(
            25,
            false,
            "销毁物品而非修复",
            25,
            "Exploits.Enchants.DestroyIllegallyEnchantedItemsInstead",
            "不是修复物品，而是销毁它。",
            "",
            0
    ),

    BlockLoopedDroppers(
            32,
            false,
            "阻止循环投掷器",
            "ALL",
            "Exploits.DropperDupe.BlockLoopedDroppers",
            "防止投掷器/发射器来回传输物品。",
            "",
            0
    ),
    PreventRNGEnchant(
            34,
            true,
            "防止随机附魔漏洞",
            "> 1.9",
            "Exploits.RNGEnchant.PreventRNGEnchant",
            "防止允许玩家破解随机附魔种子的漏洞，使他们能够选择物品上的确切附魔。",
            "",
            0
    ),
    PreventLootingExploit(
            50,
            true,
            "防止抢夺漏洞",
            "ALL",
            "Exploits.Looting.PreventLootingExploit",
            "防止玩家使用远程武器（如弓或弩）通过副手持抢夺剑来启用抢夺。",
            "",
            0
    ),
    PreventVexTrapping(
            55,
            false,
            "防止恼鬼被困",
            "> 1.11",
            "UserRequested.Mobs.PreventVexTrapping",
            "防止恼鬼被困在矿车/船中，通常用于袭击农场。用户请求的功能。",
            "",
            2

    ),
    PreventRecordDupe(
            61,
            true,
            "防止唱片复制",
            "ALL",
            "Exploits.RecordDupe",
            "防止使用 TNT、骷髅和苦力怕坑同时大量刷唱片的漏洞。",
            "",
            0
    ),

    //PacketAttackWindowClick(33,false,"防止数据包崩溃器1", "ALL", "Exploits.PacketAttack.PacketCrasher1", "防止过大数据包和数据包刷屏等。")
    //MULTI VERSION EXPLOITS
    PreventRailDupe(
            8,
            true,
            "破坏铁轨/地毯复制器",
            "1.12/1.13/1.14/1.15/1.16/1.17/1.18/1.19/1.20/1.21",
            "Exploits.Other.PreventRailDupe",
            "防止设计用于复制地毯和铁轨的红石机器，这些物品通常被复制以提供无限的熔炉燃料或在商店中出售换取游戏内货币。",
            "",
            0
    ),

    PreventNestedShulkers(
            10,
            true,
            "防止嵌套潜影盒",
            "> 1.11",
            "Exploits.Other.PreventNestedShulkers",
            "防止玩家将潜影盒放入其他潜影盒中，此漏洞导致几乎无限的存储空间。",
            "",
            0
    ),

    DisableChestsOnMobs(
            27,
            true,
            "禁用生物身上的箱子",
            "< 1.16",
            "Exploits.Other.DisableChestsOnMobs",
            "防止玩家使用或添加箱子到羊驼、驴、马等生物上。用于防止作弊客户端玩家利用这些生物进行复制。",
            "",
            0
    ),
    DisableRidingExploitableMobs(
            27,
            true,
            "禁用可被利用的带箱生物的骑乘/驯服",
            27,
            "Exploits.Other.DisableRidingExploitableMobs",
            "防止玩家骑乘或驯服可以装备箱子的生物。",
            "",
            0
    ),
    PunishForChestsOnMobs(
            27,
            false,
            "暴力惩罚屡犯者",
            27,
            "Exploits.Other.PunishForChestsOnMobs",
            "启用此选项将惩罚任何试图在带箱动物上放置箱子的玩家，动物将被销毁，玩家背包将被清空，玩家将被踢出服务器。",
            "",
            0
    ),

    PreventInvalidPotions(
            35,
            true,
            "防止无效药水",
            "1.11, 1.12, 1.13, 1.14, 1.15, 1.16, 1.17, 1.18, 1.19",
            "Exploits.Other.PreventInvalidPotions",
            "防止非 OP 玩家拥有无效/无法制作的药水。这些通常用于创造服务器的恶意目的，例如即死药水。",
            "",
            0
    ),
    PreventInfiniteElytraFlight(
            36,
            true,
            "防止无限鞘翅飞行",
            "1.9, 1.10, 1.11, 1.12, 1.13, 1.14, 1.15, 1.16, 1.17",
            "Exploits.Other.PreventInvalidElytraFlight",
            "防止玩家使用允许无烟花无限鞘翅飞行的漏洞，此漏洞允许玩家从最大建筑高度开始垂直上升，直到他们决定下降或鞘翅损坏。",
            "",
            0
    ),

    PreventItemSwapLagExploit(
            37,
            true,
            "防止物品交换卡顿漏洞",
            "> 1.9",
            "Exploits.Other.PreventItemSwapLagExploit",
            "防止玩家滥发手持物品交换，造成服务器卡顿。",
            "",
            0
    ),
    PreventPearlGlassPhasing(
            38,
            true,
            "防止末影珍珠穿玻璃",
            "> 1.9",
            "Exploits.Teleport.PearlPhasing",
            "防止玩家使用末影珍珠穿过玻璃方块。",
            "",
            0
    ),
    TeleportCorrectionNotify(
            38,
            false,
            "通知传送修正",
            38,
            "Exploit.Teleport.CorrectionNotify",
            "如果设置为 true，每当由于检测到末影珍珠穿墙而对传送进行修正时，插件将通知工作人员，默认关闭，因为它可能很刷屏。",
            "",
            0
    ),
    PreventArmorStandLagMachine(
            39,
            true,
            "防止盔甲架卡顿机器",
            "> 1.11",
            "Exploit.LagMachines.ArmorStand",
            "防止活塞垂直抬起盔甲架然后放下，通常用于构建卡顿机器。",
            "",
            0
    ),
    PreventEndCrystalLagMachine(
            43,
            true,
            "防止末地水晶卡顿机器",
            "> 1.11",
            "Exploit.LagMachines.End Crystal",
            "防止活塞将末地水晶推成一大堆，通常用于构建卡顿机器。",
            "",
            0
    ),
    PreventCommandsInBed(
            47,
            true,
            "防止在床上使用命令",
            "ALL",
            "Exploits.Other.PreventCommandsInBed",
            "防止玩家在床上使用命令。这已被关联到一个严重漏洞，即玩家在床上时并非所有事件都能正确触发，其中一个巨大漏洞是玩家如果在睡觉时能打开 GUI，就可以从 GUI 中获取任何物品。",
            "",
            0
    ),
    PreventBedExplosions(
            56,
            true,
            "防止床爆炸",
            "ALL",
            "Exploits.Other.PreventBedExplosions",
            "防止玩家在末地/下界使用床作为廉价爆炸物进行 PVP 和破坏方块。",
            "",
            0
    ),
    PreventSpawnEggsOnSpawners(
            59,
            true,
            "防止在刷怪笼上使用刷怪蛋",
            "> 1.13",
            "Misc.Spawners.PreventSpawnEggsOnSpawners",
            "防止非 OP 玩家使用刷怪蛋更改刷怪笼生成的生物类型。",
            "",
            0
    ),
    //3rd Party Plugins
    BlockCMIShulkerStacking(
            10,
            true,
            "CMI 潜影盒修复",
            "ALL",
            "Exploits.3rdParty.BlockCMIShulkerStacking",
            "CMI 插件提供了一项功能，允许在不放置的情况下打开潜影盒。由于这不是真正的潜影盒，它允许玩家在没有漏洞的情况下将潜影盒放入潜影盒。此设置阻止该行为。",
            "",
            0
    ),

    //1.12 ONLY
    PreventItemFramePistonDupe(
            13,
            true,
            "防止物品展示框/活塞复制",
            "1.12",
            "Exploits.1_12_Exploits.PreventItemFramePistonDupe",
            "防止物品展示框在被活塞破坏时复制物品。",
            "",
            0
    ),
    PreventRecipeDupe(
            9,
            true,
            "防止配方书复制",
            "1.12",
            "Exploits.1_12_Exploits.PreventRecipeDupe",
            "此复制漏洞在配方书首次引入 Minecraft 时出现，涉及丢弃一个物品然后滥发合成一个物品（如工作台），会导致大量超堆叠物品。",
            "",
            0
    ),
    PreventShulkerCrash(
            62,
            true,
            "防止 1.12 潜影盒崩溃",
            "1.12",
            "Exploits.ShulkerCrash",
            "防止玩家使用发射器将潜影盒放置在最大建筑高度以上，导致服务器崩溃。",
            "",
            0
    ),
    //1.13 ONLY
    PreventVillagerSwimExploit(
            18,
            true,
            "防止村民游泳交易漏洞",
            "1.13",
            "Exploits.1_13_Exploits.PreventVillagerSwimExploit",
            "防止玩家利用新的村民交易机制中的错误，该错误会导致村民在商人游泳时通过玩家打开/关闭交易菜单不断降低价格。",
            "",
            0
    ),
    PreventExcessiveFireworkExploit(
            32,
            true,
            "防止烟花具有过多效果",
            "1.13",
            "Exploits.1_13_Exploits.PreventExcessiveFireworkExploit",
            "检测具有过多效果的烟花。",
            "",
            0
    ),
    //1.14 ONLY
    SilkTouchBookExploit(
            28,
            true,
            "阻止精准采集书漏洞",
            "1.14",
            "Exploits.1_14_Exploits.Misc.BlockSilkTouchBookExploit",
            "防止玩家使用精准采集书（手中）挖掘方块，就好像他们使用了精准采集工具一样。",
            "",
            0
    ),
    PreventFoodDupe(
            31,
            true,
            "防止食物/消耗品复制漏洞",
            "1.14.4",
            "Exploits.1_14_Exploits.Misc.PreventFoodDupe",
            "阻止 1.14.4 消耗品/食物复制漏洞。",
            "",
            0
    ),
    PreventVibratingBlocks(
            40,
            true,
            "防止震动方块漏洞",
            "1.14",
            "Exploits.1_14_Exploits.Entities.VibratingBlockExploit",
            "防止掉落方块被困在不断更新的状态中，导致作物像零刻农场一样生长。",
            "",
            0
    ),
    //1.16 ONLY
    PreventPiglinDupe(
            57,
            true,
            "防止猪灵复制",
            "1.16 / 1.16.1 / 1.16.2",
            "Exploits.1_16_Exploits.Dupes.PreventPiglinDupe",
            "防止猪灵在以物易物时被滥用复制物品（不影响 Paper）。",
            "",
            0
    ),
    PreventShulkerCrash2(
            63,
            true,
            "防止使用打火石的潜影盒崩溃",
            "1.16",
            "Exploits.ShulkerCrash2",
            "防止玩家使用朝下的发射器配合打火石导致服务器崩溃。",
            "",
            0
    ),
    //1.14 / 1.15 ONLY

    VillagerTradeCheesing(
            19,
            true,
            "防止村民交易作弊",
            "1.14 / 1.15 / 1.16 / 1.17 / 1.18 / 1.19 / 1.20 / 1.21",
            "Exploits.1_14_Exploits.Traders.BlockVillagerTradeCheesing",
            "防止玩家反复放置/破坏村民的工作站点方块，这会迫使他们获得新交易，通常人们滥用此机制以确保从村民那里获得特定附魔或物品，而不是随机机制。",
            "",
            0
    ),
    VillagerRestockTime(
            19,
            10,
            "最小补货时间（分钟）",
            19,
            "Exploits.1_14_Exploits.Traders.VillagerRestockTime",
            "设置村民允许补货的最小分钟数。注意* 这是现实生活中的分钟数，游戏时间的任何更改都将被忽略，这意味着如果玩家与村民交易然后去睡觉以推进时间，他们通常不会在第二天早上补货。",
            "",
            0
    ),
    ZombieVillagerTransformChance(
            19,
            65,
            "村民僵尸化几率",
            19,
            "Exploits.1_14_Exploits.Traders.ZombieVillagerTransformChance",
            "允许您降低村民被感染后变成僵尸村民的几率。在原版困难难度下这是 100%。这允许玩家反复感染/治愈村民以降低其交易价格。将此值设置为小于 100 将使此类玩家面临失去村民的风险，而不是轻松作弊交易。**（仅在您的服务器难度设置为困难时真正重要）** *如果设置为零，此设置将完全阻止转化。",
            "",
            0
    ),

    PreventCactusDupe(
            12,
            true,
            "防止零刻漏洞",
            "1.13 / 1.14 / 1.15",
            "Exploits.Other.PreventZeroTickExploit",
            "破坏利用游戏机制使仙人掌和其他可生长方块生长得更快的红石机器。",
            "",
            0
    ),

    PreventTripwireDupe(
            46,
            true,
            "防止绊线复制",
            "1.15 / 1.16 / 1.17 / 1.18 / 1.19 / 1.20 / 1.21",
            "Exploits.1_15_Exploits.Dupes.PreventTripwireDupe",
            "防止玩家使用活板门复制绊线钩。",
            "",
            0
    ),
    
    PreventStringDupe(
    		46,
    		true,
    		"防止线复制",
    		46,
    		"Exploits.1_15_Exploits.Dupes.PreventStringDupe",
    		"防止水被用来触发线复制漏洞。",
    		"",
    		0
    		),
    //User Requested | Obscure Features
    PreventZombieItemPickup(
            14,
            false,
            "防止僵尸拾取物品",
            "ALL",
            "UserRequested.Mobs.PreventZombieItemPickup",
            "防止僵尸正常拾取物品，这用于防止溺尸复制漏洞，默认关闭，保留是因为用户请求。",
            "",
            2
    ),
    PreventCobbleGenerators(
            45,
            false,
            "防止圆石生成器",
            "ALL",
            "UserRequested.Cobble.PreventCobbleGenerators",
            "防止水和岩浆相互流动时生成圆石。",
            "",
            2
    ),
    SpawnerReset(
    		33,
    		false,
    		"强制刷怪笼重置",
    		"> 1.12",
    		"UserRequested.Spawners.SpawnerReset",
    		"重置特定类型刷怪笼的选项",
    		"",
    		2
    		
    ),
    ResetSpawnersOfType(
            33,
            new String[]{},
            "强制在挖掘时重置刷怪笼",
            33,
            "UserRequested.Spawners.ResetSpawnersOfType",
            "如果被挖掘，将给定 <实体类型> 的刷怪笼重置为猪刷怪笼，仅当您有一个丝触刷怪笼插件并且世界中有些特定刷怪笼您不希望允许玩家挖掘时才有用。",
            "",
            2,
            false
    ),
    ResetSpawnersOfTypeOnSpawn(
    		33,
    		new String[] {},
    		"在生成时重置刷怪笼",
    		33,
    		"UserRequested.Spawners.ResetSpawnersOfTypeOnSpawn",
    		"当列出的 <实体类型> 的刷怪笼尝试生成生物时，将其重置为猪刷怪笼。对于清除在您的服务器上设置的村民/铁傀儡/凋灵刷怪笼很有用。",
    		"",
    		2,
    		false
    		),
   
    RemoveItemsMatchingName(
            23,
            false,
            "移除特定名称的物品",
            "ALL",
            "UserRequested.ItemRemoval.RemoveItemsMatchingName",
            "如果此设置为 TRUE，任何匹配名称的物品将被销毁，如果因其他插件的漏洞导致物品从 GUI 中被取出，这很有用。默认关闭（用户请求的功能）。",
            "",
            2
    ),
    ItemNamesToRemove(
            23,
            new String[]{},
            "要匹配的物品名称",
            23,
            "UserRequested.ItemRemoval.ItemNamesToRemove",
            "将物品名称添加到此列表，如果 RemoveItemsMatchingName 为 true，它们将被移除，就像非法堆叠物品一样。",
            "",
            2,
            false
    ),
    ItemLoresToRemove(
            23,
            new String[]{},
            "要匹配的物品描述",
            23,
            "UserRequested.ItemRemoval.ItemLoresToRemove",
            "添加任何标识您希望移除的物品的描述，例如如果玩家非法从商店插件中取出物品，IllegalStack 将在检测到时移除这些物品。",
            "",
            2,
            false
    ),
    NameLoreStrictMatchMode(
            23,
            false,
            "严格匹配描述/名称",
            23,
            "UserRequested.ItemRemoval.NameLoreStrictMatchMode",
            "如果此值为 true，则物品名称或描述行必须完全匹配，包括颜色代码；如果为 false，只要文本包含在描述或名称中，就会检测到匹配。",
            "",
            2
    ),
    BlockEnchantingInstead(
            23,
            false,
            "改为阻止附魔",
            23,
            "UserRequested.ItemRemoval.BlockEnchantingInstead",
            "如果此值为 true，则不移除匹配名称/描述的物品，而是阻止玩家附魔此物品。",
            "",
            2
    ),
    BlockRepairsInstead(
            23,
            false,
            "改为阻止修复",
            23,
            "UserRequested.ItemRemoval.BlockRepairsInstead",
            "如果此值为 true，则不移除匹配名称/描述的物品，而是阻止玩家修复此物品。",
            "",
            2
    ),

    NotifyInsteadOfBlockExploits(
            26,
            new String[]{},
            "仅通知而不阻止",
            "ALL",
            "UserRequested.Misc.NotifyInsteadOfBlock",
            "添加到此列表的任何防护将不会被阻止，但仍会发送通知。注意：对于某些防护，这可能会产生相当多的刷屏信息。",
            "",
            2,
            false
    ),
    BlockBadItemsFromCreativeTab(
            30,
            false,
            "阻止来自创造模式物品栏的不良物品",
            "> 1.12",
            "UserRequested.Obscure.BlockBadItemsFromCreativeTab",
            "防止玩家在单人世界中使用 GMC 时通过创造模式物品栏给自己带有元数据的物品带入服务器。此漏洞允许玩家创建通常无法通过常规 /GMC 获得的物品，例如持有时可给予 5000 生命的木棍... 注意：此修复需要安装 ProtocolLib 才能工作！",
            "",
            2
    ),
    DestroyInvalidShulkers(
            31,
            false,
            "销毁导致客户端崩溃的潜影盒",
            "> 1.11",
            "UserRequested.Obscure.HackedShulker.DestroyInvalidShulkers",
            "销毁由作弊客户端创建的用于封禁玩家（书籍封禁变种）的潜影盒。**注意** 需要 NBT API 2.0.0（所有 Spigot 版本）才能工作！！",
            "",
            2
    ),
    CheckGroundForBadShulkerAtLogin(
            31,
            false,
            "登录时检查地面上的不良潜影盒",
            31,
            "UserRequested.Obscure.HackedShulker.CheckGroundForBadShulkerAtLogin",
            "当玩家登录时，移除世界中任何掉落的、包含无效客户端崩溃数据的潜影盒。您只应在知道区块中有不良潜影盒的区域时启用此保护。此保护将在每次服务器重启时自行关闭。**注意** 需要 NBT API 2.0.0 才能工作！！",
            "",
            2
    ),
    //PreventHeadBan(44, true, "防止头颅区块封禁", "ALL","Exploits.HeadChunkBan.PreventHeadBan","销毁玩家头颅，这些头颅放置在世界中会封禁尝试在其附近登录的玩家。注意* 这对于防止它们最初被放置很有用，要修复已放置的头颅，请参阅下一个保护。", "", 0),
    //CheckGroundForBadHeadsAtLogin(44, false, "移除地面上的不良头颅", 44,"Exploits.HeadChunkBan.CheckGroundForBadHeadsAtLogin","销毁放置在世界中的玩家头颅，这些头颅会封禁尝试在其附近登录的玩家。您只应在知道区块中有不良头颅的区域时启用此保护。此保护将在每次服务器重启时自行关闭。**注意** 需要 NBT API 2.0.0 才能工作！！","",0),
    IgnoreAllHopperChecks(
            42,
            false,
            "忽略所有漏斗检查",
            "ALL",
            "UserRequested.Obscure.HopperCheck.IgnoreAllHopperChecks",
            "强制插件忽略任何涉及漏斗的物品或漏洞。警告：这仅应在您绝对确定自己在做什么时启用，因为它可能为玩家转移复制物品甚至在某些情况下复制物品打开大门。如果您甚至认为需要开启此选项，请联系插件作者 (dNiym)。",
            "",
            2
    ),
    RemoveAllRenamedItems(
            44,
            false,
            "移除所有重命名物品",
            "ALL",
            "UserRequested.Obscure.Misc.RemoveAllRenamedItems",
            "移除任何没有 IllegalStack.RenameBypass 权限的用户身上发现的重命名物品。",
            "",
            2
    ),
    DisableBookWriting(
            53,
            false,
            "禁用所有书籍编辑",
            "ALL",
            "Exploits.BookExploit.DisableBookWriting",
            "禁用所有玩家书籍编写，任何被编辑的书和笔（作者不在 BookAuthorWhiteList 中的玩家）将被移除，并向玩家发送消息。此选项默认关闭，是用户请求的功能。",
            "",
            2
    ),
    PreventHeadInsideBlock(
            54,
            false,
            "防止头部卡入方块",
            "ALL",
            "Exploits.MineCart.PreventHeadInsideBlocks",
            "如果玩家在乘坐载具时进入方块，将其从载具中踢出。",
            "",
            2
    ),
    AlsoPreventHeadInside(
            54,
            new String[]{"COMPOSTER"},
            "同时防止玩家头部卡入",
            54,
            "Exploits.Minecart.AlsoPreventHeadInside",
            "如果玩家的头部卡入某个方块，则破坏该方块，通常用于可被滥用以进行 X 光透视的方块，例如树叶/堆肥桶。",
            "",
            2,
            false
    ),
    IgnoreAllShulkerPlaceChecks(
            58,
            false,
            "忽略所有潜影盒放置检查",
            "> 1.11",
            "UserRequested.Obscure.IgnoreAllShulkerPlaceChecks",
            "强制插件忽略任何潜影盒放置事件。这将禁用放置潜影盒时移除堆叠物品的功能。",
            "",
            2
    ),
    DamagePlayersAboveNether(
    		60,
    		false,
    		"对下界上方的玩家造成伤害",
    		"ALL",
    		"UserRequested.NetherDamage.DamagePlayersAboveNether",
    		"如果启用此选项，玩家将被允许在下界顶部，但只要他们停留在下界顶部，就会随时间受到伤害。",
    		"",
    		2
    		),
    AboveNetherDamageDelay(
    		60,
    		2,
    		"伤害延迟",
    		60,
    		"UserRequested.NetherDamage.DamageDelay",
    		"对位于下界天花板上方的玩家造成伤害的间隔时间（秒）。",
    		"",
    		2
    		),
    AboveNetherDamageAmount(
    		60,
    		2,
    		"伤害量",
    		60,
    		"UserRequested.NetherDamage.DamageAmount",
    		"对位于下界天花板上方的玩家造成的伤害量。",
    		"",
    		2
    		),
    DisableCraftingRecipes(
            62,
            new String[]{},
            "根据合成结果材料禁用合成配方",
            "> 1.12",
            "UserRequested.Obscure.DisableCraftingRecipes",
            "根据配方合成的物品结果材质禁用合成配方。",
            "",
            2,
            false
            )
    ;
    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Protections.class.getSimpleName());
    ///OPTIONS///
    private Object defaultValue = null;
    private boolean enabled = false;
    private String txtValue = "";
    private int intValue = -1;
    private HashSet<String> txtSet = new HashSet<>();
    private String serverVersion = "";
    private int protId = -1;
    private int parentId = 0;
    private boolean nukeApples = false;
    private String displayName;
    private String version = "";
    private String description = "";
    private String configPath;
    private String command = "";
    private boolean isList = false;
    private int catId = 0;
    private boolean relevant = false;
    private static Class<?> craftingStoreInventoryHolder = null;

    Protections(
            int id,
            String[] array,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId,
            boolean relevant
    ) {
        this.defaultValue = new HashSet<String>();
        for (final String value : array) {
            ((HashSet<String>) this.defaultValue).add(value);
        }

        this.isList = true;
        this.setCatId(catId);
        this.setTxtSet(new HashSet<String>());

        for (String s : array) {
            this.getTxtSet().add(s);
        }

        setBasics(id, ver, dname, desc, path, cmd);

    }

    Protections(
            int id,
            Integer intVal,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = intVal;
        this.setCatId(catId);
        this.setIntValue(intVal);
        setBasics(id, ver, dname, desc, path, cmd);
    }

    Protections(
            int id,
            Boolean value,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = value;
        this.setDefaultValue(value);
        this.setCatId(catId);
        this.enabled = value;
        setBasics(id, ver, dname, desc, path, cmd);

    }

    Protections(
            int id,
            String setting,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = setting;
        this.setCatId(catId);
        this.setTxtValue(setting);
        setBasics(id, ver, dname, desc, path, cmd);
    }

    public static Protections getProtection(String enumName) {

        for (Protections p : Protections.values()) {
            if (p.name().equalsIgnoreCase(enumName)) {
                return p;
            } else if (p.getConfigPath().contains(enumName)) {
                return p;
            }
        }

        return null;
    }

    public static void update() {

        for (Protections p : Protections.values()) {


            if (p.isList) {
                List<String> cVal = IllegalStack.getPlugin().getConfig().getStringList(p.getConfigPath());
                p.txtSet.clear();
                p.txtSet.addAll(cVal);
            } else if (p.intValue >= 0) {
                p.intValue = IllegalStack.getPlugin().getConfig().getInt(p.getConfigPath());
            } else if (p.txtValue != null && !p.txtValue.isEmpty()) {
                p.txtValue = IllegalStack.getPlugin().getConfig().getString(p.getConfigPath());
            } else {
                p.enabled = IllegalStack.getPlugin().getConfig().getBoolean(p.getConfigPath());

            }

            if ((p == Protections.DestroyBadSignsonChunkLoad || p == Protections.RemoveExistingGlitchedMinecarts || p == Protections.CheckGroundForBadShulkerAtLogin)) //p == Protections.CheckGroundForBadHeadsAtLogin ||
            {
                if (p.enabled) {

                    LOGGER.warn(
                            "您在配置中将保护 {} 设置为 TRUE。此保护旨在作为临时设置，不应长期启用！这样做会导致 IllegalStack 在每次加载区块时检查所有区块，这可能造成不必要的服务器负载，并可能导致其他服务器问题。",
                            p.configPath
                    );
                    IllegalStack.getPlugin().getConfig().set(p.getConfigPath(), false);

                }
            }
        }

    }

    public static Protections findByConfig(String key) {
        for (Protections p : Protections.values()) {
            if (p.getConfigPath().equals(key)) {
                return p;
            }
        }

        return null;
    }

    public static Protections getParentByChild(Protections child) {
        for (Protections p : Protections.values()) {
            if (p.getProtId() == child.getParentId()) {
                return p;
            }
        }

        return null;
    }

    public static HashMap<Protections, Boolean> getRelevantTo(String ver) {
        HashMap<Protections, Boolean> relevant = new HashMap<>();

        for (Protections p : Protections.values()) {
            if (!p.getCommand().isEmpty()) {
                continue;
            }
            if (p.isRelevantToVersion(ver)) {
                relevant.put(p, true);
                for (Protections child : p.getChildren()) {
                    relevant.put(child, true);
                }

            } else {  //isn't relevant to this version
                if (p.version.isEmpty()) { //skip random child nodes
                    continue;
                }
                relevant.put(p, false);
                for (Protections child : p.getChildren()) {
                    relevant.put(child, false);
                }
            }
        }
        return relevant;
    }

    public static void fixEnchants(Player player) {


        ItemStack itemStack = player.getInventory().getItemInMainHand();
        NBTStuff.checkForBadCustomData(itemStack, player, true);
        if (itemStack == null) {
            player.sendMessage(Msg.StaffNoItem.getValue());
            return;
        }
        if (itemStack.getEnchantments().isEmpty()) {
            player.sendMessage(Msg.StaffNoEnchants.getValue());
            return;
        }

        HashSet<Enchantment> replace = new HashSet<>();
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (itemStack.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel()) {

                if (enchantment.canEnchantItem(itemStack)) {
                    player.sendMessage(Msg.IllegalEnchantLevel.getValue(player, itemStack, enchantment));
                } else {
                    player.sendMessage(Msg.IllegalEnchantType.getValue(player, itemStack, enchantment));
                }
                replace.add(enchantment);

            } else {
                if (!enchantment.canEnchantItem(itemStack)) {
                    replace.add(enchantment);
                    player.sendMessage(Msg.IllegalEnchantType.getValue(player, itemStack, enchantment));
                }
            }
        }

        for (Enchantment removeEnchantment : replace) {
            itemStack.removeEnchantment(removeEnchantment);
            if (removeEnchantment.canEnchantItem(itemStack)) {
                itemStack.addEnchantment(removeEnchantment, removeEnchantment.getMaxLevel());
                player.sendMessage(Msg.StaffEnchantFixed.getValue(player, itemStack, removeEnchantment));
            }
        }
        if (replace.isEmpty()) {
            player.sendMessage(Msg.StaffEnchantNotFixed.getValue());
        }

    }

    public Object getConfigValue() {
        if (isList) {
            return getTxtSet();
        } else if (txtValue != null && !txtValue.isEmpty()) {
            return txtValue;
        } else if (intValue != -1) {
            return intValue;
        } else {
            return enabled;
        }

    }

    public Object getDefaultValue() {

        if (defaultValue instanceof HashSet) {
            return defaultValue;
        } else if (defaultValue instanceof String) {
            return defaultValue;
        } else if (defaultValue instanceof Integer) {
            return defaultValue;
        } else {
            return defaultValue;
        }

    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String findValue() {

        String retVal = "[ ]";

        if (isList) {
            retVal = "[";
            for (String v : getTxtSet()) {
                retVal = retVal + v + ", ";
            }

            if (getTxtSet().isEmpty()) {
                retVal = retVal + "未设置";
            }

            retVal = retVal.trim() + "]";
        } else if (intValue != -1) {
            retVal = "" + intValue;
        } else if (!txtValue.isEmpty()) {
            retVal = txtValue;
        } else if (enabled) {
            retVal = ChatColor.GREEN + " 已启用 ";
        } else if (!enabled) {
            retVal = ChatColor.DARK_RED + "已禁用 ";
        }


        return retVal;
    }

    private void setBasics(int id, Object ver, String dname, String desc, String path, String cmd) {
        if (ver instanceof String) {
            this.setVersion((String) ver);
        } else {
            this.setParentId((Integer) ver);
        }
        this.setDisplayName(dname);
        this.setDescription(desc);
        this.setConfigPath(path);
        this.protId = id;
        this.setCommand(cmd);
        this.relevant = isRelevantToVersion(getServerVersion());
    }

    public boolean isEnabled(Object obj) {
    	
    	World wld = getWorldFromObj(obj);
    	
    	if(!isEnabled())
    		return false;
    	
    	if(wld != null && isDisabledInWorld(wld))
    			return false;
    	
    	return true; 
    		
    }
    
    World getWorldFromObj(Object obj) {
    	
    	if(obj instanceof World)
    		return ((World)obj);
    	if(obj instanceof Player)
    		return ((Player)obj).getWorld();
        if (obj instanceof Inventory) {
        	/*
        	 * Due to a paper bug https://github.com/PaperMC/Paper/issues/9437
        	 * affected as of build #61 of 1.20.1
        	 * The inventory's getLocation() method returns null if a hopper pulls bonemeal from a composter's inventory, also apparently affects juke boxes. 
        	 * Issue only appears to affect paper, not regular spigot.
        	 * 
        	 * The following is some validation that should get the world of the object rather than throwing an error and bothering the user, perhaps
        	 * paper will implement getLocation() on juke boxes and composters via the regular method just like every other container.
        	 */
        	Inventory inv = ((Inventory)obj);
        	Location loc = inv.getLocation();
        	if(loc == null) {
        		if(!inv.getViewers().isEmpty()) {
        			if(inv.getViewers().get(0).getLocation() != null)
        				return inv.getViewers().get(0).getWorld();
        		} else {
        			if(inv.getHolder() instanceof Player) {
        				Player p = (Player) inv.getHolder();
        				if(p != null)
        					return p.getWorld();
        			}
        			if(inv.getHolder() instanceof BlockInventoryHolder)
        			{
        				BlockInventoryHolder bi = ((BlockInventoryHolder)inv.getHolder());
        				if(bi != null)
        				{
        					Block b = bi.getBlock();
       						return b.getWorld();
        				}
        			}
            		
        		}
    			return null;
        		
        	} 
            return ((Inventory) obj).getLocation().getWorld();
        }
        if (obj instanceof Location) 
            return ((Location) obj).getWorld();
        if (obj instanceof Container)
        	return ((Container)obj).getWorld();

        //if(obj != null) 
        	//IllegalStack.getPlugin().getLogger().log(Level.WARNING, "无法从对象类型获取世界信息： " + obj.toString() + " 请通过 github 或官方 IllegalStack discord 通知 dNiym 此问题！");
        
        return null;
    	
    }
    public boolean isDisabledInWorld(World wld) {
    	return Protections.DisableInWorlds.isWhitelisted(wld.getName());
    }
    
    @Deprecated
    public boolean isEnabled() {
        if (this.getVersion().isEmpty()) //child node
            return this.enabled;
        
        return this.relevant && this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private String getServerVersion() {
//        if (serverVersion.equalsIgnoreCase("")) {
//            String version = IllegalStack
//                    .getPlugin()
//                    .getServer()
//                    .getClass()
//                    .getPackage()
//                    .getName()
//                    .replace(".", ",")
//                    .split(",")[3];
//
//
//            version = IllegalStack.getString(version);
//            if (version.equalsIgnoreCase("v1_15_R1")) {
//
//                version = IllegalStack.getPlugin().getServer().getVersion().split(" ")[2];
//                if (version.contains(" ")) {
//                    version = version.replace(")", "");
//                    version = version.replace(".", "_");
//                    String[] ver = version.split("_");
//                    version = "v" + ver[0] + "_" + ver[1] + "_R" + ver[2];
//                }
//
//            }
//
//            serverVersion = version;
//        }
        return IllegalStack.getVersion();
    }

    public boolean notifyOnly() {
        return Protections.NotifyInsteadOfBlockExploits.getTxtSet().contains(this.name());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRelevantToVersion(String serverVersion) {

        if (serverVersion.contains("_")) {
            serverVersion = serverVersion.replace("_", ".");
        }
        if (this.getVersion().isEmpty()) {
            return false; //must be a child node
        }

        if (this.getVersion().contains("< 1.21")) {
            return !serverVersion.contains("1.21");
        }

        if (this.getVersion().contains("< 1.20")) {
            return !serverVersion.contains("1.20");
        }

        if (this.getVersion().contains("< 1.19")) {
            return !serverVersion.contains("1.19") 	&& !serverVersion.contains("1.20") && !serverVersion.contains("1.21");
        }

        if (this.getVersion().contains("< 1.18")) {
            return !serverVersion.contains("1.18") && !serverVersion.contains("1.18")
            		&& !serverVersion.contains("1.19") && !serverVersion.contains("1.20") && !serverVersion.contains("1.21");
        }

        if (this.getVersion().contains("< 1.17")) {
            return !serverVersion.contains("1.17") && !serverVersion.contains("1.16") && !serverVersion.contains("1.17") && !serverVersion.contains("1.18")
            		&& !serverVersion.contains("1.19") && !serverVersion.contains("1.20") && !serverVersion.contains("1.21");
        }
        if (this.getVersion().contains("< 1.16")) {
            return !serverVersion.contains("1.16") && !serverVersion.contains("1.17") && !serverVersion.contains("1.18")
            		&& !serverVersion.contains("1.19") && !serverVersion.contains("1.20") && !serverVersion.contains("1.21");
        }
        if (this.getVersion().contains("< 1.15")) {
            return !serverVersion.contains("1.15") && !serverVersion.contains("1.16") && !serverVersion.contains("1.17") && !serverVersion.contains("1.18")
            		&& !serverVersion.contains("1.19") && !serverVersion.contains("1.20") && !serverVersion.contains("1.21");
        }

        if (this.getVersion().equalsIgnoreCase("1.14.4") && !serverVersion.contains("1.14.R4")) {
            return false;
        }

        if (this.getVersion().equalsIgnoreCase("1.14.3") && !serverVersion.contains("1.14.R3")) {
            return false;
        }

        if (this.getVersion().contains("ALL")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.21") && serverVersion.contains("1.21")) {
            return true;
        }
        
        if (this.getVersion().equalsIgnoreCase("1.20") && serverVersion.contains("1.20")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.19") && serverVersion.contains("1.19")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.18") && serverVersion.contains("1.18")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.17") && serverVersion.contains("1.17")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.16") && serverVersion.contains("1.16")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.15") && serverVersion.contains("1.15")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.14") && serverVersion.contains("1.14")) {
            return true;
        }

        if (this.getVersion().contains("1.14") && serverVersion.contains("1.14")) {
            return true;
        }

        if (this.getVersion().contains("1.15") && serverVersion.contains("1.15")) {
            return true;
        }

        if (this.getVersion().contains("1.16") && serverVersion.contains("1.16")) {
            return true;
        }

        if (this.getVersion().contains("1.17") && serverVersion.contains("1.17")) {
            return true;
        }

        if (this.getVersion().contains("1.18") && serverVersion.contains("1.18")) {
            return true;
        }

        if (this.getVersion().contains("1.19") && serverVersion.contains("1.19")) {
            return true;
        }

        if (this.getVersion().contains("1.20") && serverVersion.contains("1.20")) {
            return true;
        }

        if (this.getVersion().contains("1.21") && serverVersion.contains("1.21")) {
            return true;
        }
       

        if (this.getVersion().contains("> 1.12")) {
            if (serverVersion.contains("1.21") || serverVersion.contains("1.20") || serverVersion.contains("1.19") || serverVersion.contains("1.18") || serverVersion.contains("1.17") || serverVersion.contains("1.16") 
            		|| serverVersion.contains("1.15") || serverVersion.contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12")) {
                return true;
            }
        }

        if (this.getVersion().contains("> 1.9")) {
            if (serverVersion.contains("1.21") || serverVersion.contains("1.20") || serverVersion.contains("1.19") || serverVersion.contains("1.18") || serverVersion.contains("1.17") || serverVersion.contains("1.16") || serverVersion.contains("1.15") || serverVersion
                    .contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12") || serverVersion.contains(
                    "1.11") ||
                    this.serverVersion.contains("1.10") || this.serverVersion.contains("1.9")) {
                return true;
            }
        }

        if (this.getVersion().contains("> 1.11")) {
            if (serverVersion.contains("1.21") || serverVersion.contains("1.20") || serverVersion.contains("1.19") || serverVersion.contains("1.18") || serverVersion.contains("1.17") || serverVersion.contains("1.16") || serverVersion.contains("1.15") || serverVersion
                    .contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12") || serverVersion.contains(
                    "1.11")) {
                return true;
            }
        }
        if (this.getVersion().contains("1.12")) {
            if (serverVersion.contains("1.12")) {
                return true;
            }
        }
        if (this.getVersion().contains("1.13") && serverVersion.contains("1.13")) {
            return true;
        }

        if (this.getVersion().contains("> 1.13")) {
            return Material.matchMaterial("CAVE_AIR") != null;
        }
        if (this.getVersion().contains("< 1.13")) {
            return Material.matchMaterial("CAVE_AIR") == null;
        }


        return false;

    }

    public boolean isVersionSpecific(String serverVersion) {
        if (this.version.isEmpty()) {
            return false;
        }

        if (this.getVersion().equals("1.21")) {
            if (serverVersion.contains("v1_21")) {
                return true;
            }
        }

        if (this.getVersion().equals("1.20")) {
            if (serverVersion.contains("v1_20")) {
                return true;
            }
        }

        if (this.getVersion().equals("1.19")) {
            if (serverVersion.contains("v1_19")) {
                return true;
            }
        }

        if (this.getVersion().equals("1.18")) {
            if (serverVersion.contains("v1_18")) {
                return true;
            }
        }
        if (this.getVersion().equals("1.17")) {
            if (serverVersion.contains("v1_17")) {
                return true;
            }
        }

        if (this.getVersion().equals("1.16")) {
            if (serverVersion.contains("v1_16")) {
                return true;
            }

        }
        if (this.getVersion().equals("1.15")) {
            if (serverVersion.contains("v1_15_R1")) {
                return true;
            } else {
                return serverVersion.contains("v1_15_R2");
            }
        }
        if (this.getVersion().equals("1.14.4")) {
            return serverVersion.contains("v1_14_R4");
        }


        if (this.getVersion().equals("1.14.3")) {
            return serverVersion.contains("v1_14_R3");
        }

        if (serverVersion.contains("v1_21") && this.getVersion().contains("1.21")) {
            return true;
        }

        if (serverVersion.contains("v1_20") && this.getVersion().contains("1.20")) {
            return true;
        }
        
        if (serverVersion.contains("v1_19") && this.getVersion().contains("1.19")) {
            return true;
        }

        if (serverVersion.contains("v1_18") && this.getVersion().contains("1.18")) {
            return true;
        }

        if (serverVersion.contains("v1_17") && this.getVersion().contains("1.17")) {
            return true;
        }
        if (serverVersion.contains("v1_16") && this.getVersion().contains("1.16")) {
            return true;
        }
        if (serverVersion.contains("v1_15") && this.getVersion().contains("1.15")) {
            return true;
        }
        if (serverVersion.contains("v1_14") && this.getVersion().contains("1.14")) {
            return true;
        }
        if (serverVersion.contains("v1_13") && this.getVersion().contains("1.13")) {
            return true;
        }
        if (serverVersion.contains("v1_12") && this.getVersion().contains("1.12")) {
            return true;
        }
        if (serverVersion.contains("v1_11") && this.getVersion().contains("1.11")) {
            return true;
        }
        if (serverVersion.contains("v1_10") && this.getVersion().contains("1.10")) {
            return true;
        }
        if (serverVersion.contains("v1_9") && this.getVersion().contains("1.9")) {
            return true;
        }
        return serverVersion.contains("v1_8") && this.getVersion().contains("1.8");
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public int getProtId() {
        return protId;
    }

    public void setProtId(int protId) {
        this.protId = protId;
    }

    public String getCommand() {

        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void toggleProtection() {

        this.enabled = !this.enabled;

    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean isList) {
        this.isList = isList;
    }

    public boolean remTxtSet(String value, CommandSender sender) {
        for (String v : this.getTxtSet()) {

            if (v.equalsIgnoreCase(value.trim())) {
                sender.sendMessage(ChatColor.GREEN + "成功从 " + this.name() + " 中移除了 " + value);
                this.getTxtSet().remove(value.trim());

                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + value + " 不在 " + this.name() + " 的项目列表中，请确保拼写正确！ ");
        return false;
    }

    public boolean setTxtValue(String value, CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "成功将 " + this.name() + " 更新为: " + value);
        this.txtValue = value;
        return true;
    }

    public boolean addTxtSet(String value, CommandSender sender) {

        if (sender != null) {
            sender.sendMessage(ChatColor.GREEN + "成功将 " + value + " 添加到 " + this.name());
        }
        this.getTxtSet().add(value.trim());


        return true;
    }

    public boolean addIntValue(int value, CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "成功将 " + this.name() + " 的值设置为 " + value);
        this.intValue = value;
        return true;
    }

    public boolean validate(String value, CommandSender sender) {

    	
    		
        if (this == Protections.AlsoPreventHeadInside) {
            return addTxtSet(value, sender);
        }

        if (this == Protections.BookAuthorWhitelist) {
            OfflinePlayer op = IllegalStack.getPlugin().getServer().getOfflinePlayer(value);
            if (op == null || !op.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "抱歉！" + value + " 似乎从未加入过服务器！");
                return false;
            }
            return addTxtSet(value, sender);
        }

        if (this == Protections.CustomEnchantOverride) {

            if (!value.contains(".")) {
                sender.sendMessage(ChatColor.RED + "命令用法:   /istack  value add CustomEnchantOverride <附魔名称.等级>");

                return false;
            }
            String[] val = value.split("\\.");
            if (val.length < 2) {
                sender.sendMessage(ChatColor.RED + "命令用法:   /istack  value add CustomEnchantOverride <附魔名称.等级>");

                return false;
            }

            Enchantment enc = Enchantment.getByName(val[0]);
            if (enc == null) {
                String vals = "";
                for (Enchantment en : Enchantment.values()) {
                    vals = vals + en.getName() + ", ";
                }

                sender.sendMessage(ChatColor.RED + "您必须指定一个有效的 Minecraft 附魔。有效的附魔有: " + ChatColor.DARK_GRAY + vals);
                return false;
            }

            try {
                Integer.parseInt(val[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "您必须指定一个数字作为有效等级..   " + val[1] + " 不是数字！");
                return false;
            }

            return addTxtSet(value, sender);
        }
        if (this == Protections.ValidCharset) {

            try {
                if (Charset.forName(value.trim()).newEncoder().canEncode(ChatColor.stripColor(value))) {
                    return setTxtValue(value.trim(), sender);
                } else {
                    LOGGER.error("无法编码？ {}", value);
                }

            } catch (IllegalCharsetNameException ex) {
                sender.sendMessage(ChatColor.RED + "抱歉！" + value + " 似乎不是有效的字符集！有关有效字符集的列表，请参阅：https://docs.oracle.com/javase/7/docs/api/java/nio/charset/CharsetEncoder.html");
                return false;

            } catch (UnsupportedCharsetException ex) {
                sender.sendMessage(ChatColor.RED + "抱歉！" + value + " 似乎是不受支持的字符集！有关有效字符集的列表，请参阅：https://docs.oracle.com/javase/7/docs/api/java/nio/charset/CharsetEncoder.html");
                return false;
            }

        }
		/*
		if(this == Protections.FishingKickMessage || this == Protections.FishModKickMessage || this == Protections.SwimExploitMessage) {

			return setTxtValue(value,sender);
		}
		*/
        if (this == Protections.ItemNamesToRemove || this == Protections.ItemLoresToRemove) {
            return addTxtSet(ChatColor.translateAlternateColorCodes('&', value), sender);
        }

        if (this == Protections.EnchantedItemWhitelist) {

            return addTxtSet(value, sender);

        }
        if (this == Protections.NotifyInsteadOfBlockExploits) {
            Protections p = Protections.getProtection(value.trim());
            if (p != null) {
                return addTxtSet(value, sender);
            }

            StringBuilder vals = new StringBuilder();
            for (Protections pa : Protections.values()) {
                if (pa.findValue().contains("ENABLED") && pa.relevant) //pa.isRelevantToVersion(IllegalStack.getVersion()))
                {
                    vals.append(pa.name()).append(" ,");
                }
            }
            sender.sendMessage(ChatColor.DARK_RED + "抱歉！" + value + " 似乎不是有效的防护名称，请使用以下值之一: " + ChatColor.GRAY + vals);
            return false;
        }

        if (this == Protections.ResetSpawnersOfType || this == Protections.NetherWhiteList || this == Protections.EndWhiteList || this == Protections.ResetSpawnersOfTypeOnSpawn) {
            EntityType et = null;
            StringBuilder types = new StringBuilder();
            for (EntityType e : EntityType.values()) {
                if (e.name().equalsIgnoreCase(value)) {
                    return addTxtSet(value, sender);
                }
                types.append(e.name()).append(", ");
            }

            sender.sendMessage(ChatColor.DARK_RED + "抱歉！" + value + " 似乎不是有效的实体类型！");
            sender.sendMessage(ChatColor.DARK_AQUA + "有效的实体类型有: " + ChatColor.GRAY + types);
            return false;
        }
        if (this == Protections.AllowStack || this == Protections.MinecartBlockWhiteList || this == Protections.AllowStackForGroup || this == Protections.RemoveItemTypes) {

            if (this == Protections.AllowStackForGroup) {
                if (value.equals("*")) {
                    return addTxtSet(value, sender);
                }
            }
            Material m = Material.matchMaterial(value);
            if (m == null) {

                int id = -1;
                int data = 0;

                if (value.contains(":")) {
                    String[] magicNumber = value.split(":");
                    try {
                        id = Integer.parseInt(magicNumber[0]);
                        data = Integer.parseInt(magicNumber[1]);
                        return addTxtSet(value, sender);
                    } catch (NumberFormatException ignored) {
                        sender.sendMessage(ChatColor.DARK_RED + "使用数据值时，必须同时使用物品 ID 和数据值的数值.. 例如: 397:3");
                        return false;
                    }


                }

                sender.sendMessage(ChatColor.DARK_RED + "抱歉！" + value + " 似乎不是有效的物品类型！");
                return false;
            }

            return addTxtSet(value, sender);
        }

        if (this == Protections.OnlyFunctionInWorlds || this == Protections.DisableInWorlds || this == Protections.ExcludeNetherWorldFromHeightCheck) {
            World w = IllegalStack.getPlugin().getServer().getWorld(value.trim());
            if (w == null) {
                sender.sendMessage(ChatColor.DARK_RED + "抱歉！" + value + " 似乎不是有效的世界名称！");
                return false;
            }
            return addTxtSet(value, sender);
        }
        if (this == Protections.ProjectileDespawnDelay || this == Protections.LimitNumberOfPages || this == Protections.NetherYLevel || this == Protections.VillagerRestockTime || this == Protections.ZombieVillagerTransformChance || this == Protections.PageCountThreshold || this == MaxFishAllowedBeforeKick || this == MaxFishToNotifyStaffThenBlock || this == AboveNetherDamageDelay || this == AboveNetherDamageAmount) {
            try {
                int intCheck = Integer.parseInt(value.trim());
                if (intCheck < 0) {
                    sender.sendMessage(ChatColor.RED + "抱歉！此防护的值不能小于零。");
                    return false;
                }
                if (this == AboveNetherDamageAmount) {
                	if(intCheck < 1) {
                		sender.sendMessage(ChatColor.DARK_RED + "此防护的最小值必须大于 1。");
                		return false;
                	}
                }
                if (this == ProjectileDespawnDelay || this == AboveNetherDamageDelay) {
                	if(intCheck < 1) {
                		sender.sendMessage(ChatColor.DARK_RED + "此防护的最小值必须大于 1 秒。");
                		return false;
                	}
                }
                if (this == Protections.ZombieVillagerTransformChance) {
                    if (intCheck < 0 || intCheck > 100) {
                        sender.sendMessage(ChatColor.DARK_RED + "抱歉！此防护的值必须在 1 到 100 之间。");
                        return false;
                    }
                }


                return addIntValue(intCheck, sender);

            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "抱歉！" + value + " 对于 " + this.name() + " 必须是整数");
                return false;
            }

        }
        LOGGER.error("防护 {} 没有验证步骤.. 无法验证用户输入: 请向 dNiym 报告。", this.name());
        return false;
    }

    public HashMap<String, Boolean> getLoreNameList() {
        HashMap<String, Boolean> target = new HashMap<>();
        if (this == Protections.RemoveItemsMatchingName) {
            for (String s : Protections.ItemLoresToRemove.getTxtSet()) {
                target.put(s, true);
            }
            for (String s : Protections.ItemNamesToRemove.getTxtSet()) {
                target.put(s, false);
            }
        }
        return target;
    }

    public HashSet<String> getTxtSet() {
        return txtSet;
    }

    public void setTxtSet(HashSet<String> txtSet) {
        this.txtSet = txtSet;
    }

    public boolean isWhitelisted(String name) {
        return isWhitelisted(name, null);
    }
    
    public boolean isWhitelisted(EntityType et) {
    	if (this == Protections.ResetSpawnersOfTypeOnSpawn && et != null)
    	{
    		for(String s:Protections.ResetSpawnersOfTypeOnSpawn.getTxtSet())
    			if(s.contains("*") || s.toLowerCase().contains(et.name().toLowerCase()))
    				return true;

    	}
    	return false;
    	
    }
    public boolean isWhitelisted(String name, Player player) {

    	if (this == Protections.RemoveItemTypes && player != null) {
            if (player.hasPermission("illegalstack.removeitemsoftypebypass")) {
                return true;
            }
        }

        if (this == Protections.AllowStack && player != null) {
            if (player.hasPermission("illegalstack.overstack") || player.isOp()) {
                for (String s : Protections.AllowStackForGroup.getTxtSet()) {
                    if (s.contains("*")) {
                        return true;
                    }
                }
            }
        }

        for (String s : this.txtSet) {
            if (s.equalsIgnoreCase(name.trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean loreNameMatch(ItemMeta itemMeta) {
        HashMap<String, Boolean> target = Protections.RemoveItemsMatchingName.getLoreNameList();
        boolean found = false;
        for (String s : target.keySet()) {

            if (target.get(s) && itemMeta.hasLore()) //lore matching
            {
                for (String line : itemMeta.getLore()) {
                    if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                        if (line.equals(s)) {
                            found = true;
                        }
                    } else if (ChatColor.stripColor(line).contains(s)) {
                        found = true;
                    }
                }


            } else {
                if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                    if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(s)) {
                        found = true;
                    }
                } else if (itemMeta.hasDisplayName() && ChatColor.stripColor(itemMeta.getDisplayName()).contains(s)) {
                    found = true;
                }
            }
        }
        return found;
    }

    public boolean isWhitelisted(Material type) {
        for (String s : this.getTxtSet()) {
            Material m = Material.matchMaterial(s);
            if (m != null && type == m) {
                return true;
            }

            if (m == null && type.name().contains(s.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhitelisted(ItemStack is) {

        if (this == Protections.RemoveItemTypes && IllegalStack.hasIds()) {  //check for magic number type values
            int id = -1;
            int data = 0;

            for (String s : this.getTxtSet()) {
                if (s.contains(":")) {
                    String[] splStr = s.split(":");
                    try {
                        id = Integer.parseInt(splStr[0]);
                        data = Integer.parseInt(splStr[1]);
                    } catch (NumberFormatException ignored) {

                    }
                }

            }
            if (id == is.getType().getId() && data == is.getDurability()) {
                return true;
            }

        }
        if (this == Protections.RemoveItemTypes && this.nukeApples && !is
                .getEnchantments()
                .isEmpty() && is.getType() == Material.matchMaterial("GOLDEN_APPLE")) {
            return true;
        }

        if (this != Protections.RemoveItemTypes && IllegalStack.isHasMagicPlugin() && MagicHook.isMagicItem(is)) {
            return true;
        }

        for (String s : this.getTxtSet()) {
            Material m = Material.matchMaterial(s);
            if (m != null && is.getType() == m) {
                return true;
            }
            if (is.serialize().toString().equalsIgnoreCase(s)) {
                return true;
            }
        }


        return false;
    }

    public static void runReflectionChecks() {
        try {
            craftingStoreInventoryHolder = Class.forName("net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder");
            LOGGER.info("检测到 CraftingStore 插件！IllegalStack 现在将能够检测和处理 CraftingStore 库存。");
        } catch (ClassNotFoundException e) {
            craftingStoreInventoryHolder = null;
        }
    }

    public boolean isThirdPartyInventory(InventoryView inv) {

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("CraftingStore") != null) {
            if (craftingStoreInventoryHolder == null) {
                return false;
            }
            InventoryHolder holder = inv.getTopInventory().getHolder();
            if (holder == null) {
                return false;
            }
            if (inv.getTopInventory().getHolder().getClass() == craftingStoreInventoryHolder) {
                return true;
            }

        }

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("DynamicShop") != null) {
            String name = "TRADE_TITLE";
            if (inv.getTitle().equals(name)) {
                return true;
            }
        }

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("ShopGUIPlus") != null) {
            return inv.getTopInventory().getHolder() instanceof OpenGui;
        }

        return false;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public HashSet<Protections> getChildren() {
        HashSet<Protections> children = new HashSet<>();
        for (Protections p : Protections.values()) {
            if (p.getParentId() == this.protId) {
                children.add(p);
            }
        }

        return children;
    }

    public boolean isAllowedEnchant(Enchantment en, int lvl) {


        for (String s : this.getTxtSet()) {
            String[] val = s.split("\\.");
            if (val.length < 2) {
                LOGGER.error(
                        "无法从 {} 路径转换附魔/等级，请检查 config.yml！",
                        this.getConfigPath()
                );
                return false;
            }
            Enchantment ench = Enchantment.getByName(val[0]);
            int level = Integer.parseInt(val[1]);
            if (ench == null) {
                LOGGER.error(
                        "无法找到附魔: {} 请检查您的 config.yml 中的部分: {} 并确保您使用的是有效的附魔。",
                        val[0],
                        this.getConfigPath()
                );
                return false;
            }

            if (en != ench) //not a overridden enchantment
            {
                continue;
            }

            //level higher than override.
            return lvl <= level;

            //otherwise enchant is good
        }
        return false;
    }

    public boolean isNukeApples() {
        return nukeApples;
    }

    public void setNukeApples(boolean nukeApples) {
        this.nukeApples = nukeApples;
    }

    public boolean isThirdPartyObject(Entity entity) {

/*        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("JetsMinions") != null) {
            boolean minion = MinionAPI.isMinion(entity);
            if (minion) {
                return true;
            } 
        } */
        if(entity instanceof LivingEntity && SpigotMethods.isNPC((LivingEntity)entity))
        	return true;
        
        return false;
    }


}