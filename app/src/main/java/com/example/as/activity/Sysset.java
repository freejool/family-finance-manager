package com.example.as.activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.as.R;

public class Sysset extends Activity {
	EditText txtpwd;// 创建EditText对象
	Button btnSet, btnsetCancel;// 创建两个Button对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sysset);// 设置布局文件

		txtpwd = findViewById(R.id.txtPwd);// 获取密码文本框
		btnSet = findViewById(R.id.btnSet);// 获取设置按钮
		btnsetCancel = findViewById(R.id.btnsetCancel);// 获取取消按钮

//		btnSet.setOnClickListener(new OnClickListener() {// 为设置按钮添加监听事件
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				PwdDAO pwdDAO = new PwdDAO(Sysset.this);// 创建PwdDAO对象
//				SchemaUserInfo schemaUserInfo = new SchemaUserInfo(txtpwd.getText().toString());// 根据输入的密码创建Tb_pwd对象
//				if (pwdDAO.getCount() == 0) {// 判断数据库中是否已经设置了密码
//					pwdDAO.add(schemaUserInfo);// 添加用户密码
//				} else {
//					pwdDAO.update(schemaUserInfo);// 修改用户密码
//				}
//				// 弹出信息提示
//				Toast.makeText(Sysset.this, "〖密码〗设置成功！", Toast.LENGTH_SHORT)
//						.show();
//			}
//		});

		btnsetCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txtpwd.setText("");// 清空密码文本框
				txtpwd.setHint("请输入密码");// 为密码文本框设置提示
			}
		});
	}
}
