package samueljdecanio.com.mynotify;

import java.util.UUID;

public class OurNotification {
    private UUID id;
    private String title;
    private String details;
    private int timeOfNotification; //using unix timestamp
    private boolean hasPhoto;
    private boolean hasLink;

    //TODO: This will need to be modified to accommodate real data
    public OurNotification(String title, String details,
                           int timeOfNotification, boolean hasLink,
                           boolean hasPhoto) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.details = details;
        this.timeOfNotification = timeOfNotification;
        this.hasLink = hasLink;
        this.hasPhoto = hasPhoto;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    //TODO: Change time functions to return correct time format
    public int getTimeOfNotification() {
        return timeOfNotification;
    }

    //TODO: Change time function to take in correct time format
    public void setTimeOfNotification(int timeOfNotification) {
        this.timeOfNotification = timeOfNotification;
    }

    //TODO: Change this too
    public String getTimeOfNotificationAsString() {
        return Integer.toString(timeOfNotification);
    }

    public boolean getHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public boolean getHasLink() {
        return hasLink;
    }

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;
    }
}
