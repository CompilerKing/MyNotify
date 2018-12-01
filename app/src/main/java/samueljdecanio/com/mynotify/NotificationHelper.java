package samueljdecanio.com.mynotify;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationHelper {
    private static NotificationHelper sNotificationHelper;
    private List<OurNotification> mNotifications;

    public static NotificationHelper get(Context context) {
        if(sNotificationHelper == null) {
            sNotificationHelper = new NotificationHelper(context);
        }

        return sNotificationHelper;
    }

    private NotificationHelper(Context context) {
        //TODO: Set up fake notifcations in here for sample data before creating database
        mNotifications = new ArrayList<OurNotification>();
        this.createFakeNotifications();
    }

    public List<OurNotification> getOurNotifications() {
        return this.mNotifications;
    }

    public OurNotification getOurNotification(UUID notificationId) {
        for (OurNotification notif : mNotifications) {
            if (notif.getId().equals(notificationId)) {
                return notif;
            }
        }

        return null;
    }

    private void createFakeNotifications() {
        OurNotification fake1 = new OurNotification("title1", "desc1", 100, true, true);
        OurNotification fake2 = new OurNotification("title2", "desc2", 200, true, true);
        OurNotification fake3 = new OurNotification("title3", "desc3", 300, true, true);
        OurNotification fake4 = new OurNotification("title4", "desc4", 400, true, true);

        this.mNotifications.add(fake1);
        this.mNotifications.add(fake2);
        this.mNotifications.add(fake3);
        this.mNotifications.add(fake4);
    }
}
