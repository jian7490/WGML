package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile {
	
	float x, y;
	float radius;
	float xSpeed;
	float ySpeed;
	float health;
	int group; // projectile will not damage objects in the same group
	Body body;

	public Projectile(World world, float x, float y, float radius, float xSpeed, float ySpeed, float health, int group){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.health = health;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.group = group;

		// Define body and fixture
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		body = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution = 1.0f; // Perfectly elastic collision

		body.createFixture(fixtureDef);
		shape.dispose();

		body.setLinearVelocity(xSpeed, ySpeed);
	}
	
	public boolean collision(Block block) {
		float x1, x2, y1, y2, xn, yn;
		x1 = block.x;
		y1 = block.y;
		x2 = block.x + block.width;
		y2 = block.y + block.height;
		
		xn = Math.max(x1, Math.min(x2, x));
		yn = Math.max(y1, Math.min(y2, y));
		
		if (new Vector2(xn, yn).dst(x, y) <= radius) {
			return true;
		}
		return false;
	}

	public boolean collision(Triangle triangle) {
		Polygon polygon = new Polygon(triangle.vertices);
		return polygon.contains(x, y);
	}
	
	public boolean collision(Enemy enemy) {
		float x1, x2, y1, y2, xn, yn;
		x1 = enemy.x;
		y1 = enemy.y;
		x2 = enemy.x + enemy.width;
		y2 = enemy.y + enemy.height;
		
		xn = Math.max(x1, Math.min(x2, x));
		yn = Math.max(y1, Math.min(y2, y));
		
		if (new Vector2(xn, yn).dst(x, y) <= radius) {
			return true;
		}
		return false;
	}

	public void update(Map map) {
		ArrayList<Block> blocks = map.getBlockList();
		ArrayList<Triangle> triangles = map.getTriangleList();
		ArrayList<Enemy> enemies = map.getEnemyList();

		for (Block blk : blocks) {
			if (collision(blk)) {
				xSpeed = -xSpeed;
				ySpeed = -ySpeed;
				health -= 1;
			}
		}

		for (Triangle tri : triangles) {
			if (collision(tri)) {
				xSpeed = -xSpeed;
				ySpeed = -ySpeed;
				health -= 1;
			}
		}

		for (Enemy e : enemies) {
			if (collision(e) && e.getHealth() > 0) {
				xSpeed = -xSpeed;
				ySpeed = -ySpeed;
				health -= 1;
			}
		}
		
		x += xSpeed;
		y -= ySpeed;
		
		if (x - radius < 0 || x + radius > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
            health -= 1;
        }
        if (y - radius < 0 || y + radius > Gdx.graphics.getHeight()) {
        	ySpeed = -ySpeed;
            health -= 1;
        }
        
	}
	
	public float getHealth() {
		return health;
	}

	public void decreaseHealth() {
		this.health = this.health >= 1 ? this.health - 1 : 0;
	}

	public int getGroup() {
		return group;
	}
	
	public void draw(ShapeRenderer shape) {
		shape.circle(x, y, radius);
	}
	
	
}
