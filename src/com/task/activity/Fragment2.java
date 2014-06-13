package com.task.activity;

import roboguice.fragment.RoboFragment;

import android.os.Bundle;
import android.util.Log;

public class Fragment2 extends RoboFragment {
	private static final String TAG = "Fragment2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate...");
	}


	/**********************************************/
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy...");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop...");
	}

}
