package com.corvideon.lessdroidbasic;

import java.util.Map;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.corvideon.lessdroidhome.R;

public class FragmentsTabFaves extends SherlockFragment {
	
	private static final String PREF_KEY_NO_FAVES = "NO_FAVES";
	private ListView listApps;
	private AppListingAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setContentView(R.layout.fragments_tab_faves);
		CreateListView();
	}

	private void CreateListView() {
		listApps = new ListView(getActivity());
		listApps = (ListView) getActivity().findViewById(R.id.listApps);
		adapter = new AppListingAdapter();
		Map<String, AppHolder> faveApps = LessDroidApp.getDefault().getFaveApplications();
		if(faveApps.size() == 0){
			if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PREF_KEY_NO_FAVES, true)) {
				PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(PREF_KEY_NO_FAVES, false).commit();
				Toast.makeText(getActivity(), "No favourite apps selected!", Toast.LENGTH_SHORT).show();
				return;
			}
		}		
		adapter.setListItems(LessDroidApp.getDefault().getFaveApplications());
		listApps.setAdapter(adapter);
		listApps.setFastScrollEnabled(true);
		listApps.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				final AppHolder app = (AppHolder) parent.getItemAtPosition(position);
				LessDroidApp.getDefault().launchApp(app.appInfo.className, LessDroidApp.getContext());
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.setListItems(LessDroidApp.getDefault().getFaveApplications());
    	adapter.notifyDataSetChanged();
    }  
}