package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.Department;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class UpdatePersonalInfoActivity extends MyActivity {
	
	private static final String TAG = "UpdatePersonalInfoActivity";
	private MyApplication application;
	@InjectExtra("user") User user;
	@InjectView(R.id.update_dept_spinner) Spinner deptSp;
	@InjectView(R.id.update_posi_spinner) Spinner positionSp;
	ActionBar actionBar;
	ArrayAdapter<Department> deptAdapter;
	DbUtils db;
	Gson gson = new Gson();
	List<Department> deptList = new ArrayList<Department>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_personal_info);
		application = (MyApplication) this.getApplicationContext();
		initActionBar();
		initData();
	}
	
	
	private void initData() {
		db = DbUtils.create(this);
		List<Department> deptListTmp = null;
		try {
			deptListTmp = db.findAll(Department.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		if(deptListTmp != null && deptListTmp.size()>0){
			deptList.addAll(deptListTmp);
		}
		deptAdapter = new ArrayAdapter<Department>(this, android.R.layout.simple_spinner_item);
		deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deptAdapter.addAll(deptList);
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

		@Override
		public void getMessage(TranObject msg) {
			
		}
	/***************************/
	@Override
	public void onBackPressed() {
		finish();
	}

}
