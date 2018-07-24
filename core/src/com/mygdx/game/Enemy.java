package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Texture texture;
    private Vector2 position;
    private Circle hitBox;
    private float hitBoxRadius;
    private float width;
    private float height;
    private boolean alive = true;

    public Enemy(String rutaText){
        texture = new Texture(rutaText);
        width = texture.getWidth();
        height = texture.getHeight();
        position = new Vector2(400,200);
        hitBoxRadius = 20;
        hitBox = new Circle(position, hitBoxRadius);

    }

    public void draw(SpriteBatch batch){
        if (alive){
            batch.draw(texture, position.x - width/2, position.y - height/2);
        }
    }

    public void drawDebug(ShapeRenderer renderer){
        if (alive){
            renderer.circle(position.x, position.y, hitBoxRadius);
        }
    }

    public Circle getHitBox() {
        hitBox.setPosition(position.x, position.y);
        return hitBox;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void dispose(){
        texture.dispose();
    }
}
