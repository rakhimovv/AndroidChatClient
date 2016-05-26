package ru.mail.ruslan.androidchatclient.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class JSONDataProcessor implements DataProcessor {
    private static final char OPEN_BRACE = '{';
    private static final char CLOSE_BRACE = '}';

    @Override
    public List<String> process(String data) {
        if (data == null || !data.trim().endsWith(String.valueOf(CLOSE_BRACE))) {
            return null;
        }

        int openBracesCount = 0;
        int closeBracesCount = 0;

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == OPEN_BRACE) {
                openBracesCount++;
            } else if (data.charAt(i) == CLOSE_BRACE) {
                closeBracesCount++;
            }
        }

        if (openBracesCount == 0 ||
                closeBracesCount == 0 ||
                openBracesCount != closeBracesCount) {
            return null;
        }

        List<String> dataParts = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        int firstOpenBrace = -1;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == OPEN_BRACE) {
                if (firstOpenBrace == -1) {
                    firstOpenBrace = i;
                }
                stack.push(OPEN_BRACE);
            } else if (data.charAt(i) == CLOSE_BRACE) {
                if (!stack.isEmpty()) {
                    stack.pop();
                    if (stack.isEmpty()) {
                        dataParts.add(data.substring(firstOpenBrace, i + 1));
                        firstOpenBrace = -1;
                    }
                }
            }
        }

        return dataParts;
    }
}