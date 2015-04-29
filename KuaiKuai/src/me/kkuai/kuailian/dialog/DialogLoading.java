package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class DialogLoading extends AlertDialog {

	private TextView tvDialogContent;
	private String content;

	public DialogLoading(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		
		tvDialogContent = (TextView) findViewById(R.id.tv_dialog_content);
	}

	public void setContent(String content) {
		this.content = content;
	}

}
