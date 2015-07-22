package com.lanzivision.opengles2engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.android.openglES2Text.GLText;
import com.android.openglES2Text.TextureHelper;



import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GLRenderer implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Declare sprite and texture vars
	public SSSprite logoiconSS;
	public SSTexture logoSpriteSheet;

	public Texture logo;
	public Sprite  logoSprite;
	
	// Our screen resolution
	float	mScreenWidth;
	float	mScreenHeight;

	// A GLText Instance
	private GLText glText;                             

	
	// Misc
	Context mContext;
	long mLastTime;
	int mProgram;
	
	public GLRenderer(Context c, int width, int height)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
		
		mScreenWidth = width;
		mScreenHeight = height;

	}
	
	public void onPause()
	{
		/* Pause app assets when needed */
	}
	
	public void onResume()
	{
		/* Resume app assets when needed */
		mLastTime = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Get the current time
    	long now = System.currentTimeMillis();
    	
    	// We should make sure we are valid and sane
    	if (mLastTime > now) return;
        
    	// Get the amount of time the last frame took.
    	long delta = now - mLastTime;
		
		// Update our scene
    	updateScene((float)delta/ 1000);
		
		// Render our example
		render(mtrxProjectionAndView);
		
		// Save the current time to see how long it took :).
        mLastTime = now;
		
	}
	
	private void render(float[] m) {
		
		System.out.println(GLES20.GL_TEXTURE0 );
		System.out.println(GLES20.GL_TEXTURE1 );

		// Clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Get handle to vertex shader's vPosition member
	    int mPositionHandle = GLES20.glGetAttribLocation(ShaderTools.sp_Image, "vPosition");
	    
	    // Enable generic vertex attribute array
	    GLES20.glEnableVertexAttribArray(mPositionHandle); 
	    
	    // Get handle to texture coordinates location
	    int mTexCoordLoc = GLES20.glGetAttribLocation(ShaderTools.sp_Image, "a_texCoord" );
	    
	    // Enable generic vertex attribute array
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	    
	    // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ShaderTools.sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
        
        // Draw all sprites before drawing GLtext
        drawSprites(mPositionHandle, mTexCoordLoc, m);
             
        glText.begin( 0.90f, 0.90f, 0.90f, 1.0f, mtrxProjectionAndView );  
      	glText.draw( "OpenGL Activity", (float)(mScreenWidth * .42), (float)(mScreenHeight * .9) ); 
      	glText.end();
      		        
      		
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
      
	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);
		
		// Clear our matrices
	    for(int i=0;i<16;i++)
	    {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
	    
	    // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
        
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		
		// Set the clear color to black
		GLES20.glClearColor(0.20f, 0.20f, 0.20f, 1);	

	    // Create the shaders, solid color
	    int vertexShader = ShaderTools.loadShader(GLES20.GL_VERTEX_SHADER, ShaderTools.vs_SolidColor);
	    int fragmentShader = ShaderTools.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderTools.fs_SolidColor);

	    ShaderTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(ShaderTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(ShaderTools.sp_SolidColor);                  // creates OpenGL ES program executables
	    
	    // Create the shaders, images
	    vertexShader = ShaderTools.loadShader(GLES20.GL_VERTEX_SHADER, ShaderTools.vs_Image);
	    fragmentShader = ShaderTools.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderTools.fs_Image);

	    ShaderTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(ShaderTools.sp_Image, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(ShaderTools.sp_Image, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(ShaderTools.sp_Image);                  // creates OpenGL ES program executables
	    
	    // Enable OpenGL transparency blending
	    GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	    
	    // Set our shader programm
		GLES20.glUseProgram(ShaderTools.sp_Image);
		
		// Create the GLText
		glText = new GLText(mContext.getAssets());

		// Load the font from file (set size + padding), creates the texture		
		glText.load( "LinLibertine_DR.ttf", 36, 2, 2 );  

		setupTextures();

			
	}

	
	public void processTouchEvent(MotionEvent event)
	{
		
		float touchX = event.getX() / mScreenWidth;
		float touchY = event.getY() / mScreenHeight;
		
		
		if(touchY < .7)
		{
			// Translate sprite to touch location
			logoSprite.translateSprite(touchX * mScreenWidth, mScreenHeight - touchY * mScreenHeight);
		}
		else
		{
			// Switch activity while preserving OpenGL Context
			Intent intent = new Intent(mContext,ActivityTwo.class);
			mContext.startActivity(intent);
			this.onPause();

		}
	}
	
	public void updateScene(float delta)
	{
		/* Update scene according to delta time */
		
		
	}
	
	
	public void setupTextures()
	{
		// Load texture and associated sprite
		logo = new Texture(true, 1, "drawable/logo", mContext);
		// Sprite(location X, location Y, bind width, bind height, app context) 
		logoSprite = new Sprite(mScreenWidth/2,mScreenHeight/2, 600,259, mContext);

		// Load sprite sheet and an associated sprite sheet sprite
		// SSTexture(filename, cell count X, cell count Y, app context )
		logoSpriteSheet = new SSTexture("drawable/logospritesheet250x250", 2, 2, mContext);
		// SSSprite(location X, location Y, bind width, bind height, cell X, cell Y, app context) 
		logoiconSS = new SSSprite(mScreenWidth / 2 ,mScreenHeight/6, 250,250, logoSpriteSheet, 0, 0, mContext);

	}
	
	public void drawSprites(int mPositionHandle, int mTexCoordLoc, float [] m)
	{
		 // Draw sprites
        logoSprite.drawSprite(mPositionHandle, mTexCoordLoc, m, logo);
        logoiconSS.drawSprite(mPositionHandle, mTexCoordLoc, m);

	}
	
	
	
}
	
	
