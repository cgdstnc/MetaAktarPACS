package com.metasoft.main.Aktarici;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.Model.Patient;
import com.metasoft.main.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 11.04.2017.
 */
public class PatientAktarim {
    private DatabaseOps db;
    private long dicomattrsPkStart;
    private long pagerSize;
    private int pagerStart;

    private String logFileName;
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");

    public PatientAktarim(DatabaseOps db, long dicomattrsPkStart, long pagerSize, int pagerStart) throws Throwable {
        this.db = db;
        this.dicomattrsPkStart = dicomattrsPkStart;
        this.pagerSize = pagerSize;
        this.pagerStart = pagerStart;

        int availPk = db.getDicomattrsAvailablePkStart();
        if (dicomattrsPkStart < availPk) {
            throw new Throwable("PatientAktarim dicomattrsPkStart uygunlugunu kontrol et en yuksek pk dan 1 fazla olmasi lazim en az.");
        }


        Date date = new Date();
        logFileName = dateFormat.format(date) + "_PatientAktarim.log";
    }

    public void aktar() {
        try {
            int patientCount = db.getPatientTableCount();

            for (int i = pagerStart; i < patientCount; i += pagerSize) {
                try {
                    System.out.println("Patient " + i + "-" + (i + pagerSize));
                    //tabloyu paging kullanarak cek
                    ResultSet rs = db.getPatientTable(i, i + pagerSize);
                    LinkedList<Patient> patients = new LinkedList<Patient>();
                    while (rs.next()) {
                        //tablodaki her kayit icin nesne olustur hex degerini hesapla listeye ekle
                        try {
                            Patient patient = new Patient(rs.getLong("pk"), rs.getString("family_name"), rs.getString("given_name"), rs.getString("pat_id"), rs.getString("pat_birthdate"), rs.getString("pat_sex"), "Aktarim:" + System.currentTimeMillis() + ",\nYorum:" + rs.getString("pat_custom1"));
                            String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateFirstAttributesBlob("ISO_IR 148", patient.getName(), patient.getPatientId(), patient.getPatientBirthDate(), patient.getPatientSex(), patient.getPatientComments())));
                            patient.setAttrsBlob(blob);
                            patients.add(patient);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                utils.appendLog(logFileName, "**************************");
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                            } catch (Exception e1) {
                            }
                        }
                    }
                    // Listeyi dolas dicomAttrs tablosuna dicomattrsPkStart dan baslayarak ekle ve degeri arttır.
                    // Sonra dicomAttrs'ın pk sını patient tablosundaki dicomAttrsFk ya yaz
                    for (Patient patient : patients) {
                        try {
                            db.insertAttributeHexBlob(dicomattrsPkStart, patient.getAttrsBlob());
                            dicomattrsPkStart++;
                            db.updateDicomAttrsFkForPatient(patient.getPk(), dicomattrsPkStart - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(patient.toString());
                            try {
                                utils.appendLog(logFileName, "**************************");
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + "dicomattrsPkStart bu hata sirasinda su indexteydi:" + dicomattrsPkStart);
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + patient.toString());
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                            } catch (Exception e1) {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        utils.appendLog(logFileName, "**************************");
                        utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                        utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + "dicomattrsPkStart bu hata sirasinda su indexteydi:" + dicomattrsPkStart);
                        utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                        utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                    } catch (Exception e1) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                utils.appendLog(logFileName, "**************************");
                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                utils.appendLog(logFileName, "<PatientAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
            } catch (Exception e1) {
            }
        }
    }

    public long getDicomattrsPkStart() {
        return dicomattrsPkStart;
    }
}
