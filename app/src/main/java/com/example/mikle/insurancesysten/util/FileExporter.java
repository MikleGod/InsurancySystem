package com.example.mikle.insurancesysten.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.example.mikle.insurancesysten.dao.IndividualClientDao;
import com.example.mikle.insurancesysten.dao.LegalClientDao;
import com.example.mikle.insurancesysten.entity.IndividualClient;
import com.example.mikle.insurancesysten.entity.LegalClient;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class FileExporter {
    public static void export(ClientsDTO dto) {
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "myData.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            createLegalList(workbook, dto.legalClients);
            createIndividualList(workbook, dto.individualClients);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static void createIndividualList(WritableWorkbook workbook, List<IndividualClient> individualClients) throws WriteException {
        WritableSheet sheet = workbook.createSheet("individualClients", 1);
        // column and row
        sheet.addCell(new Label(0, 0, "FullName"));
        sheet.addCell(new Label(1, 0, "BirthDate"));
        sheet.addCell(new Label(2, 0, "Sex"));
        sheet.addCell(new Label(3, 0, "DrivingExperience"));
        sheet.addCell(new Label(4, 0, "Address"));
        sheet.addCell(new Label(5, 0, "Phone"));

        for (int i = 1; i <= individualClients.size(); i++) {
            IndividualClient individualClient = individualClients.get(i-1);
            sheet.addCell(new Label(0, i, individualClient.getFullName()));
            sheet.addCell(new Label(1, i, individualClient.getBirthDate()));
            sheet.addCell(new Label(2, i, individualClient.getSex()));
            sheet.addCell(new Label(3, i, individualClient.getDrivingExperience()+""));
            sheet.addCell(new Label(4, i, individualClient.getAddress()));
            sheet.addCell(new Label(5, i, individualClient.getPhone()));
        }
    }

    private static void createLegalList(WritableWorkbook workbook, List<LegalClient> legalClients) throws WriteException {
        WritableSheet sheet = workbook.createSheet("legalClients", 0);
        // column and row
        sheet.addCell(new Label(0, 0, "Name"));
        sheet.addCell(new Label(1, 0, "UniqueNumber"));
        sheet.addCell(new Label(2, 0, "Director"));
        sheet.addCell(new Label(3, 0, "Accountant"));
        sheet.addCell(new Label(4, 0, "Address"));
        sheet.addCell(new Label(5, 0, "Phone"));

        for (int i = 1; i <= legalClients.size(); i++) {
            LegalClient legalClient = legalClients.get(i-1);
            sheet.addCell(new Label(0, i, legalClient.getName()));
            sheet.addCell(new Label(1, i, legalClient.getUniqueNumber()));
            sheet.addCell(new Label(2, i, legalClient.getDirector()));
            sheet.addCell(new Label(3, i, legalClient.getAccountant()+""));
            sheet.addCell(new Label(4, i, legalClient.getAddress()));
            sheet.addCell(new Label(5, i, legalClient.getPhone()));
        }

    }

    public static class ClientsDTO{
        private List<IndividualClient> individualClients;
        private List<LegalClient> legalClients;

        public ClientsDTO(@NotNull List<LegalClient> lClients, @NotNull List<IndividualClient> iClients) {
            this.individualClients = iClients;
            this.legalClients = lClients;

        }

        public List<IndividualClient> getIndividualClients() {
            return individualClients;
        }

        public void setIndividualClients(List<IndividualClient> individualClients) {
            this.individualClients = individualClients;
        }

        public List<LegalClient> getLegalClients() {
            return legalClients;
        }

        public void setLegalClients(List<LegalClient> legalClients) {
            this.legalClients = legalClients;
        }
    }
}
