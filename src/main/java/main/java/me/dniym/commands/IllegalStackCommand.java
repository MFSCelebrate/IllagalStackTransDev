package main.java.me.dniym.commands;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.Scheduler;
import main.java.me.dniym.utils.SpigotMethods;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IllegalStackCommand extends Command implements TabExecutor {

    public IllegalStackCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return onCommand(sender, this, commandLabel, args);
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, this, alias, args);
    }

    // ---------- 以下为原 IllegalStackCommand 的 onCommand 和 onTabComplete 内容（未做逻辑修改） ----------
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }

                sender.sendMessage(ChatColor.GREEN + "[IllegalStack] - 所有配置设置已重新加载。");
                IllegalStack.ReloadConfig(true);
                return true;
            }
        }

        if (args.length >= 1) {
            if (args[0].toLowerCase().startsWith("ver")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }

                String ver = IllegalStack.getPlugin().getDescription().getVersion();
                sender.sendMessage(ChatColor.AQUA + "-----===== 非法堆叠插件 (版本 " + ver + ") =====-----");
                sender.sendMessage(ChatColor.GOLD + " 检测到的服务器版本: " + IllegalStack.getVersion() + (IllegalStack.isSpigot()
                        ? ""
                        : "Spigot"));
                return true;

            }

            if (args[0].toLowerCase().startsWith("fix")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("IllegalStack.fixCommand")) {
                        Protections.fixEnchants(((Player) sender));
                    } else {
                        sender.sendMessage("您没有权限强制修复物品上的附魔等级。");
                    }
                } else {
                    sender.sendMessage("此命令只能在游戏内由玩家使用。");
                }
                return true;

            }
            if (args[0].toLowerCase().startsWith("prot")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== 非法堆叠插件防护 =====-----");
                sender.sendMessage(ChatColor.GOLD + " 检测到的服务器版本: " + IllegalStack.getVersion() + (IllegalStack.isSpigot()
                        ? ""
                        : "Spigot"));
                sender.sendMessage(ChatColor.AQUA + "-   版本特定防护   -");
                int parentId = 0;
                int catId = 0;
                if (args.length >= 2) {
                	try {
                		catId = Integer.parseInt(args[1].trim());
                	} catch (NumberFormatException ex) {
                		
                	}
                }
                if (args.length == 3) {
                	try {
                	    parentId = Integer.parseInt(args[2].trim());	
                	} catch (NumberFormatException ex) {
                    
                    }
                }
                for (Protections p : Protections.values()) {
                    if (!p.getCommand().isEmpty()) {
                        continue;
                    }
                    if (p.isVersionSpecific(IllegalStack.getVersion()) && p.isRelevantToVersion(IllegalStack.getVersion()) && p.getCatId() != 2) {
                        sendProtection(sender, p, parentId, catId);
                    }
                }

                //sender.sendMessage(ChatColor.AQUA + "-     Multi-Version Protections     -");
                sendCategory(sender, "多版本防护", (catId == 1), 1);
                if (catId == 1) {
                    for (Protections p : Protections.values()) {
                        if (!p.getCommand().isEmpty() || p.getCatId() == 2) {
                            continue;
                        }
                        if (p.isRelevantToVersion(IllegalStack.getVersion()) && !p.isVersionSpecific(IllegalStack.getVersion())) {
                            sendProtection(sender, p, parentId, catId);
                        }
                    }

                }

                sendCategory(sender, "杂项 / 用户请求的功能", (catId == 2), 2);
                //sender.sendMessage(ChatColor.AQUA + "-  Misc / User Requested Features  -");
                if (catId == 2) {
                    for (Protections p : Protections.values()) {
                        if (!p.getCommand().isEmpty()) {
                            continue;
                        }

                        if (p.isRelevantToVersion(IllegalStack.getVersion()) && p.getCatId() == catId) {
                            sendProtection(sender, p, parentId, catId);
                        }
                    }
                }
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("enchantwhitelistmode")) {
                if (sender instanceof Player) {
                    Player plr = (Player) sender;
                    if (!fListener.getInstance().getItemWatcher().remove(plr)) {
                        fListener.getInstance().getItemWatcher().add(plr);
                        plr.sendMessage(Msg.StaffEnchantBypass.getValue());
                        return true;
                    } else {
                        plr.sendMessage(Msg.StaffEnchantBypassCancel.getValue());
                        return true;

                    }
                }
            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("teleport")) {
                if (!hasPerm(sender, "illegalstack.notify")) {
                    return true;
                }
                if (sender instanceof Player) {
                    if (args.length >= 4) {
                        World w = IllegalStack.getPlugin().getServer().getWorld(args[4]);
                        Location loc = new Location(
                                w,
                                Integer.parseInt(args[1]),
                                Integer.parseInt(args[2]),
                                Integer.parseInt(args[3])
                        );
                        if (Scheduler.FOLIA) {
                            ((Player) sender).teleportAsync(loc);
                        } else {
                            ((Player) sender).teleport(loc);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage("此命令只能由玩家使用。");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                Protections p = Protections.getProtection(args[1]);
                if (p != null) {
                    p.toggleProtection();
                    String status = "开启";
                    if (!p.isEnabled()) {
                        status = "关闭";
                    }
                    fListener.getLog().append2(Msg.StaffProtectionToggleMsg.getValue(p, sender.getName(), status));
                    refreshCommands(sender);
                    return true;
                }
            }

        }

        if (args.length >= 4) {
            if (!hasPerm(sender, "illegalstack.admin")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("value")) {

                Protections pro = Protections.getProtection(args[2]);
                if (pro == null) {
                    sender.sendMessage(Msg.StaffInvalidProtectionMsg.getValue());
                    StringBuilder prots = new StringBuilder();
                    for (Protections p : Protections.values()) {
                        if (p.isList()) {
                            prots.append(p.name()).append(", ");
                        }
                    }
                    sender.sendMessage(ChatColor.GRAY + prots.toString());

                    return true;
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    StringBuilder val = new StringBuilder(args[3].trim());
                    if (args.length >= 4) {
                        val = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            val.append(args[i]).append(" ");
                        }

                        val = new StringBuilder(val.toString().trim());

                    }

                    pro.remTxtSet(val.toString(), sender);
                    //refreshCommands(sender);
                    return true;
                }

                if (args[1].equalsIgnoreCase("add")) {

                    if (pro.validate(args[3].trim(), sender)) {
                        sender.sendMessage(Msg.StaffOptionUpdated.getValue());
                        //refreshCommands(sender);
                    }
                    return true;
                }
            }
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== 可用防护开关 =====-----");
                StringBuilder prots = new StringBuilder();
                for (Protections p : Protections.values()) {
                    if (p.getParentId() == 0) {
                        prots.append(p.name()).append(", ");
                    }
                }

                sender.sendMessage(ChatColor.GRAY + prots.toString());
                sender.sendMessage(ChatColor.GOLD + "/istack toggle <防护名称>" + ChatColor.GRAY + " - 开启/关闭一个防护");
                return true;
            }

            if (args[0].equalsIgnoreCase("value")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                if (args.length > 3 && args[1].equalsIgnoreCase("add")) {
                    Protections pro = Protections.getProtection(args[2]);
                    if (pro != Protections.ItemNamesToRemove && pro != Protections.ItemLoresToRemove) {
                        sender.sendMessage(Msg.StaffSingleWordsOnly.getValue());
                        return true;
                    }
                    StringBuilder text = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    if (pro.validate(text.toString().trim(), sender)) {
                        sender.sendMessage(Msg.StaffStringUpdated.getValue(text.toString()));
                    }
                    return true;

                }
                if (args.length > 3 && args[1].equalsIgnoreCase("set")) {
                    Protections pro = Protections.getProtection(args[2]);
                    StringBuilder text = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    if (pro.validate(text.toString(), sender)) {
                        sender.sendMessage(Msg.StaffOptionUpdated.getValue());
                        //refreshCommands(sender);
                    }

                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== 可用防护选项 =====-----");
                sender.sendMessage(ChatColor.GOLD + "/istack values < set | remove > <防护名称>" + ChatColor.GRAY + " - 从防护列表中添加/移除一个值");
            }
        }

        sender.sendMessage(ChatColor.AQUA + "非法堆叠插件 - 可用命令");
        sender.sendMessage(ChatColor.GOLD + "/istack protections" + ChatColor.GRAY + " 显示防护状态");
        sender.sendMessage(ChatColor.GOLD + "/istack toggle <防护名称>" + ChatColor.GRAY + " - 开启/关闭一个防护");
        sender.sendMessage(ChatColor.GOLD + "/istack reload" + ChatColor.GRAY + " - 从config.yml重新加载配置");

        return true;
    }

    private boolean hasPerm(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        }
        sender.sendMessage(Msg.StaffMsgNoPerm.getValue(perm));

        return false;
    }

    private void sendCategory(CommandSender sender, String category, Boolean show, int catId) {
        Player plr = null;
        if (sender instanceof Player) {
            plr = (Player) sender;
        }

        if (IllegalStack.isSpigot() && plr != null) {
            plr.spigot().sendMessage(SpigotMethods.makeCategoryText(category, show, catId));
        }
    }

    private void sendProtection(CommandSender sender, Protections p, int parentId, int catId) {

        boolean line = false;
        String status = ChatColor.GREEN + " 开启";
        Player plr = null;
        if (sender instanceof Player) {
            plr = (Player) sender;
        }
        if (!p.isEnabled()) {
            status = ChatColor.DARK_RED + "关闭";
        }
        if (IllegalStack.isSpigot() && plr != null) {
            boolean hasKids = false;
            HashSet<Protections> kids = new HashSet<>();
            for (Protections child : Protections.values()) {
                if (child.getParentId() == p.getProtId()) {
                    hasKids = true;
                    kids.add(child);
                }
            }

            plr.spigot().sendMessage(SpigotMethods.makeParentText(p, status, hasKids, catId));
            for (Protections child : kids) {
                if (parentId != 0 && parentId == p.getProtId()) {
                    plr.spigot().sendMessage(SpigotMethods.makeChildText(child, catId));
                }
            }

        } else {
            if (!p.isList()) {
                sender.sendMessage(ChatColor.GOLD + "[" + status + ChatColor.GOLD + "] " + ChatColor.DARK_AQUA + "" + p.getDisplayName() + "");
            } else {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + "   " + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "" + p.getDisplayName() + " " + p
                        .findValue());
            }
            for (Protections child : Protections.values()) {
                if (child.getParentId() == p.getProtId()) {
                    sender.sendMessage(ChatColor.AQUA + "-> " + ChatColor.DARK_AQUA + child.getDisplayName() + " " + ChatColor.GRAY + child
                            .findValue());
                    line = false;
                }
            }
        }
        if (line) {
            sender.sendMessage(" ");
        }
    }

    private void refreshCommands(final CommandSender sender) {
        Scheduler.runTaskLater(IllegalStack.getPlugin(), () -> IllegalStack.getPlugin().getServer().dispatchCommand(sender, "istack prot"), 5);
    }

    // ---------- 原有的 onTabComplete 内容 ----------
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final @NotNull String[] args) {

        List<String> arguments = new ArrayList<>();
        arguments.add("protections");
        arguments.add("toggle");
        arguments.add("reload");

        List<String> result = new ArrayList<>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        }
        return null;
    }
                    }
