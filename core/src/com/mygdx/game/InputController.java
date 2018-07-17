package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputController {
    private boolean isShooting;
    private int xDirNow;
    private int yDirNow;

    public InputController(){

    }

    public void readInput(){
        xDirNow = 0;
        yDirNow = 0;
        isShooting = false;

        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            xDirNow = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            xDirNow = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            yDirNow = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)){
            yDirNow = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            isShooting = true;
        }
    }

    public int getxDirNow() {
        return xDirNow;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public int getyDirNow() {
        return yDirNow;
    }
}
