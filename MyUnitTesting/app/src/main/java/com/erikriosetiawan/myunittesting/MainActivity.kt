package com.erikriosetiawan.myunittesting

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.erikriosetiawan.myunittesting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainViewModel = MainViewModel(CuboidModel())

        activityMainBinding.apply {
            btnSave.setOnClickListener(this@MainActivity)
            btnCalculateSurfaceArea.setOnClickListener(this@MainActivity)
            btnCalculateCircumference.setOnClickListener(this@MainActivity)
            btnCalculateVolume.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(v: View?) {
        val length = activityMainBinding.edtLength.text.toString().trim()
        val width = activityMainBinding.edtWidth.text.toString().trim()
        val height = activityMainBinding.edtHeight.text.toString().trim()

        when {
            TextUtils.isEmpty(length) ->
                activityMainBinding.edtLength.error = "This field not must be empty."
            TextUtils.isEmpty(width) ->
                activityMainBinding.edtWidth.error = "This field not must be empty."
            TextUtils.isEmpty(height) ->
                activityMainBinding.edtHeight.error = "This field not must be empty."
            else -> {
                val valueLength = length.toDouble()
                val valueWidth = width.toDouble()
                val valueHeight = width.toDouble()

                when (v?.id) {
                    R.id.btn_save -> {
                        mainViewModel.save(valueLength, valueWidth, valueHeight)
                        visible()
                    }
                    R.id.btn_calculate_circumference -> {
                        activityMainBinding.tvResult.text =
                            mainViewModel.getCircumference().toString()
                        gone()
                    }
                    R.id.btn_calculate_surface_area -> {
                        activityMainBinding.tvResult.text =
                            mainViewModel.getSurfaceArea().toString()
                        gone()
                    }
                    R.id.btn_calculate_volume -> {
                        activityMainBinding.tvResult.text =
                            mainViewModel.getVolume().toString()
                        gone()
                    }
                }
            }
        }
    }

    private fun visible() {
        activityMainBinding.apply {
            btnCalculateVolume.visibility = View.VISIBLE
            btnCalculateCircumference.visibility = View.VISIBLE
            btnCalculateSurfaceArea.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
        }
    }

    private fun gone() {
        activityMainBinding.apply {
            btnCalculateVolume.visibility = View.GONE
            btnCalculateCircumference.visibility = View.GONE
            btnCalculateSurfaceArea.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        }
    }
}