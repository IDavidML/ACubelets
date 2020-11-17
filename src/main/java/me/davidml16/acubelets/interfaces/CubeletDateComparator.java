package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Cubelet;

import java.util.Comparator;

public class CubeletDateComparator implements Comparator<Cubelet> {

    @Override
    public int compare(Cubelet o1, Cubelet o2) {
        try {
            return Math.toIntExact(o2.getReceived() - o1.getReceived());
        } catch (ArithmeticException e) {
            return Math.toIntExact(getIntMaxMinLong(o2.getReceived()) - getIntMaxMinLong(o1.getReceived()));
        }
    }

    public static int getIntMaxMinLong(long longNumber){

        int intNumber = 0;

        if (longNumber < Integer.MIN_VALUE )
            intNumber = Integer.MIN_VALUE;

        else if (longNumber > Integer.MAX_VALUE)
            intNumber = Integer.MAX_VALUE;

        else
            intNumber = (int) longNumber;

        return intNumber;
    }

}
