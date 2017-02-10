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

public class ColorsFragment extends Fragment {
    String TAG = "ColorsFragment";
    ArrayList<Word> color;
    ListView listView;
    int[]images;
    private MediaPlayer player;
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mOncompletionListener = new MediaPlayer.OnCompletionListener() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);
        //Request and setup Audio Manager to request Audio Focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        color = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultColors).length];
        listView = (ListView) v.findViewById(R.id.list);

        for(int i = 0; i < getResources().getStringArray(R.array.defaultColors).length;i++){
            color.add(new Word((getResources().getStringArray(R.array.miwokColors))[i],
                    getResources().getStringArray(R.array.defaultColors)[i],getImages()[i],getAudio()[i]));
        }
        WordAdapter colorAdapter = new WordAdapter(getActivity(),color,R.color.category_colors);
        listView.setAdapter(colorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word listItem = color.get(position);
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    player = MediaPlayer.create(getActivity(),listItem.getAudioResourceId());
                    player.start();
                    player.setOnCompletionListener(mOncompletionListener);
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

    public int[] getImages(){
        images[0] = R.drawable.color_red;
        images[1] = R.drawable.color_green;
        images[2] = R.drawable.color_brown;
        images[3] = R.drawable.color_gray;
        images[4] = R.drawable.color_black;
        images[5] = R.drawable.color_white;
        images[6] = R.drawable.color_dusty_yellow;
        images[7] = R.drawable.color_mustard_yellow;
        return images;
    }
    public int[] getAudio(){
        int colorAudio[] = new int[10];
        colorAudio[0] = R.raw.color_red;
        colorAudio[1] = R.raw.color_green;
        colorAudio[2] = R.raw.color_brown;
        colorAudio[3] = R.raw.color_gray;
        colorAudio[4] = R.raw.color_black;
        colorAudio[5] = R.raw.color_white;
        colorAudio[6] = R.raw.color_dusty_yellow;
        colorAudio[7] = R.raw.color_mustard_yellow;
        return colorAudio;
    }
}
