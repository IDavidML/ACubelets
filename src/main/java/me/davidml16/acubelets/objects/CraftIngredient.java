package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.enums.CraftType;

import java.util.UUID;

public class CraftIngredient {

    private UUID uuid;
    private String parentType;
    private CraftType craftType;
    private String name;
    private int amount;

    public CraftIngredient(String parentType, CraftType craftType, int amount) {
        this(parentType, craftType, "", amount);
    }

    public CraftIngredient(String parentType, CraftType craftType, String name, int amount) {
        this.uuid = UUID.randomUUID();
        this.parentType = parentType;
        this.craftType = craftType;
        this.amount = amount;

        if(craftType == CraftType.CUBELET)
            this.name = name;
        else if(craftType == CraftType.MONEY)
            this.name = "MONEY";
        else if(craftType == CraftType.POINTS)
            this.name = "POINTS";
    }

    public UUID getUuid() { return uuid; }

    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public String getParentType() { return parentType; }

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
