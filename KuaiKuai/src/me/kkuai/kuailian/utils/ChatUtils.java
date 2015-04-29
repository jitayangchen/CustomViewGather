package me.kkuai.kuailian.utils;

import java.io.File;

public class ChatUtils {
	
//	public static int getMaxChatItemId(List<ChatItem> currentItems) {
//		int maxId = 0;
//		for (ChatItem item : currentItems) {
//			if (item.getId() > maxId) {
//				maxId = item.getId();
//			}
//		}
//		return maxId;
//	}
	
	public static boolean isFileExists(String filename) {
		File file = new File(filename);
		
		return file.exists();
	}
	
//	public static String genAudioFileName(Context context) {
//		FileStore fs = ServicePool.getinstance(context.getApplicationContext()).getFileStore();
//		StringBuilder filename = new StringBuilder();
//		filename.append(fs.getFileStorePath());
//		filename.append(System.currentTimeMillis());
//		// filename.append(".amr");
//		return filename.toString();
//	}
	
}
