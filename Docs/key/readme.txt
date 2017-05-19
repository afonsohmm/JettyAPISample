KEY GENERATION

pass: 123456
keytool path: /usr/java/default/bin/keytool
domain: localhost

openssl genrsa -des3 -out jettyapisample.key
openssl req -new -x509 -key jettyapisample.key -out jettyapisample.crt
/usr/java/default/bin/keytool -keystore keystore -import -alias jettyapisample -file jettyapisample.crt -trustcacerts
openssl req -new -key jettyapisample.key -out jettyapisample.csr
openssl pkcs12 -inkey jettyapisample.key -in jettyapisample.crt -export -out jettyapisample.pkcs12
/usr/java/default/bin/keytool -importkeystore -srckeystore jettyapisample.pkcs12 -srcstoretype PKCS12 -destkeystore jettyapisample.jks

Essa chave é apenas de teste. Você deve comprar uma chave válida
