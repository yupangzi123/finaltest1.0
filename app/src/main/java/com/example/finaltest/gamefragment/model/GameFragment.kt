package com.example.finaltest.gamefragment.model

import android.content.ContentValues
import android.content.res.Configuration
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cardgame.CardMatchingGame
import com.example.finaltest.R
import kotlinx.android.synthetic.main.fragment_game.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
const val gameFile = "gameFile"
lateinit var db: SQLiteDatabase
lateinit var cursor: Cursor
class GameFragment : Fragment() {

    companion object {
        private lateinit var game: CardMatchingGame
    }
    val cardButtons = mutableListOf<Button>()
    lateinit var adapter:CardRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val recylerView = view.findViewById<RecyclerView>(R.id.recylerView1)
        val reset = view.findViewById<Button>(R.id.reset)
        val uri = Uri.parse("content://com.example.sqlitedemo.provider/users")
        val contentResolver = getActivity()?.getContentResolver()
        val rgame = loadData()
        Log.d("START","start")
        if (rgame != null) {
            game = rgame
        }else{
            game = CardMatchingGame(24)
        }
        adapter = CardRecyclerViewAdapter(game)
        recylerView.adapter = adapter
        val configuration = resources.configuration
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recylerView.layoutManager = GridLayoutManager(activity, 6)
        }else{
            val gridLayoutManager = GridLayoutManager(activity, 4)
            recylerView.layoutManager = gridLayoutManager
        }
        updateUI()
        adapter.setOnCardClickListener {
            game.chooseCardAtIndex(it)
            updateUI()
        }

        reset.setOnClickListener {
            game.reset()
            updateUI()
        }

        submit.setOnClickListener {
            val score = game.score
            val name = getActivity()?.intent?.getStringExtra("name")
            if (name != null) {
                Log.d("name",name)
            }
            val contentValues = ContentValues().apply {
                put("name",name)
                put("score",score)
                Log.d("VALUES","YEAHHHHHH"+score.toString())
            }
            //db.update(TABLE_NAME,contentValues,"name = ?", arrayOf(name))
            if (contentResolver != null) {
                contentResolver.update(uri,contentValues,"name = ?",arrayOf(name))
                Log.d("UPDATE","OKKKKKKKKK")
                val builder: AlertDialog.Builder? = getActivity()?.let { it1 ->
                    AlertDialog.Builder(
                        it1
                    )
                }
                builder?.setTitle("提示")
                builder?.setMessage("提交成功！")
                builder?.setPositiveButton("确定", null)
                builder?.show()
            }
                Log.d("wan","wYYYYYYYYYY")
        }
    }


    fun updateUI() {
        adapter.notifyDataSetChanged()
        val score = view?.findViewById<TextView>(R.id.tscore)
        //score?.text = String.format("%s%d",getString(R.string.score),game.score)
        score?.text = game.score.toString()
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun saveData() {
        try {
            val output = activity?.openFileOutput(gameFile, AppCompatActivity.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(output)
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
            output?.close()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun loadData(): CardMatchingGame? {
        try {
            val input = activity?.openFileInput(gameFile)
            val objectInputStream =  ObjectInputStream(input)
            val game = objectInputStream.readObject() as CardMatchingGame
            objectInputStream.close()
            input?.close()
            return game
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}