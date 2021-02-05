package com.example.awesometimer.Activities;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.awesometimer.Data.AppDatabase;
import com.example.awesometimer.Data.ItemDao;
import com.example.awesometimer.Models.Item;
import com.example.awesometimer.R;

import java.util.Locale;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        SwitchPreferenceCompat listPreferenceMode = findPreference("theme");
        ListPreference listPreferenceFont = findPreference("font");
        ListPreference listPreferenceLanguage = findPreference("locale");
        Preference preferenceDelete = findPreference("delete");

        listPreferenceLanguage.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setDefaultValue(newValue);
            Locale locale = new Locale(newValue.toString());
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            getActivity().getResources().updateConfiguration(configuration, null);
            getActivity().recreate();
            return true;
        });

        listPreferenceMode.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setDefaultValue(newValue);
            getActivity().recreate();
            return true;
        });

        listPreferenceFont.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setDefaultValue(newValue);
            Configuration configuration = getActivity().getResources().getConfiguration();
            configuration.fontScale = Float.parseFloat(newValue.toString());
            getActivity().getResources().updateConfiguration(configuration, null);
            getActivity().recreate();
            return true;
        });

        preferenceDelete.setOnPreferenceClickListener(preference -> {
            new deleteAllAsyncTask(AppDatabase.getDatabase(getActivity())).execute();
            Toast.makeText(getActivity(),"Deleting all records...",Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase db;
        deleteAllAsyncTask(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.clearAllTables();
            return null;
        }
    }


}