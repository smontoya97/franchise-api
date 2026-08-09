resource "aws_security_group" "ec2_sg" {
  name        = "franchise-api-ec2-sg"
  description = "Security group for the Franchise API EC2 instance"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description = "SSH - restricted to your IP only"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.admin_ip_cidr]
  }

  ingress {
    description = "Public HTTP - served by Caddy"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Public HTTPS - reserved for future use once a domain is added"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "franchise-api-ec2-sg" }
}

resource "aws_security_group" "rds_sg" {
  name        = "franchise-api-rds-sg"
  description = "Security group for the Franchise API RDS instance"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description     = "MySQL - only reachable from the app EC2 instance"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "franchise-api-rds-sg" }
}