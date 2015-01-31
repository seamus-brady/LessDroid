package com.corvideon.lessdroidpremium;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.corvideon.lessdroidpremium.R;


public class MainActivity extends SherlockFragmentActivity {

	private static final String PREF_KEY_FIRST_TIME_USE = "FIRST_TIME_USE";
	private static final String PREF_KEY_CURRENT_TAB = "CURRENT_TAB";
	private static final String PREF_KEY_TITLE = "LessDroidHomeTitlePref";
	private static final String PREF_KEY_ROTATION = "LessDroidHomeRotationPref";
	private static final int RESULT_SETTINGS = 1;
	private String defaultTitle;
	private boolean disableRotation = true;
	FragmentsTabHome fragHome;
	FragmentsTabFaves fragFaves;
	FragmentsTabApps fragApps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// define the fragments
		fragHome = new FragmentsTabHome();
		fragFaves = new FragmentsTabFaves();
		fragApps = new FragmentsTabApps();
		configureActionBar();
		setActionBarTitle();
		setRotation();
		// load the applications
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				PREF_KEY_FIRST_TIME_USE, true)) {
			// first time use
			@SuppressWarnings("rawtypes")
			AsyncTask task = new FirstRunTask(this).execute();
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putBoolean(PREF_KEY_FIRST_TIME_USE, false).commit();
			try{
				getSupportActionBar().setSelectedNavigationItem(0);
			}
			catch(IndexOutOfBoundsException e){
				Log.v(LessDroidApp.LOG_TAG, "Error setting  tab - " + e.getMessage());
			}	
		} else {
			// load the apps from the database if they are not available in the global map
			if(LessDroidApp.getDefault().getApplications(false).size() == 0){
				getSupportActionBar().setSelectedNavigationItem(0);
				new StartAppTask(this).execute();
			}
		}
	}

	
	
	protected void reloadLists() {
		try{
			 fragFaves.reloadList();
			 fragApps.reloadList();
		}
		catch(Exception e){
			Log.v(LessDroidApp.LOG_TAG, "Error reloading lists - " + e.getMessage());
		}	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveCurrentTab(getSupportActionBar().getSelectedTab().getPosition());
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			// load the apps from the database if they are not available in the global map
			if(LessDroidApp.getDefault().getApplications(false).size() == 0){
				getSupportActionBar().setSelectedNavigationItem(0);
				new StartAppTask(this).execute();
			} else {
				// just reload the last tab
				getSupportActionBar().setSelectedNavigationItem(
						(PreferenceManager.getDefaultSharedPreferences(this).getInt(
								PREF_KEY_CURRENT_TAB, 0)));
			}
		}
		catch(IndexOutOfBoundsException e){
			Log.v(LessDroidApp.LOG_TAG, "Error reloading last tab - " + e.getMessage());
		}	
	}

	public void refreshApplications() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.refresh_apps)
				.setMessage(R.string.refresh_apps_question)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								new RefreshAppsTask().execute();
							}
						})
				.setNeutralButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	private class RefreshAppsTask extends AsyncTask<Void, Void, Void> {

		@SuppressWarnings("unused")
		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Please wait...");
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... apps) {
			LessDroidApp.getDefault().refreshApplications();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	//unregister event bus
	protected void onDestroy() {  
	        super.onDestroy();  
	}  
	
	private void saveCurrentTab(int value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putInt(PREF_KEY_CURRENT_TAB, value).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings_button:
			Intent settingsScreen = new Intent(
					android.provider.Settings.ACTION_SETTINGS);
			settingsScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(settingsScreen);
			return true;
		case R.id.help_button:
			// Intent helpScreen = new Intent(getApplicationContext(),
			// HelpActivity.class);
			// startActivity(helpScreen);
			String url = getResources().getString(R.string.url_help);
			Intent helpScreen = new Intent(Intent.ACTION_VIEW);
			helpScreen.setData(Uri.parse(url));
			startActivity(helpScreen);
			return true;
		case R.id.add_fave_button:
			// edit fave apps
			Intent nextScreen = new Intent(getApplicationContext(),
					FaveAppsActivity.class);
			startActivity(nextScreen);
			return true;
		case R.id.change_lessdroid_settings:
			Intent prefScreen = new Intent(getApplicationContext(),
					LessDroidPrefActivity.class);
			startActivityForResult(prefScreen, RESULT_SETTINGS);
			return true;
		case R.id.android_settings_button:
			settingsScreen = new Intent(
					android.provider.Settings.ACTION_SETTINGS);
			settingsScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(settingsScreen);
			return true;
		case R.id.refresh_button:
			refreshApplications();
			return true;
		case R.id.about_button:
			showVersionDialog();
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SETTINGS:
			settingsChangeEvent();
			break;
		}
	}

	private void settingsChangeEvent() {
		setActionBarTitle();
		setRotation();
	}

	private void setActionBarTitle() {
		defaultTitle = getResources().getString(R.string.app_name);
		String title = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(PREF_KEY_TITLE, defaultTitle);
		getSupportActionBar().setTitle(title);
	}
	
	private void setRotation() {
		disableRotation = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREF_KEY_ROTATION, true);
		if(disableRotation){
			setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}
	
	
	public void showVersionDialog(){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(getResources().getString(R.string.app_name));
	    builder.setMessage(getVersionInfoString());
	    builder.setCancelable(true);
	    builder.setNeutralButton(android.R.string.ok,
	            new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            dialog.cancel();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}

	public String getVersionInfoString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getResources().getString(R.string.app_name) + " ");
		PackageInfo pInfo;
		String version; 
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
			sb.append(version + "\n");
		} catch (NameNotFoundException e) {
			// do nothing
		}
		sb.append(getResources().getString(R.string.website) + "\n");
		sb.append(getResources().getString(R.string.company_name) + " ");
		sb.append(getResources().getString(R.string.copyright) + "\n");
		return sb.toString();
	}

	private void configureActionBar() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = getSupportActionBar().newTab();
		// Create first Tab
		tab = getSupportActionBar().newTab()
				.setTabListener(new MyTabListener());
		tab.setIcon(R.drawable.ic_action_home);
		tab.setText(R.string.home_tab);
		getSupportActionBar().addTab(tab);

		// Create Second Tab
		tab = getSupportActionBar().newTab()
				.setTabListener(new MyTabListener());
		tab.setIcon(R.drawable.ic_action_heart);
		tab.setText(R.string.faves_tab);
		getSupportActionBar().addTab(tab);

		// Create Third Tab
		tab = getSupportActionBar().newTab()
				.setTabListener(new MyTabListener());
		tab.setIcon(R.drawable.ic_action_tiles_small);
		tab.setText(R.string.apps_tab);
		getSupportActionBar().addTab(tab);

	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("LastTab", getSupportActionBar().getSelectedTab()
				.getPosition());
	}

	private class MyTabListener implements ActionBar.TabListener {
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {

			if (tab.getPosition() == 0) {
				ft.replace(android.R.id.content, fragHome);
			} else if (tab.getPosition() == 1) {
				ft.replace(android.R.id.content, fragFaves);
			} else {
				ft.replace(android.R.id.content, fragApps);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
