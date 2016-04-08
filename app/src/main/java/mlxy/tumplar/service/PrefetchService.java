package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mlxy.tumplar.global.App;
import mlxy.tumplar.model.UserModel;

public class PrefetchService extends Service {
    @Inject
    UserModel model;
    @Inject
    EventBus eventBus;
    @Inject
    Gson gson;

    public PrefetchService() {
        App.graph.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void prefetch() {
    }
}
