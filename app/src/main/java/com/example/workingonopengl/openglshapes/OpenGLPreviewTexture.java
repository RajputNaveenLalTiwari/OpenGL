package com.example.workingonopengl.openglshapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.workingonopengl.openglrenders.OpenGLRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 2114 on 06-07-2017.
 */

public class OpenGLPreviewTexture
{
    private FloatBuffer floatBuffer;
    static final int COORDINATES_PER_VERTEX = 2;
    private static float texture_coordinates[] =
            {
                    0.0f,1.0f,  //  Top-Left
                    0.0f,0.5f,  //  Bottom-Left
                    0.5f,0.5f,  //  Bottom-Right
                    0.5f,1.0f   //  Top-Right
            };

    private final String vertexShaderCode =
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                            "  gl_Position = vPosition;" +
                            "  v_texCoord = a_texCoord;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int program;

    private int vertex_shader_position;
    private int fragment_shader_color;

    private final int vertexStride = COORDINATES_PER_VERTEX * 4; // 4 bytes per vertex
    ByteBuffer texture_buffer;

    public OpenGLPreviewTexture(Context context)
    {
        int texture_capacity = texture_coordinates.length*4;
        texture_buffer = ByteBuffer.allocateDirect(texture_capacity);
        texture_buffer.order(ByteOrder.nativeOrder());

        floatBuffer = texture_buffer.asFloatBuffer();
        floatBuffer.put(texture_coordinates);
        floatBuffer.position(0);

        /**/
        int[] texture = new int[1];
        GLES20.glGenTextures(1,texture,0);

        int image_resource_id = context.getResources().getIdentifier("drawable/ic_launcher", null,
                context.getPackageName());

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),image_resource_id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // We are done using the bitmap so we should recycle it.
        bitmap.recycle();
        /**/

        int vertex_shader = OpenGLRender.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragment_shader = OpenGLRender.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertex_shader);
        GLES20.glAttachShader(program,fragment_shader);
        GLES20.glLinkProgram(program);
    }

    public void draw()
    {
        GLES20.glUseProgram(program);

        int texture_coordinates_location = GLES20.glGetAttribLocation(program,"a_texCoord");
        GLES20.glEnableVertexAttribArray(texture_coordinates_location);

        GLES20.glVertexAttribPointer(
                texture_coordinates_location,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                texture_buffer
        );

        GLES20.glDisableVertexAttribArray(texture_coordinates_location);
    }
}
