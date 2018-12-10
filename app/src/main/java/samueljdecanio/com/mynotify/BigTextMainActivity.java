package samueljdecanio.com.mynotify;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class BigTextMainActivity extends Activity {
    private static final String ARG_NOTIFICATION_ID = "notification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        UUID notificationId = (UUID) intent.getSerializableExtra(ARG_NOTIFICATION_ID);

        // Cancel Notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NotificationListActivity.NOTIFICATION_ID);

        Intent notificationIntent = NotificationActivity.newIntent(this, notificationId);
        startActivity(notificationIntent);
    }
}
