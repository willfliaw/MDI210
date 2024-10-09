package descent.model;

/**
 * Model a linear constraint that is written: coeffx * x + coeffy * y + constant <= 0 .
 * The line of equation: coeffx * x + coeffy * y + constant = 0 is the "border line".
 */
public class Constraint {
	private final double coeffx;
	private final double coeffy;
	private final double constant;
	private Couple gradient;     // the gradient of the fonction : (x, y) -> coeffx * x + coeffy * y + constant
	private Couple unitairyEdgeVector; //a unit vector parallel to the boundary line

	/**
	 * @param coeffx the coefficient of x
	 * @param coeffy the coefficient of y
	 * @param constant the constant
	 */
	public Constraint(double coeffx, double coeffy, double constant) {
		super();
		this.coeffx = coeffx;
		this.coeffy = coeffy;
		this.constant = constant;
		this.gradient = new Couple(coeffx, coeffy);
		this.unitairyEdgeVector = new Couple(-coeffy, coeffx);
		this.unitairyEdgeVector = this.unitairyEdgeVector.mult(1/this.unitairyEdgeVector.norm());
	}
	
	/**
	 * Computes the value of coeffx * x + coeffy * y + constant for a given couple of real 
	 * @param P the point in which the value of the linear function is calculated
	 * @return the result of the computation
	 */
	public double value(Couple P) {
		return this.coeffx * P.x + this.coeffy * P.y + this.constant;
	}

	/**
	 * Searches if the point P satisfies or not the constraint
	 * @param P 	the considered point 
	 * @return 		true if the point P satisfies the constraint, false otherwise
	 */
	public boolean isSatisfied(Couple P) {
		double val = this.value(P);
		return  val <= 0 || Descent.isNull(val);	
	}

	/**
	 * Searches if the point P saturates or not the constraint, that is to say belongs to the boundary line
	 * @param P 	the considered point
	 * @return 		true if the point belongs to the boundary line, false otherwise
	 */
	public boolean isSaturated(Couple P) {
		return Descent.isNull(value(P));	
	}

	/**
	 * Searches the intersection of the boundary line with the half-line parameterized  for t > 0 by: 
	 *  t -> P0 + td 
	 * @param P0 the origin of the half-line with which one seeks the intersection
	 * @param d the director vector of the half-line
	 * @return if there is an intersection, the value of the parameter t corresponding to the intersection,
	 * otherwise -1
	 */
	public double intersection(Couple P0, Couple d) {
		double t;
		double product = d.scalarProduct(gradient);

		if (Descent.isNull(product)) return -1;
		t = (-constant - P0.scalarProduct(gradient)) / product;
		return t;	
	}

	/**
	 * Computes the intersection of the boundary line of the concerned constraint ("this")
	 * with the boundary line of another constraint
	 * @param c the constraint for which the intersection is computed
	 * @return the point of intersection of the two lines, and null if the lines are parallel
	 */
	public Couple intersection(Constraint c) {
		double determinant = this.coeffx * c.coeffy - this.coeffy * c.coeffx;

		if (Descent.isNull(determinant)) return null;

		Couple I = new Couple();
		I.x = (c.constant * this.coeffy - this.constant * c.coeffy) / determinant;
		I.y = (this.constant * c.coeffx - c.constant * this.coeffx) / determinant;

		return I;	
	}

	/**
	 * finds out if c is parallel to this.
	 * If so, looks at whether the gradient vectors are in the same direction; if it is the case, one of the two
	 * constraints contains the whole domain, we can eliminate it.
	 * If the gradient vectors are in opposite directions, the two corresponding half-planes
	 *  have empty intersection, the domain is empty, the two constraints give edges to the domain. 
	 * @param c	constraint compared to "this"
	 * @return 	0 if the constraint c is not parallel to "this"
	 			* <br> -1 if the constraint c is parallel to "this", their gradients vectors are in the same direction
	 			* and that makes c useless
	 			* 1 if the constraint c is parallel to "this", their gradient vectors are in the same direction
	 			* and  that makes "this"  useless
	 * <br> -2 if the constraint c is parallel to "this", their gradient vectors are in opposite directions
	 * and the two half-planes corresponding to the constraints have empty intersection
	 * 2 if the constraint c is parallel to "this", their gradient vectors are in opposite directions
	 * and c and "this" delimit the domain
	 */
	public int isParallel(Constraint c) {
		Couple g = this.getGradient();
		Couple g1 = c.getGradient();

		if (!g.isPerpendicular(c.getUnitaryEdgeVector())) return 0;
		double produit = g.scalarProduct(g1);
		if (produit > 0) 
			if (this.isSatisfied(c.aBorderPoint())) return -1;
			else return 1;
		else 
			if (this.isSatisfied(c.aBorderPoint())) return 2;
			else return -2;
	}

	/**
	 * Looks for a point on the border line
	 * @return a point on the border line
	 */
	public Couple aBorderPoint() {
		if (this.coeffy != 0) return new Couple(0, -this.constant / this.coeffy);
		else return new Couple(-this.constant / this.coeffx, 0);
	}

	@Override
	public String toString() {
		String string;
		boolean sign = true;
		if (this.coeffx == (int)this.coeffx)
			if (this.coeffx == 0) {
				string = "      ";
				sign = false;
			}
			else if (this.coeffx == 1) string = "   x  ";
			else if (this.coeffx == -1) string = "  -x  ";
			else string =  (int)this.coeffx + " x ";
		else 
			string = this.coeffx + " x  ";

		if (this.coeffy == (int)this.coeffy) 
			if (this.coeffy == 0) string += "       <=  ";
			else if (this.coeffy == 1) 
				if (sign) string += " +   y  <=  ";
				else string += "      y >=  ";
			else if (this.coeffy == -1) 
				if (sign) string += " -   y  <=  ";
				else string += "   - y <=  ";
			else if (this.coeffy < 0) 
				if (sign) string += " - " + (-(int)this.coeffy) +  " y  <=  ";
				else string += "     " + ((int)this.coeffy) +  " y  <=  ";
			else string += " + " + (int)this.coeffy +  " y  <=  ";
		else 	
			if (this.coeffy > 0) 
				if (sign) string += "+ " + this.coeffy + " y <= ";
				else string += " " + this.coeffy + " y <= ";
			else if (this.coeffy > 0) string += "         <= ";
			else string += "- " + (-this.coeffy) + " y <= ";	

		if (this.constant == (int)this.constant) string += -(int)this.constant;
		else string += -this.constant;
		return string;
	}

	/**
	 * @return a unit vector of the boundary line
	 */
	public Couple getUnitaryEdgeVector() {
		return this.unitairyEdgeVector;
	}

	/**
	 * @return the gradient of the function: (x, y) -> coeffx * x + coeffy * y + constant
	 * giving the border line
	 */
	public Couple getGradient() {
		return this.gradient;
	}

	/**
	 * @return the coefficient of x in the equation: (x, y) -> coeffx * x + coeffy * y + constant 
	 * giving the border line
	 */
	public double getCoeffx() {
		return this.coeffx;
	}
	
	/**
	 * @return the coefficient of y in the equation (x, y) -> coeffx * x + coeffy * y + constant 
	 * giving the border line
	 */
	public double getCoeffy() {
		return this.coeffy;
	}

	/**
	 * @return the constant in the l'equation : (x, y) -> coeffx * x + coeffy * y + constant 
	 * giving the border line
	 */
	public double getConstante() {
		return this.constant;
	}
}
