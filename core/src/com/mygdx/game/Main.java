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
import com.badlogic.gdx.math.MathUtils;
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
	private Array<ModelInstance> balaInstances = new Array<ModelInstance>();
	private OrthographicCamera cam;
	private CameraInputController camController;
	private Model enemigo3D;
	private int rot;
	private Model bala3D;
	private ModelInstance enemigo3DIns;

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

		ModelBuilder modelBuilder = new ModelBuilder();
		bala3D = modelBuilder.createCapsule(1f,2,3, new Material(ColorAttribute.createDiffuse(Color.YELLOW)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		enemigo3D = modelBuilder.createBox(5f, 5f, 5f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position |VertexAttributes.Usage.Normal);

		enemigo3DIns = new ModelInstance(enemigo3D);
		enemigo3DIns.transform.translate(15,15,-20);
		enemigo3DIns.transform.rotate(1,1,1,20);
	}

	private void doneLoading(){
		Model ship = assets.get("nave.g3db", Model.class);
		ModelInstance shipInstance = new ModelInstance(ship);
		shipInstance.transform.scale(0.5f,0.5f,0.5f);
		shipInstance.transform.translate(10,30, -20);

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



			instances.get(0).transform.trn(velNave.x/8 * delta, 0,0);


		} else if (Gdx.input.isKeyPressed(Input.Keys.A)){


			instances.get(0).transform.trn(-velNave.x/8 * delta, 0,0);

		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){

			instances.get(0).transform.trn(0,-velNave.y/8 * delta,0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)){

			instances.get(0).transform.trn(0,velNave.y/8 * delta,0);
		}



		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){

			ModelInstance balaIns = new ModelInstance(bala3D);
			Vector3 shipPos = new Vector3();
			instances.get(0).transform.getTranslation(shipPos);
			balaIns.transform.translate(shipPos);
			balaInstances.add(balaIns);



		}


		if (balaInstances.size != 0){

			for (ModelInstance balaIns : balaInstances){
				balaIns.transform.trn(25/8,0,0);
			}
		}





		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.render(balaInstances, environment);
		//modelBatch.render(enemigo3DIns, environment);
		modelBatch.end();
	}

	private void checkColission(){

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
