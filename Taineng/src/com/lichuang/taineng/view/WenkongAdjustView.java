package com.lichuang.taineng.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.abs;

public class WenkongAdjustView extends View implements OnGestureListener{
	
	private GestureDetectorCompat gestureDetector;
    private int width;
    private int height;
    private int huadong_start_x;
    private int huadong_start_y;
    private int huadong_stop_x;
    private int huadong_stop_y;
    private boolean is_huadong;
    private static float[] data= new float[7];
    private float[] pts = new float[28];
    private float y_rongcuo_value = 0.8f; //这两个值是根据触碰到的点来确定对应坐标点的容错值,这两个值都小于等于1
    private float x_rongcuo_value= 0.4f;

	public WenkongAdjustView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		gestureDetector=new GestureDetectorCompat(context,this);
        for(int i=0;i<7;i++){
            data[i]=17;
        }
	}
	
	public float[] GetData(){
	       return data;
	    }
	public void SetData(float[] src){
		data= src;
	}
	

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		width = getWidth();
        height = getHeight();
        DrawAxis(canvas,width,height);
        DrawPoint(canvas);
	}
	
	/**
     * 画坐标系
     * @param canvas
     * @param width
     * @param height
     */
    private void DrawAxis(Canvas canvas,int width,int height){
        int pw = width/9;
        int ph=height/13;
        Paint paint= new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        canvas.drawLine(pw,height-ph,width-pw,height-ph,paint);   //画横坐标
        canvas.drawLine(pw,height-12*ph,pw,height-ph,paint);                //画纵坐标
        paint.setColor(Color.BLACK);
        for(int i=0;i<8;i++){
            canvas.drawText(i+"",pw*(i+1),height-ph+12,paint);
        }
        int j=2;
        for(int i=18;i<29;i++){
            canvas.drawText(i+"",pw-15,height-ph*j,paint);
            j++;
        }
        paint.setColor(Color.parseColor("#83f3e4"));
        j=2;
        for(int i=0;i<11;i++){
            canvas.drawLine(pw,height-ph*j,pw*8,height-ph*j,paint);   //画横线
            j++;
        }
        j=2;
        for(int i=0;i<7;i++){
            canvas.drawLine(pw*j,height-ph,pw*j,height-12*ph,paint);               //画竖线
            j++;
        }
//        invalidate();

    }

    private void DrawPoint(Canvas canvas){
        int pw = width/9;
        int ph=height/13;
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);


        float value[] = new float[8];
        for(int i=0;i<8;i++){
            if(i==0){
                value[i]=height-ph;
            }else{
                value[i]=height-ph-(ph*(data[i-1]-17));
            }

        }
        for(int i=1;i<9;i++){

             canvas.drawCircle(pw*i,value[i-1],7,paint);   //画点
        }
        for(int i=1,j=0;i<8;i++){

                pts[j]=pw*i;
                pts[j+1]=value[i-1];
                pts[j+2]=pw*(i+1);
                pts[j+3]=value[i];

            j+=4;
        }
        canvas.drawLines(pts,paint);                             //点连线

    }

	//**************************触控事件处理***************************
    
    
    
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		  float x = e.getX();
	        float y = e.getY();
	        Point point = HandleCoordinate(x,y);
	        if(point != null){
	            huadong_start_x = point.x;
	            huadong_start_y = point.y;
	        }else{
	            huadong_start_x = 0;
	            huadong_start_y = 0;
	        }
	        Log.i("wenkong event","触发 down 事件 x:"+huadong_start_x+"....y:"+huadong_start_y);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		this.gestureDetector.onTouchEvent(event);
        return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if(huadong_start_x!=0){
            int pw = width/9;
            int ph=height/13;
            int index = huadong_start_x/pw;
            float x = e2.getX();
            float y = e2.getY();
            if(x>pw && x<=pw*(8+x_rongcuo_value)){
                if(y<=(height-ph) && y>= (height-ph*12)){
                    float value = (float)(Math.round(((height-ph-y)/ph+17)*100))/100;
                    Log.i("wenkong event","触发 scroll 事件  value:"+value);
                    data[index-2]= value;  //算出温度值并保留两位小数
                    invalidate();
                }
            }
        }
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	//********************触控事件处理 结束**************************************
	  //处理坐标，找到想要去移动的点
    private Point HandleCoordinate(float x,float y){
        Point resultPoint= new Point();

        int pw = width/9;
        int ph=height/13;
        int bzX = 0;
        int bzY = 0;
        if(x>pw && x<=(pw*(8+x_rongcuo_value))){
            if(y<=(height-ph*(1-y_rongcuo_value)) && y>= (height-ph*12)){
                float tempFloat= x/pw;
                int tempInt = (int)(x/pw);
                Log.i("wenkong diy view","....tempint:"+tempInt);
                tempFloat= tempFloat-tempInt;
                if(tempInt == 1){
                    if(tempFloat >= (1-x_rongcuo_value)){
                        bzX=pw*2;
                        bzY=(int)(height-ph-(ph*(data[0]-17)));
                        if(abs(y - bzY)/ph<=y_rongcuo_value){
                            resultPoint.x= bzX;
                            resultPoint.y=bzY;
                            return resultPoint;

                        }
                        return null;
                    }else{
                        //返回空
                        return null;
                    }
                }else{
                    if(tempFloat <= x_rongcuo_value){
                       bzX=(tempInt)*pw;
                       bzY=(int)(height-ph-(ph*(data[tempInt-2]-17)));
                        if(abs(y - bzY)/ph<=y_rongcuo_value){
                            resultPoint.x= bzX;
                            resultPoint.y=bzY;
                            return resultPoint;

                        }
                        return null;
                    }
                    if(tempFloat >=(1-x_rongcuo_value)){

                        bzX=(tempInt+1)*pw;
                        bzY=(int)(height-ph-(ph*(data[tempInt-1]-17)));
                        if(abs(y - bzY)/ph<=y_rongcuo_value){
                            resultPoint.x= bzX;
                            resultPoint.y=bzY;
                            return resultPoint;
                        }
                        return null;
                    }
                }
            }
            return null;
        }
        return null;
    }
	
}
