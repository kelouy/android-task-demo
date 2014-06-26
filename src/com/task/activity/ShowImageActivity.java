package com.task.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowImageActivity extends RoboActivity {
	
	@InjectExtra("url") String url;
	@InjectView(R.id.show_img) ImageView mImg;
	@InjectView(R.id.show_back_btn) Button mBackBtn;
	PhotoViewAttacher attacher; 
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		initImage();
		attacher = new PhotoViewAttacher(mImg);  
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
				.build();
		imageLoader.displayImage(url, mImg, options);
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.nochange, R.anim.show_img_out);
	}
}
