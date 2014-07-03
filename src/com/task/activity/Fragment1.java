package com.task.activity;

import roboguice.fragment.RoboFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1 extends RoboFragment {
	private String TAG = "Fragment1";
	
	public interface OnCreateTaskListener {
		public void callCreatEvent();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate...");
		//显示actionbar上的progressbar
		((MainActivity) getActivity()).actionBar.showProgressBar(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment1, container, false);
	}
	
}
