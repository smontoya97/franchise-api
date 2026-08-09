output "ec2_public_ip" {
  description = "Fixed public IP of the EC2 instance — use this to reach the API"
  value       = aws_eip.franchise_api_eip.public_ip
}

output "rds_endpoint" {
  description = "RDS endpoint (internal use only — not reachable from the internet)"
  value       = aws_db_instance.franchise_db.address
}

output "ssh_command" {
  description = "Ready-to-use SSH command to connect to the instance"
  value       = "ssh -i /path/to/${var.key_pair_name}.pem ec2-user@${aws_eip.franchise_api_eip.public_ip}"
}