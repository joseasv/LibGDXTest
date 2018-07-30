package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private Vector2 direction;
    private float velocity;
    private boolean active;
    private Rectangle hitBox;

    public Bullet(String textPath){
        position = new Vector2();
        direction = new Vector2();
        texture = new Texture(textPath);
        velocity = 1000;
        active = false;
        hitBox = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void init(Vector2 newPos, Vector2 direction){
        position.set(newPos);
        this.direction.set(direction);
        active = true;
    }

    public void init(Vector2 newPos, int xDirection){
        position.set(newPos);
        direction.set(xDirection, 0);
        active = true;
    }

    public void move(float delta){
        position.add(velocity*direction.x*delta, velocity*direction.y*delta);
        //Gdx.app.log("PlayerBullet: ", "position " + position);
        if (position.x > Gdx.graphics.getWidth()){
            //Gdx.app.log("PlayerBullet: ", "salio de la pantalla");
            //Gdx.app.log("PlayerBullet: ", "position.y " + position.x);
            //Gdx.app.log("PlayerBullet: ", "pantalla " + Gdx.graphics.getWidth());
            active = false;
        }
    }

    public void draw(SpriteBatch batch){
        if (active){
            batch.draw(texture, position.x - texture.getWidth()/2, position.y-texture.getHeight()/2);
        }

    }

    public Rectangle getHitBox() {
        hitBox.setPosition(position.x - texture.getWidth()/2, position.y - texture.getHeight()/2);
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector2 getPosition() {
        return position;
    }
}
