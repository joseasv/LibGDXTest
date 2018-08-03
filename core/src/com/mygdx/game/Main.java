package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;


public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private ModelBatch modelBatch;

	private ShapeRenderer shapeRenderer;
	private Environment environment;
	private AssetManager assets;
	private boolean loading;
	private Array<ModelInstance> escenarioIns = new Array<ModelInstance>();

	private PerspectiveCamera cam2;
	private PlayerShip ship;

	private InputController input;
	private BulletManager bulletManager;
	private EnemyManager enemyManager;

	private float physicsUpdateSpeed = 1/60f;
	private float accumulator = 0;
	private float backgroundLast = 0f;

	private boolean isRunning;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		ship = new PlayerShip("sprites.png", 400);

		input = new InputController();

		bulletManager = new BulletManager();
		enemyManager = new EnemyManager(bulletManager);
		enemyManager.generateEnemy();

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
		isRunning = true;

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
		if (loading && assets.update())
			doneLoading();
		else{
			accumulator += Gdx.graphics.getRawDeltaTime();
			//Gdx.app.log("Main", "rawDeltaTime " + Gdx.graphics.getRawDeltaTime());
			// Esperando a que el modelo 3D se cargue en memoria
			if (loading && assets.update())
				doneLoading();

			//Gdx.gl.glClearColor(0.2F, 0, 0, 1);

			//Gdx.app.log("Main", "accumulator " + accumulator);

			input.readInput();

			int currentUpdateLoop = 0;
			//Gdx.app.log("Main", "entrando al bucle " + accumulator);
			while (accumulator >= physicsUpdateSpeed){
				//Gdx.app.log("Main", "updating " + accumulator);
				update(physicsUpdateSpeed);
				accumulator -= physicsUpdateSpeed;
				currentUpdateLoop++;
			}

			//Gdx.app.log("Main", "saliendo del bucle " + accumulator);
			//update(Gdx.graphics.getDeltaTime());
			draw();
		}




	}



	private void update(float delta) {

		//float delta = Gdx.graphics.getDeltaTime();

		ship.move(input.getxDirNow(), input.getyDirNow(), delta);

		if (input.isShooting()){
			Vector2 bulletPos = ship.getPosition().cpy();
			bulletPos.add(ship.getTexture().getWidth()/2, 0);
			bulletManager.firePlayerBullet(bulletPos);
		}

		enemyManager.update(delta);

		bulletManager.update(delta);

		CollisionManager.checkColPlayerEnemies(ship, bulletManager, enemyManager);
		CollisionManager.checkColBulletEnemies(bulletManager, enemyManager);

		Vector3 pos = new Vector3();
		if (escenarioIns.size > 0){
			escenarioIns.get(0).transform.getTranslation(pos);
		}


		if (escenarioIns.size> 0 && pos.x > -20){
			//ector3 pos = new Vector3();
			//escenarioIns.get(0).transform.getTranslation(pos);
			//Gdx.app.log("Main:", "pos.x " + pos.x);
			//float nextValue = Interpolation.smoother.apply(backgroundLast,-2f * delta, 1);
			//float nextValue = Interpolation.smoother.apply(backgroundLast);
			//Gdx.app.log("Main: ", "diferencia " + (backgroundLast - nextValue));
			//Gdx.app.log("Main:", "nextValue " + nextValue);

			escenarioIns.get(0).transform.trn(-2f * delta,0,0);
			//backgroundLast = -10f * delta;

		}
	}

	private void draw() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam2);
		modelBatch.render(escenarioIns, environment);
		modelBatch.end();

		batch.begin();
		bulletManager.draw(batch);
		ship.draw(batch);
		enemyManager.draw(batch);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		enemyManager.drawDebug(shapeRenderer);
		ship.drawDebug(shapeRenderer);
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		ship.dispose();
		bulletManager.dispose();
		enemyManager.dispose();
		modelBatch.dispose();
		escenarioIns.clear();
		assets.dispose();
	}
}
