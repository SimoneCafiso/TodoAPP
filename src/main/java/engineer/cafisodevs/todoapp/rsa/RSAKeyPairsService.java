package engineer.cafisodevs.todoapp.rsa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class RSAKeyPairsService {


    private final KeyFactory keyFactory;


    /**
     * Constructor to initialize the KeyFactory RSA instance
     * @throws NoSuchAlgorithmException
     */


    public RSAKeyPairsService() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance("RSA");
    }


    /**
     * Load the public key from the classpath
     * @return RSAPublicKey object
     * @throws Exception
     */

    public RSAPublicKey loadPublicKey() throws Exception {

        java.security.Security.addProvider(new BouncyCastleProvider());

        ClassPathResource pkResource = new ClassPathResource("keypair/public.key");

        Path path = Paths.get(pkResource.getURI());

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            byte[] publicKeyBytes = new PemReader(reader).readPemObject().getContent(); // Uses BouncyCastle to read the PEM file
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        }
    }

    /**
     * Load the private key from the classpath
     * @return RSAPrivateKey object
     * @throws Exception
     */
    public RSAPrivateKey loadPrivateKey() throws Exception {

        java.security.Security.addProvider(new BouncyCastleProvider());

        ClassPathResource pkResource = new ClassPathResource("keypair/private.pkcs8.key");

        Path path = Paths.get(pkResource.getURI());

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            byte[] privateKeyBytes = new PemReader(reader).readPemObject().getContent(); // Uses BouncyCastle to read the PEM file
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        }
    }


}
