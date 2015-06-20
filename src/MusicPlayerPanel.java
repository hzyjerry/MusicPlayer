/*
 * File: MusicPlayerPanel.java
 * ---------------------------------
 * This class creates the user interface for music player.
 * The interface is generated based on java awt graphic API.
 * Event listeners will be added.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;
import java.io.IOException;

/* The whole music player UI
 * The original plan was to use MusicPlayerPanel to implement all interactive
 * elements, and separate sound files elsewhere. However later it is realized that
 * UI and sound files are mingled together most of the time and are very difficult
 * to separate cleanly.
 * This class contains both java Swing components and sound files, but it would have
 * been a better and neater plan to keep different Swing components in different class.
 */
public class MusicPlayerPanel extends JFrame implements MusicPlayerConstants{
	static int playModeNum;
	private SongListTab listTab;
	private SongTab songtab;
	// The Title Tab and Music Image Tab are embedded inside MusicPlayerPanel
	private PlayModeComboBox playmode;
	private JLabel playmode_ImgLabel;
	private JLabel musicImage = new JLabel();
	
	public MusicPlayerPanel(MusicPlayerSonglist songList) throws IOException{
		// Component set up and display need to be separated, otherwise
		// the program will be prone to NullPointerException
		setUpComponents(songList);
		displayComponents();

		this.setTitle("Jerry's Music Player");
		this.setSize(MUSICPLAYER_WIDTH, MUSICPLAYER_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void setUpComponents(MusicPlayerSonglist songList){
		// Set up songTab and listTab
		this.songtab = new SongTab();
		this.listTab = new SongListTab();
		songtab.setAttributes(this.listTab, musicImage);
		listTab.setAttributes(songList, songtab, musicImage);
		// The image label that display the current play mode (upper right corner)
		playmode_ImgLabel = new JLabel();
		setLabel(playmode_ImgLabel, playModeImgs[0]);
		// The comboBox with information about the current play mode
		playmode = new PlayModeComboBox(playModeName);
		playmode.setAttributes(this.listTab, this.songtab, playmode_ImgLabel, songtab.getBar());
	}
	
	private void displayComponents(){
		this.setLayout(new BorderLayout());			//layout.setHgap(-10 * COMPONENT_GAP);
		createDisplayTitleTab();					//Create the title tab (consist of image and a playmode button)
		createDisplayMusicImageTab();				//Set up the Image tab (consist of music image)
		this.add(musicImage, BorderLayout.CENTER);
		// display() methods are called once for displaying after initialization of every component
		listTab.display();
		songtab.display();
		this.add(listTab, BorderLayout.EAST);
		this.add(songtab, BorderLayout.SOUTH);
	}
	
	/* Create the title tab that's displayed at the top of music player
	 * Consists of a title image(apple icon) and a play mode button (loop, shuffle or single)
	 */
	private void createDisplayTitleTab(){
		// Title Image (Apple Icon)
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.insets = new Insets(0, TITLEIMAGE_LEFT_INSET, 0, 0);
		panel.add(getTitleImage(), c);
		
		// Play mode comboBox
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, TITLEBOX_LEFT_INSET, 0, 0);
		panel.add(playmode, c);
		
		// Play mode Icon
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.5;
		c.insets = new Insets(0, TITLELABEL_LEFT_INSET, 0, 0);
		panel.add(playmode_ImgLabel, c);
		
		this.add(panel, BorderLayout.NORTH);
	}
	
	/* Helper function of createTitleTab
	 * Create and return the Title image(apple icon) JLabel
	 */
	private JLabel getTitleImage(){
		ImageIcon icon = new ImageIcon("image/Apple Icon.png");
		int ImageH_origin = icon.getIconHeight();
		int ImageW_origin = icon.getIconWidth();
		
		int ImageH = TITLEIMAGE_HEIGHT;
		int ImageW = (int)(((float)ImageH/ImageH_origin) * ImageW_origin);
		Image img = icon.getImage().getScaledInstance(ImageW, ImageH, java.awt.Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(img));
	}

	/* Given a button label and the name of an image
	 * Set the label to display that image (resized to button icon size)
	 * In this case, it's used to create the playmode image
	 */
	private void setLabel(JLabel label, String buttonName){
		ImageIcon icon = new ImageIcon("image/" + buttonName);
		Image img = icon.getImage().getScaledInstance(BUTTONICON_SIZE, BUTTONICON_SIZE
				, java.awt.Image.SCALE_SMOOTH);
		label.setIcon(new ImageIcon(img));
		label.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
	}
	
	/* Create the music image tab that's displayed in the middle of the music player
	 * Consists of image of the current song being played
	 */
	private void createDisplayMusicImageTab(){
		//BufferedImage titleImg = new BufferedImage(new File("image/Apple Icon.png"));
		ImageIcon icon = new ImageIcon("image/Background.png");
		musicImage.setIcon(MusicPlayerSong.createMusicImage(icon));
		musicImage.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	/* Class used for play mode combo box (selecting "shuffle", "loop" or "single")
	 * Added with event listener
	 */
	private class PlayModeComboBox extends JComboBox<String>{
		private SongListTab listTab;
		private SongTab tab;
		private JLabel ImgLabel;
		private PlayBar bar;
		
		PlayModeComboBox(String[] list){
			super(list);
		}
		private void setAttributes(SongListTab listTab, SongTab tab, JLabel ImgLabel, PlayBar bar){
			this.listTab = listTab;
			this.tab = tab;
			this.ImgLabel = ImgLabel;
			this.setup();
			this.bar = bar;
		}
		
		public void setup(){
			this.setSelectedIndex(0);
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					PlayModeComboBox self = (PlayModeComboBox)e.getSource();
					int modeNum = self.getSelectedIndex();
					setLabel(ImgLabel, playModeImgs[modeNum]);
					listTab.setMode(modeNum);
					listTab.resetPanelList();
					listTab.displayPlain();
					self.bar.timer_stop();
					self.bar.resetPlayBar();
					self.listTab.chooseSong(0);
					createDisplayMusicImageTab();
					tab.changedMode();
				}
			});
		}
	}	
}

/* Class used for play buttons
 * Contains a picture that's resized to button size
 */
class PlayerButton extends JButton implements MusicPlayerConstants{
	private PlayBar bar;
	public void setBar(PlayBar bar){
		this.bar = bar;
	}
	
	public PlayBar getBar(){
		return this.bar;
	}
	
	/* Helper function for creating buttons
	 * Given a button, set its image icon based on given button name
	 */
	public void setButton(String name){
		ImageIcon icon = new ImageIcon("image/" + name);
		Image img = icon.getImage().getScaledInstance(BUTTONICON_SIZE, BUTTONICON_SIZE
				, java.awt.Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(img));
		setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
	}
}

/* Class displayed at the button of music player panel
 * Includes song buttons and a playBar
 */
class SongTab extends JPanel implements MusicPlayerConstants{
	private PlayerButton btSongLast = new PlayerButton();
	private PlayerButton btSongPlay = new PlayerButton();
	private PlayerButton btSongNext = new PlayerButton();
	private PlayBar bar;
	
	/* Create the song tab that's displayed at the button of the music player
	 * Consists of two parts:
	 * 	1) SongButtons: play, last song, next song buttons
	 * 	2) Play Bar: two text labels indicating time, a middle bar
	 */
	SongTab(){
		initSongButtons();
		this.bar = new PlayBar();
	}
	
	public void setSong(MusicPlayerSong song){
		this.bar.setSong(song);
	}
	
	public void resetButtonImg_stopped(){
		btSongPlay.setButton("player_play.png");
	}
	
	public void resetButtonImg_played(){
		btSongPlay.setButton("player_pause.png");
	}

	public void setAttributes(SongListTab listTab, JLabel musicImage){
		this.bar.setAttributes(listTab, musicImage, this);
		btSongLast.setBar(bar);
		btSongPlay.setBar(bar);
		btSongNext.setBar(bar);
	}
	
	/* Helper function for to setup all the play buttons. These buttons include:
	 * 		btSongLast: go to the last song (first click stay in the current song)
	 * 		btSongPlay: pause or resume the current song
	 * 		btSongNext: go to the next song
	 */
	private void initSongButtons(){
		btSongLast.setButton("player_last.png");
		btSongLast.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//System.out.println("song_last pressed");
				PlayBar bar = btSongLast.getBar();
				bar.timer_start();			// Make the song grid move
				bar.song_last();
			}
		});
		
		btSongPlay.setButton("player_play.png");
		btSongPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (bar.togglePlayButton()){
					btSongPlay.setButton("player_pause.png");
				}else{
					btSongPlay.setButton("player_play.png");
				}
			}
		});
		
		btSongNext.setButton("player_next.png");
		btSongNext.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				PlayBar bar = btSongNext.getBar();
				bar.timer_start();
				bar.song_next();
			}
		});
	}

	/* Called during music player panel initialization
	 * Add in all three buttons and the play bar
	 */
	public void display(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel songButtonsBar = new JPanel();
		songButtonsBar.setLayout(new GridBagLayout());
		
		//Set up the last_song button
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(0, 0, 0, 20);
		btSongLast.setBar(bar);
		songButtonsBar.add(btSongLast, c);
		
		//Set up the play_song button
		c.gridx = 1;
		c.insets = new Insets(0, 20, 0, 20);
		
		//add(bar);
		songButtonsBar.add(btSongPlay, c);
		
		//Set up the next_song button
		c.gridx = 2;
		c.insets = new Insets(0, 20, 0, 0);
		
		songButtonsBar.add(btSongNext, c);
		
		add(songButtonsBar);
		add(this.bar);
	}
	
	public PlayBar getBar(){
		return this.bar;
	}
	
	public void replay(){
		this.bar.song_replay();
	}
	
	public void play(){
		this.bar.song_play();
	}
	
	public void next(){
		this.bar.song_next();
	}
	
	public void last(){
		this.bar.song_last();
	}
	
	public boolean isPlaying(){
		return !this.bar.timerStopped();
	}
	
	public void changedMode(){
		this.bar.resetPlayBar_noSong();
		if (this.bar.getSong()!= null){
			this.bar.getSong().stop();
			this.bar.getSong().setFramePosition(0);
		}
		this.setSong(null);
		this.resetButtonImg_stopped();
		this.bar.setSong(null);
	}
}

/* The JPanel on the right side of the music player, with all songs
 * in it displayed as a list. The SongListTab is able to highlight the
 * song being played, support using click to choose songs.
 */
class SongListTab extends JPanel implements MusicPlayerConstants{
	private MusicPlayerSonglist songList;
	private JPanel[] panelList = new JPanel[SONGLISTTAB_NUM];
	private SongTab tab;
	private MusicPlayerSong song = null;;
	private int chosenNum = 0;
	private JLabel musicImage;
	
	public void setAttributes(MusicPlayerSonglist songList, SongTab tab, JLabel musicImage){
		this.tab = tab;
		this.songList = songList;
		this.songList.mode_loop();
		this.musicImage = musicImage;
	}
	
	public void display(){
		this.setPreferredSize(new Dimension(SONGLISTTAB_WIDTH, SONGLISTTAB_HEIGHT));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.resetPanelList();
		this.displayPlain();
	}
	
	public void setMode(int mode){
		switch(mode){
			case SONGLISTTAB_MODE_LOOP: songList.mode_loop(); break;
			case SONGLISTTAB_MODE_SHUFFLE: songList.mode_shuffle(); break;
			case SONGLISTTAB_MODE_ONE: songList.mode_one(); break;
			default: return;
		}
		resetPanelList();
		this.song = null;
	}
	
	// Return the next song, and at the same time, set up the display
	public MusicPlayerSong getNextSong(){
		if (this.song != null){
			chosenNum = (chosenNum + 1)%songList.getLength();
		}
		this.song = songList.getSong(chosenNum);
		this.displaySong();
		return this.song;
	}
	
	public MusicPlayerSong getLastSong(){
		chosenNum = (chosenNum - 1 + songList.getLength())%songList.getLength();
		this.song = songList.getSong(chosenNum);
		this.displaySong();
		return this.song;
	}
	
	public int getSongNum() throws Exception{
		return songList.getSongList().size();
	}
	
	// Called every time the display of panelList needs to be refreshed
	public void resetPanelList(){
		ArrayList<MusicPlayerSong> list = this.songList.getSongList();
		for (int i = 0; i < SONGLISTTAB_NUM; i++){
			JPanel panel = new JPanel();
			Color color = (i%2 == 0)? Color.LIGHT_GRAY:null;
			panel.setBackground(color);
			panel.setPreferredSize(new Dimension(SONGLISTTAB_WIDTH, 
					SONGLISTTAB_HEIGHT/SONGLISTTAB_NUM));
			panel.setBorder(BorderFactory.createLineBorder(Color.gray));
			if (i < list.size())
				panel.add(new JLabel(list.get(i).getName()));
			panel.addMouseListener(new listTabItemListener(i, this, tab));
			panelList[i] = panel;
		}
	}
	
	public void chooseSong(int index){
		this.chosenNum = index;
	}
	
	private void highlightTab(int index){
		for (int i = 0; i < SONGLISTTAB_NUM; i++){
			panelList[i].setBackground((i%2 == 0)? Color.LIGHT_GRAY:null);
		}
		panelList[index].setBackground(SONGLISTTAB_HIGHLIGHT);
	}
	
	public void displayPlain(){
		this.removeAll();
		for (int i = 0; i < SONGLISTTAB_NUM; i++){
			this.add(this.panelList[i]);
		}
		this.revalidate();
	}
	
	/* 	displaySong() is used when a song is chosen, but not played
	 *  The song need to be chosen beforehand using chooseSong(index)
	 */
	public void displaySong(){
		this.displayPlain();
		highlightTab(this.chosenNum);
		this.revalidate();
	}
	
	public void reset(){
		this.chosenNum = 0;
		this.displayPlain();
	}
	
	public void restartSong() throws Exception{
		if (this.songList == null)
			throw(new Exception("Unable to play: songlist not initialized"));
		MusicPlayerSong song = this.songList.getSongList().get(this.chosenNum);
		if (tab.getBar().getSong() != null)
			tab.getBar().getSong().stop();
		song.getSongClip().setFramePosition(0);
		this.tab.setSong(song);
		//System.out.println("Now playing song: " + song.getName());
		this.displaySong();
		this.musicImage.setIcon(song.getImg());
		tab.replay();
	}
}

/* The mouse event listener implemented by SongListTab. Catches
 * mouse click events.
 */
class listTabItemListener extends MouseAdapter{
	private SongListTab tab;
	private int index;
	private SongTab songtab;
	
	listTabItemListener(int index, SongListTab tab, SongTab songtab){
		this.tab = tab;
		this.index = index;
		this.songtab = songtab;
	}
	
	public void mouseClicked(MouseEvent e){
		try{
			if (index > tab.getSongNum()) return;
			tab.chooseSong(index);
			tab.displaySong();
			if (e.getClickCount() >= 2){
				tab.restartSong();
				songtab.resetButtonImg_played();
				songtab.getBar().timer_start();
			}
		}catch (Exception e1){
			e1.printStackTrace();
		}
	}
}

/* PlayBar class included in the song tab
 * The play bar consists of two JLabels (time now and time remain) and
 * 		a middle bar that indicates the proportion of music played
 * The play bar contains a timer, which fires an event every second
 * The time info needed by middle bar and the two JLabels is kept by the play bar.
 * The Song file is kept by the Middle Bar component inside the play bar.
 */
class PlayBar extends JPanel implements MusicPlayerConstants, ActionListener {
	private float time_all;
	private float time_now;
	private float time_remain;
	private MiddleBar bar;
	private Timer timer;
	private boolean timer_stopped = true;
	private JLabel time1;
	private JLabel time2;
	private SongListTab listTab;
	private JLabel musicImage;
	private SongTab songtab;
	
	// Initializer
	PlayBar(){
		this.setLayout(new FlowLayout());
		timer = new Timer(TIME_INTERVAL, this);
		this.time_all = PLAYBAR_PAINT_DEFAULT_LENGTH;
		this.time_now = 0;
		this.time_remain = this.time_all - this.time_now;
		time1 = new JLabel("-:-");		// Default time display
		time2 = new JLabel("-:-");
		this.bar = new MiddleBar((int)this.time_all, this);
		this.bar.setPreferredSize(new Dimension(PLAYBAR_BAR_WIDTH, PLAYBAR_HEIGHT));
		this.add(time1);
		this.bar.paint(0);
		this.add(bar);
		this.add(time2);
		this.revalidate();
	}
	
	public void setAttributes(SongListTab listTab, JLabel musicImage, SongTab songtab){
		this.listTab = listTab;
		this.musicImage = musicImage;
		this.songtab = songtab;
	}
	
	// Set the player to the default state (before start_
	public void setSong(MusicPlayerSong song){
		if (song!= null){
			bar.setSong(song.getSongClip());
			initPlayBar(song.getLength());
		}else{
			bar.setSong(null);
		}
	}
	
	// Initialization of the playBar after song is added
	private void initPlayBar(int time){
		this.time_all = time;
		setDisplayTime(0, time);
	}
	
	// Reset time values, using the same song
	public void resetPlayBar(){
		this.time_now = 0;
		this.time_remain = this.time_all - this.time_now;
		this.setDisplayTime(0, (int)this.time_all);
		this.repaintTime();
	}
	
	public Clip getSong(){
		return this.bar.getSong();
	}
	
	// Reset play bar to the state without a song
	public void resetPlayBar_noSong(){
		time1.setText("-:-");
		time2.setText("-:-");
		this.bar.paint(PLAYBAR_PAINT_DEFAULT);
	}
	
	// Set time values, using the same song
	private void setTime(int time_new){
		this.time_now = time_new;
		this.time_remain = this.time_all - this.time_now;
	}
	
	/* Method called every period, or after the user dragged the play grid
	 * In the second case, the method is called by middle bar.
	 */
	public void repaintTime(){
		if (this.time_remain < 0){
			Clip song = this.bar.getSong();
			if (song == null){
				int time_now = (int)this.time_now;
				//System.out.println("time now: " + this.time_now);
				int time_remain = 0;
				this.resetPlayBar_noSong();
				this.timer_stopped = true;
				this.timer.stop();
			}
		}else{
			int time_now = (int)this.time_now;
			int time_remain = (int)this.time_remain + 1;	// fixing off-by-one error
			this.time_now += (float)TIME_INTERVAL /1000;
			this.time_remain -= (float)TIME_INTERVAL /1000;
			this.setDisplayTime(time_now, time_remain);
			if (!this.bar.isPressed())
				this.bar.paint(this.time_now);
		}
	}
	
	// Helper function called during initialization and every re-rendering
	private void setDisplayTime(int time_now, int time_remain){
		time1.setText(time_now/60 + ":" + time_now%60);
		time2.setText(time_remain/60 + ":" + time_remain%60);
	}
	
	public int getTimeNow(){
		return (int)this.time_now;
	}
	
	// Called by the play bar's timer
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == timer)
			if (this.time_remain < 0){
				this.song_next();
			}
			repaintTime();
	}
	
	public boolean timerStopped(){
		return this.timer_stopped;
	}
	
	public void timer_stop(){
		this.timer_stopped = true;
		this.timer.stop();
	}
	
	public void timer_start(){
		this.timer_stopped = false;
		this.timer.start();
	}
	
	public void song_play(){
		if (bar.getSong() == null){
			song_next();
		}
		if (!this.timer_stopped){
			this.timer.stop();
			bar.getSong().stop();
		}
		else{
			this.timer.start();
			bar.getSong().start();
		}
		this.timer_stopped = !this.timer_stopped;
	}
	
	public void song_replay(){
		resetPlayBar();		// set up and paint time
		this.timer_stopped = false;
		this.timer.start();
		bar.getSong().setFramePosition(0);
		bar.getSong().start();
	}
	
	public boolean song_last(){
		if (this.time_now <= LAST_SONG_PRESS_TIME){
			MusicPlayerSong lastSong = this.listTab.getLastSong();
			if (lastSong != null)
				bar.getSong().stop();
				this.setSong(lastSong);
				this.musicImage.setIcon(lastSong.getImg());
				this.songtab.resetButtonImg_played();
				bar.getSong().start();
		}
		this.resetPlayBar();
		this.repaintTime();
		return true;
	}
	
	// Find the next song, set it up and play
	// Return whether the process is successful
	public boolean song_next(){
		MusicPlayerSong nextSong = this.listTab.getNextSong();
		if (nextSong != null){
			if (bar.getSong() != null){
				bar.getSong().setFramePosition(0);
				bar.getSong().stop();	
			}
			this.setSong(nextSong);
			this.resetPlayBar();
			this.repaintTime();
			this.songtab.resetButtonImg_played();
			this.musicImage.setIcon(nextSong.getImg());
			bar.getSong().start();
			return true;
		}else{
			return false;
		}
	}
	
	/* Press the play button
	 * Return a boolean: whether the song is at state "play"
	 */
	public boolean togglePlayButton(){
		this.song_play();
		return !this.timerStopped();
	}
	
	// Included inside the play bar tab: the middle bar that has a long
	// bar and a moving grid. Supports mouse dragging interactions.
	class MiddleBar extends JPanel{
		private int time_all;
		private Clip songClip = null;
		
		// time_paint: the current time to be displayed
		// NOTE: MiddleBar does not have information about the actual current time
		// 		 Time info is stored inside the playbar instance variable
		private float time_paint;
		private MyListener listener;
		private PlayBar playbar;
		private int grid_x = 0;
		private boolean grid_pressed = false;
		MiddleBar(int time_all, PlayBar playbar){
			this.time_paint = 0;
			this.playbar = playbar;
			this.time_all = time_all;
			listener = new MyListener(this);
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}
		
		public void setSong(Clip songClip){
			this.songClip = songClip;
			if (songClip!= null)
				this.time_all = (int)(songClip.getMicrosecondLength()/1000000);
		}
		
		// Paint the play bar based on the given time (time now)
		// Used in interactive dragging
		public void paint(float time){
			if (time == PLAYBAR_PAINT_DEFAULT){
				this.time_paint = 0;
				this.time_all = PLAYBAR_PAINT_DEFAULT_LENGTH;
			}
			else this.time_paint = time;
			repaint();
		}
		
		/* Interface for the mouse listener to fire repaint request to playBar
		 * to repaint time display (will not change the play bar)
		 */
		public void paintTimeNew(float time_new){
			if (this.getSong()!=null){
				playbar.setTime((int)time_new);
				playbar.repaintTime();
			}else{
				playbar.resetPlayBar_noSong();
			}
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setColor(Color.GRAY);
			g.fillRect(0, PLAYBAR_BAR_Y, PLAYBAR_BAR_WIDTH, PLAYBAR_BAR_HEIGHT);
			this.grid_x = (int)((PLAYBAR_BAR_WIDTH - PLAYBAR_GRID_WIDTH) * 
					this.time_paint/this.time_all);
			g.setColor(Color.BLACK);
			g.fillRect(this.grid_x, PLAYBAR_GRID_Y, PLAYBAR_GRID_WIDTH, PLAYBAR_GRID_HEIGHT);
		}
		
		public int getGridX(){
			return this.grid_x;
		}
		
		public void setGridX(int new_x){
			this.grid_x = new_x;
		}
		
		public boolean isPressed(){
			return this.grid_pressed;
		}
		
		public void setPressed(boolean pressed){
			this.grid_pressed = pressed;
		}
		
		public Clip getSong(){
			return this.songClip;
		}
	}
	
	private class MyListener extends MouseAdapter{	
		private MiddleBar bar;
		private float time_paint = 0;
		
		MyListener(MiddleBar bar){
			this.bar = bar;
		}
		
		public void mousePressed(MouseEvent e) {
			if ((e.getX() >= this.bar.getGridX()) 
					&& (e.getX() <= this.bar.getGridX() + PLAYBAR_GRID_WIDTH) 
					&& (e.getY() >= PLAYBAR_GRID_Y) 
					&& (e.getY() <= PLAYBAR_GRID_Y + PLAYBAR_GRID_HEIGHT))
				this.bar.setPressed(true);
		}
		
		public void mouseDragged(MouseEvent e){
			if (this.bar.isPressed()){
				float ratio = (float)e.getX()/PLAYBAR_BAR_WIDTH;
				float time_paint = (ratio * this.bar.time_all);
				this.time_paint = (int)time_paint;
				this.bar.paint(time_paint);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (this.bar.isPressed()){
				this.bar.setPressed(false);
				if (this.bar.getSong() != null){
					this.bar.paintTimeNew(this.time_paint);
					Clip songClip = this.bar.songClip;
					float ratio = (float)e.getX()/PLAYBAR_BAR_WIDTH;
					songClip.stop();
					songClip.setMicrosecondPosition((long)(ratio* songClip.getMicrosecondLength()));
					songClip.start();
				}else{
					// Will reset time display and the play bar
					this.bar.paintTimeNew(0);
				}
			}
		}
	}
}
