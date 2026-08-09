#!/bin/bash
set -e

# Install Docker
dnf install -y docker
systemctl enable docker
systemctl start docker
usermod -aG docker ec2-user

# Install the Docker Compose CLI plugin (v2 syntax: "docker compose")
mkdir -p /usr/local/lib/docker/cli-plugins
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
    -o /usr/local/lib/docker/cli-plugins/docker-compose
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Directory where the application will be deployed (next step) —
# actual content (Dockerfile, docker-compose.prod.yml, Caddyfile) is
# uploaded there separately, Terraform only prepares the ground.
mkdir -p /opt/franchise-api
chown ec2-user:ec2-user /opt/franchise-api

# Environment file for docker-compose.prod.yml, pre-filled with the
# real RDS endpoint that Terraform just created.
cat > /opt/franchise-api/.env.prod << EOF
DB_HOST=${db_host}
DB_USER=${db_user}
DB_PASSWORD=${db_password}
EOF
chown ec2-user:ec2-user /opt/franchise-api/.env.prod
chmod 600 /opt/franchise-api/.env.prod