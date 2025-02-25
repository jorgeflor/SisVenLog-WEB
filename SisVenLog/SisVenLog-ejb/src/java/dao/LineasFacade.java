/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Lineas;
import entidad.Lineas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class LineasFacade extends AbstractFacade<Lineas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineasFacade() {
        super(Lineas.class);
    }

    public List<Lineas> listarLineasOrdenadoXCategoria() {
        Query q = getEntityManager().createNativeQuery("select l.*\n"
                + "from lineas l, categorias c\n"
                + "where l.cod_CATEGORIA = c.COD_CATEGORIA\n"
                + "order by c.xdesc asc", Lineas.class);

        System.out.println(q.toString());

        List<Lineas> respuesta = new ArrayList<Lineas>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<Lineas> listarLineasClientesNoCompran() {
        Query q = getEntityManager().createNativeQuery("SELECT l.cod_linea, l.xdesc "
                + "FROM lineas l, sublineas s "
                + "WHERE l.cod_linea = s.cod_linea "
                + "AND exists (SELECT 1 "
                + "FROM mercaderias m "
                + "WHERE m.cod_sublinea = s.cod_sublinea "
                + "AND m.mestado = 'A')", Lineas.class);
        return q.getResultList();
    }

    public void insertarLineas(Lineas lineas) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarLineas");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_categoria", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("xdesc", lineas.getXdesc());
        q.setParameter("cod_categoria", lineas.getCodCATEGORIA().getCodCategoria());
        q.setParameter("falta", lineas.getFalta());
        q.setParameter("cusuario", lineas.getCusuario());

        q.execute();

    }

    public List<Lineas> listarLineasActivas() {
        Query q = getEntityManager().createNativeQuery("SELECT l.cod_linea, l.xdesc "
                + "FROM lineas l "
                + "WHERE l.mestado = 'A' "
                + "ORDER BY l.xdesc", Lineas.class);
        return q.getResultList();
    }
}
