package com.example.cesarguerrero.movies

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BlankFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BlankFragment : android.support.v4.app.Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var newRow = ContentValues()

    var movieInfo=ArrayList<ArrayList<String>>()
    lateinit var dhHelper: ChatDataBaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var results: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var dataPassed= getArguments()

        // Inflate the layout for this fragment
        var screen=inflater.inflate(R.layout.fragment_blank, container, false)
        var title_textView=screen.findViewById<TextView>(R.id.text_title)
        var duration_textView=screen.findViewById<TextView>(R.id.text_duration)
        var actors_textView=screen.findViewById<TextView>(R.id.text_actors)
        var year_textView=screen.findViewById<TextView>(R.id.text_year)
        var rating_textView=screen.findViewById<TextView>(R.id.text_rating)
        var description_textView=screen.findViewById<TextView>(R.id.text_description)
        var saveButton=screen.findViewById<Button>(R.id.saveIt)
//        var poster=screen.findViewById<ImageView>(R.id.text_poster)


        val getdata_title = (dataPassed?.getString("title"))
        val getdata_duration = (dataPassed?.getString("duration"))
        val getdata_actors = (dataPassed?.getString("actors"))
        val getdata_year = (dataPassed?.getString("year"))
        val getdata_rating = (dataPassed?.getString("rating"))
        val getdata_description = (dataPassed?.getString("description"))
        val getdata_poster = (dataPassed?.getString("posterUrl"))

        title_textView.setText("Title: "+getdata_title)
        duration_textView.setText("Runtime: "+getdata_duration)
        actors_textView.setText("Cast: "+getdata_actors)
        year_textView.setText("Year: "+getdata_year)
        rating_textView.setText("Rating: "+getdata_rating)
        description_textView.setText("Synopsys: "+getdata_description)

//
        dhHelper = ChatDataBaseHelper()// this will open the database

        db = dhHelper.writableDatabase // you can insert into here

        results = db.query(TABLE_NAME, arrayOf( dhHelper.KEY_TITLE,dhHelper.KEY_RUNTIME,dhHelper.KEY_ACTORS,dhHelper.KEY_YEAR,dhHelper.KEY_RATED,dhHelper.KEY_PLOT,dhHelper.KEY_POSTER, dhHelper.KEY_ID),
            null, null, null, null,null,null
        )

        saveButton.setOnClickListener {


            var newRow = ContentValues()
            newRow.put( dhHelper.KEY_TITLE, getdata_title)
            newRow.put( dhHelper.KEY_RUNTIME, getdata_duration)
            newRow.put( dhHelper.KEY_ACTORS, getdata_actors)
            newRow.put( dhHelper.KEY_YEAR, getdata_year)
            newRow.put( dhHelper.KEY_RATED, getdata_rating)
            newRow.put( dhHelper.KEY_PLOT, getdata_description)
            newRow.put( dhHelper.KEY_POSTER, getdata_poster)

            db.insert(TABLE_NAME, "", newRow)
            results = db.query(TABLE_NAME, arrayOf( dhHelper.KEY_TITLE,dhHelper.KEY_RUNTIME,dhHelper.KEY_ACTORS,dhHelper.KEY_YEAR,dhHelper.KEY_RATED,dhHelper.KEY_PLOT,dhHelper.KEY_POSTER, dhHelper.KEY_ID),
                null, null, null, null,null,null
            )

            Toast.makeText(activity, "Item was saved", Toast.LENGTH_LONG).show()


        }
        return  screen

    }
    val DATABASE_NAME = "Movies.db"
    val VERSION_NUM = 4
    val TABLE_NAME = "Movies"

    inner class ChatDataBaseHelper(ctx:Context = con ) : SQLiteOpenHelper(ctx, DATABASE_NAME, null, VERSION_NUM)
    {


        val KEY_ID = "_id"
        val KEY_TITLE = "Title"
        val KEY_RUNTIME = "Runtime"
        val KEY_ACTORS = "Actors"
        val KEY_YEAR="Year"
        val KEY_RATED="Rated"
        val KEY_PLOT="Plot"
        val KEY_POSTER="Poster"


        override fun onCreate(db: SQLiteDatabase) {

            db.execSQL("CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TITLE TEXT, $KEY_RUNTIME TEXT, $KEY_ACTORS TEXT, $KEY_YEAR TEXT, $KEY_RATED TEXT, $KEY_PLOT TEXT, $KEY_POSTER TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME) // delete all current data
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }



    }


    override fun onAttach(context: Activity) {
        super.onAttach(context)
        con = context
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
