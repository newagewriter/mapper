import com.kgb.User
import com.kgb.UserInfo
import com.kgb.UserStatus
import com.kgb.mapper.UserMapper

fun main() {
    println("run main")
    val userInfo = UserInfo("http://avatar.url", System.currentTimeMillis())
    val user = User("kb", 12345, userInfo,75.5f)

    println("create user: $user ")
    println("use mapper to create map of field from user id: ${user.id}")
    val mm = mapOf<String, Any?>(
        "name" to user.name,
        "id" to user.id
    )
    val mapper = UserMapper(user)
    println("user to map: ${mapper.toMap()}")
    println("user to json: ${mapper.toJson()}")

}