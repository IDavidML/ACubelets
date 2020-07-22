package me.davidml16.acubelets.objects;

import java.util.List;

public class ColorAnimation {

    private List<String> colors;
    private int index;

    public ColorAnimation() {
        this.index = 0;
    }

    public ColorAnimation(List<String> colors) {
        this.colors = colors;
        this.index = 0;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String nextColor() {
        index++;
        if(index >= colors.size()) index = 0;
        return colors.get(index);
    }

    public void reset() {
        this.index = 0;
    }

}
