package com.example.adminblinkitclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.Adapters.OrdersAdapter
import com.example.adminblinkitclone.Models.OrderedItems
import com.example.adminblinkitclone.databinding.FragmentHomeBinding
import com.example.adminblinkitclone.databinding.FragmentOrdersBinding
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class OrdersFragment : Fragment() {
    private lateinit var binding:FragmentOrdersBinding
    private val viewModel:AdminViewModel by viewModels()
    private lateinit var adapter: OrdersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentOrdersBinding.inflate(inflater,container,false)
        getAllOrders()
         return binding.root
    }

    private fun getAllOrders() {
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList->
                if (orderList.isNotEmpty()){
                    val orderedList=ArrayList<OrderedItems>()
                    for(orders in orderList){
                        val title= StringBuilder()
                        var totalPrice=0;
                        for(products in orders.orderList!!){
                            val price=products.productPrice?.substring(1)?.toInt()
                            val itemCount=products.productCount!!
                            totalPrice+=(price?.times(itemCount)!!)

                            title.append("${products.productCategory}, ")
                        }
                        val orderedItem=OrderedItems(
                            orders.orderId,orders.orderDate,orders.orderStatus,title.toString(),totalPrice,orders.userAddress
                        )
                        orderedList.add(orderedItem)
                    }
                    adapter=OrdersAdapter(requireContext(),::showOrderDetail)
                    binding.RVyourOrders.adapter=adapter
                    adapter.differ.submitList(orderedList)
                }
            }
        }
    }


    fun showOrderDetail(orders: OrderedItems){
        val bundle=Bundle()
        bundle.putInt("status",orders.itemStatus!!)
        bundle.putString("orderId", orders.orderId)
        bundle.putString("address", orders.address)
        findNavController().navigate(R.id.action_ordersFragment_to_orderDetailFragment,bundle)
    }

}