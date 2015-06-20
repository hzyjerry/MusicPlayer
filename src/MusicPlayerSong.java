import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/* Wrapper of sound files
 * MusicPlayerSongs are kept inside the MusicPlayerSonglist
 */
public class MusicPlayerSong implements MusicPlayerConstants{
	private String name;
	private int length;
	private String ImgName;
	private ImageIcon image;
	private Clip songClip;
	
	MusicPlayerSong(String name, String ImgName){
		this.name = name;
		this.ImgName = ImgName;
		if (ImgName == "")
			ImgName = "No background.png";
		this.image = createMusicImage(new ImageIcon("image/" + ImgName));
		//System.out.println(this.image);
		//System.out.println("songs/" + name + ".wav");
		URL url = this.getClass().getClassLoader().getResource("songs/" + name + ".wav");
		//System.out.println(url);
        AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(url);
	        // Get a sound clip resource.
	        //System.out.println(audioIn);
	        songClip = AudioSystem.getClip();
	        // Open audio clip and load samples from the audio input stream.
	        songClip.open(audioIn);
	        songClip.setFramePosition(0);
	        this.length = (int)(songClip.getMicrosecondLength()/1000000);
	     } catch (UnsupportedAudioFileException e) {
	          e.printStackTrace();
	     } catch (IOException e) {
	          e.printStackTrace();
	     } catch (LineUnavailableException e) {
	          e.printStackTrace();
	     }
	}
	
	public Clip getSongClip(){
		return this.songClip;
	}
	
	public static ImageIcon createMusicImage(ImageIcon icon){
		int ImageH = MUSICIMAGE_HEIGHT;
		int ImageW = MUSICIMAGE_WIDTH;
		Image img = icon.getImage().getScaledInstance(ImageW, ImageH, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}
	
	public int getLength(){
		return this.length;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getImgName(){
		return this.ImgName;
	}
	
	public ImageIcon getImg(){
		return this.image;
	}
} 
