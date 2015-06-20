import java.awt.Color;


public interface MusicPlayerConstants {
	public static final int SIDE_MARGIN = 5;
	public static final int COMPONENT_GAP = 5;
	public static final int TIME_INTERVAL = 50;
	
	public static final int MUSICPLAYER_WIDTH = 550;
	public static final int MUSICPLAYER_HEIGHT = 600;
	
	public static final int TITLETAB_HEIGHT = (int)(0.15 * MUSICPLAYER_HEIGHT);
	public static final int TITLEIMAGE_HEIGHT = TITLETAB_HEIGHT - (int)(1.5 * SIDE_MARGIN);
	public static final int TITLEIMAGE_LEFT_INSET = 190;
	public static final int TITLEBOX_LEFT_INSET = 20;
	public static final int TITLELABEL_LEFT_INSET = -50;
	public static final int TITLE_GRID_GAP = 0;
	
	public static final int IMAGETAB_HEIGHT = 400;
	public static final int MUSICIMAGE_HEIGHT = IMAGETAB_HEIGHT - 3 * SIDE_MARGIN;
	public static final int MUSICIMAGE_WIDTH = MUSICIMAGE_HEIGHT;
	
	public static final int ICONTAB_HEIGHT = (int)(0.075 * MUSICPLAYER_HEIGHT);
	
	public static final int DEFAULT_TIME_PAINT = -1;
	
	public static final int PLAYBAR_HEIGHT = (int)(0.075 * MUSICPLAYER_HEIGHT);
	public static final int PLAYBAR_BAR_HEIGHT = 5;
	public static final int PLAYBAR_BAR_WIDTH = 400;
	public static final int PLAYBAR_BAR_Y = (int)(((float)PLAYBAR_HEIGHT - PLAYBAR_BAR_HEIGHT)/2);
	public static final int PLAYBAR_GRID_HEIGHT = 3 * PLAYBAR_BAR_HEIGHT;
	public static final int PLAYBAR_GRID_WIDTH = 10;
	public static final int PLAYBAR_GRID_Y = (int)(((float)PLAYBAR_HEIGHT - PLAYBAR_GRID_HEIGHT)/2);
	public static final int PLAYBAR_PAINT_DEFAULT = -1;
	public static final int PLAYBAR_PAINT_DEFAULT_LENGTH = 50;
	
	public static final int SONGLISTTAB_WIDTH = MUSICPLAYER_WIDTH - IMAGETAB_HEIGHT;
	public static final int SONGLISTTAB_HEIGHT = MUSICIMAGE_HEIGHT;
	public static final int SONGLISTTAB_NUM = 15;
	public static final int SONGLISTTAB_MODE_SHUFFLE = 0;
	public static final int SONGLISTTAB_MODE_LOOP = 1;
	public static final int SONGLISTTAB_MODE_ONE = 2;
	public static final Color SONGLISTTAB_HIGHLIGHT = new Color(31, 190, 214);
	
	public static final int LAST_SONG_PRESS_TIME = 3;
	
	public static final int BUTTONICON_SIZE = 25;
	public static final int BUTTON_SIZE = (int)(BUTTONICON_SIZE * 1.4);
	
	public static final String[] playModeName = {"shuffle", "loop", "single"};
	public static final String[] playModeImgs = {"player_shuffle.png", "player_loop.png",
		"player_one.png"};
}