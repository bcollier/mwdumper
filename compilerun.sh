ant compile
ant jar
cd build
java -Xms1024m -Xmx8000m -jar mwdumper.jar --format=flatfile /home/bcollier/Data/WPDump/stub-history-01-16-2011/enwiki-20110115-stub-meta-history1.xml > stubmetahistorylog3-21.log

