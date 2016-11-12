/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gcblog.pulltorefreshoverlyscrollview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class PullToRefreshOverlyScrollView extends ScrollView {

	public PullToRefreshOverlyScrollView(Context context) {
		super(context);
	}

	public PullToRefreshOverlyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void scrollBehindTo(int x, int y) {
		float detalY = (y + getHeight());
		if (detalY < 0) {
			detalY = 0;
		}
		scrollTo(x, (int) (detalY));
	}

	private static final int MAX_INTERVAL_FOR_CLICK = 250;
	private static final int MAX_DISTANCE_FOR_CLICK = 100;
	int mDownX = 0;
	int mDownY = 0;
	int mTempX = 0;
	int mTempY = 0;
	float mTop = 0;
	boolean mIsWaitUpEvent = false;
	boolean mIsEnable = true;
	Runnable mTimerForUpEvent = new Runnable() {
		public void run() {
			if (mIsWaitUpEvent) {
				mIsWaitUpEvent = false;
			}
		}
	};

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsEnable && !mIsWaitUpEvent
				&& ev.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onInterceptTouchEvent(ev);
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) ev.getX();
			mDownY = (int) ev.getY();
			mIsWaitUpEvent = true;
			postDelayed(mTimerForUpEvent, MAX_INTERVAL_FOR_CLICK);
			break;
		case MotionEvent.ACTION_MOVE:
			mTempX = (int) ev.getX();
			mTempY = (int) ev.getY();
			if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
					|| Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK * 10) {
				mIsWaitUpEvent = false;
				removeCallbacks(mTimerForUpEvent);
			}

			float dis = mTempY - mDownY;
			if (getScrollY() == 0 && dis < MAX_DISTANCE_FOR_CLICK * 10 && dis > 0) {
				mIsEnable = false;
				mIsWaitUpEvent = true;
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			mTempX = (int) ev.getX();
			mTempY = (int) ev.getY();
			if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
					|| Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
				mIsWaitUpEvent = false;
				removeCallbacks(mTimerForUpEvent);
				break;
			} else {
				mIsWaitUpEvent = false;
				removeCallbacks(mTimerForUpEvent);
			}
		case MotionEvent.ACTION_CANCEL:
			mIsWaitUpEvent = false;
			removeCallbacks(mTimerForUpEvent);
			mTempX = 0;
			mDownX = 0;
			mTempY = 0;
			mDownY = 0;
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	public void setScrollEnable(boolean enable) {
		this.mIsEnable = enable;
	}
}
