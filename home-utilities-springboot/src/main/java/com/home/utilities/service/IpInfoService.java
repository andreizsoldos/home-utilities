package com.home.utilities.service;

import com.home.utilities.entity.IpInfo;

import javax.servlet.http.HttpServletRequest;

public interface IpInfoService {

    String getIpAddress(HttpServletRequest request);

    IpInfo createIpInfo(HttpServletRequest request);
}
