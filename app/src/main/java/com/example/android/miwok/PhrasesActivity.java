package com.example.android.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ehayes on 1/21/2017.
 */

public class PhrasesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String TAG = "NumbersActivity";
    ArrayList<Word> phrases;
    int[] images;
    ListView listView;
    MediaPlayer player;
    ActionBar actionBar;
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
        //To allow UP navigation in the Action Bar
        //getActionBar().setDisplayShowHomeEnabled(true);
        phrases = new ArrayList<Word>();
        images = new int[getResources().getStringArray(R.array.defaultWords).length];
        listView = (ListView) findViewById(R.id.list);

        //Add content from resources string-array to ArrayList
        for (int i = 0; i < getResources().getStringArray(R.array.defaultWords).length; i++) {
            phrases.add(new Word((getResources().getStringArray(R.array.miwokPhrases)[i]),
                    getResources().getStringArray(R.array.defaultPhrases)[i],getAudio()[i]));
            //Log.v(TAG,"Word at index "+ i +": "+words.get(i).getMiwokTranslation());
        }

        WordAdapter phraseAdapter = new WordAdapter(this,phrases,R.color.category_phrases);
        listView.setAdapter(phraseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word listItem = phrases.get(position);
                player = MediaPlayer.create(PhrasesActivity.this,listItem.getAudioResourceId());
                player.start();
                player.setOnCompletionListener(mOnCompletionListener);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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
