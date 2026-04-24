package com.dumpdiary.backend.service

import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.MimeMessage
import java.util.Properties

data class EmailSendResult(
    val delivered: Boolean,
    val detail: String,
)

class EmailSender {
    private val host = System.getenv("SMTP_HOST").orEmpty()
    private val port = System.getenv("SMTP_PORT").orEmpty().ifBlank { "587" }
    private val username = System.getenv("SMTP_USERNAME").orEmpty()
    private val password = System.getenv("SMTP_PASSWORD").orEmpty()
    private val senderAddress = System.getenv("SMTP_FROM").orEmpty().ifBlank { username }
    private val startTls = System.getenv("SMTP_STARTTLS").orEmpty().ifBlank { "true" }.toBoolean()
    private val ssl = System.getenv("SMTP_SSL").orEmpty().ifBlank { "false" }.toBoolean()

    fun sendVerificationCode(email: String, code: String, purposeLabel: String): EmailSendResult {
        if (host.isBlank() || username.isBlank() || password.isBlank() || senderAddress.isBlank()) {
            return EmailSendResult(
                delivered = false,
                detail = "SMTP is not configured. Development code: $code",
            )
        }

        val props = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", port)
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", startTls.toString())
            put("mail.smtp.ssl.enable", ssl.toString())
        }

        val session = Session.getInstance(
            props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication =
                    PasswordAuthentication(username, password)
            },
        )

        val message = MimeMessage(session).apply {
            setHeader("From", senderAddress)
            setHeader("To", email)
            setRecipients(jakarta.mail.Message.RecipientType.TO, email)
            subject = "DumpDiary verification code"
            setText(
                """
                Your DumpDiary $purposeLabel verification code is: $code

                This code will expire in 10 minutes.
                """.trimIndent(),
            )
        }

        Transport.send(message)
        return EmailSendResult(
            delivered = true,
            detail = "Verification code sent to $email",
        )
    }
}
