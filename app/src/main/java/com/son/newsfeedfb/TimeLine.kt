package com.son.newsfeedfb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

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

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to exit?")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            super.finish()
            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
        }
        builder.show()
    }

}