package com.lichuang.taineng.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "Preview";

    SurfaceHolder m_holder;
    //public Camera camera;
    
    public  Preview(Context context) {
        super(context);
        
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        m_holder = getHolder();
        m_holder.addCallback(this);
        m_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
    	CBCamera.instance().initialPreview(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
    	CBCamera.instance().stopPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
    	CBCamera.instance().startPreview(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
    		super.draw(canvas);
    		Paint p= new Paint(Color.RED);
    		Log.d(TAG,"draw");
    		canvas.drawText("PREVIEW", canvas.getWidth()/2, canvas.getHeight()/2, p );
    }
}