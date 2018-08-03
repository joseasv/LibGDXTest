package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Texture texture;
    private Vector2 position;
    private Circle hitBox;
    private int dir;
    private float velocity;
    private float hitBoxRadius;
    private float width;
    private float height;
    private float timer = 0;
    private boolean alive = true;

    public Enemy(String rutaText, float velocity){
        texture = new Texture(rutaText);
        width = texture.getWidth();
        height = texture.getHeight();
        position = new Vector2();
        hitBoxRadius = 20;
        hitBox = new Circle(position, hitBoxRadius);
        this.velocity = velocity;

        dir = 1;
    }

    public void init(float x, float y){
        position.set(x, y);
    }

    public void move(float delta, BulletManager bulletManager){
        timer += delta;

        if (timer > 0.5){
            Vector2 bulletPos = position.cpy();
            bulletPos.add(-width /2, 0);
            bulletManager.fireEnemyBullet(bulletPos);
            timer = 0;
        }

        position.add(velocity*-1*delta, 0);
        /*position.add(0, velocity*dir*delta);

        if (position.y + height/2 > Gdx.graphics.getHeight() || position.y - height/2 < 0){
            dir = -dir;
        }*/
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

    public Vector2 getPosition() {
        return position;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void dispose(){
        texture.dispose();
    }

    public void killed(){
        alive = false;

    }
}
