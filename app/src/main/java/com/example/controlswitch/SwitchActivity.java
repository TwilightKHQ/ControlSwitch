package com.example.controlswitch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.controlswitch.Adapter.RecyclerAdapter;
import com.example.controlswitch.Model.DataBean;
import com.example.controlswitch.Utils.AddItemDialog;
import com.example.controlswitch.Utils.BaseActivity;
import com.example.controlswitch.Utils.SwitchDBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SwitchActivity extends BaseActivity {

    private String TAG = "Test";

    private RecyclerView mRecyclerView;
    private List<DataBean> dataBeanList; //RecyclerView的Item数据
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;

    private FloatingActionButton floatingActionButton;

    private AddItemDialog addItemDialog;

    private Spinner spinner;

    //spinner的下拉列表数据
    private List<String > dataList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
    private List<String > numberList = new ArrayList<String>(); //number列表
    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        initData();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this, dataBeanList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getItemAnimator().setChangeDuration(300);
        mRecyclerView.getItemAnimator().setMoveDuration(300);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog = new AddItemDialog(SwitchActivity.this);

                SwitchDBHelper switchDbHelper = new SwitchDBHelper(SwitchActivity.this,"SwitchItem.db",null,1);
                SQLiteDatabase database = switchDbHelper.getWritableDatabase();
                Cursor cursor = database.query("switch", null , null, null, null, null, null);
                if (cursor.getCount() != 0) {
                    Log.d(TAG, "onClick: " + cursor.getCount());
                    while (cursor.moveToNext()) {
                        Log.d(TAG, "port: " + cursor.getString(cursor.getColumnIndex("port")));
                        numberList.add(cursor.getString(cursor.getColumnIndex("port")));
                        Log.d(TAG, "numberList: " + numberList);
                    }
                }
                cursor.close();

                spinner = addItemDialog.getSpinner();
                //去两个List的差集
                List<String> portList = dataList.stream().filter(item -> !numberList.contains(item)).collect(toList());
                Log.d(TAG, "portlist: " + portList);
                adapter = new ArrayAdapter<String>(SwitchActivity.this, android.R.layout.simple_spinner_item, portList);

                //为适配器设置下拉列表下拉时的菜单样式。
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            addItemDialog.getSpinnerText().setText(adapter.getItem(position));
                            Log.d(TAG, "onItemSelected: " + adapter.getItem(position));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                addItemDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addItemDialog.isShowing()) {
                            addItemDialog.dismiss();
                        }
                    }
                });
                addItemDialog.getConfirmedButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addItemDialog.getAddNme().length() == 0) {
                            Toast.makeText(SwitchActivity.this, "请输入开关名称！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (addItemDialog.isShowing()) {
                                addItemDialog.dismiss();
                            }
                            addItemToDB();
                            queryAll();
                        }
                    }
                });
                addItemDialog.show();
            }
        });
    }

    //添加数据到数据库当中
    private void addItemToDB() {
        String name = addItemDialog.getAddNme().getText().toString();
        String port = addItemDialog.getSpinnerText().getText().toString();
        String location = addItemDialog.getAddLocation().getText().toString();
        String introduction = addItemDialog.getAddIntroduction().getText().toString();
        SwitchDBHelper switchDbHelper = new SwitchDBHelper(this,"SwitchItem.db",null,1);
        SQLiteDatabase sqLiteDatabase = switchDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("port", port);
        values.put("location", location);
        values.put("introduction", introduction);
        values.put("isChecked", 0);
        sqLiteDatabase.insert("switch", null, values);
    }
    //查询所有数据
    private void queryAll() {
        try {
            SwitchDBHelper switchDbHelper = new SwitchDBHelper(this,"SwitchItem.db",null,1);
            SQLiteDatabase sqLiteDatabase = switchDbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query("switch",null,null,null,null,null,null);
            Log.d(TAG, "queryAll: "+ cursor.getCount());
            if (cursor.getCount() != 0) {
                dataBeanList.clear();
                cursor.moveToFirst();
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String port = cursor.getString(cursor.getColumnIndex("port"));
                    String location = cursor.getString(cursor.getColumnIndex("location"));
                    String introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                    int isChecked = cursor.getInt(cursor.getColumnIndex("isChecked"));
                    dataBean = new DataBean();
                    dataBean.setSwitchName(name);
                    dataBean.setPort(port);
                    dataBean.setSwitchLocation(location);
                    dataBean.setChildIntroduction(introduction);
                    dataBean.setParentIntroduction(introduction);
                    dataBean.setIsOn(isChecked != 0);
                    dataBeanList.add(dataBean); //取出所有开关信息，填充到列表
                } while (cursor.moveToNext());
                cursor.close();
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟数据
     */
    private void initData(){
        dataBeanList = new ArrayList<>();
        dataBean = new DataBean();
        dataBean.setPort("0");
        dataBean.setSwitchName("暂无开关");
        dataBean.setSwitchLocation("远在天边，近在眼前");
        dataBean.setChildIntroduction("子内容--0");
        dataBean.setParentIntroduction("请点击“+”添加开关");
        dataBeanList.add(dataBean);

        queryAll();

    }

}