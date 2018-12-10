package samueljdecanio.com.mynotify;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static samueljdecanio.com.mynotify.NotificationListActivity.NOTIFICATION_ID;

public class NotificationTriggersFragment extends Fragment{

    private static final String ARG_NOTIFICATION_ID = "notification_id";

    private OurNotification mNotification;

    private TextView notificationTitle;
    private TextView dateTimeTextView;
    private Button pickTimeButton;
    private Button pickDateButton;
    private Button saveNotificationButton;

    public static NotificationTriggersFragment newInstance(UUID notificationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_ID, notificationId);

        NotificationTriggersFragment fragment = new NotificationTriggersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationHelper helper = NotificationHelper.get(getActivity());
        UUID notificationId = (UUID) getArguments().getSerializable(ARG_NOTIFICATION_ID);
        mNotification = helper.getOurNotification(notificationId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_trigger, container, false);

        notificationTitle = v.findViewById(R.id.notification_title);
        notificationTitle.setText(mNotification.getTitle());

        dateTimeTextView = v.findViewById(R.id.trigger_date_time_textview);
        updateDateTimeDisplay();



        pickDateButton = v.findViewById(R.id.trigger_set_date_button);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar notificationDateTime = mNotification.getDateTimeOfNotification();
                        notificationDateTime.set(Calendar.MONTH, selectedmonth);
                        notificationDateTime.set(Calendar.DAY_OF_MONTH, selectedday);
                        notificationDateTime.set(Calendar.YEAR, selectedyear);

                        mNotification.setDateTimeOfNotification(notificationDateTime);
                        updateDateTimeDisplay();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        pickTimeButton = v.findViewById(R.id.trigger_set_time_button);
        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar notificationDateTime = mNotification.getDateTimeOfNotification();
                        notificationDateTime.set(Calendar.HOUR, selectedHour);
                        notificationDateTime.set(Calendar.MINUTE, selectedMinute);
                        notificationDateTime.set(Calendar.SECOND, 0);

                        mNotification.setDateTimeOfNotification(notificationDateTime);
                        updateDateTimeDisplay();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        saveNotificationButton = v.findViewById(R.id.trigger_save_notification_button);
        saveNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.notification_saved, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getActivity(), NotificationListActivity.class);
                startActivity(myIntent);

                Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
                alarmIntent.putExtra(ARG_NOTIFICATION_ID, mNotification.getId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                manager.set(AlarmManager.RTC_WAKEUP, mNotification.getDateTimeOfNotification().getTimeInMillis(), pendingIntent);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        NotificationHelper.get(getActivity())
                .updateNotification(mNotification);
    }

    private void updateDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss");
        dateTimeTextView.setText(sdf.format(mNotification.getDateTimeOfNotification().getTime()));
    }


}
