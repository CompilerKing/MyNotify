package samueljdecanio.com.mynotify;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
    }

    public List<OurNotification> getOurNotifications() {
        return this.mNotifications;
    }
}
