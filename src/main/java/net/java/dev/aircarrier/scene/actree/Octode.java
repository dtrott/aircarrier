package net.java.dev.aircarrier.scene.actree;

import com.jme.scene.Node;

public class Octode extends Node {
	
	Octode[][][] octodes;
	int level;
	
	public Octode(String name, int level) {
		super(name);
		octodes = new Octode[2][2][2];
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}

	public boolean isLeaf() {
		return false;
	}
	
	public Octode getChild(int x, int y, int z) {
		return getChild(x, y, z, false);
	}

	public Octode getChild(int x, int y, int z, boolean create) {
		Octode o = octodes[x][y][z];
		
		if (!create) return o;
		
		//If there is no octode, and we are above level 1, then create
		//and return a new child. If we are at level 1, then the child
		//layer is of cubes, which we do not autocreate
		if (o==null && level > 1) {
			o = new Octode(getName() + "->(" + x + ", " + y + ", " + z + ")", level-1);
			octodes[x][y][z] = o;
			attachChild(o);
		}
		
		return o;
	}

	public ACube getLeaf(int x, int y, int z) {
		return getLeaf(x, y, z, false);
	}

	public ACube getLeaf(int x, int y, int z, boolean create) {
		int xo = ((x & (1<<level))>0) ? 1 : 0;
		int yo = ((y & (1<<level))>0) ? 1 : 0;
		int zo = ((z & (1<<level))>0) ? 1 : 0;
		//If we are at level 1, then the next level down is the actual
		//cube layer, so just return any current element (we never
		//auto-create cubes)
		if (level == 1) {
			return (ACube)getChild(x, y, z, false);
			
		//If we are above level 1, then we get the octode (creating
		//if necessary) and return the result of recursively searching
		//through it
		} else {
			Octode o = getChild(xo, yo, zo, create);
			return o.getLeaf(x, y, z, create);
		}
	}

	public void setLeaf(int x, int y, int z, ACube leaf) {
		int xo = ((x & (1<<level))>0) ? 1 : 0;
		int yo = ((y & (1<<level))>0) ? 1 : 0;
		int zo = ((z & (1<<level))>0) ? 1 : 0;
		
		//If we are at level 1, then the next level down is the actual
		//cube layer, so we should insert the child cube
		if (level == 1) {
			octodes[xo][yo][zo] = leaf;
			attachChild(leaf);
			
		//If we are above level 1, then we get the octode (creating
		//if necessary) and return the result of recursively searching
		//through it
		} else {
			Octode o = getChild(xo, yo, zo, true);
			o.setLeaf(x, y, z, leaf);
		}
	}

}
