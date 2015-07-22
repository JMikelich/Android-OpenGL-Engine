package com.lanzivision.opengles2engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurf extends GLSurfaceView {

	private final GLRenderer mRenderer;
	
	public GLSurf(Context context, int width, int height) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context, width, height);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
    	}

	@Override
	public void onPause() {
		super.onPause();

        this.setPreserveEGLContextOnPause(true);
		mRenderer.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mRenderer.onResume();
	}
	
	
	@Override
    public boolean onTouchEvent(MotionEvent e) {
		mRenderer.processTouchEvent(e);
		return true;
	}

}
