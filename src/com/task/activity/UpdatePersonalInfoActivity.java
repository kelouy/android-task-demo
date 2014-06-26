package com.task.activity;

import com.task.common.bean.User;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;

public class UpdatePersonalInfoActivity extends RoboActivity {
	
	@InjectExtra("user") User user;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_personal_info);
		initActionBar();
	}
	
	//初始化actionbar 
		private void initActionBar() {
			actionBar = this.getActionBar();
			actionBar.setTitle(user.getRealName());
			actionBar.setIcon(R.drawable.icon_back_nol);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.update_person_actionbar, menu);
			return true;
		}
	
	/***************************/
	@Override
	public void onBackPressed() {
		finish();
	}
}
