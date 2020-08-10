package com.salman.coronacloneapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_countries.*
import org.json.JSONArray
import org.json.JSONException

class CountriesActivity : AppCompatActivity() {

    companion object {
        var countryList: MutableList<Country> = ArrayList()
    }

    lateinit var countryListAdapter: CountryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)



        supportActionBar?.title = "Countries"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // hide the keyboard everytime the activity starts
        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fetchData()

        list_item.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, CountryDetailsActivity::class.java)
            intent.putExtra(CountryDetailsActivity.COUNTRY_POSITION, position)
            startActivity(intent)
        }

        edit_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                countryListAdapter.filter.filter(s)
                countryListAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun fetchData() {
        val url = "https://corona.lmao.ninja/v2/countries/"

        loader.start()

        val request = StringRequest(Request.Method.GET, url, Response.Listener {

            try {

               val jsonArray = JSONArray(it.toString());

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val countryName = jsonObject.getString("country")
                    val cases = jsonObject.getString("cases")
                    val todayCases = jsonObject.getString("todayCases")
                    val deaths = jsonObject.getString("deaths")
                    val todayDeaths = jsonObject.getString("todayDeaths")
                    val recovered = jsonObject.getString("recovered")
                    val active = jsonObject.getString("active")
                    val critical = jsonObject.getString("critical")
                    val countryInfo = jsonObject.getJSONObject("countryInfo")
                    val flagUrl = countryInfo.getString("flag")

                    val country = Country(flagUrl,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical)
                    countryList.add(country)


                }
                countryListAdapter = CountryListAdapter(this, countryList)
                list_item.adapter = countryListAdapter
                loader.stop()
                loader.visibility = View.GONE

            } catch (e: JSONException) {
                e.printStackTrace()
                loader.stop()
                loader.visibility = View.GONE

            }


        }, Response.ErrorListener {
            loader.stop()
            loader.visibility = View.GONE
            Toast.makeText(this@CountriesActivity, it.message, Toast.LENGTH_SHORT).show()
        })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}