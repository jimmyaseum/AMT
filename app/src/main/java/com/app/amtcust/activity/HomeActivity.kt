package com.app.amtcust.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.amtcust.Chat.ChatConstant
import com.app.amtcust.Chat.ChatListModel
import com.app.amtcust.R
import com.app.amtcust.fragment.ExploreFragment
import com.app.amtcust.fragment.IndiaTourFragment
import com.app.amtcust.fragment.ProfileFragment
import com.app.amtcust.fragment.WorldTourFragment
import com.app.amtcust.model.GroupListModel
import com.app.amtcust.model.response.*
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.nav_header_home.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    var sharedPreference: SharedPreference? = null

    private var currentUserID: String? = null
    private var mAuth: FirebaseAuth? = null
    var RootRef: DatabaseReference? = null
    var currentUser: FirebaseUser? = null
    private var UsersRef: DatabaseReference? = null
    val arraylistGroupMember: ArrayList<GroupListModel> = ArrayList()
    private val userList: ArrayList<ChatListModel> = ArrayList()


    private var CustRef: DatabaseReference? = null
    private var EmpRef: DatabaseReference? = null

    var Uid : String? = ""
    var device_token : String? = ""
    var id: Int? = 0
    var image: String? = ""
    var mobile: String? = ""
    var name : String? = ""
    var usertype : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        UsersRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_USER)

        CustRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_CUSTOMER)
        EmpRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_EMPLOYEE)

        if (currentUser != null) {
            currentUserID = mAuth!!.getCurrentUser()!!.getUid()
            RootRef = FirebaseDatabase.getInstance().reference
            GetCurrentUserInfo()
        }

        getSetting()
        initializeView()
    }

    override fun initializeView() {

        setToolBar()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar1,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        initBottomNavigation()
        bottomNavigationListener()

        val headerLayout = navigationView.getHeaderView(0)

        val LLEditProfile: LinearLayout = headerLayout.findViewById(R.id.LLEditProfile)
        LLEditProfile.setOnClickListener(this)

        // Call Find By Id and Set Data
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        callCustomerDetailApi(userId)

        showProgress()

        if(currentUser != null) {
            EmpRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userList.clear()
                        for (dn in snapshot.children) {
                            for(postsnapchat in snapshot.children) {
                                val message = postsnapchat.getValue(ChatListModel::class.java)
                                if (!userList.contains(message)) {
                                    userList.add(message!!)
                                }
                            }
                        }
                        if(userList.size > 0) {
                            hideProgress()
                            callOngoingTour(userId)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        //Default Home Fragment called first time
          showHomeFragment()
/*
        var i = 1
        if(currentUser != null) {
            UsersRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("Total","==>"+snapshot.childrenCount)
                    if (snapshot.exists()) {
                        for (dn in snapshot.children) {
                            Log.e("I","==>"+i)
                            i =  i + 1
                            val message = dn.getValue(ChatListModel::class.java)
                            if (!message!!.usertype.equals("customer")) {
                                val currentUserID = message.Uid.toString()
                                EmpRef!!.child(currentUserID).child("Uid").setValue(message.Uid)
                                EmpRef!!.child(currentUserID).child("device_token").setValue(message.device_token)
                                EmpRef!!.child(currentUserID).child("id").setValue(message.id)
                                EmpRef!!.child(currentUserID).child("usertype").setValue(message.usertype)
                                EmpRef!!.child(currentUserID).child("name").setValue(message.name)
                                EmpRef!!.child(currentUserID).child("mobile").setValue(message.mobile)
                                EmpRef!!.child(currentUserID).child("image").setValue(message.image)

                            } else {
                                val currentUserID = message.Uid.toString()
                                CustRef!!.child(currentUserID).child("Uid").setValue(message.Uid)
                                CustRef!!.child(currentUserID).child("device_token").setValue(message.device_token)
                                CustRef!!.child(currentUserID).child("id").setValue(message.id)
                                CustRef!!.child(currentUserID).child("usertype").setValue(message.usertype)
                                CustRef!!.child(currentUserID).child("name").setValue(message.name)
                                CustRef!!.child(currentUserID).child("mobile").setValue(message.mobile)
                                CustRef!!.child(currentUserID).child("image").setValue(message.image)
                            }
                        }
//                        if(userList.size > 0) {
//                            hideProgress()
//                            callOngoingTour(userId)
//                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }*/
    }

    private fun setToolBar() {
        setSupportActionBar(toolbar1)
        toolbar1.tbTvTitle!!.text = "Dashboard"
        // AI005 remove comment for future update
        toolbar1?.tbImgLeft!!.setImageResource(R.drawable.ic_menu)
        toolbar1?.tbImgLeft!!.setOnClickListener(this)
        toolbar1?.tbImgRight!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.tbImgLeft -> {
//                  openCloseDrawerLayout()
            }
            R.id.tbImgRight -> {
            }
            R.id.LLEditProfile -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
        }
    }

    private fun showHomeFragment() {

        toolbar1.tbTvTitle!!.text = "Explore"
        replaceFragment(ExploreFragment(), R.id.container, ExploreFragment::class.java.simpleName)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.menu.findItem(R.id.nav_Couple_Tours).isChecked = true
        showHideToolBarRightImage(false, 0)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        showHideToolBarRightImage(false, R.drawable.ic_filter_24dp)

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_Couple_Tours -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_couple_tour)
            }
            R.id.nav_Diwali_Vacation -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_diwali_vacation)
            }
            R.id.nav_Customized_Holidays -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_customized_holidays)
            }
            R.id.nav_Weekend_Gateways -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_weekend_gateways)
            }
            R.id.nav_Contact -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_contact)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showHideToolBarRightImage(isShow: Boolean, resourceId: Int) {
        toolbar1.tbImgLeft.invisible()
        if (isShow) {
            toolbar1.tbImgRight.visible()
            tbImgRight.setImageResource(resourceId)
        } else {
            toolbar1.tbImgRight.invisible()
        }
    }

    fun initBottomNavigation() {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)
    }

    private fun bottomNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_Explore -> {
                    showHomeFragment()
                }
                R.id.nav_India -> {
                    toolbar1.tbTvTitle!!.text = "India Tours"
                    replaceFragment(IndiaTourFragment(), R.id.container, IndiaTourFragment::class.java.simpleName)
                }
                R.id.nav_World -> {
                    toolbar1.tbTvTitle!!.text = "World Tours"
                    replaceFragment(WorldTourFragment(), R.id.container, WorldTourFragment::class.java.simpleName)
                }
                R.id.nav_Profile -> {
                    toolbar1.tbTvTitle!!.text = "Profile"
                    replaceFragment(ProfileFragment(), R.id.container, ProfileFragment::class.java.simpleName)
                    showHideToolBarRightImage(false, 0)
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val isCallAPI = data!!.getBooleanExtra(AppConstant.IS_API_CALL, false)
                    if (isCallAPI) {
                        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
                        callCustomerDetailApi(userId)
                    }
                }

            }
        }
    }

    private fun callCustomerDetailApi(userId: Int) {

        val call = ApiUtils.apiInterface.getDetailByCustomers(userId)

        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {

                if (response.code() == 200) {
                    var arrayList: CustomerListModel? = null
                    if (response.body()?.Status == 200) {
                        arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                }
            }
            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: CustomerListModel) {

        if(arrayList.FirstName != null && arrayList.FirstName != "") {
            tvUserName.text = arrayList.FirstName + " " + arrayList.LastName
            if (arrayList.CustomerImage != "" && arrayList.CustomerImage != null) {
                profile_image.loadUrlRoundedCorner(
                    arrayList.CustomerImage,
                    R.drawable.ic_profile,
                    1
                )

                if(currentUserID != "") {
                    UpdateImageInFB(arrayList.CustomerImage)
                }
            }
        }
    }

    private fun UpdateImageInFB(image: String) {
        val profileMap = HashMap<String, String>()
        profileMap.put("uid",currentUserID!!)
        profileMap.put("image",image)

        RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID!!).updateChildren(profileMap as Map<String, String>)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this@HomeActivity, "Error : $message", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        callCustomerDetailApi(userId)
    }

    fun getSetting() {

        sharedPreference = SharedPreference(this)
        val call = ApiUtils.apiInterface.getSetting()
        call.enqueue(object : Callback<SettingResponse> {

            override fun onResponse(call: Call<SettingResponse>, response: Response<SettingResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        sharedPreference!!.setPreference(PrefConstants.PREF_ADMIN_CALL, arrayList.MobileNo)
                        sharedPreference!!.setPreference(PrefConstants.PREF_ADMIN_MAIL, arrayList.Email)
                    }

                }
            }

            override fun onFailure(call: Call<SettingResponse>, t: Throwable) {
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun openCloseDrawerLayout() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun callOngoingTour(userId: Int) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", userId)

        val call = ApiUtils.apiInterface.getCustomersOngoingTrip(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<OngoingTourDataResponse> {
            override fun onResponse(call: Call<OngoingTourDataResponse>, response: Response<OngoingTourDataResponse>) {
                if (response.code() == 200) {
                    var arrayList: OngoingTourDataModel? = null
                    if (response.body()?.Status == 200) {
                        arrayList = response.body()?.Data!!
                        val sharedPreference = SharedPreference(this@HomeActivity)
                        sharedPreference.setPreference("OnGoingTour", arrayList.TourID)
                        sharedPreference.setPreference("OnGoingTourName", arrayList.TourName)
                        sharedPreference.setPreference("OnGoingTourCode", arrayList.TourDateCode)

                        CallCustomerwiseEmployee(userId, arrayList.TourID, arrayList.TourName , arrayList.TourDateCode)

                    } else {
                        val sharedPreference = SharedPreference(this@HomeActivity)
                        sharedPreference.setPreference("OnGoingTour", 0)

//                        showHomeFragment()
                    }
                }
            }
            override fun onFailure(call: Call<OngoingTourDataResponse>, t: Throwable) {
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
                val sharedPreference = SharedPreference(this@HomeActivity)
                sharedPreference.setPreference("OnGoingTour", 0)
            }
        })
    }

    private fun CallCustomerwiseEmployee(userId: Int, tourId: Int, tourName: String, tourDateCode: String) {
        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", userId)
        jsonObject.put("TourID", tourId)
        val call = ApiUtils.apiInterface.getCustomersEmployeeData(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<EmployeeDataResponse> {
            override fun onResponse(call: Call<EmployeeDataResponse>, response: Response<EmployeeDataResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        if (currentUser != null) {
                            for (i in 0 until arrayList.size) {
                                for(j in 0 until userList.size) {
                                    if(arrayList[i].ID == userList[j].id) {
                                        arraylistGroupMember.add(GroupListModel(userList[j].Uid,
                                            userList[j].device_token,
                                            userList[j].id,
                                            userList[j].image,
                                            userList[j].mobile,
                                            userList[j].name,
                                            userList[j].usertype
                                        ))

                                    }
                                }
                            }
                            CreateNewGroup(userId, tourName, tourId, tourDateCode, arraylistGroupMember)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeDataResponse>, t: Throwable) {
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CreateNewGroup(userId: Int,
                               TourName: String,
                               TourID: Int,
                               TourDateCode: String,
                               employeeDataModel: ArrayList<GroupListModel>?) {

        val groupname = TourName + " - "+ TourDateCode

        RootRef!!.child(ChatConstant.F_GROUP).child(groupname).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in 0 until employeeDataModel!!.size) {
                        val memberObject = ArrayList<GroupListModel>()
                        memberObject.add(employeeDataModel[i])
                        RootRef?.child(ChatConstant.F_GROUP)?.child(groupname)?.child(employeeDataModel[i].Uid.toString())!!
                            .push()
                            .setValue(memberObject)

                    }
                } else {
                    val senderObject = ArrayList<GroupListModel>()
                    senderObject.add(GroupListModel(Uid, device_token, id, image, mobile, name, usertype))
                    RootRef?.child(ChatConstant.F_GROUP)?.child(groupname)?.child(currentUserID!!)?.push()
                        ?.setValue(senderObject)?.addOnSuccessListener {
                            for (i in 0 until employeeDataModel!!.size) {
                                val memberObject = ArrayList<GroupListModel>()
                                memberObject.add(employeeDataModel[i])
                                RootRef?.child(ChatConstant.F_GROUP)?.child(groupname)?.child(employeeDataModel[i].Uid.toString())!!
                                    .push()
                                    .setValue(memberObject)
                            }

                            val calForDate = Calendar.getInstance()
                            val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                            val currentDateTime = currentDateFormat.format(calForDate.time)
                            RootRef!!.child(ChatConstant.F_GROUP).child(groupname).child("ISLatest").setValue(currentDateTime)
                        }
                }
            }
            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun GetCurrentUserInfo() {
        RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        id = snapshot.child("id").value.toString().toInt()
                        device_token = snapshot.child("device_token").value.toString()
                        name = snapshot.child("name").value.toString()
                        Uid = snapshot.child("Uid").value.toString()
                        image = snapshot.child("image").value.toString()
                        mobile = snapshot.child("mobile").value.toString()
                        usertype = snapshot.child("usertype").value.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

}


