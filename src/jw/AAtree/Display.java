package jw.AAtree;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Display extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final int treeWidth = 1600;
	private static final int treeHeight = 800;
	private static final int optionsHeight = 40;
	private static final int textFieldSize = 20; 
	private static final int space = 80;
	private static final int radius = 30;
	
	private boolean AAdisplay = true;
	
	AAtree tree; 
	
	JLabel oLabel, curLabel;
	imagePanel optionsPanel, curPanel;
	JButton bInsert, bDelete, bView;
	JTextField input;
	
	BufferedImage image;
	Graphics2D g;
	
	public Display (String title) {
		super (title);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout (new BorderLayout());
			
		optionsPanel = new imagePanel ();
		optionsPanel.setPreferredSize(new Dimension (treeWidth, optionsHeight));
		optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		oLabel = new JLabel ("Type in a list of numbers seperated by spaces:");
		optionsPanel.add(oLabel);
		
		input = new JTextField (textFieldSize);
		optionsPanel.add(input);
		
		bInsert = new JButton ("Insert");
		bInsert.addActionListener(this);
		bInsert.setActionCommand("Insert");
		optionsPanel.add(bInsert);
		
		bDelete = new JButton ("Delete");
		bDelete.addActionListener(this);
		bDelete.setActionCommand("Delete");
		optionsPanel.add(bDelete);
		
		bView = new JButton ("View BST");
		bView.addActionListener(this);
		bView.setActionCommand("Change View");
		optionsPanel.add(bView);
		
		add (optionsPanel, BorderLayout.SOUTH);
		
		curPanel = new imagePanel ();
		curPanel.setPreferredSize(new Dimension (treeWidth, treeHeight));
		curPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		add(curPanel, BorderLayout.CENTER);
		
		image = new BufferedImage (treeWidth, treeHeight, BufferedImage.TYPE_INT_RGB);
		g = image.createGraphics();
		g.setFont (new Font ("Arial Black", Font.PLAIN, 20));
		
		tree = new AAtree ();
		drawTree (tree);
		
		pack();
		setVisible(true);
	}
	
	public void actionPerformed (ActionEvent evt) {
		if (evt.getActionCommand().equals("Change View")) {
			bView.setText((AAdisplay? "View AA":"View BST"));
			AAdisplay = !AAdisplay;
		}
		else {
			String[] text = input.getText().split(" ");
			input.setText("");
			int[] values = new int[text.length];
			
			for (int i = 0; i < values.length; i++) {
				try {
					values[i] = Integer.parseInt (text[i]);
				} catch (NumberFormatException nfe) {}
			}
			
			if (evt.getActionCommand().equals("Insert") && input.getText() != null) {
				tree.insertKeysArray(values);
			}
			else if (evt.getActionCommand().equals("Delete") && input.getText() != null) {		
				tree.deleteKeysArray(values);
			}
		}
		drawTree (tree);
		repaint();
	}
	
	
	// Centers text horizontally and vertically
	private void writeCentered (String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		
		g.drawString(text, x - fm.stringWidth(text)/2, y - fm.getHeight() / 2 + fm.getAscent());
	}
	
	// x is the x-coordinate of the leftmost node, and y is the y-coordinate of the current node
	// returns a pair which contains the x-coordinate for the parent, and the x-coordinate of node n
	// coordinates refer to the centers of the drawn circles
	private Pair drawTree (int x, int y, node n) {
		if (n == null) {
			return new Pair (x, y);
		}
		else {
			Pair pLeft, pRight;
			int deltaX, deltaY;
			double dist;
			
			pLeft = drawTree (x, y + space, n.getLeft());				
			if (n.getLeft() != null) {		
				deltaX = pLeft.getFst() - pLeft.getSnd();
				deltaY = space;
				dist = Math.sqrt (deltaX*deltaX + deltaY*deltaY);
				g.drawLine (pLeft.getSnd() + (int)(radius*deltaX/dist), y + space - (int)(radius*deltaY/dist), pLeft.getFst() - (int)(radius*deltaX/dist), y + (int)(radius*deltaY/dist));
			}
			
			g.drawOval (pLeft.getFst() - radius, y - radius, 2 * radius, 2 * radius);
			writeCentered (Integer.toString(n.getKey()), pLeft.getFst(), y);			
			
			if (n.getRight() != null) {
				if (n.getRight().getLevel() == n.getLevel() && AAdisplay) {
					pRight = drawTree (pLeft.getFst() + space, y, n.getRight());
					g.drawLine(pLeft.getFst() + radius, y, pRight.getSnd() - radius, y);
				}
				else {
					pRight = drawTree (pLeft.getFst() + space, y + space, n.getRight());				
					deltaX = pRight.getSnd() - pLeft.getFst();
					deltaY = space;
					dist = Math.sqrt (deltaX*deltaX + deltaY*deltaY);
					g.drawLine (pLeft.getFst() + (int)(radius*deltaX/dist), y + (int)(radius*deltaY/dist), pRight.getSnd() - (int)(radius*deltaX/dist), y + space - (int)(radius*deltaY/dist));
				}
			}
			else {
				pRight = drawTree (pLeft.getFst() + space, y + space, n.getRight());
			}	
			
			return new Pair (pRight.getFst(), pLeft.getFst());
		}
	}
	
	private void drawTree (AAtree tree) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, treeWidth, treeHeight);	
		g.setColor (Color.RED);
		drawTree ((treeWidth - tree.getWidth() * space)/2 + space/2, (treeHeight - tree.getHeight()*space)/2 + space/2, tree.root);
		
		this.curPanel.setImage(image);
	}
	
	public static void main (String[] args) {
		new Display ("AAtree Simulator");
	}
}
