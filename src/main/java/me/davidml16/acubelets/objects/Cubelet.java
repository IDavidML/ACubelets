package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.UUID;

public class Cubelet {

    private UUID uuid;
    private String type;
    private Long received;
    private Long expire;

    public Cubelet(UUID uuid, String type, Long received, Long expire) {
        this.uuid = uuid;
        this.type = type;
        this.received = received;
        this.expire = expire;
    }

    public Cubelet(String type, Long received) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.received = received;
    }

    public Cubelet(CubeletType type) {
        this.uuid = UUID.randomUUID();
        this.type = type.getId();
        this.received = System.currentTimeMillis();
        this.expire = received + type.getExpireTime();
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
