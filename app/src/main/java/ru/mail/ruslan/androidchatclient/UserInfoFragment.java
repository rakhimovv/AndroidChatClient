package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserInfoFragment extends Fragment {
    private String mNickname;
    private String mStatus;

    public static UserInfoFragment newInstance(String nickname, String status) {
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.mNickname = nickname;
        fragment.mStatus = status;
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
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        TextView nickname = (TextView) view.findViewById(R.id.nickname);
        TextView status = (TextView) view.findViewById(R.id.status);

        nickname.setText(mNickname);
        status.setText(mStatus);
        return view;
    }

    public static String tag() {
        return UserInfoFragment.class.getSimpleName();
    }
}
