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
package com.huahui.datasphere.mdm.rest.core.service;

import static java.util.stream.Collectors.toList;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.ADMIN_SYSTEM_MANAGEMENT;
import static com.huahui.datasphere.mdm.core.util.SecurityUtils.AUDIT_ACCESS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import com.huahui.datasphere.mdm.core.context.UpsertLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.UpsertUserEventRequestContext;
import com.huahui.datasphere.mdm.core.dto.UserEventDTO;
import com.huahui.datasphere.mdm.core.service.LargeObjectsService;
import com.huahui.datasphere.mdm.core.service.UserService;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import com.huahui.datasphere.mdm.core.type.search.AuditHeaderField;
import com.huahui.datasphere.mdm.core.type.search.AuditIndexType;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;
import com.huahui.datasphere.mdm.rest.search.converter.SearchResultToRestSearchResultConverter;
import com.huahui.datasphere.mdm.rest.search.ro.SearchResultRO;
import com.huahui.datasphere.mdm.rest.system.ro.ErrorResponse;
import com.huahui.datasphere.mdm.rest.system.ro.UpdateResponse;
import com.huahui.datasphere.mdm.rest.system.service.AbstractRestService;
import com.huahui.datasphere.mdm.search.context.NestedSearchRequestContext;
import com.huahui.datasphere.mdm.search.context.SearchRequestContext;
import com.huahui.datasphere.mdm.search.context.SearchRequestContext.SearchRequestContextBuilder;
import com.huahui.datasphere.mdm.search.dto.SearchResultDTO;
import com.huahui.datasphere.mdm.search.dto.SearchResultHitDTO;
import com.huahui.datasphere.mdm.search.service.SearchService;
import com.huahui.datasphere.mdm.search.type.form.FieldsGroup;
import com.huahui.datasphere.mdm.search.type.form.FormField;
import com.huahui.datasphere.mdm.search.type.query.SearchQuery;
import com.huahui.datasphere.mdm.search.type.sort.SortField;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.service.MessagingService;
import com.huahui.datasphere.mdm.system.type.messaging.DomainInstance;
import com.huahui.datasphere.mdm.system.util.ConvertUtils;

import com.huahui.datasphere.mdm.rest.core.converter.AuditConverter;
import com.huahui.datasphere.mdm.rest.core.exception.CoreRestExceptionIds;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditDomainsRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditSearchFieldRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditSearchRequestComplexRO;
import com.huahui.datasphere.mdm.rest.core.ro.audit.AuditSortFieldRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The Class AuditLogSearchRestService.
 *
 * @author theseusyang
 */
@Path("audit")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class AuditSearchRestService extends AbstractRestService {

    @Autowired
    private MessagingService messagingService;

    /** The search service. */
    @Autowired
    private SearchService searchService;

    /** The user service. */
    @Autowired
    private UserService userService;

    /** The data records service. */
    @Autowired
    private LargeObjectsService largeObjectsServiceComponent;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    private static final FastDateFormat DEFAULT_TIMESTAMP = FastDateFormat.getInstance("yyyy_MM_dd'T'HH_mm_ss_SSS");

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + AUDIT_ACCESS + "')")
    @Operation(
        description = "Audit domains, message types and their headers, registered in the system.",
        method = HttpMethod.GET,
        responses = {
                @ApiResponse(content = @Content(schema = @Schema(implementation = AuditDomainsRO.class)), responseCode = "200"),
                @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
    })
    public Response types() {
        Collection<DomainInstance> domains = messagingService.getDomains();
        return ok(AuditConverter.to(domains));
    }

    /**
     * Search.
     *
     * @return the response
     */
    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + AUDIT_ACCESS + "')")
    @Operation(
        description = "Поиск записей аудита по параметрам.",
        method = HttpMethod.POST,
        requestBody = @RequestBody(
                content = @Content(schema = @Schema(implementation = AuditSearchRequestComplexRO.class)),
                description = "Поисковый запрос"),
        responses = {
                @ApiResponse(content = @Content(schema = @Schema(implementation = SearchResultRO.class)), responseCode = "200"),
                @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
    })
    public Response search(final AuditSearchRequestComplexRO request) {
        SearchResultDTO result = searchAudit(request);
        return ok(SearchResultToRestSearchResultConverter.convert(result));
    }

    /**
     * Export.
     *
     * @param request
     *            the request
     * @return the response
     */
    @POST
    @Path("/export")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isAdminUser()"
            + " or"
            + " T(com.huahui.datasphere.mdm.core.util.security.SecurityUtils).isReadRightsForResource('"
            + ADMIN_SYSTEM_MANAGEMENT + "," + AUDIT_ACCESS + "')")
    @Operation(
        description = "кспортировать записи аудита по параметрам.",
        method = HttpMethod.POST,
        requestBody = @RequestBody(
                content = @Content(schema = @Schema(implementation = AuditSearchRequestComplexRO.class)),
                description = "Поисковый запрос"),
        responses = {
                @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateResponse.class)), responseCode = "200"),
                @ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = "500")
    })
    public Response export(final AuditSearchRequestComplexRO request) {
        final String storageId = "default";
        String userName = SecurityUtils.getCurrentUserName();
        executor.submit(() -> {
            SearchResultDTO result = searchAudit(request);
            export(result, userName);
        });
        return ok(new UpdateResponse(true, storageId));
    }

    /**
     * Search audit.
     *
     * @param request
     *            the request
     * @return the search result DTO
     */
    private SearchResultDTO searchAudit(AuditSearchRequestComplexRO request) {

        List<AuditHeaderField> topLevelFields = Arrays.stream(AuditHeaderField.values())
                .filter(f -> f != AuditHeaderField.PARAMETERS && f != AuditHeaderField.PARAMETER_KEY && f != AuditHeaderField.PARAMETER_VALUE)
                .collect(toList());

        SearchRequestContextBuilder searchContextBuilder = SearchRequestContext.builder(AuditIndexType.AUDIT, AuditIndexType.INDEX_NAME)
                .page(request.getPage() > 0 ? request.getPage() - 1 : request.getPage())
                .count(request.getCount())
                .returnFields(Arrays.stream(AuditHeaderField.values()).map(AuditHeaderField::getPath).collect(toList()))
                .returnAsValues(AuditHeaderField.PARAMETERS.getPath());

        FieldsGroup formGroup = FieldsGroup.and();
        FieldsGroup nestedGroup = FieldsGroup.and();

        if (request.getFrom() != null || request.getTo() != null) {
            formGroup.add(FormField.range(AuditHeaderField.WHEN_HAPPENED,
                    ConvertUtils.localDateTime2Date(request.getFrom()),
                    ConvertUtils.localDateTime2Date(request.getTo())));
        }

        // Normal header fields
        if (CollectionUtils.isNotEmpty(request.getSearchFields())) {

            for (AuditSearchFieldRO searchField : request.getSearchFields()) {

                Optional<AuditHeaderField> auditHeaderField = topLevelFields.stream()
                        .filter(i -> StringUtils.equalsIgnoreCase(i.getName(), searchField.getFieldName()))
                        .findFirst();

                auditHeaderField.ifPresent(field -> formGroup.add(FormField.exact(field, searchField.getValues())));
            }
        }

        // Parameters
        if (CollectionUtils.isNotEmpty(request.getParameters())) {
            request.getParameters().forEach(f -> nestedGroup.add(
                    FieldsGroup.and(
                            FormField.exact(AuditHeaderField.PARAMETER_KEY, f.getFieldName()),
                            FormField.exact(AuditHeaderField.PARAMETER_VALUE, f.getValues()))));
        }

        searchContextBuilder
            .fetchAll(formGroup.isEmpty() && nestedGroup.isEmpty())
            .query(formGroup.isEmpty() ? null : SearchQuery.formQuery(formGroup));

        if (!nestedGroup.isEmpty()) {

            searchContextBuilder.nestedSearch(
                    NestedSearchRequestContext.objects(
                        SearchRequestContext.builder(AuditIndexType.AUDIT, AuditIndexType.INDEX_NAME)
                            .nestedPath(AuditHeaderField.PARAMETERS.getPath())
                            .query(SearchQuery.formQuery(nestedGroup))
                            .count(1000)
                            .build())
                        .nestedQueryName(AuditHeaderField.PARAMETERS.getPath())
                        .build());
        }

        if (CollectionUtils.isNotEmpty(request.getSortFields())) {

            List<SortField> sortFields = new ArrayList<>();
            for (AuditSortFieldRO auditSortField : request.getSortFields()) {

                Optional<AuditHeaderField> auditHeaderField = topLevelFields.stream()
                        .filter(i -> StringUtils.equalsIgnoreCase(i.getName(), auditSortField.getFieldName()))
                        .findFirst();

                auditHeaderField.ifPresent(ahf ->
                    sortFields.add(SortField.of(ahf, SortField.SortOrder.valueOf(auditSortField.getOrder()))));
            }

            searchContextBuilder.sorting(sortFields);
        }

        SearchResultDTO result = searchService.search(searchContextBuilder.build());

        // Remove inner hits from preview
        for (SearchResultHitDTO hit : result.getHits()) {
            hit.getPreview().remove(AuditHeaderField.PARAMETER_KEY.getPath());
            hit.getPreview().remove(AuditHeaderField.PARAMETER_VALUE.getPath());
        }

        // Remove nested filds since UI doesn't need them.
        result.getFields().removeAll(List.of(AuditHeaderField.PARAMETER_KEY.getPath(), AuditHeaderField.PARAMETER_VALUE.getPath()));

        return result;
    }

    /**
     * Export.
     *
     * @param result
     *            the result
     */
    private void export(SearchResultDTO result, String user) {

        try (ByteArrayOutputStream output = new ByteArrayOutputStream(); Workbook wb = createTemplateWorkbook(result)) {
            fillTemplateWbWithData(wb, result);
            wb.write(output);
            InputStream is = new ByteArrayInputStream(output.toByteArray());
            UpsertUserEventRequestContext uueCtx = UpsertUserEventRequestContext.builder()
                    .login(user).type("AUDIT_EXPORT").content("Экспорт журнала аудита.")
                    .build();
            UserEventDTO userEventDTO = userService.upsert(uueCtx);
            // save result and attach it to the early created user event
            UpsertLargeObjectContext slorCtx = UpsertLargeObjectContext.builder()
                    .subjectId(userEventDTO.getId())
                    .mimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").binary(true)
                    .input(is)
                    .filename("audit_" + DEFAULT_TIMESTAMP.format(new Date()) + ".xlsx")
                    .acceptance(LargeObjectAcceptance.ACCEPTED)
                    .build();

            largeObjectsServiceComponent.saveLargeObject(slorCtx);

        } catch (IOException e) {
            throw new PlatformFailureException("Unable to export data for {} to XLS.",
                    CoreRestExceptionIds.EX_CORE_AUDIT_EXPORT_UNABLE_TO_EXPORT_XLS);
        }
        result.getFields();
    }

    private String toParameterDetails(List<Object> values) {

        if (Objects.nonNull(values)
         && CollectionUtils.isNotEmpty(values)) {

            StringBuilder b = new StringBuilder();

            for (Object payload : values) {

                @SuppressWarnings("unchecked")
                Map<String, Object> entries = (Map<String, Object>) payload;
                if (MapUtils.isEmpty(entries)) {
                    continue;
                }

                String keyString = (String) entries.get("key");
                String valueString = (String) entries.get("value");

                if (StringUtils.isNotBlank(keyString)) {

                    b.append(keyString)
                     .append(" =>> ")
                     .append(StringUtils.isBlank(valueString) ? StringUtils.EMPTY : valueString)
                     .append(StringUtils.CR)
                     .append(StringUtils.LF);
                }
            }

            return b.toString();
        }

        return StringUtils.EMPTY;
    }

    /**
     * Fill template wb with data.
     *
     * @param wb
     *            the wb
     * @param result
     *            the result
     */
    private void fillTemplateWbWithData(Workbook wb, SearchResultDTO result) {

        SXSSFSheet sheet = (SXSSFSheet) wb.getSheet("AUDIT");
        Map<String, Integer> indexes = new HashMap<>();
        for (int i = 0; i < result.getFields().size(); i++) {
            indexes.put(result.getFields().get(i), i);
        }

        for (int i = 0; i < result.getHits().size(); i++) {

            SXSSFRow row = sheet.createRow(i + 1);
            result.getHits().get(i).getPreview().forEach((k, v) -> {

                if (indexes.containsKey(k)) {

                    String val;
                    if (AuditHeaderField.PARAMETERS.getName().equals(k)) {
                        val = toParameterDetails(v.getValues());
                    } else {
                        val = (CollectionUtils.isNotEmpty(v.getValues()) ? v.getFirstValue().toString() : StringUtils.EMPTY);
                    }

                    SXSSFCell cell = row.createCell(indexes.get(k));
                    cell.setCellValue(cell.getStringCellValue() + " " + val);
                }
            });
        }
    }

    /**
     * Creates the template workbook.
     *
     * @param result
     *            the result
     * @return the workbook
     */
    private Workbook createTemplateWorkbook(SearchResultDTO result) {
        // create new workbook
        final Workbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("AUDIT");
        // create cell style
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        Row sysRow = sheet.createRow(0);
        for (int i = 0; i < result.getFields().size(); i++) {
            Cell sysCell = sysRow.createCell(i);
            sysCell.setCellStyle(cellStyle);
            sysCell.setCellValue(result.getFields().get(i));
        }
        // block editing for headers
        sheet.createFreezePane(0, 1);
        // set headers as repeating rows
        sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));
        return wb;
    }

}
