package com.tintingpatch.advancedmaintenance.object;

import com.tintingpatch.advancedmaintenance.AdvancedMaintenance;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ServerState {
    boolean inMaintenance;
    public boolean autoSave;

    ArrayList<Group> groups;

    ArrayList<UUID> allowedPlayers;

    public ServerState(){
        inMaintenance = AdvancedMaintenance.getInstance().getConfig().getBoolean("inMaintenance");
        autoSave = AdvancedMaintenance.getInstance().getConfig().getBoolean("autoSave");
        Bukkit.getConsoleSender().sendMessage(String.valueOf(autoSave) + String.valueOf(inMaintenance));
        groups = new ArrayList<>();
        allowedPlayers = new ArrayList<>();

        //Load groups
        ConfigurationSection groupsSection = AdvancedMaintenance.getInstance().getConfig().getConfigurationSection("groups");
        if(groupsSection != null){
            for (String key : groupsSection.getKeys(false)) {
                Group group = new Group(key);
                group.active = groupsSection.getBoolean(key + ".active");
                for (String s : groupsSection.getString(key + ".members").split(",")) {
                    if(!Objects.equals(s, "")){
                        group.getMembers().add(UUID.fromString(s));
                    }
                }
                groups.add(group);
            }
        }

        //Load allowed players
        for (String s : AdvancedMaintenance.getInstance().getConfig().getString("allowedPlayers").split(",")) {
            if(!Objects.equals(s, "")){
                allowedPlayers.add(UUID.fromString(s));
            }
        }


    }

    public void save(){
        //Updating in Maintenance
        AdvancedMaintenance.getInstance().getConfig().set("inMaintenance", inMaintenance);

        //Updating auto save
        AdvancedMaintenance.getInstance().getConfig().set("autoSave", autoSave);

        //Saving groups
        AdvancedMaintenance.getInstance().getConfig().set("groups", null);

        for (Group group : groups) {
            AdvancedMaintenance.getInstance().getConfig().set("groups." + group.getName() + ".active", group.active);

            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (UUID item : group.getMembers())
            {
                if (first)
                    first = false;
                else
                    builder.append(",");
                builder.append(item);
            }

            AdvancedMaintenance.getInstance().getConfig().set("groups." + group.getName() + ".members", builder.toString());
        }

        //Saving players
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (UUID item : allowedPlayers)
        {
            if (first)
                first = false;
            else
                builder.append(",");
            builder.append(item);
        }

        AdvancedMaintenance.getInstance().getConfig().set("allowedPlayers", builder.toString());

        AdvancedMaintenance.getInstance().saveConfig();
    }

    public boolean playerCanJoinInMaintenance(UUID uuid){

        if(allowedPlayers.contains(uuid)){
            return true;
        }

        for (Group group : getGroups()) {
            if(group.getMembers().contains(uuid)){
                return true;
            }
        }

        return false;
    }

    public void setInMaintenance(boolean inMaintenance) {
        this.inMaintenance = inMaintenance;

        if(inMaintenance){
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(!playerCanJoinInMaintenance(player.getUniqueId())){
                    player.kickPlayer(AdvancedMaintenance.getInstance().getConfig().getString("notAllowedMessage"));
                }
            }
        }

        if(autoSave){
            save();
        }
    }

    public ArrayList<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public boolean isInMaintenance() {
        return inMaintenance;
    }
}
