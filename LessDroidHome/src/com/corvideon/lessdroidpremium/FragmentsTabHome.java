package com.corvideon.lessdroidpremium;

import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.corvideon.lessdroidpremium.R;

public class FragmentsTabHome extends SherlockFragment {

	Button phone_button;
	Button contact_button;
	Button messaging_button;
	Button camera_button;
	Button gallery_button;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragments_tab_home);
        
        phone_button = (Button) getActivity().findViewById(R.id.phone_button);
        phone_button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = LessDroidApp.getDefault().getAppManager().getAppInfoByTitle( getResources().getString(R.string.app_phone));
				HomeAppLaunchHelper.openApp(LessDroidApp.getContext(), appInfo, "PHONE");
			}
		});
        
        contact_button = (Button) getActivity().findViewById(R.id.contact_button);
        contact_button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = LessDroidApp.getDefault().getAppManager().getAppInfoByTitle( getResources().getString(R.string.app_contacts));
				HomeAppLaunchHelper.openContacts(LessDroidApp.getContext(), appInfo, "CONTACTS");
			}
		});
        
        messaging_button = (Button) getActivity().findViewById(R.id.messaging_button);
        messaging_button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = LessDroidApp.getDefault().getAppManager().getAppInfoByTitle( getResources().getString(R.string.app_messaging));
				HomeAppLaunchHelper.openApp(LessDroidApp.getContext(), appInfo, "MESSAGING");
			}
		});
        
        camera_button = (Button) getActivity().findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = LessDroidApp.getDefault().getAppManager().getAppInfoByTitle( getResources().getString(R.string.app_camera));
				HomeAppLaunchHelper.openCamera(LessDroidApp.getContext(), appInfo, "CAMERA");
			}
		});
        
        gallery_button = (Button) getActivity().findViewById(R.id.gallery_button);
        gallery_button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppInfo appInfo = LessDroidApp.getDefault().getAppManager().getAppInfoByTitle( getResources().getString(R.string.app_gallery));
				HomeAppLaunchHelper.openApp(LessDroidApp.getContext(), appInfo, "GALLERY");
			}
		});
    }
	
	@Override
	public void onResume(){
		super.onResume();
	}
	

}