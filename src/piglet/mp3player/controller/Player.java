package piglet.mp3player.controller;

import java.util.Random;

import piglet.mp3player.R;
import piglet.mp3player.common.CPConsts;
import piglet.mp3player.common.CPEnums.ActionMode;
import piglet.mp3player.common.CPEnums.MusicState;
import piglet.mp3player.model.Song;
import piglet.mp3player.services.AudioService;
import piglet.mp3player.services.AudioService.MusicBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Player extends AbstractPlayer {

    /** Seekbar for the song */
    private SeekBar mpSeekBar;

    MediaController mediaController;

    /** The handler is control runnable */
    private Handler handler;

    /** Repeat advance pull-down list */
    private Spinner snChangeRepeatTimes;

    /** current time when the song is playing */
    private TextView tvTimeStart;

    /** Time total for a song */
    private TextView tvTimeEnd;

    private Intent playerIntent;
    private AudioService audioService;

    /** This bundle is used to sending data from Activity to services */
    private Bundle extraMusicBundle;

    private boolean musicBound;
    private boolean isRand;
    private ActionMode repeatMode;

    // ////////////////////////////////////////
    // Constant
    // /////////////

    private static final String AUDIO_SERVICE = "piglet.mp3player.services.audioService";

    // //////////////////////////////////////
    // Override method
    // /////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_lo);
        getSongList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (playerIntent == null) {
            // playerIntent = new Intent(this, AudioService.class);
            playerIntent = new Intent(AUDIO_SERVICE);
            bindService(playerIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);

            extraMusicBundle = new Bundle();
            extraMusicBundle.putString("actionName", "Player");
            playerIntent.putExtra("songIndex", getIntent().getIntExtra("songIndex", 0));
            startService(playerIntent);

            // if (!MusicState.Playing.equals(audioService.getMusicState())) {
            // audioService.setMusicState(MusicState.Playing);
            // }
        }

        activityIntit();
    }

    // @Override
    // protected void onSaveInstanceState(Bundle outState) {
    // outState.putInt("mediaPlayer", mediaPlayer.getAudioSessionId());
    // outState.putInt("repeatCount", repeatCount);
    // outState.putInt("CurrentPosition", mediaPlayer.getCurrentPosition());
    // // outState.putSerializable("mediaPlayer", mediaPlayer);
    // getIntent().putExtras(savedInstanceState);
    // super.onSaveInstanceState(outState);
    // Player.savedInstanceState = outState;
    // }
    //
    @Override
    protected void onResume() {
        super.onResume();
    }

    //
    // @Override
    // protected void onRestart() {
    // // TODO Auto-generated method stub
    // super.onRestart();
    // }

    // @Override
    // protected void onPause() {
    // // TODO Auto-generated method stub
    // super.onPause();
    // if (isBackPress) {
    // // onSaveInstanceState(Player.savedInstanceState);
    // }
    // }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // audioService.destroyMedia();
        // stopService(playerIntent);
        // audioService = null;

        super.onDestroy();
    }

    // //////////////////////////////////////
    // Execute method
    // /////////

    /**
     * Next to song
     *
     * @param view
     */
    public void forward(View view) {
        if (musicBound) {
            if (isRand) {
                Random rand = new Random();
                currentSongIndex = rand.nextInt(songList.size() - 1);
            } else {
                changeSongIdx();
            }
            audioService.setActionMode(ActionMode.Next);
            audioService.setCurrentSongIndex(currentSongIndex);
            setDisplayMusicContent();
            audioService.nexṭ();
        }
    }
    /**
     * Previous the song
     *
     * @param view
     */
    public void rewind(View view) {
        if (musicBound) {
            // Set music state flag.
            if (isRand) {
                Random random = new Random();
                currentSongIndex = random.nextInt(songList.size() - 1);
            } else {
                changeSongIdx();
            }
            audioService.prev();
            audioService.setActionMode(ActionMode.Prev);
            audioService.setCurrentSongIndex(currentSongIndex);
            setDisplayMusicContent();
        }
    }

    /**
     * Play / Pause song
     *
     * @param view
     */
    public void play(View view) {
        ImageButton btPlay = (ImageButton) findViewById(R.id.btPlayPause);
        if (musicBound) {
            if (MusicState.Playing.equals(audioService.getMusicState())) {
                btPlay.setImageResource(android.R.drawable.ic_media_pause);
                audioService.pause(MusicState.Pause);
            } else {
                btPlay.setImageResource(android.R.drawable.ic_media_play);
                audioService.play(MusicState.Playing);
            }
        }
    }

    public void rand(View view) {
        if (isRand) {
            isRand = false;
        } else {
            isRand = true;
        }
    }

    /**
     * Change action mode of the player
     *
     **/
    public void changeRepeatMode(View view) {
        ImageButton btActMode = (ImageButton) findViewById(R.id.btChangeAct);
        switch (repeatMode) {
            case None:
                repeatMode = ActionMode.Repeat;
                btActMode.setImageResource(R.drawable.media_repeat);
                break;
            case Repeat:
                repeatMode = ActionMode.RepeatOne;
                btActMode.setImageResource(R.drawable.media_repeat_one);
                break;
            default:
                repeatMode = ActionMode.None;
                btActMode.setImageResource(R.drawable.media_none_mode);
                break;
        }
        setDisplayRepeatMode(repeatMode);

        if (musicBound) {
            audioService.setActionMode(repeatMode);
        }
    }

    // //////////////////////////////////////
    // private logic method
    // /////////
    /**
     * I will check next song is valid. <br />
     * So if it's valid. I will get next index of the song.<br />
     * If if it finished playing the end song, Stop and release memory
     *
     * @return true. If can next playing<br />
     *         false: If can't next playing and stopped
     */
    private boolean changeSongIdx() {
        // MusicState mode = Enum.valueOf(MusicState.class,
        // String.valueOf(actionMode));
        currentSongIndex = audioService.getCurrentSongIndex();

        switch (actionMode) {
            case Prev:
                currentSongIndex = currentSongIndex == 0 ? songList.size() - 1 : --currentSongIndex;
                break;
            case Next:
                currentSongIndex = currentSongIndex < songList.size() - 1 ? ++currentSongIndex : 0;
                break;
            case Repeat:
                if ("Forever".equals(snChangeRepeatTimes.getSelectedItem())) {
                    break;
                } else if (repeatCount < Integer.valueOf(snChangeRepeatTimes.getSelectedItem().toString())) {

                    // Count the number of the time has repeated
                    repeatCount++;
                    break;
                }

                if (currentSongIndex < songList.size() - 1) {
                    currentSongIndex++;

                    // Reset for new song
                    repeatCount = 0;

                    // If it's the end song
                } else {
                    // Reset to first song.
                    currentSongIndex = 0;
                }
                break;
            case RepeatOne:
                break; // no change current Song index.
            case Rand:
                // TODO: enter code here
                break;
            default:
                if (currentSongIndex < songList.size() - 1) {
                    currentSongIndex++;

                    // If it's the end song
                } else {
                    // Reset to first song.
                    currentSongIndex = 0;

                    // Stop player
                    // audioService.stop();
                    // mediaPlayer.reset();
                    return false;
                }
                break;
        }

        return true;
    }

    /**
     * When I seek on seekbar. Song will playing corresponding
     * */
    private void seekBar() {
        mpSeekBar.setMax(audioService.getDuration());
        mpSeekBar.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
    }

    //
    /**
     * When I seek on seekbar. Song will playing corresponding
     */
    private void seekChange(View v) {
        if (MusicState.Playing.equals(audioService.getMusicState())) {

            SeekBar sb = (SeekBar) v;
            audioService.seekTo(sb.getProgress());
        }
    }

    /**
     * Update current song status
     *
     **/
    private void playProgressUpdate() {

        Runnable progressSeekBarRunnable = new Runnable() {

            @Override
            public void run() {
                if (audioService != null && audioService.isPlaying()) {
                    mpSeekBar.setProgress(audioService.getCurrentPosition());
                    tvTimeStart.setText(convertToMinutes(mpSeekBar.getProgress()));

                    // Reset song information
                    if (audioService.getCurrentPosition() < 5000) {
                        setDisplayMusicContent();
                    }

                    playProgressUpdate();
                } else {
                    mpSeekBar.setProgress(0);
                }

            }
        };
        // progressSeekThread = new Thread(progressSeekBarRunnable);
        // progressSeekThread.start();
        handler = new Handler();
        handler.postDelayed(progressSeekBarRunnable, 1000);
    }

    private String convertToMinutes(int time) {
        String duration;

        int seconds = time / 1000 % 60;
        int minutes = time / (1000 * 60) % 60;

        duration = TextUtils.concat(Integer.toString(minutes), CPConsts.COLON, String.format("%02d", seconds)).toString();
        return duration;
    }

    private void setDisplayRepeatMode(ActionMode mode) {
        snChangeRepeatTimes = (Spinner) findViewById(R.id.spnRepeatTimes);

        if (ActionMode.Repeat.equals(mode)) {
            if (!snChangeRepeatTimes.isShown()) {
                snChangeRepeatTimes.setVisibility(View.VISIBLE);
            }
            snChangeRepeatTimes.setSelection(6);
        } else {
            snChangeRepeatTimes.setVisibility(View.INVISIBLE);
        }
    }

    protected ServiceConnection musicServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder musicBinder = (MusicBinder) service;
            audioService = musicBinder.getService();
            audioService.setSongList(songList);
            audioService.setActionMode(repeatMode);
            audioService.setCurrentSongIndex(getIntent().getIntExtra("songIndex", 0));
            if (ActionMode.FromList.equals(getIntent().getSerializableExtra("actionMode"))) {
                audioService.setFromList(true);
            } else {
                audioService.setFromList(false);
            }

            audioService.start();
            musicBound = true;
            playerIntent.removeExtra("songIndex");
            seekBar();
            setDisplayMusicContent();
        }
    };

    private void activityIntit() {
        mpSeekBar = (SeekBar) findViewById(R.id.playSeekBar);
        tvTimeStart = (TextView) findViewById(R.id.tvTimeStart);
        tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);
        snChangeRepeatTimes = (Spinner) findViewById(R.id.spnRepeatTimes);

        repeatMode = ActionMode.None;
        isRand = false;
    }

    /**
     *
     **/
    private void setDisplayMusicContent() {
        // Set display current Song.
        if (musicBound) {
            Song currentSong = audioService.getCurrentSong();
            TextView currentSongDisp = (TextView) findViewById(R.id.currentSongTxt);
            TextView tvAlbumDisp = (TextView) findViewById(R.id.tvAlbumDis);
            currentSongDisp.setText(currentSong.getSongTitle());
            tvAlbumDisp.setText(currentSong.getSongAblumn());

            tvTimeEnd.setText(convertToMinutes((int) currentSong.getTimeTotal()));

            // Set progress seekbar status
            playProgressUpdate();
        }
    }
}