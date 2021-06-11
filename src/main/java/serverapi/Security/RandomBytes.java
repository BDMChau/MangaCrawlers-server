package serverapi.Security;

import java.security.SecureRandom;

public class RandomBytes {
    private byte[] bytes;

    public String randomBytes(int number) {
        byte[] bytes = new byte[number];
        new SecureRandom().nextBytes(bytes);

        return convertBytesToHexString(bytes);
    }

    private static String convertBytesToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }

        return result.toString();
    }
}
