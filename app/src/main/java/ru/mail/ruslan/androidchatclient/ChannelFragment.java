package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.adapter.ChannelMessageAdapter;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LastMessage;
import ru.mail.ruslan.androidchatclient.msg.response.User;

public class ChannelFragment extends ListFragment {

    private Controller mController;
    private String mUserId;
    private String mSessionId;
    private String mChannelId;

    private List<User> mChannelUsers;
    private List<ChannelMessage> mChannelMessages;

    private ChannelMessageAdapter mAdapter;

    private Button mChannelUsersButton;
    private EditText mMessage;
    private ImageView mSendButton;

    public static ChannelFragment newInstance(Controller controller,
                                              String userId,
                                              String sessionId,
                                              String channelId,
                                              List<User> users,
                                              List<LastMessage> lastMessages) {
        ChannelFragment fragment = new ChannelFragment();
        fragment.mUserId = userId;
        fragment.mSessionId = sessionId;
        fragment.mChannelId = channelId;
        fragment.mChannelUsers = users;
        fragment.mChannelMessages = new ArrayList<>();
        for (LastMessage lastMessage : lastMessages) {
            fragment.mChannelMessages.add(new ChannelMessage(lastMessage));
        }
        fragment.mController = controller;
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
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        mChannelUsersButton = (Button) view.findViewById(R.id.btn_channel_users);
        mMessage = (EditText) view.findViewById(R.id.message);
        mSendButton = (ImageView) view.findViewById(R.id.btn_send);

        mChannelUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.fragmentReplacer.showUserListFragment(mChannelUsers, true);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageBody = mMessage.getText().toString();
                if (!messageBody.isEmpty()) {
                    mController.send(messageBody, mChannelId, mUserId, mSessionId);
                    mMessage.setText(null);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChannelMessageAdapter(getActivity(), mUserId, mChannelMessages);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ChannelMessage message = mAdapter.getItem(position);
        if (mUserId.equals(message.from)) {
            mController.fragmentReplacer.showChangeUserInfoFragment(true);
        } else {
            mController.getUserInfo(message.from, mUserId, mSessionId);
        }
    }

    public void processMessage(String channelId, ChannelMessage message) {
        if (mChannelId != null && mChannelId.equals(channelId)) {
            mAdapter.add(message);
        }
    }

    public static String tag(String channelId) {
        return ChannelFragment.class.getSimpleName() + "_" + channelId;
    }
}