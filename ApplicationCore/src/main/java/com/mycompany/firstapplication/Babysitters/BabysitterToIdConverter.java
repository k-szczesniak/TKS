package com.mycompany.firstapplication.Babysitters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class BabysitterToIdConverter implements Converter<Babysitter>, Serializable {

    @Inject
    private BabysittersRepository babysittersRepository;

    @Override
    public Babysitter getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return babysittersRepository.findByKey(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent,
                              Babysitter babysitter) {
        return babysitter.getUuid();
    }
}
