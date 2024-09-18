PORT ?= 8000
IMAGE_NAME = hero-randomizer-api

build:
	@sh ./mvnw clean package -DskipTests
	@docker build -t $(IMAGE_NAME) .

run:
	@docker run --rm -p $(PORT):8080 -v $(PWD)/data:/app/data $(IMAGE_NAME)

clean:
	@rm -rf target
	@docker rmi -f $(IMAGE_NAME)

.PHONY: build run clean