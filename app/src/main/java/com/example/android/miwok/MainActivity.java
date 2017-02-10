/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MiwokViewPager(getSupportFragmentManager()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG,"onRestart Called!!");
    }
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart Called!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop Called!!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy Called!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause Called!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume Called!!");
    }
}
