package com.rutgers.ai.gridworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import javax.swing.plaf.basic.BasicTreeUI.TreeTraverseAction;

public class MazeRunner {
	
	static int[][] fValue = new int[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH]; 
	static int[][] gValue = new int[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH]; 
	static int[][] hValue = new int[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH]; 
	static int[][] hNewValue = new int[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH]; 
	

	public static void main(String[] args) {
		
		MazeGenerator maze1 = new MazeGenerator();
		Cell[][] maze = maze1.createMazeDFS();
		maze1.printMaze();
		
		HashMap<Cell, Cell> cameFrom = new HashMap<Cell, Cell>();

		Cell goal = new Cell();
		goal.dfsx = Utilities.GOALX;
		goal.dfsy = Utilities.GOALY;

		Cell start = new Cell();
		start.dfsx = Utilities.STARTX;
		start.dfsy = Utilities.STARTY;

		System.out.println("Start point: " + Utilities.STARTX + " , " + Utilities.STARTY);
		System.out.println("Goal point: " + Utilities.GOALX + " , " + Utilities.GOALY);
		
		HashMap<Integer, Object> output= new HashMap<>();
		
		long startTime = System.nanoTime();
		output = aStar(maze,cameFrom,Utilities.STARTX,Utilities.STARTY,Utilities.GOALX,Utilities.GOALY);
		long endTime   = System.nanoTime();
		if(output!=null)
		{
			cameFrom = (HashMap<Cell, Cell>) output.get(1);
			List<Cell> expandedNodes = (List<Cell>)output.get(2);
			if(cameFrom!=null)
			{
				long totalTime = endTime - startTime;
				System.out.println("Found a path!! Computed in time  : "+totalTime/Math.pow(10, 6));
				System.out.println("Total Path : "+fValue[Utilities.GOALX][Utilities.GOALY]);
				traverse(start,goal,cameFrom,expandedNodes);
			}
		}
		else
		{
			System.out.println("No Path!!!!");
		}
		
		System.out.println("Start point: " + Utilities.GOALX + " , " + Utilities.GOALY);
		System.out.println("Goal point: " + Utilities.STARTX + " , " + Utilities.STARTY);
		
		cameFrom = new HashMap<Cell,Cell>();
		Long startTime2 = System.nanoTime();
		output = aStar(maze,cameFrom,Utilities.GOALX,Utilities.GOALY,Utilities.STARTX,Utilities.STARTY);
		Long endTime2   = System.nanoTime();
		if(output!=null)
		{
			cameFrom = (HashMap<Cell, Cell>) output.get(1);
			List<Cell> expandedNodes = (List<Cell>)output.get(2);
			if(cameFrom!=null)
			{
				long totalTime2 = endTime2 - startTime2;
				System.out.println("Found a path!! Computed in time  : "+totalTime2/Math.pow(10, 6));
				System.out.println("Total Path : "+fValue[Utilities.STARTX][Utilities.STARTY]);
				traverse(goal,start,cameFrom,expandedNodes);
			}
		}
		else
		{
			System.out.println("No Path!!!!");
		}
		System.out.println("=================================");
		System.out.println("Adaptive A*-->  ");
		System.out.println();
		List<List> output1 = adaptiveAstar(maze);
		if(output==null)
			System.out.println("FAILURE!!!");
		else {
			output1.get(0);
			Iterator<Cell> itr1 = output1.get(0).iterator();
			System.out.println("Result: ");
			while (itr1.hasNext())
				System.out.print(itr1.next() + " -> ");
			System.out.println("Expanded List: Count : " + output1.get(1).size());
			itr1 = output1.get(1).iterator();
			while (itr1.hasNext())
				System.out.print(itr1.next() + " -> ");
		}
	}
	public static void traverse(Cell start, Cell goal, HashMap<Cell, Cell> cameFrom,List<Cell> expandedNodes)
	{
		System.out.print(goal +" --> " );
		while (true) {
			Iterator<Map.Entry<Cell, Cell>> i = cameFrom.entrySet().iterator();
			while (i.hasNext()) {
				Entry<Cell, Cell> e = i.next();
				if (goal.equals(e.getKey())) {
					goal = e.getValue();
					if(goal.equals(start))
						System.out.print(goal);
					else
						System.out.print(goal +" --> " );	
				}
			}
			if(goal.equals(start))
				break;
		}
		System.out.println();
		
		/*while(cameFrom.containsKey(goal))
		{
			goal = cameFrom.get(goal);
			System.out.println(goal);
		}*/
		System.out.println("The expanded nodes are (count = "+expandedNodes.size()+ " ) : ");
		for(Cell cell:expandedNodes)
			System.out.print(" "+cell+ " ");
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println();
	}
	public static int[][] heuristic(int goalX,int goalY)
	{
		int[][] hValue = new int[Utilities.MAZE_HEIGHT][Utilities.MAZE_WIDTH]; 
		for(int i=0;i<Utilities.MAZE_HEIGHT;i++)
		{
			for(int j=0;j<Utilities.MAZE_WIDTH;j++)
			{
				hValue[i][j]=Math.abs(goalX-i) + Math.abs(goalY-j);
				
			}
		}
		return hValue;
	}
	
	public static HashMap<Integer, Object> aStar(Cell [][] maze,HashMap<Cell, Cell> cameFrom,int startX, int startY, int goalX, int goalY)
	{
		HashMap<Integer, Object> output = new HashMap<>();
		hValue = heuristic(goalX, goalY);
		PriorityQueue<TreeNode> openList = new PriorityQueue<TreeNode>();
		List<Cell> closedList = new ArrayList<Cell>();
		
		for(int i=0;i<Utilities.MAZE_HEIGHT;i++)
			for(int j=0;j<Utilities.MAZE_WIDTH;j++)
				gValue[i][j] = 9999;
		
		gValue[startX][startY]=0;
		fValue[startX][startY] = gValue[startX][startY]+ hValue[startX][startY];
		
		TreeNode start = new TreeNode();
		start.cell=maze[startX][startY];
		
		start.hValue = hValue[startX][startY];
		start.fValue = fValue[startX][startY];
		start.gValue = gValue[startX][startY];
		
		openList.add(start);
		
		while(openList.size()>0)
		{
			TreeNode current = openList.remove();
			closedList.add(current.cell);
			if(current.cell.dfsx == goalX && current.cell.dfsy == goalY)
			{
				//System.out.println("Total Path : "+fValue[goalX][goalY]);
				output.put(1, cameFrom);
				output.put(2, closedList);
				output.put(3, hValue);
				return output;
			}
			
			for(int d=0;d<4;d++)
			{
				Cell neighbor = current.cell.succ[d];
				if(neighbor == null)
					continue;
				if(closedList.contains(neighbor))
					continue;
				if(neighbor.blocked==1)
				{
					closedList.add(neighbor);
					continue;
				}
				
				TreeNode neighborNode = new TreeNode();
				neighborNode.cell =  neighbor;
				neighborNode.gValue = gValue[neighbor.dfsx][neighbor.dfsy];
				neighborNode.hValue = hValue[neighbor.dfsx][neighbor.dfsy];
				neighborNode.fValue = neighborNode.gValue+neighborNode.hValue ;
				
				
				if(!openList.contains(neighborNode))
				{
					openList.add(neighborNode);
				}
					
				int tentativeGscore = gValue[current.cell.dfsx][current.cell.dfsy] + 1;
				if (tentativeGscore > gValue[neighbor.dfsx][neighbor.dfsy])
					continue;
				
				cameFrom.put(neighbor,current.cell);
				
				gValue[neighbor.dfsx][neighbor.dfsy]=tentativeGscore;
				fValue[neighbor.dfsx][neighbor.dfsy]= gValue[neighbor.dfsx][neighbor.dfsy] + hValue[neighbor.dfsx][neighbor.dfsy];		
			}
			
		}
		return null;
		
	}
	
	public static List<List> adaptiveAstar(Cell [][] maze)
	{
		List output1 = new ArrayList<>();
		TreeNode start = new TreeNode();
		start.cell=maze[Utilities.STARTX][Utilities.STARTY];
		TreeNode goal = new TreeNode();
		goal.cell=maze[Utilities.GOALX][Utilities.GOALY];
		TreeNode current = start;
		List<Cell> closedList =  new ArrayList<>();
		List<Cell> doneList =  new ArrayList<>();
		List<Cell> expandedList = new ArrayList<>();
		HashMap<Cell, Cell> cameFrom = new HashMap<>();
		List<Cell> result = new ArrayList<>();
		
		while(!((current.cell).equals(goal.cell))){
			result.add(current.cell);
			expandedList.add(current.cell);
			if(current.equals(goal)){
				output1.add(result);
				output1.add(expandedList);
				return output1;
			}
				
			
			gValue[current.cell.dfsx][current.cell.dfsy] =0;
			HashMap output = aStar(maze, cameFrom, current.cell.dfsx, current.cell.dfsy, goal.cell.dfsx, goal.cell.dfsy);
			doneList.add(current.cell);
			cameFrom = (HashMap<Cell, Cell>) output.get(1);
			closedList = (List<Cell>) output.get(2);
			if(cameFrom==null)
				return null;
			Iterator<Cell> itr = closedList.iterator();
			Cell state = new Cell();
			while(itr.hasNext())
			{
				state = itr.next();
				hValue[state.dfsx][state.dfsy] = gValue[goal.cell.dfsx][goal.cell.dfsy] - gValue[state.dfsx][state.dfsy];
			}
			state = current.cell;
			int tentativeHscore = 9999;
			for(int d =0 ;d<4 && !((state).equals(goal.cell));d++)
			{
				if(!closedList.contains(state.succ[d]))
					continue;
				if(state.succ[d].blocked==1)
					continue;
				if(doneList.contains(state.succ[d]))
					continue;
				int tentativeGscore = hValue[state.dfsx][state.dfsy];
				
				if(tentativeHscore >= hValue[state.succ[d].dfsx][state.succ[d].dfsy])
				{
					current.cell = state.succ[d];
					tentativeHscore = hValue[state.succ[d].dfsx][state.succ[d].dfsy];
				}
				
				int xcord = state.succ[d].dfsx;
				int ycord = state.succ[d].dfsy;
				/*System.out.println("Gvalues: current: "+gValue[state.dfsx][state.dfsy]);
				System.out.println("Gvalues: succesor: "+gValue[state.succ[d].dfsx][state.succ[d].dfsy]);
				System.out.println("Hvalues current: "+hValue[state.dfsx][state.dfsy]);
				System.out.println("Hvalues: succesor: "+hValue[state.succ[d].dfsx][state.succ[d].dfsy]);*/
				
				if(tentativeGscore > gValue[state.succ[d].dfsx][state.succ[d].dfsy])
				/*{
					gValue[state.succ[d].dfsx][state.succ[d].dfsy] = tentativeGscore;
					current.cell = state.succ[d];
				}
				else*/
					continue;
			}
			if(current.equals(goal)){
				result.add(current.cell);
				expandedList.add(current.cell);
				output1.add(result);
				output1.add(expandedList);
				return output1;
			}
		}
		return output1;
	}
	

}
