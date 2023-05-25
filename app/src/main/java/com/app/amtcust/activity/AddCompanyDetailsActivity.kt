package com.app.amtcust.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.dialog.DialogCityAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.*
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_DOCS
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_MEDIA
import kotlinx.android.synthetic.main.activity_add_company_details.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class AddCompanyDetailsActivity : BaseActivity(),  View.OnClickListener, EasyPermissions.PermissionCallbacks {

    var sharedPreference: SharedPreference? = null

    var arrayListCity: ArrayList<CityModel>? = null
    var CityId : Int = 0
    var CityName : String = ""
    var StateId : Int = 0
    var StateName : String = ""
    var CountryId : Int = 0
    var CountryName : String = ""

    var gstPath: Uri? = null
    var panPath: Uri? = null

    var ImagePaths = java.util.ArrayList<Uri>()
    var docPaths = java.util.ArrayList<Uri>()

    var gst_path : String = ""
    var pan_path : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_company_details)
        initializeView()
    }

    override fun initializeView() {
        InitiaListner()
    }

    private fun InitiaListner() {
        imgBack.setOnClickListener(this)
        txtSave.setOnClickListener(this)
        edtCity.setOnClickListener(this)
        LLChooseGST.setOnClickListener(this)
        LLChoosePAN.setOnClickListener(this)

        // Call Find By Id and Set Data
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        callCustomerDetailApi(userId)

        ImgGSTCopy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(gst_path))
            startActivity(browserIntent)
        }
        txtGSTCopy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(gst_path))
            startActivity(browserIntent)
        }

        ImgPANCopy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pan_path))
            startActivity(browserIntent)
        }
        txtPANCopy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pan_path))
            startActivity(browserIntent)
        }


    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                preventTwoClick(v)
                finish()
            }
            R.id.txtSave -> {
                preventTwoClick(v)
                hideKeyboard(applicationContext, v)
                val flag = isValidate()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallEditCompanyAPI()
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }
            R.id.edtCity -> {
                preventTwoClick(v)
                if(!arrayListCity.isNullOrEmpty()) {
                    selectCityDialog()
                } else {
                    GetAllCityAPI()
                }
            }
            R.id.LLChooseGST -> {
                /*preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showBottomSheetDialogGST()
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }*/
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogGST()
                    }
                    else {
                        EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.msg_permission_camera),
                            900,
                            Manifest.permission.CAMERA
                        )
                    }
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }

            }
            R.id.LLChoosePAN -> {
               /* preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showBottomSheetDialogPAN()
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }*/
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogPAN()
                    }
                    else {
                        EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.msg_permission_camera),
                            900,
                            Manifest.permission.CAMERA
                        )
                    }
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }

            }
        }
    }

    private fun showBottomSheetDialogPAN() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
//            documentpickerPAN()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 103)
            bottomSheetDialog.dismiss()
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {

            photopickerPAN()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showBottomSheetDialogGST() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
//            documentpickerGST()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 104)
            bottomSheetDialog.dismiss()
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {

            photopickerGST()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun documentpickerPAN() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(docPaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickFile(this, 22);
    }

    private fun photopickerPAN() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 11);
    }

    private fun documentpickerGST() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(docPaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickFile(this, 222);
    }

    private fun photopickerGST() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 111);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            11 -> if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(KEY_SELECTED_MEDIA)!!)

                panPath = ImagePaths[0]

                ImgPANCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                txtPANCopy.gone()
                ImgPANCopy.visible()
            }
            22 -> if (resultCode == RESULT_OK && data != null) {
                docPaths = ArrayList()
                docPaths.addAll(data.getParcelableArrayListExtra(KEY_SELECTED_DOCS)!!)

                panPath = docPaths[0]

                val pathname = getFileName(docPaths[0])
                txtPANCopy.text = pathname

                txtPANCopy.visible()
                ImgPANCopy.gone()
            }
            111 -> if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(KEY_SELECTED_MEDIA)!!)

                gstPath = ImagePaths[0]

                ImgGSTCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                txtGSTCopy.gone()
                ImgGSTCopy.visible()
            }
            222 -> if (resultCode == RESULT_OK && data != null) {
                docPaths = ArrayList()
                docPaths.addAll(data.getParcelableArrayListExtra(KEY_SELECTED_DOCS)!!)

                gstPath = docPaths[0]

                val pathname = getFileName(docPaths[0])
                txtGSTCopy.text = pathname

                txtGSTCopy.visible()
                ImgGSTCopy.gone()
            }

            103 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                panPath = Uri.fromFile(fileFromContentUri3("passport", applicationContext, sUri))

                val pathname = sPath
                txtPANCopy.text = pathname

                txtPANCopy.visible()
                ImgPANCopy.gone()
            }
            104 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                gstPath = Uri.fromFile(fileFromContentUri4("passport", applicationContext, sUri))

                val pathname = sPath

                txtGSTCopy.text = pathname

                txtGSTCopy.visible()
                ImgGSTCopy.gone()
            }
        }
    }

    fun fileFromContentUri3(name  :String, context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file_" + name + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    fun fileFromContentUri4(name  :String, context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file_" + name + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    private fun CallEditCompanyAPI() {

        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        var partsgstList: MultipartBody.Part
        if (gstPath != null) {
            if(gstPath.toString().contains(".pdf")) {
                partsgstList = CommonUtil.prepareFilePart(this, "*/*", "CompanyGSTCopy", gstPath!!)
            } else {
                partsgstList = CommonUtil.prepareFilePart(this, "*/*", "CompanyGSTCopy", gstPath!!)
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsgstList = MultipartBody.Part.createFormData("CompanyGSTCopy", "", attachmentEmpty)
        }

        var partspanList: MultipartBody.Part
        if (panPath != null) {
            if(panPath.toString().contains(".pdf")) {
                partspanList = CommonUtil.prepareFilePart(this, "application/*", "CompanyPanCopy", panPath!!)
            } else {
                partspanList = CommonUtil.prepareFilePart(this, "image/*", "CompanyPanCopy", panPath!!)
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partspanList = MultipartBody.Part.createFormData("CompanyPanCopy", "", attachmentEmpty)
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var call = ApiUtils.apiInterface.CustomerCompanyUpdate(
            CreatedBy, edtCompanyName.text.toString().trim(), edtCompanyAddress.text.toString().trim(),
            edtCompanyMobileNo.text.toString().trim(), edtCompanyEmail.text.toString().trim(), edtGSTNo.text.toString().trim(),
            edtPANNo.text.toString().trim(), edtPincode.text.toString().trim(), CityId, StateId, CountryId,
            true, CreatedBy,
            imagegst = partsgstList,
            imagepan = partspanList
        )

        call.enqueue(object : Callback<CommonResponse> {
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
            }
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                hideProgress()
                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)

                        val intent = Intent()
                        intent.putExtra(AppConstant.IS_API_CALL, true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    } else {
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
        })
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun isValidate(): Boolean {
        var check = true

        if (edtCompanyName.text.isEmpty()) {
            edtCompanyName.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtCompanyMobileNo.text.isEmpty()) {
            edtCompanyMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtCompanyEmail.text.isEmpty()) {
            edtCompanyEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtCompanyMobileNo.text!!.length < 10) {
            edtCompanyMobileNo.error = getString(R.string.error_valid_mobile_number)
            edtCompanyMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (!edtCompanyEmail.text!!.toString().isValidEmail()) {
            edtCompanyEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            edtCompanyEmail.error = getString(R.string.error_valid_email)
            check = false
        }
        if (edtPANNo.text.isEmpty()) {
            edtPANNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
//        else {
//            if (!isValidPanCardNo(edtPANNo.text!!.toString())) {
//                edtPANNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
//                edtPANNo.error = getString(R.string.error_valid_panno)
//                check = false
//            } else {
//                if (!edtGSTNo.text.isEmpty()) {
//                    if (!isValidGSTNo(edtGSTNo.text!!.toString())) {
//                        edtGSTNo.error = getString(R.string.error_valid_gstno)
//                        edtGSTNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
//                        check = false
//                    }
//                }
//            }
//        }

        return check
    }

    private fun callCustomerDetailApi(userId: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getDetailByCustomers(userId)

        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.code() == 200) {
                    var arrayList: CustomerListModel? = null
                    if (response.body()?.Status == 200) {
                        arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: CustomerListModel) {

        CityId = arrayList.CompanyCityID
        CityName = arrayList.CompanyCityName
        StateId = arrayList.CompanyStateID
        StateName = arrayList.CompanyStateName
        CountryId = arrayList.CompanyCountryID
        CountryName = arrayList.CompanyCountryName

        edtCompanyName.setText(arrayList.CompanyName)
        edtCompanyEmail.setText(arrayList.CompanyEmailID)
        edtCompanyMobileNo.setText(arrayList.CompanyMobileNo)
        edtCompanyAddress.setText(arrayList.CompanyAddress)
        edtGSTNo.setText(arrayList.CompanyGSTNo)
        edtPANNo.setText(arrayList.CompanyPanNo)
        edtCity.setText(CityName)
        edtState.setText(StateName)
        edtCountry.setText(CountryName)
        edtPincode.setText(arrayList.CompanyPincode)

        if(arrayList.CompanyGSTCopy != ""  && arrayList.CompanyGSTCopy != null) {
            if(arrayList.CompanyGSTCopy.contains(".pdf")) {
                txtGSTCopy.setText(arrayList.CompanyGSTCopy.substringAfterLast("/"))
                txtGSTCopy.visible()
                ImgGSTCopy.gone()

                gst_path = arrayList.CompanyGSTCopy
            } else {
                ImgGSTCopy.loadUrlRoundedCorner(
                    arrayList.CompanyGSTCopy,
                    R.drawable.ic_profile,
                    1
                )

                ImgGSTCopy.visible()
                txtGSTCopy.gone()

                gst_path = arrayList.CompanyGSTCopy
            }
        }

        if(arrayList.CompanyPanCopy != "" && arrayList.CompanyPanCopy != null) {
            if(arrayList.CompanyPanCopy.contains(".pdf")) {
                txtPANCopy.setText(arrayList.CompanyPanCopy.substringAfterLast("/"))
                txtPANCopy.visible()
                ImgPANCopy.gone()

                pan_path = arrayList.CompanyPanCopy
            } else {
                ImgPANCopy.loadUrlRoundedCorner(
                    arrayList.CompanyPanCopy,
                    R.drawable.ic_profile,
                    1
                )

                ImgPANCopy.visible()
                txtPANCopy.gone()

                pan_path = arrayList.CompanyPanCopy
            }
        }


    }

    //Permission Result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //EasyPermissions.PermissionCallbacks
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    //EasyPermissions.PermissionCallbacks
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun GetAllCityAPI() {

        showProgress()
        val call = ApiUtils.apiInterface.getAllCity()
        call.enqueue(object : Callback<CityResponse> {
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        arrayListCity = response.body()?.data!!
                        if(arrayListCity!!.size > 0) {
                            selectCityDialog()
                        } else {
                            toast("No Value Available.", AppConstant.TOAST_SHORT)
                        }

                    } else {
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                        hideProgress()
                    }
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    /** AI005
     * This method is to open City dialog
     */
    private fun selectCityDialog() {
        var dialogSelectCity = Dialog(this)
        dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectCity.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCity.window!!.attributes)

        dialogSelectCity.window!!.attributes = lp
        dialogSelectCity.setCancelable(true)
        dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCity.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectCity.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogCityAdapter(this, arrayListCity!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CityId = arrayListCity!![pos].CityID
                CityName = arrayListCity!![pos].CityName
                StateId = arrayListCity!![pos].StateID
                StateName = arrayListCity!![pos].StateName
                CountryId = arrayListCity!![pos].CountryID
                CountryName = arrayListCity!![pos].CountryName

                edtCity.setText(CityName)
                edtState.setText(StateName)
                edtCountry.setText(CountryName)
                dialogSelectCity!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<CityModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListCity!!) {
                        if (model.CityName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogCityAdapter(this@AddCompanyDetailsActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrItemsFinal1!![pos].CityID
                            CityName = arrItemsFinal1!![pos].CityName
                            StateId = arrItemsFinal1!![pos].StateID
                            StateName = arrItemsFinal1!![pos].StateName
                            CountryId = arrItemsFinal1!![pos].CountryID
                            CountryName = arrItemsFinal1!![pos].CountryName

                            edtCity.setText(CityName)
                            edtState.setText(StateName)
                            edtCountry.setText(CountryName)

                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCityAdapter(this@AddCompanyDetailsActivity, arrayListCity!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrayListCity!![pos].CityID
                            CityName = arrayListCity!![pos].CityName
                            StateId = arrayListCity!![pos].StateID
                            StateName = arrayListCity!![pos].StateName
                            CountryId = arrayListCity!![pos].CountryID
                            CountryName = arrayListCity!![pos].CountryName

                            edtCity.setText(CityName)
                            edtState.setText(StateName)
                            edtCountry.setText(CountryName)

                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectCity!!.show()
    }
}