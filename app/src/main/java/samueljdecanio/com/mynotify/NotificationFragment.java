package samueljdecanio.com.mynotify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.UUID;

public class NotificationFragment extends Fragment{

    private static final String ARG_NOTIFICATION_ID = "notification_id";

    private OurNotification mNotification;
    private TextView mNotificationTitle;
    private EditText mDetails;
    private EditText mLinkField;
    private Button mAddLink;
    private LinearLayout mLinksLayout;

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
        UUID notificationId = (UUID) getArguments().getSerializable(ARG_NOTIFICATION_ID);
        mNotification = NotificationHelper.get(getActivity()).getOurNotification(notificationId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifcation, container, false);

        mNotificationTitle = v.findViewById(R.id.notification_title);
        mNotificationTitle.setText(mNotification.getTitle());

        mDetails = v.findViewById(R.id.notification_details);
        mDetails.setText(mNotification.getDetails());

        mLinksLayout = v.findViewById(R.id.links_layout);

        mLinkField = v.findViewById(R.id.link_field);
        mLinkField.setHint(R.string.link_field_hint);
        MakeHyperText(mLinkField);

        mAddLink = v.findViewById(R.id.add_link_button);
        mAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                EditText newLinkView = new EditText(getContext());
                newLinkView.setLayoutParams(params);
                MakeHyperText(newLinkView);

                mLinksLayout.addView(newLinkView);
            }
        });

        return v;
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
