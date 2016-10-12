package main.java.com.dar.api;

public enum DeckChair implements IAPI {
    // Returns the latest image from a camera.
    CAMERA ("http://api.deckchair.com/v1/viewer/camera/"),
    WIDTH("width="),
    HEIGHT("height="),
    QUALITY("quality="),
    RESIZE("resizeMode="),
    GRAVITY("gravity=");

    private String value;
    DeckChair(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
