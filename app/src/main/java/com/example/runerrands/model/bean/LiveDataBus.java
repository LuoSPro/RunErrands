package com.example.runerrands.model.bean;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

public class LiveDataBus {
    private static class Lazy{
        static LiveDataBus  sLiveDataBus = new LiveDataBus();
    }
    public static LiveDataBus get(){
        return Lazy.sLiveDataBus;
    }

    private ConcurrentHashMap<String,StickyLiveData> mHasMap = new ConcurrentHashMap<>();

    public StickyLiveData with(String evenName){
        StickyLiveData liveData = mHasMap.get(evenName);
        if (liveData == null){
            liveData = new StickyLiveData(evenName);
            mHasMap.put(evenName,liveData);
        }
        return liveData;

    }
    public class StickyLiveData<T> extends LiveData<T> {

        private  T mStickyData;
        private String mEvenName;
        private int mVersion = 0;

        public StickyLiveData(String evenName){
            mEvenName = evenName;

        }

        @Override
        public void setValue(T value) {
            mVersion++;
            super.setValue(value);
        }

        @Override
        protected void postValue(T value) {
            mVersion++;
            super.postValue(value);
        }
        public void setStickyData(T stickyData){
            this.mStickyData = stickyData;
            setValue(stickyData);
        }
        public void postStickyData(T stickyData){
            this.mStickyData = stickyData;
            postValue(stickyData);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            observerSticky(owner,observer,false);
        }


        public void observerSticky(LifecycleOwner owner, Observer<? super T> observer, boolean sticky) {


            super.observe(owner,new WrapperObserver(this,observer,sticky));
            owner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY){
                        mHasMap.remove(mEvenName);
                    }
                }
            });
        }
        private class WrapperObserver<T> implements Observer<T>{
            private StickyLiveData mLiveData;
            private Observer<T> mObserver;
            private boolean mSticky;
            private int mLastVersion = 0;

            public WrapperObserver(StickyLiveData liveData,Observer<T> observer,boolean sticky){

                mLiveData = liveData;
                mObserver = observer;
                mSticky = sticky;
                mLastVersion = mLiveData.mVersion;

            }

            @Override
            public void onChanged(T t) {
                if (mLastVersion>=mLiveData.mVersion){
                    if (mSticky&&mLiveData.mStickyData!=null){
                        mObserver.onChanged((T) mLiveData.mStickyData);
                    }
                    return;
                }
                mLastVersion = mLiveData.mVersion;
                mObserver.onChanged(t);
            }
        }
    }
}
