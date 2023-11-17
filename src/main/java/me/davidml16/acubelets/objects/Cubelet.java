package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.UUID;

public class Cubelet {

    private UUID uuid;
    private String type;
    private Long received;
    private Long expire;

    private CubeletType cubeletType;

    public Cubelet(UUID uuid, String type, Long received, Long expire) {
        this.uuid = uuid;
        this.type = type;
        this.received = received;
        this.expire = expire;

        this.cubeletType = Main.get().getCubeletTypesHandler().getTypeBydId(type);
    }

    public Cubelet(String type, Long received) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.received = received;

        this.cubeletType = Main.get().getCubeletTypesHandler().getTypeBydId(type);
    }

    public Cubelet(CubeletType type) {
        this.uuid = UUID.randomUUID();
        this.type = type.getId();
        this.received = System.currentTimeMillis();
        this.expire = type.getExpireTime() >= 0 ? received + type.getExpireTime() : -1;

        this.cubeletType = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public Long getExpire() { return expire; }

    public void setExpire(Long expire) { this.expire = expire; }

    public CubeletType getCubeletType() { return cubeletType; }

    public void setCubeletType(CubeletType cubeletType) { this.cubeletType = cubeletType; }

    @Override
    public String toString() {
        return "Cubelet{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                ", received=" + received +
                ", expire=" + expire +
                '}';
    }

}
