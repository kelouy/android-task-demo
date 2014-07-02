package com.task.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.task.client.ClientOutputThread;
import com.task.common.bean.Department;
import com.task.common.bean.Position;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.DialogFactory;
import com.task.common.utils.MyDialogTools;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class UpdatePersonalInfoActivity extends MyActivity {

	private static final String TAG = "UpdatePersonalInfoActivity";
	private MyApplication application;
	@InjectExtra("user") User old;	
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
	@Inject Resources res;
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
		
		if(!TextUtils.isEmpty(user.getPhoneNum()))
			phoneEdit.setText(user.getPhoneNum());
		
		if(user.getQq() > 0)
			qqEdit.setText(user.getQq()+"");
		
		if(!TextUtils.isEmpty(user.getEmail()))
			emailEdit.setText(user.getEmail());
		
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
	}
	

	//提交修改
	private void submit() {
		
		user.setRealName(realNameEdit.getText().toString());
		user.setEmail(emailEdit.getText().toString());
		if(qqEdit.getText().toString().length() > 0)
			user.setQq(Integer.parseInt(qqEdit.getText().toString()));
		user.setPhoneNum(phoneEdit.getText().toString());
		if(femaleRadio.isChecked())
			user.setSex(0);
		else 
			user.setSex(1);
		
		if(user.eq(old))
			return;
		// 通过Socket验证信息
		if (application.isClientStart()) {
			MyDialogTools.showDialog(this,"更新中…");
			ClientOutputThread out = application.getClient().getClientOutputThread();
			TranObject o = new TranObject(TranObjectType.UPDATE);
			o.setJson(gson.toJson(user));
			out.setMsg(o);
		} else {
			DialogFactory.showToast(this,"服务器连接失败！");
		}
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
				user.setDeptId(dept.getDeptId());
				user.setDeptName(dept.getDeptName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		for(int i=0;i<deptList.size();i++){
			if(deptList.get(i).getDeptName().equals(user.getDeptName())){
				deptSp.setSelection(i, true);
				user.setDeptId(deptList.get(i).getDeptId());
				old.setDeptId(deptList.get(i).getDeptId());
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
				user.setPositionId(posi.getPositionId());
				user.setPositionName(posi.getPositionName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		for(int i=0;i<positionList.size();i++){
			if(positionList.get(i).getPositionName().equals(user.getPositionName())){
				positionSp.setSelection(i, true);
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
		//actionBar.setIcon(R.drawable.icon_back_nol);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.update_person_actionbar, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home : 
				onBackPressed();
				break;
			default : 
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void getMessage(TranObject msg) {
		//只处理UPDATE消息
		if(msg.getType() == TranObjectType.UPDATE){
			MyDialogTools.closeDialog();
			if(msg.isSuccess()){
				Intent i = new Intent();
				i.putExtra("changed", true);
				i.putExtra("user", user);
				setResult(ActivityTag.PERSONNAL_INFO, i);
				finish();
			} else{
				DialogFactory.ToastDialog(this, res.getString(R.string.common_tip), msg.getMsg());
			}
				
		}
	}

	/***************************/
	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.putExtra("changed", false);
		setResult(ActivityTag.PERSONNAL_INFO, i);
		finish();
	}
	
	
	private void debug(String s) {
		Log.v(TAG, s);
	}

}
