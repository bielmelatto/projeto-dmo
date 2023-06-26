package com.projeto.organizaidoso.ui.emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projeto.organizaidoso.R;

public class EmergencyFragment extends Fragment {

    private Button buttonCall190, buttonCall192, buttonCall193;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        buttonCall190 = view.findViewById(R.id.button_call_190);
        buttonCall192 = view.findViewById(R.id.button_call_192);
        buttonCall193 = view.findViewById(R.id.button_call_193);

        buttonCall190.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "190"));
                startActivity(intent);
            }
        });

        buttonCall192.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "192"));
                startActivity(intent);
            }
        });

        buttonCall193.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "193"));
                startActivity(intent);
            }
        });

        return view;
    }
}