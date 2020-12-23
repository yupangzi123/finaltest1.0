package com.example.finaltest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this,SecondActivity::class.java)
        denglu.setOnClickListener {
            val uri = Uri.parse("content://com.example.sqlitedemo.provider/users")
            val cursor = contentResolver.query(uri,null,null,null,null)
            cursor?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex("name"))
                    val password = getString(getColumnIndex("password"))
                    val admin = getString(getColumnIndex("admin"))
                    Log.d("Users", "name = $name ,  password = $password, admin = $admin")
                    if (editText_name.text.toString().equals(name) && editText_Password.text.toString().equals(password)){
                        Log.d("YEAH","SUCCESS")
                        intent.putExtra ("name", name)
                        startActivity(intent)
                        close()
                    }else{
                        Log.d("YEAH","FALSE")
                    }
                }
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("提示")
                builder.setMessage("账号或密码错误")
                builder.setPositiveButton("确定", null)
                builder.show()
                close()
            }
        }
    }

}