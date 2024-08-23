package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    float x;
    float y;
    float width;
    float height;
    float xSpeed;
    float ySpeed;
    private static final int SPACECOOLDOWNFRAMES = 300; // 300 frames cooldown for roll
    private static final int ROLLFRAMES = 30; // 30 frames rolling
    private static final int SHOOTCOOLDOWNFRAMES = 45; // 45 frames cooldown for shoot
    private boolean isRolling = false;
    private int rollFramesRemaining = 0;
    private int spaceCooldownFramesRemaining = 0;
    private int shootCooldownFramesRemaining = 0;

    public Character(float x, float y, float width, float height, float xSpeed, float ySpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update(Map map) {
        move();
        roll();
        shoot(map);
        collision(map);
    }

    private int coolDown(int framesRemaining) {
        if (framesRemaining > 0) {
            return framesRemaining - 1; // Decrease cooldown
        }
        return 0; // Cooldown finished
    }

    public void move() {
        if (!isRolling) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                x += xSpeed * deltaTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                x -= xSpeed * deltaTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                y += ySpeed * deltaTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                y -= ySpeed * deltaTime;
            }
        }
    }

    public void roll() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (isRolling) {
            if (rollFramesRemaining > 0) {
                rollFramesRemaining--;
                float rollSpeed = 600 * deltaTime; // Adjust roll speed as needed
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    x += rollSpeed;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    x -= rollSpeed;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    y += rollSpeed;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    y -= rollSpeed;
                }
            } else {
                isRolling = false;
                spaceCooldownFramesRemaining = SPACECOOLDOWNFRAMES; // Reset cooldown
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && spaceCooldownFramesRemaining == 0) {
            startRolling();
        }

        // Handle cooldown decrement
        if (!isRolling) {
            spaceCooldownFramesRemaining = coolDown(spaceCooldownFramesRemaining);
        }
    }

    private void startRolling() {
        isRolling = true;
        rollFramesRemaining = ROLLFRAMES; // Roll for 60 frames
    }

    public void shoot(Map map) {
        shootCooldownFramesRemaining = coolDown(shootCooldownFramesRemaining);
        if (shootCooldownFramesRemaining == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                map.addProjectile(x + width + 50, y + height / 2, 50, 4, 0, 2, 0);
                shootCooldownFramesRemaining = SHOOTCOOLDOWNFRAMES; // Reset cooldown
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                map.addProjectile(x - 50, y + height / 2, 50, -4, 0, 2, 0);
                shootCooldownFramesRemaining = SHOOTCOOLDOWNFRAMES; // Reset cooldown
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                map.addProjectile(x + width / 2, y + height + 50, 50, 0, -4, 2, 0);
                shootCooldownFramesRemaining = SHOOTCOOLDOWNFRAMES; // Reset cooldown
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                map.addProjectile(x + width / 2, y - 50, 50, 0, 4, 2, 0);
                shootCooldownFramesRemaining = SHOOTCOOLDOWNFRAMES; // Reset cooldown
            }
        }
    }

    public void collision(Map map) {
        if (x < 0) {
            x = 0;
        }
        if (x > 1920 - width) {
            x = 1920 - width;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > 1080 - height) {
            y = 1080 - height;
        }

        ArrayList<Block> blocks = map.getBlockList();
        ArrayList<Enemy> enemies = map.getEnemyList();

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

        for (Enemy e:enemies) {
            if (e.getHealth() > 0 && new Rectangle(x, y, width, height).overlaps(new Rectangle(e.x, e.y, e.width, e.height))) {
                float deltaX1 = e.x - (x + width); // Move left
                float deltaX2 = (e.x + e.width) - x; // Move right
                float deltaY1 = e.y - (y + height); // Move down
                float deltaY2 = (e.y + e.height) - y; // Move up

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

    public void checkGateCollision(Map map, WGML game) {
        if (new Rectangle(x, y, width, height).overlaps(map.getTopGate().orElse(new Rectangle()))) {
            game.switchMap(game.currentMapIndex1 - 1, game.currentMapIndex2); // Switch to top map
            this.x = 910;
            this.y = 60;
        } else if (new Rectangle(x, y, width, height).overlaps(map.getBottomGate().orElse(new Rectangle()))) {
            game.switchMap(game.currentMapIndex1 + 1, game.currentMapIndex2); // Switch to bottom map
            this.x = 910;
            this.y = 920;
        } else if (new Rectangle(x, y, width, height).overlaps(map.getLeftGate().orElse(new Rectangle()))) {
            game.switchMap(game.currentMapIndex1, game.currentMapIndex2 - 1); // Switch to left map
            this.x = 1760;
            this.y = 515;
        } else if (new Rectangle(x, y, width, height).overlaps(map.getRightGate().orElse(new Rectangle()))) {
            game.switchMap(game.currentMapIndex1, game.currentMapIndex2 + 1); // Switch to right map
            this.x = 60;
            this.y = 515;
        }
    }


    public void draw(ShapeRenderer shape){
        shape.rect(x, y, width, height);
    }

}