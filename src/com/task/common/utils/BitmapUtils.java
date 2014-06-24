package com.task.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class BitmapUtils {

	/**
	 * 图片圆角处理，pixels越大，圆角半径越大
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		/**
		 * ALPHA_8 每个像素只要1字节~可惜只能代表透明度,没有颜色属性 ARGB_4444
		 * 每个像素要2字节~带透明度的颜色~可惜官方不推荐使用了 ARGB_8888 每个像素要4字节~带透明度的颜色, 默认色样 RGB_565
		 * 每个像素要2字节~不带透明度的颜色 默认为ARGB_8888,如果想丧心病狂的继续减少图片所占大小~不需要透明度参数的话,
		 * 那就可以把色彩样式设为RGB_565
		 */
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 从file读取bitmap并压缩到需要的大小
	 * 
	 * @param url
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String url, int reqWidth, int reqHeight) {
		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
		BitmapFactory.decodeFile(url, options);
		// 保存图片原宽高值
		final int realH = options.outHeight;
		final int realW = options.outWidth;
		// 初始化压缩比例为1
		int inSampleSize = 1;
		// 计算压缩比例
		if (realH > reqHeight || realW > reqWidth) {
			if (realW > realH) {
				options.inSampleSize = Math.round(realH / reqHeight);
			} else {
				options.inSampleSize = Math.round(realW / reqWidth);
			}
		}
		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = inSampleSize;
		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeFile(url, options);
	}

	/**
	 * 从网络获取图片取压缩到需要的大小
	 * @param url
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromNet(String url, int reqWidth, int reqHeight){
		HttpURLConnection conn = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(ImageLoader.NET_CONN_TIMEOUT);
			conn.setReadTimeout(ImageLoader.NET_READ_TIMEOUT);
			conn.connect();
			final int responseCode = conn.getResponseCode();
			if (200 == responseCode) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				is.close();
				byte[] bytes = outStream.toByteArray();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inSampleSize = 1;
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				final int realH = options.outHeight;
				final int realW = options.outWidth;
				if (realH > reqHeight || realW > reqWidth) {
					if (realW > realH) {
						options.inSampleSize = Math.round(realH / reqWidth);
					} else {
						options.inSampleSize = Math.round(realW / reqHeight);
					}
				}
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}
}
