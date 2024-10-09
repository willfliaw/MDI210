package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb3 extends Pb {
	public Pb3() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(-1, 0, 0.2));
		contraintes.add(new Constraint(0, -1, 0.2));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return  -Math.log(P.x + 2 * P.y);
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		if (P == null) return null;
		valx = -1 / (P.x + 2 * P.y);
		valy = -2 / (P.x + 2 * P.y);
		return new Couple(valx, valy);
	}
	
	public String toString() {
		return "f = -ln(x + 2 y)\n" +super.toString();
	}

}
