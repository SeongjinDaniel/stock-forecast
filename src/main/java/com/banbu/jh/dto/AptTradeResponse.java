package com.banbu.jh.dto;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 공공데이터포털 "아파트 매매 실거래가 상세 자료" XML 응답 DTO
 */
@Getter
@Setter
public class AptTradeResponse {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    // -----------------------
    // 내부 클래스 정의
    // -----------------------
    @Getter
    @Setter
    public static class Header {
        /**
         * 결과코드
         */
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        /**
         * 결과메시지
         */
        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {
        /**
         * 한 페이지 결과 수
         */
        @JacksonXmlProperty(localName = "numOfRows")
        private String numOfRows;

        /**
         * 페이지 번호
         */
        @JacksonXmlProperty(localName = "pageNo")
        private String pageNo;

        /**
         * 전체 결과 수
         */
        @JacksonXmlProperty(localName = "totalCount")
        private String totalCount;

        /**
         * 아이템 목록(실 거래 정보 리스트)
         */
        @JacksonXmlProperty(localName = "items")
        private Items items;
    }

    @Getter
    @Setter
    public static class Items {
        /**
         * item 배열
         */
        @JacksonXmlElementWrapper(useWrapping = false) // <items> 하위에 <item>들이 나열됨
        @JacksonXmlProperty(localName = "item")
        private List<Item> itemList;
    }

    @Getter
    @Setter
    public static class Item {
        // -------------------------------------------------------
        // 아래는 공공데이터 포털에서 명시한 모든 응답 필드
        // -------------------------------------------------------

        @JacksonXmlProperty(localName = "sggCd")
        private String sggCd;                  // 법정동시군구코드

        @JacksonXmlProperty(localName = "umdCd")
        private String umdCd;                  // 법정동읍면동코드

        @JacksonXmlProperty(localName = "landCd")
        private String landCd;                 // 법정동지번코드

        @JacksonXmlProperty(localName = "bonbun")
        private String bonbun;                 // 법정동본번코드

        @JacksonXmlProperty(localName = "bubun")
        private String bubun;                  // 법정동부번코드

        @JacksonXmlProperty(localName = "roadNm")
        private String roadNm;                 // 도로명

        @JacksonXmlProperty(localName = "roadNmSggCd")
        private String roadNmSggCd;            // 도로명시군구코드

        @JacksonXmlProperty(localName = "roadNmCd")
        private String roadNmCd;               // 도로명코드

        @JacksonXmlProperty(localName = "roadNmSeq")
        private String roadNmSeq;              // 도로명일련번호코드

        @JacksonXmlProperty(localName = "roadNmbCd")
        private String roadNmbCd;              // 도로명지상지하코드

        @JacksonXmlProperty(localName = "roadNmBonbun")
        private String roadNmBonbun;           // 도로명건물본번호코드

        @JacksonXmlProperty(localName = "roadNmBubun")
        private String roadNmBubun;            // 도로명건물부번호코드

        @JacksonXmlProperty(localName = "umdNm")
        private String umdNm;                  // 법정동

        @JacksonXmlProperty(localName = "aptNm")
        private String aptNm;                  // 단지명

        @JacksonXmlProperty(localName = "jibun")
        private String jibun;                  // 지번

        @JacksonXmlProperty(localName = "excluUseAr")
        private String excluUseAr;             // 전용면적

        @JacksonXmlProperty(localName = "dealYear")
        private String dealYear;               // 계약년도

        @JacksonXmlProperty(localName = "dealMonth")
        private String dealMonth;              // 계약월

        @JacksonXmlProperty(localName = "dealDay")
        private String dealDay;                // 계약일

        @JacksonXmlProperty(localName = "dealAmount")
        private String dealAmount;             // 거래금액(만원)

        @JacksonXmlProperty(localName = "floor")
        private String floor;                  // 층

        @JacksonXmlProperty(localName = "buildYear")
        private String buildYear;              // 건축년도

        @JacksonXmlProperty(localName = "aptSeq")
        private String aptSeq;                 // 단지 일련번호

        @JacksonXmlProperty(localName = "cdealType")
        private String cdealType;              // 해제여부

        @JacksonXmlProperty(localName = "cdealDay")
        private String cdealDay;               // 해제사유발생일

        @JacksonXmlProperty(localName = "dealingGbn")
        private String dealingGbn;             // 거래유형 (중개거래/직거래여부)

        @JacksonXmlProperty(localName = "estateAgentSggNm")
        private String estateAgentSggNm;       // 중개사소재지 (시군구단위)

        @JacksonXmlProperty(localName = "rgstDate")
        private String rgstDate;               // 등기일자

        @JacksonXmlProperty(localName = "aptDong")
        private String aptDong;                // 아파트 동명

        @JacksonXmlProperty(localName = "slerGbn")
        private String slerGbn;                // 매도자 거래주체정보 (개인/법인/공공기관/기타)

        @JacksonXmlProperty(localName = "buyerGbn")
        private String buyerGbn;               // 매수자 거래주체정보 (개인/법인/공공기관/기타)

        @JacksonXmlProperty(localName = "landLeaseholdGbn")
        private String landLeaseholdGbn;       // 토지임대부 아파트 여부 (Y/N)
    }
}