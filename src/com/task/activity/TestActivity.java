package com.task.activity;



import com.task.activity.R;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.Department;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.tools.component.MyApplication;
import com.task.tools.component.MyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class TestActivity extends Activity {

	private String TAG = "TestActivity";
	private Button button1 = null;
	private Button button2 = null;
	private MyApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate……");
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉状态栏
		setContentView(R.layout.main);
		application = (MyApplication) getApplication();
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "startService……");
				Intent intent = new Intent(TestActivity.this, MyService.class);
				startService(intent);
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "out.setMsg TranObjectType.TEST……");
				Client client = application.getClient();
				ClientOutputThread out = client.getClientOutputThread();
				TranObject o = new TranObject(TranObjectType.TEST);
				out.setMsg(o);
			}
		});
	}
	
	@Override
	protected void onStart() {
		Log.e(TAG, "onStart……");
		super.onStart();
	}
}
