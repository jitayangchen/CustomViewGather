package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogNormal extends AlertDialog implements OnClickListener {

	private TextView tvContent;
	private Button btnCancel;
	private Button btnOk;
	private DialogOnClickOkListener dialogOnClickOkListener;
	private String content;

	public DialogNormal(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_normal);
		
		tvContent = (TextView) findViewById(R.id.tv_content);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnOk = (Button) findViewById(R.id.btn_ok);
		
		btnCancel.setOnClickListener(this);
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
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_ok:
			if (null != dialogOnClickOkListener) {
				dialogOnClickOkListener.onDialogOk();
			}
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public void setDialogOnClickOkListener(DialogOnClickOkListener dialogOnClickOkListener) {
		this.dialogOnClickOkListener = dialogOnClickOkListener;
	}

	public interface DialogOnClickOkListener {
		void onDialogOk();
	}

}
