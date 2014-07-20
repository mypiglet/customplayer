package piglet.mp3player.controller;

import piglet.mp3player.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        ScrollView scrollView =  (ScrollView) findViewById(R.id.svMain);
//        scrollView.set
        setContentView(R.layout.main);

    }

    public void playerTransit(View view) {
        Intent intentPlayer = new Intent("piglet.mp3player.controller.PLAYER");
        setIntent(intentPlayer);

        startActivity(intentPlayer);
    }

    public void playListTransit(View view) {
        Intent intentPlayer = new Intent("piglet.mp3player.controller.PLAYLIST");
        startActivity(intentPlayer);
    }

    public void dirTransit(View view) {

    }

    public void playListNavTransit(View view) {
        Intent playListNavIntent = new Intent("piglet.mp3player.activities.PLAYLISTACTIVITY");
        startActivity(playListNavIntent);
    }

    public void playerTabTransit(View view) {
        Intent playListNavIntent = new Intent("piglet.mp3player.activities.PLAYERTABACTIVITY");
        startActivity(playListNavIntent);
    }

    public void playerTabHostTransit(View view) {
        Intent playListNavIntent = new Intent("piglet.mp3player.activities.PLAYERTABHOSTACTIVITY");
        startActivity(playListNavIntent);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}
