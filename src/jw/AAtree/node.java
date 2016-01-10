package jw.AAtree;

public class node {
	private int key, value, level;
	private node left, right;
	
	public node (int k, int v) {
		key = k;
		value = v;
		level = 1;
		left = null;
		right = null;
	}
	
	
	public int getKey () {
		return key;
	}
	
	public int getValue () {
		return value;
	}
	
	public int getLevel () {
		return level;
	}
	
	public node getLeft () {
		return left;
	}
	
	public node getRight () {
		return right;
	}
	
	public void setKey (int k) {
		key = k;
	}
	
	public void setValue (int v) {
		value = v;
	}
	
	public void setLevel (int lvl) {
		level = lvl;
	}
	
	public void setLeft (node l) {
		left = l;
	}
	
	public void setRight (node r) {
		right = r;
	}
}
