package com.rijaldev.expertmovies.ui.main.more

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rijaldev.expertmovies.R
import com.rijaldev.expertmovies.databinding.FragmentMoreBinding
import java.util.*

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        when (Locale.getDefault().language) {
            "in" -> setImageEnd(R.drawable.ic_flag_id)
            "en" -> setImageEnd(R.drawable.ic_flag_us)
        }
        binding?.tvLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setImageEnd(drawable: Int) {
        binding?.tvLanguage?.setCompoundDrawablesWithIntrinsicBounds(
            null, null, ContextCompat.getDrawable(requireActivity(), drawable), null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}