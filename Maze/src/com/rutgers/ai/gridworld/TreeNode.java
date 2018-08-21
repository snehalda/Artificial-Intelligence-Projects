package com.rutgers.ai.gridworld;

public class TreeNode implements Comparable<TreeNode> {

	Cell cell;
	Integer fValue;
	Integer hValue;
	Integer gValue;
	
	
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public int getfValue() {
		return fValue;
	}
	public void setfValue(int fValue) {
		this.fValue = fValue;
	}
	public int gethValue() {
		return hValue;
	}
	public void sethValue(int hValue) {
		this.hValue = hValue;
	}
	public int getgValue() {
		return gValue;
	}
	public void setgValue(int gValue) {
		this.gValue = gValue;
	}
	@Override
	public int compareTo(TreeNode o) {
		if(this.fValue == o.fValue){
			return o.gValue.compareTo(this.gValue);
		}
		return this.fValue.compareTo(o.fValue);
	}
	@Override
	public boolean equals(Object o)
	{
		if(this.cell.dfsx==((TreeNode)o).cell.dfsx && this.cell.dfsy==((TreeNode)o).cell.dfsy)
			return true;
		else 
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return 100;
	}
	
}
