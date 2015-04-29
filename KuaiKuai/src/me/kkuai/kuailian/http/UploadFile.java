package me.kkuai.kuailian.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import me.kkuai.kuailian.constant.AppConstantValues;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.FileUtils;
import me.kkuai.kuailian.utils.Preference;
import me.kkuai.kuailian.utils.Util;

public class UploadFile {
	
	private Log log = LogFactory.getLog(UploadFile.class);
	private Handler handler;
	
	public void executeUploadFile(String filepath, int fileType) {
		new UploadFileAsync().execute(filepath, fileType);
	}

	private void upload(String filepath, int fileType) throws Exception {
		
		String host = "www.xinxiannv.com";
		String requestFunction = "";
		String contentType = "";
		String uploadFileType = "";
		if (AppConstantValues.UPLOAD_FILE_TYPE_AVATAR == fileType) {
			requestFunction = "avatarupload";
			contentType = "image/png";
			uploadFileType = "0";
		} else if (AppConstantValues.UPLOAD_FILE_TYPE_PHOTO == fileType) {
			requestFunction = "uploadfile";
//			contentType = "application/octet-stream";
			contentType = "image/png";
			uploadFileType = "0";
		} else if (AppConstantValues.UPLOAD_FILE_TYPE_AUDIO == fileType) {
			requestFunction = "uploadfile";
			contentType = "application/octet-stream";
			uploadFileType = "1";
		}
		
		
		String boundary = "---------------------------boundary";			// 分割线
		File file = new File(filepath);									// 要上传的文件 
		URL url = new URL(KHttpRequest.getInstance().getBaseUrl() + requestFunction);										// 用来开启连接 
		StringBuilder sb = new StringBuilder();							// 用来拼装请求

		// token字段
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"token\"" + "\r\n");
		sb.append("\r\n");
		sb.append(Preference.getToken() + "\r\n");
		
		if (AppConstantValues.UPLOAD_FILE_TYPE_AVATAR != fileType) {
			sb.append("--" + boundary + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"fileType\"" + "\r\n");
			sb.append("\r\n");
			sb.append(uploadFileType + "\r\n");
		}

		// 文件部分
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + filepath + "\"" + "\r\n");
		sb.append("Content-Type: " + contentType + "\r\n");
		sb.append("\r\n");
		log.info("executeUploadFile === " + sb.toString());
		// 将开头和结尾部分转为字节数组
		byte[] before = sb.toString().getBytes("UTF-8");
		byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes("UTF-8");
		
		// 打开连接, 设置请求头
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");
		conn.setRequestProperty("HOST", host);	
		conn.setDoOutput(true);

		// 获取输入输出流
		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(file);

		// 将开头部分写出
		out.write(before);

		// 写出文件数据
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}

		// 将结尾部分写出
		out.write(after);
		
		in.close();
		out.close();
		if (200 == conn.getResponseCode()) {
			InputStream is = conn.getInputStream(); 
			byte[] b = Util.readDataFromIS(is);
			String result = new String(b);
			Message mess = handler.obtainMessage();
			if (AppConstantValues.UPLOAD_FILE_TYPE_AVATAR == fileType) {
				mess.what = AppConstantValues.OWNERPROFILE_UPLOAD_HEAD_FINISH;
			} else if (AppConstantValues.UPLOAD_FILE_TYPE_PHOTO == fileType) {
				mess.what = AppConstantValues.OWNERPROFILE_UPLOAD_USER_PHOTO;
			} else if (AppConstantValues.UPLOAD_FILE_TYPE_AUDIO == fileType) {
				mess.what = AppConstantValues.UPLOAD_AUDIO_SECCESS;
			}
			mess.obj = result;
			handler.sendMessage(mess);
			log.info(result);
		} else {
			log.error("upload image error");
		}
	}
	
	private class UploadFileAsync extends AsyncTask<Object, Integer, String> {

		@Override
		protected String doInBackground(Object... params) {
			String filepath = (String) params[0];
			int fileType = (Integer) params[1];
			try {
				upload(filepath, fileType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
