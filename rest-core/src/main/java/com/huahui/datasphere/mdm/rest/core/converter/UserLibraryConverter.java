package com.huahui.datasphere.mdm.rest.core.converter;

import java.util.Objects;

import com.huahui.datasphere.mdm.core.dto.UserLibraryResult;
import com.huahui.datasphere.mdm.rest.system.converter.Converter;

import com.huahui.datasphere.mdm.rest.core.ro.libraries.UserLibraryRO;

/**
 * @author theseusyang on Feb 1, 2021
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
