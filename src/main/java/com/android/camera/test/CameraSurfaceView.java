package com.android.camera.test;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import java.io.IOException;
import android.util.Log;
import android.util.AttributeSet;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	public SurfaceHolder mHolder;
	public Camera camera;

	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			camera.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera arg1) {
					Log.e("Camera","Bytes received" + data.length);
				}
			});
		} catch (IOException e) {}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		try {
			camera.stopPreview();
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		camera.stopPreview();
		camera.release();
	}

}