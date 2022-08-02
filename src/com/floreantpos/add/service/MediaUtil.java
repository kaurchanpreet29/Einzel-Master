package com.floreantpos.add.service;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.floreantpos.config.TerminalConfig;
public class MediaUtil {
	private final int BUFFER_SIZE = 128000;
	private File soundFile;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;
	private static MediaUtil mediaInstance;

	/**
	 * @param filename the name of the file that is going to be played
	 */	
	
	public void playNotification(int n) {
		int count =0;
		try {while(count<n) {
			playAgain();
			count++;
		}
		}catch(IllegalArgumentException ex) {
			playNotification1();
		}
	}
	
	public void playAgain() {
		playSound("resources/media/pling.wav");
	}
	
	public void playNotification1() {
		try {
			playSound("resources/media/anxious.wav");
		}catch(IllegalArgumentException ex) {
			
		}
	}
	
	public static MediaUtil getInstance() {
		if(mediaInstance==null)
			mediaInstance = new MediaUtil();
		
		return mediaInstance;		
			
	}
	
	public void playSound(String filename){
		try {

			String strFilename = filename;

			try {
				soundFile = new File(strFilename);
				if(!soundFile.exists()) {
					TerminalConfig.setPlaySound(false);
					return;
				}
					
			} catch (Exception e) {
				TerminalConfig.setPlaySound(false);
				return;
			}

			try {
				audioStream = AudioSystem.getAudioInputStream(soundFile);
			} catch (Exception e){
				e.printStackTrace();
				TerminalConfig.setPlaySound(false);
				return;
				}

			audioFormat = audioStream.getFormat();

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			try {
				sourceLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceLine.open(audioFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				TerminalConfig.setPlaySound(false);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				TerminalConfig.setPlaySound(false);
				return;			}

			sourceLine.start();

			int nBytesRead = 0;
			byte[] abData = new byte[BUFFER_SIZE];
			while (nBytesRead != -1) {
				try {
					nBytesRead = audioStream.read(abData, 0, abData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (nBytesRead >= 0) {
					@SuppressWarnings("unused")
					int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
				}
			}

			sourceLine.drain();
			sourceLine.close();
		}catch(Exception ex) {}
	}

}
