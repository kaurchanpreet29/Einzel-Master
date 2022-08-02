package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.floreantpos.main.Application;
import com.floreantpos.main.VideoExample;
import com.floreantpos.swing.GlassPane;
import com.floreantpos.test.MediaPlayerDemo;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class VideoPlayerDialog extends JDialog{
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private JDialog currentDialog;
	 JSlider slider;
	public VideoPlayerDialog()
	{
		super(Application.getPosWindow(), true);
		this.setUndecorated(true);
		this.setFocusable(true);
		setLayout(new BorderLayout());
		currentDialog = this;
	     NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Programme\\VideoLAN\\VLC");
	     Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                playVideo();
	            }
	     });
	}
    private void playVideo() {
    	
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        getContentPane().add(mediaPlayerComponent, BorderLayout.CENTER);       
        mediaPlayerComponent.getMediaPlayer().playMedia("C:\\floreantpos\\Video\\kasse.mp4");
        JPanel panel = new JPanel();
       // panel.setLayout(new MigLayout());
        panel.setBackground(Color.BLACK);
    
        JButton button = new JButton(">>");
        setFont(button);
        button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayerComponent.getMediaPlayer().skip(10000);
			}
        	
        });
        final JButton pauseButton = new JButton("||");
        setFont(pauseButton);
        pauseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(pauseButton.getText().compareTo("||") == 0)
					pauseButton.setText(">");
				else
					pauseButton.setText("||");
				mediaPlayerComponent.getMediaPlayer().pause();
			}
        	
        });
        JButton backButton = new JButton("<<");
        setFont(backButton);
        backButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayerComponent.getMediaPlayer().skip(-10000);
			}
        	
        });
        JButton closeButton = new JButton("Close");
        setFont(closeButton);
        closeButton.setFont(new Font("Tahoma", Font.BOLD,18));
       
        closeButton.setBackground(new Color(255,153,153));
        closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayerComponent.getMediaPlayer().stop();
				dispose();
			}
        });
        panel.add(button);
        panel.add(backButton);
        panel.add(pauseButton);
        panel.add(closeButton);
        getContentPane().add(panel,BorderLayout.SOUTH);
        
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.BLACK);
        JButton previousButton = new JButton("Previous Chapter");
        setFont(previousButton);
        previousButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayerComponent.getMediaPlayer().previousChapter();
			}
        	
        });
        JButton nextButton = new JButton("Next Chapter");
        setFont(nextButton);
        nextButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayerComponent.getMediaPlayer().nextChapter();
			}
        	
        });
        northPanel.add(previousButton);
        northPanel.add(nextButton);
        getContentPane().add(northPanel,BorderLayout.NORTH);
    }
    public void setFont(JButton button)
    {
    	button.setFont(new Font("Tahoma", Font.BOLD,18));
    	button.setBackground(new Color(102,255,102));
    }
	
    
}
