package com.example.workingonopengl.openglrenders;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.workingonopengl.openglactivities.OpenGLMainActivity;
import com.example.workingonopengl.openglshapes.MyTextureOne;
import com.example.workingonopengl.openglshapes.MyTextureTwo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 2114 on 06-07-2017.
 */

public class OpenGLRender implements GLSurfaceView.Renderer
{
    private OpenGLMainActivity context;
    private boolean isPortrait;
//    private OpenGLPreviewOne openGLPreviewOne;
//    private OpenGLPreviewTwo openGLPreviewTwo;
//    private OpenGLPreviewThree openGLPreviewThree;
//    private OpenGLPreviewFour openGLPreviewFour;
//    private OpenGLPreviewTexture openGLPreviewTexture;

    private MyTextureOne myTexture;
//    private MyTextureTwo myTextureTwo;
    private MyTextureTwo myTextureTwo;

//    private int[] texture = new int[1];
    private SurfaceTexture surfaceTexture;

    public OpenGLRender(OpenGLMainActivity context,boolean isPortrait)
    {
        this.context = context;
        this.isPortrait = isPortrait;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

//        openGLPreviewOne = new OpenGLPreviewOne();
//        openGLPreviewTwo = new OpenGLPreviewTwo();
//        openGLPreviewThree = new OpenGLPreviewThree();
//        openGLPreviewFour = new OpenGLPreviewFour();
//        openGLPreviewTexture = new OpenGLPreviewTexture(context);

//        myTexture = new MyTextureOne(context,isPortrait);
//        myTexture.addTexture();

        myTextureTwo = new MyTextureTwo(context,isPortrait);
        myTextureTwo.addTexture();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
        MyTextureTwo.bitmap_width =width;
        MyTextureTwo.bitmap_height=height;
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        openGLPreviewOne.draw();
//        openGLPreviewTwo.draw();
//        openGLPreviewThree.draw();
//        openGLPreviewFour.draw();
//        openGLPreviewTexture.draw();

//        myTexture.draw(surfaceTexture);
        myTextureTwo.draw(surfaceTexture);


    }


    public static int loadShader(int type, String shaderCode)
    {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture)
    {
        this.surfaceTexture = surfaceTexture;

    }
}
