package com.metasoft.main.Aktarici;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Cagdas Tunca on 04.04.2017.
 */
public class deneme {

    final static protected char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] attributesToByteArray(Attributes attrs) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        try {
            DicomOutputStream dos = new DicomOutputStream(out, UID.ExplicitVRLittleEndian);
            dos.writeDataset(null, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static void main(String[] args) {
        try {
//            InputStream fis=new FileInputStream(new File("C:\\test2.dcm"));

            DicomInputStream dis = new DicomInputStream(new File("C:\\Users\\Administrator\\Desktop\\1.2.276.0.7230010.3.1.4.351150954.10692.1491392357.407"));
            DicomInputStream.IncludeBulkData includeBulkData = DicomInputStream.IncludeBulkData.URI;
            dis.setIncludeBulkData(includeBulkData);
            dis.setBulkDataDirectory(null);
            dis.setBulkDataFilePrefix("blk");
            dis.setBulkDataFileSuffix(null);

            Attributes attrs = dis.readDataset(-1, -1);
            System.out.println(attrs.getString(Tag.SpecificCharacterSet));
            Attributes pk1 = new Attributes();
            pk1.setString(Tag.SpecificCharacterSet, VR.CS, attrs.getString(Tag.SpecificCharacterSet));//ISO_IR 148
            pk1.setString(Tag.PatientName, VR.PN,"SELAHATTİN^ÜNSAL" /*attrs.getString(Tag.PatientName)*/);
            pk1.setString(Tag.PatientID, VR.LO, "1858889"/*attrs.getString(Tag.PatientID)*/);
            pk1.setString(Tag.PatientBirthDate, VR.DA, "20140314"/*attrs.getString(Tag.PatientBirthDate)*/);
            pk1.setString(Tag.PatientSex, VR.CS, "M"/*attrs.getString(Tag.PatientSex)*/);
            pk1.setString(Tag.PatientComments, VR.LT,"aktarim"/*attrs.getString(Tag.PatientComments)*/);

            Attributes pk2 = new Attributes();
            pk2.setString(Tag.SpecificCharacterSet, VR.CS, attrs.getString(Tag.SpecificCharacterSet));
            pk2.setString(Tag.StudyDate, VR.DA, "20170310"/*attrs.getString(Tag.StudyDate)*/);
            pk2.setString(Tag.StudyTime, VR.TM, "000000.000"/*attrs.getString(Tag.StudyTime)*/);
            pk2.setString(Tag.AccessionNumber, VR.SH, "000000"/*attrs.getString(Tag.AccessionNumber)*/);
            pk2.setString(Tag.ReferringPhysicianName, VR.PN, "cgdstnc"/*attrs.getString(Tag.ReferringPhysicianName)*/);
            pk2.setString(Tag.PatientAge, VR.AS, "0"/*attrs.getString(Tag.PatientAge)*/);
            pk2.setString(Tag.StudyInstanceUID, VR.UI, "1.2.246.512.1000.872295856.1140204905.11143265510833363329" /*attrs.getString(Tag.StudyInstanceUID)*/);
            pk2.setString(Tag.StudyID, VR.SH, "*"/*attrs.getString(Tag.StudyID)*/);

            Attributes pk3 = new Attributes();
            pk3.setString(Tag.SpecificCharacterSet, VR.CS, attrs.getString(Tag.SpecificCharacterSet));
            pk3.setString(Tag.Modality, VR.CS, "IO"/*attrs.getString(Tag.Modality)*/);
            pk3.setString(Tag.SeriesInstanceUID, VR.UI, "1.2.246.512.1001.2024863782.1128529683.2302536272740316815"/*attrs.getString(Tag.SeriesInstanceUID)*/);
            pk3.setInt(Tag.SeriesNumber, VR.IS, -1/*attrs.getString(Tag.SeriesNumber)*/);

            Attributes pk4 = new Attributes();
            pk4.setString(Tag.SpecificCharacterSet, VR.CS, attrs.getString(Tag.SpecificCharacterSet));
            pk4.setString(Tag.SOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.1.3.1"/*attrs.getString(Tag.SOPClassUID)*/);
            pk4.setString(Tag.SOPInstanceUID, VR.UI, "1.2.246.512.1002.1444066067.1294886088.1762403713880326819"/*attrs.getString(Tag.SOPInstanceUID)*/);
            pk4.setInt(Tag.InstanceNumber, VR.IS, -1/*attrs.getString(Tag.InstanceNumber)*/);
            pk4.setString(Tag.NumberOfFrames, VR.IS, "1"/*attrs.getString(Tag.NumberOfFrames)*/);
            pk4.setString(Tag.Rows, VR.US, "1292"/*attrs.getString(Tag.Rows)*/);
            pk4.setString(Tag.Columns, VR.US, "806"/*attrs.getString(Tag.Columns)*/);
            pk4.setString(Tag.BitsAllocated, VR.US, "16"/*attrs.getString(Tag.BitsAllocated)*/);

            String pk1Hex = bytesToHex(attributesToByteArray(pk1));
            String pk2Hex = bytesToHex(attributesToByteArray(pk2));
            String pk3Hex = bytesToHex(attributesToByteArray(pk3));
            String pk4Hex = bytesToHex(attributesToByteArray(pk4));

            System.out.println("=========PK=1=========");
            System.out.println("0x"+pk1Hex);
            System.out.println("=========PK=2=========");
            System.out.println("0x"+pk2Hex);
            System.out.println("=========PK=3=========");
            System.out.println("0x"+pk3Hex);
            System.out.println("=========PK=4=========");
            System.out.println("0x"+pk4Hex);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
