package com.example.adminblinkitclone.Models

import android.net.Uri
import com.example.adminblinkitclone.Utils
import java.util.UUID

data class Product(
    val productRandomId:String=Utils.getRandomUid(),
    val productTitle:String?=null,
    val productQuantity:Int?=null,
    val productTUnit:String?=null,
    val productPrice:Int?=null,
    val productStock:Int?=null,
    val productCategory:String?=null,
    val productType:String?=null,
    val itemCount:Int?=null,
    val adminUid:String?=null,
    var productImageUris:ArrayList<String?>?=null,
    val timestamp: Long = System.currentTimeMillis()
)
