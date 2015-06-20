/*
 * File: MusicPlayerSonglist.java
 * ---------------------------------
 * This class handles the music player's content
 * It can read in all the music files, create a song list
 * and generate the next song to play based on MODE 
 * (single, loop, shuffle) constant.
 */

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;

/* Back-end file structure
 * All the songs are kept inside an ArrayList.
 * The MusicPlayerSonglist class is able to generate ArrayList with
 * different sequences.
 */
public class MusicPlayerSonglist {
	ArrayList<MusicPlayerSong> songList;		//Song list in added order
	ArrayList<MusicPlayerSong> orderList;		//Song list in chosen order
	
	MusicPlayerSonglist(){
		songList = new ArrayList<MusicPlayerSong>();
		orderList = songList;
	}
	
	public void createList(String[] songList, String[] songImgList) throws IOException{
		try{
			int i = 0;
			for(String song: songList){
				MusicPlayerSong newSong = new MusicPlayerSong(song, songImgList[i]);
				i++;
				this.addSong(newSong);
			}
		}catch (Exception e){
			System.out.println("Music File Can't be read");
			e.printStackTrace();
		}
	}
	
	public void addSong(MusicPlayerSong song){
		songList.add(song);
	}
	
	public MusicPlayerSong getSong(int index){
		return orderList.get(index);
	}
	
	public int getLength(){
		return orderList.size();
	}
	
	public MusicPlayerSong popSong(){
		MusicPlayerSong first = orderList.get(0);
		orderList.remove(0);
		orderList.add(first);
		return first;
	}
	
	public void chooseSong(int index){
		MusicPlayerSong chosen = orderList.get(index);
		orderList = new ArrayList<MusicPlayerSong>();
		orderList.add(chosen);
	}
	
	public ArrayList<MusicPlayerSong> getSongList(){
		return orderList;
	}
	
	public void mode_shuffle(){
		Random random = new Random();
		orderList = songList;
		for (int i = orderList.size() - 1; i > 0; i--){
			int j = random.nextInt(i);
			MusicPlayerSong temp = orderList.get(i);
			orderList.set(i, orderList.get(j));
			orderList.set(j, temp);
		}
	}
	
	public void mode_loop(){
		orderList = songList;
	}
	
	
	public void mode_one(){
		MusicPlayerSong first = orderList.get(0);
		orderList = new ArrayList<MusicPlayerSong>();
		orderList.add(first);
	}
}
