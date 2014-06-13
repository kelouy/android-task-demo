package com.task.activity;

import roboguice.fragment.RoboFragment;
import com.task.common.utils.Constants;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Fragment4 extends RoboFragment {
	private final String TAG = "Fragment4";
	private View clear;
	private View versionMsg;
	private View aboutUs;
	private View help;
	private View changeUser;
	private AlertDialog versionMsgDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate ^^^");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment4, container, false);
		clear = view.findViewById(R.id.clear);
		versionMsg = view.findViewById(R.id.version_msg);
		aboutUs = view.findViewById(R.id.about_us);
		help = view.findViewById(R.id.help);
		changeUser = view.findViewById(R.id.change_user);
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v(TAG, "clear.setOnClickListener");
			}
		});
		versionMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == versionMsgDialog)
					showVersionMsgDialog();
				versionMsgDialog.show();
			}
		});
		aboutUs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(TAG, "aboutUs.setOnClickListener");
			}
		});
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(TAG, "help.setOnClickListener");
			}
		});
		
		changeUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().stopService(new Intent(Constants.TASK_SYNC_SERVICE));
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
		return view;
	}
	
	private void showVersionMsgDialog() {
		/*versionMsgDialog = new AlertDialog.Builder(getActivity())
							.setTitle("版本信息")
							.setMessage("当前版本:" + VersionUtils.getVerName())
							.setPositiveButton(R.string.dialog_text_ok, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create();*/
	} 
	
	@Override
	public void onPause() {
		super.onPause();
		if(null != versionMsgDialog){
			versionMsgDialog.dismiss();
			versionMsgDialog = null;
		}
	}
	
}
