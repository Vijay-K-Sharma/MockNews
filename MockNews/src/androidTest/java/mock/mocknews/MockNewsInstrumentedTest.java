package mock.mocknews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import mock.mocknews.model.NewsData;
import mock.mocknews.service.ServiceDataClass;
import mock.mocknews.utils.InternetUtil;
import mock.mocknews.utils.JsonUtils;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MockNewsInstrumentedTest {

    String testUrl = Constants.SERVICE_URL;

    @Test
    public void testService() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        MockNewsComponent component = DaggerMockNewsComponent.builder().serviceDataClass(new ServiceDataClass(appContext,"https://api.myjson.com/bins/m47pd")).build();
        ServiceDataClass serviceDataClass = component.getService();
        assertNotEquals(null, serviceDataClass);

    }

    @Test
    public void testJsonUtils() throws Exception {
        MockNewsComponent component = DaggerMockNewsComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = component.getJsonUtils();
        assertNotEquals(null, jsonUtils);

    }
    @Test
    public void testJsonResults()  throws Throwable{
        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        assertNotEquals(null,jsonResult);
    }

    @Test
    public void testNewsfeedCount() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        MockNewsComponent jsonUtilsComp = DaggerMockNewsComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals(14, NewsData.length);
    }
    @Test
    public void testJsonTitle() throws Exception {

        String jsonResult = InternetUtil.sendHttpRequest(testUrl,"");
        MockNewsComponent jsonUtilsComp = DaggerMockNewsComponent.builder().jsonUtils(new JsonUtils("")).build();
        JsonUtils jsonUtils = jsonUtilsComp.getJsonUtils();
        jsonUtils.parseJson(jsonResult);
        assertEquals("About Canada",NewsData.newsTitle);
    }
}