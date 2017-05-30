package com.metasoft.main.Aktarici;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.Model.Study;
import com.metasoft.main.utils;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 11.04.2017.
 */
public class StudyAktarim {
    private DatabaseOps db;
    private long dicomattrsPkStart;
    private long pagerSize;
    private int pagerStart;

    private String logFileName;
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");

    public StudyAktarim(DatabaseOps db, long dicomattrsPkStart, long pagerSize,int pagerStart) throws Throwable {
        this.db = db;
        this.dicomattrsPkStart = dicomattrsPkStart;
        this.pagerSize = pagerSize;
        int availPk=db.getDicomattrsAvailablePkStart();
        this.pagerStart=pagerStart;

        if (dicomattrsPkStart<availPk){
            throw new Throwable("PatientAktarim dicomattrsPkStart uygunlugunu kontrol et en yuksek pk dan 1 fazla olmasi lazim en az.");
        }

        Date date = new Date();
        logFileName=dateFormat.format(date)+"_StudyAktarim.log";
    }

    public void aktar(){
        try {
            int studyCount = db.getStudyTableCount();
            for (int i = pagerStart; i < studyCount; i += pagerSize) {
                try {
                    System.out.println("Study " + i + "-" + (i + pagerSize));
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
                            try {
                                utils.appendLog(logFileName,"**************************");
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                            }catch (Exception e1){}
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
                            try {
                                utils.appendLog(logFileName,"**************************");
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+study.toString());
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                            }catch (Exception e1){}
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        utils.appendLog(logFileName,"**************************");
                        utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                        utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                        utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                    }catch (Exception e1){}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utils.appendLog(logFileName,"**************************");
                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                utils.appendLog(logFileName,"<StudyAktarim"+dateFormat.format(new Date())+"> "+e.toString());
            }catch (Exception e1){}
        }
    }
}
