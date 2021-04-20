package com.erikriosetiawan.academy.ui.reader.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.databinding.FragmentModuleListBinding
import com.erikriosetiawan.academy.ui.reader.CourseReaderActivity
import com.erikriosetiawan.academy.ui.reader.CourseReaderCallback
import com.erikriosetiawan.academy.ui.reader.CourseReaderViewModel
import com.erikriosetiawan.academy.viewmodel.ViewModelFactory
import com.erikriosetiawan.academy.vo.Status

class ModuleListFragment : Fragment(), MyAdapterClickListener {

    companion object {
        val TAG: String = ModuleListFragment::class.java.simpleName

        fun newInstance() = ModuleListFragment()
    }

    private lateinit var fragmentModuleListBinding: FragmentModuleListBinding
    private lateinit var adapter: ModuleListAdapter
    private lateinit var courseReaderCallback: CourseReaderCallback
    private lateinit var viewModel: CourseReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentModuleListBinding = FragmentModuleListBinding.inflate(inflater, container, false)
        return fragmentModuleListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[CourseReaderViewModel::class.java]
        adapter = ModuleListAdapter(this)

        viewModel.modules.observe(viewLifecycleOwner, { moduleEntities ->
            moduleEntities?.let {
                when (moduleEntities.status) {
                    Status.LOADING -> fragmentModuleListBinding.progressBar.visibility =
                        View.VISIBLE
                    Status.SUCCESS -> {
                        fragmentModuleListBinding.progressBar.visibility = View.GONE
                        populateRecyclerView(moduleEntities.data as List<ModuleEntity>)
                    }
                    Status.ERROR -> {
                        fragmentModuleListBinding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        courseReaderCallback = context as CourseReaderActivity
    }

    override fun onItemClicked(position: Int, moduleId: String) {
        courseReaderCallback.moveTo(position, moduleId)
        viewModel.setSelectedModule(moduleId)
    }

    private fun populateRecyclerView(modules: List<ModuleEntity>) {
        with(fragmentModuleListBinding) {
            progressBar.visibility = View.GONE
            adapter.setModules(modules)
            rvModule.layoutManager = LinearLayoutManager(context)
            rvModule.setHasFixedSize(true)
            rvModule.adapter = adapter
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            rvModule.addItemDecoration(dividerItemDecoration)
        }
    }
}