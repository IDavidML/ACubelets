package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Cubelet;

import java.util.Comparator;

public class CubeletTypeComparator implements Comparator<Cubelet> {

    @Override
    public int compare(Cubelet o1, Cubelet o2) {
        try {
            return o1.getType().compareTo(o2.getType());
        } catch (Exception e) {
            return -1;
        }
    }

}
