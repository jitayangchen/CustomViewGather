package me.kkuai.kuailian.dialog;

import me.kkuai.kuailian.R;
import me.kkuai.kuailian.log.Log;
import me.kkuai.kuailian.log.LogFactory;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogEditBox extends Dialog implements OnClickListener {
	
	private Log log = LogFactory.getLog(DialogEditBox.class);
	private Context context;
	private Button btnCancel, btnOk;
	private DialogOkListener dialogOkListener;
	private EditText etEditContext;
	private TextView tvEditDialogTitle;

	public DialogEditBox(Context context) {
		super(context, R.style.editDialogStyle);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit_box);
		initViews();
		setListener();
	}

	private void initViews() {
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnOk = (Button) findViewById(R.id.btn_ok);
		
		tvEditDialogTitle = (TextView) findViewById(R.id.tv_edit_dialog_title);
		etEditContext = (EditText) findViewById(R.id.et_edit_context);
	}
	
	private void setListener() {
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_ok:
			if (null != dialogOkListener) {
				String editContext = etEditContext.getText().toString().trim();
				dialogOkListener.dialogOk(editContext);
			}
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public void setDialogOkListener(DialogOkListener dialogOkListener) {
		this.dialogOkListener = dialogOkListener;
	}

	public interface DialogOkListener {
		void dialogOk(String editContext);
	}

	public void setDialogTitle(String title) {
		tvEditDialogTitle.setText(title);
	}
	
	public void setContent(String content) {
		etEditContext.setText(content);
	}
	
	@Override
	public void show() {
		super.show();
		/*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开 
//		if (!isOpen) {
//			imm.showSoftInput(etEditContext, 0); 
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//		}
		etEditContext.requestFocus();
		etEditContext.setFocusable(true);
		imm.showSoftInput(etEditContext, 0);*/
		
//		etEditContext.setFocusable(true);
//		etEditContext.setFocusableInTouchMode(true);
//		etEditContext.requestFocus();
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); 
//		imm.showSoftInput(etEditContext, InputMethodManager.RESULT_SHOWN); 
////		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); 
//		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	public void dismiss() {
//		Util.hiddenSoftKeyborad(etEditContext, context);
		super.dismiss();
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
//		boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开 
//		if (isOpen) {
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//		}
	}

}
