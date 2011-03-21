.PHONY : all clean dist distclean install uninstall rpm

VERSION=0.0.1

PRODUCT=mwdumper

INSTALL_PREFIX?=/usr/local
INSTALL_BINDIR=$(INSTALL_PREFIX)/bin

PACKAGE_PREFIX?=$(INSTALL_PREFIX)
PACKAGE_BINDIR=$(PACKAGE_PREFIX)/bin

GCJ?=gcj
CFLAGS?=-O2

SOURCES_DUMPER=\
  src/org/mediawiki/dumper/Dumper.java \
  src/org/mediawiki/dumper/ProgressFilter.java \
  src/org/mediawiki/dumper/Tools.java \
  src/org/apache/commons/compress/bzip2/BZip2Constants.java \
  src/org/apache/commons/compress/bzip2/CBZip2InputStream.java \
  src/org/apache/commons/compress/bzip2/CBZip2OutputStream.java \
  src/org/apache/commons/compress/bzip2/CRC.java

SOURCES_IMPORT=\
  src/org/mediawiki/importer/AfterTimeStampFilter.java \
  src/org/mediawiki/importer/BeforeTimeStampFilter.java \
  src/org/mediawiki/importer/Buffer.java \
  src/org/mediawiki/importer/Contributor.java \
  src/org/mediawiki/importer/DumpWriter.java \
  src/org/mediawiki/importer/ExactListFilter.java \
  src/org/mediawiki/importer/LatestFilter.java \
  src/org/mediawiki/importer/ListFilter.java \
  src/org/mediawiki/importer/RevisionListFilter.java \
  src/org/mediawiki/importer/MultiWriter.java \
  src/org/mediawiki/importer/NamespaceFilter.java \
  src/org/mediawiki/importer/NamespaceSet.java \
  src/org/mediawiki/importer/NotalkFilter.java \
  src/org/mediawiki/importer/Page.java \
  src/org/mediawiki/importer/PageFilter.java \
  src/org/mediawiki/importer/Revision.java \
  src/org/mediawiki/importer/Siteinfo.java \
  src/org/mediawiki/importer/SphinxWriter.java \
  src/org/mediawiki/importer/SqlFileStream.java \
  src/org/mediawiki/importer/SqlLiteral.java \
  src/org/mediawiki/importer/SqlStream.java \
  src/org/mediawiki/importer/SqlServerStream.java \
  src/org/mediawiki/importer/SqlWriter.java \
  src/org/mediawiki/importer/SqlWriter14.java \
  src/org/mediawiki/importer/SqlWriter15.java \
  src/org/mediawiki/importer/TimeStampFilter.java \
  src/org/mediawiki/importer/Title.java \
  src/org/mediawiki/importer/TitleMatchFilter.java \
  src/org/mediawiki/importer/XmlDumpReader.java \
  src/org/mediawiki/importer/XmlDumpWriter.java \
  src/org/mediawiki/importer/XmlWriter.java

TMPDIST=mwdumper-$(VERSION)
DISTDIRS=build src
MISCFILES=Makefile.gcj mwdumper.spec README

DISTFILES=$(SOURCES_IMPORT) $(SOURCES_DUMPER) $(MISCFILES)

all: build/$(PRODUCT)

clean:
	rm -f build/mwdumper

distclean : clean
	rm -rf $(TMPDIST)
	rm -f $(TMPDIST).tar.gz

dist : $(DISTFILES) Makefile
	rm -rf $(TMPDIST)
	mkdir $(TMPDIST)
	for x in $(DISTDIRS); do mkdir $(TMPDIST)/$$x; done
	for x in $(DISTFILES); do cp -p $$x $(TMPDIST)/$$x; done
	tar zcvf $(TMPDIST).tar.gz $(TMPDIST)

rpm : dist
	cp $(TMPDIST).tar.gz /usr/src/redhat/SOURCES
	cp mwdumper.spec /usr/src/redhat/SPECS
	cd /usr/src/redhat/SPECS && rpmbuild -ba mwdumper.spec

install: all
	install -d $(PACKAGE_BINDIR)
	install -m 0755 build/mwdumper $(PACKAGE_BINDIR)/mwdumper

uninstall :
	rm -f $(INSTALL_BINDIR)/mwdumper

build/mwdumper : $(SOURCES_DUMPER) $(SOURCES_IMPORT)
	$(GCJ) $(CFLAGS) --main=org.mediawiki.dumper.Dumper \
	  -o $@ $(SOURCES_DUMPER) $(SOURCES_IMPORT)
