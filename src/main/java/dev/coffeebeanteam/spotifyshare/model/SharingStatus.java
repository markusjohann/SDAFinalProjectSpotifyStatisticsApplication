package dev.coffeebeanteam.spotifyshare.model;

public enum SharingStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted");

    private final String displayName;

    SharingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

