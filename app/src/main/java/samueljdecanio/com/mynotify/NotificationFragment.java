package samueljdecanio.com.mynotify;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NotificationFragment extends Fragment{

    private static final String ARG_NOTIFICATION_ID = "notification_id";


    public static NotificationFragment newInstance(UUID notificationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_ID, notificationId);

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
