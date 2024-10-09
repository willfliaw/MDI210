package descent.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model an edge of the domain; it can be a straight line, a half-line, a segment, or be empty
 *
 */
public class Edge {
	private Couple[] ends = new Couple[2]; // contains both ends when it comes from a segment,
										   // the end if it is a half-line
	private boolean infinite = false;
	private Couple d = null; // if it is a half-line, the direction of this half-line
	private boolean empty = false;
	private Constraint constraint; // the constraint corresponding to this edge

	public Edge(Domain domain, Constraint c) {
		super();
		int nb = 0;
		this.constraint = c;
		ArrayList<Constraint> constraints = domain.getConstraints();
		Constraint coupe = null;

		// computes intersections with other straight lines
		for (Constraint c1 : constraints) {
			if (domain.isCorner(c, c1) != null) {
				Couple inter = c.intersection(c1);
				if (nb == 0) coupe = c1;
				ends[nb] = inter;
				nb++;
			}
		}

		if (nb == 0) {
			infinite = true;
			for (Constraint c1 : constraints) {
				if (c != c1) {
					int parallel = c.isParallel(c1);
					if (parallel == 0) {
						this.empty = true;
					}
					if (parallel == -2) {
						domain.setEmpty();
					}
					if (parallel == -1) this.empty = true;		
				}
			}
		}

		if((nb == 2) && (Descent.isNull(ends[0].distance(this.ends[1])))) this.empty = true;	
		else if (nb == 1) {
			d = c.getUnitaryEdgeVector();
			if (d.scalarProduct(coupe.getGradient()) > 0) d = d.mult(-1);
			infinite = true;
		}		
	}

	@Override
	public String toString() {
		return "Edge [d=" + this.d + ", ends =" + Arrays.toString(this.ends) + ", infinite ="
		+ this.infinite + ", empty =" + this.empty + "], constraint : " + this.constraint;
	}

	/**
	 * @return true if the edge is a line or a half-line, false if it's a segment
	 */
	public boolean isInfinite() {
		return this.infinite;
	}

	/**
	 * @return true if the constraint does not limit the domain, false otherwise
	 */
	public boolean isEmpty() {
		return this.empty;
	}

	/**
	 * @return if it is a half-line, the direction of this half-line, as an unitary vector, otherwise null
	 */
	public Couple getD() {
		return this.d;
	}

	/**
	* @return - when it comes from a segment, both ends,
	* <br> - when it comes from a half-line, its extremity in the box of index 0, null in the other box
	* <br> - when it comes from a straight line, null in both boxes
	 */
	public Couple[] getEnds() {
		return ends;
	}
	
	/**
	 * @return the constraint corresponding to this edge
	 */
	public Constraint getConstraint() {
		return constraint;
	}
}
