package samueljdecanio.com.mynotify;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationListFragment extends Fragment {

    private RecyclerView mNotificationRecyclerView;
    private NotificationAdapter mAdapter;

    public NotificationListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_notification_list, container, false);

        mNotificationRecyclerView = view.findViewById(R.id.notifcation_recycler_view);
        mNotificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                mAdapter.removeItem(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mNotificationRecyclerView);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OurNotification mNotification;
        private TextView notificationTitle;
        private TextView alarmTimeText;
        private ImageView hasLinkIcon;
        private ImageView hasPhotoIcon;
        public ConstraintLayout viewForeground;

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));
            notificationTitle = itemView.findViewById(R.id.notification_title);
            alarmTimeText = itemView.findViewById(R.id.alarm_time_text);
            hasLinkIcon = itemView.findViewById(R.id.has_link_icon);
            hasPhotoIcon = itemView.findViewById(R.id.has_image_icon);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            itemView.setOnClickListener(this);
        }

        public void bind(OurNotification notification) {
            mNotification = notification;
            notificationTitle.setText(mNotification.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss");
            alarmTimeText.setText(sdf.format(mNotification.getDateTimeOfNotification().getTime()));

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

        public void removeItem(int position) {
            NotificationHelper.get(getContext()).deleteNotification(mNotifications.get(position));
            mNotifications.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(getContext(), "Notification Deleted", Toast.LENGTH_LONG).show();
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
