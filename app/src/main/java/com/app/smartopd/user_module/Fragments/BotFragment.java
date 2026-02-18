package com.app.smartopd.user_module.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.R.layout.*;
import android.R.id.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.ChatAdapter;
import com.app.smartopd.ChatMessage;
import com.app.smartopd.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BotFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageView btnSend;

    private List<ChatMessage> messageList = new ArrayList<>();
    private ChatAdapter adapter;

    private final String API_KEY = "YOUR_OPENAI_API_KEY";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bot, container, false);

        recyclerView = view.findViewById(R.id.chatRecycler);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);

        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {

        String userText = etMessage.getText().toString().trim();
        if (userText.isEmpty()) return;

        messageList.add(new ChatMessage(userText, true));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size() - 1);
        etMessage.setText("");

        callOpenAI(userText);
    }

    private void callOpenAI(String prompt) {

        OkHttpClient client = new OkHttpClient();

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-4o-mini");

            JSONArray messagesArray = new JSONArray();

            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content",
                    "You are a medical assistant. Only answer medical related questions. If question is not medical say: I only answer medical queries.");

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);

            messagesArray.put(systemMsg);
            messagesArray.put(userMsg);

            jsonBody.put("messages", messagesArray);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/responses")
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    requireActivity().runOnUiThread(() -> {
                        messageList.add(new ChatMessage("Network error", false));
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String res = response.body().string();

                    try {
                        JSONObject obj = new JSONObject(res);
                        String reply = obj.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        requireActivity().runOnUiThread(() -> {
                            messageList.add(new ChatMessage(reply.trim(), false));
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        });

                    } catch (Exception e) {
                        requireActivity().runOnUiThread(() -> {
                            messageList.add(new ChatMessage("Error parsing response", false));
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}