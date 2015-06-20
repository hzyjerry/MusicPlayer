import java.util.ArrayList;

import javax.swing.JFrame;

import java.io.*;
import java.net.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.media.Manager;  
import javax.media.MediaLocator;
import javax.media.Player;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
//import javazoom.jl.player.Player;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


// TODO: Fix bug while empty list

/* The main entry point for the program
 * MusicPlayer supports wav sound files. All songs are kept inside [root/bin/songs] folder
 * Preview images should be kept inside [root/image] folder
 */
public class MusicPlayer{
	private MediaPlayer mediaPlayer;
	private MusicPlayerPanel panel;
	
	MusicPlayer(){
		MusicPlayerSonglist songList = new MusicPlayerSonglist();
		
		// The song names will be automatically concatenated with ".wav" extension
		String[] songNameList = {"突然好想你", "步步", "我不愿让你一个人", "离开地球表面", "如烟"};
		String[] songImgList = {"突然好想你.jpeg", "步步.png", "我不愿让你一个人.png", "离开地球表面.png", "如烟.jpg"};
		
		try {
			songList.createList(songNameList, songImgList);
			songList.mode_shuffle();
			songList.mode_loop();
			ArrayList<MusicPlayerSong> list1 = songList.getSongList();
			//for (MusicPlayerSong song: list1)
			//	System.out.println(song.getName());
			
			this.panel = new MusicPlayerPanel(songList);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// Method used for testing
	public void play(){
		URL url = this.getClass().getClassLoader().getResource("songs/茜拉-想你的夜.wav");
		//System.out.println(url);
        AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(url);
	        // Get a sound clip resource.
	        //System.out.println(audioIn);
	        Clip clip = AudioSystem.getClip();
	        // Open audio clip and load samples from the audio input stream.
	        clip.open(audioIn);
	        clip.setFramePosition(0);
	        //System.out.println("Song length: " + clip.getMicrosecondLength()/1000000);
	        clip.setMicrosecondPosition(1000000);
	        clip.start();
	     } catch (UnsupportedAudioFileException e) {
	          e.printStackTrace();
	     } catch (IOException e) {
	          e.printStackTrace();
	     } catch (LineUnavailableException e) {
	          e.printStackTrace();
	     }
	}
	
	public static void main(String[] args) throws Exception{
		MusicPlayer player = new MusicPlayer();
	}
}