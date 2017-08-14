package liking.com.iqiyimedia;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aaron.android.codelibrary.http.RequestError;

import org.junit.Test;
import org.junit.runner.RunWith;

import liking.com.iqiyimedia.http.IqiyiApiService;
import liking.com.iqiyimedia.http.callback.BaseRequestCallback;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;

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

//        assertEquals("liking.com.iqiyimedia.test", appContext.getPackageName());
        IqiyiApiService.getCategoryList(null, new BaseRequestCallback<CategoryListResult>() {
            @Override
            public void success(CategoryListResult result) {
                System.out.println(result.getCode());
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });

        IqiyiApiService.getAlbumList(null, "7", new BaseRequestCallback<AlbumListResult>() {
            @Override
            public void success(AlbumListResult result) {
                System.out.println(result.getCode());
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
