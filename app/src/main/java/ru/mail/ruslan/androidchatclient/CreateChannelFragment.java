package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateChannelFragment extends Fragment {
    public static final String TAG = "CreateChannelFragment";

    private Controller mController;
    private String mUserId;
    private String mSessionId;
    private EditText mChannelName;
    private EditText mChanneDescription;
    private Button mCreateButton;

    public static CreateChannelFragment newInstance(Controller controller,
                                                    String userId,
                                                    String sessionId) {

        CreateChannelFragment fragment = new CreateChannelFragment();
        fragment.mController = controller;
        fragment.mUserId = userId;
        fragment.mSessionId = sessionId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_channel, container, false);
        mChannelName = (EditText) view.findViewById(R.id.channel_name);
        mChanneDescription = (EditText) view.findViewById(R.id.channel_description);
        mCreateButton = (Button) view.findViewById(R.id.btn_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelName = mChannelName.getText().toString().trim();
                String channelDescription = mChanneDescription.getText().toString().trim();
                if (channelName.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_channel_name, Toast.LENGTH_SHORT).show();
                } else {
                    mController.createChannel(channelName, channelDescription, mUserId, mSessionId);
                }
            }
        });
        return view;
    }

    public static String tag() {
        return CreateChannelFragment.class.getSimpleName();
    }
}

