package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

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
	private OrthographicCamera cam;
	private CameraInputController camController;
	private int rot;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("vicviper1.png");
		spriteNave = new Sprite(img);
		spriteNave.setPosition(200,200);
		spriteNave.scale(2);
		velNave = new Vector2(400,400);
		acNave = new Vector2();

		Texture texEnemigo = new Texture("enemigo1.png");
		spriteEnemigo = new Sprite(texEnemigo);
		spriteEnemigo.setPosition(400, 200);
		spriteEnemigo.scale(2);

		texBala = new Texture("bala1.png");

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new OrthographicCamera(30, 30 * (800 / 600));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

		cam.update();

		/*cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(6f, 6f, 6f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();*/



		assets = new AssetManager();
		assets.load("nave.g3db", Model.class);
		loading = true;


		listaBalas = new ArrayList<Sprite>();
	}

	private void doneLoading(){
		Model ship = assets.get("nave.g3db", Model.class);
		ModelInstance shipInstance = new ModelInstance(ship);
		shipInstance.transform.scale(0.5f,0.5f,0.5f);
		shipInstance.transform.translate(30,30, -20);

		shipInstance.transform.rotate(0,1,0,90);


		instances.add(shipInstance);
		loading = false;
	}

	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();


		//Gdx.gl.glClearColor(0.2F, 0, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			spriteNave.translateX(velNave.x * delta);


			instances.get(0).transform.trn(velNave.x/6 * delta, 0,0);


		} else if (Gdx.input.isKeyPressed(Input.Keys.A)){
			spriteNave.translateX(-velNave.x* delta);

			instances.get(0).transform.trn(-velNave.x/6 * delta, 0,0);

		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			spriteNave.translateY(-velNave.y* delta);
			instances.get(0).transform.trn(0,-velNave.x/6 * delta,0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)){
			spriteNave.translateY(velNave.y* delta);
			instances.get(0).transform.trn(0,velNave.x/6 * delta,0);
		}



		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			Sprite spriteBala = new Sprite(texBala);
			spriteBala.scale(2);
			spriteBala.setPosition(spriteNave.getBoundingRectangle().getX(), spriteNave.getBoundingRectangle().getY() );
			listaBalas.add(spriteBala);
		}

		batch.begin();
		if (!listaBalas.isEmpty()){
			for (Sprite bala: listaBalas
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

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			rot += 1;

			if (rot > 360){
				rot = 0;
			}
			System.out.println("rotando " + rot);
			instances.get(0).transform.rotate(0, 1, 0, rot);
		}

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}
}
