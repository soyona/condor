package sample.jcu.unsafe;

import org.junit.Test;

/**
 * @author soyona
 * @Package sample.jcu.unsafe
 * @Desc:
 * @date 2018/7/14 15:38
 */
public class ThrowExceptionTest {
    @Test(expected = Exception.class)
    public void testThrowChecked() throws Exception {
        throwChecked();
    }

    public void throwChecked() {
        UnsafeUtils.unsafe.throwException(new Exception(""));
    }
}
