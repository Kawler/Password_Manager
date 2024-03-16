package com.artemla.passwordmanager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.artemla.passwordmanager.databinding.FragmentHomeBinding
import com.artemla.passwordmanager.domain.adapters.PasswordsRVAdapter
import com.artemla.passwordmanager.ui.passwordpage.PasswordModalFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(requireContext())
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        intiRV()
        initFab()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFab() {
        binding.homeFab.setOnClickListener {
            PasswordModalFragment.newInstance(null)
                .show(requireActivity().supportFragmentManager, PasswordModalFragment.TAG)
        }
    }

    private fun intiRV() {
        val rvAdapter = PasswordsRVAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = rvAdapter
        initDataObserver(rvAdapter)
    }

    private fun initDataObserver(adapter: PasswordsRVAdapter) {
        viewModel.getData().observe(viewLifecycleOwner) {
            viewModel.fillAdapter(adapter, it)
            if (it.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.homeEmptyList.visibility = View.GONE
            } else {
                binding.homeEmptyList.visibility = View.VISIBLE
            }
        }
    }
}