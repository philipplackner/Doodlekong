package com.plcoding.doodlekong.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.data.remote.ws.Room
import com.plcoding.doodlekong.repository.SetupRepository
import com.plcoding.doodlekong.util.Constants.MAX_ROOM_NAME_LENGTH
import com.plcoding.doodlekong.util.Constants.MAX_USERNAME_LENGTH
import com.plcoding.doodlekong.util.Constants.MIN_ROOM_NAME_LENGTH
import com.plcoding.doodlekong.util.Constants.MIN_USERNAME_LENGTH
import com.plcoding.doodlekong.util.DispatcherProvider
import com.plcoding.doodlekong.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class SetupEvent {
        data class GetRoomEvent(val rooms: List<Room>) : SetupEvent()
        data class GetRoomErrorEvent(val error: String) : SetupEvent()
        object GetRoomLoadingEvent : SetupEvent()
        object GetRoomEmptyEvent : SetupEvent()

        data class JoinRoomEvent(val roomName: String): SetupEvent()
        data class JoinRoomErrorEvent(val error: String) : SetupEvent()
    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    private val _rooms = MutableStateFlow<SetupEvent>(SetupEvent.GetRoomEmptyEvent)
    val rooms: StateFlow<SetupEvent> = _rooms

    fun getRooms(searchQuery: String) {
        _rooms.value = SetupEvent.GetRoomLoadingEvent
        viewModelScope.launch(dispatchers.main) {
            val result = repository.getRooms(searchQuery)
            if(result is Resource.Success) {
                _rooms.value = SetupEvent.GetRoomEvent(result.data ?: return@launch)
            } else {
                _setupEvent.emit(SetupEvent.GetRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }

    fun joinRoom(username: String, roomName: String) {
        viewModelScope.launch(dispatchers.main) {
            val result = repository.joinRoom(username, roomName)
            if(result is Resource.Success) {
                _setupEvent.emit(SetupEvent.JoinRoomEvent(roomName))
            } else {
                _setupEvent.emit(SetupEvent.JoinRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }
}