package com.liking.treadmill;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.liking.treadmill.socket.result.MemberListResult;
import com.liking.treadmill.storge.Preference;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.liking.treadmill", appContext.getPackageName());
    }

    @Test
    public void getMemberList() throws Exception {
        String memberlist = "{type:\"member_list\",msg_id:0,data:{\"bracelet_id\":{\"member\":[1111,1222],\"manger\":[0111,0222]}}}";
        Gson gson = new Gson();
        MemberListResult memberListResult = gson.fromJson(memberlist, MemberListResult.class);
        MemberListResult.MemberData memberData = memberListResult.getData();
        if (memberData != null) {
            MemberListResult.MemberData.MemberListData memberList = memberData.getBraceletId();
            try {
                Preference.setMemberList(gson.toJson(memberList));
            }catch (Exception e) {
            }
        }
        MemberListResult.MemberData.MemberListData members = gson.fromJson(Preference.getMemberList(), MemberListResult.MemberData.MemberListData.class);
        System.out.println("BraceletId_Member : "+ members.getMember() + "---- BraceletId_Manger" + members.getManger());
    }

    @Test
    public void getParseResult() throws Exception{
        String jsonMessage = "{\"type\":\"update\",\"msg_id\":\"\",\"data\":{\"lastest_version\":\"1.0.1\",\"url\":\"http:\\/\\/testrun.likingfit.com\\/LikingTreadMill.apk\"}}\\r\\n";
         String[] results = jsonMessage.split("\\\\r\\\\n");
        for (String r: results) {
            System.out.println("getParseResult:"+ r);
        }
    }
}
