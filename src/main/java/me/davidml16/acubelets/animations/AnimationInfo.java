package me.davidml16.acubelets.animations;

public class AnimationInfo {

    private String id;
    private String displayName;
    private String previewURL;

    public AnimationInfo(String id, String displayName, String previewURL) {
        this.id = id;
        this.displayName = displayName;
        this.previewURL = previewURL;
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

}
