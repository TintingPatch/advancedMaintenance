package com.tintingpatch.advancedmaintenance;

import com.tintingpatch.advancedmaintenance.command.MaintenanceCommand;
import com.tintingpatch.advancedmaintenance.listener.MotdEvent;
import com.tintingpatch.advancedmaintenance.listener.PreJoinEvent;
import com.tintingpatch.advancedmaintenance.object.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdvancedMaintenance extends JavaPlugin {

    private static AdvancedMaintenance instance;

    private ServerState serverState;

    public final String prefix = "§7[§cMaintenance§7]";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        serverState = new ServerState();

        Bukkit.getPluginCommand("maintenance").setExecutor(new MaintenanceCommand());
        Bukkit.getPluginManager().registerEvents(new MotdEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PreJoinEvent(), this);
    }

    @Override
    public void onDisable() {
        if(serverState.autoSave){
            serverState.save();
        }
    }

    public static AdvancedMaintenance getInstance() {
        return instance;
    }

    public ServerState getServerState() {
        return serverState;
    }
}
