resource "aws_route53_zone" "primary" {
  name = var.domain_name

  tags = { Name = "primary-zone" }
}

resource "aws_route53_record" "franchise_api" {
  zone_id = aws_route53_zone.primary.zone_id
  name    = "${var.subdomain}.${var.domain_name}"
  type    = "A"
  ttl     = 300
  records = [aws_eip.franchise_api_eip.public_ip]
}