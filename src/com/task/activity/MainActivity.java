package com.task.activity;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.google.inject.Inject;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboFragmentActivity {

	private static final String TAG = "MainActivity";
	@SuppressWarnings("rawtypes")
	private final Class[] fragments = {Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class};
	// 定义FragmentTabHost对象
	@InjectView(android.R.id.tabhost)
	FragmentTabHost tabHost;
	@Inject
	Resources res;
	private TextView tvTitle;
	private ProgressBar pbUpdate;
	private RadioGroup tabGadioGroup;
	private MenuInflater menu;// 菜单 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate……");
		setContentView(R.layout.main_fragment);
		initActionBar();
		initView();
	}
	
	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT, 
				ActionBar.LayoutParams.MATCH_PARENT, 
				Gravity.CENTER);
		View actionBarView = getLayoutInflater().inflate(R.layout.main_actionbar, null);
		actionBar.setCustomView(actionBarView, lp);
		// actionBar.setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
		actionBar.setDisplayShowHomeEnabled(false);// 使左上角图标可点击，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true); // 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
		tvTitle = (TextView) actionBar.getCustomView().findViewById(android.R.id.title);
		pbUpdate = (ProgressBar) actionBar.getCustomView().findViewById(R.id.main_progressBar);
		actionBar.show();
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
						tvTitle.setText("待办事件");
						pbUpdate.setVisibility(View.GONE);
						break;
					case R.id.tab_rb_2 :
						tabHost.setCurrentTab(1);
						tvTitle.setText("我的同事");
						pbUpdate.setVisibility(View.VISIBLE);
						break;
					case R.id.tab_rb_3 :
						tabHost.setCurrentTab(2);
						tvTitle.setText("动态");
						break;
					case R.id.tab_rb_4 :
						tabHost.setCurrentTab(3);
						tvTitle.setText("我");
						break;
					default :
						break;
				}
			}
		});
		tabHost.setCurrentTab(0);
	}
}
