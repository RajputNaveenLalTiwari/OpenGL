package com.example.workingonopengl.openglshapes;

import android.opengl.GLES20;

import com.example.workingonopengl.openglrenders.OpenGLRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by 2114 on 06-07-2017.
 */

public class OpenGLPreviewOne
{
    private FloatBuffer floatBuffer;
    private ShortBuffer shortBuffer;

    static final int COORDINATES_PER_VERTEX = 3;

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    static float vertex_array[] =
            {
                -1.0f,1.0f,0.0f,    // Top-Left
                -1.0f,0.0f,0.0f,    // Bottom-Left
                0.0f,0.0f,0.0f,     // Bottom-Right
                0.0f,1.0f,0.0f      // Top-Right
            };

    private short drawing_order_of_vertices[] =
            {0,1,2,0,2,3};



    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
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

    private final int vertexCount = vertex_array.length / COORDINATES_PER_VERTEX;
    private final int vertexStride = COORDINATES_PER_VERTEX * 4; // 4 bytes per vertex

    public OpenGLPreviewOne()
    {
/**/
        int vertices_capacity = vertex_array.length*4;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices_capacity);
        byteBuffer.order(ByteOrder.nativeOrder());

        floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(vertex_array);
        floatBuffer.position(0);
/**/
        int drawing_vertices_capacity = drawing_order_of_vertices.length*2;
        ByteBuffer drawingByteBuffer = ByteBuffer.allocateDirect(drawing_vertices_capacity);
        drawingByteBuffer.order(ByteOrder.nativeOrder());

        shortBuffer = drawingByteBuffer.asShortBuffer();
        shortBuffer.put(drawing_order_of_vertices);
        shortBuffer.position(0);
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

        vertex_shader_position = GLES20.glGetAttribLocation(program,"vPosition");
        fragment_shader_color  = GLES20.glGetUniformLocation(program,"vColor");

        GLES20.glEnableVertexAttribArray(vertex_shader_position);
        GLES20.glVertexAttribPointer(
                vertex_shader_position,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                floatBuffer);

        GLES20.glUniform4fv(fragment_shader_color,1,color,0);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                drawing_order_of_vertices.length,
                GLES20.GL_UNSIGNED_SHORT,
                shortBuffer);

        GLES20.glDisableVertexAttribArray(vertex_shader_position);

    }
}
