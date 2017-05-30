package com.metasoft.main.Aktarici;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.Model.Serie;
import com.metasoft.main.utils;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 11.04.2017.
 */
public class SeriesAktarim {
    private DatabaseOps db;
    private long dicomattrsPkStart;
    private long pagerSize;
    private int pagerStart;

    private String logFileName;
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");

    public SeriesAktarim(DatabaseOps db, long dicomattrsPkStart, long pagerSize,int pagerStart) throws Throwable {
        this.db = db;
        this.dicomattrsPkStart = dicomattrsPkStart;
        this.pagerSize = pagerSize;
        this.pagerStart=pagerStart;

        int availPk=db.getDicomattrsAvailablePkStart();
        if (dicomattrsPkStart<availPk){
            throw new Throwable("PatientAktarim dicomattrsPkStart uygunlugunu kontrol et en yuksek pk dan 1 fazla olmasi lazim en az.");
        }

        Date date = new Date();
        logFileName=dateFormat.format(date)+"_SeriesAktarim.log";
    }

    public void aktar(){
        try {
            int seriesCount = db.getSeriesTableCount();
            for (int i = pagerStart; i < seriesCount; i += pagerSize) {
                try {
                    System.out.println("Series " + i + "-" +(i + pagerSize));
                    ResultSet rs = db.getSeriesTable(i, i + pagerSize);
                    LinkedList<Serie> series = new LinkedList<Serie>();
                    while (rs.next()) {
                        try {
                            Serie serie = new Serie(rs.getLong("pk"), rs.getString("modality"), rs.getString("series_iuid"), rs.getInt("series_no"));
                            String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateThirdAttributesBlob("ISO_IR 148", serie.getModality(), serie.getSeries_iuid(), serie.getSeries_no())));
                            serie.setBlob(blob);
                            series.add(serie);
                            System.out.println(utils.decodeAttributes(utils.hexStringToByteArray(blob.replaceAll("0x", ""))));
                            System.out.println();
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                utils.appendLog(logFileName,"**************************");
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                            }catch (Exception e1){}
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
                            try {
                                utils.appendLog(logFileName,"**************************");
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+serie.toString());
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                            }catch (Exception e1){}
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        utils.appendLog(logFileName,"**************************");
                        utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+"PAGER START bu hata sirasinda su indexteydi:"+i);
                        utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                        utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.toString());
                    }catch (Exception e1){}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utils.appendLog(logFileName,"**************************");
                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.getLocalizedMessage());
                utils.appendLog(logFileName,"<SeriesAktarim"+dateFormat.format(new Date())+"> "+e.toString());
            }catch (Exception e1){}
        }
    }
}
