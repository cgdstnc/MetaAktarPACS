package com.metasoft.main;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Cagdas Tunca on 05.04.2017.
 */
public class utils {

    final static protected char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
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

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Attributes decodeAttributes(byte[] b) {
        Attributes result = new Attributes();
        ByteArrayInputStream is = new ByteArrayInputStream(b);
        try {
            DicomInputStream dis = new DicomInputStream(is);
            dis.readFileMetaInformation();
            dis.readAttributes(result, -1, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Attributes generateFirstAttributesBlob(String SpecificCharacterSet, String PatientName, String PatientID, String PatientBirthDate, String PatientSex, String PatientComments) {
        Attributes pk1 = new Attributes();
        pk1.setString(Tag.SpecificCharacterSet, VR.CS, SpecificCharacterSet);
        pk1.setString(Tag.PatientName, VR.PN, PatientName);
        pk1.setString(Tag.PatientID, VR.LO, PatientID);
        pk1.setString(Tag.PatientBirthDate, VR.DA, PatientBirthDate);
        pk1.setString(Tag.PatientSex, VR.CS, PatientSex);
        pk1.setString(Tag.PatientComments, VR.LT, PatientComments);

        return pk1;
    }

    public static Attributes generateSecondAttributesBlob(String SpecificCharacterSet, String StudyDate, String StudyTime, String AccessionNumber, String ReferringPhysicianName, String PatientAge, String StudyInstanceUID, String StudyID) {
        Attributes pk2 = new Attributes();
        pk2.setString(Tag.SpecificCharacterSet, VR.CS, SpecificCharacterSet);
        pk2.setString(Tag.StudyDate, VR.DA, StudyDate);
        pk2.setString(Tag.StudyTime, VR.TM, StudyTime);
        pk2.setString(Tag.AccessionNumber, VR.SH, AccessionNumber);
        pk2.setString(Tag.ReferringPhysicianName, VR.PN, ReferringPhysicianName);
        pk2.setString(Tag.PatientAge, VR.AS, PatientAge);
        pk2.setString(Tag.StudyInstanceUID, VR.UI, StudyInstanceUID);
        pk2.setString(Tag.StudyID, VR.SH, StudyID);

        return pk2;
    }

    public static Attributes generateThirdAttributesBlob(String SpecificCharacterSet, String Modality, String SeriesInstanceUID, int SeriesNumber) {
        Attributes pk3 = new Attributes();
        pk3.setString(Tag.SpecificCharacterSet, VR.CS, SpecificCharacterSet);
        pk3.setString(Tag.Modality, VR.CS, Modality);
        pk3.setString(Tag.SeriesInstanceUID, VR.UI, SeriesInstanceUID);
        pk3.setInt(Tag.SeriesNumber, VR.IS, SeriesNumber);

        return pk3;
    }

    public static Attributes generateFourthAttributesBlob(String SpecificCharacterSet, String SOPClassUID, String SOPInstanceUID, int InstanceNumber, String NumberOfFrames, String Rows, String Columns, String BitsAllocated) {
        Attributes pk4 = new Attributes();
        pk4.setString(Tag.SpecificCharacterSet, VR.CS, SpecificCharacterSet);
        pk4.setString(Tag.SOPClassUID, VR.UI, SOPClassUID);
        pk4.setString(Tag.SOPInstanceUID, VR.UI, SOPInstanceUID);
        pk4.setInt(Tag.InstanceNumber, VR.IS, InstanceNumber);
        pk4.setString(Tag.NumberOfFrames, VR.IS, NumberOfFrames);
        pk4.setString(Tag.Rows, VR.US, Rows);
        pk4.setString(Tag.Columns, VR.US, Columns);
        pk4.setString(Tag.BitsAllocated, VR.US, BitsAllocated);

        return pk4;
    }


    public static void main(String[] args) {
        String hex = "0x08000800435316004F524947494E414C2F5052494D4152592F2F303030310800160055491A00312E322E3834302E31303030382E352E312E342E312E312E37000800180055492000312E322E3834302E31303030382E3230313630373132303930363338373330000800230044410800323031363037313208003300544D0E003039303633382E3731373030302020001300495302003220280008004953020031202800100055530200C8052800110055530200570A28000001555302000800";
        hex = hex.replaceAll("0x", "");
        System.out.println(decodeAttributes(hexStringToByteArray(hex)));
    }
}