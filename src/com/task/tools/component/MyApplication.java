package com.task.tools.component;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.task.client.Client;
import com.task.common.utils.Constants;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	private String TAG = "MyApplication";
	private Client client;// 客户端
	private boolean isClientStart;// 客户端连接是否启动

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "onCreate……");
		client = new Client(Constants.SERVER_IP, Constants.SERVER_PORT);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().build();
		ImageLoader.getInstance().init(config);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public boolean isClientStart() {
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}
}
