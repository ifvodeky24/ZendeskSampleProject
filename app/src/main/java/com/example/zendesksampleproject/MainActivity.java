package com.example.zendesksampleproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;

import java.util.ArrayList;

import zendesk.chat.Chat;
import zendesk.chat.ChatConfiguration;
import zendesk.chat.ChatEngine;
import zendesk.chat.ChatInfo;
import zendesk.chat.ChatProvider;
import zendesk.chat.ChatProvidersConfiguration;
import zendesk.chat.ObservationScope;
import zendesk.chat.VisitorInfo;
import zendesk.messaging.MessagingActivity;

public class MainActivity extends AppCompatActivity {
    Button btn_click;
    private ObservationScope observationScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Global.isMissingCredentials()) {
            setContentView(R.layout.missing_credentials);
            return;
        } else {
            setContentView(R.layout.activity_main);
        }

        /* Add CHAT_ACCOUNT_KEY and APP_ID in Global.java*/

        /* We use the observe function to make the chat clear when clicked on the "end chat"
        button from the web and the display on android returns to the second page automatically
        if the chat status is "ENDED". function of "end chat" button on android is hide because we don't use it */

        /* observationScope = new ObservationScope();
        Chat.INSTANCE.providers().chatProvider().observeChatState(observationScope, chatState -> {
            Log.d("TAG", "chatState.getChatSessionStatus() "+chatState.getChatSessionStatus().name());

            if (chatState.getChatSessionStatus().name().equals("ENDED")) {
                Chat.INSTANCE.clearCache();
                Intent intent = new Intent(this, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/

        btn_click = findViewById(R.id.btn_click);

        btn_click.setOnClickListener(v -> {
            VisitorInfo visitorInfo = VisitorInfo.builder()
                    .withName("test")
                    .withEmail("test@gmail.com")
                    .withPhoneNumber("02382384343") // numeric string
                    .build();

            ChatProvidersConfiguration chatProvidersConfiguration = ChatProvidersConfiguration.builder()
                    .withVisitorInfo(visitorInfo)
                    .build();

            Chat.INSTANCE.setChatProvidersConfiguration(chatProvidersConfiguration);

            ArrayList<String> list = new ArrayList<>();
            list.add("TAG 1");
            list.add("TAG 2");

            Chat.INSTANCE.providers().profileProvider().addVisitorTags(list, new ZendeskCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Log.i("Success ", "Success send list tags " + result);
                }

                @Override
                public void onError(ErrorResponse error) {
                    Log.i("Error ", "Error send list tags " + error);
                }
            });

            ChatProvider chatProvider = Chat.INSTANCE.providers().chatProvider();
            chatProvider.getChatInfo(new ZendeskCallback<ChatInfo>() {
                @Override
                public void onSuccess(ChatInfo result) {
                    if (!result.isChatting()) {
                        Chat.INSTANCE.providers()
                                .chatProvider().sendMessage(
                                        "Saya mau konsultasi nih dok"
                                );
                    }
                }

                @Override
                public void onError(ErrorResponse error) {
                    Log.d("MyTag", "Error get chat info : " + error);
                }
            });

            ChatConfiguration chatConfiguration = ChatConfiguration.builder()
                    .withTranscriptEnabled(false)
                    .withPreChatFormEnabled(false)
                    .withAgentAvailabilityEnabled(false)
                    .withChatMenuActions()
                    .build();

            MessagingActivity.builder()
                    .withEngines(ChatEngine.engine())
                    .withMultilineResponseOptionsEnabled(true)
                    .show(v.getContext(), chatConfiguration);
        });
    }
}