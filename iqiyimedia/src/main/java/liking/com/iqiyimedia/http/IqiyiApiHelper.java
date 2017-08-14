package liking.com.iqiyimedia.http;

import android.support.v4.util.ArrayMap;

import liking.com.iqiyimedia.R;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiApiHelper {

    public static ArrayMap<String, Integer> eCodeAndMess = new ArrayMap<>();

    static {
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_PARAMETER_EXCEPTION, R.string.network_iqiyi_parameter_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_UNREGISTER_EXCEPTION, R.string.network_iqiyi_parameter_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_UNAUTHORIZED_EXCEPTION, R.string.network_iqiyi_unauthorized_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_NODATA_EXCEPTION, R.string.network_iqiyi_nodata_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_UPDATE_EXCEPTION, R.string.network_iqiyi_update_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_CHANNEL_RESTRICTION_EXCEPTION, R.string.network_iqiyi_channel_restriction_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_INPUT_CHANNEL_EXCEPTION, R.string.network_iqiyi_input_channel_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_DATA_INVALID_EXCEPTION, R.string.network_iqiyi_data_invalid_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_KEY_INVALID_EXCEPTION, R.string.network_iqiyi_key_invalid_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_VIDEO_INCREMENT_EXCEPTION, R.string.network_iqiyi_video_increment_exception_text);
        eCodeAndMess.put(IqiyiResponseCode.IQIYI_SPECIAL_INCREMENT_EXCEPTION, R.string.network_iqiyi_special_increment_exception_text);

    }

    public static final String IQIYI_APIKEY = "71c300df4a7f4e89a43d8e19e5458e6f";

}
