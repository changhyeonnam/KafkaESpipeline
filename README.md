## KafkaESpipline

Kafka와 ElasticSearch(OpenSearch)을 이용해서 간단한 pipeline 및 웹 어플리케이션을 Springboot 기반으로 작성하고 있습니다.


### Issues

개발하면서, 경험했던 에러들의 원인과 어떻게 디버깅 했는지에대해 자세하게 작성하고 있습니다.

**현재 해결한 에러들**
- @KafkaListener로 리스너 구현시, 함수 인자 타입에 대한 에러
- ElasticSearch: Unable to parse response body for Response 과 관련된 에러
- Limit of total fields [1000] has been exceeded와 관련된 에러

### Branch

각 기능 개발별로 브랜치를 나눠서, PR를 날리고 있습니다.

**현재 개발한 기능들**

*[x] Springboot에 GET request for index.html 띄우기

*[x] poll 메서드 기반 Kafka Consumer 구현

*[x] Kafka Listener로 데이터 받아오고, OpenSearch에 데이터 저장 및 Get 요청으로 데이터 불러와서 웹으로 띄우기

* [ ] OpenSearch에서 가져온 데이터 table로 출력하기.