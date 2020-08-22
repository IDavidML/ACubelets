package me.davidml16.acubelets.animations;

public class AnimationSettings {

    private String id;
    private String displayName;
    private String previewURL;

    private boolean outlineParticles;
    private boolean floorParticles;
    private boolean aroundParticles;

    public AnimationSettings(String id, String displayName, String previewURL) {
        this(id, displayName, previewURL, true, true);
    }

    public AnimationSettings(String id, String displayName, String previewURL, boolean outline, boolean floor) {
        this.id = id;
        this.displayName = displayName;
        this.previewURL = previewURL;
        this.outlineParticles = outline;
        this.floorParticles = floor;
    }

    public AnimationSettings(String id, String displayName, String previewURL, boolean around) {
        this.id = id;
        this.displayName = displayName;
        this.previewURL = previewURL;
        this.aroundParticles = around;
        this.outlineParticles = true;
        this.floorParticles = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public boolean isOutlineParticles() { return outlineParticles; }

    public void setOutlineParticles(boolean outlineParticles) { this.outlineParticles = outlineParticles; }

    public boolean isFloorParticles() { return floorParticles; }

    public void setFloorParticles(boolean floorParticles) { this.floorParticles = floorParticles; }

    public boolean isAroundParticles() { return aroundParticles; }

    public void setAroundParticles(boolean aroundParticles) { this.aroundParticles = aroundParticles; }

}
