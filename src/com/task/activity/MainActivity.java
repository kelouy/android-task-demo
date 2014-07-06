package com.task.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.DialogFactory;
import com.task.common.utils.Encode;
import com.task.common.utils.MyDialogTools;
import com.task.common.utils.Utils;
import com.task.tools.component.MyActionBar;
import com.task.tools.component.MyApplication;
import com.task.tools.component.popupwindow.ActionItem;
import com.task.tools.component.popupwindow.TitlePopup;

public class MainActivity extends RoboFragmentActivity{

	private static final String TAG = "MainActivity";
	@SuppressWarnings("rawtypes")
	private final Class[] fragments = {Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class};
	private TitlePopup titlePopup;
	private RadioGroup tabGadioGroup;
	private MenuInflater menu;// 菜单  
	// 定义FragmentTabHost对象
	@InjectView(android.R.id.tabhost)
	FragmentTabHost tabHost;
	@Inject
	Resources res;
	MyActionBar actionBar;
	MyApplication application;
	User user = Utils.getMy();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate……");
		setContentView(R.layout.main_fragment);
		application = (MyApplication) getApplication();
		initActionBarAndTitlePopup();
		initView();
		
		/*ActionBar actionBar2 = getActionBar();
		actionBar2.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		                // 依次添加3个Tab页，并为3个Tab标签添加事件监听器
		                actionBar2.addTab(actionBar2.newTab().setText("第一页")
		                        .setTabListener(this));
		                actionBar2.addTab(actionBar2.newTab().setText("第二页")
		                        .setTabListener(this));
		                actionBar2.addTab(actionBar2.newTab().setText("第三页")
		                        .setTabListener(this));*/
	}
	 
	private void initActionBarAndTitlePopup() {
		titlePopup = new TitlePopup(this);
		titlePopup.addAction(new ActionItem(this, "上传头像", R.drawable.icon_head));
		titlePopup.addAction(new ActionItem(this, "修改资料", R.drawable.icon_person_data));
		titlePopup.addAction(new ActionItem(this, "修改密码", R.drawable.icon_pwd));
		titlePopup.addAction(new ActionItem(this, "系统设置", R.drawable.icon_setting));
		actionBar = new MyActionBar(this, "待办事件", R.drawable.panel_add_icon, true,false);
		actionBar.setOnRightBtnClickListener(new MyActionBar.OnRightBtnClickListener() {
			@Override
			public void onClick(View view) {  
				titlePopup.show(view);
			}
		});
		
		titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				debug("item position "+position);
				goActivity(position);
			}
		});
	}
	
	private void goActivity(int position) {
		switch(position){
			case 0: 
				Intent intent1 = new Intent(this, PicCutAndUploadActivity.class);
				intent1.putExtra("user", Utils.getMy());
				startActivityForResult(intent1, ActivityTag.PIC_CUT_AND_UPLOAD_HEAD);
				break;
			case 1 : 
				Intent intent2 = new Intent(this, UpdatePersonalInfoActivity.class);
				intent2.putExtra("user", Utils.getMy());
				startActivityForResult(intent2, ActivityTag.PERSONNAL_INFO);
				break;
			case 2: 
				showUpdatePwdDialog();
				break;
			case 3:
				break;
			default : 
				break;
		}
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		debug("onCreateOptionsMenu "+menu );
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fragment4_actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		debug("onOptionsItemSelected "+item.getItemId());
		return super.onOptionsItemSelected(item);
	}*/
	

	private void initView() {
		menu = new MenuInflater(this);
		tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容 
			TabSpec tabSpec = tabHost.newTabSpec(i + "").setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			tabHost.addTab(tabSpec, fragments[i], null);
		}

		tabGadioGroup = (RadioGroup) findViewById(R.id.tab_rg_menu);
		tabGadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.tab_rb_1 :
						tabHost.setCurrentTab(0);
						actionBar.setTitleAndShowItem("待办事件",false);
						break;
					case R.id.tab_rb_2 :
						tabHost.setCurrentTab(1);
						actionBar.setTitleAndShowItem("我的同事",false,false);
						break;
					case R.id.tab_rb_3 :
						tabHost.setCurrentTab(2);
						actionBar.setTitleAndShowItem("动态",false,false);
						break;
					case R.id.tab_rb_4 :
						tabHost.setCurrentTab(3);
						actionBar.setTitleAndShowItem("我",false,true);
						break;
					default :
						break;
				}
			}
		});
		tabHost.setCurrentTab(0);
	}


	/*@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.e(TAG, tab.toString()+"=="+ft.toString());
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		debug("requestCode="+requestCode+"  resultCode="+resultCode+" data="+data);
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
					DialogFactory.showToast(MainActivity.this, "密码不能为空！");
					return;
				}else if(!pwd.equals(pwd2)){
					DialogFactory.showToast(MainActivity.this, "密码不一至！");
					return;
				}else if (application.isClientStart()) {
					//showRequestDialog(); 
					Client client = application.getClient();
					ClientOutputThread out = client.getClientOutputThread();
					TranObject o = new TranObject(TranObjectType.UPDATE_PWD);
					user.setPassword(Encode.getEncode("MD5", pwd));
					o.setJson(new Gson().toJson(user));
					out.setMsg(o);
					MyDialogTools.showDialog(MainActivity.this, "更新中…");
					dialog.dismiss();
				} else {
					DialogFactory.showToast(MainActivity.this, "服务器连接失败！");
				}
			}
		}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
	}
	
	/********************************************/
	
	private void debug(String s) {
		Log.v(TAG, s);
	}
}
