package com.example.documentarian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class HtmlReportGenerator {
    private Path reportDir; // путь до дериктории с html отчетом
    private String htmlReport; // html документ
    private String reportFileName; // имя html документа
    private ArrayList<String> instances = new ArrayList<>(); // html разметка для каждого экземпляра класса
    private String css; // css стили html документа
    public HtmlReportGenerator() {
        reportDir = Paths.get("report");
        reportFileName = AbsolutePath(reportDir) + "\\report.html";
        css = "body {\n" +
                "  color: black;\n" +
                "}\n" +
                ".title {\n" +
                "  font-size: 2em;\n" +
                "  font-weight: 600;\n" +
                "  display: grid;\n" +
                "  justify-content: start;\n" +
                "  grid-template-columns: auto auto auto;\n" +
                "  column-gap: 10px;\n" +
                "  row-gap: 15px;\n" +
                "  margin: 10px 0;\n" +
                "}\n" +
                ".fields {\n" +
                "  background-color: #f9fbfd;\n" +
                "  font-size: 1.2em;\n" +
                "  padding: 10px;\n" +
                "  display: inline-grid;\n" +
                "  justify-content: start;\n" +
                "  grid-template-columns: auto auto auto auto;\n" +
                "  column-gap: 10px;\n" +
                "  row-gap: 15px;\n" +
                "}\n" +
                ".modifiers {\n" +
                "  color: #d05a24;\n" +
                "}";
        htmlReport = "<!DOCTYPE html>\n<html>\n<body><head>\n<style>";
        htmlReport += css;
        htmlReport += "</style>\n</head>";
    } // конструктор генерирующий стандартный html документ
    public void addClassInstance(ArrayList<FieldDTO> fields) {

        String instance = "<div class=\"instance\">\n<div class=\"title\">\n";
        instance += "<div class=\"modifiers\">\n" + fields.get(0).modifiers + "</div>\n";
        instance += "<div class=\"type\">\n" +  fields.get(0).type + "</div>\n";
        instance += "<div class=\"value\" ><a name=\""+fields.get(0).value+"\">"+ fields.get(0).value + "</a></div>\n</div>\n";
        instance += "<div class=\"fields\">\n";
        boolean isBaseClass = true;
        for (int i = 1; i < fields.size(); i++) {
            FieldDTO field= fields.get(i);
            instance += "<div class=\"modifiers\">" + field.modifiers + "</div>\n";
            instance += "<div class=\"type\">" + field.type + "</div>\n";
            instance += "<div class=\"name\">"+ field.name + "</div>\n";
            instance += "<div class=\"value\">";
            if (!field.isBasicType && !field.isCollection)
                instance += "<a href=\"#" +field.value+ "\">" + field.value + "</a>";
            else
                instance += field.value;
            instance += "</div>";
        }
        instance += "</div>\n</div>";
        instances.add(instance);
    } // добавление в instances html разметки для экземпляра класса
    public String htmlReportFileName() {
        return reportFileName;
    } // возвращает имя html документа
    public void saveHtmlReport() {
        for (int i = instances.size()-1; i >= 0; i--)
            htmlReport += instances.get(i);
        closeDefaulHtmlTags();
        File out = new File(reportFileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            bw.write(htmlReport);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // сохраняет html документ
    boolean deleteAllFilesInDirectory(File directoryToBeDeleted) {
        for (File file: Objects.requireNonNull(directoryToBeDeleted.listFiles())) {
            file.delete();
        }
        return true;
    } // удаляет все файлы из папки с отчетом
    public String AbsolutePath(Path path) {
        String absolutePath = path.toAbsolutePath().toString();
        return absolutePath;
    }
    public String absoluteReportPath() {
        return AbsolutePath(reportDir);
    }
    public boolean clearReportDir() {
        boolean reportDirEmptyAndExist = false;
        if (Files.exists(reportDir))
            reportDirEmptyAndExist = deleteAllFilesInDirectory(reportDir.toFile());
        else {
            File newReportDir = new File(absoluteReportPath());
            reportDirEmptyAndExist = newReportDir.mkdir();
        }
        return reportDirEmptyAndExist;
    }
    public void  setReportFileName(String instance) {
        reportFileName = AbsolutePath(reportDir) + "\\"+instance+"-report.html";
    }
    public void closeDefaulHtmlTags() {
        htmlReport += "</body>\n</html>";
    }

    public void createHtmlFileFromClassInstance(ArrayList<FieldDTO> fields) {
        String html = "<!DOCTYPE html>\n<html>\n<body><head>\n<style>";
        html += css;
        html += "</style>\n</head>";
        html += "<div class=\"instance\">\n<div class=\"title\">\n";
        html += "<div class=\"modifiers\">\n" + fields.get(0).modifiers + "</div>\n";
        html += "<div class=\"type\">\n" +  fields.get(0).type + "</div>\n";
        html += "<div class=\"name\">"+ fields.get(0).name + "</div>\n";
        html += "<div class=\"value\">"+ fields.get(0).value + "</div>\n</div>\n";

        html += "<div class=\"fields\">\n";
        boolean isBaseClass = true;
        for (FieldDTO field : fields) {
            if (isBaseClass) {
                isBaseClass = false;
                continue;
            }
            html += "<div class=\"modifiers\">" + field.modifiers + "</div>\n";
            html += "<div class=\"type\">" + field.type + "</div>\n";
            html += "<div class=\"name\">"+ field.name + "</div>\n";
            html += "<div class=\"value\">";
            if (!field.isBasicType && !field.isCollection)
                html += "<a href=\""+ AbsolutePath(reportDir) + "\\" +field.value+ ".html"+"\">" + field.value + "</a>";
            else {
                html += field.value;
            }
            html += "</div>";
        }
        html += "</div>\n</div>";
        html +="</body>\n</html>";
//        System.out.println(html);
        File out = new File(AbsolutePath(reportDir) + "\\" +fields.get(0).value+ ".html");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            bw.write(html);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public String curAbsolutePath() {
//        return curPath.toAbsolutePath().toString();
//    }
}
//value += "<a href=\""+ htmlController.absoluteReportPath() + "\\" + getValueInfo(subField.get(subObj))+ ".html"+"\">" + getValueInfo(subField.get(subObj)) + "</a>" + ", ";


//<div class="instance">
//<div class="title">
//<div class="modifiers">
//public
//</div>
//<div class="type">
//        Point
//</div>
//<div class="value">
//        Point@23434
//</div>
//</div>
//<div class="fields">
//<div class="modifiers">
//        modifiers
//</div>
//<div class="type">
//        type
//</div>
//<div class="value">
//        value
//</div>
//
//<div class="modifiers">
//public static
//</div>
//<div class="type">
//        double
//</div>
//<div class="value">
//        10.2345
//</div>
//
//<div class="modifiers">
//public static final
//</div>
//<div class="type">
//        double
//</div>
//<div class="value"> <a href="#">
//        10.2345
//</a>
//</div>
//</div>
//</div>
