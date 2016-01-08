package configfile;
import java.text.SimpleDateFormat;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class FormatHTMLLayout extends HTMLLayout {
	/**
	 * 该类重新HTMLLayout，用户控制日志文件的样式及输出的内容〄1�7
	 * */
	public FormatHTMLLayout() {
	}

	protected final int BUF_SIZE = 256;

	protected final int MAX_CAPACITY = 1024;

	static String TRACE_PREFIX = "<br>    ";

	private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

	String title = "GroupAutoTestReport";

	/**
	 * A string constant used in naming the option for setting the the HTML
	 * document title. Current value of this string constant is <b>Title</b>.
	 */
	public static final String TITLE_OPTION = "Title";

	// Print no location info by default
	boolean locationInfo = true;

	public String format(LoggingEvent event) {
		if (sbuf.capacity() > MAX_CAPACITY) {
			sbuf = new StringBuffer(BUF_SIZE);
		} else {
			sbuf.setLength(0);
		}

		if (event.getLevel().equals(Level.FATAL)) {
			setLineBreak(Transform.escapeTags(event.getRenderedMessage()));
			return sbuf.toString();
		}

		sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);

		sbuf.append("<td title=\"执行时间\">");
		sbuf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new java.util.Date()));
		sbuf.append("</td>" + Layout.LINE_SEP);

		sbuf.append("<td title=\"状�1�7�\">");
		/*
		 * if (event.getLevel().equals(Level.FATAL)) {
		 * sbuf.append("<font color=\"red\"><strong>"); //
		 * sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
		 * sbuf.append("紧�1�7�1�7"); sbuf.append("</strong></font>"); } else
		 */if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
			sbuf.append("<font color=\"red\">");
			// sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
			sbuf.append("失败");
			sbuf.append("</font>");
		} else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
			sbuf.append("<font color=\"yellow\">");
			// sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
			sbuf.append("警告");
			sbuf.append("</font>");
		} else if (event.getLevel().isGreaterOrEqual(Level.INFO)) {
			sbuf.append("<font color=\"green\">");
			// sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
			sbuf.append("成功");
			sbuf.append("</font>");
		}
		sbuf.append("</td>" + Layout.LINE_SEP);

		if (locationInfo) {
			LocationInfo locInfo = event.getLocationInformation();
			sbuf.append("<td title=\"扄1�7在类：行\">");
			// sbuf.append(Transform.escapeTags(locInfo.getFileName()));
			// sbuf.append(':');
			// sbuf.append(locInfo.getLineNumber());
			// sbuf.append("<font color=\"red\"><strong>");
			// sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
			// sbuf.append("</strong></font>");
			sbuf.append(Transform.escapeTags(locInfo.getFileName()));
			sbuf.append(':');
			sbuf.append(locInfo.getLineNumber());
		}
		sbuf.append("</td>" + Layout.LINE_SEP);

		sbuf.append("<td title=\"信息\">");
		sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
		sbuf.append("</td>" + Layout.LINE_SEP);
		sbuf.append("</tr>" + Layout.LINE_SEP);

		if (event.getNDC() != null) {
			sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
			sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));
			sbuf.append("</td></tr>" + Layout.LINE_SEP);
		}

		String[] s = event.getThrowableStrRep();
		if (s != null) {
			sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"4\">");
			appendThrowableAsHTML(s, sbuf);
			sbuf.append("</td></tr>" + Layout.LINE_SEP);
		}
		return sbuf.toString();
	}

	private void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
		if (s != null) {
			int len = s.length;
			if (len == 0)
				return;
			sbuf.append(Transform.escapeTags(s[0]));
			sbuf.append(Layout.LINE_SEP);
			for (int i = 1; i < len; i++) {
				sbuf.append(TRACE_PREFIX);
				sbuf.append(Transform.escapeTags(s[i]));
				sbuf.append(Layout.LINE_SEP);
			}
		}
	}

	/**
	 * Returns appropriate HTML headers.
	 */
	public String getHeader() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
				+ Layout.LINE_SEP);
		sbuf.append("<html>" + Layout.LINE_SEP);
		sbuf.append("<head>" + Layout.LINE_SEP);
		sbuf.append("<META content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">"
				+ Layout.LINE_SEP);
		sbuf.append("<title>" + title + "</title>" + Layout.LINE_SEP);
		sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
		sbuf.append("<!--" + Layout.LINE_SEP);
		sbuf.append("body, table {font-family: '??',arial,sans-serif; font-size: 12px;}"
				+ Layout.LINE_SEP);
		sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}"
				+ Layout.LINE_SEP);
		sbuf.append("-->" + Layout.LINE_SEP);
		sbuf.append("</style>" + Layout.LINE_SEP);
		sbuf.append("</head>" + Layout.LINE_SEP);
		sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">"
				+ Layout.LINE_SEP);
		sbuf.append("<h1><font color=\"green\">");
		sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
		sbuf.append("B2B团队自动化执行开始：" + new java.util.Date() + "<br>"
				+ Layout.LINE_SEP);
		// sbuf.append("<br>" + Layout.LINE_SEP);
		sbuf.append("</font></h1>");
		return sbuf.toString();
	}

	private void setLineBreak(String caseName) {
		sbuf.append("<h3><font color=\"green\">");
		// sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
		sbuf.append(caseName + "<br>" + Layout.LINE_SEP);
		// sbuf.append("<br>" + Layout.LINE_SEP);
		sbuf.append("</font></h3>");
		sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">"
				+ Layout.LINE_SEP);
		sbuf.append("<tr>" + Layout.LINE_SEP);

		sbuf.append("<th>执行时间</th>" + Layout.LINE_SEP);
		sbuf.append("<th>级别</th>" + Layout.LINE_SEP);

		if (locationInfo) {
			sbuf.append("<th>扄1�7在行</th>" + Layout.LINE_SEP);
		}

		sbuf.append("<th>信息</th>" + Layout.LINE_SEP);
		sbuf.append("</tr>" + Layout.LINE_SEP);
		sbuf.append("<br></br>" + Layout.LINE_SEP);
	}

}