package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.amtcust.R
import com.app.amtcust.model.response.TourImages
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_welcome_pager.view.*

class TourImagePagerAdapter(val context: Context,private val arrImages: ArrayList<TourImages>) : PagerAdapter() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val itemView = inflater.inflate(R.layout.adapter_welcome_pager, container, false)!!

        if(arrImages[position].TourImage != null && arrImages[position].TourImage != "") {
            itemView.imgTutorial.loadUrlRoundedCorner2(
                arrImages[position].TourImage,
                R.drawable.no_image,
                1
            )
        } else {
            itemView.imgTutorial.loadUrlRoundedCorner2(
                "",
                R.drawable.no_image_placeholder,
                10
            )
        }
//        itemView.imgTutorial.setImageURI(arrImages[position])
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