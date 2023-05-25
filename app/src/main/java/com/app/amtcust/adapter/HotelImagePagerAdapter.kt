package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.amtcust.R
import com.app.amtcust.model.response.HotelImages
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_welcome_pager.view.*

class HotelImagePagerAdapter(val context: Context, private val arrImages: ArrayList<HotelImages>) : PagerAdapter() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val itemView = inflater.inflate(R.layout.adapter_welcome_pager, container, false)!!

        if(arrImages[position].HotelImage != null && arrImages[position].HotelImage != "") {
            itemView.imgTutorial.loadUrlRoundedCorner2(
                arrImages[position].HotelImage,
                R.drawable.no_image,
                10
            )
        } else {
            itemView.imgTutorial.loadUrlRoundedCorner2(
                "",
                R.drawable.no_image_placeholder,
                10
            )
        }
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return arrImages.size
    }
}