package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Cubelet;

import java.util.Comparator;

public class CubeletDateComparator implements Comparator<Cubelet> {

    @Override
    public int compare(Cubelet o1, Cubelet o2) {
        return Math.toIntExact(o2.getDate() - o1.getDate());
    }

}
