package descent.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GadgetsPanel extends Box {
	private static final long serialVersionUID = 1L;
	protected JButton start = new JButton("Start");
	protected JButton suspend = new JButton("Suspend");
	protected JButton restart = new JButton("Restart");
	protected JButton stop = new JButton("Stop");
	protected JTextField valueX = new JTextField(10);
	protected JTextField valueY = new JTextField(10);
	protected JButton validatePb  = new JButton("Validate problem");
	protected JLabel  indication = new JLabel("Choose a problem");
	protected JLabel  endIndication = new JLabel("              ");
	protected JTextField choicePb = new JTextField(4);
	protected JButton more = new JButton("+");
	protected JButton less = new JButton("-");
	protected JButton center = new JButton("Center");
	protected JTextField minLengthGradient = new JTextField(new Double(0.00001).toString());
	protected JTextArea problemView = new JTextArea(7, 20);
	protected JLabel theLambda1 = new JLabel("  ");
	protected JLabel theLambda2 = new JLabel("  ");
	protected JLabel nbSteps = new JLabel("0");
	protected JTextField delay = new JTextField(7);
	protected View view;

	protected GadgetsPanel(View view){
		super(BoxLayout.Y_AXIS);
		JPanel panel;
		JPanel grid;
		ControllerGadgets controleurGadgets = new ControllerGadgets(view);

		this.view = view;
		
		this.add(Box.createVerticalStrut(10));
		
		grid = new JPanel(new GridLayout(2, 1));
		this.indication.setForeground(Color.RED);
		this.endIndication.setForeground(Color.RED);
		String nom = this.indication.getFont().getFontName();
		this.indication.setFont(new Font(nom,Font.BOLD, 14));
		this.endIndication.setFont(new Font(nom,Font.BOLD, 14));
		grid.add(this.indication);
		grid.add(endIndication);
		this.add(grid);	

		grid = new JPanel(new GridLayout(2, 1));
		panel = new JPanel();
		panel.add(new JLabel("The chosen problem (1, 2, ...)"));
		choicePb.setText("1");
		choicePb.setActionCommand("choicePb");
		panel.add(this.choicePb);
		choicePb.addActionListener(controleurGadgets);
		grid.add(panel);

		panel = new JPanel();
		this.validatePb.setActionCommand("validatePb");
		this.validatePb.addActionListener(controleurGadgets);
		panel.add(this.validatePb);
		grid.add(panel);
		
		grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(grid);

		this.add(Box.createVerticalStrut(20));
		
		grid = new JPanel(new GridLayout(2, 1));
		
		panel = new JPanel();
		panel.add(new JLabel("abscissa = "));
		this.valueX.setText("0");
		this.valueX.setActionCommand("position");
		this.valueX.addActionListener(controleurGadgets);
		panel.add(this.valueX);
		grid.add(panel);
		
		panel = new JPanel();
		panel.add(new JLabel("ordonnate = "));
		this.valueY.setText("0");
		this.valueY.setActionCommand("position");
		this.valueY.addActionListener(controleurGadgets);
		panel.add(this.valueY);
		grid.add(panel);	

		grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(grid);

		this.add(Box.createVerticalStrut(10));

		this.start.setActionCommand("start");
		this.start.addActionListener(controleurGadgets);
		this.add(start);

		this.add(Box.createVerticalStrut(10));
		suspend.setActionCommand("suspend");
		add(suspend);
		suspend.addActionListener(controleurGadgets);
		suspend.setEnabled(false);

		this.add(Box.createVerticalStrut(10));
		this.restart.setActionCommand("restart");
		this.restart.setEnabled(false);
		this.restart.addActionListener(controleurGadgets);
		add(this.restart);	

		this.add(Box.createVerticalStrut(10));
		this.stop.setActionCommand("stop");
		this.stop.setEnabled(false);
		this.stop.addActionListener(controleurGadgets);
		this.add(stop);	

		this.add(Box.createVerticalStrut(10));

		panel = new JPanel();
		this.problemView.setEditable(false);
		JScrollPane ascenseurs = new JScrollPane(this.problemView);
		panel.add(ascenseurs);
		this.add(panel);
		
		panel = new JPanel();
		
		this.more.setActionCommand("more");
		this.more.addActionListener(controleurGadgets);
		panel.add(this.more);

		this.less.setActionCommand("less");
		this.less.addActionListener(controleurGadgets);
		panel.add(this.less);	

		this.center.setActionCommand("center");
		this.center.addActionListener(controleurGadgets);
		panel.add(center);
		this.add(panel);

		this.add(Box.createVerticalGlue());

		panel = new JPanel();
		panel.add(new JLabel("minimum length of gradient"));
		panel.add(minLengthGradient);
		this.add(panel);	
		
		this.add(Box.createVerticalGlue());
		
		panel = new JPanel();
		this.delay.setText("2000");
		this.delay.setActionCommand("delay");
		this.delay.addActionListener(controleurGadgets);
		panel.add(new JLabel("delay in ms (enter) : "));
		panel.add(this.delay);
		this.add(panel);

		this.add(Box.createVerticalStrut(5));
		
		panel = new JPanel();
		panel.add(new JLabel("number of steps : "));
		panel.add(this.nbSteps);
		this.add(panel);

		panel = new JPanel(new GridLayout(2, 1));
		panel.add(theLambda1);
		panel.add(theLambda2);
		this.add(panel);

		this.add(Box.createVerticalStrut(5));	
	}
}
