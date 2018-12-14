package com.example.cesarguerrero.movies

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MovieFavorites : AppCompatActivity() {
    var movieInfo=ArrayList<ArrayList<String>>()
    lateinit var adapter: FavAdapter
    lateinit var dhHelper: BlankFragment.ChatDataBaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var results: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_favorites)


        var favList = findViewById<ListView>(R.id.myList)

        dhHelper = BlankFragment().ChatDataBaseHelper(this)// this will open the database

        db = dhHelper.writableDatabase // you can insert into here

        results = db.query(TABLE_NAME, arrayOf( dhHelper.KEY_TITLE,dhHelper.KEY_RUNTIME,dhHelper.KEY_ACTORS,dhHelper.KEY_YEAR,dhHelper.KEY_RATED,dhHelper.KEY_PLOT,dhHelper.KEY_POSTER, dhHelper.KEY_ID),
            null, null, null, null,null,null
        )

        Log.i("result", results.toString())
        val numColumns = results.getColumnCount()

        Log.i("Movie", "Cursor’s  column count =” + numColumns")
        for(i in 0..numColumns-1) {
            Log.i("Movie", "Column number " + i + " = " + results.getColumnName(i))
        }

        results.moveToFirst() // read from the first row
        val titleIndex = results.getColumnIndex( dhHelper.KEY_TITLE)
        val runtimeIndex = results.getColumnIndex(dhHelper.KEY_RUNTIME)
        val actorIndex = results.getColumnIndex(dhHelper.KEY_ACTORS)
        val yearIndex =results.getColumnIndex(dhHelper.KEY_YEAR)
        val ratedIndex = results.getColumnIndex(dhHelper.KEY_RATED)
        val plotIndex = results.getColumnIndex(dhHelper.KEY_PLOT)
        val posterIndex = results.getColumnIndex(dhHelper.KEY_POSTER)

        while(!results.isAfterLast)
        {
            var newArr=ArrayList<String>()
            newArr.add(results.getString( titleIndex ))
            newArr.add(results.getString( runtimeIndex ))
            newArr.add(results.getString( actorIndex ))
            newArr.add(results.getString( yearIndex ))
            newArr.add(results.getString( ratedIndex ))
            newArr.add(results.getString( plotIndex ))
            newArr.add(results.getString( posterIndex ))
            movieInfo.add(newArr)
            results.moveToNext()

        }


        adapter = FavAdapter(this)
        favList.setAdapter(adapter)
        adapter.notifyDataSetChanged()

    }
    inner class FavAdapter(ctx: Context) : ArrayAdapter<ArrayList<String>>(ctx,0){
        override fun getCount(): Int {
            return movieInfo.size
        }
        override fun getItem(position: Int): ArrayList<String>? {
            return movieInfo.get(position)
        }
        override fun getItemId(position: Int): Long {
            return 0
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var createList= LayoutInflater.from(parent?.getContext())
            var rowSelected=createList.inflate(R.layout.movie_favorites_display, null)
            var displayMTitle=rowSelected.findViewById<TextView>(R.id.movie_title)
            var displayMPlot=rowSelected.findViewById<TextView>(R.id.movie_actors)
            var deleteBtn=rowSelected.findViewById<Button>(R.id.delete)


            var pos=movieInfo.get(position)
            deleteBtn.setOnClickListener {
                dhHelper.writableDatabase.delete(BlankFragment().TABLE_NAME, "_id=$position", null)
                movieInfo.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(this@MovieFavorites, "Item was deleted", Toast.LENGTH_LONG).show()
            }
            displayMTitle.setText(pos[0])
            displayMPlot.setText(pos[2])
            return rowSelected
        }
    }

    val DATABASE_NAME = "Movies.db"
    val VERSION_NUM = 4
    val TABLE_NAME = "Movies"

}
