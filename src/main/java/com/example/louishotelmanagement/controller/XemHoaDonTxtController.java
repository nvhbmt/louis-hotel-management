package com.example.louishotelmanagement.controller;

import com.example.louishotelmanagement.view.XemHoaDonTxtView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XemHoaDonTxtController {

    @FXML
    private TextArea txtHoaDon;
    @FXML
    private Button btnInHoaDon;
    @FXML
    private Button btnDong;

    /**
     * Hàm này được gọi từ QuanLyHoaDonController để nhận nội dung TXT
     */

    public XemHoaDonTxtController(XemHoaDonTxtView view) {
        this.txtHoaDon = view.getTxtHoaDon();
        this.btnInHoaDon = view.getBtnInHoaDon();
        this.btnDong = view.getBtnDong();
        btnInHoaDon.setOnAction(this::handleIn);
        btnDong.setOnAction(this::handleDong);
    }

    public void initData(String noiDungHoaDon) {
        txtHoaDon.setText(noiDungHoaDon);
    }

    @FXML
    private void handleDong(ActionEvent event) {
        Stage stage = (Stage) btnDong.getScene().getWindow();
        stage.close();
    }

    /**
     * Hàm hỗ trợ tạo PdfPCell
     */
    private PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    /**
     * Hàm hỗ trợ tạo bảng 2 cột cho phần Header (Địa chỉ vs Sổ HĐ, MST vs Ngày lập)
     */
    private PdfPTable createHeaderTable(String leftText, String rightText, Font font) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{50, 50});
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);

        PdfPCell leftCell = createCell(leftText, font, Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER);
        leftCell.setPadding(0);

        PdfPCell rightCell = createCell(rightText, font, Element.ALIGN_RIGHT);
        rightCell.setBorder(PdfPCell.NO_BORDER);
        rightCell.setPadding(0);

        table.addCell(leftCell);
        table.addCell(rightCell);

        return table;
    }

    @FXML
    private void handleIn(ActionEvent event) {
        try {
            String content = txtHoaDon.getText();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file PDF");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File file = fileChooser.showSaveDialog(btnInHoaDon.getScene().getWindow());
            if (file == null) return;

            // 1. Khởi tạo Document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // 2. Khởi tạo Fonts
            BaseFont unicodeFont = null;
            try {
                // Đảm bảo file font nằm ở đường dẫn này: src/main/resources/com/example/louishotelmanagement/font/DejaVuSans.ttf
                unicodeFont = BaseFont.createFont(
                        "src/main/resources/com/example/louishotelmanagement/font/DejaVuSans.ttf",
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED
                );
            } catch (IOException e) {
                System.err.println("Font Unicode không tìm thấy, sử dụng font mặc định.");
                unicodeFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            }

            Font font = new Font(unicodeFont, 10);
            Font boldFont = new Font(unicodeFont, 10, Font.BOLD);
            Font headerFont = new Font(unicodeFont, 14, Font.BOLD);
            Font smallFont = new Font(unicodeFont, 9);

            // 3. Phân tích nội dung và Ghi vào PDF
            String[] lines = content.split("\n");

            // Biến trạng thái
            boolean inPaymentDetailSection = false;
            boolean inSummarySection = false;

            // Bảng cho CHI TIẾT THANH TOÁN (5 cột)
            PdfPTable detailTable = new PdfPTable(5);
            detailTable.setWidthPercentage(100);
            detailTable.setSpacingBefore(10f);
            // Tỷ lệ cho STT | Tên phòng/Dịch vụ | SL | Đơn giá | Thành tiền
            detailTable.setWidths(new float[]{8, 37, 10, 25, 20});

            // Bảng cho TỔNG KẾT (2 cột)
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10f);
            summaryTable.setWidths(new float[]{60, 40});

            for (String line : lines) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) continue;

                // --- Xử lý các dấu phân cách ---
                if (trimmedLine.contains("=============")) {
                    document.add(new Paragraph(line, smallFont));
                    continue;
                }

                // --- Xử lý 3 dòng Header bằng Table để căn chỉnh Ngày lập (Đã fix lỗi OutOfBounds) ---

                if (line.contains("Địa chỉ:") && line.contains("Số HĐ:")) {
                    int index = line.indexOf("Số HĐ:");
                    String left = line.substring(0, index).trim();
                    String right = line.substring(index).trim();
                    document.add(createHeaderTable(left, right, smallFont));
                    continue;
                } else if (line.contains("MST:") && line.contains("Ngày lập:")) {
                    int index = line.indexOf("Ngày lập:");
                    String left = line.substring(0, index).trim();
                    String right = line.substring(index).trim();
                    document.add(createHeaderTable(left, right, smallFont));
                    continue;
                }

                // --- Trạng thái Bảng Tổng kết ---
                if (trimmedLine.startsWith("Phương thức")) {
                    inPaymentDetailSection = false;
                    inSummarySection = true;
                    // Thêm bảng chi tiết vào document trước khi chuyển sang bảng tổng kết
                    document.add(detailTable);
                }

                // --- Trạng thái Bảng Chi tiết ---
                else if (trimmedLine.startsWith("CHI TIẾT THANH TOÁN")) {
                    inPaymentDetailSection = true;
                    inSummarySection = false;
                    document.add(new Paragraph("\n", font));
                    document.add(new Paragraph(trimmedLine, boldFont));
                    continue;
                }

                // --- Ghi nội dung vào PDF ---

                if (inPaymentDetailSection) {
                    if (trimmedLine.startsWith("| STT |")) {
                        // Dòng tiêu đề của bảng Chi tiết
                        String[] headers = trimmedLine.split("\\|", -1);
                        for (int i = 1; i < headers.length - 1; i++) {
                            PdfPCell cell = createCell(headers[i].trim(), boldFont, Element.ALIGN_CENTER);
                            detailTable.addCell(cell);
                        }
                    } else if (trimmedLine.startsWith("|") && trimmedLine.endsWith("|")) {
                        // Dòng dữ liệu Chi tiết thanh toán
                        String[] cols = trimmedLine.substring(1, trimmedLine.length() - 1).split("\\|", -1);
                        for (int i = 0; i < cols.length; i++) {
                            String cellContent = cols[i].trim();
                            int alignment = (i == 0) ? Element.ALIGN_CENTER : // STT
                                    (i == 1) ? Element.ALIGN_LEFT :    // Tên phòng/dịch vụ
                                            Element.ALIGN_RIGHT;              // SL, Đơn giá, Thành tiền

                            detailTable.addCell(createCell(cellContent, font, alignment));
                        }
                    }
                } else if (inSummarySection) {
                    // Xử lý dòng Tổng kết
                    String label = trimmedLine;
                    String value = "";
                    int splitIndex = trimmedLine.lastIndexOf(':');

                    if (splitIndex != -1 && !trimmedLine.contains("|")) {
                        // Dạng "Label: Value"
                        label = trimmedLine.substring(0, splitIndex).trim();
                        value = trimmedLine.substring(splitIndex + 1).trim();
                    } else if (trimmedLine.contains("TỔNG CỘNG")) {
                        // Xử lý dòng "TỔNG CỘNG 2.763,000 đ"
                        label = "TỔNG CỘNG";
                        // Cắt bỏ phần "TỔNG CỘNG" và lấy giá trị còn lại
                        value = trimmedLine.substring(label.length()).trim();
                    }

                    if (!label.equals(trimmedLine) || trimmedLine.contains("Trạng thái") || trimmedLine.contains("Phương thức")) {
                        // Chỉ thêm vào bảng nếu đã phân tích được thành Label và Value, hoặc là dòng tiêu đề
                        Font currentFont = trimmedLine.contains("TỔNG CỘNG") ? boldFont : font;

                        PdfPCell labelCell = createCell(label, currentFont, Element.ALIGN_LEFT);
                        PdfPCell valueCell = createCell(value, currentFont, Element.ALIGN_RIGHT);

                        // Xóa border hoàn toàn cho tất cả các cell trong bảng tổng kết
                        labelCell.setBorder(PdfPCell.NO_BORDER);
                        valueCell.setBorder(PdfPCell.NO_BORDER);

                        summaryTable.addCell(labelCell);
                        summaryTable.addCell(valueCell);
                    }
                } else {
                    // Phần Header (còn lại) và Thông tin khách hàng
                    Paragraph p = new Paragraph(line, font);
                    if (trimmedLine.contains("HÓA ĐƠN THANH TOÁN")) {
                        p.setFont(headerFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                    } else if (trimmedLine.contains("LOUIS HOTEL & RESORT")) {
                        p.setFont(boldFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                    } else if (trimmedLine.startsWith("THÔNG TIN KHÁCH HÀNG")) {
                        p.setFont(boldFont);
                    }
                    document.add(p);
                }
            }

            // Đảm bảo bảng tổng kết được thêm vào nếu nó là phần cuối cùng
            if (inSummarySection) {
                document.add(summaryTable);
            }

            document.close();

            // 4. Thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText("Xuất PDF thành công!");
            alert.setContentText("File hóa đơn đã được lưu.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể tạo PDF");
            alert.setContentText("Có lỗi xảy ra khi tạo file: " + e.getMessage());
            alert.showAndWait();
        }
    }
}