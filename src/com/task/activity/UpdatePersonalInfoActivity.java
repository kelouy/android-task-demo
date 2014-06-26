package com.task.activity;

import com.task.common.bean.Department;
import com.task.common.bean.User;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class UpdatePersonalInfoActivity extends RoboActivity {
	
	private static final String TAG = "UpdatePersonalInfoActivity";
	@InjectExtra("user") User user;
	@InjectView(R.id.update_dept_spinner) Spinner deptSp;
	@InjectView(R.id.update_posi_spinner) Spinner positionSp;
	
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_personal_info);
		initActionBar();
		initData();
	}
	
	private void initData() {
		ArrayAdapter<Department> deptAdapter = new ArrayAdapter<Department>(this, android.R.layout.simple_spinner_item);
		deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deptAdapter.add(new Department("aaaa"));
		deptAdapter.add(new Department("bbbb"));
		deptAdapter.add(new Department("cccc"));
		deptSp.setAdapter(deptAdapter);
		deptSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				Department dept = (Department) adapterView.getItemAtPosition(position);
				debug("dept:"+dept+" position:"+position+"  id:"+id);
			}

			private void debug(String s) {
				Log.v(TAG, s);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
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
