package com.xcx;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

//@SpringBootTest
class TliasWebManagementApplicationTests {

    @Test
    public void testGenJWT(){

        String jwt =Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,"xcxxcxc")//签名算法
                .setClaims(null)
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .compact();
        System.out.println(jwt);
    }

    @ Test
    public void testParseJWT(){
        Claims claims=Jwts.parser()
                .setSigningKey("xcxxcxc")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3ODU1NzcwOTF9.O7WeuntNsVn9YypeuBc1VnhPvM3G1qVlyu2PLCGvpxI")
                .getBody();
        System.out.println(claims);
    }
}
