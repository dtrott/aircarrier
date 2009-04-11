package net.java.dev.aircarrier.planes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;
import net.java.dev.aircarrier.scene.ApproximatelySphericalNode;
import net.java.dev.aircarrier.util.SpatialMeshLocker;
import net.java.dev.aircarrier.util.SpatialVBOInfoSetter;
import net.java.dev.aircarrier.util.SpatialWalker;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class SimplePlaneModel implements PlaneModel {

	List<Node> gunPositions;
	List<Node> propPositions;
	List<Node> damagePositions;

	List<Node> wreckage;
	List<Node> wreckageStartPositions;

	List<Node> forwardFlaps;
	List<Node> rearFlaps;

	List<Node> bulletBounds;

	Spatial physicsBounds;

	private Node model;

	ApproximatelySphericalNode avoidanceNode;

	Vector3f cameraOffset;

	public SimplePlaneModel(
			String modelResource,
			String textureResource,
			String wreckageTextureResource,
			float scale,
			boolean mipmap) throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "sphere");
		model = jbr.loadBinaryFormat(SimplePlaneModel.class.getClassLoader()
				.getResourceAsStream(modelResource));

		model.setLocalScale(scale);

		model.updateGeometricState(0, true);

		Texture bodyTexture = TextureManager.loadTexture(ChimeraModel.class
				.getClassLoader().getResource(textureResource),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        if (!mipmap) {
			bodyTexture.setMinificationFilter(Texture.MinificationFilter.BilinearNoMipMaps);
		}

		Texture wreckageTexture = TextureManager.loadTexture(ChimeraModel.class
				.getClassLoader().getResource(wreckageTextureResource),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);

		if (bodyTexture == null) {
			System.err.println("Null plane texture " + textureResource);
		}

		Texture envTexture = TextureManager.loadTexture(SimplePlaneModel.class
				.getClassLoader()
				.getResource("resources/sky_env.jpg"),
            Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		envTexture.setEnvironmentalMapMode(Texture.EnvironmentalMapMode.SphereMap);
		envTexture.setApply(Texture.ApplyMode.Add);

		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.None);

		//System.out.println("===========================");
		//SpatialWalker.printSpatialTree(model);
		//System.out.println("===========================");

		for (Object o : model.getChildren()) {
			//System.out.println(o);
			if (o instanceof Node) {

				Node n = (Node) o;

				TextureState ts = (TextureState) n
						.getRenderState(RenderState.RS_TEXTURE);
				if (ts == null) {
					ts = DisplaySystem.getDisplaySystem().getRenderer()
							.createTextureState();
				}

				// Initialize the texture state
				if (n.getName().startsWith("wreckage")) {
					ts.setTexture(wreckageTexture, 0);
				} else {
					ts.setTexture(bodyTexture, 0);
				}

				// Add shiny environment to shield, cowling and engines
				if (n.getName().startsWith("env")) {
				      ts.setTexture( envTexture, 1 );
				}

				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

				// Add twosided as appropriate
				if (n.getName().indexOf("2S") >=0) {
					n.setRenderState(cullState);
				}
			}
		}

		gunPositions = extractNodes(model, "gun");
		//System.out.println("gun positions " + gunPositions.size());
		propPositions = extractNodes(model, "prop");
		//System.out.println("prop positions " + propPositions.size());
		damagePositions = extractNodes(model, "damage");
		System.out.println("damage positions " + damagePositions.size());

		forwardFlaps = extractNodes(model, "flapf");
		//System.out.println("forward flaps " + forwardFlaps.size());

		rearFlaps = extractNodes(model, "flapr");
		//System.out.println("rear flaps " + rearFlaps.size());

		bulletBounds = extractNodes(model, "bulletBounds");
		//System.out.println("bullet bounds " + bulletBounds.size());

		wreckage = extractNodes(model, "wreckage");
		//System.out.println("wreckage " + wreckage.size());

		//Make new nodes to mark wreckage start positions (where they are now, before we start
		//using them)
		wreckageStartPositions = new ArrayList<Node>();
		for (Node n : wreckage) {
			Node pos = new Node();
			model.attachChild(pos);
			pos.getLocalRotation().set(n.getLocalRotation());
			pos.getLocalTranslation().set(n.getLocalTranslation());
			pos.getLocalScale().set(n.getLocalScale());
			wreckageStartPositions.add(pos);
		}
		//System.out.println("wreckage start positions" + wreckageStartPositions.size());

		//Get the physics bounds
		Spatial s = model.getChild("physicsBounds");
		if (s != null) {
			physicsBounds = s;
			model.detachChild(physicsBounds);
		//Default bounds
		} else {
			physicsBounds = new Box("Plane physics bounds box", new Vector3f(), 3, 1, 2);
		}

        BoundingSphere shootableBounds = new BoundingSphere();

        //Make shootable bounds
        for (Node b : bulletBounds) {
            shootableBounds = (BoundingSphere)shootableBounds.merge(b.getWorldBound());
        }

        //System.out.println(shootableBounds.getCenter() + " radius " + shootableBounds.getRadius());

        //Make sphere for testing (makes avoidance sphere visible)
        /*Sphere sphere = new Sphere("overallbounds", 9, 9, shootableBounds.getRadius());
        model.attachChild(sphere);
        sphere.getLocalTranslation().set(shootableBounds.getCenter());*/

        //Make spherical node for plane to mark out approx area
        ApproximatelySphericalNode sphericalNode = new ApproximatelySphericalNode("Avoidance area", shootableBounds.getRadius());
        model.attachChild(sphericalNode);
        sphericalNode.getLocalTranslation().set(shootableBounds.getCenter());
        avoidanceNode = sphericalNode;

		//Detach wreckage
		for (Node n : wreckage) {
			model.detachChild(n);
		}

		//find camera position
		List<Vector3f> cameraPositions = extractPositions(model, "camera");
		if (cameraPositions.size() < 1) throw new IOException("Camera position not found");
		cameraOffset = cameraPositions.get(0);
		//System.out.println("Camera offset: " + cameraOffset);

		//FIXME remove this to use damage positions
		//damagePositions.clear();

		/*
		Node bomb = BombModel.createBombModel();
		model.attachChild(bomb);
		bomb.getLocalTranslation().setY(-0.7f);
		bomb.getLocalTranslation().setZ(0.6f);
		bomb.setLocalScale(0.75f);
		*/

		//SpatialWalker.actOnSpatialTree(model, new SpatialBoundsClearer());

		//Strip bounds?
		//SpatialWalker.actOnSpatialTree(model, new SpatialClodinator(0.05f));
		SpatialWalker.actOnSpatialTree(model, new SpatialVBOInfoSetter(true));

		SpatialWalker.actOnSpatialTree(model, new SpatialMeshLocker());

		model.setModelBound(new BoundingSphere());
		model.updateModelBound();


	}

	/**
	 * Extract the positions of a sequence of children of the
	 * given model node, where the sequence must be named with the
	 * specified base, and a consecutive sequence of integers beginning
	 * with 0. As soon as a numbered child is missing, the list is
	 * returned (may be empty)
	 * @param model
	 * 		The model to check children of, for positions
	 * @param base
	 * 		The base name of the sequence of numbered children
	 * @return
	 * 		A list of the positions (local translations) of numbered
	 * 		sequential children, may be empty
	 */
	public static List<Vector3f> extractPositions(Node model, String base) {

		boolean found = true;
		int index = 0;

		List<Vector3f> positions = new ArrayList<Vector3f>();


		do {
			Spatial s = model.getChild(base + index);
			found = (s != null);
			if (found) {
				positions.add(s.getLocalTranslation());
			}
			index++;
		} while (found);


		return positions;
	}

	/**
	 * Extract a sequence of child nodes of the
	 * given model node, where the sequence must be named with the
	 * specified base, and a consecutive sequence of integers beginning
	 * with 0. As soon as a numbered child is missing, the list is
	 * returned (may be empty)
	 * @param model
	 * 		The model to check children of, for nodes
	 * @param base
	 * 		The base name of the sequence of numbered child nodes
	 * @return
	 * 		A list of the numbered sequential child nodes, may be empty
	 */
	public static List<Node> extractNodes(Node model, String base) {

		boolean found = true;
		int index = 0;

		List<Node> nodes = new ArrayList<Node>();


		do {
			Spatial s = model.getChild(base + index);
			found = (s instanceof Node);
			if (found) {
				nodes.add((Node)s);
			}
			index++;
		} while (found);


		return nodes;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneModel#getGunPositions()
	 */
	public List<Node> getGunPositions() {
		return gunPositions;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneModel#getPropPositions()
	 */
	public List<Node> getPropPositions() {
		return propPositions;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneModel#getModel()
	 */
	public Node getModel() {
		return model;
	}

	public List<Node> getDamagePositions() {
		return damagePositions;
	}

	public List<Node> getForwardFlaps() {
		return forwardFlaps;
	}

	public List<Node> getRearFlaps() {
		return rearFlaps;
	}

	public List<Node> getBulletBounds() {
		return bulletBounds;
	}

	public ApproximatelySphericalNode getAvoidanceNode() {
		return avoidanceNode;
	}

	public List<Node> getWreckage() {
		return wreckage;
	}

	public Spatial getPhysicsBounds() {
		return physicsBounds;
	}

	public List<Node> getWreckageStartPositions() {
		return wreckageStartPositions;
	}

	public Vector3f getCameraOffset() {
		return cameraOffset;
	}
}
