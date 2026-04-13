package mouse.univ.hash;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SHA256 implements HashFunction {
    @Override
    public String hash(String origin) {
        return Hashing.sha256().hashString(origin, StandardCharsets.UTF_8).toString();
    }
}
