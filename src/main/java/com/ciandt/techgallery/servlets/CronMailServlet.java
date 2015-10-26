package com.ciandt.techgallery.servlets;

import com.ciandt.techgallery.service.CronService;
import com.ciandt.techgallery.service.impl.CronServiceImpl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CronMailServlet extends HttpServlet {

  public static final Logger _LOG = Logger.getLogger(CronMailServlet.class.getName());
  private CronService cronService = CronServiceImpl.getInstance();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    cronService.sendEmailtoFollowers();
    _LOG.info("Executing schedule task.");
  }
}
