package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.loothistory.LootHistory;

import java.util.Comparator;

public class LootDateComparator implements Comparator<LootHistory> {

    @Override
    public int compare(LootHistory o1, LootHistory o2) {

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
