package piglet.mp3player.controller;

import java.util.ArrayList;
import java.util.List;

import piglet.mp3player.R;
import piglet.mp3player.common.CPEnums.ActionMode;
import piglet.mp3player.model.Song;
import piglet.mp3player.services.AudioService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayerList extends AbstractPlayer implements OnItemClickListener {

    private List<String> songs;

    ListView listViewSong;

    private Bundle savedInstanceState;
    private AudioService audioService;
    private Intent listIntent;

    // //////////////////////////////////////
    // Override method
    // /////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_lo);
        getSongList();
        listViewSong = (ListView) findViewById(R.id.list);

        songs = new ArrayList<String>();
        for (Song song : songList) {
            songs.add(song.getSongTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlayerList.this, R.layout.playlist_lo, R.id.tvLv, songs);
        // listViewSong.set(adapter);

        listViewSong.setAdapter(adapter);
        listViewSong.setOnItemClickListener(PlayerList.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        String songTitle = (String) listViewSong.getItemAtPosition(position);
        currentSongIndex = position;
        Intent nowPlayer = new Intent("piglet.mp3player.controller.PLAYER");
        nowPlayer.putExtra("songIndex", currentSongIndex);
        nowPlayer.putExtra("actionMode", ActionMode.FromList);

        Toast.makeText(getApplicationContext(), "Position: " + position + " Song name: "
                + songTitle, Toast.LENGTH_LONG).show();

        startActivity(nowPlayer);
    }

    // private ServiceConnection musicServiceConnection = new
    // ServiceConnection() {
    //
    // @Override
    // public void onServiceDisconnected(ComponentName name) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onServiceConnected(ComponentName name, IBinder service) {
    // MusicBinder binder = (MusicBinder) service;
    // audioService = binder.getService();
    // }
    // };

}
