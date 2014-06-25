package com.task.activity;

import com.task.common.bean.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class PersonalInfoActivity extends RoboActivity {
	private static String TAG = "PersonalInfoActivity";
	
	@InjectExtra(value="user",optional=true) User user;
	@InjectView(R.id.person_title) TextView pTitleTV;
	@InjectView(R.id.person_realname) TextView pRealNameTV;
	@InjectView(R.id.person_username) TextView pUserNameTV;
	@InjectView(R.id.person_deptname) TextView pDeptNameTV;
	@InjectView(R.id.person_positionname) TextView pPositionNameTV;
	@InjectView(R.id.person_sex) TextView pSexTV;
	@InjectView(R.id.person_email) TextView pEmailTV;
	@InjectView(R.id.person_phonenum) TextView pPhoneNumTV;
	@InjectView(R.id.person_qq) TextView pQqTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate……");
		setContentView(R.layout.personal_info);
		initData();
	}
	
	//初始化数据 
	private void initData(){
		if(null != user){
			if(!TextUtils.isEmpty(user.getRealName())){
				pTitleTV.setText(user.getRealName());
				pRealNameTV.setText(user.getRealName());
			}
			if(!TextUtils.isEmpty(user.getUserName()))
				pUserNameTV.setText(user.getUserName());
			if(!TextUtils.isEmpty(user.getDeptName()))
				pDeptNameTV.setText(user.getDeptName());
			if(!TextUtils.isEmpty(user.getEmail()))
				pEmailTV.setText(user.getEmail());
			if(!TextUtils.isEmpty(user.getPhoneNum()))
				pPhoneNumTV.setText(user.getPhoneNum());
			if(!TextUtils.isEmpty(user.getPositionName()))
				pPositionNameTV.setText(user.getPositionName());
			if(user.getQq() > 0)
				pQqTV.setText(user.getQq());
			pSexTV.setText(user.getSex());
		}
	}
	
	
	/*************************************/
	@Override
	public void onBackPressed() {
		finish();
	}
	
	void debug(String s){
		Log.v(TAG, s);
	}
}
