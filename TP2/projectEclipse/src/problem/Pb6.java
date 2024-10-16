package problem;

import java.util.ArrayList;

import descent.model.Constraint;
import descent.model.Couple;
import descent.model.Domain;


public class Pb6 extends Pb {
	public Pb6() {
		ArrayList<Constraint> contraintes = new ArrayList<Constraint>();
		contraintes.add(new Constraint(-1, -1, 1));
		contraintes.add(new Constraint(-1, 0, 0));
		domain = new Domain(contraintes, true);
	}

	public double f(Couple P) {
		return P.x * P.x + P.y * P.y + P.x * P.y + 2 * P.x;   
	}
	
	public Couple gradientf(Couple P) {
		double valx, valy;
		
		valx = 2 * P.x + P.y + 2;
		valy = 2 * P.y + P.x ;
		return new Couple(valx, valy);
	}

	public String toString() {
		return  "f = x^2 + y^2 + x * y + 2x\n" + super.toString();
	}
}
