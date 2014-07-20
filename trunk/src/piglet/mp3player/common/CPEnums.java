package piglet.mp3player.common;

public class CPEnums {

    /**
     * <p>
     * Music state and music command
     * </p>
     * Stopped: stopped state. The song no longer to play <br />
     * Pause: Pause state<br />
     * Playing: Playing state<br />
     * Preparing: Preparing state <br />
     * */
    public enum MusicState {
        /* Stop state */Stopped(1)
        /* Pause state */, Pause(2)
        /* Playing state */, Playing(3)
        /* Preparing state */, Preparing(4)
        /* None */, None(10);

        MusicState(final int code) {
            this.code = code;
        }

        private int code;

        /**
         * Get code value of enum option
         * 
         * @return (int) code value
         */
        public int getCode() {
            return code;
        }
    }

    /**
     * <p>
     * Mmusic command
     * </p>
     * Next: next command. <br />
     * Pre: Previous command<br />
     * rand: Random mode<br />
     * repeat: Repeat mode <br />
     * */
    public enum ActionMode {
        /* Next command */Next(1)
        /* Previous command */, Prev(2)
        /* Random mode */, Rand(3)
        /* Repeat mode */, Repeat(4)
        /* Repeat one mode */, RepeatOne(5)
        /* None */, None(6)
        /* The song is selected from List */, FromList(7);

        ActionMode(final int code) {
            this.code = code;
        }

        private int code;

        /**
         * Get code value of enum option
         * 
         * @return (int) code value
         */
        public int getCode() {
            return code;
        }
    }

}
