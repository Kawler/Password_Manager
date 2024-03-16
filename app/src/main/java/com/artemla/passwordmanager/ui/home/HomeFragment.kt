package com.artemla.passwordmanager.ui.home

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.artemla.passwordmanager.CryptoManager
import com.artemla.passwordmanager.adapters.PasswordsRVAdapter
import com.artemla.passwordmanager.databinding.FragmentHomeBinding
import com.artemla.passwordmanager.dt.Password
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
        val root: View = binding.root

        intiRV()

        binding.homeFab.setOnClickListener {
            PasswordModalFragment.newInstance(null)
                .show(requireActivity().supportFragmentManager, PasswordModalFragment.TAG)
        }
        return root
    }

    private fun intiRV(){
        val rvAdapter = PasswordsRVAdapter()
        val cryptoManager = CryptoManager()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = rvAdapter
        viewModel.getData().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.homeEmptyList.visibility = View.GONE
                val dataList = mutableListOf<Password>()
                for (pass in it){
                    dataList.add(Password(pass.id,pass.website,cryptoManager.decrypt(pass.password)))
                }
                rvAdapter.data = dataList
            } else {
                binding.homeEmptyList.visibility = View.VISIBLE
                rvAdapter.data = emptyList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}