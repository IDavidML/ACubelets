package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;

public interface Animation {

    int getId();

    void start(CubeletBox box, CubeletType type);

    void stop();

}
