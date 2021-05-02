package com.plcoding.doodlekong.ui.setup.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.plcoding.doodlekong.R
import com.plcoding.doodlekong.data.remote.ws.Room
import com.plcoding.doodlekong.databinding.FragmentCreateRoomBinding
import com.plcoding.doodlekong.databinding.FragmentSelectRoomBinding
import com.plcoding.doodlekong.databinding.FragmentUsernameBinding
import com.plcoding.doodlekong.ui.setup.SetupViewModel
import com.plcoding.doodlekong.util.navigateSafely
import com.plcoding.doodlekong.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private var _binding: FragmentCreateRoomBinding? = null
    private val binding: FragmentCreateRoomBinding
        get() = _binding!!

    private val viewModel: SetupViewModel by activityViewModels()
    private val args: CreateRoomFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateRoomBinding.bind(view)
        setupRoomSizeSpinner()
        listenToEvents()

        binding.btnCreateRoom.setOnClickListener {
            binding.createRoomProgressBar.isVisible = true
            viewModel.createRoom(
                    Room(
                            binding.etRoomName.text.toString(),
                            binding.tvMaxPersons.text.toString().toInt()
                    )
            )
        }
    }

    private fun listenToEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect { event ->
                when (event) {
                    is SetupViewModel.SetupEvent.CreateRoomEvent -> {
                        viewModel.joinRoom(args.username, event.room.name)
                    }
                    is SetupViewModel.SetupEvent.InputEmptyError -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackbar(R.string.error_field_empty)
                    }
                    is SetupViewModel.SetupEvent.InputTooShortError -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackbar(R.string.error_room_name_too_short)
                    }
                    is SetupViewModel.SetupEvent.InputTooLongError -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackbar(R.string.error_room_name_too_long)
                    }
                    is SetupViewModel.SetupEvent.CreateRoomErrorEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackbar(event.error)
                    }
                    is SetupViewModel.SetupEvent.JoinRoomEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        findNavController().navigateSafely(
                                R.id.action_createRoomFragment_to_drawingActivity,
                                args = Bundle().apply {
                                    putString("username", args.username)
                                    putString("roomName", event.roomName)
                                }
                        )
                    }
                    is SetupViewModel.SetupEvent.JoinRoomErrorEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackbar(event.error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRoomSizeSpinner() {
        val roomSizes = resources.getStringArray(R.array.room_size_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.textview_room_size, roomSizes)
        binding.tvMaxPersons.setAdapter(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}