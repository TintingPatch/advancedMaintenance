package com.tintingpatch.advancedmaintenance.object;

import com.tintingpatch.advancedmaintenance.AdvancedMaintenance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class ServerState {
    boolean inMaintenance;
    public boolean autoSave;

    ArrayList<Group> groups;

    ArrayList<UUID> allowedPlayers;

    public ServerState(){
        inMaintenance = AdvancedMaintenance.getInstance().getConfig().getBoolean("inMaintenance");
        inMaintenance = AdvancedMaintenance.getInstance().getConfig().getBoolean("autoSave");
        groups = new ArrayList<>();
        allowedPlayers = new ArrayList<>();
    }

    public void save(){
        AdvancedMaintenance.getInstance().reloadConfig();

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
