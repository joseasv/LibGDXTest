package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Queue;


public class Main extends ApplicationAdapter {
	private SpriteBatch batch;




	private ModelBatch modelBatch;
	private ShapeRenderer shapeRenderer;
	private Environment environment;
	private AssetManager assets;
	private boolean loading;
	private Array<ModelInstance> escenarioIns = new Array<ModelInstance>();
	private Array<Sprite> listaBalas2 = new Array<Sprite>();

	private PerspectiveCamera cam2;
	private PlayerShip ship;
	private Enemy enemy;
	private InputController input;
	private final Queue<PlayerBullet> bullets = new Queue<PlayerBullet>(10);

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		ship = new PlayerShip("sprites.png", 400);
		enemy = new Enemy("enemigo1.png");
		input = new InputController();

		for (int i = 0; i < 10; i++){
			bullets.addLast(new PlayerBullet("bala1.png"));

		}
		Gdx.app.log("Main", "size bullet pool: " + bullets.size);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam2 = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam2.position.set(0f, 5f, 3f);
		cam2.lookAt(0,0,-10);
		cam2.near = 1f;
		cam2.far = 300f;
		cam2.update();

		assets = new AssetManager();
		assets.load("escena.g3db", Model.class);
		loading = true;

	}

	private void doneLoading(){


		Model scene = assets.get("escena.g3db", Model.class);
		ModelInstance sceneInstance = new ModelInstance(scene);
		sceneInstance.transform.translate(20,0, -5);
		sceneInstance.transform.rotate(0,1,0,90);

		escenarioIns.add(sceneInstance);
		loading = false;
	}

	@Override
	public void render () {
		// Esperando a que el modelo 3D se cargue en memoria
		if (loading && assets.update())
			doneLoading();

		//Gdx.gl.glClearColor(0.2F, 0, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		input.readInput();

		ship.move(input.getxDirNow(), input.getyDirNow(), delta);

		if (input.isShooting()){
			if (!bullets.first().isActive()){
				PlayerBullet newBullet = bullets.removeFirst();
				newBullet.init(ship.getPosition());
				bullets.addLast(newBullet);
			}

		}

		if (escenarioIns.size> 0){
			escenarioIns.get(0).transform.trn(-2f * delta,0,0);
		}

		modelBatch.begin(cam2);
		modelBatch.render(escenarioIns, environment);
		modelBatch.end();

		batch.begin();

		for(int i=bullets.size - 1; i >= 0; i--){
			PlayerBullet playerBullet = bullets.get(i);
			if(playerBullet.isActive()){
				playerBullet.move(delta);
				playerBullet.draw(batch);
				if (Intersector.overlaps(enemy.getHitBox(), playerBullet.getHitBox())){
					enemy.setAlive(false);
					playerBullet.setActive(false);
				}
			} else {
				PlayerBullet removedBullet = bullets.removeIndex(i);
				bullets.addFirst(removedBullet);
			}
		}

		ship.draw(batch);
		enemy.draw(batch);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		enemy.drawDebug(shapeRenderer);
		shapeRenderer.end();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		ship.dispose();
		enemy.dispose();
		modelBatch.dispose();
		escenarioIns.clear();
		assets.dispose();
	}
}
