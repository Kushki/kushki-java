package com.kushkipagos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by lmunda on 12/2/15 10:31.
 * sent by: Ashish Mandloi (amandloi@aurusinc.com)
 */
public class Encryption {

    private final Cipher cipher;

    public Encryption() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String publickey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtCBqxKQo4DgvZLk5CMDFqgRdCrW10GBcbcrh5b3jicGzU4PIoNEx/g4YNmj5jD6/BEYkHp6aAXMUtn/MWzd+P+dIUvckvf4hn9IMV1gtBYkfbVcfcB37Q65eTYNCkjryWpxW4TMJ5cbjD9t0ywTU5txBdQBRYxyp7YXu+EGPpk1u1rcYhJu+27xA+VKqEi5VxL73qA//EBQRdFYoqhWwNVLeKenyoQo8OtoF7wMgM+BSK0kVsvFQP6/OJpnlGPxTOIws34Yw50TYe15w+ueGLEtQmmFAHPFkEyGTXhczBoOr2obPuJmno3JKPGBs48xXckazyotRC7B4ymCJI3fvvwIDAQAB";
        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(publickey));
    }

    public String encryptMessageChunk(String requestMessage) throws BadPaddingException, IllegalBlockSizeException {
        byte[] cipherData;
        String chnkString;
        String[] msgChunks = requestMessage.split("(?<=\\G.{214})");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        StringBuilder buf = new StringBuilder();
        for (String msgChunk : msgChunks) {
            cipherData = encrypt(msgChunk);
            chnkString = base64Encoder.encodeBuffer(cipherData);
            chnkString = chnkString.replace("\n", "");
            buf.append(chnkString);
            buf.append("<FS>");
        }
        return buf.toString();
    }

    public byte[] encrypt(String message) throws BadPaddingException, IllegalBlockSizeException {
        byte[] eMessageBytes = message.getBytes(Charset.forName("UTF-8"));       // Request message conversion to Byte array
        return cipher.doFinal(eMessageBytes);
    }

    public final PublicKey loadPublicKey(String stored) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] data = base64Decoder.decodeBuffer(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }
}
