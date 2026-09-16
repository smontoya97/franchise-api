resource "aws_db_subnet_group" "franchise_db_subnet_group" {
  name       = "franchise-api-db-subnet-group"
  subnet_ids = data.aws_subnets.default.ids

  tags = { Name = "franchise-api-db-subnet-group" }
}

resource "aws_db_instance" "franchise_db" {
  identifier     = "franchise-api-db"
  engine         = "mysql"
  engine_version = "8.4"

  instance_class    = var.db_instance_class
  allocated_storage = 20
  storage_type      = "gp3"

  db_name  = var.db_name
  username = var.db_username
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.franchise_db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]

  publicly_accessible = false

  multi_az                 = false
  backup_retention_period  = 0
  skip_final_snapshot      = true
  deletion_protection      = false

  allow_major_version_upgrade = true
  apply_immediately            = true

  tags = { Name = "franchise-api-db" }
}