package com.task.activity;

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.DialogFactory;
import com.task.common.utils.Encode;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;

public class RegisterActivity extends MyActivity implements OnClickListener {

	@InjectView(R.id.register_btn)
	Button registerBtn;
	@InjectView(R.id.reg_back_btn)
	Button backBtn;
	@InjectView(R.id.reg_account)
	EditText accountEdit;
	@InjectView(R.id.reg_password)
	EditText pwdEdit;
	@InjectView(R.id.reg_password2)
	EditText pwdEdit2;
	@Inject
	Resources res;
	Gson gson = new Gson();

	private String TAG = "RegisterActivity";
	private MyApplication application;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);
		application = (MyApplication) this.getApplicationContext();
		registerBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在注册中...");
		mDialog.show();
	}

	@Override
	public void onBackPressed() {// 捕获返回键
		toast(RegisterActivity.this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.register_btn :
				submit();
				break;
			case R.id.reg_back_btn :
				toast(RegisterActivity.this);
				break;
			default :
				break;
		}
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context)
			.setTitle(res.getString(R.string.common_tip))
			.setMessage("亲！您真的不注册了吗？")
			.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
	}

	private void submit() {
		String userName = accountEdit.getText().toString();
		String passwd = pwdEdit.getText().toString();
		String passwd2 = pwdEdit2.getText().toString();
		if (userName.equals("") || passwd.equals("") || passwd2.equals("")) {
			DialogFactory.ToastDialog(RegisterActivity.this, res.getString(R.string.reg_submit), "亲！带*项是不能为空的哦");
		} else {
			if (passwd.equals(passwd2)) {
				// 提交注册信息
				if (application.isClientStart()) {// 如果已连接上服务器
					showRequestDialog();//显示提示友好界面
					Client client = application.getClient();
					ClientOutputThread out = client.getClientOutputThread();
					TranObject o = new TranObject(TranObjectType.REGISTER);
					User u = new User();
					u.setUserName(userName);
					u.setPassword(Encode.getEncode("MD5", passwd));
					o.setJson(gson.toJson(u));
					out.setMsg(o);
				} else {
					DialogFactory.ToastDialog(this, res.getString(R.string.reg_submit), "亲！未连接上服务器，请检查你的网络是否畅通或者联系管理员。");
				}

			} else {
				DialogFactory.ToastDialog(RegisterActivity.this, res.getString(R.string.reg_submit), "亲！您两次输入的密码不同哦");
			}
		} 
	}

	public void getMessage(TranObject msg) {
		switch (msg.getType()) {
			case REGISTER :
				User u = gson.fromJson(msg.getJson(),User.class);
				if (mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				if(!msg.isSuccess())
					DialogFactory.ToastDialog(RegisterActivity.this, res.getString(R.string.common_tip), msg.getMsg());
				
				break;

			default :
				break;
		}
	}
}
