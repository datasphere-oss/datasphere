package com.huahui.datasphere.platform.rest.core.converter;

import java.util.Objects;

import org.unidata.mdm.core.dto.UserLibraryResult;
import org.unidata.mdm.rest.system.converter.Converter;

import com.huahui.datasphere.platform.rest.core.ro.libraries.UserLibraryRO;

/**
 * @author Mikhail Mikhailov on Feb 1, 2021
 */
public class UserLibraryConverter extends Converter<UserLibraryResult, UserLibraryRO> {
    /**
     * Constructor.
     * @param to
     * @param from
     */
    public UserLibraryConverter() {
        super(UserLibraryConverter::convert, null);
    }

    protected static UserLibraryRO convert(UserLibraryResult in) {

        if (Objects.isNull(in)) {
            return null;
        }

        UserLibraryRO result = new UserLibraryRO();
        result.setCreateDate(in.getCreateDate());
        result.setCreatedBy(in.getCreatedBy());
        result.setDescription(in.getDescription());
        result.setFilename(in.getFilename());
        result.setMimeType(in.getMimeType().getCode());
        result.setEditable(in.isEditable());
        result.setSize(in.getSize());
        result.setStorageId(in.getStorageId());
        result.setVersion(in.getVersion());

        return result;
    }
}
