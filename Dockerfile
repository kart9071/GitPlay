# Use an official Ubuntu base image
FROM ubuntu:latest

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Update and install necessary packages
RUN apt-get update && apt-get install -y \
    build-essential \
    openjdk-11-jdk \
    python3 \
    python3-pip \
    curl \
    git \
    g++ \
    python3-venv \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Install Node.js (v20)
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

# Upgrade npm to the latest version
RUN npm install -g npm@latest

# Create a virtual environment for Python dependencies
RUN python3 -m venv /workspace/venv

# Activate virtual environment and install Flask
RUN /workspace/venv/bin/pip install flask

# Copy the Flask API script into the container
COPY api.py /workspace/api.py

# Set the working directory
WORKDIR /workspace

# Expose the Flask API port
EXPOSE 5000

# Use an explicit shell command to run Flask in the virtual environment
CMD ["/workspace/venv/bin/python3", "api.py"]
