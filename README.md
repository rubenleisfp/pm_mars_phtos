Mars Photos
==================================
En esta rama start del proyecto se ofrece una maqueta a completar 
por el estudiante en la que tendrá que obtener una serie de imagenes de Marte
de un API y renderizarlas en la aplicación.

Es una aplicación demo que muestra fotos de la superficio de Marte.
Estas fotos están alojadas en un Servidor Web, concretamente en un Servicio Rest

Esta app demuestra el uso de Retrofit para hacer peticiones REST

Esta app demuestra el uso de [Retrofit](https://square.github.io/retrofit/) para
hacer peticiones al servicio web [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) to
maneja la deserialización del JSON retornado a objetos Kotlin [Coil](https://coil-kt.github.io/coil/)
para mostrar la URL como imagen


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