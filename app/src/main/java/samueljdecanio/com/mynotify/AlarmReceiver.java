package samueljdecanio.com.mynotify;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import static samueljdecanio.com.mynotify.NotificationListActivity.NOTIFICATION_ID;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String ARG_NOTIFICATION_ID = "notification_id";
    private OurNotification mNotification;
    private NotificationManagerCompat mNotificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "MyNotify Alarm", Toast.LENGTH_SHORT).show();
        UUID uuid = (UUID) intent.getSerializableExtra(ARG_NOTIFICATION_ID);
        mNotificationManagerCompat = NotificationManagerCompat.from(context);

        mNotification = NotificationHelper.get(context).getOurNotification(uuid);
        generateBigTextStyleNotification(context);
    }

    /*
     * Generates a BIG_TEXT_STYLE Notification that supports both phone/tablet and wear. For devices
     * on API level 16 (4.1.x - Jelly Bean) and after, displays BIG_TEXT_STYLE. Otherwise, displays
     * a basic notification.
     */
    public void generateBigTextStyleNotification(Context context) {
        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up main Intent for notification
        //      4. Create additional Actions for the Notification
        //      5. Build and issue the notification

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(context);


        // 2. Build the BIG_TEXT_STYLE.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText(mNotification.getTitle())
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(mNotification.getDetails())
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText(mNotification.getDetails());


        // 3. Set up main Intent for notification.
        Intent notifyIntent = new Intent(context, BigTextMainActivity.class);
        notifyIntent.putExtra(ARG_NOTIFICATION_ID, mNotification.getId());
        // Sets the Activity to start in a new, empty task
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack.
        stackBuilder.addParentStack(BigTextMainActivity.class);
        // Adds the Intent to the top of the stack.
        stackBuilder.addNextIntent(notifyIntent);
        // Gets a PendingIntent containing the entire back stack.
        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        // 4. Create additional Actions (Intents) for the Notification.

        // Dismiss Action.
        Intent dismissIntent = new Intent(context, BigTextIntentService.class);
        dismissIntent.setAction(BigTextIntentService.ACTION_DISMISS);

        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_cancel_white,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();


        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        context, notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        Notification notification = notificationCompatBuilder
                // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
                .setStyle(bigTextStyle)
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle(mNotification.getTitle())
                // Content for API <24 (7.0 and below) devices.
                .setContentText(mNotification.getDetails())
                .setSmallIcon(R.drawable.ic_alarm_white)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ic_alarm_white))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(mainPendingIntent)
                // Set primary color (important for Wear 2.0 Notifications).
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))

                // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
                // devices and all Wear devices. If you have more than one notification and
                // you prefer a different summary notification, set a group key and create a
                // summary notification via
                // .setGroupSummary(true)
                // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

                .setCategory(Notification.CATEGORY_REMINDER)

                // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
                // 'importance' which is set in the NotificationChannel. The integers representing
                // 'priority' are different from 'importance', so make sure you don't mix them.
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
                // visibility is set in the NotificationChannel.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Adds additional actions specified above.
                .addAction(dismissAction)

                .build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

}
