This is a demo Rest application for Webflux and Spring security. 

To start application, run
````
./gradlew bootJar
./gradlew bootRun
````

To test the API, use Idea scripts in [http-requests](http-requests) or type directly: 

````
curl -X GET --location "http://localhost:8080/currencies" \
-H "Authorization: Basic dXNlcjpwYXNzd29yZA=="
````

````
curl -X POST --location "http://localhost:8080/rateDifference" \
-H "Content-Type: application/json" \
-H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
-d '{
"from": "USD",
"to": "CZK"
}'
````
