package com.example.Context.Database.Tables.Enum

enum class RoleTypes(val value: Int) {
    User(0),
    Manager(1),
    Admin(2),
}

fun Int.getRoleByInt(): RoleTypes {
    return when (this) {
        RoleTypes.User.value -> RoleTypes.User
        RoleTypes.Manager.value -> RoleTypes.Manager
        RoleTypes.Admin.value -> RoleTypes.Admin
        else -> RoleTypes.User
    }
}