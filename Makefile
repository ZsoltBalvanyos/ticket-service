build-api:
	cd api && mvn clean package

image-api:
	docker build -t otp-mobil-api ./api

build-ticket:
	cd ticket && mvn clean package

image-ticket:
	docker build -t otp-mobil-ticket ./ticket

build-core:
	cd core && mvn clean package

image-core:
	docker build -t otp-mobil-core ./core

build-partner:
	cd partner && mvn clean package

image-partner:
	docker build -t otp-mobil-partner ./partner

build-all: build-api build-ticket build-core build-partner
image-all: image-api image-ticket image-core image-partner

run-dbs:
	docker-compose up postgres-core postgres-ticket postgres-partner

