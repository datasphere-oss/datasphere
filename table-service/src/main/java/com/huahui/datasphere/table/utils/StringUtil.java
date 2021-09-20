package com.huahui.datasphere.table.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class StringUtil {
  private static final Logger logger = LogManager.getLogger(StringUtil.class.getName());
  
  public static final String UTF_8 = "UTF-8";
  
  public static final String EMPTY = "";
  
  public static final int INDEX_NOT_FOUND = -1;
  
  public static final String BLANK = " ";
  
  public static final char LF = '\n';
  
  public static final char CR = '\r';
  
  public static final char NUL = '\000';
  
  public static final char AT_SIGN = '@';
  
  public static final Pattern PATTERN_HUMP2UNDERLINE = Pattern.compile("[A-Z]");
  
  public static final Pattern PATTERN_UNDERLINE2HUMP = Pattern.compile("_[a-z]");
  
  public StringUtil() {
    throw new AssertionError("No StringUtil instances for you!");
  }
  
  public static boolean isEmpty(CharSequence cs) {
    return (cs == null || cs.length() == 0);
  }
  
  public static boolean isNotEmpty(CharSequence cs) {
    return !isEmpty(cs);
  }
  
  public static boolean isBlank(CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0)
      return true; 
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i)))
        return false; 
    } 
    return true;
  }
  
  public static boolean isNotBlank(CharSequence cs) {
    return !isBlank(cs);
  }
  
  public static String humpToUnderline(String str) {
    Matcher matcher = PATTERN_HUMP2UNDERLINE.matcher(str);
    StringBuilder builder = new StringBuilder(str);
    for (int i = 0; matcher.find(); i++)
      builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase()); 
    if (builder.charAt(0) == '_')
      builder.deleteCharAt(0); 
    return builder.toString();
  }
  
  public static String underlineToHump(String str) {
    Matcher matcher = PATTERN_UNDERLINE2HUMP.matcher(str);
    StringBuilder builder = new StringBuilder(str);
    for (int i = 0; matcher.find(); i++)
      builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase()); 
    if (Character.isUpperCase(builder.charAt(0)))
      builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0)))); 
    return builder.toString();
  }
  
  public static String firstToUpper(String str) {
    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }
  
  public static String firstToLower(String str) {
    return Character.toLowerCase(str.charAt(0)) + str.substring(1);
  }
  
  public static int substringCount(String sourceStr, String searchStr) {
    if (isEmpty(sourceStr) || isEmpty(searchStr))
      return 0; 
    int loc = sourceStr.indexOf(searchStr);
    if (loc < 0)
      return 0; 
    int count = 1;
    int serLen = searchStr.length();
    String subStr = sourceStr.substring(loc + serLen);
    while (true) {
      loc = subStr.indexOf(searchStr);
      if (loc < 0)
        break; 
      subStr = subStr.substring(loc + serLen);
      count++;
    } 
    return count;
  }
  
  public static String replaceFirst(String sourceStr, String searchStr) {
    return replaceFirst(sourceStr, searchStr, "");
  }
  
  public static String replaceFirst(String sourceStr, String searchStr, String replaceStr) {
    if (isEmpty(sourceStr) || isEmpty(searchStr))
      return sourceStr; 
    int firstLoc = sourceStr.indexOf(searchStr);
    if (firstLoc == 0) {
      if (isEmpty(replaceStr))
        return sourceStr.substring(searchStr.length()); 
      return replaceStr + sourceStr.substring(searchStr.length());
    } 
    int srcLen = sourceStr.length();
    int serLen = searchStr.length();
    if (isEmpty(replaceStr))
      return sourceStr.substring(0, firstLoc) + sourceStr.substring(firstLoc + serLen, srcLen); 
    int repLen = replaceStr.length();
    StringBuilder sb = new StringBuilder(srcLen - serLen + repLen);
    sb.append(sourceStr.substring(0, firstLoc))
      .append(replaceStr)
      .append(sourceStr.substring(firstLoc + serLen, srcLen));
    return sb.toString();
  }
  
  public static String replaceLast(String sourceStr, String searchStr) {
    return replaceLast(sourceStr, searchStr, "");
  }
  
  public static String replaceLast(String sourceStr, String searchStr, String replaceStr) {
    if (isEmpty(sourceStr) || isEmpty(searchStr))
      return sourceStr; 
    if (sourceStr.endsWith(searchStr)) {
      int loc = sourceStr.lastIndexOf(searchStr);
      if (isEmpty(replaceStr))
        return sourceStr.substring(0, loc); 
      return sourceStr.substring(0, loc) + replaceStr;
    } 
    int lastLoc = sourceStr.lastIndexOf(searchStr);
    if (lastLoc < 0)
      return sourceStr; 
    if (lastLoc == 0) {
      if (isEmpty(replaceStr))
        return sourceStr.substring(searchStr.length()); 
      return replaceStr + sourceStr.substring(searchStr.length());
    } 
    int srcLen = sourceStr.length();
    int serLen = searchStr.length();
    int repLen = replaceStr.length();
    StringBuilder sb = new StringBuilder(srcLen - serLen + repLen);
    sb.append(sourceStr.substring(0, lastLoc))
      .append(replaceStr)
      .append(sourceStr.substring(lastLoc + serLen, srcLen));
    return sb.toString();
  }
  
  public static void identityToString(StringBuffer buffer, Object object) throws Exception {
    Validator.notNull(object, "Cannot get the toString of a null object");
    String name = object.getClass().getName();
    String hexString = Integer.toHexString(System.identityHashCode(object));
    buffer.ensureCapacity(buffer.length() + name.length() + 1 + hexString.length());
    buffer.append(name)
      .append('@')
      .append(hexString);
  }
  
  public static boolean containsNone(CharSequence cs, char... searchChars) {
    if (cs == null || searchChars == null)
      return true; 
    int csLen = cs.length();
    int csLast = csLen - 1;
    int searchLen = searchChars.length;
    int searchLast = searchLen - 1;
    for (int i = 0; i < csLen; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLen; j++) {
        if (searchChars[j] == ch)
          if (Character.isHighSurrogate(ch)) {
            if (j == searchLast)
              return false; 
            if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1))
              return false; 
          } else {
            return false;
          }  
      } 
    } 
    return true;
  }
  
  public static boolean containsAny(CharSequence cs, char... searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0)
      return false; 
    int csLength = cs.length();
    int searchLength = searchChars.length;
    int csLast = csLength - 1;
    int searchLast = searchLength - 1;
    for (int i = 0; i < csLength; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLength; j++) {
        if (searchChars[j] == ch)
          if (Character.isHighSurrogate(ch)) {
            if (j == searchLast)
              return true; 
            if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1))
              return true; 
          } else {
            return true;
          }  
      } 
    } 
    return false;
  }
  
  public static String trimCrLf(String str) {
    if (isEmpty(str))
      return str; 
    if (str.length() == 1) {
      char ch = str.charAt(0);
      if (ch == '\r' || ch == '\n')
        return ""; 
      return str;
    } 
    int lastIdx = str.length() - 1;
    char last = str.charAt(lastIdx);
    if (last == '\n') {
      if (str.charAt(lastIdx - 1) == '\r')
        lastIdx--; 
    } else if (last != '\r') {
      lastIdx++;
    } 
    return str.substring(0, lastIdx);
  }
  
  public static String replace(String srcString, char searchChar, char repChar) {
    if (srcString == null)
      return null; 
    return srcString.replace(searchChar, repChar);
  }
  
  public static String replace(String srcString, String searchStr, String repStr) {
    if (isEmpty(srcString) || isEmpty(searchStr) || repStr == null)
      return srcString; 
    int start = 0;
    int end = srcString.indexOf(searchStr, start);
    if (end == -1)
      return srcString; 
    int repLen = searchStr.length();
    int increase = repStr.length() - repLen;
    increase = (increase > 0) ? (increase * 16) : 0;
    StringBuilder buf = new StringBuilder(srcString.length() + increase);
    while (end != -1) {
      buf.append(srcString, start, end).append(repStr);
      start = end + repLen;
      end = srcString.indexOf(searchStr, start);
    } 
    buf.append(srcString, start, srcString.length());
    return buf.toString();
  }
  
  public static List<String> split(String str, String separator) {
    if (str == null)
      return Collections.emptyList(); 
    int len = str.length();
    if (len == 0)
      return Collections.emptyList(); 
    if (separator == null || "".equals(separator))
      return Collections.singletonList(str); 
    int separatorLength = separator.length();
    List<String> substrings = new ArrayList<>();
    int begin = 0;
    int end = 0;
    while (end < len) {
      end = str.indexOf(separator, begin);
      if (end > -1) {
        if (end > begin) {
          substrings.add(str.substring(begin, end));
          begin = end + separatorLength;
          continue;
        } 
        substrings.add("");
        begin = end + separatorLength;
        continue;
      } 
      substrings.add(str.substring(begin));
      end = len;
    } 
    return substrings;
  }
  
  public static String lenientFormat(String template, Object... args) {
    template = String.valueOf(template);
    if (args == null) {
      args = new Object[] { "(Object[])null" };
    } else {
      for (int j = 0; j < args.length; j++)
        args[j] = lenientToString(args[j]); 
    } 
    StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
    int templateStart = 0;
    int i = 0;
    while (i < args.length) {
      int placeholderStart = template.indexOf("%s", templateStart);
      if (placeholderStart == -1)
        break; 
      builder.append(template, templateStart, placeholderStart);
      builder.append(args[i++]);
      templateStart = placeholderStart + 2;
    } 
    builder.append(template, templateStart, template.length());
    if (i < args.length) {
      builder.append(" [");
      builder.append(args[i++]);
      while (i < args.length) {
        builder.append(", ");
        builder.append(args[i++]);
      } 
      builder.append(']');
    } 
    return builder.toString();
  }
  
  private static String lenientToString(Object o) {
    try {
      return String.valueOf(o);
    } catch (Exception e) {
      String objectToString = o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
      logger.warn("Exception during lenientFormat for " + objectToString, e);
      return "<" + objectToString + " threw " + e.getClass().getName() + ">";
    } 
  }
  
  public static String string2Unicode(String string) {
    StringBuffer unicode = new StringBuffer();
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      unicode.append("\\u" + Integer.toHexString(c));
    } 
    return unicode.toString();
  }
  
  public static String unicode2String(String unicode) {
    StringBuffer string = new StringBuffer();
    String[] hex = unicode.split("\\\\u");
    for (int i = 1; i < hex.length; i++) {
      int data = Integer.parseInt(hex[i], 16);
      string.append((char)data);
    } 
    return string.toString();
  }
}

