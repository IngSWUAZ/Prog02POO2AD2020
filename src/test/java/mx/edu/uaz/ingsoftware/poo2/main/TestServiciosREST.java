package mx.edu.uaz.ingsoftware.poo2.main;

import junit.framework.TestCase;
import mx.edu.uaz.ingsoftware.poo2.clientes.basedatos.DaoConcursosREST;
import mx.edu.uaz.ingsoftware.poo2.entidades.Entidad;
import mx.edu.uaz.ingsoftware.poo2.entidades.Institucion;
import mx.edu.uaz.ingsoftware.poo2.entidades.Municipio;
import mx.edu.uaz.ingsoftware.poo2.entidades.Persona;
import mx.edu.uaz.ingsoftware.poo2.interfaces.DAOConcursos;
import mx.edu.uaz.ingsoftware.poo2.utils.HttpUtils;
import org.dbunit.*;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServiciosREST extends TestCase {
    private static IDatabaseTester databaseTester;
    private final static String driverName="com.mysql.cj.jdbc.Driver";
    private static List<Institucion> datosEsperados;
    private static IDatabaseConnection conndbunit;
    private final static int CALIF_OBTENER=5;
    private final static int CALIF_AGREGAR=5;
    private final static int CALIF_UPDATE=5;
    private final static int CALIF_DELETE=5;
    private final static int MAX_CALIF_INST=50;
    private final static int MAX_CALIF_ENTIDAD=5;
    private final static int MAX_CALIF_MUNICIPIO=5;
    private final static int MAX_CALIF_PERSONA=50;
    private final static int MAX_CALIF_DATOSESTUDIANTE=40;
    private final static int MAX_CALIF_SEDE=35;
    private final static int MAX_CALIF_CONCURSO=35;
    private final static int MAX_CALIF_EQUIPO=35;
    private final static int MAX_CALIF_SEDECONCURSO=20;
    private final static int MAX_CALIF_EQUIPOSEDECONCURSO=20;
    private final static double PORCENTAJE_INST=0.25;
    private final static double PORCENTAJE_ENTIDAD=0.25;
    private final static double PORCENTAJE_PERSONA=0.5;
    private final static double PORCENTAJE_DATOSESTUDIANTE=0.5;
    private final static double PORCENTAJE_SEDE=0.5;
    private final static double PORCENTAJE_CONCURSO=0.5;
    private final static double PORCENTAJE_EQUIPO=0.5;
    private final static double PORCENTAJE_SEDECONCURSO=0.5;
    private final static double PORCENTAJE_EQUIPOSEDECONCURSO=0.5;
    private final static double PORCENTAJE_MUNICIPIO=2.0;
    private final static double PORCENTAJE_EMAILS=2.0;
    private final static double PORCENTAJE_SEDESDISP=5.0;
    private final static double PORCENTAJE_SEDESASIG=5.0;
    private final static double PORCENTAJE_EQUIPOSDISP=7.0;
    private final static double PORCENTAJE_EQUIPOSREG=5.0;
    private final static double MAX_CALIF_METODOS_ESP=5;

    private static int calif_inst;
    private static int calif_persona;
    private static int calif_datos_estudiante;
    private static int calif_municipio;
    private static int calif_entidad;
    private static int calif_sede;
    private static int calif_equipo;
    private static int calif_concurso;
    private static int calif_sede_concurso;
    private static int calif_equipos_sede_concurso;
    private static int calif_emails;
    private static int calif_sedesdisp;
    private static int calif_sedesasig;
    private static int calif_equiposdisp;
    private static int calif_equiposreg;

    private static String urlbaseGF;
    private static String matricula;
    private static String nombreCompleto;
    private static String ubicacionGF;
    private static String puertoGF;
    private static DAOConcursos dao;

    public static class CustomConfigurationOperationListener extends DefaultOperationListener implements IOperationListener {
        @Override
        public void connectionRetrieved(IDatabaseConnection iDatabaseConnection) {
            super.connectionRetrieved(iDatabaseConnection);
            iDatabaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        }
    }

    @BeforeAll
    public static void inicializa() throws Exception {
        ResourceBundle res=ResourceBundle.getBundle("datosmysql", Locale.ROOT);
        matricula=res.getString("matricula");
        nombreCompleto=res.getString("nombre");

        String usuario=res.getString("usuario");
        String clave= res.getString("clave");
        String basedatos=res.getString("basedatos");

        String tipo_prueba=System.getProperty("tipo_prueba","local");
        if (tipo_prueba==null || tipo_prueba.isEmpty()) {
            tipo_prueba="local";
        }
        System.out.println("Tipo_prueba: '"+tipo_prueba+"'");
        String ubicacionmysl=res.getString("ubicacionmysql"+"_"+tipo_prueba);
        String puertomysql=res.getString("puertomysql"+"_"+tipo_prueba);

        ubicacionGF=res.getString("ubicacionglassfish"+"_"+tipo_prueba) ;
        puertoGF=res.getString("puertoglassfish"+"_"+tipo_prueba);

        urlbaseGF= String.format("http://%s:%s/RESTConcursos-%s",
                ubicacionGF,puertoGF,matricula);
        String url=String.format("jdbc:mysql://%s:%s/%s",
                ubicacionmysl,puertomysql,basedatos);
        //System.out.println("URL MySQL:"+url);

        String pagina= HttpUtils.httpGet(urlbaseGF, null);
        int posInicioTitulo=pagina.indexOf("<title>")+7;
        int posFinTitulo=pagina.indexOf("</title>");
        int posInicioH1=pagina.indexOf("<h1>")+4;
        int posFinH1=pagina.indexOf("</h1>");
        int posInicioH2=pagina.indexOf("<h2>")+4;
        int posFinH2=pagina.indexOf("</h2>");
        String textoTitulo=pagina.substring(posInicioTitulo, posFinTitulo).toUpperCase();
        String textoH1=pagina.substring(posInicioH1, posFinH1).toUpperCase();
        String textoH2=pagina.substring(posInicioH2, posFinH2).toUpperCase();
        String tituloEsperado=String.format("Servicios REST de %s",nombreCompleto).toUpperCase();
        String h2Esperado=String.format("Matricula %s",matricula).toUpperCase();

        assertEquals(tituloEsperado,textoTitulo);
        assertEquals(tituloEsperado,textoH1);
        assertEquals(h2Esperado,textoH2);

        dao=new DaoConcursosREST(ubicacionGF,Integer.parseInt(puertoGF),matricula);

        databaseTester=new JdbcDatabaseTester(driverName,url,
                usuario,clave);
        databaseTester.setOperationListener(new CustomConfigurationOperationListener());
        conndbunit=databaseTester.getConnection();
        DatabaseConfig config=conndbunit.getConfig();
        config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS,true);
        IDataSet dataSet=new FlatXmlDataSetBuilder().build(new FileInputStream("concursosv3.xml"));
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.onSetup();
    }

    @AfterAll
    public static void termina() throws Exception {
        databaseTester.setTearDownOperation(DatabaseOperation.REFRESH);
        databaseTester.onTearDown();
        if (calif_inst>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Institucion): %.2f/%.2f\n", calif_inst * PORCENTAJE_INST / MAX_CALIF_INST, PORCENTAJE_INST);
        }
        if (calif_entidad>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Entidad): %.2f/%.2f\n", calif_entidad * PORCENTAJE_INST / MAX_CALIF_ENTIDAD, PORCENTAJE_ENTIDAD);
        }
        if (calif_persona>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Persona): %.2f/%.2f\n", calif_persona * PORCENTAJE_PERSONA / MAX_CALIF_PERSONA, PORCENTAJE_PERSONA);
        }
        if (calif_datos_estudiante>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (DatosEstudiante): %.2f/%.2f\n", calif_datos_estudiante * PORCENTAJE_DATOSESTUDIANTE / MAX_CALIF_DATOSESTUDIANTE, PORCENTAJE_DATOSESTUDIANTE);
        }
        if (calif_sede>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Sede): %.2f/%.2f\n", calif_sede * PORCENTAJE_SEDE / MAX_CALIF_SEDE, PORCENTAJE_SEDE);
        }
        if (calif_concurso>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Concurso): %.2f/%.2f\n", calif_concurso * PORCENTAJE_CONCURSO / MAX_CALIF_CONCURSO, PORCENTAJE_CONCURSO);
        }
        if (calif_equipo>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (Equipo): %.2f/%.2f\n", calif_equipo * PORCENTAJE_EQUIPO / MAX_CALIF_EQUIPO, PORCENTAJE_EQUIPO);
        }
        if (calif_sede_concurso>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (SedeConcurso): %.2f/%.2f\n", calif_sede_concurso * PORCENTAJE_SEDECONCURSO / MAX_CALIF_SEDECONCURSO, PORCENTAJE_SEDECONCURSO);
        }
        if (calif_equipos_sede_concurso>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (EquiposSedeConcurso): %.2f/%.2f\n", calif_equipos_sede_concurso * PORCENTAJE_EQUIPOSEDECONCURSO / MAX_CALIF_EQUIPOSEDECONCURSO, PORCENTAJE_EQUIPOSEDECONCURSO);
        }
        if (calif_municipio>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenMunicipios): %.2f/%.2f\n", calif_municipio * PORCENTAJE_MUNICIPIO / MAX_CALIF_MUNICIPIO, PORCENTAJE_MUNICIPIO);
        }
        if (calif_emails>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenCorreosDeInstitucion): %.2f/%.2f\n", calif_emails * PORCENTAJE_EMAILS / MAX_CALIF_METODOS_ESP, PORCENTAJE_EMAILS);
        }
        if (calif_sedesdisp>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenSedesDisponibles): %.2f/%.2f\n", calif_sedesdisp * PORCENTAJE_SEDESDISP / MAX_CALIF_METODOS_ESP, PORCENTAJE_SEDESDISP);
        }
        if (calif_sedesasig>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenSedesAsignadas): %.2f/%.2f\n", calif_sedesasig * PORCENTAJE_SEDESASIG / MAX_CALIF_METODOS_ESP, PORCENTAJE_SEDESASIG);
        }
        if (calif_equiposdisp>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenEquiposDisponibles): %.2f/%.2f\n", calif_equiposdisp * PORCENTAJE_EQUIPOSDISP / MAX_CALIF_METODOS_ESP, PORCENTAJE_EQUIPOSDISP);
        }
        if (calif_equiposreg>0) {
            System.out.printf("Puntos para Pruebas ServiciosREST (obtenEquiposRegistrados): %.2f/%.2f\n", calif_equiposreg * PORCENTAJE_EQUIPOSREG / MAX_CALIF_METODOS_ESP, PORCENTAJE_EQUIPOSREG);
        }
    }

    /// INSTITUCION

    private void comparaInst(Institucion actual, ITable expected, int numrow) {
        try {
            assertEquals(String.valueOf(actual.getIdInstitucion()), expected.getValue(numrow, "id_institucion").toString());
            assertEquals(actual.getNombreInstitucion(), expected.getValue(numrow, "nombre_institucion"));
            assertEquals(actual.getNombreCortoInstitucion(), expected.getValue(numrow, "nombre_corto_institucion"));
            assertEquals(actual.getUrlInstitucion(), expected.getValue(numrow, "url_institucion"));
            assertEquals(actual.getCalleNumInstitucion(), expected.getValue(numrow, "calle_num_institucion"));
            assertEquals(actual.getColoniaInstitucion(), expected.getValue(numrow, "colonia_institucion"));
            assertEquals(String.valueOf(actual.getIdMunicipioInstitucion()), expected.getValue(numrow, "id_municipio_institucion").toString());
            assertEquals(String.valueOf(actual.getIdEntidadInstitucion()), expected.getValue(numrow, "id_entidad_institucion").toString());
            assertEquals(actual.getCodpostalInstitucion(), expected.getValue(numrow, "codpostal_institucion"));
            assertEquals(actual.getTelefonoInstitucion(), expected.getValue(numrow, "telefono_institucion"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion comparar las instituciones",ex);
        }
    }

    @Test
    @Order(1)
    public void testInstObtenTodas() throws Exception {
        List<Institucion> actual = dao.obtenInstituciones();

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("concursosv3.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaInst(actual.get(i), expectedTable,i);
        }
        calif_inst += CALIF_OBTENER;
    }

    @Test
    @Order(2)
    public void testInstAgregarValida() throws Exception {
        long id=5;
        String nom="Universidad Tecnologica Estado de Zacatecas";
        String nomcorto="UTZAC";
        String url="http://www.utzac.edu.mx";
        String calle="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        String colonia="Ejido Cieneguitas";
        long idmun=32017;
        long ident=32;
        String codpostal="98601";
        String tel="4929276180";
        Institucion inst= new Institucion(id,nom,nomcorto,url,
                calle,idmun,ident);
        inst.setColoniaInstitucion(colonia);
        inst.setCodpostalInstitucion(codpostal);
        inst.setTelefonoInstitucion(tel);
        boolean resultado=false;
        try {
            resultado = dao.agregaInstitucion(inst);
        }
        catch (Exception ex) {
            resultado=false;
            ex.printStackTrace();
            assertNull("No deberia generarse excepcion al llamar a agregaInstitucion",ex);
        }
        assertTrue(resultado);

        ITable actualTable=conndbunit.createQueryTable("institucion",
                "SELECT * FROM institucion WHERE id_institucion>4");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("inst_add.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");

        Assertion.assertEquals(expectedTable,actualTable);
        calif_inst += CALIF_AGREGAR;
    }

    @Test
    @Order(3)
    public void testInstAgregarDuplicada() throws Exception {
        long id=5;
        String nom="Universidad Tecnologica Estado de Zacatecas";
        String nomcorto="UTZAC";
        String url="http://www.utzac.edu.mx";
        String calle="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        String colonia="Ejido Cieneguitas";
        long idmun=32017;
        long ident=32;
        String codpostal="98601";
        String tel="4929276180";

        Institucion inst= new Institucion(id,nom,nomcorto,url,
                calle,idmun,ident);
        inst.setColoniaInstitucion(colonia);
        inst.setCodpostalInstitucion(codpostal);
        inst.setTelefonoInstitucion(tel);

        boolean resultado;
        try {
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generarse excepcion al llamar a agregaInstitucion",ex);
        }

        calif_inst += CALIF_AGREGAR;
    }

    private void validaNullo_Vacio(String valor, long id, boolean nuevo) {
        String nom="Universidad Tecnologica Estado de Zacatecas";
        String nomcorto="UTZAC";
        String url="http://www.utzac.edu.mx";
        String calle="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        String colonia="Ejido Cieneguitas";
        long idmun=32017;
        long ident=32;
        String codpostal="98601";
        String tel="4929276180";
        boolean resultado;
        String valprev;

        Institucion inst= new Institucion(id,valor,nomcorto,url,
                calle,idmun,ident);
        inst.setColoniaInstitucion(colonia);
        inst.setCodpostalInstitucion(codpostal);
        inst.setTelefonoInstitucion(tel);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertFalse(resultado);

        inst.setNombreInstitucion(nom);
        valprev=inst.getNombreCortoInstitucion();
        inst.setNombreCortoInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertFalse(resultado);

        inst.setNombreCortoInstitucion(valprev);
        valprev=inst.getUrlInstitucion();
        inst.setUrlInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertFalse(resultado);

        inst.setUrlInstitucion(valprev);
        valprev=inst.getCalleNumInstitucion();
        inst.setCalleNumInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertFalse(resultado);

        inst.setCalleNumInstitucion(valprev);
        valprev=inst.getColoniaInstitucion();
        inst.setColoniaInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertTrue(resultado);

        Connection conn;
        Statement stmt=null;
        String sql=String.format("DELETE FROM institucion WHERE id_institucion=%d",id);
        try {
            if (nuevo) {
                conn = conndbunit.getConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        }
        catch (SQLException exsql) {
            assertNull("Problema al eliminar la institucion que se agrego en la prueba",exsql);
        }

        inst.setColoniaInstitucion(valprev);
        valprev=inst.getCodpostalInstitucion();
        inst.setCodpostalInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertTrue(resultado);

        try {
            if (nuevo) {
                stmt.executeUpdate(sql);
            }
        }
        catch (SQLException exsql) {
            exsql.printStackTrace();
            assertNull("Problema al eliminar la institucion que se agrego en la prueba",exsql);
        }

        inst.setCodpostalInstitucion(valprev);
        inst.setTelefonoInstitucion(valor);
        resultado=nuevo?dao.agregaInstitucion(inst):dao.actualizaInstitucion(inst);
        assertTrue(resultado);

        try {
            if (nuevo) {
                stmt.executeUpdate(sql);
            }
        }
        catch (SQLException exsql) {
            exsql.printStackTrace();
            assertNull("Problema al eliminar la institucion que se agrego en la prueba",exsql);
        }
    }

    @Test
    @Order(4)
    public void testInstAgregarInvalida() throws Exception {
        long id=6;
        String[] nom={"Universidad","Universidad Con un Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String[] nomcorto={"UEZAM","UniversidadTecnologicadelEstadodeZacatecas"};
        String url1="Este URL es extremadamente largo y por tanto no deberia de pasar ";
        url1 += url1; url1 += url1; url1 += url1;
        String[] url ={"http://es.com",url1};
        String calle1="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        calle1 += calle1;
        calle1 += calle1;
        String[] calle={"Calle Tolosa 25",calle1};
        String colonia1="Ejido Cieneguitas";
        colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1;
        String[] colonia={"Centro",colonia1};
        long[] idmun={32056,32100};
        long[] ident={32,35};
        String[] codpostal={"98000","198601"};
        String[] tel={"4921234567","49292762180"};

        boolean resultado;
        Institucion inst= new Institucion(id,nom[1],nomcorto[0],url[0],
                calle[0],idmun[0],ident[0]);
        inst.setColoniaInstitucion(colonia[0]);
        inst.setCodpostalInstitucion(codpostal[0]);
        inst.setTelefonoInstitucion(tel[0]);

        try {
            //resultado=dao.agregaInstitucion(null);
            //assertFalse(resultado);

            validaNullo_Vacio(null,100,true);
            validaNullo_Vacio("",100,true);

            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setNombreInstitucion(nom[0]);
            inst.setNombreCortoInstitucion(nomcorto[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setNombreCortoInstitucion(nomcorto[0]);
            inst.setCalleNumInstitucion(calle[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setCalleNumInstitucion(calle[0]);
            inst.setColoniaInstitucion(colonia[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setColoniaInstitucion(colonia[0]);
            inst.setIdMunicipioInstitucion(idmun[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setIdMunicipioInstitucion(idmun[0]);
            inst.setIdEntidadInstitucion(ident[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setIdEntidadInstitucion(ident[0]);
            inst.setCodpostalInstitucion(codpostal[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);

            inst.setCodpostalInstitucion(codpostal[0]);
            inst.setTelefonoInstitucion(tel[1]);
            resultado=dao.agregaInstitucion(inst);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            resultado=true;
        }
        assertFalse(resultado);
        calif_inst += CALIF_AGREGAR;
    }

    @Test
    @Order(5)
    public void testInstActualizarValida() throws Exception {
        long id=5;
        String nom="Universidad Tecnologica de Aguascalientes";
        String nomcorto="UTAGS";
        String url="http://www.utags.edu.mx";
        String calle="Blvd. Juan Pablo II 1302";
        String colonia="Canteras de San Agustin";
        long idmun=1001;
        long ident=1;
        String codpostal="20200";
        String tel="4499105000";
        Institucion inst= new Institucion(id,nom,nomcorto,url,
                calle,idmun,ident);
        inst.setColoniaInstitucion(colonia);
        inst.setCodpostalInstitucion(codpostal);
        inst.setTelefonoInstitucion(tel);

        boolean resultado=false;
        try {
            resultado=dao.actualizaInstitucion(inst);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo actualizaInstitucion",ex);
        }

        assertTrue(resultado);

        ITable actualTable=conndbunit.createQueryTable("institucion",
                "SELECT * FROM institucion WHERE id_institucion>4");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("instcompleto_upd.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");
        Assertion.assertEquals(expectedTable,actualTable);
        calif_inst += CALIF_UPDATE;
    }

    @Test
    @Order(6)
    public void testInstActualizarInexistente() throws Exception {
        long id=16;
        String nom="Universidad Tecnologica de Aguascalientes";
        String nomcorto="UTAGS";
        String url="http://www.utags.edu.mx";
        String calle="Blvd. Juan Pablo II 1302";
        String colonia="Canteras de San Agustin";
        long idmun=1001;
        long ident=1;
        String codpostal="20200";
        String tel="4499105000";
        Institucion inst= new Institucion(id,nom,nomcorto,url,
                calle,idmun,ident);
        inst.setColoniaInstitucion(colonia);
        inst.setCodpostalInstitucion(codpostal);
        inst.setTelefonoInstitucion(tel);

        boolean resultado=true;
        try {
            resultado=dao.actualizaInstitucion(inst);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo actualizaInstitucion",ex);
        }
        assertFalse(resultado);
        calif_inst += CALIF_UPDATE;
    }

    @Test
    @Order(7)
    public void testInstActualizarInvalida() throws Exception {
        long id=5;
        String[] nom={"Universidad","Universidad Con un Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String[] nomcorto={"UEZAM","UniversidadTecnologicadelEstadodeZacatecas"};
        String url1="Este URL es extremadamente largo y por tanto no deberia de pasar ";
        url1 += url1; url1 += url1; url1 += url1;
        String[] url ={"http://es.com",url1};
        String calle1="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        calle1 += calle1;
        calle1 += calle1;
        String[] calle={"Calle Tolosa 25",calle1};
        String colonia1="Ejido Cieneguitas";
        colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1;
        String[] colonia={"Centro",colonia1};
        long[] idmun ={32056,32100};
        long[] ident={32,35};
        String[] codpostal={"98000","198601"};
        String[] tel={"4921234567","49292762180"};

        boolean resultado;

        Institucion inst= new Institucion(id,nom[1],nomcorto[0],url[0],
                calle[0],idmun[0],ident[0]);
        inst.setColoniaInstitucion(colonia[0]);
        inst.setCodpostalInstitucion(codpostal[0]);
        inst.setTelefonoInstitucion(tel[0]);


        try {

            validaNullo_Vacio(null,5,false);
            validaNullo_Vacio("",5,false);


            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setNombreInstitucion(nom[0]);
            inst.setNombreCortoInstitucion(nomcorto[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);


            inst.setNombreCortoInstitucion(nomcorto[0]);
            inst.setCalleNumInstitucion(calle[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setCalleNumInstitucion(calle[0]);
            inst.setColoniaInstitucion(colonia[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setColoniaInstitucion(colonia[0]);
            inst.setIdMunicipioInstitucion(idmun[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setIdMunicipioInstitucion(idmun[0]);
            inst.setIdEntidadInstitucion(ident[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setIdEntidadInstitucion(ident[0]);
            inst.setCodpostalInstitucion(codpostal[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);

            inst.setCodpostalInstitucion(codpostal[0]);
            inst.setTelefonoInstitucion(tel[1]);
            resultado=dao.actualizaInstitucion(inst);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generarse excepcion al llamar al metodo actualizaInstitucion",ex);
            resultado=true;
        }
        assertFalse(resultado);
        calif_inst += CALIF_UPDATE;
    }

    @Test
    @Order(8)
    public void testInstEliminar() throws Exception {
        long id=5;
        try {
            boolean resultado=dao.eliminaInstitucion(id);
            assertTrue(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generarse excepcion al llamar al metodo eliminaInstitucion",ex);
        }

        ITable actualTable=conndbunit.createQueryTable("institucion",
                "SELECT * FROM institucion");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("inst_del.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");
        Assertion.assertEquals(expectedTable,actualTable);
        calif_inst += CALIF_DELETE;
    }


    @Test
    @Order(9)
    public void testInstEliminarInexistente() throws Exception {
        long id=16L;
        try {
            boolean resultado=dao.eliminaInstitucion(id);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            assertNull("No deberia generarse excepcion al llamar al metodo eliminaInstitucion",ex);
        }
        ITable actualTable=conndbunit.createQueryTable("institucion",
                "SELECT * FROM institucion");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("inst_del.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");

        Assertion.assertEquals(expectedTable,actualTable);
        calif_inst += CALIF_DELETE;
    }

    @Test
    @Order(10)
    public void testInstEliminarInvalida() throws Exception {
        long id=1L;
        try {
            boolean resultado=dao.eliminaInstitucion(id);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion al llamar al metodo eliminaInstitucion",ex);
        }

        ITable actualTable=conndbunit.createQueryTable("institucion",
                "SELECT * FROM institucion");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("inst_del.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");

        Assertion.assertEquals(expectedTable,actualTable);
        calif_inst += CALIF_DELETE;
    }


    /// MUNICIPIO

    private void comparaMunicipio(Municipio actual, ITable expected, int numrow) {
        try {
            assertEquals(String.valueOf(actual.getIdMunicipio()), expected.getValue(numrow, "id_municipio").toString());
            assertEquals(actual.getNombreMunicipio(), expected.getValue(numrow, "nombre_municipio"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion comparar los municipios",ex);
        }
    }

    @Test
    @Order(11)
    public void testMunicipioObten() throws Exception {
        List<Municipio> actual;
        long id=32;
        try {
            actual=dao.obtenMunicipios(id);
        }
        catch (Exception ex) {
            actual=new ArrayList<>();
            ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo obtenMunicipios",ex);
        }
        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("municipio32.xml"));
        ITable expectedTable=expectedDataSet.getTable("municipio");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaMunicipio(actual.get(i), expectedTable,i);
        }

        id=1;
        try {
            actual=dao.obtenMunicipios(id);
        }
        catch (Exception ex) {
            actual=new ArrayList<>();
            ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo obtenMunicipios",ex);
        }
        expectedDataSet=new FlatXmlDataSetBuilder().build(new File("municipio1.xml"));
        expectedTable=expectedDataSet.getTable("municipio");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaMunicipio(actual.get(i), expectedTable,i);
        }
        calif_municipio += CALIF_OBTENER;
    }

    ///   ENTIDAD

    private void comparaEntidad(Entidad actual, ITable expected, int numrow) {
        try {
            assertEquals(String.valueOf(actual.getIdEntidad()), expected.getValue(numrow, "id_entidad").toString());
            assertEquals(actual.getNombreEntidad(), expected.getValue(numrow, "nombre_entidad"));
        }
        catch (Exception ex) {
            //ex.printStackTrace();
            assertNull("No deberia generar excepcion comparar las entidades",ex);
        }
    }

    @Test
    @Order(12)
    public void testEntidadObten() throws Exception {
        List<Entidad> actual;
        try {
            actual=dao.obtenEntidades();
        }
        catch (Exception ex) {
            actual=new ArrayList<>();
            assertNull("No deberia generar excepcion el metodo obtenEntidades",ex);
        }
        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("entidad.xml"));
        ITable expectedTable=expectedDataSet.getTable("entidad");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaEntidad(actual.get(i), expectedTable,i);
        }
        calif_entidad += CALIF_OBTENER;
    }

    // PERSONA

    private void comparaPersona(Persona actual, ITable expected, int numrow) {
        try {
            assertEquals(actual.getEmailPersona(), expected.getValue(numrow, "email_persona"));
            assertEquals(actual.getNombrePersona(), expected.getValue(numrow, "nombre_persona"));
            assertEquals(actual.getApellidosPersona(), expected.getValue(numrow, "apellidos_persona"));
            assertEquals(actual.getSexoPersona(), expected.getValue(numrow, "sexo_persona"));
            assertEquals(actual.getCalleNumPersona(), expected.getValue(numrow, "calle_num_persona"));
            assertEquals(actual.getColoniaPersona(), expected.getValue(numrow, "colonia_persona"));
            assertEquals(String.valueOf(actual.getIdMunicipioPersona()), expected.getValue(numrow, "id_municipio_persona").toString());
            assertEquals(String.valueOf(actual.getIdEntidadPersona()), expected.getValue(numrow, "id_entidad_persona").toString());
            assertEquals(actual.getCodpostalPersona(), expected.getValue(numrow, "codpostal_persona"));
            assertEquals(actual.getTelefonoPersona(), expected.getValue(numrow, "telefono_persona"));
            java.sql.Date f1;
            f1=new java.sql.Date(actual.getFechaNacPersona().getTime());
            assertEquals(f1.toString(), expected.getValue(numrow, "fecha_nac_persona"));
            assertEquals(String.valueOf(actual.getIdInstitucionPersona()), expected.getValue(numrow, "id_institucion_persona").toString());
            assertEquals(actual.getTipoPersona(), expected.getValue(numrow, "tipo_persona"));
        }
        catch (Exception ex) {
            //ex.printStackTrace();
            assertNull("No deberia generar excepcion comparar las personas",ex);
        }
    }

    @Test
    @Order(13)
    public void testPersonaObten() throws Exception {
        
        List<Persona> actual;
        try {
            actual=dao.obtenPersonas();
        }
        catch (Exception ex) {
            actual=new ArrayList<>();
            assertNull("No deberia generar excepcion el metodo obtenPersonas de DaoConcursosImpl",ex);
        }
        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("concursosv3.xml"));
        ITable expectedTable=expectedDataSet.getTable("persona");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaPersona(actual.get(i), expectedTable,i);
        }
        calif_persona += CALIF_OBTENER;
    }

    @Test
    @Order(14)
    public void testPersonaAgregarValida() throws Exception {
        String email="misalas@itszn.edu.mx";
        String nom="Miguel";
        String apellidos="Salas Guzman";
        String sexo="M";
        String calle="Herreros 25";
        String colonia="Centro";
        long idmun=32042L;
        long ident=32L;
        String codpostal="99104";
        String tel="4331234567";
        Date fecha=new Date(java.sql.Date.valueOf("2001-10-13").getTime());
        long idinst=3L;
        String tipo="Estudiante";
        Persona per= new Persona(email,nom,apellidos,sexo,calle,idmun,ident,tel,fecha,idinst);
        per.setColoniaPersona(colonia);
        per.setCodpostalPersona(codpostal);
        per.setTipoPersona(tipo);

        
        boolean resultado;
        try {
            resultado = dao.agregaPersona(per);
        }
        catch (Exception ex) {
            resultado=false;
            assertNull("No deberia generar excepcion el metodo agregaPersona de DaoConcursosImpl",ex);
        }
        assertTrue(resultado);


        ITable actualTable=conndbunit.createQueryTable("persona",
                "SELECT * FROM persona");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("personacompleto_add.xml"));
        ITable expectedTable=expectedDataSet.getTable("persona");

        Assertion.assertEquals(expectedTable,actualTable);
        calif_persona += CALIF_AGREGAR;
    }

    @Test
    @Order(15)
    public void testPersonaAgregarDuplicada() throws Exception {
        String email="misalas@itszn.edu.mx";
        String nom="Miguel";
        String apellidos="Salas Guzman";
        String sexo="M";
        String calle="Herreros 25";
        String colonia="Centro";
        long idmun=32042L;
        long ident=32L;
        String codpostal="99104";
        String tel="4331234567";
        Date fecha=new Date(java.sql.Date.valueOf("2001-10-13").getTime());
        long idinst=3L;
        String tipo="Estudiante";
        Persona per= new Persona(email,nom,apellidos,sexo,calle,idmun,ident,tel,fecha,idinst);
        per.setColoniaPersona(colonia);
        per.setCodpostalPersona(codpostal);
        per.setTipoPersona(tipo);

        boolean resultado;
        try {
            resultado=dao.agregaPersona(per);
        }
        catch (Exception ex) {
            resultado=false;
            assertNull("No deberia generar excepcion el metodo agregaPersona de DaoConcursosImpl",ex);
        }
        assertFalse(resultado);
        calif_persona += CALIF_AGREGAR;
    }

    private void validaNullo_Vacio(String valor, String id, boolean nuevo) {
        String email=id;
        String nom="Miguel";
        String apellidos="Salas Guzman";
        String sexo="M";
        String calle="Herreros 25";
        String colonia="Centro";
        long idmun=32042L;
        long ident=32L;
        String codpostal="99104";
        String tel="4331234567";
        Date fecha=new Date(java.sql.Date.valueOf("2001-10-13").getTime());
        long idinst=3L;
        String tipo="Estudiante";
        boolean resultado=true;
        String valprev;

        Persona per= new Persona(valor,nom,apellidos,sexo,calle,idmun,ident,tel,fecha,idinst);
        per.setColoniaPersona(colonia);
        per.setCodpostalPersona(codpostal);
        per.setTipoPersona(tipo);

        if (nuevo) {
            resultado = nuevo ? dao.agregaPersona(per) : dao.actualizaPersona(per);
            assertFalse(resultado);
        }

        per.setEmailPersona(email);
        valprev=per.getNombrePersona();
        per.setNombrePersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setNombrePersona(valprev);
        valprev=per.getApellidosPersona();
        per.setApellidosPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setApellidosPersona(valprev);
        valprev=per.getSexoPersona();
        per.setSexoPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setSexoPersona(valprev);
        valprev=per.getCalleNumPersona();
        per.setCalleNumPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setCalleNumPersona(valprev);
        valprev=per.getTelefonoPersona();
        per.setTelefonoPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setTelefonoPersona(valprev);
        if (valor==null) {
            Date fechaprev=per.getFechaNacPersona();
            per.setFechaNacPersona(null);
            resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
            assertFalse(resultado);
            per.setFechaNacPersona(fechaprev);
        }

        valprev=per.getTipoPersona();
        per.setTipoPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertFalse(resultado);

        per.setTipoPersona(valprev);
        valprev=per.getColoniaPersona();
        per.setColoniaPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertTrue(resultado);

        Connection conn;
        Statement stmt=null;
        String sql=String.format("DELETE FROM persona WHERE email_persona='%s'",email);
        try {
            if (nuevo) {
                conn = conndbunit.getConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        }
        catch (SQLException exsql) {
            assertNull("Problema al eliminar la persona que se agrego en la prueba",exsql);
        }


        per.setColoniaPersona(valprev);
        valprev=per.getCodpostalPersona();
        per.setCodpostalPersona(valor);
        resultado=nuevo?dao.agregaPersona(per):dao.actualizaPersona(per);
        assertTrue(resultado);
        try {
            if (nuevo) {
                stmt.executeUpdate(sql);
            }
        }
        catch (SQLException exsql) {
            assertNull("Problema al eliminar la persona que se agrego en la prueba",exsql);
        }

    }

    @Test
    @Order(16)
    public void testPersonaAgregarInvalida() throws Exception {
        String[] email={"xxtat@yho.com","correoultralarguisimoquenodeberiadeseraceptado@masalla.delmasalla.delmasalla.com"};
        String[] nom={"Mario","Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String[] apellido={"Ramires","Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String sexo[] ={"F","N"};
        String calle1="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        calle1 += calle1;
        calle1 += calle1;
        String[] calle={"Calle Tolosa 25",calle1};
        String colonia1="Ejido Cieneguitas";
        colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1;
        String[] colonia={"Centro",colonia1};
        long[] idmun={32056,32100};
        long[] ident={32,35};
        String[] codpostal={"98000","198601"};
        String[] tel={"4921234567","49292762180"};
        
        java.sql.Date[] fechaSQL={java.sql.Date.valueOf("2000-10-10"),java.sql.Date.valueOf("2000-10-10")};
        Date[] fecha = {new Date(fechaSQL[0].getTime()), new Date(fechaSQL[1].getTime())};
        long[] idinst={1,350};
        String[] tipo={"Profesor","Teacher"};
        Persona per= new Persona(email[1],nom[0],apellido[0],sexo[0],calle[0],idmun[0],ident[0],tel[0],fecha[0],idinst[0]);
        per.setColoniaPersona(colonia[0]);
        per.setCodpostalPersona(codpostal[0]);
        per.setTipoPersona(tipo[0]);

        boolean resultado;
        try {
            //validaNullo_Vacio(null,"xxuigg@demonio.com",true);
            validaNullo_Vacio("","xxuigg@demonio.com",true);

            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setEmailPersona(email[0]);
            per.setNombrePersona(nom[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setNombrePersona(nom[0]);
            per.setApellidosPersona(apellido[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setApellidosPersona(apellido[0]);
            per.setSexoPersona(sexo[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setSexoPersona(sexo[0]);
            per.setCalleNumPersona(calle[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setCalleNumPersona(calle[0]);
            per.setColoniaPersona(colonia[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setColoniaPersona(colonia[0]);
            per.setIdMunicipioPersona(idmun[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setIdMunicipioPersona(idmun[0]);
            per.setIdEntidadPersona(ident[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setIdEntidadPersona(ident[0]);
            per.setCodpostalPersona(codpostal[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setCodpostalPersona(codpostal[0]);
            per.setTelefonoPersona(tel[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setTelefonoPersona(tel[0]);
            per.setIdInstitucionPersona(idinst[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);

            per.setIdInstitucionPersona(idinst[0]);
            per.setTipoPersona(tipo[1]);
            resultado=dao.agregaPersona(per);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            assertNull("No deberia generar excepcion el metodo save de DaoPersona",ex);
            ex.printStackTrace();
            resultado=true;
        }
        assertFalse(resultado);
        calif_persona += CALIF_AGREGAR;
    }


    @Test
    @Order(17)
    public void testPersonaActualizarValida() throws Exception {
        String email="misalas@itszn.edu.mx";
        String nom="Maria";
        String apellidos="Guzman Salas";
        String sexo="F";
        String calle="Juarez 215";
        String colonia="Las Flores";
        long idmun=1001L;
        long ident=1L;
        String codpostal="91010";
        String tel="4911234567";
        Date fecha=new Date(java.sql.Date.valueOf("2001-10-14").getTime());
        long idinst=1L;
        String tipo="Profesor";
        Persona per= new Persona(email,nom,apellidos,sexo,calle,idmun,ident,tel,fecha,idinst);
        per.setColoniaPersona(colonia);
        per.setCodpostalPersona(codpostal);
        per.setTipoPersona(tipo);

        
        boolean resultado=false;
        try {
            resultado=dao.actualizaPersona(per);
        }
        catch (Exception ex) {
            assertNull("No deberia generar excepcion el metodo actualizaPersona de DaoConcursosImpl",ex);
        }

        assertTrue(resultado);

        ITable actualTable=conndbunit.createQueryTable("persona",
                "SELECT * FROM persona WHERE email_persona='misalas@itszn.edu.mx'");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("persona_upd.xml"));
        ITable expectedTable=expectedDataSet.getTable("persona");
        Assertion.assertEquals(expectedTable,actualTable);
        calif_persona += CALIF_UPDATE;
    }

    @Test
    @Order(18)
    public void testPersonaActualizarInexistente() throws Exception {
        String email="xmisalas@itszn.edu.mx";
        String nom="Maria";
        String apellidos="Guzman Salas";
        String sexo="F";
        String calle="Juarez 215";
        String colonia="Las Flores";
        long idmun=1001L;
        long ident=1L;
        String codpostal="91010";
        String tel="4911234567";
        Date fecha=new Date(java.sql.Date.valueOf("2001-10-14").getTime());
        long idinst=1L;
        String tipo="Profesor";
        Persona per= new Persona(email,nom,apellidos,sexo,calle,idmun,ident,tel,fecha,idinst);
        per.setColoniaPersona(colonia);
        per.setCodpostalPersona(codpostal);
        per.setTipoPersona(tipo);

        
        boolean resultado=true;
        try {
            resultado=dao.actualizaPersona(per);
        }
        catch (Exception ex) {
            //ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo actualizaPersona de DaoConcursosImpl",ex);
        }
        assertFalse(resultado);
        calif_persona += CALIF_UPDATE;
    }

    @Test
    @Order(19)
    public void testPersonaActualizarInvalida() throws Exception {
        String[] email={"xxtat@yho.com","correoultralarguisimoquenodeberiadeseraceptado@masalla.delmasalla.delmasalla.com"};
        String[] nom={"Mario","Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String[] apellido={"Ramires","Nombre Extremadamente Largo para el Limite que tiene el campo y que por tanto no deberia de pasar Tecnologica Estado de Zacatecas"};
        String[] sexo={"F","N"};
        String calle1="Carretera Zacatecas, Cd Cuauhtemoc km. 5";
        calle1 += calle1;
        calle1 += calle1;
        String[] calle={"Calle Tolosa 25",calle1};
        String colonia1="Ejido Cieneguitas";
        colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1; colonia1 += colonia1;
        String[] colonia={"Centro",colonia1};
        long[] idmun={32056,32100};
        long[] ident={32,35};
        String[] codpostal={"98000","198601"};
        String[] tel={"4921234567","49292762180"};
        java.sql.Date[] fechaSQL={java.sql.Date.valueOf("2000-10-10"), java.sql.Date.valueOf("2000-10-10")};
        Date[] fecha = {new Date(fechaSQL[0].getTime()), new Date(fechaSQL[1].getTime())};
        long[] idinst={1,350};
        String[] tipo={"Profesor","Teacher"};
        Persona per= new Persona(email[1],nom[0],apellido[0],sexo[0],calle[0],idmun[0],ident[0],tel[0],fecha[0],idinst[0]);
        per.setColoniaPersona(colonia[0]);
        per.setCodpostalPersona(codpostal[0]);
        per.setTipoPersona(tipo[0]);

        boolean resultado;
        try {
            //validaNullo_Vacio(null,"misalas@itszn.edu.mx",false);
            validaNullo_Vacio("","misalas@itszn.edu.mx",false);


            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setEmailPersona(email[0]);
            per.setNombrePersona(nom[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setNombrePersona(nom[0]);
            per.setApellidosPersona(apellido[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setApellidosPersona(apellido[0]);
            per.setSexoPersona(sexo[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setSexoPersona(sexo[0]);
            per.setCalleNumPersona(calle[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setCalleNumPersona(calle[0]);
            per.setColoniaPersona(colonia[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setColoniaPersona(colonia[0]);
            per.setIdMunicipioPersona(idmun[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setIdMunicipioPersona(idmun[0]);
            per.setIdEntidadPersona(ident[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setIdEntidadPersona(ident[0]);
            per.setCodpostalPersona(codpostal[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setCodpostalPersona(codpostal[0]);
            per.setTelefonoPersona(tel[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setTelefonoPersona(tel[0]);
            per.setIdInstitucionPersona(idinst[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);

            per.setIdInstitucionPersona(idinst[0]);
            per.setTipoPersona(tipo[1]);
            resultado=dao.actualizaPersona(per);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            assertNull("No deberia generar excepcion el metodo update de DaoPersona",ex);
            ex.printStackTrace();
            resultado=true;
        }

        assertFalse(resultado);
        calif_persona += CALIF_UPDATE;
    }

    @Test
    @Order(20)
    public void testPersonaEliminarValida() throws Exception {
        String id="misalas@itszn.edu.mx";
        
        try {
            boolean resultado=dao.eliminaPersona(id);
            assertTrue(resultado);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            assertNull("No deberia generar excepcion el metodo eliminaPersona de DaoConcursosImpl",ex);
        }

        //ITable actualTable=conndbunit.createTable("institucion");
        ITable actualTable=conndbunit.createQueryTable("persona",
                "SELECT * FROM persona");

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("concursosv3.xml"));
        ITable expectedTable=expectedDataSet.getTable("persona");
        Assertion.assertEquals(expectedTable,actualTable);
        calif_persona += CALIF_DELETE;
    }


    @Test
    @Order(21)
    public void testPersonaEliminaInexistente() throws Exception {
        String id="xmisalas@itszn.edu.mx";
        
        try {
            boolean resultado=dao.eliminaPersona(id);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            assertNull("No deberia generar excepcion el metodo eliminaPersona de DaoConcursosImpl",ex);
        }
        calif_persona += CALIF_DELETE;
    }

    @Test
    @Order(22)
    public void testPersonaEliminaInvalida() throws Exception {
        String id="charly@uaz.edu.mx";
        try {
            boolean resultado=dao.eliminaPersona(id);
            assertFalse(resultado);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
            assertNull("No deberia generar excepcion el metodo delete de DaoPersona",ex);
        }
        calif_persona += CALIF_DELETE;
    }
}
