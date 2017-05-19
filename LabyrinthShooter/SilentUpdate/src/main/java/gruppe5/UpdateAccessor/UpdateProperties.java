/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.UpdateAccessor;

import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;

/**
 *
 * @author nick
 */
public class UpdateProperties {
    public static void setUpdateCenterCodeName(String codeName) {
        UpdateHandler.SILENT_UC_CODE_NAME = codeName;
    }
}
