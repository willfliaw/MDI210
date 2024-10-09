package descent.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import descent.model.Couple;
import descent.model.Descent;

public class View extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	protected TracePanel panelTrace;
	protected GadgetsPanel gadgetsPanel;
	protected Descent descent;
	protected JScrollPane scrollPane;
	protected Thread thread;
	protected boolean  hasStarted;
	protected boolean pbReady;
	
	 public View( ) {
		this.setLayout(new BorderLayout());
		this.panelTrace = new TracePanel(this);
		scrollPane = new JScrollPane(this.panelTrace);
		scrollPane.setPreferredSize(panelTrace.dimView);
		this.centerWindow(); 
		this.add(scrollPane, BorderLayout.CENTER);
		
		gadgetsPanel = new GadgetsPanel(this);
		this.add(gadgetsPanel, BorderLayout.EAST);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	protected void centerWindow() {
		scrollPane.getViewport().setViewPosition(new Point((this.panelTrace.width - this.panelTrace.dimView.width)/2, 
				(panelTrace.height - this.panelTrace.dimView.height)/2));	
	}

	@Override
	public void update(Observable o, Object arg) {
		Couple P = this.descent.getP();
		if (P != null) {
			Couple PCourt = P.truncate(5);
			this.gadgetsPanel.valueX.setText(new Double(PCourt.x).toString());
			this.gadgetsPanel.valueY.setText(new Double(PCourt.y).toString());
			this.KarushKuhnTucker();
		}
		this.gadgetsPanel.nbSteps.setText(new Integer(this.descent.getNbSteps()).toString()); 
		if (!this.descent.reachesItsMinimum()) {
			this.gadgetsPanel.indication.setText("pb does not reach minimum");
			panelTrace.repaint();
			this.gadgetsPanel.stop.setEnabled(false);
			this.gadgetsPanel.restart.setEnabled(false);
			this.gadgetsPanel.suspend.setEnabled(false);
			this.pbReady = false;
			return;
		}
		panelTrace.theP.add(P);
		boolean change = false;
		if (P.x < panelTrace.xMin) {
			panelTrace.xMin = P.x;
			change = true;
		}
		else if (P.x > panelTrace.xMax) {
			panelTrace.xMax = P.x;
			change = true;
		}
		if (P.y < panelTrace.yMin) {
			panelTrace.yMin = P.y;
			change = true;
		}
		else if (P.y > panelTrace.yMax) {
			panelTrace.yMax = P.y;
			change = true;
		}
		if (change) {
			panelTrace.ajustDimensionView(P);
			this.panelTrace.center();
		}
		if (this.descent.isFinished()) {
			if (this.descent.isStopped()) this.gadgetsPanel.indication.setText("La descente est stoppee"); 
			else {
				double val = Couple.truncate(this.descent.getPb().f(descent.getP()), 3);
				this.gadgetsPanel.indication.setText("The minimum of f is " + val);
				this.gadgetsPanel.endIndication.setText("for x = " + descent.getP().truncate(4).x 
						+ " and y = " + descent.getP().truncate(4).y);
			}
			this.gadgetsPanel.stop.setEnabled(false);
			this.gadgetsPanel.restart.setEnabled(false);
			this.gadgetsPanel.suspend.setEnabled(false);
			this.gadgetsPanel.validatePb.setEnabled(true);
		}	
		pbReady = false;
		panelTrace.repaint();
	}


	protected void KarushKuhnTucker() {
		String string1 = "KKT: ";
		String string2;
		if (this.descent == null) return;
		Couple theLambda = this.descent.KarushKuhnTucker(this.descent.getP());
		if (theLambda == null) {
			string1 += "not satisfied"; 
			string2 = " ";
		}
		else {
			if (theLambda.x == 0.0) {
				string1 += "satisfied inside";
				string2 = "gradient below the threshold";
			}
			else if (theLambda.y == 0.0) {
				string1 += "satisfied on an edge";
				string2 = "(lambda = " +  Couple.truncate(theLambda.x, 2) + ")" ;
			}
			else {
				string1 += "satisfied on a corner";
				string2 = "(lambda1 = " +  Couple.truncate(theLambda.x, 2) + ", lambda2 = " + Couple.truncate(theLambda.y, 2) + ")" ;
			}
		}
		this.gadgetsPanel.theLambda1.setText(string1);
		this.gadgetsPanel.theLambda2.setText(string2);
	}

	protected void setDescent(Descent descent) {
		this.descent = descent;
		this.panelTrace.setDescent(descent);
	}
}
