package org.processmining.plugins.tpm.ui;

import javax.swing.JPanel;

public abstract class TpmWizardStep extends JPanel {
	
	private static final long serialVersionUID = -4337054345821151902L;
	
	protected abstract void initComponents();
	public abstract void fillSettings();
}
