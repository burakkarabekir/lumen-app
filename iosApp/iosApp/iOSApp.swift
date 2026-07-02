import SwiftUI
import ComposeApp
import GoogleSignIn

@main
struct iOSApp: App {

    init() {
        InitKoinKt.doInitKoin()
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

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

private extension UIApplication {
    var topViewController: UIViewController? {
        let keyWindow = connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }
        var top = keyWindow?.rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}
