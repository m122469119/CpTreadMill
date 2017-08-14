package liking.com.iqiyimedia.http;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface IqiyiResponseCode {

    /** 获取数据成功 */
    String IQIYI_OBTAIN_SUCCESS = "A00000";

    /** 注册成功 */
    String IQIYI_REGISTER_SUCCESS = "A00001";

    /** 更新成功 */
    String IQIYI_UPDATE_SUCCESS = "A00002";

    /** 参数错误 */
    String IQIYI_PARAMETER_EXCEPTION = "E00001";

    /** 未注册 */
    String IQIYI_UNREGISTER_EXCEPTION = "E00002";

    /** 版权受限 */
    String IQIYI_UNAUTHORIZED_EXCEPTION = "E00003";

    /** 没有数据 */
    String IQIYI_NODATA_EXCEPTION = "E00004";

    /** 更新失败 */
    String IQIYI_UPDATE_EXCEPTION = "E00005";

    /** 频道受限 */
    String IQIYI_CHANNEL_RESTRICTION_EXCEPTION = "E00006";

    /** 请输入频道 */
    String IQIYI_INPUT_CHANNEL_EXCEPTION = "E00007";

    /** 此来源数据已失效,请做下线 */
    String IQIYI_DATA_INVALID_EXCEPTION = "E00008";

    /** 无效的 apikey,注册失败 */
    String IQIYI_KEY_INVALID_EXCEPTION = "E00009";

    /** 视频增量时间跨度不能超过 3 小时 */
    String IQIYI_VIDEO_INCREMENT_EXCEPTION = "E00010";

    /** 专辑增量时间跨度不能超过 2 天 */
    String IQIYI_SPECIAL_INCREMENT_EXCEPTION = "E00011";
}
