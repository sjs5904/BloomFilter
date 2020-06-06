# BloomFilter
세가지 다른 방식의 Hash Function을 이용해 BloomFilter를 구현해 테스트, 비교한 프로젝트입니다.  
## FNV Hash Function
Deterministic hash functions의 일종으로 64-bit unsigned integer 타입을 이용합니다.
![fnv](https://user-images.githubusercontent.com/49792776/83942457-6e41f600-a82e-11ea-8d90-969f59c1e442.PNG)

## RAN Hash Function
블룸필터의 사이즈보다 큰 최소의 소수를 골라 해시 다음과 같이 랜덤으로 해시값을 생성합니다.    
>**algorithm** hash is  
>> prime := getprime  
>> a,b := {0, ... , prime-1} 안의 무작위 숫자  
>> hash := 데이터  
>> hash := (a*데이터 + b)%prime  
>>  
>> **return** hash

## Dynamic Hash Function
필터 사이즈가 변형 가능하도록 랜덤 해쉬함수을 기반으로 일정 갯수 이상의 아이템이 추가될때 필터를 기존 두배의 크기로 새로 만듭니다.

## 긍정 오류 예상치 평가
세개의 블룸필터에 동일한 무작위로 생성된 문자열을 입력한 후 긍정오류의 확률을 계산했습니다.
해쉬함수의 갯수는 4, 8, 10개의 경우를 각각 테스트했습니다.
![test](https://user-images.githubusercontent.com/49792776/83942994-7e100900-a833-11ea-903e-b22ef05bf10e.PNG)


## 블룸필터를 이용한 통계적 추산
## 블룸필터의 효율성
## 커뮤니케이션 비용 
