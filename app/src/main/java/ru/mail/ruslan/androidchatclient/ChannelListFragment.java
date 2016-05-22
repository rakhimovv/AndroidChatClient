package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.adapter.ChannelListAdapter;
import ru.mail.ruslan.androidchatclient.msg.response.Channel;

public class ChannelListFragment extends ListFragment {
    public static final String TAG = "AuthFragment";

    private Controller mController;
    private List<Channel> mChannels;
    private ChannelListAdapter mAdapter;
    private TextView mNoConnectionStub;

    public static ChannelListFragment newInstance(Controller controller, List<Channel> channels) {
        ChannelListFragment fragment = new ChannelListFragment();
        fragment.mController = controller;
        fragment.mChannels = channels;
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
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mNoConnectionStub = (TextView) view.findViewById(R.id.no_connection_stub);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChannelListAdapter(getActivity(), mChannels);
        setListAdapter(mAdapter);
        //registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.channel_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.leave_channel: {
                Channel channel = mAdapter.getItem(info.position);
                return true;
            }
            default: {
                return super.onContextItemSelected(item);
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Channel channel = mAdapter.getItem(position);
        mController.showChannelFragment(channel, true);
    }
}