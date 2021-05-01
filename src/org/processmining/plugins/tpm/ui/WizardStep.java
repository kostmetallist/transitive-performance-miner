package org.processmining.plugins.tpm.ui;

import javax.swing.JPanel;

public abstract class WizardStep extends JPanel {
	
	private static final long serialVersionUID = -4337054345821151902L;
	
	public abstract void fillSettings();
}
