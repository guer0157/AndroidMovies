package com.example.cesarguerrero.movies

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity()  {

    var movieSearched = ""
    var movieTitle = ""
    var runtime =""
    var actors =""
    var year =""
    var rated =""
    var plot =""
    var poster =""
    var messageClicked=0
    var movieInfo=ArrayList<ArrayList<String>>()
    lateinit var editText: EditText
    lateinit var getMovies: Button
    lateinit var movieFavorites: Button
    lateinit var progress: ProgressBar
    //lateinit var parent: Context
    var adapter:MyAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress=findViewById(R.id.progress)
        progress.visibility= View.INVISIBLE
        editText=findViewById(R.id.my_search)
        var listview=findViewById<ListView>(R.id.myList)
//        set on click listener for list tiems
        listview.setOnItemClickListener { parent, view, position, id ->
            var detailActivity= Intent(this,MovieFrame::class.java)
            var passdata=Bundle()
            passdata.putString("title", movieInfo[0][0])
            passdata.putString("duration", movieInfo[0][1])
            passdata.putString("actors", movieInfo[0][2])
            passdata.putString("year", movieInfo[0][3])
            passdata.putString("rating", movieInfo[0][4])
            passdata.putString("description", movieInfo[0][5])
            passdata.putString("posterUrl", movieInfo[0][6])
            detailActivity.putExtras(passdata)
            startActivity(detailActivity)
        }
        adapter=MyAdapter(this)

        listview?.setAdapter(adapter)

        getMovies=findViewById<Button>(R.id.doneButton)
        movieFavorites=findViewById<Button>(R.id.movieFavorites)

        movieFavorites.setOnClickListener{
            var detailActivity= Intent(this,MovieFavorites::class.java)

            startActivityForResult(detailActivity, 35)
        }

        getMovies.setOnClickListener({
            progress=findViewById(R.id.progress)
            progress.visibility= View.VISIBLE
            movieSearched= Uri.encode( editText.getText().toString())
            Log.i(movieSearched, "this is the movie");
            var myQuery =ForecastQuery()
            myQuery.execute()

        })


    }
    inner class ForecastQuery : AsyncTask<String, Integer, String>() {

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL("http://www.omdbapi.com/?apikey=6c9862c2&r=xml&type=movie&t=$movieSearched")
                Log.i(url.toString(), "this is the movie title")
                var urlConnection = url.openConnection() as HttpURLConnection
                val stream = urlConnection.getInputStream()
                val factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(false)
                val xpp = factory.newPullParser()
                xpp.setInput(stream, "UTF-8")
                while (xpp.eventType != XmlPullParser.END_DOCUMENT) {

                    when (xpp.eventType) {

                        XmlPullParser.START_TAG -> {

                            if(xpp.name.equals("movie")) {

                                movieTitle = xpp.getAttributeValue(null, "title")
                                runtime=xpp.getAttributeValue(null, "runtime")
                                actors=xpp.getAttributeValue(null, "actors")
                                year=xpp.getAttributeValue(null, "year")
                                rated=xpp.getAttributeValue(null, "rated")
                                plot=xpp.getAttributeValue(null, "plot")
                                poster=xpp.getAttributeValue(null, "poster")

                            } }

                    }

                    xpp.next()
                }

            } catch (e: Exception) {
                Log.i("Error", "Error")

            }
            return "Done"
        }
        override fun onPostExecute(result: String?) {
            progress.visibility= View.INVISIBLE
            var newArr=ArrayList<String>()
            newArr.add(movieTitle)
            newArr.add(runtime)
            newArr.add(actors)
            newArr.add(year)
            newArr.add(rated)
            newArr.add(plot)
            newArr.add(poster)
            movieInfo=ArrayList<ArrayList<String>>()
            movieInfo.add(newArr)
            Log.i("Error", "Hello")
            adapter?.notifyDataSetChanged()
        }

    }

    inner class MyAdapter(ctx: Context) : ArrayAdapter<ArrayList<String>>(ctx,0){
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
            var rowSelected=createList.inflate(R.layout.movie_display, null)
            var displayMTitle=rowSelected.findViewById<TextView>(R.id.movie_title)
            var displayMPlot=rowSelected.findViewById<TextView>(R.id.movie_plot)

            var pos=movieInfo.get(position)


            displayMTitle.setText(pos[0])
            displayMPlot.setText(pos[1])
            return rowSelected
        }
    }



}
