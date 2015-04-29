package me.kkuai.kuailian.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.activity.chat.EmojiItemClickListener;
import me.kkuai.kuailian.utils.Util;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class EmojiDialog extends Dialog implements OnItemClickListener
{
	private Context context;

	private GridView emojiGrid;

	private EmojiItemClickListener emojiItemListener;

	public EmojiDialog(Context context, List<Integer> resIds,
			EmojiItemClickListener Menu_Item)
	{
		super(context, R.style.dialog_fullscreen);
		setContentView(R.layout.dialog_layout_emoji);
		this.context = context;
		this.emojiItemListener = Menu_Item;
		setProperty();
		emojiGrid = (GridView) this.findViewById(R.id.emoji_grid);
		emojiGrid.setAdapter(getMenuAdapter(resIds));
		emojiGrid.setOnItemClickListener(this);

		this.setCanceledOnTouchOutside(true);

	}


	private void setProperty()
	{

		Window w = getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.height = (int) (Util.getScreenHeight(context)-context.getResources().getDimension(R.dimen.emoji_dialog_height));
		lp.alpha = 1.0f;
//		lp.width = (int) (Util.getScreenWidth(context) - context.getResources().getDimension(R.dimen.emoji_dialog_width));
		w.setAttributes(lp);

	}

	public SimpleAdapter getMenuAdapter(List<Integer> resIds)
	{
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resIds.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", resIds.get(i));
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems,
				R.layout.single_expression_cell, new String[] { "image" },
				new int[] { R.id.image_smile });		
		return simpleAdapter;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		emojiItemListener.emojiItemClickListener(position);
//		this.dismiss();
	}

}
