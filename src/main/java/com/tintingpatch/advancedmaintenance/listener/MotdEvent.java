package com.tintingpatch.advancedmaintenance.listener;

import com.tintingpatch.advancedmaintenance.AdvancedMaintenance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdEvent implements Listener {
    @EventHandler
    public void OnServerPing(ServerListPingEvent event){
        if(AdvancedMaintenance.getInstance().getServerState().isInMaintenance()){
            event.setMotd(AdvancedMaintenance.getInstance().getConfig().getString("maintenanceMotd"));
        }
    }
}
