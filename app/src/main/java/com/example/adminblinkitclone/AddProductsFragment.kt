package com.example.adminblinkitclone

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkitclone.Adapters.SelectedImageAdapter
import com.example.adminblinkitclone.Models.Product
import com.example.adminblinkitclone.databinding.FragmentAddProductsBinding
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddProductsFragment : Fragment() {
    private val viewModel : AdminViewModel by viewModels()
    private lateinit var binding:FragmentAddProductsBinding
    private val ImageUri:ArrayList<Uri> = arrayListOf()
    val selectedImage= registerForActivityResult(ActivityResultContracts.GetMultipleContents()){listOfUri->
        val fiveImages=listOfUri.take(5)
        ImageUri.clear()
        ImageUri.addAll(fiveImages)
        binding.selectedImageRv.adapter=SelectedImageAdapter(ImageUri)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddProductsBinding.inflate(layoutInflater)
        setStatusBarColor()
        setAutoCompleteTextView()
        onImageButtonClick()
        onAddbtnClicked()
        return binding.root
    }

    private fun onAddbtnClicked() {
        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(),"Uploading Images")
            val productTitle=binding.etProductTitle.text.toString()
            val productQuantity=binding.etproductQuantity.text.toString()
            val productUnit=binding.etProductunit.text.toString()
            val productPrice=binding.etPrice.text.toString()
            val productStock=binding.etNoOfStock.text.toString()
            val productCategory=binding.etProductCategory.text.toString()
            val productType=binding.etProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() ||
                productStock.isEmpty() || productPrice.isEmpty() || productCategory.isEmpty() ||productType.isEmpty()){
                Utils.hideDialog()
                Utils.Toast(requireContext(),"Empty Fields are not allowed")
                return@setOnClickListener
            }else if (ImageUri.isEmpty()){
                Utils.hideDialog()
                Utils.Toast(requireContext(),"Please select some Images")
                return@setOnClickListener
            }else{
                val products =Product(
                    productTitle=productTitle,
                    productQuantity =productQuantity.toInt(),
                    productTUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUid()
                )

                saveImage(products)
            }
        }
    }

    private fun saveImage(products: Product) {
        viewModel.saveImagesInDB(ImageUri)
        lifecycleScope.launch {
            viewModel.isImageUploaded.collect{
                if (it){
                    Utils.hideDialog()
                    Utils.Toast(requireContext(),"Image Uploaded")
                    getUrls(products)
                }
            }
        }
    }

    private fun getUrls(products: Product) {
        Utils.showDialog(requireContext(),"Publishing Product........")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
                val urls=it
                products.productImageUris=urls
                saveProduct(products)
            }
        }

    }

    private fun saveProduct(products: Product) {
            viewModel.saveProduct(products)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect{
                if (it){
                    Utils.hideDialog()
                    startActivity(Intent(requireActivity(),AdminMainActivity::class.java))
                    Utils.Toast(requireContext(),"Your Product is Live")
                }
            }
        }
    }

    private fun onImageButtonClick() {
        binding.btnSelectedimage.setOnClickListener {
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView() {
        val units=ArrayAdapter(requireContext(),R.layout.showlist,Constants.allUnitsOfProduct)
        val category=ArrayAdapter(requireContext(),R.layout.showlist,Constants.allProductsCategory)
        val productType=ArrayAdapter(requireContext(),R.layout.showlist,Constants.allProductType)
        binding.apply {
            etProductunit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors= ContextCompat.getColor(requireContext(),R.color.yellow)
            statusBarColor=statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}