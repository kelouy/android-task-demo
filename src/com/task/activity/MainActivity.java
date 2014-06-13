package com.task.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

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
	private RadioGroup tabGadioGroup;
	private MenuInflater menu;// 菜单 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate……");
		setContentView(R.layout.main_fragment);
		initView();
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
						break;
					case R.id.tab_rb_2 :
						tabHost.setCurrentTab(1);
						break;
					case R.id.tab_rb_3 :
						tabHost.setCurrentTab(2);
						break;
					case R.id.tab_rb_4 :
						tabHost.setCurrentTab(3);
						break;
					default :
						break;
				}
			}
		});
		tabHost.setCurrentTab(0);
	}
}
