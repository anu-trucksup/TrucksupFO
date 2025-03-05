package com.trucksup.field_officer.data.model

import com.glovejob.data.model.Token

class User {
    var id: Int? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var passwordSalt: String? = null
    var password: String? = null
    var cpassword: String? = null
    var loginResponse: Token? = null
}