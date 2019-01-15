#!/usr/bin/env bash
export LANG=en_US.UTF-8
#sudo -u postgres /usr/bin/postgres -D /var/lib/pgsql/data &
sudo -u postgres /usr/pgsql-9.6/bin/postgres -D /var/lib/pgsql/data &
cd /opt/solr-7.3.1/
sudo -u solr bin/solr start 
sudo -u solr bin/solr create_core -c collection1 -d server/solr/collection1/conf 

# start apache, in both foreground and background...
apachectl -DFOREGROUND &

cd /opt/glassfish4
sudo -u glassfish bin/asadmin start-domain --debug
sleep infinity

