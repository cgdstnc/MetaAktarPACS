package com.metasoft.main;

import com.metasoft.main.Model.InfoTableEntity;
import com.metasoft.main.Model.dbo_Aktarim_Result;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 31.03.2017.
 */
public class main {


    public static void main(String[] args) {
        DB db = new DB("192.168.12.132", "1433", "PACSDB1", "sa", "meta26.soft");
        try {
            int count = db.getInfoTableCount();
            long pk = 10;
            for (int i = 0; i < count; i += 20) {
                ResultSet rs = db.getInfoTable(i, i + 20);
                LinkedList<dbo_Aktarim_Result> results = new LinkedList<dbo_Aktarim_Result>();
                while (rs.next()) {
                    try {
                        InfoTableEntity ite = new InfoTableEntity(rs);

                        String blob1 = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateFirstAttributesBlob("ISO_IR 148", ite.getFamily_name() + " " + ite.getGiven_name(), ite.getPat_id(), ite.getPat_birthdate(), ite.getPat_sex(), ite.getPat_custom1())));
                        String blob2 = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateSecondAttributesBlob("ISO_IR 148", ite.getStudy_date(), ite.getStudy_time(), ite.getAccession_no(), String.valueOf(ite.getRef_phts_name_fk()), "0", ite.getStudy_iuid(), ite.getStudy_id())));
                        String blob3 = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateThirdAttributesBlob("ISO_IR 148", ite.getModality(), ite.getSeries_iuid(), ite.getSeries_no())));
                        String blob4 = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateFourthAttributesBlob("ISO_IR 148", ite.getSop_cuid(), ite.getSop_iuid(), ite.getInst_no(), ite.getNum_frames().toString(), ite.getRows().toString(), ite.getColumns().toString(), "16")));

                        db.insertAttributeHexBlob(pk, pk + 1, pk + 2, pk + 3, blob1, blob2, blob3, blob4);
                        dbo_Aktarim_Result tmp = new dbo_Aktarim_Result(ite.getLocation_pk(),pk, pk + 1, pk + 2, pk + 3, "");
                        results.add(tmp);
                        pk += 4;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    db.insert_dbo_aktarim_Results(results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
