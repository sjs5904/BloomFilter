# BloomFilter
세가지 다른 방식의 Hash Function을 이용해 BloomFilter를 구현해 테스트, 비교한 프로젝트입니다.  
## FNV Hash Function
Deterministic hash functions의 일종으로 64-bit unsigned integer 타입을 이용합니다.
![fnv](https://user-images.githubusercontent.com/49792776/83942457-6e41f600-a82e-11ea-8d90-969f59c1e442.PNG)

## RAN Hash Function
블룸필터의 사이즈보다 큰 최소의 소수를 골라 해시 다음과 같이 랜덤으로 해시값을 생성합니다.  
>algorithm hash is
>>prime := getprime
>>a,b := {0, ... , prime-1} 안의 무작위 숫자
>>hash := 데이터
>>hash := (a*데이터 + b)%prime

## Dynamic Hash Function
## 긍정 오류 예상치 평가
## 블룸필터를 이용한 통계적 추산
## 블룸필터의 효율성
## 커뮤니케이션 비용 
