package com.example.zendesksampleproject;

import android.app.Application;

import com.zendesk.logger.Logger;
import com.zendesk.util.StringUtils;

import zendesk.chat.Chat;

public class Global extends Application {

    private static final String CHAT_ACCOUNT_KEY = "";
    private static final String APP_ID = "";

    private static boolean missingCredentials = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable logging
        Logger.setLoggable(true);

        if (StringUtils.isEmpty(CHAT_ACCOUNT_KEY)) {
            missingCredentials = true;
        }

        Chat.INSTANCE.init(this, CHAT_ACCOUNT_KEY, APP_ID);
    }

    static boolean isMissingCredentials() {
        return missingCredentials;
    }
}

