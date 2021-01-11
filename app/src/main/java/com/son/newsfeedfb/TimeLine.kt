package com.son.newsfeedfb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class TimeLine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        if (savedInstanceState == null) {
            val fragment = PostFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fg_TimeLine, fragment)
                .commit()
        }

    }
}