# PROGRAMA 2. SERVICIOS REST PARA DAOConcursos EN LA BASE DE DATOS controlconcursos.

## PREREQUISITOS PARA ESTE PROGRAMA
1. Tener instalado el Glassfish 5.1.0, que se puede bajar de [Página de Glassfish](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) (la versión Full Profile)
2. Tener instalada la versión 8 de Java (Glassfish solo funciona para esa versión), que se puede bajar de [Página de Java 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
3. Configurar IntellijIDEA para usar Glassfish
4. Para probar sus servicios se sugiere usar algun cliente REST como por ejemplo Postman, que se puede bajar la [Pagina de Postman](https://www.postman.com/downloads/) 
4. Para realizar las pruebas desde Github será necesario tener ngrok, el cual se puede bajar de [Página de ngrok](https://dashboard.ngrok.com/get-started/setup), además de haberlo configurado de acuerdo a lo que se realizó para la practica 9.

## COPIA DEL REPOSITORIO REMOTO EN SU COMPUTADORA LOCAL

Como primer paso, será necesario crear una copia local del repositorio remoto creado en Github al aceptar la tarea. Para ello, es necesario hacer los siguientes pasos:
1. Entrar a la página cuyo URL les fue proporcionado al aceptar la tarea, en tal página dé click en el botón Code y copie el URL que aparece en el cuadro de texto de nombre **Clone with HTTPS** (comienza con *https://*)
2. Abre IntelliJ IDEA e indica que harás un clon local de tu repositorio:
   - Si no tienes ningún repositorio abierto selecciona la opción **Get From Version Control** de la Ventana de Bienvenida, o si tienes un proyecto abierto, puedes entrar al menú **VCS** y seleccionar la opción **Get From Version Control**
   - En el cuadro de diálogo que aparece:
     - Selecciona Git
     - Pega el URL que copiaste en el paso 1  en el cuadro de texto **URL**
     - Selecciona en Directory la carpeta donde lo colocarás, es importante que crees una nueva carpeta o se colocará (da click en el icono de carpeta , navega a donde lo quieres colocar y da click en el icono de New Folder para crear una nueva carpeta)
     - Da click en **Clone**
     - Si te pide usuario y clave de Github proporciona esos datos
     - Después de unos segundos tendrás listo tu un clon de tu repositorio listo para trabajar en Intellij IDEA

## MODIFICACIÓN DE ARCHIVOS DE CONFIGURACIÓN

Antes de hacer los cambios en el código haga las siguientes modificaciones:

1. En el archivo index.html, incluido dentro de la subcarpeta **webapp** en la sección **main** del proyecto, deberá sustituir las NNNNNNNNNNNNN por su nombre completo y las XXXXXXXX por su matrícula, lo cual también deberá realizar en el archivo **datosmysql.properties** ubicado dentro de la subcarpeta **resources** en la sección **main**

2. Edite el archivo .gitignore dentro de la carpeta .idea para que la ruta al DataSources sea la correcta de acuerdo a la ubicación donde quedó clonado el proyecto

3. Asegúrese de estar usando la versión 8 de Java (en el menu File-> Project Structure, Sección Project, Opción Project SDK)

4. En el archivo build.gradle modifique el valor de version para que se use su matrícula

5. De clik en el icono de Gradle que aparece para que se sincronicen los cambios

6. Agregue una configuración de ejecución para poder poner a correr el Glassfish, para ello:
   - Dé click en Add Configuration...
   - Dé click en el +
   - Seleccione GlassFish Server -> Local
   - En el cuadro de diálogo que aparece seleccione domain1 en Server Domain, marque Preserve Sessions Across Redeployment, en la pestaña Deployment dé click en +, seleccione Artifact y seleccione RESTConcursos-XXXXXXXX.jar (donde las XXXXXXXX ya debieran aparecer como su matricula), de click en OK, regrese a la pestaña Server y seleccione Redeploy en On 'Update' Action, de click en OK


## MODIFICACIÓN DEL CÓDIGO PROPORCIONADO

Una vez que tengas el repositorio local, el trabajo principal consiste en crear las clases para proporcionar los servicios REST descritos en la siguiente sección. Aseguráte de una vez creados los servicios REST, poner en el método **agregaRecursosREST** de la clase **AplicacionConfig** código para agregar las clases que representan a los servicios REST al Set que recibe como argumento. De hecho puedes comenzar agregando los dos servicios REST que se realizaron para la práctica 9.

Vea las instrucciones en el archivo **POO2_Progr2AgoDic2020.pdf** para mas detalles sobre lo que tiene que implementar.

## CALIFICACIÓN

Habrá un archivo para realizar las pruebas dentro de la sección **test**, que es la clase **testServiciosREST**, la cual deberá irse actualizando conforme lo indique el profesor, el incluido solo hace un par de pruebas. La calificación será mostrada al final de la prueba. Ese archivo será ejecutado de manera automática en el Github al hacer push, aunque debido a la naturaleza del proyecto, el servidor Glassfish deberá estar corriendo en su computadora local y Github se comunicará con su servidor Glassfish y MySQL  por lo cual será necesario que tenga corriendo ngrok de acuerdo a lo indicado para la práctica 9 y deberá haber colocado los datos correspondientes para que desde Github se pueda hacer conexión a su computadora cambiando los datos del archivo **datosmysql.properties**


Para ejecutar las pruebas de tu programa en tu computadora local selecciona **testServiciosREST**  dentro de src/test/java, dale click con el botón derecho y selecciona **Run** (la opción tendrá un triángulo verde ), te mostrará el resultado de las pruebas y la calificación obtenida

## NOTAS IMPORTANTES
1. Las clases que representan a los servicios REST deben tener un constructor vacío que manda llamar al constructor de la clase padre pasándole como referencia la clase de entidad con la que se trabajará.
2. Deben tener un atributo privado de tipo EntityManager que tendrá la anotación siguiente: **@PersistenceContext(unitName = "default")**
3. El método getEntityManager (que debe implementar) regresará simplemente el objeto EntityManager que tiene como atributo
4. Solo implementará los métodos que se requieran de acuerdo a la tabla para la cual se está haciendo los servicios REST con las anotaciones adecuadas (GET,POST,PUT,DELETE, si consume información de que tipo, si genera información de que tipo y las rutas asociadas)

5. Cada vez que completes un método hazle deploy al servicio y ejecuta la prueba para verificar que las pruebas del método completado son exitosas

6. Una vez vez que completes un método y verifiques que pasa las pruebas haz un Commit : 
   - Para hacer commit, selecciona primero el proyecto, después entra al menú VCS y selecciona la opción Commit...
   - Te aparecerá una lista de archivos con los cambios detectados, verifica que incluye todos los archivos que deberían estar
   - Teclea un mensaje que describa los cambios realizados de manera clara y concisa (debe ser un mensaje que permita a otras personas darse cuenta de lo realizado)
   - Dé click en el ícono del engrane (Show Commit Options), escriba su nombre en el cuadro Author, deseleccione Perform Code Analysis y Check TODO (Esto es necesario solo en el primer commit que hagan)
   - Dé click en Commit

7. Después de haber hecho todos los commits que completan tu programa, súbelo al repositorio remoto haciendo lo siguiente:
   - Entre al menú VCS y seleccione la opción Git->Push...
   - Dé click en el botón Push en el cuadro de diálogo que aparece

8. Cada vez que subas tu proyecto al repositorio privada con un push, se aplicarán automáticamente las pruebas sobre tu código para verificar si funciona correctamente. Recuerda que en la página de tu repositorio en la sección **Pull Requests**, se encuentra una subsección de nombre **Feedback**, donde podrás encontrar los resultados de las pruebas en la pestaña denominada **Check** (expandiendo la parte que dice **Run education/autograding@v1**), y cualquier comentario general que el profesor tenga sobre tu código en la pestaña **Conversation**. 
