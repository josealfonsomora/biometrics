package com.example.josealfonsomora.biometricsp

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_fingerprint.*

@SuppressLint("SetTextI18n")
class FingerprintActivity : AppCompatActivity() {

    val cancellationSignal = CancellationSignal()

    val callback = object : FingerprintManagerCompat.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            fingerPrintCallback.text = "Error Code: $errorCode = $errString"
        }

        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            fingerPrintCallback.text = "onAuthenticationSucceeded"
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
            fingerPrintCallback.text = "Help Code: $helpCode = $helpString"
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            fingerPrintCallback.text = "Authentication failed"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)
    }

    override fun onStart() {
        super.onStart()

        val fingerprintManager = FingerprintManagerCompat.from(this)
//        val fingerprintManager = getSystemService(FingerprintManagerCompat::class.java)
        fingerprintManager.isHardwareDetected

        fingerprintManager.hasEnrolledFingerprints()

        cancelFingerprint.setOnClickListener {
            cancellationSignal.cancel()
        }

        fingerprint.setOnClickListener {
            fingerPrintCallback.text = "Listening.."
            cancelFingerprint.visibility = View.VISIBLE

            fingerprintManager.authenticate(
                null, // cryptoObject
                0, // should be 0
                cancellationSignal, // cancel listener
                callback, // FingerprintManagerCompat.AuthenticationCallback()
                null // Handler to run callbacks
            )
        }
    }
}