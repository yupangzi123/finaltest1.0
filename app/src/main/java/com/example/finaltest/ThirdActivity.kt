package com.example.finaltest

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.finaltest.gamefragment.model.cursor
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.activity_third.tijiao
import kotlinx.android.synthetic.main.fragment_game.*

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val uri = Uri.parse("content://com.example.sqlitedemo.provider/users")
        val intent = Intent(this,MainActivity::class.java)
        tijiao.setOnClickListener {
            val cursor = contentResolver.query(uri,null,null,null,null)
            var flag = true
            cursor?.apply {
                if (etname.text.isBlank() || etpassword.text.isBlank()){
                    val builder1: AlertDialog.Builder = AlertDialog.Builder(this@ThirdActivity)
                    builder1.setTitle("提示")
                    builder1.setMessage("用户名或密码不能为空！！")
                    builder1.setPositiveButton("确定", null)
                    builder1.show()
                    flag = false
                    //close()
                }
                while (moveToNext()) {
                    val name = getString(getColumnIndex("name"))
                    val password = getString(getColumnIndex("password"))
                    Log.d("Users", "name = $name ,  password = $password")
                    if (etname.text.toString().equals(name)){
                        val builder2: AlertDialog.Builder = AlertDialog.Builder(this@ThirdActivity)
                        builder2.setTitle("提示")
                        builder2.setMessage("用户名已存在")
                        builder2.setPositiveButton("确定", null)
                        builder2.show()
                        flag = false

                    }else{
                        Log.d("USEABLE","YES")
                    }


                }
                if (flag){
                val contentValues = ContentValues().apply {
                    val name1 = etname.text.toString()
                    val password1 = etpassword.text.toString()
                    Log.d("name",name1)
                    Log.d("password",password1)
                    put("name",name1)
                    put("password",password1)
                    put("admin",0)
                    put("score",0)
                }
                contentResolver.insert(uri,contentValues)
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@ThirdActivity)
                    val name1 = etname.text.toString()
                    val password1 = etpassword.text.toString()
                    Log.d("name",name1)
                    Log.d("password",password1)
                builder.setTitle("提示")
                builder.setMessage("注册成功！")
                builder.setPositiveButton("确定", null)
                builder.show()
                    swapCursor(cursor)
                }
                swapCursor(cursor)


        }
    }
        fanhui.setOnClickListener {
            startActivity(intent)
        }

}
    fun swapCursor(newCursor: Cursor){
        val uri = Uri.parse("content://com.example.sqlitedemo.provider/users")
        var cursor = contentResolver.query(uri,null,null,null,null)
        if (cursor == newCursor) return
        if (cursor != null) {
            cursor.close()
        }
        cursor = newCursor
    }
}