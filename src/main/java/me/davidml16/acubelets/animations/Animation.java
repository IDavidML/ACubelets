package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.data.CubeletBox;
import me.davidml16.acubelets.data.CubeletType;

public interface Animation {

    int getId();

    void start(CubeletBox box, CubeletType type);

    void stop();

}
