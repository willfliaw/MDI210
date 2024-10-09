package descent.view;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import descent.model.Couple;

public class ControllerPanelTrace extends MouseAdapter {
	private View vue;
	private Couple PDepart;

	protected ControllerPanelTrace(View vue) {
		this.vue = vue;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if ((this.vue.thread != null) && this.vue.thread.isAlive()) return;
		this.vue.panelTrace.theP.clear();
		Couple P = new Couple(evt.getX(), evt.getY());
		Couple PReel = vue.panelTrace.inverseConvert(P);
		this.vue.panelTrace.validerPosition(PReel);
	}

	@Override
	public void mouseEntered(MouseEvent evt)  {
		this.vue.panelTrace.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	

	@Override
	public void mouseExited(MouseEvent evt)  {
		this.vue.panelTrace.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		PDepart = new Couple(evt.getX(), evt.getY());
	}
	@Override
	public void mouseReleased(MouseEvent evt) {
		PDepart = null;
	}
	
	@Override
	 public void mouseDragged(MouseEvent evt) {
		 if (PDepart != null) {
				Couple PActuel = new Couple(evt.getX(), evt.getY());
				Couple difference = PActuel.substract(PDepart);
				Point anciennePos = vue.scrollPane.getViewport().getViewPosition();
				int nouvellePosX = anciennePos.x - (int)difference.x;
				int nouvellePosY = anciennePos.y - (int)difference.y;
				vue.scrollPane.getViewport().setViewPosition(new Point(nouvellePosX, nouvellePosY));
				vue.panelTrace.repaint();
		 }
	 }      
}
