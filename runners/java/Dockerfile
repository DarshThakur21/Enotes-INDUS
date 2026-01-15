FROM eclipse-temurin:17-jdk-alpine

# 1️⃣ Create sandbox user
RUN addgroup -S sandbox && adduser -S sandbox -G sandbox

# 2️⃣ Switch to non-root user
USER sandbox

# 3️⃣ Working directory (read-only mount later)
WORKDIR /app

# 4️⃣ Execute user code
CMD ["sh", "-c", "javac Main.java && echo \"$INPUT\" | java Main"]
