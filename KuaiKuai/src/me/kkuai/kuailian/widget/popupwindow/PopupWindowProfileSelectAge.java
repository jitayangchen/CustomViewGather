package me.kkuai.kuailian.widget.popupwindow;

import java.util.ArrayList;
import java.util.List;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.adapter.WheelSingleAdapter;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import me.kkuai.kuailian.widget.wheelview.WheelView;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class PopupWindowProfileSelectAge {
	
	private Log log = LogFactory.getLog(PopupWindowProfileSelectAge.class);
	private Context context;
	private PopupWindow popupWindow;
	private View popLayout;
	private List<String> datas = new ArrayList<String>();
	private WheelSingleAdapter addressAdapter;
	
	public PopupWindowProfileSelectAge(Context context) {
		this.context = context;
		
		initPopupWindow();
	}
	
	private void initPopupWindow() {
		popLayout = View.inflate(context, R.layout.dialog_single_wheel, null);
		popupWindow = new PopupWindow(popLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		
		WheelView wheelSingle = (WheelView) popLayout.findViewById(R.id.wheel_single);
		addressAdapter = new WheelSingleAdapter();
		
		wheelSingle.setAdapter(addressAdapter);
//		wheelSingle.setCyclic(true);
		wheelSingle.setCurrentItem(0);
		wheelSingle.TEXT_SIZE = 35;
	}

	public void showPopupWindow() {
//		addressAdapter.setDatas(datas);
		popupWindow.showAtLocation(popLayout, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}
	
	
}
