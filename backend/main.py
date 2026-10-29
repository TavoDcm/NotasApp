from fastapi import FastAPI, HTTPException, Depends
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from pydantic import BaseModel, EmailStr, Field
import sqlite3
import hashlib
import secrets
import time
from pathlib import Path

app = FastAPI(title="NotasApp API")
seguridad = HTTPBearer(auto_error=False)
DB = Path(__file__).with_name("notas.db")

def conectar():
    conexion = sqlite3.connect(DB)
    conexion.row_factory = sqlite3.Row
    conexion.execute("PRAGMA foreign_keys = ON")
    return conexion

def iniciar_bd():
    with conectar() as db:
        db.execute("""CREATE TABLE IF NOT EXISTS usuarios (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            correo TEXT NOT NULL UNIQUE,
            salt TEXT NOT NULL,
            clave_hash TEXT NOT NULL
        )""")
        db.execute("""CREATE TABLE IF NOT EXISTS sesiones (
            token_hash TEXT PRIMARY KEY,
            usuario_id INTEGER NOT NULL,
            expira INTEGER NOT NULL,
            FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        )""")
        db.execute("""CREATE TABLE IF NOT EXISTS notas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            usuario_id INTEGER NOT NULL,
            titulo TEXT NOT NULL,
            contenido TEXT NOT NULL,
            fecha_creacion TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        )""")

iniciar_bd()

class Registro(BaseModel):
    nombre: str = Field(min_length=2, max_length=70)
    correo: EmailStr
    contrasena: str = Field(min_length=8, max_length=100)

class Login(BaseModel):
    correo: EmailStr
    contrasena: str

class NotaNueva(BaseModel):
    titulo: str = Field(min_length=1, max_length=120)
    contenido: str = Field(min_length=1, max_length=3000)

def hash_clave(clave, salt):
    return hashlib.pbkdf2_hmac("sha256", clave.encode(), bytes.fromhex(salt), 200000).hex()


def usuario_actual(
    credenciales: HTTPAuthorizationCredentials | None = Depends(seguridad)
):
    if credenciales is None:
        raise HTTPException(401, "Falta token de autorización")

    token = credenciales.credentials
    token_hash = hashlib.sha256(token.encode()).hexdigest()

    with conectar() as db:
        fila = db.execute("""
            SELECT usuarios.id, usuarios.nombre, usuarios.correo
            FROM sesiones
            JOIN usuarios ON usuarios.id = sesiones.usuario_id
            WHERE sesiones.token_hash = ?
            AND sesiones.expira > ?
        """, (token_hash, int(time.time()))).fetchone()

    if fila is None:
        raise HTTPException(401, "Token inválido o vencido")

    return dict(fila)


@app.post("/registro", status_code=201)
def registro(datos: Registro):
    nombre = datos.nombre.strip()
    if len(nombre) < 2:
        raise HTTPException(400, "Nombre demasiado corto")
    salt = secrets.token_hex(16)
    try:
        with conectar() as db:
            db.execute("INSERT INTO usuarios (nombre, correo, salt, clave_hash) VALUES (?, ?, ?, ?)",
                       (nombre, datos.correo.lower(), salt, hash_clave(datos.contrasena, salt)))
    except sqlite3.IntegrityError:
        raise HTTPException(409, "Este correo ya existe")
    return {"mensaje": "Usuario registrado"}

@app.post("/login")
def login(datos: Login):
    with conectar() as db:
        usuario = db.execute("SELECT * FROM usuarios WHERE correo = ?", (datos.correo.lower(),)).fetchone()
        if usuario is None or not secrets.compare_digest(
                hash_clave(datos.contrasena, usuario["salt"]), usuario["clave_hash"]):
            raise HTTPException(401, "Correo o contraseña incorrectos")
        token = secrets.token_urlsafe(32)
        token_hash = hashlib.sha256(token.encode()).hexdigest()
        db.execute("INSERT INTO sesiones (token_hash, usuario_id, expira) VALUES (?, ?, ?)",
                   (token_hash, usuario["id"], int(time.time()) + 86400))
    return {"token": token, "nombre": usuario["nombre"], "correo": usuario["correo"]}

@app.post("/notas", status_code=201)
def crear_nota(nota: NotaNueva, usuario=Depends(usuario_actual)):
    titulo, contenido = nota.titulo.strip(), nota.contenido.strip()
    if not titulo or not contenido:
        raise HTTPException(400, "El título y el contenido son obligatorios")
    with conectar() as db:
        cursor = db.execute("INSERT INTO notas (usuario_id, titulo, contenido) VALUES (?, ?, ?)",
                            (usuario["id"], titulo, contenido))
        numero = cursor.lastrowid
    return {"id": numero, "mensaje": "Nota guardada correctamente"}

@app.get("/notas")
def listar_notas(usuario=Depends(usuario_actual)):
    with conectar() as db:
        filas = db.execute("""SELECT id, titulo, contenido, fecha_creacion
            FROM notas WHERE usuario_id = ? ORDER BY id DESC""", (usuario["id"],)).fetchall()
    return [dict(f) for f in filas]
