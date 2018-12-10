package samueljdecanio.com.mynotify;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.UUID;

public class NotificationActivity extends SingleFragmentActivity {

    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    public static Intent newIntent(Context packageContext, UUID notificationId) {
        Intent intent = new Intent(packageContext, NotificationActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID notificationId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTIFICATION_ID);
        return NotificationFragment.newInstance(notificationId);
    }
}
