package net.java.dev.aircarrier.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;
import net.java.dev.aircarrier.util.SpatialWalker;
import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class TerrainQuadModel {

	private Node model;

	public TerrainQuadModel(
			String modelResource,
			String textureResource,
			String detailTextureResource) throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "box");

		model = jbr.loadBinaryFormat(TerrainQuadModel.class.getClassLoader()
				.getResourceAsStream(modelResource));

		model.updateGeometricState(0, true);

		Texture texture = TextureLoader.loadTexture(textureResource);
		//Texture detailTexture = TextureLoader.loadTexture(detailTextureResource);

		System.out.println("===========================");
		SpatialWalker.printSpatialTree(model);
		System.out.println("===========================");

		//Node terrainRoot = new Node("terrainRoot");
		List<Node> pages = extractNodes(model, "desert");
		System.out.println("Number of pages " + pages.size());

		//Arrange the pages in a quadtree

		//Work out the edge lengths
		int pagesCount = pages.size();
		int edgeLength = (int)Math.round(Math.sqrt(pagesCount));
		if (edgeLength * edgeLength != pagesCount) throw new IOException("Invalid page model, does not have a square number of pages.");
		System.out.println("edgeLength " + edgeLength);

		//Work out the number of levels in the quadtree
		int levels = (int)Math.round(Math.log(edgeLength)/Math.log(2));
		System.out.println("levels " + levels);

		//Set bounding boxes
		for (Node page : pages) {
			page.setModelBound(new BoundingBox());
			page.updateModelBound();
		}

		//Set parent bounding box
		model.setModelBound(new BoundingBox());
		model.updateModelBound();

		//Build the quadtree! It has quadbranches and little quadleaves.



		/*
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

		*/

		//Lock model and set vbo info
		//SpatialWalker.actOnSpatialTree(model, new SpatialLocker());
		//SpatialWalker.actOnSpatialTree(model, new SpatialVBOInfoSetter(true));

		TextureState ts = (TextureState) model.getRenderState(RenderState.RS_TEXTURE);
		if (ts == null) {
			ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		}
		texture.setWrap(Texture.WrapMode.Repeat);
		ts.setTexture(texture);
		model.setRenderState(ts);
		model.updateRenderState();


		model.setModelBound(new BoundingSphere());
		model.updateModelBound();

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
	 * @see net.java.dev.aircarrier.planes.PlaneModel#getModel()
	 */
	public Node getModel() {
		return model;
	}

}
