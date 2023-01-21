package com.hhh.paws.database.repository

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.MainActivity
import com.hhh.paws.R
import com.hhh.paws.database.dao.UserDao
import com.hhh.paws.database.model.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val database: FirebaseFirestore): UserDao {
    override fun getUser(): User {
        return User()
    }
}