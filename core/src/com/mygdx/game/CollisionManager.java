package com.mygdx.game;

import com.badlogic.gdx.math.Intersector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class CollisionManager {
    public static void checkColPlayerEnemies(PlayerShip ship, BulletManager bulletManager, EnemyManager enemyManager){
        List<Bullet> bullets = bulletManager.getActiveEnemyBullets();
        for(int i =bullets.size() - 1; i >=0; i--){
            Bullet bullet = bullets.get(i);
            if (Intersector.overlaps(ship.getHitBox(), bullet.getHitBox())){
                ship.setAlive(false);
                bulletManager.recycleEnemyBullet(bullet);
            }
        }

        if (ship.isAlive()){
            List<Enemy> enemies = enemyManager.getActiveEnemies();
            for (int i = enemies.size() - 1; i >= 0; i--){
                Enemy enemy = enemies.get(i);
                if (Intersector.overlaps(ship.getHitBox(), enemy.getHitBox())){
                    ship.setAlive(false);
                }
            }
        }


    }

    public static void checkColBulletEnemies(BulletManager bulletManager, EnemyManager enemyManager){
        List<Enemy> enemies = enemyManager.getActiveEnemies();
        List<Bullet> bullets = bulletManager.getActiveBullets();

        for(int i = bullets.size() - 1; i>=0; i--){
            Bullet bullet = bullets.get(i);
            for (int j = enemies.size() - 1; j >= 0;j--){
                Enemy enemy = enemies.get(j);
                if (Intersector.overlaps(enemy.getHitBox(), bullet.getHitBox())){
                    bulletManager.recyclePlayerBullet(bullet);
                    enemyManager.enemyKilled(enemy);
                }
            }
        }
    }

    public static boolean checkCollision(Bullet bullet, Enemy enemy){
        if (Intersector.overlaps(enemy.getHitBox(), bullet.getHitBox())){

            return true;
        }

        return  false;
    }

    public static boolean checkCollision(Bullet bullet, PlayerShip ship){
        if (Intersector.overlaps(ship.getHitBox(), bullet.getHitBox())){

            return true;
        }

        return  false;
    }

    public static boolean checkCollision(PlayerShip ship, Enemy enemy){
        if (Intersector.overlaps(ship.getHitBox(), enemy.getHitBox())){
            return true;
        }

        return false;
    }
}
