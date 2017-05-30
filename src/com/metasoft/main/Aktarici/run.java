package com.metasoft.main.Aktarici;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.Model.Instance;
import com.metasoft.main.Model.Patient;
import com.metasoft.main.Model.Serie;
import com.metasoft.main.Model.Study;
import com.metasoft.main.utils;
import org.dcm4che3.io.DicomInputStream;

import java.io.File;
import java.net.URI;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 10.04.2017.
 */
public class run {

    private static void printHowToUse() {
        System.out.println("String ip = args[0];");
        System.out.println("String port = args[1];");
        System.out.println("String dbName = args[2];");
        System.out.println("String user = args[3];");
        System.out.println("String pass = args[4];");
        System.out.println("String baseFilePath = args[4];");
        System.out.println("String baseFilePath = args[5];");
        System.out.println("long dicomattrsPkStart =  Long.valueOf(args[6]);");
        System.out.println("long pagerSize = Long.valueOf(args[7]);");
        System.out.println("int pagerStart =Integer.valueOf(args[8]);");
        System.out.println("///Ornek");
        System.out.println("java -jar MetaAktarPACS.jar 192.168.12.132 1433 PACSDB1 sa meta26.soft \\\\192.168.12.132\\MetapacsStorage\\ServerStudies\\ 0 20 0");
    }

    public static void main(String[] args) {
        printHowToUse();
        String ip = args[0];
        String port = args[1];
        String dbName = args[2];
        String user = args[3];
        String pass = args[4];
        String baseFilePath =args[5];
        long dicomattrsPkStart = Long.valueOf(args[6]);
        long pagerSize = Long.valueOf(args[7]);
        int pagerStart = Integer.valueOf(args[8]);

        System.out.println(Arrays.toString(args));
        System.out.println();
        System.out.println();

        DatabaseOps db = new DatabaseOps(ip, port, dbName, user, pass);

//
//        DatabaseOps db = new DatabaseOps("192.168.12.132", "1433", "PACSDB1", "sa", "meta26.soft");//localhost PACSDB2 sa mtsft.1234
//        pagerSize = 20;
//        dicomattrsPkStart = 0;
        //baseFilePath = "\\\\192.168.12.132\\MetapacsStorage\\ServerStudies\\"; //database location.storage_path başına gelecek

        try {
            long start = System.currentTimeMillis();

            PatientAktarim patientAktarim = new PatientAktarim(db, dicomattrsPkStart, pagerSize, pagerStart);
            patientAktarim.aktar();
            long PatientFinish= System.currentTimeMillis();


            long StudyStart= System.currentTimeMillis();
            int x = db.getDicomattrsAvailablePkStart();
            StudyAktarim studyAktarim = new StudyAktarim(db, x, pagerSize, pagerStart);
            studyAktarim.aktar();
            long StudyFinish= System.currentTimeMillis();

            long SeriesStart= System.currentTimeMillis();
            x = db.getDicomattrsAvailablePkStart();
            SeriesAktarim seriesAktarim = new SeriesAktarim(db, x, pagerSize, pagerStart);
            seriesAktarim.aktar();
            long SeriesFinish= System.currentTimeMillis();


            long InstanceStart= System.currentTimeMillis();
            x = db.getDicomattrsAvailablePkStart();
            InstanceAktarim instanceAktarim = new InstanceAktarim(db, x, pagerSize, pagerStart, baseFilePath);
            instanceAktarim.aktar();
            long InstanceFinish= System.currentTimeMillis();

            System.out.println("######################################################");
            System.out.println("Patient  :"+(PatientFinish-start));
            System.out.println("Study    :"+(StudyFinish-StudyStart));
            System.out.println("Series   :"+(SeriesFinish-SeriesStart));
            System.out.println("Instance :"+(InstanceFinish-InstanceStart));
            System.out.println("TOPLAM   :"+(System.currentTimeMillis()-start));
            System.out.println("######################################################");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
