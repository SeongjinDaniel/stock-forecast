package com.banbu.jh.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor
public class AptLottoResponse {
    private int currentCount;
    private List<Item> data;
    private int matchCount;
    private int page;
    private int perPage;
    private int totalCount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("HOUSE_MANAGE_NO")
        private String HOUSE_MANAGE_NO;
        
        @JsonProperty("SUBSCRPT_AREA_CODE_NM")
        private String SUBSCRPT_AREA_CODE_NM;
        
        @JsonProperty("RCEPT_BGNDE")
        private String RCEPT_BGNDE;
        
        @JsonProperty("RCEPT_ENDDE")
        private String RCEPT_ENDDE;
        
        @JsonProperty("HOUSE_NM")
        private String HOUSE_NM;
        
        @JsonProperty("HOUSE_DTL_SECD_NM")
        private String HOUSE_DTL_SECD_NM;
        
        @JsonProperty("PBLANC_URL")
        private String PBLANC_URL;
        
        @JsonProperty("BSNS_MBY_NM")
        private String BSNS_MBY_NM;
        
        @JsonProperty("CNSTRCT_ENTRPS_NM")
        private String CNSTRCT_ENTRPS_NM;
        
        @JsonProperty("TOT_SUPLY_HSHLDCO")
        private String TOT_SUPLY_HSHLDCO;
        
        @JsonProperty("HSSPLY_ADRES")
        private String HSSPLY_ADRES;
    }
} 