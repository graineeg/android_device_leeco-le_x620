package com.cyanogenmod.settings.device;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.NotificationManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.session.MediaSessionLegacyHelper;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import android.view.ViewConfiguration;
import android.telecom.TelecomManager;
import com.android.internal.os.DeviceKeyHandler;
import com.android.internal.util.ArrayUtils;
import android.hardware.input.InputManager;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import com.android.internal.R;

public class KeyHandler implements DeviceKeyHandler {
	
	private static final String TAG = KeyHandler.class.getSimpleName();
	
	private final Context handlerContext;
	private Vibrator handlerVibrator;
	private final AudioManager handlerAudioManager;
	private Handler hHandler;
	private boolean homeConsumed = false;
	private boolean homePressed = false;
	private boolean homeDoubleTapPending = false;
	private boolean additioanalConsumed = false;
	private boolean additioanalPressed = false;
	private long[] shortPressVibePattern;
	private long[] longPressVibePattern;
	
	private class PolicyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
            }
        }
    }
    
    public KeyHandler(Context context) {
		handlerContext = context;
		hHandler = new PolicyHandler();
		handlerAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		handlerVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		if (handlerVibrator == null || !handlerVibrator.hasVibrator()) {
			handlerVibrator = null;
        }
        shortPressVibePattern = getLongIntArray(context.getResources(), com.android.internal.R.array.config_virtualKeyVibePattern);
        longPressVibePattern = getLongIntArray(context.getResources(), com.android.internal.R.array.config_longPressVibePattern);
    }
    
    private final Runnable homeDoubleTapTimeoutRunnable = new Runnable() {
		@Override
		public void run() {
			if (homeDoubleTapPending) {
				homeDoubleTapPending = false;
				handlerTriggerVirtualKeypress(KeyEvent.KEYCODE_BACK);
            }
        }
    };
    
    private final Runnable homeLongPressTimeoutRunnable = new Runnable() {
		@Override
		public void run() {
			if (homePressed) {
				handlerTriggerVirtualKeypress(KeyEvent.KEYCODE_APP_SWITCH);
				doHapticFeedback(longPressVibePattern);
				homeConsumed = true;
            }
        }
    };
    
    private final Runnable additionalLongPressTimeoutRunnable = new Runnable() {
		@Override
		public void run() {
			if (additioanalPressed) {
				if (handlerAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
					handlerAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			    } else if (handlerAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
					handlerAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                additioanalConsumed = true;
            }
        }
    };
    
    public void handlerTriggerVirtualKeypress(final int keyCode) {
		InputManager im = InputManager.getInstance();
		long now = SystemClock.uptimeMillis();
		final KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
		keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
		KeyEvent.FLAG_FROM_SYSTEM, InputDevice.SOURCE_KEYBOARD);
		final KeyEvent upEvent = KeyEvent.changeAction(downEvent, KeyEvent.ACTION_UP);
		im.injectInputEvent(downEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
		im.injectInputEvent(upEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }
    
    public boolean handleKeyEvent(KeyEvent event) {
		int scanCode = event.getScanCode();
		boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
		TelecomManager telecomManager = (TelecomManager) handlerContext.getSystemService(Context.TELECOM_SERVICE);
		if (telecomManager != null && telecomManager.isRinging()) {
			homeConsumed = false;
			homePressed = false;
			homeDoubleTapPending = false;
			additioanalConsumed = false;
			additioanalPressed = false;
			hHandler.removeCallbacks(homeLongPressTimeoutRunnable);
			hHandler.removeCallbacks(homeDoubleTapTimeoutRunnable);
			hHandler.removeCallbacks(additionalLongPressTimeoutRunnable);
			return true;
        }
        if (scanCode == 250) {
			if(down){
				additioanalPressed = true;
				hHandler.postDelayed(additionalLongPressTimeoutRunnable, 300);
            }
            if (!down){
				additioanalPressed = false;
				if (additioanalConsumed){
					additioanalConsumed = false;
					return true;
                }
                hHandler.removeCallbacks(additionalLongPressTimeoutRunnable);
                PackageManager pm = handlerContext.getPackageManager();
                if (pm.getLaunchIntentForPackage("com.shved.elebuttonsettings") != null){
					Intent startPackage = new Intent("com.shved.elebuttonsettings.startpackage");
					startPackage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					handlerContext.startActivity(startPackage);
                }
            }
            return true;
        }
        if (scanCode == 102) {
			if (down){
				doHapticFeedback(shortPressVibePattern);
				homePressed = true;
				if (homeConsumed == false && homeDoubleTapPending == false) {
					hHandler.postDelayed(homeLongPressTimeoutRunnable, 300);
                }
            }
            if (!down) {
				homePressed = false;
				hHandler.removeCallbacks(homeLongPressTimeoutRunnable);
				if (homeConsumed) {
					homeConsumed = false;
					return true;
                }
                homeDoubleTapPending = true;
                hHandler.postDelayed(homeDoubleTapTimeoutRunnable, 300);
                return true;
            }
            if (homeDoubleTapPending) {
				homeDoubleTapPending = false;
				homeConsumed = true;
				hHandler.removeCallbacks(homeDoubleTapTimeoutRunnable);
				handlerTriggerVirtualKeypress(KeyEvent.KEYCODE_HOME);
            }
            return true;
        }
        return false;
    }
         
    static long[] getLongIntArray(Resources r, int resid) {
		int[] ar = r.getIntArray(resid);
		if (ar == null) {
			return null;			
        }
        long[] out = new long[ar.length];
        for (int i=0; i<ar.length; i++) {
			out[i] = ar[i];
        }
        return out;
    }
    
    private void doHapticFeedback(long[] pattern) {
		if (handlerVibrator == null) {
			return;
        }
        final boolean hapticsDisabled = Settings.System.getIntForUser(handlerContext.getContentResolver(),
        Settings.System.HAPTIC_FEEDBACK_ENABLED, 0, UserHandle.USER_CURRENT) == 0;
        if (hapticsDisabled) {
			return;
        }
        handlerVibrator.vibrate(pattern, -1);
    }
}
