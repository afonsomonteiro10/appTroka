package pt.ipsantarem.apptroka

data class UserProfile(
        val uid: String = "",
        val name: String = "",
        val email: String = "",
        val photoUrl: String = "",
        val skillOffer: String = "",
        val skillWant: String = "",
        val createdAt: Long = 0L
    )


}