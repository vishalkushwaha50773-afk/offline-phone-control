package com.vishal.offlinephonecontrol

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.btnStart)
        val stopButton = findViewById<Button>(R.id.btnStop)

        startButton.setOnClickListener {
            Toast.makeText(this, "Assistant Started", Toast.LENGTH_SHORT).show()
        }

        stopButton.setOnClickListener {
            Toast.makeText(this, "Assistant Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}
