package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherClient {

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getTodayWeather() {
        ResponseEntity<WeatherDto[]> responseEntity =
                restTemplate.getForEntity(buildWeatherApiUri(), WeatherDto[].class);

        WeatherDto[] weatherArray = responseEntity.getBody();
//        2. 리팩토링 퀴즈 - 불필요한 if-else 피하기
//        패키지 package org.example.expert.client; 의 WeatherClient 클래스에 있는
//        getTodayWeather() 중 아래의 코드 부분을 리팩토링해주세요.
//        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//            throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
//        } else {
//            if (weatherArray == null || weatherArray.length == 0) {
//                throw new ServerException("날씨 데이터가 없습니다.");
//            }
//        }

        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
        }
        if (weatherArray == null || weatherArray.length == 0) {
            throw new ServerException("날씨 데이터가 없습니다.");
        }


        String today = getCurrentDate();

        for (WeatherDto weatherDto : weatherArray) {
            if (today.equals(weatherDto.getDate())) {
                return weatherDto.getWeather();
            }
        }

        throw new ServerException("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.");
    }

    private URI buildWeatherApiUri() {
        return UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return LocalDate.now().format(formatter);
    }
}
