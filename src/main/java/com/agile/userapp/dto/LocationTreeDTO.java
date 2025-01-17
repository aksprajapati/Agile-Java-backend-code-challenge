package com.agile.userapp.dto;

import java.util.ArrayList;
import java.util.List;

public class LocationTreeDTO {
    private String name;
    private List<LocationTreeDTO> children = new ArrayList<>();

    public LocationTreeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<LocationTreeDTO> getChildren() {
        return children;
    }

    public void addChild(LocationTreeDTO child) {
        this.children.add(child);
    }
}
