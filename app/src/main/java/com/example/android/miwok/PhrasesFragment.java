package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ehayes on 2/8/2017.
 */

public class PhrasesFragment extends Fragment {

    String TAG = "NumbersFragment";
    ArrayList<Word> phrases;
    int[] images;
    ListView listView;
    private MediaPlayer player;
    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                player.pause();
                player.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                player.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
    public PhrasesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        phrases = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultWords).length];
        listView = (ListView) v.findViewById(R.id.list);

        //Add content from resources string-array to ArrayList
        for (int i = 0; i < getResources().getStringArray(R.array.defaultWords).length; i++) {
            phrases.add(new Word((getResources().getStringArray(R.array.miwokPhrases)[i]),
                    getResources().getStringArray(R.array.defaultPhrases)[i],getAudio()[i]));
            //Log.v(TAG,"Word at index "+ i +": "+words.get(i).getMiwokTranslation());
        }

        WordAdapter phraseAdapter = new WordAdapter(getActivity(),phrases,R.color.category_phrases);
        listView.setAdapter(phraseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word listItem = phrases.get(position);
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    player = MediaPlayer.create(getActivity(),listItem.getAudioResourceId());
                    player.start();
                    player.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });

        return v;
    }

    private void releaseMediaPlayer(){
        Log.v(TAG, "releaseMediaPlayer() Called");
        // If the media player is not null, then it may be currently playing a sound.
        if(player != null){
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            player.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            player = null;
            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
    public int[] getAudio(){
        int phrasesAudio[] = new int[10];
        phrasesAudio[0] = R.raw.phrase_where_are_you_going;
        phrasesAudio[1] = R.raw.phrase_what_is_your_name;
        phrasesAudio[2] = R.raw.phrase_my_name_is;
        phrasesAudio[3] = R.raw.phrase_how_are_you_feeling;
        phrasesAudio[4] = R.raw.phrase_im_feeling_good;
        phrasesAudio[5] = R.raw.phrase_are_you_coming;
        phrasesAudio[6] = R.raw.phrase_yes_im_coming;
        phrasesAudio[7] = R.raw.phrase_im_coming;
        phrasesAudio[8] = R.raw.phrase_lets_go;
        phrasesAudio[9] = R.raw.phrase_come_here;
        return phrasesAudio;
    }
}
