package samueljdecanio.com.mynotify;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationListActivityFragment extends Fragment {

    private RecyclerView mNotificationRecyclerView;
    private NotificationAdapter mAdapter;

    public NotificationListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_notification_list, container, false);

        mNotificationRecyclerView = view.findViewById(R.id.notifcation_recycler_view);
        mNotificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OurNotification mNotification;
        private TextView notificationTitle;
        private TextView alarmTimeText;
        private ImageView hasLinkIcon;
        private ImageView hasPhotoIcon;

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));
            notificationTitle = itemView.findViewById(R.id.notification_title);
            alarmTimeText = itemView.findViewById(R.id.alarm_time_text);
            hasLinkIcon = itemView.findViewById(R.id.has_link_icon);
            hasPhotoIcon = itemView.findViewById(R.id.has_image_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(OurNotification notification) {
            mNotification = notification;
            notificationTitle.setText(mNotification.getTitle());
            alarmTimeText.setText(mNotification.getTimeOfNotificationAsString());

            Log.i("bind","title: " + mNotification.getTitle() +
                    " hasLink:" + mNotification.getHasLink() +
                    " hasPhoto: " + mNotification.getHasPhoto());

            if(mNotification.getHasLink()) {
                hasLinkIcon.setVisibility(View.VISIBLE);
            }
            else {
                hasLinkIcon.setVisibility(View.INVISIBLE);
            }

            if(mNotification.getHasPhoto()) {
                hasPhotoIcon.setVisibility(View.VISIBLE);
            }
            else {
                hasPhotoIcon.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = NotificationActivity.newIntent(getActivity(), mNotification.getId());
            startActivity(intent);
        }
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
        private List<OurNotification> mNotifications;

        public NotificationAdapter(List<OurNotification> notifications) {
            mNotifications = notifications;
        }

        @Override
        public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new NotificationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(NotificationHolder holder, int position) {
            OurNotification notification = mNotifications.get(position);
            holder.bind(notification);

        }

        @Override
        public int getItemCount() {
            return mNotifications.size();
        }

        public void setNotifications(List<OurNotification> notifications) {
            mNotifications = notifications;
        }
    }

    private void updateUI() {
        NotificationHelper helper = NotificationHelper.get(getActivity());
        List<OurNotification> notifications = helper.getOurNotifications();

        if (mAdapter == null) {
            mAdapter = new NotificationAdapter(notifications);
            mNotificationRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotifications(notifications);
            mAdapter.notifyDataSetChanged();
        }
    }
}
