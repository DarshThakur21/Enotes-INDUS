FROM gcc:13

# Create unprivileged user
RUN useradd -m sandbox
USER sandbox

WORKDIR /app

CMD ["sh", "-c", "g++ main.cpp -o /run/main && echo \"$INPUT\" | /run/main"]
