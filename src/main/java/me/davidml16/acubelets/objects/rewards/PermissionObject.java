package me.davidml16.acubelets.objects.rewards;

public class PermissionObject {

    private String id;
    private String permission;

    public PermissionObject(String id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "CommandObject{" +
                "id='" + id + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }

}
