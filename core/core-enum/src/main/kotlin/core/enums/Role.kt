package core.enums

enum class Role(
    val roleName: String,
    val number: Int,
) {
    STUDENT("학생", 0),
    TEACHER("선생님", 1),
    ADMIN("관리자", 2),
    ;

    companion object {
        fun fromNumber(number: Int): Role {
            return values().first { it.number == number }
        }

        fun fromRoleName(role: Role): Int {
            return role.number
        }
    }
}
