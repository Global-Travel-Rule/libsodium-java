import com.globaltravelrule.sodium.LazySodium;
import com.globaltravelrule.sodium.LazySodiumJava;
import com.globaltravelrule.sodium.SodiumJava;
import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.Box;
import com.globaltravelrule.sodium.utils.KeyPair;
import com.globaltravelrule.sodium.utils.LibraryLoader;

public class CryptoExample {

    public static void main(String[] args) throws SodiumException {
        // Initialize Sodium
        // load modern native library if available, otherwise use bundled version, recommended for `LibraryLoader.Mode.PREFER_BUNDLED`
        LazySodium sodium = new LazySodiumJava(new SodiumJava(LibraryLoader.Mode.PREFER_BUNDLED));

        // Generate key pair
        KeyPair aliceKp = sodium.cryptoSignSeedKeypair(sodium.randomBytesBuf(Box.SEEDBYTES));
        KeyPair bobKp = sodium.cryptoSignSeedKeypair(sodium.randomBytesBuf(Box.SEEDBYTES));

        // Encrypt message
        String message = "Secure message";
        byte[] noise = sodium.randomBytesBuf(Box.NONCEBYTES);

        // Make sure we have Alice's private key and Bob's public key to encrypt the message
        KeyPair aliceToBobCruve25519KeyPair = sodium.convertKeyPairEd25519ToCurve25519(new KeyPair(bobKp.getPublicKey(), aliceKp.getSecretKey()));
        String aliceToBobSecretKey = sodium.cryptoBoxBeforeNm(aliceToBobCruve25519KeyPair);
        String encrypted = sodium.cryptoBoxEasyAfterNm(message, noise, aliceToBobSecretKey);
        System.out.println("Alice uses her private key to encrypt the message with Bob's public key.");
        System.out.println("Encryption result: " + encrypted);

        // Make sure we have Bob's private key and Alice's public key to decrypt the message
        KeyPair bobFromAliceCurve25519KeyPair = sodium.convertKeyPairEd25519ToCurve25519(new KeyPair(aliceKp.getPublicKey(), bobKp.getSecretKey()));
        String bobFromAliceSecretKey = sodium.cryptoBoxBeforeNm(bobFromAliceCurve25519KeyPair);
        String decrypted = sodium.cryptoBoxOpenEasyAfterNm(encrypted, noise, bobFromAliceSecretKey);
        System.out.println("Bob uses his private key to decrypt the message with Alice's public key.");
        System.out.println("Decryption result: " + decrypted);
    }
}