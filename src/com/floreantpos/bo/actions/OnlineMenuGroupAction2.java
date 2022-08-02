package com.floreantpos.bo.actions;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.OnlineMenuGroupView;

public class OnlineMenuGroupAction2 extends AbstractAction {

	public OnlineMenuGroupAction2() {
		super("Gruppen Action");
		init();
	}

	public OnlineMenuGroupAction2(String name) {
		super(name);
		init();
	}

	public OnlineMenuGroupAction2(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		OnlineMenuGroupView dialog = null;
		dialog = new OnlineMenuGroupView(BackOfficeWindow.getInstance(),2);
		
		dialog.pack();
		dialog.open();
	}
	public void init() {
		OnlineMenuGroupView dialog = null;
		dialog = new OnlineMenuGroupView(BackOfficeWindow.getInstance(),2);
		
		dialog.pack();
		dialog.open();
	}

}
