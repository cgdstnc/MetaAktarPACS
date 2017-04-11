package com.metasoft.main;

import com.metasoft.main.Model.dbo_Aktarim_Result;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 06.04.2017.
 */
public class DB {

    private Connection connection;

    public DB(String ip, String port, String dbName, String dbUser, String dbPass) {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                    + ip + ":" + port + "/" + dbName, dbUser, dbPass);

            System.out.println("connected");
            this.connection = conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getInfoTableCount() throws SQLException {
        String query = "SELECT    COUNT(*) as count FROM  patient INNER JOIN" +
                "                    patient_id ON patient.patient_id_fk = patient_id.pk INNER JOIN" +
                "                    person_name ON patient.pat_name_fk = person_name.pk INNER JOIN" +
                "                    study ON patient.pk = study.patient_fk INNER JOIN" +
                "                    series ON study.pk = series.study_fk INNER JOIN" +
                "                    instance ON series.pk = instance.series_fk INNER JOIN" +
                "                    location ON instance.pk = location.instance_fk INNER JOIN" +
                "                    aktarim ON location.pk = aktarim.location_fk";
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery(query);
        rs.next();
        return rs.getInt("count");
    }

    protected ResultSet getInfoTable(int start, int stop) throws SQLException {
        String query = "select *\n" +
                "                from  (\n" +
                "                    select ROW_NUMBER() over (order by patient.pk) as rowNum,\n" +
                "                           patient.pk AS patient_pk, study.pk AS study_pk, series.pk AS series_pk, instance.pk AS instance_pk, location.pk AS location_pk, person_name.family_name, person_name.given_name,\n" +
                "                                         patient_id.pat_id, patient.pat_birthdate, patient.pat_sex, patient.pat_custom1, study.study_date, study.study_time, study.accession_no, study.ref_phys_name_fk, study.study_iuid, study.study_id,\n" +
                "                                         series.modality, series.series_iuid, series.series_no, instance.sop_cuid, instance.sop_iuid, instance.inst_no, instance.num_frames, aktarim.Rows, aktarim.Columns\n" +
                "                FROM            patient INNER JOIN\n" +
                "                                         patient_id ON patient.patient_id_fk = patient_id.pk INNER JOIN\n" +
                "                                         person_name ON patient.pat_name_fk = person_name.pk INNER JOIN\n" +
                "                                         study ON patient.pk = study.patient_fk INNER JOIN\n" +
                "                                         series ON study.pk = series.study_fk INNER JOIN\n" +
                "                                         instance ON series.pk = instance.series_fk INNER JOIN\n" +
                "                                         location ON instance.pk = location.instance_fk INNER JOIN \n" +
                "                                         aktarim ON location.pk = aktarim.location_fk\n" +
                "                ) as RowConstrainedResult\n" +
                "                WHERE   RowNum >=+" + start + "\n" +
                "                    AND RowNum < +" + stop + "\n" +
                "                ORDER BY RowNum";

        Statement state = connection.createStatement();
        return state.executeQuery(query);
    }

    protected void insertAttributeHexBlob(long dicomAttrPK1, long dicomAttrPK2, long dicomAttrPK3, long dicomAttrPK4,
                                          String pk1Hex, String pk2Hex, String pk3Hex, String pk4Hex) throws SQLException {
        String query = "SET IDENTITY_INSERT dicomattrs on " +
                " " +
                " " +
                "insert into dicomattrs (pk,attrs) values (" + dicomAttrPK1 + "," + pk1Hex + ") " +
                "insert into dicomattrs (pk,attrs) values (" + dicomAttrPK2 + "," + pk2Hex + ") " +
                "insert into dicomattrs (pk,attrs) values (" + dicomAttrPK3 + "," + pk3Hex + ") " +
                "insert into dicomattrs (pk,attrs) values (" + dicomAttrPK4 + "," + pk4Hex + ") " +
                " " +
                "SET IDENTITY_INSERT dicomattrs off " +
                "";

        Statement state = connection.createStatement();
        state.execute(query);
    }

    protected void insert_dbo_aktarim_Results(LinkedList<dbo_Aktarim_Result> results) {
        for (dbo_Aktarim_Result r : results) {
            try {

                String query = "UPDATE aktarim " +
                        "           SET patientDicomAttrs_fk = " + r.getPatientDicomAttrs_fk() + "," +
                        "               studyDicomAttrs_fk = " + r.getStudyDicomAttrs_fk() + "," +
                        "               seriesDicomAttrs_fk = " + r.getSeriesDicomAttrs_fk() + "," +
                        "               instanceDicomAttrs_fk = " + r.getInstanceDicomAttrs_fk() + "," +
                        "               updateTime = " + System.currentTimeMillis() +
                        "           WHERE location_fk=" + r.getLocation_fk();

                Statement state = connection.createStatement();
                state.execute(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
