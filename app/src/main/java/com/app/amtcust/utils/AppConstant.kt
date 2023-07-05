package com.app.amtcust.utils

object AppConstant {

    var TOKEN : String = ""

    val BASE_URL_APP = "http://38.17.55.183/amtapp2022/api/" //  AI005 development purpose
    val BASE_URL_WEB = "http://38.17.55.183/amtapi2022/api/" //  AI005 development purpose

    /*val BASE_URL_APP = "http://amtapp.ajaymoditravels.com/api/" //  AI005 live purpose
    val BASE_URL_WEB = "https://amtcrm.ajaymoditravels.com/api/" //  AI005 live purpose*/

    val DEVICETYPE = 1

    const val TOAST_LONG = 1
    const val TOAST_SHORT = 0
    val IS_API_CALL = "IS_API_CALL"
    const val BLUR_RADIUS = 35
    internal val PREF_NAME = "amt_pref"

    //Date format
    val YYYY_MM_dd_Slash: String = "yyyy/MM/dd"
    val yyyy_MM_dd_Dash: String = "yyyy-MM-dd"
    val dd_MM_yyyy_Slash: String = "dd/MM/yyyy"
    const val DD_MMM_YYYY_FORMAT = "MMMM, dd yyyy"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val HH_MM_AA_FORMAT = "hh:mm aa" //04:30 PM
    const val HH_MM_FORMAT = "HH:mm" //16:30
    const val day_d_MM_YYYY_HH_mm_ss_z_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z"
    const val day_d_MM = "EEE, d MMM"
    const val day_d_MM_YYYY = "EEE, d MMM yyyy"
    //Date comparision
    const val BEFORE = "BEFORE"
    const val AFTER = "AFTER"
    const val EQUAL = "EQUAL"

    val SpecialityFilters = "SpecialityFilters"
    val DurationFilters = "DurationFilters"
    val BudgetFilters = "BudgetFilters"
}