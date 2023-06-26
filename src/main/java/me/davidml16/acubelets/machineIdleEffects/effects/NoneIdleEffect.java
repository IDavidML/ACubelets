package me.davidml16.acubelets.machineIdleEffects.effects;

import me.davidml16.acubelets.machineIdleEffects.IdleEffect;
import me.davidml16.acubelets.machineIdleEffects.IdleEffectAnimation;

public class NoneIdleEffect extends IdleEffectAnimation {

    public NoneIdleEffect(IdleEffect idleEffect) {
        super(idleEffect);
    }

    @Override
    public void start() {}

}
