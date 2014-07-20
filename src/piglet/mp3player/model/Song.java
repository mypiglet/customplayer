package piglet.mp3player.model;

/**
 * Song class
 * <p>
 * 
 * </p>
 * 
 * @author VoXuanHoan
 * @version
 */
public class Song {

    /** The ID of the song */
    private long songId;

    /** name of the song */
    private String songTitle;

    /** Album which is contain the song */
    private String songAblumn;

    /** Time total for a song */
    private long timeTotal;

    public long getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(long timeTotal) {
        this.timeTotal = timeTotal;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongAblumn() {
        return songAblumn;
    }

    public void setSongAblumn(String songAblumn) {
        this.songAblumn = songAblumn;
    }

}
