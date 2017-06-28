package com.metasoft.main.Aktarici;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.Model.Instance;
import com.metasoft.main.utils;
import org.dcm4che3.data.Attributes;

import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Cagdas Tunca on 11.04.2017.
 */
public class InstanceAktarim {
    private DatabaseOps db;
    private long dicomattrsPkStart;
    private long pagerSize;
    private String baseFilePath;
    private int pagerStart;

    private String logFileName;
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");

    public InstanceAktarim(DatabaseOps db, long dicomattrsPkStart, long pagerSize, int pagerStart, String baseFilePath) throws Throwable {
        this.db = db;
        this.dicomattrsPkStart = dicomattrsPkStart;
        this.pagerSize = pagerSize;
        this.baseFilePath = baseFilePath;
        this.pagerStart = pagerStart;

        int availPk = db.getDicomattrsAvailablePkStart();
        if (dicomattrsPkStart < availPk) {
            throw new Throwable("PatientAktarim dicomattrsPkStart uygunlugunu kontrol et en yuksek pk dan 1 fazla olmasi lazim en az.");
        }

        Date date = new Date();
        logFileName = dateFormat.format(date) + "_InstanceAktarim.log";
    }

    public void aktar() {
        try {
            int instanceCount = db.getInstanceTableCount();
            for (int i = pagerStart; i < instanceCount; i += pagerSize) {
                try {
                    System.out.println("Instance " + i + "-" + (i + pagerSize));
                    ResultSet rs = db.getInstanceTable(i, i + pagerSize);
                    LinkedList<Instance> instances = new LinkedList<Instance>();
                    while (rs.next()) {
                        try {
                            Instance instance = new Instance(rs.getLong("pk"),rs.getString("storage_path"));
//                                    , rs.getString("sop_cuid"), rs.getString("sop_iuid"), rs.getInt("inst_no"), rs.getInt("num_frames"), rs.getInt("Rows"), rs.getInt("Columns"), rs.getString("storage_path"));
                            String sop_iuid=rs.getString("sop_iuid");

                            String storage_path=instance.getStorage_path().replaceAll("/", "\\\\");

                            if (baseFilePath.endsWith("\\\\")){
                                baseFilePath=baseFilePath.substring(0,baseFilePath.length()-2);
                            }
                            if (storage_path.startsWith("\\\\")){
                                storage_path=storage_path.substring(2,storage_path.length());
                            }

                            File f = new File(baseFilePath+"\\" + storage_path/*instance.getStorage_path().replaceAll("/", "\\\\")*/);
                            if (f.exists()) {
                                String pk4Hex = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.readInstanceRelatedAttributes(f)));

//                                String blob = "0x" + utils.bytesToHex(utils.attributesToByteArray(utils.generateFourthAttributesBlob("ISO_IR 148", instance.getSop_cuid(),
//                                        instance.getSop_iuid(), instance.getInst_no(),
//                                        String.valueOf(instance.getNum_frames().intValue()), String.valueOf(instance.getRows().intValue()),
//                                        String.valueOf(instance.getColumns().intValue()),
//                                        utils.getBitsAllocated(f))));
                               // System.out.println(utils.decodeAttributes(utils.hexStringToByteArray(pk4Hex.replaceAll("0x", ""))));
                               // System.out.println();
                                instance.setBlob(pk4Hex);
                                instances.add(instance);
                            } else {
                                try {
                                    utils.appendLog("sop_iuid",sop_iuid);
                                    utils.appendLog(logFileName, "**************************");
                                    utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "DOSYA BULUNAMADI ->" + baseFilePath +"\\" + instance.getStorage_path().replaceAll("/", "\\\\"));
                                    utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);

                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                utils.appendLog(logFileName, "**************************");
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                            } catch (Exception e1) {
                            }
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
                            try {
                                utils.appendLog(logFileName, "**************************");
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + instance.toString());
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                            } catch (Exception e1) {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        utils.appendLog(logFileName, "**************************");
                        utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "PAGER START bu hata sirasinda su indexteydi:" + i);
                        utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                        utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
                    } catch (Exception e1) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utils.appendLog(logFileName, "**************************");
                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + "En dis try-catch");
                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + db.toString());
                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.getLocalizedMessage());
                utils.appendLog(logFileName, "<InstanceAktarim" + dateFormat.format(new Date()) + "> " + e.toString());
            } catch (Exception e1) {
            }
        }
    }
}
