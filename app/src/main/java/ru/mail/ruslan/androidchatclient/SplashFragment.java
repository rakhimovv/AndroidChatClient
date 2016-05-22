package ru.mail.ruslan.androidchatclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends Fragment {
    public static final String TAG = "SplashFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeFullscreen);
        //LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        //return localInflater.inflate(R.layout.fragment_splash, container, false);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}
