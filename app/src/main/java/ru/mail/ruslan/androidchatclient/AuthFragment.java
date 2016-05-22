package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ruslan on 22/05/16.
 */
public class AuthFragment extends Fragment {
    public static final String TAG = "SplashFragment";

    private Controller mController;
    private AutoCompleteTextView mLogin;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mRegisterButton;

    public static AuthFragment newInstance(Controller controller) {
        AuthFragment fragment = new AuthFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLogin = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPassword = (EditText) view.findViewById(R.id.password);
        mLoginButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mRegisterButton = (Button) view.findViewById(R.id.email_register_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mLogin.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();

                if (login.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_login, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                mController.authorize(login, pass);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mController.showRegisterFragment(true);
                String login = mLogin.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();

                if (login.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_login, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.toast_empty_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                mController.register(login, login, pass);
            }
        });

        return view;
    }
}