package samueljdecanio.com.mynotify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NotificationTriggersActivity extends SingleFragmentActivity{
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    public static Intent newIntent(Context packageContext, UUID notificationId) {
        Intent intent = new Intent(packageContext, NotificationTriggersActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID notificationId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTIFICATION_ID);
        return NotificationTriggersFragment.newInstance(notificationId);
    }
}
