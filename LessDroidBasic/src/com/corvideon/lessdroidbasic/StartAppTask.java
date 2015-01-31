package com.corvideon.lessdroidbasic;

import com.corvideon.lessdroidhome.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StartAppTask extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
	
    public StartAppTask(Context context) {
    	this.context = context;
        dialog = new ProgressDialog(context);
    }


    protected void onPreExecute() {
        // this.dialog.setMessage(context.getResources().getString(R.string.start_up_message));
        // this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected Boolean doInBackground(String... params) {
       try{    
    	  LessDroidApp.getDefault().getApplicationsOnStart();
          return true;
       } catch (Exception e){
          Log.v(LessDroidApp.LOG_TAG, "Error! System apps could not be loaded - " + e.getMessage());
          return false;
       }
    }
}