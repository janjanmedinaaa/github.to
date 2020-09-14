package com.medina.juanantonio.githubto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.medina.juanantonio.githubto.features.UserListItem

@Entity
class User : UserListItem {

    @PrimaryKey
    var id: Int = 0
    var login: String = ""
    var node_id: String = ""
    var avatar_url: String = ""
    var html_url: String = ""
    var name: String? = null
    var company: String? = null
    var blog: String? = null
    var email: String? = null
    var hireable: Boolean = false
    var bio: String? = null
    var followers: Int = 0
    var following: Int = 0
    var created_at: String = ""

    var note: String = ""
}