package samueljdecanio.com.mynotify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.List;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            List<OurNotification> notificationList = NotificationHelper.get(context).getOurNotifications();

            for (OurNotification notif: notificationList) {

                manager.set(AlarmManager.RTC_WAKEUP, notif.getDateTimeOfNotification().getTimeInMillis(), pendingIntent);
            }
        }
    }
}
