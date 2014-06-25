package com.task.activity;

import com.task.common.bean.User;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;

public class PersonalInfoActivity extends RoboActivity {
	private static String TAG = "PersonalInfoActivity";
	
	@InjectExtra("user") User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate……");
		TextView textview = new TextView(this);
		textview.setText("个人信息页面\nusername:"+user.getUserName()+"\nurl:"+user.getHeadUrl());
		setContentView(textview);
	}
	
	
	/*************************************/
	@Override
	public void onBackPressed() {
		finish();
	}
	
	void debug(String s){
		Log.v(TAG, s);
	}
}
