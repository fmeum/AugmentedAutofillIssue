package com.chrome.example

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.core.content.edit
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import kotlin.system.exitProcess

class MainActivity : Activity() {

    companion object {
        private const val PREFERENCE_IMPORTANT_FOR_AUTOFILL = "importantForAutofill"

        private val EXPLANATION_BROKEN = buildSpannedString {
            append("Autofill requests are correctly dispatched to the user-configured Autofill service for as long as the app is not restarted or a manual Autofill request is executed ")
            bold {
                append("if the first field tapped by the user is ")
                italic {
                    append("not")
                }
                append(" the URL bar.")
            }
            append("\n\nAutofill requests never reach the user-configured Autofill service (and go to the augmented Autofill service instead) for as long as the app is not restarted or a manual Autofill request is executed ")
            bold {
                append("if the first field tapped by the user is the URL bar.")
            }
        }

        private val EXPLANATION_WORKAROUND = buildSpannedString {
            append("Autofill requests are always correctly dispatched to the user-configured Autofill service.")
        }
    }

    private val urlBar: EditText by lazy { findViewById<EditText>(R.id.url_bar) }
    private val usernameField: EditText by lazy { findViewById<EditText>(R.id.username) }
    private val passwordField: EditText by lazy { findViewById<EditText>(R.id.password) }
    private val submit: Button by lazy { findViewById<Button>(R.id.submit) }
    private val switchImportantForAutofill: Switch by lazy { findViewById<Switch>(R.id.switchImportantForAutofill) }
    private val explanationText: TextView by lazy { findViewById<TextView>(R.id.explanationText) }

    private var urlBarImportantForAutofill: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            PREFERENCE_IMPORTANT_FOR_AUTOFILL, false
        )
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(this).edit(commit = true) {
                putBoolean(PREFERENCE_IMPORTANT_FOR_AUTOFILL, value)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        switchImportantForAutofill.isChecked = urlBarImportantForAutofill
        urlBar.importantForAutofill =
            if (urlBarImportantForAutofill) View.IMPORTANT_FOR_AUTOFILL_AUTO else View.IMPORTANT_FOR_AUTOFILL_NO
        switchImportantForAutofill.setOnCheckedChangeListener { _, b ->
            urlBarImportantForAutofill = b
            restartApp()
        }
        explanationText.text =
            if (urlBarImportantForAutofill) EXPLANATION_WORKAROUND else EXPLANATION_BROKEN

        submit.setOnClickListener {
            val url = urlBar.text.toString()
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            Toast.makeText(
                applicationContext,
                "Logged in to $url as $username with password $password",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun restartApp() {
        startActivity(Intent.makeRestartActivityTask(ComponentName(this, MainActivity::class.java)))
        exitProcess(0)
    }
}