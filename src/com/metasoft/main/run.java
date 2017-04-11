package com.metasoft.main;

import com.metasoft.main.Model.Instance;
import com.metasoft.main.Model.Patient;
import com.metasoft.main.Model.Serie;
import com.metasoft.main.Model.Study;

import java.sql.ResultSet;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 10.04.2017.
 */
public class run {

    public static void main(String[] args) {
       /* String ip=args[0];
        String port=args[1];
        String dbName=args[2];
        String user=args[3];
        String pass=args[4];

        DatabaseOps db=new DatabaseOps(ip, port, dbName, user, pass);*/

        DatabaseOps db = new DatabaseOps("192.168.12.132", "1433", "PACSDB1", "sa", "meta26.soft");
        long dicomattrsPkStart = 0;
        long pagerSize = 20;

        //region PATIENT AKTARIM
        try {
            int patientCount = db.getPatientTableCount();

            for (int i = 0; i < patientCount; i += pagerSize) {
                try {
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
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        //region STUDY AKTARIM
        pagerSize = 20;

        try {
            int studyCount = db.getStudyTableCount();
            for (int i = 0; i < studyCount; i += pagerSize) {
                try {
                    ResultSet rs = db.getStudyTable(i, i + pagerSize);
                    LinkedList<Study> studies = new LinkedList<Study>();
                    while (rs.next()) {
                        try {
                            Study study = new Study(rs.getLong("pk"), rs.getString("study_date"), rs.getString("study_time"), rs.getString("accession_no"), rs.getString("study_iuid"), rs.getString("study_id"));
                            String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateSecondAttributesBlob("ISO_IR 148", study.getStudyDate(), study.getStudyTime(), study.getAccessionNo(), "cgdstnc_Aktarim", "0", study.getStudyIUID(), study.getStudyID())));
                            study.setAttrsBlob(blob);
                            studies.add(study);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (Study study : studies) {
                        try {
                            db.insertAttributeHexBlob(dicomattrsPkStart, study.getAttrsBlob());
                            dicomattrsPkStart++;
                            db.updateDicomAttrsFkForStudy(study.getPk(), dicomattrsPkStart - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(study.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion



        //region SERIES AKTARIM
        pagerSize = 20;

        try {
            int seriesCount = db.getSeriesTableCount();
            for (int i = 0; i < seriesCount; i += pagerSize) {
                try {
                    ResultSet rs = db.getSeriesTable(i, i + pagerSize);
                    LinkedList<Serie> series = new LinkedList<Serie>();
                    while (rs.next()) {
                        try {
                            Serie serie = new Serie(rs.getLong("pk"), rs.getString("modality"), rs.getString("series_iuid"), rs.getInt("series_no"));
                            String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateThirdAttributesBlob("ISO_IR 148", serie.getModality(), serie.getSeries_iuid(), serie.getSeries_no())));
                            serie.setBlob(blob);
                            series.add(serie);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (Serie serie : series) {
                        try {
                            db.insertAttributeHexBlob(dicomattrsPkStart, serie.getBlob());
                            dicomattrsPkStart++;
                            db.updateDicomAttrsFkForSeries(serie.getPk(), dicomattrsPkStart - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(serie.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        //region INSTANCE AKTARIM
        pagerSize = 20;

        try {
            int instanceCount = db.getInstanceTableCount();
            for (int i = 0; i < instanceCount; i += pagerSize) {
                try {
                    ResultSet rs = db.getInstanceTable(i, i + pagerSize);
                    LinkedList<Instance> instances = new LinkedList<Instance>();
                    while (rs.next()) {
                        try {
                            Instance instance = new Instance(rs.getLong("pk"),rs.getString("sop_cuid"),rs.getString("sop_iuid"),rs.getInt("inst_no"),rs.getInt("num_frames"),rs.getInt("Rows"),rs.getInt("Columns"));
                            String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateFourthAttributesBlob("ISO_IR 148",instance.getSop_cuid(),instance.getSop_iuid(),instance.getInst_no(),String.valueOf(instance.getNum_frames().intValue()),String.valueOf(instance.getRows().intValue()),String.valueOf(instance.getColumns().intValue()),"8")));
                            instance.setBlob(blob);
                            instances.add(instance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (Instance instance : instances) {
                        try {
                            db.insertAttributeHexBlob(dicomattrsPkStart, instance.getBlob());
                            dicomattrsPkStart++;
                            db.updateDicomAttrsFkForInstance(instance.getPk(), dicomattrsPkStart - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(instance.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion


    }
}
