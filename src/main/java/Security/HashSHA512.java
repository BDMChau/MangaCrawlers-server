package Security;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class HashSHA512 {

    public String hash(Object originalString) throws NoSuchAlgorithmException {
        final byte[] salt = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(salt);

        final HashCode stringHashed =
                Hashing.sha512().hashBytes(originalString.toString().getBytes(StandardCharsets.UTF_8));
        final HashCode saltHashed = HashCode.fromBytes(salt);

        return stringHashed.toString() + saltHashed.toString();
    }


    public Boolean compare(String originalString, String stringHashed) {

        final int stringHashedLength = stringHashed.getBytes(StandardCharsets.UTF_8).length;
        if (stringHashed.length() < stringHashedLength) {
            return false;
        }

        // Get salt from hash
        final String originalSalt = stringHashed.substring(stringHashedLength - 24);

        final HashCode originalStringHashed = Hashing.sha512().hashBytes(originalString.getBytes(StandardCharsets.UTF_8));


        return (originalStringHashed.toString() + originalSalt.toString()).equals(stringHashed);
    }
}
