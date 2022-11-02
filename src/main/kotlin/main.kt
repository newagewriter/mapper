import com.kgb.User
import com.kgb.mapper.UserMapper

fun main() {
    println("run main")
    val user = User("kb", "12345")
    println("create user: $user ")
    println("use mapper to create map of field from user")
    val mapper = UserMapper(user)
    println("user to map: ${mapper.toMap()}")

}