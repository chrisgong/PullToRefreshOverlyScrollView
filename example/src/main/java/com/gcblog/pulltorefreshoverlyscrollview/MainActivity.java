package com.gcblog.pulltorefreshoverlyscrollview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.gcblog.pulltorefreshoverlyscrollview.library.PullToRefreshBase;
import com.gcblog.pulltorefreshoverlyscrollview.library.PullToRefreshOverlyScrollView;
import com.gcblog.pulltorefreshoverlyscrollview.library.PullToRefreshScrollView;

public class MainActivity extends Activity implements PullToRefreshBase.OnRefreshListener<ScrollView> {

    private PullToRefreshScrollView mPtrMain;
    private PullToRefreshOverlyScrollView mOverScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPtrMain = (PullToRefreshScrollView) findViewById(R.id.ptr_main);
        mPtrMain.setOnRefreshListener(this);

        /**
         * 自动刷新
         */
        mPtrMain.autoRefresh();
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mPtrMain.onRefreshComplete();
            }
        }, 3000);
    }

    /**
     * 停止刷新
     */
    private void handlerStopMeasure() {
        mPtrMain.setPullRefreshEnabled(true);
        mOverScroll.setScrollEnable(true);
    }
}
