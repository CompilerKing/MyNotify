package samueljdecanio.com.mynotify;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;
import java.util.logging.Logger;

public class NotificationFragment extends Fragment{

    private static final String ARG_NOTIFICATION_ID = "notification_id";

    private OurNotification mNotification;
    private TextView mNotificationTitle;
    private EditText mDetails;
    private EditText mLinkField;
    private Button mAddLink;
    private Button mNextTrigger;
    private LinearLayout mLinksLayout;
    private boolean isNew = false;
    private boolean isCancelled = true;

    public static NotificationFragment newInstance(UUID notificationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_ID, notificationId);

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationHelper helper = NotificationHelper.get(getActivity());
        UUID notificationId = (UUID) getArguments().getSerializable(ARG_NOTIFICATION_ID);
        mNotification = helper.getOurNotification(notificationId);

        if(mNotification == null) {
            mNotification = new OurNotification(notificationId);
            helper.addNotification(mNotification);
            isNew = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifcation, container, false);

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mNotificationTitle = v.findViewById(R.id.notification_title);
        mNotificationTitle.setText(mNotification.getTitle());
        mNotificationTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mNotification.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });


        mDetails = v.findViewById(R.id.notification_details);
        mDetails.setText(mNotification.getDetails());
        mDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mNotification.setDetails(s.toString());
                Log.i("NotificationFragment", "details changed..." + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mLinksLayout = v.findViewById(R.id.links_layout);

        for (String link :
                mNotification.getLinks()) {
            EditText newLinkView = new EditText(getContext());
            newLinkView.setLayoutParams(params);
            newLinkView.setText(link);
            MakeHyperText(newLinkView);

            mLinksLayout.addView(newLinkView);
        }

        mAddLink = v.findViewById(R.id.add_link_button);
        mAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newLinkView = new EditText(getContext());
                newLinkView.setLayoutParams(params);
                MakeHyperText(newLinkView);

                mLinksLayout.addView(newLinkView);
            }
        });



        mNextTrigger = v.findViewById(R.id.trigger_button);
        mNextTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
                hideSoftKeyboard(getActivity(), v);
                isCancelled = false;
                Intent triggerIntent = NotificationTriggersActivity.newIntent(getActivity(), mNotification.getId());
                getActivity().startActivity(triggerIntent);
            }
        });

        return v;
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isNew && isCancelled) {
            NotificationHelper.get(getActivity()).get(getActivity())
                    .deleteNotification(mNotification);
            Toast.makeText(this.getActivity(), "Notification Deleted", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            for (int i = 0; i < mLinksLayout.getChildCount(); i++) {
                View v = mLinksLayout.getChildAt(i);
                if (v instanceof EditText) {
                    String link = ((EditText) v).getText().toString();
                    boolean exists = mNotification.checkIfLinkExists(link);
                    Log.i("NotificationFragment", "exists:" + exists);

                    if(!exists) {
                        mNotification.addLink(link);
                        mNotification.setHasLink(true);
                        Log.i("NotificationFragment", "link added..." + link);
                    }
                }
            }

            Log.i("NotificationFragment", "links:");
            for (String link :
                    mNotification.getLinks()) {
                Log.i("NotificationFragment", link.toString());
            }
            
            NotificationHelper.get(getActivity()).get(getActivity())
                    .updateNotification(mNotification);
            Log.i("NotificationFragment", "Notification updated");

        }
    }

    private void MakeHyperText(EditText text) {
        //setting up hyperlink functionality
        text.setLinksClickable(true);
        text.setAutoLinkMask(Linkify.WEB_URLS);
        text.setMovementMethod(CustomMovementMethod.getInstance());
        Linkify.addLinks(text, Linkify.WEB_URLS);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Linkify.addLinks(s, Linkify.WEB_URLS);
            }
        });
    }

}
