package com.corvideon.lessdroidpremium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.corvideon.lessdroidpremium.R;
import com.orm.SugarApp;

public class LessDroidApp extends SugarApp  {
	
	// instance
	private static LessDroidApp instance = null;
	private static Context context;
	private AppInfoManager appManager;

	public static final String LOG_TAG = "LESSDROID";
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		context = getApplicationContext();
		appManager = new AppInfoManager();
	}
	
	public static LessDroidApp getDefault(){
		return instance;
	}
	
	public static Context getContext(){
		return context;
	}

	public AppInfoManager getAppManager(){
		return appManager;
	}
	
	public Map<String, AppHolder> getApplications(boolean reload){
			return appManager.getApplications(reload);
	}
	
	public void getApplicationsOnStart(){
		try {
			appManager.setAppGlobalMap(appManager.getApplicationsFromDatabase()); 
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG, "Error! Apps could not be loaded from database - " + e.getMessage());
			Toast.makeText(context, R.string.error_loading_data, Toast.LENGTH_SHORT).show();
		}
	}
		
	public void refreshApplications(){
		try {
			AppInfo.deleteAll(AppInfo.class);
			appManager.loadApplicationsFromSystem();
		} catch (NameNotFoundException e) {
			Log.v(LessDroidApp.LOG_TAG, "Error! System apps could not be loaded - " + e.getMessage());
			Toast.makeText(context, R.string.error_loading_data, Toast.LENGTH_SHORT).show();
		}
	}
	
	public Map<String, AppHolder> getFaveApplications(){
		return appManager.getFaveApplications();
	}
	
	public void saveApplications(ArrayList<AppHolder> appList){
		appManager.saveApplications(appList);
	}
	
	public void launchApp(String appPackageName, Context context) {
		appManager.launchApp(appPackageName, context);
	}
	
	public void appInstalledEvent(String packageName){
		appManager.appInstalledEvent(packageName);
	}
	
	public void appUninstalledEvent(String packageName){
		appManager.appUninstalledEvent(packageName);
	}
	
	public static boolean hasPhone(Context context){
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            return false;
        }else{
            return true;
        }
	}
	
	public static boolean hasCamera(Context context){
        return isFeatureAvailable(context, PackageManager.FEATURE_CAMERA);
	}
	
	public static boolean isFeatureAvailable(Context context, String feature) {
        final PackageManager packageManager = context.getPackageManager();
        final FeatureInfo[] featuresList = packageManager.getSystemAvailableFeatures();
        for (FeatureInfo f : featuresList) {
            if (f.name != null && f.name.equals(feature)) {
                 return true;
            }
        }
       return false;
    }

	
	// an inner class to keep all app management methods together
	class AppInfoManager {
		
		private Map<String, AppHolder> appList = new HashMap<String, AppHolder>();
		
		
		private void loadApplicationsFromSystem() throws NameNotFoundException {
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
			appList = new HashMap<String, AppHolder>();
			for (int i = 0; i < packs.size(); i++) {
				PackageInfo p = packs.get(i);
				if ((p.versionName == null)) {
					continue;
				}
				addAppToLessDroid(p.packageName);
			}
		}
		
		private Map<String, AppHolder> getApplicationsFromDatabase() {
			List<AppInfo> apps = AppInfo.listAll(AppInfo.class);
			Map<String, AppHolder> appList = new HashMap<String, AppHolder>();
			PackageManager pm = context.getPackageManager();
			for( AppInfo app : apps ) {
				ApplicationInfo sysAppInfo;
				try {
					sysAppInfo = context.getPackageManager().getApplicationInfo(app.className, 0);
					Drawable icon = getAppIcon(sysAppInfo);
					AppHolder appHolder = new AppHolder(app, icon);
					appList.put(app.title, appHolder);
				} catch (NameNotFoundException e) {
					Log.v(LessDroidApp.LOG_TAG, app.title + " not found on system - " + e.getMessage());
				}
		    }
			return appList;
		}
		
		public void setAppGlobalMap(Map<String, AppHolder> theAppList){
			appList = theAppList;
		}
		
		public Map<String, AppHolder> getApplications(Boolean reload){
			if(reload){
				return getApplicationsFromDatabase();
			} else {
				return appList;
			}
		}
		
		private Drawable getAppIcon(ApplicationInfo systemAppInfo) {
			Drawable icon;
			try {
				icon  = systemAppInfo.loadIcon(context.getPackageManager());
			} catch (NullPointerException e){
				Log.v(LessDroidApp.LOG_TAG, systemAppInfo.className + " icon set to default - " + e.getMessage());
				icon = LessDroidApp.getContext().getResources().getDrawable(R.drawable.default_icon);
			}
			return icon; 
		}
		
		@SuppressWarnings("unused")
		private boolean areAppsLoadedIntoDatabase(){
			List<AppInfo> apps = AppInfo.listAll(AppInfo.class);
			if(apps.size() == 0){
				return false;
			} else {
				return true;
			}
		}
		
		public boolean isLaunchableApp(String packageName) throws NameNotFoundException {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
			if (context.getPackageManager().getLaunchIntentForPackage(appInfo.packageName) != null) {
				return true;
			} else {
				return false;
			}
		}
		
		public void launchApp(String appPackageName, Context context) {
			Intent appStartIntent;
			PackageManager pm = context.getPackageManager();
			appStartIntent = pm.getLaunchIntentForPackage(appPackageName);
			if (null != appStartIntent) {
				try {
					context.startActivity(appStartIntent);
				} catch (Exception e) {
					Log.v(LessDroidApp.LOG_TAG,
							" Error launching " + appPackageName + " - " + e.getMessage());
					Toast.makeText(context, R.string.app_not_launched,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, R.string.app_not_launched,
						Toast.LENGTH_SHORT).show();
				Log.v(LessDroidApp.LOG_TAG, appPackageName + " could not be launched!");
			}
		}
		
		public AppInfo getAppInfoByPackage(String packageName){
			List<AppInfo> apps = AppInfo.listAll(AppInfo.class);
			for( AppInfo app : apps ) {
				if(app.className.equals(packageName)){
					return app;
				}
		    }
			return null;
		}
		
		public AppInfo getAppInfoByTitle(String title){
			List<AppInfo> apps = AppInfo.listAll(AppInfo.class);
			for( AppInfo app : apps ) {
				if(app.title.equals(title)){
					return app;
				}
		    }
			return null;
		}
		
		public Map<String, AppHolder> getFaveApplications(){
			List<AppInfo> faveAppList = AppInfo.find(AppInfo.class, "IS_FAVOURITE = ?", new String[]{"Y"});
			Map<String, AppHolder> faveApps = new HashMap<String, AppHolder>();
			for( AppHolder appholder : appList.values()){
				for (AppInfo faveApp : faveAppList) {
					if(appholder.appInfo.className.equals(faveApp.className)){
						faveApps.put(appholder.appInfo.className, appholder);
					}
		        }
			}			
			return faveApps;
		}
		
		public void saveApplications(ArrayList<AppHolder> appList2){
			for (int i = 0; i < appList2.size(); i++) {
				AppHolder app = appList2.get(i);
				app.appInfo.save();
			}
		}
		
		public void appInstalledEvent(String packageName) {
			Log.v(LessDroidApp.LOG_TAG,"app install event for " + packageName);
			addAppToLessDroid(packageName);
		}
		
		public void appUninstalledEvent(String packageName){
			Log.v(LessDroidApp.LOG_TAG,"app uninstall event for " + packageName);
			removeAppFromLessDroid(packageName);
		}
		
		public void addAppToLessDroid( String packageName ){
			try {
				ApplicationInfo sysAppInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
				String theAppName = sysAppInfo.loadLabel(context.getPackageManager()).toString();
				if (isLaunchableApp(packageName)) {
					AppInfo appInfo = new AppInfo(context, theAppName, packageName, "N");
					appInfo.save();
					Drawable icon = getAppIcon(sysAppInfo);
					AppHolder appHolder = new AppHolder(appInfo, icon);
					appList.put(theAppName, appHolder);
				}
			} catch (NameNotFoundException e){
				Log.v(LessDroidApp.LOG_TAG, packageName + " could not be added to the database - " + e.getMessage());
			}
		}
		
		public void removeAppFromLessDroid( String packageName ){
			try {
				AppInfo app = getAppInfoByPackage(packageName);
				if (app == null){
					Log.v(LessDroidApp.LOG_TAG, "Uninstall error - No app found with package name " + packageName);
				} else {
					appList.remove(app.title);
					app.delete();
				}
			} catch (Exception e){
				Log.v(LessDroidApp.LOG_TAG, "Error removing " + packageName + " : " + e.getMessage());
			}
		}
	}

}
