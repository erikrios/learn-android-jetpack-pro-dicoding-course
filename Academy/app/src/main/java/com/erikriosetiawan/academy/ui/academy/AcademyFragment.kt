package com.erikriosetiawan.academy.ui.academy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erikriosetiawan.academy.databinding.FragmentAcademyBinding
import com.erikriosetiawan.academy.viewmodel.ViewModelFactory
import com.erikriosetiawan.academy.vo.Status

class AcademyFragment : Fragment() {

    private lateinit var fragmentAcademyBinding: FragmentAcademyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAcademyBinding = FragmentAcademyBinding.inflate(layoutInflater, container, false)
        return fragmentAcademyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(
                this,
                factory
            )[AcademyViewModel::class.java]

            val academyAdapter = AcademyAdapter()

            fragmentAcademyBinding.progressBar.visibility = View.VISIBLE
            viewModel.getCourses().observe(viewLifecycleOwner, { courses ->
                courses.let {
                    when (courses.status) {
                        Status.LOADING -> fragmentAcademyBinding.progressBar.visibility =
                            View.VISIBLE
                        Status.SUCCESS -> {
                            fragmentAcademyBinding.progressBar.visibility = View.GONE
                            academyAdapter.setCourses(courses.data)
                            academyAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR -> {
                            fragmentAcademyBinding.progressBar.visibility = View.GONE
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })

            with(fragmentAcademyBinding.rvAcademy) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = academyAdapter
            }
        }
    }
}