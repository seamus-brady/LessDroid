package com.corvideon.lessdroidpremium;

import com.corvideon.lessdroidpremium.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class LessDroidPrefActivity extends PreferenceActivity{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
