package com.mediatek.engineermode.boot;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


import com.mediatek.engineermode.Elog;

/**
 * a start service of EM boot.
 * @author: mtk81238
 */
public class EmBootStartService extends Service {
    private static final String TAG = "EM/BootService";
    private static final String KEY_REQ_START_SERV = "req_start_serv";
    private static final int MSG_CHECK_STOP_SERVICE = 111;

    public static Map<String, IBootServiceHandler> sStartReqHandlerMap
            = new HashMap<String, IBootServiceHandler>();

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_CHECK_STOP_SERVICE:
                Elog.d(TAG, "check stop service");
                synchronized (EmBootStartService.class) {
                    if (sStartReqHandlerMap.size() == 0) {
                        Elog.d(TAG, "stop service");
                        stopSelf();
                    }
                }
                break;
            default:
                Elog.d(TAG, "unhandled msg:" + msg.what);
                break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * request start service.
     * @param context system context
     * @param handler IBootServiceHandler
     */
    public static void requestStartService(Context context, IBootServiceHandler handler) {
        if (handler == null || context == null) {
            throw new IllegalArgumentException("invalid Context or invalid IBootServiceHandler");
        }
        Intent servIntent = new Intent(context, EmBootStartService.class);
        String handlerName = handler.getClass().getCanonicalName();
        servIntent.putExtra(KEY_REQ_START_SERV, handlerName);
        synchronized (EmBootStartService.class) {
            sStartReqHandlerMap.put(handlerName, handler);
        }
        context.startService(servIntent);
    }

    /**
     * stop started service.
     * @param handler IBootServiceHandler
     */
    public void stopStartedService(IBootServiceHandler handler) {
        if (handler != null) {
            String handlerName = handler.getClass().getCanonicalName();
            synchronized (EmBootStartService.class) {
                sStartReqHandlerMap.remove(handlerName);
            }
        }
        mMainHandler.sendEmptyMessage(MSG_CHECK_STOP_SERVICE);
    }

    private int onHandleRequest(String handlerName) {
        IBootServiceHandler handler = sStartReqHandlerMap.get(handlerName);
        if (handler == null) {
            return IBootServiceHandler.HANDLE_INVALID;
        }
        return handler.handleStartRequest(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String handlerName = intent.getStringExtra(KEY_REQ_START_SERV);
            int ret = onHandleRequest(handlerName);
            if (ret != IBootServiceHandler.HANDLE_ONGOING) {
                stopStartedService(sStartReqHandlerMap.get(handlerName));
            }
        }
        return START_NOT_STICKY;
    }

}
