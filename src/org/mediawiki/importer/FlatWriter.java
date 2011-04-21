/*
 * MediaWiki import/export processing tools
 * Copyright 2005 by Brion Vibber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * $Id: SqlWriter15.java 54087 2009-07-31 10:39:07Z daniel $
 */

// Doesn't actually work yet...

package org.mediawiki.importer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class FlatWriter implements DumpWriter {
	protected OutputStream stream;
	protected static final int DELETED_TEXT = 1;
	protected static final int DELETED_COMMENT = 2;
	protected static final int DELETED_USER = 4;
	protected static final int DELETED_RESTRICTED = 8;
	//protected BufferedOutputStream writer;
	protected static final Integer ONE = new Integer(1);
	protected static final Integer ZERO = new Integer(0);
	protected BufferedWriter pageWriter = null;
	protected BufferedWriter revWriter = null;
	//protected BufferedOutputStream revWriter;
	private Page currentPage;
	private Revision lastRevision;
	protected String encoding;
	protected BufferedWriter writer;
	
	public FlatWriter(OutputStream output, String pageOutFileLocation) throws IOException {
		stream = output;
		//writer = new BufferedOutputStream(stream);
		pageWriter = new BufferedWriter(new FileWriter(pageOutFileLocation));
		//revWriter = new BufferedWriter(new FileWriter("/home/bcollier/revisions_mwdumper.txt"));
		//revWriter = new BufferedOutputStream(System.out, OUT_BUF_SZ);
		//revWriter = writer;
		
		encoding = "utf-8";
		revWriter = new BufferedWriter(new OutputStreamWriter(stream, "UTF8"));
		
	}
	
	protected String timestampFormat(Calendar time) {
		String revDate;
		
		revDate = Integer.toString(time.get(Calendar.YEAR));
		
		if (time.get(Calendar.MONTH) < 9) 
			revDate = revDate + "-0" + Integer.toString(time.get(Calendar.MONTH) + 1);
		else
			revDate = revDate + "-" + Integer.toString(time.get(Calendar.MONTH) + 1);
		
		return revDate + "-" + Integer.toString(time.get(Calendar.DAY_OF_MONTH));
		
		//return revDate;
		
		//return Integer.toString(time.get(Calendar.YEAR)) + "-" + Integer.toString(time.get(Calendar.MONTH) + 1) + "-" + Integer.toString(time.get(Calendar.DAY_OF_MONTH));

				/*new Integer(time.get(Calendar.YEAR)),
				new Integer(time.get(Calendar.DAY_OF_MONTH)),
				new Integer(time.get(Calendar.HOUR_OF_DAY)),
				new Integer(time.get(Calendar.MINUTE)),
				new Integer(time.get(Calendar.SECOND))*/
	}
	
	public void close() throws IOException {
		//writer.write("closing".getBytes());
		//writer.write("\n".getBytes());
	}
	
	public void writeStartWiki() throws IOException{
		//writer.write("Begin Writing...\n".getBytes());
	}
	
	public void writeEndWiki() throws IOException{
		//writer.write("Finished Writing.".getBytes());
		//writer.write("\n".getBytes());
		//writer.flush();
		//writer.close();
		pageWriter.flush();
		revWriter.flush();
		pageWriter.close();
		revWriter.close();
	}
	
	public void writeSiteinfo(Siteinfo info) throws IOException{
		//writer.write("siteinfo".getBytes());
		//writer.write("\n".getBytes());
	}
	
	public void writeStartPage(Page page) throws IOException{
		currentPage = page;
		lastRevision = null;
		//writer.write("Starting a new page".getBytes());
		//writer.write("\n".getBytes());
		
		//pageWriter.write("page\n");
	}
	
	public void writeEndPage() throws IOException{
		if (lastRevision != null) {
			updatePage(currentPage, lastRevision);
		}
		currentPage = null;
		lastRevision = null;
		
		pageWriter.flush();
		revWriter.flush();
		//writer.write("endpage".getBytes());
		//writer.write("\n".getBytes());
	}
	
	//clean the string of tabs and new lines
	public String clStr(String dirtyString){
		String cleanString; 
		cleanString = dirtyString.replace("\n", " ");
		return cleanString.replace("\t", " ");
		
	}
	
	public void writeRevision(Revision revision) throws IOException{
		
		int rev_deleted = 0; 
		if (revision.Contributor.Username==null) rev_deleted |= DELETED_USER;
		if (revision.Comment==null) rev_deleted |= DELETED_COMMENT;
		if (revision.Text==null) rev_deleted |= DELETED_TEXT;
		
		
		revWriter.write(Integer.toString(revision.Id));
		revWriter.write("\t");
		revWriter.write(Integer.toString(currentPage.Id));
		revWriter.write("\t");
		revWriter.write(revision.Comment == null ? "" : clStr(revision.Comment));
		revWriter.write("\t");
		revWriter.write(Integer.toString(revision.Contributor.Username == null ? ZERO :  new Integer(revision.Contributor.Id)));
		revWriter.write("\t");
		revWriter.write(revision.Contributor.Username == null ? "" : revision.Contributor.Username);
		revWriter.write("\t");
		revWriter.write(timestampFormat(revision.Timestamp));
		revWriter.write("\t");
		revWriter.write(Integer.toString(revision.Timestamp.get(Calendar.YEAR)));
		revWriter.write("\t");
		revWriter.write(Integer.toString(revision.Timestamp.get(Calendar.MONTH)+1));
		revWriter.write("\t");
		revWriter.write(Integer.toString(revision.Minor ? ONE : ZERO));
		revWriter.write("\t");
		revWriter.write(Integer.toString(rev_deleted==0 ? ZERO : new Integer(rev_deleted)));
		revWriter.write("\t");
		revWriter.write(revision.Text == null ? "0" : Integer.toString(lengthUtf8(revision.Text)));
		revWriter.write("\t");
		revWriter.write(revision.Text == null ? "" : clStr(revision.Text));
		revWriter.write("\n");

		lastRevision = revision;
		
		//use this line to get the text
		//old_text", revision.Text == null ? "" : revision.Text
		
		
		//writer.write("revision".getBytes());
		//writer.write("\n".getBytes());
		
		/*
		int rev_deleted = 0; 
		if (revision.Contributor.Username==null) rev_deleted |= DELETED_USER;
		if (revision.Comment==null) rev_deleted |= DELETED_COMMENT;
		if (revision.Text==null) rev_deleted |= DELETED_TEXT;

		bufferInsertRow("revision", new Object[][] {
				{"rev_id", new Integer(revision.Id)},
				{"rev_page", new Integer(currentPage.Id)},
				{"rev_text_id", new Integer(revision.Id)},
				{"rev_comment", revision.Comment == null ? "" : revision.Comment},
				{"rev_user", revision.Contributor.Username == null ? ZERO :  new Integer(revision.Contributor.Id)},
				{"rev_user_text", revision.Contributor.Username == null ? "" : revision.Contributor.Username},
				{"rev_timestamp", timestampFormat(revision.Timestamp)},
				{"rev_minor_edit", revision.Minor ? ONE : ZERO},
				{"rev_deleted", rev_deleted==0 ? ZERO : new Integer(rev_deleted) }}); */
	}
	
	private void updatePage(Page page, Revision revision) throws IOException {
		pageWriter.write(Integer.toString(page.Id));
		pageWriter.write("\t");
		pageWriter.write(Integer.toString(page.Title.Namespace));
		pageWriter.write("\t");
		pageWriter.write(titleFormat(page.Title.Text));
		pageWriter.write("\t");
		pageWriter.write(page.Restrictions);
		pageWriter.write("\t");
		pageWriter.write(Integer.toString(revision.isRedirect() ? ONE : ZERO));
		pageWriter.write("\t");
		pageWriter.write(Integer.toString(revision.Id));
		pageWriter.write("\t");
		pageWriter.write(Integer.toString(lengthUtf8(revision.Text)));
		pageWriter.write("\n");
		
		
//		bufferInsertRow("page", new Object[][] {
//				{"page_id", new Integer(page.Id)},
//				{"page_namespace", page.Title.Namespace},
//				{"page_title", titleFormat(page.Title.Text)},
//				{"page_restrictions", page.Restrictions},
//				{"page_counter", ZERO},
//				{"page_is_redirect", revision.isRedirect() ? ONE : ZERO},
//				{"page_is_new", ZERO},
//				{"page_random", traits.getRandom()},
//				{"page_touched", traits.getCurrentTime()},
//				{"page_latest", new Integer(revision.Id)},
//				{"page_len", new Integer(lengthUtf8(revision.Text))}});
//		checkpoint();
	}
	
	protected static String titleFormat(String title) {
		return title.replace(' ', '_');
	}
	
	private static int lengthUtf8(String s) {
		final int slen = s.length();
		final char[] buf = Buffer.get(slen);
		s.getChars(0, slen, buf, 0);
		int len = 0;
		for (int i = 0; i < slen; i++) {
			char c = buf[i];
			if (c < 0x80)
				len++;
			else if (c < 0x800)
				len+=2;
			else if (c < 0xD800 || c >= 0xE000)
				len+=3;
			else {
				// Surrogate pairs are assumed to be valid.
				len+=4;
				i++;
			}
		}
		return len;
	}

}
