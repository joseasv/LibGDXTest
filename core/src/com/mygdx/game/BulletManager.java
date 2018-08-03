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
    private float playerCooldown = 0.3f;
    private int playerMaxBullets = 3;
    private int playerCurrentBullets = 0;
    private float playerCurrentCooldown = 0;

    public BulletManager(){

        for (int i=0; i < 20; i++){
            inactiveBullets.addFirst(new Bullet("bala2.png"));
            //Gdx.app.log("Main", "size bullet pool Player: " + inactiveBullets.size);
        }

        for (int i=0; i < 10; i++){
            inactiveEnemyBullets.addFirst(new Bullet("bala2.png"));
            //Gdx.app.log("Main", "size bullet pool Enemy: " + inactiveEnemyBullets.size);
        }
    }

    public void firePlayerBullet(Vector2 bulletPosition, float delta){
        playerCurrentCooldown += delta;
        if (playerCurrentCooldown > playerCooldown || activeBullets.isEmpty()){
            playerCurrentCooldown = 0;
            Bullet newBullet = inactiveBullets.removeFirst();
            newBullet.init(bulletPosition, 1);
            activeBullets.add(newBullet);
            //Gdx.app.log("BulletManager", "size inactivebullet pool: " + inactiveBullets.size);
            //Gdx.app.log("BulletManager", "size activebullet pool: " + activeBullets.size());
        }
    }

    public void fireEnemyBullet(Vector2 bulletPosition){
        if (inactiveEnemyBullets.size > 0){
            Bullet newBullet = inactiveEnemyBullets.removeFirst();
            newBullet.init(bulletPosition, -1);
            activeEnemyBullets.add(newBullet);
            //Gdx.app.log("BulletManager", "size inactivebullet pool: " + inactiveBullets.size);
            //Gdx.app.log("BulletManager", "size activebullet pool: " + activeBullets.size());
        }

    }

    public void update(float delta){

        for (int i = activeBullets.size() - 1; i >= 0; i--){
            Bullet bullet = activeBullets.get(i);
            bullet.move(delta);



            if (bullet.getPosition().x > Gdx.graphics.getWidth()){
                //Gdx.app.log("BulletManager", "bala salio de la pantalla");
                recyclePlayerBullet(bullet, activeBullets, inactiveBullets);
            }

        }

        for (int i = activeEnemyBullets.size() - 1; i >= 0; i--){
            Bullet bullet = activeEnemyBullets.get(i);
            bullet.move(delta);

            if (bullet.getPosition().x > Gdx.graphics.getWidth() || bullet.getPosition().x < 0){
                recycleEnemyBullet(bullet, activeEnemyBullets, inactiveEnemyBullets);
            }

        }
    }

    private void recyclePlayerBullet(Bullet bullet, List<Bullet> activeBullets, Queue<Bullet> inactiveBullets) {
        bullet.setActive(false);
        activeBullets.remove(bullet);
        inactiveBullets.addFirst(bullet);
    }

    private void recycleEnemyBullet(Bullet bullet, List<Bullet> activeEnemyBullets, Queue<Bullet> inactiveEnemyBullets) {
        bullet.setActive(false);
        activeEnemyBullets.remove(bullet);
        inactiveEnemyBullets.addFirst(bullet);
    }

    public void recycleEnemyBullet(Bullet bullet){
        bullet.setActive(false);
        activeEnemyBullets.remove(bullet);
        inactiveEnemyBullets.addFirst(bullet);
    }

    public void recyclePlayerBullet(Bullet bullet){
        bullet.setActive(false);
        activeBullets.remove(bullet);
        inactiveBullets.addFirst(bullet);
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

    public List<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public void dispose(){
        while(inactiveEnemyBullets.size>0){
            inactiveEnemyBullets.removeFirst().dispose();
        }

        while(inactiveBullets.size > 0){
            inactiveBullets.removeFirst().dispose();
        }

        for(int i=0;i < activeEnemyBullets.size(); i++){
            activeEnemyBullets.get(i).dispose();
        }

        for(int i=0;i < activeBullets.size(); i++){
            activeBullets.get(i).dispose();
        }
    }
}
