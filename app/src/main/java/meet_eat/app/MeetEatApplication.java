package meet_eat.app;

import android.app.Application;
import android.content.Context;

/**
 * Represents the main application class of Meet & Eat.
 */
public class MeetEatApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Returns the {@link Context context} of the application.
     *
     * @return the context of the application
     */
    public static Context getAppContext() {
        return context;
    }
}
