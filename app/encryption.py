from cryptography.hazmat.primitives.asymmetric import ec
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.hkdf import HKDF
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
import os
import base64

backend = default_backend()

def generate_ecdh_keys():
    private_key = ec.generate_private_key(ec.SECP384R1(), backend)
    public_key = private_key.public_key()
    return private_key, public_key

def ecdh_shared_key(private_key, peer_public_key):
    shared_secret = private_key.exchange(ec.ECDH(), peer_public_key)
    shared_key = HKDF(
        algorithm=hashes.SHA256(),
        length=32,
        salt=None,
        info=b'handshake data',
        backend=backend
    ).derive(shared_secret)
    return shared_key

def encrypt_message(shared_key, message):
    iv = os.urandom(12)
    encryptor = Cipher(
        algorithms.AES(shared_key),
        modes.GCM(iv),
        backend=backend
    ).encryptor()
    ciphertext = encryptor.update(message.encode('utf-8')) + encryptor.finalize()
    return base64.b64encode(iv + encryptor.tag + ciphertext).decode('utf-8')

def decrypt_message(shared_key, encrypted_message):
    encrypted_message = base64.b64decode(encrypted_message)
    iv = encrypted_message[:12]
    tag = encrypted_message[12:28]
    ciphertext = encrypted_message[28:]
    decryptor = Cipher(
        algorithms.AES(shared_key),
        modes.GCM(iv, tag),
        backend=backend
    ).decryptor()
    message = decryptor.update(ciphertext) + decryptor.finalize()
    return message.decode('utf-8')

def serialize_public_key(public_key):
    return public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    ).decode('utf-8')

def serialize_private_key(private_key):
    return private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    ).decode('utf-8')

def deserialize_public_key(pem):
    return serialization.load_pem_public_key(pem.encode('utf-8'), backend=backend)