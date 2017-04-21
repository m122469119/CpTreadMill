package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.liking.treadmill.db.entity.Member;

import java.util.List;

/**
 * Created on 2017/04/20
 * desc: 成员列表
 *
 * @author: chenlei
 * @version:1.0
 */

public class MemberListMessage extends BaseMessage{

    public List<Member> mMemberList = null;

    public MemberListMessage(List<Member> memberList) {
        this.mMemberList = memberList;
    }
}
