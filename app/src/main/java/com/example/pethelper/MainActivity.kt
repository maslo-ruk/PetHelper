package com.example.pethelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pethelper.ui.theme.PetHelperTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            PetHelperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PetHelperApp(modifier = Modifier.padding(innerPadding), auth = auth)
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }
}
//    private fun sendEmailVerification() {
//        // [START send_email_verification]
//        val user = auth.currentUser!!
//        user.sendEmailVerification()
//            .addOnCompleteListener(this) { task ->
//                // Email Verification sent
//            }
//        // [END send_email_verification]
//    }
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetHelperTheme {
        Greeting("Android")
    }
}