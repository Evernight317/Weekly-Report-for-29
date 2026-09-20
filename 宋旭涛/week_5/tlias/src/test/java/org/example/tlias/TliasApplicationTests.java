package org.example.tlias;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class TliasApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void testGenJWT(){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id1", 1);
        claims.put("name", "tom");
        String jwt=Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "secret")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .compact();
        System.out.println(jwt);


    }
}
