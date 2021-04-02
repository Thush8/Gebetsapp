package com.example.gebetsapp.ui.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.gebetsapp.MainActivity;
import com.example.gebetsapp.R;

public class NotificationPreference extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NotificationPreference.this, MainActivity.class);
                startActivity(intent);
            }
        }); */

        addPreferencesFromResource(R.xml.notification);

    }
}
