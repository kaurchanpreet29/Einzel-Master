package com.floreantpos.test;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.media.*;
 
public class MediaPlayerDemo extends JFrame {
   private Player player;
   private File file;
 
   public MediaPlayerDemo()
   {
      super( "Demonstrating the Java Media Player" );
 
      /*openFile();
      createPlayer();*/

      setSize( 300, 300 );
      show();
   }
 
   private void openFile()
   {      
      file = new File("video/kasse.mp4");
      
   }
 
   private void createPlayer()
   {
      if ( file == null )
         return; 
 
      removePreviousPlayer();
 
      try {
         // create a new player and add listener
    	  Player mediaPlayer = Manager.createRealizedPlayer(file.toURL());
    	
         Component video = mediaPlayer.getVisualComponent();
         Component controls = mediaPlayer.getControlPanelComponent();

         if ( video != null )
             add( video, BorderLayout.CENTER ); // add video component

         if ( controls != null )
             add( controls, BorderLayout.SOUTH ); // add controls
         mediaPlayer.start();  // start player
      }
      catch ( Exception e ){
         JOptionPane.showMessageDialog( this,
            "Invalid file or location", "Error loading file",
            JOptionPane.ERROR_MESSAGE );
         e.printStackTrace();
      }
   }
 
   private void removePreviousPlayer()
   {
      if ( player == null )
         return;
 
      player.close();
 
      Component visual = player.getVisualComponent();
      Component control = player.getControlPanelComponent();
 
      Container c = getContentPane();
      
      if ( visual != null ) 
         c.remove( visual );
 
      if ( control != null ) 
         c.remove( control );
   }
 
   public static void main(String args[])
   {
      MediaPlayerDemo app = new MediaPlayerDemo();
 
      app.addWindowListener(
         new WindowAdapter() {
            public void windowClosing( WindowEvent e )
            {
               System.exit(0);
            }
         }
      );
   }
 
   // inner class to handler events from media player
   private class EventHandler implements ControllerListener {
      public void controllerUpdate( ControllerEvent e ) {
         if ( e instanceof RealizeCompleteEvent ) {
            Container c = getContentPane();
          
            // load Visual and Control components if they exist
            Component visualComponent =
               player.getVisualComponent();
 
            if ( visualComponent != null )
               c.add( visualComponent, BorderLayout.CENTER );
 
            Component controlsComponent =
               player.getControlPanelComponent();
 
            if ( controlsComponent != null )
               c.add( controlsComponent, BorderLayout.SOUTH );
 
            c.doLayout();
         }
      }
   }
}