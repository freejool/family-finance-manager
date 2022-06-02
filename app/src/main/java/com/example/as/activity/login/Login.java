package com.example.as.activity.login;

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

import androidx.fragment.app.FragmentActivity;

import com.example.as.R;
import com.example.as.activity.MainActivity;
import com.example.as.dao.CommonDAO;
import com.example.as.dao.LoginDAO;
import com.example.as.Entity.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Login extends FragmentActivity {
    EditText passwordExittext; // 密码编辑框
    EditText usernameEdittext; // 用户名编辑框

    Button buttonLogin, buttonClose, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);// 设置布局文件

        usernameEdittext = findViewById(R.id.login_user_id);// 获取密码文本框
        passwordExittext = findViewById(R.id.login_passwd);// 获取密码文本框

        buttonLogin = findViewById(R.id.button_login);
        buttonClose = findViewById(R.id.button_close);
        buttonRegister = findViewById(R.id.button_register);


        TextView.OnEditorActionListener onEnterClicked = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    buttonLogin.performClick();
                }
                return false;
            }
        };
        usernameEdittext.setOnEditorActionListener(onEnterClicked);
        passwordExittext.setOnEditorActionListener(onEnterClicked);


        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRegister dialogRegister = new DialogRegister();
                dialogRegister.show(Login.this.getSupportFragmentManager(), "register");

            }
        });

        buttonLogin.setOnClickListener(new OnClickListener() {// 为登录按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Login.this, MainActivity.class);// 创建Intent对象
                CommonDAO<UserInfo> dao = new CommonDAO<>();// 创建PwdDAO对象
                // 判断是否有密码及是否输入了密码
                long passwordCnt = 0;
                try {
                    passwordCnt = dao.count(new UserInfo());
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(),
                            Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
                    return;
                }

                if (passwordCnt == 0) {
                    if (passwordExittext.getText().toString().isEmpty()) {
                        startActivity(intent);// 启动主Activity
                    } else {
                        Toast.makeText(Login.this, "请不要输入任何密码登录系统！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 判断输入的密码是否与数据库中的密码一致
                    String username = usernameEdittext.getText().toString();
                    String passwd = passwordExittext.getText().toString();
                    if (username.equals("") || passwd.equals("")) {
                        Toast.makeText(Login.this, "用户或密码为空！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ResultSet rs;
                    try {
                        rs = dao.find(new UserInfo(), "*",
                                "where name='" + usernameEdittext.getText() + "' and password='" + passwordExittext.getText()+"'");
                    } catch (SQLException e) {
                        Toast.makeText(getApplicationContext(),
                                Arrays.toString(e.getStackTrace()), Toast.LENGTH_LONG).show();
                        return;
                    }
                    UserInfo user = new UserInfo();
                    try {
                        rs.next();
                    } catch (SQLException e) {
                        Toast.makeText(getApplicationContext(),Arrays.toString(e.getStackTrace()),Toast.LENGTH_LONG).show();
                    }
                    user.setByResultSet(rs);

                    if (user.ID.value != null) {
                        intent.putExtra("user_id", user.ID.value);
                        startActivity(intent);// 启动主Activity
                    } else {
                        // 弹出信息提示
                        Toast.makeText(Login.this, "用户或密码不正确！",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        buttonClose.setOnClickListener(new OnClickListener() {// 为取消按钮设置监听事件
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();// 退出当前程序
            }
        });
    }
}
