/**
 * Awkward Salami Games
 * MCE
 * 12/01/2011
 */
package com.salami.awkward.mobile.control.experiment;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Wrapper around body with a display image for a piece of wall
 * @author David/Tim
 *
 */
public class Ground extends AnimatedSprite implements Entity{

	
	static private BitmapTextureAtlas mBitmapTextureAtlas;
	
	//tile sizes
	static public int TILE_WIDTH=32;
	static public int TILE_HEIGHT=32;
	
	//Default starting positions
	private static final float START_X_POSITION = 1;
	private static final float START_Y_POSITION = 1;
	private static final float PX_TO_M =PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	
	private Body mBody;
	 
	//Friction and restitution constants. Feel free to play around with these.
	//Increasing friction makes things less slidy
	//Increasing restitution makes things more bouncy, but really low 
	//restitutions can make the player get stuck
	private static final float TOP_FRICTION = 6f;
	private static final float TOP_RESTITUTION = 0f;
	
	private static final float SIDE_FRICTION = 0f;
	private static final float SIDE_RESTITUTION = 0f;
	
		
	/**
	 * Creates a Ground 
	 * @param activity
	 * @param world
	 * @param xPosition
	 * @param yPosition
	 * @return
	 */
	public static Ground create_ground(BaseGameActivity activity, PhysicsWorld world,float xPosition, float yPosition, int width, int height) {
		//Make sure everything is loaded
		onLoadResources(activity);
		
		TiledTextureRegion displayTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, activity, "ground_tiled.png", 0, 0,1,1);
		displayTexture.setWidth(width/TILE_WIDTH*displayTexture.getWidth());
		displayTexture.setHeight(height/TILE_HEIGHT*displayTexture.getHeight());
		
		return new Ground(world,xPosition, yPosition,width,height,displayTexture);
	}
		
	/**
	 * Creates the ground. Called from create_ground.
	 * @param world
	 */
	private Ground(PhysicsWorld world,float xPosition, float yPosition, int width, int height, TiledTextureRegion displayTexture){
		super(xPosition, yPosition, width, height, displayTexture);

		//build the body and the primary fixture
		FixtureDef topFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, TOP_FRICTION);
		topFixtureDef.restitution=TOP_RESTITUTION;
		mBody = PhysicsFactory.createBoxBody(world, this, BodyType.StaticBody, topFixtureDef);
		
		/*
		 * Build another fixture for the sides, since they have different physics constants
		 */
		PolygonShape ps = new PolygonShape();
		FixtureDef sideFixtureDef = PhysicsFactory.createFixtureDef(1,0.5f, SIDE_FRICTION);
		sideFixtureDef.restitution=SIDE_RESTITUTION;
		
		
		//the 0.01 offsets are a gross kludge so these trigger for the sides
		//but not the top -- works better than single fixture for each edge.
		ps.setAsBox((width/PX_TO_M)*(TILE_WIDTH/PX_TO_M)/2 + 0.01f,(height/PX_TO_M)*(TILE_HEIGHT/PX_TO_M)/2 - 0.01f);
		sideFixtureDef.shape=ps;
		mBody.createFixture(sideFixtureDef);

		world.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
		
		this.animate(new long[]{200,200}, 0, 1, true);
		mBody.setUserData(this);		
	}
		
	public static void onLoadResources(BaseGameActivity activity){
		mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		activity.getEngine().getTextureManager().loadTexture(mBitmapTextureAtlas);
	}
		
	@Override
	public void onManagedUpdate(float pSecondsElapsed){
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	/*
	 * Direct gets and sets to Body properties. I have a feeling we might accumulate a lot of these...
	 * 
	 */
	public Vector2 getLinearVelocity(){
		return mBody.getLinearVelocity();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GROUND_ENTITY;
	}

	@Override
	public void onCollide(Fixture other, Contact contact) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSeparate(Fixture other, Contact contact) {
		// TODO Auto-generated method stub
		
	}
	
}
