package com.task.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class FileUtils {

	public static void addBitmapToSD(String url,String fileName, Bitmap bmp) {

		File dir = new File(url);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		FileOutputStream m_fileOutPutStream = null;
		try {
			m_fileOutPutStream = new FileOutputStream(url + "/" + fileName);
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
}
