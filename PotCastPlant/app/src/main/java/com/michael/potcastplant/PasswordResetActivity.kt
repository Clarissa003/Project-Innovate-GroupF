package com.michael.potcastplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PasswordResetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
    }

    /*try {
        val result = Amplify.Auth.resetPassword("email")
        Log.i("AuthQuickstart", "Password reset OK: $result")
    }
    catch (error: AuthException) {
        Log.e("AuthQuickstart", "Password reset failed", error)
    }

    try {
        Amplify.Auth.confirmResetPassword("email", "NewPassword123", "code you received")
        Log.i("AuthQuickstart", "New password confirmed")
    }
    catch (error: AuthException) {
        Log.e("AuthQuickstart", "Failed to confirm password reset", error)
    }

    */
}