package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Block {
    float x;
    float y;
    float height;
    float width;
    boolean breakable;
    
    public Block(float x, float y, float height, float width, boolean breakable) {
    	this.x = x;
    	this.y = y;
    	this.height = height;
    	this.width = width;
    	this.breakable = breakable;
    }
    
    public void draw(ShapeRenderer shape){
    	shape.rect(x, y, width, height);
    }
    
    public boolean getBreakable() {
    	return breakable;
    }

}
