// Sartaj Sidhu Final Project Mario remake


package com.mygdx.game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	Texture map1,map2,map3,map4,maskT1,maskT2,maskT3,maskT4,coinimg,menu,ins,playimg,ctrlimg,lost,won; //all images 
	Pixmap mask1,mask2,mask3,mask4; 
	Pixmap[] masks; // array of masks for easier access
	ArrayList<Goomba> goombas; // goombas arraylist
	ArrayList<Coin> coins,coins2,coins3; // coins arraylists 
	ArrayList<Float> gExes,cExes,cWise,cExes2,cWise2,cExes3;
	Rectangle runrect,walkrect,standrect,jumprect,portal,brect,downrect;
	Texture[] jumpR,runR,standR,walkR,fireR,standL,jumpL,runL,walkL,goombs,bowsrR,bowsrL,bowsrD,bowsrDL;
	Animation <Texture> goombawalk,jumpingR,runningR,walkingR,standingR,fireballR,runningL,walkingL,jumpingL,standingL,bowserR,bowserL,bowserD,bowserDL;
	HashMap<Integer, Animation<Texture>> sprites, Bsprites;
	Mario mario;
	Bowser bowser; //bowser obj
	Goomba g;
	Coin c,c2,c3;
	ShapeRenderer sr;
	float elapsedTime;
	int red,red2,red3,red4;
	OrthographicCamera cam;
	public float velocity = 0f; //velocity to be added to the Y for jumping
	public float gravity = 0; // gravity to be subtracted from the velocity for falling down
	public static int gameMode = 0; //variable for active game mode (main menu, level 1, level 2, level 3)
	public static int action = 1; //variable for current movement mario is using
	public static int Baction =1; // var for current movement Bowser is using
	public static int dir = 0; //variable for direction (0 is right , 1 is left)
	public static int Bdir = 1; // var for bowser direction
	public static int curMask =0; // var for index of masks
	
	public int cnum(int r, int g, int b, int a){
		return (r<<24) + (g<<16) + (b<<8) + a;
	}
	public Animation<Texture> createAnimation(Texture[] list,String name,float frames){ // takes in a texture array, string , and the frames and creates an animation
		for(int i = 0; i<list.length;i++){
			Texture curText = new Texture(name+i+".png");
			list[i]=curText;
		}
		Animation<Texture> x = new Animation<Texture>(frames,list);
		return x;
	}

	@Override
	public void create () {
		font = new BitmapFont();  //font used for words at top of screen 
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 1024,768);
		cam.update();
		Gdx.graphics.setWindowedMode(1024, 768);
		sr = new ShapeRenderer();
		batch = new SpriteBatch();
		coinimg = new Texture("coin.png");
		playimg = new Texture("play.png");
		ctrlimg=new Texture("control.png");
		lost = new Texture("L.png");
		won = new Texture("W.png");
		menu = new Texture("bg.png");
		ins = new Texture("ctrl.png");
		map1 = new Texture("level1.png");
		mask1 = new Pixmap(Gdx.files.internal("mask1.png"));
		map2 = new Texture("level2.png");
		mask2 = new Pixmap(Gdx.files.internal("mask2.png"));
		map3 = new Texture("level3.png");
		mask3 = new Pixmap(Gdx.files.internal("mask3.png"));
		map4 = new Texture("level4.png");
		mask4 = new Pixmap(Gdx.files.internal("mask4.png"));
		red = mask1.getPixel(5, mask1.getHeight()-4); // the red for the ground and other collisions 
		red2= mask2.getPixel(5, mask2.getHeight()-4); // the red for the ground and other collisions 
		red3= mask3.getPixel(5, mask3.getHeight()-4); // the red for the ground and other collisions 
		red4= mask4.getPixel(5, mask4.getHeight()-4); // the red for the ground and other collisions 
		
		// texture arrays to be used with animation method
		jumpR = new Texture[1];
		runR = new Texture[7];
		walkR = new Texture[2];
		standR = new Texture[26];
		fireR = new Texture[3];
		standL = new Texture[26];
		runL = new Texture[7];
		walkL = new Texture[2];
		jumpL = new Texture[1];
		goombs = new Texture[3];
		bowsrD = new Texture[1];
		bowsrR = new Texture[16];
		bowsrL = new Texture[16];
		bowsrDL = new Texture[1];
		
		//pixmap array 
		masks = new Pixmap[4];
		masks[0]= mask1;
		masks[1]= mask2;
		masks[2]= mask3;
		masks[3]= mask4;
		
		// right sprites
		standingR = createAnimation(standR,"Standing/Standing",0.15f); // action 1
		walkingR = createAnimation(walkR,"Walking/Walking",0.08f);    // action 2
		runningR = createAnimation(runR,"Run/Run",0.08f);	           // action 3
		jumpingR = createAnimation(jumpR,"Jumping/Jumping",0.25f);     // action 4

		// left sprites
		standingL = createAnimation(standL,"StandingL/StandingL",0.15f);// action -1
		walkingL = createAnimation(walkL,"WalkL/WalkL",0.08f);          // action -2
		runningL = createAnimation(runL,"RunL/RunL",0.08f);	            // action -3
		jumpingL = createAnimation(jumpL,"JumpingL/JumpingL",0.08f);    // action -4
		
		//goomba sprite
		goombawalk = createAnimation(goombs,"goomba/goomba",0.08f);
		
		//bowser sprites
		bowserR = createAnimation(bowsrR,"BowserR/BowserR",0.01f);
		bowserL = createAnimation(bowsrL,"BowserL/BowserL",0.01f);
		bowserD = createAnimation(bowsrD,"BowserD/BowserD",0.08f);
		bowserDL = createAnimation(bowsrDL,"BowserDL/BowserDL",0.08f);
		
		//hashmap of all animations to easily switch through them
		sprites = new HashMap<Integer, Animation<Texture>>(9);
		
		// adding all animations
		sprites.put(1, standingR); //image 8 is wrong
		sprites.put(2, walkingR);
		sprites.put(3, runningR);
		sprites.put(4, jumpingR);
		sprites.put(-1, standingL);
		sprites.put(-2, walkingL);
		sprites.put(-3, runningL);
		sprites.put(-4, jumpingL);

		// make mario obj
		mario  = new Mario();
		
		// Bowser hasmap of sprites
		Bsprites = new HashMap<Integer, Animation<Texture>>(4);
		Bsprites.put(1, bowserL);
		Bsprites.put(2, bowserR);
		Bsprites.put(3, bowserD);
		Bsprites.put(4, bowserDL);
		
		//make bowser obj
		bowser = new Bowser();
		
		//Goombas for map two
		gExes = new ArrayList<Float>(Arrays.asList(1250f,1610f,2280f,2716f));
		goombas = new ArrayList<Goomba>();
		for(int i=0;i<gExes.size();i++) {
			g = new Goomba(gExes.get(i),24f,goombs);
			goombas.add(g);
		}
		
		// coins for map one
		coins = new ArrayList<Coin>();
		cExes = new ArrayList<Float>(Arrays.asList(309.5f,291f,541f,561.5f,582f,658f,683.5f,708f,1216f,1234f,1252f,1270f,1288f,1663f,1683f,1703f,1723f,1743f,1763f,1854.5f+3,1874.5f+3,1894.5f+3,1949.5f+3,1969.5f+3,1989.5f+3));
		cWise = new ArrayList<Float>(Arrays.asList(62f,62f,138f,138f,138f,168f,168f,168f,135f,135f,135f,135f,135f,119f,119f,119f,119f,119f,119f,88f,88f,88f,88f,88f,88f));
		for(int i=0;i<cExes.size();i++) {
			c = new Coin(cExes.get(i),cWise.get(i),coinimg);
			coins.add(c);
		}
		
		//coins for map two
		coins2 = new ArrayList<Coin>();
		cExes2 = new ArrayList<Float>(Arrays.asList(1522.5f,1542.5f,1562.5f,1582.5f,1602.5f,1622.5f,1642.5f,1662.5f,1682.5f,1702.5f,1722.5f,1742.5f,1762.5f,1782.5f,1802.5f,1822.5f,1283.5f,1303.5f,1323.5f,1343.5f,1363.5f,1383.5f,457f,618f,746f,922f,322.5f,353.5f,384.5f,2698f,2716f));
		cWise2 = new ArrayList<Float>(Arrays.asList(25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,25f,152f,152f,152f,152f,152f,152f,57f,72f,87f,87f,88f,88f,88f,88f,88f));
		for(int i=0;i<cExes2.size();i++) {
			c2 = new Coin(cExes2.get(i),cWise2.get(i),coinimg);
			coins2.add(c2);
		}
		
		//coins for map 3
		coins3 = new ArrayList<Coin>();
		cExes3 = new ArrayList<Float>(Arrays.asList(50f,75f,100f,125f,150f,175f,200f,225f,250f,275f,300f,325f,350f,375f,400f,425f));
		for(int i=0;i<cExes3.size();i++) {
			c3 = new Coin(cExes3.get(i),26,coinimg);
			coins3.add(c3);
		}
		
	}
	// moving all goombas method 
	public void moveGoomba(Goomba goomb){
		goomb.setX(goomb.getX()+goomb.getVX());
		if(goomb.getX()>goomb.getOX()+24f){
			goomb.setVX(-1);
		}
		if(goomb.getX()<goomb.getOX()-40f){
			goomb.setVX(-1);
		}
	}
	// puts mario back at start 
	public void reset() {
		mario.x=28.5f;
		mario.y=24f;
		mario.oy=24f;
	}
	// movement functions 
	public void WalkRight(){
		dir = 0;
		action=2;
		mario.x+=mario.speedW;
		mario.hitbox=walkrect;
	}
	public void WalkLeft(){
		dir = 1; //change direction to left
		action = -2; //change action to walking left
		mario.x-=mario.speedW;
		mario.hitbox=walkrect;
	}
	public void RunRight(){
		dir = 0;
		action=3;
		mario.x+=mario.speedR;
		mario.hitbox=runrect;
	}
	public void RunLeft(){
		dir = 1;
		action = -3;
		mario.x-=mario.speedR;
		mario.hitbox=runrect;
	}
	
	//checking if mario will touch something 
	public boolean checkR(){
			if(red != masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y+mario.y/2))){
				return false;
			}
		return true;
	}
	public boolean checkL(){
		if(red != masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y+mario.y/2))){
			return false;
		}
		return true;
	}
	
	// all inputs
	public void keys(){
		if(mario.y>0) {
			if (Gdx.input.isKeyPressed(Input.Keys.A) && checkL()==false && mario.x > 28.5f){ //press left
				WalkLeft();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.D) && checkR()==false){
				WalkRight();
			}
			else{
				//idling 
				if(mario.oy==mario.y) {
				if(dir==0){
					action=1;
				}
				else{
					action=-1;
				}
				mario.hitbox=standrect; //changing hitbox based on current action
				}
			}
			if(velocity!=0){
				if(dir==0){
					action=4;
				}
				else{
					action=-4;
				}
				mario.hitbox=jumprect;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyPressed(Input.Keys.D) && checkR()==false) {
				RunRight();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.A) &&Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && checkL()==false && mario.x > 28.5f){
				RunLeft();
			}
			Gdx.input.setInputProcessor(new InputProcessor(){
				@Override
				public boolean keyDown(int keycode) {
					if(keycode==Input.Keys.W){
						if(mario.y==mario.oy){
							velocity = mario.jump; //adding to the velocity 
							gravity = -0.5f; //subtracting from the gravity
						}
					}
					return false;
				}

				@Override
				public boolean keyUp(int keycode) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean keyTyped(char character) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean touchDragged(int screenX, int screenY, int pointer) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean mouseMoved(int screenX, int screenY) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean scrolled(int amount) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
	}
	public void menu(){
		mario.goombaBox.x=mario.x+3; //making enemy kill box (smaller than hitbox) 
		mario.goombaBox.y=mario.y-1;
		mario.goombaBox.width=mario.hitbox.width-2;
		mario.goombaBox.height=2;
		if(mario.hearts==0) { // if mario is dead tell user they lost 
			gameMode=-1;
		}
		if(gameMode==0) {
			// menu
			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			sr.begin(ShapeType.Filled);
			Rectangle play = new Rectangle(200,70,150,150); // play game
			Rectangle controls = new Rectangle(674,70,150,150); //controls
			Rectangle mouse = new Rectangle(Gdx.input.getX(),768 - Gdx.input.getY(),1,1);
	        sr.end();
			batch.begin();
			batch.draw(menu, 0, 1);
	        batch.draw(playimg,play.x,play.y);
	        batch.draw(ctrlimg,controls.x,controls.y);
			batch.end();
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)&& mouse.overlaps(controls)){
	        	batch.begin();
	        	batch.draw(ins,0,5);
	        	batch.end();
	        }
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)&& mouse.overlaps(play)){
				gameMode=1;
	        }
		}
		else if(gameMode==1){
			// level 1
        	curMask =0;
    		sr.begin(ShapeType.Filled);
    		runrect = new Rectangle(mario.x,mario.y,runR[0].getWidth(),runR[0].getHeight());
    		walkrect = new Rectangle(mario.x,mario.y,walkR[0].getWidth(),walkR[0].getHeight());
    		standrect = new Rectangle(mario.x,mario.y,standR[0].getWidth(),standR[0].getHeight());
    		jumprect = new Rectangle(mario.x,mario.y,jumpR[0].getWidth(),jumpR[0].getHeight());
    		sr.end();
    		keys();
    		mario.y+=velocity; //adding to the y value for jumping
    		velocity+=gravity; // subtracting so that mario comes back down
    		if(mario.y==mario.oy){
    			velocity = 0;
    			gravity = 0; // setting the velocity and gravity back to 0 if mario y equals old y
    		}
    		
    		// all mask collides
    		if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y)) || red == masks[curMask].getPixel(Math.round(mario.x),mask1.getHeight()-Math.round(mario.y))  ){
    			mario.oy=mario.y;
    			if(velocity!=0){
    				gravity=0;
    				velocity=0;
    			}
    		}
    		else if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width/2),masks[curMask].getHeight()-Math.round(mario.y+mario.hitbox.height))){
    			velocity=-0.5f;
    			gravity=0;
    		}
    		else if(red != masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y)) || red != masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y))){
    			mario.oy=24f;
    			gravity=-0.5f;
    		}
    		
    		elapsedTime += Gdx.graphics.getDeltaTime();
    		Gdx.gl.glClearColor(0, 0, 0, 0);
    		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    		if(mario.x+510<2117.0f) {
    			cam.position.set(mario.x+510, 768/2, 0);
    		}
    		else{ // if mario reaches near the end of  the level the camera locks
    			cam.position.set(2117.0f,768/2, 0);
    		}
    		cam.update();
    		Animation<Texture> current = sprites.get(action);
    		batch.setProjectionMatrix(cam.combined);
    		batch.begin();
    		batch.draw(map1, 0, 0);
    		// drawing coins
    		for(int i=0;i<coins.size();i++) {
    			batch.draw(coinimg,coins.get(i).getX(),coins.get(i).getY());
    			if(mario.hitbox.overlaps(coins.get(i).getR())==true) {
    				//removing coins and adding to the money earned
    				coins.remove(i);
    				mario.money+=1;
    			}
    		}
    		batch.draw(current.getKeyFrame(elapsedTime,true),mario.x,mario.y);
    		
    		//drawing text
    		font.draw(batch, "Level: 1", mario.x+10, 750);
    		font.draw(batch, "Lives: "+mario.hearts, mario.x+70, 750);
    		font.draw(batch, "Coins: "+mario.money, mario.x+130, 750);
    		batch.end();
    		// if mario falls off he loses a life 
    		if(mario.y+mario.hitbox.y<0){
    			mario.hearts-=1;
    			reset();
    		}
    		// if mario passes the flag next level starts 
    		if(mario.x+mario.hitbox.width>=2430.5f) {
    			reset();
    			gameMode=2;
    		}
        }
        else if(gameMode==2){
        	// level 2
        	curMask =1;
    		sr.begin(ShapeType.Filled);
    		runrect = new Rectangle(mario.x,mario.y,runR[0].getWidth(),runR[0].getHeight());
    		walkrect = new Rectangle(mario.x,mario.y,walkR[0].getWidth(),walkR[0].getHeight());
    		standrect = new Rectangle(mario.x,mario.y,standR[0].getWidth(),standR[0].getHeight());
    		jumprect = new Rectangle(mario.x,mario.y,jumpR[0].getWidth(),jumpR[0].getHeight());
    		sr.end();
    		keys();
    		mario.y+=velocity; //adding to the y value for jumping
    		velocity+=gravity; // subtracting so that mario comes back down
    		if(mario.y==mario.oy){
    			velocity = 0;
    			gravity = 0; // setting the velocity and gravity back to 0 if mario y equals old y
    		}
    		if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y)) || red == masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y))  ){
    			mario.oy=mario.y;
    			if(velocity!=0){
    				gravity=0;
    				velocity=0;
    			}
    		}
    		else if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width/2),masks[curMask].getHeight()-Math.round(mario.y+mario.hitbox.height))){
    			velocity=-0.5f;
    			gravity=0;
    		}
    		else if(red != masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y)) || red != masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y))){
    			mario.oy=24f;
    			gravity=-0.5f;
    		}
    		//drawing goombas 
    		for(int i=0;i<goombas.size();i++){
    			moveGoomba(goombas.get(i));
    			// mario jumps oon goomba and kills it
    			if(mario.goombaBox.overlaps(goombas.get(i).getR())){ 
    				goombas.remove(i);
    			}
    			//goomba touches mario and takes a life
    			else if(mario.hitbox.overlaps(goombas.get(i).getR())) {
    				mario.hearts-=1;
    				reset();
    			}
    		}
    		elapsedTime += Gdx.graphics.getDeltaTime();
    		Gdx.gl.glClearColor(0, 0, 0, 0);
    		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    		if(mario.x+510<2361.5f+510) {
    			cam.position.set(mario.x+510, 768/2, 0);
    		}
    		else{ // if mario reaches near the end of  the level the camera locks
    			cam.position.set(2361.5f+510,768/2, 0);
    		}
    		
    		cam.update();
    		Animation<Texture> current = sprites.get(action);
    		batch.setProjectionMatrix(cam.combined);
    		batch.begin();
    		batch.draw(map2, 0, 0);
    		for(int i=0;i<coins2.size();i++) {
    			batch.draw(coinimg,coins2.get(i).getX(),coins2.get(i).getY());
    			if(mario.hitbox.overlaps(coins2.get(i).getR())) {
    				coins2.remove(i);
    				mario.money+=1;
    			}
    		}
    		batch.draw(current.getKeyFrame(elapsedTime,true),mario.x,mario.y);
    		for(int i=0;i<goombas.size();i++){
    			batch.draw(goombawalk.getKeyFrame(elapsedTime,true),goombas.get(i).getX(),goombas.get(i).getY());
    		}
    		font.draw(batch, "Level 2", mario.x+10, 750);
    		font.draw(batch, "Coins: "+mario.money, mario.x+130, 750);
    		font.draw(batch, "Lives: "+mario.hearts, mario.x+70, 750);
    		batch.end();
    		if(mario.y+mario.hitbox.y<0){
    			mario.hearts-=1;
    			reset();
    		}
    		if(mario.x+mario.hitbox.width>=3171f) {
    			reset();
    			gameMode=3;
    		}
        }
        else if(gameMode==3){
        	// level 3 (explains bowser fight)
        	curMask =2;
    		sr.begin(ShapeType.Filled);
    		portal = new Rectangle(450f,24f,10f,30f);
    		runrect = new Rectangle(mario.x,mario.y,runR[0].getWidth(),runR[0].getHeight());
    		walkrect = new Rectangle(mario.x,mario.y,walkR[0].getWidth(),walkR[0].getHeight());
    		standrect = new Rectangle(mario.x,mario.y,standR[0].getWidth(),standR[0].getHeight());
    		jumprect = new Rectangle(mario.x,mario.y,jumpR[0].getWidth(),jumpR[0].getHeight());
    		sr.end();
    		keys();
    		mario.y+=velocity; //adding to the y value for jumping
    		velocity+=gravity; // subtracting so that mario comes back down
    		if(mario.y==mario.oy){
    			velocity = 0;
    			gravity = 0; // setting the velocity and gravity back to 0 if mario y equals old y
    		}
    		if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y)) || red == masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y))  ){
    			mario.oy=mario.y;
    			if(velocity!=0){
    				gravity=0;
    				velocity=0;
    			}
    		}
    		else if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width/2),masks[curMask].getHeight()-Math.round(mario.y+mario.hitbox.height))){
    			velocity=-0.5f;
    			gravity=0;
    		}
    		else if(red != masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y)) || red != masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y))){
    			mario.oy=24f;
    			gravity=-0.5f;
    		}
    		elapsedTime += Gdx.graphics.getDeltaTime();
    		Gdx.gl.glClearColor(0, 0, 0, 0);
    		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    		cam.position.set(510, 768/2, 0);
    		cam.update();
    		Animation<Texture> current = sprites.get(action);
    		batch.setProjectionMatrix(cam.combined);
    		batch.begin();
    		batch.draw(map3, 0, 0);
    		for(int i=0;i<coins3.size();i++) {
    			batch.draw(coinimg,coins3.get(i).getX(),coins3.get(i).getY());
    			if(mario.hitbox.overlaps(coins3.get(i).getR())) {
    				coins3.remove(i);
    				mario.money+=1;
    			}
    		}
    		batch.draw(current.getKeyFrame(elapsedTime,true),mario.x,mario.y);
    		font.draw(batch, "Level 3",10, 750);
    		font.draw(batch, "Coins: "+mario.money,130, 750);
    		font.draw(batch, "Lives: "+mario.hearts,70, 750);
    		batch.end();
    		if(mario.y+mario.hitbox.y<0){
    			mario.hearts-=1;
    			reset();
    		}
    		if(mario.hitbox.overlaps(portal)) {
    			reset();
    			gameMode=4;
    		}
        }
        else if(gameMode==4){
        	// level 4 bowser fight 
        	curMask =3;
    		sr.begin(ShapeType.Filled);
    		runrect = new Rectangle(mario.x,mario.y,runR[0].getWidth(),runR[0].getHeight());
    		walkrect = new Rectangle(mario.x,mario.y,walkR[0].getWidth(),walkR[0].getHeight());
    		standrect = new Rectangle(mario.x,mario.y,standR[0].getWidth(),standR[0].getHeight());
    		jumprect = new Rectangle(mario.x,mario.y,jumpR[0].getWidth(),jumpR[0].getHeight());
    		brect = new Rectangle(bowser.x,bowser.y,bowsrR[0].getWidth(),bowsrR[0].getHeight());
    		downrect = new Rectangle(bowser.x,bowser.y,bowsrD[0].getWidth(),bowsrD[0].getHeight());
    		sr.end();
    		keys();
    		mario.y+=velocity; //adding to the y value for jumping
    		velocity+=gravity; // subtracting so that mario comes back down
    		if(mario.y==mario.oy){
    			velocity = 0;
    			gravity = 0; // setting the velocity and gravity back to 0 if mario y equals old y
    		}
    		if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y)) || red == masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y))  ){
    			mario.oy=mario.y;
    			if(velocity!=0){
    				gravity=0;
    				velocity=0;
    			}
    		}
    		else if(red == masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width/2),masks[curMask].getHeight()-Math.round(mario.y+mario.hitbox.height))){
    			velocity=-0.5f;
    			gravity=0;
    		}
    		else if(red != masks[curMask].getPixel(Math.round(mario.x),masks[curMask].getHeight()-Math.round(mario.y)) || red != masks[curMask].getPixel(Math.round(mario.x+mario.hitbox.width),masks[curMask].getHeight()-Math.round(mario.y))){
    			mario.oy=24f;
    			gravity=-0.5f;
    		}
    		elapsedTime += Gdx.graphics.getDeltaTime();
    		Gdx.gl.glClearColor(0, 0, 0, 0);
    		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    		cam.position.set(510, 768/2, 0);
    		cam.update();
    		Animation<Texture> current = sprites.get(action);
    		Animation<Texture> Bcurrent = Bsprites.get(Baction);
    		batch.setProjectionMatrix(cam.combined);
    		batch.begin();
    		batch.draw(map4, 0, 0);
    		batch.draw(current.getKeyFrame(elapsedTime,true),mario.x,mario.y);
    		batch.draw(Bcurrent.getKeyFrame(elapsedTime,true),bowser.x,bowser.y);
    		font.draw(batch, "Level 3",10, 750);
    		font.draw(batch, "Coins: "+mario.money,130, 750);
    		font.draw(batch, "Lives: "+mario.hearts,70, 750);
    		batch.end();
    		
    		// make bowser move left to right 
    		bowser.x+=bowser.run;
    		// change direction and subtact an atk 
    		if(Bdir==1 && bowser.x<30) {
    			Bdir=0;
    			bowser.run*=-1;
    			Baction=2;
    			bowser.atk-=1;
    		}
    		// change direction and subtact an atk 
    		if(Bdir==0 && bowser.x>994-bowser.hitbox.width) {
    			Bdir=1;
    			bowser.run*=-1;
    			Baction=1;
    			bowser.atk-=1;
    		}
    		// if no more atks bowser lays down 
    		if(bowser.atk==0) {
    			bowser.run=0;
    			if(Bdir==0) {
    				Baction=3;
    			}
    			else if(Bdir==1) {
    				Baction=4;
    			}
    		}
    		if(mario.y+mario.hitbox.y<0){
    			mario.hearts-=1;
    			reset();
    		}
    		// if mario gets hit by bowser
    		if(mario.hitbox.overlaps(bowser.hitbox)) {
    			mario.hearts-=1;
    			reset();	
    		}
    		//if bowser is laying down and collides with the kill box you win
    		if(mario.goombaBox.overlaps(bowser.hitbox)) {
    			if(Baction==3 || Baction==4) {
    				gameMode = -2; //won the game
    			}
    		}
    		// changing hitbox 
    		if(Baction == 1 || Baction==2) {
    			bowser.hitbox=brect;
    		}
    		else if(Baction == 3 || Baction==4) {
    			bowser.hitbox=downrect;
    		}
        }
        else if(gameMode==-1) {
        	// user lost screen 
        	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			cam.position.set(0, 0, 0);
			cam.update();
			batch.setProjectionMatrix(cam.combined);
        	batch.begin();
			batch.draw(lost,-511.5f,-384,1024,768);
			batch.end();
        }
        else if(gameMode==-2) {
        	// user win screen
        	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			cam.position.set(0, 0, 0);
			cam.update();
			batch.setProjectionMatrix(cam.combined);
        	batch.begin();
			batch.draw(won,-511.5f,-384,1024,768);
			batch.end();
        }
	}
	@Override
	public void render (){
		menu();
	}

	@Override
	public void dispose () {
		batch.dispose();
		map1.dispose();
		map2.dispose();
		map3.dispose();
		map4.dispose();
		mask1.dispose();
		mask2.dispose();
		mask3.dispose();
		mask4.dispose();
		ins.dispose();
		menu.dispose();
		won.dispose();
		lost.dispose();
		ctrlimg.dispose();
		playimg.dispose();
		coinimg.dispose();
	}
}
// mario class
class Mario{
	public int hearts,money;
	public float x,y,jump,speedR,speedW,oy;
	public Rectangle hitbox,goombaBox;
	public Mario(){
		hearts = 3;
		money = 0;
		jump = 8.5f;
		speedW = 0.5f;
		speedR = 1.5f;
		oy = 24.00f;
		x = 28.50f;
		y = 24.00f;
		hitbox= new Rectangle(0,0,0,0);
		goombaBox = new Rectangle(0,0,0,0);
		
	}
}
// goomba class
class Goomba{
	private float x,y,ox,vx;
	private Rectangle hitbox;
	public Goomba(float gx,float gy,Texture[] img){
		vx=1f;
		ox=gx;
		x=gx;
		y=gy;
		hitbox = new Rectangle(x,y,img[0].getWidth(),img[0].getHeight());
	}
	public float getX() {
		return x;
	}
	public float getOX() {
		return ox;
	}
	public float getVX() {
		return vx;
	}
	public void setX(float n) {
		x=n;
	}
	public void setVX(float n) {
		vx*=n;
	}
	public float getY() {
		return y;
	}
	public Rectangle getR() {
		return hitbox;
	}
}
// coin class
class Coin{
	private float x,y;
	private Rectangle hitbox; 
	public Coin(float cx, float cy,Texture img){
		x=cx;
		y=cy;
		hitbox = new Rectangle(x,y,img.getWidth(),img.getHeight());
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public Rectangle getR(){
		return hitbox;
	}
}
// bowser class
class Bowser{
	public float x,y,ox,run;
	public int atk;
	public Rectangle hitbox;
	public Bowser(){
		x = 900f;
		y=24f;
		ox = 900f;
		atk = 4;
		run=-6f;
		hitbox = new Rectangle(0,0,0,0);
	}
}

