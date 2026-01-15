FROM python:3.11-alpine

RUN addgroup -S sandbox && adduser -S sandbox -G sandbox
USER sandbox

WORKDIR /app

CMD ["sh", "-c", "echo \"$INPUT\" | python main.py"]
