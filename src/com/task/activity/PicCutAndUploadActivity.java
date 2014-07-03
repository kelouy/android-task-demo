package com.task.activity;

import java.io.File;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.task.client.Client;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.Constants;
import com.task.common.utils.FileUtils;
import com.task.common.utils.MyDate;
import com.task.common.utils.MyDialogTools;
import com.task.tools.component.MyActivity;
import com.task.tools.component.MyApplication;
import com.task.tools.component.MyDialog;

public class PicCutAndUploadActivity extends MyActivity {
	private static String TAG = "PicCutAndUploadActivity";
	private String fileName;
	private MyApplication application;
	private Gson gson = new Gson();
	@InjectView(R.id.cut_img) ImageView cutImg;
	@InjectExtra("user") User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cut_pic_upload);
		application = (MyApplication) this.getApplicationContext();
		ShowPickDialog();
		//showDialog();
		//dialog.setInfo("上传");
	}

	private void ShowPickDialog() {
		new AlertDialog.Builder(this).setTitle("设置头像").setNeutralButton("图库", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				/**
				 * 调用系统图库
				 */
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				/**
				 * 设置过滤图片类型
				 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 * intent.setType(""image/*");
				 */
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, ActivityTag.PIC_TAKE);

			}
		}).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.png")));
				startActivityForResult(intent, ActivityTag.PIC_LIB);
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onBackPressed();
			}
		}).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ActivityTag.PIC_TAKE:
				startPhotoZoom(data.getData());
				break;
			case ActivityTag.PIC_LIB:
				File temp = new File(Environment.getExternalStorageDirectory()+ "/temp.png");
				startPhotoZoom(Uri.fromFile(temp));
				break;
			case ActivityTag.PIC_CUT:
				if(data != null){
					picCutedCallback(data);
				}else{
					ShowPickDialog();
				}
				break;
			default:
				break;

			}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 调用系统的截图工具，
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置在开启的Intent中设置显示的VIEW可裁剪 
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例 
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高 
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, ActivityTag.PIC_CUT);
	}
	
	/**
	 * 
	 * @param picdata
	 */
	private void picCutedCallback(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			cutImg.setImageBitmap(photo);
			String url = Environment.getExternalStorageDirectory()+"/taskimagecache/";
			FileUtils.addBitmapToSD(url, "temp.png", photo);
			
			//上传图片到服务器
			fileName = MyDate.getDateYYYYMMDDHHMMSS()+".png";
			RequestParams params = new RequestParams(); 
			//params.addBodyParameter("fileName", MyDate.getDateYYYYMMDDHHMMSS()+".png");
			params.addBodyParameter("file", new File(url,"temp.png"));
			//params.setBodyEntity(new BodyParamsEntity("multipart/form-data"));
			HttpUtils http = new HttpUtils(); 
			http.send(HttpMethod.POST,
				Constants.IMG_SERVER_URL+"&name="+fileName,
			    params,
			    new RequestCallBack<String>() {

			        @Override
			        public void onStart() {
			        	MyDialogTools.showDialog(PicCutAndUploadActivity.this);
			        	MyDialogTools.setText("开始上传");
			        }

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {
			        	debug("total:"+total+"  current:"+current+"   isUploading:"+isUploading);
			        	MyDialogTools.setText((int)(current/total)*100+"%");
			        }

			        @Override
			        public void onSuccess(ResponseInfo<String> responseInfo) {
			        	submit();
			        	//closeDialog();
			        	//Toast.makeText(PicCutAndUploadActivity.this, "图片上传成功！", Toast.LENGTH_SHORT).show();
			        }

					@Override
					public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
						Toast.makeText(PicCutAndUploadActivity.this, "图片上传出错！", Toast.LENGTH_SHORT).show();
						onBackPressed();
					}
			});
		}else {
			ShowPickDialog();
		}
	}
	
	private void submit() {
		if (application.isClientStart()) {
			Client client = application.getClient();
			ClientOutputThread out = client.getClientOutputThread();
			TranObject o = new TranObject(TranObjectType.UPDATE_HEAD);
			user.setHeadUrl(fileName);
			o.setJson(gson.toJson(user));
			o.setFromUser(user.getUserId());
			out.setMsg(o);
		} else {
			Toast.makeText(PicCutAndUploadActivity.this, "后台服务器连接失败！", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}

	}
	
	@Override
	public void getMessage(TranObject msg) {
		if(msg.getType() == TranObjectType.UPDATE_HEAD ){
			MyDialogTools.closeDialog();
			Intent intent = new Intent();
			if(msg.isSuccess()){
				user.setHeadUrl(fileName);
				intent.putExtra("fileName", user.getHeadUrl());
			} else {
				intent.putExtra("fileName", "");
				Toast.makeText(PicCutAndUploadActivity.this, "上传出错！", Toast.LENGTH_SHORT).show();
			}
			setResult(ActivityTag.PIC_CUT_AND_UPLOAD_HEAD, intent);
			finish();
		}
	}
	
	
	/**********************/
	
	@Override
	public void onBackPressed() {
		MyDialogTools.closeDialog();
		Intent intent = new Intent();
		intent.putExtra("fileName", "");
		setResult(ActivityTag.PIC_CUT_AND_UPLOAD_HEAD, intent);
		finish();
	}
	
	

	
	void debug(String s){
		Log.v(TAG, s);
	}


	
}
