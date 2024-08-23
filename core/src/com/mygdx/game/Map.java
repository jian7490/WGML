package com.mygdx.game;

import java.util.*;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
	ArrayList<Block> blocks;
	ArrayList<Projectile> projectiles;
	ArrayList<Enemy> enemies;
	ArrayList<SpawnPoint> points;
	Character character;
	ArrayList<Triangle> triangles;
	float pointCounter;
	Optional<Rectangle> topGate;
	Optional<Rectangle> bottomGate;
	Optional<Rectangle> leftGate;
	Optional<Rectangle> rightGate;
	World world; // Box2D World
	// Gates

	public Map(World world, Boolean[] gateValidity) {
		blocks = new ArrayList<Block>();
		triangles = new ArrayList<Triangle>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();
		points = new ArrayList<SpawnPoint>();
		pointCounter = 0;
		this.world = world;

		if (gateValidity.length == 4) {
			topGate = gateValidity[0] ?
					Optional.of(new Rectangle(910, 1030, 100, 50)) : Optional.of(new Rectangle());
			bottomGate = gateValidity[1] ?
					Optional.of(new Rectangle(910, 0, 100, 50)) : Optional.of(new Rectangle());
			leftGate = gateValidity[2] ?
					Optional.of(new Rectangle(0, 515, 50, 100)) : Optional.of(new Rectangle());
			rightGate = gateValidity[3] ?
					Optional.of(new Rectangle(1870, 515, 50, 100)) : Optional.of(new Rectangle());
		}

	}

	public void addBlock(float x, float y, float height, float width, boolean breakable) {
		blocks.add(new Block(x, y, height, width, breakable));
	}

	public void addTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		triangles.add(new Triangle(x1, y1, x2, y2, x3, y3));
	}

	public void addProjectile(float x, float y, float radius, float xSpeed, float ySpeed, float health, int group) {
		Projectile projectile = new Projectile(world, x, y, radius, xSpeed, ySpeed, health, group);
		projectiles.add(projectile);
	}

	public void addEnemy(float x, float y, float height, float width, float health, boolean breakable) {
		enemies.add(new Enemy(x, y, height, width, health, breakable));
	}

	public void addCharacter(Character character) {
		this.character = character;
	}

	public void addPoints(float x, float y) {
		points.add(new SpawnPoint(x, y));
		pointCounter++;
	}

	public void draw(ShapeRenderer shape) {
		shape.set(ShapeRenderer.ShapeType.Line);
		for (Block blk : blocks) {
			blk.draw(shape);
		}
		for (Triangle tri : triangles) {
			tri.draw(shape);
		}
		shape.set(ShapeRenderer.ShapeType.Filled);
		for (Projectile p : projectiles) {
			if (p.getHealth() > 0) {
				p.draw(shape);
			}
		}
		for (Enemy e : enemies) {
			if (e.getHealth() > 0) {
				e.draw(shape);
			}
		}
		topGate.ifPresent(x -> shape.rect(x.getX(), x.getY(), x.getWidth(), x.getHeight()));
		bottomGate.ifPresent(x -> shape.rect(x.getX(), x.getY(), x.getWidth(), x.getHeight()));
		leftGate.ifPresent(x -> shape.rect(x.getX(), x.getY(), x.getWidth(), x.getHeight()));
		rightGate.ifPresent(x -> shape.rect(x.getX(), x.getY(), x.getWidth(), x.getHeight()));
	}

	public void generateEnemy(Random rd, float difficulty, float min, float max) {
		float num = 0;
		for (float i = 0; i < pointCounter; i++) {
			if (rd.nextFloat() < difficulty/2) {
				addEnemy(points.get((int) i).x, points.get((int) i).y, 100, 100, 1000, false);
				num++;
			}
			if (!(num < max)) {
				break;
			}
		}
		if (num < min) {
			for (int i = 0; num < min; i++) {
				addEnemy(points.get((int) i).x, points.get((int) i).y, 100, 100, 1000, false);
				num++;
			}
		}
	}

	public ArrayList<Block> getBlockList(){
		return blocks;
	}

	public ArrayList<Triangle> getTriangleList() {
		return triangles;
	}

	public ArrayList<Projectile> getProjectileList(){
		return projectiles;
	}

	public ArrayList<Enemy> getEnemyList(){
		return enemies;
	}

	public Character getCharacter() {
		return character;
	}

	public Optional<Rectangle> getTopGate() {
		return topGate;
	}

	public Optional<Rectangle> getBottomGate() {
		return bottomGate;
	}

	public Optional<Rectangle> getLeftGate() {
		return leftGate;
	}

	public Optional<Rectangle> getRightGate() {
		return rightGate;
	}
}
