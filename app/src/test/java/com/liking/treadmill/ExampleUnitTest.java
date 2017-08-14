package com.liking.treadmill;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    String flag = "\\r\\n";
    int flaglength = flag.length();//4
    StringBuilder resultHandlerBuilder = new StringBuilder();

    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        String data = "123456\\r\\ndasd\\r";

        resultHandlerBuilder.append(data);
        resultHandlerBuilder.append("\\n\\r\\n");

        int flagLastIndex = resultHandlerBuilder.lastIndexOf(flag);

        if (flagIsLast(flagLastIndex, resultHandlerBuilder.length(), flaglength)) {
            String results = resultHandlerBuilder.toString();
            resultHandlerBuilder.setLength(0);
            handlerFullResult(results);
        } else if (flagLastIndex > -1) {
            int partStart = flagLastIndex + flaglength;
            //不完整部分
            String imperfectData = resultHandlerBuilder.substring(partStart);
            //去除不完整部分
            resultHandlerBuilder.replace(partStart, resultHandlerBuilder.length(), "");
            //处理完整部分
            handlerFullResult(resultHandlerBuilder.toString());
            resultHandlerBuilder.setLength(0);
            resultHandlerBuilder.append(imperfectData);
            System.out.println("未处理：" + resultHandlerBuilder.toString());
        }

    }


    /**
     * 判断是否 以 flag 结尾
     * @param flagLastPoi
     * @param dataLen
     * @param flagLen
     * @return
     */
    public boolean flagIsLast(int flagLastPoi, int dataLen, int flagLen) {
        return flagLastPoi > -1 && dataLen == flagLastPoi + flagLen;
    }

    public void handlerFullResult(String rs) {
        List<String> rlist = Arrays.asList(rs.split("\\\\r\\\\n"));
        for (String result : rlist) {
            System.out.println(result);
        }
    }
}