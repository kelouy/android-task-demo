package com.task.common.utils;

import com.task.activity.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class DialogFactory {
	public static Dialog creatRequestDialog(final Context context, String tip) {

		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.dialog_layout);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int width = Utils.getScreenWidth(context);
		lp.width = (int) (0.6 * width);

		TextView msgTV = (TextView) dialog.findViewById(R.id.tvLoad);
		if (tip == null || tip.length() == 0) {
			msgTV.setText(R.string.sending_request);
		} else {
			msgTV.setText(tip);
		}
 
		return dialog;
	}
	

	public static void ToastDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton("确定", null).create().show();
	}
	
	public static void showToast(Context context,  String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
