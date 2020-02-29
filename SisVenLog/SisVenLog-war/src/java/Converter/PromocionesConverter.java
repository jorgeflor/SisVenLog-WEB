/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import entidad.Promociones;
import entidad.PromocionesPK;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author SAMACACE
 */
@Named(value = "promocionesConverter")
@ApplicationScoped

public class PromocionesConverter  implements Converter, Serializable{
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return new Promociones(new PromocionesPK(new Short("2"), Long.parseLong(value)));
            //return new Promociones(new PromocionesPK(Integer.parseInt(value), 0));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null || value.equals(""))
        {
            return "";
        }
        else
        {
            
            PromocionesPK promocionesPK = ((Promociones) value).getPromocionesPK();
            //return String.valueOf(((Integer) value));
            return (promocionesPK == null) ? "" : String.valueOf(promocionesPK.getNroPromo());
        }
    }

    
}
