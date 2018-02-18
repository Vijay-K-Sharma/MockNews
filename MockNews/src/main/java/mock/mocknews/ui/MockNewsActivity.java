package mock.mocknews.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import mock.mocknews.Constants;
import mock.mocknews.model.CustomAdapter;
import mock.mocknews.utils.JsonUtils;
import mock.mocknews.model.NewsData;
import mock.mocknews.utils.NewsUtils;
import mock.mocknews.R;
import mock.mocknews.ResponseCallback;
import mock.mocknews.MockNewsHolder;
import mock.mocknews.service.ServiceDataClass;

public class MockNewsActivity extends AppCompatActivity implements ResponseCallback {
    private final String TAG = MockNewsActivity.class.getSimpleName();
	private ListView newsList;
    private CustomAdapter adapter = null;
    private int firstVisible = -1;
    private int totalVisible = -1;
    private JsonUtils jsonUtils;
    private ServiceDataClass serviceDataClass;
    private ProgressDialog mProgressDialog;
    private MockNewsHolder mockNewsHolder = new MockNewsHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        newsList = (ListView)findViewById(R.id.menuList);
        NewsUtils.setNewsFeedHolder(mockNewsHolder);
        serviceDataClass = new ServiceDataClass(MockNewsActivity.this,
                Constants.SERVICE_URL);
        serviceDataClass.setResponseCallback(this);
        serviceDataClass.execute();

        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING){
                    Log.d(TAG,"MockNewsActivity - onScrollState: FLING ----:"+scrollState);
                }else if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    Log.d(TAG,"MockNewsActivity - onScrollState:IDLE ----:"+scrollState);
                    /**
                     * Images will start downloading for visible section
                     * of screen once user has swiped or pressed refresh
                     */
                }else{
                    Log.d(TAG,"MockNewsActivity - onScrollState: ----:"+scrollState);
                }

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                totalVisible = visibleItemCount;
                if(firstVisible == firstVisibleItem)
                    return;
                if(firstVisible == -1 ){
                    firstVisible = firstVisibleItem;
                    totalVisible = visibleItemCount;
                    return;
                }

                if(firstVisible != firstVisibleItem){
                    firstVisible = firstVisibleItem;
                    totalVisible = visibleItemCount;
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            /**
             * Images will start downloading for visible section
             * of screen once user has swiped or pressed refresh
             */
            /*imageUtils = new ImageUtils(MockNewsActivity.this,
                    firstVisible,totalVisible);
            imageUtils.setResponseCallback(MockNewsActivity.this);
            imageUtils.execute();*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String result) {
        jsonUtils = new JsonUtils(MockNewsActivity.this,
                result);
        jsonUtils.setResponseCallback(this);
        jsonUtils.execute();
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(getApplicationContext(),"Error:"+errorMessage,Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onUpdate(int state,int index){
        switch(state){
            case Constants.DIALOG_DISMISS:
                dismiss();
                break;
            case Constants.LIST_UPDATE:
                update(index);
                break;
            case Constants.JSON_PARSE_PARTIAL:
                update(index);
                break;
            case Constants.JSON_PARSE_COMPLETED:
                dismiss();
                break;
            case Constants.SHOW_DIALOG:
                showProgressDialog("");
                break;
            case Constants.UPDATE_TITLE:
                getSupportActionBar().setTitle(NewsData.newsTitle);
                break;
            case Constants.IMAGE_DOWNLOAD_COMPLETED:
                update(index);
                dismiss();
                break;

            default:
        }
    }

    public NewsData getNewsItem(int index){
        return mockNewsHolder.getNewsDataList().get(index);
    }

    public void update(int index){
        if(adapter == null){
            adapter = new CustomAdapter(this,this);
            newsList.setAdapter(adapter);
        }
        adapter.updateNewsFeed();
    }

    public void showProgressDialog(String message){
        if(null == message || "".equals(message)){
            message = "Updating";
        }
        if(null == mProgressDialog){
            mProgressDialog = ProgressDialog.show(this, "", message + "...", true, false);
            mProgressDialog.setCancelable(true);
        }
    }

    public void dismiss(){
        try {
            if (null != mProgressDialog && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch(IllegalArgumentException e){
            Log.e(TAG, "dismiss() - IllegalArgumentException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "dismiss() - Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy(){
        dismiss();
        if(null != serviceDataClass)
            serviceDataClass.cancel(true);
        if(null != jsonUtils)
            jsonUtils.cancel(true);
        NewsUtils.setNewsFeedHolder(null);
        super.onDestroy();
    }

}
