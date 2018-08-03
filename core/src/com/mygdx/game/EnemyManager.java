package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    private final List<Enemy> activeEnemies = new ArrayList<Enemy>(10);
    private final Queue<Enemy> inactiveEnemies = new Queue<Enemy>(10);
    private BulletManager bulletManager;

    public EnemyManager(BulletManager bulletManager){
        this.bulletManager = bulletManager;

        for (int i=0; i < 10; i++){
            inactiveEnemies.addFirst(new Enemy("enemigo2.png", 300));
        }


    }

    public void generateEnemy(){
        if (inactiveEnemies.size > 0){
            Enemy enemy = inactiveEnemies.removeFirst();
            activeEnemies.add(enemy);
            enemy.init(400, 200);
        }
    }

    public void update(float delta){
        for (int i=activeEnemies.size() - 1; i >= 0; i--){
            Enemy enemy = activeEnemies.get(i);
            enemy.move(delta, bulletManager);

        }
    }

    public void enemyKilled(Enemy enemy){
        enemy.setAlive(false);
        activeEnemies.remove(enemy);
        inactiveEnemies.addLast(enemy);
    }

    public void draw(SpriteBatch batch){
        for(int i=0; i < activeEnemies.size(); i++){
            activeEnemies.get(i).draw(batch);
        }
    }

    public void drawDebug(ShapeRenderer renderer){
        for(int i=0; i < activeEnemies.size(); i++){
            activeEnemies.get(i).drawDebug(renderer);
        }
    }

    public void dispose(){
        while(inactiveEnemies.size > 0){
            inactiveEnemies.removeFirst().dispose();
        }

        for (int i=0; i < activeEnemies.size(); i++){
            activeEnemies.get(i).dispose();
        }
    }

    public List<Enemy> getActiveEnemies() {
        return activeEnemies;
    }
}
