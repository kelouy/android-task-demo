package com.task.tools.component;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.task.activity.R;

public class MyActionBar {

	private ActionBar actionBar;
	private TextView tvTitle;
	private ProgressBar pbUpdate;
	
	public MyActionBar(Activity activity) {
		actionBar = activity.getActionBar();
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT, 
				ActionBar.LayoutParams.MATCH_PARENT, 
				Gravity.CENTER);
		View actionBarView = activity.getLayoutInflater().inflate(R.layout.main_actionbar, null);
		actionBar.setCustomView(actionBarView, lp);
		// actionBar.setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
		actionBar.setDisplayShowHomeEnabled(false);// 使左上角图标可点击，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true); // 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
		tvTitle = (TextView) actionBar.getCustomView().findViewById(android.R.id.title);
		pbUpdate = (ProgressBar) actionBar.getCustomView().findViewById(R.id.main_progressBar);
		showProgressBar(true);
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void showProgressBar(boolean flag){
		if(flag){
			pbUpdate.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					pbUpdate.setVisibility(View.GONE);
				}
			}, 6000);
		}else{
			pbUpdate.setVisibility(View.GONE);
		}
		
	}
	
	public void show(){
		if(!actionBar.isShowing())
			actionBar.show();
	}
	
	public void hide(){
		if(actionBar.isShowing())
			actionBar.hide();
	}
}
