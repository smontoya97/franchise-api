variable "aws_region" {
  description = "AWS region where all resources are deployed"
  type        = string
  default     = "us-east-1"
}

variable "admin_ip_cidr" {
  description = "Your public IP in CIDR format (e.g. 200.1.2.3/32) — the only origin allowed for SSH"
  type        = string
}

variable "db_name" {
  type    = string
  default = "franchisedb"
}

variable "db_username" {
  type      = string
  default   = "franchise_app"
  sensitive = true
}

variable "db_password" {
  description = "Database password — never commit this, it belongs in terraform.tfvars (gitignored)"
  type        = string
  sensitive   = true
}

variable "key_pair_name" {
  description = "Name of an EC2 key pair that already exists in your AWS account"
  type        = string
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "db_instance_class" {
  type    = string
  default = "db.t4g.micro"
}