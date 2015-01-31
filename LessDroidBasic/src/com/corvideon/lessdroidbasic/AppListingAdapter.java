package com.corvideon.lessdroidbasic;

/**
 * Derived in part from sample code copyright 2011 Manuel Schwarz (m.schwarz[at]impressive-artworx.de)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.corvideon.lessdroidhome.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListingAdapter extends BaseAdapter {
   
   private LayoutInflater inflater;
   private Context context;
   private List<AppHolder> apps;
   
   public AppListingAdapter() {
      context = LessDroidApp.getContext();
      inflater = LayoutInflater.from(context);
   }
   
   
   @Override
   public int getCount() {
      return apps.size();
   }
   
   @Override
   public Object getItem(int position) {
      return apps.get(position);
   }
   
   @Override
   public long getItemId(int position) {
      return position;
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      AppViewHolder holder;
      if(convertView == null) {
         convertView = inflater.inflate(R.layout.list_item, null);
         holder = new AppViewHolder();
         holder.appName = (TextView) convertView.findViewById(R.id.appName);
         holder.icon = (ImageView) convertView.findViewById(R.id.icon);
         convertView.setTag(holder);
      } else { 
         holder = (AppViewHolder) convertView.getTag();
      }
      AppHolder app = apps.get(position);
      holder.setTitle(app.appInfo.title);
      holder.setIcon(app.icon);
      return convertView; 
   }
   
   public void setListItems(Map<String, AppHolder> theApps) { 
	  List<AppHolder> values = new ArrayList<AppHolder>(theApps.values());
	  if (values.size() > 0) {
		    Collections.sort(values, new Comparator<AppHolder>() {
		        @Override
		        public int compare(final AppHolder app1, final AppHolder app2) {
		            return app1.appInfo.title.compareTo(app2.appInfo.title);
		        }
		       } );
		   }
      apps = values; 
   }
 

   public class AppViewHolder {
      
      private TextView appName;
      private ImageView icon;
      
      public void setTitle(String title) {
    	  appName.setText(title);
      }
      
      public void setIcon(Drawable img) {
         if (img != null) {
            icon.setImageDrawable(img);
         }
      }
   }
}
