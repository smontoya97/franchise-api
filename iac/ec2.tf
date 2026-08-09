resource "aws_instance" "franchise_api" {
  ami                    = data.aws_ami.amazon_linux.id
  instance_type          = var.instance_type
  key_name               = var.key_pair_name
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  subnet_id               = data.aws_subnets.default.ids[0]

  user_data = templatefile("${path.module}/user_data.sh.tpl", {
    db_host     = aws_db_instance.franchise_db.address
    db_user     = var.db_username
    db_password = var.db_password
  })

  tags = { Name = "franchise-api-ec2" }
}

resource "aws_eip" "franchise_api_eip" {
  instance = aws_instance.franchise_api.id
  domain   = "vpc"

  tags = { Name = "franchise-api-eip" }
}