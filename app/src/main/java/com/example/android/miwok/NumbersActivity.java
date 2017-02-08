package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ehayes on 1/21/2017.
 */

public class NumbersActivity extends AppCompatActivity{
    String TAG = "NumbersActivity";
    ArrayList<Word> words;
    int[] images;
    ListView listView;
    MediaPlayer player;
    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;


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

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate Called!!!");
        setContentView(R.layout.word_list);
        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        words = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultWords).length];
        listView = (ListView) findViewById(R.id.list);

        //Add content from resources string-array to ArrayList
        for (int i = 0; i < getResources().getStringArray(R.array.defaultWords).length; i++) {
            words.add(new Word((getResources().getStringArray(R.array.miwokWords)[i]),
                    getResources().getStringArray(R.array.defaultWords)[i],getImages()[i],getAudio()[i]));
            //Log.v(TAG,"Word at index "+ i +": "+words.get(i).getMiwokTranslation());
        }

        WordAdapter wordAdapter = new WordAdapter(this,words,R.color.category_numbers);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();
                Word wordItem = words.get(position);
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    player = MediaPlayer.create(NumbersActivity.this,wordItem.getAudioResourceId());
                    player.start();
                    player.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }

    private void releaseMediaPlayer(){
        // If the media player is not null, then it may be currently playing a sound.
        if(player != null){
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            player.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            player = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause Called!!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public int[] getImages(){
        images[0] = R.drawable.number_one;
        images[1] = R.drawable.number_two;
        images[2] = R.drawable.number_three;
        images[3] = R.drawable.number_four;
        images[4] = R.drawable.number_five;
        images[5] = R.drawable.number_six;
        images[6] = R.drawable.number_seven;
        images[7] = R.drawable.number_eight;
        images[8] = R.drawable.number_nine;
        images[9] = R.drawable.number_ten;
        return images;
    }

    public int[] getAudio(){
        int numbers[] = new int[10];
        numbers[0] = R.raw.number_one;
        numbers[1] = R.raw.number_two;
        numbers[2] = R.raw.number_three;
        numbers[3] = R.raw.number_four;
        numbers[4] = R.raw.number_five;
        numbers[5] = R.raw.number_six;
        numbers[6] = R.raw.number_seven;
        numbers[7] = R.raw.number_eight;
        numbers[8] = R.raw.number_nine;
        numbers[9] = R.raw.number_ten;
        return numbers;
    }
}
