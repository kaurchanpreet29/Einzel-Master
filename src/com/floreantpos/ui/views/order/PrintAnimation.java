package com.floreantpos.ui.views.order;

import com.floreantpos.ui.dialog.PrintDelayDialog;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class PrintAnimation implements Runnable{
	Thread t;
	PrintAnimation()
	{
		t = new Thread(this);
		t.start();
    }
	@Override
	public void run() {
		final PrintDelayDialog dialog = new PrintDelayDialog();
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.pack();
        dialog.open();
	}
}
