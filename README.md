# yesterday-s-weather


<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/MariaDB-1F305F?style=flat-square&logo=MariaDB&logoColor=white"/> <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white"/> <img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJIDEA&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=AmazonEC2&logoColor=white"/>

<h3> Spring Boot 기반 스케쥴러 구현 </h3>

<p>
  <b>"어제의 날씨"</b> 프로젝트의 가장 큰 문제는 공공데이터 포털에서 제공하는 기상청 API에서 오늘의 일기예보 데이터 만을 제공한다는 점 이었습니다. 다시말해, 과거에 내려진 기상예보의 경우 API에서 조회를 해올 수 없다는 점 이었습니다. 저는 이런 문제를 해결 하기 위해, 일정 시간이 되면 기상청 api에서 일기예보를 조회해서 Server DB에 저장하는 Scheduler 구현을 최우선 과제로 정하고 구현 하였습니다.    
</p>

<img src="./README/scheduler.png" style="width:900px; height:40%; display:inline-block; margin:auto "/>

<p>
매일 일정한 시간에 로직을 수행하기 위해 Spring Scheduler에 cron식을 사용하여 매일 아침 6시에 데이터가 저장되도록 하였습니다.
</p>
