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
package com.huahui.datasphere.platform.rest.core.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.unidata.mdm.core.context.UserLibraryGetContext;
import org.unidata.mdm.core.context.UserLibraryQueryContext;
import org.unidata.mdm.core.context.UserLibraryRemoveContext;
import org.unidata.mdm.core.context.UserLibraryUpsertContext;
import org.unidata.mdm.core.dto.UserLibraryResult;
import org.unidata.mdm.core.service.UserLibraryService;
import org.unidata.mdm.core.type.libraries.LibraryMimeType;
import org.unidata.mdm.rest.system.service.AbstractRestService;
import org.unidata.mdm.rest.system.util.RestConstants;
import org.unidata.mdm.system.exception.PlatformBusinessException;

import com.huahui.datasphere.platform.rest.core.converter.UserLibraryConverter;
import com.huahui.datasphere.platform.rest.core.exception.CoreRestExceptionIds;
import com.huahui.datasphere.platform.rest.core.ro.libraries.DeleteLibraryPO;
import com.huahui.datasphere.platform.rest.core.ro.libraries.GetLibrariesRO;
import com.huahui.datasphere.platform.rest.core.ro.libraries.UploadLibraryRO;
import com.huahui.datasphere.rest.system.ro.DetailedErrorResponseRO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author Mikhail Mikhailov on Feb 1, 2021
 * UL REST interface.
 */
@Path("/libraries")
@Consumes({"application/json"})
@Produces({"application/json"})
public class UserLibrariesRestService extends AbstractRestService {
    /**
     * ULS.
     */
    @Autowired
    private UserLibraryService userLibraryService;
    /**
     * Converter.
     */
    private UserLibraryConverter converter = new UserLibraryConverter();

    /**
     * Constructor.
     */
    public UserLibrariesRestService() {
        super();
    }
    /**
     * Gets libraries list.
     * @param filename the filename
     * @param mimeType the MIME type
     * @param withData fetch data
     * @param latest get latest
     * @return collection of records
     */
    @GET
    @Operation(
        description = "Return libraries list.",
        method = HttpMethod.GET,
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetLibrariesRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "400"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "500")
        }
    )
    public Response list(
            @Parameter(description = "Library name.") @QueryParam("filename") String filename,
            @Parameter(description = "MIME type.") @QueryParam("mimeType") String mimeType,
            @Parameter(description = "Fetch data.") @QueryParam("withData") @DefaultValue("false") boolean withData,
            @Parameter(description = "Return latest (by create date).") @QueryParam("latest") @DefaultValue("false") boolean latest) {

        List<UserLibraryResult> hits = userLibraryService.query(UserLibraryQueryContext.builder()
                .filename(filename)
                .latest(latest)
                .mimeType(LibraryMimeType.fromCode(mimeType))
                .withData(withData)
                .build());

        return ok(new GetLibrariesRO(converter.to(hits)));
    }

    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(
        description = "Uploads a user library.",
        method = HttpMethod.POST,
        parameters = { },
        requestBody =
            @RequestBody(content =
                @Content(
                        mediaType = MediaType.MULTIPART_FORM_DATA,
                        schema = @Schema(implementation = LibraryUploadDescriptor.class),
                        encoding = {@Encoding(name = "content", contentType = "application/x-java-archive, text/x-python, application/x-python-code, text/x-groovy")}),
                description = "Attachment content."),
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UploadLibraryRO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "401"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "500")
        }
    )
    public Response upload(
            @Multipart("filename") String fileName,
            @Multipart("version") String version,
            @Multipart("description") String description,
            @Multipart("content") Attachment attachment) {

        MediaType mediaType = attachment.getContentType();

        userLibraryService.upsert(UserLibraryUpsertContext.builder()
            .filename(fileName)
            .mimeType(mediaType.getType() + "/" + mediaType.getSubtype())
            .editable(true)
            .input(attachment.getObject(InputStream.class))
            .description(description)
            .version(version)
            .build());

        return ok(new UploadLibraryRO(true));
    }

    /**
     * Gets library data associated with optional storage ID, name and version.
     *
     * @return byte stream
     * @throws Exception the exception
     */
    @GET
    @Path("/download")
    @Operation(
        description = "Gets the data of a libary.",
        method = HttpMethod.GET,
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(type = "string", format = "binary")), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "500")
        }
    )
    public Response download(
            @Parameter(description = "Library name.") @QueryParam("filename") String name,
            @Parameter(description = "Library version.") @QueryParam("version") String version)
        throws UnsupportedEncodingException {

        UserLibraryResult result = userLibraryService.get(UserLibraryGetContext.builder()
            .filename(name)
            .version(version)
            .withData(true)
            .build());

        if (Objects.isNull(result) || ArrayUtils.isEmpty(result.getPayload())) {
            throw new PlatformBusinessException("Empty library response.",
                    CoreRestExceptionIds.EX_CORE_LIBRARIES_UNABLE_TO_EXPORT_LIBRARY);
        }

        final String encodedFilename = URLEncoder.encode(result.getFilename(),
                StandardCharsets.UTF_8.name());

        final StreamingOutput retval = output -> {
            try {
                output.write(result.getPayload());
            } catch (Exception e) {
                throw new PlatformBusinessException("Sending of library content failed.", e,
                        CoreRestExceptionIds.EX_CORE_LIBRARIES_FAILED_TO_SEND_LIBRARY);
            }
        };

        return Response.ok(retval)
                .encoding(StandardCharsets.UTF_8.name())
                .header("Content-Disposition", "attachment; filename=" + result.getFilename() + "; filename*=UTF-8''" + encodedFilename)
                .header("Content-Type", MediaType.valueOf(result.getMimeType().getCode()))
                .build();
    }

    /**
     * Deletes an entity.
     * @param filename entity id
     * @return empty string
     * @throws Exception
     */
    @DELETE
    @Path("{" + RestConstants.PATH_PARAM_FILENAME + "}")
    @Operation(
        description = "Removes a library record.",
        method = HttpMethod.DELETE,
        parameters = {
                @Parameter(name = RestConstants.PATH_PARAM_FILENAME, description = "Library filename.", in = ParameterIn.PATH),
                @Parameter(name = RestConstants.PATH_PARAM_VERSION, description = "Library version. Optional. Deletes all records with the same name, if not given.", in = ParameterIn.QUERY)
        },
        responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteLibraryPO.class)), responseCode = "200"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = DetailedErrorResponseRO.class)), responseCode = "500")
        }
    )
    public Response delete(
             @PathParam(RestConstants.PATH_PARAM_FILENAME) String filename,
             @QueryParam(RestConstants.PATH_PARAM_VERSION) String version) {

        userLibraryService.remove(UserLibraryRemoveContext.builder()
            .filename(filename)
            .version(StringUtils.trimToNull(version))
            .build());

        return ok(new DeleteLibraryPO(true));
    }

    /**
     * @author Mikhail Mikhailov on Feb 2, 2021
     * Not really a functional class, but a OpenAPI helper.
     */
    static class LibraryUploadDescriptor {

        @Schema(name = "filename", description = "Name of the library file and part of ID.", type = "string")
        public String filename;

        @Schema(name = "version", description = "Version of the library file and part of ID.", type = "string")
        public String version;

        @Schema(name = "description", description = "An optional description.", type = "string")
        public String description;

        @Schema(name = "content", description = "Upload content.", type = "string", format = "binary")
        public String content;
    }
}
