package me.davidml16.acubelets.machineIdleEffects;

import org.bukkit.Particle;

public enum IdleEffectType {
    NONE;

    public static boolean isValid(String name) {
        for(IdleEffectType type : IdleEffectType.values()) {
            if(type.name().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static boolean isValidParticle(String name) {
        for(Particle particle : Particle.values()) {
            if(particle.name().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

}
