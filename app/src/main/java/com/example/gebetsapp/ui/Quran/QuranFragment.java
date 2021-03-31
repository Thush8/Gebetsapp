package com.example.gebetsapp.ui.Quran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gebetsapp.R;

public class QuranFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quran, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        textView.setText("Quran coming soon");
        return root;
    }
}