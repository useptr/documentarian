package com.example.documentarian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HtmlReportController {
//    private Path curPath;
    private Path reportPath;
    private String css;
    public HtmlReportController() {
//        curPath = Paths.get("");
        reportPath = Paths.get("report");
        css = "body {\n" +
                "  color: white;\n" +
                "  background-color: #1d1e22;\n" +
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
                "  font-size: 1.2em;\n" +
                "  padding: 10px;\n" +
                "  background-color: #303134;\n" +
                "  display: grid;\n" +
                "  justify-content: start;\n" +
                "  grid-template-columns: auto auto auto;\n" +
                "  column-gap: 10px;\n" +
                "  row-gap: 15px;\n" +
                "}\n" +
                ".modifiers {\n" +
                "  color: #d05a24;\n" +
                "}";
    }
    public String AbsolutePath(Path path) {
        return path.toAbsolutePath().toString();
    }
    public String absoluteReportPath() {
        return AbsolutePath(reportPath);
    }
    public boolean clearReport() {
        boolean reportDirEmptyAndExist = false;
        if (Files.exists(reportPath))
            reportDirEmptyAndExist = deleteDirectory(reportPath.toFile());
        else {
            File newReportDir = new File(absoluteReportPath());
            reportDirEmptyAndExist = newReportDir.mkdir();
        }
        return reportDirEmptyAndExist;
    }
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return true;
    }
    public void createHtmlFileFromClassInstance(ArrayList<FieldDTO> fields) {
        String html = "<!DOCTYPE html>\n<html>\n<body><head>\n<style>";
        html += css;
        html += "</style>\n</head>";
        html += "<div class=\"instance\">\n<div class=\"title\">\n";
        html += "<div class=\"modifiers\">\n" + fields.get(0).modifiers + "</div>\n";
        html += "<div class=\"type\">\n" +  fields.get(0).type + "</div>\n";
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
            html += "<div class=\"value\">";
            if (!field.isBasicType && !field.isArray)
                html += "<a href=\""+ AbsolutePath(reportPath) + "\\" +field.value+ ".html"+"\">" + field.value + "</a>";
            else {
                html += field.value;
            }
            html += "</div>";
        }
        html += "</div>\n</div>";
        html +="</body>\n</html>";
//        System.out.println(html);
        File out = new File(AbsolutePath(reportPath) + "\\" +fields.get(0).value+ ".html");

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
