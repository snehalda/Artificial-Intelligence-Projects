package com.rutgers.ai.gridworld;

class Cell{
	public int dfsx;
	public int dfsy;
	public int blocked;
	public Cell[] succ;
	public Cell(int x, int y, int blocked) {
		super();
		this.dfsx = x;
		this.dfsy = y;
		this.blocked = blocked;
		succ = new Cell[4];	
	}
	public Cell(){
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString()
	{
		return "("+this.dfsx+","+this.dfsy+")";
		
	}
	@Override
	public boolean equals(Object o)
	{
		Cell cell = new Cell();
		cell.dfsx=((Cell)o).dfsx;
		cell.dfsy=((Cell)o).dfsy;
		
		
		if((this.dfsx == cell.dfsx) && (this.dfsy == cell.dfsy) )
		{
			
			return true;
		}
		else 
			return false;
	}
	@Override
	public int hashCode(){
		return this.dfsx%10;
	    }
}

public class MazeGenerator {
	
	static int[] dx = {1, 0, -1,  0};
	static int[] dy = {0,  1,  0, -1};

	
	public final Cell[][] maze;
	
	
	public MazeGenerator() {
		super();
		this.maze = new Cell[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH];
	}
	public void printMaze() {
		
		for (int col=0; col<3*Utilities.MAZE_WIDTH+3; col++)
		    System.out.print("X");
		  
		  System.out.println();
		for(int j=0;j<Utilities.MAZE_HEIGHT;j++)
		{	System.out.print("X");
			for(int i=0;i<Utilities.MAZE_WIDTH;i++)
			{
				
				if(i==Utilities.STARTX && j == Utilities.STARTY)
				{
					System.out.print("|A ");
				}
				else if(i==Utilities.GOALX && j == Utilities.GOALY)
				{
					System.out.print("|T ");
				}
				else if(maze[i][j].blocked == 0){
					System.out.print("|  ");
				}
					
				if(maze[i][j].blocked == 1){
					System.out.print("|X ");
				}
			}
			System.out.println("|X");
		}
		for (int col=0; col<3*Utilities.MAZE_WIDTH+3; col++)
		    System.out.print("X");
		System.out.println();

	}
	public Cell[][] createMazeDFS()
	{
		
		
		for(int row=0;row<Utilities.MAZE_HEIGHT;row++)
			for (int col=0; col<Utilities.MAZE_WIDTH;col++){
			maze[row][col] = new Cell();
			maze[row][col].succ = new Cell[4];
			maze[row][col].blocked=0;
			maze[row][col].dfsx = 0;
			maze[row][col].dfsy = 0;
			}
		
			for(int row=0;row<Utilities.MAZE_WIDTH;row++)
				for (int col=0; col<Utilities.MAZE_HEIGHT;col++){
				for (int d = 0;d<4;d++) {
					int newy = col + dy[d];
					int newx = row + dx[d];
					maze[row][col].succ[d] = new Cell();
					if(newy >= 0 && newy < Utilities.MAZE_HEIGHT && newx >= 0 && newx < Utilities.MAZE_WIDTH)
					{
						maze[row][col].succ[d] = maze[newx][newy];
						maze[row][col].succ[d].dfsx=newx;
						maze[row][col].succ[d].dfsy=newy;
					}
					else
						maze[row][col].succ[d] = null;
					
				}
			}
			
			boolean visited[][] = new boolean[101][101];
			for(int i=0;i<Utilities.MAZE_HEIGHT;i++)
				for(int j=0;j<Utilities.MAZE_WIDTH;j++)
					visited[i][j]=false;
			
			
			
			DFS(Utilities.STARTX,Utilities.STARTY,visited);
			maze[Utilities.STARTX][Utilities.STARTY].blocked = 0;
			maze[Utilities.GOALX][Utilities.GOALY].blocked = 0;
			
		return maze;
		
	}
	
	public void explore(int x, int y, boolean[][] visited) {

		for (int d = 0; d < 4; d++) {
			if (maze[x][y].succ[d] != null && !visited[x][y])
			{
				visited[x][y]= true;
				maze[x][y].succ[d].blocked=(Math.random()> 0.3 ? 0: 1);
				if(maze[x][y].succ[d].blocked == 0)
					explore(maze[x][y].succ[d].dfsx, maze[x][y].succ[d].dfsy, visited);
				else {
					visited[maze[x][y].succ[d].dfsx][maze[x][y].succ[d].dfsy]= true;
					continue;
				}
					
			}
		}

	}
	
	public void DFS(int x, int y, boolean[][] visited) {

		for (int i = x; i < Utilities.MAZE_HEIGHT; i++) {
			for (int j = y ; j < Utilities.MAZE_WIDTH; j++) {
				if (!visited[i][j])
					explore(i, j, visited);
			}
		}
		for (int i = 0; i < Utilities.MAZE_HEIGHT; i++) {
			for (int j = 0 ; j < Utilities.MAZE_WIDTH; j++) {
				if (!visited[i][j])
					explore(i, j, visited);
			}
		}

	}
	
	
	
}

