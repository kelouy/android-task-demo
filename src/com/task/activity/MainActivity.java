package com.task.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import com.google.inject.Inject;
import com.task.tools.component.MyActionBar;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboFragmentActivity{

	private static final String TAG = "MainActivity";
	@SuppressWarnings("rawtypes")
	private final Class[] fragments = {Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class};
	// 定义FragmentTabHost对象
	@InjectView(android.R.id.tabhost)
	FragmentTabHost tabHost;
	@Inject
	Resources res;
	MyActionBar actionBar;
	private RadioGroup tabGadioGroup;
	private MenuInflater menu;// 菜单 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate……");
		setContentView(R.layout.main_fragment);
		actionBar = new MyActionBar(this);
		actionBar.show();
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
						actionBar.setTitle("待办事件");
						break;
					case R.id.tab_rb_2 :
						tabHost.setCurrentTab(1);
						actionBar.setTitle("我的同事");
						actionBar.showProgressBar(false);
						break;
					case R.id.tab_rb_3 :
						tabHost.setCurrentTab(2);
						actionBar.setTitle("动态");
						actionBar.showProgressBar(false);
						break;
					case R.id.tab_rb_4 :
						tabHost.setCurrentTab(3);
						actionBar.setTitle("我");
						actionBar.showProgressBar(false);
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
}
