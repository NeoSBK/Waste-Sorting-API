package com.neosbk.wastesorting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class RecycleTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Recycling tip description is required")
    private String recyclingTip;


    public RecycleTip() {}
    public RecycleTip(Long id, String ItemName, String recyclingTip) {
        this.id = id;
        this.itemName = ItemName;
        this.recyclingTip = recyclingTip;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRecyclingTip() {
        return recyclingTip;
    }

    public void setRecyclingTip(String recyclingTip) {
        this.recyclingTip = recyclingTip;
    }
}