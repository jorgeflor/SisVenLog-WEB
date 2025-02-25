/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.ZonasFacade;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author SAMACACE
 */
@Named(value = "ZonasConverter")
@ApplicationScoped

public class ZonasConverter  implements Converter, Serializable{

    @EJB
    private ZonasFacade facade;
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return facade.find(new ZonasPK(new Short("2"), value));
            //return new Zonas(new ZonasPK(new Short("2"), value));
            //return new Zonas(new ZonasPK(Integer.parseInt(value), 0));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value){
        if (value == null || value.equals(""))
        {
            return "";
        }
        else{

            return String.valueOf(((Zonas) value).getZonasPK().getCodZona());
        }
    }

    
}
