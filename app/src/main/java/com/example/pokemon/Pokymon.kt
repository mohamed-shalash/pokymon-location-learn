package com.example.pokemon

import android.location.Location

class Pokymon {
    var name:String?=null;
    var des:String?=null;
    var image:Int?=null;
    var power:Double?=null;
    var location:Location?=null;
    var isCatch:Boolean?=false;

    constructor(
        name: String?,des: String?,image: Int?,power: Double?,longtude: Double,latitude: Double) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.location = Location(name)
        this.location!!.longitude = longtude

        this.location!!.latitude = latitude



    }







}