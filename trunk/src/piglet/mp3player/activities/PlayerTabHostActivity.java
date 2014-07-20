package piglet.mp3player.activities;

import piglet.mp3player.R;
import piglet.mp3player.controller.DirectoryActivity;
import piglet.mp3player.controller.Player;
import piglet.mp3player.controller.PlayerList;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class PlayerTabHostActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_tab_host);

        TabHost tabHost = getTabHost();

        // Player
        TabSpec tabSpecPlayer = tabHost.newTabSpec("Player");
        tabSpecPlayer.setIndicator("Player", getResources().getDrawable(R.drawable.selector_playertabhost_player_tab));
        Intent intentPlayer = new Intent(this, Player.class);
        tabSpecPlayer.setContent(intentPlayer);

        // Player List
        TabSpec tabSpecPlayerList = tabHost.newTabSpec("PlayerList");
        tabSpecPlayerList.setIndicator("Player List", getResources().getDrawable(R.drawable.selector_playertabhost_player_tab));
        Intent intentPlayerList = new Intent(this, PlayerList.class);
        tabSpecPlayerList.setContent(intentPlayerList);

        // Directory list
        TabSpec tabSpecDir = tabHost.newTabSpec("Directory");
        Intent intentDir = new Intent(this, DirectoryActivity.class);
        tabSpecDir.setIndicator("Directory", getResources().getDrawable(R.drawable.selector_playertabhost_player_tab))
                  .setContent(intentDir);


        tabHost.addTab(tabSpecDir);
        tabHost.addTab(tabSpecPlayer);
        tabHost.addTab(tabSpecPlayerList);
//        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.icon_playertabhost_dir_tab);
    }

    @SuppressLint("NewApi")
	private View prepareTabView(String text, int resId) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        view.findViewById(R.id.imageViewTab).setBackgroundResource(R.drawable.selector_playertabhost_dir_tab);
        TextView tv = (TextView) view.findViewById(R.id.textViewTab);
        tv.setText(text);
        return view;
   }
}
