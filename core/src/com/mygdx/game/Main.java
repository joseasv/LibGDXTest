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
	private OrthographicCamera cam;
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

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(30, 30 * (h / w));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

		cam.update();

		cam2 = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam2.position.set(0f, 5f, 3f);
		cam2.lookAt(0,0,-10);
		cam2.near = 1f;
		cam2.far = 300f;
		cam2.update();

		camController = new CameraInputController(cam2);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();
		assets.load("nave.g3db", Model.class);
		assets.load("escena.g3db", Model.class);
		loading = true;


		listaBalas = new ArrayList<Sprite>();

		ModelBuilder modelBuilder = new ModelBuilder();
		bala3D = modelBuilder.createCapsule(1f,2,3, new Material(ColorAttribute.createDiffuse(Color.YELLOW)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Model enemigo3D = modelBuilder.createBox(2f, 2f, 2f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		enemigo3DIns = new ModelInstance(enemigo3D);
		Vector3 enemigoPos = new Vector3(15,15,-20);
		enemigo3DIns.transform.translate(enemigoPos);
		enemigo3DIns.transform.rotate(1,1,1,20);
		eneActivo = true;
		eneBB = new BoundingBox();
		enemigo3DIns.calculateBoundingBox(eneBB);
		eneBB.mul(new Matrix4(enemigoPos, new Quaternion().idt(), new Vector3(1,1,1)));
		//eneBB.set(eneBB.min.add(enemigoPos), eneBB.max.add(enemigoPos));
		System.out.println("eneBB " + enemigoPos + " " +  eneBB);

	}

	private void doneLoading(){
		Model ship = assets.get("nave.g3db", Model.class);
		ModelInstance shipInstance = new ModelInstance(ship);
		//shipInstance.transform.setToScaling(0.8f,0.8f,0.8f);
		shipInstance.transform.translate(5,10, -20);
		shipInstance.transform.scale(0.6f,0.6f,0.6f);
		shipInstance.transform.rotate(0,1,0,90);

		Model scene = assets.get("escena.g3db", Model.class);
		sceneInstance = new ModelInstance(scene);
		sceneInstance.transform.translate(-20,0, -5);
		sceneInstance.transform.rotate(0,1,0,90);

		instances.add(shipInstance);
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
			instances.get(0).transform.trn(velNave.x/8 * delta, 0,0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.A)){
			instances.get(0).transform.trn(-velNave.x/8 * delta, 0,0);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			instances.get(0).transform.trn(0,-velNave.y/8 * delta,0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)){
			instances.get(0).transform.trn(0,velNave.y/8 * delta,0);
		} else {

		}



		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){

			ModelInstance balaIns = new ModelInstance(bala3D);
			Vector3 shipPos = new Vector3();
			instances.get(0).transform.getTranslation(shipPos);
			balaIns.transform.setToTranslation(shipPos);

			balaSphere.add(new Sphere(shipPos, 1));

			balaInstances.add(balaIns);
		}


		if (balaInstances.size != 0){

			for (int i=0; i < balaInstances.size; i++){
				ModelInstance bala = balaInstances.get(i);
				BoundingBox balaBB = new BoundingBox();
				Vector3 balaPos = new Vector3();

				bala.calculateBoundingBox(balaBB);
				bala.transform.trn(25/8,0,0);
				bala.transform.getTranslation(balaPos);
				balaBB.mul(new Matrix4(balaPos, new Quaternion().idt(), new Vector3(1,1,1)));
				//balaBB.set(balaBB.min.add(balaPos), balaBB.max.add(balaPos));

				if (balaBB.intersects(eneBB)){

					eneActivo = false;

				}
			}
		}

		if (eneActivo){
			enemigo3DIns.transform.rotate(Vector3.Y, 5);
		}

		if (escenarioIns.size> 0){
			escenarioIns.get(0).transform.trn(2f * delta,0,0);
		}

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.render(balaInstances, environment);
		if (eneActivo){
			modelBatch.render(enemigo3DIns, environment);
		}
		modelBatch.end();


		modelBatch.begin(cam2);
		modelBatch.render(escenarioIns, environment);
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
		escenarioIns.clear();
		balaInstances.clear();
		assets.dispose();
	}
}
