package com.example.josealfonsomora.biometricsp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fingerprint.*

@SuppressLint("SetTextI18n")
class FingerprintActivity : AppCompatActivity() {


    private val cancellationSignal = CancellationSignal()

    // DONT DO THIS: BiometricPrompt requires API 28 it will crash if you try in a device with API <
    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            fingerPrintCallback.text = "Error code: $errorCode = $errString"
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            fingerPrintCallback.text = "onAuthenticationSucceeded"
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
            fingerPrintCallback.text = "Help code: $helpCode = $helpString"
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            fingerPrintCallback.text = "onAuthenticationFailed"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)
    }

    override fun onStart() {
        super.onStart()

        fingerprint.setOnClickListener {
            fingerPrintCallback.text = "Listening.."

            biometricPrompt().authenticate(
                cancellationSignal,
                mainExecutor,
                callback
            )
        }
    }


    private fun biometricPrompt() = BiometricPrompt
        .Builder(this)
        .setTitle("title")
        .setSubtitle("subTitle")
        .setDescription("description")
        .setNegativeButton(
            "Cancel",
            mainExecutor,
            DialogInterface.OnClickListener { _, _ -> fingerPrintCallback.text = "description" })
        .build()
}