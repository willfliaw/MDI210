package simplex;

import java.io.IOException;


/**
 * Contains the main method
 */
public class Main { 
	public static void main(String[] arg) throws IOException {
		Simplex simplex = new Simplex();
		Scenario scenario = new Scenario(simplex);
		simplex.view = scenario;
		simplex.controller = scenario.controller;
	}
}
