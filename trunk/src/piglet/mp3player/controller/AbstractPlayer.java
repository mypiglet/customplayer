package piglet.mp3player.controller;

import java.util.ArrayList;
import java.util.List;

import piglet.mp3player.common.CPEnums.ActionMode;
import piglet.mp3player.common.CPEnums.MusicState;
import piglet.mp3player.model.Song;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

public abstract class AbstractPlayer extends Activity {

    /** Music status */
    protected MusicState musicState = MusicState.Preparing;
    protected ActionMode actionMode = ActionMode.None;
    protected ActionMode randMode = ActionMode.None;

    /** The song is currently playing */
    protected int currentSongIndex = 0;

    /** The Song object is currently playing */
    protected Song currentSong;

    /** Count the number of repeat for a song */
    protected int repeatCount;

    protected ArrayList<Song> songList;

    public AbstractPlayer() {
    }

    // //////////////////////////////////////
    // Business method
    // /////////

    /**
     * Get all song from memory to list for player
     */
    public List<Song> getSongList() {
        songList = new ArrayList<Song>();
        Song song;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Fetch song from Memory card.
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.isFirst() == cursor.isLast()) {
            return songList;
        }
        do {
            song = new Song();

            // Get index which song is located
            song.setSongId(cursor.getLong(cursor.getColumnIndex(Media._ID)));
            song.setSongTitle(cursor.getString(cursor.getColumnIndex(Media.TITLE)));
            song.setSongAblumn(cursor.getString(cursor.getColumnIndex(Media.ALBUM)));
            song.setTimeTotal(cursor.getLong(cursor.getColumnIndex(Media.DURATION)));

            songList.add(song);
        } while (cursor.moveToNext());

        return songList;
    }

}
