import ComposeApp
import GoogleSignIn
import Sentry
import UIKit

enum AppBootstrap {

    static func configureSentry() {
        #if !DEBUG
        SentrySDK.start { options in
            options.dsn = "https://5a7292343acfb18e77a8769f235e63df@o4509799192985600.ingest.de.sentry.io/4511683042672720"
            options.environment = "production"
            options.tracesSampleRate = 0.1
            options.sendDefaultPii = false
            options.beforeSend = { event in
                event.request = nil
                event.user = nil
                return event
            }
        }
        #endif
        CrashReporterSetupKt.setCrashReporter { message in
            SentrySDK.capture(message: message)
        }
    }

    static func initKoin() {
        #if DEBUG
        InitKoinKt.doInitKoin(isDebug: true, config: nil)
        #else
        InitKoinKt.doInitKoin(isDebug: false, config: nil)
        #endif
    }

    static func configureGoogleSignIn() {
        GoogleSignInSetupKt.setGoogleSignInProvider { onResult in
            guard let presenting = UIApplication.shared.topViewController else {
                onResult(nil, "No presenting view controller")
                return
            }
            GIDSignIn.sharedInstance.signIn(withPresenting: presenting) { signInResult, error in
                if let error = error {
                    if let signInError = error as? GIDSignInError, signInError.code == .canceled {
                        onResult(nil, "cancelled")
                    } else {
                        onResult(nil, error.localizedDescription)
                    }
                    return
                }
                guard let idToken = signInResult?.user.idToken?.tokenString else {
                    onResult(nil, "Missing Google ID token")
                    return
                }
                onResult(idToken, nil)
            }
        }
    }

    static func configureAppleSignIn() {
        AppleSignInSetupKt.setAppleSignInProvider { onResult in
            let coordinator = AppleSignInCoordinator()
            coordinator.start { idToken, nonce, error in
                onResult(idToken, nonce, error)
            }
        }
    }
}
