package me.kkuai.kuailian.http;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.utils.FileUtils;
import me.kkuai.kuailian.utils.MD5;
import me.kkuai.kuailian.utils.ShowToastUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

public class KHttpDownload {

	private Log log = LogFactory.getLog(KHttpDownload.class);
	private FinalHttp finalHttp;
	private static final KHttpDownload instance = new KHttpDownload();
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	
	private KHttpDownload() {
		finalHttp = new FinalHttp();
	}
	
	public static KHttpDownload getInstance() {
		return instance;
	}
	
	public String addDownloadUrl(String url, AjaxCallBack<File> ajaxCallBack) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
//		url = url.replace("kuaikuai_online/", "");
		String fileName = MD5.getMD5Code(url);
		String filePath = Environment.getExternalStorageDirectory()
				+ "/kuaikuaiTemp/" + fileName;
		if (FileUtils.fileIsExists(Environment.getExternalStorageDirectory() + "/kuaikuaiTemp/", fileName)) {
			ajaxCallBack.onSuccess(new File(filePath));
			return filePath;
		}
		finalHttp.download(url, filePath, ajaxCallBack);
		return filePath;
	}
	
}
