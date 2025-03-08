package com.slippery.codereview.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Long EXPIRATION_TIME =8640000L;
    private final String KEY_STRING="d40a77e2114707c2c0a72e48402fea6638c9257793b38d60bf547b1d5b8d96a62d8ed39ac5691973ed7cd7ffc96bbaf24a8909eb7591fa7d8c16735aabb85159a9f08e4c834b4785d1b79bf4bfc1123d1e7843205b3de2107a63c12ea46f24559236533c17084a44e8b0075e1026114726a6ec54592d660997dd5b45bacbdbc3a29f74329036d66d44e4514bbe932a96437e33b3fb8492739536e9adeabdef4e8b751fb2a66b3581b9804a3a5698d92cac463f33f17cde2ea5b81b50d2f51db8f5af39beab3738649d73aac699f449ac7e4900c45a5ac61f989bc31906f0ba966fd136c364436127bbb337a935f0d339c40784395838b8cf236fa0a9256bb1c7";


    public String extractUsernameFromToken(String token) {
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return true;
    }
}
