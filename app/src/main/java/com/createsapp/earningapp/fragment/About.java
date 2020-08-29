package com.createsapp.earningapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createsapp.earningapp.BuildConfig;
import com.createsapp.earningapp.R;

public class About extends Fragment {

    private TextView versionTv;

    public About() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        versionTv = view.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        String version = "Version " + versionName + "." + versionCode;
        versionTv.setText(version);
    }
}