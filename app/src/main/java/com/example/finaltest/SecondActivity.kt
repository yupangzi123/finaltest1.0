package com.example.finaltest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.finaltest.gamefragment.model.GameFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_second.*

const val CHAT_INTENT = "com.example.fragmentdemo.Chart.New"
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment1 = GameFragment()
        setContentView(R.layout.activity_second)

        //textView.setText("欢迎您！"+intent.getStringExtra("name"))
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout,fragment1)
                .commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNV)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.game ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,fragment1)
                        .commit()
            }
            true
        }

        val intentFilter = IntentFilter(CHAT_INTENT)
        val receiver = MyReceiver()
        registerReceiver(receiver,intentFilter)


    }

    fun printFragments() {
        supportFragmentManager.fragments.forEach {
            Log.d("Fragment","id: ${it}")
        }

    }


}

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("BroadcastReceiver","onReceive")
    }

}