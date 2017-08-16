package com.example.workingonopengl.openglsurfaceviews;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.workingonopengl.openglactivities.OpenGLMainActivity;
import com.example.workingonopengl.openglrenders.OpenGLRender;

/**
 * Created by 2114 on 06-07-2017.
 */

public class OpenGLSurfaceView extends GLSurfaceView
{

    private final OpenGLRender openGLRender;

    public OpenGLSurfaceView(Context context,boolean isPortrait)
    {
        super(context);

        setEGLContextClientVersion(2);

        openGLRender = new OpenGLRender((OpenGLMainActivity)context,isPortrait);
        setPreserveEGLContextOnPause(true);
        setRenderer(openGLRender);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public OpenGLRender getOpenGLRender()
    {
        return openGLRender;
    }
}
