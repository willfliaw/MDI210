package descent.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import problem.Pb;

import descent.model.Couple;
import descent.model.Descent;

 public class ControllerGadgets implements ActionListener {
	private View view;
	protected ControllerGadgets (View view) {
		this.view = view;
	}

	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();

		if (command.equals("start")) {	
			if ((this.view.thread != null) && this.view.thread.isAlive()) return;
			if (this.view.hasStarted) this.reinit();
			if (!this.view.pbReady) this.validatePb();
			Couple PReel = new Couple(new Double(this.view.gadgetsPanel.valueX.getText()),
					new Double(this.view.gadgetsPanel.valueY.getText()));

			if (!this.view.descent.getPb().getDomaine().isFeasible(PReel)) {
				this.view.gadgetsPanel.indication.setText("Le point n'est pas dans le domaine");
				this.view.gadgetsPanel.endIndication.setText("Choisissez un autre point");
				return;
			}
			this.view.descent.setP(PReel);
			this.view.panelTrace.theP.add(PReel);
			this.view.descent.setDelay(Integer.parseInt(this.view.gadgetsPanel.delay.getText()));
			this.view.panelTrace.ajustDimensionView(PReel);
			this.view.centerWindow();
			this.view.panelTrace.repaint();
			this.view.panelTrace.repaint();

			this.view.gadgetsPanel.suspend.setEnabled(true);
			this.view.gadgetsPanel.stop.setEnabled(true);
			this.view.gadgetsPanel.indication.setText("    ");
			this.view.gadgetsPanel.endIndication.setText("    ");
			this.view.descent.setThreshold(new Double(this.view.gadgetsPanel.minLengthGradient.getText()));
			this.view.hasStarted = true;
			this.view.thread = new Thread(this.view.descent);
			this.view.thread.start();
		}

		else if (command.equals("position")) {
			if ((this.view.thread != null) && this.view.thread.isAlive()) return;
			this.view.panelTrace.theP.clear();
			Couple PReel = new Couple(new Double(this.view.gadgetsPanel.valueX.getText()),
					new Double(this.view.gadgetsPanel.valueY.getText()));
			this.view.panelTrace.validerPosition(PReel);
		}

		else if (command.equals("suspend")) {
			if ((this.view.thread == null) || !this.view.thread.isAlive()) return;
			this.view.descent.setSuspend(true);
			this.view.gadgetsPanel.restart.setEnabled(true);
			this.view.gadgetsPanel.stop.setEnabled(false);
		}
		else if (command.equals("restart")) {
			if ((this.view.thread == null) || !this.view.thread.isAlive()) return;
			this.view.descent.setSuspend(false);
			this.view.gadgetsPanel.stop.setEnabled(true);
			synchronized(this.view.descent) {
				this.view.descent.notifyAll();
			}
		}
		else if (command.equals("stop")) {
			if ((this.view.thread == null) || !this.view.thread.isAlive()) return;				
			this.view.descent.stop();	
		}

		else if (command.equals("validatePb")) {
			if ((this.view.thread != null) && this.view.thread.isAlive()) return;
			validatePb();
		}
		else if (command.equals("less")) {
			if (this.view.descent == null) return;
			double dx = this.view.panelTrace.xMax - this.view.panelTrace.xMin;
			double dy = this.view.panelTrace.yMax - this.view.panelTrace.yMin;
			this.view.panelTrace.xMin -= dx/4;
			this.view.panelTrace.xMax += dx/4;
			this.view.panelTrace.yMin -= dy/4;
			this.view.panelTrace.yMax += dy/4;
			this.view.panelTrace.makeOrthogonal();
			this.view.panelTrace.center();
			this.view.panelTrace.repaint();
		}

		else if (command.equals("more")) {
			if (this.view.descent == null) return;
			double dx = this.view.panelTrace.xMax - this.view.panelTrace.xMin;
			double dy = this.view.panelTrace.yMax - this.view.panelTrace.yMin;
			this.view.panelTrace.xMin += dx/8;
			this.view.panelTrace.xMax -= dx / 8;
			this.view.panelTrace.yMin += dy/8;
			this.view.panelTrace.yMax -= dy/8;
			this.view.panelTrace.makeOrthogonal();
			this.view.panelTrace.center();
			this.view.panelTrace.repaint();
		}


		else if (command.equals("center")) {
			if (this.view.descent == null) return;
			this.view.panelTrace.center();
			this.view.panelTrace.repaint();
		}

		else if (command.equals("delay")) {
			if (this.view.descent != null) this.view.descent.setDelay(Integer.parseInt(this.view.gadgetsPanel.delay.getText()));
		}
	}


	protected void reinit() {
		this.view.setDescent(null);
		this.view.panelTrace.theP.clear();
		this.view.gadgetsPanel.indication.setText("Choose a problem");
		this.view.gadgetsPanel.endIndication.setText("     ");
		this.view.gadgetsPanel.nbSteps.setText("0");
		this.view.scrollPane.getViewport().setViewPosition(new Point
				((this.view.panelTrace.width - this.view.panelTrace.dimView.width)/2, 
				(this.view.panelTrace.height - this.view.panelTrace.dimView.height)/2));
		this.view.hasStarted = false;
		this.view.panelTrace.xMin = this.view.panelTrace.xMax = this.view.panelTrace.yMin = this.view.panelTrace.yMax = 0;
		this.view.panelTrace.repaint();
	}

	protected void validatePb() {
		if (this.view.hasStarted) reinit();
		try {
			Pb pb = choicePb();
			this.view.setDescent(new Descent(pb));
			if (pb.getDomaine().isEmpty()) {
				this.view.gadgetsPanel.indication.setText("The domain is empty");
				this.view.gadgetsPanel.endIndication.setText("Choose another problem");
				return;
			}
			this.view.gadgetsPanel.indication.setText("Choose the starting point");
			this.view.gadgetsPanel.endIndication.setText("by clicking or directly");
			this.view.panelTrace.repaint();
			this.view.gadgetsPanel.problemView.setText(pb.toString());
			this.view.descent.addObserver(this.view);
			this.view.pbReady = true;
		}
		catch(ClassNotFoundException exc) {
			exc.printStackTrace();
			view.gadgetsPanel.indication.setText("Non-existent problem");
			view.gadgetsPanel.endIndication.setText("Choose another number");
		}
		catch(IllegalAccessException exc) {
			view.gadgetsPanel.indication.setText("Illegal access problem");
			view.gadgetsPanel.endIndication.setText("Choose another number");
		}
		catch(InstantiationException exc) {
			view.gadgetsPanel.indication.setText("Instantiation problem");
			view.gadgetsPanel.endIndication.setText("Choose another number");
		}
	}

	protected Pb choicePb() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<?> classe;	
		Pb pb = null;	
		String nom = "problem.Pb" + this.view.gadgetsPanel.choicePb.getText();

		classe = Class.forName(nom);
		pb = (Pb) classe.newInstance();
		return pb;
	}
}
