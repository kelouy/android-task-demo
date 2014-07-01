package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.task.common.bean.Department;
import com.task.common.bean.Position;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.utils.ActivityTag;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class UpdatePersonalInfoActivity extends MyActivity {

	private static final String TAG = "UpdatePersonalInfoActivity";
	private MyApplication application;
	@InjectExtra("user") User user;
	@InjectView(R.id.update_dept_spinner) Spinner deptSp;
	@InjectView(R.id.update_posi_spinner) Spinner positionSp;
	@InjectView(R.id.update_realname) EditText realNameEdit;
	@InjectView(R.id.update_phone) EditText phoneEdit;
	@InjectView(R.id.update_qq) EditText qqEdit;
	@InjectView(R.id.update_email) EditText emailEdit;
	@InjectView(R.id.update_sex_rg) RadioGroup sexRadioGroup;
	@InjectView(R.id.update_radioMale) RadioButton maleRadio;
	@InjectView(R.id.update_radioFemale) RadioButton femaleRadio;
	@InjectView(R.id.update_person_submit) Button submitBtn;
	ActionBar actionBar;
	DbUtils db;
	Gson gson = new Gson();
	ArrayAdapter<Department> deptAdapter;
	ArrayAdapter<Position> positionAdapter;
	List<Department> deptList = new ArrayList<Department>();
	List<Position> positionList = new ArrayList<Position>();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_personal_info);
		db = DbUtils.create(this);
		if(user == null)
			onBackPressed();
		application = (MyApplication) this.getApplicationContext();
		initActionBar();
		init();
		initData();
	}
	
	private void init() {
		if(!TextUtils.isEmpty(user.getRealName()))
			realNameEdit.setText(user.getRealName());
		realNameEdit.addTextChangedListener(textWatcher);
		
		if(!TextUtils.isEmpty(user.getPhoneNum()))
			phoneEdit.setText(user.getPhoneNum());
		phoneEdit.addTextChangedListener(textWatcher);
		
		if(user.getQq() > 0)
			qqEdit.setText(user.getQq()+"");
		qqEdit.addTextChangedListener(textWatcher);
		
		if(!TextUtils.isEmpty(user.getEmail()))
			emailEdit.setText(user.getEmail());
		emailEdit.addTextChangedListener(textWatcher);
		
		if(user.getSex() == 0)
			femaleRadio.setChecked(true);
		else
			maleRadio.setChecked(true);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});
		maleRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				debug(buttonView+"");
				submitBtn.setClickable(true);
			}
		});
	}
	

	//提交修改
	private void submit() {
		user.setRealName(realNameEdit.getText().toString());
		user.setEmail(emailEdit.getText().toString());
		if(qqEdit.getText().toString().length() > 0)
			user.setQq(Integer.parseInt(qqEdit.getText().toString()));
		user.setPhoneNum(phoneEdit.getText().toString());
		debug(user.toString());
		
	}

	private void initData() {
		
		try {
			deptList = db.findAll(Department.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		deptAdapter = new ArrayAdapter<Department>(this, android.R.layout.simple_spinner_item);
		deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deptAdapter.addAll(deptList);
		deptSp.setAdapter(deptAdapter);
		deptSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				Department dept = (Department) adapterView.getItemAtPosition(position);
				submitBtn.setClickable(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		for(int i=0;i<deptList.size();i++){
			if(deptList.get(i).getDeptName().equals(user.getDeptName())){
				deptSp.setSelection(i, true);
				break;
			}
		}
		
		try {
			positionList = db.findAll(Position.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		positionAdapter = new ArrayAdapter<Position>(this, android.R.layout.simple_spinner_item);
		positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionAdapter.addAll(positionList);
		positionSp.setAdapter(positionAdapter);
		positionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				Position posi = (Position) adapterView.getItemAtPosition(position);
				submitBtn.setClickable(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		for(int i=0;i<positionList.size();i++){
			if(positionList.get(i).getPositionName().equals(user.getPositionName())){
				deptSp.setSelection(i, true);
				break;
			}
		}
	}

	// 初始化actionbar
	private void initActionBar() {
		actionBar = this.getActionBar();
		if(!TextUtils.isEmpty(user.getRealName()))
			actionBar.setTitle(user.getRealName());
		else
			actionBar.setTitle(user.getUserName());
		actionBar.setIcon(R.drawable.icon_back_nol);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.update_person_actionbar, menu);
		return true;
	}
	
	TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			debug(s.toString());
			submitBtn.setClickable(true);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};

	@Override
	public void getMessage(TranObject msg) {

	}

	/***************************/
	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.putExtra("user", user);
		setResult(ActivityTag.PERSONNAL_INFO, i);
		finish();
	}
	
	private void debug(String s) {
		Log.v(TAG, s);
	}

}
