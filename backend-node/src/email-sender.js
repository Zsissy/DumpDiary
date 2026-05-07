import nodemailer from "nodemailer";

function envFlag(name, fallback) {
  const value = process.env[name];
  if (value == null || value === "") return fallback;
  return value === "true";
}

export class EmailSender {
  constructor() {
    this.host = process.env.SMTP_HOST ?? "";
    this.port = Number(process.env.SMTP_PORT ?? "587");
    this.username = process.env.SMTP_USERNAME ?? "";
    this.password = process.env.SMTP_PASSWORD ?? "";
    this.senderAddress = process.env.SMTP_FROM || this.username;
    this.startTls = envFlag("SMTP_STARTTLS", true);
    this.ssl = envFlag("SMTP_SSL", false);
  }

  async sendVerificationCode(email, code, purposeLabel) {
    if (!this.host || !this.username || !this.password || !this.senderAddress) {
      return {
        delivered: false,
        detail: `SMTP is not configured. Development code: ${code}`
      };
    }

    const transporter = nodemailer.createTransport({
      host: this.host,
      port: this.port,
      secure: this.ssl,
      requireTLS: this.startTls,
      auth: {
        user: this.username,
        pass: this.password
      }
    });

    await transporter.sendMail({
      from: this.senderAddress,
      to: email,
      subject: "DumpDiary verification code",
      text: `Your DumpDiary ${purposeLabel} verification code is: ${code}\n\nThis code will expire in 10 minutes.`
    });

    return {
      delivered: true,
      detail: `Verification code sent to ${email}`
    };
  }
}
