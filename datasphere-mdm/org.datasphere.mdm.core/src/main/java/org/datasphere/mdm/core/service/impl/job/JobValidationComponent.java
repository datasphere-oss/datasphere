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

package org.datasphere.mdm.core.service.impl.job;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.datasphere.mdm.core.dao.JobDefinitionsDAO;
import org.datasphere.mdm.core.module.CoreModule;
import org.datasphere.mdm.core.po.job.JobDefinitionPO;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDefinition;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.exception.ValidationResult;

/**
 * @author Denis Kostovarov
 */
@Component
public class JobValidationComponent {
    /**
     * Jon name length exceeded.
     */
    private static final String JOB_DEFINITION_NAME_EXCEEDS_LENGTH = CoreModule.MODULE_ID + ".job.name.exceeds.length";
    /**
     * Cron expression generally invalid.
     */
    private static final String JOB_DEFINITION_CRON_EXPRESSION = CoreModule.MODULE_ID + ".job.cron.expression";
    /**
     * Suspicious second spec.
     */
    private static final String JOB_DEFINITION_CRON_SUSPICIOUS_SECOND = CoreModule.MODULE_ID + ".job.cron.suspicious.second";
    /**
     * Suspicious minute spec.
     */
    private static final String JOB_DEFINITION_CRON_SUSPICIOUS_MINUTE = CoreModule.MODULE_ID + ".job.cron.suspicious.minute";
    /**
     * Suspicious short cycles spec.
     */
    private static final String JOB_DEFINITION_CRON_SUSPICIOUS_SHORT_CYCLES_DOM = CoreModule.MODULE_ID + ".job.cron.suspicious.cycles.dom";
    /**
     * Param name exceeds allowed length.
     */
    private static final String JOB_DEFINITION_PARAM_NAME_EXCEEDS_LENGTH = CoreModule.MODULE_ID + ".job.parameter.name.length";
    /**
     * Param name is not unique.
     */
    private static final String JOB_DEFINITION_PARAM_NAME_NOT_UNIQUE = CoreModule.MODULE_ID + ".job.parameter.name.unique";
    /**
     * Param required but missing.
     */
    private static final String JOB_DEFINITION_PARAM_REQUIRED_MISSING = CoreModule.MODULE_ID + ".job.parameter.required.missing";
    /**
     * Unknown parameters.
     */
    private static final String JOB_DEFINITION_PARAM_UNKNOWN = CoreModule.MODULE_ID + ".job.unknown.parameters";
    /**
     * Job name is not unique.
     */
    private static final String JOB_DEFINITION_JOB_NAME_NOT_UNIQUE = CoreModule.MODULE_ID + ".job.same.name";
    /**
     * Invalid parameter value according to validator (message).
     */
    private static final String JOB_DEFINITION_INVALID_PARAMETER_TEXT = "Parameter [{}] considered inavlid by validator function.";
    /**
     * Invalid parameter value according to validator (translation code).
     */
    private static final String JOB_DEFINITION_INVALID_PARAMETER_CODE = CoreModule.MODULE_ID + ".job.definition.parameter.invalid";
    /**
     * Invalid parameter value according to validator (translation code).
     */
    private static final String JOB_DEFINITION_INCOMPATIBLE_PARAMETER_TYPES = CoreModule.MODULE_ID + ".job.definition.parameter.incompatible.types";
    /**
     * Invalid parameter value according to validator (translation code).
     */
    private static final String JOB_DEFINITION_INCOMPATIBLE_PARAMETER_LAYOUT = CoreModule.MODULE_ID + ".job.definition.parameter.incompatible.layout";
    /**
     * Job name field allowed length.
     */
    private static final int JOB_NAME_LIMIT = 100;
    /**
     * Param name field allowed length.
     */
    private static final int PARAM_NAME_LIMIT = 100;

    private static final Set<Integer> CRON_ALL_SECONDS_MINUTES;

    static {
        CRON_ALL_SECONDS_MINUTES = new HashSet<>();
        for (int i = 0; i < 60; ++i) {
            CRON_ALL_SECONDS_MINUTES.add(i);
        }
    }

    @Autowired
    private JobDefinitionsDAO jobDefinitionsDAO;

    @Autowired
    private CustomJobRegistry jobRegistry;

    public JobValidationComponent() {
        super();
    }

    public List<ValidationResult> validate(JobDefinition def, boolean skipCronWarnings) {

        Objects.requireNonNull(def, "Job definition must not be null.");

        final List<ValidationResult> validations = new ArrayList<>();

        // 1. Name
        validations.addAll(validateName(def));

        // 2. Check cron expression syntax.
        validations.addAll(validateCronExpression(def.getCronExpression(), skipCronWarnings));

        return validations;
    }

    public List<ValidationResult> validate(String jobName, final Map<String, JobParameterDefinition<?>> parameters) {

        final JobDescriptor descriptor = jobRegistry.getDescriptor(jobName);
        final List<ValidationResult> failed = new ArrayList<>();
        final Map<String, Integer> unique = new HashMap<>();
        final Set<String> unknown = new HashSet<>();

        for (final JobParameterDefinition<?> p : parameters.values()) {

            // 1. Unique (check the object itself to catch typos and C&P).
            unique.compute(p.getName(), (k, v) -> Objects.isNull(v) ? Integer.valueOf(1) : Integer.valueOf(v.intValue() + 1));

            JobParameterDescriptor<?> d = descriptor.findParameter(p.getName());

            // 2. Unknown
            if (Objects.isNull(d)) {
                unknown.add(p.getName());
                continue;
            }

            // 3. Validator
            ValidationResult pr = validateParameter(d, p);
            if (Objects.nonNull(pr)) {
                failed.add(pr);
            }

            // 4. Name length
            if (p.getName().length() > PARAM_NAME_LIMIT) {
                failed.add(new ValidationResult("Job parameter name [{}] exceeds length limit.",
                        JOB_DEFINITION_PARAM_NAME_EXCEEDS_LENGTH, p.getName()));
            }
        }

        // 5. Duplicates
        List<String> hits = unique.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Entry::getKey)
            .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(hits)) {
            failed.add(new ValidationResult("Job parameter name(s) [{}] is not unique.",
                    JOB_DEFINITION_PARAM_NAME_NOT_UNIQUE, hits));
        }

        // 6. Required, but missing
        descriptor.getAllParameters().values().stream()
            .filter(JobParameterDescriptor::isRequired)
            .forEach(d -> {

                JobParameterDefinition<?> p = parameters.get(d.getName());
                if (Objects.isNull(p) || p.isEmpty()) {
                    failed.add(new ValidationResult("Job parameter [{}] is is required, but missing.",
                            JOB_DEFINITION_PARAM_REQUIRED_MISSING, d.getName()));
                }
            });

        // 7. Unknown parameters
        if (CollectionUtils.isNotEmpty(unknown)) {
            failed.add(new ValidationResult("Unknown parameters supplied for job [{}] : [{}].",
                    JOB_DEFINITION_PARAM_UNKNOWN, jobName, unknown));
        }

        return failed;
    }

    @SuppressWarnings("unchecked")
    private ValidationResult validateParameter(JobParameterDescriptor<?> d, JobParameterDefinition<?> p) {

        if (d.getType() != p.getType()) {
            return new ValidationResult("Parameter [{}] has type [{}], incompatible with descriptor's type [{}].",
                    JOB_DEFINITION_INCOMPATIBLE_PARAMETER_TYPES, d.getDisplayName(), p.getType().name(), d.getType().name());
        }

        if (d.getKind() != p.getKind()) {
            return new ValidationResult("Parameter [{}] has layout [{}], incompatible with descriptor's layout [{}].",
                    JOB_DEFINITION_INCOMPATIBLE_PARAMETER_LAYOUT, d.getDisplayName(), p.getKind().name(), d.getKind().name());
        }

        switch (d.getType()) {
        case BOOLEAN:
            return validateBoolean((JobParameterDescriptor<Boolean>) d, (JobParameterDefinition<Boolean>) p);
        case DATE:
            return validateDate((JobParameterDescriptor<LocalDate>) d, (JobParameterDefinition<LocalDate>) p);
        case INSTANT:
            return validateInstant((JobParameterDescriptor<Instant>) d, (JobParameterDefinition<Instant>) p);
        case INTEGER:
            return validateInteger((JobParameterDescriptor<Long>) d, (JobParameterDefinition<Long>) p);
        case NUMBER:
            return validateNumber((JobParameterDescriptor<Double>) d, (JobParameterDefinition<Double>) p);
        case CLOB:
        case STRING:
            return validateString((JobParameterDescriptor<String>) d, (JobParameterDefinition<String>) p);
        case TIME:
            return validateTime((JobParameterDescriptor<LocalTime>) d, (JobParameterDefinition<LocalTime>) p);
        case TIMESTAMP:
            return validateTimestamp((JobParameterDescriptor<LocalDateTime>) d, (JobParameterDefinition<LocalDateTime>) p);
        default:
            break;
        }

        return null;
    }

    private ValidationResult validateBoolean(JobParameterDescriptor<Boolean> d, JobParameterDefinition<Boolean> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateDate(JobParameterDescriptor<LocalDate> d, JobParameterDefinition<LocalDate> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateInstant(JobParameterDescriptor<Instant> d, JobParameterDefinition<Instant>p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateInteger(JobParameterDescriptor<Long> d, JobParameterDefinition<Long> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateNumber(JobParameterDescriptor<Double> d, JobParameterDefinition<Double> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateString(JobParameterDescriptor<String> d, JobParameterDefinition<String> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateTime(JobParameterDescriptor<LocalTime> d, JobParameterDefinition<LocalTime> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private ValidationResult validateTimestamp(JobParameterDescriptor<LocalDateTime> d, JobParameterDefinition<LocalDateTime> p) {

        if ((d.isCollection() && !d.validate(p.collection()))
         || (d.isMap() && !d.validate(p.map()))
         || (d.isSingle() && !d.validate(p.single()))) {
            return new ValidationResult(JOB_DEFINITION_INVALID_PARAMETER_TEXT, JOB_DEFINITION_INVALID_PARAMETER_CODE, d.getDisplayName());
        }

        return null;
    }

    private Collection<ValidationResult> validateName(JobDefinition definition) {

        final List<ValidationResult> validations = new ArrayList<>();

        List<JobDefinitionPO> hits
            = jobDefinitionsDAO.load(Map.of(JobDefinitionPO.FIELD_NAME, Collections.singleton(definition.getDisplayName())),
                    null, null, null, null);

        if (CollectionUtils.isNotEmpty(hits) && !hits.get(0).getId().equals(definition.getId())) {
            validations.add(new ValidationResult("Job with display name [{}] already exists.",
                    JOB_DEFINITION_JOB_NAME_NOT_UNIQUE, definition.getDisplayName()));
        }

        if (definition.getDisplayName().length() > JOB_NAME_LIMIT) {
            validations.add(new ValidationResult("Job definition display name [{}] exceeds allowed name length [{}].",
                    JOB_DEFINITION_NAME_EXCEEDS_LENGTH, definition.getDisplayName(), JOB_NAME_LIMIT));
        }

        return validations;
    }

    private Collection<ValidationResult> validateCronExpression(final String cronExpression, final boolean ignoreWarnings) {

        if (StringUtils.isBlank(cronExpression)) {
            return Collections.emptyList();
        }

        List<ValidationResult> result = new ArrayList<>();
        try {

            if (ignoreWarnings) {
                CronExpressionAdapter.validateExpression(cronExpression);
            } else {

                final CronExpressionAdapter expr = new CronExpressionAdapter(cronExpression);
                final Set<Integer> seconds = expr.getSecondSet();
                final Set<Integer> minutes = expr.getMinuteSet();
                final Set<Integer> daysOfMonth = expr.getDayOfMonthSet();
                final Set<Integer> temp = new HashSet<>(seconds);

                temp.retainAll(CRON_ALL_SECONDS_MINUTES);
                if (temp.size() == CRON_ALL_SECONDS_MINUTES.size()) {
                    result.add(new ValidationResult("Cron expression suspiciously starts operation every minute.",
                            JOB_DEFINITION_CRON_SUSPICIOUS_MINUTE));
                }

                temp.clear();
                temp.addAll(minutes);
                temp.retainAll(CRON_ALL_SECONDS_MINUTES);
                if (temp.size() == CRON_ALL_SECONDS_MINUTES.size()) {
                    result.add(new ValidationResult("Cron expression suspiciously starts operation every second.",
                            JOB_DEFINITION_CRON_SUSPICIOUS_SECOND));
                }

                // a bit arbitrary
                if (daysOfMonth.size() > 5 && daysOfMonth.size() < 28) {
                    result.add(new ValidationResult("Short cycles in \"day of the month\" field will be unpredictable closer to the end of the month.",
                            JOB_DEFINITION_CRON_SUSPICIOUS_SHORT_CYCLES_DOM));
                }
            }
        } catch (final ParseException e) {
            result.add(new ValidationResult("Cron expression generally invalid.",
                    JOB_DEFINITION_CRON_EXPRESSION));
        }

        return result;
    }

    /**
     * Code from {@link CronExpression} in order to gain access to parsed expression and objects.
     */
    private static class CronExpressionAdapter {

        private static final int SECOND = 0;
        private static final int MINUTE = 1;
        private static final int HOUR = 2;
        private static final int DAY_OF_MONTH = 3;
        private static final int MONTH = 4;
        private static final int DAY_OF_WEEK = 5;
        private static final int YEAR = 6;
        private static final int ALL_SPEC_INT = 99; // '*'
        private static final int NO_SPEC_INT = 98; // '?'
        private static final Integer ALL_SPEC = ALL_SPEC_INT;
        private static final Integer NO_SPEC = NO_SPEC_INT;

        private static final Map<String, Integer> monthMap = new HashMap<>(20);
        private static final Map<String, Integer> dayMap = new HashMap<>(60);
        static {
            monthMap.put("JAN", 0);
            monthMap.put("FEB", 1);
            monthMap.put("MAR", 2);
            monthMap.put("APR", 3);
            monthMap.put("MAY", 4);
            monthMap.put("JUN", 5);
            monthMap.put("JUL", 6);
            monthMap.put("AUG", 7);
            monthMap.put("SEP", 8);
            monthMap.put("OCT", 9);
            monthMap.put("NOV", 10);
            monthMap.put("DEC", 11);

            dayMap.put("SUN", 1);
            dayMap.put("MON", 2);
            dayMap.put("TUE", 3);
            dayMap.put("WED", 4);
            dayMap.put("THU", 5);
            dayMap.put("FRI", 6);
            dayMap.put("SAT", 7);
        }

        protected TreeSet<Integer> seconds;
        private TreeSet<Integer> minutes;
        private TreeSet<Integer> hours;
        private TreeSet<Integer> daysOfMonth;
        private TreeSet<Integer> months;
        private TreeSet<Integer> daysOfWeek;
        private TreeSet<Integer> years;

        private int nthdayOfWeek = 0;
        private boolean lastdayOfMonth = false;

        private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR) + 100;

        CronExpressionAdapter(String cronExpression) throws ParseException {
            buildExpression(cronExpression.toUpperCase(Locale.US));
        }

        static void validateExpression(String cronExpression) throws ParseException {
            CronExpression.validateExpression(cronExpression);
        }

        Set<Integer> getSecondSet() {
            return seconds;
        }

        Set<Integer> getMinuteSet() {
            return minutes;
        }

        Set<Integer> getDayOfMonthSet() {
            return daysOfMonth;
        }

        private void buildExpression(String expression) throws ParseException {

            try {

                if (seconds == null) {
                    seconds = new TreeSet<>();
                }
                if (minutes == null) {
                    minutes = new TreeSet<>();
                }
                if (hours == null) {
                    hours = new TreeSet<>();
                }
                if (daysOfMonth == null) {
                    daysOfMonth = new TreeSet<>();
                }
                if (months == null) {
                    months = new TreeSet<>();
                }
                if (daysOfWeek == null) {
                    daysOfWeek = new TreeSet<>();
                }
                if (years == null) {
                    years = new TreeSet<>();
                }

                int exprOn = SECOND;

                StringTokenizer exprsTok = new StringTokenizer(expression, " \t",
                        false);

                while (exprsTok.hasMoreTokens() && exprOn <= YEAR) {
                    String expr = exprsTok.nextToken().trim();

                    // throw an exception if L is used with other days of the month
                    if(exprOn == DAY_OF_MONTH && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
                        throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1);
                    }
                    // throw an exception if L is used with other days of the week
                    if(exprOn == DAY_OF_WEEK && expr.indexOf('L') != -1 && expr.length() > 1  && expr.contains(",")) {
                        throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
                    }
                    if(exprOn == DAY_OF_WEEK && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') +1) != -1) {
                        throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
                    }

                    StringTokenizer vTok = new StringTokenizer(expr, ",");
                    while (vTok.hasMoreTokens()) {
                        String v = vTok.nextToken();
                        storeExpressionVals(0, v, exprOn);
                    }

                    exprOn++;
                }

                if (exprOn <= DAY_OF_WEEK) {
                    throw new ParseException("Unexpected end of expression.",
                            expression.length());
                }

                if (exprOn <= YEAR) {
                    storeExpressionVals(0, "*", YEAR);
                }

                TreeSet<Integer> dow = getSet(DAY_OF_WEEK);
                TreeSet<Integer> dom = getSet(DAY_OF_MONTH);

                // Copying the logic from the UnsupportedOperationException below
                boolean dayOfMSpec = !dom.contains(NO_SPEC);
                boolean dayOfWSpec = !dow.contains(NO_SPEC);

                if (!dayOfMSpec || dayOfWSpec) {
                    if (!dayOfWSpec || dayOfMSpec) {
                        throw new ParseException(
                                "Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0);
                    }
                }
            } catch (ParseException pe) {
                throw pe;
            } catch (Exception e) {
                throw new ParseException("Illegal cron expression format ("
                        + e.toString() + ")", 0);
            }
        }

        private int storeExpressionVals(int pos, String s, int type)
                throws ParseException {

            int incr = 0;
            int i = skipWhiteSpace(pos, s);
            if (i >= s.length()) {
                return i;
            }
            char c = s.charAt(i);
            if ((c >= 'A') && (c <= 'Z') && (!s.equals("L")) && (!s.equals("LW")) && (!s.matches("^L-[0-9]*[W]?"))) {
                String sub = s.substring(i, i + 3);
                int sval = -1;
                int eval = -1;
                if (type == MONTH) {
                    sval = getMonthNumber(sub) + 1;
                    if (sval <= 0) {
                        throw new ParseException("Invalid Month value: '" + sub + "'", i);
                    }
                    if (s.length() > i + 3) {
                        c = s.charAt(i + 3);
                        if (c == '-') {
                            i += 4;
                            sub = s.substring(i, i + 3);
                            eval = getMonthNumber(sub) + 1;
                            if (eval <= 0) {
                                throw new ParseException("Invalid Month value: '" + sub + "'", i);
                            }
                        }
                    }
                } else if (type == DAY_OF_WEEK) {
                    sval = getDayOfWeekNumber(sub);
                    if (sval < 0) {
                        throw new ParseException("Invalid Day-of-Week value: '"
                                + sub + "'", i);
                    }
                    if (s.length() > i + 3) {
                        c = s.charAt(i + 3);
                        if (c == '-') {
                            i += 4;
                            sub = s.substring(i, i + 3);
                            eval = getDayOfWeekNumber(sub);
                            if (eval < 0) {
                                throw new ParseException(
                                        "Invalid Day-of-Week value: '" + sub
                                                + "'", i);
                            }
                        } else if (c == '#') {
                            try {
                                i += 4;
                                nthdayOfWeek = Integer.parseInt(s.substring(i));
                                if (nthdayOfWeek < 1 || nthdayOfWeek > 5) {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                throw new ParseException(
                                        "A numeric value between 1 and 5 must follow the '#' option",
                                        i);
                            }
                        } else if (c == 'L') {
                            i++;
                        }
                    }

                } else {
                    throw new ParseException(
                            "Illegal characters for this position: '" + sub + "'",
                            i);
                }
                if (eval != -1) {
                    incr = 1;
                }
                addToSet(sval, eval, incr, type);
                return (i + 3);
            }

            if (c == '?') {
                i++;
                if ((i + 1) < s.length()
                        && (s.charAt(i) != ' ' && s.charAt(i + 1) != '\t')) {
                    throw new ParseException("Illegal character after '?': "
                            + s.charAt(i), i);
                }
                if (type != DAY_OF_WEEK && type != DAY_OF_MONTH) {
                    throw new ParseException(
                            "'?' can only be specfied for Day-of-Month or Day-of-Week.",
                            i);
                }
                if (type == DAY_OF_WEEK && !lastdayOfMonth) {
                    int val = daysOfMonth.last();
                    if (val == NO_SPEC_INT) {
                        throw new ParseException(
                                "'?' can only be specfied for Day-of-Month -OR- Day-of-Week.",
                                i);
                    }
                }

                addToSet(NO_SPEC_INT, -1, 0, type);
                return i;
            }

            if (c == '*' || c == '/') {
                if (c == '*' && (i + 1) >= s.length()) {
                    addToSet(ALL_SPEC_INT, -1, incr, type);
                    return i + 1;
                } else if (c == '/'
                        && ((i + 1) >= s.length() || s.charAt(i + 1) == ' ' || s
                        .charAt(i + 1) == '\t')) {
                    throw new ParseException("'/' must be followed by an integer.", i);
                } else if (c == '*') {
                    i++;
                }
                c = s.charAt(i);
                if (c == '/') { // is an increment specified?
                    i++;
                    if (i >= s.length()) {
                        throw new ParseException("Unexpected end of string.", i);
                    }

                    incr = getNumericValue(s, i);

                    i++;
                    if (incr > 10) {
                        i++;
                    }
                    if (incr > 59 && (type == SECOND || type == MINUTE)) {
                        throw new ParseException("Increment > 60 : " + incr, i);
                    } else if (incr > 23 && (type == HOUR)) {
                        throw new ParseException("Increment > 24 : " + incr, i);
                    } else if (incr > 31 && (type == DAY_OF_MONTH)) {
                        throw new ParseException("Increment > 31 : " + incr, i);
                    } else if (incr > 7 && (type == DAY_OF_WEEK)) {
                        throw new ParseException("Increment > 7 : " + incr, i);
                    } else if (incr > 12 && (type == MONTH)) {
                        throw new ParseException("Increment > 12 : " + incr, i);
                    }
                } else {
                    incr = 1;
                }

                addToSet(ALL_SPEC_INT, -1, incr, type);
                return i;
            } else if (c == 'L') {
                i++;
                if (type == DAY_OF_MONTH) {
                    lastdayOfMonth = true;
                }
                if (type == DAY_OF_WEEK) {
                    addToSet(7, 7, 0, type);
                }
                if(type == DAY_OF_MONTH && s.length() > i) {
                    c = s.charAt(i);
                    if(c == '-') {
                        ValueSet vs = getValue(0, s, i+1);
                        int lastdayOffset = vs.value;
                        if(lastdayOffset > 30)
                            throw new ParseException("Offset from last day must be <= 30", i+1);
                        i = vs.pos;
                    }
                    if(s.length() > i) {
                        c = s.charAt(i);
                        if(c == 'W') {
                            i++;
                        }
                    }
                }
                return i;
            } else if (c >= '0' && c <= '9') {
                int val = Integer.parseInt(String.valueOf(c));
                i++;
                if (i >= s.length()) {
                    addToSet(val, -1, -1, type);
                } else {
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        ValueSet vs = getValue(val, s, i);
                        val = vs.value;
                        i = vs.pos;
                    }
                    i = checkNext(i, s, val, type);
                    return i;
                }
            } else {
                throw new ParseException("Unexpected character: " + c, i);
            }

            return i;
        }

        private int checkNext(int pos, String s, int val, int type)
                throws ParseException {

            int end = -1;
            int i = pos;

            if (i >= s.length()) {
                addToSet(val, end, -1, type);
                return i;
            }

            char c = s.charAt(pos);

            if (c == 'L') {
                if (type == DAY_OF_WEEK) {
                    if(val < 1 || val > 7)
                        throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
                } else {
                    throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
                }
                TreeSet<Integer> set = getSet(type);
                set.add(val);
                i++;
                return i;
            }

            if (c == 'W') {
                if (type != DAY_OF_MONTH) {
                    throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
                }
                if(val > 31)
                    throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
                TreeSet<Integer> set = getSet(type);
                set.add(val);
                i++;
                return i;
            }

            if (c == '#') {
                if (type != DAY_OF_WEEK) {
                    throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
                }
                i++;
                try {
                    nthdayOfWeek = Integer.parseInt(s.substring(i));
                    if (nthdayOfWeek < 1 || nthdayOfWeek > 5) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    throw new ParseException(
                            "A numeric value between 1 and 5 must follow the '#' option",
                            i);
                }

                TreeSet<Integer> set = getSet(type);
                set.add(val);
                i++;
                return i;
            }

            if (c == '-') {
                i++;
                c = s.charAt(i);
                int v = Integer.parseInt(String.valueOf(c));
                end = v;
                i++;
                if (i >= s.length()) {
                    addToSet(val, end, 1, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = getValue(v, s, i);
                    end = vs.value;
                    i = vs.pos;
                }
                if (i < s.length() && ((c = s.charAt(i)) == '/')) {
                    i++;
                    c = s.charAt(i);
                    int v2 = Integer.parseInt(String.valueOf(c));
                    i++;
                    if (i >= s.length()) {
                        addToSet(val, end, v2, type);
                        return i;
                    }
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        ValueSet vs = getValue(v2, s, i);
                        int v3 = vs.value;
                        addToSet(val, end, v3, type);
                        i = vs.pos;
                        return i;
                    } else {
                        addToSet(val, end, v2, type);
                        return i;
                    }
                } else {
                    addToSet(val, end, 1, type);
                    return i;
                }
            }

            if (c == '/') {
                i++;
                c = s.charAt(i);
                int v2 = Integer.parseInt(String.valueOf(c));
                i++;
                if (i >= s.length()) {
                    addToSet(val, end, v2, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = getValue(v2, s, i);
                    int v3 = vs.value;
                    addToSet(val, end, v3, type);
                    i = vs.pos;
                    return i;
                } else {
                    throw new ParseException("Unexpected character '" + c + "' after '/'", i);
                }
            }

            addToSet(val, end, 0, type);
            i++;
            return i;
        }


        private int skipWhiteSpace(int i, String s) {
            while (i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
                i++;
            }

            return i;
        }

        private int findNextWhiteSpace(int i, String s) {
            while (i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t')) {
                i++;
            }

            return i;
        }

        private void addToSet(int val, int end, int incr, int type)
                throws ParseException {

            TreeSet<Integer> set = getSet(type);

            if (type == SECOND || type == MINUTE) {
                if ((val < 0 || val > 59 || end > 59) && (val != ALL_SPEC_INT)) {
                    throw new ParseException(
                            "Minute and Second values must be between 0 and 59",
                            -1);
                }
            } else if (type == HOUR) {
                if ((val < 0 || val > 23 || end > 23) && (val != ALL_SPEC_INT)) {
                    throw new ParseException(
                            "Hour values must be between 0 and 23", -1);
                }
            } else if (type == DAY_OF_MONTH) {
                if ((val < 1 || val > 31 || end > 31) && (val != ALL_SPEC_INT)
                        && (val != NO_SPEC_INT)) {
                    throw new ParseException(
                            "Day of month values must be between 1 and 31", -1);
                }
            } else if (type == MONTH) {
                if ((val < 1 || val > 12 || end > 12) && (val != ALL_SPEC_INT)) {
                    throw new ParseException(
                            "Month values must be between 1 and 12", -1);
                }
            } else if (type == DAY_OF_WEEK) {
                if ((val == 0 || val > 7 || end > 7) && (val != ALL_SPEC_INT)
                        && (val != NO_SPEC_INT)) {
                    throw new ParseException(
                            "Day-of-Week values must be between 1 and 7", -1);
                }
            }

            if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
                if (val != -1) {
                    set.add(val);
                } else {
                    set.add(NO_SPEC);
                }

                return;
            }

            int startAt = val;
            int stopAt = end;

            if (val == ALL_SPEC_INT && incr <= 0) {
                incr = 1;
                set.add(ALL_SPEC); // put in a marker, but also fill values
            }

            if (type == SECOND || type == MINUTE) {
                if (stopAt == -1) {
                    stopAt = 59;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 0;
                }
            } else if (type == HOUR) {
                if (stopAt == -1) {
                    stopAt = 23;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 0;
                }
            } else if (type == DAY_OF_MONTH) {
                if (stopAt == -1) {
                    stopAt = 31;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 1;
                }
            } else if (type == MONTH) {
                if (stopAt == -1) {
                    stopAt = 12;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 1;
                }
            } else if (type == DAY_OF_WEEK) {
                if (stopAt == -1) {
                    stopAt = 7;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 1;
                }
            } else if (type == YEAR) {
                if (stopAt == -1) {
                    stopAt = MAX_YEAR;
                }
                if (startAt == -1 || startAt == ALL_SPEC_INT) {
                    startAt = 1970;
                }
            }

            // if the end of the range is before the start, then we need to overflow into
            // the next day, month etc. This is done by adding the maximum amount for that
            // type, and using modulus max to determine the value being added.
            int max = -1;
            if (stopAt < startAt) {
                switch (type) {
                    case       SECOND : max = 60; break;
                    case       MINUTE : max = 60; break;
                    case         HOUR : max = 24; break;
                    case        MONTH : max = 12; break;
                    case  DAY_OF_WEEK : max = 7;  break;
                    case DAY_OF_MONTH : max = 31; break;
                    case         YEAR : throw new IllegalArgumentException("Start year must be less than stop year");
                    default           : throw new IllegalArgumentException("Unexpected type encountered");
                }
                stopAt += max;
            }

            for (int i = startAt; i <= stopAt; i += incr) {
                if (max == -1) {
                    // ie: there's no max to overflow over
                    set.add(i);
                } else {
                    // take the modulus to get the real value
                    int i2 = i % max;

                    // 1-indexed ranges should not include 0, and should include their max
                    if (i2 == 0 && (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH) ) {
                        i2 = max;
                    }

                    set.add(i2);
                }
            }
        }

        private TreeSet<Integer> getSet(int type) {
            switch (type) {
                case SECOND:
                    return seconds;
                case MINUTE:
                    return minutes;
                case HOUR:
                    return hours;
                case DAY_OF_MONTH:
                    return daysOfMonth;
                case MONTH:
                    return months;
                case DAY_OF_WEEK:
                    return daysOfWeek;
                case YEAR:
                    return years;
                default:
                    return null;
            }
        }

        protected ValueSet getValue(int v, String s, int i) {
            char c = s.charAt(i);
            StringBuilder s1 = new StringBuilder(String.valueOf(v));
            while (c >= '0' && c <= '9') {
                s1.append(c);
                i++;
                if (i >= s.length()) {
                    break;
                }
                c = s.charAt(i);
            }
            ValueSet val = new ValueSet();

            val.pos = (i < s.length()) ? i : i + 1;
            val.value = Integer.parseInt(s1.toString());
            return val;
        }

        private int getNumericValue(String s, int i) {
            int endOfVal = findNextWhiteSpace(i, s);
            String val = s.substring(i, endOfVal);
            return Integer.parseInt(val);
        }

        private int getMonthNumber(String s) {
            Integer integer = monthMap.get(s);

            if (integer == null) {
                return -1;
            }

            return integer;
        }

        private int getDayOfWeekNumber(String s) {
            Integer integer = dayMap.get(s);

            if (integer == null) {
                return -1;
            }

            return integer;
        }

        private class ValueSet {
            public int value;

            int pos;
        }
    }
}
