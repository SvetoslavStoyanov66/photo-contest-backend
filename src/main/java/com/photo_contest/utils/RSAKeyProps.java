package com.photo_contest.utils;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Getter
@Setter
public class RSAKeyProps {

//    private RSAPublicKey publicKey;
//    private RSAPrivateKey privateKey;
//
//    public RSAKeyProps() {
//   /*
//        Used when no static keys are present
//        DD
// */
//        KeyPair keyPair = KeyGeneratorUtil.generateRsaKeyPair();
//        this.publicKey = (RSAPublicKey) keyPair.getPublic();
//        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//    }


        private RSAPublicKey publicKey;
        private RSAPrivateKey privateKey;

        public RSAKeyProps() throws Exception {
            /*
            Used when static keys are present
             */
            Path keyDir = Paths.get("/app/keys");  // matches your Dockerfile
            byte[] privateKeyBytes = Files.readAllBytes(keyDir.resolve("privateKey.pem"));
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);


            byte[] publicKeyBytes = Files.readAllBytes(keyDir.resolve("publicKey.pem"));
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
        }
}
