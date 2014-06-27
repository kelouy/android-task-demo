package com.task.activity;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.BodyParamsEntity;
import com.task.common.utils.Constants;
import com.task.common.utils.FileUtils;
import com.task.common.utils.MyDate;
import com.task.tools.view.TasksCompletedView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class PicCutAndUploadActivity extends RoboActivity {
	private static String TAG = "PicCutAndUploadActivity";
	@InjectView(R.id.cut_img) ImageView cutImg;
	@InjectView(R.id.cut_tasks_view) TasksCompletedView progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cut_pic_upload);
		ShowPickDialog();
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
				startActivityForResult(intent, 1);

			}
		}).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.png")));
				startActivityForResult(intent, 2);
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				PicCutAndUploadActivity.this.finish();
			}
		}).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				startPhotoZoom(data.getData());
				break;
			case 2:
				File temp = new File(Environment.getExternalStorageDirectory()+ "/temp.png");
				startPhotoZoom(Uri.fromFile(temp));
				break;
			case 3:
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
		startActivityForResult(intent, 3);
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
			String fileName = url+"temp.png";
			FileUtils.addBitmapToSD(url, "temp.png", photo);
			
			//上传图片到服务器
			RequestParams params = new RequestParams();
			//params.addBodyParameter("fileName", MyDate.getDateYYYYMMDDHHMMSS()+".png");
			params.addBodyParameter("file", new File(url));
			//params.setBodyEntity(new BodyParamsEntity("multipart/form-data"));
			HttpUtils http = new HttpUtils(); 
			http.send(HttpMethod.POST,
				Constants.IMG_SERVER_URL+"?name="+fileName,
			    params,
			    new RequestCallBack<String>() {

			        @Override
			        public void onStart() {
			        	progressBar.setVisibility(View.VISIBLE);
			        }

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {
			           if (isUploading) {
			        	   progressBar.setProgress((int) (current/total * 100));
			            } 
			        }

			        @Override
			        public void onSuccess(ResponseInfo<String> responseInfo) {
			        	progressBar.setVisibility(View.GONE);
			        	Toast.makeText(PicCutAndUploadActivity.this, "图片上传成功！", Toast.LENGTH_SHORT).show();
			        }

					@Override
					public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PicCutAndUploadActivity.this, "图片上传出错！", Toast.LENGTH_SHORT).show();
					}
			});
		}else {
			ShowPickDialog();
		}
	}
	
	
	/**********************/
	void debug(String s){
		Log.v(TAG, s);
	}
	
}
