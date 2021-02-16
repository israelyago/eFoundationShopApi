package core.user

class UserValidator {
    companion object {
        fun validate(user: User): UserValidationError? {

            // TODO: Real email validation using regex?
            if (!user.email.contains("@")) {
                return UserValidationError.INVALID_EMAIL
            }

            return null
        }
    }
}