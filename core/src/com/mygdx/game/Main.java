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
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;


public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private Texture texBala;
	private Sprite spriteNave;
	private float velNave, velBala;
	private Sprite spriteEnemigo;

	private ModelBatch modelBatch;
	private Environment environment;
	private AssetManager assets;
	private boolean loading;
	private Array<ModelInstance> escenarioIns = new Array<ModelInstance>();
	private Array<Sprite> listaBalas2 = new Array<Sprite>();

	private PerspectiveCamera cam2;
	private Texture texEnemigo;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("nave.png");
		spriteNave = new Sprite(img);
		spriteNave.setPosition(200,200);
		velNave = 400;
		velBala = 1500;

		texEnemigo = new Texture("enemigo1.png");
		spriteEnemigo = new Sprite(texEnemigo);
		spriteEnemigo.setPosition(400, 200);
		spriteEnemigo.scale(2);

		texBala = new Texture("bala1.png");

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

		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			spriteNave.translateX(velNave * delta);

		} else if (Gdx.input.isKeyPressed(Input.Keys.A)){
			spriteNave.translateX(-velNave * delta);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			spriteNave.translateY(-velNave* delta);

		} else if (Gdx.input.isKeyPressed(Input.Keys.W)){

			spriteNave.translateY(velNave* delta);
		}


		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			Sprite spriteBala = new Sprite(texBala);
			spriteBala.scale(2);
			spriteBala.setPosition(spriteNave.getBoundingRectangle().getX() + spriteNave.getWidth() / 2, spriteNave.getBoundingRectangle().getY() + spriteNave.getHeight() / 2 );
			listaBalas2.add(spriteBala);
		}

		if (escenarioIns.size> 0){
			escenarioIns.get(0).transform.trn(-2f * delta,0,0);
		}

		modelBatch.begin(cam2);
		modelBatch.render(escenarioIns, environment);
		modelBatch.end();

		batch.begin();
		if (listaBalas2.size != 0){
			for (Sprite bala: listaBalas2
					) {
				bala.translateX(velBala * delta);
				bala.draw(batch);

				if (Intersector.overlaps(spriteEnemigo.getBoundingRectangle(), bala.getBoundingRectangle())){
					spriteEnemigo.setAlpha(0);
				}
			}
		}

		spriteNave.draw(batch);

			spriteEnemigo.draw(batch);


		batch.end();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		texEnemigo.dispose();
		modelBatch.dispose();
		escenarioIns.clear();
		assets.dispose();
	}
}
