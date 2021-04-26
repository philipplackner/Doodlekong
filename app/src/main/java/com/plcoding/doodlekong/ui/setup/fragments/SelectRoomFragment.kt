package com.plcoding.doodlekong.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.plcoding.doodlekong.R
import com.plcoding.doodlekong.databinding.FragmentSelectRoomBinding
import com.plcoding.doodlekong.databinding.FragmentUsernameBinding

class SelectRoomFragment : Fragment(R.layout.fragment_select_room) {

    private var _binding: FragmentSelectRoomBinding? = null
    private val binding: FragmentSelectRoomBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectRoomBinding.bind(view)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}