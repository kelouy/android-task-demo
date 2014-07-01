package com.task.activity;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.task.common.bean.User;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.Constants;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class PersonalInfoActivity extends RoboActivity implements OnClickListener{
	private static String TAG = "PersonalInfoActivity";
	
	@InjectExtra(value="user",optional=true) User user;
	@InjectView(R.id.person_realname) TextView pRealNameTV;
	@InjectView(R.id.person_username) TextView pUserNameTV;
	@InjectView(R.id.person_deptname) TextView pDeptNameTV;
	@InjectView(R.id.person_positionname) TextView pPositionNameTV;
	@InjectView(R.id.person_sex) TextView pSexTV;
	@InjectView(R.id.person_email) TextView pEmailTV;
	@InjectView(R.id.person_phonenum) TextView pPhoneNumTV;
	@InjectView(R.id.person_qq) TextView pQqTV;
	@InjectView(R.id.person_head_img) ImageView pHeadIMG;
	ActionBar actionBar;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate……  ");
		setContentView(R.layout.personal_info);
		initActionBar();
		initData(user);
		initImage();
	}
	
	@SuppressWarnings("static-access")
	private void initImage() {
		imageLoader = imageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.person_head)
				.showImageForEmptyUri(R.drawable.person_head)
				.showImageOnFail(R.drawable.person_head).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
				.considerExifParams(true)
				//.displayer(new RoundedBitmapDisplayer(10))//设置圆角
				//.displayer(new FadeInBitmapDisplayer(100))
				.build();
		imageLoader.displayImage(user.getHeadUrl(), pHeadIMG, options);
		if(!TextUtils.isEmpty(user.getHeadUrl())){
			pHeadIMG.setOnClickListener(this);
		}
	}

	//初始化actionbar 
	private void initActionBar() {
		actionBar = this.getActionBar();
		actionBar.setTitle(user.getRealName());
		actionBar.setIcon(R.drawable.icon_back_nol);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	//初始化数据 
	private void initData(User user){
		if(null != user){
			if(!TextUtils.isEmpty(user.getRealName())){
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
				pQqTV.setText(user.getQq()+"");
			if(user.getSex() == 0 ) 
				pSexTV.setText("女");
			else
				pSexTV.setText("男");
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.person_actionbar, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home : 
				onBackPressed();
				break;
			case R.id.person_menu_updatehead : 
				Intent intent1 = new Intent(this, PicCutAndUploadActivity.class);
				intent1.putExtra("user", user);
				startActivityForResult(intent1, ActivityTag.PIC_CUT_AND_UPLOAD_HEAD);
				break;
			case R.id.person_menu_updatedata : 
				Intent intent2 = new Intent(this, UpdatePersonalInfoActivity.class);
				intent2.putExtra("user", user);
				startActivityForResult(intent2, ActivityTag.PERSONNAL_INFO);
				break;
			default : 
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.person_head_img :  
				Intent intent = new Intent(PersonalInfoActivity.this, ShowImageActivity.class);
				intent.putExtra("url", user.getHeadUrl());
				startActivity(intent);
				overridePendingTransition(R.anim.show_img_in, R.anim.nochange);
				break;
			default : 
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		debug("requestCode:"+requestCode+"  resultCode:"+resultCode);
		switch(requestCode){
			case ActivityTag.PIC_CUT_AND_UPLOAD_HEAD : 
				setHeadImg(data);
				break;
			case ActivityTag.PERSONNAL_INFO : 
				setUser(data);
				break;
			default : break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//重置个人信息
	private void setUser(Intent intent) {
		User u = (User) intent.getSerializableExtra("user");
		if(u != null)
			initData(u);
	}

	//从上传头像页面返回后重置头像图片
	private void setHeadImg(Intent intent) {
		String fileName =  intent.getStringExtra("fileName");
		if(!TextUtils.isEmpty(fileName))
			imageLoader.displayImage(fileName, pHeadIMG, options);
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
