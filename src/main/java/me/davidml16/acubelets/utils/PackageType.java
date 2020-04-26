package me.davidml16.acubelets.utils;


import org.bukkit.Bukkit;

/**
 * Represents an enumeration of dynamic packages of NMS and CraftBukkit
 * <p/>
 * This class is part of the <b>ReflectionUtils</b> and follows the same usage conditions
 *
 * @author DarkBlade12
 * @since 1.0
 */
public enum PackageType {
    MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
    CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
    CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
    CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
    CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
    CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
    CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
    CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
    CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
    CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
    CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
    CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
    CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
    CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
    CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
    CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
    CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
    CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
    CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
    CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

    private final String path;

    /**
     * Construct a new package type
     *
     * @param path Path of the package
     */
    PackageType(String path) {
        this.path = path;
    }

    /**
     * Construct a new package type
     *
     * @param parent Parent package of the package
     * @param path   Path of the package
     */
    PackageType(PackageType parent, String path) {
        this(parent + "." + path);
    }

    /**
     * Returns the path of this package type
     *
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the class with the given name
     *
     * @param className Name of the desired class
     * @return The class with the specified name
     * @throws ClassNotFoundException If the desired class with the specified name and package cannot be found
     */
    public Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(this + "." + className);
    }

    // Override for convenience
    @Override
    public String toString() {
        return path;
    }

    /**
     * Returns the version of your server
     *
     * @return The server version
     */
    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
}