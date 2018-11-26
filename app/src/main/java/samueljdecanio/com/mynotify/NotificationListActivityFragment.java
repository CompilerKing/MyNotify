package samueljdecanio.com.mynotify;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));
            itemView.setOnClickListener(this);
        }

        public void bind(OurNotification notification) {
            mNotification = notification;
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
