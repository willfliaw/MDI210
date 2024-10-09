package descent.model;

import java.util.ArrayList;

/**
 * Models a domain defined by a set of linear constraints
  * We suppose that there are not three lines determining the constraints that intersect in the same point.
 */
public class Domain {
	private ArrayList<Constraint> constraints; 
	private double lowerBound_x;
	private double upperBound_x;
	private double lowerBound_y;
	private double upperBound_y;
	private boolean empty;
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<Couple> corners = new ArrayList<Couple>();

	/**
	 * Constructor
	 * @param constraints all the constraints of the domain
	 * @param compute indicates whether or not to compute the edges, the corners of the domain, 
	 * 		  the boundaries of the domain
	 */
	public Domain(ArrayList<Constraint> constraints, boolean compute) {
		super();
		this.constraints = constraints;
		if (compute) {
			this.computeEdgesDomain();
			this.refine();
			this.computeCornersDomain();
			this.computeBounds();
		}
	}

	/**
	 * Searches if a given point is or is not in the domain
	 * @param P the given point
	 * @return true if P is in the domain, false otherwise
	 */
	public boolean isFeasible(Couple P) {
		boolean in = true;

		for (Constraint c : this.constraints) {
			if (!c.isSatisfied(P)) {
				in = false;
				break;
			}
		}
		return in;
	}

	/**
	 * Finds if a given point is on an edge of the domain
	 * @return if P is on an edge, a constraint on which is P
	 *         otherwise null.   
	 */
	public Constraint isOnEdge(Couple P) {
		for (Constraint c : this.constraints) 
			if (c.isSaturated(P)) return c;
		return null;
	}

	/**
	 * Given two constraints, searches if the intersection of their border lines is a corner of the domain
	 * @param c1 the first constraint
	 * @param c2 the second constraint
	 * @return if the two lines intersect at a corner of the domain, the point of intersection
	 *         otherwise null
	 */
	public Couple isCorner(Constraint c1, Constraint c2) {
		Couple P;
		boolean in = true;

		if (c1 == c2) return null;
		P = c1.intersection(c2);
		if (P == null) return null;
		for (Constraint c : this.constraints) {
			if ((c1 != c) && (c2 != c) && !c.isSatisfied(P)) in = false;
		}
		if (in) return P;
		return null;
	}

	/**
	 * Searches if a given point is in a corner of the domain
	 * @param P the  considered point
	 * @return if P is in a corner of the domain, an array containing the two constraints forming the corner,
	 *         otherwise null
	 */
	public Constraint[] isCorner(Couple P) {
		Constraint[] theBoth = {null, null}; 
		Constraint c1;

		if (!this.isFeasible(P)) return null;
		c1 = this.isOnEdge(P);
		if (c1 == null) return null;
		theBoth[0] = c1;
		for (Constraint c2 : constraints) {
			if (c2 == c1) continue;
			if (c2.isSaturated(P)) {
				theBoth[1] = c2;
				break;
			}
		}
		if (theBoth[1] != null) return theBoth;
		return null;
	}

	/**
	 * Searches an intersection of a half-line with any edge of the domain; if the origin of the half-line
	 * is on one of the edges, it is not considered as intersection
	 * @param P0 the origin of the half-line
	 * @param d the direction of the half-line
	 * @return if there is an intersection, the value of t (t > 0) such that PO + td is on an edge of the domain
	 * otherwise -1
	 */
	public double intersection(Couple P0, Couple d) {
		double t;
		Couple P;
		
		if (d == null) return 0;
		for (Constraint c : constraints) {
			if (c.isSaturated(P0)) continue;
			t = c.intersection(P0, d);
			if (t >= 0) {
				P = P0.add(d.mult(t));
				if (this.isFeasible(P)) return t;
			}
		}
		return -1;
	}

	/**
	 * Computes all edges of the domain
	 */
	private void computeEdgesDomain() {
		for (Constraint c : constraints) {
			edges.add(new Edge(this, c));
		}
	}

	/**
	 * Removes constraints that do not delimit the domain
	 */
	private void refine() {
		Edge toRemove;

		do {
			toRemove = null;
			for (Edge b : edges)  {
				if (b.isEmpty()) {
					toRemove = b;
					break;
				}
			}
			if (toRemove != null) {
				edges.remove(toRemove);
				constraints.remove(toRemove.getConstraint());			
			}
		}while (toRemove != null);
	}
	
	/**
	 * Computes the corners of the domain; each corner is a couple containing the coordinates of the corner
	 */
	private void computeCornersDomain() {		
		Couple inter;

		for (Constraint c1 : this.constraints) 
			for (Constraint c2 : this.constraints) {
				inter = isCorner(c1, c2);		
				if (inter != null) corners.add(inter);
			}
	}

	/**
	 * Computes the minimum and the maximum of the abscissas of the domain as well as the ordinates.
	 */
	private void computeBounds() {
		ArrayList<Couple> corners = getCorners();
		Constraint c;
		
		this.lowerBound_x = 0;
		this.upperBound_x = 0;
		this.lowerBound_y = 0;
		this.upperBound_y = 0;
		for (Couple coin : corners)  {	
			if (coin.x < lowerBound_x) this.lowerBound_x = coin.x;
			if (coin.x > upperBound_x) this.upperBound_x = coin.x;
			if (coin.y < lowerBound_y) this.lowerBound_y = coin.y;
			if (coin.y > upperBound_y) this.upperBound_y = coin.y;
		}
		for (Edge b : edges) {
			if ((b.isInfinite()) && (b.getEnds()[0] == null)) {
				c = b.getConstraint();
				if (c.getCoeffx() != 0) {
					double valx = -c.getConstante() / c.getCoeffx();
					if (valx < lowerBound_x) this.lowerBound_x = valx;
					else if (valx > upperBound_x) this.upperBound_x = valx;
				}
				if (c.getCoeffy() != 0) {
					double valy = -c.getConstante() / c.getCoeffy();
					if (valy < lowerBound_y) this.lowerBound_y = valy;
					else if (valy > upperBound_y) this.upperBound_y = valy;
				}
			}
		}
	}

	@Override
	public String toString() {
		String string = "";
		for (Constraint c : constraints) string += c + "\n";
		return string;
	}
	
	/**
	 * @return the list of domain constraints, after removing unnecessary constraints
	 */
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}	
	
	/**
	 * @return the list of edges of the domain
	 */
	public ArrayList<Edge> getBorders() {
		return edges;
	}
	

	/**
	 * @return the list of corners of the domain; each corner is a couple containing the coordinates of the corner
	 */
	public ArrayList<Couple> getCorners()  {
		return corners;
	}
	
	/**
	 * Notes that the domain is empty
	 */
	public void setEmpty() {
		empty = true;
	}
	
	/**
	 * @return true if the domain is empty, false otherwise
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * @return the largest abscissa of the points of the domain
	 */
	public double getUpperBound_x() {
		return upperBound_x;
	}
	
	/**
	 * @return the largest ordinate of the points of the domain
	 */
	public double getUpperBound_y() {
		return upperBound_y;
	}

	/**
	 * @return the smallest abscissa of the points of the domain
	 */
	public double getLowerBound_x() {
		return lowerBound_x;
	}

	/**
	 * @return the smallest ordinate of the points of the domain
	 */
	public double getLowerBound_y() {
		return lowerBound_y;
	}
}
