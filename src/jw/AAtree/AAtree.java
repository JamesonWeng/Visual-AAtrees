package jw.AAtree;

import java.util.ArrayList;

// Here we implement an AAtree in order to help provide a visualization of how the tree works.
// The AAtree is a type of balanced binary search tree with guaranteed O(log n) height, and 
// it satisfies the following invariants:
// 1. All nodes to the left of a node have a smaller key than the node, and all nodes to the right have a larger key.
// 2. All nodes also have a field recoding its level - the level of a leaf (a node with no children) is 1.
// 3. The left child of a node has a level one lower than the node. The right child of the node has either the same level or a level one lower than the node.
// 4. The right child of the right child of a node cannot have the same level as the node (it must be lower).

// A node with a right child at the same level is called a double node.
// Otherwise, the node is called a single node.

public class AAtree {
	node root;
	
	public AAtree () {
		root = null;
	}
	
	// for debugging
	private void disp (node n) {
		if (n != null) {
			System.out.print(" " + n.getKey() + "." + n.getLevel());
			disp (n.getLeft());
			disp (n.getRight());
		}
		else {
			System.out.print("null");
		}
	}
	
	public void print () {
		disp (root);
		System.out.print("\n");
	}
	
	private static int max (int a, int b) {
		return (a > b ? a : b);
	}
	
	private static int findHeight (node n) {
		if (n == null)  {
			return 0;
		}
		else { 
			return 1 + max (findHeight (n.getLeft()), findHeight (n.getRight()));
		}
	}
	
	public int getHeight () {
		return findHeight (root);
	}
	
	private static int findWidth (node n) {
		if (n == null) {
			return 0;
		}
		else {
			if (checkSingle (n)) {
				return 1 + findWidth (n.getLeft()) + findWidth (n.getRight());
			}
			else {
				return 2 + findWidth (n.getLeft()) + findWidth (n.getRight().getLeft()) + findWidth (n.getRight().getRight());				
			}
		}
	}
	
	public int getWidth () {
		return findWidth (root);
	}
	
	////////////////////////////////////////
	// SEARCH //
	////////////////////////////////////////
	
	private static Integer searchRec (node n, int k) {
		if (n == null) {
			return null;
		}
		else if (n.getKey() == k) {
			return n.getValue();
		}
		else if (k > n.getKey()) {
			return searchRec (n.getRight(), k);
		}
		else {
			return searchRec (n.getLeft(), k);
		}
	}
	
	public Integer search (int k) {
		return searchRec (root, k);
	}
	
	
	////////////////////////////////////////
	// INSERTION //
	////////////////////////////////////////
	
	private static node skew (node n) {
		if (n != null && n.getLeft() != null && (n.getLevel() == n.getLeft().getLevel())) {
			node rNode = new node (n.getKey(), n.getValue());
			rNode.setLevel(n.getLevel());
			rNode.setRight(n.getRight());
			rNode.setLeft(n.getLeft().getRight());			
			
			node newNode = new node (n.getLeft().getKey(), n.getLeft().getValue());
			newNode.setLevel(n.getLevel());
			newNode.setLeft(n.getLeft().getLeft());
			newNode.setRight(rNode);
			
			return newNode;
		}
		
		else {
			return n;
		}		
	}
	
	private static node split (node n) {
		if (n != null && n.getRight() != null && n.getRight().getRight() != null && 
				(n.getLevel() == n.getRight().getLevel()) && (n.getLevel() == n.getRight().getRight().getLevel())) {		
			node lNode = new node (n.getKey(), n.getValue());
			lNode.setLevel(n.getLevel());
			lNode.setLeft(n.getLeft());
			lNode.setRight(n.getRight().getLeft());
			
			node rNode = new node (n.getRight().getRight().getKey(), n.getRight().getRight().getValue());
			rNode.setLevel(n.getLevel());
			rNode.setLeft(n.getRight().getRight().getLeft());
			rNode.setRight(n.getRight().getRight().getRight());
			
			node newNode = new node (n.getRight().getKey(), n.getRight().getValue());
			newNode.setLevel(n.getLevel() + 1);
			newNode.setLeft(lNode);
			newNode.setRight(rNode);			
			
			return newNode;
		}
		
		else {
			return n;
		}
	}
	
	private static node ins (node current, int k, int v) {
		if (current == null) {
			current = new node (k, v);
		}
		
		else if (k > current.getKey()) {
			current.setRight(ins(current.getRight(), k, v));
		}
		
		else if (k < current.getKey()) {
			current.setLeft(ins(current.getLeft(), k, v));
		}
		
		return split (skew (current));
	}
	
	public void insert (int k, int v) {
		root = ins (root, k, v);
	}
	
	// for the purposes of visualizing the tree, the contents of the keys aren't important
	// so we just assign an arbitrary value
	public void insertKeysArrayList (ArrayList<Integer> a) {
		for (int i = 0; i < a.size(); i++) {
			insert (a.get(i), 0);
		}
	}
	
	
	////////////////////////////////////////
	// DELETION //
	////////////////////////////////////////
	
	// returns true if the given node is a single node, and false otherwise
	private static boolean checkSingle (node n) {
		if (n == null) {
			return false;
		}
		else if (n.getRight() == null) {
			return true;
		}
		else {
			return n.getLevel() > n.getRight().getLevel();
		}
	}
	
	// Balances a node after deletion
	private static node adjust (node n) {
		if (n != null && n.getRight() != null && n.getLeft() != null) {
			
			// First case: right child is two levels below n, and left child is a single node
			if ((n.getLevel() == n.getRight().getLevel() + 2) && checkSingle(n.getLeft())) {
				node rNode = new node (n.getKey(), n.getValue());
				rNode.setLevel(n.getLevel() - 1);
				rNode.setLeft(n.getLeft().getRight());
				rNode.setRight(n.getRight());
				
				node newNode = new node (n.getLeft().getKey(), n.getLeft().getValue());
				newNode.setLevel(n.getLevel() - 1);
				newNode.setLeft(n.getLeft().getLeft());
				newNode.setRight(rNode);
				return newNode;
			}
			
			// Second case: right child is two levels below n, and left child is a double node
			else if (n.getLevel() == n.getRight().getLevel() + 2) {
				node lNode = new node (n.getLeft().getKey(), n.getLeft().getValue());
				lNode.setLevel(n.getLevel() - 1);
				lNode.setLeft(n.getLeft().getLeft());
				lNode.setRight(n.getLeft().getRight().getLeft());
				
				node rNode = new node (n.getKey(), n.getValue());
				rNode.setLevel(n.getLevel() - 1);
				rNode.setLeft(n.getLeft().getRight().getRight());
				rNode.setRight(n.getRight());
				
				node newNode = new node (n.getLeft().getRight().getKey(), n.getLeft().getRight().getValue());
				newNode.setLevel(n.getLevel());
				newNode.setLeft(lNode);
				newNode.setRight(rNode);
				return newNode;
			}
			
			// Third case: left child is two levels below n, and n is a single node
			else if ((n.getLevel() == n.getLeft().getLevel() + 2) && checkSingle(n)) {
				n.setLevel(n.getLevel() - 1);
				return split (n);		// call split in case n's right child is a double node
			}
			
			// Fourth case: left child is two levels below n, and n is a double node
			else if (n.getLevel() == n.getLeft().getLevel() + 2) {
				node lNode = new node (n.getKey(), n.getValue());
				lNode.setLevel(n.getLevel() - 1);
				lNode.setLeft(n.getLeft());
				lNode.setRight(n.getRight().getLeft().getLeft());
				
				node rNode = new node (n.getRight().getKey(), n.getRight().getValue());		
				rNode.setLevel((checkSingle(n.getRight().getLeft())? n.getLevel() - 1: n.getLevel()));		
				rNode.setLeft(n.getRight().getLeft().getRight());
				rNode.setRight(n.getRight().getRight());
				
				node newNode = new node (n.getRight().getLeft().getKey(), n.getRight().getLeft().getValue());
				newNode.setLevel(n.getLevel());
				newNode.setLeft(lNode);
				newNode.setRight(split(rNode)); // call split in case the right child of the right child of n is a double
				
				return newNode;
			}
		}
		
		return n;
	}
	
	// finds node of the in-order successor
	private static node succ (node n) {
		if (n.getLeft() == null) {
			return n;
		}
		else {
			return succ (n.getLeft());
		}
	}
	
	// deletes the in-order successor
	private static node delsucc (node n) {
		node newNode;
		
		if (n.getLeft() == null) {
			newNode = n.getRight();
		}
		else {
			newNode = new node (n.getKey(), n.getValue());
			newNode.setRight(n.getRight());
			newNode.setLeft(delsucc(n.getLeft()));
		}
		
		return newNode;	
	}
	
	private static node del (node current, int k) {
		if (current != null) {
			if (k > current.getKey()) {
				current.setRight(del (current.getRight(), k));
				return adjust (current);
			}
			
			else if (k < current.getKey()) {
				current.setLeft(del (current.getLeft(), k));
				return adjust (current);
			}
			
			else { 
				//delete the current node
				if (current.getRight() == null) {
					return current.getLeft();
				}
				else if (current.getLeft() == null) {
					return current.getRight();
				}
				else {
					node successor = succ (current.getRight());
							
					current.setKey(successor.getKey());
					current.setValue(successor.getValue());
					current.setRight(delsucc(current.getRight()));	
					return current;
				}				
			}		
		}
		else {
			return current;
		}
	}
	
	public void delete (int k) {
		this.root = del (this.root, k);
	}
	
	public void deleteKeysArrayList (ArrayList<Integer> a) {
		for (int i = 0; i < a.size(); i++) {
			delete (a.get(i));
		}
	}

}
