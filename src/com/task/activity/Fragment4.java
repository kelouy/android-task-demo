package com.task.activity;

import roboguice.fragment.RoboFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.task.common.bean.User;
import com.task.common.utils.Utils;
import com.task.tools.component.MyApplication;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment4 extends RoboFragment {
	private final String TAG = "Fragment4";
	
	private User user = new User();
	private TextView pRealNameTV;
	private TextView pUserNameTV;
	private TextView pDeptNameTV;
	private TextView pPositionNameTV;
	private TextView pSexTV;
	private TextView pEmailTV;
	private TextView pPhoneNumTV;
	private TextView pQqTV;
	private ImageView pHeadIMG;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate ^^^");
		application =(MyApplication) getActivity().getApplication();
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.personal_info, container, false);
		pRealNameTV = (TextView) view.findViewById(R.id.person_realname);
		pUserNameTV = (TextView) view.findViewById(R.id.person_username);
		pDeptNameTV = (TextView) view.findViewById(R.id.person_deptname);
		pPositionNameTV = (TextView) view.findViewById(R.id.person_positionname);
		pSexTV = (TextView) view.findViewById(R.id.person_sex);
		pEmailTV = (TextView) view.findViewById(R.id.person_email);
		pPhoneNumTV = (TextView) view.findViewById(R.id.person_phonenum);
		pQqTV = (TextView) view.findViewById(R.id.person_qq);
		pHeadIMG = (ImageView) view.findViewById(R.id.person_head_img);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		DbUtils db = DbUtils.create(getActivity());
		try {
			user = db.findFirst(Selector.from(User.class).where("userId", "=", Utils.getMy().getUserId()));
		} catch (DbException e) {
			e.printStackTrace();
		}
		debug(user.toString());
		initImage();
		initData(user);
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
				.build();
		imageLoader.displayImage(user.getHeadUrl(), pHeadIMG, options);
		if(!TextUtils.isEmpty(user.getHeadUrl())){
			pHeadIMG.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ShowImageActivity.class);
					intent.putExtra("url", user.getHeadUrl());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.show_img_in, R.anim.nochange);
					
				}
			});
		}
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
	
		/*********************************/
	
	@Override
	public void onPause() {
		super.onPause();
		debug("onPause...");
	}
	
	private void debug(String s) {
		Log.v(TAG, s);
	}
	
}
