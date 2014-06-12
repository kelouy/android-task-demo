package com.task.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;


import com.google.inject.Inject;

import android.R.anim;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends RoboFragmentActivity   {

	/*private static final String TAG = "MainActivity";

	
	// 定义FragmentTabHost对象 
	@InjectView(android.R.id.tabhost) FragmentTabHost mTabHost;
	@Inject Resources res;
	
	private RadioGroup mTabRg;
	private MenuInflater menu;// 菜单
	
	private Fragment1 fragment1 = null;

	@SuppressWarnings("rawtypes")
	private final Class[] fragments = { Fragment1.class, Fragment2.class,
			Fragment3.class, Fragment4.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		
		Fragment1 fragment1 = new Fragment1();
	}

	private void initView() {
		menu = new MenuInflater(this);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragments[i], null);
		}

		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu); 
		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_rb_1: 
					mTabHost.setCurrentTab(0);
					break;
				case R.id.tab_rb_2:
					mTabHost.setCurrentTab(1);
					break;
				case R.id.tab_rb_3:
					mTabHost.setCurrentTab(2);
					break;
				case R.id.tab_rb_4:
					mTabHost.setCurrentTab(3);
					break;
				default:
					break;
				}
			}
		});

		mTabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu.inflate(R.menu.exit_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	// 菜单选项事件处理
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit_button:
			showExitDialog();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showExitDialog() {
		new AlertDialog.Builder(MainActivity.this)
		.setTitle(res.getText(R.string.dialog_title))
		.setMessage(res.getText(R.string.dialog_exit_tip))
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(res.getText(R.string.dialog_text_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						stopService(new Intent(MyConst.TASK_SYNC_SERVICE));
						finish();
					}
				}).setNegativeButton(res.getText(R.string.dialog_text_cancel), null).create()
		.show();
	}

	//双击退出退出程序
	int index = 0;
	@Override
	public void onBackPressed() {
		index++;
		if(index == 1){
			Toast.makeText(this, R.string.exit_tip, Toast.LENGTH_SHORT).show();
			//如果在2秒内没有按第二次则要重新按
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					index = 0;
				}
			}, 2000);
		}else if(index == 2){
			index = 0;
			showExitDialog();
		}
	}

	@Override
	public void callCreatEvent() {
		Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();		
	}
	
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "MainActivity onDestroy...");
		stopService(new Intent(MyConst.TASK_SYNC_SERVICE));
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(MyConst.TASK_Notification_ID);
		mNotificationManager.cancel(MyConst.UPGRADE_Notification_ID);
		
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(MyConst.TASK_Notification_ID);
		mNotificationManager.cancel(MyConst.UPGRADE_Notification_ID);
	}
	
*/
}
