import SwiftUI
import ComposeApp
import FirebaseCore

@main
struct iOSApp: App {

    init() {
        FirebaseApp.configure()
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}