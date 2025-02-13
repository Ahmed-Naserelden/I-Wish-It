package service;

import connection.Response;
import connection.SignInPayload;
import connection.SignUpPayload;
import dal.UserRepository;
import connection.MemberPayload;

public class AuthService {

    public static Response signIn(SignInPayload payload) {
        MemberPayload user = UserRepository.signIn(
                payload.getEmail(),
                payload.getPassword()
        );
        return new Response(user != null, user);
    }

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
