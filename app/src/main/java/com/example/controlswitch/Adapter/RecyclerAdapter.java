package com.example.controlswitch.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlswitch.ConnectActivity;
import com.example.controlswitch.Model.DataBean;
import com.example.controlswitch.Utils.ChangeItemDialog;
import com.example.controlswitch.Utils.ConfirmDialog;
import com.example.controlswitch.R;
import com.example.controlswitch.Utils.SwitchDBHelper;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<DataBean> dataBeanList;
    private int opened = -1;

    private PrintStream out;    //  打印输出流
    private Socket mSocket;     //  套接字
    private ConnectThread mConnectThread;   //  TCP连接线程

    public RecyclerAdapter(Context context, List<DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.switch_item, parent, false);
        mConnectThread = new ConnectThread("192.168.4.1", 8080);
        mConnectThread.start();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindView(position,dataBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    /**
     * 设置列表数据
     * @param dataBeanList 数据
     */
    public void setLists(List<DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;

        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView switchName;
        private TextView switchPort;
        private TextView switchLocation;
        private TextView parentIntroduction;
        private TextView childIntroduction;
        private LinearLayout linearLayout;
        private LinearLayout expend_layout;

        private SwitchButton switchButton;
        private ImageView imageView;

        private Button itemDelete;
        private Button itemChange;

        public ViewHolder(View view) {
            super(view);
            switchName = (TextView) view.findViewById(R.id.switch_name);
            switchPort = (TextView) view.findViewById(R.id.switch_port);
            switchLocation = (TextView) view.findViewById(R.id.switch_location);
            parentIntroduction = (TextView) view.findViewById(R.id.parent_introduction);
            childIntroduction = (TextView) view.findViewById(R.id.child_introduction);

            switchButton = (SwitchButton) view.findViewById(R.id.button_turn);
            imageView = (ImageView) view.findViewById(R.id.button_image);

            itemDelete = (Button) view.findViewById(R.id.item_delete);
            itemChange = (Button) view.findViewById(R.id.item_change);

            expend_layout = (LinearLayout) view.findViewById(R.id.expend_layout);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout_all);
            linearLayout.setOnClickListener(this);
        }

        /**
         * 此方法实现列表数据的绑定和item的展开/关闭
         */
        void bindView(final int position, final DataBean dataBean) {

            switchName.setText(dataBean.getSwitchName());
            switchPort.setText(dataBean.getPort());
            switchLocation.setText(dataBean.getSwitchLocation());
            parentIntroduction.setText(dataBean.getParentIntroduction());
            childIntroduction.setText(dataBean.getChildIntroduction());

            switchButton.setCheckedImmediately(dataBean.getIsOn());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String outString = "1";
                    String isOpen = "开启";
                    if (dataBean.getIsOn()){
                        dataBean.setIsOn(false);
                        outString = "0";
                        isOpen = "关闭";
                    }
                    final ConfirmDialog confirmDialog = new ConfirmDialog(context);
                    confirmDialog.getDialogText().setText("确认"+ isOpen + "该开关嘛？");
                    confirmDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (confirmDialog.isShowing()){
                                confirmDialog.dismiss();
                            }
                        }
                    });
                    String finalOutString = outString;
                    confirmDialog.getConfirmedButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchButton.toggle();
                            dataBean.setIsOn(true);
                            try {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        out.print(finalOutString);
                                        out.flush();
                                    }
                                }).start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (confirmDialog.isShowing()){
                                confirmDialog.dismiss();
                            }
                        }
                    });
                    confirmDialog.show();
                }
            });

            itemChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ChangeItemDialog changeItemDialog = new ChangeItemDialog(context);
                    changeItemDialog.getChangeName().setText(switchName.getText());
                    changeItemDialog.getChangeLocation().setText(switchLocation.getText());
                    changeItemDialog.getChangeIntroduction().setText(childIntroduction.getText());
                    changeItemDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (changeItemDialog.isShowing()) {
                                changeItemDialog.dismiss();
                            }
                        }
                    });
                    changeItemDialog.getConfirmedButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (changeItemDialog.getChangeName().length() == 0) {
                                Toast.makeText(context, "请输入开关名称！", Toast.LENGTH_SHORT).show();
                            } else {
                                //修改更新数据库
                                SwitchDBHelper switchDbHelper = new SwitchDBHelper(context,"SwitchItem.db",null,1);
                                SQLiteDatabase database = switchDbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("name",changeItemDialog.getChangeName().getText().toString());
                                values.put("location",changeItemDialog.getChangeLocation().getText().toString());
                                values.put("introduction",changeItemDialog.getChangeIntroduction().getText().toString());
                                database.update("switch",values,"port=?",new String[]{dataBeanList.get(position).getPort()});

                                //修改更新RecyclerView当中的内容
                                switchName.setText(changeItemDialog.getChangeName().getText());
                                switchLocation.setText(changeItemDialog.getChangeLocation().getText());
                                childIntroduction.setText(changeItemDialog.getChangeIntroduction().getText());
                                parentIntroduction.setText(changeItemDialog.getChangeIntroduction().getText());
                                if (changeItemDialog.isShowing()) {
                                    changeItemDialog.dismiss();
                                }
                            }
                        }
                    });
                    changeItemDialog.show();
                }
            });

            itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ConfirmDialog confirmDialog = new ConfirmDialog(context);
                    confirmDialog.getDialogText().setText("确定要删除该开关嘛？");
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
                            //删除数据
                            SwitchDBHelper switchDbHelper =new SwitchDBHelper(context,"SwitchItem.db",null,1);
                            SQLiteDatabase database = switchDbHelper.getWritableDatabase();
                            database.delete("switch","port=?",new String[]{dataBeanList.get(position).getPort()});
                            dataBeanList.remove(position);
                            notifyDataSetChanged();
                            if (confirmDialog.isShowing()){
                                confirmDialog.dismiss();
                            }
                        }
                    });
                    confirmDialog.show();
                }
            });

            if (position == opened){
                expend_layout.setVisibility(View.VISIBLE);
            } else{
                expend_layout.setVisibility(View.GONE);
            }
        }


        /**
         * item的点击事件
         * @param view
         */
        @Override
        public void onClick(View view) {
            if (opened == getAdapterPosition()) {
                //当点击的item已经被展开了, 就关闭.
                opened = -1;
                notifyItemChanged(getAdapterPosition());
            } else {
                int oldOpened = opened;
                opened = getAdapterPosition();
                notifyItemChanged(oldOpened);
                notifyItemChanged(opened);
            }
        }
    }

    // 暴露接口，更新数据源
    public void updateList(List<DataBean> newDatas) {
        if (newDatas != null) {
            dataBeanList.addAll(newDatas);
        }
        notifyDataSetChanged();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
