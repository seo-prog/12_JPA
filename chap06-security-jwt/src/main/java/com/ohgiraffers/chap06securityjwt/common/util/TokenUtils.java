package com.ohgiraffers.chap06securityjwt.common.util;


import com.ohgiraffers.chap06securityjwt.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 토큰 만들고, 유효성 검사, 복호화등 토큰 관련 수행 클래스
@Component
public class TokenUtils {


    private static String jwtSecretKey;

    private static long tokenValidateTime;

    // 우리가 막 만들어둔 그 yml 에 그거 불러오는거임
    @Value("${jwt.key}")
     public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }
    // 시간 가져오기
    @Value("${jwt.time}")
     public void setTokenValidateTime(long tokenValidateTime) {
        TokenUtils.tokenValidateTime = tokenValidateTime;
    }

     /**
      * header 의 token 을 분리하는 메소드
      * @Param header : Authrization 의 header 값을 가져온다.
      * @return token : Authrization 의 token 을 반환한다.
      *
      */
     public static String splitHeader(String header) {

         if(!header.equals("")){
             return header.split(" ")[1]; // BEARER 를 제외한 토큰 값만 반환 // 인덱스번호1번 (BEARER asdfasdgc)니까
         }else{
             return null;
         }
     }


     /**
      * 유효한 토큰인지 확인하는 메소드
      * @Param token : 토큰
      * @return boolean : 유효 여부
      */
     public static boolean isValidtoken(String token) {
         // 로그인 시 토큰이 유효한지 확인하는 메소드

         try{
             Claims claims = getClaimsFromToken(token);
             return true;
         }catch (Exception e){
             return false;
         }
     }

     /**
      * 토근을 복호화 하는 메소드
      * @Param token
      * @return Claims
      *
      */

     private static Claims getClaimsFromToken(String token) {
         // jwts.parser 와 parseClaimsJws 를 이용해 JWT 의 유효성을 검증한다.
         // 만약 JWT 가 유효하지 않으면 예외가 발생한다.
         // 우리가 입력한 키를 기준으로 복호화를 시킨다.
         return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                 .parseClaimsJws(token).getBody();
     }

    /**
     * 토큰을 생성하는 메소드
     * @param user userEntity
     * @return String token
     */

    public static String generateJwtToken(User user) {
        // 로그인 시 토큰을 생성해주는 메소드

        // 토큰 만료 시간을 현재 시간에서 지정된 유효 시간 이후로 설정
        Date expireTime = new Date(System.currentTimeMillis() + tokenValidateTime);
        // JwtBuilder 를 사용해 JWT 토큰을 생성하는 객체 초기화
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader()) // 토큰의 헤더 설정(헤더에는 토큰의 타입 및 알고리즘 정보가 담김)
                .setClaims(createClaims(user)) // 토큰의 클레임 설정(사용자 정보와 같은 데이터를 담음)
                .setSubject("ohgiraffers token : " + user.getUserNo()) // 토큰의 설명 정보를 담아줌
                .signWith(SignatureAlgorithm.HS256, createSignature()) // 토큰 암호화 방식 정의(비밀키 생성)
                .setExpiration(expireTime); // 만료시간 설정
        // 최종적으로 생성된 토큰을 문자열 형태로 반환
        return builder.compact();
    }

    /**
     * token 의 header 를 설정하는 메소드
     * @return Mao<String, Object> header 의 설정 정보
     */

    private static Map<String, Object> createHeader() {
        // header 의 설정 정보 넣어주기(헤더 설정)

        Map<String, Object> header = new HashMap<>();
        // 헤더의 토큰 타입을 JWT 로 설정
        header.put("type", "jwt");
        // 헤더의 토큰의 알고리즘을 HS25 으로 설정
        header.put("alg", "HS256");
        // 헤더에 토큰 생성 시간을 밀리초 단위로 추가
        header.put("date", System.currentTimeMillis());

        return header;
    }

    /**
     * 사용자 정보를 기반으로 클레임을 생성해주는 메소드
     * @param user 사용자 정보
     * @return Map<String, Object> Claims 정보
     * // 실질적으로 몸체에 들어가있는 데이터들 ( 사용자의 정보 )
     */

    private static Map<String, Object> createClaims(User user) {

        //setHeader, setClaims 의 반환타입이 map 인 이유는 setHeader, setClaims 메소드 자체에 들어가보면
        // map 으로 지정이 되어있기 때문이다.
        // 토큰에 담겨서 클라이언트에게 보내질꺼니까 이 토큰에는 비밀번호는 담지 않는다 !
        // 이제 클라이언트가 리액트쪽에서 받아서 저장을 해 둔 다음에 다음 요청이 날라오면 토큰을 클라이언트 쪽에서 담아서 요청을 반환단다.
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole());
        claims.put("userEmail", user.getUserEmail());
        return claims;

    }

    /**
     * jwt 서명을 발급해주는 메소드
     * @return key
     *
     */
    private static Key createSignature(){
        // 쉽게 생각해서 우리의 무작위 문자열을 가지고 알고리즘을 사용하여 비밀키를 만든다고 생각.

        // 비밀 키 문자열을 Base64 로 디코딩하여 바이트 배열로 반환
        byte[] secretKey = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        // 변환된 바이트 배열을 알고리즘을 사용해 key 객체로 반환
        // HS256 으로 암호회 후 반환함.
        return new SecretKeySpec(secretKey, SignatureAlgorithm.HS256.getJcaName());
    }
}
