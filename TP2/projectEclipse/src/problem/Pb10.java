package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb10 extends Pb {
	public Pb10() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		if (P.x == 0 && P.y == 0) throw new ArithmeticException();
		return 1 / Math.sqrt(P.x * P.x + P.y * P.y);   
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		if (P.x == 0 && P.y == 0) throw new ArithmeticException();
		valx = -P.x * Math.pow(P.x * P.x + P.y * P.y, -1.5);
		valy = -P.y * Math.pow(P.x * P.x + P.y * P.y, -1.5) / 2;
		return new Couple(valx, valy);
	}

	public String toString() {
		return "f = 1 / sqrt(x^2 + y^2 )\n" + super.toString();
	}
}