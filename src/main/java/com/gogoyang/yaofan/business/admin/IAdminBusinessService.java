package com.gogoyang.yaofan.business.admin;

import java.util.Map;

public interface IAdminBusinessService {
    void createAdmin(Map in) throws Exception;

    Map loginAdmin(Map in) throws Exception;

    Map listRegisterUser(Map in) throws Exception;

    Map getUser(Map in) throws Exception;

    Map listUserAction(Map in) throws Exception;

    Map totalUserActionType(Map in) throws Exception;

    Map getUserAction(Map in) throws Exception;
}
