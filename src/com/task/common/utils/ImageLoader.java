package com.task.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.task.common.utils.ImageLoaderII.ImageInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;

public class ImageLoader {

	private final static String TAG = "ImageLoader";// 调试
	private final static String mSavePath = "/mnt/sdcard/ImageLoader/cache/images"; // 图片sd存储路径
	private final static long IMAGE_MAX_SAVE_TIME = 1000 * 60 * 60 * 24;// 图片最长保存时间1天
	private final static int MSG_LOAD_IMAGE_COMPLETE = 101;// 图片加载完成
	private final static int CORE_POOL_SIZE = 2;// 线程最小维护队列个数
	private final static int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;// 线程最大维护队列个数
	private final static int KEEP_ALIVE_TIME = 60;// 线程池维护线程所允许的空闲时间(s)
	public final static int NET_CONN_TIMEOUT = 5 * 1000;// 设置网络超时
	public final static int NET_READ_TIMEOUT = 15 * 1000;
	
	private Context mContext = null;	// 上下文保存
	private LinkedBlockingQueue<Runnable> mThreadPoolQueue = null;// 无界限边界队列
	private ThreadPoolExecutor mThreadPool = null;// 线程池配置
	private LruCache<String, Bitmap> mImageCache = null;// 图片内存缓存

	public ImageLoader() {
		// 初始化线程池
		mThreadPoolQueue = new LinkedBlockingQueue<Runnable>();
		mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mThreadPoolQueue);
		int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;// 控制缓存大小为内存的1/8
		mImageCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
		debug("初始化线程池和图片缓存，控制缓存大小内存为：" + cacheSize);
	}

	public void loadImage(final View v, final String url, final ImageLoaderListener l) {
		if (null == mThreadPool) {
			debug("Thread pool err, please check");
			return;
		}
		
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_LOAD_IMAGE_COMPLETE && l != null) {
					ImageInfo info = (ImageInfo) msg.obj;
					l.onImageLoad(info.view, info.bmp, info.url);
				}
			}
		};

		// 将任务加入队列
		mThreadPool.execute(new Runnable() {
			// 取图片顺序为内存-sd卡-网络下载
			@Override
			public void run() {
				Bitmap bmp = null;
				if (null != (bmp = getBitmapFromCache(url))) {
					debug("find image in cache url = " + url);
				} else if (null != (bmp = getBitmapFromSD(url,v.getWidth(),v.getHeight()))) {
					debug("find image in sd url = " + url);
					addBitmapToCache(url, bmp);
				} else if (null != (bmp = BitmapUtils.decodeSampledBitmapFromNet(url,v.getWidth(),v.getHeight()))) {
					debug("find image in net url = " + url);
					addBitmapToSD(url, bmp);
					addBitmapToCache(url, bmp);
				} else {
					return;
				}
				ImageInfo info = new ImageInfo();
				info.bmp = bmp;
				info.url = url;
				info.view = v;
				// 发送图片加载完成消息
				Message msg = mHandler.obtainMessage(MSG_LOAD_IMAGE_COMPLETE, info);
				mHandler.sendMessage(msg);
			}

		});
	}

	/**
	 * 从缓存中取出图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromCache(String url) {
		return mImageCache.get(url);
	}

	/**
	 * 从sd卡中获取图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromSD(String url,int reqWidth,int reqHeight) {
		if (!checkSD()) {
			debug("sd card not ready.");
			return null;
		}
		String fileName = getImageName(url);
		File fp = new File(mSavePath + "/" + fileName);
		if (fp.exists()) {
			// 检查图片是否过期，如果过期则删除它
			if (checkImageDirty(fp.lastModified())) {
				debug("image dirty, ready to del...... file = " + fileName);
				fp.delete();
				return null;
			}
			Bitmap bmp = BitmapUtils.decodeSampledBitmapFromFile(mSavePath + "/" + fileName, reqWidth, reqHeight);
			return bmp;
		}

		return null;
	}
	
	
	/**
	 * 检测当前网络是否可用
	 * 
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}
	
	/**
	 * 添加图片到sd卡中
	 * 
	 * @param url
	 * @param bmp
	 */
	private void addBitmapToSD(String url, Bitmap bmp) {
		if (!checkSD()) {
			debug("sd not ready");
			return;
		}

		// 检测是否有空间
		if (!checkSpace(bmp)) {
			debug("no enough space");
			return;
		}

		File dir = new File(mSavePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 检测图片数量是否已经达到上限
		//checkMaxSaveImages();
		String fileName = getImageName(url);
		debug("file name = " + fileName);

		FileOutputStream m_fileOutPutStream = null;
		try {
			m_fileOutPutStream = new FileOutputStream(mSavePath + "/" + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测当前sd卡是否可以使用
	 * 
	 * @return
	 */
	private boolean checkSD() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * 检测是否有空间存储图片
	 * 
	 * @param bmp
	 * @return
	 */
	private boolean checkSpace(Bitmap bmp) {
		long size = getBitmapsize(bmp);
		long sdSize = getAvailableStore(Environment.getExternalStorageDirectory().getPath());

		debug("size = " + size + " sd size = " + sdSize);
		if (size < sdSize) {
			return true;
		}

		return false;
	}

	/**
	 * 返回图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public long getBitmapsize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();// 只支持3.1以上的版本
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 获取本机剩余空间
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getAvailableStore(String filePath) {
		StatFs statFs = new StatFs(filePath);
		long blocSize = statFs.getBlockSizeLong();
		long availaBlock = statFs.getAvailableBlocksLong();
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
	}

	/**
	 * 根据url返回文件名
	 * 
	 * @param url
	 * @return
	 */
	private String getImageName(String url) {
		int index = url.lastIndexOf('/');
		return url.substring(index + 1);
	}

	/**
	 * 检测图片是否过期
	 * 
	 * @param lastmodifytime
	 * @return
	 */
	private boolean checkImageDirty(long lastmodifytime) {
		long curtime = System.currentTimeMillis();
		return (curtime - lastmodifytime) > IMAGE_MAX_SAVE_TIME ? true : false;
	}

	/**
	 * 将图片存入缓存
	 * 
	 * @param url
	 * @param bmp
	 */
	private void addBitmapToCache(String url, Bitmap bmp) {
		mImageCache.put(url, bmp);
	}

	/**
	 * 图片载入回调
	 * 
	 * @author Administrator
	 */
	public interface ImageLoaderListener {
		/**
		 * 图片加载完成后回调
		 * 
		 * @param v
		 * @param bmp
		 * @param url
		 */
		public void onImageLoad(View v, Bitmap bmp, String url);
	}

	public void debug(String s) {
		Log.e(TAG, s);
	}
}
