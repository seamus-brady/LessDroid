package com.corvideon.lessdroidbasic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppUninstallListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(LessDroidApp.LOG_TAG,"app uninstall broadcast recieved for " + intent.getDataString());
		String packageString = intent.getDataString();
		if(!packageString.equals("") || packageString != null ){
			if(!packageString.equals("") || packageString != null ){
				if(packageString.split(":").length > 0){
					String packageName = packageString.split(":")[1];
					LessDroidApp.getDefault().appUninstalledEvent(packageName);
				}
			}
		}
	}
}