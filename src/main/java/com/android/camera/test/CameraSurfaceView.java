package com.android.camera.test;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static String TAG = CameraSurfaceView.class.getSimpleName();
    private Camera camera;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void surfaceCreated(final SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated()");
        if (this.camera == null) this.camera = Camera.open();
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.e(TAG, "surfaceChanged() holder width=" + w + ", height=" + h);
        Camera.Size cameraSize = getOptimalPreviewSize(camera.getParameters().getSupportedPreviewSizes(), w, h);
        Log.e(TAG, "surfaceChanged() camera width=" + cameraSize.width + ", height=" + cameraSize.height);
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(cameraSize.width, cameraSize.height);
            camera.setParameters(parameters);
            if (w < h)
                camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Log.e(TAG, "onPreviewFrame(): data=" + data.length);
                }
            });
            camera.startPreview();
        } catch (IOException ignored) {
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed();");
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }
}