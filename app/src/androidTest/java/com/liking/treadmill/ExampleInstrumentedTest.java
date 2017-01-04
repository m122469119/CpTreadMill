package com.liking.treadmill;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.liking.treadmill.socket.result.MemberListResult;
import com.liking.treadmill.storge.Preference;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        String memberlist = "{type:\"member_list\",msg_id:0,data:{\"bracelet_id\":{\"member\":[12345,54321],\"manger\":[12345,54321]}}}";
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
        MemberListResult.MemberData members = gson.fromJson(Preference.getMemberList(), MemberListResult.MemberData.class);
        System.out.println();
    }
}
