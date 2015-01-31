package com.corvideon.lessdroidbasic;

/**
 * Derived in part from sample code copyright 2011 Manuel Schwarz (m.schwarz[at]impressive-artworx.de)
 */

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.corvideon.lessdroidhome.R;

public class FragmentsTabApps extends SherlockFragment {

	private ListView listApps;
	private AppListingAdapter adapter = new AppListingAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setContentView(R.layout.fragments_tab_apps);
		CreateListView();
		CreateRefreshButton();
	}
	
	private void CreateRefreshButton() {
		Button button = (Button) getActivity().findViewById(R.id.refresh_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				reloadList();
			}
		});
	}
	
	private void CreateListView() {
		listApps = new ListView(getActivity());
		listApps = (ListView) getActivity().findViewById(R.id.listApps); 
		adapter.setListItems(LessDroidApp.getDefault().getApplications(false));
		listApps.setAdapter(adapter);
		listApps.setFastScrollEnabled(true);
		listApps.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				final AppHolder app = (AppHolder) parent.getItemAtPosition(position);
				// Toast.makeText(getActivity(), app.className, Toast.LENGTH_SHORT).show();
				LessDroidApp.getDefault().launchApp(app.appInfo.className, LessDroidApp.getContext());
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reloadList();
    }  
	
	public void reloadList(){
		adapter.setListItems(LessDroidApp.getDefault().getApplications(false));
    	adapter.notifyDataSetChanged();
	}  
}
