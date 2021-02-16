package core.user

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long,
    @SerializedName("username")
    val userName: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean = false,
)
