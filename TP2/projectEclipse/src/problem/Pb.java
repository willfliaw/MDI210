package problem;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;

/**
 * Models a problem of minimizing a function on a domain limited by linear constraints
 */
public abstract class Pb {
	public Domain domain;

	/**
	 * The function to optimize
	 * @param P		the couple  (x, y) 
	 * @return		the value of the function
	 */
	public abstract double f(Couple P);

	/**
	 * The gradient of the function to minimize
	 * @param P the point where the gradient is computed
	 * @return a variable of type Couple containing the gradient of f at point P
	 */
	public abstract Couple gradientf(Couple P);

	/**
	 * If t-> P0 + td is the parametric equation of a half-line,
	 * computes the derivative of the function t -> f (P0 + td)
	 * @param P0 the origin of the half-line
	 * @param d the direction of the half-line
	 * @param t the value of the parameter for which the derivative is calculated
	 * @return the result of the computation
	 */
	public double phiDerivative(Couple P0,  Couple d, double t) {
		Couple P = P0.add(d.mult(t));
		return d.scalarProduct(gradientf(P));
	}
	
	public String toString() {
		String string ;
		if (this.domain.getConstraints().size() != 0 ) {
			string = "with:\n";
			for (Constraint c : this.domain.getConstraints()) 
				string += c + "\n";
		}
		else string = "No constraint";
		return string;
	}
	
	/**
	 * @return the domain of the problem
	 */
	public Domain getDomaine() {
		return domain;
	}

}