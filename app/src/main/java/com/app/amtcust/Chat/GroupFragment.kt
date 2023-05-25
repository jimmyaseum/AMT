package com.app.amtcust.Chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.dialog.DialogCityAdapter
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.CityModel
import com.app.amtcust.utils.PrefConstants
import com.app.amtcust.utils.SharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_company_details.*
import kotlinx.android.synthetic.main.fragment_cb_group.*
import kotlinx.android.synthetic.main.fragment_cb_group.view.*

class GroupFragment: BaseFragment() {
    private var views: View? = null

    var sharedPreference: SharedPreference? = null

    private var UsersRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private val userList: ArrayList<String> = ArrayList()
    private var current_User_id: String? = null
    lateinit var adapter: GroupListAdapter

    var current_Group_Name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_cb_group, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        views!!.rvGroupList.layoutManager = layoutManager

        sharedPreference = SharedPreference(activity!!)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        showProgress()

        adapter = GroupListAdapter(activity!!, userList)
        views!!.rvGroupList.adapter = adapter

        val sharedPreference = SharedPreference(activity!!)
        val OnGoingTourID = sharedPreference?.getPreferenceInt("OnGoingTour")
        val OnGoingTourName = sharedPreference?.getPreferenceString("OnGoingTourName")
        val OnGoingTourCode = sharedPreference?.getPreferenceString("OnGoingTourCode")

        current_Group_Name = OnGoingTourName +" - "+ OnGoingTourCode

        UsersRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_GROUP)
        mAuth = FirebaseAuth.getInstance()
        current_User_id = mAuth!!.getCurrentUser()!!.uid

        LoadFCMGroup()

        views!!.edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: ArrayList<String> = ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in userList!!) {
                        if (model!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = GroupListAdapter(requireActivity(), arrItemsFinal1)
                    views!!.rvGroupList.adapter = itemAdapter


                } else {
                    val itemAdapter = GroupListAdapter(requireActivity(), userList!!)
                    views!!.rvGroupList.adapter = itemAdapter

                }
            }
        })
    }

    private fun LoadFCMGroup() {
        UsersRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snap in dataSnapshot.children) {

                    UsersRef!!.child(snap.key!!).addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                            for (snap1 in dataSnapshot.children) {
                                if(current_User_id.equals(snap1.key)) {
                                    if(current_Group_Name.equals(snap.key)) {
                                        userList.add(snap.key!!)
                                    }
                                    if(userList.size > 0) {

                                        adapter.notifyDataSetChanged()
                                        hideProgress()
                                    } else {
                                        hideProgress()
                                    }
                                } else {
                                    hideProgress()
                                }
                            }
                        }
                        override fun onCancelled(@NonNull databaseError: DatabaseError) {
                            //enter code here
                            hideProgress()
                        }
                    })
                }
            }
            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                //enter code here
                hideProgress()
            }
        })
    }
}