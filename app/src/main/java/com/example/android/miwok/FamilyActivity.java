package com.example.android.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ehayes on 1/21/2017.
 */

public class FamilyActivity extends AppCompatActivity {

    ArrayList<Word> family;
    ListView listView;
    int[]images;
    MediaPlayer player;
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        family = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultFamily).length];
        listView = (ListView) findViewById(R.id.list);

        for(int i = 0; i < getResources().getStringArray(R.array.defaultFamily).length;i++){
            family.add(new Word(getResources().getStringArray(R.array.miwokFamily)[i],
                    getResources().getStringArray(R.array.defaultFamily)[i],getImages()[i],getAudio()[i]));
        }

        WordAdapter familyAdapter = new WordAdapter(this,family,R.color.category_family);
        listView.setAdapter(familyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word listItem = family.get(position);
                player = MediaPlayer.create(FamilyActivity.this,listItem.getAudioResourceId());
                player.start();
                player.setOnCompletionListener(mOnCompletionListener);
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
    protected void onStop() {
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
