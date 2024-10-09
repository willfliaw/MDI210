package descent.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import descent.model.Edge;
import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Descent;
import descent.model.Domain;

/**
 * Models the panel in which the lines are drawn
 */
public class TracePanel extends JPanel {	
	protected static final long serialVersionUID = 1L;
	protected int width = 5000, height = 5000;
	protected Dimension dim = new Dimension(width, height);
	protected Domain domain;
	protected double realMargin = 0;
	protected int marginPanelX ;
	protected int marginPanelY ;
	protected View view;
	protected ArrayList<Couple> corners;
	protected Domain viewDdomain;
	protected ArrayList<Couple> theP = new ArrayList<Couple>(); 
	protected Dimension dimView = new Dimension(800, 800);
	protected Couple copyDirection;
	protected double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
	private boolean in;
	
	protected TracePanel(View view) {
		this.view = view;
		marginPanelX = (this.width - (int)dimView.getWidth()) / 2 + 50;
		marginPanelY = (this.height - (int)dimView.getHeight()) / 2  + 50;
		ControllerPanelTrace controleurSouris = new ControllerPanelTrace(view);
		addMouseListener(controleurSouris);
		addMouseMotionListener(controleurSouris);
		setPreferredSize(dim);
	}
	
	protected void ajustDimensionView(Couple P) {	
		if (P != null) {
			if (P.x < this.xMin) xMin = P.x;
			else if (P.x > this.xMax) xMax = P.x;
			if (P.y < this.yMin) yMin = P.y;
			else if (P.y > this.yMax) yMax = P.y;
		}
		this.makeOrthogonal();
	}
	protected void makeOrthogonal() {
		if ((this.height - 2 * this.marginPanelY) *(this.xMax - this.xMin) > 
		(this.width - 2 * this.marginPanelX) * (this.yMax - this.yMin)) 
			this.yMax = (this.height - 2 * this.marginPanelY) * (this.xMax - this.xMin) / (this.width - 2 * this.marginPanelX) + this.yMin;
		else this.xMax = (this.width - 2 * this.marginPanelX) * (this.yMax - this.yMin) / (this.height - 2 * this.marginPanelY) + this.xMin;
	}
	
	// transforms the equation of a constraint into the measurements of the window 
	protected Constraint viewConstraint(Constraint c)  {
		double coeffxVue, coeffyVue, constanteVue;
		
		double facteurx = (this.xMax - this.xMin) / (this.dim.width - 2 * this.marginPanelX);
		double facteury = (this.yMax - this.yMin) / (this.dim.height - 2 * this.marginPanelY);
		coeffxVue = c.getCoeffx() * facteurx;
		coeffyVue = -c.getCoeffy() * facteury;
		constanteVue = c.getCoeffx() * (this.xMin - this.marginPanelX * facteurx) +
		c.getCoeffy() * (this.yMin + (this.dim.height - this.marginPanelY) * facteury) + c.getConstante();
		
		return new Constraint(coeffxVue, coeffyVue, constanteVue);	
	}
		
	// Converts the coordinates of P in the view frame
	protected Couple convert(Couple P) {
		if (P == null) return null;
		double abs = (this.dim.width - 2 * this.marginPanelX) * (P.x - this.xMin) / (this.xMax - this.xMin) + this.marginPanelX;
		double ord = this.dim.height + (2 * this.marginPanelY - this.dim.height) * (P.y - this.yMin) / (this.yMax - this.yMin) - this.marginPanelY;
		
		return new Couple(abs, ord);
	}
	
	// Inverse of the method convert
	protected Couple inverseConvert(Couple P) {
		if (P == null) return null;
		double abs = this.xMin + (P.x - this.marginPanelX) * (this.xMax - this.xMin) / (this.dim.width - 2 * this.marginPanelX);
		double ord = this.yMin + (P.y + this.marginPanelY - this.dim.height) * (this.yMax - this.yMin)/(2 * this.marginPanelY - this.dim.height);
		return new Couple(abs, ord);
	}

	// Converts a vector in the View frame
	protected Couple vectorInView(Couple P, Couple d) {
		Couple dSym = new Couple(d.x * (this.width - 2 * this.marginPanelX) / (this.xMax - this.xMin) , 
				-d.y * (this.height - 2 * this.marginPanelY) / (this.yMax - this.yMin));
		double t = viewDdomain.intersection(P, dSym);
		return P.add(dSym.mult(t));
	}

	protected void drawVector(Couple origin, Couple d, Graphics2D g, boolean toBeNorm) {
		g.setStroke(new BasicStroke(3));
		if (d == null) return;
		Couple diagonal = new Couple(this.xMax - this.xMin, this.yMax - this.yMin);
		
		// The following three lines are used to norm
		double longueurDiagonale = diagonal.norm();
		double norme = d.norm();
		if (toBeNorm) d = d.mult(longueurDiagonale/(10 * norme));
		
		// Computation of the vector in the View frame
		Couple extr = origin.add(d);
		Couple origineVue = this.convert(origin);
		Couple extrVue = this.convert(extr);

		Couple v = extrVue.add(origineVue.mult(-1));
		if (v.norm() < 5) g.drawLine((int) origineVue.x, (int) origineVue.y, (int) extrVue.x, (int) extrVue.y);	
		// Drawing of the vector
		else this.drawArrow(g, (int) origineVue.x, (int) origineVue.y, (int) extrVue.x, (int) extrVue.y);	

		g.setStroke(new BasicStroke(1));
	}

	// Draws the path
	protected void drawThePath(Graphics2D g) {
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(3));
		if (theP.size() < 2)return;
		Couple XVue1 = this.convert(theP.get(0));
		Couple XVue2;
		
		for (int i = 1; i < theP.size(); i++) {
			XVue2 = this.convert(theP.get(i));
			g.drawLine((int)XVue1.x, (int)XVue1.y, (int)XVue2.x, (int)XVue2.y);
			XVue1 = XVue2;
			if (i == theP.size() - 1) g.setColor(Color.BLACK);
			g.fillOval((int)XVue1.x - 5, (int)XVue1.y - 5, 10, 10);
		}
		g.setStroke(new BasicStroke(1));
	}
	
	protected void drawAxes(Graphics2D g) {
		Couple p1;
		Couple p2;
		Couple d;
		Couple origine = new Couple(0, 0);
		
		g.setColor(Color.GRAY);
		p1 = this.convert(origine);
		d = new Couple(0, 1);
		p2 = this.vectorInView(p1, d);
		this.drawArrow(g, (int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		d = new Couple(1, 0);
		p2 = this.vectorInView(p1, d);
		this.drawArrow(g, (int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		d = new Couple(0, -1);
		p2 = this.vectorInView(p1, d);
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		d = new Couple(-1, 0);
		p2 = this.vectorInView(p1, d);
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		g.setColor(Color.BLACK);
	}
	
	protected void drawBorders(Edge b, Graphics2D g) {
		Couple p1 = null;
		Couple p2 = null;
		Couple d;
		
		if (!b.isInfinite()) {
			p1 = this.convert(b.getEnds()[0]);
			p2 = this.convert(b.getEnds()[1]);
		}
		else {
			if (b.getEnds()[0] != null) {
				p1 = this.convert(b.getEnds()[0]);
				d = b.getD();
				p2 = this.vectorInView(p1, d);
			}
			else {
				Couple inter;
				Constraint c = b.getConstraint();
				Constraint cVue = this.viewConstraint(c);
				
				inter = this.viewDdomain.getConstraints().get(0).intersection(cVue);
				if (inter != null) {
					p1 = inter;
					p2 = this.viewDdomain.getConstraints().get(2).intersection(cVue);
				}
				else {
					p1 = this.viewDdomain.getConstraints().get(1).intersection(cVue);
					p2 = this.viewDdomain.getConstraints().get(3).intersection(cVue);
					
				}
			}
		}
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
	}
	
	protected void drawDomain(Graphics2D g) {	
		Couple p;
	
		if (this.domain.isEmpty()) return;
		g.setStroke(new BasicStroke(2));
		for (Edge b : this.domain.getBorders()) {
			this.drawBorders(b, g);
		}
		
		for (Couple coin : corners) {
			p = this.convert(coin);		
			g.drawString(coin.truncate(2).toString(), (int)p.x + 15,(int) p.y);
		}
		g.setStroke(new BasicStroke(1));
	}
	
	@Override
	protected void paintComponent(Graphics g1) {
		Couple direction;
		
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		Descent descente = view.descent;
		if (descente == null) return;
		Couple P  = descente.getP();
		if (P == null) {
			this.drawAxes(g);
			this.drawDomain(g);
			if ((descente != null) && !descente.reachesItsMinimum())  {
				this.drawThePath(g);	
				g.setColor(Color.BLUE);
				Couple dernierP = theP.get(theP.size() - 1);
				this.drawVector(dernierP, descente.getPb().gradientf(dernierP), g, false);
				g.setColor(Color.GREEN);
				this.drawVector(dernierP, copyDirection, g, true);
			}
			return;
		}
		this.drawAxes(g); 
		this.drawDomain(g);
    	Couple PVue = this.convert(P);
    	g.fillOval((int)PVue.x - 5, (int)PVue.y - 5, 10, 10);
    	if (!in) return;
		this.drawThePath(g);	
    	g.setColor(Color.BLUE);
		this.drawVector(P, descente.getPb().gradientf(P), g, false);
		g.setColor(Color.GREEN);
		if(descente.isFinished()) return;
		direction = descente.getDirectionToFollow();
		this.drawVector(P, direction, g, true);
		copyDirection = direction;
	}

	protected void center() {
		Couple centreVue;
		if (this.view.descent.getP() != null) centreVue = this.view.panelTrace.convert(this.view.descent.getP());
		else centreVue = this.view.panelTrace.convert(new Couple((this.view.panelTrace.xMax + this.view.panelTrace.xMin)/2,
				(this.view.panelTrace.yMax + this.view.panelTrace.yMin)/2));
		Point point = new Point((int)centreVue.x - this.view.panelTrace.dimView.width / 2, 
				(int)centreVue.y - this.view.panelTrace.dimView.height / 2);
		this.view.scrollPane.getViewport().setViewPosition(point);
	}
	/**
	 * To visualize the starting position of the descent
	 * @param PReal the starting position
	 */
	protected void validerPosition(Couple PReal) {
		if (view.descent == null) return;

		this.view.descent.setP(PReal);
		if (!this.view.descent.getPb().getDomaine().isFeasible(PReal)) {
			this.view.gadgetsPanel.indication.setText("Le point n'est pas dans le domaine");
			this.view.gadgetsPanel.endIndication.setText("Choisissez un autre point");
			this.in = false;
		}
		else {
			this.view.gadgetsPanel.indication.setText("Vous pouvez demarrer");
			this.view.gadgetsPanel.endIndication.setText("     ");
			this.view.KarushKuhnTucker();
			this.in = true;
		}
		Couple PCourt = PReal.truncate(2);
		view.gadgetsPanel.valueX.setText((Double.toString(PCourt.x)));
		view.gadgetsPanel.valueY.setText(Double.toString(PCourt.y));
		view.panelTrace.repaint();
	}
	
	// Draw an arrow from (x,y) to (xx, yy)
	protected void drawArrow(Graphics g, int x, int y, int xx, int yy ){
		float arrowWidth = 6.0f ;
		float theta = 0.423f ;
		int[] xPoints = new int[ 3 ],yPoints = new int[ 3 ] ;
		float[] vecLine = new float[ 2 ] ;
		float[] vecLeft = new float[ 2 ] ;
		float fLength;
		float th;
		float ta;
		float baseX, baseY ;
		
		xPoints[ 0 ] = xx ;
		yPoints[ 0 ] = yy ; // build the line vector
		vecLine[ 0 ] = (float)xPoints[ 0 ] - x ;
		vecLine[ 1 ] = (float)yPoints[ 0 ] - y ;
		// build the arrow base vector - normal to the line
		vecLeft[ 0 ] = -vecLine[ 1 ] ;
		vecLeft[ 1 ] = vecLine[ 0 ] ;
		// setup length parameters
		fLength = (float)Math.sqrt( vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1] ) ;
		th = arrowWidth / ( 2.0f * fLength ) ;
		ta = arrowWidth / ( 2.0f * ( (float)Math.tan( theta ) / 2.0f ) * fLength ) ;
		// find the base of the arrow
		baseX = ( (float)xPoints[ 0 ] - ta * vecLine[0]);
		baseY = ( (float)yPoints[ 0 ] - ta * vecLine[1]);
		// build the points on the sides of the arrow
		xPoints[1] = (int)( baseX + th * vecLeft[0] );
		yPoints[1] = (int)( baseY + th * vecLeft[1] );
		xPoints[2] = (int)( baseX - th * vecLeft[0] );
		yPoints[2] = (int)( baseY - th * vecLeft[1] );
		g.drawLine( x, y, (int)baseX, (int)baseY ) ;
		g.fillPolygon( xPoints, yPoints, 3 ) ;
	}

	protected void setDescent(Descent descent) {
		if (descent == null) return;
		ArrayList<Constraint> viewConstraints = new ArrayList<Constraint>(); 
		viewConstraints.add(new Constraint(-1, 0, 0)); 
		viewConstraints.add(new Constraint(0, -1, 0)); 
		viewConstraints.add(new Constraint(1, 0, -this.width)); 
		viewConstraints.add(new Constraint(0, 1, -this.height));
		viewDdomain = new Domain(viewConstraints, false);
		this.domain = descent.getPb().getDomaine();
		this.xMin = domain.getLowerBound_x();
		this.xMax = domain.getUpperBound_x();
		this.yMin = domain.getLowerBound_y();
		this.yMax = domain.getUpperBound_y();

		if (this.xMax - this.xMin < 1) {
			this.xMax += 2;
			this.xMin -= 2;
		}
		else {
			this.xMax += 0.1;
			this.xMin -= 0.1;
		}
		if (this.yMax - this.yMin < 1) {
			this.yMax += 2;
			this.yMin -= 2;
		}
		else {
			this.yMax += 0.1;
			this.yMin -= 0.1;
		}
		this.corners = domain.getCorners();
		this.ajustDimensionView(null);
		this.repaint();
	}
}

