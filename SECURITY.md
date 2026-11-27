# Security

IDroot Authenticator keeps one-time codes and short notes on the device. Please report a vulnerability privately.

Use [GitHub private vulnerability reporting](https://github.com/arcanis2k3/Authenticator/security/advisories/new) for this repository. Do not open a public issue for a security report, and do not include a vault file, a password, or a live one-time code.

## In scope

- The Android app in this repository, including the encrypted vault and the notes export
- The release signing of `org.idroot.authenticator`

The vault file format is the Aegis format, with an optional `notes` field that Aegis ignores. A flaw in that shared format should also be reported to [Beem Development](https://github.com/beemdevelopment/Aegis/security). This project is not affiliated with them.

## Out of scope

- The idroot.org website and the handle service
- A device that is already unlocked, rooted, or has a readable backup you created yourself
- Missing Play Store or F-Droid distribution

There is no bug bounty.
