package com.tintingpatch.advancedmaintenance.listener;

import com.tintingpatch.advancedmaintenance.AdvancedMaintenance;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PreJoinEvent implements Listener {
    //TODO: Add blocking
    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event){
        if(!AdvancedMaintenance.getInstance().getServerState().isInMaintenance() || AdvancedMaintenance.getInstance().getServerState().playerCanJoinInMaintenance(event.getUniqueId())){
            event.allow();
        }else {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, AdvancedMaintenance.getInstance().getConfig().getString("notAllowedMessage"));
        }
    }
}
