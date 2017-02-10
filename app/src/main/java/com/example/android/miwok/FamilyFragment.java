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

public class FamilyFragment extends Fragment {
    ArrayList<Word> family;
    ListView listView;
    int[]images;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);
        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        family = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultFamily).length];
        listView = (ListView) v.findViewById(R.id.list);

        for(int i = 0; i < getResources().getStringArray(R.array.defaultFamily).length;i++){
            family.add(new Word(getResources().getStringArray(R.array.miwokFamily)[i],
                    getResources().getStringArray(R.array.defaultFamily)[i],getImages()[i],getAudio()[i]));
        }

        WordAdapter familyAdapter = new WordAdapter(getActivity(),family,R.color.category_family);
        listView.setAdapter(familyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word listItem = family.get(position);
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
        Log.v("FamilyFragment", "releaseMediaPlayer() Called");
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
        images[0] = R.drawable.family_father;
        images[1] = R.drawable.family_mother;
        images[2] = R.drawable.family_son;
        images[3] = R.drawable.family_daughter;
        images[4] = R.drawable.family_older_brother;
        images[5] = R.drawable.family_younger_brother;
        images[6] = R.drawable.family_older_sister;
        images[7] = R.drawable.family_younger_sister;
        images[8] = R.drawable.family_grandmother;
        images[9] = R.drawable.family_grandfather;
        return images;
    }
    public int[] getAudio(){
        int familyAudio[] = new int[10];
        familyAudio[0] = R.raw.family_father;
        familyAudio[1] = R.raw.family_mother;
        familyAudio[2] = R.raw.family_son;
        familyAudio[3] = R.raw.family_daughter;
        familyAudio[4] = R.raw.family_older_brother;
        familyAudio[5] = R.raw.family_younger_brother;
        familyAudio[6] = R.raw.family_older_sister;
        familyAudio[7] = R.raw.family_younger_sister;
        familyAudio[8] = R.raw.family_grandmother;
        familyAudio[9] = R.raw.family_grandfather;
        return familyAudio;
    }
}
