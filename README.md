# ViniloApp

ViniloApp es una aplicación Android para la gestión de colecciones de vinilos, desarrollada en Kotlin.

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- [Android Studio](https://developer.android.com/studio) (versión más reciente recomendada)
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) o superior
- [Git](https://git-scm.com/downloads)

## 🚀 Configuración del Entorno de Desarrollo

1. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/felipesuarezB/MISOS2C2_aplicaciones_moviles.git
   cd MISOS2C2_aplicaciones_moviles
   ```

2. **Configurar Java 17**
   - Asegúrate de que Java 17 esté configurado como JDK en Android Studio
   - En Android Studio: File > Project Structure > SDK Location > JDK Location
   - Selecciona la ruta de tu JDK 17

3. **Configurar Gradle**
   - El proyecto ya incluye la configuración necesaria en `gradle.properties`
   - Verifica que la ruta de Java 17 sea correcta en tu sistema:
     ```properties
     org.gradle.java.home=/ruta/a/tu/jdk17
     ```

## 🔨 Construcción del Proyecto

1. **Abrir el Proyecto en Android Studio**
   - Abre Android Studio
   - Selecciona "Open an existing project"
   - Navega hasta la carpeta del proyecto y selecciónala

2. **Sincronizar el Proyecto**
   - Android Studio detectará automáticamente el proyecto Gradle
   - Espera a que se complete la sincronización inicial
   - Si se te pide, acepta la instalación de los componentes necesarios

3. **Construir la APK de Debug**
   ```bash
   ./gradlew assembleDebug
   ```
   La APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

4. **Construir la APK de Release**
   ```bash
   ./gradlew assembleRelease
   ```
   La APK se generará en: `app/build/outputs/apk/release/app-release.apk`

## 🔑 Configuración de Firma para Release

Para generar una APK firmada, necesitarás un keystore. El proyecto incluye un keystore de ejemplo con las siguientes credenciales:

- Ruta del keystore: `my-release-key.jks`
- Contraseña del keystore: `android`
- Alias de la clave: `my-key-alias`
- Contraseña de la clave: `android`

Para producción, se recomienda generar un nuevo keystore con credenciales seguras.

## 📱 Ejecución en Dispositivo o Emulador

1. **Conectar un Dispositivo Físico**
   - Activa el modo desarrollador en tu dispositivo Android
   - Habilita la depuración USB
   - Conecta el dispositivo vía USB

2. **Usar un Emulador**
   - En Android Studio: Tools > Device Manager
   - Crea un nuevo dispositivo virtual si es necesario
   - Selecciona un dispositivo con Android 7.0 o superior

3. **Ejecutar la Aplicación**
   - Selecciona el dispositivo/emulador en la barra de herramientas
   - Haz clic en el botón "Run" (▶️)
   - La aplicación se instalará y ejecutará automáticamente

## 🧪 Pruebas

El proyecto incluye pruebas unitarias y pruebas de UI. Para ejecutarlas:

```bash
# Ejecutar todas las pruebas
./gradlew test

# Ejecutar pruebas de UI
./gradlew connectedAndroidTest
```

## 📦 Dependencias Principales

- AndroidX Core KTX
- Material Design
- Navigation Component
- Glide para manejo de imágenes
- Volley para peticiones HTTP
- Coroutines para programación asíncrona

## ⚠️ Solución de Problemas Comunes

1. **Error de JDK**
   - Verifica que estés usando JDK 17
   - Actualiza la ruta en `gradle.properties`

2. **Error de Gradle Sync**
   - Limpia el proyecto: `./gradlew clean`
   - Sincroniza nuevamente: `./gradlew --refresh-dependencies`

3. **Error de Build**
   - Verifica que todas las dependencias estén correctamente configuradas
   - Asegúrate de tener suficiente memoria RAM asignada a Gradle
 
4. **Error de backend**

   **MAC**
   - Clonar el repositorio del backend que se encuentra en el siguiente link : `https://github.com/TheSoftwareDesignLab/BackVynils`
   - Con el  comando `docker-compose up`  desplegar los docker configurados en el backend.
   - Verificar que exista conexión a `http://127.0.0.1:3000/`
   - En el archivo Android Manifest verificar que exista la linea `android:networkSecurityConfig="@xml/network_security_config"`
   - Verificar que en la ruta de archivos *res* en la carpeta de `xml` exista un archivo llamado `network_security_config.xml`
   - Abrir la terminal de comandos e introducir el comando `ifconfig`
   - Buscar la `Broadcast` IP. Debe verse de la sigueinte manera 	`inet 192.168.1.6 netmask 0xffffff00 broadcast 192.168.1.255`
   - En el archivo *NetworkServiceAdapter.kt* descomentar la línea y cambiar la IP por la que obtuvo en el paso anterior `const val BASE_URL = "http://192.168.1.6:3000/"`
   - En el archivo *NetworkServiceAdapter.kt* comentar la línea `const val BASE_URL = "http://backvynils-q6yc.onrender.com/"`
   - Correr Nuevamente ek proyecto. Ahora el backend apuntará al repositorio local.

   **WINDOWS**
   - Clonar el repositorio del backend que se encuentra en el siguiente link : `https://github.com/TheSoftwareDesignLab/BackVynils`
   - Con el  comando `docker-compose up`  desplegar los docker configurados en el backend.
   - Verificar que exista conexión a `http://127.0.0.1:3000/`
   - En el archivo Android Manifest verificar que exista la linea `android:networkSecurityConfig="@xml/network_security_config"`
   - Verificar que en la ruta de archivos *res* en la carpeta de `xml` exista un archivo llamado `network_security_config.xml`
   - En el archivo *NetworkServiceAdapter.kt* descomentar la línea `const val BASE_URL = "http://10.0.2.2:3000/"`
   - En el archivo *NetworkServiceAdapter.kt* comentar la línea `const val BASE_URL = "http://backvynils-q6yc.onrender.com/"`
   - Correr Nuevamente ek proyecto. Ahora el backend apuntará al repositorio local.

## 📝 Notas Adicionales

- El proyecto usa ViewBinding y DataBinding
- La versión mínima de Android soportada es 7.0 (API 24)
- Se recomienda usar Android Studio Arctic Fox o superior

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, sigue estos pasos:
1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request