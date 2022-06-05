package com.example.as.activity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.as.R;
import com.example.as.dao.AddIncomeDAO;
import com.example.as.dao.AddPayDAO;
import com.example.as.dao.InaccountDAO;
import com.example.as.dao.OutaccountDAO;

public class TotalChartspan extends Activity {
    private float[] money = new float[]{600, 1000, 600, 300, 1500};    //各项金额的默认值
    private final int[] color = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE};    //各项颜色
    private final int WIDTH = 30 * 2;    //柱型的宽度
    private final int OFFSET = 15 * 2;    //间距
    private final int x = 70 * 2;    //起点x
    private final int y = 329 * 2;    //终点y
    private final int height = 200 * 2;    //高度
    String[] type = null;        //金额的类型
    private String passType = "";    //记录是收入信息还是支出信息
    float totalmoney = 0;
    String username;
    String starttime;
    String overtime;
    LocalDateTime StartTime;
    LocalDateTime OverTime;
//    private String[] statistic = new String[2];//创建字符串数组记录数据分类类型以及是收入还是支出
    //    private String[] statistic2 = new String[]{"type","in"};
//    private String[] statistic3 = new String[]{"type","out"};

    List<String> typeList = new ArrayList<>();//记录收入类型或者支出类型
    AddIncomeDAO addincomeDAO = new AddIncomeDAO();
    AddPayDAO addPayDAO = new AddPayDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chartspan);        //设置使用的布局文件
        Intent intent = getIntent();        //获取Intent对象
        Bundle bundle = intent.getExtras();        //获取传递的数据包
        username = bundle.getString("username");
        passType = bundle.getString("in_or_out");
        starttime = bundle.getString("starttime");
        overtime = bundle.getString("overtime");
//         StartTime=LocalDateTime.parse(starttime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//         OverTime=LocalDateTime.parse(overtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (passType.equals("收入")) {
            passType = "ininfo";
        } else {
            passType = "outinfo";
        }
//        statistic=bundle.getStringArray("statistic");
//        passType=statistic[1];
//		Resources res=getResources();	//获取Resources对象

        if ("outinfo".equals(passType)) {
            try {
                typeList = addPayDAO.findTypefromtransactionsspan(username, starttime, overtime);
            } catch (SQLException e) {
                Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            }
            type = typeList.toArray(new String[typeList.size()]);
        } else if ("ininfo".equals(passType)) {
            try {
                typeList = addincomeDAO.findTypefromtransactionsspan(username, starttime, overtime);
            } catch (SQLException e) {
                Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            }
            type = typeList.toArray(new String[typeList.size()]);
        }


        FrameLayout ll = findViewById(R.id.canvas);//获取布局文件中添加的帧布局管理器
        ll.addView(new MyView(this));                //将自定义的MyView视图添加到帧布局管理器中

    }

    public class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);                    //指定画布的背景色为白色
            Paint paint = new Paint();                        //创建采用默认设置的画笔
            paint.setAntiAlias(true);                        //使用抗锯齿功能
            /***********绘制坐标轴**********************/
            paint.setStrokeWidth(1);                        //设置笔触的宽度
            paint.setColor(Color.BLACK);        //设置笔触的颜色
            canvas.drawLine(50 * 2, 330 * 2, 300 * 2, 330 * 2, paint);    //横
            canvas.drawLine(50 * 2, 100 * 2, 50 * 2, 330 * 2, paint);    //竖
            canvas.drawLine(50 * 2, 330 * 4, 300 * 2, 330 * 4, paint);    //横
            canvas.drawLine(50 * 2, 100 * 8, 50 * 2, 330 * 4, paint);    //竖
            /******************************************/
            /***********绘制柱型**********************/
            paint.setStyle(Style.FILL);                    //设置填充样式为填充
            int left = 0;    //每个柱型的起点X坐标
            money = getMoney(passType, username, starttime, overtime);                        //新获取的金额******************
            float max = maxMoney(money);
            for (int i = 0; i < money.length; i++) {
                paint.setColor(color[i]);        //设置笔触的颜色
                left = x + i * (OFFSET + WIDTH);    //计算每个柱型起点X坐标
                canvas.drawRect(left, y - height / max * money[i], left + WIDTH, y, paint);
                totalmoney = totalmoney + money[i];
            }
            canvas.drawRect(140, 330 * 4 - height, 200, 330 * 4, paint);
            /******************************************/
            /***********绘制纵轴的刻度**********************/
            paint.setColor(Color.BLACK);        //设置笔触的颜色
            int tempY = 0;
            int tempY2 = 0;
            for (int i = 0; i < 11; i++) {
                tempY = y - height + height / 10 * i + 1;
                tempY2 = 330 * 4 - height + height / 10 * i + 1;
                canvas.drawLine(47 * 2, tempY, 50 * 2, tempY, paint);
                canvas.drawLine(47 * 2, tempY2, 50 * 2, tempY2, paint);
                paint.setTextSize(12 * 2);    //设置字体大小
                canvas.drawText(String.valueOf((int) (max / 10 * (10 - i))), 15 * 2, tempY + 5, paint);    //绘制纵轴题注
                canvas.drawText(String.valueOf((int) (totalmoney / 10 * (10 - i))), 15 * 2, tempY2 + 5, paint);
            }
            /******************************************/
            /***********绘制说明文字**********************/
            paint.setColor(Color.BLACK);        //设置笔触的颜色
            paint.setTextSize(21);    //设置字体大小
            /******************绘制标题*********************************/
            if ("outinfo".equals(passType)) {
                canvas.drawText(username + "在" + starttime + "至" + overtime + "的支出统计图", 40 * 2, 55 * 2, paint);    //绘制标题
            } else if ("ininfo".equals(passType)) {
                canvas.drawText(username + "在" + starttime + "至" + overtime + "的收入统计图", 40 * 2, 55 * 2, paint);    //绘制标题
            }
            /***********************************************************/
            paint.setTextSize(16 * 2);    //设置字体大小

            String str_type = "";
            for (int i = 0; i < type.length; i++) {
                str_type += type[i] + "   ";
                canvas.drawText(str_type, 68 * 2, 350 * 2, paint);    //绘制横轴题注
            }
//            canvas.drawText(str_type, 68*2,350*2, paint);	//绘制横轴题注
            canvas.drawText("总计", 68 * 2, 350 * 4, paint);    //绘制横轴题注
        }
    }

    //计算最大金额
    float maxMoney(float[] money) {
        float max = money[0];    //将第一个数组元素赋值给变量max
        for (int i = 0; i < money.length - 1; i++) {
            if (max < money[i + 1]) {
                max = money[i + 1];        //更新max
            }
        }
        return max;
    }

    //获取收支数据
    float[] getMoney(String flagType, String UserName, String starttime, String overtime) {
        Map mapMoney = null;
        System.out.println(flagType);

        if ("ininfo".equals(flagType)) {
            //			InaccountDAO inaccountinfo = new InaccountDAO(TotalChart.this);// 创建TotalChart对象
            //			mapMoney=inaccountinfo.getTotal();  //获取收入汇总信息
            try {
                mapMoney = addincomeDAO.FindSpanTypeMoney(UserName, starttime, overtime);
            } catch (SQLException e) {
                Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            }

        } else if ("outinfo".equals(flagType)) {
            //			OutaccountDAO outaccountinfo = new OutaccountDAO(TotalChart.this);// 创建TotalChart对象
            //			mapMoney=outaccountinfo.getTotal();	//获取支出汇总信息
            try {
                mapMoney = addPayDAO.FindSpanTypeMoney(UserName, starttime, overtime);
            } catch (SQLException e) {
                Log.e("SQL error", Arrays.toString(e.getStackTrace()));
            }
        }


        int size = type.length;
        float[] money1 = new float[size];
        for (int i = 0; i < size; i++) {
            money1[i] = (mapMoney.get(type[i]) != null ? ((Float) mapMoney.get(type[i])) : 0);
        }
        return money1;
    }
}
