package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb12 extends Pb {
	public Pb12() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(0, -1, 0));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return  Math.pow(P.x - 5, 4) + Math.pow(P.y - 2, 4);  
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		valx = 4 * Math.pow(P.x - 5, 3);
		valy = 4 * Math.pow(P.y - 2, 3);
		return new Couple(valx, valy);
	}

	public String toString() {
		return "f = (x - 5) ^ 4 + (y - 2) ^ 4\n" + super.toString();
	}
}