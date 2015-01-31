package com.corvideon.lessdroidpremium;

import com.corvideon.lessdroidpremium.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

public class HelpActivity extends Activity {
	
	private WebView webViewHelp;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		webViewHelp = new WebView(this);
		webViewHelp = (WebView) findViewById(R.id.webviewHelp);
		webViewHelp.setInitialScale(1);
		webViewHelp.getSettings().setJavaScriptEnabled(true);
		webViewHelp.getSettings().setLoadWithOverviewMode(true);
		webViewHelp.getSettings().setUseWideViewPort(true);
		webViewHelp.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webViewHelp.setScrollbarFadingEnabled(false);
		webViewHelp.loadUrl(getResources().getString(R.string.url_help));
	}
}
