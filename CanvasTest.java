import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CanvasTest implements MouseListener, MouseMotionListener, ActionListener, ItemListener{
	JFrame fr;
	Canvas canvas;
	int x, y, x1, y1, x2, y2;
	Panel toolbar;
		Choice colorSelection;
			JColorChooser colorChooser;
			Color color;
		TextField lineThickness;
		Label beginSpacer, lineSpacer, createSpacer;
		Button ovalTraceButton, rectTraceButton, starTraceButton;
		Button rectCreateButton, circCreateButton, clearButton;

	enum Selection {OVAL_TRACE, RECT_TRACE, STAR_TRACE, RECT_CREATE, CIRC_CREATE, CLEAR};
	Selection ss;

	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		x1 = e.getX();
		y1 = e.getY();
	}
	public void mouseReleased(MouseEvent e){
		x2 = e.getX();
		y2 = e.getY();
		if (ss == Selection.RECT_CREATE || ss == Selection.CIRC_CREATE || ss == Selection.CLEAR)
			canvas.repaint();
	}
	public void mouseDragged(MouseEvent e){
		x = e.getX();
		y = e.getY();
		if (ss == Selection.OVAL_TRACE || ss == Selection.RECT_TRACE || ss == Selection.STAR_TRACE)
			canvas.repaint();
	}
	public void mouseMoved(MouseEvent e){}

	public void itemStateChanged(ItemEvent e){
		if (e.getItem().equals("Red")) color = Color.RED;
		else if (e.getItem().equals("Orange")) color = Color.ORANGE;
		else if (e.getItem().equals("Yellow")) color = Color.YELLOW;
		else if (e.getItem().equals("Green")) color = Color.GREEN;
		else if (e.getItem().equals("Blue")) color = new Color(28,142,224);
		else if (e.getItem().equals("Other (Color Wheel)")){
			color = colorChooser.showDialog(null,"Select Color", Color.RED);
			System.out.println(color);
		}
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == ovalTraceButton){
			ss = Selection.OVAL_TRACE;
		}
		if (e.getSource() == rectTraceButton){
			ss = Selection.RECT_TRACE;
		}
		if (e.getSource() == starTraceButton){
			ss = Selection.STAR_TRACE;
		}
		if (e.getSource() == rectCreateButton){
			ss = Selection.RECT_CREATE;
		}
		if (e.getSource() == circCreateButton){
			ss = Selection.CIRC_CREATE;
		}
		if (e.getSource() == clearButton){
			ss = Selection.CLEAR;
		}
	}

	class CanvasInnerClass extends Canvas{
		@Override
		public void paint(Graphics g){
			g.setColor(color);
			for(int i=0; i<1024; i=i+5)		
				g.drawLine(i,0,768,i);
			if (!(x==0 && y==0) && ss == Selection.OVAL_TRACE)
				g.fillOval(x,y,new Integer(lineThickness.getText()).intValue(),new Integer(lineThickness.getText()).intValue());
			else if (!(x==0 && y==0) && ss == Selection.RECT_TRACE)
				g.fillRect(x,y,new Integer(lineThickness.getText()).intValue(),new Integer(lineThickness.getText()).intValue());
			else if (!(x==0 && y==0) && ss == Selection.STAR_TRACE)
				//g.fillPolygon(x,y,new Integer(lineThickness.getText()).intValue(),new Integer(lineThickness.getText()).intValue());
				g.fillOval(x,y,new Integer(lineThickness.getText()).intValue(),new Integer(lineThickness.getText()).intValue());
			else if (ss == Selection.RECT_CREATE || ss == Selection.CIRC_CREATE || ss == Selection.CLEAR){
				System.out.println("Let's create!");
			}
			if(ss == Selection.RECT_CREATE){
				/*if(x1>x2 && y2>y1)
					g.drawRect(x1,y1,x1-x2,y2-y1);
				else if (x2>x1 && y1>y2)
					g.drawRect(x1,y1,x2-x1,y1-y2);
				else if (x1>x2 && y1>y2)
					g.drawRect(x1,y1,x1-x2,y1-y2);
				else g.drawRect(x1,y1,x2-x1,y2-y1);*/
				g.drawRect(x1,y1,x2-x1,y2-y1);
			}
			if(ss == Selection.CIRC_CREATE){
				g.drawOval(x1,y1,x2-x1,y2-y1);
			}
			if(ss == Selection.CLEAR){
				g.clearRect(x1,y1,x2-x1,y2-y1);
			}

		}
		@Override
		public void update(Graphics g){
			//g.drawLine(x1,y1,x2,y2);
			paint(g);
		}
	}

	CanvasTest(){
		fr = new JFrame("Paint");
			fr.setSize(1024,768);
			fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new CanvasInnerClass();
			canvas.setBackground(new Color(253,231,86));
			canvas.setForeground(new Color(28,142,224));
			canvas.addMouseListener(this);
			canvas.addMouseMotionListener(this);
		fr.add("Center", canvas);

		toolbar = new Panel();
			ss = Selection.OVAL_TRACE;
			beginSpacer = new Label("Color: ");				toolbar.add(beginSpacer);
			colorSelection = new Choice();					toolbar.add(colorSelection);
				colorSelection.add( "Red" );
				colorSelection.add( "Orange" );
				colorSelection.add( "Yellow" );
				colorSelection.add( "Green" );
				colorSelection.add( "Blue" );
				colorSelection.add( "Other (Color Wheel)" );
				colorSelection.addItemListener( this );
				colorChooser = new JColorChooser();
			lineSpacer = new Label("   Line: ");			toolbar.add(lineSpacer);
			lineThickness = new TextField("5", 3);			toolbar.add(lineThickness);
				//lineThickness.addKeyListener( new KeyAdapter(){} );
			ovalTraceButton = new Button("◯");				toolbar.add(ovalTraceButton);	ovalTraceButton.addActionListener(this);
			rectTraceButton = new Button("▢");				toolbar.add(rectTraceButton);	rectTraceButton.addActionListener(this);
			starTraceButton = new Button("☆");				toolbar.add(starTraceButton);	starTraceButton.addActionListener(this);
			createSpacer = new Label("   Create: ");		toolbar.add(createSpacer);
			rectCreateButton = new Button("Rectangle");		toolbar.add(rectCreateButton);	rectCreateButton.addActionListener(this);
			circCreateButton = new Button("Circle");		toolbar.add(circCreateButton);	circCreateButton.addActionListener(this);
			clearButton = new Button("CLEAR");				toolbar.add(clearButton);		clearButton.addActionListener(this);
		fr.add("North", toolbar);

		fr.setVisible(true);
	}

	public static void main(String[] args){
		new CanvasTest();
	}
}
