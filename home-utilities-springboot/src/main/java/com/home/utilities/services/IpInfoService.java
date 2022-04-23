package com.home.utilities.services;

import com.home.utilities.entities.IpInfo;

import javax.servlet.http.HttpServletRequest;

public interface IpInfoService {

    String getIpAddress(HttpServletRequest request);

    IpInfo createIpInfo(HttpServletRequest request);
}
