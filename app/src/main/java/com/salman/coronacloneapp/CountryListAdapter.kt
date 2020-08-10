package com.salman.coronacloneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class CountryListAdapter : ArrayAdapter<Country> {
    var mContext: Context
    var mCountryList: MutableList<Country>
    var mCountryFilterList: MutableList<Country>

    constructor (
        context: Context,
        countryList: MutableList<Country>
    ) : super(context, R.layout.country_list_item, countryList) {
        mContext = context
        mCountryList = countryList
        mCountryFilterList = mCountryList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val rowView: View?

        if (convertView == null) {
            rowView = LayoutInflater.from(parent.context).inflate(R.layout.country_list_item, parent, false)

            viewHolder = ViewHolder(rowView)
            rowView.tag = viewHolder

        } else {
            rowView = convertView
            viewHolder = rowView.tag as ViewHolder
        }

        viewHolder.countryName!!.text = mCountryFilterList[position].country
        Glide.with(mContext).load(mCountryFilterList[position].flag).into(viewHolder.countryFlag!!)

        return rowView!!
    }

    override fun getItem(position: Int): Country? {
        return mCountryFilterList.get(position)
    }

    override fun getCount(): Int {
        return mCountryFilterList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //val charSearch = constraint.toString()
                val filterResults = FilterResults()
                if (constraint == null || constraint.toString().length == 0 ) {
                    filterResults.count = mCountryList.size
                    filterResults.values = mCountryList
                } else {
                    var resultsModel : MutableList<Country> = ArrayList()
                    var searchStr = constraint.toString().toLowerCase()
                    for (countryItem : Country in mCountryList) {
                        if (countryItem.country.toLowerCase().contains(searchStr)) {
                            resultsModel.add(countryItem)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mCountryFilterList = results?.values as MutableList<Country>
                CountriesActivity.countryList = results?.values as MutableList<Country>
                notifyDataSetChanged()
            }

        }
    }
}

class ViewHolder(view: View?) {
    val countryName = view?.findViewById<TextView>(R.id.tvCountryName)
    val countryFlag = view?.findViewById<ImageView>(R.id.imageFlag)
}