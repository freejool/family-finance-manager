package com.example.as.dao;

import android.content.Context;
import android.util.Log;

import com.example.as.database.DatabaseService;
import com.example.as.Entity.UserInfo;

import java.sql.ResultSet;
import java.util.Arrays;

public class PwdDAO {
    private DatabaseService db;
    private ResultSet resultSet;

    public PwdDAO(Context context) {// 定义构造函数

    }


    //    /**
//     * 添加密码信息
//     *
//     * @param schemaUserInfo
//     */
//    public void add(SchemaUserInfo schemaUserInfo) {
//        // 执行添加密码操作
//        db.execSQL("insert into tb_pwd (password) values (?)",
//                new Object[]{schemaUserInfo.getPassword()});
//    }
//
//    /**
//     * 设置密码信息
//     *
//     * @param schemaUserInfo
//     */
//    public void update(SchemaUserInfo schemaUserInfo) {
////		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
//        // 执行修改密码操作
//        db.execSQL("update tb_pwd set password = ?",
//                new Object[]{schemaUserInfo.getPassword()});
//    }

    /**
     * 查找密码信息
     *
     * @return
     */
    public UserInfo findWithPassword(String password) {
        try {
            db=new DatabaseService("select * from user_info where password=" + password);
            db.run();
           ResultSet res=db.getResultSet();
            if (resultSet.next()) {// 遍历查找到的密码信息
                // 将密码存储到Tb_pwd类中
                return new UserInfo(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        }
        return null;// 如果没有信息，则返回null
    }

    public int getPasswordCount() {
        try {
            db = new DatabaseService("select count(*) from user_info");
            db.start();
            db.join();
            ResultSet res=db.getResultSet();
            if (res.next()) {// 判断Cursor中是否有数据
                return res.getInt(1);
            }
            res.close();// 关闭游标
        } catch (Exception e) {
            Log.e("SqlError", Arrays.toString(e.getStackTrace()));
        }
        return 0;
    }
}