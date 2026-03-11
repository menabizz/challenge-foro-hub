![imagen](https://i.imgur.com/4RPGAIx.png)  
# Foro Hub
Implementación del Challenge Foro Hub del curso de Back End de Alura LATAM

¡Bienvenido! A través de este código, podrás simular el comportamiento de un foro de discusión con usuarios y tópicos.


## Funcionalidades
* Registro y gestión de usuarios con contraseñas encriptadas con BCrypt.
  
* Inicio de sesión, validación de credenciales y emisión de tokens JWT.
  
* CRUD de tópicos del foro:
  * Crear
  * Listar
  * Actualizar
  * Desactivar


## Uso
* Clonar el repositorio, ajustar el application-properties a tu servidor de preferencia. En este caso el código utiliza PostgreSQL.

* Probar endpoints con Postman:

```POST /registro``` → registrar usuario

```POST /auth``` → login y obtención de token

```/topicos``` → CRUD de tópicos (requiere el token de la autenticación)


#

 ### ¡Diviertete probando con distintos topicos y usuarios!

``` Hecho por menabizz. ```
