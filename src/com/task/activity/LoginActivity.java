package com.task.activity;


import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.Constants;
import com.task.common.utils.DialogFactory;
import com.task.common.utils.Encode;
import com.task.common.utils.SharePreferenceUtil;
import com.task.tools.MyActivity;
import com.task.tools.MyApplication;
import com.task.tools.MyService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



/**
 * 登录
 * 
 */
public class LoginActivity extends MyActivity implements OnClickListener {
	@InjectView(R.id.regist_btn)  		Button registerBtn;
	@InjectView(R.id.login_btn) 		Button loginBtn;
	@InjectView(R.id.login_accounts) 	EditText accountsEdit;
	@InjectView(R.id.login_password) 	EditText passwordEdit;
	@InjectView(R.id.auto_save_password) CheckBox autoSavePassword;
	@InjectView(R.id.more) 				View moreView;// “更多登录选项”的view
	@InjectView(R.id.more_image) 		ImageView moreImage;// “更多登录选项”的箭头图片
	@InjectView(R.id.moremenu) 			View moreMenuView;// “更多登录选项”中的内容view
	@Inject Resources res;
	private 							MenuInflater mi;// 菜单
	private								MyApplication application;
	private String TAG = "LoginActivity";
	private boolean mShowMenu = false;// “更多登录选项”的内容是否显示
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		application = (MyApplication) this.getApplicationContext();
		initView();
		mi = new MenuInflater(this); 
	}

	@Override
	protected void onResume() {// 在onResume方法里面先判断网络是否可用，再启动服务,这样在打开网络连接之后返回当前Activity时，会重新启动服务联网，
		super.onResume();
		if (isNetworkAvailable()) {
			Intent service = new Intent(this, MyService.class);
			startService(service);
		} else {
			toast(this);
		}
	}

	public void initView() {
		moreView.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		if (autoSavePassword.isChecked()) {
			SharePreferenceUtil util = new SharePreferenceUtil(this, Constants.SAVE_USER);
			accountsEdit.setText(util.getId());
			passwordEdit.setText(util.getPasswd());
		}
	}

	/**
	 * “更多登录选项”内容的显示方法
	 * 
	 * @param bShow
	 *            是否显示
	 */
	public void showMoreView(boolean bShow) {
		if (bShow) {
			moreMenuView.setVisibility(View.GONE);
			moreImage.setImageResource(R.drawable.login_more_up);
			mShowMenu = true;
		} else {
			moreMenuView.setVisibility(View.VISIBLE);
			moreImage.setImageResource(R.drawable.login_more);
			mShowMenu = false;
		}
	}

	/**
	 * 处理点击事件
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.more:
			showMoreView(!mShowMenu);
			break;
		case R.id.regist_btn:
			goRegisterActivity();
			break;
		case R.id.login_btn:
			submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入注册界面
	 */
	public void goRegisterActivity() {
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击登录按钮后，弹出验证对话框
	 */
	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
	}

	/**
	 * 提交账号密码信息到服务器
	 */
	private void submit() {
		String accounts = accountsEdit.getText().toString();
		String password = passwordEdit.getText().toString();
		if (accounts.length() == 0 || password.length() == 0) {
			DialogFactory.ToastDialog(this, res.getString(R.string.common_tip), "亲！帐号或密码不能为空哦");
		} else {
			// 通过Socket验证信息
			if (application.isClientStart()) {
				showRequestDialog();
				Client client = application.getClient();
				ClientOutputThread out = client.getClientOutputThread();
				TranObject<User> o = new TranObject<User>(TranObjectType.LOGIN);
				User u = new User();
				u.setUserId(Integer.parseInt(accounts));
				u.setPassword(Encode.getEncode("MD5", password));
				o.setObject(u);
				out.setMsg(o);
			} else {
				DialogFactory.ToastDialog(LoginActivity.this, res.getString(R.string.common_tip),
						"亲！服务器暂未开放哦");
			}
		}
	}

	
	// 依据自己需求处理父类广播接收者收取到的消息
	public void getMessage(TranObject msg) {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		Log.e(TAG, "getmsg from brocastreceive");
		if (msg != null) {
			if (msg.isSuccess()) {
				switch (msg.getType()) {
				case LOGIN:// LoginActivity只处理登录的消息
					User user = (User) msg.getObject();
					if (user != null) {
						// 保存用户信息
						SharePreferenceUtil util = new SharePreferenceUtil(
								LoginActivity.this, Constants.SAVE_USER);
						util.setId(accountsEdit.getText().toString());
						util.setPasswd(passwordEdit.getText().toString());

						/*
						 * UserDB db = new UserDB(LoginActivity.this);
						 * db.addUser(list);
						 */

						Intent i = new Intent(LoginActivity.this,
								MainActivity.class);
						i.putExtra(Constants.MSGKEY, user);
						startActivity(i);
						finish();
					} else {
						DialogFactory.ToastDialog(LoginActivity.this, res.getString(R.string.common_tip),
								"亲！账号或密码错误。");
					}
					break;
				default:
					break;
				}
			} else {
				DialogFactory.ToastDialog(LoginActivity.this,
						res.getString(R.string.common_tip), msg.getMsg());
			}
		}
	}

	@Override
	// 添加菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		mi.inflate(R.menu.login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单选项添加事件处理
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_menu_setting:
			setDialog();
			break;
		case R.id.login_menu_exit:
			exitDialog(LoginActivity.this, res.getString(R.string.common_tip), "亲！您真的要退出吗？");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {// 捕获返回按键
		exitDialog(LoginActivity.this, res.getString(R.string.common_tip), "亲！您真的要退出吗？");
	}

	/**
	 * 退出时的提示框
	 * 
	 * @param context
	 *            上下文对象
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 */
	private void exitDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (application.isClientStart()) {// 如果连接还在，说明服务还在运行
							// 关闭服务
							Intent service = new Intent(LoginActivity.this,
									MyService.class);
							stopService(service);
						}
						close();// 调用父类自定义的循环关闭方法
					}
				}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
	}

	/**
	 * “设置”菜单选项的功能实现
	 */
	private void setDialog() {
		final View view = LayoutInflater.from(LoginActivity.this).inflate(
				R.layout.setting_view, null);
		new AlertDialog.Builder(LoginActivity.this).setTitle("设置服务器ip、port")
				.setView(view)
				.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 把ip和port保存到文件中
						EditText ipEditText = (EditText) view
								.findViewById(R.id.setting_ip);
						EditText portEditText = (EditText) view
								.findViewById(R.id.setting_port);
						String ip = ipEditText.getText().toString();
						String port = portEditText.getText().toString();
						SharePreferenceUtil util = new SharePreferenceUtil(
								LoginActivity.this, Constants.IP_PORT);
						if (ip.length() > 0 && port.length() > 0) {
							util.setIp(ip);
							util.setPort(Integer.valueOf(port));
							Toast.makeText(getApplicationContext(),
									"亲！保存成功，重启生效哦", 0).show();
							finish();
						}else{
							Toast.makeText(getApplicationContext(),
									"亲！ip和port都不能为空哦", 0).show();
						}
					}
				}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context)
				.setTitle(res.getString(R.string.common_tip))
				.setMessage("亲！您的网络连接未打开哦")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}
						}).setNegativeButton(res.getString(R.string.cancel), null).create().show();
	}
}
