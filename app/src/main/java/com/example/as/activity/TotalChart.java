package com.example.as.activity;
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
import android.view.View;
import android.widget.FrameLayout;

import com.example.as.R;
import com.example.as.dao.InaccountDAO;
import com.example.as.dao.OutaccountDAO;

public class TotalChart extends Activity {
	private float[] money=new float[]{600,1000,600,300,1500};	//各项金额的默认值
	private final int[] color=new int[]{Color.GREEN,Color.YELLOW,Color.RED,Color.MAGENTA,Color.BLUE};	//各项颜色
	private final int WIDTH = 30;	//柱型的宽度
	private final int OFFSET = 15;	//间距
	private final int x =70;	//起点x
	private final int y=329;	//终点y
	private final int height=5200;	//高度
	String[] type=null;		//金额的类型
	private String passType="";	//记录是收入信息还是支出信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);		//设置使用的布局文件
		Intent intent=getIntent();		//获取Intent对象
		Bundle bundle=intent.getExtras();		//获取传递的数据包
		passType=bundle.getString("passType");
		Resources res=getResources();	//获取Resources对象
		if("outinfo".equals(passType)){
			type=res.getStringArray(R.array.outtype);	//获取支出类型数组
			
		}else if("ininfo".equals(passType)){
			type=res.getStringArray(R.array.intype);	//获取收入类型数组
		}
		
		
		
		
		
		
		
		FrameLayout ll= findViewById(R.id.canvas);//获取布局文件中添加的帧布局管理器
		ll.addView(new MyView(this));				//将自定义的MyView视图添加到帧布局管理器中
		
	}
	public class MyView extends View{
		public MyView(Context context) {
			super(context);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawColor(Color.WHITE);					//指定画布的背景色为白色
			Paint paint=new Paint();						//创建采用默认设置的画笔
			paint.setAntiAlias(true);						//使用抗锯齿功能
			/***********绘制坐标轴**********************/
			paint.setStrokeWidth(1);						//设置笔触的宽度
			paint.setColor(Color.BLACK);		//设置笔触的颜色
			canvas.drawLine(50, 330, 300, 330, paint);	//横
			canvas.drawLine(50, 100, 50, 330, paint);	//竖
			/******************************************/
			/***********绘制柱型**********************/
			paint.setStyle(Style.FILL);					//设置填充样式为填充
			int left=0;	//每个柱型的起点X坐标
			money=getMoney(passType);						//新获取的金额******************
			float max=maxMoney(money);
			for(int i=0;i<money.length;i++){
				paint.setColor(color[i]);		//设置笔触的颜色
				left=x+i*(OFFSET+WIDTH);	//计算每个柱型起点X坐标
				canvas.drawRect(left, y-height/max*money[i], left+WIDTH, y, paint);	
			}
			/******************************************/
			/***********绘制纵轴的刻度**********************/
			paint.setColor(Color.BLACK);		//设置笔触的颜色
			int tempY=0;
			for(int i=0;i<11;i++){
				tempY=y-height+height/10*i+1;
				canvas.drawLine(47,tempY , 50, tempY, paint);
				paint.setTextSize(12);	//设置字体大小
				canvas.drawText(String.valueOf((int)(max/10*(10-i))), 15,tempY+5, paint);	//绘制纵轴题注	
			}
			/******************************************/
			/***********绘制说明文字**********************/
			paint.setColor(Color.BLACK);		//设置笔触的颜色
			paint.setTextSize(21);	//设置字体大小
			/******************绘制标题*********************************/
			if("outinfo".equals(passType)){
				canvas.drawText("个人理财通的支出统计图", 40,55, paint);	//绘制标题
			}else if("ininfo".equals(passType)){
				canvas.drawText("个人理财通的收入统计图", 40,55, paint);	//绘制标题
			}	
			/***********************************************************/
			paint.setTextSize(16);	//设置字体大小

			String str_type="";
			for(int i=0;i<type.length;i++){
				str_type+=type[i]+"   ";
			}
			canvas.drawText(str_type, 68,350, paint);	//绘制横轴题注
		}
	}
	//计算最大金额
	float maxMoney(float[] money){
		float max=money[0];	//将第一个数组元素赋值给变量max
		for(int i=0;i<money.length-1;i++){
			if(max<money[i+1]){
				max=money[i+1];		//更新max
			}
		}
		return max;
	}
	//获取收支数据
	float[] getMoney(String flagType){
		Map mapMoney=null;
		System.out.println(flagType);
		if("ininfo".equals(flagType)){
			InaccountDAO inaccountinfo = new InaccountDAO(TotalChart.this);// 创建TotalChart对象
			mapMoney=inaccountinfo.getTotal();  //获取收入汇总信息			
		}else if("outinfo".equals(flagType)){
			OutaccountDAO outaccountinfo = new OutaccountDAO(TotalChart.this);// 创建TotalChart对象
			mapMoney=outaccountinfo.getTotal();	//获取支出汇总信息
		}	
		int size=type.length;
		float[] money1=new float[size];
		for(int i=0;i<size;i++){
			money1[i]=( mapMoney.get(type[i])!=null?((Float) mapMoney.get(type[i])):0);
		}
		return money1;
	}
}
