package com.example.louishotelmanagement.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SearchBoxUtil {

    public static void makeSearchable(ComboBox<String> comboBox) {
        // 1. Kiểm tra xem ComboBox này đã được xử lý SearchBoxUtil chưa
        if (comboBox.getProperties().containsKey("isSearchable")) {
            return; // Đã xử lý rồi thì thoát ra, không làm gì thêm
        }

        // 2. Đánh dấu đã xử lý
        comboBox.getProperties().put("isSearchable", true);

        comboBox.setEditable(true);
        comboBox.getStyleClass().add("search-style");

        // 3. Sử dụng try-catch để an toàn tuyệt đối với promptText
        try {
            comboBox.getEditor().setPromptText("Nhập tên để tìm kiếm...");
        } catch (RuntimeException e) {
            // Nếu đã bị bind từ trước thì bỏ qua, không gây sập ứng dụng
        }

        // Giữ nguyên logic xử lý danh sách masterItems và sự kiện phím bên dưới...
        ObservableList<String> masterItems = FXCollections.observableArrayList(comboBox.getItems());

        comboBox.getEditor().setOnKeyReleased(event -> {

            if (event.getCode().isNavigationKey()
                    || event.getCode() == KeyCode.ENTER
                    || event.getCode() == KeyCode.TAB) {
                return;
            }

            String text = comboBox.getEditor().getText();

            if (text == null || text.isBlank()) {
                comboBox.setItems(masterItems);
                comboBox.hide();
                return;
            }

            String searchKey = removeAccent(text.toLowerCase());
            ObservableList<String> filtered = FXCollections.observableArrayList();

            for (String item : masterItems) {
                if (item != null) {
                    String itemUnaccent = removeAccent(item.toLowerCase());
                    if (itemUnaccent.contains(searchKey)) {
                        filtered.add(item);
                    }
                }
            }

            comboBox.setItems(filtered);
            if (!filtered.isEmpty()) comboBox.show();
        });

        // ✅ Khi đóng dropdown → trả về danh sách gốc
        comboBox.setOnHidden(e -> {
            comboBox.setItems(masterItems);

            String text = comboBox.getEditor().getText();
            if (text == null) return;

            for (String item : masterItems) {
                if (item.equalsIgnoreCase(text)) {
                    comboBox.getSelectionModel().select(item);
                    break;
                }
            }
        });
    }

    private static String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp)
                .replaceAll("")
                .replace('đ','d')
                .replace('Đ','D');
    }
}
