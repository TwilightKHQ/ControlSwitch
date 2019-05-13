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
import android.widget.EditText;

import com.example.controlswitch.R;

/**
 * Created by TwilightKHQ on 19-4-18.
 */

public class ChangeItemDialog extends Dialog {

    private Context context;
    private EditText changeName;
    private EditText changeLocation;
    private EditText changeIntroduction;
    private Button cancelButton;
    private Button confirmedButton;

    public ChangeItemDialog(Context context) {
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

        View view  = LayoutInflater.from(context).inflate(R.layout.changeitem_dialog,null);
        changeName = view.findViewById(R.id.add_name);
        changeLocation = view.findViewById(R.id.add_location);
        changeIntroduction = view.findViewById(R.id.add_introduction);
        cancelButton = view.findViewById(R.id.button_cancelled);
        confirmedButton = view.findViewById(R.id.button_confirmed);
        setContentView(view);
    }

    public EditText getChangeName() {
        return changeName;
    }

    public EditText getChangeLocation() {
        return changeLocation;
    }

    public EditText getChangeIntroduction() {
        return changeIntroduction;
    }

    public Button getCancelButton(){
        return cancelButton;
    }

    public Button getConfirmedButton(){
        return confirmedButton;
    }

}
