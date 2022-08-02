package com.floreantpos.bo.actions;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.OnlineMenuGroupView;

public class OnlineMenuGroupAction extends AbstractAction {

	public OnlineMenuGroupAction() {
		super("Gruppen Action");
		init();
	}

	public OnlineMenuGroupAction(String name) {
		super(name);
		init();
	}

	public OnlineMenuGroupAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		OnlineMenuGroupView dialog = null;
		dialog = new OnlineMenuGroupView(BackOfficeWindow.getInstance(),1);
		
		dialog.pack();
		dialog.open();
	}
	public void init() {
		OnlineMenuGroupView dialog = null;
		dialog = new OnlineMenuGroupView(BackOfficeWindow.getInstance(),1);
		
		dialog.pack();
		dialog.open();
	}

}
