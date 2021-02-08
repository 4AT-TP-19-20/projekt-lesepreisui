package sample.backend;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sample.backend.data.Contestant;
import sample.backend.data.Data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Comparator;

public class Export {
    private static PdfPTable table;

    public static void export() {
        table = new PdfPTable(4);

        addHeaderCell("Name");
        addHeaderCell("Klasse");
        addHeaderCell("Lose");
        addHeaderCell("Qualifiziert");

        Data.contestants.sorted(Comparator.comparing(Contestant::getPoints).reversed()).forEach(Export::addContentCell);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("export.pdf"));

            document.open();
            document.add(table);
            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            ex.printStackTrace();
        }
    }

    private static void addHeaderCell(String title) {
        PdfPCell header = addCellWithContent(title);
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
    }

    private static void addContentCell(Contestant contestant) {
        addCellWithContent(contestant.getFirstName() + " " + contestant.getLastName());
        addCellWithContent(contestant.getGrade());
        addCellWithContent(String.valueOf(contestant.getPoints()));
        addCellWithContent(contestant.isQualified() ? "Ja" : "Nein");
    }

    private static PdfPCell addCellWithContent(String content) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(content));
        table.addCell(cell);
        return cell;
    }
}
