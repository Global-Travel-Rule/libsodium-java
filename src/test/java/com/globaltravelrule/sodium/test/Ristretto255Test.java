/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:53
 */

package com.globaltravelrule.sodium.test;


import com.globaltravelrule.sodium.LazySodium;
import com.globaltravelrule.sodium.LazySodiumJava;
import com.globaltravelrule.sodium.SodiumJava;
import com.globaltravelrule.sodium.exceptions.SodiumException;
import com.globaltravelrule.sodium.interfaces.Ristretto255;
import com.globaltravelrule.sodium.interfaces.Ristretto255.RistrettoPoint;
import com.globaltravelrule.sodium.utils.Base64MessageEncoder;
import com.globaltravelrule.sodium.utils.HexMessageEncoder;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class Ristretto255Test extends BaseTest {

    @Test
    public void scalarConversions() {
        BigInteger n = BigInteger.valueOf(1);
        BigInteger m = Ristretto255.bytesToScalar(Ristretto255.scalarToBytes(n));
        assertEquals(n, m);

        m = Ristretto255.bytesToScalar(Ristretto255.scalarToBytes(n, false));
        assertEquals(n, m);

        n = BigInteger.valueOf(12345678);
        m = Ristretto255.bytesToScalar(Ristretto255.scalarToBytes(n));
        assertEquals(n, m);

        m = Ristretto255.bytesToScalar(Ristretto255.scalarToBytes(n, false));
        assertEquals(n, m);
    }

    @Test
    public void randomPoint() {
        // This should not throw
        RistrettoPoint p = RistrettoPoint.random(lazySodium);
        RistrettoPoint q = RistrettoPoint.random(lazySodium);

        // Random points should be non-equal
        assertNotEquals(p, q);
    }

    @Test
    public void invalidPoint() {
        // Test vectors from https://ristretto.group/test_vectors/ristretto255.html
        String[] badEncodings = new String[]{
            // These are all bad because they're non-canonical field encodings.
            "00ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
            "f3ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
            "edffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
            // These are all bad because they're negative field elements.
            "0100000000000000000000000000000000000000000000000000000000000000",
            "01ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
            "ed57ffd8c914fb201471d1c3d245ce3c746fcbe63a3679d51b6a516ebebe0e20",
            "c34c4e1826e5d403b78e246e88aa051c36ccf0aafebffe137d148a2bf9104562",
            "c940e5a4404157cfb1628b108db051a8d439e1a421394ec4ebccb9ec92a8ac78",
            "47cfc5497c53dc8e61c91d17fd626ffb1c49e2bca94eed052281b510b1117a24",
            "f1c6165d33367351b0da8f6e4511010c68174a03b6581212c71c0e1d026c3c72",
            "87260f7a2f12495118360f02c26a470f450dadf34a413d21042b43b9d93e1309",
            // These are all bad because they give a nonsquare x^2.
            "26948d35ca62e643e26a83177332e6b6afeb9d08e4268b650f1f5bbd8d81d371",
            "4eac077a713c57b4f4397629a4145982c661f48044dd3f96427d40b147d9742f",
            "de6a7b00deadc788eb6b6c8d20c0ae96c2f2019078fa604fee5b87d6e989ad7b",
            "bcab477be20861e01e4a0e295284146a510150d9817763caf1a6f4b422d67042",
            "2a292df7e32cababbd9de088d1d1abec9fc0440f637ed2fba145094dc14bea08",
            "f4a9e534fc0d216c44b218fa0c42d99635a0127ee2e53c712f70609649fdff22",
            "8268436f8c4126196cf64b3c7ddbda90746a378625f9813dd9b8457077256731",
            "2810e5cbc2cc4d4eece54f61c6f69758e289aa7ab440b3cbeaa21995c2f4232b",
            // These are all bad because they give a negative xy value.
            "3eb858e78f5a7254d8c9731174a94f76755fd3941c0ac93735c07ba14579630e",
            "a45fdc55c76448c049a1ab33f17023edfb2be3581e9c7aade8a6125215e04220",
            "d483fe813c6ba647ebbfd3ec41adca1c6130c2beeee9d9bf065c8d151c5f396e",
            "8a2e1d30050198c65a54483123960ccc38aef6848e1ec8f5f780e8523769ba32",
            "32888462f8b486c68ad7dd9610be5192bbeaf3b443951ac1a8118419d9fa097b",
            "227142501b9d4355ccba290404bde41575b037693cef1f438c47f8fbf35d1165",
            "5c37cc491da847cfeb9281d407efc41e15144c876e0170b499a96a22ed31e01e",
            "445425117cb8c90edcbc7c1cc0e74f747f2c1efa5630a967c64f287792a48a4b",
            // This is s = -1, which causes y = 0.
            "ecffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"
        };

        for (String badEncoding : badEncodings) {
            assertFalse(lazySodium.cryptoCoreRistretto255IsValidPoint(badEncoding));

            try {
                // Calling RistrettoPoint.fromHex with a bad encoding should throw
                RistrettoPoint p = RistrettoPoint.fromHex(lazySodium, badEncoding);
                fail(p.toHex());
            } catch (IllegalArgumentException e) {
                // expected
            }
        }

        // Wrong length
        byte[] invalidPoint = new byte[42];
        String invalidPointHex = "01";
        assertFalse(lazySodium.cryptoCoreRistretto255IsValidPoint(invalidPoint));
        assertFalse(lazySodium.cryptoCoreRistretto255IsValidPoint(invalidPointHex));

        try {
            // Calling RistrettoPoint.fromBytes with a bad encoding should throw
            RistrettoPoint p = RistrettoPoint.fromBytes(lazySodium, invalidPoint);
            fail(p.toHex());
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            // Calling RistrettoPoint.fromHex with a bad encoding should throw
            RistrettoPoint p = RistrettoPoint.fromHex(lazySodium, invalidPointHex);
            fail(p.toHex());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void fromHash() throws Exception {
        byte[] hash = new byte[64];
        new Random().nextBytes(hash);

        RistrettoPoint p = lazySodium.cryptoCoreRistretto255FromHash(hash);

        String hashHex = LazySodium.toHex(hash);
        RistrettoPoint q = lazySodium.cryptoCoreRistretto255FromHash(hashHex);

        assertEquals(p, q);

        byte[] differentHash = new byte[64];
        RistrettoPoint different = lazySodium.cryptoCoreRistretto255FromHash(differentHash);
        assertNotEquals(p, different);

        byte[] invalidHash = new byte[42];
        try {
            lazySodium.cryptoCoreRistretto255FromHash(invalidHash);
            fail("Should throw");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test vectors from https://ristretto.group/test_vectors/ristretto255.html
        String[] testInput = new String[]{
            "Ristretto is traditionally a short shot of espresso coffee",
            "made with the normal amount of ground coffee but extracted with",
            "about half the amount of water in the same amount of time",
            "by using a finer grind.",
            "This produces a concentrated shot of coffee per volume.",
            "Just pulling a normal shot short will produce a weaker shot",
            "and is not a Ristretto as some believe."
        };

        String[] expectedPoint = new String[]{
            "3066f82a1a747d45120d1740f14358531a8f04bbffe6a819f86dfe50f44a0a46",
            "f26e5b6f7d362d2d2a94c5d0e7602cb4773c95a2e5c31a64f133189fa76ed61b",
            "006ccd2a9e6867e6a2c5cea83d3302cc9de128dd2a9a57dd8ee7b9d7ffe02826",
            "f8f0c87cf237953c5890aec3998169005dae3eca1fbb04548c635953c817f92a",
            "ae81e7dedf20a497e10c304a765c1767a42d6e06029758d2d7e8ef7cc4c41179",
            "e2705652ff9f5e44d3e841bf1c251cf7dddb77d140870d1ab2ed64f1a9ce8628",
            "80bd07262511cdde4863f8a7434cef696750681cb9510eea557088f76d9e5065"
        };

        for (int i = 0; i < testInput.length; ++i) {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");

            byte[] hashed = sha512.digest(testInput[i].getBytes(StandardCharsets.UTF_8));
            RistrettoPoint encoded = lazySodium.cryptoCoreRistretto255FromHash(hashed);

            RistrettoPoint alternative = RistrettoPoint.hashToPoint(lazySodium,
                lazySodium.encodeToString(testInput[i].getBytes(StandardCharsets.UTF_8)));

            assertEquals(RistrettoPoint.fromHex(lazySodium, expectedPoint[i]), encoded);
            assertEquals(encoded, alternative);
        }
    }

    @Test
    public void scalarMultOne() throws Exception {
        // When multiplying with 1, the point should not change
        BigInteger one = BigInteger.ONE;

        // scalar encoding is little-endian
        String oneHex =
            "01" + IntStream.range(0, 31).mapToObj(i -> "00").collect(Collectors.joining());
        byte[] oneBytes = Ristretto255.scalarBuffer();
        oneBytes[0] = 1;

        RistrettoPoint point = lazySodium.cryptoCoreRistretto255Random();
        RistrettoPoint res1 = lazySodium.cryptoScalarmultRistretto255(one, point);
        RistrettoPoint res2 = point.times(one);
        assertEquals(res1, res2);
        assertEquals(point, res1);

        res1 = lazySodium.cryptoScalarmultRistretto255(oneHex, point);
        assertEquals(point, res1);
        res1 = lazySodium.cryptoScalarmultRistretto255(oneBytes, point);
        assertEquals(point, res1);
    }

    @Test
    public void scalarMultZero() {
        // When multiplying with 0, it should throw
        BigInteger zero = BigInteger.ZERO;

        // scalar encoding is little-endian
        String zeroHex = IntStream.range(0, 32).mapToObj(i -> "00").collect(Collectors.joining());
        byte[] zeroBytes = Ristretto255.scalarBuffer();
        RistrettoPoint point = lazySodium.cryptoCoreRistretto255Random();

        try {
            RistrettoPoint res = lazySodium.cryptoScalarmultRistretto255(zero, point);
            fail(res.toHex());
        } catch (SodiumException e) {
            // expected
        }

        try {
            RistrettoPoint res = point.times(zero);
            fail(res.toHex());
        } catch (SodiumException e) {
            // expected
        }

        try {
            RistrettoPoint res = lazySodium.cryptoScalarmultRistretto255(zeroHex, point);
            fail(res.toHex());
        } catch (SodiumException e) {
            // expected
        }
        try {
            RistrettoPoint res = lazySodium.cryptoScalarmultRistretto255(zeroBytes, point);
            fail(res.toHex());
        } catch (SodiumException e) {
            // expected
        }
    }

    @Test
    public void scalarMultBase() throws Exception {
        // Test vectors from https://ristretto.group/test_vectors/ristretto255.html
        String[] expected = new String[]{
            // This is the basepoint
            "e2f2ae0a6abc4e71a884a961c500515f58e30b6aa582dd8db6a65945e08d2d76",
            // These are small multiples of the basepoint
            "6a493210f7499cd17fecb510ae0cea23a110e8d5b901f8acadd3095c73a3b919",
            "94741f5d5d52755ece4f23f044ee27d5d1ea1e2bd196b462166b16152a9d0259",
            "da80862773358b466ffadfe0b3293ab3d9fd53c5ea6c955358f568322daf6a57",
            "e882b131016b52c1d3337080187cf768423efccbb517bb495ab812c4160ff44e",
            "f64746d3c92b13050ed8d80236a7f0007c3b3f962f5ba793d19a601ebb1df403",
            "44f53520926ec81fbd5a387845beb7df85a96a24ece18738bdcfa6a7822a176d",
            "903293d8f2287ebe10e2374dc1a53e0bc887e592699f02d077d5263cdd55601c",
            "02622ace8f7303a31cafc63f8fc48fdc16e1c8c8d234b2f0d6685282a9076031",
            "20706fd788b2720a1ed2a5dad4952b01f413bcf0e7564de8cdc816689e2db95f",
            "bce83f8ba5dd2fa572864c24ba1810f9522bc6004afe95877ac73241cafdab42",
            "e4549ee16b9aa03099ca208c67adafcafa4c3f3e4e5303de6026e3ca8ff84460",
            "aa52e000df2e16f55fb1032fc33bc42742dad6bd5a8fc0be0167436c5948501f",
            "46376b80f409b29dc2b5f6f0c52591990896e5716f41477cd30085ab7f10301e",
            "e0c418f7c8d9c4cdd7395b93ea124f3ad99021bb681dfc3302a9d99a2e53e64e",
        };

        RistrettoPoint base = RistrettoPoint.base(lazySodium);
        assertEquals(RistrettoPoint.fromHex(lazySodium, expected[0]), base);

        for (byte i = 1; i <= 15; ++i) {
            BigInteger n = BigInteger.valueOf(i);

            // scalar encoding is little-endian
            String nHex = String.format("%02x", i) + IntStream.range(0, 31).mapToObj(j -> "00")
                                                         .collect(Collectors.joining());
            byte[] nBytes = Ristretto255.scalarBuffer();
            nBytes[0] = i;

            RistrettoPoint res1 = lazySodium.cryptoScalarmultRistretto255Base(n);
            RistrettoPoint res2 = lazySodium.cryptoScalarmultRistretto255Base(nHex);
            RistrettoPoint res3 = lazySodium.cryptoScalarmultRistretto255Base(nBytes);
            RistrettoPoint res4 = base.times(n);

            RistrettoPoint expectedPoint = RistrettoPoint.fromHex(lazySodium, expected[i - 1]);

            assertEquals(expectedPoint, res1);
            assertEquals(res1, res2);
            assertEquals(res2, res3);
            assertEquals(res3, res4);
        }
    }

    @Test
    public void addSub() throws Exception {
        final RistrettoPoint ZERO = RistrettoPoint.zero(lazySodium);

        RistrettoPoint p = RistrettoPoint.random(lazySodium);
        RistrettoPoint sum1 = p.plus(ZERO);
        RistrettoPoint sum2 = ZERO.plus(p);
        RistrettoPoint diff1 = p.minus(ZERO);
        RistrettoPoint diff2 = ZERO.minus(p);

        assertEquals(p, sum1);
        assertEquals(sum1, sum2);
        assertEquals(sum1, diff1);
        assertEquals(ZERO, p.plus(diff2));
        assertEquals(p.negate(), diff2);
        assertEquals(ZERO, p.plus(p.negate()));
    }

    @Test
    public void randomScalar() {
        BigInteger s1 = lazySodium.cryptoCoreRistretto255ScalarRandom();
        BigInteger s2 = lazySodium.cryptoCoreRistretto255ScalarRandom();
        BigInteger s3 = lazySodium.cryptoCoreRistretto255ScalarRandom();

        // all three scalars should be positive and non-equal
        assertFalse(s1.compareTo(BigInteger.ZERO) < 0);
        assertFalse(s2.compareTo(BigInteger.ZERO) < 0);
        assertFalse(s3.compareTo(BigInteger.ZERO) < 0);

        assertNotEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertNotEquals(s2, s3);
    }

    @Test
    public void reduceScalar() {
        // Those should be reduced to exactly x - L
        BigInteger oneModL = Ristretto255.RISTRETTO255_L.add(BigInteger.ONE);
        BigInteger tenModL = Ristretto255.RISTRETTO255_L.add(BigInteger.TEN);

        BigInteger rand = lazySodium.cryptoCoreRistretto255ScalarRandom();
        BigInteger randModL = Ristretto255.RISTRETTO255_L.add(rand);

        // Those should be reduced to 0
        BigInteger lTimes2 = Ristretto255.RISTRETTO255_L.multiply(BigInteger.valueOf(2));
        BigInteger lTimes42 = Ristretto255.RISTRETTO255_L.multiply(BigInteger.valueOf(42));

        assertEquals(BigInteger.ONE, lazySodium.cryptoCoreRistretto255ScalarReduce(oneModL));
        assertEquals(BigInteger.TEN, lazySodium.cryptoCoreRistretto255ScalarReduce(tenModL));
        assertEquals(rand, lazySodium.cryptoCoreRistretto255ScalarReduce(randModL));

        assertEquals(BigInteger.ZERO, lazySodium.cryptoCoreRistretto255ScalarReduce(lTimes2));
        assertEquals(BigInteger.ZERO, lazySodium.cryptoCoreRistretto255ScalarReduce(lTimes42));

        // Scalars within [0, L[ should not be reduced
        assertEquals(BigInteger.ZERO,
            lazySodium.cryptoCoreRistretto255ScalarReduce(BigInteger.ZERO));
        assertEquals(BigInteger.ONE, lazySodium.cryptoCoreRistretto255ScalarReduce(BigInteger.ONE));
        assertEquals(BigInteger.TEN, lazySodium.cryptoCoreRistretto255ScalarReduce(BigInteger.TEN));
        assertEquals(rand, lazySodium.cryptoCoreRistretto255ScalarReduce(rand));

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarReduce(invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }

        // little-endian hex strings should be accepted
        String validHex = "01" + IntStream.range(0, 63).mapToObj(i -> "00")
                                     .collect(Collectors.joining());
        assertEquals(BigInteger.ONE, lazySodium.cryptoCoreRistretto255ScalarReduce(validHex));
    }

    @Test
    public void scalarInvert() throws Exception {
        for (int i = 0; i < 50; ++i) {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarRandom();
            byte[] sBytes = Ristretto255.scalarToBytes(s);
            String sHex = lazySodium.toHexStr(sBytes);

            BigInteger sInv = lazySodium.cryptoCoreRistretto255ScalarInvert(s);
            BigInteger sInvBytes = lazySodium.cryptoCoreRistretto255ScalarInvert(sBytes);
            BigInteger sInvHex = lazySodium.cryptoCoreRistretto255ScalarInvert(sHex);

            assertEquals(BigInteger.ONE, s.multiply(sInv).mod(Ristretto255.RISTRETTO255_L));
            assertEquals(sInv, sInvBytes);
            assertEquals(sInv, sInvHex);
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarInvert(invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }

        // 0 can't be inverted
        try {
            BigInteger i = lazySodium.cryptoCoreRistretto255ScalarInvert(BigInteger.ZERO);
            fail(i.toString());
        } catch (SodiumException e) {
            // expected
        }
    }

    @Test
    public void scalarNegate() {
        for (int i = 0; i < 50; ++i) {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarRandom();
            byte[] sBytes = Ristretto255.scalarToBytes(s);
            String sHex = lazySodium.toHexStr(sBytes);

            BigInteger sNeg = lazySodium.cryptoCoreRistretto255ScalarNegate(s);
            BigInteger sNegBytes = lazySodium.cryptoCoreRistretto255ScalarNegate(sBytes);
            BigInteger sNegHex = lazySodium.cryptoCoreRistretto255ScalarNegate(sHex);

            assertEquals(BigInteger.ZERO, s.add(sNeg).mod(Ristretto255.RISTRETTO255_L));
            assertEquals(sNeg, sNegBytes);
            assertEquals(sNeg, sNegHex);
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarNegate(invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void scalarComplement() {
        for (int i = 0; i < 50; ++i) {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarRandom();
            byte[] sBytes = Ristretto255.scalarToBytes(s);
            String sHex = lazySodium.toHexStr(sBytes);

            BigInteger sComp = lazySodium.cryptoCoreRistretto255ScalarComplement(s);
            BigInteger sCompBytes = lazySodium.cryptoCoreRistretto255ScalarComplement(sBytes);
            BigInteger sCompHex = lazySodium.cryptoCoreRistretto255ScalarComplement(sHex);

            assertEquals(BigInteger.ONE, s.add(sComp).mod(Ristretto255.RISTRETTO255_L));
            assertEquals(sComp, sCompBytes);
            assertEquals(sComp, sCompHex);
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarNegate(invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void scalarAdd() {
        for (int i = 0; i < 50; ++i) {
            BigInteger s1 = lazySodium.cryptoCoreRistretto255ScalarRandom();
            BigInteger s2 = lazySodium.cryptoCoreRistretto255ScalarRandom();

            BigInteger expected = s1.add(s2).mod(Ristretto255.RISTRETTO255_L);

            byte[] s1Bytes = Ristretto255.scalarToBytes(s1);
            byte[] s2Bytes = Ristretto255.scalarToBytes(s2);

            String s1Hex = lazySodium.toHexStr(s1Bytes);
            String s2Hex = lazySodium.toHexStr(s2Bytes);

            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Bytes, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Hex, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Bytes, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Hex, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Hex, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarAdd(s1Bytes, s2Bytes));
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarAdd(invalidHex, BigInteger.ONE);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarAdd(BigInteger.ONE, invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void scalarSub() {
        for (int i = 0; i < 50; ++i) {
            BigInteger s1 = lazySodium.cryptoCoreRistretto255ScalarRandom();
            BigInteger s2 = lazySodium.cryptoCoreRistretto255ScalarRandom();

            BigInteger expected = s1.subtract(s2).mod(Ristretto255.RISTRETTO255_L);

            byte[] s1Bytes = Ristretto255.scalarToBytes(s1);
            byte[] s2Bytes = Ristretto255.scalarToBytes(s2);

            String s1Hex = lazySodium.toHexStr(s1Bytes);
            String s2Hex = lazySodium.toHexStr(s2Bytes);

            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Bytes, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Hex, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Bytes, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Hex, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Hex, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarSub(s1Bytes, s2Bytes));
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarSub(invalidHex, BigInteger.ONE);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarSub(BigInteger.ONE, invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void scalarMul() {
        for (int i = 0; i < 50; ++i) {
            BigInteger s1 = lazySodium.cryptoCoreRistretto255ScalarRandom();
            BigInteger s2 = lazySodium.cryptoCoreRistretto255ScalarRandom();

            BigInteger expected = s1.multiply(s2).mod(Ristretto255.RISTRETTO255_L);

            byte[] s1Bytes = Ristretto255.scalarToBytes(s1);
            byte[] s2Bytes = Ristretto255.scalarToBytes(s2);

            String s1Hex = lazySodium.toHexStr(s1Bytes);
            String s2Hex = lazySodium.toHexStr(s2Bytes);

            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Bytes, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Hex, s2));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Bytes, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Hex, s2Bytes));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Hex, s2Hex));
            assertEquals(expected, lazySodium.cryptoCoreRistretto255ScalarMul(s1Bytes, s2Bytes));
        }

        // Invalid scalar hex strings should not be accepted
        // too short
        String invalidHex = "01";
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarMul(invalidHex, BigInteger.ONE);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            BigInteger s = lazySodium.cryptoCoreRistretto255ScalarMul(BigInteger.ONE, invalidHex);
            fail(s.toString());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void encoders() {
        LazySodiumJava lsHex = new LazySodiumJava(new SodiumJava(), new HexMessageEncoder());
        LazySodiumJava lsBase64 = new LazySodiumJava(new SodiumJava(), new Base64MessageEncoder());

        RistrettoPoint randomPoint = RistrettoPoint.random(lazySodium);

        RistrettoPoint hexPoint = RistrettoPoint.fromBytes(lsHex, randomPoint.toBytes());
        RistrettoPoint base64Point = RistrettoPoint.fromBytes(lsBase64, randomPoint.toBytes());

        String hexEncoded = hexPoint.encode();
        String base64Encoded = base64Point.encode();

        assertEquals(randomPoint.toHex(), hexEncoded);
        assertEquals(Base64.getEncoder().encodeToString(randomPoint.toBytes()), base64Encoded);
    }
}
