/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.service.UPathService;
import org.datasphere.mdm.core.service.impl.upath.CollectorElementImpl;
import org.datasphere.mdm.core.service.impl.upath.ExpressionElementImpl;
import org.datasphere.mdm.core.service.impl.upath.PredicateElementImpl;
import org.datasphere.mdm.core.service.impl.upath.SubscriptElementImpl;
import org.datasphere.mdm.core.type.data.Attribute;
import org.datasphere.mdm.core.type.data.ComplexAttribute;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.ArrayAttribute.ArrayDataType;
import org.datasphere.mdm.core.type.data.Attribute.AttributeType;
import org.datasphere.mdm.core.type.data.CodeAttribute.CodeDataType;
import org.datasphere.mdm.core.type.data.SimpleAttribute.SimpleDataType;
import org.datasphere.mdm.core.type.data.impl.ComplexAttributeImpl;
import org.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import org.datasphere.mdm.core.type.upath.UPath;
import org.datasphere.mdm.core.type.upath.UPathApplicationMode;
import org.datasphere.mdm.core.type.upath.UPathConstants;
import org.datasphere.mdm.core.type.upath.UPathElement;
import org.datasphere.mdm.core.type.upath.UPathElementType;
import org.datasphere.mdm.core.type.upath.UPathExecutionContext;
import org.datasphere.mdm.core.type.upath.UPathIncompletePath;
import org.datasphere.mdm.core.type.upath.UPathResult;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov
 * UPath bits implementation.
 */
@Service
public class UPathServiceImpl implements UPathService {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UPathServiceImpl.class);
    /**
     * MVEL evaluation context.
     */
    private static final ParserContext EVALUATION_CONTEXT = ParserContext.create()
            .withInput(UPathConstants.UPATH_RECORD_NAME, DataRecord.class)
            .withImport(AttributeType.class)
            .withImport(SimpleDataType.class)
            .withImport(CodeDataType.class)
            .withImport(ArrayDataType.class)
            .withImport(LocalDate.class)
            .withImport(LocalDateTime.class)
            .withImport(LocalTime.class);
    /**
     * Tries to reuse expressions.
     */
    private final Map<String, CompiledExpression> expressions = new ConcurrentHashMap<>();
    /**
     * Constructor.
     */
    public UPathServiceImpl() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UPath upathCreate(String path) {

        if (StringUtils.isBlank(path)) {
            final String message = "Invalid input. Path is blank.";
            LOGGER.warn(message);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_INPUT_PATH_IS_BLANK);
        }

        // Check for whole record, being requested.
        String[] pathTokens = splitPath(path);
        if (pathTokens.length == 0) {
            final String message = "Invalid input. Path [{}] was split to zero elements.";
            LOGGER.warn(message, path);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_INPUT_SPLIT_TO_ZERO_ELEMENTS, path);
        }

        UPath upath = null;
        for (int i = 0; i < pathTokens.length; i++) {

            String token = pathTokens[i];

            if (i == 0) {

                upath = init(token);
                if (Objects.nonNull(upath.getNameSpace()) || Objects.nonNull(upath.getTypeName())) {
                    token = token.substring(token.lastIndexOf(':') + 1);
                }
            }

            // 1. Subscript or expression.
            if ((i == 0 && checkRootRecord(upath, token))
              || checkSubscriptFilter(upath, token)
              || checkExpressionFilter(upath, token)) {
                continue;
            }

            // 2. Simple collecting
            upath.getElements().add(new CollectorElementImpl(token));
        }

        return upath;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UPathResult upathResult(String path, DataRecord record) {
        UPath upath = upathCreate(path);
        return upathGet(upath, record, UPathApplicationMode.MODE_ALL);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UPathResult upathGet(UPath upath, DataRecord record, UPathExecutionContext context, UPathApplicationMode mode) {

        if (context == UPathExecutionContext.FULL_TREE) {
            return upathFullTreeGetImpl(upath, record, mode);
        } else if (context == UPathExecutionContext.SUB_TREE) {
            return upathSubTreeGetImpl(upath, record, mode);
        }

        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UPathResult upathGet(UPath upath, DataRecord record, UPathApplicationMode mode) {
        return upathGet(upath, record, UPathExecutionContext.FULL_TREE, mode);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UPathResult upathGet(UPath upath, DataRecord record) {
        return upathGet(upath, record, UPathApplicationMode.MODE_ALL);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upathSet(UPath upath, DataRecord record, Attribute target) {
        return upathSet(upath, record, target, UPathApplicationMode.MODE_ALL);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upathSet(UPath upath, DataRecord record, Attribute target, UPathApplicationMode mode) {
        return upathSet(upath, record, target, UPathExecutionContext.FULL_TREE, mode);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upathSet(UPath upath, DataRecord record, Attribute target, UPathExecutionContext context,
            UPathApplicationMode mode) {

        if (context == UPathExecutionContext.FULL_TREE) {
            return upathFullTreeSetImpl(upath, record, target, mode);
        } else if (context == UPathExecutionContext.SUB_TREE) {
            return upathSubTreeSetImpl(upath, record, target, mode);
        }

        return false;
    }

    private UPathResult upathSubTreeGetImpl(UPath upath, DataRecord record, UPathApplicationMode mode) {

        UPathResult result = new UPathResult(mode);

        // 1. Return immediately if this upath is empty or no input exist
        if (Objects.isNull(record) || upath.getElements().isEmpty()) {
            return result;
        }

        List<Pair<String, DataRecord>> segmentsChain = toSegmentsChain(record);

        // 2. Check root path and return immediately
        if (upath.isRoot()) {
            result.getAttributes().add(ComplexAttributeImpl.ofUnattended(UPathConstants.UPATH_ROOT_NAME, segmentsChain.get(0).getRight()));
            return result;
        }

        // 3. Check number of segments and proceed with diff
        int segmentsDiff = upath.getNumberOfSegments() - (segmentsChain.size() - 1);
        if (segmentsDiff > 0) {

            // 3.1. Childern, possibly filtered
            UPath subPath = upath.getSubSegmentsUPath(segmentsChain.size() - 1);
            return upathFullTreeGetImpl(subPath, record, mode);
        } else {

            // 3.2. Ancestors path processing
            List<UPathElement> upathSegments = upath.getSegments();
            for (int i = 0; i < upathSegments.size(); i++) {

                UPathElement element = upathSegments.get(i);
                Pair<String, DataRecord> source = segmentsChain.get(i);

                // 3.2.1 Check attribute exists to ensure path validity
                if (i < (upathSegments.size() - 1)) {

                    Attribute selection = source.getRight().getAttribute(element.getElement());
                    if (Objects.isNull(selection) || selection.getAttributeType() != AttributeType.COMPLEX) {

                        // Path lost. Return.
                        return result;
                    }

                    continue;
                }

                // 3.2.2 Last segment
                Attribute selection = source.getRight().getAttribute(element.getElement());
                if (Objects.nonNull(selection)) {

                    // 3.2.1.1 Complex attribute
                    if (selection.getAttributeType() == AttributeType.COMPLEX && segmentsChain.size() > (i + 1)) {
                        source = segmentsChain.get(i + 1);
                        result.getAttributes().add(ComplexAttributeImpl.ofUnattended(source.getLeft(), source.getRight()));
                    // 3.2.1.2 Simple, code, array
                    } else {
                        result.getAttributes().add(selection);
                    }
                }
            }
        }

        return result;
    }

    private UPathResult upathFullTreeGetImpl(UPath upath, DataRecord record, UPathApplicationMode mode) {

        UPathResult result = new UPathResult(mode);

        // Return immediately if this upath is empty or no input exist
        if (Objects.isNull(record) || upath.getElements().isEmpty()) {
            return result;
        }

        List<Attribute> collected = new ArrayList<>();
        List<Attribute> packaged = new ArrayList<>();

        // Add the first one for iteration.
        packaged.add(ComplexAttributeImpl.ofUnattended(UPathConstants.UPATH_ROOT_NAME, record));

        for (int i = 0; i < upath.getElements().size(); i++) {

            // Nothing filtered.
            if (packaged.isEmpty()) {
                break;
            }

            UPathElement element = upath.getElements().get(i);
            boolean isComplex = false;
            boolean isTerminating = i == (upath.getElements().size() - 1);

            // For each complex attribute
            for (ListIterator<Attribute> ci = packaged.listIterator(); ci.hasNext(); ) {

                ComplexAttribute holder = (ComplexAttribute) ci.next();
                for (Iterator<DataRecord> li = holder.iterator(); li.hasNext(); ) {

                    // Filtering
                    DataRecord dr = li.next();
                    if (element.isFiltering()) {

                        if (!element.getFilter().matches(dr)) {
                            li.remove();
                        }

                        continue;
                    }

                    // Collecting
                    Attribute attr = dr.getAttribute(element.getElement());
                    if (Objects.nonNull(attr)) {

                        isComplex = attr.getAttributeType() == AttributeType.COMPLEX;
                        if (!isTerminating && !isComplex) {
                            final String message = "Attribute selected for an intermediate path element [{}] is not a complex attribute.";
                            LOGGER.warn(message, element.getElement());
                            throw new PlatformFailureException(message,
                                    CoreExceptionIds.EX_UPATH_NOT_A_COMPLEX_ATTRIBUTE_FOR_INTERMEDIATE_PATH_ELEMENT,
                                    element.getElement());
                        }

                        // UN-9738 handle complex attributes with no records
                        if (isComplex && !isTerminating) {
                            ComplexAttribute ca = attr.narrow();
                            if (ca.isEmpty() && mode == UPathApplicationMode.MODE_ALL_WITH_INCOMPLETE) {
                                result.getIncomplete().add(new UPathIncompletePath(dr, element));
                                // Prevent collecting of empty attribute
                                continue;
                            }
                        }

                        collected.add(attr);
                    } else {
                        // Collect incomplete element, if requested
                        if (mode == UPathApplicationMode.MODE_ALL_WITH_INCOMPLETE) {
                            result.getIncomplete().add(new UPathIncompletePath(dr, element));
                        }
                    }
                }

                // Remove attribute, if empty
                if (holder.isEmpty()) {
                    ci.remove();
                }
            }

            if (element.isCollecting()) {

                packaged.clear();
                if (isComplex) {
                    for (Attribute attr : collected) {
                        packaged.add(ComplexAttributeImpl.ofUnattended(attr.getName(), ((ComplexAttribute) attr).toCollection()));
                    }

                    collected.clear();
                }
            }
        }

        // Return only one value.
        // Wait until this is reached, because of the case with filtered and of path, denoting complex attributes
        if (mode == UPathApplicationMode.MODE_ONCE) {

            if (CollectionUtils.isNotEmpty(collected)) {
                result.getAttributes().add(collected.get(0));
            }

            if (CollectionUtils.isNotEmpty(packaged)) {
                result.getAttributes().add(packaged.get(0));
            }
        } else {

            result.getAttributes().addAll(collected);
            result.getAttributes().addAll(packaged);
        }

        return result;
    }
    /**
     * Sets the attribute to target record.
     * @param upath UPath to process
     * @param data the record to manipulate
     * @param target target attribute to set
     * @param mode application mode
     * @return modified record
     */
    private boolean upathSubTreeSetImpl(UPath upath, DataRecord data, Attribute target, UPathApplicationMode mode) {

        // 1. Return immediately if this upath is empty, denotes root (what is not allowed) or record is null
        if (upath.getElements().isEmpty() || upath.isRoot() || Objects.isNull(data)) {
            return false;
        }

        // 2. Collect segments
        List<Pair<String, DataRecord>> segmentsChain = toSegmentsChain(data);

        // 3. Check number of segments and proceed with diff
        int segmentsDiff = upath.getNumberOfSegments() - (segmentsChain.size() - 1);
        if (segmentsDiff > 0) {

            // 3.1. Childern, possibly filtered
            UPath subPath = upath.getSubSegmentsUPath(segmentsChain.size() - 1);
            return upathFullTreeSetImpl(subPath, data, target, mode);
        } else {

            // 3.2. Check, we're generally able to set with params
            checkGeneralSetAbility(upath.getElements().get(upath.getElements().size() - 1), target);

            // 3.3. Ancestors path processing
            List<UPathElement> upathSegments = upath.getSegments();
            for (int i = 0; i < upathSegments.size(); i++) {

                UPathElement element = upathSegments.get(i);
                Pair<String, DataRecord> source = segmentsChain.get(i);

                // 3.2.1 Check attribute exists to ensure path validity
                if (i < (upathSegments.size() - 1)) {

                    Attribute selection = source.getRight().getAttribute(element.getElement());
                    if (Objects.isNull(selection) || selection.getAttributeType() != AttributeType.COMPLEX) {

                        // Path lost. Return.
                        return false;
                    }

                    continue;
                }

                // 3.2.2 Last segment. Put.
                source.getRight().addAttribute(target);
                return true;
            }
        }

        return false;
    }
    /**
     * Sets the attribute to target record.
     * @param upath UPath to process
     * @param source the record to manipulate
     * @param target target attribute to set
     * @param mode application mode
     * @return modified record
     */
    private boolean upathFullTreeSetImpl(UPath upath, DataRecord source, Attribute target, UPathApplicationMode mode) {

        // Return immediately if this upath is empty
        if (upath.getElements().isEmpty() || Objects.isNull(source)) {
            return false;
        }

        // Check, we're generally able to set with params
        checkGeneralSetAbility(upath.getElements().get(upath.getElements().size() - 1), target);

        // Think about whether such behaviour is really desired.
        DataRecord record = Objects.isNull(source) ? new SerializableDataRecord() : source;

        List<Attribute> collected = new ArrayList<>(16);
        List<Attribute> packaged = new ArrayList<>(8);

        // Add the first one for iteration.
        packaged.add(ComplexAttributeImpl.ofUnattended("ROOT", record));

        boolean hadApplications = false;
        for (int i = 0; i < upath.getElements().size(); i++) {

            // Nothing filtered.
            if (packaged.isEmpty()) {
                break;
            }

            UPathElement element = upath.getElements().get(i);
            boolean isComplex = false;
            boolean isTerminating = i == (upath.getElements().size() - 1);

            // For each complex attribute
            for (ListIterator<Attribute> ci = packaged.listIterator(); ci.hasNext(); ) {

                ComplexAttribute holder = (ComplexAttribute) ci.next();
                for (Iterator<DataRecord> li = holder.iterator(); li.hasNext(); ) {

                    // Filtering
                    DataRecord dr = li.next();
                    if (element.isFiltering()) {

                        if (!element.getFilter().matches(dr)) {
                            li.remove();
                        }

                        continue;
                    }

                    // Set
                    if (isTerminating) {

                        dr.addAttribute(target);

                        // And finish
                        if (mode == UPathApplicationMode.MODE_ALL || mode == UPathApplicationMode.MODE_ALL_WITH_INCOMPLETE) {
                            hadApplications = true;
                            continue;
                        } else if (mode == UPathApplicationMode.MODE_ONCE) {
                            return true;
                        }
                    }

                    // Collecting
                    Attribute attr = dr.getAttribute(element.getElement());
                    if (Objects.nonNull(attr)) {

                        isComplex = attr.getAttributeType() == AttributeType.COMPLEX;
                        if (!isComplex) {
                            final String message = "Attribute selected for an intermediate path element [{}] is not a complex attribute.";
                            LOGGER.warn(message, element.getElement());
                            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_SET_NOT_A_COMPLEX_FOR_INTERMEDIATE, element.getElement());
                        }

                        collected.add(attr);
                    }
                }

                // Remove attribute, if empty
                if (holder.isEmpty()) {
                    ci.remove();
                }
            }

            if (element.isCollecting()) {

                packaged.clear();
                if (isComplex) {
                    for (Attribute attr : collected) {
                        packaged.add(ComplexAttributeImpl.ofUnattended(attr.getName(), ((ComplexAttribute) attr).toCollection()));
                    }

                    collected.clear();
                }
            }
        }

        return hadApplications;
    }
    /**
     * Builds segments chain from bottom to top.
     * @param last the lowest end of the hierarсhie
     * @return chain
     */
    private List<Pair<String, DataRecord>> toSegmentsChain(DataRecord last) {

        if (Objects.isNull(last)) {
            return Collections.emptyList();
        }

        List<Pair<String, DataRecord>> chain = new ArrayList<>();
        DataRecord backpointer = last;
        while (!backpointer.isTopLevel()) {
            chain.add(0, Pair.of(backpointer.getHolderAttribute().getName(), backpointer));
            backpointer = backpointer.getParentRecord();
        }

        chain.add(0, Pair.of(UPathConstants.UPATH_ROOT_NAME, backpointer));
        return chain;
    }
    /**
     * MMI: Taken from apache-commons StringUtils and modified for our needs.
     *
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that do not return a
     * maximum array length.
     *
     * @param str  the String to parse, may not be {@code null}
     * @param separatorChar the separate character
     * @param preserveAllTokens if {@code true}, adjacent separators are
     * treated as empty token separators; if {@code false}, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private String[] splitPath(@Nonnull final String str) {

        final int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        final List<String> list = new ArrayList<>();

        int i = 0;
        int start = 0;
        int expr = 0;
        char current;
        while (i < len) {

            current = str.charAt(i);
            if (expr == 0 && current == UPathConstants.UPATH_SEPARATOR_CHAR) {

                if (i > start) {

                    // UD, honor escape sym followed by path separator,
                    // which may be part of expression
                    if (str.charAt(i - 1) == UPathConstants.UPATH_ESCAPE_CHAR) {
                        i++;
                        continue;
                    }

                    list.add(str.substring(start, i));
                }

                // Move to next position,
                // but not increase counter (done below)
                start = i + 1;
            } else if (current == UPathConstants.UPATH_EXPRESSION_START) {
                expr++;
            } else if (current == UPathConstants.UPATH_EXPRESSION_END) {
                expr--;
            }

            i++;
        }

        if (i > start) {
            list.add(str.substring(start, i));
        }

        return list.toArray(String[]::new);
    }
    /**
     * Checks the general ability to set the target attribute to this UPath.
     * @param last last UPath element
     * @param target the target attribute
     */
    private void checkGeneralSetAbility(UPathElement last, Attribute target) {

        if (last.getType() != UPathElementType.COLLECTOR) {
            final String message = "Invalid input. UPath for set operations must end with collecting element. Element '{}' is not a collecting one.";
            LOGGER.warn(message, last.getElement());
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_SET_WRONG_END_ELEMENT, last.getElement());
        }

        if (!StringUtils.equals(last.getElement(), target.getName())) {
            final String message = "Invalid input. Attribute '{}' and last UPath element '{}' have different names.";
            LOGGER.warn(message, target.getName(), last.getElement());
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_SET_WRONG_ATTRIBUTE_NAME, target.getName(), last.getElement());
        }
    }
    /**
     * Check root record special notation.
     * @param upath the {@link UPath} currently being built
     * @param element the element being processed
     * @return true, if element has root special notation, false otherwise
     */
    private boolean checkRootRecord(UPath upath, String element) {

        int start = element.indexOf(UPathConstants.UPATH_EXPRESSION_START);
        if (start == -1) {
            return false;
        }

        int end = element.indexOf(UPathConstants.UPATH_EXPRESSION_END, start);
        if (end == -1) {
            final String message = "Invalid input. Root record expression incorrect [{}].";
            LOGGER.warn(message, element);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_ROOT_EXPRESSION, element);
        }

        boolean isUnfilteredRoot = end - start == 1;
        if (isUnfilteredRoot) {
            upath.getElements().add(new PredicateElementImpl(UPathConstants.UPATH_ROOT_NAME, DataRecord::isTopLevel));
            return true;
        } else {
            return checkExpressionFilter(upath, element);
        }
    }
    /**
     * Check subscript (record ordinal).
     * @param upath the {@link UPath} currently being built
     * @param element the element being processed
     * @return true, if element has subscript filtering, false otherwise
     */
    private boolean checkSubscriptFilter(UPath upath, String element) {

        int start = element.indexOf(UPathConstants.UPATH_SUBSCRIPT_START);
        if (start == -1) {
            return false;
        }

        int end = element.indexOf(UPathConstants.UPATH_SUBSCRIPT_END, start);
        if (end == -1) {
            final String message = "Invalid input. Subscript expression incorrect [{}].";
            LOGGER.warn(message, element);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_SUBSCRIPT_EXPRESSION, element);
        }

        final String subscriptAsString = element.substring(start + 1, end);
        final int ordinal = Integer.parseUnsignedInt(subscriptAsString);
        final String subpath = element.substring(0, start);

        upath.getElements().add(new CollectorElementImpl(subpath));
        upath.getElements().add(new SubscriptElementImpl(element.substring(start, (end + 1)), ordinal));

        return true;
    }
    /**
     * Checks the supplied expression.
     * @param upath current upath
     * @param element the element name
     * @return true, if the element has been processed, false otherwise
     */
    private boolean checkExpressionFilter(UPath upath, String element) {

        int start = element.indexOf(UPathConstants.UPATH_EXPRESSION_START);
        if (start == -1) {
            return false;
        }

        String name = element.substring(0, start);

        int end = element.length() - 1;
        if (element.charAt(end) != UPathConstants.UPATH_EXPRESSION_END) {
            final String message = "Malformed UPath expression [{}].";
            LOGGER.warn(message, element);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_MALFORMED_EXPRESSION, element);
        }

        String value = StringUtils.trimToNull(element.substring(start + 1, end));

        // name == "", but the rest is ok - this is a root expression
        // omit complex attribute part
        boolean hasPath = StringUtils.isNotBlank(name);
        if (hasPath) {
            upath.getElements().add(new CollectorElementImpl(name));
        }

        boolean hasExpression = StringUtils.isNotBlank(value);
        if (hasExpression) {
            upath.getElements().add(new ExpressionElementImpl(value, expressions.computeIfAbsent(value, k -> {
                ExpressionCompiler ec = new ExpressionCompiler(k, EVALUATION_CONTEXT);
                return ec.compile();
            })));
        }

        return hasPath || hasExpression;
    }
    /**
     * Checks and sets namespace and type name if possible.
     * @param element the element being processed
     */
    private UPath init(String element) {

        int count = 0;
        int prev = 0;
        int pos;

        String nameSpace = null;
        String typeName = null;
        while ((pos = element.indexOf(':', prev)) != -1) {

            if (count > 2) {
                final String message = "Malformed UPath expression [{}]. Malformed intro (namespace:typename:).";
                LOGGER.warn(message, element);
                throw new PlatformFailureException(message, CoreExceptionIds.EX_UPATH_INVALID_INPUT_MORE_THEN_TWO_COLONS, element);
            }

            if ((pos - prev) > 0) {

                String part = element.substring(prev, pos);
                switch (count) {
                case 0:
                    nameSpace = part;
                    break;
                case 1:
                    typeName = part;
                    break;
                default:
                    break;
                }
            }

            prev = (pos + 1);
            count++;
        }

        return new UPath(nameSpace, typeName);
    }
}
