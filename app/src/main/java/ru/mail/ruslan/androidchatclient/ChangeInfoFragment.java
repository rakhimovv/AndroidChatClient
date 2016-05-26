package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangeInfoFragment extends Fragment {
    private Controller mController;
    private String mUserId;
    private String mSessionId;
    private String mLogin;
    private ImageView mAvatar;
    private EditText mLoginEditText;
    private EditText mStatusEditText;
    private Button mSaveButton;

    public static ChangeInfoFragment newInstance(Controller controller,
                                                 String userId,
                                                 String sessionId,
                                                 String login) {
        ChangeInfoFragment fragment = new ChangeInfoFragment();
        fragment.mController = controller;
        fragment.mUserId = userId;
        fragment.mSessionId = sessionId;
        fragment.mLogin = login;
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
        View view = inflater.inflate(R.layout.fragment_change_info, container, false);
        mAvatar = (ImageView) view.findViewById(R.id.avatar);
        mLoginEditText = (EditText) view.findViewById(R.id.login);
        mLoginEditText.setText(mLogin);
        mStatusEditText = (EditText) view.findViewById(R.id.status);
        mSaveButton = (Button) view.findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus = mStatusEditText.getText().toString().trim();
                if (newStatus.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_status, Toast.LENGTH_SHORT).show();
                } else {
                    mController.changeUserInfo(newStatus, mUserId, mSessionId);
                }
            }
        });
        return view;
    }

    public static String tag() {
        return ChangeInfoFragment.class.getSimpleName();
    }
}
