package com.banbu.jh.feign;

import com.banbu.jh.dto.AptTradeResponse;
import com.banbu.jh.dto.AptLottoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "aptTradeClient", url = "https://api.odcloud.kr/api")
public interface AptTradeFeignClient {

    /**
     * 아파트 매매 실거래가 상세 자료 조회
     *
     * @param serviceKey 공공데이터포털에서 발급받은 인증키 (URL Encode)
     * @param pageNo     페이지번호
     * @param numOfRows  한 페이지 결과 수
     * @param LAWD_CD    지역코드 (행정표준코드 중 앞 5자리)
     * @param DEAL_YMD   계약년월 (6자리, 예: 202407)
     * @return AptTradeResponse (XML 파싱 결과)
     */
    @GetMapping(
            value = "/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    AptTradeResponse getAptTradeDetail(
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("LAWD_CD") String LAWD_CD,
            @RequestParam("DEAL_YMD") String DEAL_YMD
    );

    @GetMapping("/AptTradeSvc/v1/getAptTradingInfo")
    AptTradeResponse getAptTradeData(
            @RequestParam("LAWD_CD") String lawdCd,
            @RequestParam("DEAL_YMD") String dealYmd,
            @RequestParam("serviceKey") String serviceKey);

    @GetMapping("/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail")
    AptLottoResponse getAptLottoData(
            @RequestParam("page") int page,
            @RequestParam("perPage") int perPage,
            @RequestParam("serviceKey") String serviceKey);
}