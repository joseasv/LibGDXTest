package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.utils.Array;
import com.sun.javafx.geom.transform.Identity;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture texBala;
	Sprite spriteNave;
	Vector2 velNave, acNave;
	Sprite spriteEnemigo;
	List<Sprite> listaBalas;
	private float x = 0;
	private ModelBatch modelBatch;
	private Environment environment;
	private AssetManager assets;
	private boolean loading;
	private Array<ModelInstance> instances = new Array<ModelInstance>();
	private Array<ModelInstance> balaInstances = new Array<ModelInstance>();
	private Array<ModelInstance> escenarioIns = new Array<ModelInstance>();
	private Array<Sphere> balaSphere = new Array<Sphere>();
	private Array<Sprite> listaBalas2 = new Array<Sprite>();

	private CameraInputController camController;

	private Model bala3D;
	private ModelInstance enemigo3DIns;
	private boolean eneActivo;
	private BoundingBox eneBB;
	private PerspectiveCamera cam2;
	private ModelInstance sceneInstance;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("nave.png");
		spriteNave = new Sprite(img);
		spriteNave.setPosition(200,200);
		//spriteNave.scale(2);
		velNave = new Vector2(400,400);
		acNave = new Vector2();


		Texture texEnemigo = new Texture("enemigo1.png");
		spriteEnemigo = new Sprite(texEnemigo);
		spriteEnemigo.setPosition(400, 200);
		spriteEnemigo.scale(2);

		texBala = new Texture("bala1.png");
		listaBalas = new ArrayList<Sprite>();

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();



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
		sceneInstance = new ModelInstance(scene);
		sceneInstance.transform.translate(20,0, -5);
		sceneInstance.transform.rotate(0,1,0,90);


		escenarioIns.add(sceneInstance);
		//instances.add(sceneInstance);
		
		loading = false;
	}

	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();
		//camController.update();

		//Gdx.gl.glClearColor(0.2F, 0, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			spriteNave.translateX(velNave.x * delta);

		} else if (Gdx.input.isKeyPressed(Input.Keys.A)){
			spriteNave.translateX(-velNave.x * delta);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			spriteNave.translateY(-velNave.y* delta);

		} else if (Gdx.input.isKeyPressed(Input.Keys.W)){

			spriteNave.translateY(velNave.y* delta);
		}


		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			Sprite spriteBala = new Sprite(texBala);
			spriteBala.scale(2);
			spriteBala.setPosition(spriteNave.getBoundingRectangle().getX() + spriteNave.getWidth() / 2, spriteNave.getBoundingRectangle().getY() + spriteNave.getHeight() / 2 );
			//listaBalas.add(spriteBala);
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
				bala.translateX(25);
				bala.draw(batch);

				if (spriteEnemigo.getBoundingRectangle().contains(bala.getBoundingRectangle())){
					spriteEnemigo.setAlpha(0);
				}
			}
		}

		spriteNave.draw(batch);
		if (spriteEnemigo != null){
			spriteEnemigo.draw(batch);
		}

		batch.end();



	}

	private void checkColission(){

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		modelBatch.dispose();
		instances.clear();
		escenarioIns.clear();
		balaInstances.clear();
		assets.dispose();
	}
}
