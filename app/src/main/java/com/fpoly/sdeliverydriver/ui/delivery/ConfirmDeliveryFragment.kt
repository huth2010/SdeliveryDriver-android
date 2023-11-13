package com.fpoly.sdeliverydriver.ui.delivery

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.CreateDeliveryOrderRequest
import com.fpoly.sdeliverydriver.data.model.Notify
import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.databinding.FragmentConfirmDeliveryBinding
import com.fpoly.sdeliverydriver.ultis.Constants.Companion.SUCCESS_STATUS
import com.fpoly.sdeliverydriver.ultis.checkPermissionGallery
import com.fpoly.sdeliverydriver.ultis.showSnackbar
import com.fpoly.sdeliverydriver.ultis.showUtilDialog
import com.fpoly.sdeliverydriver.ultis.startToDetailPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ConfirmDeliveryFragment : PolyBaseFragment<FragmentConfirmDeliveryBinding>(){
    private val deliveryViewModel: DeliveryViewModel by activityViewModel()
    private var currentOrder: OrderResponse? = null
    private var multipartBody: MultipartBody.Part? = null
    private var currentPhotoUri: Uri? = null
    private var takePictureLauncher: ActivityResultLauncher<Uri>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupEventListeners()
        registerPictureLauncher()
    }

    private fun setupAppBar() {
        with(views.appBar) {
            btnBackToolbar.visibility = View.VISIBLE
            tvTitleToolbar.text = "Tóm tắt cuốc giao"
        }
    }

    private fun setupEventListeners() {
        views.appBar.btnBackToolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        views.btnCapturePhoto.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        views.btnChoosePhoto.setOnClickListener { selectImageFromGallery() }
        views.btnConfirm.setOnClickListener { createDeliveryOrder() }
    }

    private fun registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { result ->
            try {
                if (result) {
                    showCapturedImage()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showPermissionDeniedToast() {
        showSnackbar(
            requireView(),
            "Camera permission denied, please allow permission",
            false,
            ""
        ) {
            activity?.startToDetailPermission()
        }
    }

    private fun showCapturedImage() {
        views.choosePhoto.setImageURI(null)
        views.choosePhoto.setImageURI(currentPhotoUri)
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (hasCameraPermission()) {
            createUri()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                takePictureLauncher?.launch(createUri())
            } else {
                showPermissionDeniedToast()
            }
        }

    private fun selectImageFromGallery() {
        checkPermissionGallery { isAllowed ->
            if (isAllowed) {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                resultLauncher.launch(intent)
            } else {
                showPermissionDeniedToast()
            }
        }
    }

    private val resultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null) {
            val intent = result.data
            if (intent?.data != null && result.resultCode == Activity.RESULT_OK) {
                handleGalleryImageSelection(intent.data!!)
            }
        }
    }

    private fun handleGalleryImageSelection(selectedImageUri: Uri) {
        currentPhotoUri = selectedImageUri
        loadSelectedImage(selectedImageUri)
    }

    private fun loadSelectedImage(selectedImageUri: Uri) {
        Glide.with(this)
            .load(selectedImageUri)
            .placeholder(R.drawable.loading_img)
            .into(views.choosePhoto)
    }

    private fun createUri(): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "camera_photo.jpg")
        }

        currentPhotoUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        currentPhotoUri?.let {
            val intentPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, it)
            takePictureLauncher?.launch(it)
        }

        return currentPhotoUri
    }

    private fun createDeliveryOrder() {
        currentOrder?.let {
            val mediaType = "text/plain".toMediaTypeOrNull()

            if (currentPhotoUri != null) {
                val filePath = uriToFilePath(currentPhotoUri!!)
                if (filePath.isNotEmpty()) {
                    val requestFile =
                        File(filePath).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    Log.d("TAG", "createDeliveryOrder: $filePath")
                    multipartBody =
                        MultipartBody.Part.createFormData(
                            "images",
                            File(filePath).name,
                            requestFile
                        )
                    val createRequest = CreateDeliveryOrderRequest(
                        it._id.toRequestBody(mediaType),
                        SUCCESS_STATUS.toRequestBody(mediaType),
                        multipartBody!!,
                        views.comment.text.toString().toRequestBody(mediaType),
                        "".toRequestBody(mediaType)
                    )
                    handleCreateDeliveryOrder(createRequest)
                } else {
                    showSnackbar(requireView(), "Error creating delivery order", false, "") {}
                }
            } else {
                showSnackbar(requireView(), "Error creating delivery order", false, "") {}
            }
        }
    }

    private fun handleCreateDeliveryOrder(createRequest: CreateDeliveryOrderRequest) {
        deliveryViewModel.handle(DeliveryViewAction.CreateDeliveryOrder(createRequest))
    }

    private fun uriToFilePath(uri: Uri): String {
        return when (uri.scheme) {
            "file" -> {
                uri.path ?: ""
            }

            "content" -> {
                getFilePathFromContentUri(uri)
            }

            else -> ""
        }
    }

    private fun getFilePathFromContentUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    return it.getString(columnIndex) ?: ""
                }
            }
        }
        return ""
    }

    private fun setupUi(orderCode: String?) {
        views.orderCode.text = orderCode
    }

    private fun showSuccessDialog() {
        activity?.showUtilDialog(
            Notify(
                "Tóm tắt cuộc giao",
                "Thành công",
                "Hệ thống đã ghi nhận",
                R.raw.animation_successfully
            )
        )
    }

    private fun showErrorDialog() {
        activity?.showUtilDialog(
            Notify(
                "Tóm tắt cuộc giao",
                "Thất bại",
                "Hệ thống lỗi, xin vui lòng thử lại sau",
                R.raw.animation_falure
            )
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentConfirmDeliveryBinding {
        return FragmentConfirmDeliveryBinding.inflate(inflater, container, false)
    }

    override fun invalidate(): Unit = withState(deliveryViewModel) {
        when (it.asyncGetCurrentOrder) {
            is Success -> {
                currentOrder = it.asyncGetCurrentOrder.invoke()
                currentOrder?.let { order ->
                    setupUi(order._id)
                }
            }

            is Fail -> {
                setupUi("Đang tải...")
            }

            else -> {
            }
        }
        when (it.asyncCreateDelivery) {
            is Success -> {
                showSuccessDialog()
                findNavController().popBackStack()
            }

            is Fail -> {
                showErrorDialog()
                deliveryViewModel.handleRemoveAsyncCreateDelivery()
            }

            else -> {
            }
        }
    }
}