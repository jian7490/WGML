package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	float x;
    float y;
    float height;
    float width;
    float health;
    boolean breakable;
    boolean attacked;
    Color col = Color.WHITE;
	
    public Enemy(float x, float y, float height, float width, float health, boolean breakable) {
    	this.x = x;
    	this.y = y;
    	this.height = height;
    	this.width = width;
    	this.health = health;
    	this.breakable = breakable;
    	this.attacked = false;
    }

    public void collision(Map map) {
        ArrayList<Block> blocks = map.getBlockList();
        for (Block blk:blocks) {
            if (new Rectangle(x, y, width, height).overlaps(new Rectangle(blk.x, blk.y, blk.width, blk.height))) {

                float deltaX1 = blk.x - (x + width); // Move left
                float deltaX2 = (blk.x + blk.width) - x; // Move right
                float deltaY1 = blk.y - (y + height); // Move down
                float deltaY2 = (blk.y + blk.height) - y; // Move up


                float minDeltaX = Math.abs(deltaX1) < Math.abs(deltaX2) ? deltaX1 : deltaX2;
                float minDeltaY = Math.abs(deltaY1) < Math.abs(deltaY2) ? deltaY1 : deltaY2;

                if (Math.abs(minDeltaX) < 15) {
                    x += minDeltaX;
                }
                if (Math.abs(minDeltaY) < 15) {
                    y += minDeltaY;
                }
            }
        }
    }

    public boolean collision(Projectile p) {
        float x2, y2, xn, yn;
        x2 = x + width;
        y2 = y + height;

        xn = Math.max(x, Math.min(x2, p.x));
        yn = Math.max(y, Math.min(y2, p.y));

        if (new Vector2(xn, yn).dst(p.x, p.y) <= p.radius) {
            return true;
        }
        return false;
    }

    public void update(Map map) {
        ArrayList<Projectile> projectiles = map.getProjectileList();
        Character character = map.getCharacter();
        for (Projectile p:projectiles) {
            if (collision(p) && p.getGroup() != 1) {
                attacked = true;
                health--;
            }
        }
        if (health > 1) {
            move(character);
            collision(map);
            attack(map);
        }
    }

    public void draw(ShapeRenderer shape) {
        if (attacked) {
            shape.setColor(Color.RED);
            attacked = false;
        }

        shape.rect(x, y, width, height);
    }

    public float getHealth() {
        return health;
    }

    public void move(Character c){
        float x_diff = c.x - x;
        float y_diff = c.y - y;

        if (Math.abs(x_diff) > Math.abs(y_diff)) {
            x += x_diff/(Math.abs(x_diff));
        }
        else if(x_diff != 0 && y_diff != 0) {
            y += y_diff/(Math.abs(y_diff));
        }
    }

    public void attack(Map map) {
        Character c = map.getCharacter();

        float x_diff = c.x + (c.width/2) - x - (width/2);
        float y_diff = c.y + (c.height/2) - y - (height/2);

        float xSpeed = (float) (10*x_diff/Math.sqrt(x_diff*x_diff + y_diff*y_diff));
        float ySpeed = (float) (10*y_diff/Math.sqrt(x_diff*x_diff + y_diff*y_diff));;

        map.addProjectile(x + width/2, y + height/2, 20, xSpeed, -ySpeed, 1, 1);
    }
    
	
}
