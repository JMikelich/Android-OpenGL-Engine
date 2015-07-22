package com.lanzivision.opengles2engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

/* The Sprite class simplifies displaying texture assets on screen. 
 * The class takes the input of the position vector, along with each dimension size,
 * and lastly the filename of the asset. This class is designed for 2d.
 */

public class Sprite {
	static Sprite me;

	//center position of sprite
	public float positionX;
	public float positionY;
	
	//OpenGL triangle vars
	public float vertices[];
	public short indices[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
		
	//size for triangle binding
	public int sizeX;
	public int sizeY;
	
	/*
	 * Sprite create function. Takes the center position for the sprite, and the size dimensions and binds a 
	 * triangle around it.
	 */
	
	public Sprite(float positionX, float positionY, int sizeX, int sizeY, Context mContext) {
		super();
		me = this;
		this.positionX = positionX;
		this.positionY = positionY;
		
		//interpolate position with size to compose vertice array
		this.vertices = new float[]
		           {
					positionX - sizeX/2, positionY + sizeY/2, 0.0f,
					positionX - sizeX/2, positionY - sizeY/2, 0.0f,
					positionX + sizeX/2, positionY - sizeY/2, 0.0f,
					positionX + sizeX/2, positionY + sizeY/2, 0.0f,
		           };
		// The order of vertexrendering.
		this.indices = new short[] {0, 1, 2, 0, 2, 3}; 	
		
		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(this.vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		this.vertexBuffer = bb.asFloatBuffer();
		this.vertexBuffer.put(this.vertices);
		this.vertexBuffer.position(0);
				
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(this.indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		this.drawListBuffer = dlb.asShortBuffer();
		this.drawListBuffer.put(this.indices);
		this.drawListBuffer.position(0);
		
		//sizes of texture
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		
	}
	
	/*
	 * Specify current texture and draw it.
	 */
	public void drawSprite(int mPositionHandle, int mTexCoordLoc, float[] m, Texture currentTexture){
		
		
		 // Set our shader programm
		GLES20.glUseProgram(ShaderTools.sp_Image);
	
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTexture.textureIndex);		
			// Set filtering
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	  
		 // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, 3,
	                                 GLES20.GL_FLOAT, false,
	                                 0, vertexBuffer);
	    
	    // Prepare the texturecoordinates
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false, 
                0, currentTexture.uvBuffer);
	    
	    // Get handle to textures locations
        int mSamplerLoc2 = GLES20.glGetUniformLocation (ShaderTools.sp_Image, "s_texture" );
                
		// Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc2, 1);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        

	}
	
	public void resetSprite()
	{
		positionX = 0;
		positionY = 0;
		
		vertices = new float[0];
		indices = new short[0];
		
		sizeX = 0;
		sizeY = 0;
	}
	
	/*
	 * Translates sprite and updates position vars to new location.
	 */
	public void translateSprite(float X, float Y)
	{

		positionX = X;
		positionY = Y;
		this.vertices = new float[]
		           {
					positionX - sizeX/2, positionY + sizeY/2, 0.0f,
					positionX - sizeX/2, positionY - sizeY/2, 0.0f,
					positionX + sizeX/2, positionY - sizeY/2, 0.0f,
					positionX + sizeX/2, positionY + sizeY/2, 0.0f,
			       };
			
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
			
	}
}
