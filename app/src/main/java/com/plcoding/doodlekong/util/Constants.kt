package com.plcoding.doodlekong.util

object Constants {

    const val USE_LOCALHOST = true

    const val HTTP_BASE_URL = ""
    const val HTTP_BASE_URL_LOCALHOST = "http://192.168.0.3:8001/"

    const val WS_BASE_URL = ""
    const val WS_BASE_URL_LOCALHOST = "http://192.168.0.3:8001/ws/draw"

    const val DEFAULT_PAINT_THICKNESS = 12f

    const val MIN_USERNAME_LENGTH = 4
    const val MAX_USERNAME_LENGTH = 12

    const val MIN_ROOM_NAME_LENGTH = 4
    const val MAX_ROOM_NAME_LENGTH = 16

    const val SEARCH_DELAY = 300L

    const val RECONNECT_INTERVAL = 3000L

    const val MAX_WORD_VOICE_GUESS_AMOUNT = 3

    const val TYPE_CHAT_MESSAGE = "TYPE_CHAT_MESSAGE"
    const val TYPE_DRAW_DATA = "TYPE_DRAW_DATA"
    const val TYPE_ANNOUNCEMENT = "TYPE_ANNOUNCEMENT"
    const val TYPE_JOIN_ROOM_HANDSHAKE = "TYPE_JOIN_ROOM_HANDSHAKE"
    const val TYPE_GAME_ERROR = "TYPE_GAME_ERROR"
    const val TYPE_PHASE_CHANGE = "TYPE_PHASE_CHANGE"
    const val TYPE_CHOSEN_WORD = "TYPE_CHOSEN_WORD"
    const val TYPE_GAME_STATE = "TYPE_GAME_STATE"
    const val TYPE_NEW_WORDS = "TYPE_NEW_WORDS"
    const val TYPE_PLAYERS_LIST = "TYPE_PLAYERS_LIST"
    const val TYPE_PING = "TYPE_PING"
    const val TYPE_DISCONNECT_REQUEST = "TYPE_DISCONNECT_REQUEST"
    const val TYPE_DRAW_ACTION = "TYPE_DRAW_ACTION"
    const val TYPE_CUR_ROUND_DRAW_INFO = "TYPE_CUR_ROUND_DRAW_INFO"
}