package ch.eif.ihm2.model;

import ch.eif.ihm2.cst.Constants;

public class World {
	private static World world;
	boolean[][] tab = new boolean[Constants.WORLD_WIDTH+2][Constants.WORLD_HEIGHT+2];

	private World() {
	}

	public static World getInstance() {
		if (world == null) {
			world = new World();
		}
		return world;
	}
	
	public boolean[][] get(){
		return tab;
	}

	public void set(int x, int y){
		tab[x][y]=true;
	}
}
