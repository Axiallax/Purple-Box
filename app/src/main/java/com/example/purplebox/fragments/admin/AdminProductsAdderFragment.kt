package com.example.purplebox.fragments.admin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.purplebox.R
import com.example.purplebox.activities.AdminActivity
import com.example.purplebox.databinding.FragmentAdminProductsAdderBinding
import com.example.purplebox.model.Products
import com.example.purplebox.viewmodel.admin.AdminViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class AdminProductsAdderFragment : Fragment() {
    private lateinit var viewModel: AdminViewModel
    private lateinit var binding: FragmentAdminProductsAdderBinding
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as AdminActivity).viewModel

        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog
                .Builder(activity)
                .setTitle("Product color")
                .setPositiveButton("Select", object : ColorEnvelopeListener {

                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            viewModel.selectedColors.add(it.color)
                            updateColors()
                        }
                    }

                }).setNegativeButton("Cancel") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }


        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data

                    //Multiple images selected
                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 0
                        (0 until count).forEach { it ->
                            val imagesUri = intent.clipData?.getItemAt(it)?.uri
                            imagesUri?.let { viewModel.selectedImages.add(it) }
                        }

                        //One images was selected
                    } else {
                        val imageUri = intent?.data
                        imageUri?.let { viewModel.selectedImages.add(it) }
                    }
                    updateImages()
                }
            }
        //6
        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminProductsAdderBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSaveClick()
        onHomeClick()
    }


    private fun onSaveClick() {
        binding.buttonSaveProduct.setOnClickListener {
            val productValidation = validateInformation()
            if (!productValidation) {
                Toast.makeText(activity, "Check your inputs", Toast.LENGTH_SHORT).show()
            }
            saveProducts {
                Log.d("test", it.toString())
            }
        }
    }

    //2
    private fun validateInformation(): Boolean {
        if (viewModel.selectedImages.isEmpty())
            return false
        if (binding.edName.text.toString().trim().isEmpty())
            return false
        if (binding.edCategory.text.toString().trim().isEmpty())
            return false
        if (binding.edPrice.text.toString().trim().isEmpty())
            return false
        return true
    }

    //3
    private fun saveProducts(state: (Boolean) -> Unit) {
        val sizes = getSizesList(binding.edSizes.text.toString().trim())
        val imagesByteArrays = getImagesByteArrays() //7
        val name = binding.edName.text.toString().trim()
        val images = mutableListOf<String>()
        val category = binding.edCategory.text.toString().trim()
        val productDescription = binding.edDescription.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPercentage = binding.edOfferPercentage.text.toString().trim()

        lifecycleScope.launch {
            showLoading()
            try {
                withContext(Dispatchers.Default) {
                    Log.d("test1", "test")
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imagesStorage = storage.child("products/images/$id")
                            val result = imagesStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                hideLoading()
                state(false)
            }

            Log.d("test2", "test")

            val product = Products(
                UUID.randomUUID().toString(),
                name,
                category,
                price.toFloat(),
                if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                productDescription.ifEmpty { null },
                viewModel.selectedColors,
                sizes,
                images
            )
            firestore.collection("Products").add(product).addOnSuccessListener {
                state(true)
                hideLoading()
            }.addOnFailureListener {
                Log.e("test2", it.message.toString())
                state(false)
                hideLoading()
            }
        }
    }


    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }


    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }


    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        viewModel.selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val contentResolver = activity?.contentResolver
            val imageBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 85, stream)) {
                val imageAsByteArray = stream.toByteArray()
                imagesByteArray.add(imageAsByteArray)
            }
        }
        return imagesByteArray
    }


    private fun getSizesList(sizes: String): List<String>? {
        if (sizes.isEmpty())
            return null
        return sizes.split(",").map { it.trim() }
    }

    //5
    private fun updateColors() {
        var colors = ""
        viewModel.selectedColors.forEach {
            colors = "$colors ${Integer.toHexString(it)}, "
        }
        binding.tvSelectedColors.text = colors
    }


    private fun updateImages() {
        binding.tvSelectedImages.text = viewModel.selectedImages.size.toString()
    }


    private fun onHomeClick() {
        val btm = activity?.findViewById<BottomNavigationView>(R.id.admin_bottom_navigation)
        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
            activity?.onBackPressed()
            true
        }
    }


}