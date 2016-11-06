/**
 * Created by wanbin on 11/5/16.
 */
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class testTrimmedMean {

    @Test
    public void test() {
        TrimmedMean mean = new TrimmedMean(10,2,2,false);
        for(int i = 0; i < 100; i++) {
            mean.setValue((long)i);
        }
        List<Long> top = new ArrayList<Long>();
        top.add(89L);
        top.add(99L);
        List<Long> bottom = new ArrayList<Long>();
        bottom.add(0L);
        bottom.add(10L);
        assertArrayEquals(mean.getTopExceptionValue().toArray(),top.toArray());
        assertArrayEquals(mean.getBottomExceptionValue().toArray(),bottom.toArray());
        Avg<Long> avg = new Avg();
        avg.setValue(48L);
        avg.setWeight(96);
        assertEquals(mean.getValue(),avg);



    }
}
