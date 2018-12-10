package samueljdecanio.com.mynotify.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import samueljdecanio.com.mynotify.OurNotification;

public class NotificationCursorWrapper extends CursorWrapper {
    public NotificationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public OurNotification getNotification() {
        String uuidString = getString(getColumnIndex(NotificationDatabaseSchema.NotificationTable.Cols.UUID));
        String title = getString(getColumnIndex(NotificationDatabaseSchema.NotificationTable.Cols.TITLE));
        long dateTime = getLong(getColumnIndex(NotificationDatabaseSchema.NotificationTable.Cols.DATE));

        OurNotification notification = new OurNotification(UUID.fromString(uuidString));
        notification.setTitle(title);

        notification.setDetails(getString(getColumnIndex(NotificationDatabaseSchema.NotificationTable.Cols.DETAILS)));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        notification.setDateTimeOfNotification(calendar);

        ArrayList<String> links = ParseLinks(getString(getColumnIndex(NotificationDatabaseSchema.NotificationTable.Cols.LINKS)));
        Log.i("NotifCursorWrapper", notification.getTitle() + "link size: " + links.size());
        if(links.size() > 0) {
            notification.setHasLink(true);
        }

        notification.setLinks(links);

        return notification;
    }

    private ArrayList<String> ParseLinks(String links) {
        ArrayList<String> listLinks;

        listLinks = new ArrayList<>(Arrays.asList(links.split(" ")));

        for (String link :
                listLinks) {
            if(link.equals("") || link.equals(" ")) {
                listLinks.remove(link);
            }
        }
        
        return listLinks;
    }
}
