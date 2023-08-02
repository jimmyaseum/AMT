package com.app.amtcust.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.dialog.DialogInitialAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.CommonResponse
import com.app.amtcust.model.response.TourBookingPaxResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.AppConstant
import com.app.amtcust.utils.CommonUtil
import com.app.amtcust.utils.PrefConstants
import com.app.amtcust.utils.SharedPreference
import com.app.amtcust.utils.convertDateStringToString
import com.app.amtcust.utils.getRequestJSONBody
import com.app.amtcust.utils.gone
import com.app.amtcust.utils.hideKeyboard
import com.app.amtcust.utils.isConnectivityAvailable
import com.app.amtcust.utils.isOnline
import com.app.amtcust.utils.loadURIRoundedCorner
import com.app.amtcust.utils.loadUrlRoundedCorner2
import com.app.amtcust.utils.preventTwoClick
import com.app.amtcust.utils.toast
import com.app.amtcust.utils.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale
import kotlinx.android.synthetic.main.activity_add_payment_receipt.*
import kotlinx.android.synthetic.main.activity_add_payment_receipt.view.*
import kotlinx.android.synthetic.main.activity_edit_profile.edtInitial
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

class AddPaymentReceiptActivity : BaseActivity(),  View.OnClickListener, EasyPermissions.PermissionCallbacks {

    var state: String = ""
    var sharedPreference: SharedPreference? = null
    var calDate = Calendar.getInstance()
    var PaymentDate: String = ""

    var PRDocument: String = ""
    var ImagePaths: ArrayList<Uri> = ArrayList()
    private var ImageUri: Uri? = null

    private val arrPaymentForList: ArrayList<String> = ArrayList()
    var PaymentFor : String = ""

    private val arrPaymentTypeList: ArrayList<String> = ArrayList()
    var PaymentType : String = ""

    var PRID: Int = 0
    var PDate: String = ""
    var TBNO: String = ""
    var Amount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_receipt)
        getIntentData()
        initializeView()
    }
    private fun getIntentData() {
        state = intent.getStringExtra("State").toString()
        if(state.equals("Update")) {
            PRID = intent.getIntExtra("PRID",0)
            PDate = intent.getStringExtra("PDate").toString()
            TBNO = intent.getStringExtra("TBNO").toString()
            Amount = intent.getStringExtra("Amount").toString()
            PRDocument = intent.getStringExtra("PRDocument").toString()
            PaymentFor = intent.getStringExtra("PaymentFor").toString()
            PaymentType = intent.getStringExtra("PaymentType").toString()


            edtPaymentDate.setText(PDate)
            PaymentDate = convertDateStringToString(PDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!

            edtTourBookingNo.setText(TBNO)
            edtAmount.setText(Amount)
            edtPaymentFor.setText(PaymentFor)
            edtPaymentType.setText(PaymentType)

            if(PRDocument.contains(".pdf")) {
                select_image.gone()
                ll_Pdf.visible()
                select_pdf.text = PRDocument
            } else {
                select_image.visible()
                ll_Pdf.gone()
                select_image.loadUrlRoundedCorner2(
                    PRDocument,
                    R.drawable.ic_image,1
                )
            }

            CallTourBookingPaxAPI()
        }
    }
    override fun initializeView() {
        sharedPreference = SharedPreference(this@AddPaymentReceiptActivity)
        setToolBar()
        InitiaListner()
    }
    private fun setToolBar() {
        if(state.equals("Add")) {
            tbTvTitle.text = "Add Payment Receipt"
            upload_image.text = "Add Document"
        } else {
            tbTvTitle.text = "Edit Payment Receipt"
            upload_image.text = "Change Document"
        }
    }
    private fun InitiaListner() {

        arrPaymentForList.add("TOUR")
        arrPaymentForList.add("PACKAGE")
        arrPaymentForList.add("TICKET")

        arrPaymentTypeList.add("CASH")
        arrPaymentTypeList.add("CREDIT CARD")
        arrPaymentTypeList.add("CHEQUE")
        arrPaymentTypeList.add("NEFT/IMPS")
        arrPaymentTypeList.add("UPI")
        arrPaymentTypeList.add("BANK DEPOSIT")

        imgBack.setOnClickListener(this)
        txtSave.setOnClickListener(this)
        upload_image.setOnClickListener(this)
        edtPaymentDate.setOnClickListener(this)
        edtPaymentFor.setOnClickListener(this)
        edtPaymentType.setOnClickListener(this)

        upload_image.setPaintFlags(upload_image.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        if(state.equals("Update")) {
            select_image.setOnClickListener {
                if(isOnline(this)) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PRDocument))
                    startActivity(browserIntent)
                } else {
                    toast(resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }

            ll_Pdf.setOnClickListener {
                if(isOnline(this)) {
                    var format = "https://docs.google.com/gview?embedded=true&url=%s"
                    val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, PRDocument)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                    startActivity(browserIntent)
                } else {
                    toast(resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }
        } else {
            updateSelectDate(calDate)
        }

        edtTourBookingNo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                CallTourBookingPaxAPI()
                true
            } else {
                false
            }
        }

        edtTourBookingNo.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (edtTourBookingNo.text!!.isNotEmpty()) {
                    CallTourBookingPaxAPI()
                    hideKeyboard(this@AddPaymentReceiptActivity, view)

                }
            }
        })
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectDate(cal: Calendar) {
        PaymentDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtPaymentDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
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
                if(state.equals("Add")) {
                    hideKeyboard(applicationContext, txtSave)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallAddPaymentReceiptAPI()
                            } else {
                                toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                            }
                        }
                    }

                } else {
                    hideKeyboard(applicationContext, txtSave)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallEditPaymentReceiptAPI()
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
            R.id.edtPaymentDate -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectDate(calDate)
                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.edtPaymentFor -> {
                preventTwoClick(v)
                selectPaymentForDialog()
            }
            R.id.edtPaymentType -> {
                preventTwoClick(v)
                selectPaymentTypeDialog()
            }
            R.id.upload_image -> {
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
        }
    }

    /** AI005
     * This method is to open PaymentFor dialog
     */
    private fun selectPaymentForDialog() {
        var dialogSelectPaymentFor = Dialog(this)
        dialogSelectPaymentFor.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPaymentFor.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPaymentFor.window!!.attributes)

        dialogSelectPaymentFor.window!!.attributes = lp
        dialogSelectPaymentFor.setCancelable(true)
        dialogSelectPaymentFor.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPaymentFor.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPaymentFor.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPaymentFor.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPaymentFor.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(this, arrPaymentForList)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                PaymentFor = arrPaymentForList!![pos]
                edtPaymentFor.setText(PaymentFor)
                dialogSelectPaymentFor!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPaymentFor!!.show()
    }

    /** AI005
     * This method is to open PaymentType dialog
     */
    private fun selectPaymentTypeDialog() {
        var dialogSelectPaymentType = Dialog(this)
        dialogSelectPaymentType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPaymentType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPaymentType.window!!.attributes)

        dialogSelectPaymentType.window!!.attributes = lp
        dialogSelectPaymentType.setCancelable(true)
        dialogSelectPaymentType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPaymentType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPaymentType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPaymentType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPaymentType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(this, arrPaymentTypeList)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                PaymentType = arrPaymentTypeList!![pos]
                edtPaymentType.setText(PaymentType)
                dialogSelectPaymentType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPaymentType!!.show()
    }

    private fun isValidate(): Boolean {
        var check = true

        if (edtTourBookingNo.text.isEmpty()) {
            edtTourBookingNo.error = "Enter Tour Booking No"
            edtTourBookingNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtPaymentFor.text.isEmpty()) {
            edtPaymentFor.error = "Select Payment For"
            edtPaymentFor.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtPaymentDate.text.isEmpty()) {
            edtPaymentDate.error = "Select Payment Date"
            edtPaymentDate.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtAmount.text.isEmpty()) {
            edtAmount.error = "Enter Amount"
            edtAmount.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }

        if(state.equals("Add")) {
            if (ImageUri == null) {
                toast("Please upload image", Toast.LENGTH_LONG)
                check = false
            }
        }
        return check
    }

    private fun showBottomSheetDialogGST() {
        val bottomSheetDialog = BottomSheetDialog(this,R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)

        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)
        val Select_WordDoc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_WordDoc)

        Select_Image!!.setOnClickListener {
            photopickerGST()
            bottomSheetDialog.dismiss()
        }
        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 104)
            bottomSheetDialog.dismiss()
        }
        Select_WordDoc!!.gone()

        bottomSheetDialog.show()
    }

    private fun photopickerGST() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 111);
    }

    /** AI005
     * This method is to get image from external storage */
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri!!
                if (isConnectivityAvailable(this)) {
                    ImageUri = Uri.fromFile(File(resultUri.path))
                    select_image.loadURIRoundedCorner(
                        resultUri,
                        R.drawable.ic_image,
                        1
                    )
                    select_image.visible()
                    ll_Pdf.gone()
                } else {
                    toast(getString(R.string.str_msg_no_internet),Toast.LENGTH_LONG)
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                toast("No apps can perform this action.", Toast.LENGTH_LONG)
            }
        }
        if (requestCode == 111) {
            if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
                val PassportPath = ImagePaths[0]
                CropImage.activity(PassportPath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)

            }
        }
        if(requestCode == 104) {
            // PDF File
            if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path
                ImageUri = Uri.fromFile(fileFromContentUri4("passport", applicationContext, sUri))

                select_pdf.text = sPath
                ll_Pdf.visible()
                select_image.gone()
            }
        }
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

    private fun CallTourBookingPaxAPI() {
        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("TourBookingNo", edtTourBookingNo.text.toString().trim())

        val call = ApiUtils.apiInterface.getTourBookingPaxDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourBookingPaxResponse> {
            override fun onResponse(call: Call<TourBookingPaxResponse>, response: Response<TourBookingPaxResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val Info = response.body()?.Data!!

                        llTBNOInfo.visible()
                        if(Info.Name != null && Info.Name != "") {
                            txtName.text = Info.Name
                        }
                        if(Info.MobileNo != null && Info.MobileNo != "") {
                            txtMobile.text = Info.MobileNo
                        }

                    } else {
                        hideProgress()
                        llTBNOInfo.gone()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingPaxResponse>, t: Throwable) {
                hideProgress()
                llTBNOInfo.gone()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })


    }

    private fun CallAddPaymentReceiptAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            if(ImageUri.toString().contains(".pdf")) {
                partsList.add(CommonUtil.prepareFilePart(this, "*/*", "ReceiptImage", ImageUri!!))
            } else {
                partsList.add(CommonUtil.prepareFilePart(this, "image/*", "ReceiptImage", ImageUri!!))
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("ReceiptImage", "", attachmentEmpty))
        }

        val MTourBookingNo = CommonUtil.createPartFromString(edtTourBookingNo.text.toString().trim())
        val MPaymentFor = CommonUtil.createPartFromString(edtPaymentFor.text.toString().trim())
        val MPaymentType = CommonUtil.createPartFromString(edtPaymentType.text.toString().trim())
        val MPaymentDate = CommonUtil.createPartFromString(PaymentDate)
        val MAmount= CommonUtil.createPartFromString(edtAmount.text.toString().trim())

        var call = ApiUtils.apiInterface.AddPaymentReceipt(
            MTourBookingNo,
            MPaymentFor,
            MPaymentDate,
            MAmount,
            MPaymentType,
            ReceiptImage = partsList
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

    private fun CallEditPaymentReceiptAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            if(ImageUri.toString().contains(".pdf")) {
                partsList.add(CommonUtil.prepareFilePart(this, "*/*", "ReceiptImage", ImageUri!!))
            } else {
                partsList.add(CommonUtil.prepareFilePart(this, "image/*", "ReceiptImage", ImageUri!!))
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("ReceiptImage", "", attachmentEmpty))
        }

        val MPRID = CommonUtil.createPartFromString(PRID.toString())
        val MTourBookingNo = CommonUtil.createPartFromString(edtTourBookingNo.text.toString().trim())
        val MPaymentFor = CommonUtil.createPartFromString(edtPaymentFor.text.toString().trim())
        val MPaymentType = CommonUtil.createPartFromString(edtPaymentType.text.toString().trim())
        val MPaymentDate = CommonUtil.createPartFromString(PaymentDate)
        val MAmount= CommonUtil.createPartFromString(edtAmount.text.toString().trim())

        var call = ApiUtils.apiInterface.EditPaymentReceipt(
            MPRID,
            MTourBookingNo,
            MPaymentFor,
            MPaymentDate,
            MAmount,
            MPaymentType,
            ReceiptImage = partsList
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
}