job "esignsante_fse_psc" {
	datacenters = ["${datacenter}"]
	type = "service"
	vault {
		policies = ["esignsante-fse-psc"]
		change_mode = "restart"
	}

	group "esignsante_fse_psc-servers" {
		count = "1"
		restart {
			attempts = 3
			delay = "60s"
			interval = "1h"
			mode = "fail"
		}
		
		network {
				port "http" { to = 8080 }
		}

		update {
			max_parallel      = 1
			canary            = 1
			min_healthy_time  = "30s"
			progress_deadline = "5m"
			healthy_deadline  = "2m"
			auto_revert       = true
			auto_promote      = false
		}
			
		task "esignsante_fse_psc" {
			env {
				JAVA_TOOL_OPTIONS="${user_java_opts} -Dspring.config.location=/secrets/application.properties -Dspring.profiles.active=${swagger_ui} -Dhttp.proxyHost=${proxy_host} -Dhttps.proxyHost=${proxy_host} -Dhttp.proxyPort=${proxy_port} -Dhttps.proxyPort=${proxy_port} -Dhttp.nonProxyHosts=${non_proxy_host_list}"
			}
			driver = "docker"
			config {
				image = "${artifact.image}:${artifact.tag}"
				ports = [ "http" ]
			}
			template {
data = <<EOF
logging.level.org.springframework=ERROR
logging.level.fr.ans=INFO

spring.servlet.multipart.max-file-size=${spring_http_multipart_max_file_size}
spring.servlet.multipart.max-request-size=${spring_http_multipart_max_request_size}
spring.application.name=esignsante_psc

server.servlet.context-path=/esignsante-psc
{{with secret "esignsante-fse-psc/esignsante_ws"}}
esignsante.webservices.signature.confId={{.Data.data.signature_confid}}
esignsante.webservices.signature.secret={{.Data.data.signature_secret}}
esignsante.webservices.proof.confId={{.Data.data.proof_confid}}
esignsante.webservices.checksignature={{.Data.data.checksignature}}
esignsante.webservices.appliantId={{.Data.data.appliantid}}
esignsante.webservices.proofTag={{.Data.data.prooftag}}

#Conf. pour fse (pkcs7)
esignsante.webservices.signature.fse.confId={{.Data.data.signature_confid_fse}}
esignsante.webservices.signature.fse.secret={{.Data.data.signature_secret_fse}}
esignsante.webservices.signature.fse.checksignature={{.Data.data.checksignature_fse}}

{{range service ("esignsante-fse") }}
esignsante.webservices.basepath=http://{{.Address}}:{{.Port}}{{end}}{{.Data.data.base_path}}
{{end}}

{{range service ( "esignsante-fse-psc-mongodb-server") }}
spring.data.mongodb.host={{.Address}}
spring.data.mongodb.port={{.Port}}{{end}}

{{with secret "esignsante-fse-psc/mongodb"}}
spring.data.mongodb.database=esignsante_psc
spring.data.mongodb.user={{.Data.data.user}}
spring.data.mongodb.password={{.Data.data.password}}
{{end}}

{{with secret "esignsante-fse-psc/psc"}}
psc.url.introspection={{.Data.data.url_introspection}}
psc.url.userinfo={{.Data.data.url_userinfo}}
psc.clientID={{.Data.data.client_id}}
psc.clientSecret={{.Data.data.clientsecret}}
with.gravitee={{.Data.data.with_gravitee}}
{{end}}
#psc.bypass=${psc_bypass}

management.endpoints.web.exposure.include=prometheus,metrics
EOF
			destination = "secrets/application.properties"
			}
			resources {
				cpu = 1000
				memory = ${esignsantefsepsc_appserver_mem_size}
			}
			service {
				name = "esignsante-fse-psc"
				meta {
				       gravitee_path = "/esignsante-psc"
				       gravitee_ssl = false
				}
				tags = ["urlprefix-/esignsante-psc/"]
				canary_tags = ["canary instance to promote"]
				port = "http"
				check {
					type = "http"
					port = "http"
					path = "/esignsante-psc/v1/ca"
					header {
						Accept = ["application/json"]
					}
					name = "alive"
					interval = "30s"
					timeout = "10s"
				}
			}
			service {
				name = "metrics-exporter"
				port = "http"
				tags = ["_endpoint=/esignsante-psc/actuator/prometheus",
								"_app=esignsante_psc",]
			}
		}

# begin log-shipper
# Ce bloc doit être décommenté pour définir le log-shipper.
# Penser à remplir la variable LOGSTASH_HOST.
#        task "log-shipper" {
#			driver = "docker"
#			restart {
#				interval = "30m"
#				attempts = 5
#				delay    = "15s"
#				mode     = "delay"
#			}
#			meta {
#				INSTANCE = "$\u007BNOMAD_ALLOC_NAME\u007D"
#			}
#			template {
#				data = <<EOH
#LOGSTASH_HOST = "${logstash_host}"
#ENVIRONMENT = "${datacenter}"
#EOH
#				destination = "local/file.env"
#				env = true
#			}
#			config {
#				image = "ans/nomad-filebeat:latest"
#			}
#	    }
# end log-shipper
	}
}
