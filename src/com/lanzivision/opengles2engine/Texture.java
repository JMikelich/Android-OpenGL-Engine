package com.lanzivision.opengles2engine;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.android.openglES2Text.TextureHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/*
 * Texture loads up a texture bitmap with openGL and binds the matrix.
 */
public class Texture {
	public boolean isLoaded;
	public int textureIndex;
	public int[] textureArray;
	public  float uvs[];
	public FloatBuffer uvBuffer;
	
	public Texture(boolean b, int textureIndexTemp, String textureName, Context mContext) {
		
	super();
	this.isLoaded = b;
		
	//this.textureIndex = textureIndexTemp;
		
		
	uvs = new float[] {
		0.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,			
		1.0f, 0.0f			
	};
		
	// The texture buffer
	ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
	bb.order(ByteOrder.nativeOrder());
	uvBuffer = bb.asFloatBuffer();
	uvBuffer.put(uvs);
	uvBuffer.position(0);
				
	// Check that texture exists
	int id;
	File file = new File(textureName);
	id = mContext.getResources().getIdentifier(textureName, null, mContext.getPackageName());
			
	// Temporary create a bitmap
	Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
		
	this.textureArray = TextureHelper.loadTextureWithRelease(bmp);
        this.textureIndex = textureArray[0];
        
        // We are done using the bitmap so we should recycle it.
	bmp.recycle();
		
	}
	
	public void resetTexture()
	{
	this.isLoaded = false;		
	this.textureIndex = 0;
		
	GLES20.glDeleteTextures(1, this.textureArray, 0);
		
	this.uvs = new float[0];
	this.textureArray = new int[0];
		
	//freeNative(this.uvBuffer);
	}

}
