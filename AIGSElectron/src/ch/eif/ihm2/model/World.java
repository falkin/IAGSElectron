package ch.eif.ihm2.model;

import ch.eif.ihm2.cst.Constants;

public class World {
	private static World world;
	boolean[][] tab = new boolean[Constants.WORLD_WIDTH + 2][Constants.WORLD_HEIGHT + 2];

	private World() {
	}

	public static World getInstance() {
		if (world == null) {
			world = new World();
		}
		return world;
	}

	public boolean isObstacle(int x, int y) {
		if (x < 0 || y < 0 || y > Constants.WORLD_HEIGHT
				|| x > Constants.WORLD_WIDTH) {
			return true;
		}
		return tab[x][y];
	}
	
	public void init(){
		for(int i=0; i<tab.length;i++){
			for(int j=0; j<tab[0].length; j++){
				tab[i][j]=false;
			}
		}
	}

	public void set(int x, int y) {
		if (x < 0 || y < 0 || y > Constants.WORLD_HEIGHT
				|| x > Constants.WORLD_WIDTH)
			return;
		tab[x][y] = true;
	}
}
