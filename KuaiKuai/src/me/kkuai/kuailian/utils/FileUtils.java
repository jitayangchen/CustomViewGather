package me.kkuai.kuailian.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;

public class FileUtils {
	
	private static Log log = LogFactory.getLog(FileUtils.class);
	public final static String PHOTO_NAME = "tempImage.jpg";
	
	public static String getTempFilePath() {
		String filePath = Environment.getExternalStorageDirectory() + "/kuaikuaiTemp";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}
	
	public static void copyBitmapToTempFile(InputStream is, CopyBitmapFinishListener copyBitmapFinishListener) {
		copyBitmapToTempFile(is, PHOTO_NAME, copyBitmapFinishListener);
	}
	
	public static void copyBitmapToTempFile(InputStream is, String photoName, CopyBitmapFinishListener copyBitmapFinishListener) {
		new CopyBitmapToTempFile(copyBitmapFinishListener).execute(is, photoName);
	}
	
	public static boolean isKuaikuaiTempFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		} 
		return file.exists();
	}
	
	private static class CopyBitmapToTempFile extends AsyncTask<Object, Integer, String> {
		
		private CopyBitmapFinishListener copyBitmapFinishListener;

		public CopyBitmapToTempFile(CopyBitmapFinishListener copyBitmapFinishListener) {
			this.copyBitmapFinishListener = copyBitmapFinishListener;
		}
		
		@Override
		protected String doInBackground(Object... params) {
			log.info(" --- doInBackground ---");
			InputStream is = (InputStream) params[0];
			String photoName = (String) params[1];
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap decodeStream = BitmapFactory.decodeStream(is, new Rect(), options);
			File file = new File(getTempFilePath(), photoName);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				decodeStream.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bitmap decodeFile = BitmapUtils.getImage(file.getAbsolutePath());
			try {
				FileOutputStream fos = new FileOutputStream(file);
				decodeFile.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file.getAbsolutePath();
			
			
			
			
//			File picture = new File(getTempFilePath(), photoName);
//			FileOutputStream out = null;
//			try {
//				out = new FileOutputStream(picture);
//				byte[] buffer = new byte[1024];
//				while (is.read(buffer) != -1) {
//					out.write(buffer);
//				}
//			} catch (FileNotFoundException e) {
//				log.error("FileNotFoundException", e);
//			} catch (IOException e) {
//				log.error("IOException", e);
//			} finally {
//				try {
//					is.close();
//				} catch (IOException e) {
//					log.error("is.close()", e);
//				}
//				try {
//					out.close();
//				} catch (IOException e) {
//					log.error("out.close()", e);
//				}
//			}
//			log.info("picture.getAbsolutePath() === " + picture.getAbsolutePath());
//			return picture.getAbsolutePath();
		}
		
		@Override
		protected void onPostExecute(String result) {
			log.info(" --- onPostExecute ---");
			copyBitmapFinishListener.copyBitmapFinish(result);
		}
		
	}
	
	public interface CopyBitmapFinishListener {
		void copyBitmapFinish(String filePath);
	}
	
	public static boolean fileIsExists(String filePath, String fileNmae) {
		boolean isExists = false;
		File folder = new File(filePath);
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				if (file.isFile() && file.getName().equals(fileNmae)) {
					isExists = true;
				}
			}
		} else {
			folder.mkdirs();
		}
		return isExists;
	}
}
