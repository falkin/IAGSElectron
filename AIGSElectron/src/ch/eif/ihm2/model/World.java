package ch.eif.ihm2.model;

import java.awt.Color;

import ch.eif.ihm2.cst.Constants;

public class World {
	private static World world;
	int width= Constants.WORLD_WIDTH + 2;
	int height = Constants.WORLD_HEIGHT + 2;
	boolean[][] tab = new boolean[Constants.WORLD_WIDTH + 2][Constants.WORLD_HEIGHT + 2];
	char[][] tabColor = new char[Constants.WORLD_WIDTH + 2][Constants.WORLD_HEIGHT + 2];
	double[] colorCase = new double[4];
	// v = Vide o = bombe r = rouge b = bleu
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
				tabColor[i][j]='v';
			}
		}
	}
	public void set(int x, int y,char color) {
		if (x < 0 || y < 0 || y > Constants.WORLD_HEIGHT
				|| x > Constants.WORLD_WIDTH)
			return;
		tab[x][y] = true;
		tabColor[x][y]=color;
	}
	public void remove(int x, int y) {
		if (x < 0 || y < 0 || y > Constants.WORLD_HEIGHT
				|| x > Constants.WORLD_WIDTH || tabColor[x][y]=='o')
			return;
		tab[x][y] = false;
		tabColor[x][y] ='v';
	}
	public void printTable(){
		for(int j=0; j<tab[0].length; j++){
			for(int i=0; i<tab.length;i++){
				if(tab[i][j]==false)System.out.print(0);
				else System.out.print(1);
			}
			System.out.println(" ");
		}
	}
	public double[] getTrackObstaclSensors(int posX,int posY,Color color){
		double[] dist = new double[4];
		for (int a=0;a<4;a++){
			int x = posX;
			int y = posY;
			switch (a) {
				case 0:
					x+=1;
					// RIGHT
					while(x< width && x > 0 && y < height && y>0 && !tab[x][y]){
						x+=1;
						dist[a]+=1;
					}
					x=posX+(Constants.WEAPON_FIRE_RANGE+2);
					break;
				case 1:
					x-=1;
					//LEFT
					while(x< width && x > 0 && y < height && y>0 &&!tab[x][y]){
						x-=1;
						dist[a]+=1;
					}
					x=posX-(Constants.WEAPON_FIRE_RANGE+2);
					break;
				case 2:
					y+=1;
					//DOWN
					while(x< width && x > 0 && y <height && y>0 &&!tab[x][y]){
						y+=1;
						dist[a]+=1;
					}
					y=posY+(Constants.WEAPON_FIRE_RANGE+2);
					break;
				case 3:
					y-=1;
					//UP
					while(x< width && x > 0 && y < height && y>0 &&!tab[x][y]){
						y-=1;
						dist[a]+=1;
					}
					y=posY-(Constants.WEAPON_FIRE_RANGE+2);
					break;
			}
			if(x< width && x >= 0 && y < height && y>=0 && tabColor[x][y] == 'r' &&  color.getBlue() == 255 ){	
				colorCase[a]=2;
			}
			else if(x< width && x >= 0 && y <height && y>=0 && tabColor[x][y] == 'b'&& color.getRed() == 255){	
				colorCase[a]=2;
			}
			else if(x< width && x >= 0 && y < height && y>=0 && (tabColor[x][y] != 'b' && tabColor[x][y] != 'r')){	
				colorCase[a]=1;
			}
			else if(x< width && x >= 0 && y < height && y>=0 && (tabColor[x][y] == 'b' || tabColor[x][y] == 'r')){	
				colorCase[a]=-1;
			}
			else{
				colorCase[a]=0;
			}
		}
		return dist;
	}
	

	public double[] getTrackObstaclColor(){
		return colorCase;
	}
}
