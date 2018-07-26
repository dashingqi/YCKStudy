package com.example.dashingqi.yckstudy;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_show_dialog = findViewById(R.id.btn_show_dialog);

        btn_show_dialog.setOnClickListener(new View.OnClickListener() {

            private CommonDialog commonDialog;

            @Override
            public void onClick(View v) {
                if (commonDialog == null) {
                    commonDialog = new CommonDialog(MainActivity.this, "弹出对话框", R.style.dialog, new CommonDialog.CancelListener() {
                        @Override
                        public void onClicks(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                //Toast.makeText(MainActivity.this,"谈了",Toast.LENGTH_LONG).show();
                                ToastUtils.show(MainActivity.this,"谈了");
                            }else{
                                dialog.dismiss();
                            }
                        }
                    });
                    commonDialog.setTitle("二网确认关联");
                    commonDialog.setPositiveButton("确认");
                    commonDialog.setNegativeButton("取消");
                }
                if (!commonDialog.isShowing()){
                    commonDialog.show();
                }
            }
        });
    }
}
