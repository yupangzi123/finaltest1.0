package com.example.finaltest

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_rank.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri = Uri.parse("content://com.example.sqlitedemo.provider/users")
        val contentResolver = getActivity()?.getContentResolver()
        val cursor = contentResolver?.query(uri,null,null,null,null)
        adapter = MyRecyclerViewAdapter(cursor!!)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_rank, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class MyRecyclerViewAdapter(var cursor: Cursor) :
        RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        fun swapCursor(newCursor: Cursor) {
            if (cursor == newCursor) return
            cursor.close()
            cursor = newCursor
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView_Name: TextView
            val textView_Password: TextView
            val textView_Admin: TextView
            val textView_Score: TextView

            init {
                textView_Name = view.findViewById(R.id.textView_Name)
                textView_Password = view.findViewById(R.id.textView_Age)
                textView_Admin = view.findViewById(R.id.textView_Gender)
                textView_Score = view.findViewById(R.id.textView_score)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.person_layout, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            cursor.moveToPosition(position)
            holder.textView_Name.text = cursor.getString(cursor.getColumnIndex("name"))
            holder.textView_Password.text = cursor.getString(cursor.getColumnIndex("password"))
            holder.textView_Admin.text =
                if (cursor.getInt(cursor.getColumnIndex("admin")) == 1) "是" else "否"
            holder.textView_Score.text = cursor.getString(cursor.getColumnIndex("score"))
        }

        override fun getItemCount(): Int {
            return cursor.count
        }
    }
}