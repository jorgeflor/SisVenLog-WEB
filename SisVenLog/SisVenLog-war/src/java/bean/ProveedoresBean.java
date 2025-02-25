package bean;

import dao.ProveedoresFacade;
import entidad.Proveedores;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ProveedoresBean implements Serializable {

    @EJB
    private ProveedoresFacade proveedoressFacade;

    private String filtro = "";

    private Proveedores proveedoress = new Proveedores();
    private List<Proveedores> listaProveedores = new ArrayList<Proveedores>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ProveedoresBean() {

        //instanciar();
    }

    public boolean isHabBtnEdit() {
        return habBtnEdit;
    }

    public void setHabBtnEdit(boolean habBtnEdit) {
        this.habBtnEdit = habBtnEdit;
    }

    public boolean isHabBtnAct() {
        return habBtnAct;
    }

    public void setHabBtnAct(boolean habBtnAct) {
        this.habBtnAct = habBtnAct;
    }

    public boolean isHabBtnInac() {
        return habBtnInac;
    }

    public void setHabBtnInac(boolean habBtnInac) {
        this.habBtnInac = habBtnInac;
    }

    public Proveedores getProveedores() {
        return proveedoress;
    }

    public void setProveedores(Proveedores proveedoress) {
        this.proveedoress = proveedoress;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaProveedores = new ArrayList<Proveedores>();
        this.proveedoress = new Proveedores();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();
//        RequestContext.getCurrentInstance().update("formProveedores");
        PrimeFaces.current().ajax().update("formProveedores");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.proveedoress = new Proveedores();
    }

    public List<Proveedores> listar() {
        listaProveedores = proveedoressFacade.findAll();
        return listaProveedores;
    }
    
    public List<Proveedores> listarProveedoresActivos(){
        listaProveedores = proveedoressFacade.buscarProveedoresActivos();
        return listaProveedores;
    }
    
    public void insertar() {
        try {

            proveedoress.setXnombre(proveedoress.getXnombre().toUpperCase());
            proveedoress.setXruc(proveedoress.getXruc().toUpperCase());
            proveedoress.setXdirec(proveedoress.getXdirec().toUpperCase());
            proveedoress.setXcontacto(proveedoress.getXcontacto().toUpperCase());
            proveedoress.setXtelef(proveedoress.getXtelef().toUpperCase());
            proveedoress.setFalta(new Date());
            proveedoress.setCusuario("admin");

            proveedoressFacade.insertarProveedores(proveedoress);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.proveedoress.getXnombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                proveedoress.setXnombre(proveedoress.getXnombre().toUpperCase());
                proveedoressFacade.edit(proveedoress);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditProv').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            proveedoressFacade.remove(proveedoress);
            this.proveedoress = new Proveedores();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacProveedores').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.proveedoress.getXnombre()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (proveedoress != null) {

            if (proveedoress.getXnombre()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarProv').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarProv').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");

    }

}
