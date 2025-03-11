package com.example;

import java.util.ArrayList;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;

    private ArrayList<String> chatHistory = new ArrayList<>();

    private void addChatHistory(String inputText, String response) {
        chatHistory.add("[ユーザ] " + inputText + "\n" + "[AI] " + response);
        if (chatHistory.size() > 2) {
            chatHistory.removeFirst();
        }
    }

    private SystemMessage createSystemMessage() {
        return SystemMessage.from("あなたはJava開発の専門家です。簡潔かつ友達のように優しく教えてください。"
                + "【前提】わたしは[ユーザ]で、あなたは[AI]とするとき、これまでのやりとりは次のとおりです。"
                + String.join("\n", chatHistory)
                + "以上のやりとりを前提として回答してください。");
    }

    public void initialize() {
        inputField.setOnAction((e) -> {
            var inputText = inputField.getText();
            inputField.clear();

            var oldText = outputArea.getText();
            oldText = oldText.isEmpty() ? "" : oldText + "\n";

            var newText = oldText + "[あなた] " + inputText;
            outputArea.setText(newText);

            new Thread(() -> {
                try {
                    var gemini = GoogleAiGeminiChatModel.builder()
                            .apiKey(System.getenv("GEMINI_AI_KEY"))
                            .modelName("gemini-2.0-flash-lite")
                            .build();

                    var chatResponse = gemini.chat(createSystemMessage(), UserMessage.from(inputText));

                    var response = chatResponse.aiMessage().text();
                    System.out.println("token:" + chatResponse.tokenUsage().totalTokenCount());

                    addChatHistory(inputText, response);

                    Platform.runLater(() -> {
                        // UIスレッドで処理
                        outputArea.setText(newText + "\n\n[AI] " + response);
                    });
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        outputArea.setScrollTop(Double.MAX_VALUE);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });
    }
}
