package com.salman.coronacloneapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_country_details.*
import kotlinx.android.synthetic.main.activity_country_details.tvActive
import kotlinx.android.synthetic.main.activity_country_details.tvCases
import kotlinx.android.synthetic.main.activity_country_details.tvCritical
import kotlinx.android.synthetic.main.activity_country_details.tvRecovered
import kotlinx.android.synthetic.main.activity_country_details.tvTodayCases
import kotlinx.android.synthetic.main.activity_country_details.tvTodayDeaths
import kotlinx.android.synthetic.main.activity_country_details.tvTotalDeaths
import kotlinx.android.synthetic.main.activity_main.*
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel

class CountryDetailsActivity : AppCompatActivity() {

    companion object {
         const val COUNTRY_POSITION: String = "Position"
    }
    var positionCountry: Int = 0
    lateinit var actionBarName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)


        val mIntent = intent
        positionCountry = mIntent.getIntExtra(COUNTRY_POSITION,0)
        actionBarName = CountriesActivity.countryList.get(positionCountry).country

        supportActionBar?.title = "Details of $actionBarName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //show stats header for selected country
        stats_headerTV.text = actionBarName


        tvCountry.text = CountriesActivity.countryList.get(positionCountry).country
        tvCases.text = CountriesActivity.countryList.get(positionCountry).cases
        tvRecovered.text = CountriesActivity.countryList.get(positionCountry).recovered
        tvCritical.text = CountriesActivity.countryList.get(positionCountry).critical
        tvActive.text = CountriesActivity.countryList.get(positionCountry).active
        tvTodayCases.text = CountriesActivity.countryList.get(positionCountry).todayCases
        tvTotalDeaths.text = CountriesActivity.countryList.get(positionCountry).deaths
        tvTodayDeaths.text = CountriesActivity.countryList.get(positionCountry).todayDeaths

        showBarChart()
        showPieChart()
    }



    fun showPieChart() {
        country_details_piechart.addPieSlice(PieModel("Cases", tvCases.text.toString().toFloat(), Color.parseColor("#FFA726")))
        country_details_piechart.addPieSlice(PieModel("Recovered", tvRecovered.text.toString().toFloat(), Color.parseColor("#66BB6A")))
        country_details_piechart.addPieSlice(PieModel("Deaths", tvTotalDeaths.text.toString().toFloat(), Color.parseColor("#EF5350")))
        country_details_piechart.addPieSlice(PieModel("Active", tvActive.text.toString().toFloat(), Color.parseColor("#29B6F6")))

        country_details_piechart.startAnimation()

    }

    fun showBarChart() {
        country_details_barchart.addBar(BarModel( tvCases.text.toString().toFloat(), Color.parseColor("#FFA726")))
        country_details_barchart.addBar(BarModel( tvRecovered.text.toString().toFloat(), Color.parseColor("#66BB6A")))
        country_details_barchart.addBar(BarModel( tvTotalDeaths.text.toString().toFloat(), Color.parseColor("#EF5350")))
        country_details_barchart.addBar(BarModel( tvActive.text.toString().toFloat(), Color.parseColor("#29B6F6")))

        // "Cases", "Recovered", "Deaths", "Active",
        country_details_barchart.startAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}