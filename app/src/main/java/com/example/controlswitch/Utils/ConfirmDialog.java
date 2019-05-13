package com.example.controlswitch.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.controlswitch.R;

/**
 * Created by TwilightKHQ on 19-4-18.
 */

public class ConfirmDialog extends Dialog {

    private Context context;
    private TextView dialogText;
    private Button cancelButton;
    private Button confirmedButton;

    public ConfirmDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = display.getWidth()*4/5;// 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(layoutParams);
        setCanceledOnTouchOutside(false);//点击外部 Dialog不消失

        View view  = LayoutInflater.from(context).inflate(R.layout.confirm_dialog,null);
        cancelButton = view.findViewById(R.id.button_cancelled);
        confirmedButton = view.findViewById(R.id.button_confirmed);
        dialogText = view.findViewById(R.id.dialog_text);
        setContentView(view);
    }

    public Button getCancelButton(){
        return cancelButton;
    }

    public Button getConfirmedButton(){
        return confirmedButton;
    }

    public TextView getDialogText() {
        return dialogText;
    }

}
