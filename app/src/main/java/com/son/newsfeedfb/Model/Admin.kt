package com.son.newsfeedfb.Model

class Admin {
         lateinit var idChild: String
         lateinit var nameAmdin: String
        lateinit var avatarAdmin: String

    companion object{
        lateinit var admin: Admin
        fun  getId() : Admin{
            if (this::admin.isInitialized.not()){
                    admin = Admin()
            }
            return admin
        }
    }

}