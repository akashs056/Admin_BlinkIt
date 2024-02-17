package com.example.adminblinkitclone

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkitclone.Adapters.CategoryAdapter
import com.example.adminblinkitclone.Adapters.ProductAdapter
import com.example.adminblinkitclone.Models.Category
import com.example.adminblinkitclone.databinding.FragmentHomeBinding
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel : AdminViewModel by viewModels()
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()
        setProductCategrory()
        getAllProducts("All")
        return binding.root
    }

    private fun getAllProducts(title: String?) {
        lifecycleScope.launch {
            viewModel.fetchAllProducts(title).collect{
                if (it.isEmpty()){
                    binding.textView2.visibility=View.VISIBLE
                    binding.productsRv.visibility=View.GONE
                }else{
                    binding.textView2.visibility=View.GONE
                    binding.productsRv.visibility=View.VISIBLE
                }
                val adapterProduct=ProductAdapter()
                binding.productsRv.adapter=adapterProduct
                adapterProduct.differ.submitList(it)
        }
        }

    }

    private fun setProductCategrory() {
        val categoryList =ArrayList<Category>()
        for (i in 0 until Constants.allProductsCategory.size){
            categoryList.add(Category(Constants.allProductsCategory[i],Constants.allProductsCategoryIcon[i]))
        }
        binding.cateoryRv.adapter= CategoryAdapter(categoryList,::getProductByCategory)
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
    private fun getProductByCategory(category: Category){
        getAllProducts(category.title)
    }
}