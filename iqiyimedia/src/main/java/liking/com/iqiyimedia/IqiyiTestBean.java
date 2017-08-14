package liking.com.iqiyimedia;

/**
 * Created on 2017/07/24
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiTestBean {

    public String beanTitle;
    public int beanId;
    public String beanUrl;
    public int beanTvid;

    public DataType beanType;

    public enum DataType {
        CATEGORYLIST,ALBUMLIST,TOPLIST
    }
}
