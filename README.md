# NotasApp

Proyecto del segundo parcial de **Desarrollo Móvil**. Es una aplicación Android para crear y consultar notas personales.

## ¿Qué permite hacer?

- Registrar usuarios e iniciar sesión.
- Mostrar una pantalla de bienvenida.
- Crear notas con título y contenido.
- Consultar las notas guardadas por cada usuario.

## Tecnologías utilizadas

- **Android Studio, Kotlin y XML:** desarrollo y diseño de la aplicación.
- **MVVM y LiveData:** organización del código y manejo de los datos en las pantallas.
- **Retrofit y OkHttp:** conexión entre Android y el servidor.
- **Python y FastAPI:** funcionamiento del backend.
- **SQLite:** almacenamiento de usuarios y notas.

## Estructura del proyecto

- `app/`: aplicación Android.
- `backend/`: servidor Python y sus dependencias.

## Cómo ejecutarlo

1. Abrir el proyecto en **Android Studio**.
2. Abrir una terminal dentro de la carpeta `backend` y ejecutar:

   ```powershell
   py -m venv .venv
   .\.venv\Scripts\python.exe -m pip install -r requirements.txt
   .\.venv\Scripts\python.exe -m uvicorn main:app --host 0.0.0.0 --port 8000
   ```

3. En `ApiClient.kt`, configurar `BASE_URL` con la dirección donde está funcionando el backend. Para el emulador se puede usar `http://10.0.2.2:8000/`; para un celular físico conectado a la misma red, utilizar la IP de la computadora.
4. Ejecutar la aplicación desde Android Studio.

La base de datos SQLite se crea al iniciar el servidor y **no se incluye en el repositorio**.

