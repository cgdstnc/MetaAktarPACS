package com.metasoft.main.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Cagdas Tunca on 07.04.2017.
 */
public class InfoTableEntity {
    //region FIELDS
    private Long rowNum;
    private Long patient_pk;
    private Long study_pk;
    private Long series_pk;
    private Long instance_pk;
    private Long location_pk;
    private String family_name;
    private String given_name;
    private String pat_id;
    private String pat_birthdate;
    private String pat_sex;
    private String pat_custom1;
    private String study_date;
    private String study_time;
    private String accession_no;
    private Long ref_phts_name_fk;
    private String study_iuid;
    private String study_id;
    private String modality;
    private String series_iuid;
    private Integer series_no;
    private String sop_cuid;
    private String sop_iuid;
    private Integer inst_no;
    private Integer num_frames;
    private Integer rows;
    private Integer columns;
    //endregion

    public InfoTableEntity() {
    }

    public InfoTableEntity(ResultSet rs) throws SQLException {
        this.rowNum =rs.getLong(1);
        this.patient_pk = rs.getLong(2);
        this.study_pk = rs.getLong(3);
        this.series_pk = rs.getLong(4);
        this.instance_pk = rs.getLong(5);
        this.location_pk = rs.getLong(6);

        this.family_name = rs.getString(7);
        this.given_name = rs.getString(8);
        this.pat_id = rs.getString(9);
        this.pat_birthdate = rs.getString(10);
        this.pat_sex = rs.getString(11);
        this.pat_custom1 = rs.getString(12);
        this.study_date = rs.getString(13);
        this.study_time = rs.getString(14);
        this.accession_no = rs.getString(15);

        this.ref_phts_name_fk = rs.getLong(16);

        this.study_iuid = rs.getString(17);
        this.study_id = rs.getString(18);
        this.modality = rs.getString(19);
        this.series_iuid = rs.getString(20);

        this.series_no = rs.getInt(21);

        this.sop_cuid = rs.getString(22);
        this.sop_iuid = rs.getString(23);

        this.inst_no =  rs.getInt(24);
        this.num_frames =  rs.getInt(25);
        this.rows =  rs.getInt(26);
        this.columns =  rs.getInt(27);
    }

    public InfoTableEntity(Long rowNum, Long patient_pk, Long study_pk, Long series_pk, Long instance_pk, Long location_pk, String family_name, String given_name, String pat_id, String pat_birthdate, String pat_sex, String pat_custom1, String study_date, String study_time, String accession_no, Long ref_phts_name_fk, String study_iuid, String study_id, String modality, String series_iuid, Integer series_no, String sop_cuid, String sop_iuid, Integer inst_no, Integer num_frames, Integer rows, Integer columns) {
        this.rowNum = rowNum;
        this.patient_pk = patient_pk;
        this.study_pk = study_pk;
        this.series_pk = series_pk;
        this.instance_pk = instance_pk;
        this.location_pk = location_pk;
        this.family_name = family_name;
        this.given_name = given_name;
        this.pat_id = pat_id;
        this.pat_birthdate = pat_birthdate;
        this.pat_sex = pat_sex;
        this.pat_custom1 = pat_custom1;
        this.study_date = study_date;
        this.study_time = study_time;
        this.accession_no = accession_no;
        this.ref_phts_name_fk = ref_phts_name_fk;
        this.study_iuid = study_iuid;
        this.study_id = study_id;
        this.modality = modality;
        this.series_iuid = series_iuid;
        this.series_no = series_no;
        this.sop_cuid = sop_cuid;
        this.sop_iuid = sop_iuid;
        this.inst_no = inst_no;
        this.num_frames = num_frames;
        this.rows = rows;
        this.columns = columns;
    }

    //region GETTER-SETTER
    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public Long getPatient_pk() {
        return patient_pk;
    }

    public void setPatient_pk(Long patient_pk) {
        this.patient_pk = patient_pk;
    }

    public Long getStudy_pk() {
        return study_pk;
    }

    public void setStudy_pk(Long study_pk) {
        this.study_pk = study_pk;
    }

    public Long getSeries_pk() {
        return series_pk;
    }

    public void setSeries_pk(Long series_pk) {
        this.series_pk = series_pk;
    }

    public Long getInstance_pk() {
        return instance_pk;
    }

    public void setInstance_pk(Long instance_pk) {
        this.instance_pk = instance_pk;
    }

    public Long getLocation_pk() {
        return location_pk;
    }

    public void setLocation_pk(Long location_pk) {
        this.location_pk = location_pk;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name==null? "":family_name;
    }

    public String getGiven_name() {
        return given_name==null? "":given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }

    public String getPat_birthdate() {
        return pat_birthdate;
    }

    public void setPat_birthdate(String pat_birthdate) {
        this.pat_birthdate = pat_birthdate;
    }

    public String getPat_custom1() {
        return pat_custom1;
    }

    public void setPat_custom1(String pat_custom1) {
        this.pat_custom1 = pat_custom1;
    }

    public String getStudy_date() {
        return study_date;
    }

    public void setStudy_date(String study_date) {
        this.study_date = study_date;
    }

    public String getStudy_time() {
        return study_time;
    }

    public void setStudy_time(String study_time) {
        this.study_time = study_time;
    }

    public String getAccession_no() {
        return accession_no;
    }

    public void setAccession_no(String accession_no) {
        this.accession_no = accession_no;
    }

    public Long getRef_phts_name_fk() {
        return ref_phts_name_fk;
    }

    public void setRef_phts_name_fk(Long ref_phts_name_fk) {
        this.ref_phts_name_fk = ref_phts_name_fk;
    }

    public String getStudy_iuid() {
        return study_iuid;
    }

    public void setStudy_iuid(String study_iuid) {
        this.study_iuid = study_iuid;
    }

    public String getStudy_id() {
        return study_id;
    }

    public void setStudy_id(String study_id) {
        this.study_id = study_id;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getSeries_iuid() {
        return series_iuid;
    }

    public void setSeries_iuid(String series_iuid) {
        this.series_iuid = series_iuid;
    }

    public Integer getSeries_no() {
        return series_no;
    }

    public void setSeries_no(Integer series_no) {
        this.series_no = series_no;
    }

    public String getSop_cuid() {
        return sop_cuid;
    }

    public void setSop_cuid(String sop_cuid) {
        this.sop_cuid = sop_cuid;
    }

    public String getSop_iuid() {
        return sop_iuid;
    }

    public void setSop_iuid(String sop_iuid) {
        this.sop_iuid = sop_iuid;
    }

    public Integer getInst_no() {
        return inst_no;
    }

    public void setInst_no(Integer inst_no) {
        this.inst_no = inst_no;
    }

    public Integer getNum_frames() {
        return num_frames;
    }

    public void setNum_frames(Integer num_frames) {
        this.num_frames = num_frames;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public String getPat_sex() {
        return pat_sex;
    }

    public void setPat_sex(String pat_sex) {
        this.pat_sex = pat_sex;
    }

    //endregion
}

