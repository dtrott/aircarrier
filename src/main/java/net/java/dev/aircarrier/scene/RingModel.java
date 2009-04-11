package net.java.dev.aircarrier.scene;

import java.io.IOException;
import java.util.List;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;
import net.java.dev.aircarrier.planes.Propeller;
import net.java.dev.aircarrier.planes.SimplePlaneModel;
import net.java.dev.aircarrier.util.SpatialVBOInfoSetter;
import net.java.dev.aircarrier.util.SpatialWalker;

import jmetest.renderer.TestEnvMap;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class RingModel {

	List<Node> propPositions;
	
	private Node model;
	
	public RingModel() throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "sphere");
		model = jbr.loadBinaryFormat(RingModel.class.getClassLoader()
				.getResourceAsStream("resources/ring.jme"));

		//SpatialWalker.actOnSpatialTree(model, new SpatialClodinator(0.05f));
		SpatialWalker.actOnSpatialTree(model, new SpatialVBOInfoSetter(true));
		
		model.updateGeometricState(0, true);
		
		Texture ringTexture = TextureManager.loadTexture(RingModel.class
				.getClassLoader().getResource("resources/ring.png"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);

		Texture engineTexture = TextureManager.loadTexture(RingModel.class
				.getClassLoader().getResource("resources/ringEngine.png"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);

		Texture envTexture = TextureManager.loadTexture(TestEnvMap.class
				.getClassLoader()
				.getResource("resources/sky_env.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		envTexture.setEnvironmentalMapMode(Texture.EM_SPHERE);
		envTexture.setApply(Texture.AM_ADD);

		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_NONE);
		
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
				if (n.getName().startsWith("ring")) {
					ts.setTexture(ringTexture, 0);					
				} else {
					ts.setTexture(engineTexture, 0);
				}

				// Add shiny environment to shield, cowling and engines
				if (n.getName().indexOf("env") >= 0) {
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
				
		propPositions = SimplePlaneModel.extractNodes(model, "prop");
		System.out.println("prop positions " + propPositions.size());

		//Attach propellers
		for (Node propPosition : propPositions) {
			Propeller prop = new Propeller(new Vector3f(0, -0.422f, -1));
			propPosition.attachChild(prop);
			prop.addController(new UpdatableController(prop));
		}
		
		model.setModelBound(new BoundingSphere());
		model.updateModelBound();
		
	}
	

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneModel#getModel()
	 */
	public Node getModel() {
		return model;
	}

}
