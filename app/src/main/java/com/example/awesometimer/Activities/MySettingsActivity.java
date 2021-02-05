package com.example.awesometimer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.awesometimer.R;

import java.util.Locale;

public class MySettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new MySettingsFragment())
                .commit();
    }

    public static void SetConfigurations(Context caller){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(caller);
        float size = Float.parseFloat(sp.getString("font", "1.0"));
        String lang = sp.getString("locale", "en");
        Configuration configuration = new Configuration();
        configuration.fontScale = size;

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        configuration.setLocale(locale);

        boolean nMode = sp.getBoolean("theme",  false);
        if (nMode) {
            caller.setTheme(R.style.my_theme_dark);
        } else {
            caller.setTheme(R.style.my_theme_light);
        }
        caller.getResources().updateConfiguration(configuration, null);
    }
}