package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb8 extends Pb {
	public Pb8() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(-1, 0, 1));
		contraintes.add(new Constraint(0, -1, 0));
		contraintes.add(new Constraint(-1, -1, 4));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return P.x * P.x * P.x + 4 *P.y * P.y - P.x * P.y;   
	}
	
	public Couple gradientf(Couple P) {
		double valx, valy;
		
		valx = 3 * P.x * P.x - P.y;
		valy = 8 * P.y - P.x ;
		return new Couple(valx, valy);
	}

	public String toString() {
		return "f = x^3 + 4*y^2 - x * y\n" + super.toString();
	}
}
