package com.tintingpatch.advancedmaintenance.command;

import com.tintingpatch.advancedmaintenance.AdvancedMaintenance;
import com.tintingpatch.advancedmaintenance.object.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand implements CommandExecutor, TabCompleter {

    String prefix = AdvancedMaintenance.getInstance().prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        boolean senderIsPlayer;

        senderIsPlayer = commandSender instanceof Player;
        Player player = senderIsPlayer ? (Player) commandSender : null;

        if(args.length < 1){
            //First command arg
            commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
            if(senderIsPlayer){
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
            }
            return true;
        } else if (args.length == 1) {
            if(args[0].equalsIgnoreCase("help")){
                commandSender.sendMessage("§7------------------------§6Help§7------------------------");
                if(!AdvancedMaintenance.getInstance().getServerState().autoSave){
                    commandSender.sendMessage("§6/maintenance save");
                }
                commandSender.sendMessage("§6/maintenance set [on/off]");
                commandSender.sendMessage("§6/maintenance player {player name} [add/remove/list]");
                commandSender.sendMessage("§6/maintenance group list");
                commandSender.sendMessage("§6/maintenance group [create/delete] {group name}");
                commandSender.sendMessage("§6/maintenance group manage {group name} [enable/disable]");
                commandSender.sendMessage("§6/maintenance group manage {group name} [add/remove] {player name}");
                commandSender.sendMessage("§7----------------------------------------------------");

                //sending success sound to sender if player
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                }
            }else if (args[0].equalsIgnoreCase("save")){
                if (!AdvancedMaintenance.getInstance().getServerState().autoSave){
                    commandSender.sendMessage(prefix + "§r §aAuto save is active. No need to save.");
                }else {
                    AdvancedMaintenance.getInstance().getServerState().save();
                    commandSender.sendMessage(prefix + "§r §aSaved!");
                }
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                }
            } else {
                commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                }
                return true;
            }
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("set")){
                if(args[1].equalsIgnoreCase("on")){
                    AdvancedMaintenance.getInstance().getServerState().setInMaintenance(true);
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                    }
                } else if (args[1].equalsIgnoreCase("off")) {
                    AdvancedMaintenance.getInstance().getServerState().setInMaintenance(false);
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                    }
                } else {
                    commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("list")) {
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (Group item : AdvancedMaintenance.getInstance().getServerState().getGroups())
                {
                    if (first)
                        first = false;
                    else
                        builder.append("§7,§6 ");
                    builder.append(item.getName());
                }
                commandSender.sendMessage(prefix + "§r §6" + builder);
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                }
            }
        } else if (args.length == 3) {
            if(args[0].equalsIgnoreCase("player")){
                //Check if command is valid
                if(!(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("check"))){
                    commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }
                if(args[2].equalsIgnoreCase("add")){
                    //Check if player is already on the allow list and return if true
                    if(AdvancedMaintenance.getInstance().getServerState().getAllowedPlayers().contains(Bukkit.getOfflinePlayer(args[1]).getUniqueId())){
                        commandSender.sendMessage(prefix + "§r §aNothing changed! The player was already allowed.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                        }
                        return true;
                    }
                    AdvancedMaintenance.getInstance().getServerState().getAllowedPlayers().add(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    commandSender.sendMessage(prefix + "§r §aAdded player to maintenance allow list.");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                    }
                } else if (args[2].equalsIgnoreCase("remove")) {
                    //Check if player is already removed from the allow list and return if true
                    if(!AdvancedMaintenance.getInstance().getServerState().getAllowedPlayers().contains(Bukkit.getOfflinePlayer(args[1]).getUniqueId())){
                        commandSender.sendMessage(prefix + "§r §aNothing changed! The player wasn't allowed.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                        }
                        return true;
                    }
                    AdvancedMaintenance.getInstance().getServerState().getAllowedPlayers().remove(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    commandSender.sendMessage(prefix + "§r §aRemoved player from maintenance allow list.");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                    }
                } else if (args[2].equalsIgnoreCase("check")) {
                    //Check if player is contained in any groups
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    ArrayList<Group> containedGroups = new ArrayList<>();
                    for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                        if(group.getMembers().contains(offlinePlayer.getUniqueId())){
                            containedGroups.add(group);
                        }
                    }

                    StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for (Group item : containedGroups)
                    {
                        if (first)
                            first = false;
                        else
                            builder.append("§7,§6 ");
                        builder.append(item.getName());
                    }

                    commandSender.sendMessage(prefix + "§r §aInfo of Player §6" + offlinePlayer.getName());
                    commandSender.sendMessage(prefix + "§r §aOn allow list: §6" + AdvancedMaintenance.getInstance().getServerState().getAllowedPlayers().contains(offlinePlayer.getUniqueId()));
                    commandSender.sendMessage(prefix + "§r §aIn group(s): §6" + (containedGroups.toArray().length > 0 ? builder.toString() : "None"));
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                    }
                }
            } else if (args[0].equalsIgnoreCase("group")) {
                if(!(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("delete"))){
                    commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }

                if(args[1].equalsIgnoreCase("create")){
                    //Checking if group exists
                    for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                        if (group.getName().equalsIgnoreCase(args[2])) {
                            commandSender.sendMessage("§cGroup already exists.");
                            if(senderIsPlayer){
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                            }
                            return true;
                        }
                    }

                    AdvancedMaintenance.getInstance().getServerState().getGroups().add(new Group(args[2]));
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                } else if (args[1].equalsIgnoreCase("delete")) {
                    Group groupToDelete = null;

                    //Checking if group exists
                    for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                        if (group.getName().equalsIgnoreCase(args[2])) {
                            groupToDelete = group;
                            break;
                        }

                    }

                    if(groupToDelete == null){
                        commandSender.sendMessage("§cGroup doesn't exists.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                        }
                        return true;
                    }

                    AdvancedMaintenance.getInstance().getServerState().getGroups().remove(groupToDelete);
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                }

                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                }
            }
        } else if (args.length == 4) {
            if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("manage")){
                Group currentGroup = null;

                //Checking if group exists
                for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                    if (group.getName().equalsIgnoreCase(args[2])) {
                        currentGroup = group;
                        break;
                    }

                }

                if(currentGroup == null){
                    commandSender.sendMessage("§cGroup doesn't exists.");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }

                if(args[3].equalsIgnoreCase("enable")){
                    if(currentGroup.active){
                        commandSender.sendMessage(prefix + "§r §aNothing changed! Group already was active.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                        }
                        return true;
                    }
                    currentGroup.active = true;
                } else if (args[3].equalsIgnoreCase("disable")) {
                    if(!currentGroup.active){
                        commandSender.sendMessage(prefix + "§r §aNothing changed! Group was not active.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                        }
                        return true;
                    }
                    currentGroup.active = false;
                }else {
                    commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }

                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, 20, 20);
                }
            }else {
                commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                }
                return true;
            }
        } else if (args.length == 5) {
            if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("manage")){
                Group currentGroup = null;

                //Checking if group exists
                for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                    if (group.getName().equalsIgnoreCase(args[2])) {
                        currentGroup = group;
                        break;
                    }

                }

                if(currentGroup == null){
                    commandSender.sendMessage("§cGroup doesn't exists.");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }

                if(args[3].equalsIgnoreCase("add")){
                    if(currentGroup.getMembers().contains(Bukkit.getOfflinePlayer(args[4]).getUniqueId())){
                        commandSender.sendMessage("§cNothing changed. Player was already in group.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                        }
                        return true;
                    }
                    currentGroup.getMembers().add(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                } else if (args[3].equalsIgnoreCase("remove")) {
                    if(!currentGroup.getMembers().contains(Bukkit.getOfflinePlayer(args[4]).getUniqueId())){
                        commandSender.sendMessage("§cNothing changed. Player wasn't in group.");
                        if(senderIsPlayer){
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                        }
                        return true;
                    }

                    currentGroup.getMembers().remove(Bukkit.getOfflinePlayer(args[4]).getUniqueId());
                    commandSender.sendMessage(prefix + "§r §aSuccess!");
                }else {
                    commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                    if(senderIsPlayer){
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                    }
                    return true;
                }

                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                }
            }else {
                commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
                if(senderIsPlayer){
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
                }
                return true;
            }
        }else {
            commandSender.sendMessage("§cInvalid Usage. Try /maintenance help");
            if(senderIsPlayer){
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 20,20);
            }
            return true;
        }

        //Auto save
        if(AdvancedMaintenance.getInstance().getServerState().autoSave){
            AdvancedMaintenance.getInstance().getServerState().save();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> returnList = new ArrayList<>();

        String currentArg = args[args.length - 1];


        if(args.length == 1){
            //First command arg
            list.add("help");
            list.add("set");
            list.add("player");
            list.add("group");
            list.add("save");
        }else if(args.length == 2){
            //Second command arg

            //Checking first arg so second arg can be responsive
            if(args[0].equalsIgnoreCase("set")){
                list.add("on");
                list.add("off");
            }else if(args[0].equalsIgnoreCase("player")){
                for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                    list.add(player.getName());
                }
            }else if(args[0].equalsIgnoreCase("group")){
                list.add("create");
                list.add("delete");
                list.add("list");
                list.add("manage");
            }
        }else if (args.length == 3){
            //Third command arg

            if(args[0].equalsIgnoreCase("player")){
                list.add("add");
                list.add("remove");
                list.add("check");
            } else if (args[0].equalsIgnoreCase("group") && (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("manage"))) {
                for (Group group : AdvancedMaintenance.getInstance().getServerState().getGroups()) {
                    list.add(group.getName().toLowerCase());
                }
            }
        } else if (args.length == 4) {
            if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("manage")){
                list.add("add");
                list.add("remove");
                list.add("enable");
                list.add("disable");
            }
        } else if (args.length == 5) {
            if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("manage")){
                if(args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("remove")){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        list.add(player.getName());
                    }
                }
            }
        }

        for (String s : list){
            String s1 = s.toLowerCase();
            if(s1.startsWith(currentArg)){
                returnList.add(s);
            }
        }

        return returnList;
    }
}
