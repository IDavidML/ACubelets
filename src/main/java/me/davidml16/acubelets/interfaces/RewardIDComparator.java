package me.davidml16.acubelets.interfaces;

import me.davidml16.acubelets.objects.Reward;

import java.util.Comparator;

public class RewardIDComparator implements Comparator<Reward> {

    @Override
    public int compare(Reward o1, Reward o2) {
        int o1_id = Integer.parseInt(o1.getId().substring(1));
        int o2_id = Integer.parseInt(o2.getId().substring(1));

        return o1_id - o2_id;
    }

}
