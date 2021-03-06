package com.credits.crypto;

import com.credits.crypto.exception.CreditsCryptoException;
import org.bouncycastle.crypto.digests.Blake2sDigest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Blake2S {

    public static byte[] generateHash(int digestSize) throws CreditsCryptoException {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            byte[] seed = random.generateSeed(digestSize);
            byte[] outDigest = new byte[digestSize];
            Blake2sDigest blake2sDigest = new Blake2sDigest(seed, digestSize, null, null);
            blake2sDigest.doFinal(outDigest, 0);
            return outDigest;
        } catch (NoSuchAlgorithmException e) {
            throw new CreditsCryptoException(e);
        }
    }
}
