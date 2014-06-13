package com.task.activity;

import roboguice.fragment.RoboFragment;

import android.os.Bundle;
import android.util.Log;

public class Fragment1 extends RoboFragment {
	private String TAG = "Fragment1";
	
	public interface OnCreateTaskListener {
		public void callCreatEvent();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate...");
	}
	
}
