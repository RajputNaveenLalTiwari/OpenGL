package com.example.workingonopengl.openglshapes;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.example.workingonopengl.openglactivities.OpenGLMainActivity;
import com.example.workingonopengl.openglrenders.OpenGLRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 2114 on 10-07-2017.
 */

public class MyTextureTwo
{
    public static int  bitmap_width,bitmap_height;
    private OpenGLMainActivity context;
    private static final int COORDINATES_PER_VERTEX = 2;
    int[] texture = new int[2];;

    private FloatBuffer vertex_float_buffer,
            texture_float_buffer;
    private int vertex_capacity,
            texture_capacity;
    private ByteBuffer vertex_byte_buffer,
            texture_byte_buffer;

    /*private static float vertex_array[] =
            {
                    1.0f,-1.0f,
                    -1.0f,-1.0f,
                    1.0f,1.0f,
                    -1.0f,1.0f
            };*/
    private static float vertex_array[];
    private static float texture_array[];



    /*private final String vertex_shader_code =
            "attribute vec2 vertex_position;\n" +
            "attribute vec2 vertex_texture_coordinates;\n" +
            "varying vec2 texture_coordinates;\n" +
            "void main()\n" +
            "{\n" +
                "texture_coordinates = vertex_texture_coordinates;\n" +
                "gl_position = vec4(vertex_position.x, vertex_position.y, 0.0, 1.0);\n" +
            "}";

    private final String fragment_shader_code =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "uniform samplerExternalOES sample_texture;\n" +
                    "varying vec2 texture_coordinates;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sample_texture,texture_coordinates);\n" +
                    "}";*/

    private final String vertex_shader_code =
            "attribute vec2 vPosition;\n" +
                    "attribute vec2 vTexCoord;\n" +
                    "varying vec2 texCoord;\n" +
                    "void main() {\n" +
                    "  texCoord = vTexCoord;\n" +
                    "  gl_Position = vec4 ( vPosition.x, vPosition.y, 0.0, 1.0 );\n" +
                    "}";

    private final String fragment_shader_code =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "varying vec2 texCoord;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture,texCoord);\n" +
                    "}";
    int program;

    private int vertexCount;
    private int vertexStride;

    private EffectContext effectContext;
    private Effect effect;


    public MyTextureTwo(OpenGLMainActivity context, boolean isPortrait)
    {
        this.context = context;
        if (isPortrait)
        {
            vertex_array = new float[]
                    {
                            0.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 0.0f,
                            1.0f, 1.0f
                    };

            texture_array = new float[]
                    {
                            1.0f,1.0f,
                            0.0f,1.0f,
                            1.0f,0.0f,
                            0.0f,0.0f
                    };
        }
        else
        {
            vertex_array = new float[]
                    {
                            1.0f,0.0f,
                            0.0f,0.0f,
                            1.0f,1.0f,
                            0.0f,1.0f
                    };

            texture_array = new float[]
                    {
                            1.0f,1.0f,
                            0.0f,1.0f,
                            1.0f,0.0f,
                            0.0f,0.0f
                    };
        }

        vertexCount = vertex_array.length / COORDINATES_PER_VERTEX;
        vertexStride = COORDINATES_PER_VERTEX*4;

        vertex_capacity = vertex_array.length * 4;
        texture_capacity = texture_array.length * 4;

        vertex_byte_buffer = ByteBuffer.allocateDirect(vertex_capacity);
        vertex_byte_buffer.order(ByteOrder.nativeOrder());
        vertex_float_buffer = vertex_byte_buffer.asFloatBuffer();
        vertex_float_buffer.put(vertex_array);
        vertex_float_buffer.position(0);

        texture_byte_buffer = ByteBuffer.allocateDirect(texture_capacity);
        texture_byte_buffer.order(ByteOrder.nativeOrder());
        texture_float_buffer = texture_byte_buffer.asFloatBuffer();
        texture_float_buffer.put(texture_array);
        texture_float_buffer.position(0);

        int vertex_shader = OpenGLRender.loadShader(GLES20.GL_VERTEX_SHADER,vertex_shader_code);
        int fragment_shader = OpenGLRender.loadShader(GLES20.GL_FRAGMENT_SHADER,fragment_shader_code);
        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertex_shader);
        GLES20.glAttachShader(program,fragment_shader);
        GLES20.glLinkProgram(program);

    }

    public void draw(SurfaceTexture surfaceTexture)
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glUseProgram(program);
        GLES20.glDisable(GLES20.GL_BLEND);

        synchronized (this)
        {
            if (surfaceTexture!=null) {
                surfaceTexture.updateTexImage();
            }
        }

        int vertex_position_location = GLES20.glGetAttribLocation(program,"vPosition");
        int vertex_texture_coordinates_location = GLES20.glGetAttribLocation(program,"vTexCoord");
        int sample_texture_location = GLES20.glGetUniformLocation(program,"sTexture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,texture[0]);
        GLES20.glUniform1i(sample_texture_location,0);


        GLES20.glVertexAttribPointer(
                vertex_position_location,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertex_float_buffer);

        GLES20.glVertexAttribPointer(
                vertex_texture_coordinates_location,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                texture_float_buffer);

        GLES20.glEnableVertexAttribArray(vertex_position_location);
        GLES20.glEnableVertexAttribArray(vertex_texture_coordinates_location);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
        GLES20.glFlush();
    }

    public void addTexture()
    {

        GLES20.glGenTextures ( 2, texture, 0 );
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        /*if (effectContext == null)
        {
            effectContext = EffectContext.createWithCurrentGlContext();
        }

        if (effect != null)
        {
            effect.release();
        }
        documentaryEffect();*/
        context.sendTexture(texture[0]);
//        context.openCamera(Camera.Came raInfo.CAMERA_FACING_BACK,texture[0]);
    }

    private void documentaryEffect()
    {
        EffectFactory effectFactory = effectContext.getFactory();
        effect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
        effect.apply(texture[0],50,100,texture[1]);


    }

    public void removeTexture()
    {
        if (texture!=null)
            GLES20.glDeleteTextures(1,texture,0);
    }
}
