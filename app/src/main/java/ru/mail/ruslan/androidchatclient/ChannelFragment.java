package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import ru.mail.ruslan.androidchatclient.msg.response.Channel;

public class ChannelFragment extends ListFragment {
    private Channel mChannel;
    private Controller mController;
    private EditText mMessage;
    private ImageView mSendButton;

    public static ChannelFragment newInstance(Controller controller, Channel channel) {
        ChannelFragment fragment = new ChannelFragment();
        fragment.mChannel = channel;
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
        mMessage = (EditText) view.findViewById(R.id.message);
        mSendButton = (ImageView) view.findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    public static String tag() {
        return ChannelFragment.class.getSimpleName();
    }
}