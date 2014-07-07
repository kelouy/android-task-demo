package com.task.tools.component;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.task.activity.R;

public class MyActionBar {

	private ActionBar actionBar;
	private TextView tvTitle;
	private ProgressBar pbUpdate;
	private ImageButton rightBtn;
	private OnRightBtnClickListener onRightBtnClickListener;
	
	/**
	 * 
	 * @param activity
	 * @param title	actionbar标题
	 * @param rightImageBtnId 右上角图片
	 * @param isShowProgressBar	是否显示progressbar
	 * @param isShowRightBtn	是否显示右上角图标
	 */
	public MyActionBar(final Activity activity,String title,int rightImageBtnId,boolean isShowProgressBar,boolean isShowRightBtn) {
		actionBar = activity.getActionBar();
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT, 
				ActionBar.LayoutParams.MATCH_PARENT, 
				Gravity.CENTER);
		View actionBarView = activity.getLayoutInflater().inflate(R.layout.main_actionbar, null);
		actionBar.setCustomView(actionBarView, lp);
		// actionBar.setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
		actionBar.setDisplayShowHomeEnabled(false);// 使左上角图标可点击，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true); // 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
		tvTitle = (TextView) actionBar.getCustomView().findViewById(android.R.id.title);
		pbUpdate = (ProgressBar) actionBar.getCustomView().findViewById(R.id.actionbar_progressBar);
		rightBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.actionbar_rightBtn);
		setRightImageBtnSrc(rightImageBtnId);
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation anim = AnimationUtils.loadAnimation(activity, R.anim.xuanzhuan);
				v.startAnimation(anim);
				if(onRightBtnClickListener != null)
					onRightBtnClickListener.onClick(v);
			}
		});
		
		setTitleAndShowItem(title,isShowProgressBar,isShowRightBtn );
		show();
	}
	
	/**
	 * 
	 * @param title 设置title
	 * @param isShowProgressBar	是否显示progressbar
	 * @param isShowRightBtn	是否显示右上角图标
	 */
	public void setTitleAndShowItem(String title,boolean isShowProgressBar,boolean isShowRightBtn ) {
		setTitle(title);
		showProgressBar(isShowProgressBar);
		showRightImageBtn(isShowRightBtn);
	}
	
	/**
	 * 
	 * @param title 设置title
	 * @param isShowRightBtn	是否显示右上角图标
	 */
	public void setTitleAndShowItem(String title,boolean isShowRightBtn ) {
		setTitle(title);
		showRightImageBtn(isShowRightBtn);
	}
	
	public void setTitle(String title){
		if(title != null)
			tvTitle.setText(title);
	}
	
	public void showRightImageBtn(boolean flag){
		if(flag)
			rightBtn.setVisibility(View.VISIBLE);
		else
			rightBtn.setVisibility(View.GONE);
	}
	
	public void setRightImageBtnSrc(int id){
		if(id>0)
			rightBtn.setImageResource(id);
	}
	
	public void showProgressBar(boolean flag){
		if(flag){
			pbUpdate.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					pbUpdate.setVisibility(View.GONE);
				}
			}, 6000);
		}else{
			pbUpdate.setVisibility(View.GONE);
		}
		
	}
	
	public void show(){
		if(!actionBar.isShowing())
			actionBar.show();
	}
	
	public void hide(){
		if(actionBar.isShowing())
			actionBar.hide();
	}
	
	public ImageButton getRightBtn() {
		return rightBtn;
	}
	

	public void setOnRightBtnClickListener(OnRightBtnClickListener onRightBtnClickListener) {
		this.onRightBtnClickListener = onRightBtnClickListener;
	}

	public static interface OnRightBtnClickListener{
		void onClick(View view);
	}
}
