package samueljdecanio.com.mynotify;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class OurNotification {
    private UUID id;
    private String title;
    private String details;
    private Calendar getDateTimeOfNotification; //using unix timestamp
    private boolean hasPhoto = false;
    private boolean hasLink = false;
    private ArrayList<String> links;

    public OurNotification(UUID uuid) {
        this.id = uuid;
        this.getDateTimeOfNotification = Calendar.getInstance();
        links = new ArrayList<>();
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

    public Calendar getDateTimeOfNotification() {
        return getDateTimeOfNotification;
    }

    public void setDateTimeOfNotification(Calendar timeOfNotification) {
        this.getDateTimeOfNotification = timeOfNotification;
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

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void removeLink(String linkToDelete) {
        for (String link : links) {
            if(link.equals(linkToDelete)) {
                links.remove(link);
                return;
            }
        }
    }

    public boolean checkIfLinkExists(String linkToCheck) {
        for (String link : links) {
            if(link.equals(linkToCheck)) {
                return true;
            }
        }

        return false;
    }

    public void addLink(String link) {
        if(!(link.equals("") || link.equals(" "))) {
            links.add(link);
        }
    }
}
