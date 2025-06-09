# Libsodium Java Bindings

[![License](https://img.shields.io/badge/license-MPL-blue.svg)](LICENSE)

Java bindings for the Libsodium cryptographic library, specifically designed to support Global Travel Rule compliance
requirements.

## Features

- Complete Java bindings for Libsodium cryptographic functions
- Support for Travel Rule compliant encryption algorithms
- Simple and intuitive Java API
- Cross-platform support (Windows/Linux/macOS)
- Cross-architecture support (X86/X64/ARM)
- Automatic native library loading

## Quick Start

### Requirements

- Java 8 or higher
- Libsodium 1.0.18+ (automatically handled as dependency)

### Installation

#### Maven

```xml

<project>
    <repositories>
        <repository>
            <id>global-travel-rule-github-public</id>
            <name>Global Travel Rule GitHub Public Packages</name>
            <url>https://maven.pkg.github.com/Global-Travel-Rule/libsodium-java</url>
            <!-- public repository -->
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <!-- globaltravelrule libsodium dependency -->
        <dependency>
            <groupId>com.globaltravelrule.libsodium</groupId>
            <artifactId>libsodium-java</artifactId>
            <version>{LATEST_VERSION}</version>
        </dependency>
    </dependencies>
</project>
```

#### Gradle

```groovy

implementation 'com.globaltravelrule.libsodium:libsodium-java:1.0.0'
```

### Usage

#### Basic Example

```java

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
```

### API Reference

#### Key Generation

- `keygen(method)`: Generates a new key pair for the specified method.
- `crypto_kx_keypair(publicKey, secretKey)`: Generates a new key pair for use with the box functions.
- `crypto_kx_seed_keypair(seed)`: Generate deterministic Key pair.

#### Encryption and Decryption

- `crypto_box_easy(ciphertext, message, messageLength, nonce, publicKey, secretKey)`: Encrypts a message using the
  public key and secret key.
- `crypto_box_open_easy(decryptedMessage, ciphertext, ciphertextLength, nonce, publicKey, secretKey)`: Decrypts a
  message using the public key and secret key.

#### Signatures

- `crypto_sign_keypair(publicKey, secretKey)`: Generates a new key pair for use with the sign functions.
- `crypto_sign_publickey(publicKey, secretKey)`: Extracts the public key from a key pair.
- `crypto_sign_secretkey(publicKey, secretKey)`: Extracts the secret key from a key pair.
- `crypto_sign_detached(signature, message, messageLength, secretKey)`: Creates a detached signature for a message.
- `crypto_sign_verify_detached(signature, message, messageLength, publicKey)`: Verifies a detached signature for a
  message.

#### Utilities

- `crypto_randombytes(length)`: Generates random bytes.
- `crypto_hash(hash, input, inputLength)`: Computes the hash of an input.

### Development

#### Build Project

```shell
  mvn clean package
```

#### Run Tests

```shell
  mvn clean test
```

### Contribution Workflow

1. Fork the repository

2. Create feature branch (git checkout -b feature/xyz)

3. Commit changes (git commit -am 'Add feature xyz')

4. Push to branch (git push origin feature/xyz)

5. Open Pull Request

### License

MPL License - See [LICENSE.md](LICENSE.md) for details.

### Support

- Report issues at: \
  https://github.com/Global-Travel-Rule/libsodium-java/issues
