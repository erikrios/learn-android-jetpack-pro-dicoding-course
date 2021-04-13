package com.erikriosetiawan.myunittesting

class MainViewModel(private val cuboidModel: CuboidModel) {

    fun getCircumference() = cuboidModel.getCircumference()

    fun getSurfaceArea() = cuboidModel.getSurfaceArea()

    fun getVolume() = cuboidModel.getVolume()

    fun save(width: Double, length: Double, height: Double) {
        cuboidModel.save(width, length, height)
    }
}