package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Triangle {
    float[] vertices;

    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.vertices = new float[]{x1, y1, x2, y2, x3, y3};
    }

    public boolean contains(float x, float y) {
        Polygon polygon = new Polygon(vertices);
        return polygon.contains(x, y);
    }

    public void draw(ShapeRenderer shape) {
        shape.triangle(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5]);
    }
}
