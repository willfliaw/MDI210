package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb11 extends Pb {
	public Pb11() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(0, -1, 0));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return  P.x + P.y;   
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		valx = 1;
		valy = 1;
		return new Couple(valx, valy);
	}

	public String toString() {
		return "f = x + y\n" + super.toString();
	}
}