package com.example.as.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.as.R;
import com.example.as.dao.PwdDAO;
import com.example.as.Entity.UserInfo;

import java.sql.SQLException;

public class Login extends Activity {
    EditText edittextPasswd;// 创建EditText对象
    EditText edittextUsername;

    Button btnlogin, btnclose;// 创建两个Button对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);// 设置布局文件

        edittextUsername = (EditText) findViewById(R.id.login_user_id);// 获取密码文本框
        edittextPasswd = (EditText) findViewById(R.id.login_passwd);// 获取密码文本框

        btnlogin = (Button) findViewById(R.id.btnLogin);// 获取登录按钮
        btnclose = (Button) findViewById(R.id.btnClose);// 获取取消按钮


        TextView.OnEditorActionListener onEnterClicked = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnlogin.performClick();
                }
                return false;
            }
        };
        edittextUsername.setOnEditorActionListener(onEnterClicked);
        edittextPasswd.setOnEditorActionListener(onEnterClicked);


        btnlogin.setOnClickListener(new OnClickListener() {// 为登录按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Login.this, MainActivity.class);// 创建Intent对象
                PwdDAO pwdDAO = new PwdDAO(Login.this);// 创建PwdDAO对象
                // 判断是否有密码及是否输入了密码
                long passwordCnt = 0;
                passwordCnt = pwdDAO.getPasswordCount();

                //  passwordCnt = 0;
                if (passwordCnt == 0) {
                    if (edittextPasswd.getText().toString().isEmpty()) {
                        startActivity(intent);// 启动主Activity
                    } else {
                        Toast.makeText(Login.this, "请不要输入任何密码登录系统！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 判断输入的密码是否与数据库中的密码一致
                    UserInfo user = pwdDAO.findWithPassword(edittextPasswd.getText().toString());
                    if (user != null) {
                        intent.putExtra("user_id", user.getID());
                        startActivity(intent);// 启动主Activity
                    } else {
                        // 弹出信息提示
                        Toast.makeText(Login.this, "请输入正确的密码！",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                edittextPasswd.setText("");// 清空密码文本框
            }
        });

        btnclose.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();// 退出当前程序
            }
        });
    }
}
