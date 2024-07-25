package com.skysoftsolution.todolistreminder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.skysoftsolution.todolistreminder.todoList.DashBoardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val handler = Handler()
        handler.postDelayed({
            val mIntent = Intent(
                this@MainActivity,
                DashBoardActivity::class.java
            )
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mIntent)
            finish()
        }, 1000)
    }
}