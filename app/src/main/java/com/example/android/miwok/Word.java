package com.example.android.miwok;

/**
 * Created by ehayes on 1/22/2017.
 */

public class Word {

    private String mMiwokTranslation;
    private String mDefaultTranslation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;


    public Word(String miwokWord, String defaultWord){
        mMiwokTranslation = miwokWord;
        mDefaultTranslation = defaultWord;
    }

    public Word(String miwokWord, String defaultWord, int audio){
        mMiwokTranslation = miwokWord;
        mDefaultTranslation = defaultWord;
        mAudioResourceId = audio;
    }


    public Word(String miwokWord, String defaultWord, int image, int audioResourceId){
        mMiwokTranslation = miwokWord;
        mDefaultTranslation = defaultWord;
        mImageResourceId = image;
        mAudioResourceId = audioResourceId;
    }


    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }
    public int getImageResourceId(){
        return mImageResourceId;
    }
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
    public int getAudioResourceId(){
        return mAudioResourceId;
    }

}
