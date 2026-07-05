import SwiftUI

@main
struct iOSApp: App {

    init() {
        AppBootstrap.configureSentry()
        AppBootstrap.initKoin()
        AppBootstrap.configureGoogleSignIn()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
