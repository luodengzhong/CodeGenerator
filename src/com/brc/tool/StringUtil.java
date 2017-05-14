 package com.brc.tool;
 
 import java.io.PrintStream;
 import java.io.UnsupportedEncodingException;
 import java.net.URLDecoder;
 import java.text.DecimalFormat;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 public class StringUtil
 {
   private static DecimalFormat df = new DecimalFormat("#.0");
 
   public static boolean isNum(String str)
   {
     String regex = "9";
     if (str == null)
       return false;
     if (str.length() == 0)
       return false;
     for (int i = 0; i < str.length(); i++) {
       if (regex.indexOf(str.charAt(i)) == -1)
         return false;
     }
     return true;
   }
 
   public static String convertQuot(String orgStr)
   {
     return orgStr.replace("'", "\\'").replace("\"", "\\\"");
   }
 
   public static String convertPoint(String orgStr)
   {
     return orgStr.replace(".", "\\").replace(";", "\\");
   }
 
   public static String convertPointToQuot(String orgStr)
   {
     return orgStr.replace(".", "/").replace(";", "/");
   }
 
   public static String convertUnderLine(String orgStr)
   {
     return orgStr.replace("_", "");
   }
 
   public static String htmlEntityToString(String dataStr)
   {
     int start = 0;
     int end = 0;
     StringBuffer buffer = new StringBuffer();
     while (start > -1) {
       int system = 10;
       if (start == 0) {
         int t = dataStr.indexOf("&#");
         if (start != t)
           start = t;
       }
       end = dataStr.indexOf(";", start + 2);
       String charStr = "";
       if (end != -1) {
         charStr = dataStr.substring(start + 2, end);
 
         char s = charStr.charAt(0);
         if ((s == 'x') || (s == 'X')) {
           system = 16;
           charStr = charStr.substring(1);
         }
       }
       try
       {
         char letter = (char)Integer.parseInt(charStr, system);
         buffer.append(new Character(letter).toString());
       } catch (NumberFormatException e) {
         e.printStackTrace();
       }
 
       start = dataStr.indexOf("&#", end);
       if (start - end > 1) {
         buffer.append(dataStr.substring(end + 1, start));
       }
 
       if (start == -1) {
         int length = dataStr.length();
         if (end + 1 != length) {
           buffer.append(dataStr.substring(end + 1, length));
         }
       }
     }
     return buffer.toString();
   }
 
   public static String stringToHtmlEntity(String str)
   {
     StringBuffer sb = new StringBuffer();
     for (int i = 0; i < str.length(); i++) {
       char c = str.charAt(i);
       switch (c) {
       case '\n':
         sb.append(c);
         break;
       case '<':
         sb.append("&lt;");
         break;
       case '>':
         sb.append("&gt;");
         break;
       case '&':
         sb.append("&amp;");
         break;
       case '\'':
         sb.append("&apos;");
         break;
       case '"':
         sb.append("&quot;");
         break;
       default:
         if ((c < ' ') || (c > '~')) {
           sb.append("&#x");
           sb.append(Integer.toString(c, 16));
           sb.append(';');
         } else {
           sb.append(c);
         }break;
       }
     }
     return sb.toString();
   }
 
   public static String stringToUnicode(String s)
   {
     String unicode = "";
     char[] charAry = new char[s.length()];
     for (int i = 0; i < charAry.length; i++) {
       charAry[i] = s.charAt(i);
       unicode = unicode + "\\u" + Integer.toString(charAry[i], 16);
     }
     return unicode;
   }
 
   public static String unicodeToString(String unicodeStr) {
     StringBuffer sb = new StringBuffer();
     String[] str = unicodeStr.toUpperCase().split("\\\\U");
     for (int i = 0; i < str.length; i++)
       if (!str[i].equals(""))
       {
         char c = (char)Integer.parseInt(str[i].trim(), 16);
         sb.append(c);
       }
     return sb.toString();
   }
 
   public static String html2Text(String inputString) {
     String htmlStr = inputString;
     String textStr = "";
     try
     {
       String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
 
       String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
 
       String regEx_html = "<[^>]+>";
       Pattern p_script = Pattern.compile(regEx_script, 
         2);
       Matcher m_script = p_script.matcher(htmlStr);
       htmlStr = m_script.replaceAll("");
       Pattern p_style = Pattern.compile(regEx_style, 
         2);
       Matcher m_style = p_style.matcher(htmlStr);
       htmlStr = m_style.replaceAll("");
       Pattern p_html = Pattern.compile(regEx_html, 
         2);
       Matcher m_html = p_html.matcher(htmlStr);
       htmlStr = m_html.replaceAll("");
       textStr = htmlStr;
     } catch (Exception e) {
       System.err.println("Html2Text: " + e.getMessage());
     }
     return textStr;
   }
 
   public static String escape(String src)
   {
     StringBuffer tmp = new StringBuffer();
     tmp.ensureCapacity(src.length() * 6);
     for (int i = 0; i < src.length(); i++) {
       char j = src.charAt(i);
       if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || 
         (Character.isUpperCase(j))) {
         tmp.append(j);
       } else if (j < 'Ā') {
         tmp.append("%");
         if (j < '\020')
           tmp.append("0");
         tmp.append(Integer.toString(j, 16));
       } else {
         tmp.append("%u");
         tmp.append(Integer.toString(j, 16));
       }
     }
     return tmp.toString();
   }
 
   public static String unescape(String src) {
     StringBuffer tmp = new StringBuffer();
     tmp.ensureCapacity(src.length());
     int lastPos = 0; int pos = 0;
 
     while (lastPos < src.length()) {
       pos = src.indexOf("%", lastPos);
       if (pos == lastPos) {
         if (src.charAt(pos + 1) == 'u') {
           char ch = (char)Integer.parseInt(
             src.substring(pos + 2, pos + 6), 16);
           tmp.append(ch);
           lastPos = pos + 6;
         } else {
           char ch = (char)Integer.parseInt(
             src.substring(pos + 1, pos + 3), 16);
           tmp.append(ch);
           lastPos = pos + 3;
         }
       }
       else if (pos == -1) {
         tmp.append(src.substring(lastPos));
         lastPos = src.length();
       } else {
         tmp.append(src.substring(lastPos, pos));
         lastPos = pos;
       }
     }
 
     return tmp.toString();
   }
 
   public static String formatDou2Str(Double d) {
     if (d == null)
       return "0";
     return df.format(d);
   }
 
   public static String decodeStr(String encodeparam) {
     try {
       if ((encodeparam == null) || ("".equalsIgnoreCase(encodeparam))) {
         return null;
       }
       return URLDecoder.decode(encodeparam, "UTF-8");
     } catch (UnsupportedEncodingException e) {
       e.printStackTrace();
     }return null;
   }
 
   public static String replaceSlash(String str)
   {
     String result = str.replaceAll("//", ".");
     result = result.replaceAll("/", ".");
     result = result.replaceAll("\\\\", ".");
     return result;
   }
 
   public static String getPropertyName(String columnName)
   {
     if ((columnName == null) || (columnName.length() == 0)) {
       return "";
     }
     columnName = columnName.toLowerCase();
     StringBuffer sb = new StringBuffer();
     String regex = "_";
     String addString = "";
     String[] sbArr = columnName.split(regex);
     if ((sbArr.length == 1) && (sbArr[0].length() == 1))
       sb.append(sbArr[0]);
     else {
       for (int i = 0; i < sbArr.length; i++) {
         if (i == 0) {
           if (sbArr[i].length() == 1)
             addString = sbArr[i].toUpperCase();
           else {
             addString = sbArr[i];
           }
         }
         else if (sbArr[i].length() == 1)
           addString = sbArr[i].toUpperCase();
         else {
           addString = sbArr[i].substring(0, 1).toUpperCase() + 
             sbArr[i].substring(1, sbArr[i].length());
         }
 
         sb.append(addString);
       }
     }
     return sb.toString();
   }
 
   public static String addPoint(String str, String addStr)
   {
     String result = str;
     if (str.endsWith("."))
       result = result + addStr;
     else {
       result = result + "." + addStr;
     }
     return result;
   }
 
   public static String initialStrToUpper(String str)
   {
     return str.substring(0, 1).toUpperCase() + str.substring(1);
   }
 
   public static String initialStrToLower(String str)
   {
     return str.substring(0, 1).toLowerCase() + str.substring(1);
   }
 
   public static String replaceAll(String str, String oldStr, String newStr)
   {
     if ((str == null) || (oldStr == null))
       return str;
     newStr = newStr == null ? "" : newStr;
     int i = str.indexOf(oldStr);
     int n = 0;
     while (i != -1) {
       str = str.substring(0, i) + newStr + 
         str.substring(i + oldStr.length());
       i = str.indexOf(oldStr, i + newStr.length());
       n++;
     }
     return str;
   }
 
   public static boolean isBlank(String str)
   {
     if (str == null)
       return true;
     if ("".equals(str.trim())) {
       return true;
     }
     return false;
   }
 
   public static void main(String[] args) {
     String name = "org_Kind_ID";
     System.out.println(getPropertyName(name));
   }
 }

