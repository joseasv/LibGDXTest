package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BulletManager {
    private final Queue<PlayerBullet> bullets = new Queue<PlayerBullet>(10);
    private final List<PlayerBullet> activeBullets = new ArrayList<PlayerBullet>(10);
    private final Queue<PlayerBullet> inactiveBullets = new Queue<PlayerBullet>(10);

    public BulletManager(){
        for (int i = 0; i < 10; i++){
            bullets.addLast(new PlayerBullet("bala2.png"));

            //Gdx.app.log("Main", "size bullet pool: " + bullets.size);

        }

        for (int i=0; i < 10; i++){
            inactiveBullets.addFirst(new PlayerBullet("bala2.png"));
            Gdx.app.log("Main", "size bullet pool: " + inactiveBullets.size);
        }
    }

    public void firePlayerBullet(Vector2 bulletPosition){


        /*if (!bullets.first().isActive()){
            PlayerBullet newBullet = bullets.removeFirst();
            newBullet.init(bulletPosition);
            bullets.addLast(newBullet);
        }*/

        if (inactiveBullets.size > 0){
            PlayerBullet newBullet = inactiveBullets.removeFirst();
            newBullet.init(bulletPosition);
            activeBullets.add(newBullet);
            Gdx.app.log("BulletManager", "size inactivebullet pool: " + inactiveBullets.size);
            Gdx.app.log("BulletManager", "size activebullet pool: " + activeBullets.size());
        }
    }

    public void updateAndDraw(float delta, Enemy enemy, SpriteBatch batch){
        /*for(int i=bullets.size - 1; i >= 0; i--){
            PlayerBullet playerBullet = bullets.get(i);
            if(playerBullet.isActive()){
                playerBullet.move(delta);
                playerBullet.draw(batch);
                if (Intersector.overlaps(enemy.getHitBox(), playerBullet.getHitBox())){
                    enemy.setAlive(false);
                    playerBullet.setActive(false);
                    bullets.addFirst(playerBullet);
                }
            } else {
                PlayerBullet removedBullet = bullets.removeIndex(i);
                bullets.addFirst(removedBullet);
            }
        }*/

        for (int i = activeBullets.size() - 1; i >= 0; i--){
            PlayerBullet playerBullet = activeBullets.get(i);
            playerBullet.move(delta);
            playerBullet.draw(batch);
            if (Intersector.overlaps(enemy.getHitBox(), playerBullet.getHitBox())){
                activeBullets.remove(playerBullet);
                enemy.setAlive(false);
                inactiveBullets.addFirst(playerBullet);
            } else {
                if (playerBullet.getPosition().x > Gdx.graphics.getWidth()){
                    activeBullets.remove(playerBullet);
                    inactiveBullets.addFirst(playerBullet);
                }
            }


        }
    }

    public void render(){

    }
}
