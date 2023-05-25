package com.app.amtcust.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
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
import com.app.amtcust.adapter.MyDocumentAdapter
import com.app.amtcust.adapter.dialog.DialogInitialAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.CommonResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_document.*
import kotlinx.android.synthetic.main.activity_add_document.imgBack
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AddDocumentActivity : BaseActivity(),  View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    var calDate = Calendar.getInstance()
    var DOB: String = ""

    private val arrDocTypeList: ArrayList<String> = ArrayList()
    var documentType : String = ""

    var ImageUri: Uri? = null

    var ImagePaths = ArrayList<Uri>()
    var docPaths = ArrayList<Uri>()

    var ImagePathas = ArrayList<String>()

    lateinit var adapter: MyDocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_document)
        initializeView()
    }

    override fun initializeView() {
        InitiaListner()
    }

    private fun InitiaListner() {

        // AI005 PassPort, Aadharcard, Visa, pancard, other
        arrDocTypeList.add("PassPort")
        arrDocTypeList.add("Aadharcard")
        arrDocTypeList.add("Visa")
        arrDocTypeList.add("PanCard")
        arrDocTypeList.add("Other")

        imgBack.setOnClickListener(this)
        edtDocumentType.setOnClickListener(this)

        txtVisaCopy.setOnClickListener(this)
        LLcardButtonUploadVisa.setOnClickListener(this)
        edtVisaValidTill.setOnClickListener(this)

        txtPanCopy.setOnClickListener(this)
        LLcardButtonUploadPan.setOnClickListener(this)
        edtPanValidTill.setOnClickListener(this)

        txtPassportCopy.setOnClickListener(this)
        LLcardButtonUploadPassport.setOnClickListener(this)
        edtPassportValidTill.setOnClickListener(this)

        txtAadharCopy.setOnClickListener(this)
        LLcardButtonUploadAadhar.setOnClickListener(this)
        edtAadharValidTill.setOnClickListener(this)

        txtOtherCopy.setOnClickListener(this)
        LLcardButtonUploadOther.setOnClickListener(this)
        edtOtherValidTill.setOnClickListener(this)

        // Call Find By Id and Set Data
        sharedPreference = SharedPreference(this)

        documentType = arrDocTypeList!![0]
        edtDocumentType.setText(documentType)
        if(documentType.equals("PassPort")) {
            LLPassport.visible()
            LLVisa.gone()
            LLAadhar.gone()
            LLPan.gone()
            LLOther.gone()
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.edtDocumentType -> {
                selectInitialDialog()
            }

            R.id.txtVisaCopy ->  {

                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogVisa()
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
            R.id.edtVisaValidTill -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDate.time)
                        edtVisaValidTill.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time))

                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.LLcardButtonUploadVisa -> {
                hideKeyboard(applicationContext, v)
                val flag = isValidateVisa()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallUploadDocumentAPI(edtVisaNo.text.toString().trim() , edtVisaValidTill.text.toString().trim())
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }

            R.id.txtPassportCopy ->  {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogPassport()
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
            R.id.edtPassportValidTill -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDate.time)
                        edtPassportValidTill.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time))
                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.LLcardButtonUploadPassport -> {
                hideKeyboard(applicationContext, v)
                val flag = isValidatePassport()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallUploadDocumentAPI(edtPassportNo.text.toString().trim() , edtPassportValidTill.text.toString().trim())
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }

            R.id.txtAadharCopy ->  {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogAadhar()
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
            R.id.edtAadharValidTill -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDate.time)
                        edtAadharValidTill.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time))
                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.LLcardButtonUploadAadhar -> {
                hideKeyboard(applicationContext, v)
                val flag = isValidateAadhar()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallUploadDocumentAPI(edtAadharNo.text.toString().trim() , edtAadharValidTill.text.toString().trim())
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }

            R.id.txtPanCopy ->  {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogPan()
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
            R.id.edtPanValidTill -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDate.time)
                        edtPanValidTill.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time))

                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.LLcardButtonUploadPan -> {
                hideKeyboard(applicationContext, v)
                val flag = isValidatePan()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallUploadDocumentAPI(edtPanNo.text.toString().trim() , edtPanValidTill.text.toString().trim())
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }

            R.id.txtOtherCopy ->  {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogOther()
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
            R.id.edtOtherValidTill -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDate.time)
                        edtOtherValidTill.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time))

                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.LLcardButtonUploadOther -> {
                hideKeyboard(applicationContext, v)
                val flag = isValidateOther()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            CallUploadDocumentAPI(edtOtherNo.text.toString().trim() , edtOtherValidTill.text.toString().trim())
                        } else {
                            toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }
        }
    }

    private fun CallUploadDocumentAPI(documentNo: String, validtill: String) {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        var partsList: MultipartBody.Part
        if (ImageUri != null) {
            if(ImageUri.toString().contains(".pdf")) {
                partsList = CommonUtil.prepareFilePart(this, "application/*", "DocumentCopy", ImageUri!!)
            } else {
                partsList = CommonUtil.prepareFilePart(this, "image/*", "DocumentCopy", ImageUri!!)
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList = MultipartBody.Part.createFormData("DocumentCopy", "", attachmentEmpty)
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var call = ApiUtils.apiInterface.CustomerDocumetnUpload(
            CreatedBy, documentType, documentNo, DOB,
            true, CreatedBy,
            imagegst = partsList
        )

        call.enqueue(object : Callback<CommonResponse> {
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
            }
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)

                        edtDocumentType.setText("")
                        documentType = ""
                        ImageUri = null

                        edtAadharNo.setText("")
                        edtAadharValidTill.setText("")
                        ImgAadharCopy.setImageResource(0)
                        txtAadharCopy.setText("Choose File")

                        edtVisaNo.setText("")
                        edtVisaValidTill.setText("")
                        ImgVisaCopy.setImageResource(0)
                        txtVisaCopy.setText("Choose File")

                        edtPanNo.setText("")
                        edtPanValidTill.setText("")
                        ImgPanCopy.setImageResource(0)
                        txtPanCopy.setText("Choose File")

                        edtPassportNo.setText("")
                        edtPassportValidTill.setText("")
                        ImgPassportCopy.setImageResource(0)
                        txtPassportCopy.setText("Choose File")

                        edtOtherNo.setText("")
                        edtOtherValidTill.setText("")
                        ImgOtherCopy.setImageResource(0)
                        txtOtherCopy.setText("Choose File")


                        LLAadhar.gone()
                        LLVisa.gone()
                        LLPan.gone()
                        LLPassport.gone()
                        LLOther.gone()

                        val intent = Intent()
                        intent.putExtra(AppConstant.IS_API_CALL, true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
        })
    }

    private fun isValidateOther(): Boolean {
        var check = true
        if (edtOtherNo.text.isEmpty()) {
            edtOtherNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtOtherValidTill.text.isEmpty()) {
            edtOtherValidTill.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (ImageUri == null) {
            RlOther.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    private fun isValidateVisa(): Boolean {
        var check = true
        if (edtVisaNo.text.isEmpty()) {
            edtVisaNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtVisaValidTill.text.isEmpty()) {
            edtVisaValidTill.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (ImageUri == null) {
            RlVisa.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    private fun isValidateAadhar(): Boolean {
        var check = true
        if (edtAadharNo.text.isEmpty()) {
            edtAadharNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtAadharValidTill.text.isEmpty()) {
            edtAadharValidTill.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (!isValidAadhaarNumber(edtAadharNo.text!!.toString())) {
            edtAadharNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            edtAadharNo.error = getString(R.string.error_valid_aadharno)
            check = false
        }
        if (ImageUri == null) {
            RlAadhar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    private fun isValidatePan(): Boolean {
        var check = true
        if (edtPanNo.text.isEmpty()) {
            edtPanNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtPanValidTill.text.isEmpty()) {
            edtPanValidTill.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (!isValidPanCardNo(edtPanNo.text!!.toString())) {
            edtPanNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            edtPanNo.error = getString(R.string.error_valid_panno)
            check = false
        }
        if (ImageUri == null) {
            RlPan.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    private fun isValidatePassport(): Boolean {
        var check = true
        if (edtPassportNo.text.isEmpty()) {
            edtPassportNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtPassportValidTill.text.isEmpty()) {
            edtPassportValidTill.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
//        if (!isValidPassportNo(edtPassportNo.text!!.toString())) {
//            edtPassportNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
//            edtPassportNo.error = getString(R.string.error_valid_passportno)
//            check = false
//        }
        if (ImageUri == null) {
            RlPassport.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    private fun photopicker() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 111);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            // Camera Image
            111 -> if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)

                ImageUri = ImagePaths[0]

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.loadURI(ImagePaths[0], R.drawable.ic_image)
                }
            }

            // Passport pdf
            101 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                ImageUri = Uri.fromFile(fileFromContentUri1("passport",applicationContext , sUri))

                val pathname = sPath

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPassportCopy.text = pathname
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtAadharCopy.text = pathname
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtVisaCopy.text = pathname
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPanCopy.text = pathname
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtOtherCopy.text = pathname
                }

            }

            // Aadhar pdf
            201 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                ImageUri = Uri.fromFile(fileFromContentUri2("aadhar",applicationContext , sUri))

                val pathname = sPath

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPassportCopy.text = pathname
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtAadharCopy.text = pathname
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtVisaCopy.text = pathname
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPanCopy.text = pathname
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtOtherCopy.text = pathname
                }
            }

            // visa pdf
            301 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                ImageUri = Uri.fromFile(fileFromContentUri3("visa",applicationContext , sUri))

                val pathname = sPath

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPassportCopy.text = pathname
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtAadharCopy.text = pathname
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtVisaCopy.text = pathname
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPanCopy.text = pathname
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtOtherCopy.text = pathname
                }
            }

            // pan pdf
            401 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                ImageUri = Uri.fromFile(fileFromContentUri4("pan",applicationContext , sUri))

                val pathname = sPath

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPassportCopy.text = pathname
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtAadharCopy.text = pathname
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtVisaCopy.text = pathname
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPanCopy.text = pathname
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtOtherCopy.text = pathname
                }
            }

            // other pdf
            501 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path

                ImageUri = Uri.fromFile(fileFromContentUri5("other",applicationContext , sUri))

                val pathname = sPath

                if(documentType.equals("PassPort")) {
                    ImgPassportCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPassportCopy.text = pathname
                } else if (documentType.equals("Aadharcard")) {
                    ImgAadharCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtAadharCopy.text = pathname
                } else if (documentType.equals("Visa")) {
                    ImgVisaCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtVisaCopy.text = pathname
                } else if (documentType.equals("PanCard")) {
                    ImgPanCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtPanCopy.text = pathname
                } else if (documentType.equals("Other")) {
                    ImgOtherCopy.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    txtOtherCopy.text = pathname
                }
            }
        }
    }

    /** AI005
     * This method is to open Initial dialog
     */
    private fun selectInitialDialog() {
        var dialogSelectInitial = Dialog(this)
        dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectInitial.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectInitial.window!!.attributes)

        dialogSelectInitial.window!!.attributes = lp
        dialogSelectInitial.setCancelable(true)
        dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectInitial.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectInitial.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(this, arrDocTypeList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                documentType = arrDocTypeList!![pos]
                edtDocumentType.setText(documentType)
                dialogSelectInitial!!.dismiss()

                if(documentType.equals("PassPort")) {
                    LLPassport.visible()
                    LLVisa.gone()
                    LLAadhar.gone()
                    LLPan.gone()
                    LLOther.gone()
                } else if (documentType.equals("Aadharcard")) {
                    LLPassport.gone()
                    LLVisa.gone()
                    LLAadhar.visible()
                    LLPan.gone()
                    LLOther.gone()
                } else if (documentType.equals("Visa")) {
                    LLPassport.gone()
                    LLVisa.visible()
                    LLAadhar.gone()
                    LLPan.gone()
                    LLOther.gone()
                } else if (documentType.equals("PanCard")) {
                    LLPassport.gone()
                    LLVisa.gone()
                    LLAadhar.gone()
                    LLPan.visible()
                    LLOther.gone()
                } else if(documentType.equals("Other")) {
                    LLPassport.gone()
                    LLVisa.gone()
                    LLAadhar.gone()
                    LLPan.gone()
                    LLOther.visible()
                }
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectInitial!!.show()
    }

    private fun showBottomSheetDialogPan() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 401)
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {
            photopicker()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showBottomSheetDialogVisa() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 301)
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {
            photopicker()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showBottomSheetDialogPassport() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 101)
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {
            photopicker()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showBottomSheetDialogAadhar() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 201)
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {
            photopicker()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showBottomSheetDialogOther() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)

        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 501)
            bottomSheetDialog.dismiss()
        }

        Select_Image!!.setOnClickListener {
            photopicker()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun fileFromContentUri1(name  :String, context: Context, contentUri: Uri): File {
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

    fun fileFromContentUri2(name  :String, context: Context, contentUri: Uri): File {
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

    fun fileFromContentUri5(name  :String, context: Context, contentUri: Uri): File {
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
}