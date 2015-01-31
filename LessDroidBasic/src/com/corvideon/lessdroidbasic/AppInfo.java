package com.corvideon.lessdroidbasic;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.orm.SugarRecord;

@SuppressWarnings("rawtypes")
public class AppInfo extends SugarRecord<AppInfo> {

	// instance parts
	public String title;
	public String className;
	public String isFavourite;

	public AppInfo(Context context) {
		super(context);
	}

	public AppInfo(Context context, String title, String className, String isFavourite) {
		super(context);
		this.title = title;
		this.className = className;
		this.isFavourite = isFavourite;
	}

	public boolean isSelected() {
		if(isFavourite.equals("Y")){
			return true;
		} else {
			return false;
		}
	}

	public void setSelected(boolean selected) {
		if(selected){
			this.isFavourite = "Y";
		} else {
			this.isFavourite = "N";
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AppInfo)) {
			return false;
		}
		AppInfo that = (AppInfo) o;
		return title.equals(that.title) && className.equals(that.className);
	}

	@Override
	public int hashCode() {
		int result;
		result = (title != null ? title.hashCode() : 0);
		final String name = className;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

}
