package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogSingleConfirm extends AlertDialog implements OnClickListener {

	private TextView tvContent;
	private String content;
	private Button btnOk;
	private DialogOkClickListener clickListener;

	public DialogSingleConfirm(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_single_confirm);
		
		tvContent = (TextView) findViewById(R.id.tv_content);
		btnOk = (Button) findViewById(R.id.btn_ok);
		
		btnOk.setOnClickListener(this);
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public void show() {
		super.show();
		tvContent.setText(content);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (clickListener != null) {
				clickListener.onOk();
			}
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public interface DialogOkClickListener {
		void onOk();
	}

	public void setClickListener(DialogOkClickListener clickListener) {
		this.clickListener = clickListener;
	}

}
