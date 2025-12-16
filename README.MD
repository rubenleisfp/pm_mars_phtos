Mars Photos
==================================
En esta rama  del proyecto se recuperan el JSON del API y se muestra el número total de elementos recuperados. No se renderizan las imágenes
Se utiliza un repositorio e inyección de dependencias

Es una aplicación demo que muestra fotos de la superficio de Marte.
Estas fotos están alojadas en un Servidor Web, concretamente en un Servicio Rest

Esta app demuestra el uso de Retrofit para hacer peticiones REST

Esta app demuestra el uso de [Retrofit](https://square.github.io/retrofit/) para
hacer peticiones al servicio web [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) to
maneja la deserialización del JSON retornado a objetos Kotlin [Coil](https://coil-kt.github.io/coil/)
para mostrar la URL como imagen

## Arquitectura y diseño técnico

Esta aplicación sigue una arquitectura moderna recomendada por Google, conocida como Arquitectura Limpia (Clean Architecture) o MVVM (Model-View-ViewModel) con un repositorio y fuentes de datos. El objetivo es separar responsabilidades, haciendo la app más escalable, testeable y mantenible.

Las capas principales del proyecto son:

1. Capa de UI (Interfaz de Usuario): Lo que el usuario ve y con lo que interactúa.
2. Capa de Dominio/ViewModel: La lógica de negocio y de presentación.
3. Capa de Datos (Data Layer): Gestiona la obtención y el almacenamiento de los datos.

A continuación, se detalla cada capa y cómo interactúan entre sí.

### 1. Capa de UI

Esta capa está construida con Jetpack Compose. Su única responsabilidad es mostrar los datos en pantalla y notificar al ViewModel sobre las acciones del usuario.

* Componentes Clave:
    * MainActivity.kt: Es el punto de entrada. Aloja el composable principal de la aplicación.
    * MarsPhotosApp.kt (o un composable principal similar): Contiene la lógica de la UI, como mostrar un estado de "Cargando", un estado de "Error" o la cuadrícula (LazyVerticalGrid) con las fotos si la carga ha sido exitosa.
    * MarsPhotoCard.kt: Un composable que define cómo se ve cada foto individual en la cuadrícula. Utiliza la librería Coil para descargar la imagen desde una URL y mostrarla.
* Interacción:
1. La UI observa un StateFlow o LiveData expuesto por el MarsViewModel. Este estado puede ser, por ejemplo, Loading, Success(List<MarsPhoto>) o Error.
2. Cuando el estado cambia en el ViewModel, la UI de Compose se recompone automáticamente para reflejar el nuevo estado (por ejemplo, pasa de mostrar un círculo de carga a mostrar la cuadrícula de fotos).
3. La UI no sabe de dónde vienen los datos (si de una API o de una base de datos local). Solo sabe que los recibe del ViewModel.

### 2. Capa de dominio / viewmodel

Esta capa actúa como un puente entre la UI y la capa de datos. Contiene la lógica de presentación y prepara los datos para ser mostrados.
* Componentes Clave:
    * MarsViewModel.kt:•Es el corazón de la lógica de la UI. Sobrevive a cambios de configuración (como rotar la pantalla).
    * Expone el estado de la UI (por ejemplo, val uiState: StateFlow<MarsUiState>).
    * Utiliza corrutinas para llamar a los métodos del repositorio en un hilo secundario, evitando bloquear el hilo principal (UI thread).
    * No tiene ninguna referencia a Retrofit, OkHttp o cualquier detalle de implementación de la red. Solo conoce la interfaz del repositorio (MarsPhotosRepository).
* Interacción
1. Cuando el MarsViewModel se inicializa (en su bloque init), lanza una corrutina para llamar a una función del repositorio, como repository.getMarsPhotos().
2. Recibe el resultado (una lista de objetos MarsPhoto o una excepción) del repositorio.
3. Actualiza su uiState con el resultado, lo que provoca que la UI se redibuje.

### 3. Capa de Datos (Data Layer)

Esta es la capa más importante para la obtención de datos. Es la única que sabe cómo y de dónde obtener la información. Se subdivide en el Repositorio y las Fuentes de Datos (Data Sources).

* Componentes clave

    * MarsPhotosRepository (Interfaz y Clase):
        * interface MarsPhotosRepository: Define el contrato. Expone funciones "qué se puede hacer", como suspend fun getMarsPhotos(): List<MarsPhoto>. Ni el ViewModel ni la UI saben cómo se implementa.
        * DefaultMarsPhotosRepository: Es la implementación concreta de la interfaz. Su trabajo es llamar a la fuente de datos de red (MarsApiService) para obtener los datos. Si en el futuro se añadiera una base de datos local (cacheo), este repositorio decidiría si obtener los datos de la red o de la base de datos.
    * MarsApiService (Interfaz de Retrofit):
        * Define los endpoints de la API REST a los que se va a llamar. Utiliza anotaciones de Retrofit (@GET, @POST, etc.).
        * Ejemplo: suspend fun getPhotos(): List<MarsPhoto>.

    * MarsPhoto (Modelo de Datos):
        * Es una data class de Kotlin que representa la estructura de un objeto foto que llega en el JSON de la API.
        * Está anotada con @Serializable para que la librería kotlinx.serialization sepa cómo convertir el JSON en un objeto Kotlin.
        * Objeto Retrofit: Configura y construye la instancia de Retrofit. Aquí se define la URL base y se añade el converterFactory de kotlinx.serialization para que Retrofit sepa cómo deserializar las respuestas JSON.
* Interacción:
1. El DefaultMarsPhotosRepository recibe una llamada del MarsViewModel.
2. El repositorio llama a la función correspondiente en MarsApiService (la interfaz de Retrofit).
3. Retrofit y OkHttp se encargan de realizar la petición de red real.
4. Cuando el servidor responde, Retrofit utiliza el conversor de kotlinx.serialization para transformar la respuesta JSON en una lista de objetos MarsPhoto.
5. El repositorio devuelve esta lista al ViewModel.



### ¿Cómo Funciona la Inyección de Dependencias?

La inyección de dependencias (ID) es el pegamento que une todas estas capas de forma desacoplada. En lugar de que una clase cree sus propias dependencias (por ejemplo, ViewModel creando su Repository), estas se le "inyectan" desde fuera.En tu proyecto, esto se implementa de una manera sencilla pero efectiva, probablemente usando un contenedor de dependencias manual:
1. Se crea un objeto AppContainer o similar:
* Este objeto es responsable de crear y mantener las instancias de los objetos importantes, como la instancia de Retrofit y el DefaultMarsPhotosRepository.
* Al crear el DefaultMarsPhotosRepository, le "inyecta" la instancia de MarsApiService que necesita.

  ```
        // Dentro del AppContainer
        private val retrofitService: MarsApiService by lazy { ... }
        val marsPhotosRepository: MarsPhotosRepository by lazy {
            DefaultMarsPhotosRepository(retrofitService) // ¡Inyección aquí!
        }

  ```
2. El ViewModel se crea usando una ViewModelProvider.Factory:
* Esta "fábrica" personalizada es la que instancia el MarsViewModel.
* En su método create, obtiene la instancia del MarsPhotosRepository desde el AppContainer y se la pasa al constructor del MarsViewModel.

 ```
         // ViewModel Factory
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MarsViewModel(appContainer.marsPhotosRepository) as T // ¡Inyección aquí!
        }
```     

Ventajas de este enfoque:
* Desacoplamiento: El MarsViewModel no sabe qué implementación concreta del MarsPhotosRepository está usando. Solo conoce la interfaz.
* Testeabilidad: Puedes crear fácilmente una prueba para el MarsViewModel y pasarle un FakeMarsPhotosRepository (un repositorio falso) que devuelva datos de prueba, sin necesidad de hacer una llamada de red real.
* Flexibilidad: Si mañana decides cambiar de Retrofit a Ktor, solo tendrías que modificar la capa de datos (el AppContainer y la fuente de datos). El ViewModel y la UI no se verían afectados.


Pre-requisitos
--------------

Debes saber:
* Composable functions
* Arquitectura Android, ViewModel
* Corrutinas
* Lazy Grid


Librerías
--------------
//Incluidas dependecias. Retrofit y serializacion
* implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
* implementation("com.squareup.retrofit2:retrofit:2.9.0")
* implementation("com.squareup.okhttp3:okhttp:4.11.0")
* implementation("io.coil-kt:coil-compose:2.4.0")
* implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")


Permisos
--------------
    <uses-permission android:name="android.permission.INTERNET" />
