package com.example.gebetsapp.ui.Quibla;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gebetsapp.R;
import com.example.gebetsapp.helper.CompassActivity;
import com.hassanjamil.hqibla.Constants;

public class QuiblaFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quibla, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getActivity(), CompassActivity.class);
        intent.putExtra(Constants.TOOLBAR_TITLE, "Quibla");		// Toolbar Title
        intent.putExtra(Constants.TOOLBAR_BG_COLOR, "#FF4AA02C");		// Toolbar Background color
        intent.putExtra(Constants.TOOLBAR_TITLE_COLOR, "#FF000000");	// Toolbar Title color
        intent.putExtra(Constants.COMPASS_BG_COLOR, "#FFFFFFFF");		// Compass background color
        intent.putExtra(Constants.ANGLE_TEXT_COLOR, "#FF000000");		// Angle Text color
        intent.putExtra(Constants.DRAWABLE_DIAL, R.drawable.dial);	// Your dial drawable resource
        intent.putExtra(Constants.DRAWABLE_QIBLA, R.drawable.qibla); 	// Your qibla indicator drawable resource
        intent.putExtra(Constants.FOOTER_IMAGE_VISIBLE, View.VISIBLE/*|View.INVISIBLE|View.GONE*/);	// Footer World Image visibility
        intent.putExtra(Constants.LOCATION_TEXT_VISIBLE, View.VISIBLE/*|View.INVISIBLE|View.GONE*/); // Location Text visibility
        startActivity(intent);

        return root;
    }
}