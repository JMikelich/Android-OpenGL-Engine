package com.lanzivision.opengles2engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.GLES20;

/*
 * SSSprite stands for SpriteSheet sprite. The data types are rearranged for multiple sprites to use the same texture while binding the
 * texture with different coordinates.
 */

public class SSSprite {
	static SSSprite me;

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

	public  float uvs[];
	public FloatBuffer uvBuffer;
	public int textureIndex;


	/*
	 * SSSprite constructor takes in the same as Sprite, with the addition of the currentTexture used, and the desired sprites coordinates.
	 * UVS and the UVBUFFER are created using these designations.
	 */
	public SSSprite(float positionX, float positionY, int sizeX, int sizeY, SSTexture currentTexture, int coordX, int coordY, Context mContext) {
		
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
		
		this.textureIndex = currentTexture.textureIndex;
		
		this.uvs = new float[4*2];
		
		// Calculate the coordinate multiplyer (1 / cellwidth) or (1 / cellheight)
		float xMultiplyer = 1 / (float)currentTexture.maxCellsX;
		float yMultiplyer = 1 / (float)currentTexture.maxCellsY;

		int u_offset = coordX;  //x offset
		int v_offset = coordY;  //y offset
		
		// Adding the UV's using the offsets
		uvs[0] = u_offset * xMultiplyer;
		uvs[1] = v_offset * yMultiplyer;
		uvs[2] = u_offset * xMultiplyer;
		uvs[3] = (v_offset+1) * yMultiplyer;
		uvs[4] = (u_offset+1) * xMultiplyer;
		uvs[5] = (v_offset+1) * yMultiplyer;
		uvs[6] = (u_offset+1) * xMultiplyer;
		uvs[7] = v_offset * yMultiplyer;	
		
		// The texture buffer
		ByteBuffer uvbb = ByteBuffer.allocateDirect(uvs.length * 4);
		uvbb.order(ByteOrder.nativeOrder());
		uvBuffer = uvbb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);
		
	}
	
	public void drawSprite(int mPositionHandle, int mTexCoordLoc, float[] m){
		
		
		 // Set our shader programm
		GLES20.glUseProgram(ShaderTools.sp_Image);
	
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.textureIndex);		
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
               0, this.uvBuffer);
	    
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
		uvs = new float[0];
		textureIndex = 0;	
	}

}
