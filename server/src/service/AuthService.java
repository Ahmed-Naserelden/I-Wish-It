package service;

import connection.MemberPayload;
import connection.Response;
import connection.SignInPayload;
import connection.SignUpPayload;
import dal.UserRepository;

/**
 * AuthService class to handle authentication-related operations.
 * Provides methods for user sign-in and sign-up actions.
 */
public class AuthService {

    /**
     * Signs in a user with the provided sign-in payload.
     *
     * @param payload The SignInPayload object containing the email and password.
     * @return A Response object containing the sign-in status and user details.
     */
    public static Response signIn(SignInPayload payload) {
        MemberPayload user = UserRepository.signIn(
                payload.getEmail(),
                payload.getPassword()
        );
        return new Response(user != null, user);
    }

    /**
     * Signs up a new user with the provided sign-up payload.
     *
     * @param payload The SignUpPayload object containing the email, username, password, and date of birth.
     * @return A Response object containing the sign-up status.
     */
    public static Response signUp(SignUpPayload payload) {
        boolean status = UserRepository.signUp(
                payload.getEmail(),
                payload.getUsername(),
                payload.getPassword(),
                payload.getDob()
        );
        return new Response(status);
    }
}
