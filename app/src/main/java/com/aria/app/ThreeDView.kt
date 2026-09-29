package com.aria.app

import android.content.Context
import android.util.AttributeSet
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ThreeDView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {
    init {
        setEGLContextClientVersion(2)
        setRenderer(CubeRenderer())
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    private class CubeRenderer : Renderer {
        private val vertices = floatArrayOf(
            -0.6f,-0.6f, 0.6f,  0.6f,-0.6f, 0.6f,  0.6f,0.6f,0.6f, -0.6f,0.6f,0.6f,
            -0.6f,-0.6f,-0.6f, -0.6f,0.6f,-0.6f, 0.6f,0.6f,-0.6f, 0.6f,-0.6f,-0.6f
        )
        private val indices = shortArrayOf(0,1,2,0,2,3,4,5,6,4,6,7,0,4,7,0,7,1,3,2,6,3,6,5,1,7,6,1,6,2,0,3,5,0,5,4)
        private val buffer: FloatBuffer = ByteBuffer.allocateDirect(vertices.size*4).order(ByteOrder.nativeOrder()).asFloatBuffer().apply{put(vertices).position(0)}
        private val indexBuffer = ByteBuffer.allocateDirect(indices.size*2).order(ByteOrder.nativeOrder()).asShortBuffer().apply{put(indices).position(0)}
        private val mvp = FloatArray(16)
        private val projection = FloatArray(16)
        private val view = FloatArray(16)
        private val model = FloatArray(16)
        private var program = 0
        private var angle = 0f
        private val vertexShader = """
            attribute vec4 aPosition;
            uniform mat4 uMvp;
            void main(){ gl_Position = uMvp * aPosition; }
        """
        private val fragmentShader = """
            precision mediump float;
            void main(){ gl_FragColor = vec4(0.20,0.65,1.0,1.0); }
        """

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            program = GLES20.glCreateProgram().also {
                val vs=compile(GLES20.GL_VERTEX_SHADER,vertexShader)
                val fs=compile(GLES20.GL_FRAGMENT_SHADER,fragmentShader)
                GLES20.glAttachShader(it,vs);GLES20.glAttachShader(it,fs);GLES20.glLinkProgram(it)
            }
            GLES20.glClearColor(0.03f,0.03f,0.08f,1f)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0,0,width,height)
            val ratio=width.toFloat()/height.coerceAtLeast(1)
            Matrix.frustumM(projection,0,-ratio,ratio,-1f,1f,2f,10f)
            Matrix.setLookAtM(view,0,0f,0f,3.5f,0f,0f,0f,0f,1f,0f)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            Matrix.setIdentityM(model,0)
            Matrix.rotateM(model,0,angle,0.4f,1f,0.2f)
            Matrix.multiplyMM(mvp,0,view,0,model,0)
            Matrix.multiplyMM(mvp,0,projection,0,mvp,0)
            GLES20.glUseProgram(program)
            val pos=GLES20.glGetAttribLocation(program,"aPosition")
            val u=GLES20.glGetUniformLocation(program,"uMvp")
            GLES20.glEnableVertexAttribArray(pos)
            GLES20.glVertexAttribPointer(pos,3,GLES20.GL_FLOAT,false,12,buffer)
            GLES20.glUniformMatrix4fv(u,1,false,mvp,0)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES,indices.size,GLES20.GL_UNSIGNED_SHORT,indexBuffer)
            GLES20.glDisableVertexAttribArray(pos)
            angle=(angle+1.0f)%360f
        }

        private fun compile(type:Int,source:String):Int{
            val shader=GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader,source);GLES20.glCompileShader(shader)
            return shader
        }
    }
}
