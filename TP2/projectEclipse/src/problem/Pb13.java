package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb13 extends Pb {
	public Pb13() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(0, -1, 0));
		contraintes.add(new Constraint(-1, 0, -1));
		contraintes.add(new Constraint(1, 0, -1));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return 2* P.x * P.x * P.x * P.x + P.y  * P.y;
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		if (P == null) return null;
		valx = 8 * P.x * P.x * P.x ;
		valy = 2 * P.y;
		return new Couple(valx, valy);
	}
	
	public String toString() {
		return "f = 2 * x^4 + y^2\n" +super.toString();
	}
}
