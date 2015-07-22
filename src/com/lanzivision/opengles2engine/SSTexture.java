package com.lanzivision.opengles2engine;

import com.android.openglES2Text.TextureHelper;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

/*
 * SSTexture stands for SpriteSheet texture. This class solely loads up a texture bitmap with openGL and stores the pointer. 
 * The binder coords are left up to the sprite. 
 */
public class SSTexture {

	// Saved openGL pointer for loaded texture
	public int textureIndex;
	public int[] textureArray;

	public boolean isLoaded;
	
	// SpriteSheet layout (for SSSprite's UVS)
	public int maxCellsY;
	public int maxCellsX;
	
	
	public SSTexture(String textureName, int maxX, int maxY, Context mContext) {
		super();
		
		this.isLoaded = true;
		
		this.maxCellsX = maxX;
		this.maxCellsY = maxY;
		
		int id = mContext.getResources().getIdentifier(textureName, null, mContext.getPackageName());
		// Temporary create a bitmap
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
		
		this.textureArray = TextureHelper.loadTextureWithRelease(bmp);
        this.textureIndex = this.textureArray[0];
        
        // We are done using the bitmap so we should recycle it.
		bmp.recycle();
	}
	
	public void resetTexture()
	{
		this.isLoaded = false;
		
		this.maxCellsX = 0;
		this.maxCellsY = 0;
		
		this.textureIndex = 0;
		
		//GLES20.glDeleteTextures(1, this.textureIndex);
		GLES20.glDeleteTextures(1, this.textureArray, 0);
		
		this.textureArray = new int[0];

	}

}
