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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controlswitch.R;

/**
 * Created by TwilightKHQ on 19-4-18.
 */

public class AddItemDialog extends Dialog {

    private Context context;
    private EditText addNme;
    private Spinner spinner;
    private TextView spinnerText;
    private EditText addLocation;
    private EditText addIntroduction;
    private Button cancelButton;
    private Button confirmedButton;

    public AddItemDialog(Context context) {
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

        View view  = LayoutInflater.from(context).inflate(R.layout.additem_dialog,null);
        addNme = view.findViewById(R.id.add_name);
        spinner = view.findViewById(R.id.spinner);
        spinnerText = view.findViewById(R.id.spinner_text);
        addLocation = view.findViewById(R.id.add_location);
        addIntroduction = view.findViewById(R.id.add_introduction);
        cancelButton = view.findViewById(R.id.button_cancelled);
        confirmedButton = view.findViewById(R.id.button_confirmed);
        setContentView(view);
    }

    public EditText getAddNme() {
        return addNme;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public TextView getSpinnerText() {
        return spinnerText;
    }

    public EditText getAddLocation() {
        return addLocation;
    }

    public EditText getAddIntroduction() {
        return addIntroduction;
    }

    public Button getCancelButton(){
        return cancelButton;
    }

    public Button getConfirmedButton(){
        return confirmedButton;
    }

}
