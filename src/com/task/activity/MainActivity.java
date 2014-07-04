package com.task.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.google.inject.Inject;
import com.task.tools.component.MyActionBar;
import com.task.tools.component.popupwindow.ActionItem;
import com.task.tools.component.popupwindow.TitlePopup;

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
	private TitlePopup titlePopup;
	private RadioGroup tabGadioGroup;
	private MenuInflater menu;// 菜单 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate……");
		setContentView(R.layout.main_fragment);
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
			}
		});
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
	
	private void debug(String s) {
		Log.v(TAG, s);
	}
}
