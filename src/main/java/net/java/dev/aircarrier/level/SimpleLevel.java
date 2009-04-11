package net.java.dev.aircarrier.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class SimpleLevel {

	List<Node> ringPositions;
	List<Node> concreteCubePositions;
	
	Node rootNode;
	
	public SimpleLevel(
			String modelResource, 
			float scale) throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "sphere");
		rootNode = jbr.loadBinaryFormat(SimpleLevel.class.getClassLoader()
				.getResourceAsStream(modelResource));

		rootNode.setLocalScale(scale);
		
		rootNode.updateGeometricState(0, true);

		ringPositions = extractNodes(rootNode, "ring.", 3, 1);
		System.out.println(ringPositions.size() + " ring positions found");

		//Strip everything beneath ring position nodes
		for (Node n : ringPositions) {
			n.detachAllChildren();
		}
		
		concreteCubePositions = extractNodes(rootNode, "concreteCube.", 3, 1);
		System.out.println(ringPositions.size() + " concrete cube positions found");

		//Strip everything beneath concrete cube position nodes
		for (Node n : concreteCubePositions) {
			n.detachAllChildren();
		}

	}
	
	public Node getRootNode() {
		return rootNode;
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
	public static List<Node> extractNodes(Node model, String base, int digits, int firstIndex) {
		
		boolean found = true;
		int index = firstIndex;
		
		List<Node> nodes = new ArrayList<Node>();
		
		
		do {
			String indexString = Integer.toString(index);
			while (indexString.length() < digits) indexString = "0" + indexString;
			System.out.println("Looking for " + base + indexString);
			Spatial s = model.getChild(base + indexString);
			found = (s instanceof Node);
			if (found) {
				nodes.add((Node)s);
			}
			index++;
		} while (found);
		
		
		return nodes;
	}

	public List<Node> getRingPositions() {
		return ringPositions;
	}

	public List<Node> getConcreteCubePositions() {
		return concreteCubePositions;
	}
	
}
