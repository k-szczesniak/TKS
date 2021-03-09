package com.mycompany.firstapplication;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@RequestScoped
@Named
public class BreadcrumbController implements Serializable {
    @Inject
    private HttpServletRequest httpServletRequest;

    public List<String> getBreadCrumbs() {
        return Arrays.asList(httpServletRequest.getRequestURI().split("/"));
    }
}
