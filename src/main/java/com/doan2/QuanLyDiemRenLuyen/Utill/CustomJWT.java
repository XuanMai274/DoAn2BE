package com.doan2.QuanLyDiemRenLuyen.Utill;

import com.doan2.QuanLyDiemRenLuyen.DTO.AuthenticationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomJWT {
    // tạo khóa bí mật
    private final String SECRET_KEY="bookingcare";
    // thời gian hết hạn của token
    private final long ACCESS_TOKEN_EXPIRY=60*1000;// 1 phút
    // thời gian hết hạn của refresh token
    private final long REFRESH_TOKEN=5*60*1000;// 5 phút

    // hàm mã hóa chuỗi thành base54
    public String encodeBase64(String input){
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    // Hàm tạo chữ kí HMAC SHA256( nhận vào một chuỗi đã được mã hóa,một secret_key
    public String createHMACSignature(String headerPayloadEncodeBase64,String secretKey){
        // tạo khóa bí mật thông qua class SecretKeySpec
        // nhận vào một mảng byte(theo bảng mã UTF-8) của khóa bí mật và tên thuật toán để tạo khóa
        // tạo SecretKeySpec cho HAC không nhận vào chuỗi mà nhân vào một đối tượng để bọc khóa bí mật
        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(secretKeySpec);
            // gọi hàm băm doFinal để băm (header + payload của JWT đã được encode Base64) cùng với khóa bí mật(SecretKeySpec)
            byte[] hash= hmac.doFinal(headerPayloadEncodeBase64.getBytes());
            //chuyển đổi giá trị hash (mảng byte) thành một chuỗi Base64Url vì chữ ký phải ở dạng chuỗi Base64Url, không hỗ trợ định dạng byte nên cần phải chuyển về
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    // hàm tạo token
    public AuthenticationResponse generateToken(String userName,int id,String role) throws Exception{
        // tạo header dưới dạng chuỗi json
        String headerJson="{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        // mã hóa header thành dạng base64
        String encodeHeader=encodeBase64(headerJson);
        ZoneId vietnamZone = ZoneId.of("Asia/Saigon");

        // Lấy thời gian hiện tại (Issued At) theo múi giờ UTC
        LocalDateTime issuedAt = LocalDateTime.now(ZoneOffset.UTC);
        // Chuyển thời gian `issuedAt` và `expirationTime` sang giờ Việt Nam (UTC+7)( Đây là thời gian bắt đầu của token và access token)
        LocalDateTime issuedAtVietnam = issuedAt.atZone(ZoneOffset.UTC).withZoneSameInstant(vietnamZone).toLocalDateTime();
        // Thêm 180 giây để tính thời gian hết hạn (Expiration) theo múi giờ UTC ( đây là thời gian hết hạn của token)
        LocalDateTime expirationTime = LocalDateTime.now(ZoneOffset.UTC).plus(1800, ChronoUnit.SECONDS);
        LocalDateTime expirationTimeVietnam = expirationTime.atZone(ZoneOffset.UTC).withZoneSameInstant(vietnamZone).toLocalDateTime();
        // tạo thời gian hết hạn cho refresh token
        LocalDateTime expirationTimeRefreshToken = LocalDateTime.now(ZoneOffset.UTC).plus(3600, ChronoUnit.SECONDS);
        LocalDateTime expirationTimeVietnamRefreshToken = expirationTimeRefreshToken.atZone(ZoneOffset.UTC).withZoneSameInstant(vietnamZone).toLocalDateTime();
        // Chuyển đổi thời gian sang giây (epoch second) cho `iat` và `exp` theo giờ Việt Nam
        long iat = issuedAtVietnam.atZone(vietnamZone).toEpochSecond();
        // cho access token
        long exp = expirationTimeVietnam.atZone(vietnamZone).toEpochSecond();
        // cho refresh token
        long expRefreshToken =expirationTimeVietnamRefreshToken.atZone(vietnamZone).toEpochSecond();
        // tạo payload cho access token thông qua Map
        Map<String, Object> claimsAccessToken=new HashMap<>();
        claimsAccessToken.put("userName",userName);
        claimsAccessToken.put("iat",iat);
        claimsAccessToken.put("exp",exp);
        claimsAccessToken.put("id",id);
        claimsAccessToken.put("role",role);
        // chuyển claims v kiểu json
        String payloadJson=new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(claimsAccessToken);
        // mã hóa sang base64
        String encodedPayload=encodeBase64(payloadJson);
        Map<String, Object> claimsRefresh = new HashMap<>();
        claimsRefresh.put("userName", userName);
        claimsRefresh.put("iat",iat); // thời điểm bắt đầu refresh token
        claimsRefresh.put("exp", expRefreshToken); // thời gian hết hạn của refresh token
        claimsRefresh.put("id", id);
        claimsAccessToken.put("role",role);
        String payloadJsonRefresh = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(claimsRefresh);
        String encodedPayloadRefresh = encodeBase64(payloadJsonRefresh);
        // tạo khóa cho access token
        // Signature (Header + Payload)  cho access token
        String signatureInput = encodeHeader + "." + encodedPayload;
        String signatureAccess = createHMACSignature(signatureInput,SECRET_KEY);
        // tạo khóa cho refresh token
        String signatureInputRefresh=encodeHeader+"."+encodedPayloadRefresh;
        String signatureRefresh = createHMACSignature(signatureInputRefresh,SECRET_KEY);
        // trả v 2 khóa này
        AuthenticationResponse authentication=new AuthenticationResponse();
        authentication.setAccessToken(encodeHeader + "." + encodedPayload + "."+ signatureAccess);
        authentication.setRefreshToken(encodeHeader + "." + encodedPayloadRefresh + "."+ signatureRefresh);
        return authentication;
    }
    public String refreshToken(String userName, int id,String role) throws JsonProcessingException {
        String headerJson="{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String encodeHeader=encodeBase64(headerJson);
        ZoneId vietnamZone = ZoneId.of("Asia/Saigon");
        LocalDateTime issuedAt = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime issuedAtVietnam = issuedAt.atZone(ZoneOffset.UTC).withZoneSameInstant(vietnamZone).toLocalDateTime();
        LocalDateTime expirationTime = LocalDateTime.now(ZoneOffset.UTC).plus(120, ChronoUnit.SECONDS);
        LocalDateTime expirationTimeVietnam = expirationTime.atZone(ZoneOffset.UTC).withZoneSameInstant(vietnamZone).toLocalDateTime();
        long iat = issuedAtVietnam.atZone(vietnamZone).toEpochSecond();
        long exp = expirationTimeVietnam.atZone(vietnamZone).toEpochSecond();
        Map<String, Object> claimsAccessToken=new HashMap<>();
        claimsAccessToken.put("userName",userName);
        claimsAccessToken.put("iat",iat);
        claimsAccessToken.put("exp",exp);
        claimsAccessToken.put("id",id);
        claimsAccessToken.put("role",role);
        String payloadJson=new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(claimsAccessToken);
        String encodedPayload=encodeBase64(payloadJson);
        String signatureInput = encodeHeader + "." + encodedPayload;
        String signatureAccess = createHMACSignature(signatureInput,SECRET_KEY);
        return encodeHeader + "." + encodedPayload + "."+ signatureAccess;
    }
    // kiểm tra xem token có hợp lệ hay không
    public int validateToken(String token) throws Exception{
        // giải mã token được truyền vào thành 3 phần phân tách nhau bằng dấu . ( lí do có \\ là vì dâu . trong regex là một kí tự đặc biệt
        // tức là mọi giá trị đều bằng dấu chấm nên phải dùng \\ để chỉ rằng dấu chấm là 1 kí tự bình thường)
        String[] parts = token.split("\\.");
        // kiểm tra token truyền vào có đủ 3 thành phần:header, payload,signature
        if(parts.length<3){
            return 400;
        }
        // lấy head và payload=parts[0] + "." + parts[1];
        String headAndPayload=parts[0] + "." + parts[1];
        String signature=parts[2];
        // tạo lại chữ kí từ headAndPayload
        String expectedSignature=createHMACSignature(headAndPayload,SECRET_KEY);
        // kiểm tra khóa vừa tạo và khóa gốc có bằng nhau không
        if(expectedSignature.equals(signature)){
            // kiểm tra thời hạn
            //giải mã payload để lấy exp từ token so sánh với thời gian hiện tại
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Chuyển đổi JSON thành Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);
            long expirationTime = ((Number) claims.get("exp")).longValue();
            LocalDateTime expirationDateTime = LocalDateTime.ofEpochSecond(expirationTime, 0, ZoneId.of("Asia/Ho_Chi_Minh").getRules().getOffset(LocalDateTime.now()));
            // Hiển thị thời gian hết hạn theo định dạng yêu cầu
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss");
            String formattedExpirationTime = expirationDateTime.format(formatter);
            // System.out.println("Expiration Time (formatted): " + formattedExpirationTime);
            // Lấy thời gian hiện tại theo giờ Việt Nam
            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            if (currentDateTime.isBefore(expirationDateTime)) {
                return 200;
            }
            else{
                return 401;
            }

        }else{
            return 400;
        }
    }


    public String extractUserName(String token){
        try{
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            // System.out.println("extractUsername: "+payloadMap.get("sub").toString());
            return payloadMap.get("userName").toString();
        }catch(Exception e){
            return null;
        }
    }
    public String extractUserId(String token){
        try{
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            // System.out.println("extractUsername: "+payloadMap.get("sub").toString());
            return payloadMap.get("id").toString();
        }catch(Exception e){
            return null;
        }
    }
    public String extractRole(String token){
        try {
            // chuyển từ base64 sang chuỗi bình thường
            String payload=new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            ObjectMapper objectMappe=new ObjectMapper();
            Map<String,Object> payloadMap=objectMappe.readValue(payload,Map.class);
            return payloadMap.get("role").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}