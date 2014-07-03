package com.task.activity;


import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.Constants;
import com.task.common.utils.DialogFactory;
import com.task.common.utils.Encode;
import com.task.common.utils.MyDialogTools;
import com.task.common.utils.Utils;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;
import com.task.tools.component.SharePreferenceUtil;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class PersonalInfoActivity extends MyActivity implements OnClickListener{
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
	@Inject Resources res;
	ActionBar actionBar;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	MyApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate……  ");
		setContentView(R.layout.personal_info);
		application = (MyApplication) getApplication();
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
		if(!TextUtils.isEmpty(user.getRealName()))
			actionBar.setTitle(user.getRealName());
		else
			actionBar.setTitle(user.getUserName());
		//actionBar.setIcon(R.drawable.icon_back_nol);
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
		if(Utils.getMy()!=null && Utils.getMy().getUserName().equals("admin")){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.person_actionbar, menu);
		}
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
			case R.id.person_menu_updatepwd : 
				showUpdatePwdDialog();
				break;
			default : 
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showUpdatePwdDialog() {
		final View view = LayoutInflater.from(this).inflate(R.layout.setting_pwd, null);
		new AlertDialog.Builder(this).setTitle("重置密码").setView(view).setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 把ip和port保存到文件中
				EditText pwdText = (EditText) view.findViewById(R.id.setting_pwd);
				EditText pwd2Text = (EditText) view.findViewById(R.id.setting_pwd2);
				String pwd = pwdText.getText().toString();
				String pwd2 = pwd2Text.getText().toString();
				if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd2)){
					DialogFactory.showToast(PersonalInfoActivity.this, "密码不能为空！");
					return;
				}else if(!pwd.equals(pwd2)){
					DialogFactory.showToast(PersonalInfoActivity.this, "密码不一至！");
					return;
				}else if (application.isClientStart()) {
					//showRequestDialog(); 
					Client client = application.getClient();
					ClientOutputThread out = client.getClientOutputThread();
					TranObject o = new TranObject(TranObjectType.UPDATE_PWD);
					user.setPassword(Encode.getEncode("MD5", pwd));
					o.setJson(new Gson().toJson(user));
					out.setMsg(o);
					MyDialogTools.showDialog(PersonalInfoActivity.this, "更新中…");
					dialog.dismiss();
				} else {
					DialogFactory.showToast(PersonalInfoActivity.this, "服务器连接失败！");
				}
			}
		}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
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
		boolean flag = intent.getBooleanExtra("changed", false);
		if(flag){
			DbUtils db = DbUtils.create(this);
			try {
				user = db.findFirst(Selector.from(User.class).where("userId", "=", user.getUserId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
			initData(user);
		}
	}

	//从上传头像页面返回后重置头像图片
	private void setHeadImg(Intent intent) {
		String fileName =  intent.getStringExtra("fileName");
		if(!TextUtils.isEmpty(fileName)){
			imageLoader.displayImage(Constants.IMG_ROOT_URL+fileName, pHeadIMG, options);
			pHeadIMG.setOnClickListener(this);
		}
	}
	

	@Override
	public void getMessage(TranObject msg) {
		if(msg.getType() == TranObjectType.UPDATE_PWD){
			MyDialogTools.closeDialog();
			if(msg.isSuccess())
				DialogFactory.showToast(PersonalInfoActivity.this, "更新成功！");
			else
				DialogFactory.showToast(PersonalInfoActivity.this, msg.getMsg());
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
