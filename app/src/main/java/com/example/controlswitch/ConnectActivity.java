package com.example.controlswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlswitch.Utils.WiFiConnector;
import com.example.controlswitch.Utils.WiFiDBHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectActivity extends AppCompatActivity {

    private String TAG = "Test";

    private Button btnConnect;
    private Button btnPort;

    private WifiManager wiFiManager;
    private WiFiConnector wac;

    private TextView textView1;
    private EditText editSSID;
    private EditText editPwd;

    private CheckBox isCheck;
    //数据库相关
    private WiFiDBHelper wiFiDBHelper;
    private SQLiteDatabase database;

    private Socket mSocket;     //  套接字
    private EditText mIp;       //  IP
    private EditText mPort;     //  端口号
    private String mStrIp;      //  字符串类型ip
    private int miPort;         //  字符类型端口
    private PrintStream out;    //  打印输出流
    private ConnectThread mConnectThread;   //  TCP连接线程


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        wiFiDBHelper =new WiFiDBHelper(ConnectActivity.this,"WiFiInfo.db",null,1);
        database = wiFiDBHelper.getWritableDatabase();

        initToolBar();

        initView();

        btnPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  连接
                if (mSocket == null || !mSocket.isConnected()) {
                    mStrIp = mIp.getText().toString();
                    miPort = Integer.valueOf(mPort.getText().toString());
                    Log.d(TAG, "ip: "+ mStrIp + "port:" + miPort);
                    mConnectThread = new ConnectThread(mStrIp, miPort);
                    mConnectThread.start();
                }
                if (mSocket != null && mSocket.isConnected()) {
                    try {
                        mSocket.close();
                        mSocket = null;   //  清空mSocket
                        btnPort.setText("连接");
                        Log.d(TAG, "ip: "+ mStrIp + "port:" + miPort);
                        Toast.makeText(ConnectActivity.this, "连接已关闭", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnConnect.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    wac.connect(editSSID.getText().toString(), editPwd.getText().toString(),
                            editPwd.getText().toString().equals("")? WiFiConnector.WifiCipherType.WIFICIPHER_NOPASS: WiFiConnector.WifiCipherType.WIFICIPHER_WPA);
                    Log.d(TAG, "onClick: " + editSSID.getText().toString());
                    Log.d(TAG, "onClick: " + editPwd.getText().toString());
                    String ssid = editSSID.getText().toString();
                    String password = editPwd.getText().toString();
                    int rememberPass = 1;
                    if (!isCheck.isChecked()) {
                        ssid = "Null";
                        password = "Null";
                        rememberPass = 0;
                    }
                    ContentValues values = new ContentValues();
                    values.put("Ssid", ssid);
                    values.put("Password", password);
                    values.put("isChecked", rememberPass);
                    database.update("wifi",values,"number=?",new String[]{"1"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        wac.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 操作界面
                super.handleMessage(msg);
            }
        };
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.connect_toolbar);

        //导入Toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        btnConnect = (Button) findViewById(R.id.button_connect);
        btnPort = (Button) findViewById(R.id.button_port);

//        textView1 = (TextView) findViewById(R.id.txtMessage);

        editSSID = (EditText) findViewById(R.id.text_ssid);
        editPwd = (EditText) findViewById(R.id.text_password);
        mIp = (EditText) findViewById(R.id.text_ip);
        mPort = (EditText) findViewById(R.id.text_port);

        wiFiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wac = new WiFiConnector(wiFiManager);

        isCheck = (CheckBox) findViewById(R.id.remember_pass);

        try {
            Cursor cursor = database.query("wifi", null , null, null, null, null, null);
            cursor.moveToFirst();
            editSSID.setText(cursor.getString(cursor.getColumnIndex("Ssid")));
            editPwd.setText(cursor.getString(cursor.getColumnIndex("Password")));
            isCheck.setChecked(cursor.getInt(cursor.getColumnIndex("isChecked")) != 0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ConnectThread extends Thread {
        private String ip;
        private int port;

        ConnectThread(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                mSocket = new Socket(ip, port);
                out = new PrintStream(mSocket.getOutputStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPort.setText("断开");
                        Toast.makeText(ConnectActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnectActivity.this, "连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

}
