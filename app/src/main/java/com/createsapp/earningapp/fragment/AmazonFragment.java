package com.createsapp.earningapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createsapp.earningapp.R;

public class AmazonFragment extends Fragment {

    private RadioGroup radioGroup;
    private Button withdrawBtn;
    private TextView coinsTv;


    public AmazonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amazon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

    }

    private void init(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        withdrawBtn = view.findViewById(R.id.submitBtn);
        coinsTv = view.findViewById(R.id.coinsTv);
    }

}