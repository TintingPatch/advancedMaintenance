package com.tintingpatch.advancedmaintenance.object;

import java.util.ArrayList;
import java.util.UUID;

public class Group {
    private final String name;

    private final ArrayList<UUID> members;

    public boolean active;



    public Group(String name){
        this.name = name;
        active = false;
        members = new ArrayList<>();
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }
}
