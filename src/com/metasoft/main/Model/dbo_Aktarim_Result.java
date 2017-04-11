package com.metasoft.main.Model;

/**
 * Created by Cagdas Tunca on 07.04.2017.
 */
public class dbo_Aktarim_Result {
    private Long location_fk;
    private Long patientDicomAttrs_fk;
    private Long studyDicomAttrs_fk;
    private Long seriesDicomAttrs_fk;
    private Long instanceDicomAttrs_fk;
    private String mesaj;

    public dbo_Aktarim_Result(Long location_fk, Long patientDicomAttrs_fk, Long studyDicomAttrs_fk, Long seriesDicomAttrs_fk, Long instanceDicomAttrs_fk, String mesaj) {
        this.location_fk = location_fk;
        this.patientDicomAttrs_fk = patientDicomAttrs_fk;
        this.studyDicomAttrs_fk = studyDicomAttrs_fk;
        this.seriesDicomAttrs_fk = seriesDicomAttrs_fk;
        this.instanceDicomAttrs_fk = instanceDicomAttrs_fk;
        this.mesaj = mesaj;
    }

    public Long getLocation_fk() {
        return location_fk;
    }

    public Long getPatientDicomAttrs_fk() {
        return patientDicomAttrs_fk;
    }

    public Long getStudyDicomAttrs_fk() {
        return studyDicomAttrs_fk;
    }

    public Long getSeriesDicomAttrs_fk() {
        return seriesDicomAttrs_fk;
    }

    public Long getInstanceDicomAttrs_fk() {
        return instanceDicomAttrs_fk;
    }

    public String getMesaj() {
        return mesaj;
    }
}
