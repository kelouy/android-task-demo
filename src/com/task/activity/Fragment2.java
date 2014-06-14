package com.task.activity;

import roboguice.fragment.RoboFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends RoboFragment {
	private static final String TAG = "Fragment2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate...");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment2, container, false);
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
