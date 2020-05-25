package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.enums.CraftType;

public class CraftIngredient {

    private String parentType;
    private CraftType craftType;
    private String name;
    private int amount;

    public CraftIngredient(String parentType, CraftType craftType, int amount) {
        this(parentType, craftType, "", amount);
    }

    public CraftIngredient(String parentType, CraftType craftType, String name, int amount) {
        this.parentType = parentType;
        this.craftType = craftType;
        this.name = name;
        this.amount = amount;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public CraftType getCraftType() {
        return craftType;
    }

    public void setCraftType(CraftType craftType) {
        this.craftType = craftType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
