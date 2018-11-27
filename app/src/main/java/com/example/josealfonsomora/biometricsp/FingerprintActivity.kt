package com.example.josealfonsomora.biometricsp

import android.content.DialogInterface
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_fingerprint.*

class FingerprintActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    private val biometricPromptCallback =
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                fingerPrintCallback.text = "$errorCode : $errString"
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                fingerPrintCallback.text = "onAuthenticationSucceeded"
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                fingerPrintCallback.text = "$helpCode : $helpString"
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                fingerPrintCallback.text = "onAuthenticationFailed"
            }
        }

    private val authCallback = object : FingerprintManager.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            fingerPrintCallback.text = "$errorCode : $errString"
        }

        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            fingerPrintCallback.text = "onAuthenticationSucceeded"

        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
            fingerPrintCallback.text = "$helpCode : $helpString"

        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            fingerPrintCallback.text = "onAuthenticationFailed"
        }
    }

    private val cancelSignal = CancellationSignal().apply {
        setOnCancelListener {
            fingerPrintCallback.text = "setOnCancelListener"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fingerprint)
    }

    override fun onStart() {
        super.onStart()

        val fingerprintManager = getSystemService(FingerprintManager::class.java)

        hardwareDetectedButton.setBackgroundColor(
            if (fingerprintManager.isHardwareDetected) {
                Color.GREEN
            } else {
                Color.RED
            }
        )

        fingerPrintSaved.setBackgroundColor(
            if (fingerprintManager.hasEnrolledFingerprints()) {
                Color.GREEN
            } else {
                Color.RED
            }
        )

        fingerprint.setOnClickListener {
            cancelFingerprint.visibility = View.VISIBLE
            cancelFingerprint.setOnClickListener { cancelSignal.cancel() }

            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    openBiometricPrompt().authenticate(
                        cancelSignal,
                        mainExecutor,
                        biometricPromptCallback
                    )
                } else {
                    fingerprintManager.authenticate(
                        null,
                        cancelSignal,
                        0,
                        authCallback,
                        null
                    )
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun openBiometricPrompt() =
        BiometricPrompt.Builder(this)
            .setDescription("description")
            .setNegativeButton("no", mainExecutor,
                DialogInterface.OnClickListener { dialog, which -> fingerPrintCallback.text = "Negative clicked" })
            .setSubtitle("tubtitle")
            .setTitle("title")
            .build()

}