package com.corvideon.lessdroidbasic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.corvideon.lessdroidhome.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FaveAppsActivity extends Activity {

	MyFaveAppsAdapter dataAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fave_apps);
		this.setTitle(R.string.title_activity_choose_apps);
		displayListView();
		checkButtonClick();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.fave_apps, menu);
		return true;
	}

	private void displayListView() {
		ArrayList<AppHolder> appList = new ArrayList<AppHolder>();
		appList = new ArrayList<AppHolder>(LessDroidApp.getDefault().getApplications(true).values());
		Collections.sort(appList, new Comparator<AppHolder>(){
			  public int compare(AppHolder app1, AppHolder app2) {
			    return app1.appInfo.title.compareToIgnoreCase(app2.appInfo.title);
			  }
		});;
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyFaveAppsAdapter(this, R.layout.fave_apps_listview, appList);
		ListView listView = (ListView) findViewById(R.id.faveApps);
		listView.setFastScrollEnabled(true);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
	}

	private class MyFaveAppsAdapter extends ArrayAdapter<AppHolder> {

		private ArrayList<AppHolder> appList;

		public MyFaveAppsAdapter(Context context, int textViewResourceId, ArrayList<AppHolder> appList) {
			super(context, textViewResourceId, appList);
			this.appList = new ArrayList<AppHolder>();
			this.appList.addAll(appList);
		}

		private class ViewHolder {
			TextView code;
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.fave_apps_listview, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView
						.findViewById(R.id.appTitle);
				holder.name = (CheckBox) convertView.findViewById(R.id.isFave);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						AppHolder app = (AppHolder) cb.getTag();
						app.appInfo.setSelected(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AppHolder app = appList.get(position);
			holder.name.setText(app.appInfo.title);
			holder.name.setChecked(app.appInfo.isSelected());
			holder.name.setTag(app);
			return convertView;

		}

	}

	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.findSelected);
		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<AppHolder> appList = dataAdapter.appList;
				// save changes to the apps
				LessDroidApp.getDefault().saveApplications(appList);
	            Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
	            startActivity(nextScreen);
			}
		});

	}

}
