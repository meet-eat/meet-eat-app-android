package meet_eat.app;

import android.app.Application;
import android.content.Context;

public class MeetEatApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
