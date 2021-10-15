/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
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

package com.huahui.datasphere.mdm.core.serialization.jaxb;

import java.io.IOException;
import java.io.Writer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

/**
 * Works as default {@link com.sun.xml.bind.marshaller.MinimumEscapeHandler} but ignores characters which are not
 * in range #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
 *
 * @author Aleksandr Magdenko
 */
public class CustomEscapeHandler implements CharacterEscapeHandler {

    private CustomEscapeHandler() {}  // no instanciation please

    public static final CharacterEscapeHandler INSTANCE = new CustomEscapeHandler();

    @Override
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

        int limit = start+length;
        for (int i = start; i < limit; i++) {
            char c = ch[i];

            // Ignore characters which not in range.
            if (isIgnored(c)) {
                if (i != start) {
                    out.write(ch, start, i - start);
                }
                start = i + 1;
                continue;
            }

            // avoid calling the Writerwrite method too much by assuming
            // that the escaping occurs rarely.
            // profiling revealed that this is faster than the naive code.
            if (c == '&' || c == '<' || c == '>' || c == '\r' || (c == '\"' && isAttVal)) {
                if (i != start) {
                    out.write(ch, start, i - start);
                }
                start = i + 1;
                switch (ch[i]) {
                    case '&':
                        out.write("&amp;");
                        break;
                    case '<':
                        out.write("&lt;");
                        break;
                    case '>':
                        out.write("&gt;");
                        break;
                    case '\"':
                        out.write("&quot;");
                        break;
                    default:
                        break;
                }
            }
        }

        if( start!=limit )
            out.write(ch,start,limit-start);
    }

    /**
     * Note that XML 1.0 is a text-only format: it cannot represent control characters or unpaired Unicode surrogate
     * codepoints, even after escaping. escapeXml10 will remove characters that do not fit in the following ranges:
     * #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
     *
     * See https://commons.apache.org/proper/commons-lang/javadocs/api-3.3/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml10(java.lang.String)
     *
     * @param c input character
     * @return true for incorrect XML character, otherwise false
     */
    private boolean isIgnored(char c) {
        return !(c == 0x9 || c == 0xA || c == 0xD ||
                (c >= 0x20 && c <= 0xD7FF) ||
                (c >= 0xE000 && c <= 0xFFFD) ||
                 c >= 0x10000 && c <= 0x10FFFF);
    }
}