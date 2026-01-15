FROM node:20-alpine

RUN addgroup -S sandbox && adduser -S sandbox -G sandbox
USER sandbox

WORKDIR /app

CMD ["sh", "-c", "echo \"$INPUT\" | node main.js"]
