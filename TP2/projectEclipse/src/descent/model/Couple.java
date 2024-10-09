package descent.model;


/**
 * Model a couple of two doubles; can model a point or a vector of the real plane
 */
public class Couple {
	public double x, y;
	
	public Couple() {
		super();
	}

	public Couple(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds a couple to the concerned couple (this.x, this.y) (without modifying this one) and returns the result
	 * @param v the couple to add
	 * @return the result of the addition
	 */
	public Couple add(Couple v) {
		return new Couple(this.x + v.x, this.y + v.y);
	}
	
	/**
	 * Multiplies the concerned couple (this.x, this.y) by a double (without modifying the concerned couple) 
	 * and returns the result
	 * @param t the multiplier
	 * @return the couple that results from the multiplication
	 */
	public Couple mult(double t) {
		return new Couple(t * this.x, t * this.y);
	}
	
	/**
	 * Subtract a couple from the concerned couple (this.x, this.y) (without modifying this one) 
	 * and return the result
	 * @param v the couple to subtract
	 * @return the result of the subtraction
	 */
	public Couple substract(Couple v) {
		return this.add(v.mult(-1));
	}
	
	/**
	 * Performs the scalar product of the concerned couple (this.x, this.y) with another couple
	 * @param v the couple with which the scalar product is performed
	 * @return the result of the scalar product
	 */
	public  double scalarProduct(Couple v) {
		return this.x * v.x + this.y * v.y;
	}
	
	/**
	 * @return 
	 * - if the couple is modeling a vector, computes the norm of the vector concerned (this.x, this.y)
	 * <br> - if the couple models a point, computes the distance from this point (this.x, this.y) to the origin
	 */
	public double norm() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	/**
	 * When the couple models a point, computes the distance from the concerned point (this.x, this.y) to another point
	 * @param P the point for which we want to compute the distance
	 * @return the distance from point (this.x, this.y) to point P
	 */
	public double distance(Couple P) {
		return this.substract(P).norm();	
	}
	
	/**
	 * When the couple (this.x, this.y) models a vector, indicates if this vector is perpendicular to another vector 
	 * @param v the vector compares to the vector concerned
	 * @return true if v is perpendicular to the vector concerned and false otherwise
	 */
	public boolean isPerpendicular(Couple v) {
		return Descent.isNull(this.scalarProduct(v));
	}
	
	/**
	 * decompose a vector in a basis to two vectors
	 * @param v the vector to decompose
	 * @param v1 the first vector of the basis
	 * @param v2 the second vector of the basis
	 * @return the two components of v on v1 and v2 or null if v1 and v2 are parallel
	 */
	public static Couple decompose(Couple v, Couple v1, Couple v2) {

		double determinant = v1.x * v2.y - v1.y * v2.x;
		
		if (Descent.isNull(determinant)) return null;
		
		double mu1, mu2;
		mu1 = (v.x * v2.y - v.y * v2.x) / determinant;
		mu2 = (v.y * v1.x - v.x * v1.y) / determinant;
			
		return new Couple(mu1, mu2);
	}
	
	@Override
	/**
	 * tests the equality of a couple with another
	 */
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) return false;
		Couple c = (Couple)obj;
		return Descent.isNull(this.x - c.x) && Descent.isNull(this.y - c.y);
	}
	
	/**
	 * truncates a double by imposing the number of digits after the decimal point
	 * @param val the double to truncate, which will not be modified
	 * @param nb the number of digits after the decimal point
	 * @return the double truncated
	 */
	public static double truncate(double val, int nb) {
		double puissance = 1;
		for (int i = 0; i < nb; i++) puissance *= 10;
		return Math.round(puissance * val) /puissance;
	}

	/** 
	 * truncates the two components of a couple and returns the result; the concerned couple (this.x, this.y) is not modified 
	 * @param nb the number of digits behind the decimal point
	 * @return the truncated couple 
	 */
	
	public Couple truncate(int nb) {
		return new Couple(Couple.truncate(this.x, nb), Couple.truncate(this.y, nb));	
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}	
	
}
