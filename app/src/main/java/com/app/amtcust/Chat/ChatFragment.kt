package com.app.amtcust.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.model.response.EmployeeDataResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cb_chat.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : BaseFragment() {

    private var views: View? = null

    private var UsersRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private val userList: ArrayList<ChatListModel> = ArrayList()
    private var current_User_id: String? = null
    lateinit var adapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        views = inflater.inflate(R.layout.fragment_cb_chat, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        views!!.rvUserList.layoutManager = layoutManager

        showProgress()

        adapter = ChatListAdapter(activity!!, userList)
        views!!.rvUserList.adapter = adapter

        UsersRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_EMPLOYEE)
        mAuth = FirebaseAuth.getInstance()
        current_User_id = mAuth!!.getCurrentUser()!!.uid

        val sharedPreference = SharedPreference(activity!!)
        val OnGoingTourID = sharedPreference?.getPreferenceInt("OnGoingTour")
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        CallCustomerwiseEmployee(userId,OnGoingTourID)

    }

    private fun CallCustomerwiseEmployee(userId: Int, tourId: Int) {
        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", userId)
        jsonObject.put("TourID", tourId)
        val call = ApiUtils.apiInterface.getCustomersEmployeeData(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<EmployeeDataResponse> {
            override fun onResponse(call: Call<EmployeeDataResponse>, response: Response<EmployeeDataResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        var arra = ArrayList<Int>()
                        val arrayList = response.body()?.Data!!
                            for (i in 0 until arrayList.size) {
                                arra.add(arrayList[i].ID)
                            }
                        if(arra.size > 0) {
                            LoadFCMData(arra)
                        }

                    }
                }
            }
            override fun onFailure(call: Call<EmployeeDataResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun LoadFCMData(arrray : ArrayList<Int>) {

        UsersRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userList.clear()
                    for (dn in snapshot.children) {
                        for(postsnapchat in snapshot.children) {
                            val message = postsnapchat.getValue(ChatListModel::class.java)
                            if (!userList.contains(message)) {
                                if(!message!!.usertype.equals("customer")) {
                                    if(message.id in arrray) {
                                        userList.add(message!!)
                                        adapter.notifyDataSetChanged()
                                        hideProgress()
                                    } else {
                                        hideProgress()
                                    }
                                } else {
                                    hideProgress()
                                }
                            } else {
                                hideProgress()
                            }
                        }
                    }
                } else {
                    hideProgress()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                hideProgress()
            }
        })
    }

}