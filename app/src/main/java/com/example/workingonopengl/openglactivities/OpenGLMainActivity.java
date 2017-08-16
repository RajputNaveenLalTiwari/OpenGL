package com.example.workingonopengl.openglactivities;

import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;

import com.example.workingonopengl.openglrenders.OpenGLRender;
import com.example.workingonopengl.openglsurfaceviews.OpenGLSurfaceView;

import java.io.IOException;

public class OpenGLMainActivity extends AppCompatActivity implements SurfaceTexture.OnFrameAvailableListener
{
    OpenGLSurfaceView openGLSurfaceView;

    private boolean isPortrait;
    private SurfaceTexture surfaceTexture;
    private OpenGLRender openGLRender;

    private Camera camera = null;
    static int FACING_FRONT;
    boolean previewing = false;
    int cameraId;
    int rotation;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        if( getResources().getConfiguration().orientation == 1 )	// ORIENTATION_PORTRAIT
        {
            Log.e("onCreate", "ORIENTATION_PORTRAIT");
            isPortrait = true;
            openGLSurfaceView = new OpenGLSurfaceView(this,isPortrait);
            openGLRender = openGLSurfaceView.getOpenGLRender();
        }
        else if( getResources().getConfiguration().orientation == 2 ) // ORIENTATION_LANDSCAPE
        {
            Log.e("onCreate", "ORIENTATION_LANDSCAPE");
            isPortrait = false;
            openGLSurfaceView = new OpenGLSurfaceView(this,isPortrait);
            openGLRender = openGLSurfaceView.getOpenGLRender();
        }

        setContentView(openGLSurfaceView);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        openGLSurfaceView.requestRender();
    }

  /*  public void openCamera(int texture)
    {
        surfaceTexture = new SurfaceTexture(texture);
        surfaceTexture.setOnFrameAvailableListener(this);
        openGLRender.setSurfaceTexture(surfaceTexture);
        releaseCamera();
        try
        {
            camera = Camera.open();
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }*/

    public void test()
    {
        if(camera != null)
        {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void releaseCamera()
    {
        try
        {
            if (camera != null)
            {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }

        }
        catch (Exception e)
        {
            camera = null;
        }
    }

    public void startCamera(int id)
    {
        releaseCamera();
        try
        {
            camera = Camera.open(id);
            camera.startPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendTexture(int texture)
    {
        surfaceTexture = new SurfaceTexture(texture);
        surfaceTexture.setOnFrameAvailableListener(this);
        openGLRender.setSurfaceTexture(surfaceTexture);
        if (camera != null)
        {
            try {
                camera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    }

    /*public boolean openCamera(int id,int texture)
    {
        boolean result = false;
        cameraId = id;
        surfaceTexture = new SurfaceTexture(texture);
        surfaceTexture.setOnFrameAvailableListener(this);
        openGLRender.setSurfaceTexture(surfaceTexture);
        releaseCamera();
        try
        {
            camera = Camera.open(id);
            //camera.setDisplayOrientation(90);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (camera != null)
        {
            try
            {
                setUpCamera(camera);

                camera.setErrorCallback(new Camera.ErrorCallback()
                {
                    @Override
                    public void onError(int arg0, Camera arg1)
                    {

                    }
                });

                camera.setPreviewTexture(surfaceTexture);
                camera.startPreview();
                result = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }*/

    private void setUpCamera(Camera camera)
    {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        rotation=getWindowManager().getDefaultDisplay().getRotation();

        int degree = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            // frontFacing
            FACING_FRONT = 1;
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        }
        else
        {
            // Back-facing
            FACING_FRONT = 0;
            rotation = (info.orientation - degree + 360) % 360;
        }
        camera.setDisplayOrientation(rotation);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(camera != null)
        {
            camera.stopPreview();
            camera.release();
            camera = null;
            previewing = false;
        }
    }
}
