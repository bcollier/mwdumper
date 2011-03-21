Summary: A tool for filtering MediaWiki XML page archives.
Name: mwdumper
Version: 0.0.1
Release: 1
Copyright: MIT
Group: Applications/Internet
Source: mwdumper-0.0.1.tar.gz
BuildRoot: /var/tmp/%{name}-buildroot
BuildArch: noarch
Requires: mono
BuildRequires: mono-devel

%description
The mwdumper program is a tool for filtering and converting
MediaWiki XML page export archives.

%prep
%setup -q

%build
INSTALL_PREFIX="/usr" PACKAGE_PREFIX="$RPM_BUILD_ROOT/usr" make

%install
rm -rf $RPM_BUILD_ROOT
INSTALL_PREFIX="/usr" PACKAGE_PREFIX="$RPM_BUILD_ROOT/usr" make install

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root)
%doc README libs/COPYING.SharpZipLib.txt libs/Readme.SharpZipLib.rtf
%dir /usr/lib/mwdumper

/usr/bin/mwdumper
/usr/lib/mwdumper/ICSharpCode.SharpZipLib.dll
/usr/lib/mwdumper/MediaWiki.Import.dll
/usr/lib/mwdumper/mwdumper.exe

%changelog
* Mon Sep 12 2005 Brion Vibber <brion@pobox.com>
- Initial packaging.

