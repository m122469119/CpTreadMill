/*
package liking.com.iqiyimedia.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.aaron.android.framework.base.ui.BaseActivity;

import liking.com.iqiyimedia.R;


*/
/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 *//*


public class PlayBackActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);

        Bundle bundle = new Bundle();
        bundle.putInt(IQIYI_VIDEO_DATATYPE, IqiyiVideoListFragment.DATA_TYPE_CATEGORY);
        launchFragment(bundle);

    }

    public void launchFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout_content, IqiyiVideoListFragment.getInstance(bundle));
        if(bundle != null) {
           int type = bundle.getInt(IQIYI_VIDEO_DATATYPE);
            if(type > 0) {
                fragmentTransaction.addToBackStack(null);
            }
        }
        fragmentTransaction.commit();
    }

    public void launchFragment2(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_content, MediaFragment.getInstance(bundle));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
*/
