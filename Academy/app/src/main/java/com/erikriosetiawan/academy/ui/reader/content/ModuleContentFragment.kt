package com.erikriosetiawan.academy.ui.reader.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.databinding.FragmentModuleContentBinding
import com.erikriosetiawan.academy.ui.reader.CourseReaderViewModel
import com.erikriosetiawan.academy.viewmodel.ViewModelFactory
import com.erikriosetiawan.academy.vo.Status

class ModuleContentFragment : Fragment() {

    companion object {
        val TAG: String = ModuleContentFragment::class.java.simpleName
        fun newInstance() = ModuleContentFragment()
    }

    private lateinit var fragmentModuleContentBinding: FragmentModuleContentBinding
    private lateinit var viewModel: CourseReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentModuleContentBinding =
            FragmentModuleContentBinding.inflate(inflater, container, false)
        return fragmentModuleContentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(
                requireActivity(),
                factory
            )[CourseReaderViewModel::class.java]

            viewModel.selectedModule.observe(viewLifecycleOwner, { moduleEntity ->
                moduleEntity?.let {
                    when (moduleEntity.status) {
                        Status.LOADING -> fragmentModuleContentBinding.progressBar.visibility =
                            View.VISIBLE
                        Status.SUCCESS -> moduleEntity.data?.let {
                            fragmentModuleContentBinding.progressBar.visibility = View.GONE
                            moduleEntity.data.contentEntity?.let {
                                populateWebView(moduleEntity.data)
                            }
                            setButtonNextPrevState(moduleEntity.data)
                            if (!moduleEntity.data.read) {
                                viewModel.readContent(moduleEntity.data)
                            }
                        }
                        Status.ERROR -> {
                            fragmentModuleContentBinding.progressBar.visibility = View.GONE
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    fragmentModuleContentBinding.btnNext.setOnClickListener { viewModel.setNextPage() }
                    fragmentModuleContentBinding.btnPrev.setOnClickListener { viewModel.setPrevPage() }
                }
            })
        }
    }

    private fun populateWebView(module: ModuleEntity) {
        fragmentModuleContentBinding.webView.loadData(
            module.contentEntity?.content ?: "",
            "text/html",
            "UTF-8"
        )
    }

    private fun setButtonNextPrevState(module: ModuleEntity) {
        activity?.let {
            when (module.position) {
                0 -> {
                    fragmentModuleContentBinding.btnPrev.isEnabled = false
                    fragmentModuleContentBinding.btnNext.isEnabled = true
                }
                viewModel.getModuleSize() - 1 -> {
                    fragmentModuleContentBinding.btnPrev.isEnabled = true
                    fragmentModuleContentBinding.btnNext.isEnabled = false
                }
                else -> {
                    fragmentModuleContentBinding.btnPrev.isEnabled = true
                    fragmentModuleContentBinding.btnNext.isEnabled = true
                }
            }
        }
    }
}