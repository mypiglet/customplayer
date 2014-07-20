package piglet.mp3player.services;

import java.io.IOException;
import java.util.List;

import piglet.mp3player.common.CPEnums.ActionMode;
import piglet.mp3player.common.CPEnums.MusicState;
import piglet.mp3player.model.Song;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

public class AudioService extends Service implements OnCompletionListener {

    protected MediaPlayer mediaPlayer;

    private final Binder audioServiceBinder = new MusicBinder();

    /** Music status */
    private MusicState musicState;
    protected ActionMode actionMode;

    protected MusicState randMode = MusicState.None;

    /** The song is currently playing */
    protected int currentSongIndex = 0;

    /** The Song object is currently playing */
    protected Song currentSong;

    /** Count the number of repeat for a song */
    protected int repeatCount;

    /** List all song */
    protected List<Song> songList;

    private boolean isFromList;

    // //////////////////////////////////////
    // OVERRIDE method
    // /////////

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return audioServiceBinder;
    }

    public class MusicBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // if ("Player".equals(intent.getStringExtra("actionName"))) {
        // }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // if (changeSongIdx()) {
        mp.stop();
        mp.reset();
        nexṭ();
        // } else {
        // musicState = MusicState.None;
        // }
    }

    // //////////////////////////////////////
    // Business method
    // /////////

    public void start() {
        initPlay();
    }

    /**
     * Next the song which is playing
     *
     * @param state
     */
    public void nexṭ() {
        musicState = MusicState.Playing;
        playSong();
    }

    /**
     * Previous music which is playing
     *
     * @param state
     */
    public void prev() {
        musicState = MusicState.Playing;
        actionMode = ActionMode.Prev;
        // changeSongIdx();
        playSong();
    }

    /**
     * Pause music is playing
     **/
    public void pause(MusicState state) {
        if (mediaPlayer.isPlaying()) {
            musicState = state;
            mediaPlayer.pause();
        }
    }

    public void play(MusicState state) {
        if (!mediaPlayer.isPlaying()) {
            musicState = state;
            mediaPlayer.start();
        }
    }

    /**
     * Stop music is playing and release memory
     */
    public void destroyMedia() {
        if (mediaPlayer != null || mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        } else {
            return mediaPlayer.isPlaying();
        }
    }

    /**
     * Seeks to specified time position.
     *
     * @param position
     */
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    /**
     * Release media player
     */
    protected void releaseMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    // //////////////
    // Getter Setter
    // //////////////

    public void setMusicState(MusicState musicState) {
        this.musicState = musicState;
    }

    public MusicState getMusicState() {
        return musicState;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Gets the duration of the file.
     *
     * @return duration
     */
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * Gets the current playback position.
     *
     * @return the current position in milliseconds
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        this.currentSongIndex = currentSongIndex;
    }

    public void setFromList(boolean isFromList) {
        this.isFromList = isFromList;
    }

    // //////////////////////////////////////
    // Private method
    // /////////

    /**
     * Prepare for play these song
     */
    private void initPlay() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
        }

        musicState = MusicState.Playing;
        playSong();
    }

    private void playSong() {

        if (songList.isEmpty()) {
            return;
        }
        // Reset music state
        if (mediaPlayer.isPlaying()
                && !(ActionMode.Next.equals(actionMode) || ActionMode.Prev.equals(actionMode) || isFromList)) {
            return;
        }

        currentSong = songList.get(currentSongIndex);
        Uri contentUris = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong.getSongId());

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), contentUris);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // /**
    // * I will check next song is valid. <br />
    // * So if it's valid. I will get next index of the song.<br />
    // * If if it finished playing the end song, Stop and release memory
    // *
    // * @return true. If can next playing<br />
    // * false: If can't next playing and stopped
    // */
    // private boolean changeSongIdx() {
    // // MusicState mode = Enum.valueOf(MusicState.class,
    // // String.valueOf(actionMode));
    //
    // switch (actionMode) {
    // case Prev:
    // currentSongIndex = currentSongIndex == 0 ? songList.size() - 1 :
    // --currentSongIndex;
    // break;
    // case Next:
    // currentSongIndex = currentSongIndex < songList.size() - 1 ?
    // ++currentSongIndex : 0;
    // break;
    // case Repeat:
    // // if ("Forever".equals(snChangeRepeatTimes.getSelectedItem()))
    // // {
    // // break;
    // // } else if (repeatCount <
    // // Integer.valueOf(snChangeRepeatTimes.getSelectedItem().toString()))
    // // {
    // //
    // // // Count the number of the time has repeated
    // // repeatCount++;
    // // break;
    // // }
    //
    // if (currentSongIndex < songList.size() - 1) {
    // currentSongIndex++;
    //
    // // Reset for new song
    // repeatCount = 0;
    //
    // // If it's the end song
    // } else {
    // // Reset to first song.
    // currentSongIndex = 0;
    // }
    // break;
    // case RepeatOne:
    // break; // no change current Song index.
    // case Rand:
    // // TODO: enter code here
    // break;
    // default:
    // if (currentSongIndex < songList.size() - 1) {
    // currentSongIndex++;
    //
    // // If it's the end song
    // } else {
    // // Reset to first song.
    // currentSongIndex = 0;
    //
    // // Stop player
    // mediaPlayer.stop();
    // mediaPlayer.reset();
    // return false;
    // }
    // break;
    // }
    //
    // return true;
    // }

}
