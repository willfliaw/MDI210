package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb2 extends Pb {
	public Pb2() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return  P.x * P.x +  P.y * P.y * P.y * P.y;
	}

	public Couple gradientf(Couple P) {
		double valx, valy;

		valx = 2 * P.x;
		valy = 4* P.y * P.y * P.y;
		return new Couple(valx, valy);
	}
	

	public String toString() {
		String string = "f = x^2 + y^4\n" +super.toString();
		return string;
	}
}

