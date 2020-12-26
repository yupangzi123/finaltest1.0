package com.example.finaltest

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class PersonContentProvider : ContentProvider() {

    private val personTable = 0
    private val personItem = 1
    private val autority = "com.example.sqlitedemo.provider"
    private lateinit var db: SQLiteDatabase
    private val uriMatcher: UriMatcher
    init {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(autority, "users",0)
        uriMatcher.addURI(autority, "users/*",1)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            personTable -> "vnd.android.cursor.dir/vnd.$autority.users"
            personItem -> "vnd.android.cursor.item/vnd.$autority.users"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        context?.let {
            val openSqLiteHelper = MyOpenSqLiteHelper(it,1)
            db = openSqLiteHelper.readableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            personTable -> db.query(TABLE_NAME,null,null,null,null,null,null)
            personItem -> {
                val name = uri.pathSegments[1]
                db.query(TABLE_NAME,null,"name = ?", arrayOf(name),null,null,null)
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var num:Int? = 0
        when(uriMatcher.match(uri)){
            personTable -> num = db?.update(TABLE_NAME, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unrecognized Uri !")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return num ?: 0
    }
}
