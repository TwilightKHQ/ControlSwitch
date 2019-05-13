package com.example.controlswitch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlswitch.Utils.ChangeDialog;
import com.example.controlswitch.Utils.ConfirmDialog;
import com.kyleduo.switchbutton.SwitchButton;

public class SecurityActivity extends AppCompatActivity {

    private Button securityChange;
    private TextView securitySSID;
    private TextView securityPwd;

    private SwitchButton hideButton;
    private ImageView hideImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        initToolBar();
        initView();

        securityChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ChangeDialog changeDialog = new ChangeDialog(SecurityActivity.this);
                changeDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (changeDialog.isShowing()) {
                            changeDialog.cancel();
                        }
                    }
                });
                changeDialog.getConfirmedButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (changeDialog.getEditSSID().length() == 0) {
                            Toast.makeText(SecurityActivity.this, "请输入WIFI名称！", Toast.LENGTH_SHORT).show();
                        } else if (changeDialog.getEditPassword().length() < 8) {
                            Toast.makeText(SecurityActivity.this, "WIFI密码过短", Toast.LENGTH_SHORT).show();
                        } else  {
                            securitySSID.setText(changeDialog.getEditSSID().getText());
                            securityPwd.setText(changeDialog.getEditPassword().getText());
                            if (changeDialog.isShowing()) {
                                changeDialog.dismiss();
                            }
                        }

                    }
                });
                changeDialog.show();
            }
        });

        hideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConfirmDialog confirmDialog = new ConfirmDialog(SecurityActivity.this);
                confirmDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (confirmDialog.isShowing()){
                            confirmDialog.dismiss();
                        }
                    }
                });
                confirmDialog.getConfirmedButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideButton.toggle();
                        if (confirmDialog.isShowing()){
                            confirmDialog.dismiss();
                        }
                    }
                });
                if (!hideButton.isChecked()){
                    confirmDialog.getDialogText().setText("是否隐藏ESP8266所发射WIFI的SSID？");
                    confirmDialog.show();
                } else {
                    confirmDialog.getDialogText().setText("是否显示ESP8266所发射WIFI的SSID？");
                    confirmDialog.show();
                }
            }
        });
    }

    private void initView() {
        securityChange = (Button) findViewById(R.id.security_change);
        securitySSID = (TextView) findViewById(R.id.security_ssid);
        securityPwd = (TextView) findViewById(R.id.security_password);

        hideButton = (SwitchButton) findViewById(R.id.hide_turn);
        hideImage = (ImageView) findViewById(R.id.hide_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish(); // back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.security_toolbar);

        //导入Toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
