package com.example.louishotelmanagement.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SearchBoxUtil {

    /**
     * Biến ComboBox thường thành ComboBox có thể tìm kiếm
     * @param comboBox ComboBox cần thêm chức năng tìm kiếm
     */
    public static void makeSearchable(ComboBox<String> comboBox) {
        comboBox.setEditable(true);

        ObservableList<String> items = comboBox.getItems();

        comboBox.getEditor().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN ||
                    event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT ||
                    event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END ||
                    event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                return;
            }

            String text = comboBox.getEditor().getText();
            if (text == null || text.isEmpty()) {
                comboBox.setItems(items);
                comboBox.hide();
            } else {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
                String searchKey = removeAccent(text.toLowerCase());

                for (String item : items) {
                    if (item != null) {
                        String itemUnaccent = removeAccent(item.toLowerCase());
                        if (itemUnaccent.contains(searchKey)) {
                            filteredList.add(item);
                        }
                    }
                }

                comboBox.setItems(filteredList);
                if (!filteredList.isEmpty()) {
                    comboBox.show();
                } else {
                    comboBox.hide();
                }
            }
        });

        comboBox.setOnHidden(event -> {
            String text = comboBox.getEditor().getText();
            if (text != null && !text.isEmpty()) {
                boolean match = false;
                for (String item : items) {
                    if (item.equalsIgnoreCase(text)) {
                        comboBox.getSelectionModel().select(item);
                        match = true;
                        break;
                    }
                }
            }
        });
    }

    private static String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}