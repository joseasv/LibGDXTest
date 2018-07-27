package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PlayerBullet implements Pool.Poolable {
    private Texture texture;
    private Vector2 position;
    private float velocity;
    private boolean active;
    private Rectangle hitBox;

    public PlayerBullet(String textPath){
        position = new Vector2();
        texture = new Texture(textPath);
        velocity = 1000;
        active = false;
        hitBox = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void init(Vector2 newPos){
        position.set(newPos);
        active = true;
    }

    public void move(float delta){
        position.add(velocity*delta, 0);
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

    @Override
    public void reset() {
        position.set(0,0);
        active = false;
    }

    public Vector2 getPosition() {
        return position;
    }
}
