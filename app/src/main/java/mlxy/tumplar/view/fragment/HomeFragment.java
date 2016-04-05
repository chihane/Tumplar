package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

import mlxy.tumplar.R;
import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.service.PrefetchService;

public class HomeFragment extends BaseFragment {
    private TextView textView;

    public HomeFragment() {
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefetchService service = new PrefetchService();
                service.prefetch();
            }
        });
        return view;
    }

    @Subscribe
    public void onProgressEvent(PrefetchProgressEvent event) {
        String text = String.format(Locale.CHINA, "%s\n%s/%s", event.url, event.bytesRead, event.totalBytes);
        textView.setText(text);
    }
}
