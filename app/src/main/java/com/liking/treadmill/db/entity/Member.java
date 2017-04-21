package com.liking.treadmill.db.entity;

import com.facebook.common.internal.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/04/18
 * desc:
 *      场馆会员信息
 * @author: chenlei
 * @version:1.0
 */

public class Member implements Serializable {

    public static final int BRACELET_OPERATE_NEW = 1;//1=>新增手环， 2=>删除手环

    public static final int BRACELET_OPERATE_DELETE = 2;

    public static final int MEMBER_TYPE_MANAGER = 1;//1 => 管理员 ，2 => 普通会员

    public static final int MEMBER_TYPE_MEMBER = 2;

    @SerializedName("id")
    private String memberId;//会员ID

    @SerializedName("bracelet_id")
    private String braceletId;//手环ID

    @SerializedName("bracelet_operate")
    private int braceletOperate;//新增手环， 2=>删除手环

    @SerializedName("member_type")
    private int memberType;//1 => 管理员 ，2 => 普通会员

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBraceletId() {
        return braceletId;
    }

    public void setBraceletId(String braceletId) {
        this.braceletId = braceletId;
    }

    public int getBraceletOperate() {
        return braceletOperate;
    }

    public void setBraceletOperate(int braceletOperate) {
        this.braceletOperate = braceletOperate;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member data = (Member) o;
        return Objects.equal(memberId, data.getMemberId()) &&
                Objects.equal(braceletId, data.getBraceletId());
    }
}
