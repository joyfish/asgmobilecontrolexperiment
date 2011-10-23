package com.salami.awkward.mobile.control.experiment;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import android.view.MotionEvent;

public class VirtualControlScheme implements IControlScheme, IOnSceneTouchListener{
	
	private Hero mHero;
	private Scene mScene;
	private Engine mEngine;
	
	private TextureRegion mBase;
	private TextureRegion mKnob;	
	private TextureRegion mButtonBase;
	private TiledTextureRegion mButton;
	private ITouchArea mTouch;
	
	private TiledSprite buttonTile;
	
	private int mCameraWidth;
	private int mCameraHeight;
	

	public VirtualControlScheme(Hero hero, Scene scene, Engine engine, TextureRegion base, TextureRegion knob, TiledTextureRegion button, int cameraWidth, int cameraHeight) {
		mHero = hero;
		mScene = scene;
		mEngine = engine;
		mBase = base;
		mKnob = knob;
		mButton = button;
		mCameraWidth=cameraWidth;
		mCameraHeight=cameraHeight;
		initOnScreenControls();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerListeners(Scene scene, BaseGameActivity activity) {
		scene.setOnSceneTouchListener(this);
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getX() >= 285 + mEngine.getCamera().getMinX() && pSceneTouchEvent.getY() >= 165 + mEngine.getCamera().getMinY()){
		
			if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
				buttonTile.setCurrentTileIndex(1);
				mHero.jump();
				return true;
			}
		
			else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
				buttonTile.setCurrentTileIndex(0);
				return false;
			}
		
		}

	return false;
	
	}
	
	
	void initOnScreenControls() {
			
		buttonTile = new TiledSprite(mCameraWidth-75, mCameraHeight-75, mButton);
		mTouch = buttonTile;
		
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, mCameraHeight - mBase.getHeight(), this.mEngine.getCamera(), mBase, mKnob, 0.1f, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				mHero.move(pValueX);

			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
			}
		});	
		
		analogOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(0.75f);
		analogOnScreenControl.getControlKnob().setScale(0.75f);
		analogOnScreenControl.refreshControlKnobPosition();
		
		buttonTile.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//buttonTile.setAlpha(.5f);
		

		this.mScene.setChildScene(analogOnScreenControl);
		analogOnScreenControl.attachChild(buttonTile);
		
	}

}
