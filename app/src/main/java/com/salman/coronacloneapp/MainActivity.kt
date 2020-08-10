package com.salman.coronacloneapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_country_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tvActive
import kotlinx.android.synthetic.main.activity_main.tvCases
import kotlinx.android.synthetic.main.activity_main.tvCritical
import kotlinx.android.synthetic.main.activity_main.tvRecovered
import kotlinx.android.synthetic.main.activity_main.tvTodayCases
import kotlinx.android.synthetic.main.activity_main.tvTodayDeaths
import kotlinx.android.synthetic.main.activity_main.tvTotalDeaths
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var networkAvailable: Boolean = false
    lateinit var mDialog: SimpleArcDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findViewById<TextView>(R.id.tvCases)

        mDialog = SimpleArcDialog(this)
        mDialog.setConfiguration(ArcConfiguration(this))
        mDialog.setTitle("Fetching data..")


        if (isNetWorkAvailable()) {
            fetchData()
        } else {
            //loader.stop()
            //loader.visibility = View.GONE
            val dialog = CustomDialog()
            dialog.show(supportFragmentManager, "Network checking")
        }

        btn_track.setOnClickListener {
            val intent = Intent(this, CountriesActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            //finish()
        }
    }

      fun fetchData() {
          val url = "https://corona.lmao.ninja/v2/all"

          //loader.start()
          mDialog.show()

          val request = StringRequest(Request.Method.GET, url, Response.Listener {

              try {
                  val jsonObject = JSONObject(it.toString())
                  tvCases.text = jsonObject.getString("cases")
                  tvRecovered.text = jsonObject.getString("recovered")
                  tvCritical.text = jsonObject.getString("critical")
                  tvActive.text = jsonObject.getString("active")
                  tvTodayCases.text = jsonObject.getString("todayCases")
                  tvTotalDeaths.text = jsonObject.getString("deaths")
                  tvTodayDeaths.text = jsonObject.getString("todayDeaths")
                  tvAffectedCountries.text = jsonObject.getString("affectedCountries")


                  showPieChart()
                  showBarChart()

                  mDialog.hide()

                  //loader.stop()
                  //loader.visibility = View.GONE
                  //scrollStats.visibility = View.VISIBLE


              } catch (e: JSONException) {
                  e.printStackTrace()
                  mDialog.hide()
                  //loader.stop()
                  //loader.visibility = View.GONE
                  //scrollStats.visibility = View.VISIBLE
              }


          }, Response.ErrorListener {
              mDialog.hide()
              //loader.stop()
              //loader.visibility = View.GONE
              //scrollStats.visibility = View.VISIBLE
              Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
          })

          val requestQueue = Volley.newRequestQueue(this)
          requestQueue.add(request)
    }


    fun showPieChart() {
        piechart.addPieSlice(PieModel("Cases", tvCases.text.toString().toFloat(), Color.parseColor("#FFA726")))
        piechart.addPieSlice(PieModel("Recovered", tvRecovered.text.toString().toFloat(), Color.parseColor("#66BB6A")))
        piechart.addPieSlice(PieModel("Deaths", tvTotalDeaths.text.toString().toFloat(), Color.parseColor("#EF5350")))
        piechart.addPieSlice(PieModel("Active", tvActive.text.toString().toFloat(), Color.parseColor("#29B6F6")))

        piechart.startAnimation()

    }

    fun showBarChart() {
        barchart.addBar(BarModel( tvCases.text.toString().toFloat(), Color.parseColor("#FFA726")))
        barchart.addBar(BarModel( tvRecovered.text.toString().toFloat(), Color.parseColor("#66BB6A")))
        barchart.addBar(BarModel( tvTotalDeaths.text.toString().toFloat(), Color.parseColor("#EF5350")))
        barchart.addBar(BarModel( tvActive.text.toString().toFloat(), Color.parseColor("#29B6F6")))

        // "Cases", "Recovered", "Deaths", "Active",
        barchart.isShowValues = false
        barchart.startAnimation()

    }


    fun isNetWorkAvailable() : Boolean {
        val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Declare as a nullable type
        val networkInfo: NetworkInfo? = manager.activeNetworkInfo
        if (networkInfo != null) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) networkAvailable = true
            else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) networkAvailable = true
        } else {
            networkAvailable = false
        }
        return networkAvailable
    }
}