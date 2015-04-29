package me.kkuai.kuailian.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 提示工具类
 */
public class ShowToastUtil {
	
	private static Context context = null;
	 
	private static Toast toast = null;

	/**
	 * 操作成功
	 * 
	 * @param context
	 * @param id
	 */
//	public static void showSuccessToast(Context context, String msg) {
//		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//		LayoutInflater viewInflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View toastView = viewInflater.inflate(R.layout.toast_ok, null);
//		TextView tv = (TextView) toastView.findViewById(R.id.successmsg);
//		tv.setText(msg);
//		toast.setView(toastView);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
//	}

	/**
	 * 错误提示
	 * 
	 * @param context
	 * @param id
	 */
//	public static void showWarningToast(Context context, String warningMsg) {
//		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
//		LayoutInflater viewInflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View toastView = viewInflater.inflate(R.layout.toast_problem, null);
//		TextView tv = (TextView) toastView.findViewById(R.id.warningmsg);
//		tv.setText(warningMsg);
//		toast.setView(toastView);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
//	}

	/**
	 * 带图片的 toast
	 * 
	 * @param context
	 * @param iconId
	 *            图片ID
	 * @param msg
	 *            消息信息
	 */
	public static void showToastWithIcon(Context context, int iconId, String msg) {
		
//		if(ShowToastUtil.context == context){
//			toast.cancel();
//			toast.setText(msg);
//			 
//		}else{
			ShowToastUtil.context = context;
			toast = Toast.makeText(context.getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout toastView = (LinearLayout) toast.getView();
			ImageView imageCodeProject = new ImageView(
					context.getApplicationContext());
			imageCodeProject.setImageResource(iconId);
			imageCodeProject.setPadding(10, 8, 10, 8);
			toastView.addView(imageCodeProject, 0);
//		}
		
		toast.show();
	}

	/**
	 * 完全自定义toast
	 * 
	 * @param context
	 * @param iconId
	 * @param title
	 * @param msg
	 */
//	public static void showCustomToast(Activity context, int iconId,
//			String title, String msg) {
//		LayoutInflater inflater = context.getLayoutInflater();
//		View layout = inflater.inflate(R.layout.toast_custom,
//				(ViewGroup) context.findViewById(R.id.llToast));
//		ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
//		image.setImageResource(R.drawable.ic_launcher);
//		TextView titleV = (TextView) layout.findViewById(R.id.tvTitleToast);
//		titleV.setText(title);
//		TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
//		text.setText(msg);
//		Toast toast = new Toast(context.getApplicationContext());
//		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 12, 40);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();
//	}

	public static void showToast(Context context, String msg) {
		if (null == msg)
			return;
		ShowToastUtil.context = context;
		toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//		toast.setText(msg);
//		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

//	public static void showToast(Context context, String msg, boolean top) {
//		// Toast toast = new Toast(context);
//		if (null == msg)
//			return;
//		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//		LayoutInflater viewInflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View toastView = viewInflater.inflate(R.layout.toast_top, null);
//		TextView tv = (TextView) toastView.findViewById(R.id.text_msg);
//		tv.setText(msg);
//		toast.setView(toastView);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.TOP, 0, 0);
//		toast.show();
//	}
}
