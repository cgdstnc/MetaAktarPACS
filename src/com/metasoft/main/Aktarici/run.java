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
import java.util.Calendar;
import java.util.Date;
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
        String baseFilePath = "\\\\192.168.12.132\\MetapacsStorage\\ServerStudies\\"; //database location.storage_path başına gelecek

        try {
            PatientAktarim patientAktarim= new PatientAktarim(db,dicomattrsPkStart,20,0);
            patientAktarim.aktar();
            int x =db.getDicomattrsAvailablePkStart();

            StudyAktarim studyAktarim=new StudyAktarim(db,x,20,0);
            studyAktarim.aktar();

            x =db.getDicomattrsAvailablePkStart();

            SeriesAktarim seriesAktarim = new SeriesAktarim(db,x,20,0);
            seriesAktarim.aktar();

            x =db.getDicomattrsAvailablePkStart();
            InstanceAktarim instanceAktarim= new InstanceAktarim(db,x,20,0,baseFilePath);
            instanceAktarim.aktar();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
