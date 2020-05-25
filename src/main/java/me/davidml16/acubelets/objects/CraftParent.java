package me.davidml16.acubelets.objects;

import java.util.ArrayList;
import java.util.List;

public class CraftParent {

    private String cubeletType;
    private int slot;

    private List<CraftIngredient> ingrediens;

    public CraftParent(String cubeletType, int slot) {
        this.cubeletType = cubeletType;
        this.slot = slot;
        this.ingrediens = new ArrayList<>();
    }

    public CraftParent(String cubeletType, int slot, List<CraftIngredient> ingrediens) {
        this.cubeletType = cubeletType;
        this.slot = slot;
        this.ingrediens = ingrediens;
    }

    public String getCubeletType() {
        return cubeletType;
    }

    public void setCubeletType(String cubeletType) {
        this.cubeletType = cubeletType;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<CraftIngredient> getIngrediens() {
        return ingrediens;
    }

    public void setIngrediens(List<CraftIngredient> ingrediens) {
        this.ingrediens = ingrediens;
    }

}
