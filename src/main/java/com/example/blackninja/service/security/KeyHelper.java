package com.example.blackninja.service.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyHelper {

    private final RsaSigner signer;
    private final RsaVerifier verifier;

    public RsaSigner getSigner() {
        return signer;
    }

    public RsaVerifier getVerifier() {
        return verifier;
    }

    public KeyHelper() throws GeneralSecurityException, IOException {
        RSAPrivateKey rsaPrivateKey = getPrivateKeyFromString(loadKey("sample_keys/pkcs8.pem"));
        RSAPublicKey rsaPublicKey = getPublicKeyFromString(loadKey("sample_keys/rsa.pub"));
        signer = new RsaSigner(rsaPrivateKey);
        verifier = new RsaVerifier(rsaPublicKey);
    }

    private String loadKey(String name) {
        try {
            ClassPathResource resource = new ClassPathResource(name);
            String content = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
            return RegExUtils.replaceAll(content, "\\r", ""); // remove CR when checked out on Windows.
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line + "\n";
        }
        br.close();
        return strKeyPEM;
    }

    private RSAPrivateKey getPrivateKey(String filename) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getKey(filename);
        return getPrivateKeyFromString(privateKeyPEM);
    }

    private RSAPrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    private RSAPublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
        String publicKeyPEM = getKey(filename);
        return getPublicKeyFromString(publicKeyPEM);
    }

    private RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }
}
