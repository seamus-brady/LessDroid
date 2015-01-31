package com.corvideon.lessdroidbasic;

import com.corvideon.lessdroidhome.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class HomeAppLaunchHelper {

	public static void openApp(Context context, AppInfo appInfo, String appName) {
		if (isValidAppInfo(appInfo)) {
			try {
				LessDroidApp.getDefault().launchApp(appInfo.className,
						LessDroidApp.getContext());
			} catch (Exception e) {
				Log.v(LessDroidApp.LOG_TAG, " Error launching " + appName
						+ " app, trying alternative - " + e.getMessage());
				alternateOpenApp(context, appInfo, appName);
			}
		} else {
			alternateOpenApp(context, appInfo, appName);
		}
	}

	private static void alternateOpenApp(Context context, AppInfo appInfo,
			String appName) {
		if (appName.equals("PHONE")) {
			alternateOpenPhoneApp(context, appInfo, appName);
		} else if (appName.equals("CONTACTS")) {
			alternateOpenContactsApp(context, appInfo, appName);
		} else if (appName.equals("MESSAGING")) {
			alternateOpenMessagingApp(context, appInfo, appName);
		} else if (appName.equals("CAMERA")) {
			alternateOpenCameraApp(context, appInfo, appName);
		} else if (appName.equals("GALLERY")) {
			alternateOpenGalleryApp(context, appInfo, appName);
		}
	}

	private static void alternateOpenPhoneApp(Context context, AppInfo appInfo,
			String appName) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching alternate phone app - " + e.getMessage());
			Toast.makeText(context, R.string.app_not_launched,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public static void openContacts(Context context, AppInfo appInfo,
			String appName){
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
			Uri.parse("content://contacts/people/"));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching camera app - " + e.getMessage());
			// try the defaults...
			openApp(context, appInfo, appName);
		}
	}
	

	private static void alternateOpenContactsApp(Context context,
			AppInfo appInfo, String appName) {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching alternate contacts app - "
							+ e.getMessage());
			Toast.makeText(context, R.string.app_not_launched,
					Toast.LENGTH_SHORT).show();
		}
	}


	public static void alternateOpenMessagingApp(Context context,
			AppInfo appInfo, String appName) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("vnd.android-dir/mms-sms");
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching alternate messaging app - "
							+ e.getMessage());
			Toast.makeText(context, R.string.app_not_launched,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void openCamera(Context context, AppInfo appInfo,
			String appName){
		try {
			//Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching camera app - " + e.getMessage());
			// try the defaults...
			openApp(context, appInfo, appName);
		}
	}
	
	
	public static void alternateOpenCameraApp(Context context, AppInfo appInfo,
			String appName) {
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching alternate camera app - " + e.getMessage());
			Toast.makeText(context, R.string.app_not_launched,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void alternateOpenGalleryApp(Context context,
			AppInfo appInfo, String appName) {
		try {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.v(LessDroidApp.LOG_TAG,
					" Error launching alternate gallery app - "
							+ e.getMessage());
			Toast.makeText(context, R.string.app_not_launched,
					Toast.LENGTH_SHORT).show();
		}
	}

	private static boolean isValidAppInfo(AppInfo appInfo) {
		boolean returnValue = true;
		if (appInfo == null) {
			return false;
		}
		try {
			returnValue = LessDroidApp.getDefault().getAppManager()
					.isLaunchableApp(appInfo.className);
		} catch (NameNotFoundException e) {
			return false;
		}
		return returnValue;
	}
}
