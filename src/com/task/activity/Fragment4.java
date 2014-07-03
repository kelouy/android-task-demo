package com.task.activity;

import roboguice.fragment.RoboFragment;
import com.task.common.utils.Constants;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Fragment4 extends RoboFragment {
	private final String TAG = "Fragment4";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate ^^^");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment4, container, false);
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		debug("onCreateOptionsMenu...");
		//inflater.inflate(R.menu.fragment4_actionbar, menu);
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		debug("onPause...");
	}
	
	private void debug(String s) {
		Log.v(TAG, s);
	}
	
}
