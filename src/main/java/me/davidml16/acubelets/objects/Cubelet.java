package me.davidml16.acubelets.objects;

import java.util.UUID;

public class Cubelet {

    private UUID uuid;
    private String type;
    private Long date;

    public Cubelet(UUID uuid, String type, Long date) {
        this.uuid = uuid;
        this.type = type;
        this.date = date;
    }

    public Cubelet(String type, Long date) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.date = date;
    }

    public Cubelet(String type) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.date = System.currentTimeMillis();
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Cubelet{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }

}
