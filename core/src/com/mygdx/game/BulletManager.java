package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {

    private final List<Bullet> activeBullets = new ArrayList<Bullet>(10);
    private final Queue<Bullet> inactiveBullets = new Queue<Bullet>(20);
    private final Queue<Bullet> inactiveEnemyBullets = new Queue<Bullet>(10);
    private final List<Bullet> activeEnemyBullets = new ArrayList<Bullet>(5);

    public BulletManager(){
        /*for (int i = 0; i < 10; i++){
            bullets.addLast(new PlayerBullet("bala2.png"));
        }*/

        for (int i=0; i < 20; i++){
            inactiveBullets.addFirst(new Bullet("bala2.png"));
            Gdx.app.log("Main", "size bullet pool: " + inactiveBullets.size);
        }

        for (int i=0; i < 10; i++){
            inactiveEnemyBullets.addFirst(new Bullet("bala2.png"));
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
            Bullet newBullet = inactiveBullets.removeFirst();
            newBullet.init(bulletPosition, 1);
            activeBullets.add(newBullet);
            Gdx.app.log("BulletManager", "size inactivebullet pool: " + inactiveBullets.size);
            Gdx.app.log("BulletManager", "size activebullet pool: " + activeBullets.size());
        }
    }

    public void fireEnemyBullet(Vector2 bulletPosition){
        if (inactiveEnemyBullets.size > 0){
            Bullet newBullet = inactiveEnemyBullets.removeFirst();
            newBullet.init(bulletPosition, -1);
            activeEnemyBullets.add(newBullet);
            Gdx.app.log("BulletManager", "size inactivebullet pool: " + inactiveBullets.size);
            Gdx.app.log("BulletManager", "size activebullet pool: " + activeBullets.size());
        }

    }

    public void update(float delta, Enemy enemy){

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
            Bullet Bullet = activeBullets.get(i);
            Bullet.move(delta);
            if (Intersector.overlaps(enemy.getHitBox(), Bullet.getHitBox())){
                activeBullets.remove(Bullet);
                enemy.setAlive(false);
                inactiveBullets.addFirst(Bullet);
            } else {
                if (Bullet.getPosition().x > Gdx.graphics.getWidth()){
                    activeBullets.remove(Bullet);
                    inactiveBullets.addFirst(Bullet);
                }
            }
        }

        for (int i = activeEnemyBullets.size() - 1; i >= 0; i--){
            Bullet Bullet = activeEnemyBullets.get(i);
            Bullet.move(delta);

            if (Bullet.getPosition().x > Gdx.graphics.getWidth() || Bullet.getPosition().x < 0){
                activeEnemyBullets.remove(Bullet);
                inactiveEnemyBullets.addFirst(Bullet);
            }

        }
    }

    public void draw(SpriteBatch batch){
        for (int i = 0; i < activeBullets.size() ; i++){
            Bullet bullet = activeBullets.get(i);
            bullet.draw(batch);
        }

        for (int i = 0; i < activeEnemyBullets.size() ; i++){
            Bullet bullet = activeEnemyBullets.get(i);
            bullet.draw(batch);
        }
    }

    public void render(){

    }
}
