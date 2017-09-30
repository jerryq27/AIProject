package com.jerry.aiproject.core;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.jerry.aiproject.utils.SpriteLoader;
import com.jerry.aiproject.gameobjects.*;

/**
 * This class provides information of 
 * the map for the AI algorithms, 
 * the number of tiles (rows and columns), 
 * the state of the tiles, and the size of the tiles in pixels. 
 * @author Jerry
 */
public class TileMap {

	public enum TileType {GRASS, LAVA} //Tile types. 
	private BufferedImage grass, lava; //Tile images.
	
	private int mapWidth, mapHeight; //The number of rows/columns in tiles. 
	private int tileWidth, tileHeight; //The height and width of each tile in pixels. 
	private boolean[][] tileState; //The 'state' of each tile, blocked or not. 
	private TileType[][] tiles; //Map tile types.
	private boolean[][] visited; //Debug tool, notes if the path finding algorithm has visited a tile. 
	
	public TileMap(int mWidth, int mHeight, int tWidth, int tHeight) {
		mapWidth = mWidth;
		mapHeight = mHeight;
		tileWidth = tWidth;
		tileHeight = tHeight;

        int rows = getRows();
        int cols = getCols();
        tileState = new boolean[rows][cols];
        tiles = new TileType[rows][cols];
        visited = new boolean[rows][cols];
		loadTiles();
	}
	
	/**
	 * Fills the map with a single tile type. 
	 * @param type the type of tile. 
	 * @param passable can an object pass this tile?
	 */
	public void fillMap(TileType type, boolean passable) {
		for(int row  = 0; row < tiles.length; row++)
		{
			for(int col = 0; col < tiles[row].length; col++)
			{
				tiles[row][col] = TileType.GRASS;
				tileState[row][col] = passable;
			}
		}
	}

	/**
	 * Fills a whole row of the map with 
	 * a single tile type. 
	 * @param type the type of tile. 
	 * @param row the row to fill. 
	 * @param passable can an object pass this tile?
	 */
	public void fillRow(TileType type, int row, boolean passable) {
		for(int col = 0; col < tiles[row].length; col++)
		{
			tiles[row][col] = type;
			tileState[row][col] = passable;
		}
	}
	
	/**
	 * This method fills a whole column of 
	 * the map with a single tile type. 
	 * @param type the type of tile. 
	 * @param col the column to fill. 
	 * @param passable can an object pass this tile?
	 */
	public void fillCol(TileType type, int col, boolean passable) {
		for(int row = 0; row < tiles.length; row++)
		{
			tiles[row][col] = type;
			tileState[row][col] = passable;
		}
	}	
	
	/**
	 * This method fills a single tile of
	 * the map with the specified TileType.
	 * @param type type of the tile. 
	 * @param row the row of the tile to fill. 
	 * @param col the column of the tile to fill. 
	 * @param passable can an object pass this tile?
	 */
	public void fillTile(TileType type, int row, int col, boolean passable) {
		tiles[row][col] = type;
		tileState[row][col] = passable;
	}
	
	/**
	 * This method calculates the number of 
	 * rows that could fit on the map. 
	 * @return the number of rows on the map.
	 */
	public int getRows() {
		return mapHeight/tileHeight;
	}
	
	/**
	 * This method calculates the number of
	 * columns that could fit on the map.
	 * @return the number of columns on the map. 
	 */
	public int getCols() {
		return mapWidth/tileWidth;
	}
	
	/**
	 * This method returns the specified row
	 * in the map by the pixels passed in, it 
	 * only works in increments of the tileHeight. 
	 * @param pixels the pixel position of the row.
	 * @return the row number. 
	 */
	public int getRow(int pixels) {
		return pixels/tileHeight;
	}
	
	/**
	 * This method returns the specified column
	 * in the map by the pixels passed in, it 
	 * only works in increments of the tileWidth.
	 * @param pixels the pixel position of the column.
	 * @return the column number. 
	 */
	public int getCol(int pixels) {
		return pixels/tileWidth;
	}
	
	/**
	 * These methods will help place game objects
	 * on the map, it will also make using the 
	 * AI algorithms easier. 
	 * @param tileRow the tile's row coordinate.
	 * @return the tile row location in pixels. 
	 */
	public int getYCoord(int tileRow) {
		return tileRow * tileHeight;
	}
	
	/**
	 * These methods will help place game objects
	 * on the map, it will also make using the 
	 * AI algorithms easier.
	 * @param tileCol the tile's column coordinate. 
	 * @return the tile's column location in pixels. 
	 */
	public int getXCoord(int tileCol) {
		return tileCol * tileWidth;
	}
	
	/**
	 * This method determines if a particular tile
	 * is passable or not. 
	 * @param object the GameObject making the move.
	 * @param row the row of the tile. 
	 * @param col the column of the tile. 
	 * @return if the tile is passable.
	 */
	public boolean isBlocked(GameObject object, int row, int col) {
		if(row < 0 || col < 0 || row == getRows() || col == getCols())
			return false;
		else
			return tileState[row][col];
	}
	
	/**
	 * This method determines if the tile location
	 * passed in is still within the map. Also 
	 * checks if the current tile is blocked. 
	 * @param row the tile's row. 
	 * @param col the tile's column. 
	 * @return if the tile is still within the map.
	 */
	public boolean isWithinMap(GameObject object, int sRow, int sCol, int row, int col) {
		boolean withinMap = true;
		
		if(row < 0 || col < 0 || row > getRows() || col > getCols())
			withinMap = false;
		if(withinMap && row != sRow || col != sCol)
			withinMap = isBlocked(object, row, col);
		
		return withinMap;
	}
	
	/**
	 * This method determines the 'cost' to move
	 * the object from it's position to another tile. 
	 * @param object the GameObject that is moving. 
	 * @param startR starting row. 
	 * @param startC starting column. 
	 * @param tileR tile's row. 
	 * @param tileC tile's column.
	 * @return the cost to move from tile to tile. 
	 */
	public float getMoveCost(GameObject object, int startR, int startC, int tileR, int tileC) {
		return 1; //Moving in single tile units, easier to determine tile locations this way. 
	}
	
	/**
	 * Debug method, keeps track of the nodes a path
	 * finding algorithm has visited. 
	 * @param row the row visited by the algorithm. 
	 * @param col the column visited by the algorithm. 
	 */
	public void pathFinderVisited(int row, int col) {
		visited[row][col] = true;
	}
	
	/**
	 * This method loads the tile images.
	 */
	private void loadTiles() {
		lava = SpriteLoader.loadImage("res/testTiles.png", 1, 1, 32, 48);
		grass = SpriteLoader.loadImage("res/testTiles.png", 2, 1, 32, 48);
	}
	
	/**
	 * This method draws the map. 
	 * @param g the drawing tool. 
	 */
	public void render(Graphics g) {
		for(int row = 0; row < tiles.length; row++)
		{
			for(int col = 0; col < tiles[row].length; col++)
			{
				if(tiles[row][col] == TileType.GRASS)
				{
					g.drawImage(grass, col * 32, row * 48, col + 32, row + 48, null);
                    g.setColor(Color.RED);
                    g.drawRect(col * 32, row * 48, col + 32, row + 48);
				}
				else if(tiles[row][col] == TileType.LAVA)
				{
					g.drawImage(lava, col * 32, row * 48, col + 32, row + 48, null);
                    g.setColor(Color.RED);
                    g.drawRect(col * 32, row * 48, col + 32, row + 48);
				}
			}
		}
	}
	
	/**
	 * This method prints out a visual 
	 * of the map, it also insures this
	 * class is working correctly. 
	 */
	public void printMap() {
		if(tiles.length == 0 || tileState.length == 0)
		{	
			System.err.println("One or both arrays are empty.");
			return; //Just return if no printing can be done.
		}
		
		//Print the map.
		for(int row = 0; row < tiles.length; row++)
		{
			for(int col = 0; col < tiles[row].length; col++)
			{
				System.out.print(tiles[row][col] + ":" + tileState[row][col] + " ");
				if(col == tiles[row].length - 1) //For a nicer display.
					System.out.print("\n");
			}
		}
	}
}