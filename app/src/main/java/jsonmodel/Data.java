package jsonmodel;

import java.util.Map;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class Data {
    private String id ;
    private LiftParams running_params;
    private String ETE ;
    private String ESCB ;
    private String EULA ;
    private String EDLA ;
    private String EOD ;
    private String ERO ;
    private String EROCD ;
    private String ELOD ;
    private String ENLS ;
    private String EAB ;
    private String RTC ;
    private String RRC ;
    private String RSC ;
    private String RRS ;
    private String RDS ;
    private String RLU ;
    private String RLD ;
    private String RLS ;
    private String RUL ;
    private String RDL ;
    private String RAB ;

    public void setRunning_params(LiftParams running_params) {
        this.running_params = running_params;
    }

    public LiftParams getRunning_params() {

        return running_params;
    }

    public void setEAB(String EAB) {
        this.EAB = EAB;
    }

    public void setEDLA(String EDLA) {
        this.EDLA = EDLA;
    }

    public void setELOD(String ELOD) {
        this.ELOD = ELOD;
    }

    public void setENLS(String ENLS) {
        this.ENLS = ENLS;
    }

    public void setEOD(String EOD) {
        this.EOD = EOD;
    }

    public void setERO(String ERO) {
        this.ERO = ERO;
    }

    public void setEROCD(String EROCD) {
        this.EROCD = EROCD;
    }

    public void setESCB(String ESCB) {
        this.ESCB = ESCB;
    }

    public void setETE(String ETE) {
        this.ETE = ETE;
    }

    public void setEULA(String EULA) {
        this.EULA = EULA;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRAB(String RAB) {
        this.RAB = RAB;
    }

    public void setRDL(String RDL) {
        this.RDL = RDL;
    }

    public void setRDS(String RDS) {
        this.RDS = RDS;
    }

    public void setRLD(String RLD) {
        this.RLD = RLD;
    }

    public void setRLS(String RLS) {
        this.RLS = RLS;
    }

    public void setRLU(String RLU) {
        this.RLU = RLU;
    }

    public void setRRC(String RRC) {
        this.RRC = RRC;
    }

    public void setRRS(String RRS) {
        this.RRS = RRS;
    }

    public void setRSC(String RSC) {
        this.RSC = RSC;
    }

    public void setRTC(String RTC) {
        this.RTC = RTC;
    }

    public void setRUL(String RUL) {
        this.RUL = RUL;
    }


    public String getEAB() {
        return EAB;
    }

    public String getEDLA() {
        return EDLA;
    }

    public String getELOD() {
        return ELOD;
    }

    public String getENLS() {
        return ENLS;
    }

    public String getEOD() {
        return EOD;
    }

    public String getERO() {
        return ERO;
    }

    public String getEROCD() {
        return EROCD;
    }

    public String getESCB() {
        return ESCB;
    }

    public String getETE() {
        return ETE;
    }

    public String getEULA() {
        return EULA;
    }

    public String getId() {
        return id;
    }

    public String getRAB() {
        return RAB;
    }

    public String getRDL() {
        return RDL;
    }

    public String getRDS() {
        return RDS;
    }

    public String getRLD() {
        return RLD;
    }

    public String getRLS() {
        return RLS;
    }

    public String getRLU() {
        return RLU;
    }

    public String getRRC() {
        return RRC;
    }

    public String getRRS() {
        return RRS;
    }

    public String getRSC() {
        return RSC;
    }

    public String getRTC() {
        return RTC;
    }

    public String getRUL() {
        return RUL;
    }

    @Override
    public String toString() {
        return  ETE + " " +
                ESCB + " " +
                EULA+ " " +
                EDLA+ " " +
                EOD+ " " +
                ERO+ " " +
                ERO+ " " +
                ELOD+ " " +
                ENLS+ " " +
                EAB+ " " +
                RTC+ " " +
                RRC+ " " +
                RSC+ " " +
                RRS+ " " +
                RDS+ " " +
                RLU+ " " +
                RLD+ " " +
                RLS+ " " +
                RUL+ " " +
                RDL+ " " +
                RAB+ " " +
                id + " " +
                running_params;
    }
}
