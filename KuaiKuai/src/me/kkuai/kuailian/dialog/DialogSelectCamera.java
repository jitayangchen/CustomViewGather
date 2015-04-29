package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogSelectCamera extends AlertDialog implements OnClickListener {

	private TextView tvAlbum, tvCamera, tvCancel;
	private OnSelectPhotoListener onSelectPhotoListener;

	public DialogSelectCamera(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_camera);
		
		tvAlbum = (TextView) findViewById(R.id.tv_album);
		tvCamera = (TextView) findViewById(R.id.tv_camera);
		tvCancel = (TextView) findViewById(R.id.tv_cancel);
		
		tvAlbum.setOnClickListener(this);
		tvCamera.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_album:
			if (null != onSelectPhotoListener) {
				onSelectPhotoListener.selectPhotoAlbum();
			}
			dismiss();
			break;
		case R.id.tv_camera:
			if (null != onSelectPhotoListener) {
				onSelectPhotoListener.selectPhotoCamera();
			}
			dismiss();
			break;
		case R.id.tv_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public interface OnSelectPhotoListener {
		void selectPhotoAlbum();
		void selectPhotoCamera();
	}

	public void setOnSelectPhotoListener(OnSelectPhotoListener onSelectPhotoListener) {
		this.onSelectPhotoListener = onSelectPhotoListener;
	}

}
