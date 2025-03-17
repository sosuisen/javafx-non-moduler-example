package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static jooq.Tables.*;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import jooq.tables.daos.MessageDao;
import jooq.tables.pojos.Message;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {
    private String dbPath = "jdbc:sqlite:./todoapp.db";

    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;

    private String getChatHistory() {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            var conf = new DefaultConfiguration().set(conn).set(SQLDialect.SQLITE);
            var dao = new MessageDao(conf);
            // なんの条件もないなら
            // var messages = dao.findAll();
            var messages = dao.ctx()
                    .selectFrom(MESSAGE)
                    .orderBy(MESSAGE.DATE.asc())
                    .limit(10)
                    .fetchInto(Message.class);

            var history = "";
            for (var mes : messages) {
                history += "[" + mes.getName() + "] " + mes.getMessage() + "\n";
            }
            return history;
        } catch (Exception e) {
            return "";
        }
    }

    private void addChatHistory(String name, String message) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            var conf = new DefaultConfiguration().set(conn).set(SQLDialect.SQLITE);
            var dao = new MessageDao(conf);
            var mes = new Message(null, name, message,
                    LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            dao.insert(mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SystemMessage createSystemMessage() {
        return SystemMessage.from("あなたはJava開発の専門家です。簡潔かつ友達のように優しく教えてください。"
                + "【前提】わたしは[User]で、あなたは[AI]とするとき、これまでのやりとりは次のとおりです。"
                + String.join("\n", getChatHistory())
                + "以上のやりとりを前提として回答してください。");
    }

    private void startAiThread (String inputText, String currentText){
        new Thread(() -> {
            try {
                var gemini = GoogleAiGeminiChatModel.builder()
                        .apiKey(System.getenv("GEMINI_AI_KEY"))
                        .modelName("gemini-2.0-flash-lite")
                        .build();

                var chatResponse = gemini.chat(createSystemMessage(), UserMessage.from(inputText));

                var response = chatResponse.aiMessage().text();
                System.out.println("token:" + chatResponse.tokenUsage().totalTokenCount());

                addChatHistory("AI", response);

                Platform.runLater(() -> {
                    // UIスレッドで処理
                    outputArea.setText(currentText + "\n\n[AI] " + response);
                });
                Thread.sleep(500);
                Platform.runLater(() -> {
                    outputArea.setScrollTop(Double.MAX_VALUE);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void initialize() {
        inputField.setOnAction((e) -> {
            var inputText = inputField.getText();
            inputField.clear();

            var oldText = outputArea.getText();
            oldText = oldText.isEmpty() ? "" : oldText + "\n";
            var newText = oldText + "[あなた] " + inputText;
            outputArea.setText(newText);

            addChatHistory("User", inputText);

            startAiThread(inputText, newText);
        });
    }
}
