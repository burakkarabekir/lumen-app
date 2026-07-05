import SwiftUI

@main
struct iOSApp: App {

    init() {
        AppBootstrap.configureSentry()
        AppBootstrap.initKoin()
        AppBootstrap.configureGoogleSignIn()
        AppBootstrap.configureAppleSignIn()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
