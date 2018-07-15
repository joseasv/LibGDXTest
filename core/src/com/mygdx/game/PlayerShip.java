package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class PlayerShip {
    private Texture texture;
    private Vector2 position;
    private Animation<TextureRegion> shipAnimation;
    private TextureRegion downFrames[];
    private int animFrame;
    private float velocity;
    private final int SHIP_EVEN = 3;
    private final int SHIP_FULL_ROT_UP = 0;
    private final int SHIP_FULL_ROT_DOWN = 6;
    private final int SHIP_WIDTH = 128;
    private final int SHIP_HEIGTH = 72;

    public PlayerShip(String rutaText, float velocity){
        texture = new Texture(rutaText);
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth(), texture.getHeight()/7);
        downFrames = new TextureRegion[7];
        int index = 0;
        for (int i =0; i < 7; i++){
            for (int j=0; j <1; j++){
                downFrames[index++] = tmp[i][j];
                System.out.println(index);
            }

        }

        //shipAnimation = new Animation<TextureRegion>(0.025f, downFrames);
        animFrame = 3;
        this.velocity = velocity;
        position = new Vector2(200, 200);
    }

    public void move(int xDir, int yDir, float delta){
        position.add(xDir*velocity*delta, yDir*velocity*delta);

        if (yDir == 0){

            if (animFrame > SHIP_EVEN){
                System.out.println(animFrame);
                animFrame--;
            }
            if (animFrame < SHIP_EVEN) {
                System.out.println(animFrame);
                animFrame++;
            }

        } else {
            if (yDir < 0){
                animFrame++;
                if (animFrame >SHIP_FULL_ROT_DOWN){
                    animFrame = SHIP_FULL_ROT_DOWN;
                }
            } else {
                animFrame--;
                if (animFrame <SHIP_FULL_ROT_UP){
                    animFrame = SHIP_FULL_ROT_UP;
                }
            }
        }

    }

    public void draw(SpriteBatch batch){

        batch.draw(downFrames[animFrame], position.x - SHIP_WIDTH/2, position.y-SHIP_HEIGTH/2);

    }

    public void dispose(){
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }
}
