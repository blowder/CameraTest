package com.android.camera.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.ImageView;
import com.android.camera.test.R;


public class MainActivity extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        CameraSurfaceView view = (CameraSurfaceView) findViewById(R.id.surface);
        ImageView image = (ImageView) findViewById(R.id.image);
        view.setTarget(image);
    }
}