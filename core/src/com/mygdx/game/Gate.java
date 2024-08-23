package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Gate {
    public int targetMapIndex; // Index of the map to switch to
    public Rectangle bounds; // Position and size of the gate

    public Gate(float x, float y, float width, float height, int targetMapIndex) {
        this.bounds = new Rectangle(x, y, width, height);
        this.targetMapIndex = targetMapIndex;
    }

    public boolean checkCollision(float charX, float charY, float charWidth, float charHeight) {
        return bounds.overlaps(new Rectangle(charX, charY, charWidth, charHeight));
    }
}
