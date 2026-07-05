import AuthenticationServices
import CryptoKit
import Foundation
import UIKit

final class AppleSignInCoordinator: NSObject,
    ASAuthorizationControllerDelegate,
    ASAuthorizationControllerPresentationContextProviding {

    private var onResult: ((String?, String?, String?) -> Void)?
    private var rawNonce: String?
    private var retained: AppleSignInCoordinator?

    func start(onResult: @escaping (String?, String?, String?) -> Void) {
        self.onResult = onResult
        self.retained = self

        guard let nonce = Self.randomNonce() else {
            onResult(nil, nil, "nonce_generation_failed")
            retained = nil
            return
        }
        self.rawNonce = nonce

        let provider = ASAuthorizationAppleIDProvider()
        let request = provider.createRequest()
        request.requestedScopes = [.fullName, .email]
        request.nonce = Self.sha256(nonce)

        let controller = ASAuthorizationController(authorizationRequests: [request])
        controller.delegate = self
        controller.presentationContextProvider = self
        controller.performRequests()
    }

    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization authorization: ASAuthorization
    ) {
        defer { retained = nil }
        guard
            let credential = authorization.credential as? ASAuthorizationAppleIDCredential,
            let tokenData = credential.identityToken,
            let idToken = String(data: tokenData, encoding: .utf8)
        else {
            onResult?(nil, nil, "Missing Apple identity token")
            return
        }
        onResult?(idToken, rawNonce, nil)
    }

    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError error: Error
    ) {
        defer { retained = nil }
        if let authError = error as? ASAuthorizationError, authError.code == .canceled {
            onResult?(nil, nil, "cancelled")
        } else {
            onResult?(nil, nil, error.localizedDescription)
        }
    }

    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        let window = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }
        return window ?? ASPresentationAnchor()
    }

    private static func randomNonce(length: Int = 32) -> String? {
        let charset = Array("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_")
        var bytes = [UInt8](repeating: 0, count: length)
        guard SecRandomCopyBytes(kSecRandomDefault, length, &bytes) == errSecSuccess else {
            return nil
        }
        return String(bytes.map { charset[Int($0) % charset.count] })
    }

    private static func sha256(_ input: String) -> String {
        let hash = SHA256.hash(data: Data(input.utf8))
        return hash.map { String(format: "%02x", $0) }.joined()
    }
}
