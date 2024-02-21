package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.effects.MachineEffectModel;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.utils.StringUtils;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CubeletMachineHandler {

    private HashMap<Location, CubeletMachine> machines;
    private File file;
    private YamlConfiguration config;

    private Main main;

    public CubeletMachineHandler(Main main) {
        this.main = main;
        this.machines = new HashMap<Location, CubeletMachine>();
    }

    public HashMap<Location, CubeletMachine> getMachines() {
        return machines;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public CubeletMachine getMachineByLocation(Location loc) {
        return machines.get(loc);
    }

    public void createMachine(Location loc, double blockHeight) {
        CubeletMachine machine = new CubeletMachine(loc, blockHeight, blockHeight, Rotation.SOUTH);
        machines.put(loc, machine);
        main.getHologramImplementation().loadHolograms(machine);

        config.set("machines", new ArrayList<>());

        int i = 1;
        for(CubeletMachine bx : machines.values()) {
            config.set("machines." + i + ".location.world", bx.getLocation().getWorld().getName());
            config.set("machines." + i + ".location.x", bx.getLocation().getBlockX());
            config.set("machines." + i + ".location.y", bx.getLocation().getBlockY());
            config.set("machines." + i + ".location.z", bx.getLocation().getBlockZ());
            config.set("machines." + i + ".rotation", bx.getRotation().toString());
            config.set("machines." + i + ".blockHeight", bx.getBlockHeight());
            config.set("machines." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
            config.set("machines." + i + ".idleEffect.type", bx.getBlockEffectModel().name());
            config.set("machines." + i + ".idleEffect.particle", bx.getBlockEffectParticle().getParticle().name());
            i++;
        }

        saveConfig();
    }

    public void removeMachine(Location loc) {

        if(machines.containsKey(loc)) {

            CubeletMachine machine = getMachineByLocation(loc);
            main.getHologramImplementation().removeHolograms(machine);

            main.getHologramImplementation().clearHolograms(machine);

            machines.remove(loc);

            config.set("machines", new ArrayList<>());

            int i = 1;
            for(CubeletMachine bx : machines.values()) {
                config.set("machines." + i + ".location.world", bx.getLocation().getWorld().getName());
                config.set("machines." + i + ".location.x", bx.getLocation().getBlockX());
                config.set("machines." + i + ".location.y", bx.getLocation().getBlockY());
                config.set("machines." + i + ".location.z", bx.getLocation().getBlockZ());
                config.set("machines." + i + ".rotation", bx.getRotation().toString());
                config.set("machines." + i + ".blockHeight", bx.getBlockHeight());
                config.set("machines." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
                config.set("machines." + i + ".idleEffect.type", bx.getBlockEffectModel().name());
                config.set("machines." + i + ".idleEffect.particle", bx.getBlockEffectParticle().getParticle().name());
                i++;
            }

            saveConfig();
        }

    }

    public void saveMachines() {
        config.set("machines", new ArrayList<>());

        int i = 1;
        for(CubeletMachine bx : machines.values()) {
            config.set("machines." + i + ".location.world", bx.getLocation().getWorld().getName());
            config.set("machines." + i + ".location.x", bx.getLocation().getBlockX());
            config.set("machines." + i + ".location.y", bx.getLocation().getBlockY());
            config.set("machines." + i + ".location.z", bx.getLocation().getBlockZ());
            config.set("machines." + i + ".rotation", bx.getRotation().toString());
            config.set("machines." + i + ".blockHeight", bx.getBlockHeight());
            config.set("machines." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
            config.set("machines." + i + ".idleEffect.type", bx.getBlockEffectModel().name());
            config.set("machines." + i + ".idleEffect.particle", bx.getBlockEffectParticle().getParticle().name());
            i++;
        }

        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMachines() {

        machines.clear();

        File file = new File(main.getDataFolder(), "machines.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                config.set("machines", new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        this.config = config;

        if(!config.contains("machines")) {
            config.set("machines", new ArrayList<>());
        }

        saveConfig();

        Main.log.sendMessage(Utils.translate(""));
        Main.log.sendMessage(Utils.translate("  &eLoading machines:"));

        if(config.contains("machines")) {
            if(config.getConfigurationSection("machines") != null) {
                for (int i = 1; i <= config.getConfigurationSection("machines").getKeys(false).size(); i++) {
                    String world = config.getString("machines." + i + ".location.world");
                    int x = config.getInt("machines." + i + ".location.x");
                    int y = config.getInt("machines." + i + ".location.y");
                    int z = config.getInt("machines." + i + ".location.z");

                    if(Bukkit.getServer().getWorld(world) == null) continue;

                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                    Rotation rotation = Rotation.SOUTH;
                    if(config.contains("machines." + i + ".rotation"))
                        rotation = Rotation.valueOf(config.getString("machines." + i + ".rotation"));

                    double blockHeight = 0.875;
                    if(config.contains("machines." + i + ".blockHeight"))
                        blockHeight = config.getDouble("machines." + i + ".blockHeight");

                    double permanentBlockHeight = blockHeight;
                    if(config.contains("machines." + i + ".permanentBlockHeight"))
                        permanentBlockHeight = config.getDouble("machines." + i + ".permanentBlockHeight");

                    if(config.contains("machines." + i + ".permanentBlockHeight"))
                        permanentBlockHeight = config.getDouble("machines." + i + ".permanentBlockHeight");

                    CubeletMachine cubeletMachine = new CubeletMachine(loc, blockHeight, permanentBlockHeight, rotation);

                    if(config.contains("machines." + i + ".idleEffect.type")) {
                        MachineEffectModel effectModel = StringUtils.getEnum(config.getString("machines." + i + ".idleEffect.type"), MachineEffectModel.class).orElse(MachineEffectModel.NONE);
                        cubeletMachine.setBlockEffectModel(effectModel);
                    }

                    if(config.contains("machines." + i + ".idleEffect.particle")) {
                        String sParticle = config.getString("machines." + i + ".idleEffect.particle").equalsIgnoreCase("NONE") ? "FLAME" : config.getString("machines." + i + ".idleEffect.particle");
                        Particle particle = Particle.valueOf(sParticle);
                        SimpleParticle simpleParticle = SimpleParticle.of(particle).parseData("");

                        cubeletMachine.setBlockEffectParticle(simpleParticle);
                    }

                    machines.put(loc, cubeletMachine);
                }
            }

            config.set("machines", new ArrayList<>());
            int i = 1;
            for(CubeletMachine bx : machines.values()) {
                if(bx.getLocation().getWorld() == null) continue;
                config.set("machines." + i + ".location.world", bx.getLocation().getWorld().getName());
                config.set("machines." + i + ".location.x", bx.getLocation().getBlockX());
                config.set("machines." + i + ".location.y", bx.getLocation().getBlockY());
                config.set("machines." + i + ".location.z", bx.getLocation().getBlockZ());
                config.set("machines." + i + ".rotation", bx.getRotation().toString());
                config.set("machines." + i + ".blockHeight", bx.getBlockHeight());
                config.set("machines." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
                config.set("machines." + i + ".idleEffect.type", bx.getBlockEffectModel().name());
                config.set("machines." + i + ".idleEffect.particle", bx.getBlockEffectParticle().getParticle().name());
                i++;
            }
            saveConfig();
        }

        if(machines.size() == 0)
            Main.log.sendMessage(Utils.translate("    &cNo Cubelet Machines has been loaded!"));
        else
            Main.log.sendMessage(Utils.translate("    &b" + machines.size() + " &aCubelet Machines loaded!"));

    }

}
