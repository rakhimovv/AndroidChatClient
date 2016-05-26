package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.adapter.UserListAdapter;
import ru.mail.ruslan.androidchatclient.msg.response.User;

public class UserListFragment extends ListFragment {
    private Controller mController;
    private UserListAdapter mAdapter;
    private List<User> mUsers;

    private String mUserId;
    private String mSessionId;

    public static UserListFragment newInstance(Controller controller,
                                               String userId,
                                               String sessionId,
                                               List<User> users) {
        UserListFragment fragment = new UserListFragment();
        fragment.mController = controller;
        fragment.mUsers = users;
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
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new UserListAdapter(getActivity(), mUsers);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        User user = mAdapter.getItem(position);
        mController.getUserInfo(user.uid, mUserId, mSessionId);
    }

    public static String tag() {
        return UserListFragment.class.getSimpleName();
    }
}
