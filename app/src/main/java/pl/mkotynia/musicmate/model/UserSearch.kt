package pl.mkotynia.musicmate.model

data class UserSearch(val username: String, val email: String, val instrument: String,
                      val status: Int, val level: Int, val rehearsalSpace: Boolean, val result: List<User>?)
