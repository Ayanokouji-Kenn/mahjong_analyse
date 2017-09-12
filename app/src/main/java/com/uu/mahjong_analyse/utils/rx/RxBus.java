package com.uu.mahjong_analyse.utils.rx;



import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
/**
 * @author xzj
 * @date 2016/8/24 15:17.
 */
public class RxBus {
    private static RxBus mRxBus = null;
    /**
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     */
    private Subject<Object, Object> mRxBusObserverable = new SerializedSubject<>(PublishSubject.create());
    public static synchronized RxBus getInstance() {
        if (mRxBus == null) {
            mRxBus = new RxBus();
        }
        return mRxBus;
    }
    public void send(Object o, String tag) {
        mRxBusObserverable.onNext(new RxBusObject(tag, o));
    }
    //    public Observable<Object> toObserverable() {
//        return mRxBusObserverable;
//    }
    public <T> Observable<T> toObservable(final Class<T> eventType, final String tag) {
        return mRxBusObserverable.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof RxBusObject)) return false;
                RxBusObject ro = (RxBusObject) o;
                return eventType.isInstance(ro.getObj()) && tag != null
                        && tag.equals(ro.getTag());
            }
        }).map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                RxBusObject ro = (RxBusObject) o;
                return (T) ro.getObj();
            }
        });
    }
    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mRxBusObserverable.hasObservers();
    }
}
