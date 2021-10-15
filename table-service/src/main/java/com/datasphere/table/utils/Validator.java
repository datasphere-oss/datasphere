/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.datasphere.table.utils;


import java.util.Collection;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {
  private static final Logger logger = LogManager.getLogger(Validator.class.getName());
  
  public Validator() {
    throw new AssertionError("No Validator instances for you!");
  }
  
  public static CharSequence notBlank(CharSequence cs, String message) throws Exception {
    if (StringUtil.isBlank(cs)) {
      logger.error(message);
      throw new Exception(message);
    } 
    return cs;
  }
  
  public static CharSequence notBlank(CharSequence cs) throws Exception {
    notBlank(cs, "[validate failed] - the argument is required; it must not null,not a space");
    return cs;
  }
  
  public static <T> T notNull(T object) throws Exception {
    return notNull(object, "[validate failed] - the argument is required; it must not null!");
  }
  
  public static <T> T notNull(T object, String message) throws Exception {
    if (object == null) {
      logger.error(message);
      throw new Exception(message);
    } 
    return object;
  }
  
  public static <E> Collection<E> notEmpty(Collection<E> c) throws Exception {
    return notEmpty(c, "[validate failed] - the argument must not null or empty!");
  }
  
  public static <E> Collection<E> notEmpty(Collection<E> c, String message) throws Exception {
    if (c == null || c.isEmpty()) {
      logger.error(message);
      throw new Exception(message);
    } 
    return c;
  }
  
  public static <K, V> Map<K, V> notEmpty(Map<K, V> object) throws Exception {
    return notEmpty(object, "[validate failed] - the argument must not null or empty!");
  }
  
  public static <K, V> Map<K, V> notEmpty(Map<K, V> object, String message) throws Exception {
    if (object == null || object.size() < 1) {
      logger.error(message);
      throw new Exception(message);
    } 
    return object;
  }
  
  public static String notEmpty(String object) throws Exception {
    return notEmpty(object, "[validate failed] - the argument is required; it must not null or empty!");
  }
  
  public static String notEmpty(String object, String message) throws Exception {
    if (object == null || object.length() == 0) {
      logger.error(message);
      throw new Exception(message);
    } 
    return object;
  }
  
  public static boolean isTrue(boolean expression) throws Exception {
    return isTrue(expression, "[validate failed] - the argument must be true!");
  }
  
  public static boolean isTrue(boolean expression, String message) throws Exception {
    if (!expression) {
      logger.error(message);
      throw new Exception(message);
    } 
    return expression;
  }
  
  public static boolean isFalse(boolean expression) throws Exception {
    return isFalse(expression, "[validate failed] - the argument must be false!");
  }
  
  public static boolean isFalse(boolean expression, String message) throws Exception {
    if (expression) {
      logger.error(message);
      throw new Exception(message);
    } 
    return expression;
  }
  
  public static String isIpAddr(String ipAddr) throws Exception {
    return isIpAddr(ipAddr, "[validate failed] - the argument must be ip!");
  }
  
  public static String isIpAddr(String ipAddr, String message) throws Exception {
    String reg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    if (!ipAddr.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")) {
      logger.error(message);
      throw new Exception(message);
    } 
    return ipAddr;
  }
  
  public static void isPort(int port) throws Exception {
    isPort(port, "[validate failed] - the argument must be port!");
  }
  
  public static void isPort(int port, String message) throws Exception {
    if (port < 0 || port > 65535) {
      logger.error(message);
      throw new Exception(message);
    } 
  }
}
