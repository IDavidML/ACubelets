package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Cubelet;

import java.util.Comparator;

public class CubeletDateComparator implements Comparator<Cubelet> {

    @Override
    public int compare(Cubelet o1, Cubelet o2) {

        try {

            if(o1.getReceived() > o2.getReceived())
                return -1;
            else if(o1.getReceived() < o2.getReceived())
                return 1;
            else
                return 0;

        } catch (ArithmeticException | IllegalArgumentException e) {

            return -1;

        }

    }

}
