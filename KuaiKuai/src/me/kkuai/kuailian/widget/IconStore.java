package me.kkuai.kuailian.widget;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.utils.SoftReferenceMap;
import me.kkuai.kuailian.utils.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

public class IconStore {

	protected ArrayList<String> faceStrs;
	protected ArrayList<Integer> faceResIds;
	protected Map<String, Integer> faceStr2ResId;
	protected SoftReferenceMap<String, Bitmap> faceStr2Img;
	protected Context ctx;
	private static IconStore instance = null;
	
	private IconStore(){}
	
	public static IconStore getInstance(Context context) {
		if (instance == null) {
			try {
				instance = new IconStore();
				instance.init(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public void init(Context context) throws Exception {
		ctx = context;
		InputStream is = null;
		faceStrs = new ArrayList<String>();
		faceResIds = new ArrayList<Integer>();
		faceStr2ResId = new HashMap<String, Integer>();
		faceStr2Img = new SoftReferenceMap<String, Bitmap>();
		try {
			is = ctx.getAssets().open("face/face.xml");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NodeList list = node.getChildNodes();
				if (list != null) {
					sb.setLength(0);
					for (int j = 0; j < list.getLength(); j++)
						sb.append(list.item(j).getNodeValue());
				}
				faceStrs.add(sb.toString());
			}
		} finally {
			Util.close(is);
		}

		for (int i = 0; i < faceStrs.size(); ++i) {
			if(i<9){
//				int id = context.getResources().getIdentifier("f00" + i,
//					"drawable", "com.jiayuan");
				Field field = R.drawable.class.getDeclaredField("f00" + (i+1));
				int resourceId = Integer.parseInt(field.get(null).toString());
				faceStr2ResId.put(faceStrs.get(i), resourceId);
				faceResIds.add(resourceId);
			}else if(i<100){
//				int id = context.getResources().getIdentifier("f0" + i,
//						"drawable", "com.jiayuan");
				Field field = R.drawable.class.getDeclaredField("f0" + (i+1));
				int resourceId = Integer.parseInt(field.get(null).toString());
				faceStr2ResId.put(faceStrs.get(i), resourceId);
				faceResIds.add(resourceId);
			}
		}
	}

	/**
	 * get Bitmap by resource ID
	 */
	protected Bitmap getImgByResId(int id) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDensity = 300;
		return BitmapFactory.decodeResource(ctx.getResources(), id, options);
	}

	/**
	 * get all face images in sequence
	 * 
	 * @return
	 */
	public List<Bitmap> getIcons() {
		List<Bitmap> lst = new ArrayList<Bitmap>();
		for (String key : faceStrs)
			lst.add(getIcon(key));
		return lst;
	}

	/**
	 * get face namings
	 * 
	 * @return
	 */
	public List<String> getIconNames() {
		return faceStrs;
	}

	/**
	 * get face image with name
	 * 
	 * @param name
	 * @return
	 */
	public Bitmap getIcon(String name) {
		Bitmap img = faceStr2Img.get(name);
		if (img == null) {
			img = getImgByResId(faceStr2ResId.get(name));
			faceStr2Img.put(name, img);
		}

		return img;
	}

	public List<Integer> getFaceResIDs(){
		return faceResIds;
	}
	/**
	 * find face string in TextView content and replace then with face icon
	 */
	public SpannableString textToFace(String content) {
		SpannableString spannableString = new SpannableString(content);
		for (int i = 0; i < faceStrs.size(); i++) {
			String search = faceStrs.get(i);
			int length = search.length();
			int fromPos = 0;
			int pos = 0;
			while ((pos = content.indexOf(search, fromPos)) != -1) {
				fromPos = pos + length;
				Bitmap bitmap = getIcon(search);
				ImageSpan imageSpan = new ImageSpan(ctx,bitmap);
				spannableString.setSpan(imageSpan, pos, fromPos,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}
	
}
